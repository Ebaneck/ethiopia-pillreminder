package org.motechproject.icappr.mrs;

import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mrs.exception.UserAlreadyExistsException;
import org.motechproject.mrs.domain.MRSPerson;
import org.motechproject.mrs.domain.MRSProvider;
import org.motechproject.mrs.domain.MRSUser;
import org.motechproject.mrs.model.MRSPersonDto;
import org.motechproject.mrs.model.MRSProviderDto;
import org.motechproject.mrs.model.MRSUserDto;
import org.motechproject.mrs.services.MRSUserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Resolves the Motech user from the OpenMRS application. If the Motech user
 * does not exist, it will create a new Motech user
 */
@Component
public class MrsUserResolver {

    public static final String MOTECH_USERNAME = "motech";
    private MRSUserAdapter userAdapter;

    @Autowired
    public MrsUserResolver(MRSUserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    public MRSProvider resolveMotechUser() {
        MRSUser user = userAdapter.getUserByUserName(MOTECH_USERNAME);
        if (user == null) {
            user = createMotechUser();
        }
        
        MRSProvider provider = new MRSProviderDto();
        provider.setProviderId(user.getPerson().getPersonId());
        provider.setPerson(user.getPerson());
        return provider;
    }

    private MRSUser createMotechUser() {
        MRSUser user = createUserWithPerson(createPerson());
        return saveMotechUser(user);
    }

    private MRSPerson createPerson() {
        MRSPerson person = new MRSPersonDto();
        person.setFirstName("Motech");
        person.setLastName("Motech");
        person.setAddress("None");
        person.setDateOfBirth(DateUtil.now());
        person.setGender("F");
        return person;
    }

    private MRSUser createUserWithPerson(MRSPerson person) {
        MRSUser user = new MRSUserDto();
        user.setUserName(MOTECH_USERNAME);
        user.setSecurityRole("Provider");
        user.setPerson(person);
        return user;
    }

    private MRSUser saveMotechUser(MRSUser user) {
        try {
            user = (MRSUser) userAdapter.saveUser(user).get(MRSUserAdapter.USER_KEY);
        } catch (UserAlreadyExistsException e) {
            user = userAdapter.getUserByUserName(MOTECH_USERNAME);
        }
        return user;
    }
}
