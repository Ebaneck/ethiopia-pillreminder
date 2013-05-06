package org.motechproject.icappr.listener;

import org.motechproject.callflow.domain.FlowSessionRecord;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RAMessageHandler {

    @Autowired
    private FlowSessionService flowSessionService;
    
    @Autowired
    private MRSPatientAdapter patientAdapter;
    
    @MotechListener( subjects = {Events.SEND_RA_MESSAGE_ADHERENCE_CONCERNS, Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS} )
    public void sendRAMessage(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get("flowSessionId");
        FlowSessionRecord flowSession = (FlowSessionRecord) flowSessionService.getSession(flowSessionId);
        if (flowSession != null) {
            String motechId = flowSession.get("motechId");
            MRSPatient patient = patientAdapter.getPatientByMotechId(motechId);
            if (patient != null) {
                MRSPerson person = patient.getPerson();
                String phoneNumber = MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PHONE_NUMBER_ATTR, patient.getPerson().getAttributes());
                buildThenSendMessage(phoneNumber);
            }
        }
    }

    private void buildThenSendMessage(String phoneNumber) {
        // This is where the RA logic should go
        
    }
}
