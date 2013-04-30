package org.motechproject.icappr.form.model;

public class PillReminderStop extends PillReminderForm{

    private String stopDate;
    private String stopReason;
    
    public String getStopDate() {
        return stopDate;
    }
    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }
    public String getStopReason() {
        return stopReason;
    }
    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }
    
}
