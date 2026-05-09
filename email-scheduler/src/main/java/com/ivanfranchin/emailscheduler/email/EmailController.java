package com.ivanfranchin.emailscheduler.email;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ivanfranchin.emailscheduler.email.dto.CreateEmailRequest;
import com.ivanfranchin.emailscheduler.email.event.ScheduledEmail;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/scheduled-emails")
public class EmailController {

  private final ScheduledEmailProducer scheduledEmailProducer;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ScheduledEmail createScheduledEmail(@Valid @RequestBody CreateEmailRequest request) {
    ScheduledEmail scheduledEmail = ScheduledEmail.from(request);
    scheduledEmailProducer.send(scheduledEmail);
    return scheduledEmail;
  }
}
