package org.motechproject.icappr.listener;


import java.util.List;

import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.mrs.MRSConstants;
import org.motechproject.icappr.mrs.MrsEntityFacade;
import org.motechproject.icappr.service.CallInitiationService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mrs.domain.Attribute;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.server.pillreminder.api.EventKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MOTECH Listener that handles a pill reminder event. Upon receiving the event,
 * this listener attempts to retrieve information about patient, specifically
 * the patients phone number. It then initiates a call to that patient using the
 * provider IVR service. Note: Though the IVRService is a generic interface and
 * in theory could be swapped out by another implementation, this listener is
 * dependent on the Verboice IVR Service because of details within that
 * implementation class
 */
@Component
public class AdherenceCallListener {
	private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private final CallInitiationService callService;
    private final MrsEntityFacade mrsEntityFacade;
    private final PillReminderSettings settings;

    @Autowired
    public AdherenceCallListener(CallInitiationService callService, MrsEntityFacade mrsEntityFacade, PillReminderSettings settings) {
        this.callService = callService;
        this.mrsEntityFacade = mrsEntityFacade;
        this.settings = settings;
    }

    @MotechListener(subjects = EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT)
    public void handlePillReminderEvent(MotechEvent motechEvent) {
        if (maxRetryCountReached(motechEvent, settings.getMaxRetryCount())) {
            return;
        }

        String motechId = motechEvent.getParameters().get(EventKeys.EXTERNAL_ID_KEY).toString();
        Patient patient = mrsEntityFacade.findPatientByMotechId(motechId);

        String phonenum = getPhoneFromAttributes(patient.getPerson().getAttributes());
        if (phonenum == null) {
            logger.error("No Phone Number attribute found on patient with MOTECH Id: " + motechId);
            logger.error("Cannot initiate a phone call without a phone number");
            return;
        }
        
        String language = getLanguageFromAttributes(patient.getPerson().getAttributes());
        if (language == null) {
            logger.error("No Language attribute found on patient with MOTECH Id: " + motechId);
            logger.error("Cannot initiate a phone call without a phone number");
            return;
        }
        AdherenceCallEnrollmentRequest request = new AdherenceCallEnrollmentRequest();
        request.setLanguage(language);
        request.setPhoneNumber(phonenum);
        request.setMotechID(motechId);

        callService.initiateCall(request);
    }
    
    private boolean maxRetryCountReached(MotechEvent motechEvent, int maxRetryCount) {
        return Integer.parseInt(motechEvent.getParameters().get(EventKeys.PILLREMINDER_TIMES_SENT).toString()) >= maxRetryCount;
    }

    private String getPhoneFromAttributes(List<Attribute> attributes) {
        for (Attribute attr : attributes) {
            if (MRSConstants.MRS_PHONE_NUM_ATTR.equals(attr.getName())) {
                return attr.getValue();
            }
        }

        return null;
    }
    
    private String getLanguageFromAttributes(List<Attribute> attributes) {
        for (Attribute attr : attributes) {
            if (MRSConstants.MRS_LANGUAGE_ATTR.equals(attr.getName())) {
                return attr.getValue();
            }
        }

        return null;
    }
}
