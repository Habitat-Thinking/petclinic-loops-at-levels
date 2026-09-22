# Slide plan — Level 5 solo (20 minutes)

## The spine

**A habitat has two jobs.**

| | Serves | Through | In this session |
|---|---|---|---|
| **Job one** | the **agents** | the harness, the constraints, the pipeline, the gates | **explained** — act 1, slides 2–5 |
| **Job two** | the **human** | the sentinels — decision sovereignty, and the capability to keep exercising it | **sampled** — act 2, slides 6–13 |

Almost everything written about this subject only has job one. This session gives it four
minutes because it is the part the industry already argues about, and then spends the rest
on the part it does not.

Every slide belongs to one half or the other, and the deck says which.

**Length:** 18 slides, 16 beats (slides 6 and 7 share beat 6). Written budget **18:30** in
a 20-minute slot — 11:20 on slides, 7:10 on the terminal. **No slide needs cutting to fit.**

## Everything in act 2 is a sample

**Nothing dispatches an agent.** Every sentinel beat plays committed, unedited output from
a real run. No profile, no login, no `--plugin-dir`, nothing that can wander.

That is a measured decision, not a timid one:

| Sentinel | Measured | Tokens |
|---|---|---|
| `mast` | 46 s | 13,654 |
| `reservoir-warden` | 65 s | 25,948 |
| `wip-warden` | 121 s | 33,839 |
| `cost-estimator` | **289 s** | 80,072 |
| | **8 min 41 s** | |

Eight and a half minutes of dispatch does not fit in twenty, and **what makes these
instruments worth showing is their reasoning, which a sample preserves exactly.**

**One thing is still live:** slide 3, a shell script, two seconds. It is there because the
Level 3 argument rests on the harness being real rather than described.

**The shape of act 2 on screen:** the deck names the **characteristic** first; the terminal
then shows that characteristic in the instrument's own words. Never the other way round —
output first and explanation after is a tour of a plugin.

**Source evidence:** [../captures/](../captures/) (all five samples),
[../../level-5/diagnosis.yaml](../../level-5/diagnosis.yaml),
[../../level-5/FINDINGS.md](../../level-5/FINDINGS.md),
[../../level-5/descent.md](../../level-5/descent.md),
[../../level-5/repertoire.md](../../level-5/repertoire.md),
[../../level-4/FINDINGS.md](../../level-4/FINDINGS.md).

**Design direction:** no ladder motif. The visual thread is **the two jobs** — act 1 and
act 2 are visually distinct, and the divider at slide 6 should be unmistakable. Within act
2 every instrument gets the same layout: characteristic in the header, its own words below.
Slides 5, 9 and 14 are the ones to photograph. Slide 18 is the URL and nothing else. No
alarm styling and no red anywhere.

**The money rule:** `$52.16` appears once, on slide 14, always paired with `£0.00` actually
billed. `~$211` appears once, on slide 5, under the same rule. Both or neither.

## What to have ready

| Needs | Where | For |
|---|---|---|
| Repo on `level-5-supervising`, clean tree | ladder | slides 2–5 |
| `git config core.hooksPath .githooks` | ladder | slide 3 |
| `../captures/*.txt` — five samples | demo-notes | slides 6, 8–11 |
| `level-5/diagnosis.yaml`, open at reading size | demo-notes | slide 12 |
| `level-5/descent.md` figures on one prepared view | demo-notes | slide 14 |

`./bin/demo preflight sentinels --fix` checks every row. **No Claude Code profile is
needed** — and preflight says so out loud.

## Cutting

1. Slide 5 — job one at its best
2. Slide 9 — sovereignty 2, the `wip` refusal
3. Slide 3 — the live harness catch
4. Slide 13 — what none of them can do

**Slide 1 never cuts** — it is the thesis, and without it this is a tour of a plugin.
**Slides 14–18 never cut.** At 18:30 you should not need any of these.

---

## Slide 1 — The two jobs a habitat has

**Status:** **NEVER CUTS.** 60 s. The thesis. Everything after it is evidence for one half
or the other.

**Takeaway:** One job is for the agents. The other is for the person who has to answer for
what they did.

| | |
|---|---|
| **Job one — the agents** | the harness · the constraints · the pipeline · the gates |
| **Job two — the human** | **decision sovereignty**, and the **capability** to keep exercising it |

- Levels 1 to 4 are **all job one**. So is nearly everything written about this subject.
- Job two is not about the human's *productivity*. It is about whether the decisions are
  still theirs, and whether they are still able to make them.

**Visual:** two columns, equal weight. Do not let job two look like an addendum — the whole
argument is that it is the missing half, not a refinement.

**Speaker note:** Sixty seconds, and do not oversell it. *"A habitat has two jobs, and
almost everything written about this only has the first one. I'm going to spend four
minutes on job one because it's the part we already argue about, and the rest of the
session on job two, which we don't."*

