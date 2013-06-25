package org.motechproject.icappr;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Wrapper around {#link {@link SettingsFacade} to access demo related
 * properties
 */
@Component
public class PillReminderSettings {

    private static final String MOTECH_URL_PROPERTY = "motech.url";
    private static final String VERBOICE_CHANNEL_NAME_PROPERTY = "verboice.channel.name";
    private static final String CMSLITE_STREAM_PATH = "/module/cmsliteapi/stream/";
    private static final String SIDE_EFFECTS_DAYS_LATER_PROPERTY = "side.effect.days.later";
    private static final String SIDE_EFFECTS_HOUR_OF_DAY_PROPERTY = "side.effect.hour.of.day";
    private static final String SIDE_EFFECTS_MINUTE_OF_HOUR_PROPERTY = "side.effect.minute.of.hour";
    private static final String DEMO_MINUTES_PROPERTY = "demo.minutes";
    private static final String APPOINTMENT_HOUR_OF_DAY_PROPERTY = "appointment.hour.of.day";
    private static final String APPOINTMENT_MINUTE_OF_HOUR_PROPERTY = "appointment.minute.of.hour";
    private static final String ADHERENCE_DAYS_LATER_PROPERTY = "adherence.days.later";
    private static final String ADHERENCE_HOUR_OF_DAY_PROPERTY = "adherence.hour.of.day";
    private static final String ADHERENCE_MINUTE_OF_HOUR_PROPERTY = "adherence.minute.of.hour";
    private static final String EAT_TO_UTC_HOUR_DIFFERENCE_PROPERTY = "eat.to.utc.hour.difference";
    private static final String ADHERENCE_FLOW_ID_PROPERTY = "adherence.flow.id";
    private static final String PILL_REMINDER_FLOW_ID_PROPERTY = "pill.reminder.flow.id";
    private static final String SIDE_EFFECT_FLOW_ID_PROPERTY = "side.effect.flow.id";
    private static final String APPOINTMENT_REMINDER_FLOW_ID_PROPERTY = "appointment.reminder.flow.id";
    private static final String REGISTRATION_FORM_XMLNS_PROPERTY = "registration.form.xmlns";
    private static final String UPDATE_FORM_XMLNS_PROPERTY = "update.form.xmlns";
    private static final String STOP_FORM_XMLNS_PROPERTY = "stop.form.xmlns";
    private static final String DEMO_FORM_XMLNS_PROPERTY = "demo.form.xmlns";
    private static final String DAILY_REPORT_FILE_NAME_PROPERTY = "daily.report.filename";
    private static final String REPORTING_JAR_NAME_PROPERTY = "reporting.jar.name";
    private static final String REPORTING_JAR_DIRECTORY_PROPERTY = "reporting.jar.directory";
    private static final String CALL_RETRY_INTERVAL_IN_MINUTES_PROPERTY = "call.retry.interval.minutes";
    private static final String RETRY_COUNT_PROPERTY = "num.call.retries";
    private static final String CRON_JOB_PROPERTY = "report.cron.expression";
    private static final String WEEKLY_CRON_JOB_PROPERTY = "weekly.report.cron";
    private static final String DAILY_REPORT_NAME_PROPERTY = "daily.report.name";
    private static final String WEEKLY_REPORT_NAME_PROPERTY = "weekly.report.name";
    private static final String RETRY_TEST_ON_PROPERTY = "retry.test.on";

    //languages (english is default)
    private static final String AMHARIC = "amharic";
    private static final String SOMALI = "somali";
    private static final String HARARI = "harari";
    private static final String OROMIFFA = "oromiffa";

    @Autowired
    private SettingsFacade settingsFacade;

    public PillReminderSettings() { }

    @Autowired
    public PillReminderSettings(SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    public String getRegistrationFormXmlns() {
        return settingsFacade.getProperty(REGISTRATION_FORM_XMLNS_PROPERTY);
    }

    public String getUpdateFormXmlns() {
        return settingsFacade.getProperty(UPDATE_FORM_XMLNS_PROPERTY);
    }

    public String getStopFormXmlns() {
        return settingsFacade.getProperty(STOP_FORM_XMLNS_PROPERTY);
    }

    public String getDemoFormXmlns() {
        return settingsFacade.getProperty(DEMO_FORM_XMLNS_PROPERTY);
    }

    public String getAdherenceFlowId(String language) {
        return settingsFacade.getProperty(ADHERENCE_FLOW_ID_PROPERTY + "." + getLanguage(language));
    }

    public String getPillReminderFlowId(String language) {
        return settingsFacade.getProperty(PILL_REMINDER_FLOW_ID_PROPERTY + "." + getLanguage(language));
    }

    public String getSideEffectFlowId(String language) {
        return settingsFacade.getProperty(SIDE_EFFECT_FLOW_ID_PROPERTY + "." + getLanguage(language));
    }

    public String getAppointmentReminderFlowId(String language) {
        return settingsFacade.getProperty(APPOINTMENT_REMINDER_FLOW_ID_PROPERTY + "." + getLanguage(language));
    }

    public int getAppointmentMinuteOfHour() {
        return Integer.parseInt(settingsFacade.getProperty(APPOINTMENT_MINUTE_OF_HOUR_PROPERTY));

    }

    public int getAdherenceMinuteOfHour() {
        return Integer.parseInt(settingsFacade.getProperty(ADHERENCE_MINUTE_OF_HOUR_PROPERTY));

    }

    public int getSideEffectsMinuteOfHours() {
        return Integer.parseInt(settingsFacade.getProperty(SIDE_EFFECTS_MINUTE_OF_HOUR_PROPERTY));

    }

    public int getEATtoUTCHourDifference() {
        return Integer.parseInt(settingsFacade.getProperty(EAT_TO_UTC_HOUR_DIFFERENCE_PROPERTY));

    }

    public int getAppointmentHourOfDay() {
        return Integer.parseInt(settingsFacade.getProperty(APPOINTMENT_HOUR_OF_DAY_PROPERTY));
    }

    public int getAdherenceDaysLater() { 
        return Integer.parseInt(settingsFacade.getProperty(ADHERENCE_DAYS_LATER_PROPERTY));
    }

    public int getAdherenceHourOfDay() {
        return Integer.parseInt(settingsFacade.getProperty(ADHERENCE_HOUR_OF_DAY_PROPERTY));
    }

    public int getSideEffectDaysLater() {
        return Integer.parseInt(settingsFacade.getProperty(SIDE_EFFECTS_DAYS_LATER_PROPERTY));
    }

    public int getSideEffectHourOfDay() {
        return Integer.parseInt(settingsFacade.getProperty(SIDE_EFFECTS_HOUR_OF_DAY_PROPERTY));
    }

    public int getDemoMinutes() {
        return Integer.parseInt(settingsFacade.getProperty(DEMO_MINUTES_PROPERTY));
    }

    public String getMotechUrl() {
        return settingsFacade.getProperty(MOTECH_URL_PROPERTY);
    }

    public String getVerboiceChannelName() {
        return settingsFacade.getProperty(VERBOICE_CHANNEL_NAME_PROPERTY);
    }

    private String getLanguage(String language) {
        switch (language) {
            case AMHARIC:
            case SOMALI:
            case HARARI: 
            case OROMIFFA: return language;
            default: return "english";
        }
    }

    @Deprecated
    public String getCmsliteUrlFor(String soundFilename, String language) {
        return getMotechUrl() + CMSLITE_STREAM_PATH + language + "/" + soundFilename;
    }

    public String getDailyReportFileName() {
        return settingsFacade.getProperty(DAILY_REPORT_FILE_NAME_PROPERTY);
    }

    public String getReportingJarDirectory() {
        return settingsFacade.getProperty(REPORTING_JAR_DIRECTORY_PROPERTY);
    }

    public String getReportingJarName() {
        return settingsFacade.getProperty(REPORTING_JAR_NAME_PROPERTY);
    }

    public int getRetryIntervalMinutes() {
        return Integer.parseInt(settingsFacade.getProperty(CALL_RETRY_INTERVAL_IN_MINUTES_PROPERTY));
    }

    public String getRetryCount() {
        return settingsFacade.getProperty(RETRY_COUNT_PROPERTY);
    }

    public String getReportingCronExpresesion() {
        return settingsFacade.getProperty(CRON_JOB_PROPERTY);
    }

    public String getDailyReportingCronExpresesion() {
        return settingsFacade.getProperty(CRON_JOB_PROPERTY);
    }

    public String getWeeklyReportingCronExpresesion() {
        return settingsFacade.getProperty(WEEKLY_CRON_JOB_PROPERTY);
    }

    public String getDailyReportName() {
        return settingsFacade.getProperty(DAILY_REPORT_NAME_PROPERTY);
    }

    public String getWeeklyReportName() {
        return settingsFacade.getProperty(WEEKLY_REPORT_NAME_PROPERTY);
    }

    public String retryTestOn() {
        return settingsFacade.getProperty(RETRY_TEST_ON_PROPERTY);
    }
}
