package org.cubias.controllers;

import javax.validation.Valid;

import org.cubias.config.AjaxResponseBody;
import org.cubias.error.NotFoundException;
import org.cubias.pojos.ContractPojo;
import org.cubias.pojos.ContractRequest;
import org.cubias.repositories.ForceContractsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContractController {

	@Autowired
	private ForceContractsRepository contractsRepo;

	@RequestMapping(value = "/editor/{id}/contract", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("contract_edit");
		ContractPojo contract = contractsRepo.getPjoById(id);
		if (contract == null) {
			throw new NotFoundException();
		}
		modelAndView.addObject("contract", contract);
		return modelAndView;
	}

	@RequestMapping(value = "/editor/save/contract", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> doEdit(@Valid @RequestBody ContractRequest request, Errors errors) {
		AjaxResponseBody ajax = new AjaxResponseBody();
		if (errors.hasErrors()) {
			for (ObjectError oError : errors.getAllErrors()) {
				ajax.addError(oError.getDefaultMessage());
			}
			return ResponseEntity.ok(ajax);
		}
		if (!contractsRepo.doUpdate(request)) {
			ajax.addError("Error while trying to update the record");
		}
		return ResponseEntity.ok(ajax);
	}
}
