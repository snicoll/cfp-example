package com.example.cfp.submission;

import java.util.List;

import com.example.cfp.domain.Submission;
import com.example.cfp.domain.SubmissionStatus;
import com.example.cfp.domain.Track;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SubmissionServiceTest.TestApplication.class)
public class SubmissionServiceTest {

	@Autowired
	private SubmissionService submissionService;

	@Test
	public void createTalk() throws Exception {
		SubmissionRequest request = new SubmissionRequest();
		request.setSpeaker("john@example.com", "John Smith");
		request.setTalk("Alice in Wonderland", "my abstract", Track.ALTERNATE_LANGUAGES);
		Submission submission = this.submissionService.create(request);
		assertThat(submission).isNotNull();
		assertThat(submission.getSpeakerEmail()).isEqualTo("john@example.com");
		assertThat(submission.getSpeakerName()).isEqualTo("John Smith");
		assertThat(submission.getTitle()).isEqualTo("Alice in Wonderland");
		assertThat(submission.getSummary()).isEqualTo("my abstract");
		assertThat(submission.getTrack()).isEqualTo(Track.ALTERNATE_LANGUAGES);
		assertThat(submission.getStatus()).isEqualTo(SubmissionStatus.DRAFT);

		List<Submission> submissions = this.submissionService.getSubmissionRepository()
				.findBySpeakerEmail("john@example.com");
		assertThat(submissions).hasSize(1);
	}

	@SpringBootApplication
	@EntityScan(basePackageClasses = Submission.class)
	@EnableJpaRepositories(basePackageClasses = Submission.class)
	static class TestApplication {

	}

}
