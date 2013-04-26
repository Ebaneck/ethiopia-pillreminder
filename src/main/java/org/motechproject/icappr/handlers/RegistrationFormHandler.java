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
		FormValueElement topFormElement = form.getForm();

		if (topFormElement == null) {
			return;
		}

		String clinicId = getValue(topFormElement, "clinic_id");
		String patientId = getValue(topFormElement, "patient_number");
		String phoneNumber = getValue(topFormElement, "phone_number");
		String pin = getValue(topFormElement, "pin");
		String preferredLanguage = getValue(topFormElement, "preferred_language");
		String nextAppointment = getValue(topFormElement, "next_appointment");
		String iptInitiationDate = getValue(topFormElement, "ipt_initiation_date");
		String preferredCallTime = getValue(topFormElement, "preferred_call_time");
		
        PillReminderRegistration registration = new PillReminderRegistration();
        
        registration.setClinic(clinicId);
        registration.setPatientId(patientId);
        registration.setPhoneNumber(phoneNumber);
        registration.setPin(pin);
        registration.setPreferredLanguage(preferredLanguage);
        registration.setNextAppointment(nextAppointment);
        registration.setIptInitiationDate(iptInitiationDate);
        registration.setPreferredCallTime(preferredCallTime);
        
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
