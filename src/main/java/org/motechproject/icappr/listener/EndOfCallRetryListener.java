package org.motechproject.icappr.listener;

import java.util.List;

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
import org.motechproject.mrs.domain.MRSPatient;
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

        logger.trace("Handling end of call");

        if (!"true".equals(pillReminderSettings.getRetryEnabled())) {
            logger.trace("Retry is disabled.");
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
            // Demo "patients" are persisted as MRS Person objects - no retry
            // calls are made here
            logger.trace("Demo mode, no retry call for busy and no answer");
            return;
        }

        String phoneNum = session.getPhoneNumber();
        String requestType = session.get(MotechConstants.REQUEST_TYPE);
        String language = session.get(MotechConstants.LANGUAGE);
        String retriesLeft = session.get(MotechConstants.RETRIES_LEFT);

        int retries = 0;
        if (retriesLeft != null) {
            retries = Integer.parseInt(retriesLeft);
        }

        if (retries == 0) {
            logger.trace("No retries left for call with Id: " + callId);
            return;
        }

        DateTime retryCallTime = DateTime.now();
        DateTime preferredCallTime = getPreferredCallTimeForDay(session, record, retryCallTime);

        if (callTimeInPreferredRetryWindow(retryCallTime, preferredCallTime)) {
            // retry the call after a delay
            int delayMinutes;
            if (retries == 1) {
                delayMinutes = settings.getRetryDelayLongMinutes();
            } else if (retries == 2) {
                delayMinutes = settings.getRetryDelayMediumMinutes();
            } else {
                delayMinutes = settings.getRetryDelayShortMinutes();
            }
            retryCallTime = retryCallTime.plusMinutes(delayMinutes);

        } else {
            // retry tomorrow or just abandon the retry
            switch (requestType) {
            case RequestTypes.APPOINTMENT_CALL:
            case RequestTypes.SECOND_APPOINTMENT_CALL:
            case RequestTypes.PILL_REMINDER_CALL:
                retryCallTime = null;
                break;
            case RequestTypes.ADHERENCE_CALL:
            case RequestTypes.SIDE_EFFECT_CALL:
                retryCallTime = retryCallTime.plusDays(1);
            }
        }
        
        if (null == retryCallTime) {
            logger.trace("Abandoning retry outside preferred window for call: " + callId);
            return;
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

    private boolean callTimeInPreferredRetryWindow(DateTime callTime, DateTime preferredTime) {
        int retryWindowMinutes = settings.getRetryWindowMinutes();
        DateTime latestRetryTime = callTime.plusMinutes(retryWindowMinutes);
        return callTime.isAfter(latestRetryTime.getMillis());
    }

    private DateTime getPreferredCallTimeForDay(FlowSession session, CallDetailRecord record, DateTime day) {

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
            // want to look up patient preferred call time of day
            // but this is buried in the scheduler and not saved with patient or
            // message campaign
            // so use actual call start time as surrogate, should be fine unless
            // severe scheduling or events error
            DateTime callStartTime = record.getStartDate();
            return baseDate.withHourOfDay(callStartTime.getHourOfDay()).withMinuteOfHour(
                    callStartTime.getMinuteOfHour());

        default:
            return null;
        }
    }
}
