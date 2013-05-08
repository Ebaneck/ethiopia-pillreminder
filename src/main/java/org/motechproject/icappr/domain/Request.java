package org.motechproject.icappr.domain;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String motechId;
    private String phoneNumber;
    private String type;
    private String language;
    private Map<String, String> payload = new HashMap<String, String>();

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public void addParameter(String parameter, String value) {
        if (payload != null) {
            payload.put(parameter, value);
        }
    }
}
