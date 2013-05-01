package org.motechproject.icappr.web;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.icappr.events.Events;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ManifestController {

    @Autowired
    private SettingsFacade facade;

    @Autowired
    private EventRelay eventRelay;

    @RequestMapping("/manifest")
    @ResponseBody
    public String testSideEffects(HttpServletRequest request) throws IOException {
        InputStream manifest = facade.getRawConfig("manifest.xml");
        return IOUtils.toString(manifest);
    }

    @RequestMapping("/manifest2")
    @ResponseBody
    public String manifest(HttpServletRequest request) throws IOException {
        InputStream manifest = facade.getRawConfig("icapprManifest.xml");
        return IOUtils.toString(manifest);
    }

    @RequestMapping("/answer")
    @ResponseBody
    public String sideEffect(HttpServletRequest request) {
        String choice = request.getParameter("answer");
        String question = request.getParameter("questionName");
        String callSid = request.getParameter("CallSid");
        if ("1".equals(choice)) {
            //raise event here
            String eventToRaise = null;
            switch (question) {
            case "question1" : eventToRaise = Events.YES_YELLOW_SKIN_OR_EYES; break;
            case "question2" : eventToRaise = Events.YES_ABDOMINAL_PAIN_OR_VOMITING; break;
            case"question3" : eventToRaise = Events.YES_SKIN_RASH_OR_ITCHY_SKIN; break;
            case "question4" : eventToRaise = Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET; break;
            }

            MotechEvent event = new MotechEvent(eventToRaise);
            event.getParameters().put("flowSessionId", callSid);

            eventRelay.sendEventMessage(event);
        }

        return "success";
    }
}
