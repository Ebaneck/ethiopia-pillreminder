package org.motechproject.icappr.web;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.listener.ReportingJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for manually building generating and downloading reports
 */
@Controller
public class ReportingController {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    private ReportingJobListener reportingJobListener;

    @Autowired
    private PillReminderSettings pillReminderSettings;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<byte[]> generateDailyReport(HttpServletRequest request) throws IOException, InterruptedException {

        String startDate = request.getParameter("startdate");
        String endDate = request.getParameter("enddate");

        //        reportingJobListener.handleReportingJob(null);

        File file = new File(pillReminderSettings.getReportingJarDirectory() + "\\" + request.getParameter("file"));

        byte[] bytes = FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xlsx"));
        header.set("Content-Disposition", "attachment; filename=" + file.getName().replace(" ", "_"));
        header.setContentLength(bytes.length);

        return new HttpEntity<byte[]>(bytes, header);
    }

    @RequestMapping(value = "/buildreport", method = RequestMethod.GET)
    @ResponseBody
    public String buildReport(HttpServletRequest request) throws IOException, InterruptedException {

        reportingJobListener.handleDailyReportingJob(null);
        reportingJobListener.handleWeeklyReportingJob(null);

        return "success";
    }
}
