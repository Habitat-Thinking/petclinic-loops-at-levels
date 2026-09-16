# /extract-conventions — the exchange, verbatim

Captured 2026-09-16 in the Level 2 profile. Assistant text and the maintainer's
answers, in order. Tool calls and thinking omitted; nothing else edited.


## Agent

Both CLAUDE.md and HARNESS.md are in place, so we can start.

## Convention Extraction

I'll ask you five questions to draw out the unwritten rules your team follows. I'll sort each answer into one of these:

- **Constraint** (must-follow) → goes in HARNESS.md
- **Convention** (should-follow) → goes in CLAUDE.md
- **Style preference** (nice-to-have) → goes in CLAUDE.md
- **Not encodable yet** → needs breaking down before it can be written as a rule

Take your time. The conversation matters as much as the output.

One thing to note up front: your HARNESS.md says *"Constraints: None at this level. Everything in this repository is advisory"* (Level 2). If any of your answers turn out to be must-follow rules, we'll need to choose between two options:
- Add them to HARNESS.md as `unverified` constraints, which moves past that Level 2 statement.
- Record them as advisory conventions for now, to keep the Level 2 setup as it is.

I'll ask you to decide when we get to the summary.

---

### Question 1 of 5

**What architectural decisions in this project should never be left to individual judgment? These are the patterns where deviation is always wrong, not a matter of taste.**

Some starting points for Petclinic:
- How controllers, repositories and entities relate (for example, is there no service layer on purpose?)
- Keeping the H2, MySQL and Postgres schema files in step
- Package-by-feature (`owner`, `vet`, `system`) versus package-by-layer
- Thymeleaf templates and fragments versus other ways of building views

Answer in your own words. After that, I'll repeat back what I understood, ask one follow-up and suggest a category.


## Agent

Here's what I understood from your four answers. I checked each one against the code:

1. **Keep the layering as it is.** Controllers call repositories directly, and entities sit in the same package. There's one package per feature (`owner`, `vet`, `system`), plus the shared `model` package with `BaseEntity`, `NamedEntity` and `Person`. No `*Service` classes and no new layers.
2. **Schema changes go into all three databases.** Any change to `db/{h2,mysql,postgres}/schema.sql` goes into all three files, and the matching `data.sql` seed data is updated in the same change.
3. **Templates reuse fragments and use message keys.** Form fields use `fragments/inputField` and `fragments/selectField`, and all display text comes from `messages*.properties` keys. No literal display strings in templates.
4. **No new dependencies for things the stack already does.** Spring Boot, Thymeleaf, JPA, Caffeine caching and so on should cover it before anything new goes into the POM.

### Follow-up

Some parts of 2 and 4 are unclear. Which of these apply?

- **(a)** A new *feature* package (say, `billing`) is fine as long as it has the same controller → repository → entity shape. Or should new features go into existing packages?
- **(b)** For item 4, can a dependency ever be justified? For example, if it's discussed in the PR first, or it's test-scoped only. Or is it a flat "no" unless a human approves it?
- **(c)** For item 2, does the `.txt` setup and `user.sql` material for mysql and postgres count, or only `schema.sql` and `data.sql`?

### Proposed categories

| # | Rule | Category |
|---|------|----------|
| 1 | Layering and package-per-feature | **Constraint**: a deviation is always wrong, and a reviewer can check it (for example, no `@Service` classes, no cross-feature layer packages) |
| 2 | Schema parity across H2, MySQL and Postgres, plus seed data | **Constraint**: can be checked, and could later be enforced by a script that compares table and column names |
| 3 | Fragment reuse and message keys in templates | **Constraint** for message keys (it fits alongside `I18nPropertiesSyncTest`). **Convention** for fragment reuse, since a new kind of field may need a new fragment |
| 4 | No new dependencies without a reason | **Constraint** if the answer to (b) is "human approval required", otherwise **Convention** |

