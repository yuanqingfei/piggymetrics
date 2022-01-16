package com.piggymetrics.auth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.piggymetrics.auth.service.security.MongoUserDetailsService;

/**
 * @author cdov
 */
@Configuration
@Import(OAuth2AuthorizationServerConfiguration.class)
public class OAuth2AuthorizationConfig {
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		// @formatter:off
//		RegisteredClient loginClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("login-client")
//				.clientSecret("{noop}openid-connect")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/login-client")
//				.redirectUri("http://127.0.0.1:8080/authorized")
//				.scope(OidcScopes.OPENID)
//				.scope(OidcScopes.PROFILE)
//				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//				.build();

		RegisteredClient uiClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("browser")
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope("ui")
				.build();
		
		RegisteredClient accountClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("account-service")
				.clientSecret("{noop}account_secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope("server")
				.build();
		
		RegisteredClient statisticsClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("statistics-service")
				.clientSecret("{noop}stat_secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope("server")
				.build();
		
		RegisteredClient notificationClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("notification-service")
				.clientSecret("{noop}notification_secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope("server")
				.build();		

		return new InMemoryRegisteredClientRepository(uiClient, accountClient, statisticsClient, notificationClient);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// @formatter:off
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		// @formatter:on
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(KeyPair keyPair) {
		return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().issuer("http://localhost:5000/uaa").build();
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		// @formatter:off
//		UserDetails userDetails = User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//		// @formatter:on
//
//		return new InMemoryUserDetailsManager(userDetails);
//	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	
	@Bean
	public UserDetailsService userDetailsService() {
		return new MongoUserDetailsService();
	}
}
