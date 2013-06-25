package org.motechproject.icappr.support;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.scheduler.domain.RepeatingSchedulableJob;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUtil {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

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

        logger.info("Scheduling appointment calls for MotechID: " + externalId + " and phone: " + phoneNumber + " with the first coming at: " + firstReminderDate.toString() + " and the second coming at: " + secondReminderDate.toString());

        scheduleJob(firstAppointmentReminder);
        scheduleJob(secondAppointmentReminder);
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

        logger.info("Scheduling adherence survey for MotechID: " + externalId + " and phone: " + phoneNumber + " at time: " + callDate.toString());

        scheduleJob(adherenceCallJob);
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

        logger.info("Scheduling side effects survey for MotechID: " + externalId + " and phone: " + phoneNumber + " at time: " + callDate.toString());

        scheduleJob(sideEffectCallJob);
    }

    public void unscheduleAllIcapprJobs(String externalId) {
        logger.info("Unscheduling all jobs for: " + externalId);

        schedulerService.safeUnscheduleRunOnceJob(Events.SIDE_EFFECTS_SURVEY_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.ADHERENCE_ASSESSMENT_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.APPOINTMENT_SCHEDULE_CALL, externalId);
        schedulerService.safeUnscheduleRunOnceJob(Events.SECOND_APPOINTMENT_SCHEDULE_CALL, externalId);
    }

    public static void injectParameterData(String externalId, String phoneNumber, Map<String, Object> parameters) {
        parameters.put(MotechSchedulerService.JOB_ID_KEY, externalId);
        parameters.put(MotechConstants.PHONE_NUM, phoneNumber);
        parameters.put(MotechConstants.MOTECH_ID, externalId);
    }

    public void scheduleEndEvent(String motechId, DateTime stopDate, String stopReason) {
        MotechEvent endEvent = new MotechEvent(Events.END_CALLS);
        endEvent.getParameters().put(MotechConstants.MOTECH_ID, motechId);
        endEvent.getParameters().put(MotechConstants.STOP_REASON, stopReason);

        RunOnceSchedulableJob endJob = new RunOnceSchedulableJob(endEvent, stopDate.toDate());

        logger.info("Scheduling an end of program event for : " + motechId + " at: " + stopDate.toString());

        scheduleJob(endJob);
    }

    public void testSchedule(int minutes) {
        MotechEvent testEvent = new MotechEvent("TEST_EVENT");
        testEvent.getParameters().put("TIME_SCHEDULED", DateTime.now().toString());
        testEvent.getParameters().put("MINUTES_LATER", minutes);
        testEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, UUID.randomUUID().toString());

        RunOnceSchedulableJob testJob = new RunOnceSchedulableJob(testEvent, DateTime.now().plusMinutes(minutes).toDate());

        scheduleJob(testJob);
    }

    private void scheduleJob (RunOnceSchedulableJob job) {
        try {
            schedulerService.safeScheduleRunOnceJob(job);
        } catch (IllegalArgumentException e) {
            logger.error("Did not schedule job that was in the past");
        }
    }

    private void scheduleCronJob (CronSchedulableJob job) {
        try {
            schedulerService.safeScheduleJob(job);
        } catch (IllegalArgumentException e) {
            logger.error("Did not schedule job that was in the past");
        }
    }

    @PostConstruct
    public void scheduleReportingJobs() {
        MotechEvent dailyEvent = new MotechEvent(Events.DAILY_REPORT_EVENT);

        CronSchedulableJob dailyJob = new CronSchedulableJob(dailyEvent, settings.getDailyReportingCronExpresesion());

        scheduleCronJob(dailyJob);

        MotechEvent weeklyEvent = new MotechEvent(Events.WEEKLY_REPORT_EVENT);

        CronSchedulableJob weeklyJob = new CronSchedulableJob(weeklyEvent, settings.getWeeklyReportingCronExpresesion());

        scheduleCronJob(weeklyJob);       
    }
}
