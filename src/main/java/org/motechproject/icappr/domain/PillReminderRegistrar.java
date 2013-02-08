package org.motechproject.icappr.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mrs.domain.Attribute;
import org.motechproject.mrs.domain.Facility;
import org.motechproject.mrs.domain.Patient;
import org.motechproject.mrs.model.OpenMRSAttribute;
import org.motechproject.mrs.model.OpenMRSFacility;
import org.motechproject.mrs.model.OpenMRSPatient;
import org.motechproject.mrs.model.OpenMRSPerson;
import org.motechproject.mrs.services.FacilityAdapter;
import org.motechproject.mrs.services.PatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderRegistrar {

    private PatientAdapter patientAdapter;
    private FacilityAdapter facilityAdapter;

    private static final Map<String, String> clinicMappings = new HashMap<>();

    static {
        clinicMappings.put("clinic_a", "Clinic A");
        clinicMappings.put("clinic_b", "Clinic B");
    }

    @Autowired
    public PillReminderRegistrar(PatientAdapter patientAdapter, FacilityAdapter facilityAdapter) {
        this.patientAdapter = patientAdapter;
        this.facilityAdapter = facilityAdapter;
    }

    public void register(PillReminderRegistration registration) {
        createGenericPatient(registration);
    }

    private void createGenericPatient(PillReminderRegistration registration) {
        List<? extends Facility> facilities = facilityAdapter.getFacilities(clinicMappings.get(registration.getClinic()));
        if (facilities.size() == 0) {
            throw new RuntimeException("Could not find OpenMRS Facility with name: " + clinicMappings.get(registration.getClinic()));
        }
        OpenMRSFacility facility = (OpenMRSFacility) facilities.get(0);
        
        OpenMRSPerson person = new OpenMRSPerson();
        person.firstName("MOTECH Generic Patient");
        person.setLastName("MOTECH Generic Patient");
        person.setGender("M");
        person.setDateOfBirth(DateUtil.now());

        person.addAttribute(new OpenMRSAttribute("Phone Number", registration.getPhoneNumber()));
        person.addAttribute(new OpenMRSAttribute("Pin", registration.getPin()));
        person.addAttribute(new OpenMRSAttribute("Next Campaign", registration.nextCampaign()));

        OpenMRSPatient patient = new OpenMRSPatient(registration.getPatientId(), person, facility);
        patientAdapter.savePatient(patient);
    }

    public PillReminderRegistration getRegistrationForPatient(String patientId) {
        Patient patient = patientAdapter.getPatientByMotechId(patientId);
        if (patient == null) {
            return null;
        }
        
        PillReminderRegistration registration = new PillReminderRegistration();
        registration.setClinic(patient.getFacility().getName());
        registration.setPatientId(patientId);
        
        List<Attribute> attrs = patient.getPerson().getAttributes();
        registration.setNextCampaign(getAttrValue("Next Campaign", attrs));
        registration.setPhoneNumber(getAttrValue("Phone Number", attrs));
        registration.setPin(getAttrValue("Pin", attrs));
        
        return registration;
    }

    private String getAttrValue(String name, List<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (name.equals(attr.getName())) {
                return attr.getValue();
            }
        }
        
        return null;
    }
}