**Figure prompt:**

> Sepia and sanguine pencil-and-ink illustration in the manner of a 19th-century etching,
> heavy cross-hatching, warm ochre and umber on near-black. A workshop split by a shaft of
> light: on one side a lathe surrounded by jigs and gauges set up to hold a workpiece true;
> on the other an empty chair at a drawing board, a lamp trained on it, nobody sitting
> there. One room, two kinds of apparatus. Cinematic, 16:9, no text, no lettering. Grainy,
> film-like, deep shadow.

---

# ACT 1 — what the habitat does for the agents

## Slide 2 — What Level 3 built for the agents

**Status:** keep. 75 s.

**Takeaway:** Tools that say no whether or not anyone is watching.

| Constraint | Enforcement |
|---|---|
| Consistent formatting | deterministic — `spring-javaformat:validate` |
| Tests must pass | deterministic — `./mvnw verify` |
| Decision record, at commit | deterministic — `check-decision-record.sh`, warns |
| Decision record, at merge | deterministic — same script, blocks |
| Schema parity across databases | deterministic — `SchemaParityTest` |
| Abstraction earns its place | **agent — advisory**, no tool |

- Five enforced, one **honestly advisory** and labelled as such.
- **Three loops**: advisory at edit and commit, strict at merge, a scheduled sweep weekly.

**Note who this serves.** Every row points at the **artefact**. It bounds what an agent may
do to the code. **None of it has an opinion about the person.**

**Speaker note:** That last line is the one doing work. Say it plainly and move — you are
laying ground for the hinge on slide 5, not selling the harness.

---

## Slide 3 — The only live thing today

**Status:** cuttable — **cut 3rd**. **Run live** — a shell script and git, two seconds.

**Takeaway:** Same script, same hook, no argument.

```
decision-record: VIOLATED
```

- Stages a one-line change under `src/`, runs the check, puts the tree back. **Nothing is
  committed.**
- **The change it refused came from the governed pipeline on the next slide.** A better
  loop gets no discount on the rules.

**Speaker note:** One sentence and move. This is here so the room believes job one is real
before you spend the session on what it cannot do.

---

## Slide 4 — What Level 4 added, and what an instrument found in it

**Status:** keep. 90 s. The most important slide in act 1.

**Takeaway:** Four hard gates, two soft — and six of the seven are a person deciding to
stop.

| Gate | Enforcement |
|---|---|
| Slice adjudication · Objection adjudication · Plan approval | **human** |
| Choice-story surface · Consultation surface · Review escalation | **human** |
| CI must be green | **deterministic** — `.github/workflows/harness.yml` |

- A gate is a point where an agent has produced something **it is forbidden to finish.**
- **That column exists because an instrument looked at the project and found it
  over-claiming.** An assessment agent read this branch, found `PIPELINE.md` describing its
  gates in the register of mechanism while nothing in the repository refuses anything, and
  said so. **It was right.** The column was added in fifteen minutes.

**Speaker note:** Worth flagging as you pass: that is the first time in the session an
instrument points at *me* rather than the code — and it arrives inside job one, which is a
hint about where this is going.

---

## Slide 5 — Job one at its best, and the thing it still cannot see

**Status:** cuttable — **cut 1st**, and the numbers survive on the slide.

**Takeaway:** 62 decisions. 40 disposed. 22 carried knowingly. And none of it was watching
the person.

- **~45 minutes of my attention** against **~19 hours** of elapsed clock — do not quote the
  19 hours as work.
- **~$211** at list price, **£0.00** actually billed.

**This is job one working well.** Then the hinge:

> Four levels deep in fixing the fixes, at quarter to midnight, and the cost compounding.
> Hard gates block on `pending`. Soft gates report a count. The harness reads markdown and
> shrugs. **Not one of them can see that** — every instrument in job one points at the
> artefact, and the thing at risk was me.

**Visual:** the three counts large, the two cost figures together, the hinge on its own.
First photographed slide.

**Speaker note:** Say the hinge and stop. Do not soften it into a transition — slide 6 is
the transition.

---

# ACT 2 — what the habitat does for the human

## Slide 6 — What a sentinel is

**Status:** keep. Shares beat 6 with slide 7, 45 s across both.

**Takeaway:** It cannot change the thing it watches.

```
reservoir-warden:  [Read, Glob, Grep, Bash]
mast:              [Read, Glob, Grep, Bash]
wip-warden:        [Read, Glob, Grep, Bash]
cost-estimator:    [Read, Glob, Grep]

spec-writer:       [Read, Write, Edit, Glob, Grep]
integration-agent: [Read, Write, Edit, Bash]
```

