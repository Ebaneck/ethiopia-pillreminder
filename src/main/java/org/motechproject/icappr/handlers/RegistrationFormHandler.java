package org.motechproject.icappr.handlers;

import java.util.Map;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.icappr.constants.CaseConstants;
import org.motechproject.icappr.constants.FormXmlnsConstants;
import org.motechproject.icappr.form.model.PillReminderRegistrar;
import org.motechproject.icappr.form.model.PillReminderRegistration;
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
		
	    //From form get case ID
        Map<String, String> attributes = topFormElement.getAttributes();
        String caseId = attributes.get(CaseConstants.FORM_CASE_ID);
        
		String clinicId = getValue(topFormElement, "study_id");    
		String pin = getValue(topFormElement, "pin");
		String preferredLanguage = getValue(topFormElement, "preferred_language");
	    String phoneNumber = getValue(topFormElement, "phone_number");
		String iptInitiationDate = getValue(topFormElement, "ipt_initiation");
		String preferredCallTime = getValue(topFormElement, "pref_call_time");
	    String nextAppointment = getValue(topFormElement, "next_appointment");
		
		/* Old form parameters
		 * String clinicId = getValue(topFormElement, "clinic_id");*/ 
	    		
        PillReminderRegistration registration = new PillReminderRegistration();
        
        registration.setCaseId(caseId);
        
        registration.setClinic(clinicId);
        registration.setPin(pin);
        registration.setPreferredLanguage(preferredLanguage);
        registration.setPhoneNumber(phoneNumber);
        registration.setIptInitiationDate(iptInitiationDate);
        registration.setPreferredCallTime(preferredCallTime);
        registration.setNextAppointment(nextAppointment);
        
        /* Old setters for old form
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
