package org.motechproject.icappr.domain;

import java.util.Map;

import org.motechproject.commcare.events.CaseEvent;

public class CommcareCaseMapper {

    private CaseEvent caseEvent;

    public CommcareCaseMapper(CaseEvent caseEvent) {
        this.caseEvent = caseEvent;
    }

    public PillReminderRegistration toPillReminderRegistration() {
        PillReminderRegistration registration = new PillReminderRegistration();
        Map<String, String> caseValues = caseEvent.getFieldValues();
        
        registration.setClinic(caseValues.get("clinic_id"));
        registration.setPatientId(caseValues.get("patient_number"));
        registration.setPhoneNumber(caseValues.get("phone_number"));
        registration.setPin(caseValues.get("pin"));
        registration.setNextCampaign(caseValues.get("pref_medication_call_freq"));
        return registration;
    }

}
