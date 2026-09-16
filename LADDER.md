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
| `level-2-commanding`    | `level-1-dictating`     | not yet created |
| `level-3-regulating`    | `level-2-commanding`    | not yet created |
| `level-4-orchestrating` | `level-3-regulating`    | not yet created |
| `level-5-supervising`   | `level-4-orchestrating` | not yet created |

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

## Demo-safety siblings

Created so far: `level-1-dictating-after` (run 3's output, unedited).

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
