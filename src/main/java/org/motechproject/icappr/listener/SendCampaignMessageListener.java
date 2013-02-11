package org.motechproject.icappr.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.openmrs.OpenMRSConstants;
import org.motechproject.icappr.openmrs.OpenMRSUtil;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.mrs.services.PatientAdapter;
import org.motechproject.server.messagecampaign.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendCampaignMessageListener {

    private PatientAdapter patientAdapter;
    private IVRService ivrService;
    private final String HOST = "http://icappr.motechcloud.org";
    private final String VERBOICE_CHANNEL_NAME = "didlogic2";

    @Autowired
    public SendCampaignMessageListener(PatientAdapter patientAdapter, IVRService ivrService) {
        this.patientAdapter = patientAdapter;
        this.ivrService = ivrService;
    }

    @MotechListener(subjects = { EventKeys.SEND_MESSAGE })
    public void sendCampaignMessage(MotechEvent event) {
        String patientId = event.getParameters().get(EventKeys.EXTERNAL_ID_KEY).toString();
        Patient patient = patientAdapter.getPatientByMotechId(patientId);

        String phoneNumber = OpenMRSUtil.getAttrValue(OpenMRSConstants.OPENMRS_PHONE_NUM_ATTR, patient.getPerson()
                .getAttributes());
        CallRequest callRequest = new CallRequest(phoneNumber, 60, VERBOICE_CHANNEL_NAME);

        Map<String, String> payload = callRequest.getPayload();

        String callbackUrl = HOST + "/motech-platform-server/module/icappr/campaign-message";
        try {
            payload.put("callback_url", URLEncoder.encode(callbackUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }

        ivrService.initiateCall(callRequest);
    }
}
