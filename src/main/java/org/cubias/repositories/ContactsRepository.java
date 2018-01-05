package org.cubias.repositories;

import java.util.ArrayList;

import org.cubias.McBootApplication;
import org.springframework.stereotype.Repository;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;

@Repository
public class ContactsRepository {

	public ArrayList<Contact> getAll() {
		ArrayList<Contact> contact = new ArrayList<Contact>();
		try {
			QueryResult queryResults = McBootApplication.connection
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
