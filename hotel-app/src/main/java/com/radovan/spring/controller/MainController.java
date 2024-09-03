package com.radovan.spring.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.service.GuestService;

@Controller
public class MainController {

	@Autowired
	private GuestService guestService;

	@GetMapping(value = "/")
	public String indexPage() {
		return "index";
	}

	@GetMapping(value = "/home")
	public String homePage() {
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/login")
	public String login() {
		return "fragments/login :: fragmentContent";
	}

	@PostMapping(value = "/loginPassConfirm")
	public String confirmLoginPass(Principal principal) {
		Optional<Principal> authPrincipal = Optional.ofNullable(principal);
		if (!authPrincipal.isPresent()) {
			Error error = new Error("Invalid user");
			throw new InstanceUndefinedException(error);
		}

		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/loginErrorPage")
	public String logError(ModelMap map) {
		map.put("alert", "Invalid username or password!");
		return "fragments/login :: fragmentContent";
	}

	@PostMapping(value = "/loggedout")
	public String logout() {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
		SecurityContextHolder.clearContext();
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/contactInfo")
	public String getContactInfo() {
		return "fragments/contact :: fragmentContent";
	}

	@GetMapping(value = "/register")
	public String userRegistration(ModelMap map) {
		RegistrationForm registerForm = new RegistrationForm();
		map.put("registerForm", registerForm);
		return "fragments/registration :: fragmentContent";
	}

	@PostMapping(value = "/register")
	public String registerUser(@ModelAttribute("registerForm") RegistrationForm registerForm) {
		guestService.storeGuest(registerForm);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/registerComplete")
	public String registrationCompleted() {
		return "fragments/registration_completed :: fragmentContent";
	}

	@GetMapping(value = "/registerFail")
	public String registrationFailed() {
		return "fragments/registration_failed :: fragmentContent";
	}
}
