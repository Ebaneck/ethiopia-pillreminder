package org.motechproject.icappr.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class PillReminderCallEnrollmentRequest extends Request {

    @Autowired
    public PillReminderCallEnrollmentRequest(){
        setType(RequestTypes.PILL_REMINDER_CALL);
    }
    private String callStartTime;

    public String getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(String callStartTime) {
        this.callStartTime = callStartTime;
    }
}
