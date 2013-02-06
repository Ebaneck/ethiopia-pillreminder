package org.motechproject.icappr.it;

import java.util.List;

import org.motechproject.server.messagecampaign.service.CampaignEnrollmentRecord;
import org.motechproject.server.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.server.messagecampaign.service.MessageCampaignService;
import org.motechproject.testing.osgi.BaseOsgiIT;

public class CampaignEnrollmentBundleIT extends BaseOsgiIT {

    private MessageCampaignService service;

    public void testCommcareCaseEnrollsIntoWeeklyCampaign() {
        verifyServiceAvailable(MessageCampaignService.class.getName());

        service = (MessageCampaignService) bundleContext.getService(bundleContext
                .getServiceReference(MessageCampaignService.class.getName()));

        assertTrue(noCampaignMessagesPresent());
    }

    private boolean noCampaignMessagesPresent() {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId("500");
        List<CampaignEnrollmentRecord> records = service.search(query);
        return records == null || records.size() == 0;
    }

}
