package org.motechproject.icappr.handlers;

import java.util.Map;
import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.icappr.constants.CaseConstants;
import org.motechproject.icappr.support.SchedulerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StopFormHandler {

    @Autowired
    private SchedulerUtil schedulerUtil;

    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    public void handleForm(CommcareForm form, String externalId) {
        logger.debug("Handling Stop form...");

        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            return;
        }

        String stopDate = getValue(topFormElement, "stop_date");
        String stopReason =  getValue(topFormElement, "stop_reason");    //"opt_out" or "ipt_completion"

        DateTime stopDateTime = DateTime.parse(stopDate);

        if (stopDateTime.isBeforeNow()) {
            schedulerUtil.scheduleEndEvent(externalId, DateTime.now().plusMinutes(2), stopReason);
        } else {
            schedulerUtil.scheduleEndEvent(externalId, DateTime.parse(stopDate), stopReason);
        }
    }

    private String getValue(FormValueElement formElement, String elementName) {

        FormValueElement clinicElement = formElement.getElementByName(elementName);

        if (clinicElement == null) {
            return null;
        }

        return clinicElement.getValue();
    }
}
