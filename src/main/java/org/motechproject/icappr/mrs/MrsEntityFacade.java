package org.motechproject.icappr.mrs;

import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mrs.domain.MRSEncounter;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSFacility;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.domain.MRSProvider;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convenience class to access MRS entity objects
 */
@Component
public class MrsEntityFacade {

    private static final String DEFAULT_FIRST_NAME = "MOTECH First Name";
    private static final String DEFAULT_LAST_NAME = "MOTECH Last Name";
    private static final String DEFAULT_GENDER = "M";

    private MRSPatientAdapter patientAdapter;
    private MrsUserResolver userResolver;
    private MrsFacilityResolver facilityResolver;
    private MRSEncounterAdapter encounterAdapter;

    @Autowired
    public MrsEntityFacade(MRSPatientAdapter patientAdapter, MrsUserResolver userResolver,
            MrsFacilityResolver facilityResolver) {
        this.patientAdapter = patientAdapter;
        this.userResolver = userResolver;
        this.facilityResolver = facilityResolver;
    }

    public MRSProvider findMotechUser() {
        return userResolver.resolveMotechUser();
    }

    public MRSFacility findMotechFacility() {
        return facilityResolver.resolveMotechFacility();
    }

    public MRSPatient findPatientByMotechId(String motechId) {
        return (MRSPatient) patientAdapter.getPatientByMotechId(motechId);
    }

    public MRSPatient savePatient(MRSPatient patient) {
        return patientAdapter.savePatient(patient);
    }

    public MRSPatient createGenericPatient(String patientMotechId) {
        MRSPerson person = new MRSPersonDto();

        MRSPatient patient = new MRSPatientDto(null, facilityResolver.resolveMotechFacility(), person, patientMotechId);
        return patientAdapter.savePatient(patient);
    }

    public MRSEncounter getEncounterByFlowSessionId(String flowSessionId) {
        return encounterAdapter.getEncounterById(flowSessionId);
    }

    public void saveEncounterWithFlowSessionId(MRSEncounter encounter, String flowSessionId) {
        encounter.setEncounterId(flowSessionId);
        encounterAdapter.createEncounter(encounter);
    }
}
