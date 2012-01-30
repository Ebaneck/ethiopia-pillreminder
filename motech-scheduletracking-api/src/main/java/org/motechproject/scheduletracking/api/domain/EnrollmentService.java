package org.motechproject.scheduletracking.api.domain;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.RepeatingSchedulableJob;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduletracking.api.events.MilestoneEvent;
import org.motechproject.scheduletracking.api.repository.AllTrackedSchedules;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static sun.swing.MenuItemLayoutHelper.max;

@Component
public class EnrollmentService {

    public static final String MILESTONE_ALERT = "milestone_alert";
    private int MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;

    private AllTrackedSchedules allTrackedSchedules;
    private MotechSchedulerService schedulerService;

    @Autowired
    public EnrollmentService(AllTrackedSchedules allTrackedSchedules, MotechSchedulerService schedulerService) {
        this.allTrackedSchedules = allTrackedSchedules;
        this.schedulerService = schedulerService;
    }

    public void scheduleAlertsForCurrentMilestone(Enrollment enrollment) {
        int alertCount = 0;
        Schedule schedule = allTrackedSchedules.getByName(enrollment.getScheduleName());
        Milestone currentMilestone = schedule.getMilestone(enrollment.getCurrentMilestoneName());
        for (MilestoneWindow window : currentMilestone.getMilestoneWindows()) {
            if (!window.hasElapsed(getCurrentMilestoneStartDate(enrollment))) {
                for (Alert alert : window.getAlerts())
                    scheduleAlertJob(alert, enrollment, schedule, currentMilestone, window, alertCount++);
            }
        }
    }

    private void scheduleAlertJob(Alert alert, Enrollment enrollment, Schedule schedule, Milestone milestone, MilestoneWindow milestoneWindow, int alertCount) {
        MotechEvent event = new MilestoneEvent(enrollment.getId(), schedule.getName(), milestone.getName(), milestoneWindow.getName().toString()).toMotechEvent();
        event.getParameters().put(MotechSchedulerService.JOB_ID_KEY, String.format("%s_%s_%d", MILESTONE_ALERT, enrollment.getId(), alertCount));
        DateTime startTime = DateUtil.newDateTime(getJobStartDate(enrollment, milestoneWindow), enrollment.getPreferredAlertTime());
        RepeatingSchedulableJob job = new RepeatingSchedulableJob(event, startTime.toDate(), null, new Integer(numberOfAlertsToRaise(alert, enrollment, milestoneWindow)), alert.getInterval().inDays() * MILLIS_IN_A_DAY);
        schedulerService.scheduleRepeatingJob(job);
    }

    private int numberOfAlertsToRaise(Alert alert, Enrollment enrollment, MilestoneWindow milestoneWindow) {
        LocalDate startDateOfWindow = getStartDateOfWindow(enrollment, milestoneWindow);
        LocalDate endDateOfWindow = startDateOfWindow.plusDays(milestoneWindow.getWindowEndInDays());
        LocalDate today = today();
        int daysToEndOfWindow = Days.daysBetween(today, endDateOfWindow).getDays();
        int maximumAlerts = alert.getRepeatCount();
        return maximumAlerts <= daysToEndOfWindow? maximumAlerts : daysToEndOfWindow;
    }

    private LocalDate getJobStartDate(Enrollment enrollment, MilestoneWindow milestoneWindow) {
        LocalDate startDateOfWindow = getStartDateOfWindow(enrollment, milestoneWindow);
        LocalDate today = today();
        return (startDateOfWindow.isAfter(today))? startDateOfWindow : today;
    }

    private LocalDate getStartDateOfWindow(Enrollment enrollment, MilestoneWindow milestoneWindow) {
        return getCurrentMilestoneStartDate(enrollment).plusDays(milestoneWindow.getStart().inDays());
    }

    LocalDate getCurrentMilestoneStartDate(Enrollment enrollment) {
        Schedule schedule = allTrackedSchedules.getByName(enrollment.getScheduleName());
        if (enrollment.getCurrentMilestoneName().equals(schedule.getFirstMilestone().getName()))
            return enrollment.getReferenceDate();
        return (enrollment.getFulfillments().isEmpty())? enrollment.getEnrollmentDate() : enrollment.getLastFulfilledDate();
    }
}
