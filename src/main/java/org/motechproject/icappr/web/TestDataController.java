package org.motechproject.icappr.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSEncounter;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.model.MRSAttributeDto;
import org.motechproject.mrs.model.MRSEncounterDto;
import org.motechproject.mrs.model.MRSFacilityDto;
import org.motechproject.mrs.model.MRSObservationDto;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class if for testing purposes only.
 */
@Controller
public class TestDataController {

    @Autowired
    private MRSPatientAdapter patientAdapter;

    @Autowired
    private MRSFacilityAdapter facilityAdapter;

    @Autowired
    private MRSEncounterAdapter encounterAdapter;

    @RequestMapping("/testdata")
    @ResponseBody
    public String testData(HttpServletRequest request) {

        MRSPatient patient1 = createPatient("TestSite", "English", "12074509521", "1234", DateTime.now().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        MRSPatient patient2 = createPatient("TestSite2", "English2", "22074509521", "12345", DateTime.now().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        MRSPatient patient3 = createPatient("TestSite3", "English3", "32074509521", "123456", DateTime.now().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        MRSPatient patient4 = createPatient("TestSite", "English", "42074509521", "1234567", DateTime.now().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        createEncounter(patient1, "yes", "Adherence Survey Call");

        createEncounter(patient2, "yes", "Patient Expressed Concern");

        createEncounter(patient3, "yes", "Side Effect Call");

        createEncounter(patient4, "yes", "PIN Failure Encounter");

        createEncounter(patient4, "yes", "Stop Request Encounter");

        return "Data Added";
    }

    private void createEncounter(MRSPatient patient, String answer, String encounterType) {
        MRSEncounter encounter = new MRSEncounterDto();
        encounter.setDate(DateTime.now());
        encounter.setEncounterType(encounterType);
        encounter.setPatient(patient);
        encounter.setEncounterId(UUID.randomUUID().toString());

        addObservation(encounter, "not sure it matters", answer, patient.getMotechId());

        encounterAdapter.createEncounter(encounter);
    }

    private void addObservation(MRSEncounter encounter, String obsName, String answer, String motechId) {
        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue(answer);
        observation.setDate(DateTime.now());
        observation.setConceptName(obsName);
        Set<MRSObservationDto> observations;

        if (encounter.getObservations() == null) {
            observations = new HashSet<MRSObservationDto>();
        } else {
            observations = (Set<MRSObservationDto>) encounter.getObservations();
        }

        observations.add(observation);
        encounter.setObservations(observations);
    }

    public MRSPatient createPatient(String studySite, String language, String phoneNumber, String pin, String initiationDate, String mrn, String caseId) {
        MRSFacilityDto mrsFacilityDto = new MRSFacilityDto();
        mrsFacilityDto.setFacilityId(studySite);
        facilityAdapter.saveFacility(mrsFacilityDto);

        MRSPerson person = new MRSPersonDto();

        List<MRSAttribute> attributes = new ArrayList<MRSAttribute>();
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_LANGUAGE_ATTR, language));
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_PHONE_NUMBER_ATTR, phoneNumber));
        attributes.add(new MRSAttributeDto(MrsConstants.PERSON_PIN_ATTR, pin));
        attributes.add(new MRSAttributeDto(MrsConstants.IPT_INITIATION_DATE, initiationDate));
        attributes.add(new MRSAttributeDto(MrsConstants.PATIENT_MRN, mrn));
        person.setAttributes(attributes);

        MRSPatient patient = new MRSPatientDto(null, mrsFacilityDto, person, caseId);

        return patientAdapter.savePatient(patient);
    }

}
