package org.motechproject.icappr.listener;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.service.MessageCampaignEnroller;
import org.motechproject.icappr.support.SchedulerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndProgramListener {

    @Autowired
    private SchedulerUtil schedulerUtil;

    @Autowired
    private MessageCampaignEnroller messageCampaignEnroller;


    @MotechListener(subjects = Events.END_CALLS )
    public void endCalls(MotechEvent event) {
        String motechId = (String) event.getParameters().get(MotechConstants.MOTECH_ID);

        messageCampaignEnroller.unenroll(motechId);
        schedulerUtil.unscheduleAllIcapprJobs(motechId);
    }
}
