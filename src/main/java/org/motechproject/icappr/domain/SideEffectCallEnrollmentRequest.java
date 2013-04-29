package org.motechproject.icappr.domain;

import org.springframework.beans.factory.annotation.Autowired;

public class SideEffectCallEnrollmentRequest extends Request {

    @Autowired
    public SideEffectCallEnrollmentRequest(){
        setType(RequestTypes.SIDE_EFFECT_CALL);
    }
    
}
