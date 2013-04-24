package org.motechproject.icappr.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.support.CallRequestDataKeys;
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
        String phoneNum = request.getPhonenumber();
        String language = request.getLanguage();
        String motechId = request.getMotechId();
        String requestType = request.getType();

        CallRequest callRequest = new CallRequest(phoneNum, 120, settings.getVerboiceChannelName());

        Map<String, String> payload = callRequest.getPayload();

        // it's important that we store the motech id in the call request
        // payload. The verboice ivr service will copy all payload data to the
        // flow session so that we can retrieve it at a later time
        payload.put(CallRequestDataKeys.MOTECH_ID, motechId);
        payload.put(CallRequestDataKeys.REQUEST_TYPE, requestType);

        // the callback_url is used once verboice starts a call to retrieve the
        // data for the call (e.g. TwiML)
        String callbackUrl = settings.getMotechUrl() + "/module/icappr/ivr/start?motech_call_id=%s&request_type=%s&language=%s";
        String callbackStatusUrl = settings.getMotechUrl() + "/module/verboice/ivr/callstatus";

        try {
            payload.put(CallRequestDataKeys.CALLBACK_URL,
                    URLEncoder.encode(String.format(callbackUrl, callRequest.getCallId(), requestType, language), "UTF-8"));
            payload.put(CallRequestDataKeys.STATUS_CALLBACK_URL, callbackStatusUrl);
        } catch (UnsupportedEncodingException e) {
        }
        logger.info("Initiating call with requestType " + requestType + " and language " + language);
        ivrService.initiateCall(callRequest);
    }

}
