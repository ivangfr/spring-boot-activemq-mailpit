package com.ivanfranchin.emailscheduler.email;

import com.ivanfranchin.emailscheduler.email.dto.CreateEmailRequest;
import com.ivanfranchin.emailscheduler.email.event.ScheduledEmail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/scheduled-emails")
public class EmailController {

    private final ScheduledEmailProducer scheduledEmailProducer;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createScheduledEmail(@Valid @RequestBody CreateEmailRequest request) {
        scheduledEmailProducer.send(ScheduledEmail.from(request));
    }
}
