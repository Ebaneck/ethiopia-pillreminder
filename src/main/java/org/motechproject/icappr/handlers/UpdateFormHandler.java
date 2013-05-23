package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.form.model.PillReminderUpdate;
import org.motechproject.icappr.form.model.PillReminderUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateFormHandler {

    @Autowired
    private PillReminderUpdater pillReminderUpdater;

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public void handleForm(CommcareForm form, String externalId) {
        logger.debug("Handling Update form...");

        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            return;
        }

        PillReminderUpdate update = new PillReminderUpdate();

        update.setCaseId(externalId);
        update.setPhoneNumber(getValue(topFormElement, "phone_number"));
        update.setPreferredReminderFrequency(getValue(topFormElement, "pref_medication_call_freq"));
        update.setPreferredReminderDay(getValue(topFormElement, "day_of_week"));
        update.setPreferredCallTime(getValue(topFormElement, "pref_call_time"));
        update.setNextAppointment(getValue(topFormElement, "next_appointment"));
        update.setTodaysDate(getValue(topFormElement, "today"));

        pillReminderUpdater.reenroll(update);
    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElementByName(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
