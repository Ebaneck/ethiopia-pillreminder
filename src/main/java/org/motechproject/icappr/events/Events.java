package org.motechproject.icappr.events;

public interface Events {
    public static final String BASE_SUBJECT = "org.motechproject.icappr";

    public static final String PATIENT_SELECTED_CONTINUE = BASE_SUBJECT + "PatientSelectedContinue";

    public static final String PATIENT_SELECTED_STOP = BASE_SUBJECT + "PatientSelectedStop";

    public static final String PATIENT_WANTS_CLINIC_CALL = BASE_SUBJECT + "PatientSelectedClinicCall";

    public static final String PATIENT_SELECTED_END_PILL_REMINDER_CALL = BASE_SUBJECT + "PatientSelectedStopPillReminderCampaign";
}
