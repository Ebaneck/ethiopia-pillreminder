package org.motechproject.converter.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.motechproject.couch.mrs.model.CouchEncounterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllCouchEncountersImpl extends MotechBaseRepository<CouchEncounterImpl> {

    @Autowired
    protected AllCouchEncountersImpl(@Qualifier("couchEncounterDatabaseConnector") CouchDbConnector db) {
        super(CouchEncounterImpl.class, db);
        initStandardDesignDocument();
    }
}
