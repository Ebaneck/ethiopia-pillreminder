package org.motechproject.icappr.form.model;

public class PillReminderUpdate extends PillReminderForm {

    private String phoneNumber;
    private String preferredReminderFrequency;
    private String preferredReminderDay;
    private String preferredCallTime;
    private String nextAppointment;
    private String todaysDate;
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPreferredCallTime() {
        return preferredCallTime;
    }
    public void setPreferredCallTime(String preferredCallTime) {
        this.preferredCallTime = preferredCallTime;
    }
    public String getNextAppointment() {
        return nextAppointment;
    }
    public void setNextAppointment(String nextAppointment) {
        this.nextAppointment = nextAppointment;
    }   
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
