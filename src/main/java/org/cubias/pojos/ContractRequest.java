package org.cubias.pojos;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContractRequest {

	private String id;
	
	@NotNull(message = "You must select at least one Type of Work")
	@Size(min = 1, message = "You must select at least one Type of Work")
	private List<String> typeOfWork;
	
	@NotNull(message = "You must select at least one Channel type for this Contract")
	@Size(min = 1, message = "You must select at least one Channel type for this Contract")
	private List<String> channelType;
	
	@Min(value = 1, message = "Please enter a valid number for team member headcount")
	@NotNull(message = "Team Member Headcount can not be null")
	private Double teamMemberCount;

	public ContractRequest() {
	}

	public String getTypeOfWorkString() {
		if (this.typeOfWork == null) {
			return "";
		}
		return String.join(";", this.typeOfWork);
	}

	public String getChannelTypeString() {
		if (this.channelType == null) {
			return "";
		}
		return String.join(";", this.channelType);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(List<String> typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public List<String> getChannelType() {
		return channelType;
	}

	public void setChannelType(List<String> channelType) {
		this.channelType = channelType;
	}

	public Double getTeamMemberCount() {
		return teamMemberCount;
	}

	public void setTeamMemberCount(Double teamMemberCount) {
		this.teamMemberCount = teamMemberCount;
	}

}
