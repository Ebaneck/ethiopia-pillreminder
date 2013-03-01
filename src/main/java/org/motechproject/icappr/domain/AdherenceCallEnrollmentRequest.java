package org.motechproject.icappr.domain;

public class AdherenceCallEnrollmentRequest extends Request {
	
	public AdherenceCallEnrollmentRequest(){
		setType(RequestTypes.ADHERENCE_CALL);
	}

    private String dosageStartTime;

    public void setDosageStartTime(String dosageStartTime) {
        this.dosageStartTime = dosageStartTime;
    }

    public String getDosageStartTime() {
        return dosageStartTime;
    }
}
