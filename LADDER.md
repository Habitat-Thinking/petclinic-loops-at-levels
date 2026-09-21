# Branch ladder conventions

## Fixed points

- `upstream-pristine` (annotated tag): `spring-projects/spring-petclinic` `main` @ `818c4136ea971c21674525f9053de0d9c7ad8cfe`.
  Never modified, never moved.
- `level-0-baseline` (branch): created from `upstream-pristine`. It may carry at most one commit,
  touching only attribution files. At creation it carries **none**; it is identical to the tag.

## The ladder

| Branch                  | Parent                  | Status |
|-------------------------|-------------------------|--------|
| `level-0-baseline`      | `upstream-pristine`     | created |
| `level-1-dictating`     | `level-0-baseline`      | created, 0 commits (content-identical to baseline) |
| `level-2-commanding`    | `level-1-dictating`     | created: AGENTS.md, CLAUDE.md, HARNESS.md (no constraints), copilot pointer, REFLECTION_LOG.md |
| `level-3-regulating`    | `level-2-commanding`    | created: HARNESS.md, 5 deterministic + 1 advisory constraint, 3 loops, decisions/ |
| `level-4-orchestrating` | `level-3-regulating`    | created: pipeline config, one real run's gate artefacts, S2 implemented |
| `level-5-supervising`   | `level-4-orchestrating` | created: cognitive reservoir block, PIPELINE.md enforcement tagging |

Each level branch is created from its predecessor when that level is built. It's the
current tip of the predecessor at that moment, not `level-0-baseline`.

Levels are **additive**. `git diff <level-N>..<level-N+1>` must show exactly the artefacts
that level introduces and nothing else, because that diff is a slide. If a level branch
drifts from its parent by anything else, the evidence is wrong.

## What must stay empty

No AI-collaboration artefact may exist on `upstream-pristine`, `level-0-baseline` or
`level-1-dictating`: no `CLAUDE.md`, `AGENTS.md`, `HARNESS.md`, `MODEL_ROUTING.md`,
`REFLECTION_LOG.md`, `.github/copilot-instructions.md`, `.cursor/rules/` or similar.
That emptiness is the demo. Any instruction that appears to require one there has to be
confirmed with Russ first.

Linters, formatters, CI changes, pre-commit hooks, dependency scanning, application-code
changes and talk READMEs each belong to a specific level. Introducing one earlier
destroys the evidence that its level introduced it.

## Level 4 checkpoint branches

The Level 4 pipeline cannot run in stage time, so the segment enters and leaves it at
gates rather than waiting for stages. Each branch is a commit on
`level-4-orchestrating`, not a sibling of it, and nothing is merged.

| Branch | State it holds |
|--------|----------------|
| `l4-1-sliced` | Five slices, every disposition `pending` |
| `l4-2-spec` | Dispositions written, S2 progressed, spec and plan produced |
| `l4-3-objections` | Eleven objections, all `pending` |
| `l4-4-choices` | Objections adjudicated; choice stories and consultation voices produced |
| `l4-5-reviewed` | Implemented, 94 tests green, code review PASS |

`level-4-orchestrating` itself is the finished state: 96 tests, seven code-mode
objections disposed.

## The Level 5 descent sibling

`level-5-supervising-descent` holds the descent measurement: a twelve-character
typo fix put through the entire pipeline, with its nine artefacts and the
decision record. It is a sibling, not a rung.

The reason is the rule above. The descent is evidence *produced by* Level 5, not
an artefact *of* it, and left on the ladder it made
`git diff level-4-orchestrating..level-5-supervising` show a template typo and
1,457 lines of pipeline records — during a segment about whether a human can
still answer for the code. That diff is a slide.

It was moved by reverting the descent commits on the ladder branch, never by
force-pushing. The full history is on the sibling at `ab236db`, so the
measurement in `level-5/descent.md` is reproducible from it.

`level-5-supervising` therefore carries exactly two changed files, and the demo
shows it: the cognitive reservoir block, and the `PIPELINE.md` enforcement
tagging that the Level 5 `/assess` run asked for.

## Demo-safety siblings

Created so far: `level-1-dictating-after` (Level 1 run 3) and
`level-2-commanding-after` (the Level 2 run) and `level-3-regulating-after`
(the Level 3 run). All unedited.

## The convention

For any level whose live demo produces agent output, a `<branch>-after` sibling
(for example `level-2-commanding-after`) holds that output already committed. If a live
run wanders, abandon it mid-demo and `git switch <branch>-after`.

- `<branch>-after` is created from `<branch>`, and carries only the demo's output.
- The next level is still built from `<branch>`, **not** from `<branch>-after`, unless
  that level's plan explicitly says the demo output is part of the ladder.

## Repository rules

- Never force-push any tag or branch.
- Demo scaffolding (run sheets, timings, prompts, fallbacks, toolchain) lives only on the
  orphan `demo-notes` branch, never on the ladder.
- Remotes: `origin` is `Habitat-Thinking/petclinic-loops-at-levels`. `upstream` is
  `spring-projects/spring-petclinic`, with push disabled locally.
- Upstream is Apache-2.0. `LICENSE.txt` and existing attribution are preserved unchanged.
