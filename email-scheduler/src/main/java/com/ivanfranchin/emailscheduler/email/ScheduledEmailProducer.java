package com.ivanfranchin.emailscheduler.email;

import jakarta.jms.ObjectMessage;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ivanfranchin.emailscheduler.email.event.ScheduledEmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledEmailProducer {

  private final JmsTemplate jmsTemplate;

  @Value("${activemq.queue.scheduled-emails}")
  private String queue;

  @Async
  public void send(ScheduledEmail scheduledEmail) {
    log.info("Sending scheduled email {} to ActiveMQ", scheduledEmail);
    jmsTemplate.send(
        queue,
        messageCreator -> {
          ObjectMessage message = messageCreator.createObjectMessage(scheduledEmail);
          message.setLongProperty(
              ScheduledMessage.AMQ_SCHEDULED_DELAY, scheduledEmail.delay().toMillis());
          return message;
        });
  }
}
