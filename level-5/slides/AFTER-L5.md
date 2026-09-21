# Slide plan — after the Level 5 demo, and the close

**Purpose:** land what the instruments found, including what they found against the
project, and then deliver the close — the descent, the twist, the turn, the question, the
repository. This is the deck the session resolves into.
**Length:** 11 slides. Slides 1–6 back the level's remaining ~6 minutes; slides 7–11 are
the close's 4 minutes.
**Source evidence:** [../FINDINGS.md](../FINDINGS.md), [../RUN-SHEET.md](../RUN-SHEET.md),
[../descent.md](../descent.md), [../repertoire.md](../repertoire.md),
[../diagnosis.yaml](../diagnosis.yaml), [../assessment-level-5.md](../assessment-level-5.md).

**Design direction:** ladder motif, rung 5 lit, through slide 6 only. **From slide 7 the
ladder leaves the screen and does not come back** — that is the visual argument for the
turn, and it should happen without comment. Slides 7 and 10 are the ones to photograph.
Slide 11 is the URL and nothing else.

**Cutting:** slide 4 goes first, then slides 1, 2 and 3 in that order, matching the run
sheet's *cut beats 4, 5 and 7 before you touch the close.* **Slides 7–11 are UNCUTTABLE.**
If the clock is gone, go from slide 5 straight to slide 7.

**The money rule:** the cost figure appears once, on slide 7, with **both** numbers — list
price and actually billed. Nowhere else on any slide, and nowhere else spoken. Quote both
or neither.

---

## Slide 1 — The repository disagrees with the branch name

**Status:** cuttable for time — run-sheet beat 7.

**Takeaway:** The branch called `level-5-supervising` assesses as Level 4.

- Habitat Build Gap **+1.0** — *"ambition outpaces enablement"*. Run read-only against a
  detached checkout; anyone can clone it and re-run it.
- Not because the work is thin. Context engineering scores **5/5**, and the habitat
  surface is called *"the best-designed habitat surface I have assessed on this
  codebase"*.

> Every pipeline gate is prose. A `grep` for `disposition|pending` across every script,
> workflow and habitat document returns nothing.

> Nothing this project built for itself has ever been stopped by the gate it built.

**Visual:** the two assessment verdicts side by side — Level 1 branch: Level 1; branch
named level-5: Level 4 — with the gap figure between them.

**Speaker note:** *"It found that, not me, and I am showing you anyway."* The honest extra
if asked: the assessor also counted what the four gates produced — 46 dispositions, 17
still pending — and called the eleven objections sharing one rationale *"a bulk action
wearing the costume of eleven judgements"*. Do not defend the branch name.

---

## Slide 2 — It argued against its own finding, and was taken anyway

**Status:** cuttable for time — run-sheet beat 4.

**Takeaway:** It gave every reason to wave it through.

The reservoir fired on two proxies at the end of the Level 4 run, then argued against
itself three times:

- a **five-minute margin** on the span
- a **116-minute gap** inside the window that any idle cut would have split
- the possibility it was measuring **elapsed rather than engaged** time

- What happened next: **agreed with it entirely, and stopped for the night.**
- It had already refused its own briefing. Told the time was "early afternoon", it ran
  `date`, found **17:09**, and listed the discrepancy among three things it would not tidy
  away.

**Visual:** the three counter-arguments as the sentinel wrote them, with the decision
underneath in one line.

**Speaker note:** A sentinel that accepts its briefer's version of reality is not watching
anything. This is the harder story than an override, because nothing forced the stop —
which is exactly why the next slide exists.

---

## Slide 3 — Advised, rejected, reasoning recorded

**Status:** cuttable for time — run-sheet beat 5.

**Takeaway:** They advise. They do not decide.

- **Code-mode objection O4.** The adversarial reviewer said a vet-less visit taking down
  the whole owner page was too wide a blast radius.
- **Rejected**, in writing, within minutes:

> Fail loud... A vet-less visit is corruption, and a blank cell would hide it.

- Two agents disagreed with each other. A human settled it against one of them.

**Visual:** the objection and its disposition, one above the other, neither flagged as the
winner.

**Speaker note:** *"If every piece of advice in this talk had been taken, you should not
believe any of it."* If this slide is cut, that line still has to be said somewhere —
carry it into slide 6.

---

## Slide 4 — The instrument's own write surface failed silently

**Status:** cuttable, and **first to go** — it is the newest evidence and the least
load-bearing. The argument survives without it.

