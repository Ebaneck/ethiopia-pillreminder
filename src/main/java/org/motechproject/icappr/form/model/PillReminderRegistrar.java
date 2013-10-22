package org.motechproject.icappr.form.model;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
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
    private SchedulerUtil schedulerUtil;

    @Autowired
    public PillReminderRegistrar(MRSPatientAdapter patientAdapter, MRSFacilityAdapter facilityAdapter,
            MessageCampaignEnroller messageCampaignEnroller, SchedulerUtil schedulerUtil) {
        this.patientAdapter = patientAdapter;
        this.facilityAdapter = facilityAdapter;
        this.messageCampaignEnroller = messageCampaignEnroller;
        this.schedulerUtil = schedulerUtil;
    }

    public void register(PillReminderRegistration registration, boolean isDemo) {

        createGenericPatient(registration);

        messageCampaignEnroller.enrollInDailyMessageCampaign(registration.getCaseId(), registration.getPreferredCallTime());

        DateTime iptInitiationDate;
        try {
            iptInitiationDate = DateTime.parse(registration.getIptInitiationDate());
        } catch (IllegalArgumentException e) {
            iptInitiationDate = DateTime.now();
        }

        DateTime nextAppointmentDate = null;

        try {
            nextAppointmentDate = DateTime.parse(registration.getNextAppointment());
            schedulerUtil.scheduleAppointments(nextAppointmentDate, registration.getCaseId(), isDemo, registration.getPhoneNumber());
        } catch (IllegalArgumentException e) {
            logger.info("Incorrect appointment date set for: " + registration.getCaseId());
        }

        schedulerUtil.scheduleAdherenceSurvey(iptInitiationDate, registration.getCaseId(), isDemo, registration.getPhoneNumber());
        schedulerUtil.scheduleSideEffectsSurvey(iptInitiationDate, registration.getCaseId(), isDemo, registration.getPhoneNumber());
    }

    private void createGenericPatient(PillReminderRegistration registration) {
        MRSFacilityDto mrsFacilityDto = new MRSFacilityDto();
        mrsFacilityDto.setFacilityId(registration.getStudySite());
        facilityAdapter.saveFacility(mrsFacilityDto);

        MRSPerson person = new MRSPersonDto();

        List<MRSAttribute> attributes = new ArrayList<MRSAttribute>();
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_LANGUAGE_ATTR, registration.getPreferredLanguage()));
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_PHONE_NUMBER_ATTR, registration.getPhoneNumber()));
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_PIN_ATTR, registration.getPin()));
        attributes.add(new MRSAttributeDto(MrsConstants.IPT_INITIATION_DATE, registration.getIptInitiationDate()));
        attributes.add(new MRSAttributeDto(MrsConstants.PATIENT_MRN, registration.getMrn()));
        attributes.add(new MRSAttributeDto(MrsConstants.DAY_ENROLLED, DateTime.now().toString()));
        person.setAttributes(attributes);

        MRSPatient patient = new MRSPatientDto(null, mrsFacilityDto, person, registration.getCaseId());
        logger.debug("Creating generic patient with patient ID/case ID " + registration.getCaseId());
        patientAdapter.savePatient(patient);
    }
}
