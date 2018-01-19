package org.cubias.services;

import java.util.ArrayList;

import org.cubias.pojos.OptionPojo;
import org.springframework.stereotype.Service;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DescribeSObjectResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Field;
import com.sforce.soap.enterprise.FieldType;
import com.sforce.soap.enterprise.PicklistEntry;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

@Service
public class ForceEnterpriseConnection {

	static final String USERNAME = "qlik.datasource@telusinternational.com.fulltest";
	static final String PASSWORD = "4BJIQCNfbTVh9wxVvxKzBT50ITd85HvU8cJWdBBrOQd9pLYs7";
	public static EnterpriseConnection connection;

	public EnterpriseConnection getConnection() {
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(USERNAME);
		config.setPassword(PASSWORD);
		try {
			connection = Connector.newConnection(config);
			System.out.println("Auth EndPoint: " + config.getAuthEndpoint());
			System.out.println("Service EndPoint: " + config.getServiceEndpoint());
			System.out.println("Username: " + config.getUsername());
			System.out.println("SessionId: " + config.getSessionId());
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		return connection;
	}

	protected void logout() {
		try {
			connection.logout();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<SObject> query(String query) {
		ArrayList<SObject> list = new ArrayList<SObject>();
		try {
			QueryResult queryResults = getConnection().query(query);
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					list.add(queryResults.getRecords()[i]);
				}
			}
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		return list;
	}

	public SaveResult[] update(SObject[] object) throws ConnectionException {
		return getConnection().update(object);
	}

	public ArrayList<OptionPojo> getValuesForPickList(String forceObject, String fieldname) {
		ArrayList<OptionPojo> list = new ArrayList<OptionPojo>();
		DescribeSObjectResult dsr;
		try {
			dsr = getConnection().describeSObject(forceObject);
			for (int i = 0; i < dsr.getFields().length; i++) {
				Field field = dsr.getFields()[i];
				if (field.getName().equals(fieldname)) {
					if (field.getType().equals(FieldType.multipicklist) || field.getType().equals(FieldType.picklist)) {
						PicklistEntry[] picklistValues = field.getPicklistValues();
						for (int j = 0; j < field.getPicklistValues().length; j++) {
							list.add(new OptionPojo(picklistValues[j].getValue()));
						}
					}
				}
			}
		} catch (ConnectionException e) {
			return null;
		}

		return list;
	}
}
