package com.piggymetrics.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	String jwkSetUri;
	
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
	}
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
		        .httpBasic().disable()  
		        .formLogin(AbstractHttpConfigurer::disable)  
		        .csrf(AbstractHttpConfigurer::disable)
		        .sessionManagement(sessionManagement ->  
	            	sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  
				.authorizeHttpRequests((authorize) -> authorize
					.antMatchers("/", "/demo").permitAll()
					.antMatchers(HttpMethod.GET, "/notifications/**").hasAuthority("SCOPE_server")
					.antMatchers(HttpMethod.POST, "/notifications/**").hasAuthority("SCOPE_server")					
					.anyRequest().authenticated())
			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    	
        return http.build();
    }
}
