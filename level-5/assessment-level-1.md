I have complete evidence. Writing the assessment as my reply — nothing was written into the checkout.

---

# AI Literacy Assessment — Spring PetClinic (`level-1-dictating` baseline)

**Date**: 2026-09-21
**Assessed by**: `assessor` agent (Claude Opus 5), evidence-only, no clarifying questions available
**Subject**: `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1` — detached worktree, `HEAD` = `818c413`, 132 tracked files, 1042 commits, 153 distinct authors, 2013-01-09 → 2026-08-19
**Assessed level**: **Level 1 — Dictating**
**Scan mode**: single-context (132 files, below the 300-file workflow-mode threshold)

**Nothing was written into the assessed checkout.** No assessment file, no README badge, no edits, no commit. Phases 4–6 of the standard assessor process were deliberately suppressed on instruction; the baseline's emptiness is the evidence and an artefact appearing in it would destroy what it demonstrates.

---

## Habitat document discovery (Phase 1a)

### HARNESS.md

- **Status**: not found
- **Path**: —
- **Markers matched**: none — no candidate file existed to test markers against
- **Notes**: all ten conventional and alternative paths checked; no renamed equivalent present. The repository contains exactly **one** markdown file in total (`README.md`), so there is no file anywhere in the tree that could carry HARNESS markers under a different name.

### AGENTS.md

- **Status**: not found
- **Path**: —
- **Markers matched**: none
- **Notes**: all five paths checked. No `ARCH_DECISION` headings, no gotchas section, no reflection references anywhere in the tree.

### CLAUDE.md

- **Status**: not found
- **Path**: —
- **Markers matched**: none
- **Notes**: all four paths checked.

### Ambiguities

None. Discovery was unambiguous — there were no candidates at all, so no silent pick was possible and no user resolution is required.

### Paths checked but not matched

Every path below was checked and does not exist (no path-only candidates arose):

`HARNESS.md`, `docs/HARNESS.md`, `.ai/HARNESS.md`, `.agents/HARNESS.md`, `conventions/HARNESS.md`, `governance/HARNESS.md`, `CONVENTIONS.md`, `STANDARDS.md`, `docs/conventions.md`, `docs/standards.md`, `AGENTS.md`, `.agents/AGENTS.md`, `docs/AGENTS.md`, `.claude/AGENTS.md`, `.cursor/AGENTS.md`, `CLAUDE.md`, `.claude/CLAUDE.md`, `docs/CLAUDE.md`, `.ai/CLAUDE.md`

**Corroborating whole-tree search.** Two independent sweeps confirm the absence rather than resting on the path list alone:

1. `git ls-files | grep -i '\.md$'` returns exactly one line: `README.md`.
2. A case-insensitive `find` across the whole working tree for `*harness*`, `*agents*`, `*claude*`, `*cursor*`, `*copilot*`, `*windsurf*`, `*.mdc` returns **zero** results.

This is genuine absence across a completed search, not "not at the default path".

---

## Observable Evidence (Phases 1b–1c)

### Parallel-tool config surfaces

Applying `tool-config-evidence.md` in full. Every surface checked, none exist:

`.cursor/`, `.cursor/rules/`, `.github/copilot-instructions.md`, `.windsurf/`, `.windsurf/rules.md`, `.windsurf/rules/`, `.ai/`, `.llm/`, `tools/ai/`, `scripts/ai/`, `.claude/`, `.aider.conf.yml`, `.continue/`, `.codeium/`

The "habitat expressed elsewhere" finding does not apply here. This project is not routing harness control through Cursor, Copilot, or Windsurf instead of Claude — it is not routing it anywhere.

**Decisive negative signal.** `git grep -niE "copilot|claude|cursor|windsurf|openai|anthropic|llm|\bgpt\b|codeium|aider"` across all tracked files returns **zero** genuine hits. The only matches are `cursor: pointer` / `cursor: help` CSS declarations in `src/main/resources/static/resources/css/petclinic.css` and an `.allMatch(...)` call at `src/test/java/org/springframework/samples/petclinic/service/ClinicServiceTests.java:234`. The README (181 lines) contains no AI, LLM, agent, or prompting reference of any kind. No commit message in 1042 commits mentions an AI tool.

### Repository signals

