package com.piggymetrics.account.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.service.AccountService;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

//	@GetMapping("/")
//	public String index(@AuthenticationPrincipal Jwt jwt) {
//		return String.format("Hello, %s!", jwt.getSubject());
//	}

	@PreAuthorize("hasAuthority('SCOPE_server') or #name.equals('demo')")
	@GetMapping("/{name}")
	public Account getAccountByName(@PathVariable String name) {
		return accountService.findByName(name);
	}

	@GetMapping("/current")
	public Account getCurrentAccount(Principal principal) {
		return accountService.findByName(principal.getName());
	}

	@PutMapping("/current")
	public void saveCurrentAccount(Principal principal, @Valid @RequestBody Account account) {
		accountService.saveChanges(principal.getName(), account);
	}

	@PostMapping("/")
	public Account createNewAccount(@Valid @RequestBody User user) {
		return accountService.create(user);
	}
}
