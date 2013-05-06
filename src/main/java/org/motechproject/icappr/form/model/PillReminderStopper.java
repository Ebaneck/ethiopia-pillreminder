package org.motechproject.icappr.form.model;

import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.icappr.support.SchedulerUtil;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderStopper {
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private MRSPatientAdapter patientAdapter;
    
    private MessageCampaignEnroller messageCampaignEnroller;
    
    private SchedulerUtil schedulerUtil;
    
    @Autowired
    public PillReminderStopper(MessageCampaignEnroller messageCampaignEnroller, MRSPatientAdapter patientAdapter, SchedulerUtil schedulerUtil) {
        this.messageCampaignEnroller = messageCampaignEnroller;
        this.patientAdapter = patientAdapter;
        this.schedulerUtil = schedulerUtil;
    }
    
    public void unenroll(PillReminderStop stop) {
        String externalId = stop.getCaseId();
        schedulerUtil.unscheduleAllIcapprJobs(externalId);
        getPatient(stop);
        messageCampaignEnroller.unenroll(stop);      
    }
    
    private void getPatient(PillReminderStop stop) {
        logger.debug("Retrieving patient for stop form...");        
        MRSPatient patient = patientAdapter.getPatient(stop.getCaseId());       
        if (patient != null)
            logger.debug("Successfully retrieved patient for stop form");
    }

}
