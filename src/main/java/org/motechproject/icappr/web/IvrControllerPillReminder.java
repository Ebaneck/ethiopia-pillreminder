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
public class IvrControllerPillReminder {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private DecisionTreeSessionHandler decisionTreeSessionHandler;
    private PillReminderSettings settings;

    @Autowired
    public IvrControllerPillReminder(DecisionTreeSessionHandler decisionTreeSessionHandler, PillReminderSettings settings) {
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

        logger.debug("Generating security pin twiML for motechId " + motechId + " and request type " + requestType
                + " and audio file URL " + settings.getCmsliteUrlFor(SoundFiles.PIN_REQUEST, "English"));

        decisionTreeSessionHandler.updateFlowSessionIdToVerboiceId(motechId, verboiceId);

        view.addObject("path", settings.getMotechUrl());
        view.addObject("audioFileUrl", settings.getCmsliteUrlFor(SoundFiles.PIN_REQUEST, "English"));
        view.addObject("sessionId", verboiceId);
        view.addObject("requestType", requestType);

        return view;
    }

    /**
     * Attempts to authenticate the pin number entered by the patient. If the
     * pin is accepted, that is the digits entered by the user match the value
     * of the Pin attribute on the MRS Patient or CouchMRS person, then it redirects to the
     * motech-verboice module to handle the rest of the request. If it is not
     * accepted, the call ends.
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
        boolean correctPin = false;
        if (requestType.matches(RequestTypes.ADHERENCE_CALL)) {
            if (decisionTreeSessionHandler.digitsMatchPatientPin(sessionId, digits)) {
                logger.info("The pin is correct. Forwarding request to Pill reminder decision tree enrollment...");
                view = generateModelAndView(requestType, sessionId);
                correctPin = true;
            }
        }
        if (requestType.matches(RequestTypes.IVR_UI)) {
            if (decisionTreeSessionHandler.digitsMatchCouchPersonPin(sessionId, digits)) {
                logger.info("The pin is correct. Forwarding request to IVR UI Decision tree enrollment...");
                view = generateModelAndView(requestType, sessionId);
                correctPin = true;
            }
        }
        if (!correctPin) {
            logger.info("The pin is incorrect.");
            view = new ModelAndView("failed-authentication");
            view.addObject("audioFileUrl", settings.getCmsliteUrlFor(SoundFiles.INCORRECT_PIN, "English"));
        }
        return view;
    }


    /**
     * Helper method that builds the proper model and view
     * for a given request type
     */
    private ModelAndView generateModelAndView(String requestType, String sessionId) {
        ModelAndView view = null;
        String vm = getTwiMLForType(requestType);
        if (vm == null)
            logger.error("Could not retrieve request type for this call");
        else {
            view = new ModelAndView(vm);
            view.addObject("path", settings.getMotechUrl());
            view.addObject("sessionId", sessionId);
            if (requestType.matches(RequestTypes.ADHERENCE_CALL)){
                //TODO: Replace with logic that sets language to
                //that given in the CommCare form. This will probably
                //require adding the preferred language as an attribute
                //to the OpenMRS entity.
                view.addObject("language", "English");
            }
            if (requestType.matches(RequestTypes.IVR_UI))
                view.addObject("language", "English");
            logger.debug("Generating view with sessionId " + sessionId + " and language " + "English");
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
