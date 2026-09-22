# Slide plan — Level 5 solo (20 minutes)

**Purpose:** explain the foundations Levels 3 and 4 laid, without demonstrating them, and
spend the time saved on the only thing this session actually demonstrates: **two sentinels
dispatched live, in front of the room, watching the person rather than the code.** Then
close on the question.

**Length:** 17 slides, 20 minutes. Foundations 5:15 (slides 1–5) · the demo 9:20 (slides
6–12) · the close 4:00 (slides 13–17). Written budget **18:35**, leaving 1:25 — which is
the fill you will want during the two live dispatches, not spare time.

**What makes this deck different from every other one in the repository:** it is the only
segment in the talk that dispatches an agent **for the sake of the sentinel rather than
the code**, and the only one where the audience watches something being decided about the
speaker. Everything else is evidence about a repository. Slides 8 and 9 are evidence about
the person standing in front of them.

**Source evidence:** [../captures/mast.txt](../captures/mast.txt),
[../captures/reservoir.txt](../captures/reservoir.txt),
[../../level-5/FINDINGS.md](../../level-5/FINDINGS.md),
[../../level-5/reservoir-reading.md](../../level-5/reservoir-reading.md),
[../../level-5/diagnosis.yaml](../../level-5/diagnosis.yaml),
[../../level-5/descent.md](../../level-5/descent.md),
[../../level-5/repertoire.md](../../level-5/repertoire.md),
[../../level-4/FINDINGS.md](../../level-4/FINDINGS.md).

**Design direction:** no ladder motif — this deck has one rung to light and a ladder drawn
for a room that never saw the bottom is a diagram of something they were not shown. The
visual thread is **the instrument**: files, proxy tables and captured output in the same
weight throughout, so the close's numbers do not arrive looking like a different kind of
claim. Slides 5, 9 and 15 are the ones to photograph. Slide 17 is the URL and nothing
else. No alarm styling, and no red — especially not on slide 9.

**The money rule:** `$52.16` appears once, on slide 13, always paired with `£0.00` actually
billed on a subscription. `~$211` appears once, on slide 5, under the same rule. Quote both
or neither, on screen and out loud.

**Two slides are live and the rest are not.** Say which, each time. Slide 3 is a shell
script; slides 8 and 9 dispatch a real agent. Everything else is a committed artefact or a
capture.

## What to have ready

| Needs | Where | For |
|---|---|---|
| Repo on `level-5-supervising`, clean tree | ladder | slides 2, 3, 4, 5 |
| `git config core.hooksPath .githooks` | ladder | slide 3 |
| **Claude Code profile logged in, both plugins** | `~/.claude-loops-at-levels-l2` | **slides 8, 9 — the demo fails without it** |
| **`~/.claude/pacts.md` Budgets `declared`** | home config | **slide 8 — nothing to recite otherwise** |
| `HARNESS.md` Cognitive reservoir block | ladder | slide 9 — the Warden declines without it |
| `level-5/trust-boundary.txt` | demo-notes | slide 7 |
| `level-5/reservoir-reading.md` | demo-notes | slide 10 |
| `level-5/diagnosis.yaml`, open at reading size | demo-notes | slide 11 |
| `../captures/*.txt` | demo-notes | slides 8, 9 — the fallbacks |

`./bin/demo preflight sentinels --fix` checks every row, including the pact state and the
reservoir block, which nothing else in the repository checks.

## Cutting

1. Slide 5 — what four gates produced
2. Slide 10 — the night it did fire
3. Slide 3 — the live harness catch
4. Slide 12 — ceremony and consequence

**Slides 13–17 never cut. Slides 8 and 9 never cut** — they are the reason this session
exists. If you are so far over that they look tempting, cut all four above and take the
bridge in one spoken line.

---

## Slide 1 — Where this starts

**Status:** keep. 30 s.

**Takeaway:** Three levels of foundation, named, not demonstrated.

| | | |
|---|---|---|
| 1 | Dictating | one human, one prompt, one turn |
| 2 | Commanding | the project writes down what it expects |
| 3 | Regulating | tools that say no whether or not anyone is watching |
| 4 | Orchestrating | a pipeline in front of the work, with a person inside it |
| **5** | **Supervising** | **instruments pointed at the person, not the code** |

**Visual:** the five, rung 5 emphasised by weight alone. No ladder artwork.

**Speaker note:** Thirty seconds. Do not teach levels 1 and 2 — name them and move. The
room needs to know there is ground under this, not to walk it.

---

## Slide 2 — What Level 3 built

**Status:** keep. 45 s.

**Takeaway:** A harness that says no, and means it.

