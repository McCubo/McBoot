package org.cubias;

import java.util.HashMap;

import org.cubias.entities.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

@SpringBootApplication
public class McBootApplication {

	public static HashMap<Long, Student> hmapStudent;
	static final String USERNAME = "qlik.datasource@telusinternational.com.fulltest";
	static final String PASSWORD = "1XcLcenKm3oP56ATDz2iTv74JePdSaFHU8mpKVrpMJxW";
	public static EnterpriseConnection connection;

	public static void main(String[] args) {
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(USERNAME);
		config.setPassword(PASSWORD);
		try {
			connection = Connector.newConnection(config);
			System.out.println("Auth EndPoint: " + config.getAuthEndpoint());
			System.out.println("Service EndPoint: " + config.getServiceEndpoint());
			System.out.println("Username: " + config.getUsername());
			System.out.println("SessionId: " + config.getSessionId());
			SpringApplication.run(McBootApplication.class, args);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}
}
