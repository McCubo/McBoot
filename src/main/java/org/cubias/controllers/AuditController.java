package org.cubias.controllers;

import org.cubias.repositories.FowAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuditController {

	@Autowired
	private FowAuditRepository auditRepo;

	@RequestMapping(value = "/admin/audit/report", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("audit_list");
		modelAndView.addObject("auditList", auditRepo.findAll());
		return modelAndView;
	}
}
