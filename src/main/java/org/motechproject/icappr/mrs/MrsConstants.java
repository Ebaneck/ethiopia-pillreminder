package org.motechproject.icappr.mrs;

/**
 * This is a listing of constants that will remain consistent
 * throughout the application and be stored in CouchDB.
 */
public interface MrsConstants {

    final String PERSON_PHONE_NUMBER_ATTR = "Phone Number";
    final String PERSON_PIN_ATTR = "Pin";
    final String PERSON_LANGUAGE_ATTR = "Language";
    final String PERSON_NUM_PIN_ATTEMPTS = "NumPinAttempts";
    final String LOGIN_FAILURE_ATTR = "FailedLogins";

    final String DUMMY_PERSON_ATTR = "DemoPerson";

    final String IPT_INITIATION_DATE = "ipt_initiation_date";
    final String STUDY_SITE = "study_site";
    final String PATIENT_MRN = "mrn";
    final String DAY_ENROLLED = "dayEnrolled";

}
