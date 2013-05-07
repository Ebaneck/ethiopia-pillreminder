package org.motechproject.icappr.listener;

import static org.mockito.MockitoAnnotations.initMocks;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.event.MotechEvent;

public class CommcareStubFormListenerTest {

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
