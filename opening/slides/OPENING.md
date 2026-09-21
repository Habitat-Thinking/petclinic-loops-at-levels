# Slide plan — the opening, before Level 1

**Purpose:** carry the room from the keynote into the demo without re-teaching the
keynote, introduce the speaker once and briefly, put the ladder on screen, and hand over
to `BEFORE-L1` with the room knowing what it is about to watch and why it should care.

**Length:** 5 slides, ~3 minutes. The talk behind it runs about 84 minutes — 39 of demo
beats and 45 of level decks — so three minutes is under 4% of the session and buys the
frame for all of it.

**It assumes the keynote.** *Does AI deliver waterfall?* has already given this room the
question — *if this turns out to be wrong, what does it take to put right?* — the four
classes **Undo / Repair / Migrate / Answer**, the definition of a sentinel, and a closing
slide that names this session by name. So this deck **never re-teaches any of it.** It
names what the room already holds, then spends the next eighty minutes turning that
argument into a measurement.

If the keynote did not happen, or this is a different room, all five slides still work —
the question is on the screen and the talk re-earns it — but drop *"this morning"* and say
*"the argument I want to test"* instead.

**Design direction:** the keynote's register for slides 1–2, then hand the visual language
over to the talk's own at slide 3. Dark slides for 1 and 4, light for 2, 3 and 5. The
six-rung ladder appears for the first time on slide 3 **with nothing lit** — the only
place in the whole talk it is shown cold. Every later deck lights one rung. Monospace for
anything literally typed on stage; the keynote's ochre for accents; no red anywhere.

**Source evidence:** the keynote deck *Does AI Deliver Waterfall?* (54 slides),
[../../LADDER.md](../../LADDER.md), [../../level-1/CLEAN-ROOM.md](../../level-1/CLEAN-ROOM.md),
[../../level-5/repertoire.md](../../level-5/repertoire.md).

**What this deck must not do:** explain the clean room (that is `BEFORE-L1` slides 3–4),
define Dictating (slide 1 there), summarise the keynote's argument, or preview the
close. It sets a frame and gets out of the way.

**Hand-off:** slide 5 goes straight into
[../../level-1/slides/BEFORE-L1.md](../../level-1/slides/BEFORE-L1.md) slide 1. No
transition slide. That deck has been told the opener exists, so it no longer re-introduces
the ladder.

**Driving it:** `./bin/demo run opening` teleprompts the five beats from
`bin/steps-opening.txt`, one per slide. There is **no preflight** and the dispatcher says
so — this segment runs no command, switches no branch and needs no Claude Code profile.
`./bin/demo run opening --time` gives the variance table; the written budget is 3:00.

---

## Slide 1 — Tonight

**Status:** **never cuts.** 30 seconds. Dark slide, keynote title styling.

**Takeaway:** The slide the keynote ended on, arrived.

> **(Agentic) Loops At Levels, Live**
>
> Russ Miles · Habitat Thinking · TechTalk

The keynote's last slide named this session and listed what is in it — *the application:
Spring PetClinic, forked · the artefacts: HARNESS.md, the specs, the sentinels in the
loop · every level: what it can carry, and what it can't*. It signed off with **"Carpaccio,
cutting. Bring the question."**

**Visual:** the keynote's own closing slide re-rendered as this deck's title. Same
typeface, same ochre kicker rule, `✤ TONIGHT` replaced by `✤ AND HERE WE ARE`. It should
look like the keynote resumed, not a new deck opening.

**Speaker note:** Thirty seconds, and introduce yourself **once**. The room met you this
morning; a second biography spends the frame on the wrong person. One line is enough:
*"This morning I made an argument. For the next hour and a half it has to survive a
repository."* Then move.

**Figure prompt:**

> Sepia and sanguine pencil-and-ink illustration in the manner of a 19th-century etching,
> heavy cross-hatching, warm umber and ochre on a near-black ground. A single terminal
> screen glowing faintly on a wooden desk in a dark workshop, seen from behind and to one
> side; the chair is empty but recently used, a cup still beside the keyboard. Deep shadow
> across the upper two-thirds. Cinematic, 16:9, no text, no lettering, no legible UI — the
> screen is light, not content. Muted, grainy, film-like.

---

## Slide 2 — You already have the question

**Status:** **never cuts.** 30 seconds. This is the hinge. Light slide.

**Takeaway:** Nothing here teaches the question again. It gets tested.

> **If this turns out to be wrong, what does it take to put right?**

`Undo` · `Repair` · `Migrate` · `Answer`

- This morning that was **an argument**. Royce, Naur, Popper, a taxonomy — and not one
  line of running code behind it.
- For the next eighty minutes it is **a measurement**. Five levels of habitat against one
  repository, with the figures for each.
- **The last slide of this talk is this slide.** Same question, same four words — with
  five levels of evidence behind them.

**Visual:** the keynote's own question slide, unchanged, including the four class chips
bottom-right. Do not restyle it. The room should recognise it instantly and feel the loop
close when it returns in `AFTER-L5`.

**Speaker note:** The most important thirty seconds in the deck. Say it plainly: *"You
already have the question. I'm not going to teach it again. I'm going to show you what
happened when I actually ran it."* Anyone who missed the keynote loses nothing — the
question is on the screen and the talk re-earns it. Do not summarise the keynote.

