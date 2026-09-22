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
- `sentinels/` — the standalone 20-minute **Level 5 solo** session: Levels 3 and 4
  explained rather than run, then **two sentinels dispatched live**. The only segment
  that dispatches an agent for the sake of the sentinel rather than the code, so it is
  the only one needing the profile, a `declared` pact and the reservoir block — all
  checked by `./bin/demo preflight sentinels`. Captured fallbacks in
  `sentinels/captures/`.
- `./bin/demo run <segment> --time` times any of them and prints a variance table
  splitting slide time from terminal time.

Work on this branch in its own worktree so the ladder checkout is never disturbed:

```sh
git worktree add ../petclinic-loops-at-levels-demo-notes demo-notes
```
