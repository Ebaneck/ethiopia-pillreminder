package org.motechproject.icappr.support;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.server.service.FlowSessionService;
import org.motechproject.icappr.couchdb.CouchMrsConstants;
import org.motechproject.icappr.couchdb.CouchPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.mrs.MrsEntityFacade;
import org.motechproject.mrs.domain.Attribute;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.couch.mrs.model.CouchPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DecisionTreeSessionHandler {

    private final MrsEntityFacade mrsEntityFacade;
    private final FlowSessionService flowSessionService;
    private final CouchPersonUtil couchPersonUtil;

    @Autowired
    public DecisionTreeSessionHandler(MrsEntityFacade mrsEntityFacade, FlowSessionService flowSessionService, CouchPersonUtil couchPersonUtil) {
        this.mrsEntityFacade = mrsEntityFacade;
        this.flowSessionService = flowSessionService;
        this.couchPersonUtil = couchPersonUtil;
    }

    public boolean updateFlowSessionIdToVerboiceId(String oldSessionId, String newSessionId) {
        FlowSession session = flowSessionService.getSession(oldSessionId);
        if (session == null) {
            return false;
        }

        flowSessionService.updateSessionId(oldSessionId, newSessionId);
        return true;
    }
    
    public boolean digitsMatchCouchPersonPin(String sessionId, String digits) {
        String motechId = getMotechIdForSessionWithId(sessionId);
        CouchPerson person = couchPersonUtil.getPersonByID(motechId);
        String pin = readPinAttributeForCouchPerson(person);

        if (StringUtils.isNotBlank(digits) && digits.equals(pin)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean digitsMatchPatientPin(String sessionId, String digits) {
        String motechId = getMotechIdForSessionWithId(sessionId);
        Patient patient = mrsEntityFacade.findPatientByMotechId(motechId);
        String pin = readPinAttributeValue(patient);

        if (StringUtils.isNotBlank(digits) && digits.equals(pin)) {
            return true;
        } else {
            return false;
        }
    }

    private String readPinAttributeValue(Patient patient) {
        List<Attribute> attrs = patient.getPerson().getAttributes();
        String pin = null;
        for (Attribute attr : attrs) {
            if (MrsConstants.MRS_PIN_ATTR.equals(attr.getName())) {
                pin = attr.getValue();
            }
        }
        return pin;
    }
    
    private String readPinAttributeForCouchPerson(CouchPerson person){
        List<Attribute> attrs = person.getAttributes();
        String pin = null;
        for (Attribute attr : attrs) {
            if (CouchMrsConstants.PERSON_PIN.equals(attr.getName())) {
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

}
