# Slide plan — the upper levels, standalone (20 minutes)

**Purpose:** show a room that has not seen Levels 1–3, and has no time for them, what a
governed pipeline and its sentinels actually do — and then argue that this is not where
anyone should be trying to live. This is not a condensed version of the full talk. It has
a narrower job and it ends on the same question.

**Length:** 18 slides, 20 minutes. Where we are 3 min (slides 1–3) · Level 4 7 min
(slides 4–9) · Level 5 5 min (slides 10–13) · the close 4 min (slides 14–18).

**Source evidence:** [../../level-3/FINDINGS.md](../../level-3/FINDINGS.md) (recap only),
[../../level-4/FINDINGS.md](../../level-4/FINDINGS.md),
[../../level-4/RUN-SHEET.md](../../level-4/RUN-SHEET.md),
[../../level-5/FINDINGS.md](../../level-5/FINDINGS.md),
[../../level-5/RUN-SHEET.md](../../level-5/RUN-SHEET.md),
[../../level-5/descent.md](../../level-5/descent.md),
[../../level-5/repertoire.md](../../level-5/repertoire.md).

**Design direction:** no ladder motif. This deck has no rungs to light, and a ladder drawn
for a room that never saw the bottom of it is a diagram of something they were not shown.
The visual thread is instead the record — files, counts and captured output, in the same
weight throughout, so that the close's numbers do not arrive looking like a different kind
of claim. Slides 6, 14 and 17 are the ones to photograph. Slide 18 is the URL and nothing
else. No alarm styling anywhere, and no red.

**Everything on screen is a checkpoint or a capture.** No agent is run live. At twenty
minutes there is no room for a stage that might take four. The single exception is slide
2, which is a deterministic shell script and takes seconds.

**The money rule:** $52.16 appears once, on slide 14, always paired with £0.00 actually
billed on a Max subscription. Quote both or neither, on screen and out loud.

## What to have checked out and open

No Claude Code session and no plugin launcher is needed for this deck. Nothing here
dispatches an agent, so the full talk's profile (`~/.claude-loops-at-levels-l2`) and the
`diagnostic-legibility` `--plugin-dir` are not required.

| Needs | Where | For |
|---|---|---|
| Worktree on `level-4-orchestrating`, clean tree | ladder | slides 1, 2, 4, 6, 7 |
| `git config core.hooksPath .githooks` set in that clone | ladder | slide 2 |
| `scripts/check-decision-record.sh` runnable | ladder | slide 2, run live |
| Worktree or second window on `l4-1-sliced` | ladder | slide 5 — records arriving `pending` |
| `docs/superpowers/` and `decisions/` present | ladder | slides 5, 6, 8 |
| Worktree on `demo-notes` | notes | slides 11, 12, 13, 14, 15 |
| `level-5/trust-boundary.txt` | demo-notes | slide 11 |
| `level-5/diagnosis.yaml`, open in a second window at reading size | demo-notes | slide 12 — read on screen, not from notes |
| `level-5/descent.md` figures on one prepared view | demo-notes | slide 14 — **do not run the typo live** |
| `level-5/repertoire.md` read beforehand, not on stage | demo-notes | slides 16, 17 — delivered with the terminal off |

If asked where the descent history is: `level-5-supervising-descent` at `ab236db`, with
`level-5/measure-descent.py` beside the figures. Not opened on stage.

## Cutting

Every slide carries a **Status:** line. Shed in this order and no other:

1. Slide 3 — Level 3's ceiling
2. Slide 5 — `pending` on a screen of its own
3. Slide 1 — the constraints table
4. Slide 11 — the sentinel's tool list

That is about two and a half minutes. **Slides 14–18 never cut.** If the clock is gone
after slide 13, go straight to slide 14 and say nothing about having skipped anything.

---

## Slide 1 — What is already in the room

**Status:** cuttable — **cut 4**. If it goes, open cold on slide 2 and let the capture
imply the harness.

**Takeaway:** A harness that says no, and means it.

This room starts three levels in. Below here: a project told what to do in prompts, then
in files. Then this.

