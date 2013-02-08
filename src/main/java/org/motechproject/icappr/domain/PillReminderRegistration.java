package org.motechproject.icappr.domain;

public class PillReminderRegistration {
    private String phoneNumber;
    private String pin;
    private String clinic;
    private String nextCampaign;
    private String patientId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public String getClinic() {
        return clinic;
    }

    public String nextCampaign() {
        return nextCampaign;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setClinic(String string) {
        this.clinic = string;
    }

    public void setPatientId(String string) {
        this.patientId = string;
    }

    public void setPhoneNumber(String string) {
        this.phoneNumber = string;
    }

    public void setPin(String string) {
        this.pin = string;
    }

    public void setNextCampaign(String string) {
        this.nextCampaign = string;
    }

}
