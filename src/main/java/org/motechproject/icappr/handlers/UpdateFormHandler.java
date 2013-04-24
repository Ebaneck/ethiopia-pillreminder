package org.motechproject.icappr.handlers;

import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;

public class UpdateFormHandler {

    public void handleForm(CommcareForm form) {
        FormValueElement topFormElement = form.getForm();

        if (topFormElement == null) {
            return;
        }
    }
}
