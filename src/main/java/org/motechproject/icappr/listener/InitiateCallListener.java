package org.motechproject.icappr.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsEntityFacade;
import org.motechproject.icappr.service.CallInitiationService;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitiateCallListener {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    private CallInitiationService callService;

    @Autowired
    private MrsEntityFacade mrsEntityFacade;

    @Autowired
    private MRSPersonUtil mrsPersonUtil;

    @MotechListener(subjects = Events.SIDE_EFFECTS_SURVEY_CALL)
    public void handleSideEffectCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.SIDE_EFFECT_CALL);
    }

    @MotechListener(subjects = Events.APPOINTMENT_SCHEDULE_CALL )
    public void handleFirstAppointmentCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.APPOINTMENT_CALL);
    }

    @MotechListener(subjects = Events.SECOND_APPOINTMENT_SCHEDULE_CALL )
    public void handleSecondAppointmentCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.SECOND_APPOINTMENT_CALL);
    }

    @MotechListener(subjects = Events.ADHERENCE_ASSESSMENT_CALL)
    public void handleAdherenceSurveyCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.ADHERENCE_CALL);
    }

    @MotechListener(subjects = Events.PILL_REMINDER_CALL)
    public void handlePillreminderCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.PILL_REMINDER_CALL);
    }

    private void initiateCallByType(MotechEvent event, String callType) {

        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);
        String phoneNumber = (String) event.getParameters().get(MotechConstants.PHONE_NUM);

        Request request = new Request();
        request.setType(callType);
        request.setMotechId(motechId);
        request.setPhoneNumber(phoneNumber);
        request.setLanguage(getUserPreferredLanguage(motechId));

        if (RequestTypes.APPOINTMENT_CALL.equals(callType)) {
            request.addParameter(MotechConstants.REMINDER_DAYS, "2");
        } else if (RequestTypes.SECOND_APPOINTMENT_CALL.equals(callType)) {
            request.addParameter(MotechConstants.REMINDER_DAYS, "1");
        }

        callService.initiateCall(request);
    }

    private String getUserPreferredLanguage(String motechId) {
        MRSPatient patient = mrsEntityFacade.findPatientByMotechId(motechId);
        if (patient == null) {
            MRSPerson person = mrsPersonUtil.getPersonByID(motechId);
            if (person != null) {
                String preferredLanguage = MRSPersonUtil.getAttrValue(MotechConstants.LANGUAGE, person.getAttributes());
                return (preferredLanguage == null) ? "english" : preferredLanguage;
            }
            return "english";
        } else {
            String preferredLanguage = MRSPersonUtil.getAttrValue(MotechConstants.LANGUAGE, patient.getPerson().getAttributes());
            return (preferredLanguage == null) ? "english" : preferredLanguage;
        }
    }

}
