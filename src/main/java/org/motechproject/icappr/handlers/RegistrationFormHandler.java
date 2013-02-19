package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.domain.PillReminderRegistrar;
import org.motechproject.icappr.domain.PillReminderRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFormHandler {
	
	@Autowired
    private PillReminderRegistrar pillReminderRegistrar;

	public void handleForm(CommcareForm form) {
		FormValueElement topFormElement = form.getCaseElement();

		String clinicId = getValue(topFormElement, "clinic_id");
		String patientId = getValue(topFormElement, "patient_number");
		String phoneNumber = getValue(topFormElement, "phone_number");
		String pin = getValue(topFormElement, "pin");
		String callFrequency = getValue(topFormElement, "pref_medication_call_freq");
		
        PillReminderRegistration registration = new PillReminderRegistration();
        
        registration.setClinic(clinicId);
        registration.setPatientId(patientId);
        registration.setPhoneNumber(phoneNumber);
        registration.setPin(pin);
        registration.setNextCampaign(callFrequency);
        
        pillReminderRegistrar.register(registration);
	}

	private String getValue(FormValueElement formElement, String elementName) {

		FormValueElement clinicElement = formElement.getElementByName(elementName);

		if (clinicElement == null) {
			return null;
		}

		return clinicElement.getValue();
	}
}
