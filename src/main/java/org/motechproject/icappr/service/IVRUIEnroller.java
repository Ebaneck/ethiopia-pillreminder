package org.motechproject.icappr.service;

import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.domain.IVREnrollmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUIEnroller {
    
    private final CallService callService;
    
    @Autowired
    public IVRUIEnroller(CallService callService) {
        this.callService = callService;
    }

    /*
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
        
        String motechID = request.getMotechID();
        if (motechID == null) {
            response.addError("Motech ID with digits: " + request.getMotechID() + " was not found.");
            return response;
        }

        String actualStartTime = request.getCallStartTime();
        response.setStartTime(actualStartTime);

        callService.initiateCall(motechID, phoneNum);

        return response;
    }


}
