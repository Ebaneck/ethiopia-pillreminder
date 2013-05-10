package org.motechproject.icappr.form.model;

import org.joda.time.DateTime;
import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.icappr.support.SchedulerUtil;
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
    private SchedulerUtil schedulerUtil;

    @Autowired
    public PillReminderUpdater(MRSPatientAdapter patientAdapter,
            MessageCampaignEnroller messageCampaignEnroller, SchedulerUtil schedulerUtil) {
        this.patientAdapter = patientAdapter;
        this.messageCampaignEnroller = messageCampaignEnroller;
        this.schedulerUtil = schedulerUtil;
    }

    public void reenroll(PillReminderUpdate update) {

        String phoneNumber = update.getPhoneNumber();
        String motechId = update.getCaseId();

        messageCampaignEnroller.unenroll(motechId);
        schedulerUtil.unscheduleAllIcapprJobs(motechId);

        if (update.getPreferredReminderFrequency().matches("daily")) {
            logger.debug("Enrolling patient in daily message campaign");
            messageCampaignEnroller.enrollInDailyMessageCampaign(update.getCaseId(), update.getPreferredCallTime());
        }

        if (update.getPreferredReminderFrequency().matches("weekly")) {
            logger.debug("Enrolling patient in weekly message campaign");
            messageCampaignEnroller.enrollInWeeklyMessageCampaign(update);
        }

        schedulerUtil.scheduleAdherenceSurvey(DateTime.parse(update.getTodaysDate()), motechId, false, phoneNumber);
        schedulerUtil.scheduleSideEffectsSurvey(DateTime.parse(update.getTodaysDate()), motechId, false, phoneNumber);
        schedulerUtil.scheduleAppointments(DateTime.parse(update.getNextAppointment()), motechId, false, phoneNumber);
    }
}
