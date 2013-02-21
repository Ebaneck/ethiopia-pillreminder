package org.motechproject.icappr.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IvrResponseController {
    
    @RequestMapping("/campaign-message")
    @ResponseBody
    public String getCampaignMessageTwiML() {
        return "<Response>" +
        		"  <Say>This is a campaign message</Say>" +
                "</Response>";
    }

}
