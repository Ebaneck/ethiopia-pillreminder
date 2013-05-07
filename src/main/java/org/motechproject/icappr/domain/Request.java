package org.motechproject.icappr.domain;

public class Request {

	private String motechId;
	private String phonenumber;
	private String type;
	private String language;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhoneNumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getMotechId() {
		return motechId;
	}

	public void setMotechID(String motechID) {
		this.motechId = motechID;
	}

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
