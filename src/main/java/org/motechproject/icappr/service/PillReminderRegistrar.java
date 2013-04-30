package org.motechproject.icappr.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.model.MRSAttributeDto;
import org.motechproject.mrs.model.MRSFacilityDto;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderRegistrar {
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private MRSPatientAdapter patientAdapter;
    private MRSFacilityAdapter facilityAdapter;
    private MessageCampaignEnroller messageCampaignEnroller;
    private AdherenceCallEnroller adherenceCallEnroller;

    @Autowired
    public PillReminderRegistrar(MRSPatientAdapter patientAdapter, MRSFacilityAdapter facilityAdapter,
            MessageCampaignEnroller messageCampaignEnroller, AdherenceCallEnroller adherenceCallEnroller) {
        this.patientAdapter = patientAdapter;
        this.facilityAdapter = facilityAdapter;
        this.messageCampaignEnroller = messageCampaignEnroller;
        this.adherenceCallEnroller = adherenceCallEnroller;
    }

    public void register(PillReminderRegistration registration) {
        logger.debug("Starting Patient Registration");
        createGenericPatient(registration);
        logger.debug("Finishing Patient Registration");
        messageCampaignEnroller.enrollInDailyMessageCampaign(registration);
        //enrollInAdherenceCall(registration, true);
    }
  

    private void enrollInAdherenceCall(PillReminderRegistration registration, boolean updateExisting) {
        AdherenceCallEnrollmentRequest request = new AdherenceCallEnrollmentRequest();
        request.setMotechID(registration.getPatientId());
        request.setPhoneNumber(registration.getPhoneNumber());
        request.setPin(registration.getPin());
        DateTime dateTime = DateUtil.now().plusMinutes(2);
        // will change based on information in form
        request.setDosageStartTime(String.format("%02d:%02d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));
        adherenceCallEnroller.enrollPatientWithId(request, updateExisting);
    }
    

    private void createGenericPatient(PillReminderRegistration registration) {
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
    }

    public PillReminderRegistration getRegistrationForPatient(String patientId) {
        MRSPatient patient = patientAdapter.getPatientByMotechId(patientId);
        if (patient == null) {
            return null;
        }

        PillReminderRegistration registration = new PillReminderRegistration();
        registration.setClinic(patient.getFacility().getName());
        registration.setPatientId(patientId);

        List<MRSAttribute> attrs = patient.getPerson().getAttributes();
        registration.setNextCampaign(MRSPersonUtil.getAttrValue(MrsConstants.PERSON_NEXT_CAMPAIGN_ATTR, attrs));
        registration.setPhoneNumber(MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PHONE_NUMBER_ATTR, attrs));
        registration.setPin(MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PIN_ATTR, attrs));

        return registration;
    }

}
