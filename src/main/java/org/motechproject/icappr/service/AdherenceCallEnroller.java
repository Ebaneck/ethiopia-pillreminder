package org.motechproject.icappr.service;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.motechproject.icappr.domain.AdherenceCallEnrollmentRequest;
import org.motechproject.icappr.domain.AdherenceCallEnrollmentResponse;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.domain.Attribute;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.mrs.domain.Person;
import org.motechproject.mrs.model.OpenMRSAttribute;
import org.motechproject.mrs.services.PatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class will enroll patients into a prebuilt pill reminder regimen
 */
@Component
public class AdherenceCallEnroller {
	private Logger logger = LoggerFactory.getLogger("motech-icappr");
	private final AdherenceCallService adherenceCallService;
    private final PatientAdapter patientAdapter;

    @Autowired
    public AdherenceCallEnroller(AdherenceCallService pillReminders, PatientAdapter patientAdapter) {
        this.adherenceCallService = pillReminders;
        this.patientAdapter = patientAdapter;
    }

    public AdherenceCallEnrollmentResponse enrollPatientWithId(AdherenceCallEnrollmentRequest request) {
        AdherenceCallEnrollmentResponse response = new AdherenceCallEnrollmentResponse();

        if (adherenceCallService.isPatientInCallRegimen(request.getMotechId())) {
            response.addError("Patient is already enrolled in Pill Reminder Regimen.");
            return response;
        }

        Patient patient = patientAdapter.getPatientByMotechId(request.getMotechId());
        if (patient == null) {
            response.addError("No MRS Patient Found with id: " + request.getMotechId());
            return response;
        }

        setAttribute(patient.getPerson(), request.getPin(), MrsConstants.PERSON_PIN_ATTR_NAME);
        setAttribute(patient.getPerson(), request.getPhonenumber(), MrsConstants.PERSON_PHONE_NUMBER_ATTR_NAME);
        try {
            patientAdapter.updatePatient(patient);
        } catch (Exception e) {
            // if OpenMRS does not have attribute types of Pin or Phone Number
            // an exception will be thrown
            response.addError("OpenMRS does not have person attribute type: Pin or Phone Number. Please add them");
            logger.error("OpenMRS does not have person attribute types: Pin or Phone Number:" + MrsConstants.PERSON_PIN_ATTR_NAME + "  " + request.getPin() + "  " + MrsConstants.PERSON_PHONE_NUMBER_ATTR_NAME + " " + request.getPhonenumber());
            return response;
        }

        String actualStartTime = adherenceCallService.registerNewPatientIntoAdherenceCallRegimen(request.getMotechId(), request.getDosageStartTime());
        response.setStartTime(actualStartTime);

        return response;
    }

    private void setAttribute(Person person, String attrValue, String attrName) {
        Iterator<Attribute> attrs = person.getAttributes().iterator();
        
        while (attrs.hasNext()) {
            Attribute attr = attrs.next();
            if (attrName.equalsIgnoreCase(attr.getName())) {
                attrs.remove();
                break;
            }
        }
        person.getAttributes().add(new OpenMRSAttribute(attrName, attrValue));

    }

}
