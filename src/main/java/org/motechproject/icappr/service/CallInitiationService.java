package org.motechproject.icappr.service;

import java.util.Map;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.constants.CallRequestDataKeys;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallInitiationService {

    private final IVRService ivrService;
    private final PillReminderSettings settings;

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    public CallInitiationService(IVRService ivrService, PillReminderSettings settings) {
        this.ivrService = ivrService;
        this.settings = settings;        
    }

    /**This method initiates a call for a given Request. Currently the definitive
     * request types are AdherenceCallEnrollmentRequest and IVREnrollmentRequest.
     * This method assumes that phone number, language, motech ID, and request type 
     * have been specified within the request payload.
     * @param request
     */
    public void initiateCall(Request request) {

        String phoneNum = request.getPhoneNumber();
        String language = request.getLanguage();
        String motechId = request.getMotechId();
        String requestType = request.getType();

        String channelName = settings.getVerboiceChannelName();

        String callFlowId = null;

        switch (requestType) {
            case RequestTypes.ADHERENCE_CALL : callFlowId = settings.getAdherenceFlowId(language); break;
            case RequestTypes.APPOINTMENT_CALL :
            case RequestTypes.SECOND_APPOINTMENT_CALL: callFlowId = settings.getAppointmentReminderFlowId(language); break;
            case RequestTypes.PILL_REMINDER_CALL : callFlowId = settings.getPillReminderFlowId(language); break;
            case RequestTypes.SIDE_EFFECT_CALL : callFlowId = settings.getSideEffectFlowId(language); break;
        }

        CallRequest callRequest = new CallRequest(phoneNum, 120, channelName);

        Map<String, String> payload = callRequest.getPayload();

        // it's important that we store the motech id in the call request
        // payload. The verboice ivr service will copy all payload data to the
        // flow session so that we can retrieve it at a later time
        payload.put(CallRequestDataKeys.MOTECH_ID, motechId);
        payload.put(CallRequestDataKeys.REQUEST_TYPE, requestType);

        payload.putAll(request.getPayload());

        String callbackStatusUrl = settings.getMotechUrl() + "/module/verboice/ivr/callstatus";


        payload.put(CallRequestDataKeys.STATUS_CALLBACK_URL, callbackStatusUrl);
        payload.put(CallRequestDataKeys.LANGUAGE, language);
        payload.put(CallRequestDataKeys.FLOW_ID, callFlowId);

        logger.info("Initiating call with requestType " + requestType + " and language " + language + " with flow: " + callFlowId + " on channel: " + channelName);

        ivrService.initiateCall(callRequest);
    }
}
