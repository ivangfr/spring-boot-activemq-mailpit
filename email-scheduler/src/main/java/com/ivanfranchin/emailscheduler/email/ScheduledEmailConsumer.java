package com.ivanfranchin.emailscheduler.email;

import java.time.Duration;
import java.time.Instant;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ivanfranchin.emailscheduler.email.event.ScheduledEmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledEmailConsumer {

  private final EmailSender emailSender;

  @JmsListener(destination = "${activemq.queue.scheduled-emails}")
  public void onMessage(@Payload ScheduledEmail scheduledEmail) {
    log.info(
        "Received the scheduled email {} from ActiveMQ with lag of {} ms",
        scheduledEmail,
        Duration.between(scheduledEmail.expectedReturnTime(), Instant.now()).toMillis());
    emailSender.send(scheduledEmail.to(), scheduledEmail.subject(), scheduledEmail.body());
  }
}
