package org.motechproject.icappr.events;

public interface Events {
    public static final String BASE_SUBJECT = "org.motechproject.icappr.";

    public static final String PATIENT_SELECTED_CONTINUE = BASE_SUBJECT + "PatientSelectedContinue";

    public static final String PATIENT_SELECTED_STOP = BASE_SUBJECT + "PatientSelectedStop";

    public static final String PATIENT_SELECTED_END_PILL_REMINDER_CALL = BASE_SUBJECT + "PatientSelectedStopPillReminderCampaign";

    public static final String YES_YELLOW_SKIN_OR_EYES = BASE_SUBJECT + "PatientSelectedYesYellowSkinOrEyes";

    public static final String YES_ABDOMINAL_PAIN_OR_VOMITING = BASE_SUBJECT + "PatientSelectedYesAbdominalPainOrVomiting";

    public static final String YES_SKIN_RASH_OR_ITCHY_SKIN = BASE_SUBJECT + "PatientSelectedYesSkinRashOrItchySkin";

    public static final String TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET = BASE_SUBJECT + "PatientSelectedYesTinglingOrNumbnessOfHandsOrFeet";

    public static final String NO_YELLOW_SKIN_OR_EYES = BASE_SUBJECT + "PatientSelectedNoYellowSkinOrEyes";

    public static final String NO_ABDOMINAL_PAIN_OR_VOMITING = BASE_SUBJECT + "PatientSelectedNoAbdominalPainOrVomiting";

    public static final String NO_SKIN_RASH_OR_ITCHY_SKIN = BASE_SUBJECT + "PatientSelectedNoSkinRashOrItchySkin";

    public static final String NO_TINGLING_OR_NUMBNESS_OF_HANDS_OR_FEET = BASE_SUBJECT + "PatientSelectedNoTinglingOrNumbnessOfHandsOrFeet";

    public static final String SEND_RA_MESSAGE_ADHERENCE_CONCERNS = BASE_SUBJECT + "PatientConcernsAdherence";

    public static final String SEND_RA_MESSAGE_APPOINTMENT_CONCERNS = BASE_SUBJECT + "PatientConcernsAppointment";

    public static final String APPOINTMENT_SCHEDULE_CALL = BASE_SUBJECT + "AppointmentCall";

    public static final String SECOND_APPOINTMENT_SCHEDULE_CALL = BASE_SUBJECT + "SecondAppointmentCall";

    public static final String ADHERENCE_ASSESSMENT_CALL = BASE_SUBJECT + "AdherenceAssessmentCall";

    public static final String PILL_REMINDER_CALL = BASE_SUBJECT + "PillReminderCall";

    public static final String SIDE_EFFECTS_SURVEY_CALL = BASE_SUBJECT + "SideEffectsSurveyCall";

    public static final String YES_MEDICATION_YESTERDAY = BASE_SUBJECT + "YesMedicationYesterday";

    public static final String YES_MEDICATION_TWO_DAYS_AGO = BASE_SUBJECT + "YesMedicationTwoDaysAgo";

    public static final String YES_MEDICATION_THREE_DAYS_AGO = BASE_SUBJECT + "YesMedicationThreeDaysAgo";

    public static final String NO_MEDICATION_YESTERDAY = BASE_SUBJECT + "NoMedicationYesterday";

    public static final String NO_MEDICATION_TWO_DAYS_AGO = BASE_SUBJECT + "NoMedicationTwoDaysAgo";

    public static final String NO_MEDICATION_THREE_DAYS_AGO = BASE_SUBJECT + "NoMedicationThreeDaysAgo";

    public static final String INPUT_ERROR_EVENT = BASE_SUBJECT + "InputError";

    public static final String END_CALLS = BASE_SUBJECT + "EndOfProgramRequest";

    public static final String CONCERN_EVENT = BASE_SUBJECT + "RAMessageConcern";

    public static final String NO_ADHERENCE_CONCERNS = BASE_SUBJECT + "NoAdherenceConcern";

    public static final String NO_APPOINTMENT_CONCERNS = BASE_SUBJECT + "NoAppointmentConcern";

    public static final String FAILED_PIN = BASE_SUBJECT + "FailedPin";

    public static final String REPORT_EVENT = BASE_SUBJECT + "ReportGenerationEvent";

    //This event is for reporting purposes only (the other stop request event is fired upon the end of the patient's program)
    public static final String STOP_REQUEST = BASE_SUBJECT + "Reporting.StopRequest";

    public static final String PIN_FAILURE = BASE_SUBJECT + "PinFailure";
}
