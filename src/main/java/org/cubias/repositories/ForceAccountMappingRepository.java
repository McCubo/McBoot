package org.cubias.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cubias.entities.FowAudit;
import org.cubias.entities.FowUser;
import org.cubias.pojos.AccountMapRequest;
import org.cubias.security.AuthenticationFacade;
import org.cubias.services.ForceEnterpriseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Account_Mapping__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;

@Repository
public class ForceAccountMappingRepository {

	@Autowired
	private ForceEnterpriseConnection connection;

	@Autowired
	private FowAuditRepository auditRepo;

	@Autowired
	private AuthenticationFacade auth;

	@Autowired
	private FowUserRepository userRepo;

	@SuppressWarnings("unchecked")
	public ArrayList<Account_Mapping__c> getAll() {
		String query = "SELECT Id, Client__c, Business_Unit__c, TI_Director__c, Program__c, Start_Date__c, Status__c FROM Account_Mapping__c";
		ArrayList<SObject> list = connection.query(query);
		ArrayList<Account_Mapping__c> castedList = (ArrayList<Account_Mapping__c>) (List<?>) list;
		return castedList;
	}

	@SuppressWarnings("unchecked")
	public Account_Mapping__c getById(String id) {
		String query = "SELECT Id, Client__c, Business_Unit__c, TI_Director__c, Program__c, Start_Date__c, Status__c FROM Account_Mapping__c WHERE id = '"
				+ id + "'";
		try {
			ArrayList<SObject> list = connection.query(query);
			ArrayList<Account_Mapping__c> castedList = (ArrayList<Account_Mapping__c>) (List<?>) list;
			for (Account_Mapping__c account : castedList) {
				return account;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public Boolean doUpdate(AccountMapRequest mapRequest) {
		Account_Mapping__c[] updates = new Account_Mapping__c[1];
		try {
			Account_Mapping__c am = this.getById(mapRequest.getId());
			this.handleAudit(mapRequest, am);
			am.setTI_Director__c(mapRequest.getDirector());
			am.setProgram__c(mapRequest.getProgram());
			am.setStart_Date__c(mapRequest.getStartdate());
			updates[0] = am;
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

	private void handleAudit(AccountMapRequest mapRequest, Account_Mapping__c mapping) {
		ArrayList<FowAudit> list = new ArrayList<FowAudit>();
		FowUser user = userRepo.findOneByUseAdUser(auth.getAuthentication().getName());
		Date date = new Date();
		String tableName = "Account Mapping";
		if (!mapping.getTI_Director__c().equals(mapRequest.getDirector())) {
			list.add(new FowAudit(date, tableName, "TI Director", mapping.getTI_Director__c(), mapRequest.getDirector(),
					user, mapRequest.getId()));
		}
		if (!mapping.getProgram__c().equals(mapRequest.getProgram())) {
			list.add(new FowAudit(date, tableName, "Program", mapping.getProgram__c(), mapRequest.getProgram(), user, mapRequest.getId()));
		}
		if (!mapping.getStart_Date__c().equals(mapRequest.getStartdate())) {
			list.add(new FowAudit(date, tableName, "Start Date", mapping.getStart_Date__c(), mapRequest.getStartdate(),
					user, mapRequest.getId()));
		}
		if (list.size() > 0) {
			auditRepo.save(list);
		}
	}
}
