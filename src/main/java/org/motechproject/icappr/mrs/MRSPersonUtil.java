package org.motechproject.icappr.mrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mrs.model.MRSAttributeDto;
import org.motechproject.mrs.model.MRSFacilityDto;
import org.motechproject.mrs.model.MRSPatientDto;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.icappr.form.model.PillReminderRegistration;
import org.motechproject.mrs.domain.MRSAttribute;
import org.motechproject.mrs.domain.MRSFacility;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.services.MRSFacilityAdapter;
import org.motechproject.mrs.services.MRSPatientAdapter;
import org.motechproject.mrs.services.MRSPersonAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convenience class to create MRS Person objects
 */
@Component
public class MRSPersonUtil {   

    private final MRSPersonAdapter mrsPersonAdapter;
    private final MRSFacilityAdapter mrsFacilityAdapter;
    private final MRSPatientAdapter mrsPatientAdapter;

    private static final Map<String, String> clinicMappings = new HashMap<>();

    static {
        clinicMappings.put("clinic_a", "Clinic A");
        clinicMappings.put("clinic_b", "Clinic B");
    }

    @Autowired
    public MRSPersonUtil(MRSPersonAdapter mrsPersonAdapter, MRSFacilityAdapter mrsFacilityAdapter, MRSPatientAdapter mrsPatientAdapter){
        this.mrsPersonAdapter = mrsPersonAdapter;
        this.mrsFacilityAdapter = mrsFacilityAdapter;
        this.mrsPatientAdapter = mrsPatientAdapter;
    }

    public MRSPersonDto createAndSaveDemoPerson(String phoneNum, String pin, String language) {
        MRSPersonDto person = new MRSPersonDto(); 
        person.setPersonId(UUID.randomUUID().toString());
        setAttribute(person, phoneNum, MrsConstants.PERSON_PHONE_NUMBER_ATTR);
        setAttribute(person, pin, MrsConstants.PERSON_PIN_ATTR);
        setAttribute(person, language, MrsConstants.PERSON_LANGUAGE_ATTR);
        setAttribute(person, "true", MrsConstants.DUMMY_PERSON_ATTR);
        setAttribute(person, "0", MrsConstants.PERSON_NUM_PIN_ATTEMPTS);
        mrsPersonAdapter.addPerson(person);
        return person;
    }

    public MRSPersonDto createAndSaveGenericPatient(PillReminderRegistration registration){

        List<? extends MRSFacility> facilities = mrsFacilityAdapter
                .getFacilities(clinicMappings.get(registration.getClinic()));
        if (facilities.size() == 0) {
            throw new RuntimeException("Could not find Facility with name: "
                    + clinicMappings.get(registration.getClinic()));
        }
        MRSFacilityDto facility = (MRSFacilityDto)facilities.get(0);

        MRSPersonDto person = new MRSPersonDto(); 
        person.setFirstName("MOTECH Generic Patient");
        person.setLastName("MOTECH Generic Patient");
        person.setDateOfBirth(DateUtil.now());
        person.setGender("F");
        person.setAddress("Generic Address");
        setAttribute(person, registration.getPreferredLanguage(), MrsConstants.PERSON_LANGUAGE_ATTR);
        setAttribute(person, registration.getPhoneNumber(), MrsConstants.PERSON_PHONE_NUMBER_ATTR);
        setAttribute(person, registration.getPin(), MrsConstants.PERSON_PIN_ATTR);
        setAttribute(person, registration.nextCampaign(), MrsConstants.PERSON_NEXT_CAMPAIGN_ATTR);
        setAttribute(person, "0", MrsConstants.PERSON_NUM_PIN_ATTEMPTS);
        MRSPatientDto patient = new MRSPatientDto();
        patient.setPatientId(UUID.randomUUID().toString());
        patient.setMotechId(registration.getCaseId());
        patient.setPerson(person);
        patient.setFacility(facility);	        
        mrsPatientAdapter.savePatient(patient);
        return person;
    }

    public MRSPerson getPersonByID(String motechID){
        ArrayList<MRSPerson> allPersons = (ArrayList<MRSPerson>) mrsPersonAdapter.findAllPersons();
        for(MRSPerson person: allPersons){
            if (person.getPersonId().matches(motechID)){
                return person;
            }
        }
        return null;
    }

    public MRSPerson getPersonByPhoneNumber(String phoneNum){
        ArrayList<MRSPerson> allPersons = (ArrayList<MRSPerson>) mrsPersonAdapter.findAllPersons();
        for(MRSPerson person: allPersons){
            if (getAttribute(person, MrsConstants.PERSON_PHONE_NUMBER_ATTR).getValue().matches(phoneNum)){
                return person;
            }
        }
        return null;
    }

    private void setAttribute(MRSPersonDto person, String attrValue, String attrName) {
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

    public MRSAttribute getAttribute(MRSPerson person, String attrName) {
        Iterator<MRSAttribute> attrs = person.getAttributes().iterator();
        while (attrs.hasNext()) {
            MRSAttribute attr = attrs.next();
            if (attrName.equalsIgnoreCase(attr.getName())) {
                return attr;
            }
        }
        return null;
    }

    public static String getAttrValue(String name, List<MRSAttribute> attrs) {
        for (MRSAttribute attr : attrs) {
            if (name.equals(attr.getName())) {
                return attr.getValue();
            }
        }

        return null;
    }


}
