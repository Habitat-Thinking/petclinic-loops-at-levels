# /harness-constrain — the exchange, verbatim

Captured 2026-09-17. The Level 2 reflection becoming a durable rule.

The approval moment is marked below: the agent proposes, and writes nothing until
the human says so.


## Maintainer

Base directory for this skill: /Users/russellmiles/.claude-loops-at-levels-l2/plugins/cache/ai-literacy-superpowers/ai-literacy-superpowers/0.91.0/skills/constraint-design

# Constraint Design

A constraint is a rule that must be enforced — not a suggestion, but a
hard boundary. In the harness framework, each constraint occupies a
**verification slot** that can be filled by either a deterministic tool
or an agent-based review. The system does not care which backs the slot;
only whether the constraint passed.

## Anatomy of a Constraint

Every constraint in HARNESS.md has four fields:

- **Rule** — what must be true, written precisely enough for objective
  verification
- **Enforcement** — `deterministic`, `agent`, or `unverified`
- **Tool** — which tool or agent checks this (or "none yet")
- **Scope** — when the check runs: `commit`, `pr`, `weekly`, `manual`

## Writing a Verifiable Rule

The rule field is the most important. Apply the same enforceability test
used for conventions:

1. Describe what compliant code looks like
2. Describe what violating code looks like
3. Confirm a reviewer (human or agent) could check this without
   ambiguity

Consult the `context-engineering` skill's convention patterns for
detailed examples.

## Choosing Enforcement Type

| Question | If yes | If no |
| --- | --- | --- |
| Does a deterministic tool exist for this? | Use deterministic | Continue |
| Is the rule precise enough for consistent LLM review? | Use agent | Use unverified |
| Is the team ready to set up tooling? | Use deterministic | Start with agent |

Start with unverified if unsure. Promote when ready.

### Command Risk Classification

For tool-level constraints, a specific pattern applies: **Command Risk Classification** assigns each tool or command an allow, ask, or deny disposition based on two criteria:

- **Reversibility** — can the action be undone? File reads are always reversible. File writes are reversible through git. `rm -rf /` is not.
- **Blast radius** — how much damage if it goes wrong? A typo in a test file affects one test. A typo in a deployment script affects production.

The classification is a spectrum, not a binary. Pattern matching refines it: the same command with different arguments carries different risk. Commands that repeatedly pass human review can be promoted from ask to allow, following the same promotion ladder that constraints use.

For the full promotion lifecycle (unverified to agent to deterministic),
consult `references/promotion-ladder.md`.

## Choosing Scope

| Scope | When it runs | Use for |
| --- | --- | --- |
| `commit` | PreToolUse hook (advisory) | Fast checks — formatting, naming |
| `pr` | CI pipeline (strict) | Thorough checks — tests pass, no secrets |
| `weekly` | Scheduled run | Slow checks — dependency audit, structural tests |
| `manual` | `/harness-audit` only | Exploratory — new rules being calibrated |

Start with `pr` scope for most constraints. Move to `commit` only for
fast checks that benefit from immediate feedback. Use `weekly` for
checks that are too slow or expensive for every PR.

## The Verification Slot Interface

The `harness-enforcer` agent uses a uniform contract:

**Input:** constraint definition + scope + file set
**Output:** pass/fail + findings list (file:line references)

This means the enforcer does not care whether it shells out to `eslint`
or reads code against a prose rule — the interface is the same. Promote
a constraint from agent to deterministic by changing the enforcement
field in HARNESS.md; no other changes needed.

For technical details on integrating custom tools into verification
slots, consult the `verification-slots` skill.

## Common Constraint Patterns

### Formatting / Style

- **Rule**: All source files pass the configured formatter
- **Typical enforcement**: Deterministic (prettier, gofmt, black)
- **Typical scope**: commit

### Secret Detection

- **Rule**: No API keys, tokens, or passwords in source files
- **Typical enforcement**: Deterministic (gitleaks, trufflehog)
- **Typical scope**: commit

### Test Suite

- **Rule**: All tests pass with zero failures
- **Typical enforcement**: Deterministic (test runner)
- **Typical scope**: pr

### Documentation Quality

- **Rule**: Doc comments explain reasoning, not signatures
- **Typical enforcement**: Agent (nuanced quality check)
- **Typical scope**: pr

### Architectural Boundaries

- **Rule**: Package X does not import package Y
- **Typical enforcement**: Deterministic (ArchUnit, go-cleanarch) or agent
- **Typical scope**: pr

### Executable Spec Conformance

A spec describes what should be built; tests verify it was built correctly.
The executable spec constraint bridges the two: it is a constraint whose
tool command runs the spec's associated test suite. The spec itself is
never executed — the tests are the executable verification.

- **Rule**: All code changes covered by a spec must pass the spec's
  associated test suite before merge
- **Typical enforcement**: Deterministic (project test runner)
- **Typical scope**: pr

**HARNESS.md entry example:**

```markdown
### Spec conformance

- **Rule**: All code changes covered by a spec in `docs/specs/`
  must pass the spec's associated test suite before merge
- **Enforcement**: deterministic
- **Tool**: <project test runner command>
- **Scope**: pr
```

