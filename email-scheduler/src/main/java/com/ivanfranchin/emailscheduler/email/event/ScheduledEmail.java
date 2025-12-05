package com.ivanfranchin.emailscheduler.email.event;

import com.ivanfranchin.emailscheduler.email.dto.CreateEmailRequest;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public record ScheduledEmail(
        String to,
        String subject,
        String body,
        Duration delay,
        Instant createsAt,
        Instant expectedReturnTime) implements Serializable {

    public static ScheduledEmail from(CreateEmailRequest createEmailRequest) {
        Instant now = Instant.now();
        return new ScheduledEmail(
                createEmailRequest.to(),
                createEmailRequest.subject() == null ? "" : createEmailRequest.subject(),
                createEmailRequest.body() == null ? "" : createEmailRequest.body(),
                Duration.ofMillis(createEmailRequest.delayInMillis()),
                now,
                now.plusMillis(createEmailRequest.delayInMillis())
        );
    }
}