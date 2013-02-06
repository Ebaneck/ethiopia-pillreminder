package org.motechproject.icappr.it;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistry;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.icappr.events.EventSubjects;
import org.motechproject.server.messagecampaign.service.CampaignEnrollmentRecord;
import org.motechproject.server.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.server.messagecampaign.service.MessageCampaignService;
import org.motechproject.testing.osgi.BaseOsgiIT;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CampaignEnrollmentBundleIT extends BaseOsgiIT {

    private MessageCampaignService messageCampaignService;
    private EventListenerRegistryService eventListenerRegistryService;

    public void testCommcareCaseEnrollsIntoWeeklyCampaign() throws IOException {
        verifyServiceAvailable(MessageCampaignService.class.getName());
        verifyServiceAvailable(EventListenerRegistryService.class.getName());

        messageCampaignService = (MessageCampaignService) bundleContext.getService(bundleContext
                .getServiceReference(MessageCampaignService.class.getName()));

        eventListenerRegistryService = (EventListenerRegistryService) bundleContext.getService(bundleContext
                .getServiceReference(EventListenerRegistryService.class.getName()));

        assertTrue(noCampaignMessagesPresent());

        simulateCommcareCaseForward();
        waitForModuleToSendEvent(EventSubjects.PILL_REMINDER_REGISTRATION);
    }

    private void simulateCommcareCaseForward() throws IOException {
        String caseXml = IOUtils.toString(new ClassPathResource("commcare/case.xml").getInputStream());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8080/commcare/cases", caseXml, String.class);

        assertTrue("Sending Commcare Case XML did not return status 200", response.getStatusCode()
                .equals(HttpStatus.OK));
    }

    private boolean noCampaignMessagesPresent() {
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId("500");
        List<CampaignEnrollmentRecord> records = messageCampaignService.search(query);
        return records == null || records.size() == 0;
    }

    private void waitForModuleToSendEvent(String pillReminderRegistration) {
        RecordingEventListener listener = new RecordingEventListener();
        eventListenerRegistryService.registerListener(listener, EventSubjects.PILL_REMINDER_REGISTRATION);
        int retryCount = 0;
        while (retryCount < 60) {
            if (listener.callCount > 0) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            } finally {
                retryCount += 1;
            }
        }

        eventListenerRegistryService.clearListenersForBean(listener.getIdentifier());
    }

    static class RecordingEventListener implements EventListener {
        int callCount = 0;
        String identifier = UUID.randomUUID().toString();

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public void handle(MotechEvent arg0) {
            callCount += 1;
        }
    }
}
