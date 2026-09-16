# Level 1: three runs, and what they show

## Conditions

Identical across all three runs, and pinned for every level:

- Prompt, verbatim: `Add an email address to owners. It should be shown on the owner
  details page and be editable when creating or editing an owner.`
- `claude-opus-5`, `--effort high`, `--permission-mode auto`, clean room (see
  [CLEAN-ROOM.md](CLEAN-ROOM.md)).
- Fresh clone of `level-0-baseline` per run. Docker running, so the MySQL and Postgres
  integration tests actually execute.
- Verification (`./mvnw -B verify`) run by me afterwards, not by the agent.

## Results

| Run | Agent time | Turns | Cost | Tests | Build | Files | Email required? | Error-message key | JMeter plan |
|-----|-----------|-------|------|-------|-------|-------|-----------------|-------------------|-------------|
| 1 | 294 s | 31 | $1.85 | 77 | SUCCESS | 23 | **Required** (`@NotBlank`) | yes | updated |
| 2 | 91 s  | 8  | $0.61 | 78 | SUCCESS | 22 | Optional | no | no |
| 3 | 118 s | 10 | $0.76 | 78 | SUCCESS | 21 | Optional | yes | no |

**21 files were touched by all three runs**: `Owner.java`, all three schemas, all three
seed-data files, all 10 message bundles, both owner templates, `OwnerControllerTests`,
`ClinicServiceTests`. Only `fragments/inputField.html` (runs 1 and 2) and the JMeter plan
(run 1) varied in the file set.

## The uncomfortable finding

**Level 1 output here is good, and it is consistently good.** Every run:

- updated all three database schemas, not just the H2 one the unit tests use
- rewrote the positional `INSERT` statements in all three seed files
- wrote **real translations** into all 10 locale bundles — German `E-Mail`, Japanese
  `メールアドレス`, Russian `Электронная почта`, Hindi correctly `\u`-escaped to match that
  file's existing convention
- added tests, and left the build green with the database integration tests running

The failure the talk might have expected — unlocalised bundles, a forgotten schema — did
not happen once. Manufacturing one by narrowing the prompt would be rigging the cold open.

### Why the output is this good: the repo already has a harness

`I18nPropertiesSyncTest` fails the build if any locale bundle is missing a key, or if a
template contains hardcoded text. It is a deterministic constraint that was already in the
repository at `level-0-baseline`.

So the agent did not need a maintainer's knowledge about localisation. **The repository
told it, through a failing test.** This is worth saying out loud on stage, because it is
the argument for Level 3 arriving early and from an unexpected direction: the context that
survives is the context that is *enforced*, and PetClinic already had some.

### What actually varies: the decisions nobody wrote down

The three runs disagree on the one question the prompt didn't answer — **must an owner
have an email?**

- Run 1 made it **required**, reasoning that every other `Owner` field is `@NotBlank`.
- Runs 2 and 3 made it **optional**, reasoning that existing owners have no email and
  requiring one would block editing them.

Both are defensible. Each run re-derived the question from scratch, decided alone, and
reported the decision at the end for a human to ratify. Nothing carries to the next run.
That is Dictating: the reasoning happened, and evaporated.

Secondary divergences, same shape: an `email.invalid` message key (runs 1 and 3, not 2);
an `ALTER TABLE` note for existing databases (runs 2 and 3, not 1); the JMeter load-test
plan (run 1 only); a dedicated `email` input type in the shared fragment (runs 1 and 2).

### And the cost of that re-derivation is not stable

Run 1 took **3.2× the wall-clock time and 3.0× the cost** of run 2 for an equivalent
result — 31 turns against 8. Same prompt, same model, same starting commit. On stage this
matters twice: the live run's duration is unpredictable (91 s to 294 s observed), and the
economic argument for habitat is not only about quality.

## Judgement: is the task fair?

**Partly.** It is a fair, realistic task and it is not rigged. But it does not demonstrate
the gap the cold open was designed around.

- It **does not** show Level 1 producing bad or incomplete work. It doesn't.
- It **does** show Level 1 producing *non-deterministic* work: silent decisions on
  unstated questions, resolved differently each time, with 3× cost spread.

If the story is "the model can't do it without context", this task disproves it. If the
story is "without habitat, every run re-litigates what your team already decided, and you
can't predict what you'll get or what it will cost", this task proves it, with a diff.
