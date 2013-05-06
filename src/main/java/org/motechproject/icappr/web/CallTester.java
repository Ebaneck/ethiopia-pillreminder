package org.motechproject.icappr.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.domain.SideEffectCallEnrollmentRequest;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.service.CallInitiationService;
import org.motechproject.icappr.service.IVRUIEnroller;
import org.motechproject.icappr.support.CallRequestDataKeys;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.mrs.model.MRSPersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CallTester {

    @Autowired
    private PillReminderSettings pillReminderSettings;

    @Autowired
    private MRSPersonUtil mrsPersonUtil;

    @Autowired
    private IVRUIEnroller enroller;

    @Autowired
    private CallInitiationService callService;

    @RequestMapping("/sideeffects")
    @ResponseBody
    public String testSideEffects(HttpServletRequest request) {
        String phoneNumber = request.getParameter("phone");

        Request pinRequest = new SideEffectCallEnrollmentRequest();
        pinRequest.setLanguage("english");
        pinRequest.setMotechID("1984");
        pinRequest.setPhoneNumber(phoneNumber);
        pinRequest.setPin("1234");

        callService.initiateCall(pinRequest);

        return "success";
    }
}
