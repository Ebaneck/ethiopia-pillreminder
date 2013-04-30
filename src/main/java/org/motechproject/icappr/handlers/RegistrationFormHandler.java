package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.motechproject.icappr.service.PillReminderRegistrar;
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

		String studyId = getValue(topFormElement, "study_id");                    //Is this the pin?
		String preferredLanguage = getValue(topFormElement, "preferred_language");
	    String phoneNumber = getValue(topFormElement, "phone_number");
		String iptInitiationDate = getValue(topFormElement, "ipt_initiation");
		String preferredCallTime = getValue(topFormElement, "pref_call_time");
	    String nextAppointment = getValue(topFormElement, "next_appointment");
		
		/* Old form parameters
		 * String pin = getValue(topFormElement, "pin");
		 * String clinicId = getValue(topFormElement, "clinic_id");*/ 
	    		
        PillReminderRegistration registration = new PillReminderRegistration();
        
        registration.setPin(studyId);
        registration.setPreferredLanguage(preferredLanguage);
        registration.setPhoneNumber(phoneNumber);
        registration.setIptInitiationDate(iptInitiationDate);
        registration.setPreferredCallTime(preferredCallTime);
        registration.setNextAppointment(nextAppointment);
        
        /* Old setters for old form
         * registration.setClinic(clinicId);
         * registration.setPatientId(studyId);*/
        
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
