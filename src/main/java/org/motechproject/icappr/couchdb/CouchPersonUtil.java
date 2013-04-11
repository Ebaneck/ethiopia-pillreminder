package org.motechproject.icappr.couchdb;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.motechproject.icappr.domain.PillReminderRegistration;
import org.motechproject.icappr.mrs.MrsConstants;
import org.motechproject.mrs.services.FacilityAdapter;
import org.motechproject.mrs.services.PatientAdapter;
import org.motechproject.mrs.services.PersonAdapter;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.couch.mrs.model.CouchAttribute;
import org.motechproject.couch.mrs.model.CouchFacility;
import org.motechproject.couch.mrs.model.CouchPatient;
import org.motechproject.couch.mrs.model.CouchPerson;
import org.motechproject.mrs.domain.Attribute;
import org.motechproject.mrs.domain.Facility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Convenience class to create CouchMRS Person objects
 */
@Component
public class CouchPersonUtil {   
    
    private final PersonAdapter couchPersonAdapter;
    private final FacilityAdapter facilityAdapter;
    private final PatientAdapter couchPatientAdapter;
    private Logger logger = LoggerFactory.getLogger("motech-icappr");
    
    private static final Map<String, String> clinicMappings = new HashMap<>();

    static {
        clinicMappings.put("clinic_a", "Clinic A");
        clinicMappings.put("clinic_b", "Clinic B");
    }

    @Autowired
    public CouchPersonUtil(PersonAdapter couchPersonAdapter, FacilityAdapter facilityAdapter, PatientAdapter couchPatientAdapter){
            this.couchPersonAdapter = couchPersonAdapter;
            this.facilityAdapter = facilityAdapter;
            this.couchPatientAdapter = couchPatientAdapter;
    }

    public CouchPerson createAndSavePerson(String phoneNum, String pin, String language) {
        CouchPerson person = new CouchPerson(); 
        person.setPersonId(UUID.randomUUID().toString());
        setAttribute(person, phoneNum, CouchMrsConstants.PHONE_NUMBER);
        setAttribute(person, pin, CouchMrsConstants.PERSON_PIN);
        setAttribute(person, language, CouchMrsConstants.LANGUAGE);
        setAttribute(person, "0", CouchMrsConstants.NUM_PIN_ATTEMPTS);
        couchPersonAdapter.addPerson(person);
        logger.info("Created person in CouchDB with phone " + phoneNum + " and language " + language);
        return person;
    }
    
    public CouchPerson createAndSaveGenericPatient(PillReminderRegistration registration){
        
        List<? extends Facility> facilities = facilityAdapter
                .getFacilities(clinicMappings.get(registration.getClinic()));
        if (facilities.size() == 0) {
            throw new RuntimeException("Could not find Facility with name: "
                    + clinicMappings.get(registration.getClinic()));
        }
        CouchFacility facility = (CouchFacility) facilities.get(0);

        CouchPerson person = new CouchPerson(); 
        person.setFirstName("MOTECH Generic Patient");
        person.setLastName("MOTECH Generic Patient");
        person.setDateOfBirth(DateUtil.now());
        person.setGender("F");
        person.setAddress("Generic Address");
        setAttribute(person, registration.getPreferredLanguage(), CouchMrsConstants.LANGUAGE_ATTR);
        setAttribute(person, registration.getPhoneNumber(), CouchMrsConstants.PHONE_NUMBER_ATTR);
        setAttribute(person, registration.getPin(), CouchMrsConstants.PERSON_PIN_ATTR);
        setAttribute(person, registration.nextCampaign(), CouchMrsConstants.NEXT_CAMPAIGN_ATTR);
        setAttribute(person, "0", CouchMrsConstants.NUM_PIN_ATTEMPTS);
        CouchPatient patient = new CouchPatient(UUID.randomUUID().toString(), registration.getPatientId(), person, facility);
        couchPatientAdapter.savePatient(patient);
        return person;
    }
    
    public CouchPerson getPersonByID(String motechID){
        ArrayList<CouchPerson> allPersons = (ArrayList<CouchPerson>) couchPersonAdapter.findAllPersons();
        for(CouchPerson person: allPersons){
            if (person.getId().matches(motechID)){
                return person;
            }
        }
        return null;
    }
    
    public CouchPerson getPersonByPhoneNumber(String phoneNum){
        ArrayList<CouchPerson> allPersons = (ArrayList<CouchPerson>) couchPersonAdapter.findAllPersons();
        for(CouchPerson person: allPersons){
            if (getAttribute(person, CouchMrsConstants.PHONE_NUMBER).getValue().matches(phoneNum)){
                return person;
            }
        }
        return null;
    }
    
    private void setAttribute(CouchPerson person, String attrValue, String attrName) {
        Iterator<Attribute> attrs = person.getAttributes().iterator();
        while (attrs.hasNext()) {
            Attribute attr = attrs.next();
            if (attrName.equalsIgnoreCase(attr.getName())) {
                attrs.remove();
                break;
            }
        }
        person.getAttributes().add(new CouchAttribute(attrName, attrValue));
    }
    
    public Attribute getAttribute(CouchPerson person, String attrName) {
        Iterator<Attribute> attrs = person.getAttributes().iterator();
        while (attrs.hasNext()) {
            Attribute attr = attrs.next();
            if (attrName.equalsIgnoreCase(attr.getName())) {
                return attr;
            }
        }
        return null;
    }
    
}
