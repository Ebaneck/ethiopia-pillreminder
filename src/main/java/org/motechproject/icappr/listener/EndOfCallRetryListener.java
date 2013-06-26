package org.motechproject.icappr.listener;

import org.joda.time.DateTime;
import org.motechproject.callflow.domain.CallDetailRecord;
import org.motechproject.callflow.domain.CallDetailRecord.Disposition;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.EventKeys;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.support.SchedulerUtil;
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
        logger.debug("Handling end of call event...");

        CallDetailRecord record = (CallDetailRecord) event.getParameters().get("call_detail_record");

        if (record == null) {
            return;
        }

        String callId = record.getCallId();
        Disposition disposition = record.getDisposition();

        if (Disposition.BUSY.equals(disposition) || Disposition.NO_ANSWER.equals(disposition)) {
            retryCall(callId);
        } else if (Disposition.FAILED.equals(disposition)) {
            if (encounterAdapter.getEncounterById(callId) == null) {
                retryCall(callId);
            }
        }
    }

    private void retryCall(String callId) {
        FlowSession session = flowSessionService.getSession(callId);

        if (session == null) {
            logger.debug("No session for call Id: " + callId + " was found");
            return;
        }

        String motechId = session.get(MotechConstants.MOTECH_ID);

        if (patientAdapter.getPatientByMotechId(motechId) == null && pillReminderSettings.retryTestOn().equals("false")) {
            //Demo "patients" are persisted as MRS Person objects - no retry calls are made here
            logger.debug("Demo mode, no retry call for busy and no answer");
            return;
        }

        String phoneNum = session.getPhoneNumber();
        String requestType = session.get(MotechConstants.REQUEST_TYPE);
        String language = session.get(MotechConstants.LANGUAGE);
        String retriesLeft = session.get(MotechConstants.RETRIES_LEFT);
        String subject = null;

        int retries = 0;

        if (retriesLeft != null) {
            retries = Integer.parseInt(retriesLeft);
        }

        //Probably should make this declarative somewhere
        int scheduleDelay = 10;

        if (retries == 0) {
            logger.debug("No retries left for call");
            return;
        } else if (retries == 2) {
            scheduleDelay = 20;
        } else if (retries == 1) {
            scheduleDelay = 30;
        }

        logger.debug("Rescheduling retry call for session: " + callId);
        retries--;

        switch (requestType) {
            case RequestTypes.ADHERENCE_CALL : subject = Events.ADHERENCE_ASSESSMENT_CALL; break;
            case RequestTypes.APPOINTMENT_CALL : subject = Events.APPOINTMENT_SCHEDULE_CALL; break;
            case RequestTypes.SECOND_APPOINTMENT_CALL: subject = Events.SECOND_APPOINTMENT_SCHEDULE_CALL; break;
            case RequestTypes.PILL_REMINDER_CALL : subject = Events.PILL_REMINDER_CALL; break;
            case RequestTypes.SIDE_EFFECT_CALL : subject = Events.SIDE_EFFECTS_SURVEY_CALL; break;
        }

        MotechEvent event = new MotechEvent(subject);

        event.getParameters().put(MotechSchedulerService.JOB_ID_KEY, callId + "-" + requestType);
        event.getParameters().put(MotechConstants.PHONE_NUM, phoneNum);
        event.getParameters().put(MotechConstants.LANGUAGE, language);
        event.getParameters().put(MotechConstants.REQUEST_TYPE, requestType);
        event.getParameters().put(MotechConstants.RETRIES_LEFT, Integer.toString(retries));

        SchedulerUtil.injectParameterData(motechId, phoneNum, event.getParameters());

        RunOnceSchedulableJob job = new RunOnceSchedulableJob(event, DateTime.now().plusMinutes(scheduleDelay).toDate());

        schedulerService.safeScheduleRunOnceJob(job);
    }
}
