package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;

import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.domain.PillReminderUpdate;
import org.motechproject.icappr.domain.PillReminderUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateFormHandler {
    
    @Autowired
    private PillReminderUpdater pillReminderUpdater;
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public void handleForm(CommcareForm form) {
        logger.debug("Handling Update form...");
        
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
        String todaysDate = getValue(topFormElement, "date_today");
        String preferredCallTime = getValue(topFormElement, "preferred_call_time");
        String preferredReminderFrequency =  getValue(topFormElement, "preferred_reminder_frequency");    //daily or weekly
        String preferredReminderDay = getValue(topFormElement, "preferred_day");
 
        PillReminderUpdate update = new PillReminderUpdate();
        
        update.setClinic(clinicId);        
        update.setPatientId(patientId);
        update.setPhoneNumber(phoneNumber);
        update.setPin(pin);
        update.setPreferredLanguage(preferredLanguage);
        update.setNextAppointment(nextAppointment);
        update.setTodaysDate(todaysDate);
        update.setPreferredCallTime(preferredCallTime);
        update.setPreferredReminderFrequency(preferredReminderFrequency);
        update.setPreferredReminderDay(preferredReminderDay);
        
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
