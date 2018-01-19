package org.cubias.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cubias.entities.FowAudit;
import org.cubias.entities.FowUser;
import org.cubias.pojos.ContractPojo;
import org.cubias.pojos.ContractRequest;
import org.cubias.pojos.OptionPojo;
import org.cubias.security.AuthenticationFacade;
import org.cubias.services.ForceEnterpriseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.TI_Contracts__c;
import com.sforce.ws.ConnectionException;

@Repository
public class ForceContractsRepository {

	@Autowired
	private ForceEnterpriseConnection connection;

	@Autowired
	private FowUserRepository userRepo;

	@Autowired
	private FowAuditRepository auditRepo;

	@Autowired
	private AuthenticationFacade auth;

	@SuppressWarnings("unchecked")
	public ArrayList<TI_Contracts__c> getall() {
		String query = "SELECT Id, Name, Type_of_Work__c, Channel_Type__c, Team_Member_Headcount__c FROM TI_Contracts__c";
		ArrayList<SObject> list = connection.query(query);
		ArrayList<TI_Contracts__c> castedList = (ArrayList<TI_Contracts__c>) (List<?>) list;
		return castedList;
	}

	@SuppressWarnings("unchecked")
	public TI_Contracts__c getById(String id) {
		String query = "SELECT Id, Name, Type_of_Work__c, Channel_Type__c, Team_Member_Headcount__c FROM TI_Contracts__c WHERE Id = '"
				+ id + "'";
		ArrayList<SObject> list = connection.query(query);
		ArrayList<TI_Contracts__c> castedList = (ArrayList<TI_Contracts__c>) (List<?>) list;
		for (TI_Contracts__c contract : castedList) {
			return contract;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ContractPojo getPjoById(String id) {
		String query = "SELECT Id, Name, Type_of_Work__c, Channel_Type__c, Team_Member_Headcount__c FROM TI_Contracts__c WHERE Id = '"
				+ id + "'";
		ArrayList<SObject> list = connection.query(query);
		ArrayList<TI_Contracts__c> castedList = (ArrayList<TI_Contracts__c>) (List<?>) list;
		for (TI_Contracts__c contract : castedList) {
			ArrayList<OptionPojo> channelTypeOption = connection.getValuesForPickList("TI_Contracts__c",
					"Channel_Type__c");
			ArrayList<OptionPojo> typeOfWorkOption = connection.getValuesForPickList("TI_Contracts__c",
					"Type_of_Work__c");
			ContractPojo pojo = new ContractPojo(contract.getId(), contract.getName(), typeOfWorkOption,
					channelTypeOption, contract.getTeam_Member_Headcount__c());
			pojo.setTypeOfWorkActiveOptions(contract.getType_of_Work__c());
			pojo.setChannelTypeActiveOptions(contract.getChannel_Type__c());
			return pojo;
		}
		return null;
	}

	public Boolean doUpdate(ContractRequest request) {
		TI_Contracts__c[] updates = new TI_Contracts__c[1];
		try {
			TI_Contracts__c contract = this.getById(request.getId());
			this.handleAudit(request, contract);
			contract.setType_of_Work__c(request.getTypeOfWorkString());
			contract.setChannel_Type__c(request.getChannelTypeString());
			contract.setTeam_Member_Headcount__c(request.getTeamMemberCount());
			updates[0] = contract;
			SaveResult[] saveResults = connection.update(updates);
			for (int i = 0; i < saveResults.length; i++) {
				if (!saveResults[i].isSuccess()) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		} catch (ConnectionException e) {
			return Boolean.FALSE;
		}
	}

	private void handleAudit(ContractRequest contractRequest, TI_Contracts__c contract) {
		ArrayList<FowAudit> list = new ArrayList<FowAudit>();
		FowUser user = userRepo.findOneByUseAdUser(auth.getAuthentication().getName());
		Date date = new Date();
		String tableName = "TI Contracts";
		if (!contract.getType_of_Work__c().equals(contractRequest.getTypeOfWorkString())) {
			list.add(new FowAudit(date, tableName, "Type of Work", contract.getType_of_Work__c(),
					contractRequest.getTypeOfWorkString(), user, contractRequest.getId()));
		}
		if (!contract.getChannel_Type__c().equals(contractRequest.getChannelTypeString())) {
			list.add(new FowAudit(date, tableName, "Program", contract.getChannel_Type__c(),
					contractRequest.getChannelTypeString(), user, contractRequest.getId()));
		}
		if (!contract.getTeam_Member_Headcount__c().equals(contractRequest.getTeamMemberCount())) {
			list.add(new FowAudit(date, tableName, "Team Member Headcount", contract.getTeam_Member_Headcount__c().toString(),
					contractRequest.getTeamMemberCount().toString(), user, contractRequest.getId()));
		}
		if (list.size() > 0) {
			auditRepo.save(list);
		}
	}
}