**Takeaway:** Found by running the command, not by reading it.

`/mast tune` is the only path that creates a pact. Its command file says to *source*
`pact-write.sh`. That library finds itself through `BASH_SOURCE`, which **zsh does not
set** — and the caller is a Claude session's shell.

| | exit code | `block_state` |
|---|---|---|
| bash | 0 | `declared` |
| **zsh** | **0** | **`malformed`** |

- The writer's own header names this outcome: *"fails in the quietest possible way…
  and nothing tells the human which sentence is missing."*
- **Its tests cannot catch it.** The round-trip guard is the suite's whole point, and
  it runs under a `bash` shebang — it exercises the library, not the path the command
  tells a model to take.
- Every **read** surface is clean. The one **write** surface a command sources is the
  broken one, and it breaks at first authorship.

**Visual:** the two-row table, and one line of the writer's own header comment beneath
it. No alarm styling — this is not a scandal, it is a category.

**Speaker note:** Keep it to forty seconds and do not turn it into a story about a
plugin. The point is the category: *the harness could not find this, the pipeline could
not find this, and the sentinels could not find this. Running it found it.* Filed as
issue 617 the same afternoon. If you are anywhere near time, cut this slide — slide 6
makes the limits argument better, in the instrument's own words.

---

## Slide 5 — An instrument read the code and found where I would get lost

**Status:** keep — run-sheet beat 6, read on screen rather than from notes.

**Takeaway:** About code specified, adjudicated and approved without a line of it being
written.

> Two things a reader of this class cannot see from it. First, what it saves is not a
> Visit... The Owner is the unit of persistence; a Visit reaches the database by cascade
> from `Pet.visits`. There is no `VisitRepository`. **A reader looking for where a visit is
> written will not find it in this package under that name.**

- It reached the null-safety argument **cold, from a third direction** — *"defensive
  syntax is present where it cannot help and absent where a reader would look for it"* —
  corroborating what the reviewer and the objection pass had fought over, having seen
  neither.
- It disclosed its limits rather than smoothing them: two elements at medium confidence,
  each naming what it could not establish.

**Visual:** the diagnosis open in its own window, the quoted passage on screen at reading
size. Not a rendered slide — the file.

**Speaker note:** Read it aloud and do not editorialise it away. Then: *"I specified that
code. I adjudicated its gates. I approved its plan. I did not write a line of it, and an
instrument just told me where I would get lost in it."*

---

## Slide 6 — It cannot tell ceremony from consequence, and says so

**Status:** keep. This is the bridge into the close.

**Takeaway:** Asked directly, it refused to guess.

> My proxies cannot distinguish a gate on a trivial change from a gate on consequential
> work. They cannot, and I will not pretend otherwise.

- It could see the diffstat — one insertion, one deletion — and **refused to use it**,
  because diff size measures the change rather than the adjudication and would be *"a
  precise answer to the wrong question"*.
- **One unconfigured parameter decides its own verdict.** Scoped to the branch, decision
  volume is 7 and nothing fires; across all branches it is 8 and the advisory fires.
  *"Someone else applying the shipped method could reasonably get the opposite answer."*
- **It cannot see who decided.** *"Git records who committed, not who decided; an
  agent-produced disposition committed by a human and a human-reasoned disposition are
  byte-identical in the log."*

**Visual:** the three admissions as the sentinel volunteered them. No alarm styling.

**Speaker note:** The last one lands directly on Level 4's bent guardrail — the instrument
built to watch the human cannot tell whether the human did the deciding. This is the
handover into the close: nothing in five levels answers *was this the mode the work
deserved?*, and the one thing that comes closest says so in its own output. Terminal off
from here.

---

## Slide 7 — The descent

**Status:** **UNCUTTABLE.** Terminal off. One prepared view — do not run it live.

**Takeaway:** Twelve characters nobody can see.

Prototype text in a Thymeleaf template, replaced at runtime. The rendered page is
byte-identical before and after.

| | Just make it | Through the habitat |
|---|---|---|
| Wall clock | under a minute | **45 minutes** |
| Cost | **$0.00** | **$52.16** at API list price · **£0.00** actually billed (Max subscription) |
| Tokens | 0 | **74,258,121** |
| Gates requiring a human | 0 | **4** |
| Agents | 0 | **9** |
| Lines of artefact | 0 | **1,457** |

- **69% of the bill is cache reads** — each stage re-loading what the previous stages
  wrote. Output is 9%. The pipeline's cost is dominated by carrying its own record, and
  that is indifferent to the size of the change underneath it.

