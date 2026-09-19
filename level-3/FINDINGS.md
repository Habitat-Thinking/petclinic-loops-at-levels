# Level 3 findings

## What was built

`HARNESS.md` governs the loop. `AGENTS.md` governs the turn. Three directives moved out
of `AGENTS.md` because each named a loop artefact rather than describing how to make a
change — the table of moves is at the top of `AGENTS.md` and is demo material.

| Constraint | Enforcement | Tool |
|---|---|---|
| Consistent formatting | deterministic | `spring-javaformat:validate` (already in the build) |
| Tests must pass | deterministic | `./mvnw verify` |
| Decision record — commit | deterministic | `scripts/check-decision-record.sh --staged`, warns |
| Decision record — pull request | deterministic | same script, blocks |
| Schema parity across databases | deterministic | `SchemaParityTest`, plain JUnit |
| Abstraction earns its place | **agent — advisory** | reviewer judgement, no tool |

Five enforced, one honestly advisory. No new dependencies.

## The promotion: Level 2's drift becomes a rule

At Level 2, directive 12 said judgement calls go in "a Decisions section of the PR
description". The agent complied, wrote six numbered decisions into a chat message, and
they evaporated. There was no PR. Nothing checked where they landed.

The constraint names a destination that exists whether or not anyone is looking:
`decisions/<date>-<slug>.md`, in the repository, in the same change.

**The approval moment** is marked in
[harness-constrain-exchange.md](harness-constrain-exchange.md). The agent proposed and
wrote nothing until the maintainer said "Yes, accept and write it into HARNESS.md". The
reflection proposes; the human curates; only then is the rule durable.

The check is deliberately shallow: it asks whether a record is present, never whether it
is any good. Judging content is a person's job, and pretending otherwise would make the
rule feel stronger than it is.

## Did enforcement change the output, or only catch violations?

Both — and the first is the more interesting.

The frozen task was re-run under Level 3 conditions: **143 s, 15 turns, $1.25, 81 tests
green** (78 application tests plus 3 parity cases). Against the Level 2 run:

1. **It wrote `decisions/2026-09-17-owner-email.md` unprompted.** The same reasoning that
   evaporated at Level 2 is now a file, because a rule required it.
2. **It closed the Level 2 secondary drift on its own.** It added `@Size(255)` to match
   the column, and the record explains why: without it a long value reaches the database
   and fails there with a 500 rather than a form error. Nobody asked for this. The
   constraint's existence changed how carefully it worked.
3. All three schemas moved together, with the parity test proving it rather than a
   directive asking for it.

| | Level 1 | Level 2 | Level 3 |
|---|---|---|---|
| Time | 118 s | 120 s | 143 s |
| Cost | $0.76 | $1.06 | $1.25 |
| Tests | 78 | 78 | 81 |
| Decisions durable? | no | no | **yes** |

Cost has risen about 64% from Level 1. Each level buys something and none of it is free.

## The catch, both loops, same violation

Captured verbatim in [catch-verbatim.txt](catch-verbatim.txt),
[catch-strict.txt](catch-strict.txt) and [ci-failure.txt](ci-failure.txt).

An agent was asked to add the email column to the H2 schema only, with no decision
record — the Level 2 drift, reproduced deliberately.

