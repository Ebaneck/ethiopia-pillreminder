package org.motechproject.icappr.web;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.motechproject.callflow.domain.CallDetailRecord;
import org.motechproject.callflow.domain.CallEvent;
import org.motechproject.callflow.domain.FlowSessionRecord;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.support.FlowSessionHandler;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VerboiceInteractionController {

    private Logger logger = LoggerFactory.getLogger("interaction-log");

    public final static String YES_INPUT = "1";
    public final static String NO_INPUT = "3";
    public final static String CALL_SID = "CallSid";
    public final static String MANIFEST_NAME = "icapprManifest.xml";
    public final static String ICAPPR_PATH = "/module/icappr";
    public final static String FLOW_SESSION_ID = "flowSessionId";
    public final static String REQUEST_TYPE = "requestType";
    public final static String ANSWER = "answer";
    public final static String QUESTION_NAME = "questionName";
    public final static String CONCERN_TYPE = "concernType";
    private static final String INPUT_VALUE = "userInput";

    @Autowired
    private SettingsFacade facade;

    @Autowired
    private PillReminderSettings settings;

    @Autowired
    private FlowSessionService flowSessionService;

    @Autowired
    private EventRelay eventRelay;

    @Autowired
    private FlowSessionHandler flowSessionHandler;

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
        String question = request.getParameter(QUESTION_NAME);
        String choice = request.getParameter(ANSWER);
        String eventToRaise = Events.INPUT_ERROR_EVENT;

        logger.debug("Side Effect Interaction - " + "CallSid: " + callSid + " Question: " + question + " Choice: " + choice);

        if (YES_INPUT.equals(choice)) {
            switch (question) {
            case "question1" : eventToRaise = Events.YES_YELLOW_SKIN_OR_EYES; break;
            case "question2" : eventToRaise = Events.YES_ABDOMINAL_PAIN_OR_VOMITING; break;
            case "question3" : eventToRaise = Events.YES_SKIN_RASH_OR_ITCHY_SKIN; break;
            case "question4" : eventToRaise = Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET; break;
            case "adherence1" : eventToRaise = Events.NO_MEDICATION_YESTERDAY; break;
            case "adherence2" : eventToRaise = Events.NO_MEDICATION_TWO_DAYS_AGO; break;
            case "adherence3" : eventToRaise = Events.NO_MEDICATION_THREE_DAYS_AGO; break;
            } 
        } else if (NO_INPUT.equals(choice)){
            switch (question) {
            case "question1" : eventToRaise = Events.NO_YELLOW_SKIN_OR_EYES; break;
            case "question2" : eventToRaise = Events.NO_ABDOMINAL_PAIN_OR_VOMITING; break;
            case "question3" : eventToRaise = Events.NO_SKIN_RASH_OR_ITCHY_SKIN; break;
            case "question4" : eventToRaise = Events.NO_TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET; break;
            case "adherence1" : eventToRaise = Events.YES_MEDICATION_YESTERDAY; break;
            case "adherence2" : eventToRaise = Events.YES_MEDICATION_TWO_DAYS_AGO; break;
            case "adherence3" : eventToRaise = Events.YES_MEDICATION_THREE_DAYS_AGO; break;
            }
        }

        MotechEvent event = new MotechEvent(eventToRaise);
        event.getParameters().put(FLOW_SESSION_ID, callSid);
        event.getParameters().put(INPUT_VALUE, choice);

        eventRelay.sendEventMessage(event);

        return "success";
    }

    @RequestMapping("patientConcern")
    @ResponseBody
    public String adherenceAnswer(HttpServletRequest request) {
        String callSid = request.getParameter(CALL_SID);
        String concern = request.getParameter(CONCERN_TYPE);
        String choice = request.getParameter(ANSWER);

        logger.debug("Concern Interaction - " + "CallSid: " + callSid + " Concern: " + concern + " Choice: " + choice);

        if (YES_INPUT.equals(choice)) {
            String eventToRaise = Events.INPUT_ERROR_EVENT;
            switch (concern) {
            case "adherenceConcern" : eventToRaise = Events.SEND_RA_MESSAGE_ADHERENCE_CONCERNS; break;
            case "appointmentConcern" : eventToRaise = Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS; break;

            }

            MotechEvent event = new MotechEvent(eventToRaise);
            event.getParameters().put(FLOW_SESSION_ID, callSid);

            eventRelay.sendEventMessage(event);
        } else if (NO_INPUT.equals(choice)) {
            String eventToRaise = Events.INPUT_ERROR_EVENT;
            switch (concern) {
            case "adherenceConcern" : eventToRaise = Events.NO_ADHERENCE_CONCERNS; break;
            case "appointmentConcern" : eventToRaise = Events.NO_APPOINTMENT_CONCERNS; break;
            }

            MotechEvent event = new MotechEvent(eventToRaise);
            event.getParameters().put(FLOW_SESSION_ID, callSid);

            eventRelay.sendEventMessage(event);
        }

        return "success";
    }

    @RequestMapping("data")
    @ResponseBody
    public String getData(HttpServletRequest request) {
        String requestType = request.getParameter(REQUEST_TYPE);
        String callSid = request.getParameter(CALL_SID);

        logger.debug("Data Interaction - " + "CallSid: " + callSid + " Data request type: " + requestType);

        FlowSession flowSession = flowSessionService.getSession(callSid);

        if (flowSession == null) {
            logger.debug("No flow session for data for CallSid: " + callSid + " and request type: " + requestType);
            return null;
        }

        if (MotechConstants.REMINDER_DAYS.equals(requestType)) {
            return "{\"dataResult\": \"" + flowSession.get(MotechConstants.REMINDER_DAYS) + "\"}";
        } else if (MotechConstants.THREE_FAILED_LOGINS.equals(requestType)) {
            FlowSessionRecord flowSessionRecord = (FlowSessionRecord) flowSession;
            CallDetailRecord callRecord = flowSessionRecord.getCallDetailRecord();
            CallEvent callEvent = new CallEvent("Pin Failure");
            callRecord.addCallEvent(callEvent);
            callRecord.setDisposition(CallDetailRecord.Disposition.AUTHENTICATION_FAILED);
            flowSessionService.updateSession(flowSessionRecord);

            MotechEvent event = new MotechEvent(Events.PIN_FAILURE);
            event.getParameters().put(FLOW_SESSION_ID, callSid);

            eventRelay.sendEventMessage(event);

            return "{\"dataResult\": \"" + "Success" + "\"}";
        } else {
            return null;
        }
    }
}
