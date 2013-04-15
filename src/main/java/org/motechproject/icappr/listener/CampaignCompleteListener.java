package org.motechproject.icappr.listener;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.openmrs.OpenMRSUtil;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;

// @Component
public class CampaignCompleteListener {

    private MRSPatientAdapter patientAdapter;
    private MessageCampaignService messageCampaignService;

    @Autowired
    public CampaignCompleteListener(MRSPatientAdapter patientAdapter, MessageCampaignService messageCampaignService) {
        this.patientAdapter = patientAdapter;
        this.messageCampaignService = messageCampaignService;
    }

    @MotechListener(subjects = { EventKeys.CAMPAIGN_COMPLETED })
    public void handleCampaignComplete(MotechEvent event) {
        String campaignName = event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY).toString();
        if ("DailyMessageCampaign".equals(campaignName)) {
            enrollInNextCampaign(event.getParameters().get(EventKeys.EXTERNAL_ID_KEY).toString());
        }
    }

    private void enrollInNextCampaign(String externalId) {
        MRSPatient patient = patientAdapter.getPatientByMotechId(externalId);
        String nextCampaign = OpenMRSUtil.getAttrValue(MrsConstants.MRS_NEXT_CAMPAIGN_ATTR, patient.getPerson()
                .getAttributes());
        CampaignRequest request = new CampaignRequest();
        request.setCampaignName(nextCampaign);
        request.setExternalId(externalId);
        request.setReferenceDate(DateUtil.now().toLocalDate());
        request.setReferenceTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));

        messageCampaignService.startFor(request);
    }
}
