package org.motechproject.converter.repository;

import org.motechproject.couch.mrs.model.CouchPerson;

import java.util.List;

public interface AllCouchPersons {

    List<CouchPerson> findByPersonId(String personId);

    void addPerson(CouchPerson person);

    void update(CouchPerson person);

    void remove(CouchPerson person);

    List<CouchPerson> findAllPersons();
}
