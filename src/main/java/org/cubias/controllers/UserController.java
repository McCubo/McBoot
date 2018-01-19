package org.cubias.controllers;

import org.cubias.config.AjaxResponseBody;
import org.cubias.entities.FowRole;
import org.cubias.entities.FowUser;
import org.cubias.error.NotFoundException;
import org.cubias.pojos.UserRequest;
import org.cubias.repositories.FowRoleRepository;
import org.cubias.repositories.FowUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@Autowired
	private FowUserRepository userRepo;

	@Autowired
	private FowRoleRepository roleRepo;

	@RequestMapping(value = "/admin/user_list", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("user_list");
		modelAndView.addObject("userList", userRepo.findAll());
		return modelAndView;
	}

	@RequestMapping(value = "/admin/{id}/edit/user", method = RequestMethod.GET)
	public ModelAndView showById(@PathVariable Integer id) {
		ModelAndView modelAndView = new ModelAndView("user_edit");
		FowUser user = userRepo.findOne(id);
		if (user == null) {
			throw new NotFoundException();
		}
		modelAndView.addObject("roleList", roleRepo.findAll());
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@RequestMapping(value = "/admin/save/user", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody UserRequest userRequest) {
		AjaxResponseBody ajax = new AjaxResponseBody();
		FowRole role = roleRepo.findOne(userRequest.getRoleId());
		FowUser user = userRepo.findOne(userRequest.getId());
		user.setUseRole(role);
		userRepo.save(user);
		return ResponseEntity.ok(ajax);
	}
}
