package com.ivanfranchin.emailscheduler.email.dto;

import java.time.Instant;
import java.util.UUID;

import com.ivanfranchin.emailscheduler.email.model.EmailEntity;

public record EmailResponse(
    UUID id,
    String to,
    String subject,
    String body,
    Long delayInMillis,
    String status,
    Instant createdAt) {

  public static EmailResponse fromEntity(EmailEntity entity) {
    return new EmailResponse(
        entity.getId(),
        entity.getToEmail(),
        entity.getSubject(),
        entity.getBody(),
        entity.getDelayInMillis(),
        entity.getStatus().name(),
        entity.getCreatedAt());
  }
}
