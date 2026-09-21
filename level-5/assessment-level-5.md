I have everything I need. Nothing was written to the target repository — here is the full assessment for you to place externally.

---

# AI Literacy Assessment — petclinic-loops-at-levels (`level-5-supervising`)

**Date**: 2026-09-21
**Assessed by**: assessor agent (evidence-first, no clarifying questions — dispatched non-interactively)
**Repository**: `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l5` (detached worktree of `level-5-supervising`, HEAD `7e21af9`)
**Assessed level**: **Level 4 — Specification Architecture**

> **Stated assumptions.** (1) No clarifying questions were asked; every claim below is grounded in a file, a line, or a commit, and where evidence was unavailable I say so rather than infer. (2) Placement mode is **evidence-first**; the 40-statement ALCI Part D survey was not administered. (3) I did not run the build or any script, so "enforces" below means "a mechanism exists in the repository that would run", and I distinguish that from "has demonstrably run". (4) I read sibling branches via git only where they bear on whether a check fires; I changed nothing.

---

## Habitat Document Discovery (Phase 1a)

### HARNESS.md

- **Status**: found at conventional path
- **Path**: `HARNESS.md`
- **Markers matched**:
  - `## Constraints` heading at line 20, with constraint blocks following
  - Six four-field constraint blocks (`**Rule**` / `**Enforcement**` / `**Tool**` / `**Scope**`) at lines 25–33, 35–41, 56–67, 69–79, 81–93, 95–108
  - `## Garbage Collection` heading at line 112
  - `## Context` heading at line 3 with `### Stack` (5) and `### Conventions` (15) sub-headings
  - `## Status` heading at line 199 with `Last audit:` and `Constraints enforced:` fields (203–205)
- **Notes**: no `template-version:` marker (consistent with the retired-marker note in the discovery reference). The file deliberately carries *no* directives of its own — line 17–18 delegates conventions to `AGENTS.md` "so this file cannot drift from them".

### AGENTS.md

- **Status**: found at conventional path
- **Path**: `AGENTS.md`
- **Markers matched**:
  - `## Style preferences` heading at line 88 (conventions-style content)
  - A "what could not be encoded" / gap section: `## Not encodable yet` at line 95
  - Compound-learning vocabulary and an explicit promotion table at lines 22–34 ("Three directives moved to `HARNESS.md` when the harness was built"), naming each promoted rule and its new constraint
  - References to reflection-driven promotion via `HARNESS.md` line 45 ("Promoted from candidate (1) in the 2026-09-16 entry of REFLECTION_LOG.md")
- **Notes**: also serves as the **multi-tool standard** per `tool-config-evidence.md` — `CLAUDE.md` imports it and `.github/copilot-instructions.md` points at it.

### CLAUDE.md

- **Status**: found at conventional path
- **Path**: `CLAUDE.md` (7 lines)
- **Markers matched**:
  - Direct address / workflow instruction: line 3 "The directives for this project live in AGENTS.md, which is the single source"
  - Conventions block by reference: `@AGENTS.md` import at line 7 (content loaded, not merely linked — the distinction is the subject of commit `ffb2de5`)
- **Notes**: deliberately empty of its own content. This is a **design choice, not a thin habitat** — the same content reaches Claude via the import.

### Ambiguities

None. One candidate per document type.

### Paths checked but not matched

`docs/HARNESS.md`, `.ai/HARNESS.md`, `.agents/HARNESS.md`, `conventions/HARNESS.md`, `governance/HARNESS.md`, `CONVENTIONS.md`, `STANDARDS.md`, `docs/conventions.md`, `docs/standards.md`, `.agents/AGENTS.md`, `docs/AGENTS.md`, `.claude/AGENTS.md`, `.cursor/AGENTS.md`, `.claude/CLAUDE.md`, `docs/CLAUDE.md`, `.ai/CLAUDE.md` — none of these files exist.

### Parallel-tool config (Phase 1b, `tool-config-evidence.md`)

- `.github/copilot-instructions.md` — **matched** (24 lines): convention statements covering stack and build commands (lines 6–14); explicit no-duplication contract at lines 18–19 and 23–24. Header (lines 3–4) claims it is "synced from HARNESS.md … run `/convention-sync` to regenerate"; `/convention-sync` is not present in this repository.
- `.cursor/rules/`, `.windsurf/rules*`, `.ai/`, `.llm/`, `tools/ai/`, `scripts/ai/` — checked, do not exist.

Per the reference: multiple surfaces expressing the same discipline do not compound into a higher level. Copilot coverage is noted as breadth, not depth.

---

## Observable Evidence

### Repository Signals

| Signal | Found | Level indicator |
| --- | --- | --- |
| CI workflows | Yes — 4: `harness.yml`, `maven-build.yml`, `gradle-build.yml`, `deploy-and-test-cluster.yml` | L2 |
| Test coverage enforcement | **No** — JaCoCo present (`pom.xml:277–293`) but only `prepare-agent` + `report`; **no `check` goal, no threshold** | L2 |
| Vulnerability scanning | **No** — zero matches for govulncheck / OWASP / Scout / Trivy / Snyk across `.github/` and `pom.xml` | L2 |
| Mutation testing | **No** — no PIT/pitest anywhere | L2 |
| CLAUDE.md | Yes — 7 lines, imports `AGENTS.md` | L3 |
| HARNESS.md | Yes — **6 constraint blocks** (5 deterministic, 1 advisory) + 2 GC rules | L3 |
| AGENTS.md | Yes — 10 "must follow", 4 "should follow", 2 style, 2 explicitly-unencodable; 1 promotion table | L3 |
| MODEL_ROUTING.md | Yes — 3 tiers, 10 agents routed with per-agent rationale, 7-row token budget table | L3 |
| Custom skills | **No — zero in repo.** `.claude/` contains only `settings.json` | L3 |
| Custom agents | **No — zero in repo.** 10 agents *named* in `PIPELINE.md:13–26` and `MODEL_ROUTING.md:19–30`; none defined here | L3 |
| Custom commands | **No — zero in repo.** `/harness-init`, `/harness-audit`, `/harness-health`, `/reservoir`, `/reflect`, `/convention-sync`, `/extract-conventions`, `/carpaccio` all referenced, none present | L3 |
| Hooks configured | Yes — 2: `PostToolUse` on `Write|Edit` (`.claude/settings.json:3–14`), `.githooks/pre-commit` (opt-in per clone) | L3 |
| REFLECTION_LOG.md | Yes — **1 entry** (2026-09-16), mirrored by 1 fragment in `reflections/active/` | L3 |
| Specifications directory | Yes — `docs/superpowers/specs/` (678-line spec + 491-line plan) | L4 |
| Implementation plans | Yes — `visit-carries-vet-and-time-plan.md` | L4 |
| Orchestrator with safety gates | **Declared, not present.** 4 gates + a "max 3 cycles" bound in `PIPELINE.md`; no orchestrator file, no mechanism | L4 |
| Plugin/platform tooling | **No** — no `.claude-plugin/`, no vendored plugin, no version pin | L5 |
| OTel configuration | **No** — zero matches for otel/opentelemetry/telemetry repo-wide | L5 |
| Cost tracking | Yes — **1 snapshot**, `observability/costs/2026-09-20-costs.md` (94 lines) | L5 partial |

