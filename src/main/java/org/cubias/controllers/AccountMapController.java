package org.cubias.controllers;

import javax.validation.Valid;

import org.cubias.config.AjaxResponseBody;
import org.cubias.error.NotFoundException;
import org.cubias.pojos.AccountMapRequest;
import org.cubias.repositories.ForceAccountMappingRepository;
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

import com.sforce.soap.enterprise.sobject.Account_Mapping__c;

@Controller
public class AccountMapController {

	@Autowired
	private ForceAccountMappingRepository accountMapRepository;
		
	@RequestMapping(value = "/editor/{id}/acc_map", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("map_edit");
		Account_Mapping__c mapping__c = accountMapRepository.getById(id);
		if (mapping__c == null) {
			throw new NotFoundException();
		}
		modelAndView.addObject("accountMap", mapping__c);
		return modelAndView;
	}

	@RequestMapping(value = "/editor/save/acc_map", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> doEdit(@Valid @RequestBody AccountMapRequest request, Errors errors) {
		AjaxResponseBody ajax = new AjaxResponseBody();
		if (errors.hasErrors()) {
			for (ObjectError oError : errors.getAllErrors()) {
				ajax.addError(oError.getDefaultMessage());
			}
			return ResponseEntity.ok(ajax);
		}
		if (!accountMapRepository.doUpdate(request)) {
			ajax.addError("Error while trying to update the record");
		}		
		return ResponseEntity.ok(ajax);
	}
}
