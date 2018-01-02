package org.cubias.controllers;

import java.util.Map;

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

	@RequestMapping(value = "/content")
	public String content() {
		return "content";
	}
}
