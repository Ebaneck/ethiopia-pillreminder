package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.springframework.stereotype.Component;

@Component
public class IVRUITestFormHandler {

	public void handleForm(CommcareForm form) {
		FormValueElement topFormElement = form.getForm();

		if (topFormElement == null) {
			return;
		}

		/**UNFINISHED*/
        
	}

	private String getValue(FormValueElement formElement, String elementName) {
		FormValueElement ivrElement = formElement.getElementByName(elementName);
		if (ivrElement == null) {			return null;
		}
		return ivrElement.getValue();
	}
}