- No Write. No Edit. No Agent. **It cannot delegate to something that can.**
- **`cost-estimator` has no `Bash` either** — it cannot run a command to measure, only
  read. The tightest boundary of the four, and the one about to give you a number.

**Speaker note:** Two lines, both before any instrument speaks. First: *"Whatever these
tell me, not one of them can do anything about it. That is not a limitation, it's the
entire design."* Second, and say it **once** so you never have to say it again:
*"Everything from here is a sample — real, unedited output from real runs."*

---

## Slide 7 — The two things job two protects

**Status:** keep. Shares beat 6 with slide 6.

**Takeaway:** The decisions stay yours, and you stay able to make them.

| | Protects | Instruments |
|---|---|---|
| **Decision sovereignty** | the call is yours, and you can answer for it | `mast` · `wip-warden` · `cost-estimator` |
| **Standing able** | you remain in a condition, and a comprehension, to make it | `reservoir-warden` · the legibility diagnosis |

- Sovereignty without capability is a signature on something you no longer understand.
- Capability without sovereignty is being able to do work that someone else decided.

**Visual:** the two halves side by side, the five instruments under them. This map is what
the next five slides walk.

**Speaker note:** Fifteen seconds. It is a map, not an argument — the instruments make the
argument.

---

## Slide 8 — Sovereignty 1: it recites what you authored, and nothing else

**Status:** keep. 75 s. **Sample.**

**The characteristic:** *it holds a line I set, and it has no power to set one.*

> **hard_stop_hour: 21:30** · *Unspent budget is not a debt.*

- A limit set in advance holds. A limit set at the moment you are about to breach it does
  not — **you will simply move it**, because the thing you want at half past nine is to
  keep working.
- **Nothing scaffolded this file.** The plugin will not write it for you, because a default
  someone else chose is not a pact. **Authorship is the active ingredient** — that is
  decision sovereignty in one word.
- It **recites before it measures**, and three of its four keys are empty because they were
  left empty.

**Then read this aloud, unparaphrased:**

> The stamp moves only when `/mast tune` writes it. If this file were hand-edited — the
> stop hour nudged from 21:30 to 23:00 at 20:55 in a text editor — this note would still
> tell you the pact is a day old. **The check fires on the honest path and stays silent on
> the dishonest one.**

**Speaker note:** An instrument disclosing, unprompted, that its own check is blind in
exactly the direction a motivated person would go. Do not add a moral.

---

## Slide 9 — Sovereignty 2: it will not draw the line you did not draw

**Status:** cuttable — **cut 2nd**, but it is the cheapest minute in the deck. 75 s.
**Sample.**

**The characteristic:** *given a count and no limit, it refuses to invent one.*

Same file the Mast just read. This one wanted `Session WIP`. **There is no `Session WIP`
block** — a stop hour was authored and the rest declined. So it holds a number and has
nothing to check it against.

> A count wants a threshold the way a sentence wants a verb... It would be me disapproving
> of them with a number attached, and borrowing the authority of a promise they never made
> to do it. **The whole force of a pact is in its authorship. Invent the limit and you have
> kept the format and thrown away the only thing that made it legitimate.**

- It refused the opposite failure too — **silence** — because *"you cannot tell 'no pact
  declared' from 'pact declared and comfortably inside it'. Both are silent."*

**Visual:** the quotation, large. The count and its flag small or not at all — **do not use
its `7` as a punchline**; it is one real session plus six foreign files sharing a
directory, and it says so itself.

**Speaker note:** The strongest sentence in the session, and it came from an agent
explaining why it would **not** do the helpful-looking thing. Second photographed slide.

---

## Slide 10 — Sovereignty 3: it will not price what it cannot ground

**Status:** keep. 85 s. **Sample.** The only instrument here that looks *forward*.

**The characteristic:** *asked for a number before you commit, it would rather refuse than
be precise about something it cannot ground.*

- **It gave no dollar figure at all**, mechanically: the one snapshot on disk has a single
  model row resolving to no estimating tier. **It refused to promote the ~$211 list price
  into a rate.**
- It gave a token band **two orders of magnitude wide** and said why a narrower one would
  be *"more precise-looking and less honest"* — citing **this repository's own recorded
  error**, the figure wrong by two orders of magnitude because it counted output and not
  cache reads.

> Between **65% and 78%** of the tokens are produced by the judgment stages **before the
> implementer begins**. The implementer changes **three to five tokens** of text — five to
> seven orders of magnitude between the artefact and the apparatus that produces it.

**Speaker note:** **Do not say it predicted $52.16 — it gave no figure.** The refusal is
the point. It reached the close's argument independently, before the close, without being
told the answer. If the clock allows: it also said the typo **was not there** — all five
prototype strings are correctly spelled — and priced the pipeline named rather than
assuming the premise of the question.

---

## Slide 11 — Standing able 1: it will not turn "no data" into "you're fine"

