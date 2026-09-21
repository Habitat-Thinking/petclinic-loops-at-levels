# Run sheet — the upper levels, standalone (20 minutes)

Pairs with [slides/UPPER-LEVELS.md](slides/UPPER-LEVELS.md), 18 slides, and with
`bin/steps-upper-levels.txt`, 16 beats. Drive it with:

```sh
./bin/demo preflight upper        # --fix switches the branch and enables hooks
./bin/demo run upper              # teleprompts the 16 beats
./bin/demo run upper --time       # the same, timed, variance table at the end
```

**The split.** Each beat's `TIME:` is the whole beat; the optional `CMDTIME:` is the
share of it spent at the terminal, carved out of `TIME:` rather than added to it. The
report shows both against what you actually took:

| | budget | |
|---|---|---|
| slide — talking before the command runs | 11:00 | 7 beats have no command, so this is all of theirs |
| command — the terminal, until you say you are done | 5:30 | across the 7 beats that have one |
| **total** | **16:30** | against a 20-minute slot |

**The beat budgets below are written, not measured.** They total **16 min 30 s**
against a 20-minute slot — not the 19 minutes the section table implies, because the
sections were rounded independently. Rehearse with `--time` and replace them; the
report prints to stdout, so `| tee rehearsal-1.txt` keeps it. In timing mode a beat
with a command takes one extra Enter, so the time spent talking over its output is
charged to that beat rather than the next one.

**This is not the full talk with levels 1–3 removed.** It has a narrower job:
show a room what a governed pipeline and its sentinels actually do, and then
argue that this is not where anyone should be trying to live. It ends on the
same question the full talk does, and the close is the fixed point here too.

## Profile — there isn't one

**No Claude Code session, no login, no `--plugin-dir`, no launcher.** Nothing in
this demo dispatches an agent. Every stage was run in advance and is read from a
checkpoint or a capture; the only live thing is a shell script.

That is a deliberate property, not a compromise: a twenty-minute slot has no room
for a stage that might take four minutes, and the full talk's live `/carpaccio`
rehearsed at **3 min 33 s** of agent time — with a cut point prepared.

## Before you walk on

- [ ] `./bin/demo preflight upper` — **Ready, 0 warnings**
- [ ] Repo on `level-5-supervising`, clean tree. (`--fix` switches it.)
- [ ] `git config core.hooksPath .githooks` set in that clone — `--fix` does it.
- [ ] The deck open, and `level-5/diagnosis.yaml` open in a second window for beat 12.
- [ ] The descent figures on one prepared slide. **Never run the typo live.**

**Why `level-5-supervising` and not `level-4-orchestrating`:** the enforcement
column beat 4 points at only exists on the Level 5 branch, and the descent
artefacts were deliberately moved off it, so `docs/superpowers/` holds exactly
the one feature run the demo talks about. Found by running the segment, not by
reading it.

## The shape

| | Beats | Time |
|---|---|---|
| What is already in the room | 1–3 | 3 min |
| The pipeline and its gates | 4–9 | 7 min |
| What is being supervised changes | 10–12 | 5 min |
| **The close** | **13–16** | **4 min** |

Sixteen beats against eighteen slides: beats 13–16 carry the five close slides
between them, because the close is spoken rather than driven.

## Beats

1. **What is already in the room** (60 s) — six constraints, five deterministic,
   three loops. No history lesson. *Cut 4th.*
2. **Watch it catch something** (90 s) — **the only live thing in the demo.** It
   stages a one-line change under `src/`, runs the decision-record check, and puts
   the tree back. Nothing is committed. Say one sentence and move on; do not
   explain the payoff.
3. **The ceiling Level 3 hits** (30 s) — it bounds what *may* happen and has no
   opinion about what *should*. *Cut 1st.*
4. **A pipeline in front of the work** (90 s) — four hard gates, two soft. Point
   at the enforcement column: six read `human`.
5. **`pending` is the whole gate** (45 s) — from `l4-1-sliced`, read without
   checking it out. *Cut 2nd — say it over the previous slide instead.*
6. **What four gates produced** (90 s) — 62 decisions, 40 disposed, 22 carried.
   ~45 minutes of attention against ~19 hours on the clock. ~$211 and £0.00.
7. **The pipeline bought no exemption** (45 s) — **the payoff for beat 2.** The
   same script refused the governed pipeline's change.
8. **The gates recurse** (60 s) — two accepted objections produced eight more
   decisions; the translation bill went 20 → 40, underivable 10 → 20.
9. **Nothing was watching the trajectory** (45 s) — the bridge. *Cut 3rd.*
10. **What is being supervised changes** (45 s) — from the code to whether I can
    still answer for it.
11. **What a sentinel is** (30 s) — the tool list. No Write, no Edit, no Agent.
12. **An instrument found where I would get lost** (2 min) — the diagnosis, on
    screen, read aloud, not editorialised. Ends on the sentinel admitting it
    cannot tell ceremony from consequence. **Optional 20-second aside, default
    to cutting it:** the instruments can also be quietly wrong — `/mast tune`'s
    writer reads `declared` under bash and `malformed` under zsh, both exit 0.
    Only if you are ahead, and land the category, never the plugin.
13. **The descent** (90 s) — **terminal off from here.** $52.16, 45 minutes, four
    gates, nine agents, 1,457 lines, for twelve characters. Let the room laugh.
14. **Take the laugh away** (45 s) — every agent was told finding nothing was
    fine; none padded; all six findings are ones a reviewer would want.
15. **The turn** (45 s) — not a ladder to the top, a repertoire.
16. **The question, then the repository** (60 s) — Undo, Repair, Migrate, Answer.
    Both tasks mapped. Then the URL, then stop.

## Cut order, when the room runs long

**3, then 5, then 9, then 1.** Never 13–16.

Cutting all four saves about three minutes and costs the Level 3 ceiling, the
`pending` screen, the trajectory bridge and the opening inventory — in that order
of expendability. The argument survives all four cuts; it does not survive losing
the close.

## If the live catch fails

Beat 2 is a shell script and git, so the realistic failures are a dirty tree or
hooks not enabled — both of which preflight checks. If it fails anyway, the
captured `VIOLATED` / `OK` pair is on the slide and looks identical. Say it is a
recording and carry on; beat 7's payoff works either way.

## What not to do

- Do not run an agent. There is no profile configured for this demo and no time.
- Do not run the typo through the pipeline live. Ninety seconds funny, four
  minutes fatal, and the number is already measured.
- Do not end on tooling, the plugin, or a call to adopt anything.
- Do not deliver the turn as an apology.