| Constraint | Enforcement |
|---|---|
| Consistent formatting | deterministic — `spring-javaformat:validate` |
| Tests must pass | deterministic — `./mvnw verify` |
| Decision record, at commit | deterministic — `check-decision-record.sh`, warns |
| Decision record, at merge | deterministic — same script, blocks |
| Schema parity across databases | deterministic — `SchemaParityTest` |
| Abstraction earns its place | **agent — advisory**, no tool |

- Five enforced, one **honestly advisory** — and labelled as such, because a rule that
  counts callers would be precise and wrong.
- **Three loops**: advisory at edit and commit, strict at merge, and a scheduled sweep
  every Monday that reports and never blocks.

**Visual:** the table. The advisory row in the same weight as the rest — it is not a
weakness, it is a disclosure.

**Speaker note:** Forty-five seconds. The loops are the part to land: entropy does not
arrive with a pull request attached.

---

## Slide 3 — Watch it catch something

**Status:** cuttable — **cut 3rd**. **Run live** — a shell script and git, two seconds.

**Takeaway:** Same script, same hook, no argument.

```
decision-record: VIOLATED
```

- Stages a one-line change under `src/`, runs the check, puts the tree back. **Nothing is
  committed.**
- Then the line: **the change it refused came from the governed pipeline I am about to
  describe.** A better loop gets no discount on the rules.

**Visual:** the captured `VIOLATED` on the slide as the fallback, identical to live.

**Speaker note:** One sentence and move. This exists so the room believes Level 3 is real
before you spend the rest of the session on things it cannot do.

---

## Slide 4 — What Level 4 added, and what an instrument found in it

**Status:** keep. 90 s. This is the foundations' most important slide.

**Takeaway:** Four hard gates, two soft — and six of the seven are enforced by a person
deciding to stop.

| Gate | Enforcement |
|---|---|
| Slice adjudication | **human** |
| Objection adjudication | **human** |
| Choice-story surface | **human** |
| Consultation surface | **human** |
| Plan approval | **human** |
| Code review escalation | **human** |
| CI must be green | **deterministic** — `.github/workflows/harness.yml` |

- A gate is a point where an agent has produced something **it is forbidden to finish.**
- **The feedback loop worth the slide:** that column exists because an assessment agent
  read this branch, found `PIPELINE.md` describing its gates in the register of mechanism
  while nothing in the repository refuses anything, and named it as the clearest case of a
  document claiming enforcement it does not have. **It was right.** The column was added
  in fifteen minutes, and the false sentence was corrected rather than reworded.

**Visual:** the enforcement column at full weight, everything else reduced.

**Speaker note:** This is the first sentinel story of the session and it arrives before
any sentinel is named. An instrument found the project over-claiming, in writing, against
itself — and the response was to stop over-claiming rather than to start pretending. Say
the fifteen minutes; it is the shortest instrument-to-action loop in the whole build.

---

## Slide 5 — What four gates produced on one real feature

**Status:** cuttable — **cut 1st**, and the numbers survive on this slide if you cut the
talking.

**Takeaway:** 62 decisions. 40 disposed. 22 carried knowingly.

- **~45 minutes of my attention**, against **~19 hours** of elapsed clock — do not quote
  the 19 hours as work.
- **~$211** at API list price, and **£0.00** actually billed, because it ran on a
  subscription. Both or neither.
- The gates recurse: two accepted objections produced eight more decisions, and the
  translation bill went 20 strings → 40.

**And the thing none of it could see:** four levels deep in fixing the fixes, at quarter
to midnight, with the cost compounding. Hard gates block on `pending`. Soft gates report a
count. **Nothing was watching the trajectory.**

**Visual:** the three counts large; the two cost figures together; the last line on its
own. This is the first photographed slide.

**Speaker note:** The last line is the hinge of the whole session — say it and stop, do not
soften it into a transition.

---

## Slide 6 — What is being supervised changes

**Status:** keep. 45 s.

**Takeaway:** From the code, to whether I can still answer for it.

- Everything so far pointed at the **artefact**. Constraints, gates, tests, review.
- What needs supervising now is **the person who has to answer for it.**

**Visual:** two words, large. Nothing else.

**Speaker note:** Do not elaborate this. The next two slides are instruments running live
and they do the elaborating far better than you can.

**Figure prompt:**

> Sepia and sanguine pencil-and-ink illustration in the manner of a 19th-century etching,
> heavy cross-hatching, warm ochre and umber on a near-black ground. A brass surveyor's
> theodolite on a tripod in an empty workshop, turned away from the drawing board it was
> set up to measure and pointed instead at the vacant chair where the draughtsman sits.
> Cinematic, 16:9, no text, no lettering. Grainy, film-like, deep shadow.

---

## Slide 7 — What a sentinel is, before you watch two of them

