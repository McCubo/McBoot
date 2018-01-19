package org.cubias.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TIContractController {
	
	@RequestMapping(value = "/editor/contract", method = RequestMethod.GET)
	public String list() {
		return "Hello Contact Form";
	}
}
