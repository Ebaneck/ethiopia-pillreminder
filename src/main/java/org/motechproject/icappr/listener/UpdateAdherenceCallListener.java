package org.motechproject.icappr.listener;

import org.motechproject.icappr.Events;
import org.motechproject.icappr.support.DecisionTreeSessionHandler;
import org.motechproject.icappr.service.AdherenceCallService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MOTECH Listener that updates the daily pill regimen dosage status
 */
@Component
public class UpdateAdherenceCallListener {

    private AdherenceCallService adherenceCallService;
    private DecisionTreeSessionHandler decisionTreeSessionHandler;

    @Autowired
    public UpdateAdherenceCallListener(AdherenceCallService pillReminders, DecisionTreeSessionHandler decisionTreeSessionHandler) {
        this.adherenceCallService = pillReminders;
        this.decisionTreeSessionHandler = decisionTreeSessionHandler;
    }

    @MotechListener(subjects = Events.PATIENT_TOOK_DOSAGE)
    public void handleDosageTaken(MotechEvent event) {
        String motechId = decisionTreeSessionHandler.getMotechIdForSessionWithId(event.getParameters()
                .get("flowSessionId").toString());

        adherenceCallService.setDosageStatusKnownForPatient(motechId);
    }
}
