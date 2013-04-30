package org.motechproject.icappr.form.model;

import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderUpdater {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private MRSPatientAdapter patientAdapter;
    private MessageCampaignEnroller messageCampaignEnroller;

    @Autowired
    public PillReminderUpdater(MRSPatientAdapter patientAdapter,
            MessageCampaignEnroller messageCampaignEnroller) {
        this.patientAdapter = patientAdapter;
        this.messageCampaignEnroller = messageCampaignEnroller;
    }

    
    public void reenroll(PillReminderUpdate update) {
        getPatient(update);

        if (update.getPreferredReminderFrequency().matches("daily")) {
            logger.debug("Enrolling patient in daily message campaign");
            messageCampaignEnroller.enrollInDailyMessageCampaign(update);
        }

        if (update.getPreferredReminderFrequency().matches("weekly")) {
            logger.debug("Enrolling patient in weekly message campaign");
            messageCampaignEnroller.enrollInWeeklyMessageCampaign(update);
        }
    }

    private void getPatient(PillReminderUpdate update) {
        logger.debug("Retrieving patient for update form...");        
        MRSPatient patient = patientAdapter.getPatient(update.getCaseId());       
        if (patient != null)
            logger.debug("Successfully retrieved patient for update form");
    }

}