| Signal | Found | Level indicator |
| --- | --- | --- |
| CI workflows | **yes** — 3: `.github/workflows/maven-build.yml`, `gradle-build.yml`, `deploy-and-test-cluster.yml` | L2 |
| Test coverage enforcement | **no** — JaCoCo 0.8.15 at `pom.xml:277-293` binds `prepare-agent` and `report` only; no `check` goal, no `<rule>`, no threshold | L2 |
| Vulnerability scanning | **no** — no OWASP dependency-check, no Trivy/Scout/Snyk, no `dependabot.yml`, no `renovate.json`. CycloneDX SBOM generation at `pom.xml:308-312` produces an artefact but performs no scanning | L2 |
| Mutation testing | **no** — no PIT, no mutation config anywhere | L2 |
| CLAUDE.md | **no** — Phase 1a, 4 paths | L3 |
| HARNESS.md | **no** — Phase 1a, 10 paths; constraint count **0** | L3 |
| AGENTS.md | **no** — Phase 1a, 5 paths; entry count **0** | L3 |
| MODEL_ROUTING.md | **no** | L3 |
| Custom skills | **no** — `.claude/skills/` absent | L3 |
| Custom agents | **no** — `.claude/agents/` absent | L3 |
| Custom commands | **no** — `.claude/commands/` absent | L3 |
| Hooks configured | **no** (see caveat below) | L3 |
| REFLECTION_LOG.md | **no** — also no `reflections/`, no `reflections/archive/`. Combined read per the reflection-log read policy returned empty | L3 |
| Specifications directory | **no** — `specs/` absent | L4 |
| Implementation plans | **no** | L4 |
| Orchestrator with safety gates | **no** — no orchestrator; `MAX_REVIEW_CYCLES` / `GATE` / `GUARDRAIL` appear nowhere | L4 |
| Plugin/platform tooling | **no** | L5 |
| OTel configuration | **no** — no OpenTelemetry, Micrometer tracing, Prometheus, or Zipkin dependency in `pom.xml` or `build.gradle` | L5 |

### Hooks caveat — an inherited-container artefact, not branch evidence

`git config --get core.hooksPath` inside the checkout returns `.githooks`, which could be misread as hook evidence. It is not. Tracing the origin:

- `.git` in the checkout is a **file**, not a directory: `gitdir: /Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/.git/worktrees/assess-l1`. This is a linked worktree of the ladder repository.
- `git config --show-origin --get core.hooksPath` resolves to `file:/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/.git/config` — the **parent** ladder repo's config, shared across all worktrees.
- `.githooks/` does not exist in this working tree, and `git ls-tree -r HEAD --name-only | grep -iE "githooks|hooks"` returns nothing — it is not tracked at this commit.

The hooks path is a property of the container the baseline is checked out into, inherited from the ladder repo that holds the higher-level branches. It points at a directory that does not exist here. **Recorded as not-evidence.** Any future re-assessment of this baseline should re-apply this check rather than trusting `git config` output inside a worktree.

### Sophistication markers (per `sophistication-markers.md`)

Cited explicitly for auditability, as the reference requires:

- **Hooks**: 0 artefacts. No simple markers, no sophisticated markers. Nothing to analyse.
- **Scripts**: 0 habitat scripts. `mvnw` (11791 bytes), `mvnw.cmd`, `gradlew` (8654 bytes), `gradlew.bat` are vendor-supplied build wrappers shipped verbatim by Maven and Gradle — not team-authored orchestration, and explicitly **not** counted as script evidence under either the simple or sophisticated marker sets.
- **Agents**: 0 artefacts.
- **Commands**: 0 artefacts.

**Adjustment applied**: none. The reference's table maps "0–2 hooks/scripts/agents/commands, all simple" to an L2 maximum; this repository sits below even that floor at zero artefacts of every type, so no sophistication adjustment raises any discipline. No silent shift occurred — there was no marker to shift on.

### What the codebase *does* have (and why it is not AI-collaboration evidence)

This is where honesty matters most, and the brief is right to flag it. The underlying Java project is genuinely well-engineered:

- **78 `@Test` methods across 20 test classes** under `src/test/java/`, covering 30 main source files. MockMvc controller tests span the critical user workflows — `OwnerControllerTests`, `PetControllerTests`, `VisitControllerTests`, `VetControllerTests`, `WelcomeControllerTests`, `CrashControllerTests`.
- **Real integration tests**, not just unit tests: `MySqlIntegrationTests` and `PostgresIntegrationTests` run against live databases via Testcontainers (`pom.xml:158-180`); `PetClinicIntegrationTests`; and a `PetClinicConcurrencyTests` class.
- **A genuinely creative guardrail**: `src/test/java/.../system/I18nPropertiesSyncTest.java` walks `src/main`, regex-scans `.java` and `.html` sources for hard-coded text literals lacking `th:text` attributes, and fails the build on non-internationalised strings *and* on partial translations across locale bundles. This is a property-style constraint over the source tree enforced as a test — structurally the most sophisticated verification artefact in the repository.
- **Deterministic style enforcement in the build's `validate` phase**: `spring-javaformat-maven-plugin` with the `validate` goal (`pom.xml:206-215`) and `maven-checkstyle-plugin` running `nohttp-checkstyle` with the `check` goal (`pom.xml:220-248`). Mirrored in `build.gradle:70-87`. These **fail the build**, they do not merely warn.
- **`maven-enforcer-plugin`** with `requireJavaVersion` (`pom.xml:187-204`) — a hard toolchain floor with a custom failure message.
- **CI on every push and PR to `main`**: `./mvnw -B verify` and `./gradlew build`, which means the test suite, the formatter validation, and the nohttp check are all merge-gating.
- **DCO sign-off enforcement** via `.github/dco.yml`.
- **Commit hygiene consistent with small, reviewable changes**: the last 15 commits average well under 100 lines changed; the largest is a 15-file Spring Boot 4.1 upgrade at +54/−43; several are single-purpose test additions ("Add test for duplicate pet name during update", 1 file, +15).

