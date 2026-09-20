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

The project's directives live in [AGENTS.md](AGENTS.md), which is the single
source. They are not repeated here, so this file cannot drift from them.

## Constraints

The project's rules live in [AGENTS.md](AGENTS.md) and are not restated here.
This section declares which of them are checked, by what, and when.

### Consistent formatting

- **Rule**: All source files must pass the project's configured formatter
  without changes
- **Enforcement**: deterministic
- **Tool**: `./mvnw -B spring-javaformat:validate` (also bound to the Maven
  `validate` phase, so it runs on every build); in CI via
  `.github/workflows/harness.yml`
- **Scope**: pr

### Tests must pass

- **Rule**: The project's test suite must pass with zero failures before
  any code is merged
- **Enforcement**: deterministic
- **Tool**: `./mvnw -B verify`; in CI via `.github/workflows/harness.yml`
- **Scope**: pr

### Decision record for source changes

Promoted from candidate (1) in the 2026-09-16 entry of REFLECTION_LOG.md.
The check is split into two blocks because each block has one scope: a warning
at commit time and a gate on pull requests. Both run the same script. It reads
the changed file list and nothing else, so it checks that a record was added or
updated. It does not check what the record says.

Script contract: `scripts/check-decision-record.sh --staged` checks the staged
change; `scripts/check-decision-record.sh <base-ref>` checks `<base-ref>...HEAD`.
The script exits non-zero when files under `src/` changed and no record under
`decisions/` was added or updated.

#### Decision record for source changes — commit

- **Rule**: Any commit or pull request that changes files under src/ adds or
  updates a decision record under decisions/ in the same change. The record
  names each judgement call the task did not state - required vs optional,
  nullability, limits, defaults, naming, error handling - or states explicitly
  that there were none.
- **Enforcement**: deterministic
- **Tool**: `scripts/check-decision-record.sh --staged`, run by
  `.githooks/pre-commit`, which warns and never blocks (enable per clone with
  `git config core.hooksPath .githooks`)
- **Scope**: commit

#### Decision record for source changes — pull request

- **Rule**: Any commit or pull request that changes files under src/ adds or
  updates a decision record under decisions/ in the same change. The record
  names each judgement call the task did not state - required vs optional,
  nullability, limits, defaults, naming, error handling - or states explicitly
  that there were none.
- **Enforcement**: deterministic
- **Tool**: `scripts/check-decision-record.sh <PR base SHA>`; in CI via
  `.github/workflows/harness.yml`, where a failure blocks the pull request
- **Scope**: pr

### Schema parity across databases

- **Rule**: Every column present in `db/h2/schema.sql` exists in `db/mysql/schema.sql`
  and `db/postgres/schema.sql`, and where h2 and mysql both declare a length limit for
  a column, the two agree. Postgres uses `TEXT` throughout and declares no lengths, so
  only column presence is compared there.
- **Enforcement**: deterministic
- **Tool**: `SchemaParityTest` (plain JUnit, no additional dependency), run by
  `./mvnw verify`, by the edit-time advisory hook, and by CI
- **Scope**: commit, pr
- **Why**: a column added to one database and forgotten in the others is invisible
  until something runs against that database. Without Docker, that may be nobody until
  production. This is directive 2 in `AGENTS.md`, made checkable.

### Abstraction earns its place — advisory

- **Rule**: An interface, generic or base class is introduced only when a second real
  caller exists. Spring Data repository interfaces are the standing exception.
- **Enforcement**: **agent** — advisory, and weaker than every other constraint here
- **Tool**: reviewer judgement, human or agent. No tool decides this.
- **Scope**: pr
- **Why it cannot be deterministic, stated plainly**: "earns its place" is a judgement
  about design, and the honest test — would a second caller arrive? — is about the
  future. A linter can count callers; it cannot tell a premature abstraction from a
  deliberate seam. Encoding it as a rule that counts callers would produce a rule that
  is precise and wrong, and people would learn to route around it.
  It is kept, and labelled, because the alternative — quietly pretending every rule is
  enforceable — is worse. When this one is violated, a person has to notice.

---

## Garbage Collection

### Reflection-driven regression detection

- **What it checks**: Whether REFLECTION_LOG.md contains recurring
  failure patterns (same type of surprise across 2+ entries) that
  are not yet covered by a HARNESS.md constraint
- **Frequency**: weekly
- **Enforcement**: agent
- **Tool**: harness-gc agent
- **Auto-fix**: false

---

### Scheduled sweep (CI)

- **Rule**: `.github/workflows/harness.yml` job `sweep` runs every Monday at 07:00 UTC
  and on demand. It reports, and never blocks:
  1. source changes since the last tag with no decision record
  2. more than 10 active reflection entries awaiting promotion or retirement
  3. constraints in this file naming a tool that no longer exists
- **Enforcement**: deterministic
- **Scope**: scheduled
- **Why**: entropy does not arrive with a pull request attached. Check 3 exists because
  a harness whose rules name missing tools is worse than no harness: it reports success
  for checks nobody is running.

## Observability

<!-- Not yet configured. Run /harness-init and select this feature to set up. -->

### Operating cadence

<!-- Not yet configured. Run /harness-init and select this feature to set up. -->

### Health thresholds

<!-- Not yet configured. Run /harness-init and select this feature to set up. -->

### Regression detection

<!-- Not yet configured. Run /harness-init and select this feature to set up. -->

---

## Cognitive reservoir

Advisory watch on the human verifier the harness cannot verify. Every constraint
above this line bounds what may happen to the code. This block watches whether the
person who has to answer for the code is still in a state to do so.

**NOT a Constraint.** It never gates CI, never blocks a commit, a merge or a session,
and never writes a claim about anyone's cognitive state to disk — the values below are
edited by hand, by the person they describe. Promoting it into a gate would defeat its
purpose and would claim a precision the proxies cannot support.

The proxies, the observed/inferred/asked confidence discipline and the scientific
grounding — including what it deliberately does *not* assert — live in the plugin's
`skills/cognitive-reservoir/SKILL.md`.

Thresholds are disjunctive: any one crossing fires a single session-end advisory. A
cluster of advisories that get routinely ignored is a signal to raise a threshold, not
to distrust the reading.

- window_hours: 8       # how far back the proxies look
- span_minutes: 180     # continuous session span (min) before the span proxy fires
- decision_volume: 8    # approval-like events (commits/merges) in the window
- context_switches: 4   # distinct work streams touched in the window
- chronotype:           # optional: early | late | intermediate. Only when
                        #   declared is the late-hour circadian band labelled
                        #   (optimal / dip / suboptimal); otherwise the hour is
                        #   reported as asked/unverified.

These are the shipped defaults, unmodified. They have not been tuned to this
project or to its maintainer, and that is worth saying out loud: a threshold
someone else chose is a starting point, not a considered limit. Run `/reservoir` for
an on-demand read, or `/reservoir tune` to change them.

Note on what is *not* here. The Mast's pact — the stop hour and the concurrency
limit a person sets for themselves in clear weather — deliberately does not live in
this file. A pact is a property of a person, not of a repository, so it lives in
`pacts.md` in the operator's own config directory, is never committed, and is
authored only by the person it binds. `HARNESS.md` declares what the project
expects; it does not get to declare when someone should stop for the night.

---

## Status

<!-- Auto-updated by /harness-audit — do not edit manually -->

Last audit: never
Constraints enforced: 5/6 deterministic, 1 advisory
Garbage collection active: 0/1
Drift detected: not yet audited
