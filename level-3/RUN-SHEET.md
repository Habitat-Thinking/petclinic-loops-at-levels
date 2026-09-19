# Run sheet — Level 3: Regulating (8–10 minutes)

The longest of the early segments, with the most moving parts. `./bin/demo run 3` walks
the beats; this sheet is the reference behind it.

## Profile

**Same profile as Level 2** (`~/.claude-loops-at-levels-l2`). Nothing about the launch
changes at this level. What changed is in the repository, which is the point.

## Before you walk on

- [ ] `./bin/demo preflight 3`
- [ ] Repo on `level-3-regulating`, clean tree.
- [ ] **Hooks enabled in this clone**: `git config core.hooksPath .githooks`. Fresh
      clones do not carry it, and the advisory beat silently does nothing without it.
- [ ] **PR #1 open in a browser tab, already red.** Do not wait for CI on stage.
      https://github.com/Habitat-Thinking/petclinic-loops-at-levels/pull/1
- [ ] Docker running (the frozen-task fallback needs it for 81 green tests).
- [ ] `level-3-regulating-after` pushed (it is) as the fallback.

## The merge-gate decision, settled

Two things, in this order, and never a live CI wait:

1. **Run the check locally** — instant, and it is the same script CI runs.
2. **Switch to the PR tab** — real CI, red since before the talk.

The local run is what makes the argument (a tool, no agent). The tab is what makes it
credible (it is not a demo trick).

## Beats

See `bin/steps-level-3.txt` for the full script. In order:

1. **Recall the drift** (15 s) — the Level 2 reflection, still in the log.
2. **Promote it** — `/harness-constrain` live. Point at the pause where it waits for you.
3. **`HARNESS.md` next to `AGENTS.md`** (30 s) — loop versus turn, said once.
4. **Break the rule on purpose** (45 s) — prepared command, advisory warning.
5. **Try to merge it** (60 s) — blocked. *The beat the segment exists for.*
6. **The tool with no agent near it** (30 s) — `SchemaParityTest` fails on its own.
7. **The advisory constraint, honestly** (30 s) — *cut this first if over time.*
8. **The instruments** (45 s, optional) — four real mismatches on a one-day-old harness.
9. **Name the ceiling** (30 s) — it bounds what may happen, not what should. → Level 4.

## If the promotion run wanders

The constraint is already in `HARNESS.md` and the rehearsed exchange is in
`harness-constrain-exchange.md`. Say you have run it before, show the result, move on.

**If it authors a differently-worded constraint:** fine, and say so. The enforcement is
not brittle to the wording — the script checks for a file under `decisions/`, whatever
the rule text says. Rehearsed once with different wording and the catch still worked.

## Fallback

```sh
./bin/demo fallback 3      # or: git switch level-3-regulating-after
```

Unedited Level 3 output: 81 tests green, and the decision record the agent wrote by
itself.

## Known wrinkle, say it if asked

`.claude/settings.json` declares an edit-time hook. Re-tested on 2026-09-17 with plugin
0.92.0, it **does** fire — the earlier "it never runs" was it running inaudibly, since
plain stdout from a `PostToolUse` hook reaches only the debug log. It now emits
`{"systemMessage": ...}`, the channel Claude Code puts on screen. Watch for the advisory
line the first time you rehearse the catch beat interactively; if it appears, the
edit-time loop is demonstrable live and not only from the capture. See FINDINGS.md.

The rehearsed exchange (`harness-constrain-exchange.md`) was recorded on plugin 0.91.0.
Two of its closing remarks no longer apply on 0.92.0, so don't expect them live: "the
hook that fires after writes twice reported that HARNESS.md doesn't exist", and "the
plugin's commit-time hook uses an LLM to review files". That hook is now a script. On
this repository it stays silent, because it only checks shell-script constraints.

## Slide decks for this segment

- [slides/BEFORE-L3.md](slides/BEFORE-L3.md) — before the demo.
- [slides/AFTER-L3.md](slides/AFTER-L3.md) — after it.

Slide plans for Claude Design: takeaway, content, visual and speaker note per slide,
with deck-level design direction at the top.