**Linking specs to tests (naming convention):**

Spec files map to test directories or files by name. A spec at
`specs/YYYY-MM-DD-feature-design.md` corresponds to tests at
`tests/feature/` or `tests/feature_test.go`. The constraint runner
uses this convention to determine which tests to execute when
spec-covered code changes.

**The `deterministic + agent` enforcement pattern:**

A single constraint type is not enough. Deterministic tests catch
"code doesn't work" — functional failures that a test runner can
detect mechanically. Agent review catches "code works but doesn't
match what was specified" — intent drift that requires reading the
spec and judging alignment. Both are needed for full spec conformance:

| Layer | What it catches | Enforcement |
| --- | --- | --- |
| Test suite | Functional failures | Deterministic |
| Agent review | Intent drift from spec | Agent |

**Connection to BDD/Cucumber:**

For teams using BDD, the executable spec pattern is the gold standard.
Gherkin scenarios ARE the spec AND the test — there is zero drift
between intent and verification because they are the same artifact.
For teams not using BDD, the naming convention approach (spec file to
test directory) is the pragmatic alternative. Either way, the harness
constraint runs tests, not specs.

## Additional Resources

### Reference Files

- **`references/promotion-ladder.md`** — Complete lifecycle of a
  constraint from unverified through agent to deterministic, including
  partial promotion, demotion, and decision signals


## Maintainer

Base directory for this skill: /Users/russellmiles/.claude-loops-at-levels-l2/plugins/cache/ai-literacy-superpowers/ai-literacy-superpowers/0.91.0/skills/verification-slots

# Verification Slots

The verification slot is the harness framework's core technical
abstraction. Every constraint — whether backed by a linter, a formatter,
a structural test, or an LLM agent — is checked through the same
interface. This uniformity means the rest of the system (hooks, CI,
commands) does not care how a constraint is verified.

## The Contract

**Input:**

- Constraint definition (rule, enforcement type, tool)
- Scope (commit, pr, weekly, manual)
- File set (changed files, or all files)

**Output:**

- Result: `pass` or `fail`
- Findings: list of `{file, line, message}` (empty on pass)

## How the Enforcer Decides

The `harness-enforcer` agent reads each constraint from HARNESS.md and
dispatches verification based on the `enforcement` field:

| Enforcement | What happens |
| --- | --- |
| `deterministic` | Execute the `tool` command, interpret exit code |
| `agent` | Read the `rule` text, review files, produce findings |
| `deterministic + agent` | Run both, merge findings |
| `unverified` | Skip — log as unchecked |

## Deterministic Tool Integration

To fill a verification slot with a deterministic tool:

1. Identify a tool that checks the constraint (linter, formatter,
   scanner, custom script)
2. Verify it runs locally and produces usable output
3. Update the constraint in HARNESS.md: set `enforcement` to
   `deterministic` and `tool` to the exact command
4. Run `/harness-audit` to confirm the tool works in the harness
   context

For detailed integration patterns for common tools (ESLint, Prettier,
gitleaks, ArchUnit, custom scripts), consult
`references/tool-integration.md`.

## Agent-Based Verification

When no deterministic tool exists, the enforcer reads the constraint's
prose rule and reviews code against it. The output format is identical
to deterministic verification — pass/fail with file:line findings.

Agent-based checks are:

- **Flexible** — work with any constraint that can be described in prose
- **Non-deterministic** — may give different results on identical input
- **Token-intensive** — each check costs LLM inference
- **Nuance-aware** — can evaluate intent and context, not just syntax

## Mixed Enforcement

Some constraints benefit from both levels. For example, a linter checks
that doc comments exist (deterministic presence check) while the agent
reviews that comments explain reasoning rather than restating signatures
(quality check).

Use `deterministic + agent` in the enforcement field and list both tools.

## The Fan-out Slot

The **fan-out slot** is a first-class agent-backed verification slot for
large harnesses (Claude Code runtime only). Instead of one context
checking every constraint — where the enforcer tends to tire and report
"all constraints checked" after checking only some — the fan-out slot
spawns **one verifier subagent per rule**, each with a single constraint
and a clean context window, then passes every candidate violation through
a **skeptic** persona that tries to refute it before it reaches the
report.

A **synthesis barrier** waits for all N verifiers to return and reconciles
their results into the same uniform contract every slot produces:
**pass/fail with `{file, line, message}` findings**. Because the report
cannot form until every verifier has returned, the count of results
equals the count of enforceable constraints — no rule is silently
dropped. The slot is read-only and propose-only (INV-1): it reports
findings and never writes a durable artefact.

The `harness-enforcer` agent elects this slot automatically when the
enforceable-constraint count exceeds its threshold (default 8); below it,
the single-context path is used unchanged. The slot adapts the shipped
`enforcer-fanout.workflow.js` template from the `dynamic-workflows` skill.

## Additional Resources

### Reference Files

