package com.piggymetrics.auth.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piggymetrics.auth.domain.User;
import com.piggymetrics.auth.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/current")
	public Principal getUser(Principal principal) {
		return principal;
	}

//	@PreAuthorize("hasAuthority('SCOPE_server')")
	@PostMapping
	public void createUser(@RequestBody User user) {
		userService.create(user);
	}
}
