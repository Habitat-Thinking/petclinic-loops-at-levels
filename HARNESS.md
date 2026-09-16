# Harness — petclinic-loops-at-levels

## Context

### Stack

- Java 17 (`<java.version>17</java.version>`), Spring Boot, Thymeleaf
- Maven via the wrapper (`./mvnw`); Gradle also present upstream
- Tests: JUnit 5, Spring Boot test slices, Testcontainers (MySQL), Spring Boot
  Docker Compose (Postgres)
- Databases: H2 (default), MySQL, Postgres — three schema files kept in step
- Formatting: spring-javaformat, enforced by the build
- i18n: `messages.properties` plus 10 locale bundles, checked by `I18nPropertiesSyncTest`

### Conventions

Extracted 2026-09-16. These are **must-follow** rules, but at Level 2 they
are advisory: nothing below is verified by a tool or an agent, except where
noted. Should-follow conventions and style preferences live in `CLAUDE.md`.

1. **Layering stays as it is.** Controller → repository → entity, one package
   per feature (`owner`, `vet`, `system`), shared base types in `model`. No
   service layer and no other new architectural layers. A new feature package
   is fine if it follows the same shape.
2. **Schema changes land in all three databases.** Any change to
   `db/*/schema.sql` lands in `h2`, `mysql` and `postgres` in the same change,
   with each `data.sql` updated to match. The `.txt` setup notes and
   `user.sql` are out of scope.
3. **Templates use message keys.** No hardcoded display text in templates;
   all of it comes from `messages*.properties` keys.
4. **Real translations only.** Never copy English text into non-English
   locale bundles to satisfy `I18nPropertiesSyncTest`. If a trustworthy
   translation isn't available, add the key to `messages.properties` only,
   let the sync test fail, and flag the missing locales for a human.
5. **No secrets in the repository.** No secrets, keys or tokens in code,
   config or test fixtures. Allowed exception: throwaway local-container
   credentials (`petclinic`/`petclinic` in `docker-compose.yml` and the
   fallbacks in `application-{mysql,postgres}.properties`).
6. **Owner personal data stays out of output.** `firstName`, `lastName`,
   `address`, `city` and `telephone` are never logged, never in error
   messages, never in test output. Fixtures use obviously fictional data.
7. **Escaping stays on.** No `th:utext` on user-supplied values.
8. **Every dependency is a supply-chain decision.** No new dependency without
   a stated reason, confirmation that the existing stack can't do the job, and
   provenance: from Maven Central, version pinned, approved by a human in the
   PR.
9. **No failing or skipped tests.** No new `@Disabled`, `assume*` calls, or
   deleted or commented-out tests. Any change touching `db/**`, entities or
   repositories must show that `MySqlIntegrationTests` and
   `PostgresIntegrationTests` actually ran — not skipped for lack of Docker.
10. **Formatting is clean.** `./mvnw spring-javaformat:validate` passes.
    (Already enforced by the build: the plugin runs in the `validate` phase.)
11. **No unrelated reformatting.** Every changed hunk is needed by the
    change's stated purpose; reformatting untouched code drowns the review.
12. **Judgement calls are surfaced.** Every choice the task didn't state —
    required vs optional, nullability, length limits, defaults, user-visible
    naming, error handling — is listed in a "Decisions" section of the PR
    description.

## Constraints

None at this level. Everything in this repository is advisory: it is written down,
and nothing verifies it. That is the point of Level 2.
