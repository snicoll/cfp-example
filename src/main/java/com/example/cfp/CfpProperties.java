package com.example.cfp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cfp")
public class CfpProperties {

	private final Github github = new Github();

	public Github getGithub() {
		return this.github;
	}


	public static class Github {

		/**
		 * Access token ("username:access_token") to query public github endpoints.
		 */
		private String token;

		public String getToken() {
			return this.token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}

}
