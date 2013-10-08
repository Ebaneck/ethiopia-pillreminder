package org.motechproject.icappr.listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.mrs.domain.MRSEncounter;
import org.motechproject.mrs.domain.MRSObservation;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.model.MRSEncounterDto;
import org.motechproject.mrs.model.MRSObservationDto;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallInteractionListener {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public static final String YES_ANSWER = "yes";
    public static final String NO_ANSWER = "no";
    public static final String SIDE_EFFECT_ENCOUNTER_CALL = "Side Effect Call";
    public static final String ADHERENCE_SURVEY_ENCOUNTER_CALL = "Adherence Survey Call";
    public static final String PATIENT_CONCERN_ENCOUNTER = "Patient Expressed Concern";
    public static final String FLOW_SESSION_ID = "flowSessionId";
    public static final String PIN_FAILURE = "PIN Failure Encounter";
    public static final String STOP_REQUEST = "Stop Request Encounter";

    @Autowired
    private MRSEncounterAdapter encounterAdapter;

    @Autowired
    private MRSPatientAdapter patientAdapter;

    @Autowired
    private MRSPersonUtil personUtil;

    @MotechListener(subjects = {Events.YES_YELLOW_SKIN_OR_EYES, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET } )
    public void handleSideEffectEvents(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, YES_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, YES_ANSWER, SIDE_EFFECT_ENCOUNTER_CALL, DateTime.now());
        }
    }

    @MotechListener(subjects = {Events.NO_YELLOW_SKIN_OR_EYES, Events.NO_SKIN_RASH_OR_ITCHY_SKIN, Events.NO_ABDOMINAL_PAIN_OR_VOMITING, Events.NO_TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET } )
    public void handleNoSideEffectEvents(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, NO_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, NO_ANSWER, SIDE_EFFECT_ENCOUNTER_CALL, DateTime.now());
        }
    }

    @MotechListener(subjects = {Events.YES_MEDICATION_YESTERDAY, Events.YES_MEDICATION_TWO_DAYS_AGO, Events.YES_MEDICATION_THREE_DAYS_AGO } )
    public void handleAdherenceSurveyYesAnswers(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, YES_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, YES_ANSWER, ADHERENCE_SURVEY_ENCOUNTER_CALL, DateTime.now());
        }
    }

    @MotechListener(subjects = {Events.NO_MEDICATION_YESTERDAY, Events.NO_MEDICATION_TWO_DAYS_AGO, Events.NO_MEDICATION_THREE_DAYS_AGO } )
    public void handleAdherenceSurveyNoAnswers(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        MRSEncounter sideEffectEncounter = encounterAdapter.getEncounterById(flowSessionId);

        if (sideEffectEncounter != null) {
            updateEncounter(sideEffectEncounter, event, motechId, NO_ANSWER);
        } else {
            createEncounter(motechId, event, flowSessionId, NO_ANSWER, ADHERENCE_SURVEY_ENCOUNTER_CALL, DateTime.now());
        }
    }

    @MotechListener(subjects = {Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS, Events.SEND_RA_MESSAGE_ADHERENCE_CONCERNS, Events.NO_ADHERENCE_CONCERNS, Events.NO_APPOINTMENT_CONCERNS} )
    public void handleAppointmentConcern(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        MRSPatient patient = patientAdapter.getPatientByMotechId(motechId);

        if (patient == null) {
            return;
        }

        DateTime timeOfConcern = DateTime.now();

        MotechEvent concernEvent = new MotechEvent(event.getSubject() + "+DATA");
        concernEvent.getParameters().put(MotechConstants.CONCERN_TIME, timeOfConcern);
        concernEvent.getParameters().put(MotechConstants.MOTECH_ID, motechId);

        switch (event.getSubject()) {
            case Events.SEND_RA_MESSAGE_ADHERENCE_CONCERNS: createEncounter(motechId, event, flowSessionId, YES_ANSWER, PATIENT_CONCERN_ENCOUNTER + ".Adherence", DateTime.now() ); break;
            case Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS: createEncounter(motechId, event, flowSessionId, YES_ANSWER, PATIENT_CONCERN_ENCOUNTER + ".Appointment", DateTime.now() ); break;
            case Events.NO_ADHERENCE_CONCERNS: createEncounter(motechId, event, flowSessionId, YES_ANSWER, PATIENT_CONCERN_ENCOUNTER + ".Adherence", DateTime.now()); break;
            case Events.NO_APPOINTMENT_CONCERNS: createEncounter(motechId, event, flowSessionId, NO_ANSWER, PATIENT_CONCERN_ENCOUNTER + ".Appointment", DateTime.now()); break;
        }
    }
    
    @MotechListener(subjects = Events.PIN_FAILURE)
    public void handlePinFailure(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get(FLOW_SESSION_ID);
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        createEncounter(motechId, event, flowSessionId, YES_ANSWER, PIN_FAILURE, DateTime.now());
    }
    
    @MotechListener(subjects = Events.STOP_REQUEST)
    public void handleStopRequest(MotechEvent event) {

        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        createEncounter(motechId, event, UUID.randomUUID().toString(), (String) event.getParameters().get(MotechConstants.STOP_REASON), STOP_REQUEST, DateTime.parse((String) event.getParameters().get(MotechConstants.STOP_DATE)));        
    }

    private void createEncounter(String motechId, MotechEvent event, String flowSessionId, String answer, String encounterType, DateTime obsDate) {
        MRSEncounterDto encounter = new MRSEncounterDto();
        encounter.setDate(DateTime.now());
        encounter.setEncounterId(flowSessionId);
        encounter.setEncounterType(encounterType);
        MRSPatient patient = addOrRetrievePatient(motechId);

        if (patient == null) {
            return;
        }

        encounter.setPatient(patient);
        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue(answer);
        observation.setDate(DateTime.now());
        observation.setConceptName(event.getSubject());
        observation.setDate(obsDate);
        Set<MRSObservation> observations = new HashSet<MRSObservation>();
        observations.add(observation);
        encounter.setObservations(observations);
        logger.info("Saving encounter: " + encounterType + " for Motech ID: " + motechId + " corresponding to flow session: " + flowSessionId);
        encounterAdapter.createEncounter(encounter);
    }

    private MRSPatient addOrRetrievePatient(String motechId) {
        MRSPatient patient = patientAdapter.getPatientByMotechId(motechId);

        if (patient == null) {
            MRSPerson person = personUtil.getPersonByID(motechId);

            if (person != null && MRSPersonUtil.hasDummyAttr(person)) {
                return null;
            }

            patient = new MRSPatientDto();
            patient.setMotechId(motechId);
        } 

        return patient;
    }

    private void updateEncounter(MRSEncounter sideEffectEncounter, MotechEvent event, String motechId, String answer) {

        MRSObservationDto observation = new MRSObservationDto();
        observation.setPatientId(motechId);
        observation.setValue(answer);
        observation.setDate(DateTime.now());
        observation.setConceptName(event.getSubject());

        Set<MRSObservationDto> observations = (Set<MRSObservationDto>) sideEffectEncounter.getObservations();
        observations.add(observation);

        encounterAdapter.createEncounter(sideEffectEncounter);
    }
}
