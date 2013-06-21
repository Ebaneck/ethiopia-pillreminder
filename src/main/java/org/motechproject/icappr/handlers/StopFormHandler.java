package org.motechproject.icappr.handlers;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.icappr.constants.MotechConstants;
import org.motechproject.icappr.events.Events;
import org.motechproject.icappr.support.SchedulerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopFormHandler {

    @Autowired
    private SchedulerUtil schedulerUtil;

    @Autowired
    private EventRelay eventRelay;

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public void handleForm(CommcareForm form, String externalId) {
        logger.debug("Handling Stop form for patient: " + externalId);

        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            return;
        }

        String stopDate = getValue(topFormElement, MotechConstants.STOP_DATE);
        String stopReason =  getValue(topFormElement, MotechConstants.STOP_REASON);    //"opt_out" or "ipt_completion"

        DateTime stopDateTime = DateTime.parse(stopDate);

        if (stopDateTime.isBeforeNow()) {
            schedulerUtil.scheduleEndEvent(externalId, DateTime.now().plusMinutes(2), stopReason);
        } else {
            schedulerUtil.scheduleEndEvent(externalId, DateTime.parse(stopDate), stopReason);
        }

        MotechEvent stopRequest = new MotechEvent(Events.STOP_REQUEST);
        stopRequest.getParameters().put(MotechConstants.MOTECH_ID, externalId);
        stopRequest.getParameters().put(MotechConstants.STOP_DATE, stopDate);
        stopRequest.getParameters().put(MotechConstants.STOP_REASON, stopReason);

        eventRelay.sendEventMessage(stopRequest);
    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElement(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
