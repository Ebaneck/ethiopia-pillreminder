package org.motechproject.icappr.service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.initMocks;
import org.motechproject.icappr.PillReminderSettings;
import org.motechproject.icappr.domain.IVREnrollmentRequest;
import org.motechproject.icappr.domain.RequestTypes;
import org.motechproject.ivr.service.IVRService;

public class IVRUIEnrollerTest {
	
	@Mock
	CallInitiationService ciService;
	@Mock
	IVRService ivrService;
	@Mock
	PillReminderSettings pillReminderSettings;
	
	IVRUIEnroller ivrUIEnroller;
	
	private final IVREnrollmentRequest request = new IVREnrollmentRequest();
	
	@Before
    public void setUp() {
		initMocks(this);
		ivrUIEnroller = new IVRUIEnroller(ciService);
        request.setMotechID("123");
        request.setCallStartTime("10:30");
        request.setPhoneNumber("1234567890");
        request.setPin("0000");        
    }

	@Test
	public void typeShouldBeIVRUIUponEnrollment() {
		ivrUIEnroller.enrollPerson(request);
		assertEquals(request.getType(), RequestTypes.IVR_UI);
	}

}
