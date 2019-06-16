package com.example.cfp.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> cfpOAuth2UserService;

	public SecurityConfig(CfpOAuth2UserService cfpOAuth2UserService) {
		this.cfpOAuth2UserService = cfpOAuth2UserService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.oauth2Login().userInfoEndpoint().userService(this.cfpOAuth2UserService)
				.customUserType(CfpOAuth2User.class, "github");
		http.authorizeRequests()
				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/", "/news", "/login**", "/css/**", "/img/**", "/webjars/**", "/bootstrap/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.csrf()
				.ignoringAntMatchers("/admin/h2-console/*")
				.and()
			.logout()
				.logoutSuccessUrl("/")
				.permitAll()
				.and()
			.headers()
				.frameOptions().sameOrigin();
	}
	
}