**Status:** keep. 85 s. **Sample.**

**The characteristic:** *it reports what it saw, flags how confident it is, and refuses to
round silence up into reassurance.*

Four proxies over the git window. **It never writes a record of my state to disk**, and
nobody else can read it — I am not being measured *for* anyone.

**This sample came back quiet. Nothing crossed.** Watch what it refuses to do with that:

> The honest form of this read is *"no recorded activity in the window"*, not *"a measured,
> comfortable session"*. Those are different statements and I will not collapse them.

> Git silence is not evidence of rest. An empty window is consistent with eight hours of
> sleep and with eight hours of uncommitted verification, and I cannot distinguish them.

**Speaker note:** An instrument that could have flattered you, declining to. Then the
contrast in one line: the same instrument the night it **did** fire, on two proxies — and
then argued against its own finding three times before I agreed with it and stopped.

---

## Slide 12 — Standing able 2: it tells you where your understanding fails

**Status:** keep. 100 s. **Sample.** Read from the file, at reading size.

**The characteristic:** *it maps the code against whether I could still answer for it — and
names where I could not.*

> Two things a reader of this class cannot see from it... The Owner is the unit of
> persistence; a Visit reaches the database by cascade from `Pet.visits`. There is no
> `VisitRepository`. **A reader looking for where a visit is written will not find it in
> this package under that name.**

**Speaker note:** *"I specified that code. I adjudicated its gates. I approved its plan. I
did not write a line of it, and an instrument just told me where I would get lost in it."*
This is the skill half of job two: lose the theory and the code does not become wrong, it
becomes **unchangeable**. This is what notices. Leave it on screen.

---

## Slide 13 — What none of them can do, in their own words

**Status:** cuttable — **cut 4th**, but you lose the bridge. **Terminal off after this.**

**Takeaway:** Job one cannot see me. Job two can, and tells me exactly where its sight ends.

> My proxies cannot distinguish a gate on a trivial change from a gate on consequential
> work. They cannot, and I will not pretend otherwise.

- It could see the diffstat and **refused to use it** — *"a precise answer to the wrong
  question"*.
- **It cannot see who decided.** *"Git records who committed, not who decided."*

**Neither job can tell me whether the ceremony was worth it.**

---

# THE CLOSE

## Slide 14 — The descent

**Status:** **UNCUTTABLE.** Terminal off. **Do not run the typo live.**

**Takeaway:** $52.16 and 45 minutes for twelve characters nobody will ever see.

| | Just make the change | Through the habitat |
|---|---|---|
| Time | under a minute | **45 minutes** |
| Human gates | none | **4** |
| Agents | none | **9** |
| Artefact | 1 line | **1,457 lines** |
| Cost | nothing | **$52.16** list · **£0.00** billed |

**Speaker note:** One clause this version has earned: *"The estimator showed you this shape
earlier and would not put a price on it. Here is the price."* Then let the room laugh.
Third photographed slide.

---

## Slide 15 — Every one of them found something true

**Status:** **UNCUTTABLE.** Before the laugh settles.

- Every agent was told **finding nothing was a perfectly good answer** and that padding
  would corrupt the measurement. **Not one padded.**
- Each found something true: a coverage gap · a false claim in a record the harness
  requires · a scope claim wrong under the spec's own standard · a standard colliding with
  a directive · a standard with no defined answer · a question nobody had asked.
- **A reviewer would want all six.**

**Job one was working perfectly. That is the problem.**

**Speaker note:** If the room leaves thinking the pipeline is stupid, this close has failed.

---

## Slide 16 — The turn

**Status:** **UNCUTTABLE.**

**Takeaway:** Not a ladder. A repertoire.

- A habitat that can only work at its highest setting is a badly tuned habitat.
- The skill is **choosing the mode the work deserves**.
- **And choosing is job two.** No harness, no pipeline and no gate can do it for you —
  which is exactly why the sentinels exist, and why not one of them decides anything.

**Speaker note:** Deliver it as a conclusion, not an apology.

---

## Slide 17 — The question

**Status:** **UNCUTTABLE.** The last idea in the room.

**Takeaway:** If this turns out to be wrong, what does it take to put right?

**Undo · Repair · Migrate · Answer**

- **The typo → Undo.** It got four human gates, nine agents and forty-five minutes.
- **The booking feature → Migrate**, and **also → Answer**: the owner's page shows a named
  clinician and a time beside *"your visit has been booked"*, and nothing in the clinic
  agreed to either. No migration fixes that.
- **The class that picks the mode is the most expensive one the work touches.**

---

## Slide 18 — The repository

**Status:** **UNCUTTABLE.** The URL and nothing else.

`github.com/Habitat-Thinking/petclinic-loops-at-levels`

**Speaker note:** Then stop talking. **Do not end on the plugin.**
