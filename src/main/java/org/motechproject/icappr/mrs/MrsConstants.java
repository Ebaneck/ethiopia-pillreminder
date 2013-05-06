package org.motechproject.icappr.mrs;

/**
 * This is a listing of constants that will remain consistent
 * throughout the application and be stored in CouchDB.
 */
public interface MrsConstants {

    final String PILL_TAKEN_CONCEPT_NAME = "PILL TAKEN";

    final String PILL_TAKEN_CONCEPT_YES_ANSWER = "TRUE";
    final String PILL_TAKEN_CONCEPT_NO_ANSWER = "FALSE";

    final String PILL_REMINDER_ENCOUNTER_TYPE = "PILL REMINDER";

    final String PERSON_PHONE_NUMBER_ATTR = "Phone Number";
    final String PERSON_PIN_ATTR = "Pin";
    final String PERSON_LANGUAGE_ATTR = "Language";
    final String PERSON_NUM_PIN_ATTEMPTS = "NumPinAttempts";
	final String PERSON_NEXT_CAMPAIGN_ATTR = "NextCampaignAttr";
    final String LOGIN_FAILURE_ATTR = "FailedLogins";

    final String DUMMY_PERSON_ATTR = "DemoPerson";

}
