package com.piggymetrics.account.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piggymetrics.account.config.FeignClientConfig;
import com.piggymetrics.account.domain.User;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class )
public interface AuthServiceClient {

	@RequestMapping(method = RequestMethod.POST, value = "/uaa/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void createUser(User user);

}
