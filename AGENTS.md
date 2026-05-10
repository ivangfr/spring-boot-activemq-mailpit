# AGENTS.md

## Project

Single-module Maven project: `email-scheduler` is the only application module under the root parent POM.

- Java 25, Spring Boot 4.0.6
- Runtime dependencies: ActiveMQ Classic, PostgreSQL, Mailpit (SMTP)

## Commands

Always use the Maven wrapper — never bare `mvn`.

```bash
# Start required infrastructure (run before the app or full-context tests)
podman compose up -d

# Run the application
./mvnw clean spring-boot:run --projects email-scheduler

# Build
./mvnw clean package --projects email-scheduler

# Run all tests (no live services needed — see Testing section)
./mvnw clean test

# Run a single test class
./mvnw test -pl email-scheduler -Dtest=EmailControllerTest

# Run a single test method
./mvnw test -pl email-scheduler -Dtest="EmailSenderTest#methodName"

# Fix formatting (do this before verify/install)
./mvnw spotless:apply

# Check formatting
./mvnw spotless:check
```

## Formatting

`spotless:check` is bound to the `verify` lifecycle phase — `./mvnw verify` or `./mvnw clean install` will **fail** on formatting violations. Always run `./mvnw spotless:apply` before those phases.

- Java: Google Java Format (AOSP style), import order `java | jakarta | org | com`, no wildcards
- JS/HTML under `src/main/resources/static/`: Prettier 3.5.3

## Testing

The unit/slice test suite requires **no running services**. Tests use `@WebMvcTest`, `@ExtendWith(SpringExtension)` + `@Import`, and Mockito.

`EmailSchedulerApplicationTests` is intentionally `@Disabled` — it requires a live PostgreSQL + ActiveMQ stack (`podman compose up -d`). Do not re-enable it without the compose stack running.

## Architecture Notes

- `EmailService` saves the entity as `PENDING` or `IMMEDIATE`, then publishes to ActiveMQ with a JMS delivery delay.
- `EmailConsumer` re-fetches the entity from PostgreSQL and only sends the email if status is still `PENDING` or `IMMEDIATE` — this is the cancellation guard; skipping the re-fetch would break cancellation.
- Schema is managed by `spring.jpa.hibernate.ddl-auto=update` — no Flyway/Liquibase.
- Web UI is a plain `src/main/resources/static/index.html` (no template engine).

## Infrastructure (docker-compose / podman compose)

| Service   | Ports                          | Notes                              |
|-----------|--------------------------------|------------------------------------|
| activemq  | 61616 (JMS), 8161 (Admin UI)   | Custom config via `activemq/activemq.xml`; credentials `admin`/`admin` |
| mailpit   | 1025 (SMTP), 8025 (Web UI)     |                                    |
| postgres  | 5432                           | DB: `emaildb`, user/pass `postgres`/`postgres` |

## Key Environment Overrides

| Variable              | Default                      |
|-----------------------|------------------------------|
| `ACTIVEMQ_BROKER_URL` | `tcp://localhost:61616`      |
| `ACTIVEMQ_USER`       | `admin`                      |
| `ACTIVEMQ_PASSWORD`   | `admin`                      |
