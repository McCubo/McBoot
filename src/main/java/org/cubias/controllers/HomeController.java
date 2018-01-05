package org.cubias.controllers;

import java.util.Map;

import org.cubias.repositories.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Value("${home.message}")
	private String message;

	@Autowired
	private ContactsRepository contactsRepository;

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
		ModelAndView modelAndView = new ModelAndView("contract_list");
		modelAndView.addObject("contactList", contactsRepository.getAll());
		return modelAndView;
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView("login");
		return modelAndView;
	}
}
