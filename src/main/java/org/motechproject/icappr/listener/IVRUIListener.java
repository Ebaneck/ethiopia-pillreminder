package org.motechproject.icappr.listener;

import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.service.CallInitiationService;
import org.motechproject.icappr.support.DecisionTreeSessionHandler;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IVRUIListener {

	private DecisionTreeSessionHandler decisionTreeSessionHandler;
	private CallInitiationService callInitiationService;
	
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

	@Autowired
	public IVRUIListener(DecisionTreeSessionHandler decisionTreeSessionHandler,
			CallInitiationService callInitiationService) {
		this.decisionTreeSessionHandler = decisionTreeSessionHandler;
		this.callInitiationService = callInitiationService;
	}

	@MotechListener(subjects = Events.PATIENT_SELECTED_CONTINUE)
	public void handleContinueSelection(MotechEvent event) {
        logger.debug("Patient selected to continue phone calls.");
	    IVREnrollmentRequest request = new IVREnrollmentRequest();
        
		String sessionId = (event.getParameters().get("flowSessionId")
				.toString());

		String motechId = decisionTreeSessionHandler
				.getMotechIdForSessionWithId(sessionId);
		
		String phoneNum = decisionTreeSessionHandler
				.getPhoneNumForSessionWithId(sessionId);
		
		String language = decisionTreeSessionHandler
                .getLanguageForSessionWithId(sessionId);
		
        request.setLanguage(language);
        request.setPhoneNumber(phoneNum);
        request.setMotechID(motechId);

		/*We wait one minute before we initiate the next phone call	*/ 
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		callInitiationService.initiateCall(request);
	}

}
