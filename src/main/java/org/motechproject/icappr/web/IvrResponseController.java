package org.motechproject.icappr.web;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class IvrResponseController {
    
    @RequestMapping("/campaign-message")
    @ResponseBody
    public String getCampaignMessageTwiML() {
        return "<Response>" +
        		"  <Say>This is a campaign message</Say>" +
                "</Response>";
    }

}
