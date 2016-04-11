package com.example.cfp.domain;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SubmissionTest.TestApplication.class)
public class SubmissionTest {

	@Autowired
	private SubmissionRepository submissionRepository;

	@Test
	public void newSubmissionHasDraftStatus() {
		Submission submission = new Submission();
		submission.setSpeakerEmail("john@example.com");
		Submission saved = this.submissionRepository.save(submission);
		assertThat(saved.getStatus()).isEqualTo(SubmissionStatus.DRAFT);
	}

	@Test
	public void findBySpeakerEmail() {
		this.submissionRepository.save(createDummySubmission("john@example.com", "Foo"));
		this.submissionRepository.save(createDummySubmission("john@example.com", "Bar"));

		List<Submission> submissions = this.submissionRepository.findBySpeakerEmail("john@example.com");
		assertThat(submissions).hasSize(2);
	}

	private Submission createDummySubmission(String email, String title) {
		Submission submission = new Submission();
		submission.setSpeakerEmail(email);
		submission.setTitle(title);
		submission.setSummary("Live coding 4tw");
		submission.setTrack(Track.SERVER_SIDE_JAVA);
		return submission;
	}

	@SpringBootApplication
	static class TestApplication {

	}

}
