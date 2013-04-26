package org.motechproject.icappr.domain;

public class PillReminderUpdate extends PillReminderRegistration {

    private String todaysDate;
    private String preferredReminderFrequency;
    private String preferredReminderDay;
    
    public String getTodaysDate() {
        return todaysDate;
    }
    public void setTodaysDate(String todaysDate) {
        this.todaysDate = todaysDate;
    }
    public String getPreferredReminderFrequency() {
        return preferredReminderFrequency;
    }
    public void setPreferredReminderFrequency(String preferredReminderFrequency) {
        this.preferredReminderFrequency = preferredReminderFrequency;
    }
    public String getPreferredReminderDay() {
        return preferredReminderDay;
    }
    public void setPreferredReminderDay(String preferredReminderDay) {
        this.preferredReminderDay = preferredReminderDay;
    }
}
