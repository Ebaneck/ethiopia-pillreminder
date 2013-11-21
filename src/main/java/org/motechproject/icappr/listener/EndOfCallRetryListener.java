package org.motechproject.icappr.listener;

import org.joda.time.DateTime;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.support.SchedulerUtil;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallDisposition;
import org.motechproject.ivr.domain.EventKeys;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndOfCallRetryListener {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    private PillReminderSettings settings;

    @Autowired
    private FlowSessionService flowSessionService;

    @Autowired
    private MotechSchedulerService schedulerService;

    @Autowired
    private MRSPatientAdapter patientAdapter;

    @Autowired
    private PillReminderSettings pillReminderSettings;

    @Autowired
    private MRSEncounterAdapter encounterAdapter;

    @MotechListener(subjects = EventKeys.END_OF_CALL_EVENT)
    public void handleEndOfCall(MotechEvent event) {

        logger.info("Handling end of call");

        if (!"true".equals(pillReminderSettings.getRetryEnabled())) {
            logger.info("Retry is disabled.");
            return;
        }

        CallDetailRecord record = (CallDetailRecord) event.getParameters().get("call_detail_record");
        if (record == null) {
            logger.debug("No call detail record found");
            return;
        }

        String callId = record.getCallId();
        CallDisposition disposition = record.getDisposition();
        logger.debug("End of call ID: " + callId + " and disposition: " + disposition.toString());

        if (CallDisposition.BUSY.equals(disposition) || CallDisposition.NO_ANSWER.equals(disposition)) {
            retryCall(record);
        } else if (CallDisposition.FAILED.equals(disposition)) {
            if (encounterAdapter.getEncounterById(callId) == null) {
                retryCall(record);
            }
        }
    }

    private void retryCall(CallDetailRecord record) {
        String callId = record.getCallId();
        FlowSession session = flowSessionService.getSession(callId);
        if (session == null) {
            logger.debug("No session for call Id: " + callId + " was found");
            return;
        }

        String motechId = session.get(MotechConstants.MOTECH_ID);
        if (patientAdapter.getPatientByMotechId(motechId) == null && pillReminderSettings.retryTestOn().equals("false")) {
            logger.info("Demo mode, no retry call for busy and no answer");
            return;
        }

        String retriesLeft = session.get(MotechConstants.RETRIES_LEFT);
        int retries = 0;
        if (retriesLeft != null) {
            retries = Integer.parseInt(retriesLeft);
        }
        if (retries == 0) {
            logger.info("No retries left for call with Id: " + callId);
            return;
        }

        DateTime nowTime = DateTime.now();
        DateTime preferredTimeToday = preferredCallTimeForSameDay(session, record, nowTime);
        if (callTimeInRetryWindow(nowTime, preferredTimeToday)) {
            // retry the call soon
            int delayMinutes;
            if (retries == 1) {
                delayMinutes = settings.getRetryLongDelayMinutes();
            } else if (retries == 2) {
                delayMinutes = settings.getRetryMediumDelayMinutes();
            } else {
                delayMinutes = settings.getRetryShortDelayMinutes();
            }

            scheduleRetryCall(callId, nowTime.plusMinutes(delayMinutes));
            return;

        } else {
            // retry tomorrow, or never
            String requestType = session.get(MotechConstants.REQUEST_TYPE);
            switch (requestType) {
            case RequestTypes.ADHERENCE_CALL:
            case RequestTypes.SIDE_EFFECT_CALL:
                scheduleRetryCall(callId, preferredTimeToday.plusDays(1));
                return;
            case RequestTypes.APPOINTMENT_CALL:
            case RequestTypes.SECOND_APPOINTMENT_CALL:
            case RequestTypes.PILL_REMINDER_CALL:
                logger.info("Abandoning retry outside retry window for call: " + callId);
                return;
            }
        }
    }

    private void scheduleRetryCall(String callId, DateTime retryCallTime) {
        FlowSession session = flowSessionService.getSession(callId);

        String motechId = session.get(MotechConstants.MOTECH_ID);
        String phoneNum = session.getPhoneNumber();
        String requestType = session.get(MotechConstants.REQUEST_TYPE);
        String language = session.get(MotechConstants.LANGUAGE);
        String retriesLeft = session.get(MotechConstants.RETRIES_LEFT);

        int retries = 0;
        if (retriesLeft != null) {
            retries = Integer.parseInt(retriesLeft);
        }
        retries--;
        logger.debug("Rescheduling retry call for session: " + callId + " (retries left: " + retries + ")");

        String subject = null;
        switch (requestType) {
        case RequestTypes.ADHERENCE_CALL:
            subject = Events.ADHERENCE_ASSESSMENT_CALL;
            break;
        case RequestTypes.APPOINTMENT_CALL:
            subject = Events.APPOINTMENT_SCHEDULE_CALL;
            break;
        case RequestTypes.SECOND_APPOINTMENT_CALL:
            subject = Events.SECOND_APPOINTMENT_SCHEDULE_CALL;
            break;
        case RequestTypes.PILL_REMINDER_CALL:
            subject = Events.PILL_REMINDER_CALL;
            break;
        case RequestTypes.SIDE_EFFECT_CALL:
            subject = Events.SIDE_EFFECTS_SURVEY_CALL;
            break;
        }

        MotechEvent event = new MotechEvent(subject);
        event.getParameters().put(MotechSchedulerService.JOB_ID_KEY, callId + "-" + requestType);
        event.getParameters().put(MotechConstants.PHONE_NUM, phoneNum);
        event.getParameters().put(MotechConstants.LANGUAGE, language);
        event.getParameters().put(MotechConstants.REQUEST_TYPE, requestType);
        event.getParameters().put(MotechConstants.RETRIES_LEFT, Integer.toString(retries));

        SchedulerUtil.injectParameterData(motechId, phoneNum, event.getParameters());

        RunOnceSchedulableJob job = new RunOnceSchedulableJob(event, retryCallTime.toDate());
        schedulerService.safeScheduleRunOnceJob(job);
    }

    private boolean callTimeInRetryWindow(DateTime callTime, DateTime preferredTime) {
        int retryWindowMinutes = settings.getRetryWindowMinutes();
        DateTime latestRetryTime = preferredTime.plusMinutes(retryWindowMinutes);
        return callTime.isAfter(latestRetryTime.getMillis());
    }

    private DateTime preferredCallTimeForSameDay(FlowSession session, CallDetailRecord record, DateTime day) {

        String requestType = session.get(MotechConstants.REQUEST_TYPE);
        DateTime baseDate = day.withTime(0, 0, 0, 0);

        switch (requestType) {
        case RequestTypes.ADHERENCE_CALL:
            return baseDate.withHourOfDay(settings.getAdherenceHourOfDay()).withMinuteOfHour(
                    settings.getAdherenceMinuteOfHour());

        case RequestTypes.APPOINTMENT_CALL:
        case RequestTypes.SECOND_APPOINTMENT_CALL:
            return baseDate.withHourOfDay(settings.getAppointmentHourOfDay()).withMinuteOfHour(
                    settings.getAppointmentMinuteOfHour());

        case RequestTypes.SIDE_EFFECT_CALL:
            return baseDate.withHourOfDay(settings.getSideEffectHourOfDay()).withMinuteOfHour(
                    settings.getSideEffectsMinuteOfHours());

        case RequestTypes.PILL_REMINDER_CALL:
            // actual preferred call time is buried in the scheduler
            // use start time of failed call as surrogate
            DateTime callStartTime = record.getStartDate();
            return baseDate.withHourOfDay(callStartTime.getHourOfDay()).withMinuteOfHour(
                    callStartTime.getMinuteOfHour());

        default:
            return null;
        }
    }
}
