package org.motechproject.icappr.listener;

import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.domain.CommcareCaseMapper;
import org.motechproject.icappr.domain.PillReminderRegistrar;
import org.motechproject.icappr.domain.PillReminderRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommcareCaseListener {

    private PillReminderRegistrar pillReminderRegistrar;

    @Autowired
    public CommcareCaseListener(PillReminderRegistrar pillReminderRegistrar) {
        this.pillReminderRegistrar = pillReminderRegistrar;
    }

    @MotechListener(subjects = EventSubjects.CASE_EVENT)
    public void handle(MotechEvent event) {
        CaseEvent caseEvent = new CaseEvent(event);
        CommcareCaseMapper mapper = new CommcareCaseMapper(caseEvent);
        PillReminderRegistration registration = mapper.toPillReminderRegistration();
        pillReminderRegistrar.register(registration);
    }
}
