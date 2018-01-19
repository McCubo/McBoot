package org.cubias.pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContractPojo {

	private String id;
	private String contactName;
	private ArrayList<OptionPojo> typeOfWork = new ArrayList<>();
	private ArrayList<OptionPojo> channelType = new ArrayList<>();
	private Double teamMemberCount;

	public ContractPojo(String id, String contactName, ArrayList<OptionPojo> typeOfWork,
			ArrayList<OptionPojo> channelType, Double teamMemberCount) {
		super();
		this.id = id;
		this.contactName = contactName;
		this.typeOfWork = typeOfWork;
		this.channelType = channelType;
		this.teamMemberCount = teamMemberCount;
	}

	public void setTypeOfWorkActiveOptions(String options) {
		List<String> optionList = Arrays.asList(options.split(";"));
		for (OptionPojo option : this.typeOfWork) {
			if (optionList.contains(option.getOptionName())) {
				option.setSelected(true);
			}
		}
	}

	public void setChannelTypeActiveOptions(String options) {
		List<String> optionList = Arrays.asList(options.split(";"));
		for (OptionPojo option : this.channelType) {
			if (optionList.contains(option.getOptionName())) {
				option.setSelected(true);
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public ArrayList<OptionPojo> getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(ArrayList<OptionPojo> typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public ArrayList<OptionPojo> getChannelType() {
		return channelType;
	}

	public void setChannelType(ArrayList<OptionPojo> channelType) {
		this.channelType = channelType;
	}

	public Double getTeamMemberCount() {
		return teamMemberCount;
	}

	public void setTeamMemberCount(Double teamMemberCount) {
		this.teamMemberCount = teamMemberCount;
	}

}
