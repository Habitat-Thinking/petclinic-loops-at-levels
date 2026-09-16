# Directives — petclinic-loops-at-levels

The single durable source of what this project expects from anyone working in it,
human or agent. `CLAUDE.md`, `.github/copilot-instructions.md` and any other
vendor file point here and add nothing of their own.

Extracted from the maintainer on 2026-09-16 via `/extract-conventions`, answering
five questions about tacit team knowledge.

**At this level everything here is advisory.** It is written down, and nothing
verifies it. What that is worth, and where it runs out, is the subject of the next
level.

## Must follow

1. **Layering stays as it is.** Controller → repository → entity, one package per
   feature (`owner`, `vet`, `system`), shared base types in `model`. No service layer
   and no other new architectural layers. A new feature package is fine if it follows
   the same shape.
2. **Schema changes land in all three databases.** Any change to `db/*/schema.sql`
   lands in `h2`, `mysql` and `postgres` in the same change, with each `data.sql`
   updated to match. The `.txt` setup notes and `user.sql` are out of scope.
3. **Templates use message keys.** No hardcoded display text in templates; all of it
   comes from `messages*.properties` keys.
4. **Real translations only.** Never copy English text into non-English locale bundles
   to satisfy `I18nPropertiesSyncTest`. If a trustworthy translation isn't available,
   add the key to `messages.properties` only, let the sync test fail, and flag the
   missing locales for a human.
5. **No secrets in the repository.** No secrets, keys or tokens in code, config or test
   fixtures. Allowed exception: throwaway local-container credentials
   (`petclinic`/`petclinic` in `docker-compose.yml` and the fallbacks in
   `application-{mysql,postgres}.properties`).
6. **Owner personal data stays out of output.** `firstName`, `lastName`, `address`,
   `city` and `telephone` are never logged, never in error messages, never in test
   output. Fixtures use obviously fictional data.
7. **Escaping stays on.** No `th:utext` on user-supplied values.
8. **Every dependency is a supply-chain decision.** No new dependency without a stated
   reason, confirmation that the existing stack can't do the job, and provenance: from
   Maven Central, version pinned, approved by a human in the PR.
9. **No failing or skipped tests.** No new `@Disabled`, `assume*` calls, or deleted or
   commented-out tests. Any change touching `db/**`, entities or repositories must show
   that `MySqlIntegrationTests` and `PostgresIntegrationTests` actually ran — not
   skipped for lack of Docker.
10. **Formatting is clean.** `./mvnw spring-javaformat:validate` passes. (Already
    enforced by the build: the plugin runs in the `validate` phase.)
11. **No unrelated reformatting.** Every changed hunk is needed by the change's stated
    purpose; reformatting untouched code drowns the review.
12. **Judgement calls are surfaced.** Every choice the task didn't state — required vs
    optional, nullability, length limits, defaults, user-visible naming, error handling
    — is listed in a "Decisions" section of the PR description.

## Should follow

Exceptions are allowed, but state the reason in the PR's "Decisions" section.

1. **Reuse the existing fragments.** Form fields use `fragments/inputField` and
   `fragments/selectField`. Add a new fragment only for a genuinely new kind of field,
   and extend an existing fragment file where possible.
2. **Tests check behaviour a user can observe.** Use `@WebMvcTest` with MockMvc and
   assert on status, view name, model attributes and rendered content. Unit tests on
   validators and entities are fine when they assert behaviour (e.g. "`PetValidator`
   rejects a blank name"), never structure or private state.
3. **No abstraction for a single caller.** An interface or generic needs a second real
   user before it earns its place. Spring Data repository interfaces (`OwnerRepository`,
   `PetTypeRepository`, `VetRepository`) are the exception — the interface is the
   implementation.
4. **Smallest diff that works.** Extend an existing file or fragment before creating a
   new one.

## Style preferences

1. **Match PetClinic's plainness.** This is a teaching codebase: prefer the boring
   construction a newcomer can read.
2. **Deleting beats adding.** A refactoring that removes code is worth more than one
   that adds a layer.

## Not encodable yet

These were raised in extraction and could not be written as rules anyone could check.
They are recorded so they are not lost, and so the gap is visible.

- **"Plainness"** needs decomposing into observable cases (loops over clever streams,
  no reflection or annotation tricks) before it can be checked.
- **"Needed by the change's purpose"** (the no-unrelated-reformatting rule) needs worked
  examples to settle where cleanup ends and noise begins.
