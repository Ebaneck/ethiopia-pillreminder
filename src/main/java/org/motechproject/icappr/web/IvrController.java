package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;

import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.content.SoundFiles;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.icappr.support.DecisionTreeSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ivr")
public class IvrController {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private DecisionTreeSessionHandler decisionTreeSessionHandler;
    private PillReminderSettings settings;

    @Autowired
    public IvrController(DecisionTreeSessionHandler decisionTreeSessionHandler,
            PillReminderSettings settings) {
        this.decisionTreeSessionHandler = decisionTreeSessionHandler;
        this.settings = settings;
    }

    /**
     * This is the first callback invoked by the Verboice application. At this
     * point, a call has been initiated to a specific patient. <br />
     * It should be noted that Verboice uses their own internal variable to
     * identify a call. Since we have no control over the value of this
     * identifier, we must update the flow session to use this value.
     */
    @RequestMapping("/start")
    public ModelAndView generateSecurityPinTwiML(HttpServletRequest request) {
        logger.info("Pill reminder controller received /start request");
        String verboiceId = request.getParameter("CallSid");
        String motechId = request.getParameter("motech_call_id");
        String requestType = request.getParameter("request_type");
        ModelAndView view = new ModelAndView("security-pin");

        logger.debug("Generating security pin twiML for motechId "
                + motechId
                + " and request type "
                + requestType
                + " and audio file URL "
                + "http://130.111.132.59:8081/motech-platform-server/module/cmsliteapi/stream/Amharic/amharicPin");
        // settings.getCmsliteUrlFor(SoundFiles.PIN_REQUEST));

        decisionTreeSessionHandler.updateFlowSessionIdToVerboiceId(motechId,
                verboiceId);

        view.addObject("path", settings.getMotechUrl());
        logger.debug("Path is " + settings.getMotechUrl());
        // view.addObject("audioFileUrl",
        // settings.getCmsliteUrlFor(SoundFiles.PIN_REQUEST));
        view.addObject(
                "audioFileUrl",
                "http://130.111.132.59:8081/motech-platform-server/module/cmsliteapi/stream/Amharic/amharicPin");
        view.addObject("sessionId", verboiceId);
        view.addObject("requestType", requestType);

        return view;
    }

    /**
     * Attempts to authenticate the pin number entered by the patient. If the
     * pin is accepted, that is the digits entered by the user match the value
     * of the Pin attribute on the MRS Patient, then it redirects to the
     * motech-verboice module to handle the rest of the request. If it is not
     * accepted, the call ends
     */
    @RequestMapping("/authenticate")
    public ModelAndView authenticate(HttpServletRequest request) {
        logger.info("Authenticating pin...");
        String sessionId = request.getParameter("CallSid");
        String digits = request.getParameter("Digits");
        String requestType = request.getParameter("requestType");
        logger.info("The session id is " + sessionId);
        logger.info("The pin entered is " + digits);
        ModelAndView view = null;
        if (requestType.matches(RequestTypes.ADHERENCE_CALL)) {
            if (decisionTreeSessionHandler.digitsMatchPatientPin(sessionId,
                    digits)) {
                logger.info("The pin is correct. Forwarding request to Pill reminder decision tree enrollment...");
                String vm = getTwiMLForType(requestType);
                if (vm == null)
                    logger.error("Could not retrieve request type for this call");
                else {
                    view = new ModelAndView(vm);
                    view.addObject("path", settings.getMotechUrl());
                    view.addObject("sessionId", sessionId);
                }
            } else {
                view = new ModelAndView("failed-authentication");
                view.addObject(
                        "audioFileUrl",
                        "http://130.111.132.59:8081/motech-platform-server/module/cmsliteapi/stream/Amharic/incorrectPin");
                // settings.getCmsliteUrlFor(SoundFiles.INCORRECT_PIN));
            }
        }
        if (requestType.matches(RequestTypes.IVR_UI)) {
            if (decisionTreeSessionHandler.digitsMatchCouchPersonPin(sessionId, digits)) {
                logger.info("The pin is correct. Forwarding request to IVR UI Decision tree enrollment...");
                String vm = getTwiMLForType(requestType);
                if (vm == null)
                    logger.error("Could not retrieve request type for this call");
                else {
                    logger.info("Generating TwiML vm file with name " + vm + "...");
                    view = new ModelAndView(vm);
                    view.addObject("path", settings.getMotechUrl());
                    view.addObject("sessionId", sessionId);
                }
            } else {
                view = new ModelAndView("failed-authentication");
                view.addObject(
                        "audioFileUrl",
                        "http://130.111.132.59:8081/motech-platform-server/module/cmsliteapi/stream/Amharic/incorrectPin");
                // settings.getCmsliteUrlFor(SoundFiles.INCORRECT_PIN));
            }
        }

        return view;
    }

    /**
     * Helper method that returns the proper twiML file so that a custom
     * Decision Tree can be made for the proper scenario (IVR UI Test, Adherence
     * call, etc.)
     */
    private String getTwiMLForType(String requestType) {
        logger.info("Retrieving twiML for request type " + requestType);
        if (requestType.matches(RequestTypes.ADHERENCE_CALL))
            return "adherence-redirect";
        else if (requestType.matches(RequestTypes.IVR_UI))
            return "ivr-ui-redirect";
        else
            return null;
    }

}