Do these categories look right? Once you've answered (a) to (c), I'll move on to Question 2.


## Agent

I've noted your Question 2 answers. You didn't respond to the Question 1 follow-ups (a) to (c), so I'll bring them back up at the summary. Until then, the Question 1 categories are provisional.

### Here's what I understood (Question 2)

These are the things AI keeps getting wrong in this repo:

1. **Updating only the H2 schema.** MySQL and Postgres get left behind. This backs up the Question 1 schema-parity rule: it happens often, not just in theory.
2. **Filling locale bundles with English.** A new key goes into all ten locale bundles with English text, so `I18nPropertiesSyncTest` passes but the app shows English to German, Japanese and other users. The test checks that keys *exist*, not that they're translated.
3. **Testing internals instead of behaviour.** Tests check implementation details instead of what a user can see: rendered pages, model attributes, redirects, validation messages.
4. **Settling design questions silently.** Questions like required vs. optional, max lengths or defaults get decided in code without being raised as decisions.

I checked #2 against the bundles. Across all 10 locales, only 2 values are currently the same as the default English: `name` in `de` ("Name") and `error` in `es`. Both are real translations that happen to match. That means an "identical to English" check would be almost free of false positives today, so #2 could become enforced later.

### Follow-up

For #3, what does "behaviour a user can observe" mean in practice here? Which of these fits best?

- **(a)** `@WebMvcTest` + MockMvc assertions on status, view name, model attributes and rendered content count. Unit tests on entities or validators are fine as long as they check behaviour (e.g. "`PetValidator` rejects a blank name") rather than structure.
- **(b)** Only full-stack tests count (`@SpringBootTest`, real HTTP or the rendered HTML).
- **(c)** Something else, e.g. no mocking of repositories, or never checking private state through reflection or getters that exist only for tests.

### Proposed categories

| # | Rule | Category |
|---|------|----------|
| 1 | Schema changes go into H2, MySQL and Postgres together | Merges into the **Constraint** from Question 1, now backed by a known failure pattern |
| 2 | New message keys need real translations, never English copied into non-English bundles | **Constraint**. It can be checked (see the numbers above), and "tests pass but the app is wrong" is the kind of failure a constraint should stop |
| 3 | Tests check observable behaviour, not internals | **Convention**. It needs your answer to the follow-up above before it could be a rule a reviewer can apply consistently |
| 4 | Raise open design decisions (required/optional, limits, defaults) instead of settling them silently | **Convention** for now. It's about how the agent works rather than a property of the code, so it can't be checked from a diff alone. A narrower version could be: "PR description lists every validation or nullability choice made". Want me to phrase it that way? |

One question about #2: the agent can't produce trusted translations on its own. When it can't translate, what should it do? One option is to add the key to `messages.properties` only and flag the missing translations in the PR. That fails `I18nPropertiesSyncTest`, which may be what you want. Or should it write a best-effort translation and mark it for human review?

Once you've confirmed these, I'll move on to Question 3 (security checks).


## Agent

I've noted your Question 3 answers. The follow-ups from Questions 1 and 2 are still open, and I'll collect them all at the summary.

### Here's what I understood (Question 3)

1. **No secrets anywhere.** No secrets, keys or tokens in source, config files or test fixtures.
2. **Owner details are personal data.** They never appear in logs, error messages or test output.
3. **HTML escaping stays on.** Templates never use `th:utext` on anything a user entered.
4. **Every new dependency is a supply-chain decision.** It needs a stated reason and a known source (provenance). This makes Question 1 item 4 stricter: it's no longer just "don't duplicate the stack" but "every dependency has to be justified".

I checked the code, and there are two things you should know before we encode these:

