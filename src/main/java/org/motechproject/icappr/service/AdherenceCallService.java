package org.motechproject.icappr.service;

import org.motechproject.icappr.domain.AdherenceCallResponse;

public interface AdherenceCallService {

    AdherenceCallResponse findAdherenceCallByMotechId(String motechId);

    void deleteAdherenceCall(String motechId);

    void setDosageStatusKnownForPatient(String motechId);

    boolean isPatientInCallRegimen(String motechId);

    String registerNewPatientIntoAdherenceCallRegimen(String motechId, String dosageStartTime);

}
