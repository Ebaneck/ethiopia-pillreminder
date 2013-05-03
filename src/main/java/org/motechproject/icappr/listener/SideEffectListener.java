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

    public final static String YES_ANSWER = "yes";
    public final static String NO_ANSWER = "no";
    public final static String SIDE_EFFECT_ENCOUNTER_CALL = "Side Effect Call";
    public final static String FLOW_SESSION_ID = "flowSessionId";
    public final static String MOTECH_ID = "motechId";

    @Autowired
    private MRSEncounterAdapter encounterAdapter;

    @Autowired
    private FlowSessionService flowSessionService;

    @Autowired
    private MRSPatientAdapter patientAdapter;

    @MotechListener(subjects = {Events.YES_YELLOW_SKIN_OR_EYES, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET } )
    public void handleSideEffectEvents(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);

        FlowSession flowSession = flowSessionService.getSession(flowSessionId);
        String motechId = flowSession.get(MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, YES_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, YES_ANSWER, SIDE_EFFECT_ENCOUNTER_CALL);
        }
    }

    @MotechListener(subjects = {Events.YES_MEDICATION_YESTERDAY, Events.YES_MEDICATION_TWO_DAYS_AGO, Events.YES_MEDICATION_THREE_DAYS_AGO } )
    public void handleAdherenceSurveyYesAnswers(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);

        FlowSession flowSession = flowSessionService.getSession(flowSessionId);
        String motechId = flowSession.get(MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, YES_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, YES_ANSWER, SIDE_EFFECT_ENCOUNTER_CALL);
        }
    }

    @MotechListener(subjects = {Events.NO_MEDICATION_YESTERDAY, Events.NO_MEDICATION_TWO_DAYS_AGO, Events.NO_MEDICATION_THREE_DAYS_AGO } )
    public void handleAdherenceSurveyNoAnswers(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);

        FlowSession flowSession = flowSessionService.getSession(flowSessionId);
        String motechId = flowSession.get(MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, NO_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, NO_ANSWER, SIDE_EFFECT_ENCOUNTER_CALL);
        }
    }

    //move to messaging listener?
    @MotechListener(subjects = {Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS } )
    public void handleAppointmentConcern(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get("flowSessionId");

        FlowSession flowSession = flowSessionService.getSession(flowSessionId);
        String motechId = flowSession.get("motechId");
    }

    //move to messaging listener?
    @MotechListener(subjects = {Events.SEND_RA_MESSAGE_ADHERENCE_CONCERNS } )
    public void handleAdherenceConcern(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get("flowSessionId");

        FlowSession flowSession = flowSessionService.getSession(flowSessionId);
        String motechId = flowSession.get("motechId");
    }

    private void createEncounter(String motechId, MotechEvent event, String flowSessionId, String answer, String encounterType) {
        MRSEncounterDto encounter = new MRSEncounterDto();
        encounter.setEncounterId(flowSessionId);
        encounter.setEncounterType(encounterType);
        MRSPatient patient = addOrRetrievePatient(motechId);
        encounter.setPatient(patient);
        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue(answer);
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

    private void updateEncounter(MRSEncounter sideEffectEncounter, MotechEvent event, String motechId, String answer) {

        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue(answer);
        observation.setConceptName(event.getSubject());

        Set<MRSObservation> observations = (Set<MRSObservation>) sideEffectEncounter.getObservations();
        observations.add(observation);

        encounterAdapter.createEncounter(sideEffectEncounter);
    }
}
