package org.motechproject.icappr.listener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.events.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportingJobListener {

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Autowired
    private PillReminderSettings pillReminderSettings;

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @MotechListener( subjects = Events.DAILY_REPORT_EVENT)
    public void handleDailyReportingJob(MotechEvent event) throws IOException, InterruptedException {

        DateTime today = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        logger.debug("Generating weekly report for the period of: " + today.toString() + " to: " + today.minusDays(1).toString());

        generateReport(pillReminderSettings.getDailyReportName(), today, today.minusDays(1), false);
    }

    @MotechListener( subjects = Events.WEEKLY_REPORT_EVENT)
    public void handleWeeklyReportingJob(MotechEvent event) throws IOException, InterruptedException {

        DateTime today = DateTime.now().plusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        logger.debug("Generating weekly report for the period of: " + today.toString() + " to: " + today.minusWeeks(1).toString());

        generateReport(pillReminderSettings.getWeeklyReportName(), today, today.minusWeeks(1), true);
    }

    public synchronized void generateReport(String reportName, DateTime startDate, DateTime endDate, boolean weekly) throws IOException, InterruptedException {
        int year = endDate.getYear();
        int month = endDate.getMonthOfYear();
        int day = endDate.getDayOfMonth();
        StringBuilder reportFileName = new StringBuilder(reportName + "." + month + "." + day + "." + year);

        if (weekly) {
            reportFileName.append("-weekly");
        }

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", pillReminderSettings.getReportingJarName(), reportName, startDate.toString(), endDate.toString(), reportFileName.toString());
        pb.directory(new File(pillReminderSettings.getReportingJarDirectory()));
        Process p = pb.start();

        processChildProcessStreams(p);

        p.waitFor();
    }

    private void processChildProcessStreams(Process p) {
        InputStreamConsumer inputStream = new InputStreamConsumer(p.getInputStream());

        executorService.submit(inputStream);
    }

    private class InputStreamConsumer implements Callable<String>
    {
        private InputStream is;

        InputStreamConsumer(InputStream is) {
            this.is=is;
        }

        @Override
        public String call() throws Exception {
            try {
                while (is.read() >= 0) { ; }
            } catch (IOException e) {
                logger.debug("Exception: " + e.getMessage());
            }

            return "Success";
        }

    }
}
