package org.motechproject.icappr.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class IVREnrollmentRequest extends Request{

	@Autowired
	public IVREnrollmentRequest(){
		setType(RequestTypes.IVR_UI);
	}
    private String callStartTime;

    public String getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(String callStartTime) {
        this.callStartTime = callStartTime;
    }

}
