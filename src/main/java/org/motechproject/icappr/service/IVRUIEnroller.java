package org.motechproject.icappr.service;

import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.domain.IVREnrollmentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUIEnroller {
    
    private final CallInitiationService callService;
    
    private Logger logger = LoggerFactory.getLogger("motech-icappr");
	
    @Autowired
    public IVRUIEnroller(CallInitiationService callService) {
        this.callService = callService;
    }

    /**
     * Enrolls a person in the test calls. The request contains the phone
     * number, pin, motech ID, and call start time.
     */
    public IVREnrollmentResponse enrollPerson(IVREnrollmentRequest request) {
        IVREnrollmentResponse response = new IVREnrollmentResponse();

        String phoneNum = request.getPhonenumber();
        if (phoneNum == null) {
            response.addError("Phone Number with digits: " + request.getPhonenumber() + " not found.");
            return response;
        }

        String pin = request.getPin();
        if (pin == null) {
            response.addError("Pin with digits: " + request.getPin() + " not found.");
            return response;
        }
        
        String motechID = request.getMotechId();
        if (motechID == null) {
            response.addError("Motech ID with digits: " + request.getMotechId() + " was not found.");
            return response;
        }
        
        String language = request.getLanguage();
        if (language == null) {
            response.addError("Language " + language + " was not found.");
            return response;
        }
        
    	logger.debug("Initiating IVR UI Enrollment call with phone " + phoneNum + " and language " + language);

        String actualStartTime = request.getCallStartTime();
        response.setStartTime(actualStartTime);

        callService.initiateCall(request);

        return response;
    }

}
