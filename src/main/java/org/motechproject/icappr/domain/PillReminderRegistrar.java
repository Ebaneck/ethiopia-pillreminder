package org.motechproject.icappr.domain;

import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mrs.model.OpenMRSAttribute;
import org.motechproject.mrs.model.OpenMRSPatient;
import org.motechproject.mrs.model.OpenMRSPerson;
import org.motechproject.mrs.services.PatientAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderRegistrar {

    private PatientAdapter patientAdapter;

    @Autowired
    public PillReminderRegistrar(PatientAdapter patientAdapter) {
        this.patientAdapter = patientAdapter;
    }

    public void register(PillReminderRegistration registration) {
        createGenericPatient(registration);
    }

    private void createGenericPatient(PillReminderRegistration registration) {
        OpenMRSPerson person = new OpenMRSPerson();
        person.firstName("MOTECH Generic Patient");
        person.setLastName("MOTECH Generic Patient");
        person.setGender("M");
        person.setDateOfBirth(DateUtil.now());

        person.addAttribute(new OpenMRSAttribute("Phone Number", registration.getPhoneNumber()));
        person.addAttribute(new OpenMRSAttribute("Pin", registration.getPin()));
        person.addAttribute(new OpenMRSAttribute("Clinic", registration.getClinic()));
        person.addAttribute(new OpenMRSAttribute("Next Campaign", registration.nextCampaign()));

        OpenMRSPatient patient = new OpenMRSPatient(registration.getPatientId(), person, null);
        patientAdapter.savePatient(patient);
    }

}
