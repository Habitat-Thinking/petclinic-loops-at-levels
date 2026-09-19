# Model Routing

Which model tier each pipeline agent is dispatched to, and why. The rule is the
cheapest tier that can do the work reliably — not the cheapest tier.

`AGENTS.md` governs the turn. `HARNESS.md` governs the loop. This file governs the
dispatch: it is read by the orchestrator before it sends work to anyone.

## Tiers

| Tier | Model | When |
| ---- | ----- | ---- |
| Flagship | `claude-opus-5` | Judgement that a person would otherwise have to make |
| Balanced | `claude-sonnet-5` | Structured work against a stated target |
| Efficient | `claude-haiku-4-5` | Mechanical work with a fixed shape |

## Agent routing table

| Agent | Tier | Rationale |
| ----- | ---- | --------- |
| orchestrator | Flagship | Sequences the pipeline and decides when a gate has been satisfied. A cheap coordinator that mis-reads a gate is the one failure this level cannot absorb. |
| carpaccio | Flagship | Slicing a feature into end-to-end pieces is the judgement the slice gate then asks a human to check. Weak slices make the gate a formality. |
| spec-writer | Flagship | Acceptance scenarios drive the tests and the implementation. Vagueness here is paid for three stages later. |
| advocatus-diaboli | Flagship | Objections have to be grounded in quoted spec text and worth a person's time. A weak objection is worse than none: it teaches the reader to skim the record. |
| choice-cartographer | Flagship | Finding the decisions a spec made *without announcing them* is the hardest reading task in the pipeline. |
| convener | Flagship | Naming the roles a change affects, and the one question worth asking each, is judgement about people rather than code. |
| tdd-agent | Balanced | Turning stated scenarios into failing tests is structured translation with a clear target. |
| implementer | Balanced | Making a named failing test pass, inside this codebase's directives, is bounded work. |
| code-reviewer | Balanced | The CUPID and literate-programming lenses are a checklist applied systematically. |
| integration-agent | Efficient | CHANGELOG, commit message, PR body, watch CI, merge. Templated throughout. |

## Where the money goes, and why that is the right shape

Every gate-bearing agent is Flagship, and every agent whose output a person will
personally adjudicate is Flagship. The cheap tiers sit where the work is bounded by
something already decided — a written scenario, a failing test, a review checklist.

That is the opposite of the intuitive saving. The instinct is to spend on the code,
because the code is the deliverable. The pipeline's expensive stages are the ones
that produce *decisions for a human to make*, because a bad decision surfaced cheaply
still costs a person their attention, and attention is the scarcest thing this
project spends.

## Token budget guidance

| Task type | Suggested max | Notes |
| --------- | ------------- | ----- |
| Slicing record | 6 000 | A dozen slices with scope and rationale, no more |
| Spec | 8 000 | A user story, three to five scenarios, and a plan |
| Objection record | 6 000 | Six objection categories, each with quoted evidence |
| Choice story | 6 000 | One Henney-style story per material choice |
| Test generation | 4 000 | Failing tests are small; the limit prevents over-engineering |
| Implementation (per file) | 6 000 | If one file needs more, it is probably doing too much |
| Code review | 4 000 | A long review is a smell |

## Review

Revisit when a routing decision produces poor results, when an agent is added, or
when the model line-up changes. Record the reason in `REFLECTION_LOG.md`; a routing
change made silently is a cost change nobody can audit.
