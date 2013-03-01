package org.motechproject.icappr.domain;

public class IVREnrollmentRequest extends Request{

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
