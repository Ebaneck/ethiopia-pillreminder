package org.motechproject.icappr.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.domain.PillReminderCallEnrollmentRequest;
import org.motechproject.icappr.domain.Request;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.service.CallInitiationService;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendCampaignMessageListener {

    private Logger logger = LoggerFactory.getLogger("motech-icappr");
	private MRSPatientAdapter patientAdapter;
	private CallInitiationService callService;
	
	@Autowired
	private EventRelay eventRelay;

	@Autowired
	public SendCampaignMessageListener(MRSPatientAdapter patientAdapter, CallInitiationService callService) {
		this.patientAdapter = patientAdapter;
		this.callService = callService;
	}

	@MotechListener(subjects = { EventKeys.SEND_MESSAGE })
	public void sendCampaignMessage(MotechEvent event) {
	    logger.debug("received message campaign event");
		String patientId = event.getParameters().get(EventKeys.EXTERNAL_ID_KEY).toString();
		MRSPatient patient = patientAdapter.getPatientByMotechId(patientId);

		String phoneNumber = MRSPersonUtil.getAttrValue(MrsConstants.PERSON_PHONE_NUMBER_ATTR, patient.getPerson()
				.getAttributes());

		String language = MRSPersonUtil.getAttrValue(MrsConstants.PERSON_LANGUAGE_ATTR, patient.getPerson()
                .getAttributes());

		Request request = new PillReminderCallEnrollmentRequest();
		request.setLanguage(language);
		request.setMotechID(patientId);
		request.setPhoneNumber(phoneNumber);

		try {
			callService.initiateCall(request);
		} catch (Exception e) {
			MotechEvent exceptionEvent = new MotechEvent("icappr.failed.verboice.call");
			exceptionEvent.getParameters().put("exception_message", e.getMessage());
			exceptionEvent.getParameters().put("exception_type", e.getClass().getName());
			eventRelay.sendEventMessage(exceptionEvent);
		}
	}
}
