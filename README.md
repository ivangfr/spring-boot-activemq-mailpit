# spring-boot-activemq-mailpit

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-ivan.franchin-FFDD00?logo=buymeacoffee&logoColor=black)](https://buymeacoffee.com/ivan.franchin)

This project shows how to implement an email scheduling application.

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Project Overview

```mermaid
flowchart TB
    subgraph users ["Users"]
        HTTP["REST Clients"]
        Browser["Browser"]
    end

    subgraph email-scheduler ["email-scheduler:8080\n(Spring Boot)"]
        RestCtrl["EmailController\nPOST /api/scheduled-emails"]
        WebUI["Web UI\n(Thymeleaf)"]
        JMSProducer["ScheduledEmailProducer"]
        JMSConsumer["ScheduledEmailConsumer"]
        MailSender["EmailSender"]
    end

    subgraph activemq ["ActiveMQ:8161"]
        Queue[("email-queue")]
    end

    subgraph mailpit ["Mailpit:8025"]
        SMTP["SMTP Server\n:1025"]
        MailpitUI["Mailpit UI"]
    end

    HTTP -->|"POST /api/scheduled-emails"| RestCtrl
    Browser -->|"accesses"| WebUI
    WebUI -->|"submits form"| RestCtrl
    Browser -->|"views emails"| MailpitUI
    RestCtrl -->|"schedules message"| JMSProducer
    JMSProducer -->|"publishes"| Queue
    Queue -->|"delivers"| JMSConsumer
    JMSConsumer -->|"sends email"| MailSender
    MailSender -->|"SMTP"| SMTP
```

## Applications

- ### email-scheduler

  [Spring Boot](https://spring.io/projects/spring-boot) Java web application that provides REST API and a web UI for sending emails immediately or scheduling emails. It uses [`ActiveMQ`](https://activemq.apache.org/) as the message broker to handle email scheduling and [`Mailpit`](https://mailpit.axllent.org/) as a local SMTP server to capture and display sent emails.

  Endpoint:
  - `POST /api/scheduled-emails -d {"to": "...", "subject": "...", "body": "...", "delayInMillis": ...}` - Send an email immediately or schedule it for a later time.

## Prerequisites

- [`Java 25`](https://www.oracle.com/java/technologies/downloads/#java25) or higher;
- A containerization tool (e.g., [`Docker`](https://www.docker.com), [`Podman`](https://podman.io), etc.)

## Start Docker Compose services

In a terminal and inside the `spring-boot-activemq-mailpit` root folder run:
```bash
podman compose up -d
```

## Running application using Maven

In a terminal and inside the `spring-boot-activemq-mailpit` root folder, run the command below:
```bash
./mvnw clean spring-boot:run --projects email-scheduler
```

## Simulation

- Open a browser and access `Mailpit` at http://localhost:8025
- Open another browser and access `email-scheduler` application at http://localhost:8080
- Fill in the email scheduling form. You can send the email immediately or schedule it for a later time.
- Check the `Mailpit` at the scheduled time to see the received email.

## Demo

![demo](documentation/demo.gif)

## Useful links

- **ActiveMQ**

  - Access http://localhost:8161
  - Click `Manage ActiveMQ broker`.
  - To log in, use `admin` for both username and password.

- **Mailpit**

  - Access http://localhost:8025

## Shutdown

To stop and remove Docker Compose containers, network, and volumes, go to a terminal and, inside the `spring-boot-activemq-mailpit` root folder, run the following command:
```bash
podman compose down -v
```

## Running Tests

In a terminal and inside the `spring-boot-activemq-mailpit` root folder, run the following command:
```bash
./mvnw clean test
```

## Support

If you find this useful, consider buying me a coffee:

<a href="https://buymeacoffee.com/ivan.franchin"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" height="50"></a>

## License

This project is licensed under the [MIT License](./LICENSE).
