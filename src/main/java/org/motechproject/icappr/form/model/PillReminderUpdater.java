package org.motechproject.icappr.form.model;

import org.joda.time.DateTime;
import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.icappr.support.EnrollmentValidator;
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

    private MessageCampaignEnroller messageCampaignEnroller;
    private SchedulerUtil schedulerUtil;

    private MRSPatientAdapter patientAdapter;

    @Autowired
    public PillReminderUpdater(MessageCampaignEnroller messageCampaignEnroller, SchedulerUtil schedulerUtil,
            MRSPatientAdapter patientAdapter) {
        this.messageCampaignEnroller = messageCampaignEnroller;
        this.schedulerUtil = schedulerUtil;
        this.patientAdapter = patientAdapter;
    }

    public void reenroll(PillReminderUpdate update) {

        String phoneNumber = update.getPhoneNumber();
        String motechId = update.getCaseId();

        logger.debug("Re-enrolling: " + motechId);

        MRSPatient patient = patientAdapter.getPatientByMotechId(motechId);

        if (patient == null) {
            // this scenario only happens when a case has had test forms
            // submitted against it (which creates a case), and then an update
            // form is submitted. This should not be allowed due to no
            // registration
            logger.debug("Received update form for ID: " + motechId + " but no corresponending patient exists");
            return;
        }

        if (EnrollmentValidator.patientCanUpdateReminderFrequency(patient, DateTime.now())) {
            messageCampaignEnroller.unenroll(motechId);

            if (update.getPreferredReminderFrequency().matches("daily")) {
                messageCampaignEnroller.enrollInDailyMessageCampaign(update.getCaseId(), update.getPreferredCallTime());
            }

            if (update.getPreferredReminderFrequency().matches("weekly")) {
                messageCampaignEnroller.enrollInWeeklyMessageCampaign(update);
            }
        }

        String appointmentToday = update.getAppointmentToday();
        if (null != appointmentToday && appointmentToday.matches("yes")) {

            logger.debug("Re-scheduling adherence, side effect, and appointment calls for: " + motechId);

            schedulerUtil.unscheduleAllIcapprJobs(motechId);
            schedulerUtil.scheduleAdherenceSurvey(DateTime.parse(update.getTodaysDate()), motechId, false, phoneNumber);
            schedulerUtil.scheduleSideEffectsSurvey(DateTime.parse(update.getTodaysDate()), motechId, false, phoneNumber);
            schedulerUtil.scheduleAppointments(DateTime.parse(update.getNextAppointment()), motechId, false, phoneNumber);
        } else {
            logger.debug("Not re-scheduling calls for: " + motechId);
        }
    }
}
