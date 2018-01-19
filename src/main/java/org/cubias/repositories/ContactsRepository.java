package org.cubias.repositories;

import java.util.ArrayList;

import org.cubias.services.ForceEnterpriseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;

@Repository
public class ContactsRepository {
	
	@Autowired
	private ForceEnterpriseConnection connection;

	public ArrayList<Contact> getAll() {
		ArrayList<Contact> contact = new ArrayList<Contact>();
		try {
			QueryResult queryResults = connection.getConnection()
					.query("SELECT Id, FirstName, LastName, Account.Name "
							+ "FROM Contact WHERE AccountId != NULL ORDER BY CreatedDate DESC LIMIT 25");
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					contact.add((Contact) queryResults.getRecords()[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
	}
}
