package org.motechproject.icappr.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.motechproject.icappr.support.SchedulerUtil;
import org.motechproject.scheduler.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SchedulerController {

    @Autowired
    private MotechSchedulerService schedulerService;

    @Autowired
    private SchedulerUtil schedulerUtil;

    @RequestMapping("/schedules")
    @ResponseBody
    public String testSchedules(HttpServletRequest request) {

        String subject = request.getParameter("subject");
        String jobId = request.getParameter("jobId");
        List<Date> dates = schedulerService.getScheduledJobTimings(subject, jobId, DateTime.now().minusMonths(1).toDate(), DateTime.now().plusMonths(1).toDate());

        StringBuilder stringBuilder = new StringBuilder("Schedules for: " + subject + " with job ID: " + jobId + " \n");
        if (dates != null) {
            for (Date date : dates) {
                stringBuilder.append(date.toString() + "\n");
            }
        }

        return stringBuilder.toString();
    }

    @RequestMapping("/unschedule")
    @ResponseBody
    public String unschedule(HttpServletRequest request) {

        String motechId = request.getParameter("motechId");

        schedulerUtil.scheduleEndEvent(motechId, DateTime.now().plusMinutes(1), "any_reason");

        return "Unscheduled";
    }
}
