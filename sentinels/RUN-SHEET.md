# Run sheet — Level 5 solo (20 minutes)

Pairs with [slides/LEVEL-5.md](slides/LEVEL-5.md), 18 slides, and with
`bin/steps-sentinels.txt`, 16 beats.

```sh
./bin/demo preflight sentinels --fix    # branch, hooks, artefacts, samples
./bin/demo run sentinels                # teleprompts the 16 beats
./bin/demo run sentinels --time         # the same, timed, variance table at the end
```

**The segment is called `sentinels`, not `l5`.** `./bin/demo run 5` is a different segment
and `5` is one keystroke from `l5`; a wrong segment on stage is unrecoverable in a way a
wrong flag is not.

## The spine

**A habitat has two jobs.** It supports the **agents** — harness, constraints, pipeline,
gates. And it supports the **human** — the sentinels, which protect decision sovereignty
and the capability to keep exercising it.

Act 1 is the first job, **explained**. Act 2 is the second, **sampled**. Every beat serves
one half or the other; a beat that does not obviously do so should be cut rather than kept.

| | Beats | Slides | Budget |
|---|---|---|---|
| The thesis | 1 | 1 | 1:00 |
| **Act 1 — job one, the agents** | 2–5 | 2–5 | 5:00 |
| **Act 2 — job two, the human** | 6–12 | 6–13 | 8:30 |
| The close | 13–16 | 14–18 | 4:00 |
| | | | **18:30** |

1:30 spare in a 20-minute slot, and **no beat needs cutting to fit** — the previous version
of this segment ran two sentinels live and was 55 seconds over.

## Profile — there isn't one

**Nothing in act 2 dispatches an agent.** Every sentinel beat plays a committed, unedited
sample. No Claude Code session, no login, no `--plugin-dir`, no launcher. Preflight says so
in its first line.

That is measured, not timid:

| Sentinel | Measured | Tokens |
|---|---|---|
| `mast` | 46 s | 13,654 |
| `reservoir-warden` | 65 s | 25,948 |
| `wip-warden` | 121 s | 33,839 |
| `cost-estimator` | **289 s** | 80,072 |
| **all four** | **8 min 41 s** | |

Eight and a half minutes of dispatch does not fit in twenty. And nothing is lost by
sampling: **what makes these instruments worth showing is their reasoning**, which a sample
preserves exactly. The live version also spent roughly two minutes with a terminal sitting
there doing nothing visible; the samples give that back.

**One thing is still live:** beat 3, a shell script, two seconds, deterministic. It is
there because the Level 3 argument rests on the harness being real rather than described.

## The shape of every act-2 beat

**Deck names the characteristic. Terminal shows it in the instrument's own words.**

Never the other way round. Output first and explanation after is a tour of a plugin; the
characteristic first turns the same output into evidence for a claim the room is already
holding.

| Beat | Slide | The characteristic |
|---|---|---|
| 7 | 8 | *it holds a line I set, and has no power to set one* |
| 8 | 9 | *given a count and no limit, it refuses to invent one* |
| 9 | 10 | *asked for a number, it would rather refuse than be ungrounded* |
| 10 | 11 | *it will not round silence up into reassurance* |
| 11 | 12 | *it names where my understanding fails* |

The first three are **decision sovereignty**. The last two are **standing able**. Slide 7
puts that map on screen before any of them speak.

## Before you walk on

- [ ] `./bin/demo preflight sentinels` — **Ready, 0 warnings**
- [ ] All five samples read beforehand, especially the stage notes at the foot of
      `captures/reservoir.txt` and `captures/wip.txt`
- [ ] `level-5/diagnosis.yaml` open in a second window at reading size
- [ ] The descent figures on one prepared view. **Never run the typo live.**

## Say it once, at beat 6

*"Everything from here is a sample — real, unedited output from real runs."*

Say it **once**, at the act boundary, and never again. Repeating it at every beat turns a
design decision into an apology.

## Cut order

**5, then 8, then 3, then 12.** Job one at its best, the `wip` refusal, the live harness
catch, then what-none-of-them-can-do.

**Beat 1 never cuts** — it is the thesis, and without it this is a tour of a plugin.
**Beats 13–16 never cut.** At 18:30 you should not need any of these.

## What not to do

- **Do not run a sentinel live.** The timings are measured and they do not fit. If you have
  25 minutes or more and want one, `mast` at 46 s is the only candidate — and then it needs
  the profile, the login, both plugins and a `declared` pact, none of which this preflight
  checks any more.
- **Do not say the cost-estimator predicted $52.16.** It gave no dollar figure at all, and
  the refusal is the point.
- **Do not use the `wip` count of 7 as a punchline.** It is one real session plus six
  foreign files sharing a directory, and it says so itself.
- **Do not run the typo through the pipeline live.** Ninety seconds funny, four minutes
  fatal, and the number is already measured.
- **Do not put alarm styling or red anywhere**, including on the reservoir sample.
- **Do not end on tooling, the plugin, or a call to adopt anything.**
