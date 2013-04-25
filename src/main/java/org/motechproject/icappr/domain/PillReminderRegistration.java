package org.motechproject.icappr.domain;

public class PillReminderRegistration {
    private String phoneNumber;
    private String pin;
    private String clinic;
    private String nextCampaign;
    private String patientId;
	private String preferredLanguage;
	private String nextAppointment;
	private String iptInitiationDate;
	private String preferredCallTime;
	private String preferredDay;
	
    public String getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(String preferredDay) {
        this.preferredDay = preferredDay;
    }

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

	public String getNextCampaign() {
		return nextCampaign;
	}

}
