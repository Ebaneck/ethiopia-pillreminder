package org.motechproject.icappr.support;

import org.joda.time.DateTime;
import org.motechproject.commcare.domain.UpdateTask;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.MRSPatient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EnrollmentValidator {

    private static final int DAYS = 20;

    private static Logger logger = LoggerFactory.getLogger("motech-icappr");

    public static boolean patientCanUpdateReminderFrequency(MRSPatient patient, DateTime updateTime) {

        String value = MRSPersonUtil.getAttrValue(MrsConstants.DAY_ENROLLED, patient.getPerson().getAttributes());

        logger.debug("Check update allowed for patient:" + patient.getMotechId());

        if (null == value) {
            // default: old patients may be updated
            return true;
        }

        DateTime dateFirstEnrolled = DateTime.parse(value);

        if (null == dateFirstEnrolled) {
            // default: patient may be updated
            return true;
        }

        DateTime updateThreshold = dateFirstEnrolled.plusDays(DAYS);
        boolean canUpdate = updateThreshold.isBefore(updateTime);
        logger.debug("Update allowed after: " + updateThreshold + ".  Now it is " + updateTime
                + ". So update allowed is: " + canUpdate);
        return canUpdate;
    }
}
