package org.motechproject.icappr.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.openmrs.OpenMRSConstants;
import org.motechproject.icappr.openmrs.OpenMRSUtil;
import org.motechproject.icappr.service.AdherenceCallEnroller;
import org.motechproject.mrs.domain.Attribute;
import org.motechproject.mrs.domain.Facility;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.mrs.model.OpenMRSAttribute;
import org.motechproject.mrs.model.OpenMRSFacility;
import org.motechproject.mrs.model.OpenMRSPatient;
import org.motechproject.mrs.model.OpenMRSPerson;
import org.motechproject.mrs.services.FacilityAdapter;
import org.motechproject.mrs.services.PatientAdapter;
import org.motechproject.server.messagecampaign.contract.CampaignRequest;
import org.motechproject.server.messagecampaign.service.MessageCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderRegistrar {

    private PatientAdapter patientAdapter;
    private FacilityAdapter facilityAdapter;
    private MessageCampaignService messageCampaignService;
    private AdherenceCallEnroller adherenceCallEnroller;
    
    private static final Map<String, String> clinicMappings = new HashMap<>();

    static {
        clinicMappings.put("clinic_a", "Clinic A");
        clinicMappings.put("clinic_b", "Clinic B");
    }

    @Autowired
    public PillReminderRegistrar(PatientAdapter patientAdapter, FacilityAdapter facilityAdapter,
            MessageCampaignService messageCampaignService, AdherenceCallEnroller adherenceCallEnroller) {
        this.patientAdapter = patientAdapter;
        this.facilityAdapter = facilityAdapter;
        this.messageCampaignService = messageCampaignService;
        this.adherenceCallEnroller = adherenceCallEnroller;
    }

    public void register(PillReminderRegistration registration) {
        createGenericPatient(registration);
        enrollInDailyMessageCampaign(registration);
    }

    private void enrollInDailyMessageCampaign(PillReminderRegistration registration) {
        CampaignRequest request = new CampaignRequest();
        request.setCampaignName("DailyMessageCampaign");
        request.setExternalId(registration.getPatientId());
        request.setReferenceDate(DateUtil.now().toLocalDate());
        request.setReferenceTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));
        messageCampaignService.startFor(request);
    }

    private void enrollInAdherenceCall(PillReminderRegistration registration) {
        AdherenceCallEnrollmentRequest request = new AdherenceCallEnrollmentRequest();
        request.setMotechId(registration.getPatientId());
        request.setPhonenumber(registration.getPhoneNumber());
        request.setPin(registration.getPin());

        DateTime dateTime = DateUtil.now().plusMinutes(2);
// will change based on information in form
        request.setDosageStartTime(String.format("%02d:%02d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));

        adherenceCallEnroller.enrollPatientWithId(request);
    }

    private void createGenericPatient(PillReminderRegistration registration) {
        List<? extends Facility> facilities = facilityAdapter
                .getFacilities(clinicMappings.get(registration.getClinic()));
        if (facilities.size() == 0) {
            throw new RuntimeException("Could not find OpenMRS Facility with name: "
                    + clinicMappings.get(registration.getClinic()));
        }
        OpenMRSFacility facility = (OpenMRSFacility) facilities.get(0);

        OpenMRSPerson person = new OpenMRSPerson();
        person.firstName("MOTECH Generic Patient");
        person.setLastName("MOTECH Generic Patient");
        person.setGender("M");
        person.setDateOfBirth(DateUtil.now());

        person.addAttribute(new OpenMRSAttribute(OpenMRSConstants.OPENMRS_PHONE_NUM_ATTR, registration.getPhoneNumber()));
        person.addAttribute(new OpenMRSAttribute(OpenMRSConstants.OPENMRS_PIN_ATTR, registration.getPin()));
        person.addAttribute(new OpenMRSAttribute(OpenMRSConstants.OPENMRS_NEXT_CAMPAIGN_ATTR, registration
                .nextCampaign()));

        OpenMRSPatient patient = new OpenMRSPatient(registration.getPatientId(), person, facility);
        patientAdapter.savePatient(patient);
    }

    public PillReminderRegistration getRegistrationForPatient(String patientId) {
        Patient patient = patientAdapter.getPatientByMotechId(patientId);
        if (patient == null) {
            return null;
        }

        PillReminderRegistration registration = new PillReminderRegistration();
        registration.setClinic(patient.getFacility().getName());
        registration.setPatientId(patientId);

        List<Attribute> attrs = patient.getPerson().getAttributes();
        registration.setNextCampaign(OpenMRSUtil.getAttrValue(OpenMRSConstants.OPENMRS_NEXT_CAMPAIGN_ATTR, attrs));
        registration.setPhoneNumber(OpenMRSUtil.getAttrValue(OpenMRSConstants.OPENMRS_PHONE_NUM_ATTR, attrs));
        registration.setPin(OpenMRSUtil.getAttrValue(OpenMRSConstants.OPENMRS_PIN_ATTR, attrs));

        return registration;
    }

}
