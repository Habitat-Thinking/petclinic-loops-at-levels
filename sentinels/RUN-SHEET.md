# Run sheet — Level 5 solo (20 minutes)

Pairs with [slides/LEVEL-5.md](slides/LEVEL-5.md), 19 slides, and with
`bin/steps-sentinels.txt`, 17 beats.

```sh
./bin/demo preflight sentinels --fix    # branch, hooks, profile, plugins, pact, reservoir block
./bin/demo run sentinels                # teleprompts the 17 beats
./bin/demo run sentinels --time         # the same, timed, variance table at the end
```

**The segment is called `sentinels`, not `l5`.** `./bin/demo run 5` is a different segment
and `5` is one keystroke from `l5`; a wrong segment on stage is unrecoverable in a way a
wrong flag is not.

## What this session is

Levels 3 and 4 are **explained, not run.** The time that buys is spent on the only thing
this session demonstrates: **two sentinels dispatched live**, watching the person rather
than the code. It is the only segment in the repository that dispatches an agent for the
sake of the sentinel rather than the artefact.

| | Beats | Slides | Budget |
|---|---|---|---|
| Foundations — explained | 1–4 | 1–5 | 5:15 |
| **The demo — four sentinels, two live** | **5–13** | **6–14** | **11:40** |
| The close | 14–17 | 15–19 | 4:00 |
| | | | **20:55** |

**That is 55 seconds OVER a 20-minute slot. The 20-minute configuration is: cut beat 4.**
It is 1:15 and brings you to 19:40. Stated plainly rather than trimmed out of the close,
because the close is what the session is for.

## Four sentinels, two live — and the split is measured, not chosen

| Sentinel | Measured | Tokens | On stage |
|---|---|---|---|
| `mast` | **46 s** | 13,654 | **live**, beat 7 |
| `reservoir-warden` | **65 s** | 25,948 | **live**, beat 9 |
| `wip-warden` | 121 s | 33,839 | capture, beat 8 |
| `cost-estimator` | **289 s** | 80,072 | capture, beat 12 |

All four live would be **8 min 41 s of dispatch alone** in a 20-minute slot. The two fast
ones run; the two slow ones are captures with their full output committed under
`captures/`. The `cost-estimator` at 4 min 49 s is a quarter of the slot for one dispatch —
there is no version of this session where it runs live.

**Neither capture loses anything that matters.** What makes both worth showing is
*reasoning*, not a live number: one explains why it will not invent a limit, the other why
it will not invent a price.

## Profile — this one needs it

Unlike the upper-levels demo, **this segment needs the Claude Code profile, logged in, with
both plugins.** Two beats dispatch a real agent. Preflight checks all of it, plus two
things nothing else in the repository checks:

- **`~/.claude/pacts.md` Budgets reads `declared`.** If absent, beat 7 reports *"not opted
  in"* instead of reciting a stop hour — a different demo. If `malformed`, that is almost
  certainly [issue 617](https://github.com/Habitat-Thinking/ai-literacy-superpowers/issues/617):
  a pact written from zsh loses its governing clause. Re-run `/mast tune`.
- **`HARNESS.md` carries the Cognitive reservoir block.** Without it the Warden declines to
  read rather than manufacturing one.

## Before you walk on

- [ ] `./bin/demo preflight sentinels` — **Ready**
- [ ] Both captures present and read: `captures/mast.txt`, `captures/reservoir.txt`
- [ ] **`captures/reservoir.txt`'s stage notes read.** That read came back quiet. It will
      probably be quiet on the day. Quiet is the beat.
- [ ] `level-5/diagnosis.yaml` open in a second window at reading size
- [ ] The descent figures on one prepared view. **Never run the typo live.**

## The two live beats

Measured 2026-09-22, in this repository, on this machine:

| Beat | Command | Agent duration | Budget | Fallback |
|---|---|---|---|---|
| 7 | `/mast` | **46 s** | 110 s (75 s terminal) | `captures/mast.txt` |
| 9 | `/reservoir` | **65 s** | 140 s (100 s terminal) | `captures/reservoir.txt` |

Those are agent durations, not beat durations. **The budgets carry the dispatch plus the
time you spend reading the output aloud** — and the difference is the gap you have to fill
while a terminal sits there doing nothing visible.

**What to say while `/mast` runs** (~45 s): a limit you set in advance holds; a limit you
set at the moment you are about to breach it, you will simply move, because the thing you
want at 21:30 is to keep working. Nothing scaffolded this file — the plugin will not write
it for you, because a default someone else chose is not a pact.

**What to say while `/reservoir` runs** (~65 s, the longest wait in the session): this one
watches me, not the code. Four proxies over the git window — session span, decision volume,
context switches, wall-clock hour. It never writes a record of my state to disk. Nobody
else can read it. I am not the thing being measured *for* anyone.

**Neither can write.** Say it before they run, not after: *"Whatever these two are about to
tell me, neither of them can do anything about it. That is the design."*

## The thing to get right about beat 9

**Plan for a quiet read.** You will be mid-conference, the ladder will have had no commits
for hours, and nothing will cross a threshold.

That is the **stronger** version of the beat, not the weaker one. The line is: *"I did not
rehearse this to fire. It did not fire, and watch what it refuses to do with that."* Then
read its two refusals aloud — the honest form of the read is *"no recorded activity"* not
*"a comfortable session"*, and git silence is not evidence of rest. An instrument that
could have flattered you, declining to, is the argument.

Then beat 10 lands the contrast: the same instrument, the night it **did** fire, on two
proxies, arguing against its own finding three times.

**If it does fire, do not celebrate it.** Read the proxies, read its counter-arguments, say
what you are going to do about it. Treating a fired advisory as a win wrecks the beat.

## Cut order

**4 first — you will need it — then 10, then 2, then 13.** That is what four gates
produced, the night it fired, the live harness catch, then ceremony-and-consequence. About
5:15 recovered, and beat 4 alone gets you inside 20 minutes.

**Never beats 14–17**, and **never beats 7 and 9** — they are the reason this session
exists. If the clock is bad enough that the live beats look tempting, cut all four above
and take the bridge in one spoken line.

Beat 10 only cuts cheaply **if the live read fired**, because then the room has already seen
the instrument's shape.

## If a live dispatch fails or wanders

Both are read-only sentinels holding no Write, Edit or Agent, so the failure modes are not
"it broke the repository" — they are: not logged in, plugin missing, or it takes longer
than you have. Preflight catches the first two.

If it wanders or stalls past about ninety seconds, stop it, open the capture, **say it is a
recording**, and carry on. The captures are verbatim and unedited, and the argument is
identical either way. The room forgives a fallback; it does not forgive a fiction.

## What not to do

- **Do not run an agent anywhere except beats 7 and 9.** There is no time and no reason.
- **Do not try to run `/wip` or `/cost-estimate` live** because the capture feels like a
  cheat. They are 121 s and 289 s. The timing is measured and it is not close.
- **Do not run the typo through the pipeline live.** Ninety seconds funny, four minutes
  fatal, and the number is already measured.
- **Do not edit the captures to make them more dramatic.** The reservoir one says nothing
  crossed because nothing crossed.
- **Do not put alarm styling or red on beat 9**, even if it fires.
- **Do not end on tooling, the plugin, or a call to adopt anything.**
