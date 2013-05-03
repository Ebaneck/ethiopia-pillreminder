package org.motechproject.icappr.web;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.events.Events;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VerboiceInteractionController {

    public final static String YES_INPUT = "1";
    public final static String NO_INPUT = "3";
    public final static String CALL_SID = "CallSid";
    public final static String MANIFEST_NAME = "icapprManifest.xml";
    public final static String ICAPPR_PATH = "/module/icappr";
    public final static String FLOW_SESSION_ID = "flowSessionId";

    @Autowired
    private SettingsFacade facade;

    @Autowired
    private PillReminderSettings settings;

    @Autowired
    private EventRelay eventRelay;

    @RequestMapping("/manifest")
    @ResponseBody
    public String manifest(HttpServletRequest request) throws IOException {
        InputStream manifest = facade.getRawConfig(MANIFEST_NAME);
        String manifestBody = IOUtils.toString(manifest);
        String host = settings.getMotechUrl() + ICAPPR_PATH;
        return manifestBody.replace("HOST", host);
    }

    @RequestMapping("/answer")
    @ResponseBody
    public String sideEffect(HttpServletRequest request) {
        String callSid = request.getParameter(CALL_SID);
        String question = request.getParameter("questionName");
        String choice = request.getParameter("answer");
        String eventToRaise = Events.INPUT_ERROR_EVENT;

        if (YES_INPUT.equals(choice)) {
            switch (question) {
            case "question1" : eventToRaise = Events.YES_YELLOW_SKIN_OR_EYES; break;
            case "question2" : eventToRaise = Events.YES_ABDOMINAL_PAIN_OR_VOMITING; break;
            case"question3" : eventToRaise = Events.YES_SKIN_RASH_OR_ITCHY_SKIN; break;
            case "question4" : eventToRaise = Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET; break;
            case "adherence1" : eventToRaise = Events.YES_MEDICATION_YESTERDAY; break;
            case "adherence2" : eventToRaise = Events.YES_MEDICATION_TWO_DAYS_AGO; break;
            case "adherence3" : eventToRaise = Events.YES_MEDICATION_THREE_DAYS_AGO; break;
            } 

        } else if (NO_INPUT.equals(choice)){
            switch (question) {
            case "adherence1" : eventToRaise = Events.NO_MEDICATION_YESTERDAY; break;
            case "adherence2" : eventToRaise = Events.NO_MEDICATION_TWO_DAYS_AGO; break;
            case "adherence3" : eventToRaise = Events.NO_MEDICATION_THREE_DAYS_AGO; break;
            }
        }

        MotechEvent event = new MotechEvent(eventToRaise);
        event.getParameters().put(FLOW_SESSION_ID, callSid);

        eventRelay.sendEventMessage(event);

        return "success";
    }

    @RequestMapping("patientConcern")
    @ResponseBody
    public String adherenceAnswer(HttpServletRequest request) {
        String callSid = request.getParameter(CALL_SID);
        String concern = request.getParameter("concernType");
        String choice = request.getParameter("answer");

        if (YES_INPUT.equals(choice)) {
            String eventToRaise = null;
            switch (concern) {
            case "adherenceConcern" : eventToRaise = Events.SEND_RA_MSSAGE_ADHERENCE_CONCERNS; break;
            case "appointmentConcern" : eventToRaise = Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS; break;

            }

            MotechEvent event = new MotechEvent(eventToRaise);
            event.getParameters().put(FLOW_SESSION_ID, callSid);

            eventRelay.sendEventMessage(event);
        }

        return "success";
    }
}
