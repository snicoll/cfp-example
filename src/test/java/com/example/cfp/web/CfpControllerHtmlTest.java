package com.example.cfp.web;

import java.io.IOException;

import com.example.cfp.domain.Submission;
import com.example.cfp.domain.Track;
import com.example.cfp.security.SecurityConfig;
import com.example.cfp.submission.SubmissionRequest;
import com.example.cfp.submission.SubmissionService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest(CfpController.class)
@Import(SecurityConfig.class)
public class CfpControllerHtmlTest {

	@Autowired
	private WebClient client;

	@MockBean
	private SubmissionService submissionService;

	@Test
	public void submitTalk() throws IOException {
		SubmissionRequest request = new SubmissionRequest();
		request.setName("John Smith");
		request.setEmail("jsmith@example.com");
		request.setTitle("Alice in Wonderland");
		request.setSummary("my abstract");
		request.setTrack(Track.ALTERNATE_LANGUAGES);
		given(this.submissionService.create(request)).willReturn(new Submission());

		HtmlPage page = this.client.getPage("/submit");
		HtmlForm form = page.getForms().get(0);
		form.getInputByName("name").setValueAttribute(request.getName());
		form.getInputByName("email").setValueAttribute(request.getEmail());
		form.getInputByName("title").setValueAttribute(request.getTitle());
		form.getTextAreaByName("summary").setText(request.getSummary());
		form.getSelectByName("track").setSelectedAttribute(
				request.getTrack().getId(), true);

		HtmlButton submit = page.getFirstByXPath("//button[@type='submit']");
		submit.click();
		verify(this.submissionService).create(request);
	}

}
