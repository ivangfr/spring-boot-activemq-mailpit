package com.ivanfranchin.emailscheduler.email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ivanfranchin.emailscheduler.email.model.EmailEntity;
import com.ivanfranchin.emailscheduler.email.model.EmailStatus;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, UUID> {

  List<EmailEntity> findByStatus(EmailStatus status);

  Optional<EmailEntity> findByIdAndStatus(UUID id, EmailStatus status);
}
