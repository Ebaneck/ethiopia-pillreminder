package org.motechproject.icappr.it;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.motechproject.testing.osgi.BaseOsgiIT;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class CampaignEnrollmentBundleIT extends BaseOsgiIT {

    private static final String COMMCARE_URL = "http://localhost:8080/commcare/cases";
    private static final String REGISTRATION_URL = "http://localhost:8080/icappr/registration?patientId=%s";

    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();

    public void testCommcareCaseEnrollsIntoWeeklyCampaign() throws IOException {
        waitForCommcareModule();
        simulateCommcareCaseForward();
        RegistrationResponse expected = readExpectedResponse();
        RegistrationResponse result = pingForResult();

        assertEquals(expected, result);
    }

    private RegistrationResponse pingForResult() {
        String url = String.format(REGISTRATION_URL, "7025");
        int retryCount = 0;
        while (retryCount < 60) {
            try {
                String json = restTemplate.getForObject(url, String.class);
                return gson.fromJson(json, RegistrationResponse.class);
            } catch (JsonParseException e) {
                throw new RuntimeException("Could not parse response json");
            } catch (Exception e) {
                retryCount++;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        throw new RuntimeException("Could not retrieve json response");
    }

    private RegistrationResponse readExpectedResponse() throws JsonSyntaxException, IOException {
        return gson.fromJson(
                IOUtils.toString(new ClassPathResource("json/registration-result-1.json").getInputStream()),
                RegistrationResponse.class);
    }

    /**
     * Wait until we receive a 200 status code from commcare module
     */
    private void waitForCommcareModule() {
        int retryCount = 0;
        while (retryCount < 60) {
            try {
                restTemplate.getForEntity(COMMCARE_URL, String.class);
                break;
            } catch (Exception e) {
                retryCount++;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private void simulateCommcareCaseForward() throws IOException {
        String caseXml = IOUtils.toString(new ClassPathResource("commcare/case.xml").getInputStream());
        restTemplate.postForEntity(COMMCARE_URL, caseXml, String.class);
    }
}
