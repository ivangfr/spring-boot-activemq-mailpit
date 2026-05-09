package com.ivanfranchin.emailscheduler.email;

import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ivanfranchin.emailscheduler.email.event.ScheduledEmail;

@ExtendWith(SpringExtension.class)
@Import(ScheduledEmailConsumer.class)
class ScheduledEmailConsumerTest {

  @Autowired private ScheduledEmailConsumer consumer;

  @MockitoBean private EmailSender emailSender;

  @Test
  void onMessage_shouldSendEmailWithCorrectArguments() {
    ScheduledEmail scheduledEmail =
        new ScheduledEmail(
            "test@example.com",
            "Subject",
            "Body",
            Duration.ofMillis(1000),
            Instant.now(),
            Instant.now().plusMillis(1000));

    consumer.onMessage(scheduledEmail);

    verify(emailSender).send("test@example.com", "Subject", "Body");
  }
}
