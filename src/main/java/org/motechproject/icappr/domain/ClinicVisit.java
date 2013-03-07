package org.motechproject.icappr.domain;
import org.joda.time.DateTime;

public class ClinicVisit {

	private String patientId;
	private DateTime nextAppointment;

	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public DateTime getNextAppointment() {
		return nextAppointment;
	}
	public void setNextAppointment(DateTime nextAppointment) {
		this.nextAppointment = nextAppointment;
	}

}
