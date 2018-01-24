package org.cubias.pojos;

public class AutocompleteResponse {

	private String value;
	private String winLogin;
	private String unitName;

	public AutocompleteResponse() {

	}

	public AutocompleteResponse(String value, String winLogin, String unitName) {
		this.value = value;
		this.winLogin = winLogin;
		this.unitName = unitName;
	}

	public String getLabel() {
		if (this.value == null) {
			return "No user was found with the given search criteria";
		}
		return this.winLogin + " [" + this.unitName + "]";
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getWinLogin() {
		return winLogin;
	}

	public void setWinLogin(String winLogin) {
		this.winLogin = winLogin;
	}

}
