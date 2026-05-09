package com.ivanfranchin.emailscheduler.email.event;

import com.ivanfranchin.emailscheduler.email.dto.CreateEmailRequest;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record ScheduledEmail(
        String to,
        String subject,
        String body,
        Duration delay,
        Instant createdAt,
        Instant expectedReturnTime) implements Serializable {

    public static ScheduledEmail from(CreateEmailRequest createEmailRequest) {
        Instant now = Instant.now();
        return new ScheduledEmail(
                createEmailRequest.to(),
                Objects.requireNonNullElse(createEmailRequest.subject(), ""),
                Objects.requireNonNullElse(createEmailRequest.body(), ""),
                Duration.ofMillis(createEmailRequest.delayInMillis()),
                now,
                now.plusMillis(createEmailRequest.delayInMillis())
        );
    }
}