### What the harness actually enforces, mechanism by mechanism

This is the table the brief asked for. "Runs" means a mechanism exists in this checkout that executes the check; "has fired" means there is evidence in this repository that it did.

| Declared thing | Where declared | Mechanism present? | When it runs | Has it fired? |
| --- | --- | --- | --- | --- |
| Consistent formatting | `HARNESS.md:25–33` | **Yes** — spring-javaformat bound to Maven `validate` phase; `harness.yml:51–52` | Every local `./mvnw` invocation; CI on `pull_request` | Yes (build-bound, unavoidable locally) |
| Tests must pass | `HARNESS.md:35–41` | **Yes** — `harness.yml:54–55`; also `maven-build.yml` on push/PR to `main` | Local `verify`; CI on PR | Locally yes. **CI: never on this branch** (see below) |
| Schema parity | `HARNESS.md:81–93` | **Yes** — `SchemaParityTest.java`, 242 lines, 4 test methods, runs in `verify`, in `.githooks/pre-commit:17–22`, and in `scripts/harness-advisory.sh:30–37` | Every `verify`; commit-time when a schema file is staged; edit-time | **Yes, demonstrably** — branch `demo-violation-schema-and-decision-record`, commit `cd785a6` "Add email column to the H2 schema only (deliberate violation)" |
| Decision record — commit | `HARNESS.md:56–67` | Partly — `scripts/check-decision-record.sh --staged` via `.githooks/pre-commit:10–12` | **Only if the operator ran `git config core.hooksPath .githooks`**, and it **warns, never blocks** (`pre-commit:11`, `exit 0` at :23) | Unknown — no observable trace |
| Decision record — PR | `HARNESS.md:69–79` | **Yes** — `harness.yml:48–49`, blocking step | `if: github.event_name == 'pull_request'` (`harness.yml:27`) | **No. Zero PRs.** All 25 commits since the harness was built (`d1e236a..HEAD`) are direct; the last merge commit in the repository predates this project's own work |
| Abstraction earns its place | `HARNESS.md:95–108` | **No, by design** — "reviewer judgement, human or agent. No tool decides this" | — | Not applicable; honestly labelled advisory |
| GC: reflection-driven regression detection | `HARNESS.md:114–122` | **No** — names a `harness-gc agent` that does not exist in this repository | "weekly" | **Never** |
| GC: scheduled sweep (3 checks) | `HARNESS.md:126–137` | **Yes** — `harness.yml:63–93` | `cron: '0 7 * * 1'` / `workflow_dispatch` | No evidence; scheduled workflows run on the default branch, and this work is not on `main` |
| Pipeline hard gates ×4 (slice, objection, plan approval, integration approval) | `PIPELINE.md:13–26` | **No mechanism whatsoever.** `grep` for `disposition\|pending` across `*.sh`, `*.yml`, `*.json`, `HARNESS.md`, `AGENTS.md` returns **zero matches** | — | Human discipline only |
| "max 3 cycles, then escalate" | `PIPELINE.md:23` | **No** | — | 1 cycle observed (`d943593` "Review cycle 1") — consistent with the bound, but untested by it |
| Cognitive reservoir thresholds | `HARNESS.md:157–195` | **No in-repo mechanism**; `/reservoir` is external | Explicitly never gates (`HARNESS.md:163–166`) | **Never** |

**Five of the six constraints are genuinely deterministic and backed by tooling that exists.** That is real, and the deliberate-violation branch proves at least the schema-parity and decision-record checks fire. But the *blocking* path — `harness.yml`'s `enforce` job — is gated on `github.event_name == 'pull_request'`, and this project's entire Level 1→5 history is 25 direct commits on a feature branch. **Nothing this project built for itself has ever been stopped by the gate it built.** Locally, formatting and schema parity do bind (they ride the Maven lifecycle); the decision record does not.

### Sophistication markers (Phase 3, `sophistication-markers.md`)

Cited so the level determination is auditable.

- **`scripts/harness-advisory.sh`** (53 lines): 1 script + **sophisticated-script markers ×3** — *argument parsing / configurability* (`--json` flag at line 17 switching output format), *conditional dispatch* (changed-file pattern branches at lines 21–27 and 30–37), *observability* (emits `{"systemMessage": …}` at lines 45–49, with a 5-line comment at lines 8–12 explaining that plain stdout on exit 0 "reaches the debug log and nobody else"). Cost-aware guard at line 30: Maven only starts when a schema file actually moved. Weakness noted: `set -u` only, no `-e`/`pipefail`.
- **`.githooks/pre-commit`** (23 lines): **moderate — 2 markers** — *conditional dispatch* (tool-existence guard at 5–8; staged-schema branch at 17–22), *error recovery / graceful degradation* (missing script warns and exits 0 rather than failing the commit). Deliberately non-blocking.
- **`scripts/check-decision-record.sh`** (61 lines): **moderate — 2 markers** — *subcommand dispatch* (`case` at 16–26 selecting `--staged` vs base-ref semantics), *error recovery* (`set -eu` at 14) plus a structured, actionable 15-line failure message (46–60). Comment at lines 5–7 states the design intent precisely: "It does not read the record, and it has no opinion about what is in it. An agent is not involved and cannot talk it round."
- **`.github/workflows/harness.yml`**: **sophisticated** — two jobs with *event-conditional dispatch* (`if:` at 27 and 65), *lifecycle awareness* across PR-time and scheduled-time loops, a self-referential tool-existence GC check (84–93).
- **`SchemaParityTest.java`** (242 lines, 4 `@Test` methods): purpose-built constraint enforcement, not a stock unit test — dialect-aware regex parsing (49–56), per-dialect `NOT NULL` normalisation (155–229), and — the strongest marker — a **meta-test, `theParserActuallyFindsTheSchema()` at line 231**, which guards against the classic failure where a regex silently matches nothing and the check reports green forever. That is the *right* instinct, and it is the single most sophisticated artefact in the repository.
- **Coordination across artefacts**: edit-time hook → commit hook → PR gate → scheduled sweep, with the same two scripts reused at different scopes and different severities. Per the reference table, *"multiple sophisticated artefacts coordinating → L4 evidence"*. **This marker is applied and it is load-bearing for the L4 placement.**
- **Agents / commands / skills: zero.** The reference's *sophisticated-agent* markers cannot be evaluated because no agent definition exists in this checkout. Per the conservative stance, I surface this as absence of evidence, not as a downgrade — but see the reproducibility caveat below.
- **L5 markers**: the table's *"sophisticated artefacts with telemetry, cost tracking, sovereignty → L5 evidence"* requires three things. **Cost tracking: present (once). Telemetry: absent. Sovereignty/reusable plugin: absent.** One of three does not carry an L5 placement.

