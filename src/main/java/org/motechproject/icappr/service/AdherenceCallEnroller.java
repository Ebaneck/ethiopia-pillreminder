package org.motechproject.icappr.service;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentResponse;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSPatient;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.model.MRSAttributeDto;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class will enroll patients into a prebuilt pill reminder regimen
 */
@Component
public class AdherenceCallEnroller {
	private Logger logger = LoggerFactory.getLogger("motech-icappr");
	private final AdherenceCallService adherenceCallService;
    private final MRSPatientAdapter patientAdapter;

    @Autowired
    public AdherenceCallEnroller(AdherenceCallService pillReminders, MRSPatientAdapter patientAdapter) {
        this.adherenceCallService = pillReminders;
        this.patientAdapter = patientAdapter;
    }

    public AdherenceCallEnrollmentResponse enrollPatientWithId(AdherenceCallEnrollmentRequest request) {
        AdherenceCallEnrollmentResponse response = new AdherenceCallEnrollmentResponse();

        if (adherenceCallService.isPatientInCallRegimen(request.getMotechId())) {
            response.addError("Patient is already enrolled in Pill Reminder Regimen.");
            return response;
        }

        MRSPatient patient = patientAdapter.getPatientByMotechId(request.getMotechId());
        if (patient == null) {
            response.addError("No MRS Patient Found with id: " + request.getMotechId());
            return response;
        }

        setAttribute(patient.getPerson(), request.getPin(), MrsConstants.MRS_PIN_ATTR);
        setAttribute(patient.getPerson(), request.getPhonenumber(), MrsConstants.MRS_PHONE_NUM_ATTR);
        try {
            patientAdapter.updatePatient(patient);
        } catch (Exception e) {
            // if OpenMRS does not have attribute types of Pin or Phone Number
            // an exception will be thrown
            response.addError("OpenMRS does not have person attribute type: Pin or Phone Number. Please add them");
            logger.error("OpenMRS does not have person attribute types: Pin or Phone Number:" + MrsConstants.MRS_PIN_ATTR + "  " + request.getPin() + "  " + MrsConstants.MRS_PHONE_NUM_ATTR + " " + request.getPhonenumber());
            return response;
        }

        String actualStartTime = adherenceCallService.registerNewPatientIntoAdherenceCallRegimen(request.getMotechId(), request.getDosageStartTime());
        response.setStartTime(actualStartTime);

        return response;
    }

    private void setAttribute(MRSPerson person, String attrValue, String attrName) {
        Iterator<MRSAttribute> attrs = person.getAttributes().iterator();
        
        while (attrs.hasNext()) {
            MRSAttribute attr = attrs.next();
            if (attrName.equalsIgnoreCase(attr.getName())) {
                attrs.remove();
                break;
            }
        }
        person.getAttributes().add(new MRSAttributeDto(attrName, attrValue));

    }

}
