package org.cubias.pojos;

public class OptionPojo {
	
	private String optionName;
	private Boolean selected = false;

	public OptionPojo(String optionName) {
		super();
		this.optionName = optionName;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "OptionPojo [optionName=" + optionName + ", selected=" + selected + "]";
	}

}
