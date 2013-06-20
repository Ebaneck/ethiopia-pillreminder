package org.motechproject.icappr.form.model;

public class PillReminderRegistration extends PillReminderForm {

    private String phoneNumber;
    private String pin;
    private String mrn;
    private String preferredLanguage;
    private String nextAppointment;
    private String iptInitiationDate;
    private String preferredCallTime;
    private String studySite;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPhoneNumber(String string) {
        this.phoneNumber = string;
    }

    public void setPin(String string) {
        this.pin = string;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(String nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public String getIptInitiationDate() {
        return iptInitiationDate;
    }

    public void setIptInitiationDate(String iptInitiationDate) {
        this.iptInitiationDate = iptInitiationDate;
    }

    public String getPreferredCallTime() {
        return preferredCallTime;
    }

    public void setPreferredCallTime(String preferredCallTime) {
        this.preferredCallTime = preferredCallTime;
    }

    public String getStudySite() {
        return studySite;
    }

    public void setStudySite(String studySite) {
        this.studySite = studySite;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }
}
