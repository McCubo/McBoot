package org.cubias.controllers;

import org.cubias.repositories.ContactsRepository;
import org.cubias.repositories.ForceAccountMappingRepository;
import org.cubias.repositories.ForceContractsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	private ContactsRepository contactsRepository;

	@Autowired
	private ForceContractsRepository contractsRepository;

	@Autowired
	private ForceAccountMappingRepository accountMapRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("accountMapList", accountMapRepository.getAll());
		modelAndView.addObject("contract_list", contractsRepository.getall());
		return modelAndView;
	}

	@RequestMapping(value = "/admin/contacts", method = RequestMethod.GET)
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

	@RequestMapping(value = "403", method = RequestMethod.GET)
	public String error403() {
		return "/error/403";
	}
}