**Visual:** the table, large. This is the photographed slide.

**Speaker note:** Quote both money figures or neither — the list price without the billed
figure is dishonest on a subscription, and the billed figure alone hides the real
resource. Let it land, and let the room laugh. Then take the laugh away on the next slide.

---

## Slide 8 — Every one of them found something true

**Status:** **UNCUTTABLE.** This is the move the close depends on.

**Takeaway:** None of them padded.

Every stage was told, explicitly, that returning nothing was a valid and publishable
result, and that padding would corrupt the measurement. Six findings:

- a **coverage gap** — the tests assert `view().name()` only, so a deleted `th:text`
  renders cleanly and passes
- a **false claim in a harness-gated artefact** — the decision record was about to assert
  no judgement calls were made; one had been
- a **falsified scope claim** — "the one other instance" is five, under the spec's own
  standard, verified twice
- a **standard colliding with a directive** — byte-equality requires unwrapping lines
  directive 9 protects
- a **standard with no defined value at all** — found only by the last stage, because
  every earlier sweep shared the same blind definition
- an **unasked question** — it is an upstream file; fix it upstream and every fork gets it

- **A reviewer would want all six.** The ceremony was proportionate. The findings were
  genuine.

**Visual:** the six findings as a plain list, in the same weight as the numbers on the
previous slide.

**Speaker note:** This is not mockery of the tooling and must not sound like it. Every
agent behaved impeccably; every finding is one a reviewer would want. The finding is that
**the habitat has one setting and the work does not.** If the room leaves thinking the
pipeline is stupid, the close has failed.

---

## Slide 9 — The turn

**Status:** **UNCUTTABLE.**

**Takeaway:** Not a ladder. A repertoire.

- So the answer is **not to build the habitat higher.**
- Level 5 is not where anyone should be trying to live. **A habitat that can only work at
  its highest setting is a badly tuned habitat.**
- The levels are what a team *can* do. They were never a target.

**Visual:** the ladder motif, present in every deck since Level 1, leaving the screen. No
replacement graphic, no announcement.

**Speaker note:** Do not deliver this as an apology. Four levels of evidence earned the
right to say the fifth is not a destination. *"What we have been building is not a ladder
to the top. It is a repertoire — and the skill is choosing the mode the work deserves."*

---

## Slide 10 — The question

**Status:** **UNCUTTABLE.** The last idea in the room.

**Takeaway:** If this turns out to be wrong, what does it take to put right?

**Undo · Repair · Migrate · Answer**

| Class | What putting it right takes |
|---|---|
| **Undo** | A revert. Nothing moved, nobody acted on it. |
| **Repair** | Fix forward. Live but bounded, nothing to reconcile. |
| **Migrate** | State has already moved. Moving it back, everywhere it landed. |
| **Answer** | Someone outside the team was told something. No code puts that right. |

- **The typo → Undo.** It got four human gates, nine agents and forty-five minutes.
- **The booking feature → Migrate.** `NOT NULL` in three dialects, rows rewritten,
  packages coupled. Reversal is a planned migration.
- **And also → Answer.** The owner's page shows a named clinician and a time beside "your
  visit has been booked", and nothing in the clinic agreed to either. Someone has to be
  told.
- **The class that picks the mode is the most expensive one the work touches.**

**Visual:** the question at the top, at title size; the four classes beneath it; the two
tasks placed against them. This is the second photographed slide.

**Speaker note:** *"Ask only 'can I revert it?' and you get Migrate — true, and not the
whole answer."* Then the line that closes the argument: *"The question I should have asked
about the typo took ten seconds. I asked it after the fact, from the transcript, for
forty-five minutes."* Not a rubric, not a scoring model — ten seconds, out loud, before
the work.

---

## Slide 11 — The repository

**Status:** **UNCUTTABLE.** The last thing on screen.

**Takeaway:** Every level is a branch, including the parts that do not flatter me.

```
github.com/Habitat-Thinking/petclinic-loops-at-levels
```

- The assessments, the diagnosis, the sentinel readings, the cost snapshots, the seventeen
  decisions still sitting `pending`.

**Visual:** the URL alone, large. No logo, no plugin name, no adoption slide, no summary
of the talk.

**Speaker note:** *"Go and look at the one below where you are. Then the one above. Then
ask the question."* Then stop. Do not end on the plugin, on tooling, or on a call to adopt
anything — the last idea in the room is the question on the previous slide, and the last
thing on screen is this URL.
