package com.minsait.onesait.microservice.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

	@RequestMapping("/")
	public String main(Model model, Principal principal) {
		if (principal != null)
			model.addAttribute("username", principal);
		return "index";
	}
}