- **Default DB passwords are already in the repo.** `application-mysql.properties:5` and `application-postgres.properties:5` use `${MYSQL_PASS:petclinic}` / `${POSTGRES_PASS:petclinic}`, and `docker-compose.yml` hard-codes `MYSQL_PASSWORD=petclinic`, `POSTGRES_PASSWORD=petclinic` and an empty MySQL root password. A strict reading of rule 1 fails on the current code.
- **Owner has no email field.** The personal fields are `firstName`, `lastName` (from `Person`), `address`, `city` and `telephone`. Currently there are no `th:utext` uses in templates and no logging in `src/main/java`, so rules 2 and 3 hold today.

### Follow-up

How should the local-development credentials be handled?

- **(a)** Allowed exception: throwaway credentials for local containers (`petclinic`/`petclinic`, used only with docker-compose and Testcontainers) are fine. The rule covers real credentials: anything for a shared, hosted or production environment, API keys, tokens and private keys.
- **(b)** No exceptions: remove the defaults (`${MYSQL_PASS}` with no fallback, compose reads from `.env`), and treat the current state as a known violation to fix.

### Proposed categories

| # | Rule | Category |
|---|------|----------|
| 1 | No secrets, keys or tokens in code, config or fixtures | **Constraint**. It can be enforced by a tool such as gitleaks, but only once (a) or (b) is settled; otherwise it fails from day one |
| 2 | Owner details (names, address, city, telephone) never logged, in error messages, or in test output | **Constraint**. It can be checked by review today. The field list is explicit, so no one has to judge what counts as personal data |
| 3 | No `th:utext` on user-supplied values | **Constraint**. It can be enforced with a simple check (grep for `th:utext` and review each hit) |
| 4 | Dependency additions need a reason and a known source | **Constraint**, merged with Question 1 item 4 into one rule: "No new dependency without a stated reason, a known source, and confirmation that the existing stack can't do it" |

Two small checks on these. For #2, should "test output" also cover test *fixtures*? Realistic-looking fake data such as the existing `data.sql` seed owners is fine, but real people's details are not. For #4, what counts as provenance? One option is a well-known Maven Central group ID plus a pinned version. The other is something stricter, like a named human approver in the PR.

Once you've answered, I'll move on to Question 4 (instant rejections in code review).


## Agent

I've noted your Question 4 answers. The open follow-ups are still waiting for the summary.

### Here's what I understood (Question 4)

These get a change rejected straight away:

1. **Tests that fail or are skipped.** A change isn't ready if any test is red or didn't actually run.
2. **Unformatted code.** `./mvnw spring-javaformat:validate` must pass.
3. **Unrelated reformatting.** Reformatting or whitespace changes in files or lines the change didn't need, because it buries the real diff.
4. **Judgement calls nobody can see.** Any choice made without being written down somewhere a human will read it. This takes Question 2 item 4 ("settling design questions silently") and upgrades it from something you correct often to grounds for rejection.

I checked the build and tests, and the "skipped" part matters more than it looks:

- **Formatting is already checked by the build.** `spring-javaformat-maven-plugin` runs `validate` during Maven's `validate` phase (`pom.xml:205-217`), so any `./mvnw verify` or `package` fails on unformatted code. This is the one rule here that a tool already enforces. The HARNESS.md text "nothing verifies it" is slightly out of date on this point.
- **The MySQL and Postgres tests skip silently without Docker.** `MySqlIntegrationTests` uses `@Testcontainers(disabledWithoutDocker = true)` and `PostgresIntegrationTests` uses `assumeTrue(...isDockerAvailable())`. On a machine without Docker, the build is green while both are skipped. That's exactly the gap behind your Question 2 complaint that "only H2 gets updated": the tests that would catch MySQL and Postgres drift are the ones that don't run.
- There are other environment skips (`@DisabledInNativeImage`, `@DisabledInAotMode`), but they only apply to native and AOT builds, not a normal JVM run.

### Follow-up

What does "skipped" mean for rule 1?

