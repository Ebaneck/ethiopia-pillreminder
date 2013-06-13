package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.form.model.PillReminderRegistrar;
import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFormHandler {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    private PillReminderRegistrar pillReminderRegistrar;

    public void handleForm(CommcareForm form, String externalId) {
        logger.warn("Handling registration form...");

        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            logger.warn("No registration form element for case: " + externalId);
            return;
        }

        PillReminderRegistration registration = new PillReminderRegistration();

        registration.setCaseId(externalId);
        registration.setClinic(getValue(topFormElement, "mrn"));
        registration.setPin(getValue(topFormElement, "pin"));
        registration.setPreferredLanguage(getValue(topFormElement, "preferred_language"));
        registration.setPhoneNumber(getValue(topFormElement, "phone_number"));
        registration.setIptInitiationDate(getValue(topFormElement, "ipt_initiation"));
        registration.setPreferredCallTime(getValue(topFormElement, "pref_call_time"));
        registration.setNextAppointment(getValue(topFormElement, "next_appointment"));

        pillReminderRegistrar.register(registration, false);
    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElement(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
