package org.motechproject.icappr.service;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.motechproject.icappr.form.model.PillReminderStop;
import org.motechproject.icappr.form.model.PillReminderUpdate;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageCampaignEnroller {

    private MessageCampaignService messageCampaignService;

    @Autowired
    public MessageCampaignEnroller(MessageCampaignService messageCampaignService) {
        this.messageCampaignService = messageCampaignService;
    }

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public void unenroll(PillReminderStop stop){
        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery();
        query = query.withExternalId(stop.getCaseId());
        logger.debug("Stopping daily message campaign ");
        messageCampaignService.stopAll(query);
    }
    
    public void enrollInDailyMessageCampaign(PillReminderRegistration registration) {
        CampaignRequest request = new CampaignRequest();
        request.setCampaignName("DailyMessageCampaign");
        request.setExternalId(registration.getCaseId());
        request.setReferenceDate(DateUtil.now().toLocalDate());
        request.setReferenceTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));
        Time preferredTime = getPreferredTime(registration);
        logger.debug("preferred time is " + preferredTime.getHour() + ":" + preferredTime.getMinute());

        // request.setStartTime(preferredTime); //REMOVE COMMENT POST-TESTING

        logger.debug("starting daily message campaign ");
        messageCampaignService.startFor(request);
    }

    public void enrollInDailyMessageCampaign(PillReminderUpdate update) {
        CampaignRequest request = new CampaignRequest();
        request.setCampaignName("DailyMessageCampaign");
        request.setExternalId(update.getCaseId());
        request.setReferenceDate(DateUtil.now().toLocalDate());
        request.setReferenceTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));
        Time preferredTime = getPreferredTime(update);
        logger.debug("preferred time is " + preferredTime.getHour() + ":" + preferredTime.getMinute());

        // request.setStartTime(preferredTime); //REMOVE COMMENT POST-TESTING

        logger.debug("starting daily message campaign ");
        messageCampaignService.startFor(request);
    }

    public void enrollInWeeklyMessageCampaign(PillReminderUpdate update) {
        CampaignRequest request = new CampaignRequest();
        request.setExternalId(update.getCaseId());
        request.setReferenceDate(DateUtil.now().toLocalDate());
        request.setReferenceTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));

        Time preferredTime = getPreferredTime(update);
        logger.debug("preferred time is " + preferredTime.getHour() + ":" + preferredTime.getMinute());

        // request.setStartTime(preferredTime);                 //REMOVE COMMENT POST-TESTING
        request.setStartTime(new Time(DateTime.now().getHourOfDay(), DateTime.now().plusMinutes(1).getMinuteOfHour()));
        
        logger.debug("actual start time (for testing) is " + request.deliverTime());
        String dayOfWeek = update.getPreferredReminderDay();

        if (dayOfWeek.toLowerCase().matches("monday"))
            request.setCampaignName("MondayMessageCampaign");
        if (dayOfWeek.toLowerCase().matches("tuesday"))
            request.setCampaignName("TuesdayMessageCampaign");
        if (dayOfWeek.toLowerCase().matches("wednesday"))
            request.setCampaignName("WednesdayMessageCampaign");
        if (dayOfWeek.toLowerCase().matches("thursday"))
            request.setCampaignName("ThursdayMessageCampaign");
        if (dayOfWeek.toLowerCase().matches("friday"))
            request.setCampaignName("FridayMessageCampaign");
        if (dayOfWeek.toLowerCase().matches("saturday"))
            request.setCampaignName("SaturdayMessageCampaign");
        if (dayOfWeek.toLowerCase().matches("sunday"))
            request.setCampaignName("SundayMessageCampaign");

        logger.debug("Starting message campaign for day " + request.campaignName());
        messageCampaignService.startFor(request);
    }

    private Time getPreferredTime(PillReminderRegistration registration) {
        String stringPrefTime = registration.getPreferredCallTime().substring(0, 5);
        Time preferredTime = new Time();
        DateTime dateTimePreferredTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(stringPrefTime);
        int hour = dateTimePreferredTime.getHourOfDay();
        int minute = dateTimePreferredTime.getMinuteOfHour();
        preferredTime.setHour(hour);
        preferredTime.setMinute(minute);
        return preferredTime;
    }
    
    private Time getPreferredTime(PillReminderUpdate update) {
        String stringPrefTime = update.getPreferredCallTime().substring(0, 5);
        Time preferredTime = new Time();
        DateTime dateTimePreferredTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(stringPrefTime);
        int hour = dateTimePreferredTime.getHourOfDay();
        int minute = dateTimePreferredTime.getMinuteOfHour();
        preferredTime.setHour(hour);
        preferredTime.setMinute(minute);
        return preferredTime;
    }

}