- **(a)** No *newly* skipped tests: no `@Disabled`, no new `assume*` calls, no deleted or commented-out tests. Existing skips when Docker is unavailable are acceptable.
- **(b)** (a), plus: any change that touches `db/**`, repositories or entities must be verified with Docker running. The PR has to show that `MySqlIntegrationTests` and `PostgresIntegrationTests` actually *ran*, not just that the build passed.
- **(c)** Stricter still: the build must never skip for missing Docker (e.g. fail instead of skip). That changes the existing test setup.

I'd recommend **(b)**. It closes the gap behind your most frequent correction without making every build depend on Docker.

### Proposed categories

| # | Rule | Category |
|---|------|----------|
| 1 | No failing or skipped tests (as defined by your follow-up answer) | **Constraint** |
| 2 | `spring-javaformat:validate` clean | **Constraint**. It's already enforced by the build, so it could start as *deterministic* rather than unverified if you want |
| 3 | No reformatting of files or lines the change didn't need | **Constraint**, checked by review. One possible check: every changed hunk must be related to the stated purpose of the change |
| 4 | No judgement call without surfacing it | **Constraint**, merged with Question 2 item 4. To make it checkable: "Every choice not stated in the task (validation, nullability, limits, defaults, naming visible to users, error handling) is listed in the PR description or commit message." |

About #4: is the PR description the right place, or do you want a specific place, such as a "Decisions" section in the PR, or a `docs/decisions/` file?

Once you've answered, I'll move on to the last one, Question 5 (clean refactoring vs. over-engineering).


## Agent

## Question 5: here's what I understood

1. **No abstraction with only one caller.** An interface or generic needs a second real user before it's added.
2. **Keep it plain.** PetClinic is a teaching codebase, so choose the boring construction a newcomer can read.
3. **Smallest diff that works.** Extend an existing file or fragment rather than creating a new one.
4. **Deleting beats adding.** A refactoring that removes code is worth more than one that adds a layer.

There's one catch for rule 1. `OwnerRepository`, `PetTypeRepository` and `VetRepository` are interfaces with a single caller each, but they're Spring Data repositories where the interface *is* the implementation. The rule needs to exclude them explicitly, or a strict reviewer would flag the existing code.

Proposed categories: rules 1 and 3 are **conventions** (strong defaults, and an exception needs a stated reason). Rules 2 and 4 are **style preferences**, since both need judgement.

Over the session I asked follow-ups you didn't answer. Rather than go back through each one, I've filled them in below with my recommended default, each marked **[default]**. Change any you disagree with.

---

## Proposed additions

### HARNESS.md constraints (must-follow)

1. **Layering stays as it is.** Controller → repository → entity, one package per feature, shared base types in `model`. No service layer or other new layers. **[default]** A new feature package is fine if it follows the same shape.
2. **Schema changes go into all three databases.** Any change to `db/*/schema.sql` goes into `h2`, `mysql` and `postgres` in the same change, with each `data.sql` updated to match. **[default]** This covers `schema.sql` and `data.sql` only; the `.txt` setup notes and `user.sql` are out of scope.
3. **Templates use message keys.** No hardcoded display text in templates; all of it comes from `messages*.properties` keys.
4. **Real translations only.** Non-English bundles never get English copied into them. **[default]** If the agent can't translate reliably, it adds the key to `messages.properties` only, lets `I18nPropertiesSyncTest` fail, and flags the missing locales for a human.
5. **No secrets in the repo.** No secrets, keys or tokens in code, config or test fixtures. **[default]** Throwaway local-container credentials (`petclinic`/`petclinic` in `docker-compose.yml` and the `application-{mysql,postgres}.properties` fallbacks) are an allowed exception.
6. **Owner personal data stays out of output.** `firstName`, `lastName`, `address`, `city` and `telephone` are never logged, never in error messages, never in test output. **[default]** Fixtures use obviously fictional data only.
7. **Escaping stays on.** No `th:utext` on user-supplied values.
8. **Dependencies must be justified.** No new dependency without a stated reason, confirmation the existing stack can't do it, and a known source. **[default]** Known source means Maven Central, a pinned version, and a human-approved PR.
9. **No failing or skipped tests.** **[default]** This means option (b): no new `@Disabled`, `assume*` calls, or deleted or commented-out tests. Any change touching `db/**`, entities or repositories must show `MySqlIntegrationTests` and `PostgresIntegrationTests` actually ran, not skipped for missing Docker.
10. **Formatting passes.** `./mvnw spring-javaformat:validate` is clean. The build already enforces this, so it could be entered as `deterministic` rather than `unverified`.
11. **No unrelated reformatting.** Every changed hunk is needed by the change's stated purpose.
12. **Judgement calls are written down.** Every choice not stated in the task (required/optional, nullability, length limits, defaults, user-visible naming, error handling) is listed. **[default]** It goes in a "Decisions" section of the PR description.

