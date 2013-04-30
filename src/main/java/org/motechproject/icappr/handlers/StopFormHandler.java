package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;

import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.form.model.PillReminderUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StopFormHandler {
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public void handleForm(CommcareForm form) {
        logger.debug("Handling Stop form...");
        
        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            return;
        }
        
        String stopDate = getValue(topFormElement, "stop_date");
        String stopReason =  getValue(topFormElement, "stop_reason");    //"opt_out" or "ipt_completion"
        String preferredReminderDay = getValue(topFormElement, "day_of_week");
        String preferredCallTime = getValue(topFormElement, "pref_call_time");  
        String nextAppointment = getValue(topFormElement, "next_appointment");
        String todaysDate = getValue(topFormElement, "today");
    
        /* Old form parameters
         * String clinicId = getValue(topFormElement, "clinic_id");
         */

        PillReminderUpdate update = new PillReminderUpdate();
              
        update.setStopDate(stopDate);
        update.setStopReason(stopReason);
    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElementByName(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