| Constraint | Enforcement |
|---|---|
| Consistent formatting | deterministic — `spring-javaformat:validate` |
| Tests must pass | deterministic — `./mvnw verify` |
| Decision record, at commit | deterministic — `check-decision-record.sh`, warns |
| Decision record, at merge | deterministic — same script, blocks |
| Schema parity across databases | deterministic — `SchemaParityTest` |
| Abstraction earns its place | **agent — advisory**, no tool |

- Five enforced, one honestly advisory. **Three loops**: edit/commit advisory, merge-time
  strict, and a scheduled investigative sweep. Each verified to fire.

**Visual:** the table, and the three loops as three plain labels beneath it. No diagram of
a pipeline yet — there is not one.

**Speaker note:** Ninety seconds, and do not sell it. The room does not need the history,
it needs to believe the thing exists before the next slide shows it working.

---

## Slide 2 — Watch it catch something

**Status:** keep. **Run live** — it is a shell script and git, and it takes seconds. The
captured pair below is the fallback and looks identical.

**Takeaway:** Same script, same hook, no argument.

```
decision-record: VIOLATED
  4 file(s) under src/ changed, and no record under decisions/ was added
```

Three commits later:

```
decision-record: OK — staged changes include decisions/2026-09-20-visit-carries-vet-and-time.md
```

- Both of those are from a change made by the pipeline this talk is about to show.

**Visual:** the two outputs verbatim, one above the other, terminal type.

**Speaker note:** This is not scene-setting. Say the second sentence and leave it — the
line it earns arrives on slide 7, about four minutes from now, and it lands harder if the
room has already watched the check run. The one real aside if asked: the commit-scope
check is noisier than the merge gate because it evaluates a narrower window; a commit
touching only a test fires the warning, and the merge gate diffs the whole branch, where
the record is present.

---

## Slide 3 — The ceiling this hits

**Status:** cuttable — **cut 1**. The line works spoken over slide 2's last breath.

**Takeaway:** It bounds what may happen. It has no opinion about what should.

- The harness cannot tell you the field was worth adding, cannot choose between two
  designs, and cannot decide what to build next.
- **It says no.** That is the whole of it, and it is the reason there is a Level 4.

**Visual:** the two sentences, nothing else.

**Speaker note:** Thirty seconds. Do not turn it into a critique — five enforced
constraints doing exactly what they claim is not a failure, it is a boundary.

---

## Slide 4 — A pipeline in front of the work, and a person inside it

**Status:** keep.

**Takeaway:** The agent produces the decisions. It does not make them.

The feature: **an owner books a visit against a vet's published availability.** Today
`Visit` carries a pet, a bare `LocalDate` and a description — no vet, no time of day. It
cannot be added as a column.

| Hard gate | What the agent produces | What a human does |
|---|---|---|
| Slice adjudication | five slices, every choice left `pending` | disposes each one |
| Objection adjudication | objections, each grounded in quoted spec text | adjudicates |
| Plan approval | the spec, the plan, the counts still outstanding | approves, or sends it back |
| Integration approval | objections against the built code | disposes, before anything merges |

- Two **soft** gates alongside them — choice stories and consultation voices. They report
  a count and let the work past.
- The rule the level exists to demonstrate: **no agent writes a disposition or an
  adjudication, in any state, ever.**

**Visual:** four gates in a line, each with a gap in it that only a person fills. The two
soft gates drawn beside the line rather than on it.

**Speaker note:** Say the task out loud before the table. A room that thinks the feature
was chosen to flatter the pipeline will discount everything after it. Then warn them once
that we jump between checkpoint branches: the pipeline does not fit in twenty minutes and
pretending otherwise would be the one dishonest thing here.

---

## Slide 5 — `pending` is the whole gate

**Status:** cuttable — **cut 2**. Say it over slide 4's table instead, and show the
checkpoint only if the room looks sceptical.

**Takeaway:** A word in a file, and nothing proceeds.

From `l4-1-sliced` — the slice gate, before a human has touched it:

- Five slices. **Every disposition reads `pending`.** The agent produced all of them and
  will not fill any of them in.
- Hard gates **block** on `pending`. Soft gates **report a count**. That is the entire
  enforcement.

**Visual:** the slice record open at the disposition field, the word `pending` large and
repeated down the column.

**Speaker note:** Fifteen seconds on the screen. The thing worth saying is how cheap the
mechanism is — no engine, no state machine, a word in a markdown file that a person has
to change.

