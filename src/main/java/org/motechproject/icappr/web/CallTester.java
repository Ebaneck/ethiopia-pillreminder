package org.motechproject.icappr.web;

import javax.servlet.http.HttpServletRequest;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.service.CallInitiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CallTester {

    @Autowired
    private CallInitiationService callService;

    @RequestMapping("/sideeffects")
    @ResponseBody
    public String testSideEffects(HttpServletRequest request) {
        String phoneNumber = request.getParameter("phone");

        Request pinRequest = new Request();
        pinRequest.setLanguage("english");
        pinRequest.setMotechId("1984");
        pinRequest.setPhoneNumber(phoneNumber);

        callService.initiateCall(pinRequest);

        return "success";
    }
}
