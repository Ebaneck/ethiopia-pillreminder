package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;

import org.motechproject.commcare.domain.FormValueElement;
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

    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElementByName(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
