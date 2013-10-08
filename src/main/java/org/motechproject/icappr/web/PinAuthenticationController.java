package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;
import org.motechproject.icappr.support.FlowSessionHandler;
import org.motechproject.icappr.constants.MotechConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ivr")
public class PinAuthenticationController {
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    private FlowSessionHandler flowSessionHandler;

    @Autowired
    public PinAuthenticationController(FlowSessionHandler flowSessionHandler) {
        this.flowSessionHandler = flowSessionHandler;
    }

    /**
     * Attempts to authenticate the pin number entered by the patient. If the
     * pin is accepted, that is the digits entered by the user match the value
     * of the Pin attribute on the MRS Patient or MRS person, then a JSON response
     * of true or false is returned to Verboice
     */
    @ResponseBody
    @RequestMapping("/authenticate")
    public String authenticate(HttpServletRequest request) {
        logger.info("Authenticating pin...");

        String pin = request.getParameter("pin");
        String callId = request.getParameter(MotechConstants.MOTECH_CALL_ID);

        if (flowSessionHandler.digitsMatchPatientPin(callId, pin)) {
            return "{\"result\": \"true\"}";
        } else {
            return "{\"result\": \"false\"}";
        }
    }
}
