package org.motechproject.icappr.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.support.CallRequestDataKeys;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallService {
	
    private final IVRService ivrService;
    private final PillReminderSettings settings;
	
	@Autowired
    public CallService(IVRService ivrService, PillReminderSettings settings) {
        this.ivrService = ivrService;
        this.settings = settings;        
    }

	public void initiateCall(String motechId, String phonenum) {
        CallRequest callRequest = new CallRequest(phonenum, 120, settings.getVerboiceChannelName());

        Map<String, String> payload = callRequest.getPayload();

        // it's important that we store the motech id in the call request
        // payload. The verboice ivr service will copy all payload data to the
        // flow session so that we can retrieve it at a later time
        payload.put(CallRequestDataKeys.MOTECH_ID, motechId);

        // the callback_url is used once verboice starts a call to retrieve the
        // data for the call (e.g. TwiML)
        String callbackUrl = settings.getMotechUrl() + "/module/icappr/ivr/start?motech_call_id=%s";
        try {
            payload.put(CallRequestDataKeys.CALLBACK_URL,
                    URLEncoder.encode(String.format(callbackUrl, callRequest.getCallId()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        ivrService.initiateCall(callRequest);
    }

}
