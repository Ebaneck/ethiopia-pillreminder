package org.motechproject.icappr.support;

import java.util.Date;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.icappr.events.Events;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUtil {

    @Autowired
    private MotechSchedulerService schedulerService;

    public void scheduleAppointments(DateTime clinicVisitDate) {

        MotechEvent callJob = new MotechEvent(Events.APPOINTMENT_SCHEDULE_CALL);

        Date firstReminderDate = new Date();
        Date secondReminderDate = new Date();

        RunOnceSchedulableJob firstAppointmentReminder = new RunOnceSchedulableJob(callJob, firstReminderDate);

        RunOnceSchedulableJob secondAppointmentReminder = new RunOnceSchedulableJob(callJob, secondReminderDate);

        schedulerService.scheduleRunOnceJob(firstAppointmentReminder);
        schedulerService.scheduleRunOnceJob(secondAppointmentReminder);
    }

    public void scheduleAdherenceSurvey(DateTime enrollmentDate) {

        MotechEvent callJob = new MotechEvent(Events.ADHERENCE_ASSESSMENT_CALL);

        Date callDate = new Date();

        RunOnceSchedulableJob adherenceCallJob = new RunOnceSchedulableJob(callJob, callDate);

        schedulerService.scheduleRunOnceJob(adherenceCallJob);
    }

    public void scheduleSideEffectsSurvey(DateTime enrollmentDate, String patientId) {

        MotechEvent callJob = new MotechEvent(Events.SIDE_EFFECTS_SURVEY_CALL);

        callJob.getParameters().put(MotechSchedulerService.JOB_ID_KEY, patientId);
        
        Date callDate = new Date();

        RunOnceSchedulableJob sideEffectCallJob = new RunOnceSchedulableJob(callJob, callDate);

        schedulerService.scheduleRunOnceJob(sideEffectCallJob);
    }

    public void unscheduleAll(String patientId) {

        String subject = "placeHolder";
        String externalId = "externalId";
        //unschedule all subjects

        schedulerService.safeUnscheduleRunOnceJob(subject, externalId);
    }
}
