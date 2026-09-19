# Pipeline — the stage sequence and its gates

`AGENTS.md` governs the turn: what to do while making a change.
`HARNESS.md` governs the loop: what must be true before a change is allowed in.
This file governs the sequence: which agent runs when, and **where the work stops and
waits for a person**.

A gate is not a review step. It is a point where an agent has produced something it is
forbidden to finish, because finishing it is the human's job.

## Stages

| # | Stage | Produces | Gate |
|---|-------|----------|------|
| 0 | carpaccio | `docs/superpowers/slices/<task>.md` | **Slice adjudication** — hard |
| 1 | spec-writer | `docs/superpowers/specs/<slug>.md` | — |
| 1a | advocatus-diaboli | `docs/superpowers/objections/<slug>.md` | **Objection adjudication** — hard |
| 1b | choice-cartographer | `docs/superpowers/stories/<slug>.md` | Choice-story surface — soft |
| 1b′ | convener | `docs/superpowers/consultations/<slug>.md` | Consultation surface — soft |
| — | — | — | **Plan approval** — hard |
| 2 | tdd-agent | failing tests | — |
| 3 | implementers | passing tests | — |
| 4 | code-reviewer | findings or PASS | max 3 cycles, then escalate |
| 4a | advocatus-diaboli (code mode) | `docs/superpowers/objections/<slug>-code.md` | **Integration approval** — hard |
| 5 | integration-agent | CHANGELOG, commit, PR, merge | CI must be green |

## The one rule the gates rest on

**No agent writes a disposition or an adjudication. Not as a draft, not as a
placeholder, not to save time.**

The slicing record arrives with every slice `pending`. The objection record arrives with
every objection `pending`. An agent that fills those in has not saved the human any
work; it has removed the only step in the pipeline that required a human at all, and
left an artefact that looks identical either way.

A hard gate refuses to advance while anything is `pending`. A soft gate advances but
reports the count, and a merge-time constraint in `HARNESS.md` is what eventually
forces the issue.

## What the pipeline does not change

Every constraint in `HARNESS.md` applies to a pipeline change exactly as it applies to
a hand-typed one:

- **Decision record for source changes** — the pipeline writes far more than a decision
  record, but the record is still required, and the commit hook and the PR gate still
  check for it.
- **Schema parity**, **formatting**, **tests must pass** — unchanged, and enforced by
  the same scripts.
- **Integration approval** is a gate in front of the merge, not a replacement for it.
  The integration agent watches CI and does not merge on red.

The gates are new. The constraints underneath them are not, and the pipeline has no
authority to waive one. If a stage ever appears to bypass a constraint, that is a
defect in this file, not a feature of the pipeline.

## What this does not do

The pipeline decides nothing. It sequences work and it stops to ask. It has no opinion
about whether the feature was worth building, which of two designs is better, or what
should be built next — it produces the decisions and hands every one of them to a
person.

Who notices when *too many* decisions arrive for one person to give each of them real
attention is not a question this level answers.
