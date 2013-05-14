package org.motechproject.icappr.support;

import java.util.Date;
import java.util.Map;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUtil {

    @Autowired
    private MotechSchedulerService schedulerService;

    @Autowired
    private PillReminderSettings settings;

    public void scheduleAppointments(DateTime clinicVisitDate, String externalId, boolean isDemo, String phoneNumber) {

        MotechEvent callJob = new MotechEvent(Events.APPOINTMENT_SCHEDULE_CALL);
        MotechEvent callJob2 = new MotechEvent(Events.SECOND_APPOINTMENT_SCHEDULE_CALL);

        injectParameterData(externalId, phoneNumber, callJob.getParameters());
        injectParameterData(externalId, phoneNumber, callJob2.getParameters());

        Date firstReminderDate;
        Date secondReminderDate;

        if (isDemo) {
            firstReminderDate = DateTime.now().plusMinutes(settings.getDemoMinutes()).toDate();
            secondReminderDate = DateTime.now().plusMinutes(settings.getDemoMinutes() * 3).toDate();
        } else {
            firstReminderDate = clinicVisitDate.minusDays(2).withHourOfDay(settings.getAppointmentHourOfDay()).withMinuteOfHour(settings.getAppointmentMinuteOfHour()).toDate();
            secondReminderDate = clinicVisitDate.minusDays(1).withHourOfDay(settings.getAppointmentHourOfDay()).withMinuteOfHour(settings.getAppointmentMinuteOfHour()).toDate();
        }

        RunOnceSchedulableJob firstAppointmentReminder = new RunOnceSchedulableJob(callJob, firstReminderDate);

        RunOnceSchedulableJob secondAppointmentReminder = new RunOnceSchedulableJob(callJob2, secondReminderDate);

        schedulerService.safeScheduleRunOnceJob(firstAppointmentReminder);
        schedulerService.safeScheduleRunOnceJob(secondAppointmentReminder);
    }

    public void scheduleAdherenceSurvey(DateTime enrollmentDate, String externalId, boolean isDemo, String phoneNumber) {

        MotechEvent callJob = new MotechEvent(Events.ADHERENCE_ASSESSMENT_CALL);

        Date callDate;

        if (isDemo) {
            callDate = DateTime.now().plusMinutes(settings.getDemoMinutes()).toDate();
        } else {
            callDate = enrollmentDate.plusDays(settings.getAdherenceDaysLater()).withHourOfDay(settings.getAdherenceHourOfDay()).withMinuteOfHour(settings.getAdherenceMinuteOfHour()).toDate();
        }

        injectParameterData(externalId, phoneNumber, callJob.getParameters());

        RunOnceSchedulableJob adherenceCallJob = new RunOnceSchedulableJob(callJob, callDate);

        schedulerService.safeScheduleRunOnceJob(adherenceCallJob);
    }

    public void scheduleSideEffectsSurvey(DateTime enrollmentDate, String externalId, boolean isDemo, String phoneNumber) {

        MotechEvent callJob = new MotechEvent(Events.SIDE_EFFECTS_SURVEY_CALL);

        injectParameterData(externalId, phoneNumber, callJob.getParameters());

        Date callDate;

        if (isDemo) {
            callDate = DateTime.now().plusMinutes(settings.getDemoMinutes()).toDate();
        } else {
            callDate = enrollmentDate.plusDays(settings.getSideEffectDaysLater()).withHourOfDay(settings.getSideEffectHourOfDay()).withMinuteOfHour(settings.getSideEffectsMinuteOfHours()).toDate();
        }

        RunOnceSchedulableJob sideEffectCallJob = new RunOnceSchedulableJob(callJob, callDate);

        schedulerService.safeScheduleRunOnceJob(sideEffectCallJob);
    }

    public void unscheduleAllIcapprJobs(String externalId) {
        schedulerService.safeUnscheduleRunOnceJob(Events.SIDE_EFFECTS_SURVEY_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.ADHERENCE_ASSESSMENT_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.APPOINTMENT_SCHEDULE_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.SECOND_APPOINTMENT_SCHEDULE_CALL, externalId);
    }

    private void injectParameterData(String externalId, String phoneNumber, Map<String, Object> parameters) {
        parameters.put(MotechSchedulerService.JOB_ID_KEY, externalId);
        parameters.put(MotechConstants.PHONE_NUM, phoneNumber);
        parameters.put(MotechConstants.MOTECH_ID, externalId);
    }

    public void scheduleEndEvent(String motechId, DateTime stopDate, String stopReason) {
        MotechEvent endEvent = new MotechEvent(Events.END_CALLS);
        endEvent.getParameters().put(MotechConstants.MOTECH_ID, motechId);
        endEvent.getParameters().put(MotechConstants.STOP_REASON, stopReason);

        RunOnceSchedulableJob endJob = new RunOnceSchedulableJob(endEvent, stopDate.toDate());

        schedulerService.safeScheduleRunOnceJob(endJob);
    }
}
