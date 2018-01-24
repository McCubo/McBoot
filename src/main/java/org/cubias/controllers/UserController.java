package org.cubias.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.cubias.config.AjaxResponseBody;
import org.cubias.entities.FowRole;
import org.cubias.entities.FowUser;
import org.cubias.error.NotFoundException;
import org.cubias.pojos.AutocompleteResponse;
import org.cubias.pojos.UserRequest;
import org.cubias.repositories.FowRoleRepository;
import org.cubias.repositories.FowUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class UserController {

	@Autowired
	private FowUserRepository userRepo;

	@Autowired
	private FowRoleRepository roleRepo;

	@Value("${wd.web_service}")
	private String webService;

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

	@RequestMapping(value = "/admin/new/user", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView modelAndView = new ModelAndView("user_new");
		modelAndView.addObject("roleList", roleRepo.findAll());
		return modelAndView;
	}

	@RequestMapping(value = "/admin/search/user", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getEmployeeById(HttpServletRequest request) {
		ArrayList<AutocompleteResponse> ajax = new ArrayList<>();
		String term = request.getParameter("term");
		try {
			URL url = new URL(webService + term);
			HttpURLConnection webRequest = (HttpURLConnection) url.openConnection();
			webRequest.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) webRequest.getContent()));
			if (!root.isJsonArray()) {
				JsonObject rootObj = root.getAsJsonObject();
				String employee_id = rootObj.get("employee_id").getAsJsonObject().get("value").getAsString();
				String network_login = rootObj.get("network_login").getAsJsonObject().get("value").getAsString();
				String unit_id = rootObj.get("unit_id").getAsJsonObject().get("value").getAsString();
				ajax.add(new AutocompleteResponse(employee_id, network_login, unit_id));
			} else {
				ajax.add(new AutocompleteResponse());
			}
		} catch (MalformedURLException e) {
			ajax.add(new AutocompleteResponse());
		} catch (IOException e) {
			ajax.add(new AutocompleteResponse());
		}
		return ResponseEntity.ok(ajax);
	}

	@RequestMapping(value = "/admin/insert/user", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody UserRequest userRequest) {
		AjaxResponseBody ajax = new AjaxResponseBody();
		if (userRequest.getAdUser() == null || userRequest.getWdId() == null) {
			ajax.addError("Please, select a valid user");
			return ResponseEntity.ok(ajax);
		}
		FowUser dbUser = userRepo.findOneByUseAdUser(userRequest.getAdUser());
		if (dbUser == null) {
			FowRole role = roleRepo.findOne(userRequest.getRoleId());
			FowUser user = new FowUser(role, userRequest.getWdId(), userRequest.getAdUser(), new Date());
			userRepo.save(user);
		} else {
			ajax.addError("There is already an user with the given Windows Login");
		}

		return ResponseEntity.ok(ajax);
	}
}