**Status:** keep. 40 s.

**Takeaway:** It cannot change the thing it watches.

```
reservoir-warden:  tools: [Read, Glob, Grep, Bash]
mast:              tools: [Read, Glob, Grep, Bash]

spec-writer:       tools: [Read, Write, Edit, Glob, Grep]
integration-agent: tools: [Read, Write, Edit, Bash]
```

- No Write. No Edit. No Agent. **It cannot delegate to something that can.**

**Visual:** the excerpt verbatim; only the absent capabilities emphasised.

**Speaker note:** Say the framing line **before** they run, not after: *"Whatever these two
are about to tell me, neither of them can do anything about it. That is the design."* It
makes the next two slides land as evidence rather than as a product feature.

---

## Slide 8 — LIVE: the Mast recites the limit I set for myself

**Status:** **NEVER CUTS.** **Live dispatch**, measured at **46 s**. The fallback is
`../captures/mast.txt`.

**Takeaway:** A limit set in advance holds. A limit set at the moment you are about to
breach it does not.

> **hard_stop_hour: 21:30** · *Unspent budget is not a debt.*

**While it runs** — you have roughly forty-five seconds of terminal to fill:

- A limit you set in the moment you are about to breach it, you will simply move, because
  the thing you want at 21:30 is to keep working.
- **Nothing scaffolded this.** The plugin will not write the file for you: a default
  someone else chose is not a pact, so authorship is the active ingredient.

**When it returns** — it **recites before it measures**, and three of its four keys are
empty because they were left empty.

**Then read this aloud, and do not paraphrase it:**

> The stamp moves only when `/mast tune` writes it. If this file were hand-edited — the
> stop hour nudged from 21:30 to 23:00 at 20:55 in a text editor — `authored_at` would
> still read 2026-09-21 and this note would still tell you the pact is a day old. The
> check fires on the honest path and stays silent on the dishonest one.

**Visual:** the terminal, live, at reading size. No slide chrome competing with it.

**Speaker note:** That blind-spot paragraph is the whole level in one quotation — an
instrument disclosing that its own check is blind in exactly the direction a motivated
person would go. It volunteered that. Do not summarise it, and do not add a moral to it.

---

## Slide 9 — LIVE: the Warden reads the proxies, and probably tells me nothing

**Status:** **NEVER CUTS.** **Live dispatch**, measured at **65 s** — the longest wait in
the session. Fallback `../captures/reservoir.txt`, and read its stage notes first.

**Takeaway:** A quiet read is a valid read.

| Proxy | Threshold |
|---|---|
| Continuous span | 180 min |
| Decision volume | 8 |
| Context switches | 4 |
| Wall-clock hour | reported, uninterpreted — no chronotype declared |

**Expect it to come back quiet.** You will be mid-conference and the ladder will have had
no commits for hours. **Plan for quiet; treat a firing advisory as the bonus.**

**If quiet — the stronger beat.** *"I did not rehearse this to fire. It did not fire. Watch
what it refuses to do with that."* Then read both of these:

> The honest form of this read is *"no recorded activity in the window"*, not *"a measured,
> comfortable session"*. Those are different statements and I will not collapse them.

> Git silence is not evidence of rest... An empty window is consistent with eight hours of
> sleep and with eight hours of uncommitted verification, and I cannot distinguish them.

**If it fires:** do not celebrate it. Read the proxies, read its own counter-arguments, say
what you are going to do.

**Visual:** the terminal, live. **No alarm styling and no red under any circumstances** —
including if it fires. This is the second photographed slide.

**Speaker note:** An instrument that could have flattered you, declining to, is the
argument. If you have thirty spare seconds anywhere in the session, spend them on its third
limit: it volunteers unprompted that this repository's commit pattern would make its own
decision-volume proxy overcount and its own context-switch proxy fire on choreography
rather than attention. **It is telling you its numbers would be wrong here.**

---

## Slide 10 — The same instrument, the night it did fire

**Status:** cuttable — **cut 2nd**, and only if the live read fired, because then the room
has already seen the shape.

**Takeaway:** Same instrument. Opposite result. Identical honesty.

- Real, from the end of the Level 4 run. It fired on **two** proxies — then argued against
  its own finding **three times**: a five-minute margin, a 116-minute gap any idle cut
  would have split, and the possibility it was measuring elapsed time rather than time on
  task.
- **It gave me every reason to wave it through.** I agreed with it and stopped for the
  night.

**Visual:** the captured reading beside slide 9's live output, at the same size.

**Speaker note:** This is why the quiet read landed. An instrument whose only two outcomes
are *"here is what I saw, and here is why you might discount it"* is one you can keep.

---

