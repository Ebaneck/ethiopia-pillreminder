package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.domain.ClinicVisit;
import org.motechproject.icappr.domain.PillReminderRegistrar;
import org.motechproject.icappr.domain.PillReminderRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.joda.time.*;
import org.joda.time.format.*;

@Component
public class ClinicVisitFormHandler {
	
	@Autowired
    private PillReminderRegistrar pillReminderRegistrar;

	public void handleForm(CommcareForm form) {
		FormValueElement topFormElement = form.getForm();

		if (topFormElement == null) {
			return;
		}

		String patientId = getValue(topFormElement, "patient_number");
		String clinicVisitDateTime = getValue(topFormElement, "clinic_visit_date_time");
        DateTime nextAppointment = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(clinicVisitDateTime);

        ClinicVisit clinicVisit = new ClinicVisit();
        
        clinicVisit.setPatientId(patientId);
        clinicVisit.setNextAppointment(nextAppointment);
        
        pillReminderRegistrar.registerClinicVisit(clinicVisit);
	}

	private String getValue(FormValueElement formElement, String elementName) {

		FormValueElement clinicElement = formElement.getElementByName(elementName);

		if (clinicElement == null) {
			return null;
		}

		return clinicElement.getValue();
	}
}