### CLAUDE.md conventions (should-follow)

1. **Reuse the existing fragments.** Form fields use `fragments/inputField` and `fragments/selectField`. A new fragment only for a genuinely new kind of field, extending the existing fragment file where possible.
2. **Tests check behaviour a user can see.** **[default]** This means option (a): `@WebMvcTest` + MockMvc assertions on status, view, model attributes and rendered content, and unit tests on validators and entities that check behaviour, never structure or private state.
3. **No single-caller abstractions.** No interface or generic until there's a second real user. Spring Data repository interfaces are excluded.
4. **Smallest diff that works.** Extend an existing file or fragment before creating a new one.

### CLAUDE.md style preferences (nice-to-have)

1. **Match PetClinic's plainness.** Choose the boring construction a newcomer can read, because this is a teaching codebase.
2. **Prefer deleting to adding.** A refactoring that removes code beats one that adds a layer.

### Not encodable yet (needs breaking down)

1. **"Plainness" and "boring construction."** These can't be checked until they're broken into observable cases, e.g. no streams where a loop is clearer, no Optional chains longer than N, no reflection or annotation tricks. They're kept as a preference for now.
2. **"Needed by the change's purpose"** (constraint 11). A reviewer can apply it, but where the line sits between necessary cleanup and noise will need worked examples as cases come up.

---

## Two decisions before I write anything

**A. The Level 2 statement.** HARNESS.md currently says *"Constraints: None at this level. Everything in this repository is advisory."* What should I do with the 12 constraints?
- **(i)** Add them as `unverified` constraints under `## Constraints` and replace the "None at this level" paragraph. This moves the repo past its Level 2 statement.
- **(ii)** Keep Level 2 intact: write all 12 into the `### Conventions` block in HARNESS.md's Context section, marked must-follow but advisory, and leave `## Constraints` as it is.

I'd recommend **(ii)**, since the repo seems to be set up deliberately to show Level 2. Constraint 10 is the only one a tool actually enforces.

**B. Where the other items go.** Should the conventions and preferences be appended to CLAUDE.md under `## Conventions (extracted 2026-09-16)`, replacing the `<!-- Populated by /extract-conventions. -->` placeholder? That placeholder is the file's only content.

Does this look right? Tell me what to add, remove or move to another category, plus your choices for A and B, and any **[default]** you want changed. After that I'll write the files.


## Agent

I've written everything from the session into the two files. Nothing is committed yet; both files are still untracked.

**HARNESS.md:** the 12 must-follow rules are now in the Conventions block, marked as advisory to fit Level 2. I didn't change the Constraints section, so it still says "None at this level". That section also says "nothing verifies it", which is slightly out of date: the build already enforces rule 10 (formatting). I noted that next to rule 10 rather than editing the Level 2 text.

**CLAUDE.md:** the placeholder is replaced with a section dated 2026-09-16. It points to HARNESS.md for the must-follow rules, then lists:
- **4 should-follow conventions:** reuse existing form fragments, test what a user can see, no abstraction with only one caller (Spring Data repositories excepted), smallest diff that works.
- **2 style preferences:** keep it plain, and deleting beats adding.
- **2 items not encodable yet:** "plainness" and where the no-unrelated-reformatting line sits.