### Ceremony audit — dispositions and adjudications

`docs/superpowers/` holds **3,518 lines across 6 records**. Disposition census (`^ *disposition:` in YAML frontmatter):

| Record | Total | pending | accepted | rejected | deferred |
| --- | --- | --- | --- | --- | --- |
| `slices/owner-books-visit-against-vet-availability.md` | 5 | 0 | 5 | 0 | 0 |
| `objections/visit-carries-vet-and-time.md` (spec mode) | 11 | 0 | 2 | 0 | **9** |
| `objections/visit-carries-vet-and-time-code.md` (code mode) | 7 | 0 | 5 | 2 | 0 |
| `stories/visit-carries-vet-and-time.md` | 18 | **12** | 6 | 0 | 0 |
| `consultations/visit-carries-vet-and-time.md` | 5 | **5** | 0 | 0 | 0 |
| **Total** | **46** | **17 (37%)** | 18 | 2 | 9 |

What this means, read honestly:

1. **The two hard gates are clean.** Slice adjudication: 5/5 disposed (`77c00d1` "Slice gate: dispositions adjudicated by the maintainer"). Objection adjudication: 11/11 disposed (`8a7c089`). Integration approval: 7/7 disposed (`b20868a` "Integration gate: all seven code-mode objections disposed"). The hard gates were genuinely operated, and the commit history proves the sequencing — record written *before* disposition (`6d1d88c` → `8a7c089`, `9fa6ed3` → `df010f1`), which is exactly the discipline `PIPELINE.md:29–35` demands.
2. **The two soft gates were never closed.** 17 of 46 dispositions sit `pending` with `disposition_rationale: null`. Twelve are choice stories — including #1 ("Visit reaches across into the vet package"), #4 ("A visit is an instant, never an interval"), #11 ("Parsing a vet becomes an application-wide fact") — all architecturally consequential. Five are consultation voices, every one of which asks a question that only a person outside the repository can answer.
3. **`PIPELINE.md:38–39` says the backstop exists. It does not.** The text reads: *"A soft gate advances but reports the count, and a merge-time constraint in `HARNESS.md` is what eventually forces the issue."* There is **no such constraint in HARNESS.md**, and no script, workflow or hook anywhere references `disposition` or `pending`. The work merged. The 17 pending dispositions are forced by nothing, on any timescale. **This is the single clearest instance of declared-but-unenforced in the repository, and it is a claim about enforcement, not an aspiration.**
4. **Quality of the adjudications is uneven, and the unevenness is diagnostic.** The code-mode objections carry substantive, falsifiable rationales — O2's rejection (`objections/…-code.md:21`) cites Thymeleaf attribute precedence (1700 vs 1200), says it was "proved by rendering all three fragments and by javap on the jar on this build's classpath", and pins the gap with a test. That is real adjudication. By contrast, all five slice dispositions read **"works for me"**, and **all eleven spec-mode objections — including six rated `high` — carry the identical rationale "need the spec tightened"**. A rationale repeated verbatim across eleven distinct objections is a bulk action wearing the costume of eleven judgements.
5. **The nine deferrals have no trigger, and the spec says so itself.** `specs/visit-carries-vet-and-time.md:674–678`: *"The deferrals carry no stated trigger. The adjudication records a rationale but no condition, date, slice or gate at which any of these nine is revisited. Nothing in this spec, the plan or the harness will raise them again."* Among those nine is **O3 (high)**: *"mysql and postgres `schema.sql` use `CREATE TABLE IF NOT EXISTS`, so an already-provisioned database never gains the new columns, and the parity check compares files rather than databases."* That objection identifies a known, documented, unmitigated hole in **the strongest deterministic constraint the harness has** — and the code-mode record confirms it stays open (`objections/…-code.md:29`: *"the check compares the three files to each other and starts no container. Spec O3 … stays deferred"*). And **O6 (high)** — a missing translation is meant to be flagged for a human, but "Tests must pass" converts that flag into a blocked PR with no sanctioned route out — grew *larger* during the run and was left deferred anyway (`specs/…:612–614`: *"That is the second enlargement of O6's exposure in one sitting, and O6 is still deferred"*).

**I want to be fair about what this means.** The project notices all of this and writes it down in its own artefacts, in plain language, against its own interest. That is a genuinely rare and genuinely high-literacy behaviour, and it is why this does not read as vanity paperwork. But *noticing in prose* and *enforcing* are different mechanisms with different failure modes, and the gap between the two is precisely where this assessment lands.

### Reflection and compound learning

One entry, `REFLECTION_LOG.md:26–38`, mirrored by `reflections/active/2026-09-16-add-email-field-to-owner.md`. It proposed **two** constraint candidates (`REFLECTION_LOG.md:33`):

- **Candidate (1)** — a decisions record must exist in the working tree for any change that adds a field. **Promoted.** `HARNESS.md:45` cites it by name: *"Promoted from candidate (1) in the 2026-09-16 entry of REFLECTION_LOG.md."* `decisions/README.md:5–12` tells the same story. This is a complete, traceable, auditable reflection→constraint promotion, and it is the best single piece of compound-learning evidence in the repository.
- **Candidate (2)** — *"every length-limited column in `db/*/schema.sql` has a matching `@Size` on the entity field (agent, scope: pr)"*. **Never promoted.** It appears in no constraint, no test, no script, no workflow. The defect it was raised to prevent — a `VARCHAR(255)` column with no `@Size`, so over-length input surfaces as a database error instead of a form error — is not currently present on this branch (the level-2 email field is not in `level-5-supervising`; `grep -rn email src/main/java` returns nothing), so the gap is latent rather than live. But the reflection's own closing line stands unanswered: *"A defect the agent reports itself should block the change, not just be noted."*

Five days and five levels later, the promotion rate from this log is **1 of 2**, and the mechanism that was supposed to catch the unpromoted half — the weekly `harness-gc` agent at `HARNESS.md:114–122` — **does not exist in the repository**. The entry is still in `reflections/active/`, never retired. The CI sweep would only flag it above 10 active entries (`harness.yml:80`), so at a count of 1 it is invisible to every loop the project has.

### Drift found in the drift-detector itself

Four items, each verifiable by a reader with a clone:

