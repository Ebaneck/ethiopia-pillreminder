package org.motechproject.icappr.listener;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.event.MotechEvent;
import org.motechproject.icappr.openmrs.OpenMRSConstants;
import org.motechproject.icappr.openmrs.OpenMRSUtil;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.mrs.services.PatientAdapter;
import org.motechproject.server.messagecampaign.EventKeys;
import org.motechproject.server.messagecampaign.contract.CampaignRequest;
import org.motechproject.server.messagecampaign.service.MessageCampaignService;
import org.springframework.beans.factory.annotation.Autowired;

// @Component
public class CampaignCompleteListener {

    private PatientAdapter patientAdapter;
    private MessageCampaignService messageCampaignService;

    @Autowired
    public CampaignCompleteListener(PatientAdapter patientAdapter, MessageCampaignService messageCampaignService) {
        this.patientAdapter = patientAdapter;
        this.messageCampaignService = messageCampaignService;
    }

//    @MotechListener(subjects = { EventKeys.CAMPAIGN_COMPLETED })
    public void handleCampaignComplete(MotechEvent event) {
        String campaignName = event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY).toString();
        if ("DailyMessageCampaign".equals(campaignName)) {
            enrollInNextCampaign(event.getParameters().get(EventKeys.EXTERNAL_ID_KEY).toString());
        }
    }

    private void enrollInNextCampaign(String externalId) {
        Patient patient = patientAdapter.getPatientByMotechId(externalId);
        String nextCampaign = OpenMRSUtil.getAttrValue(OpenMRSConstants.OPENMRS_NEXT_CAMPAIGN_ATTR, patient.getPerson()
                .getAttributes());
        CampaignRequest request = new CampaignRequest();
        request.setCampaignName(nextCampaign);
        request.setExternalId(externalId);
        request.setReferenceDate(DateUtil.now().toLocalDate());
        request.setReferenceTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));

        messageCampaignService.startFor(request);
    }
}
