package org.motechproject.icappr.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.events.Events;


public class SideEffectListener {

    @MotechListener(subjects = {Events.YES_YELLOW_SKIN_OR_EYES, Events.YES_SKIN_RASH_OR_ITCHY_SKIN, Events.YES_ABDOMINAL_PAIN_OR_VOMITING, Events.TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET } )
    public void handleSideEffectEvents(MotechEvent event) {
        String flowSessionId = "id";
    }
}
