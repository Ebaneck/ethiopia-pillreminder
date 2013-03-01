package org.motechproject.icappr.listener;

import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.icappr.service.IVRUIEnroller;

public class CommcareStubFormListenerTest {

    @Mock
    IVRUIEnroller enroller;

    CommcareStubFormListener commcareStubFormListener;

    @Before
    public void setUp() {
        initMocks(this);
        commcareStubFormListener = new CommcareStubFormListener();
    }

    @Test
    public void shouldDelegateFormHandling() {
        MotechEvent event = new MotechEvent();
        Map<String, Object> params = event.getParameters();

        commcareStubFormListener.handleStubForm(event);

    }
   

}