- **Edit/commit time, advisory**: the hook warns, twice, and the commit still succeeds.
- **Merge time, strict**: [PR #1](https://github.com/Habitat-Thinking/petclinic-loops-at-levels/pull/1)
  is red, in real CI, permanently. `decision-record: VIOLATED`, exit 1.
- **The tool with no agent near it**: `./mvnw test -Dtest=SchemaParityTest` fails with
  `owners.email exists in h2 but not in mysql`.

## Three loops, each verified to fire

| Loop | Mechanism | Verified |
|---|---|---|
| Edit/commit, advisory | `.githooks/pre-commit` | yes — warns, commit succeeds |
| Merge, strict | `.github/workflows/harness.yml` | yes — PR #1 red in real CI |
| Scheduled, investigative | same workflow, Monday 07:00 UTC | yes — dispatched, ran green |

### One that did not fire, reported rather than hidden

`.claude/settings.json` declares a `PostToolUse` hook running
`scripts/harness-advisory.sh` after every write. **It did not execute in headless
sessions.** The debug log shows 17 hooks registered from the plugin and none from
project settings. The script itself is fine — run by hand it produces exactly the
advisory text captured above.

The likely cause is that hooks defined in project settings need per-clone approval,
which a `-p` session never grants. It is left in place, and this is stated plainly here,
because the alternative is a file that claims an enforcement nobody verified — the
Level 2 pointer failure wearing different clothes.

**Before rehearsing, check it interactively**: launch, edit a file under `src/`, and see
whether the advisory appears. If it does not, the commit-time hook is the edit-time loop
and the beat should say so.

**Correction, 2026-09-17 (plugin 0.92.0, Claude Code 2.1.274).** The hook does fire in
a headless session. The debug log for an edit to `db/h2/schema.sql` shows
`Slow PostToolUse hooks: 4166ms for Edit (2 hooks)`: the plugin's
`commit-constraint-check.sh`, which was silent, and this script, which returned
`HARNESS advisory: src/ has changed with no record under decisions/.` The agent saw
none of it. A `PostToolUse` hook's plain stdout on exit 0 goes only to the debug log,
so the loop runs and nobody hears it. The earlier conclusion ("did not execute") came
from that silence.

**Fixed the same day.** `scripts/harness-advisory.sh` now takes `--json` and wraps the
same words in `{"systemMessage": ...}`, which is the form Claude Code shows the person
at the keyboard; `.claude/settings.json` passes the flag. Run by hand with no flag it
prints the identical text, so `catch-verbatim.txt` and the edit-time beat are unchanged.
The debug log now shows the output parsed rather than discarded:
`Hooks: Parsed initial response: {"systemMessage":"HARNESS advisory: src/ has changed
with no record under decisions/..."}`.

**Still to confirm on a screen.** A `systemMessage` is shown to the person, not sent to
the agent, so a headless run cannot prove it renders — in `-p` there is no screen, and
the agent correctly reports seeing nothing. Rehearse the catch beat interactively once
and watch for the advisory line. That is also the run that clears any post-upgrade
first-run screen, so it is one errand, not two.

**Which makes the beat's own point sharper.** Three times now, an enforcement in this
repository has run and said nothing anyone could hear: the `AGENTS.md` pointer that
loaded nothing, the hook believed dead that was only inaudible, and a plugin hook that
reported a missing HARNESS.md that was there all along. None was visible by reading the
files. Each needed the loop run and watched.

## The instruments, on a one-day-old harness

Run and recorded: [health.txt](health.txt), [gc.txt](gc.txt). They were told not to
invent findings. They found four real mismatches anyway:

1. The README badge said "2/2 enforced" against 6 constraints.
2. Schema parity declared `commit` scope and **nothing ran it at commit**.
3. `HARNESS.md` listed one GC rule nothing ran, while CI ran three it never named.
4. The edit-time hook's 20 s timeout was shorter than a cold Maven start — it would have
   failed silently.

All four are now fixed, with a decision record. `/harness-gc` separately found that one
of its own checks passes for the wrong reason.

**This is the honest version of the instrument beat.** Not "your repo is rotting" — it
is one day old. The point is that a harness starts drifting from its own description
immediately, that the drift was invisible to inspection, and that the time to own an
instrument is before month six.

## Ladder cleanliness

`git diff level-2-commanding..level-3-regulating` — harness artefacts and enforcement
wiring only. **Zero application source changes**: `src/main` and `pom.xml` are untouched.

The one file under `src/` is `src/test/.../harness/SchemaParityTest.java`, which *is* the
enforcement. The formatter reformatted it on the way in, which is worth knowing: the
harness reformatted the harness.

## A note for the Level 4 handover

Everything here bounds what *may* happen. Nothing here has an opinion about what
*should* happen. The harness cannot tell you the email field was worth adding, cannot
choose between two designs, and cannot decide what to build next. It says no. That is
the ceiling this level hits, and the reason Level 4 exists.