1. **`HARNESS.md:203` "Last audit: never"** — contradicted by `decisions/2026-09-17-harness-drift-fixes.md:1`, "Fix the drift the first `/harness-health` run found". An audit ran; the Status block it is supposed to write was never written. The field that reports whether the harness is being watched is itself unwatched.
2. **`HARNESS.md:205` "Garbage collection active: 0/1"** — there are **two** GC rules declared (lines 114–122 and 126–137), and one of them, the scheduled sweep, *is* wired in CI. The count is wrong in both directions.
3. **`REFLECTION_LOG.md:8` names `scripts/regenerate-reflection-log.sh`** — the file does not exist (`scripts/` contains exactly two files). This is precisely the failure class GC check 3 exists to catch (`HARNESS.md:137`: *"a harness whose rules name missing tools is worse than no harness: it reports success for checks nobody is running"*) — but the implementation at `harness.yml:87` **hardcodes a two-item allowlist** of script paths instead of parsing `HARNESS.md`, so it structurally cannot detect any tool reference it was not told about in advance. Its `missing=1` assignment (lines 88, 92) is also dead — never read before `exit 0` at line 93.
4. **`HARNESS.md:139–153`** — `## Observability`, `### Operating cadence`, `### Health thresholds`, `### Regression detection` are all `<!-- Not yet configured. Run /harness-init … -->`, while `observability/costs/2026-09-20-costs.md` exists outside them. The harness gives a false account of the project's observability in the negative direction.

### The Cognitive reservoir block

`HARNESS.md:157–195`. Assessed exactly as it presents itself, because it presents itself accurately:

- It declares it is **"NOT a Constraint"** (line 163), never gates, and never writes a claim about anyone's cognitive state to disk. Correct — nothing in the repository reads or writes these values.
- Its five values (`window_hours: 8`, `span_minutes: 180`, `decision_volume: 8`, `context_switches: 4`, `chronotype:` blank) are, in its own words at lines 185–188, **"the shipped defaults, unmodified. They have not been tuned to this project or to its maintainer, and that is worth saying out loud: a threshold someone else chose is a starting point, not a considered limit."**
- The mechanism that would read them (`/reservoir`, `/reservoir tune`) and the skill that grounds them (`skills/cognitive-reservoir/SKILL.md`, cited at line 170) are **both external to this repository**. `chronotype` is undeclared, so by its own rule at lines 180–183 the circadian band is not even labelled.

**Assessment: this block contributes zero enforcement and zero tuned signal, and it is the most honest artefact in the file about its own status.** It belongs in the "declared, not enforced" column — and its self-labelling is why it does not belong in the "ceremony" column. It is a placed marker awaiting calibration. Commit `7e21af9` ("Level 5: the reservoir block, active") is the newest commit on the branch and adds 42 lines to one file; the word "active" in that message is doing more work than the block currently does.

### Cost and observability

`observability/costs/2026-09-20-costs.md` is, by some distance, the strongest single artefact in the repository for honesty-under-measurement:

- It reports **two figures** and refuses to pick one: £0.00 marginal on a Max subscription, ~$211 API list-price equivalent, with the reasoning at lines 7–10 ("neither alone is honest").
- It **self-corrects a published error by two orders of magnitude** (lines 74–78): a previously published "~1.4M tokens" was wrong because task-notification counts exclude cache reads, which are 91% of the total; the true figure is 169M. It names the slide deck, run sheet and findings as corrected.
- It finds the non-obvious cost shape (lines 47–51): output tokens are **11%** of the bill; 89% is cache write + cache read across twenty dispatches.
- **It invalidates the project's own `MODEL_ROUTING.md`** (lines 29–33, 80–82): *"Every dispatch in the run used `claude-opus-5` … none of that routing was exercised … The routing table describes an intent this run did not test."* That is a repository telling you its own 3,655-byte routing document is untested. It is the correct call and it should be quoted on stage.

Against that: it is **one snapshot**, with `Monthly budget: not set` (line 91), `Trend: first snapshot` (line 93), no dashboard, no declared cadence, and the `HARNESS.md` Observability/Operating-cadence/Health-threshold sections empty. There is no searchable durable log of agent activity — the numbers were reconstructed after the fact from session transcripts (lines 19–21), which is exactly what a team without instrumentation has to do.

### Evidence Summary

**Strong and real**: a four-document habitat with a genuinely non-duplicating separation of concerns (turn / loop / sequence / dispatch, stated at `AGENTS.md:10–14`) that is structurally resistant to drift because no document restates another; five deterministic constraints with tooling that exists, two of them build-bound and therefore unavoidable locally; a 242-line purpose-built enforcement test with a meta-test guarding its own parser; a four-scope coordinated loop (edit-time → commit → PR → weekly sweep); one complete, cited reflection→constraint promotion; a spec that demonstrably preceded and drove the code, auditable commit by commit; and a cost snapshot that falsifies the project's own routing document.

**Present but thin**: reflection practice (1 entry, 1 of 2 candidates promoted); observability (1 snapshot, no cadence, no dashboard, no telemetry); soft-gate closure (17 of 46 dispositions pending).

**Declared but not enforced**: all four pipeline gates; the "max 3 review cycles" bound; the merge-time backstop `PIPELINE.md` explicitly promises; the weekly `harness-gc` agent; the cognitive-reservoir thresholds; the commit-scope decision-record check in any clone where the operator did not opt in.

**Absent**: mutation testing; coverage thresholds; vulnerability scanning; OpenTelemetry; any agent, command or skill definition; any reusable/cross-team artefact.

**The reproducibility finding a stage audience will care about most**: `PIPELINE.md` and `MODEL_ROUTING.md` describe ten agents. `.claude/` contains one file, `settings.json`, 16 lines, defining one hook. **Clone this repository and you cannot run its pipeline, cannot run `/harness-audit`, cannot run `/reservoir`, cannot regenerate `REFLECTION_LOG.md`, and cannot reproduce a single one of the six records in `docs/superpowers/`.** The habitat's executable half lives in an unnamed, unpinned, unvendored external plugin. What is in the repository is the *output* of the pipeline plus the *specification* of it — which is exactly the difference between Level 4 and Level 5.

---

## Clarifying Responses

None. The dispatching session could not relay questions. Four gaps that questions would normally close, and how I resolved them from evidence:

| Gap | Resolved from | Resolution |
| --- | --- | --- |
| Are specs written before code? | `git log`: `7b45cff` (spec) → `6d1d88c` (objections) → `8a7c089` (adjudication) → `b46ef1c` (RED) → `7bd7a4a` (GREEN) | **Before, unambiguously.** Auditable in history; no "spec written afterwards" pattern |
| Is this team or individual practice? | `git log --format=%ae` and the consultation record's own statement (`consultations/…:36–41`: the project "declares no `## Stakeholders` section … Every voice below is therefore `inferred`") | **Individual.** Single maintainer. "Consistent across the team" markers scored accordingly |
| Are the observable tools consistently used? | 2 of the last 6 `src/`-touching commits (`49efea8`, `b46ef1c`) carry no `decisions/` change | Consistently at PR scope (which is what the constraint declares), inconsistently at commit scope (which is advisory by design, so this is compliant, not a violation) |
| Is cost tracked? | `observability/costs/2026-09-20-costs.md` | **Once, excellently, with no cadence and no budget set** (line 91) |

