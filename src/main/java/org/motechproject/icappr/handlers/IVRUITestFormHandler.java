package org.motechproject.icappr.handlers;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.support.SchedulerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUITestFormHandler {

    private final MRSPersonUtil mrsPersonUtil;
    private final SchedulerUtil schedulerUtil;
    private final MessageCampaignService campaignService;
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    public IVRUITestFormHandler(MRSPersonUtil mrsPersonUtil, SchedulerUtil schedulerUtil, MessageCampaignService campaignService) {
        this.mrsPersonUtil = mrsPersonUtil;
        this.schedulerUtil = schedulerUtil;
        this.campaignService = campaignService;
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
            logger.debug("Enrolling user in demo message campaign test");
            CampaignRequest enrollRequest = new CampaignRequest();
            enrollRequest.setCampaignName(MotechConstants.DEMO_CAMPAIGN);
            enrollRequest.setExternalId(person.getPersonId());
            enrollRequest.setReferenceDate(DateTime.now().toLocalDate());
            enrollRequest.setReferenceTime(new Time(DateTime.now().hourOfDay().get(), DateTime.now().minuteOfHour().get()));
            campaignService.startFor(enrollRequest);
        }
        else if (testType.matches("adherence_questions")){
            logger.debug("Enrolling user in demo adherence test");
            schedulerUtil.scheduleAdherenceSurvey(null, person.getPersonId(), true, phoneNumber);
        }
        else if (testType.matches("side_effect_questions")){
            logger.debug("Enrolling user in demo side effect test");
            schedulerUtil.scheduleSideEffectsSurvey(null, person.getPersonId(), true, phoneNumber);
        }
        else if (testType.matches("clinic_reminder")){
            logger.debug("Enrolling user in demo clinic reminder test");
            schedulerUtil.scheduleAppointments(null, person.getPersonId(), true, phoneNumber);
        }
    }

    private String getValue(FormValueElement formElement, String elementName) {
        FormValueElement ivrElement = formElement.getElementByName(elementName);
        if (ivrElement == null) {
            return null;
        }
        return ivrElement.getValue();
    }

}
