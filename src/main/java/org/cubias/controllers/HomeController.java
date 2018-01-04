package org.cubias.controllers;

import java.util.ArrayList;
import java.util.Map;

import org.cubias.McBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Contact;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Value("${home.message}")
	private String message;

	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		model.put("message", this.message);
		return "home";
	}

	@RequestMapping(value = "mav", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("message", "Model And View approach");
		return modelAndView;
	}

	@RequestMapping(value = "contacts", method = RequestMethod.GET)
	public ModelAndView contractsIndex() {
		ArrayList<Contact> contact = new ArrayList<Contact>();
		ModelAndView modelAndView = new ModelAndView("contract_list");
		try {

			// query for the 5 newest contacts
			QueryResult queryResults = McBootApplication.connection
					.query("SELECT Id, FirstName, LastName, Account.Name "
							+ "FROM Contact WHERE AccountId != NULL ORDER BY CreatedDate DESC LIMIT 15");
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					// cast the SObject to a strongly-typed Contact
					Contact c = (Contact) queryResults.getRecords()[i];
					contact.add(c);
					System.out.println("Id: " + c.getId() + " - Name: " + c.getFirstName() + " " + c.getLastName()
							+ " - Account: " + c.getAccount().getName());
				}
				modelAndView.addObject("contactList", contact);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
}
