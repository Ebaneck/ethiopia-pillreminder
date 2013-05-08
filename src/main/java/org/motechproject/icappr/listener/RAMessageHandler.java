package org.motechproject.icappr.listener;

import org.joda.time.DateTime;
import org.motechproject.callflow.domain.FlowSessionRecord;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.constants.MotechConstants;
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

    @Autowired
    private EventRelay eventRelay;

    @MotechListener( subjects = {Events.SEND_RA_MESSAGE_ADHERENCE_CONCERNS, Events.SEND_RA_MESSAGE_APPOINTMENT_CONCERNS} )
    public void sendRAMessage(MotechEvent event) {
        String flowSessionId = (String) event.getParameters().get("flowSessionId");
        FlowSessionRecord flowSession = (FlowSessionRecord) flowSessionService.getSession(flowSessionId);
        if (flowSession != null) {
            String motechId = flowSession.get(MotechConstants.MOTECH_ID);
            MRSPatient patient = patientAdapter.getPatientByMotechId(motechId);
            if (patient != null) {
                String phoneNumber = MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PHONE_NUMBER_ATTR, patient.getPerson().getAttributes());
                buildThenSendMessage(phoneNumber, patient.getMotechId(), event.getSubject());
            }
        }
    }

    private void buildThenSendMessage(String phoneNumber, String motechId, String concernType) {

        MotechEvent event = new MotechEvent(Events.CONCERN_EVENT);

        event.getParameters().put(MotechConstants.PHONE_NUM, phoneNumber);
        event.getParameters().put(MotechConstants.MOTECH_ID, motechId);
        event.getParameters().put(MotechConstants.CONCERN_TYPE, concernType);
        event.getParameters().put(MotechConstants.CONCERN_TIME, DateTime.now().toString());

        eventRelay.sendEventMessage(event);
    }
}
