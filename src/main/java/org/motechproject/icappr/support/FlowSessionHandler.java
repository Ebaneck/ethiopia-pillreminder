package org.motechproject.icappr.support;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.icappr.constants.CallRequestDataKeys;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.mrs.MrsEntityFacade;
import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.model.MRSAttributeDto;
import org.motechproject.mrs.model.MRSPersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowSessionHandler {

    private final MrsEntityFacade mrsEntityFacade;
    private final FlowSessionService flowSessionService;
    private final MRSPersonUtil mrsPersonUtil;

    @Autowired
    public FlowSessionHandler(MrsEntityFacade mrsEntityFacade, FlowSessionService flowSessionService, MRSPersonUtil mrsPersonUtil) {
        this.mrsEntityFacade = mrsEntityFacade;
        this.flowSessionService = flowSessionService;
        this.mrsPersonUtil = mrsPersonUtil;
    }

    public boolean updateFlowSessionIdToVerboiceId(String oldSessionId, String newSessionId) {
        FlowSession session = flowSessionService.getSession(oldSessionId);
        if (session == null) {
            return false;
        }

        flowSessionService.updateSessionId(oldSessionId, newSessionId);
        return true;
    }

    public boolean digitsMatchPersonPin(String sessionId, String digits) {
        String motechId = getMotechIdForSessionWithId(sessionId);
        MRSPerson person = mrsPersonUtil.getPersonByID(motechId);
        String pin = readPinAttributeForPerson(person);

        if (StringUtils.isNotBlank(digits) && digits.equals(pin)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean digitsMatchPatientPin(String sessionId, String digits) {
        String motechId = getMotechIdForSessionWithId(sessionId);
        MRSPatient patient = mrsEntityFacade.findPatientByMotechId(motechId);
        String pin;
        if (patient == null) {
            MRSPerson person = mrsPersonUtil.getPersonByID(motechId);
            if (person != null) {
                pin = readPinAttributeForPerson(person);
            } else {
                return false;
            }
        } else {
            pin = readAttributeValue(MrsConstants.PERSON_PIN_ATTR, patient);
        }

        if (StringUtils.isNotBlank(digits) && digits.equals(pin)) {
            return true;
        } else {
            return false;
        }
    }

    private String readAttributeValue(String attributeName, MRSPatient patient) {
        List<MRSAttribute> attrs = patient.getPerson().getAttributes();
        String attributeValue = null;
        for (MRSAttribute attr : attrs) {
            if (attributeName.equals(attr.getName())) {
                attributeValue = attr.getValue();
            }
        }
        return attributeValue;
    }

    private MRSAttribute readAttribute(String attributeName, MRSPatient patient) {
        List<MRSAttribute> attrs = patient.getPerson().getAttributes();

        for (MRSAttribute attr : attrs) {
            if (attributeName.equals(attr.getName())) {
                return attr;
            }
        }
        return null;
    }

    private String readPinAttributeForPerson(MRSPerson person){
        List<MRSAttribute> attrs = person.getAttributes();
        String pin = null;
        for (MRSAttribute attr : attrs) {
            if (MrsConstants.PERSON_PIN_ATTR.equals(attr.getName())) {
                pin = attr.getValue();
            }
        }
        return pin;
    }

    public String getMotechIdForSessionWithId(String sessionId) {
        FlowSession session = flowSessionService.getSession(sessionId);
        return session.get(CallRequestDataKeys.MOTECH_ID);
    }

    public String getPhoneNumForSessionWithId(String sessionId) {
        FlowSession session = flowSessionService.getSession(sessionId);
        return session.getPhoneNumber();
    }

    public String getLanguageForSessionWithId(String sessionId) {
        FlowSession session = flowSessionService.getSession(sessionId);
        return session.getLanguage();
    }

    public void updatePatientFailedLogin(String sessionId) {
        String motechId = getMotechIdForSessionWithId(sessionId);
        MRSPatient patient = mrsEntityFacade.findPatientByMotechId(motechId);
        MRSAttribute loginFailures = readAttribute(MrsConstants.LOGIN_FAILURE_ATTR, patient);
        if (loginFailures == null) {
            MRSAttribute numFailures = new MRSAttributeDto(MrsConstants.LOGIN_FAILURE_ATTR, "1");
            patient.getPerson().getAttributes().add(numFailures);
            mrsEntityFacade.savePatient(patient);
        } else {
            Integer numFailures = Integer.parseInt(loginFailures.getValue());
            numFailures++;
            loginFailures.setValue(numFailures.toString());
            mrsEntityFacade.savePatient(patient);
        }
    }

    public void updatePatientAttribute(MRSAttribute attribute, MRSPatient patient) {
        List<MRSAttribute> attrs = patient.getPerson().getAttributes();
    }

}
