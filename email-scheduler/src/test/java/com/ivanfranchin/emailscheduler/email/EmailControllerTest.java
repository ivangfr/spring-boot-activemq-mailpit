package com.ivanfranchin.emailscheduler.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ivanfranchin.emailscheduler.email.dto.CreateEmailRequest;
import com.ivanfranchin.emailscheduler.email.event.ScheduledEmail;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

  @MockitoBean private ScheduledEmailProducer producer;

  @Autowired private EmailController emailController;

  @Test
  void createScheduledEmail_shouldSendScheduledEmail() {
    CreateEmailRequest request =
        new CreateEmailRequest("test@example.com", "Subject", "Body", 1000L);

    ScheduledEmail result = emailController.createScheduledEmail(request);

    ArgumentCaptor<ScheduledEmail> captor = ArgumentCaptor.forClass(ScheduledEmail.class);
    verify(producer).send(captor.capture());

    ScheduledEmail captured = captor.getValue();
    assertThat(captured.to()).isEqualTo("test@example.com");
    assertThat(captured.subject()).isEqualTo("Subject");
    assertThat(captured.body()).isEqualTo("Body");
    assertThat(captured.delay()).isEqualTo(java.time.Duration.ofMillis(1000));

    assertThat(result).isNotNull();
    assertThat(result.to()).isEqualTo("test@example.com");
  }
}