---

## Level Assessment

### Primary Level: 4 — Specification Architecture

**Why 4 and not 3.** Level 4's distinguishing requirement is *specifications before code plus an agent pipeline with safety gates*. Both halves are evidenced, not merely claimed. The spec (678 lines, 20 FRs, 16 acceptance scenarios, 8 numbered decisions) exists in git *before* the failing tests, which exist before the implementation — `7b45cff` → `b46ef1c` → `7bd7a4a`. The pipeline ran: `MODEL_ROUTING.md` names ten agents and the cost snapshot corroborates "29 subagent transcripts" (line 19). Four gates were operated by a human who wrote adjudications that changed the spec (O1 narrowed the user story; O2 kept `NOT NULL` and named the foreclosure) and who rejected two code-mode objections with technical rebuttals that cite bytecode inspection. The sophistication reference's *"multiple sophisticated artefacts coordinating"* marker is satisfied by the edit-time → commit → PR → sweep chain. This is well past L3's "CLAUDE.md + 3 constraints + custom agents/skills".

**Why not 5.** L5 requires platform-level governance, cross-team standards, and observability. Scoring each against evidence: **platform tooling — absent** (no plugin, no vendoring, no version pin; the executable half of the habitat is an external dependency that is never even named); **cross-team standards — absent** (single maintainer, no templates, the consultation record's own frontmatter marks every stakeholder voice `inferred` because no stakeholder map exists); **observability — one snapshot, no telemetry, no cadence, and four empty `HARNESS.md` observability sections**. The one L5-flavoured artefact that *is* real, the cost snapshot, spends its strongest paragraph telling you the level-5 routing apparatus was never exercised. The branch is named `level-5-supervising`; **on observable evidence it is a Level 4 repository with Level 5 intentions written down and one Level 5 measurement taken.**

**Why not 3, given the weakest-discipline rule.** Applying the rule strictly on guardrail design (rated 3 below) would argue for L3. I place the level at 4 and state the reasoning explicitly so it can be contested: the guardrail *infrastructure* at L4 grade exists and is well-built (`harness.yml`'s blocking `enforce` job is policy-as-code with three real checks and correct event gating); what is missing is **execution evidence**, because a linear demo branch never opened a PR. Absence of execution in a demonstration repository is weaker evidence against the discipline than absence of mechanism would be. I therefore rate the discipline 3 — reflecting that the guardrails which would bound the *pipeline's own* gates do not exist at all — while placing the overall level at 4 on the strength of the specification architecture, which is the level's defining evidence and which is fully auditable. **A reader who applies the ceiling rule mechanically will get L3 from the same facts; the disagreement is about how to weigh built-but-never-fired, and I have shown the working so the audience can take either side.**

### Discipline Maturity

| Discipline | Strength (1-5) | Evidence |
| --- | --- | --- |
| Context Engineering | **5** | Four documents with genuinely disjoint scopes, stated at `AGENTS.md:10–14`, each explicitly refusing to restate another (`HARNESS.md:17–18`, `CLAUDE.md:3–5`, `copilot-instructions.md:18–19`). `CLAUDE.md` *imports* rather than links (`@AGENTS.md`, commit `ffb2de5` — the change was made deliberately and the reason recorded). `AGENTS.md:95–103` carries a **"Not encodable yet"** section that records what could *not* be turned into a rule and why — context engineering that documents its own limits is the mature form. `MODEL_ROUTING.md` gives a per-agent rationale, not just a table. Deduction of nothing: this is the best-designed habitat surface I have assessed on this codebase. |
| Architectural Constraints | **4** | 6 four-field constraint blocks; 5 deterministic with tools that exist; `SchemaParityTest` (242 lines, 4 tests, meta-test at :231) is purpose-built enforcement, not borrowed CI; the one non-deterministic constraint carries a 7-line justification (`HARNESS.md:102–108`) arguing that encoding it "would produce a rule that is precise and wrong, and people would learn to route around it" — refusing a fake constraint is a *higher*-maturity act than declaring one. Held below 5 by: the stale Status block (lines 203–205 wrong on two of four fields); the hardcoded GC allowlist (`harness.yml:87`) that cannot catch the one live missing-tool instance; the commit-scope check requiring per-clone opt-in; and O3 — a documented, deferred, untriggered hole in the strongest constraint. |
| Guardrail Design | **3** | Real: the `PostToolUse` hook is wired (`settings.json:3–14`) and its 120s timeout was deliberately raised from 20s because "a cold Maven start exceeds 20s, and the hook would fail silently — the failure mode this whole level exists to argue against" (`decisions/2026-09-17…:24–27`); `harness.yml` has a blocking PR job and a scheduled sweep; formatting and schema parity ride the Maven lifecycle. Not real: **every gate in `PIPELINE.md` — four hard, two soft — has zero mechanism**; the "merge-time constraint" the pipeline names as its backstop (`PIPELINE.md:38–39`) does not exist; the cycle bound is prose; the weekly GC agent is absent; 17 pending dispositions are forced by nothing; no coverage threshold, no mutation testing, no vulnerability scanning; and the blocking path has never executed on any of this project's own work. |

### The Weakest Discipline

**Guardrail design** is the ceiling. The project can *say* what must be true (context engineering, 5) and can *check* code-shaped facts (architectural constraints, 4), but it cannot make its own *process* facts hold. The asymmetry is precise and worth naming from the stage: the harness enforces things about the **code** — formatting, tests, schema columns, a file's presence — and enforces **nothing** about the pipeline that produces the code. Every unenforced item in this assessment is a process fact: a disposition, an adjudication, a review-cycle count, a gate, an audit date, a reflection's retirement, a tuned threshold.

That is not an accident, and `PIPELINE.md:65–66` names it in the repository's own words: *"Who notices when too many decisions arrive for one person to give each of them real attention is not a question this level answers."* Seventeen pending dispositions is that question arriving.

---

## Operational Axes (ALCI Part D)

**Placement mode: evidence-first.** The 40-statement survey was not administered (non-interactive dispatch).

| Axis | Placement | Evidence |
| --- | --- | --- |
| Composition | **L4** | "Bounded ensembles of agents composed by a harness; multi-agent workflows first-class." `PIPELINE.md:13–26` defines a 10-stage ensemble with 4 gates and a cycle bound; `MODEL_ROUTING.md:19–30` routes all ten agents by tier with per-agent rationale; the run happened — `observability/costs/…:19` records "29 subagent transcripts", and commits `6c7bbd2`→`b20868a` trace the stage sequence. Read-only critic agents (advocatus-diaboli in two modes, choice-cartographer, convener) are real and their output is in the repository. **Held at L4, not L5**: agents do not self-orchestrate, and — the caveat that matters — **no agent definition exists in this checkout**, so the composition is documented and demonstrated but not reproducible from the repository. |
| Testing | **L3** | "Tests verify code behaviour and basic business outcomes; agent-generated code includes tests before merge; automated functional tests cover critical workflows." 22 test classes; `@WebMvcTest` + MockMvc slices; Testcontainers MySQL and Docker Compose Postgres integration tests; `I18nPropertiesSyncTest`; the 242-line `SchemaParityTest`. Tests-before-code is explicit in history (`b46ef1c` RED → `7bd7a4a` GREEN) and an agent extended the suite mid-review (`49efea8` "Pin the vet guard with the assertion that was missing"). **Not L4**: no system-level regression suite, no exploratory plan, no prod-like environment — and objection O8 (deferred) records that `AS-11` sits in a `@WebMvcTest` with a mocked repository where `@OrderBy` never runs, so "the only test of FR-13 asserts the fixture's insertion order". **Not L2-plus on the mutation half**: no mutation testing, and JaCoCo (`pom.xml:277–293`) reports without a `check` goal or threshold. |
| Observability | **L2** | "Track basic metrics: token spend, latency, request counts." Token spend tracked once, in unusual depth, with cache-tier breakdown and a published self-correction (`observability/costs/2026-09-20-costs.md:39–43, 74–78`). **Not L3**: no dashboard, no known cadence, no per-PR acceptance trend, no mutation-kill rate, no AI-acceptance rate, no perception-reality calibration — and `HARNESS.md:139–153` leaves Observability, Operating cadence, Health thresholds and Regression detection all "Not yet configured". **Arguably not even a clean L2** on the first marker ("we log agent activity in a place we can search"): there is no durable agent-activity log; the snapshot was reconstructed post hoc from session transcripts (line 19–21). L2 is generous and is awarded for the depth and honesty of the one measurement. |
| Governance | **L3** | *See Governance Dimension below for the deep-dive.* Written constitution (`HARNESS.md`) that constrains agents and is enforced for 5 of 6 constraints; the unverified→agent→deterministic promotion ladder demonstrably exercised once (`HARNESS.md:45`); constraints categorised with explicit enforcement class. **Not L4** despite `harness.yml` being genuine policy-as-code with blocking steps: the blocking job has never executed on this project's work (zero PRs); `Last audit: never` (`HARNESS.md:203`); one of two GC rules has no implementation; and governance over the pipeline's own gates is nil (zero mechanism references `disposition`/`pending`). L4 requires machine-enforced constraints with explicit blocking rules *in operation*, and operation is exactly what is unevidenced. |

**Operational axes mean: L3.0**

---

## Habitat Build Gap

```text
Level placement (from cognitive assessment): L4
Operational axes mean (Part D):              L3.0
  Composition:    L4
  Testing:        L3
  Observability:  L2
  Governance:     L3
Habitat Build Gap:                           +1.0
Interpretation:                              Ambition outpaces enablement
```

A gap of **+1.0** puts this squarely in *ambition outpaces enablement*, and the axis spread tells you exactly where the ambition is unsupported. Composition is the one axis that matches the cognitive level — the team genuinely thinks and works in bounded multi-agent ensembles, and can prove it. Observability, at **L2, is two full levels below the cognitive placement**, and it is the binding constraint on everything else: a team that cannot see agent activity at a cadence cannot know whether its gates are being operated, whether its 17 pending dispositions are growing, whether its routing table is exercised, or whether its reservoir thresholds are wrong — and in every one of those cases the repository has had to *reason its way to the answer in prose* rather than read it off an instrument. That is why the same finding recurs in four different forms across this assessment: the harness measures code and narrates process. The investment that closes this gap is **not more habitat documents** — the context-engineering discipline is already at 5 and adding to it will widen the gap, not close it. It is instrumentation, cadence, and converting four or five of the process claims already written down into checks that run.

The healthy reading: coherence, not level size, is the headline signal, and a +1.0 gap on a repository that *documents its own gap this accurately* is far more tractable than a +1.0 gap on a repository that does not know it has one.

---

## Governance Dimension

> **Relationship to the Governance operational axis.** This section is the governance deep-dive; the Governance axis row above is its one-line operational placement feeding the Habitat Build Gap. Both report **Level 3**.

### 1. Governance constraint count

Six constraint blocks in `HARNESS.md`. Classifying by whether the constraint governs *how the collaboration is conducted* versus *what the code must look like*:

| Constraint | Lines | Governance-related? | Enforcement |
| --- | --- | --- | --- |
| Decision record for source changes — commit | 56–67 | **Yes** — governs durability of agent judgement calls | deterministic, advisory at commit, opt-in per clone |
| Decision record for source changes — PR | 69–79 | **Yes** — same | deterministic, blocking; never fired (no PRs) |
| Abstraction earns its place | 95–108 | **Yes** — governs design judgement under review | advisory (agent/human), honestly labelled |
| Consistent formatting | 25–33 | No — code quality | deterministic, build-bound ✔ |
| Tests must pass | 35–41 | No — code quality | deterministic ✔ |
| Schema parity across databases | 81–93 | No — code quality | deterministic ✔, demonstrated (`cd785a6`) |

**3 of 6 constraints (50%) are governance-related. Of those three, exactly zero have demonstrably blocked anything** — two require a pull-request event that this branch's history never produced, and one is advisory by design. The three that *have* bitten are all code-quality constraints.

Beyond the constraint blocks: 2 GC rules declared, **1 implemented** (the CI sweep; the weekly `harness-gc` agent does not exist). Governance audit cadence: **none declared, and `Last audit: never`**. Cognitive reservoir: declares itself not a constraint, carries untuned shipped defaults, has no in-repo mechanism.

### 2. Governance ALCI items

**The eight governance ALCI items (Levels 0, 2, 3, 5) are not distributed with the plugin** — `grep` across the `ai-literacy-superpowers` skill and reference set finds the items referenced in `agents/assessor.agent.md:316` but never defined. I therefore score against the **Governance axis L1–L5 marker statements** in `references/operational-axes.md`, which are available, and flag the missing instrument as an issue for the framework rather than for this project.

| Marker | Verdict | Evidence |
| --- | --- | --- |
| L1a — governance implicit, trust-based, no written policies | **Refuted** | `HARNESS.md`, `AGENTS.md`, `PIPELINE.md` |
| L1b — members use AI differently, no agreed norms | **Refuted** | `AGENTS.md:3–5` "the single durable source … human or agent"; single maintainer |
| L2a — conventional/informal norms only | **Refuted** | Norms are codified and tool-backed |
| L2b — discussed but not codified | **Refuted** | `AGENTS.md:22–34` records three directives *promoted* from prose to constraint |
| **L3a — written constitution constraining agents, and we enforce it** | **Met** | 5 deterministic constraints; 2 build-bound; violation demonstrated on `demo-violation-schema-and-decision-record` |
| **L3b — constraints categorised and promoted unverified → agent → deterministic** | **Met** | Every block declares `**Enforcement**`; `HARNESS.md:45` cites the promotion by reflection-entry and candidate number |
| L4a — policy-as-code, machine-enforced in CI with explicit blocking rules | **Partially met** | `harness.yml:22–59` is exactly this; **but it has never run on this project's work**, and it covers zero of the pipeline's own gates |
| L4b — constraints map to falsifiable behaviour, not aspirational language | **Partially met** | The 5 deterministic ones are falsifiable and the 1 that isn't says so at length (`HARNESS.md:102–108`). **But** `PIPELINE.md:13–26`'s four gates, its cycle bound, and its claimed merge-time backstop at :38–39 are aspirational language in a document that presents them as mechanism |
| L5a — continuous certification; every change carries evidence of compliance | **Not met** | 2 decision records for 25 commits; no compliance evidence attached to changes; no certification artefact |
| L5b — institutional reference frame explicitly modelled | **Not met** | `consultations/…:36–41` states the project declares no stakeholder section, so all five voices are `inferred` — the institutional frame is acknowledged as *absent*, which is honest but is the opposite of modelled |

### 3. Governance readiness summary

**Level 3 — solid, evidenced, and exercised; with a Level 4 apparatus built but unexercised.**

Both L3 markers are met without qualification, and the promotion ladder is not just declared but demonstrably used once end-to-end with a citation. That is more than most repositories at this level can show. Both L4 markers are *partially* met, and both partials have the same root cause: the governance mechanism exists in the repository but the repository's own workflow never routes through it. A PR gate that has never seen a PR is a governance control in the same sense that an untested backup is a recovery plan.

The second L4 shortfall is more serious than the first and is the one I would put on a slide: **`PIPELINE.md` is written in the register of enforcement and delivers none.** "A hard gate refuses to advance while anything is `pending`" (`:37`) describes a mechanism; nothing refuses. "A merge-time constraint in `HARNESS.md` is what eventually forces the issue" (`:38–39`) names a specific artefact that does not exist. A reader — human or agent — has no way to tell from the text which of `PIPELINE.md`'s claims are backed by code and which are backed by the maintainer's memory, whereas `HARNESS.md` makes that distinction on every single block via its `**Enforcement**` field. The project already owns the right convention; `PIPELINE.md` does not use it.

### 4. Governance recommendations

Level 3 → 4, in priority order:

1. **`/governance-constrain` — write the constraint `PIPELINE.md` already promises.** One deterministic, falsifiable block: *no record under `docs/superpowers/` reaches the default branch with `disposition: pending`* (or with a stated, bounded allowance for soft-gate records). A ten-line `grep` in `harness.yml` implements it. This closes the repository's single largest declared-but-unenforced claim and would today report 17.
2. **`/governance-constrain` — promote the unpromoted reflection candidate (2).** Every length-limited column in `db/*/schema.sql` has a matching `@Size`. `SchemaParityTest` already parses column lengths across three dialects (`:121–153`); extending it to assert an entity-side `@Size` is incremental, not new infrastructure. This also closes the compound-learning loop that has been open since 2026-09-16.
3. **`/governance-audit` — establish the baseline and fix `Last audit: never`.** The Status block is the harness's self-report and it is wrong on two of four fields (GC count `0/1` should be `1/2`; audit date `never` contradicts `decisions/2026-09-17-harness-drift-fixes.md`). Set a quarterly cadence and record it in the currently-empty `HARNESS.md` `### Operating cadence` section.
4. **Adopt `HARNESS.md`'s `**Enforcement**` convention in `PIPELINE.md`.** Tag each of the six gates `human`, `agent` or `deterministic`. Five of six will read `human`. That single edit converts the document from over-claiming to accurate at zero implementation cost, and it is the highest honesty-per-character change available in this repository.
5. **Make GC check 3 parse `HARNESS.md` instead of a hardcoded allowlist** (`harness.yml:84–93`). It would then catch `REFLECTION_LOG.md:8`'s reference to the non-existent `scripts/regenerate-reflection-log.sh` — the exact failure the check was written for. Also read or remove the dead `missing` variable.

Level 4 → 5 (not yet in scope, listed so the ladder is visible): `/governance-health --dashboard` for governance visibility; declare a `## Stakeholders` section so consultation voices stop being `inferred`; vendor or version-pin the plugin so the habitat is reproducible from a clone.

---

## Strengths

1. **A habitat that cannot drift, by construction.** Four documents, four disjoint scopes, declared once at `AGENTS.md:10–14`, with each file explicitly refusing to restate another (`HARNESS.md:17–18`, `CLAUDE.md:3–5`, `copilot-instructions.md:18–19`). Most projects prevent drift with discipline; this one prevents it by removing the duplicated surface that could drift. `CLAUDE.md` imports rather than links, and the reason is a commit (`ffb2de5`).
2. **Enforcement that is purpose-built and self-verifying.** `SchemaParityTest` is 242 lines of dialect-aware parsing written specifically to make `AGENTS.md` directive 2 checkable, with a meta-test at line 231 that guards against the silent-green failure mode of regex-based checks. Its constraint was demonstrated to fire via a deliberate-violation branch (`cd785a6`). This is what "make the rule hold when no agent is watching" (`SchemaParityTest.java:42–43`) looks like when someone means it.
3. **Specification genuinely preceded and drove implementation, auditably.** The commit sequence `7b45cff` → `6d1d88c` → `8a7c089` → `f1eecd0` → `b46ef1c` (RED) → `7bd7a4a` (GREEN) → `d943593` (review) → `b20868a` (integration gate) is the Level 4 loop, in order, in public. Records were written *before* their dispositions in every case — the discipline `PIPELINE.md:29–35` demands.
4. **The project reports against its own interest, repeatedly and specifically.** The spec states that its nine deferrals have no trigger and that "nothing … will raise them again" (`:674–678`). The cost snapshot says the routing table "describes an intent this run did not test" (`:33`) and publishes a two-orders-of-magnitude self-correction (`:74–78`). `HARNESS.md:185–188` says the reservoir defaults are untuned and that this "is worth saying out loud". The consultation record says every voice is `inferred` because no stakeholder map exists (`:36–41`). Most repositories at any level do not do this once; this one does it four times.
5. **A refusal to fake a constraint.** `HARNESS.md:102–108` explains at length why "abstraction earns its place" stays advisory: a caller-counting rule "would produce a rule that is precise and wrong, and people would learn to route around it". Declining to encode an unencodable rule, and labelling the gap, is higher-maturity than the constraint count it costs.

---

## Gaps

1. **Every pipeline gate is prose.** Four hard gates, two soft gates, a cycle bound, and an explicitly-named merge-time backstop, with **zero implementing mechanism** — `grep` for `disposition|pending` across all `*.sh`, `*.yml`, `*.json` and both habitat documents returns nothing. `PIPELINE.md:38–39` names a `HARNESS.md` constraint that does not exist.
2. **The blocking path has never executed.** `harness.yml`'s `enforce` job is correctly gated on `pull_request` (`:27`); all 25 commits since the harness was built are direct pushes to a feature branch. The two governance constraints that block, block nothing so far.
3. **Observability is one snapshot and four empty sections.** No dashboard, no cadence, no durable agent-activity log, no OTel, no budget set, `HARNESS.md:139–153` unconfigured. This is the axis two levels below the cognitive placement and the binding constraint on the rest.
4. **Compound learning has stalled at one entry and half a promotion.** One reflection, two candidates, one promoted; the other has been latent for five days across five levels. The GC rule meant to catch recurring unpromoted patterns names an agent that does not exist, and the CI substitute only fires above 10 active entries.
5. **The habitat is not reproducible from the repository.** Ten agents, eight slash commands and one skill are cited across `PIPELINE.md`, `MODEL_ROUTING.md`, `HARNESS.md:170`, `REFLECTION_LOG.md:8` and `copilot-instructions.md:4`; `.claude/` contains one 16-line `settings.json`. Nothing names, pins, or vendors the plugin that holds them. That is the concrete difference between this and Level 5.
6. **No mutation testing, no coverage threshold, no vulnerability scanning.** JaCoCo reports and nothing reads the report. For a repository whose thesis is "only something that checks can make it true" (`REFLECTION_LOG.md:31`), the absence of a test-quality check is a conspicuous asymmetry.

---

## Recommendations

Ordered by impact on the Habitat Build Gap.

1. **Instrument before you extend — close the Observability axis first.** This is the +1.0 gap's centre of mass at L2 against an L4 placement. Concretely: a durable agent-activity log (the cost snapshot had to be reconstructed from transcripts, which is the L1→L2 boundary in action), a declared capture cadence written into the currently-empty `HARNESS.md:143–145`, and a second cost snapshot so `Trend:` stops reading "first snapshot, no trend available". Addresses: *feedback loops*, and every "we do not know whether X is happening" finding in this document. **Do this before adding any further habitat document** — context engineering is at 5, and more prose widens the gap.
2. **Implement the pending-disposition constraint `PIPELINE.md` already promises.** A `grep -r 'disposition: pending' docs/superpowers/` step in `harness.yml` with a stated allowance, plus a `HARNESS.md` block declaring it. Highest-leverage single change available: it converts the repository's largest false enforcement claim into a true one, closes the soft gates the pipeline has no backstop for, and would today report 17. Addresses: *guardrail design* — the ceiling discipline.
3. **Tag every `PIPELINE.md` gate with an `**Enforcement**` class.** Five of six will read `human`. Zero implementation cost, and it makes the document as honest as `HARNESS.md` already is. Until this is done, a reader cannot distinguish `PIPELINE.md`'s mechanisms from its intentions — and for a document read by agents, that ambiguity is itself a defect.
4. **Retire or promote the open reflection, and give the deferred objections a trigger.** Candidate (2) has been open since 2026-09-16; the nine deferred objections have no revisit condition by the spec's own admission, and two of them (O3, O6) describe live holes in constraints the harness presents as sound. A trigger can be as light as "revisited when S1 is specced" recorded in the objection frontmatter. Addresses: *compound learning* — currently the thinnest discipline by volume.
5. **Make the habitat reproducible: name and pin the plugin, then fix the drift.** Record the plugin and version that supplies the ten agents and eight commands; that one line converts `docs/superpowers/` from artefacts into a rerunnable pipeline and is the gating prerequisite for any honest Level 5 claim. While there: correct `HARNESS.md:203–205` (`Last audit: never` → the 2026-09-17 `/harness-health` run; `Garbage collection active: 0/1` → `1/2`), and fix or remove `REFLECTION_LOG.md:8`'s reference to the non-existent `scripts/regenerate-reflection-log.sh`.

---

## Immediate Adjustments Applied

**None.** Per the dispatching instruction, this assessment is read-only: no file in the repository was created, edited or deleted; no README badge was added; no commit was made; no build was run. The adjustments the skill's Phase 4 would normally apply — correcting the stale `HARNESS.md` Status block and the missing-script reference in `REFLECTION_LOG.md` — are recorded as recommendation 5 instead.

## Workflow Operation Changes

**None applied** (read-only run). Recommended for the maintainer's decision: a quarterly `/governance-audit` cadence written into `HARNESS.md`'s empty `### Operating cadence` section; a cost-snapshot cadence; and a reflection-review rhythm that does not depend on the count exceeding ten.

## Reflection on the Assessment Itself

Three things the scan revealed that a reader should carry away.

**Paperwork volume and enforcement strength are close to orthogonal here, and the ratio is measurable.** `docs/superpowers/` holds 3,518 lines of records; `decisions/` holds another 533; the enforcement layer is 242 lines of test plus 114 lines of shell plus 93 lines of workflow. The records are not padding — the code-mode objections in particular contain findings (a Thymeleaf attribute-precedence analysis verified with `javap`) that no lint could produce. But the 37% pending rate and the eleven-fold repetition of "need the spec tightened" show what happens when the volume of decisions produced exceeds the attention available to adjudicate them, and the repository predicted exactly this at `PIPELINE.md:65–66`.

**The most valuable artefact in the repository is the one that attacks the repository.** `observability/costs/2026-09-20-costs.md` invalidates `MODEL_ROUTING.md`, corrects a previously published figure by 100×, and identifies that 89% of the spend is context re-reading rather than production. A single honest measurement did more to locate this project's real state than all 3,518 lines of records. That is the argument for recommendation 1 in one sentence.

**Future assessments of this repository should check execution, not existence.** Every serious finding here came from asking "and has it run?" rather than "does it exist?" — the PR gate exists and has never fired; the GC agent is named and does not exist; the routing table is complete and untested; the reservoir thresholds are declared and untuned; the backstop is cited and absent. A scan that stopped at file presence would have returned Level 5 on this repository, and it would have been wrong.

---

## Next Assessment

Suggested re-assessment date: **2026-12-21** (quarterly).

Previous assessment: first assessment of this branch. Intended to be read alongside the Level 1 assessment of the same codebase (`level-1-dictating`).

**Badge that would be applied if this were not a read-only run** (for you to place in the external copy):

```markdown
[![AI Literacy](https://img.shields.io/badge/AI_Literacy-Level_4_Specification_Architecture-2E8B57?style=flat-square)](assessments/2026-09-21-assessment.md)
```

Note for the stage: the repository's existing badge at `README.md:4` reads `Harness-5_enforced_1_advisory` and is accurate as to the constraint *count*. A reader checking whether those five have ever enforced anything on this branch will find that two of them are waiting on a pull request that has not been opened.