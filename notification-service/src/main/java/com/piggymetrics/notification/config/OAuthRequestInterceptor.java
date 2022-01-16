package com.piggymetrics.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OAuthRequestInterceptor implements RequestInterceptor {

	@Autowired
	private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

	@Override
	public void apply(RequestTemplate template) {
		template.header(HttpHeaders.AUTHORIZATION, getAuthorizationToken());
	}

	private String getAuthorizationToken() {
		
		OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId("account-service")
				.principal("feign client")
				.build();
		final OAuth2AccessToken accessToken = oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest)
				.getAccessToken();
		
		return String.format("%s %s", accessToken.getTokenType().getValue(), accessToken.getTokenValue());
	}

}
