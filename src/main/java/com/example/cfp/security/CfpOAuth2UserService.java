package com.example.cfp.security;

import java.util.HashSet;
import java.util.Set;

import com.example.cfp.CfpProperties;
import com.example.cfp.domain.User;
import com.example.cfp.domain.UserRepository;
import com.example.cfp.integration.github.GithubClient;
import com.example.cfp.integration.github.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 *
 * @author Stephane Nicoll
 */
@Component
public class CfpOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static final Logger logger = LoggerFactory.getLogger(CfpOAuth2UserService.class);

	private final CfpProperties cfpProperties;

	private final GithubClient githubClient;

	private final UserRepository userRepository;

	private final DefaultOAuth2UserService delegate;

	public CfpOAuth2UserService(CfpProperties cfpProperties, GithubClient githubClient,
			UserRepository userRepository) {
		this.cfpProperties = cfpProperties;
		this.githubClient = githubClient;
		this.userRepository = userRepository;
		this.delegate = new DefaultOAuth2UserService();
	}

	@Override
	public CfpOAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User user = delegate.loadUser(oAuth2UserRequest);
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		String username = (String) user.getAttributes().get("login");
		if (this.cfpProperties.getSecurity().getAdmins().contains(username)) {
			mappedAuthorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(
					"ROLE_USER,ROLE_ADMIN"));
		}
		else {
			mappedAuthorities.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(
					"ROLE_USER"));
		}
		return new CfpOAuth2User(mappedAuthorities, user.getAttributes(), getOrCreateSpeaker(username));
	}


	private User getOrCreateSpeaker(String username) {
		User speaker = userRepository.findByGithub(username);
		if (speaker == null) {
			logger.info("Initialize user with githubId {}", username);
			GithubUser user = githubClient.getUser(username);
			speaker = new User();
			speaker.setEmail(user.getEmail());
			speaker.setName(user.getName());
			speaker.setGithub(username);
			speaker.setAvatarUrl(user.getAvatar());
			userRepository.save(speaker);
		}
		return speaker;
	}

}
