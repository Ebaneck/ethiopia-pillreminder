package org.motechproject.icappr.listener;

import java.util.HashSet;
import java.util.Set;

import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.events.Events;
import org.motechproject.mrs.domain.MRSEncounter;
import org.motechproject.mrs.domain.MRSObservation;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.model.MRSEncounterDto;
import org.motechproject.mrs.model.MRSObservationDto;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SideEffectListener {

    @Autowired
    private MRSEncounterAdapter encounterAdapter;

    @Autowired
    private FlowSessionService flowSessionService;

    @Autowired
    private MRSPatientAdapter patientAdapter;

    @MotechListener(subjects = {Events.YES_YELLOW_SKIN_OR_EYES, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET } )
    public void handleSideEffectEvents(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get("flowSessionId");

        FlowSession flowSession = flowSessionService.getSession(flowSessionId);
        String motechId = flowSession.get("motechId");

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId);
        } else {
            createEncounter(motechId, event, flowSessionId);
        }
    }

    private void createEncounter(String motechId, MotechEvent event, String flowSessionId) {
        MRSEncounterDto encounter = new MRSEncounterDto();
        encounter.setEncounterId(flowSessionId);
        encounter.setEncounterType("Side Effect Call");
        MRSPatient patient = addOrRetrievePatient(motechId);
        encounter.setPatient(patient);
        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue("yes");
        observation.setConceptName(event.getSubject());
        Set<MRSObservation> observations = new HashSet<MRSObservation>();
        observations.add(observation);
        encounter.setObservations(observations);
        encounterAdapter.createEncounter(encounter);
    }

    private MRSPatient addOrRetrievePatient(String motechId) {
        MRSPatient patient = patientAdapter.getPatientByMotechId(motechId);

        if (patient == null) {
            patient = new MRSPatientDto();
            patient.setMotechId(motechId);
        } 

        return patient;
    }

    private void updateEncounter(MRSEncounter sideEffectEncounter, MotechEvent event, String motechId) {

        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue("yes");
        observation.setConceptName(event.getSubject());

        Set<MRSObservation> observations = (Set<MRSObservation>) sideEffectEncounter.getObservations();
        observations.add(observation);

        encounterAdapter.createEncounter(sideEffectEncounter);
    }
}
