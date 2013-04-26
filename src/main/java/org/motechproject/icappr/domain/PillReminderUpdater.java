package org.motechproject.icappr.domain;

import java.util.ArrayList;
import java.util.List;

import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.model.MRSAttributeDto;
import org.motechproject.mrs.model.MRSFacilityDto;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderUpdater {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private MRSPatientAdapter patientAdapter;
    private MRSFacilityAdapter facilityAdapter;
    private MessageCampaignEnroller messageCampaignEnroller;

    @Autowired
    public PillReminderUpdater(MRSPatientAdapter patientAdapter, MRSFacilityAdapter facilityAdapter,
            MessageCampaignEnroller messageCampaignEnroller) {
        this.patientAdapter = patientAdapter;
        this.facilityAdapter = facilityAdapter;
        this.messageCampaignEnroller = messageCampaignEnroller;
    }

    public void reenroll(PillReminderUpdate update) {

        createGenericPatient(update);

        if (update.getPreferredReminderFrequency().matches("daily")) {
            logger.debug("Enrolling patient in daily message campaign");
            messageCampaignEnroller.enrollInDailyMessageCampaign(update);
        }

        if (update.getPreferredReminderFrequency().matches("weekly")) {
            logger.debug("Enrolling patient in weekly message campaign");
            messageCampaignEnroller.enrollInWeeklyMessageCampaign(update);
        }
    }

    private void createGenericPatient(PillReminderUpdate registration) {
        logger.debug("Creating generic patient from update form...");
        MRSFacilityDto mrsFacilityDto = new MRSFacilityDto();
        mrsFacilityDto.setFacilityId(registration.getClinic());
        facilityAdapter.saveFacility(mrsFacilityDto);

        MRSPerson person = new MRSPersonDto();

        List<MRSAttribute> attributes = new ArrayList<MRSAttribute>();
        
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_LANGUAGE_ATTR, registration.getPreferredLanguage()));
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_PHONE_NUMBER_ATTR, registration.getPhoneNumber()));
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_PIN_ATTR, registration.getPin()));

        person.setAttributes(attributes);

        MRSPatient patient = new MRSPatientDto(null, mrsFacilityDto, person, registration.getPatientId());
        patientAdapter.savePatient(patient);
        logger.debug("Successfully saved patient from update form.");
    }

}