**Figure prompt:** *none, deliberately.* This slide must be recognised, and art breaks
recognition. Type and chips only, exactly as the keynote had it.

---

## Slide 3 — Five levels, and none of them lit yet

**Status:** **never cuts.** 45 seconds. This is where the talk's own visual language
starts.

**Takeaway:** Five ways of working with an agent. You are going to watch all five.

| | | |
|---|---|---|
| 1 | **Dictating** | one human, one prompt, one turn |
| 2 | **Commanding** | the project writes down what it expects |
| 3 | **Regulating** | tools that say no whether or not anyone is watching |
| 4 | **Orchestrating** | a pipeline in front of the work, and a person inside it |
| 5 | **Supervising** | instruments pointed at the person, not the code |

- Same repository. Same fork of Spring PetClinic. **Every level is a git branch** — six
  of them counting the untouched baseline, which is why the ladder has six rungs and this
  table has five rows. The diff between two of them is a slide.
- It is a ladder for the next eighty minutes because that is the order the evidence
  arrives in. **It is not a ranking, and the last slide of the talk says so.**

**Visual:** the six-rung ladder, **nothing lit** — the only time in the talk it appears
cold. Every later deck lights exactly one rung. Make the unlit state visually deliberate
rather than an oversight: the rungs drawn, the light absent.

**Speaker note:** Forty-five seconds, and resist the urge to preview. Name the five, say
the branch fact once, and plant the caveat — *"I'm calling it a ladder because that's the
order the evidence arrives in. By the end I'll be arguing it isn't one."* That single
sentence stops the room spending eighty minutes assuming you are selling the top rung, and
it is the only forward reference the opener is allowed.

**Figure prompt:** *none.* The ladder motif is the figure, and it needs to read as a
diagram the room will see nine more times, not as an illustration.

---

## Slide 4 — What ships, and what you are about to watch

**Status:** cuttable — **cut 1st**, and it is the only cuttable slide here. If it goes,
say its last two lines over slide 3.

**Takeaway:** Three of the sentinels run tonight. Two are still in build, and I said so
this morning.

| The keynote's map | Tonight |
|---|---|
| `carpaccio` — before the spec | **running**, live, on a real feature |
| `choice-cartographer` · `advocatus-diaboli` — on the spec | **running**, inside four human gates |
| `reservoir-warden` — not tied to a moment | **running**, and it stopped me |
| the Brief Sentinel — at the pull request | still in build. Not here. |
| the Rehearsal Sentinel — between decisions | still in build. Not here. |

- And the promise that matters: **you will also watch an instrument find this repository
  over-claiming**, against the project, in front of you — and it stayed in.

**Visual:** the keynote's *"Lights on where the decisions are"* timeline, re-used
unchanged, with the two in-build markers greyed and the three shipping ones lit. Same lamp
glyphs, same *the stretches between stay dark* caption.

**Speaker note:** This is the slide that stops the next eighty minutes being heard as a
product pitch. Two of five are not built; you said so this morning and you say it again
now. Then land the honest line — *"Dimmed everywhere is not twilight, it's theatre. You're
going to watch an instrument tell me this repository is dimmer than its branch name
claims, and I left that in."*

**Figure prompt** *(optional — only if you want the in-build markers illustrated rather
than greyed)*:

> Sepia and sanguine pencil-and-ink illustration, 19th-century etching style, heavy
> cross-hatching, warm ochre on near-black. Two unlit iron lamp-posts standing in mist at
> the far end of a long factory floor, their glass empty and cold, while three nearer
> lamps burn with a low warm light. Steep perspective down the floor. Cinematic, 16:9, no
> text, no lettering. Grainy, film-like, deep shadow.

---

## Slide 5 — One repository, eighty minutes, nothing staged

**Status:** **never cuts.** 30 seconds. Hands straight over to `BEFORE-L1`.

**Takeaway:** Live where it can be live. A recording where it cannot. Always said out
loud which.

| Level | Live on stage |
|---|---|
| 1 Dictating | the whole run — a clean room, a frozen prompt, an agent with nothing |
| 2 Commanding | `/extract-conventions` |
| 3 Regulating | `/harness-constrain`, then a rule broken on purpose and a merge refused |
| 4 Orchestrating | `/carpaccio`, and my dispositions typed in front of you |
| 5 Supervising | **nothing.** Every beat reads a committed artefact |

- Where it is not live, it is a **checkpoint or a capture**, because a pipeline stage that
  rehearsed at three and a half minutes does not fit on a stage — **and I will say so each
  time.**
- **Nothing is re-shot to look better.** The unflattering results are in the branches, and
  they are in this talk on purpose.

**Visual:** the table, plain, and the two lines beneath it. No diagram, no icons.

**Speaker note:** Thirty seconds, flat delivery, no promises about how well it will go.
This is a contract with the room, and the only way it pays off is by being kept later —
every time you say *"this one is a recording"* it spends credit you opened here. Then go
straight into `BEFORE-L1` slide 1 with no transition. **Do not** explain the clean room
here; that is `BEFORE-L1` slides 3 and 4, and doing it twice costs ninety seconds you will
want back at Level 4.

**Figure prompt:** *none.* A contract slide with artwork on it reads as marketing.