## Slide 11 — An instrument read the code and found where I would get lost

**Status:** keep. 2 min. Read on screen from the file, not from notes.

**Takeaway:** About code specified, adjudicated and approved without a line of it being
written.

> Two things a reader of this class cannot see from it. First, what it saves is not a
> Visit... The Owner is the unit of persistence; a Visit reaches the database by cascade
> from `Pet.visits`. There is no `VisitRepository`. **A reader looking for where a visit is
> written will not find it in this package under that name.**

- It reached the null-safety argument **cold, from a third direction**, corroborating what
  the reviewer and the objection pass had fought over, having seen neither.
- It disclosed its limits rather than smoothing them: two elements at medium confidence,
  each naming what it could not establish.

**Visual:** `diagnosis.yaml` open in its own window at reading size. Not a rendered slide —
the file.

**Speaker note:** *"I specified that code. I adjudicated its gates. I approved its plan. I
did not write a line of it, and an instrument just told me where I would get lost in it."*
Leave it on screen while you say that.

---

## Slide 12 — It cannot tell ceremony from consequence, and says so

**Status:** cuttable — **cut 4th**, but you lose the bridge into the close.

**Takeaway:** Asked directly, it refused to guess.

> My proxies cannot distinguish a gate on a trivial change from a gate on consequential
> work. They cannot, and I will not pretend otherwise.

- It could see the diffstat and **refused to use it** — diff size measures the change
  rather than the adjudication, and would be *"a precise answer to the wrong question"*.
- **It cannot see who decided.** *"Git records who committed, not who decided."*

**Visual:** the two admissions as volunteered. **Terminal off after this slide.**

**Speaker note:** The handover: nothing in twenty minutes answers *was this the mode the
work deserved?*, and the thing that comes closest says so in its own output.

---

## Slide 13 — The descent

**Status:** **UNCUTTABLE.** Terminal off. One prepared view — **do not run the typo live.**

**Takeaway:** $52.16 and 45 minutes for twelve characters nobody will ever see.

| | Just make the change | Through the habitat |
|---|---|---|
| Time | under a minute | **45 minutes** |
| Human gates | none | **4** |
| Agents | none | **9** |
| Artefact | 1 line | **1,457 lines** |
| Cost | nothing | **$52.16** list · **£0.00** billed |

- Twelve characters of Thymeleaf prototype text, **replaced at runtime**. The rendered page
  is identical before and after.

**Visual:** the two columns. This is the third photographed slide.

**Speaker note:** Let it land. Let the room laugh.

---

## Slide 14 — Every one of them found something true

**Status:** **UNCUTTABLE.** Say it before the laugh settles.

**Takeaway:** The ceremony was proportionate. The findings were real. It still cost $52.

- Every agent was told, explicitly, that **finding nothing was a perfectly good answer**
  and that padding would corrupt the measurement. **Not one padded.**
- And each found something true: a coverage gap · a false claim in a record the harness
  requires · a scope claim wrong under the spec's own standard · a standard that collides
  with a directive · a standard with no defined answer · a question nobody had asked.
- **A reviewer would want all six.**

**Speaker note:** If the room leaves thinking the pipeline is stupid, this close has
failed.

---

## Slide 15 — The turn

**Status:** **UNCUTTABLE.**

**Takeaway:** Not a ladder. A repertoire.

- **Level 5 is not where anyone should be trying to live.**
- A habitat that can only work at its highest setting is a badly tuned habitat.
- The skill is **choosing the mode the work deserves.**

**Speaker note:** Deliver it as a conclusion, not an apology.

---

## Slide 16 — The question

**Status:** **UNCUTTABLE.** The last idea in the room.

**Takeaway:** If this turns out to be wrong, what does it take to put right?

**Undo · Repair · Migrate · Answer**

- **The typo → Undo.** It got four human gates, nine agents and forty-five minutes.
- **The booking feature → Migrate** — `NOT NULL` in three dialects, rows rewritten,
  packages coupled.
- **And also → Answer.** The owner's page shows a named clinician and a time beside *"your
  visit has been booked"*, and nothing in the clinic agreed to either. No migration fixes
  that. Someone has to be told.
- **The class that picks the mode is the most expensive one the work touches.**

**Speaker note:** *"Ask only 'can I revert it?' and you get Migrate — true, and not the
whole answer."*

---

## Slide 17 — The repository

**Status:** **UNCUTTABLE.** The URL and nothing else.

**Takeaway:** Every level is a branch, including the parts that do not flatter me.

`github.com/Habitat-Thinking/petclinic-loops-at-levels`

**Speaker note:** Then stop talking. **Do not end on the plugin.** The last thing on screen
is the repository, and the last idea in the room is the question.