The follow-ups you didn't answer went in with my recommended defaults:
- Local-container passwords like `petclinic` are an allowed exception to the no-secrets rule.
- A change touching `db/**`, entities or repositories must show the MySQL and Postgres tests actually ran. Without Docker they skip silently and the build still passes.
- If a real translation isn't available, the key goes into `messages.properties` only and the sync test is left failing.
- Judgement calls go in a "Decisions" section of the PR description.

To change any of these, edit the matching line in HARNESS.md.

## Next steps

- Run `/harness-audit` to check which of these rules could be enforced automatically. Good first candidates are formatting (already in the build), `th:utext` (a grep) and English copied into locale bundles (almost no false matches today).
- Use `/harness-constrain` to promote a rule to an enforced constraint. Only do that if you decide to move this repo past Level 2.
- Schedule a re-extraction in your quarterly review.
- Share the updated CLAUDE.md and HARNESS.md with the team for review.


## Agent

The extraction is finished. HARNESS.md and CLAUDE.md are both written, and there's nothing left to do in this session.

"Carry on" could mean a few different things from here, so which one do you want?

1. **Commit the two files** on `level-2-commanding`. They're currently untracked. This is the one I'd recommend, since it records what we extracted.
2. **Run `/harness-audit`.** It checks the new rules against the code and updates the status in HARNESS.md.
3. **Stop here.** You or the team can review the files before anything else happens.

I haven't done any of these yet: committing and running the audit both change the repo, so I want your go-ahead first.


## Agent

I took "carry on" as a yes to my recommended option and committed CLAUDE.md and HARNESS.md to `level-2-commanding` as `5b45014`. Nothing was pushed.

An untracked `observability/` directory has appeared since the session started. I didn't create it, and I left it out of the commit.

If you want to go further, the next step is `/harness-audit`. It checks the 12 rules against the code and updates the status in HARNESS.md. I haven't run it, so the repo stays as committed until you ask.


## Maintainer

# /harness-audit

Full meta-verification of the harness itself.

Read the `harness-audit-engine` skill from this plugin before
proceeding. The engine defines the shared drift-detection logic and
the drift-report shape. This command is the read-only inspection
caller; `/harness-sync` is the read-then-fix caller.

## Process

### 1. Check HARNESS.md Exists

If no HARNESS.md exists, tell the user to run `/harness-init` first.

### 2. Discover Current State

Dispatch the `harness-discoverer` agent to scan the project's current
state — what tools are installed, what CI exists, what hooks are
configured.

### 3. Verify All Constraints

Dispatch the `harness-enforcer` agent to run every declared constraint
(all scopes) against the current codebase.

### 4. Audit Harness Health

Dispatch the `harness-auditor` agent with the discovery report and
enforcer results. The auditor will:

- Compare declared enforcement with actual project state
- Detect drift (declared but missing tools, undeclared but present
  enforcement)
- Update HARNESS.md's Status section
- Update the README badge

### 5. Validation Checkpoint

**This step is mandatory.** The auditor writes the `## Status` block, which
is structured output with downstream parsers — `/harness-status`,
`harness-auditor` itself on its next run, and `harness-gc`. Before #575 this
command had no read-back step, and the Status block sat claiming
`Drift detected: no` against a tree with four failing constraints for twelve
days.

Read `HARNESS.md`'s `## Status` section back and verify:

1. **The four fields are present and in order** — `Last audit`,
   `Constraints enforced`, `Garbage collection active`, `Drift detected`.
2. **`Last audit` is today's date**, not the previous run's.
3. **`Constraints enforced: N/M`** — `M` equals the number of `### ` headings
   under `## Constraints` with HTML-commented blocks excluded, and `N` equals
   `M` minus the count of constraints whose `- **Enforcement**:` is
   `unverified`. Count them; do not carry the previous run's figure forward.
