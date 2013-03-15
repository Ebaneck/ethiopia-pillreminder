package org.motechproject.icappr.couchdb;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.motechproject.mrs.services.PersonAdapter;
import org.motechproject.couch.mrs.model.CouchAttribute;
import org.motechproject.couch.mrs.model.CouchPerson;
import org.motechproject.mrs.domain.Attribute;
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
    private Logger logger = LoggerFactory.getLogger("motech-icappr");

    @Autowired
    public CouchPersonUtil(PersonAdapter couchPersonAdapter){
            this.couchPersonAdapter = couchPersonAdapter;
    }

    public CouchPerson createAndSavePerson(String phoneNum, String pin, String language) {
        CouchPerson person = new CouchPerson(); 
        person.setPersonId(UUID.randomUUID().toString());
        setAttribute(person, phoneNum, CouchMrsConstants.PHONE_NUMBER);
        setAttribute(person, pin, CouchMrsConstants.PERSON_PIN);
        setAttribute(person, language, CouchMrsConstants.LANGUAGE);
        couchPersonAdapter.addPerson(person);
        logger.info("Created person in CouchDB with phone " + phoneNum + " and language " + language);
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