- **`references/tool-integration.md`** — Step-by-step integration
  patterns for linters, formatters, type checkers, secret scanners,
  structural test frameworks, and custom scripts


## Agent

HARNESS.md is there. AGENTS.md has 12 must-follow rules, and only two of them are checked by anything today: formatting (#10) and passing tests (#9, in part). Which rule do you want to enforce or change next?

Here's how the unchecked rules fall, sorted by how cheaply they could be checked:

**Checkable with a script, no new tools**
- **#7 Escaping stays on.** A grep for `th:utext` in `src/main/resources/templates/`. The rule says "on user-supplied values", but that's hard to check, so the simplest version bans `th:utext` everywhere.
- **#9 No skipped tests.** A grep of the PR diff for newly added `@Disabled` and `assume*` calls, and for deleted `@Test` methods. The part about proving the MySQL and Postgres tests actually ran needs a check of the surefire reports in CI.
- **#2 Schema changes land in all three databases.** A script over `git diff --name-only`: if any `db/*/schema.sql` or `data.sql` changes, all three of `h2`, `mysql` and `postgres` must change too.

**Checkable with a tool, but adding one is a supply-chain decision under #8**
- **#5 No secrets in the repository.** gitleaks, with an allowlist for the `petclinic`/`petclinic` local-container credentials.
- **#1 Layering stays as it is.** ArchUnit, which adds a Maven dependency and so needs human approval. An agent review could cover it until then.

**Only an agent can check these**
- #3 message keys in templates, #4 real translations, #6 owner personal data kept out of logs and output, #11 no unrelated reformatting, #12 a "Decisions" section in the PR description.

**My recommendation is #2 or #9.** Both come from team knowledge that neither the compiler nor the current build catches, and each can be a short script in `.github/workflows/harness.yml` with no new dependency. #7 is the cheapest to add but the least likely to catch anything.

Also, HARNESS.md, README.md, `.github/copilot-instructions.md`, `harness.yml` and `observability/` all have uncommitted changes. When I commit at the end, should those go in the same commit, or should I commit only the new constraint?


## Agent

You said not to write the script, and I had already written it. I'm deleting my copy so the version in the repo will be yours.


## Agent

The constraint is now in HARNESS.md as two blocks: "Decision record for source changes — commit" and "— pull request". Each uses your rule text word for word. I've stopped there: nothing is committed, and I ran no audit and added no other constraints.

**Checked:** Both blocks have Rule, Enforcement, Tool and Scope. Enforcement is `deterministic` in both, and Scope is `commit` in one and `pr` in the other. Neither has governance fields.

**Things to know:**
- **Script:** I had already written `scripts/check-decision-record.sh` before your message arrived. I deleted it, so the script is still yours to write. HARNESS.md now says what it needs to do:
  - `--staged` checks what is about to be committed.
  - `<base-ref>` checks `<base-ref>...HEAD`.
  - It exits non-zero when something under `src/` changed and nothing under `decisions/` was added or updated.
- **Two blocks:** A block can only have one scope, so the rule text appears twice. If you change it, change both.
- **Commit hook:** I added `.githooks/pre-commit`. It runs the script with `--staged`, prints a warning if the check fails, and never blocks a commit. It only runs after `git config core.hooksPath .githooks` in each clone, and I haven't run that here.
- **CI isn't wired yet:** `harness.yml` has no step for this, so pull requests aren't blocked yet. The PR block's Tool field says "(step not yet added)" so HARNESS.md doesn't claim otherwise. The step will need `fetch-depth: 0` so the base commit can be compared.
- **Not run yet:** Without the script there was nothing to run, so I haven't tested the check against the codebase.
- **What the script can't check:** It only reads the list of changed files. It can tell whether a record changed, but not whether the record names the judgement calls or says there were none.
- **Clashes with AGENTS.md:** Directive 12 still says decisions go in the PR description. Also, the plugin's commit-time hook uses an LLM to review files against any `Scope: commit` constraint. So this rule will also get an agent's reading as you edit, though that can only warn.
- **Paragraph in the wrong place:** The paragraph that introduces the two blocks ended up under the "Tests must pass" block, at HARNESS.md lines 43–52, when it should sit under its own heading. I haven't moved it because you said to stop.
- **Status line:** It still says `Constraints enforced: 2/2`. I changed it to 4/4 by mistake and put it back: that section is only updated by `/harness-audit`.
- **Wrong hook message:** The hook that fires after writes twice reported that HARNESS.md doesn't exist. It does, so that hook is misreading the project root.


## Agent

Understood. I've stopped and will wait for your next instruction.


## Agent

Understood. I'm stopped and won't run anything, commit or audit until you tell me what to do next.


## Agent

I'm stopped, and I've run nothing since your first "stop". Everything is as I left it: the HARNESS.md edits, `.githooks/pre-commit`, and no commit.

If your "No" means something I did should be undone, tell me what and I'll do only that.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Still waiting, and I've run nothing. This same message has now come in many times, so something may be resending it automatically. Nothing has changed since my last summary, and I'll act only on a different instruction.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.


## Agent

Waiting.
