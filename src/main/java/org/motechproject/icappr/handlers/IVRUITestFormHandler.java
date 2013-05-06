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
import org.motechproject.icappr.support.SchedulerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUITestFormHandler {

    private final IVRUIEnroller enroller;
    private final MRSPersonUtil mrsPersonUtil;
    private final SchedulerUtil schedulerUtil;
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    public IVRUITestFormHandler(IVRUIEnroller enroller, MRSPersonUtil mrsPersonUtil, SchedulerUtil schedulerUtil) {
        this.enroller = enroller;
        this.mrsPersonUtil = mrsPersonUtil;
        this.schedulerUtil = schedulerUtil;
    }

    public void handleForm(CommcareForm form) {
        logger.debug("Handling IVR UI Test form...");

        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            return;
        }

        String testType = getValue(topFormElement, "test_type");
        String phoneNumber = getValue(topFormElement, "phone_number");
        String pin = getValue(topFormElement, "pin");
        String language = getValue(topFormElement, "preferred_language");
        MRSPersonDto person = mrsPersonUtil.createAndSaveDemoPerson(phoneNumber, pin, language);

        if (testType.matches("message_campaign")) {
            logger.debug("Enrolling user in message campaign test");
            enrollInCalls(person);
        }
        else if (testType.matches("adherence_questions")){
            logger.debug("Enrolling user in adherence_questions test");
            schedulerUtil.scheduleAdherenceSurvey(null, phoneNumber, true);
        }
        else if (testType.matches("side_effect_questions")){
            logger.debug("Enrolling user in side_effect_questions test");
            schedulerUtil.scheduleSideEffectsSurvey(null, phoneNumber, true);
        }
        else if (testType.matches("clinic_reminder")){
            logger.debug("Enrolling user in clinic_reminder test");
            schedulerUtil.scheduleAppointments(null, phoneNumber, true);
        }
    }

    private void enrollInCalls(MRSPersonDto person) {
        IVREnrollmentRequest request = new IVREnrollmentRequest();
        String language = mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_LANGUAGE_ATTR).getValue();
        request.setLanguage(language);
        request.setPhoneNumber(mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_PHONE_NUMBER_ATTR).getValue());
        request.setPin(mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_PIN_ATTR).getValue());
        request.setMotechID(person.getPersonId());
        DateTime dateTime = DateUtil.now().plusMinutes(2);
        request.setCallStartTime(String.format("%02d:%02d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));
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
