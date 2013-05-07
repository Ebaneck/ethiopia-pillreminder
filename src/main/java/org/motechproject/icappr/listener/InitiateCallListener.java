package org.motechproject.icappr.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.service.CallInitiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitiateCallListener {


    @Autowired
    private CallInitiationService callService;


    @MotechListener(subjects = Events.SIDE_EFFECTS_SURVEY_CALL)
    public void handleSideEffectCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.SIDE_EFFECT_CALL);
    }

    @MotechListener(subjects = {Events.APPOINTMENT_SCHEDULE_CALL, Events.SECOND_APPOINTMENT_SCHEDULE_CALL} )
    public void handleAppointmentCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.APPOINTMENT_CALL);
    }

    @MotechListener(subjects = Events.ADHERENCE_ASSESSMENT_CALL)
    public void handleAdherenceSurveyCall(MotechEvent event) {
        initiateCallByType(event, RequestTypes.ADHERENCE_CALL);
    }

    private void initiateCallByType(MotechEvent event, String callType) {
        Request request = new Request();
        request.setType(callType);
        request.setMotechID((String) event.getParameters().get(MotechConstants.MOTECH_ID));
        request.setPhoneNumber((String) event.getParameters().get(MotechConstants.PHONE_NUM));
        request.setLanguage((String) event.getParameters().get(MotechConstants.LANGUAGE));

        callService.initiateCall(request);
    }

}