**None of this is AI-collaboration evidence, and treating it as such would be the central error this assessment must avoid.** Three reasons, stated precisely:

1. **Provenance.** 1042 commits from 153 distinct authors spanning 2013–2026 is the signature of a long-running open-source community project — the canonical Spring PetClinic sample. This engineering discipline predates the current AI tooling era by roughly a decade. It was not built to constrain an agent; it was built to let a large, rotating pool of drive-by human contributors modify a reference application safely.
2. **Direction of constraint.** The framework's architectural-constraints discipline asks what bounds an *agent's* action space. `spring-javaformat validate` bounds a *contributor's* diff. The two are not the same artefact even when they are the same plugin invocation — the difference is whether anyone has reasoned about, declared, or tuned it as an agent-facing constraint. Nobody here has, because no agent has been here.
3. **No feedback loop to an agent.** The `tool-config-evidence.md` reference draws the convention-versus-constraint line and notes what tool evidence does *not* signal. The complementary point applies here: enforcement that no agent is subject to is not guardrail design, because there is no loop. A failing `checkstyle` run teaches a human contributor something; it currently teaches nobody and nothing else.

**What the codebase's discipline is genuinely carrying**: it is a **latent harness**. The moment an agent is pointed at this repository, `./mvnw verify` becomes a real, deterministic, merge-gating verification loop for agent output — 78 tests, two database integration suites, a formatter that fails the build, and an i18n property check, all of it free and all of it already wired into CI. That is an unusually strong starting position, and it is the single most important asset in this baseline. But it is *inherited capability*, not *practised literacy*. The distinction between the two is precisely what the Habitat Build Gap exists to measure, and it is why the Testing axis below is the only axis that rises off the floor.

One more thing the codebase does *not* carry: Spring Boot Actuator is present (`pom.xml:44`) with `management.endpoints.web.exposure.include=*` in `src/main/resources/application.properties`. That is *application* runtime observability. It is explicitly **not** Observability-axis evidence, which concerns visibility into *agent activity* — token spend, latency, acceptance rates, mutation kill rates, calibration. Conflating the two would inflate the Observability axis by a full two levels on evidence that has nothing to do with AI collaboration.

---

## Clarifying Responses

**None obtained.** The dispatching session cannot relay questions to the maintainer mid-run, so Phase 2 was suppressed by instruction and the assessment rests on observable evidence only. Assumptions made in place of answers, stated explicitly so they can be challenged:

1. **The checkout's declared premise is taken at face value** — that this is the `level-1-dictating` rung of a conference-talk ladder, representing the Spring PetClinic codebase with no AI-collaboration habitat. This premise is load-bearing for the L1-rather-than-L0 placement (see below) and for nothing else. Every other finding is independently evidenced.
2. **The AI collaboration being characterised is assumed to be a developer prompting a chat interface outside the repository**, leaving no filesystem trace. This is the definitional shape of L1 and is consistent with — though not provable from — the evidence.
3. **The upstream Spring PetClinic maintainer community is assumed not to be the "team" under assessment.** The team is whoever would work in this repository at the talk. The upstream community's engineering practices are treated as inherited environment, not as the assessed team's practice.
4. **No opt-in survey was offered or administered**, since Phase 2 was suppressed. All four operational axes are placed **evidence-first**.

Because no self-report was gathered, the usual check — does observable evidence diverge from the team's self-perception? — cannot be performed. On a baseline this empty there is little room for divergence on the habitat side, but the *self-perception of verification rigour* would be the thing worth probing at the talk: it is easy for a team sitting on a suite like this one to believe they are verifying AI output systematically when what they actually have is a suite that predates them.

---

## Level Assessment

### Primary Level: 1 — Dictating

**Rationale.**

*Why not higher.* The scoring heuristic sets L2 at "automated tests in CI **and** systematic verification of AI output". The first clause is satisfied emphatically — three workflows, 78 tests, merge-gating `verify`. The second clause has **zero** supporting evidence: there is no AI output in this repository to verify, no recorded practice of verifying it, and no artefact anywhere acknowledging that an AI collaborator exists. A verification loop that no AI output passes through does not evidence the verification *discipline* as the framework defines it. Independently, the weakest-discipline-is-the-ceiling rule fixes the ceiling at Context Engineering, which scores 1/5 on a completed search across nineteen habitat paths and every known parallel-tool surface. L3 and above are ruled out several times over: no habitat document at any path, zero harness constraints against a required minimum of three, zero custom agents or skills, no specs, no orchestrator, no plugin, no telemetry.