4. **`Drift detected`** is `yes` whenever the enforcer reported any constraint
   failing, or discovery reported a declared tool missing. A `no` beside a
   non-empty findings list is the specific failure this checkpoint exists to
   catch.
5. **The enforcement figure is qualified** wherever agent-enforced constraints
   have no dispatch path. `N/M` counts *declared* enforcement; if no CI
   workflow dispatches `harness-enforcer`, say so beside the number rather
   than leaving a reader to infer that every counted constraint can fail a
   build.

Fix deviations in place. Do not re-dispatch the auditor — a second opinion on
a number you can count yourself is slower and no more reliable.

If the Status block disagrees with the enforcer results you were given, the
enforcer results win: they were observed, and the Status block is a summary.

### 6. Present Results

Show the user:

- Enforcement ratio and breakdown
- Any drift detected (with specific details)
- Constraint pass/fail results
- Badge update summary

### 7. Recommend Actions

Based on audit findings, suggest next steps:

- Unverified constraints that could be promoted
- Drift that needs resolution
- Undeclared enforcement that should be added to HARNESS.md
- Tools that could be installed for deterministic enforcement

## Reflection log + archive coverage

The audit report now includes a `Reflection-log archival` subsection
covering:

- Active-log entry count vs archive entry count
- Curation debt: count of entries dated >180 days that lack a `Promoted` line
- Whether `Reflection log archival of promoted entries` (Path 1) GC rule
  is declared in HARNESS.md and operating
- Whether `Reflection log aged-out review` (Path 2) GC rule is declared
  (opt-in)

If Path 1 is undeclared but `Promoted` lines exist in the active log,
flag as drift — promotions are happening but archival isn't.

## Large-repository workflow path (Claude Code only)

On a repository above the deep-research threshold — **file count `> 300`**
— the `harness-auditor` elects its **workflow mode**: a deep-research
dynamic workflow that fans out by area, verifies each finding in a
separate context, and includes a verifier adversarial to the framework's
own assumptions (the self-audit guard; see the harness-auditor agent
doc). This path requires the **Claude Code** runtime; where the workflow
runtime is absent the auditor **falls back** to its single-context audit
and the report proceeds unchanged. The HARNESS.md Status section and badge
updates are identical on either path.


## Agent

Kick off tests in background while checking others.


## Agent

I've started the harness audit, since running it was the next step I suggested. Two helper agents are working in parallel:

- **Discovery:** takes stock of the build tools, formatter, CI workflows, commit hooks and which harness files exist.
- **Rule checks:** HARNESS.md declares no enforced constraints (the Level 2 setup), so formally there's nothing to run. The agent is also checking each of the 12 advisory rules against the current code as extra information. That includes running the formatter check and the test suite, and recording whether the MySQL and Postgres tests actually run or get skipped.

When both finish, a third agent will compare the results and write a Status section into HARNESS.md. HARNESS.md doesn't have one yet. I'll then check that section myself and report back.


## Agent

Both agents are still running, and I don't have results from either yet. The rule-check agent is probably taking longest, because it's running the full Maven test suite.

When they finish, I'll send their reports to the audit agent that writes the Status section into HARNESS.md. Then I'll check the numbers it wrote and report back. You don't need to do anything in the meantime.


## Agent

Both agents are still running, and neither has reported back yet. I'll carry on with the audit as soon as they finish; you don't need to keep prompting me.


## Agent

They're still running and neither has reported back. I'll get a notification when each one finishes, and I'll continue the audit then.

If the test run is taking too long, I can stop the rule-check agent and finish the audit without the test results. The Status section would then say the tests weren't checked. Just say "stop the tests" if you want that.


## Agent

Still waiting, since you didn't say "stop the tests". Both agents are still running and neither has sent results yet. I'll finish the audit as soon as they report back.

