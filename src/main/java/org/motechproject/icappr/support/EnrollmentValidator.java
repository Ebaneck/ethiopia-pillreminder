package org.motechproject.icappr.support;

import org.joda.time.DateTime;
import org.motechproject.icappr.mrs.MRSPersonUtil;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.MRSPatient;

public final class EnrollmentValidator {

    private static final int ONE_MONTH = 1;

    public static boolean patientCanUpdateReminderFrequency(MRSPatient patient, DateTime updateTime) {

        String value = MRSPersonUtil.getAttrValue(MrsConstants.DAY_ENROLLED, patient.getPerson().getAttributes());

        DateTime dateFirstEnrolled = DateTime.parse(value);

        if (dateFirstEnrolled.plusMonths(ONE_MONTH).isBefore(updateTime)) {
            return false;
        }

        return true;
    }
}
