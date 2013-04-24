package org.motechproject.icappr.listener;

import java.util.Map;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.constants.FormXmlnsConstants;
import org.motechproject.icappr.handlers.IVRUITestFormHandler;
import org.motechproject.icappr.handlers.RegistrationFormHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Came from:
 * http://code.google.com/p/motech-usm/source/browse/modules/commcare-
 * openmrs-mapping
 * /commcare-to-openmrs-mapper/src/main/java/org/motechproject/mapper
 * /listeners/FormListener.java?repo=ethiopia-1d&name=Branch_motech-0.17
 * 
 */
@Component
public class CommcareStubFormListener {

	@Autowired
	private CommcareFormService formService;

	@Autowired
	private RegistrationFormHandler registrationFormHandler;

	@Autowired
	private IVRUITestFormHandler ivrUITestFormHandler;
	
	private Logger logger = LoggerFactory.getLogger("motech-icappr");

	@MotechListener(subjects = EventSubjects.FORM_STUB_EVENT)
	public void handleStubForm(MotechEvent event) {
		logger.debug("Pill Reminder module handling form stub event...");
		
		Map<String, Object> parameters = event.getParameters();

		String formId = (String) parameters.get(EventDataKeys.FORM_ID);

		CommcareForm form = null;

		if (formId != null && formId.trim().length() > 0) {
			form = formService.retrieveForm(formId);
			logger.debug("Successfully retrieved form with formID..." + formId);
		}

		FormValueElement rootElement = null;

		if (form != null) {
			rootElement = form.getForm();
		}

		if (rootElement != null) {
			handleForm(form);
		}
		else{
			logger.debug("Root element was null...not handling form.");
		}
	}

	private void handleForm(CommcareForm form) {
		String xmlns = form.getForm().getAttributes()
				.get(FormXmlnsConstants.FORM_XMLNS_ATTRIBUTE);
		
		logger.debug("Handling form with xmlns" + xmlns);
		
		logger.debug("Checking to see if xmlns matches the registration form (" + FormXmlnsConstants.PILLREMINDER_REGISTRATION_FORM_XMLNS + ")");
		logger.debug("Checking to see if xmlns matches the IVR UI Test form (" + FormXmlnsConstants.IVR_TEST_FORM_XMLNS + ")");
		
		if (FormXmlnsConstants.PILLREMINDER_REGISTRATION_FORM_XMLNS.equals(xmlns)) {
			// delegate to registration form handler
			registrationFormHandler.handleForm(form);
			
		} else if (FormXmlnsConstants.IVR_TEST_FORM_XMLNS.equals(xmlns)) {
			// delegate to ivr test form handler
			ivrUITestFormHandler.handleForm(form);
		}
	}
}
