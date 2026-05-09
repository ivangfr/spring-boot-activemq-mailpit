package com.ivanfranchin.emailscheduler.email.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.ivanfranchin.emailscheduler.email.dto.CreateEmailRequest;

class ScheduledEmailTest {

  @Test
  void from_shouldCreateScheduledEmailWithCorrectFields() {
    CreateEmailRequest createEmailRequest =
        new CreateEmailRequest("test@example.com", "Subject", "Body", 5000L);
    Instant before = Instant.now();

    ScheduledEmail scheduledEmail = ScheduledEmail.from(createEmailRequest);

    Instant after = Instant.now();
    assertThat(scheduledEmail.to()).isEqualTo("test@example.com");
    assertThat(scheduledEmail.subject()).isEqualTo("Subject");
    assertThat(scheduledEmail.body()).isEqualTo("Body");
    assertThat(scheduledEmail.delay()).isEqualTo(Duration.ofMillis(5000));
    assertThat(scheduledEmail.createdAt()).isBetween(before, after.plusMillis(1));
    assertThat(scheduledEmail.expectedReturnTime())
        .isBetween(before.plusMillis(5000), after.plusMillis(5001));
  }

  @Test
  void from_nullSubjectAndBody_shouldDefaultToEmptyString() {
    CreateEmailRequest createEmailRequest =
        new CreateEmailRequest("test@example.com", null, null, 1000L);

    ScheduledEmail scheduledEmail = ScheduledEmail.from(createEmailRequest);

    assertThat(scheduledEmail.subject()).isEmpty();
    assertThat(scheduledEmail.body()).isEmpty();
  }

  @Test
  void from_zeroDelay_shouldSetExpectedReturnTimeEqualToCreatedAt() {
    CreateEmailRequest createEmailRequest =
        new CreateEmailRequest("test@example.com", "Subject", "Body", 0L);

    ScheduledEmail scheduledEmail = ScheduledEmail.from(createEmailRequest);

    assertThat(scheduledEmail.delay()).isEqualTo(Duration.ofMillis(0));
    assertThat(scheduledEmail.expectedReturnTime()).isEqualTo(scheduledEmail.createdAt());
  }
}
