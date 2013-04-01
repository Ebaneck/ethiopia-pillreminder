package org.motechproject.icappr.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.joda.time.DateTime;
import org.motechproject.appointments.api.service.AppointmentService;
import org.motechproject.appointments.api.service.contract.CreateVisitRequest;
import org.motechproject.appointments.api.service.contract.ReminderConfiguration;
import org.motechproject.appointments.api.service.contract.ReminderConfiguration.IntervalUnit;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.couch.mrs.model.CouchPerson;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.couchdb.CouchMrsConstants;
import org.motechproject.icappr.couchdb.CouchPersonUtil;
import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.mrs.MRSConstants;
import org.motechproject.icappr.openmrs.OpenMRSUtil;
import org.motechproject.icappr.service.IVRUIEnroller;
import org.motechproject.icappr.support.CallRequestDataKeys;
import org.motechproject.icappr.support.IVRUIDecisionTreeBuilder;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CallTester {

    @Autowired
    private IVRService ivrService;

    @Autowired
    private PillReminderSettings pillReminderSettings;
    
    @Autowired
    private CouchPersonUtil couchPersonUtil;
    
    @Autowired
    private IVRUIDecisionTreeBuilder ivrUIDecisionTreeBuilder;
    
    @Autowired
    private IVRUIEnroller enroller;



    @RequestMapping("/testcall")
    @ResponseBody
    public String testApt() {
                
        String phoneNumber = "12074509521";

        CallRequest callRequest = new CallRequest(phoneNumber, 120, pillReminderSettings.getVerboiceChannelName());

        Map<String, String> payload = callRequest.getPayload();


        String language = "english";
        
        String pin = "123";

        CouchPerson person = couchPersonUtil.createAndSavePerson(phoneNumber, pin, language);
        
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

    private void enrollInCalls(CouchPerson person) {
        IVREnrollmentRequest request = new IVREnrollmentRequest();
        String language = couchPersonUtil.getAttribute(person, CouchMrsConstants.LANGUAGE).getValue();
        request.setLanguage(language);
        request.setPhoneNumber(couchPersonUtil.getAttribute(person, CouchMrsConstants.PHONE_NUMBER).getValue());
        request.setPin(couchPersonUtil.getAttribute(person, CouchMrsConstants.PERSON_PIN).getValue());
        request.setMotechID(person.getId());
        DateTime dateTime = DateUtil.now().plusMinutes(2);
        request.setCallStartTime(String.format("%02d:%02d",
                dateTime.getHourOfDay(), dateTime.getMinuteOfHour()));
        ivrUIDecisionTreeBuilder.setLanguage(language);
        ivrUIDecisionTreeBuilder.buildTree();
        enroller.enrollPerson(request);
    }
    
}
