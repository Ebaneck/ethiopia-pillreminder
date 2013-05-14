package org.motechproject.icappr;

import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Wrapper around {#link {@link SettingsFacade} to access demo related
 * properties
 */
@Component
public class PillReminderSettings {
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private static final String MAX_RETRY_COUNT_PROPERTY = "max.reminder.retry";
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
    private static final String APPIONTMENT_REMINDER_FLOW_ID_PROPERTY = "appointment.reminder.flow.id";

    @Autowired
    private SettingsFacade settingsFacade;

    public PillReminderSettings() { }

    @Autowired
    public PillReminderSettings(SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    public String getAdherenceFlowId() {
        return settingsFacade.getProperty(ADHERENCE_FLOW_ID_PROPERTY);
    }

    public String getPillReminderFlowId() {
        return settingsFacade.getProperty(PILL_REMINDER_FLOW_ID_PROPERTY);
    }

    public String getSideEffectFlowId() {
        return settingsFacade.getProperty(SIDE_EFFECT_FLOW_ID_PROPERTY);
    }

    public String getAppointmentReminderFlowId() {
        return settingsFacade.getProperty(APPIONTMENT_REMINDER_FLOW_ID_PROPERTY);
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

    public int getMaxRetryCount() {
        int maxRetryCount = 1;
        try {
            maxRetryCount = Integer.parseInt(settingsFacade.getProperty(MAX_RETRY_COUNT_PROPERTY));
        } catch (Exception e) {
            logger.warn(MAX_RETRY_COUNT_PROPERTY + " property does not have a valid value");
        }

        return maxRetryCount;
    }

    public String getVerboiceChannelName() {
        return settingsFacade.getProperty(VERBOICE_CHANNEL_NAME_PROPERTY);
    }

    @Deprecated
    public String getCmsliteUrlFor(String soundFilename, String language) {
        return getMotechUrl() + CMSLITE_STREAM_PATH + language + "/" + soundFilename;
    }
}
