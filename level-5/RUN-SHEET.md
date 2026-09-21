# Run sheet — Level 5: Supervising, and the close (8 min + 4 min)

**The close is the fixed point of the session.** Everything yields to it —
Level 4, Level 5, the questions, the demo gods. If Level 5 has to be cut to
beats 1, 2, 3 and 6, cut it. If the close is cut, the talk has no ending and the
previous fifty minutes argue for something you did not mean.

Protect four minutes. Terminal off for them.

## Profile

Same profile as Levels 2–4 (`~/.claude-loops-at-levels-l2`), **with one change**:
the launcher now carries a second `--plugin-dir` for `diagnostic-legibility`
0.11.0. This is the first level since 3 to change the launcher, and the honest
line is that this level adds an *instrument*, not a rule.

## Before you walk on

- [ ] `./bin/demo preflight 5`
- [ ] Repo on `level-5-supervising`, clean tree.
- [ ] `docs/superpowers/` and `decisions/` present from the Level 4 and descent runs.
- [ ] The diagnosis open in a second window — it is read on screen, not from notes.
- [ ] The descent figures on one prepared view. **Do not run the typo live.**

## Beats — the level (8 min)

### 1. Pick up the Level 4 number (30 s)

No transition. The previous segment already made the argument.

*"Sixty-two decisions. Forty-five minutes of my attention. And by the second gate
I was writing the same eleven-word rationale against eleven different
objections."*

### 2. Name the shift (45 s)

Everything up to here supervised the code. Constraints, gates, tests, review —
all of it pointed at the artefact.

*"What needs supervising now is whether I can still answer for it."*

### 3. Show the sentinel definition (15 s)

`level-5/trust-boundary.txt`. Same plugin, same file format, one line each.

```
reservoir-warden:  tools: [Read, Glob, Grep, Bash]
spec-writer:       tools: [Read, Write, Edit, Glob, Grep]
integration-agent: tools: [Read, Write, Edit, Bash]
```

*"No Write. No Edit. No Agent. It cannot change the thing it watches and it
cannot delegate to something that can."* Fifteen seconds, then move.

### 4. The cognitive-resource reading (90 s)

`level-5/reservoir-reading.md` — the real one, from the end of the Level 4 run.

It fired, then argued against its own finding three times: a five-minute margin,
a 116-minute gap any idle cut would have split, and the possibility it was
measuring elapsed rather than engaged time.

**Say what you did:** agreed with it entirely, and stopped for the night.

*"It gave me every reason to wave it through. I took the advice."*

### 5. The override (60 s) — **the beat that distinguishes a sentinel from a gate**

The reservoir was taken, so the override comes from Level 4:
**code-mode objection O4**, in
`docs/superpowers/objections/visit-carries-vet-and-time-code.md`.

The adversarial reviewer said a vet-less visit taking down the whole owner page
was too wide a blast radius. **Rejected**, in writing: *"Fail loud... A vet-less
visit is corruption, and a blank cell would hide it."* Two agents disagreed with
each other and a human settled it against one of them.

*"They advise. They do not decide. If every piece of advice in this talk had been
taken, you should not believe any of it."*

### 6. `/diagnose` on the code the pipeline wrote (2 min) — **on screen, not from notes**

`level-5/diagnosis.yaml`. Read the uncomfortable part aloud and do not editorialise
it away:

> *"Two things a reader of this class cannot see from it. First, what it saves is
> not a Visit... The Owner is the unit of persistence; a Visit reaches the
> database by cascade from `Pet.visits`. There is no `VisitRepository`. A reader
> looking for where a visit is written will not find it in this package under
> that name."*

*"I specified that code. I adjudicated its gates. I approved its plan. I did not
write a line of it, and an instrument just told me where I would get lost in it."*

### 7. The two assessments, side by side (45 s)

`level-5/assessment-level-1.md` and `level-5/assessment-level-5.md`. Generated
from the repository; anyone can clone it and re-run them.

**Level 1: Level 1.** **The branch called `level-5-supervising`: Level 4.**
Gap +1.0, *"ambition outpaces enablement"*.

*"The repository disagrees with the branch name. Every pipeline gate is prose —
a grep for `disposition` across every script and workflow returns nothing. It
found that, not me, and I am showing you anyway."*

**Cut beats 4, 5 and 7 before you touch the close.**

---

## The close (4 min) — **UNCUTTABLE. Terminal off.**

### 8. The descent (90 s)

One prepared view. `level-5/descent.md`.

Twelve characters of prototype text in a Thymeleaf template. Invisible at
runtime — the rendered page is byte-identical before and after.

| Just make it | Through the habitat |
|---|---|
| under a minute | **45 minutes** |
| $0.00 | **$52.16** |
| 0 gates | **4 human gates** |
| 0 agents | **9 agents** |
| 1 line | **1,457 lines of artefact** |

Let it land. Let the room laugh.

**Then take the laugh away**, because this is the move the whole close depends on:

*"Every one of those agents was told that finding nothing was a perfectly good
answer. Not one of them padded. And every single one found something true — a
coverage gap, a false claim in a required record, a scope claim that was wrong
under the spec's own standard. A reviewer would want all six findings."*

*"The ceremony was proportionate. The findings were real. And it still cost
fifty-two dollars to fix a string nobody can see."*

### 9. The turn (45 s)

*"So the answer is not to build the habitat higher."*

Level 5 is not where anyone should be trying to live. A habitat that can only
work at its highest setting is a badly tuned habitat.

*"What we have been building is not a ladder to the top. It is a repertoire —
and the skill is choosing the mode the work deserves."*

### 10. The question (60 s)

One question picks the mode:

> **If this turns out to be wrong, what does it take to put right?**

**Undo · Repair · Migrate · Answer.**

- **The typo → Undo.** Revert it and it never happened. It got four gates and
  fifty-two dollars.
- **The booking feature → Migrate.** `NOT NULL` in three dialects, rows rewritten,
  packages coupled. Reversal is a planned migration.
- **And also → Answer.** The owner's page shows a named clinician and a time
  beside "your visit has been booked", and nothing in the clinic agreed to
  either. No migration fixes that. Someone has to be told.

*"Ask only 'can I revert it?' and you get Migrate — true, and not the whole
answer. The class that picks the mode is the most expensive one the work
touches."*

*"The question I should have asked about the typo took ten seconds. I asked it
after the fact, from the transcript, for forty-five minutes and fifty-two
dollars."*

### 11. The handover (30 s) — **the last thing on screen is the repository**

*"Every level is a branch. The assessments, the diagnosis, the sentinel readings,
the cost snapshots, the seventeen decisions still sitting `pending` — all of it
is in there, including the parts that do not flatter me."*

`github.com/Habitat-Thinking/petclinic-loops-at-levels`

*"Go and look at the one below where you are. Then the one above. Then ask the
question."*

**Do not end on the plugin.** The last idea in the room is the question.

## Fallback

There is no live run to abandon at this level — every beat reads a committed
artefact. If something will not open, say the finding and move on; the words
carry it. The close needs no terminal at all.

## What not to do

- Do not soften the diagnosis or the reservoir reading. Their rawness is the argument.
- Do not run the descent live. Ninety seconds funny, four minutes fatal.
- Do not end on tooling, a plugin, or a call to adopt anything.
- Do not deliver the turn as an apology. Four levels of evidence earned the right
  to say the fifth is not a destination.
