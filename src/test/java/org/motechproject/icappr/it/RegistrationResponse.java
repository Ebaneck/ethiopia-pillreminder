package org.motechproject.icappr.it;

import java.util.ArrayList;
import java.util.List;

public class RegistrationResponse {
    private String patientId;
    private String phoneNumber;
    private String pin;
    private String clinic;
    private String nextCampaign;
    private List<CampaignRegistration> campaigns = new ArrayList<>();
    private List<ScheduledJob> scheduledJobs = new ArrayList<>();

    public static class CampaignRegistration {
        private String name;
        private String startDate;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

    }

    private static class ScheduledJob {
        private String subject;
        private String startDate;
        private String endDate;
        private String triggerCount;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getTriggerCount() {
            return triggerCount;
        }

        public void setTriggerCount(String triggerCount) {
            this.triggerCount = triggerCount;
        }
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getNextCampaign() {
        return nextCampaign;
    }

    public void setNextCampaign(String nextCampaign) {
        this.nextCampaign = nextCampaign;
    }

    public List<CampaignRegistration> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<CampaignRegistration> campaigns) {
        this.campaigns = campaigns;
    }

    public List<ScheduledJob> getScheduledJobs() {
        return scheduledJobs;
    }

    public void setScheduledJobs(List<ScheduledJob> scheduledJobs) {
        this.scheduledJobs = scheduledJobs;
    }
}
