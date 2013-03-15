package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.content.SoundFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IvrControllerMessageCampaign {
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");
    private PillReminderSettings pillReminderSettings;
    
    @Autowired
    public IvrControllerMessageCampaign(PillReminderSettings pillReminderSettings) {
        this.pillReminderSettings = pillReminderSettings;
    }
    
    @RequestMapping("/campaign-message")
    public ModelAndView getCampaignMessageTwiML(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("campaign-message");
        String language = request.getParameter("language");
        
        logger.info("Generating Campaign Message Twiml in language " + language);
        
        view.addObject("audioFileUrl", pillReminderSettings.getCmsliteUrlFor(SoundFiles.CAMPAIGN_MESSAGE, language));
        
        logger.info("Should generate audio for " + pillReminderSettings.getCmsliteUrlFor(SoundFiles.CAMPAIGN_MESSAGE, language));
        
        return view;
    }

}
