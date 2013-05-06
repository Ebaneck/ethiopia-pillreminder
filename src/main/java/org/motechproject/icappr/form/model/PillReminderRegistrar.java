package org.motechproject.icappr.form.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.service.AdherenceCallEnroller;
import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.icappr.support.SchedulerUtil;
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
    private SchedulerUtil schedulerUtil;

    @Autowired
    public PillReminderRegistrar(MRSPatientAdapter patientAdapter, MRSFacilityAdapter facilityAdapter,
            MessageCampaignEnroller messageCampaignEnroller, AdherenceCallEnroller adherenceCallEnroller, SchedulerUtil schedulerUtil) {
        this.patientAdapter = patientAdapter;
        this.facilityAdapter = facilityAdapter;
        this.messageCampaignEnroller = messageCampaignEnroller;
        this.adherenceCallEnroller = adherenceCallEnroller;
        this.schedulerUtil = schedulerUtil;
    }

    public void register(PillReminderRegistration registration, boolean isDemo) {
        logger.debug("Starting Patient Registration");
        createGenericPatient(registration);
        logger.debug("Finishing Patient Registration");
        messageCampaignEnroller.enrollInDailyMessageCampaign(registration);
        
        DateTime iptInitiationDate = DateTime.parse(registration.getIptInitiationDate());
        DateTime nextAppointmentDate = DateTime.parse(registration.getNextAppointment());
        schedulerUtil.scheduleAdherenceSurvey(iptInitiationDate, registration.getCaseId(), isDemo);
        schedulerUtil.scheduleAppointments(nextAppointmentDate, registration.getCaseId(), isDemo);
        schedulerUtil.scheduleSideEffectsSurvey(iptInitiationDate, registration.getCaseId(), isDemo);
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

        MRSPatient patient = new MRSPatientDto(null, mrsFacilityDto, person, registration.getCaseId());
        logger.debug("Creating generic patient with patient ID/case ID " + registration.getCaseId());
        patientAdapter.savePatient(patient);
    }

    public PillReminderRegistration getRegistrationForPatient(String patientId) {
        MRSPatient patient = patientAdapter.getPatientByMotechId(patientId);
        if (patient == null) {
            return null;
        }

        PillReminderRegistration registration = new PillReminderRegistration();
        registration.setClinic(patient.getFacility().getName());
        registration.setCaseId(patientId);

        List<MRSAttribute> attrs = patient.getPerson().getAttributes();
        registration.setNextCampaign(MRSPersonUtil.getAttrValue(MrsConstants.PERSON_NEXT_CAMPAIGN_ATTR, attrs));
        registration.setPhoneNumber(MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PHONE_NUMBER_ATTR, attrs));
        registration.setPin(MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PIN_ATTR, attrs));

        return registration;
    }

}