*Why not lower.* This is the genuinely difficult call, and it deserves to be stated plainly rather than smoothed over. **L1 leaves no filesystem trace by construction.** "Some AI tool usage, basic prompting" happens in a chat window; dictating to an assistant and pasting the result back produces a commit indistinguishable from a hand-written one. The artefacts in this repository are therefore *equally consistent* with L0 (aware of AI tools, has not used them here) and L1 (prompting, ad hoc, nothing captured). No amount of further scanning can separate the two — and that unfalsifiability is itself the diagnostic signature of the L0–L1 band. I place the assessment at **L1 on the checkout's declared premise**, and record that on artefacts alone the honest finding is **"L0–L1, indeterminate"**. If the premise were withdrawn, the placement would not survive; nothing else in this report depends on it.

This is worth dwelling on for the talk, because it is the first rung's actual lesson: at Level 1 there is nothing to assess. An assessor can prove the *absence* of levels 2 through 5 with high confidence and a complete audit trail, but cannot prove the *presence* of level 1. The collaboration is real, and it is entirely invisible to everyone except the person doing it — including to that person's future self, to their colleagues, and to any tooling. Every rung above this one is, among other things, an act of making the collaboration visible enough to be examined.

### Discipline Maturity

Each discipline is scored **as an AI-collaboration discipline** (the framework's subject). Where the underlying codebase carries a materially different score as plain software engineering, that is stated in the same cell so the gap is explicit rather than buried.

| Discipline | Strength (1-5) | Evidence |
| --- | --- | --- |
| Context Engineering | **1** | Absolute floor. Zero habitat documents across 19 paths (Phase 1a); zero parallel-tool configs across 14 surfaces; zero AI references in 132 tracked files, 181 README lines, and 1042 commit messages. No `.claude/` directory. Nothing in this repository tells any agent anything about this project. *As software engineering:* the README is a solid 181-line contributor guide (local run, container build, DB config, IDE setup, contribution route) — real human-facing context engineering, addressed exclusively to humans, and never loaded into any agent's context because no agent is ever invoked. |
| Architectural Constraints | **2** | Scored above the floor deliberately, and the reasoning must be auditable. Zero constraints authored for AI collaboration: no HARNESS.md, no constraint blocks, no `unverified → agent → deterministic` promotion ladder, no declared enforcement. But real deterministic enforcement exists and would bind an agent the instant one arrived: `spring-javaformat` `validate` goal (`pom.xml:206-215`) and `checkstyle` `check` goal (`pom.xml:220-248`) both fail the build, `maven-enforcer` pins the Java floor (`pom.xml:187-204`), and CI runs all of it on every PR. This is a **latent** harness — enforcement present, intent absent, agent-facing tuning absent. Scored 2 rather than 1 to record the capability; **not** scored 3, because an undeclared constraint cannot be reasoned about, audited, promoted, or deliberately relaxed, and the framework's L3 bar is a *declared* constraint set with a stated enforcement ratio. |
| Guardrail Design | **1** | As AI guardrail design: nothing. No hooks, no agent-facing feedback loop, no bounded-loop discipline, no `MAX_REVIEW_CYCLES`, no review gate on agent output, no mutation testing to verify the tests themselves, no coverage gate (JaCoCo reports but never fails), no vulnerability scanning, no dependency automation. *As software-engineering feedback loops the codebase would score 3:* 78 tests, Testcontainers integration suites against MySQL and Postgres, a concurrency test, and the `I18nPropertiesSyncTest` property check — all merge-gating via `./mvnw -B verify` on every push and PR. The scoring gap between 1 and 3 in this row **is** the finding: the loops exist and are strong, but no AI output passes through them, so no guardrail has ever been designed, tested, or tuned against agent behaviour. |

### The Weakest Discipline

**Context Engineering (1/5)** is the ceiling, and it is the ceiling by the widest possible margin — a completed nineteen-path search returning nothing, corroborated by two independent whole-tree sweeps.

It is also the *cheapest* ceiling to lift, which is what makes this baseline instructive rather than merely bleak. Architectural Constraints and Guardrail Design both have substantial latent capability sitting underneath them, already wired into CI and already merge-gating. Context Engineering has nothing underneath it at all — and needs nothing but a file. One `CLAUDE.md` that tells an agent how to build, how to test, and that `spring-javaformat` will fail the build if it reformats anything, converts the existing latent harness into an active one in a single commit. Until that file exists, every one of those constraints is a trap an agent discovers by failing CI rather than a rule it can follow.

---

## Operational Axes (ALCI Part D)

**Placement mode: evidence-first.** The 40-statement survey was not offered, because Phase 2 was suppressed by instruction (no channel to the maintainer). All four placements derive from repository evidence per the evidence map in `operational-axes.md`.

| Axis | Placement | Evidence |
| --- | --- | --- |
| Composition | **L1** | Zero custom agents (`.claude/agents/` absent), zero critic or reviewer agents, no orchestrator, no multi-agent workflow scripts, no agent-team documentation (no AGENTS.md at any of 5 paths), no specs defining composition. Also zero *saved* composition: no `.claude/commands/`, no prompt library, no captured patterns anywhere in the tree. Matches the L1 markers exactly on both statements — "a single agent through ad-hoc prompts" and "I rarely save prompts or patterns between sessions". Nothing in the repository reaches even the L2 marker of a personal reusable prompt library. |
| Testing | **L2** | The only axis off the floor, and the one carrying the inherited codebase's weight. **Above L1**: the L1 markers ("ad-hoc unit tests; coverage is uneven") are plainly false — 78 `@Test` methods across 20 classes systematically cover 30 main sources, with Testcontainers integration suites (`MySqlIntegrationTests`, `PostgresIntegrationTests`), a concurrency suite, and merge-gating CI on every push and PR. **Below L3** for two independent reasons. First, both the L2 and L3 markers carry an agent clause — "unit tests for everything **agents produce**", "**agent-generated code** includes tests before merge" — and that clause is entirely unevidenced; there is no agent-produced code here. Second, even reading the markers purely as infrastructure, the L2 rigour pair is split: disciplined review is present (PR flow, DCO sign-off, CI gate) but mutation testing is absent, and coverage is *measured* and never *enforced* (JaCoCo `prepare-agent` + `report` at `pom.xml:277-293`, no `check` goal, no threshold rule). The functional coverage of critical workflows genuinely has an L3 *shape*; the placement is held at L2 because the verification-of-the-verification is missing and the agent clause fails outright. |
| Observability | **L1** | No agent-activity logging of any kind, no token/latency/cost capture, no dashboards, no observability snapshots, no cadence, no per-PR acceptance tracking, no mutation kill rates, no AI-acceptance rates, no perception-reality calibration, no OTel. Matches both L1 markers. **Explicitly excluded as evidence:** Spring Boot Actuator (`pom.xml:44`) with `management.endpoints.web.exposure.include=*` is *application* runtime observability, not agent-activity observability — counting it would inflate this axis by two levels on evidence unrelated to AI collaboration. Likewise the CycloneDX SBOM (`pom.xml:308-312`) is supply-chain metadata, not agent telemetry. |
| Governance | **L1** | Zero written AI-use policy of any kind; governance over AI use is necessarily implicit and trust-based, with no agreed norms recorded anywhere. Matches both L1 markers. Zero HARNESS.md constraints, therefore no enforcement ratio to compute; no promotion ladder; no audit cadence; no institutional-frame modelling. **See the Governance Dimension below for the deep-dive.** Noted and *not* counted: real machine-enforced governance of *contribution mechanics* exists — DCO sign-off (`.github/dco.yml`), formatter validation, nohttp checkstyle, an enforced Java floor — but this governs how humans contribute code, not how AI is used, and the axis measures the latter. |

**Operational axes mean: L1.3** (1 + 2 + 1 + 1 = 5; 5 ÷ 4 = 1.25, reported to one decimal as 1.3; the gap below is computed from the unrounded 1.25).

---

## Habitat Build Gap

```text
Level placement (from cognitive assessment): L1
Operational axes mean (Part D):              L1.25
  Composition:    L1
  Testing:        L2
  Observability:  L1
  Governance:     L1
Habitat Build Gap:                           -0.25
Interpretation:                              Coherent
```

The baseline is **coherent**, and that result is more interesting than it first appears — it is arguably the most useful thing this assessment produces.

`abs(-0.25) < 0.5`, so the team's cognitive level and the habitat's operational delivery sit at the same place. Nothing here is lying to anyone. There is no habitat promising a sophistication the practice cannot sustain, and no practice straining against an environment that cannot support it. The framework's own guidance is that coherence, not level size, is the headline signal — and by that measure this baseline is *healthy*. It is simply healthy at the floor.

The −0.25 tilt is entirely attributable to the Testing axis, the single point where the inherited Spring PetClinic engineering discipline lifts the habitat marginally above the collaboration practice that uses it. That slight negative is the numerical fingerprint of exactly the phenomenon the brief asked to be surfaced: **the codebase's own engineering discipline is carrying the habitat, not the other way round.** It is a hair's breadth from the *inherited habitat* regime (which begins at −0.5), and the reading is directionally the same — there is more verification capability sitting in this repository than the collaboration practice currently knows how to draw on.

But the −0.25 magnitude means the standard "inherited habitat" prescription (literacy uplift before further harness extension) does **not** apply cleanly, and it would be wrong to recommend it. That prescription assumes a mature habitat outpacing a lagging team. Here both are near the floor; there is no accumulated harness to grow into. The honest reading is that this baseline needs *both* — literacy and habitat — and the correct sequencing is the one the Recommendations set out: **declare the harness that already exists before building any new one.** Writing down what `./mvnw verify` already enforces costs one file, converts inherited capability into deliberate constraint, and simultaneously raises Context Engineering off the floor and makes the Governance axis measurable for the first time. Building new enforcement before declaring the existing enforcement would add cost while leaving the actual gap — invisibility — untouched.

---

## Governance Dimension

The Governance Dimension is the governance deep-dive; its operational placement is summarised as the **Governance axis (L1)** in the Operational Axes table above, and the two are consistent by construction.

### 1. Governance constraint count

**0 governance-related constraints.** There is no HARNESS.md to count them in — Phase 1a checked ten conventional and alternative paths plus a whole-tree markdown sweep and found no candidate file. With zero constraints there is no enforcement ratio to report (the `unverified : agent-backed : deterministic` split is undefined on an empty set), no last-audit date, and no drift to measure.

### 2. Governance ALCI items

The eight governance ALCI items (Levels 0, 2, 3, 5) are **not carried verbatim** in any reference available to this agent at runtime — `operational-axes.md`, `habitat-discovery.md`, `sophistication-markers.md`, and `tool-config-evidence.md` do not contain them, and no governance item bank exists in the plugin's skills or commands. Rather than invent the eight statements and score against fabricated text, the bands are scored from observable evidence against the Governance axis markers in `operational-axes.md`, which are the authoritative governance statements available:

| Band | Observable placement | Evidence |
| --- | --- | --- |
| **Level 0** — governance ambiguity unrecognised | **Consistent with the evidence** | Nothing in the repository acknowledges that AI use raises any governance question. No policy, no discussion artefact, no norm. Whether the ambiguity has been *recognised* by the people involved is unobservable — recognition without a written trace is exactly the L0/L1 indeterminacy noted in the level rationale. |
| **Level 2** — conventional norms, informal agreement | **Not evidenced** | No retro notes, no team-agreement documents, no CONTRIBUTING-adjacent AI guidance, no norms codified anywhere. `CONTRIBUTING`-style guidance in the README covers issue reporting and PR flow only, with no AI content. |
| **Level 3** — written constitution, enforced | **Not evidenced — decisively** | Requires CLAUDE.md or HARNESS.md constraining agent operation. Both absent across a completed 14-path search, with no parallel-tool equivalent on any of the 14 tool surfaces checked. No categorisation, no promotion ladder. |
| **Level 5** — continuous certification | **Not evidenced** | No per-change compliance evidence, no verifiable controls over AI use, no institutional reference frame modelled alongside human and AI frames. |

The Level 4 band (policy-as-code, machine-enforced constraints in CI with explicit blocking rules) is where the sharpest distinction in this whole assessment lives, so it is called out separately rather than folded into the table. **Policy-as-code genuinely exists in this repository** — `spring-javaformat validate`, `nohttp` checkstyle `check`, `maven-enforcer` `requireJavaVersion`, and DCO sign-off are all machine-enforced, all blocking, and all running on every PR. Every one of them governs *how humans contribute code*. **None** governs *how AI is used*. The Governance axis measures the latter, so the placement stays at L1 despite mature policy-as-code machinery sitting right there in `pom.xml`. The machinery is the right shape for AI governance and is pointed at a different target.

### 3. Governance readiness summary

This baseline sits at the **very start** of the governance maturity progression: **Level 0–1, indeterminate**, for the same structural reason the overall level is indeterminate — implicit, trust-based governance leaves no artefact, so its presence cannot be distinguished from its absence by scanning.

What the evidence does establish firmly is the **ceiling**: governance here cannot be at Level 2 or above, because every level from 2 upward requires *some* recorded artefact (an informal norm written somewhere, a constitution, a CI policy, a certification trail) and no such artefact exists at any of the nineteen habitat paths or fourteen tool surfaces checked.

The readiness signal is unusually favourable despite the floor placement. Three of the four things a Level-3 governance posture needs are already present: deterministic enforcement in CI, a merge gate that blocks on failure, and a contributor community accustomed to being bound by it. The missing fourth is the *declaration* — a written record of what is enforced, how, and by which tool. Governance maturity here is blocked on writing things down, not on building things.

### 4. Recommendations

The applicable rung is **Level 0 → 1**, with **Level 1 → 2** immediately reachable:

- **Level 0 → 1 — recognise governance ambiguity.** No tooling needed. The action is acknowledgement: AI is being used against this codebase, and no written norm covers what it may touch, what must be reviewed, or what must never be delegated. Naming that gap is the entire step.
- **Level 1 → 2 — start testing governance claims.** Run `/governance-constrain` to author the first falsifiable governance constraint. The highest-value first constraint is one that *describes enforcement that already exists*, because it can be verified immediately and cannot drift on day one — for example, a constraint stating that all AI-authored changes must pass `./mvnw -B verify`, with `**Enforcement**: deterministic`, `**Tool**: GitHub Actions (maven-build.yml)`, `**Scope**: all PRs to main`. This converts an inherited build step into a declared, auditable governance constraint at the cost of a few lines, and it is falsifiable from the first minute.
- **Level 2 → 3 — encode governance into the habitat.** Once the first constraint exists, run `/governance-constrain` per governance requirement to build out the constraint set, targeting the framework's three-constraint L3 minimum. The two obvious next candidates are already-enforced and therefore free: formatter validation and the nohttp check.
- **Level 3 → 4 — governance as specification.** Not yet applicable; revisit once a declared constraint set exists.
- **Level 4 → 5 — governance as platform.** Not applicable. Establish a `/governance-audit` quarterly cadence only once there is a constraint set for an audit to examine — auditing an empty set produces no signal.

---

## Strengths

Stated honestly: at Level 1 with no habitat, the strengths are the *preconditions* for AI collaboration rather than the collaboration itself. They are real and they are unusually good.

- **An exceptionally strong latent harness, already merge-gating.** `./mvnw -B verify` runs 78 tests, two Testcontainers database integration suites, a concurrency suite, formatter validation, and a nohttp check — and **fails the build** on any of them, on every push and PR to `main` (`.github/workflows/maven-build.yml`). Most teams arriving at Level 3 have to build this. Here it already exists and needs only to be pointed at an agent.
- **A creative, property-style guardrail that most codebases lack.** `I18nPropertiesSyncTest` enforces a *structural* property across the whole source tree — no hard-coded user-facing strings, no partial translations — rather than checking individual behaviours. This is the same shape as a well-designed harness constraint, and it demonstrates that someone in this codebase's history already thinks in constraints rather than only in test cases.
- **Deterministic enforcement rather than advisory linting.** `spring-javaformat` runs at the `validate` phase and `checkstyle` uses the `check` goal — both block. Advisory-only tooling is the common failure mode and this project avoids it. When an agent arrives, it gets hard feedback rather than warnings it can ignore.
- **Commit hygiene consistent with reviewable, human-paced change.** The last 15 commits average well under 100 lines; single-purpose test additions appear as their own commits. This is the Human Pace signal visible in history, and it means the review culture needed to verify AI output is already in place.
- **Genuine coherence.** Cognitive level and operational habitat sit within 0.25 of each other. There is no self-deception in this baseline — no aspirational harness document describing enforcement that does not exist, no constraint claiming to be deterministic while being unverified. It is honest about being empty, which is a better starting position than an incoherent habitat with drift to unwind first.

---

## Gaps

- **Total absence of context engineering.** Zero habitat documents across nineteen paths; zero parallel-tool configs across fourteen surfaces; zero AI references in 132 files, 181 README lines, and 1042 commit messages. Any agent entering this repository starts from nothing, rediscovers the build system by trial and error, and learns the formatter exists by failing CI. This is the L3 ceiling and the binding constraint on everything else.
- **No compound learning whatsoever.** No `REFLECTION_LOG.md`, no `reflections/`, no `reflections/archive/`, no AGENTS.md. The combined reflection-log read specified by the read policy returned empty. Every lesson from every AI session — every gotcha, every wrong turn, every discovered convention — is lost at session end. Session N+1 repeats session N's mistakes indefinitely. This is the defining pathology of Level 1 and the one with compounding cost.
- **Zero declared constraints against an L3 minimum of three.** Real enforcement exists but is undeclared, so it cannot be audited, promoted through the `unverified → agent → deterministic` ladder, deliberately relaxed, or reasoned about. An enforcement mechanism nobody has written down is a trap rather than a guardrail.
- **No verification of the verification, and no coverage gate.** JaCoCo is configured to `prepare-agent` and `report` but never `check` (`pom.xml:277-293`) — coverage is measured and never enforced, so it can silently regress. No mutation testing means the 78 tests' ability to actually *catch* defects is unmeasured. This matters disproportionately once an agent starts writing code: an agent that writes code and tests together can produce a suite that passes comprehensively while asserting almost nothing, and only mutation testing detects that.
- **No agent-activity observability at all.** No token spend, latency, cost, acceptance-rate, or calibration data. There is no way to answer "what is AI collaboration costing us, and is it working?" — which means there is no feedback loop from outcomes back into practice, and no basis for the cost awareness the framework expects from Level 2 upward.
- **No security or dependency automation.** No vulnerability scanning, no Dependabot, no Renovate. The CycloneDX SBOM (`pom.xml:308-312`) produces an artefact that nothing consumes or scans.

---

## Recommendations

Ordered by impact. The sequencing principle throughout: **declare what already exists before building anything new.** This baseline's problem is invisibility, not missing capability — and the first three recommendations cost roughly one file each while converting substantial inherited capability into deliberate, auditable practice.

1. **Write a `CLAUDE.md` at the repository root.** Highest impact by a wide margin, lowest cost, and it lifts the binding constraint. It should state the build and test commands (`./mvnw -B verify`), warn that `spring-javaformat` runs at the `validate` phase and will fail the build if code is reformatted by hand, note that `nohttp` checkstyle blocks plain-HTTP URLs, and point at `I18nPropertiesSyncTest` so an agent knows that adding a user-facing string requires updating every locale bundle. This single file converts the entire latent harness into an active one, moves Context Engineering off the floor, and is the precondition for every other recommendation. *Addresses:* the weakest discipline and the L3 ceiling.

2. **Author the first falsifiable harness constraint with `/governance-constrain`.** Start with enforcement that already exists so it is verifiable on day one and cannot drift — `**Rule**: all changes pass ./mvnw -B verify`, `**Enforcement**: deterministic`, `**Tool**: GitHub Actions (maven-build.yml)`, `**Scope**: all PRs to main`. Then add the formatter and nohttp checks as constraints two and three, reaching the L3 minimum of three constraints without writing a single line of new enforcement. This is the Level 1 → 2 governance step and it is nearly free. *Addresses:* the Architectural Constraints discipline and the Governance axis, simultaneously.

3. **Start a `REFLECTION_LOG.md` and capture one entry per AI session.** Compound learning is the discipline whose absence costs most over time, because the loss is cumulative and invisible — nobody notices the fifth rediscovery of the same gotcha. One entry per session, noting what surprised you and what you would tell the next session, is enough to start. Promote recurring entries into `AGENTS.md` once patterns emerge. *Addresses:* the compound-learning L3 discipline, entirely unevidenced today.

4. **Add a JaCoCo coverage `check` goal and introduce mutation testing (PIT).** The coverage plugin is already configured and merely reports; adding a `check` execution with a threshold at the current measured level converts a passive report into a ratchet at near-zero cost. PIT matters more than it looks: once an agent is writing code and tests in the same breath, mutation score is the only signal that distinguishes a suite that genuinely constrains behaviour from one that passes comprehensively while asserting nothing. Both raise the Testing axis and are the concrete route from L2 to L3 on that axis. *Addresses:* the Guardrail Design discipline and the Testing axis.

5. **Capture a first cost and activity baseline.** Record token spend and session count for AI work against this repository, even manually in a text file to begin with. The Observability axis is at the floor with no measurement of any kind, and cost awareness is a Level 2 expectation. A crude manual baseline beats a sophisticated instrument that does not exist, and it establishes the cadence habit that later tooling plugs into. *Addresses:* the Observability axis and the framework's cost-awareness expectation.

---

## Reflection on the assessment itself

Three things this scan revealed that are worth carrying forward:

**Level 1 is unfalsifiable from artefacts, and the assessment instrument should say so plainly.** The assessor can prove the absence of Levels 2–5 with a complete, citable audit trail, but cannot prove the presence of Level 1 — prompting leaves no trace. Any assessment of an L0–L1 repository rests on a declared premise rather than on evidence, and pretending otherwise would be the exact error this framework's auditability discipline exists to prevent.

**Worktree configuration inheritance is a live false-positive risk for hook detection.** `git config --get core.hooksPath` returned `.githooks` inside a checkout containing no hooks, because the value was inherited from the parent repository's shared config through the worktree link. An assessor trusting that output would have recorded hook evidence in a repository with none — and on this particular baseline, that single false positive would have been enough to manufacture habitat where the entire point is that there is none. The `--show-origin` check plus a `git ls-tree` confirmation should be standard whenever `.git` is a file rather than a directory.

**The sharpest distinction in this assessment was engineering quality versus AI-collaboration practice, and it recurred on every axis.** A strong test suite is not verification discipline. A blocking formatter is not an architectural constraint. Spring Boot Actuator is not observability. DCO enforcement and policy-as-code linting are not AI governance. In each case the artefact has the right *shape* and the wrong *subject* — and in each case counting it would have inflated the assessment by one to two levels on evidence that has nothing to do with AI collaboration. Future assessments of mature codebases with no habitat should expect this pressure on every single axis and apply the same test each time: **does any AI output pass through this loop?** Here the answer was no, every time.

---

## Next Assessment

Suggested re-assessment date: **2026-12-21** (quarterly).

Previous assessment: first assessment.

Re-assessment note: as a deliberate conference-talk baseline, this checkout is not expected to change — its emptiness is the artefact. The quarterly cadence applies to any working repository that adopts these recommendations, not to the frozen rung itself.

---

## Files and paths referenced

All paths absolute, as required.

**Assessed repository** (read-only; nothing written):
`/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1`

Key evidence files within it:

- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/pom.xml` — enforcer `187-204`, spring-javaformat `206-215`, checkstyle/nohttp `220-248`, JaCoCo `277-293`, CycloneDX `308-312`, Testcontainers `158-180`, Actuator `44`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/build.gradle` — checkstyle `70-87`, Actuator `41`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/.github/workflows/maven-build.yml`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/.github/workflows/gradle-build.yml`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/.github/workflows/deploy-and-test-cluster.yml`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/.github/dco.yml`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/src/test/java/org/springframework/samples/petclinic/system/I18nPropertiesSyncTest.java`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/src/main/resources/application.properties`
- `/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/scratchpad/assess-l1/README.md`

**Methodology sources read** (all under `/Users/russellmiles/code/Habitat-Thinking/ai-literacy-superpowers/ai-literacy-superpowers/skills/ai-literacy-assessment/`): `SKILL.md`, `references/habitat-discovery.md`, `references/assessment-template.md`, `references/operational-axes.md`, `references/tool-config-evidence.md`, `references/sophistication-markers.md`.