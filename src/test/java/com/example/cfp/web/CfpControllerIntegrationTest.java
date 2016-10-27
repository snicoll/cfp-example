package com.example.cfp.web;

import com.example.cfp.domain.SubmissionRepository;
import com.example.cfp.domain.Track;
import com.example.cfp.submission.SubmissionRequest;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Java6Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CfpControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private SubmissionRepository submissionRepository;

	@Test
	public void submitTalk() throws Exception {
		SubmissionRequest request = new SubmissionRequest();
		request.setName("John Smith");
		request.setEmail("jsmith@example.com");
		request.setTitle("Alice in Wonderland");
		request.setSummary("my abstract");
		request.setTrack(Track.ALTERNATE_LANGUAGES);
		this.mvc.perform(post("/submit")
				.param("email", request.getEmail())
				.param("name", request.getName())
				.param("title", request.getTitle())
				.param("summary", request.getSummary())
				.param("track", request.getTrack().getId())
				.with(csrf()))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, "/submit?navSection=submit"));
		assertThat(this.submissionRepository
				.findBySpeakerEmail("jsmith@example.com")).hasSize(1);
	}

}