---

## Slide 6 — What four gates produced on one real feature

**Status:** keep. This is a photographed slide.

**Takeaway:** Forty-six dispositions. Seventeen still pending.

One end-to-end run, 19–20 September, on that feature:

| | |
|---|---|
| Decisions surfaced | **62** — 40 disposed, 22 carried knowingly |
| Dispositions across the four gates | **46**, of which **17 are still pending** |
| Human engaged time | **~45 min** (elapsed wall clock ~19 h) |
| Agent dispatches | **20**, one of which stalled and was re-run |
| Tokens | **169M** — 0.89M out, 13.7M cache write, 154.4M cache read |
| Cost | ~$211 at list price · £0.00 billed (Max subscription) |
| Output | 20 commits, 96 tests, 40 translations across 10 bundles |

And where that attention went:

- At the first gate, **eleven decisions got "works for me" in a single line.**
- At the second, **eleven objections got one shared rationale**: *"need the spec
  tightened"* — which the project's own assessor later called *"a bulk action wearing the
  costume of eleven judgements"*.

**Visual:** the table large, the two attention findings beneath it in the same weight.

**Speaker note:** The middle rows are the talk: this loop produced sixty-two decisions and
about forty-five minutes of real attention was spent on them. Do not quote nineteen hours
as if it were work — it is a clock, not effort. And say the last part as a property of the
system rather than of the person in it: anybody's forty-five minutes spread across
sixty-two decisions looks like this. The seventeen still `pending` are not oversights —
each was surfaced, read and carried.

---

## Slide 7 — The pipeline bought no exemption

**Status:** keep. Twenty seconds. This is the payoff of slide 2.

**Takeaway:** A governed, spec-first, four-gate pipeline is still only a thing that
changes files.

- Those two lines you watched four minutes ago were this pipeline's commits.
- **Same script, same hook, no special case.** Nothing about being a pipeline made the
  harness relax.

**Visual:** the `VIOLATED` / `OK` pair from slide 2, reduced, with the gate diagram from
slide 4 beside it at the same size.

**Speaker note:** Do not elaborate. The argument was made by the capture; this slide only
names it.

---

## Slide 8 — The gates recurse

**Status:** keep.

**Takeaway:** The translation obligation went 20 → 40. The underivable half went 10 → 20.

- Two objections accepted. Closing them produced a delta pass with **eight more choice
  stories, six of which existed only because a remedy had been applied.**
- **Story #6's two-line fix produced five on its own** — among them a key mismatch that
  would head the same column differently on two screens in ten locales, and a test that
  made a German word load-bearing for a green build.
- Four of those were fixed, which produced three more decisions; one of those was fixed,
  which changed the translation obligation again.
- **No single step was wrong.**

**Visual:** the translation count as a stair, each tread a remedy, climbing.

**Speaker note:** This is the slide the Level 4 section exists for. Do not rush it, and do
not frame it as a mistake — every one of those remedies was a correct answer to a real
objection raised by a gate working exactly as specified.

---

## Slide 9 — Nothing was watching the trajectory

**Status:** keep. This is the bridge into Level 5.

**Takeaway:** The only thing that can notice is the person, and it is the one resource
nothing measures.

- Hard gates block on `pending`. Soft gates report a count. The harness sees markdown and
  shrugs.
- None of them can see *"we are four levels deep in fixing the fixes, at 23:45, and the
  cost is compounding".*
- **The pipeline governs the work. Nothing is governing what the work is spending.**

**Visual:** the three mechanisms listed with what each one can see, and the sentence none
of them can produce, underneath.

**Speaker note:** The transition line: *"We have built something that decides well. We
have not built anything that notices what deciding costs."* Keep it an observation about
the system — the person in this story ran the pipeline properly.

---

## Slide 10 — What is being supervised changes

**Status:** keep. Forty-five seconds, and it is the framing for the whole section.

**Takeaway:** Everything so far supervised the code.

- Constraints, gates, tests, review, adversarial passes — all of it pointed at the
  artefact.
- **What needs supervising now is whether the person who signs it can still answer
  for it.**

**Visual:** the mechanisms from the last twelve minutes listed with their object of care —
code, code, code, code — and one more line naming a different one.

