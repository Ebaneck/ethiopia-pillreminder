package org.motechproject.converter.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.motechproject.converter.repository.AllCouchEncountersImpl;
import org.motechproject.converter.support.CouchDAOBroker;
import org.motechproject.couch.mrs.model.CouchEncounterImpl;
import org.motechproject.mrs.domain.MRSEncounter;
import org.motechproject.mrs.domain.MRSObservation;
import org.motechproject.mrs.model.MRSEncounterDto;
import org.motechproject.mrs.services.MRSEncounterAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Converts encounters to a newer data model. The CouchMRS module
 * must be updated between the call to saveEncounters and convertEncounters
 *
 */
@Controller
public class MrsEntityConverterController {

    private Logger logger = LoggerFactory.getLogger("motech-mrs-entity-converter");

    private List<MRSEncounter> encountersInMemory = new ArrayList<MRSEncounter>();

    @Autowired
    private MRSEncounterAdapter encounterAdapter;

    @Autowired
    private AllCouchEncountersImpl allEncounters;

    @Autowired
    private CouchDAOBroker broker;

    @RequestMapping("/saveEncounters")
    @ResponseBody
    public void saveEncounters(HttpServletRequest request) {

        encountersInMemory = new ArrayList<MRSEncounter>();

        List<CouchEncounterImpl> encounters = allEncounters.getAll();

        for (CouchEncounterImpl encounter : encounters) {
            MRSEncounter fullEncounter = broker.buildFullEncounter(encounter);
            convertAndSaveEncounterInMemory(fullEncounter);
            allEncounters.remove(encounter);
        }

        logger.debug("All encounters saved in memory, # of encounters: " + encountersInMemory.size());
    }

    @RequestMapping("/convertEncounters")
    @ResponseBody
    public void convertEncounters(HttpServletRequest request) {
        for (MRSEncounter encounter : encountersInMemory) {
            encounterAdapter.createEncounter(encounter);
        }

        logger.debug("All encounters saved to DB, # of encounters: " + encountersInMemory.size());
    }

    @RequestMapping("/fixIds")
    @ResponseBody
    public void fixIds() {
        for (MRSEncounter encounter : encountersInMemory) {
            Set<? extends MRSObservation> observations =  encounter.getObservations();
            if (observations != null) {
                for (MRSObservation obs : observations) {
                    obs.setPatientId(encounter.getPatient().getPatientId());
                }
            }
        }
    }

    private void convertAndSaveEncounterInMemory(MRSEncounter fullEncounter) {
        MRSEncounterDto convertedEncounter = new MRSEncounterDto();

        //convertedEncounter.setCreator(fullEncounter.getCreator());
        convertedEncounter.setDate(fullEncounter.getDate());
        convertedEncounter.setEncounterId(fullEncounter.getEncounterId());
        convertedEncounter.setEncounterType(fullEncounter.getEncounterType());
        convertedEncounter.setFacility(fullEncounter.getFacility());
        convertedEncounter.setObservations(fullEncounter.getObservations());
        convertedEncounter.setPatient(fullEncounter.getPatient());
        convertedEncounter.setProvider(fullEncounter.getProvider());

        encountersInMemory.add(convertedEncounter);
    }
}
