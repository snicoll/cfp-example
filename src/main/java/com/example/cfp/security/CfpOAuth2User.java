package com.example.cfp.security;

import java.util.Collection;
import java.util.Map;

import com.example.cfp.domain.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

/**
 *
 * @author Stephane Nicoll
 */
public class CfpOAuth2User extends DefaultOAuth2User {

	private final User user;

	public CfpOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, User user) {
		super(authorities, attributes, "id");
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

}
