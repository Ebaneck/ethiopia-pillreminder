package org.motechproject.icappr.handlers;

import java.util.Map;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.constants.CaseConstants;
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

        //From form get case ID
        Map<String, String> attributes = topFormElement.getAttributes();
        String caseId = attributes.get(CaseConstants.FORM_CASE_ID);
        
        String phoneNumber = getValue(topFormElement, "phone_number");
        String preferredReminderFrequency =  getValue(topFormElement, "pref_medication_call_freq");    //daily or weekly
        String preferredReminderDay = getValue(topFormElement, "day_of_week");
        String preferredCallTime = getValue(topFormElement, "pref_call_time");  
        String nextAppointment = getValue(topFormElement, "next_appointment");
        String todaysDate = getValue(topFormElement, "today");
    
        /* Old form parameters
         * String clinicId = getValue(topFormElement, "clinic_id");
         */

        PillReminderUpdate update = new PillReminderUpdate();
            
        update.setCaseId(caseId);
        update.setPhoneNumber(phoneNumber);
        update.setPreferredReminderFrequency(preferredReminderFrequency);
        update.setPreferredReminderDay(preferredReminderDay);
        update.setPreferredCallTime(preferredCallTime);
        update.setNextAppointment(nextAppointment);
        update.setTodaysDate(todaysDate);
        
        /* Old form setters
         * update.setClinic(clinicId);  
         */
        
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
