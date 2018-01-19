package org.cubias.config;

import java.util.ArrayList;

public class AjaxResponseBody {

	private ArrayList<String> errorList = new ArrayList<>();

	public ArrayList<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(ArrayList<String> errorList) {
		this.errorList = errorList;
	}

	public void addError(String newMessage) {
		this.errorList.add(newMessage);
	}
}
