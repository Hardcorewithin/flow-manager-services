package com.flow.manager.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
			.antMatchers("/flow-manager/oauth2Callback**")
			.permitAll()
		.and().csrf()
		 .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	@Bean
	@ConfigurationProperties("google.client")
	public AuthorizationCodeResourceDetails google() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("google.resource")
	public ResourceServerProperties googleResource() {
		return new ResourceServerProperties();
	}
}
