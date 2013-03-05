package org.motechproject.icappr.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class AdherenceCallEnrollmentRequest extends Request {
	
	@Autowired
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
