# demo-notes

Orphan branch with no shared history with the ladder. It holds the scaffolding for the
talk "(Agentic) Loops At Levels, Live": run sheets, timings, prompts, fallback
instructions and the recorded toolchain.

It lives off the ladder on purpose. Anything committed to a level branch shows up in
the level-to-level diffs the audience sees.

- [TOOLCHAIN.md](TOOLCHAIN.md): the verified JDK, Maven and OS, plus build timings.
- [LADDER.md](LADDER.md): branch ladder, parentage rules and the `-after` sibling convention.

Decks and beats:

- `opening/slides/OPENING.md` — 5 slides, 3 min. The hinge from the *Does AI deliver
  waterfall?* keynote into the full talk. `./bin/demo run opening`.
- `level-<n>/slides/{BEFORE,AFTER}-L<n>.md` — the five levels, with
  `level-<n>/RUN-SHEET.md` beside each.
- `upper-levels/` — the standalone 20-minute demo of Levels 4 and 5 only, with its own
  deck, run sheet and preflight. `./bin/demo run upper`.
- `sentinels/` — the standalone 20-minute **Level 5 solo** session, built on one claim:
  a habitat has two jobs, one for the **agents** (harness, pipeline, gates) and one for
  the **human** (the sentinels, protecting decision sovereignty and the capability to
  keep exercising it). Act 1 explains the first; act 2 samples the second. **Nothing
  dispatches an agent** — all five sentinel beats play committed, unedited output from
  real runs in `sentinels/captures/`, because all four live is 8m41s of dispatch in a
  20-minute slot. `./bin/demo run sentinels`, no profile needed.
- `./bin/demo run <segment> --time` times any of them and prints a variance table
  splitting slide time from terminal time.

Work on this branch in its own worktree so the ladder checkout is never disturbed:

```sh
git worktree add ../petclinic-loops-at-levels-demo-notes demo-notes
```