**Speaker note:** Do not soften this into developer wellbeing. It is about whether the
person who signs the work can still account for it. Then say once, plainly, that
everything from here is a committed artefact read on screen, so the room knows nothing is
being staged.

---

## Slide 11 — What a sentinel is

**Status:** cuttable — **cut 3**. If it goes, say the last bullet as one line and move.

**Takeaway:** It cannot change the thing it watches.

Same plugin, same file format, one line each:

```
reservoir-warden:  tools: [Read, Glob, Grep, Bash]
mast:              tools: [Read, Glob, Grep, Bash]

spec-writer:       tools: [Read, Write, Edit, Glob, Grep]
tdd-agent:         tools: [Read, Write, Edit, Glob, Grep, Bash]
integration-agent: tools: [Read, Write, Edit, Bash]
```

- No Write. No Edit. No Agent. **It cannot delegate to something that can.**

**Visual:** the excerpt verbatim, the absent capabilities the only thing emphasised.

**Speaker note:** Fifteen seconds, then move. Shown rather than claimed on purpose — a
sentinel that could edit the repository is a different animal wearing the same name. Do
not define the word; let the file do it.

---

## Slide 12 — An instrument read the code and found where I would get lost

**Status:** keep. Run-sheet beat 6 — read on screen from the file, not from notes.

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

**Visual:** `level-5/diagnosis.yaml` open in its own window, the quoted passage on screen
at reading size. Not a rendered slide — the file.

**Speaker note:** Read it aloud and do not editorialise it away. Then: *"I specified that
code. I adjudicated its gates. I approved its plan. I did not write a line of it, and an
instrument just told me where I would get lost in it."*

---

## Slide 13 — It cannot tell ceremony from consequence, and says so

**Status:** keep. This is the bridge into the close. Terminal off from here.

**Takeaway:** Asked directly, it refused to guess.

> My proxies cannot distinguish a gate on a trivial change from a gate on consequential
> work. They cannot, and I will not pretend otherwise.

- It could see the diffstat — one insertion, one deletion — and **refused to use it**,
  because diff size measures the change rather than the adjudication and would be *"a
  precise answer to the wrong question"*.
- **It cannot see who decided.** *"Git records who committed, not who decided; an
  agent-produced disposition committed by a human and a human-reasoned disposition are
  byte-identical in the log."*

**Visual:** the two admissions as the sentinel volunteered them. No alarm styling.

**Speaker note:** The instrument built to watch the human cannot tell whether the human
did the deciding. That is the handover: nothing shown in the last twenty minutes answers
*was this the mode the work deserved?*, and the one thing that comes closest says so in
its own output.

---

## Slide 14 — The descent

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

## Slide 15 — Every one of them found something true

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
pipeline is stupid, the close has failed and so has the whole twenty minutes.

---

## Slide 16 — The turn

**Status:** **UNCUTTABLE.**

**Takeaway:** Not a ladder. A repertoire.

- So the answer is **not to build the habitat higher.**
- What you have been watching for twenty minutes is not where anyone should be trying to
  live. **A habitat that can only work at its highest setting is a badly tuned habitat.**
- The levels are what a team *can* do. They were never a target.

**Visual:** blank but for the three lines. Nothing carried over from any earlier slide,
and no replacement diagram.

**Speaker note:** Do not deliver this as an apology, and do not hedge it — the last
eighteen minutes of evidence earned the right to say it. *"What we have been building is
not a ladder to the top. It is a repertoire, and the skill is choosing the mode the work
deserves."*

---

## Slide 17 — The question

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

## Slide 18 — The repository

**Status:** **UNCUTTABLE.** The last thing on screen.

**Takeaway:** Every level is a branch, including the parts that do not flatter me.

```
github.com/Habitat-Thinking/petclinic-loops-at-levels
```

- The checkpoints you have just seen, the assessments, the diagnosis, the sentinel
  readings, the cost snapshots, the seventeen decisions still sitting `pending`.

**Visual:** the URL alone, large. No logo, no plugin name, no adoption slide, no summary
of the twenty minutes.

**Speaker note:** *"Go and look at the one below where you are. Then the one above. Then
ask the question."* Then stop. Do not end on the plugin, on tooling, or on a call to adopt
anything — the last idea in the room is the question on the previous slide, and the last
thing on screen is this URL.
