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

    @RequestMapping("/testcall")
    @ResponseBody
    public String testApt(HttpServletRequest request) {

        String phoneNumber = request.getParameter("phone");

        CallRequest callRequest = new CallRequest(phoneNumber, 120, pillReminderSettings.getVerboiceChannelName());

        Map<String, String> payload = callRequest.getPayload();


        String language = "english";

        String pin = "1234";

        MRSPersonDto person = mrsPersonUtil.createAndSavePerson(phoneNumber, pin, language);

        String callbackUrl = pillReminderSettings.getMotechUrl() + "/module/icappr/campaign-message?language=%s";

        String statusUrl = pillReminderSettings.getMotechUrl() + "/module/verboice/ivr/callstatus";

        try {
            payload.put(CallRequestDataKeys.CALLBACK_URL, 
                    URLEncoder.encode(String.format(callbackUrl, language), "UTF-8"));
            payload.put(CallRequestDataKeys.STATUS_CALLBACK_URL,
                    URLEncoder.encode(String.format(statusUrl, language), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        enrollInCalls(person);
        //ivrService.initiateCall(callRequest);

        return "Success";
    }

    private void enrollInCalls(MRSPersonDto person) {
        IVREnrollmentRequest request = new IVREnrollmentRequest();
        String language = mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_LANGUAGE_ATTR).getValue();
        request.setLanguage(language);
        request.setPhoneNumber(mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_PHONE_NUMBER_ATTR).getValue());
        request.setPin(mrsPersonUtil.getAttribute(person, MrsConstants.PERSON_PIN_ATTR).getValue());
        request.setMotechID(person.getPersonId());
        DateTime dateTime = DateUtil.now().plusMinutes(2);
        request.setCallStartTime(String.format("%02d:%02d",
                dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));
        enroller.enrollPerson(request);
    }

}
