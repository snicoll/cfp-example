package com.example.cfp.web;

import com.example.cfp.CfpApplication;
import com.example.cfp.domain.Submission;
import com.example.cfp.domain.Track;
import com.example.cfp.submission.SubmissionRequest;
import com.example.cfp.submission.SubmissionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { CfpApplication.class, CfpControllerTest.TestConfig.class })
@WebAppConfiguration
public class CfpControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private SubmissionService submissionService;

	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		reset(this.submissionService);
	}

	@Test
	public void submitTalk() throws Exception {
		SubmissionRequest request = new SubmissionRequest();
		request.setName("John Smith");
		request.setEmail("jsmith@example.com");
		request.setTitle("Alice in Wonderland");
		request.setSummary("my abstract");
		request.setTrack(Track.ALTERNATE_LANGUAGES);
		given(this.submissionService.create(request)).willReturn(new Submission());
		this.mvc.perform(post("/submit")
				.param("email", request.getEmail())
				.param("name", request.getName())
				.param("title", request.getTitle())
				.param("summary", request.getSummary())
				.param("track", request.getTrack().getId())
				.with(csrf()))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, "/submit?navSection=submit"));
		verify(this.submissionService).create(request);
	}

	@Configuration
	static class TestConfig {

		@Bean
		public SubmissionService submissionService() {
			return mock(SubmissionService.class);
		}

	}

}
