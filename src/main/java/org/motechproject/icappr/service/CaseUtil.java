package org.motechproject.icappr.service;

import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.icappr.constants.CaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaseUtil {
    
    @Autowired
    private CommcareCaseService commcareCaseService;
    
    public CaseInfo getCaseInfo(String caseId){
        return commcareCaseService.getCaseByCaseId(caseId);
    }
    
    public String getPinFromCase(String caseId){
        return getValueForProperty(CaseConstants.CASE_PIN, caseId);
    }
    
    public String getClinicFromCase(String caseId){
        return getValueForProperty(CaseConstants.CASE_CLINIC_ID, caseId);
    }
    
    public String getPreferredLanguageFromCase(String caseId){
        return getValueForProperty(CaseConstants.CASE_PREFERRED_LANGUAGE, caseId);
    }
    
    private String getValueForProperty(String property, String caseId){
        return this.getCaseInfo(caseId).getFieldValues().get(property);
    }

}
