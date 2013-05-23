package org.motechproject.icappr.handlers;

import java.util.Map;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.constants.CaseConstants;
import org.motechproject.icappr.form.model.PillReminderRegistrar;
import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFormHandler {

    @Autowired
    private PillReminderRegistrar pillReminderRegistrar;

    public void handleForm(CommcareForm form, String externalId) {
        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
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

        FormValueElement clinicElement = formElement.getElementByName(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
