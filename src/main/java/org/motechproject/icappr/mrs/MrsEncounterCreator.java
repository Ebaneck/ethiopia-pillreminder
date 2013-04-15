package org.motechproject.icappr.mrs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.motechproject.mrs.domain.MRSEncounter;
import org.motechproject.mrs.domain.MRSFacility;
import org.motechproject.mrs.domain.MRSObservation;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSProvider;
import org.motechproject.mrs.model.MRSEncounterDto;
import org.motechproject.mrs.model.MRSObservationDto;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class to create new MRS encounters for patients
 */
@Component
public class MrsEncounterCreator {

    private final MrsEntityFacade mrsEntityFacade;
    private final MRSEncounterAdapter encounterAdapter;

    @Autowired
    public MrsEncounterCreator(MrsEntityFacade mrsEntityFacade, MRSEncounterAdapter encounterAdapter) {
        this.mrsEntityFacade = mrsEntityFacade;
        this.encounterAdapter = encounterAdapter;
    }

    public void createPillTakenEncounterForPatient(String motechId) {
        MRSPatient patient = mrsEntityFacade.findPatientByMotechId(motechId);
        Set<MRSObservation> allObs = createObservationGroup();
        MRSEncounter encounter = createEncounter(patient, allObs);

        encounterAdapter.createEncounter(encounter);
    }

    private MRSEncounter createEncounter(MRSPatient patient, Set<MRSObservation> allObs) {
        MRSProvider provider = mrsEntityFacade.findMotechUser();
        MRSFacility facility = mrsEntityFacade.findMotechFacility();
        
        MRSEncounter encounter = new MRSEncounterDto();
        encounter.setDate(new DateTime());
        encounter.setObservations(allObs);
        encounter.setFacility(facility);
        encounter.setEncounterType(MrsConstants.PILL_REMINDER_ENCOUNTER_TYPE);
        encounter.setProvider(provider);
        encounter.setPatient(patient);

        return encounter;
    }

    private Set<MRSObservation> createObservationGroup() {
        MRSObservation obs = new MRSObservationDto(new Date(), MrsConstants.PILL_TAKEN_CONCEPT_NAME,
                MrsConstants.PILL_TAKEN_CONCEPT_YES_ANSWER);
        Set<MRSObservation> allObs = new HashSet<>();
        allObs.add(obs);
        return allObs;
    }
}
