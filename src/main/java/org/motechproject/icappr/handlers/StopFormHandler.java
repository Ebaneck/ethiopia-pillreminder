package org.motechproject.icappr.handlers;

import java.util.Map;

import org.motechproject.commcare.domain.CommcareForm;

import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.constants.CaseConstants;
import org.motechproject.icappr.form.model.PillReminderStop;
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
        
        //From form get case ID
        Map<String, String> attributes = topFormElement.getAttributes();
        String caseId = attributes.get(CaseConstants.FORM_CASE_ID);
        
        
        String stopDate = getValue(topFormElement, "stop_date");
        String stopReason =  getValue(topFormElement, "stop_reason");    //"opt_out" or "ipt_completion"
    
        /* Old form parameters
         * String clinicId = getValue(topFormElement, "clinic_id");
         */

        PillReminderStop stop = new PillReminderStop();
              
        stop.setCaseId(caseId);
        stop.setStopDate(stopDate);
        stop.setStopReason(stopReason);
    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElementByName(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
