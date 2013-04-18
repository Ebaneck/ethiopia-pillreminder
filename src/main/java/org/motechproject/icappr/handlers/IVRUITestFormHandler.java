package org.motechproject.icappr.handlers;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.service.IVRUIEnroller;
import org.motechproject.icappr.support.IVRUIDecisionTreeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUITestFormHandler {

	private final IVRUIEnroller enroller;
	private final MRSPersonUtil mrsPersonUtil;
	private final IVRUIDecisionTreeBuilder ivrUIDecisionTreeBuilder;
	
	private Logger logger = LoggerFactory.getLogger("motech-icappr");

	@Autowired
	public IVRUITestFormHandler(IVRUIEnroller enroller, MRSPersonUtil mrsPersonUtil, IVRUIDecisionTreeBuilder ivrUIDecisionTreeBuilder) {
		this.enroller = enroller;
		this.mrsPersonUtil = mrsPersonUtil;
		this.ivrUIDecisionTreeBuilder = ivrUIDecisionTreeBuilder;
	}

	public void handleForm(CommcareForm form) {
		logger.debug("Handling IVR UI Test form...");
		
		FormValueElement topFormElement = form.getForm();

		if (topFormElement == null) {
			return;
		}

		String phoneNumber = getValue(topFormElement, "phone_number");
		String pin = getValue(topFormElement, "pin");
		String language = getValue(topFormElement, "preferred_language");
		MRSPersonDto person = mrsPersonUtil.createAndSavePerson(phoneNumber, pin, language);
		enrollInCalls(person);
	}

	private void enrollInCalls(MRSPersonDto person) {
		IVREnrollmentRequest request = new IVREnrollmentRequest();
		String language = mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_LANGUAGE_ATTR).getValue();
	    request.setLanguage(language);
		request.setPhoneNumber(mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_PHONE_NUMBER_ATTR).getValue());
		request.setPin(mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_PIN_ATTR).getValue());
		request.setMotechID(person.getPersonId());
		DateTime dateTime = DateUtil.now().plusMinutes(2);
		request.setCallStartTime(String.format("%02d:%02d",
				dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));
		ivrUIDecisionTreeBuilder.buildTree();
		enroller.enrollPerson(request);
	}

	private String getValue(FormValueElement formElement, String elementName) {
		FormValueElement ivrElement = formElement.getElementByName(elementName);
		if (ivrElement == null) {
			return null;
		}
		return ivrElement.getValue();
	}

}
