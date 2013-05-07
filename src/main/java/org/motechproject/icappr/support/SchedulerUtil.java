package org.motechproject.icappr.support;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUtil {

    private static final int SIDE_EFFECTS_DAYS_LATER = 17;
    private static final int SIDE_EFFECTS_HOUR_OF_DAY = 17;
    private static final int DEMO_MINUTES = 2;
    private static final int HOUR_OF_DAY = 16;
    private static final int ADHERENCE_DAYS_LATER = 11;
    private static final int ADHERENCE_HOUR_OF_DAY = 17;

    @Autowired
    private MotechSchedulerService schedulerService;

    public void scheduleAppointments(DateTime clinicVisitDate, String externalId, boolean isDemo, String phoneNumber, String language) {

        MotechEvent callJob = new MotechEvent(Events.APPOINTMENT_SCHEDULE_CALL);
        MotechEvent callJob2 = new MotechEvent(Events.SECOND_APPOINTMENT_SCHEDULE_CALL);

        injectParameterData(externalId, phoneNumber, language, callJob.getParameters());
        injectParameterData(externalId, phoneNumber, language, callJob2.getParameters());

        Date firstReminderDate;
        Date secondReminderDate;

        if (isDemo) {
            firstReminderDate = DateTime.now().plusMinutes(DEMO_MINUTES).toDate();
            secondReminderDate = DateTime.now().plusMinutes(DEMO_MINUTES * 3).toDate();
        } else {
            firstReminderDate = clinicVisitDate.minusDays(2).withHourOfDay(HOUR_OF_DAY).toDate();
            secondReminderDate = clinicVisitDate.minusDays(1).withHourOfDay(HOUR_OF_DAY).toDate();
        }

        RunOnceSchedulableJob firstAppointmentReminder = new RunOnceSchedulableJob(callJob, firstReminderDate);

        RunOnceSchedulableJob secondAppointmentReminder = new RunOnceSchedulableJob(callJob2, secondReminderDate);

        schedulerService.safeScheduleRunOnceJob(firstAppointmentReminder);
        schedulerService.safeScheduleRunOnceJob(secondAppointmentReminder);
    }

    public void scheduleAdherenceSurvey(DateTime enrollmentDate, String externalId, boolean isDemo, String phoneNumber, String language) {

        MotechEvent callJob = new MotechEvent(Events.ADHERENCE_ASSESSMENT_CALL);

        Date callDate;

        if (isDemo) {
            callDate = DateTime.now().plusMinutes(DEMO_MINUTES).toDate();
        } else {
            callDate = enrollmentDate.plusDays(ADHERENCE_DAYS_LATER).withHourOfDay(ADHERENCE_HOUR_OF_DAY).toDate();
        }

        injectParameterData(externalId, phoneNumber, language, callJob.getParameters());

        RunOnceSchedulableJob adherenceCallJob = new RunOnceSchedulableJob(callJob, callDate);

        schedulerService.safeScheduleRunOnceJob(adherenceCallJob);
    }

    public void scheduleSideEffectsSurvey(DateTime enrollmentDate, String externalId, boolean isDemo, String phoneNumber, String language) {

        MotechEvent callJob = new MotechEvent(Events.SIDE_EFFECTS_SURVEY_CALL);

        injectParameterData(externalId, phoneNumber, language, callJob.getParameters());

        Date callDate;

        if (isDemo) {
            callDate = DateTime.now().plusMinutes(DEMO_MINUTES).toDate();
        } else {
            callDate = enrollmentDate.plusDays(SIDE_EFFECTS_DAYS_LATER).withHourOfDay(SIDE_EFFECTS_HOUR_OF_DAY).toDate();
        }

        RunOnceSchedulableJob sideEffectCallJob = new RunOnceSchedulableJob(callJob, callDate);

        schedulerService.safeScheduleRunOnceJob(sideEffectCallJob);
    }

    public void unscheduleAllIcapprJobs(String externalId) {
        schedulerService.safeUnscheduleRunOnceJob(Events.SIDE_EFFECTS_SURVEY_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.ADHERENCE_ASSESSMENT_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.APPOINTMENT_SCHEDULE_CALL, externalId);
    }

    private void injectParameterData(String externalId, String phoneNumber, String language, Map<String, Object> parameters) {
        parameters.put(MotechSchedulerService.JOB_ID_KEY, externalId);
        parameters.put(MotechConstants.PHONE_NUM, phoneNumber);
        parameters.put(MotechConstants.MOTECH_ID, externalId);
        parameters.put(MotechConstants.LANGUAGE, language);
    }

}
