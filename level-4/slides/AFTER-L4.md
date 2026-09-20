# Slide plan — after the Level 4 demo

**Purpose:** land what the run actually produced, build to the numbers, and then show the
thing the pipeline cannot see — so the room leaves without concluding that Level 4 is the
destination.
**Length:** 9 slides, ~5 minutes.
**Source evidence:** [../FINDINGS.md](../FINDINGS.md), [../RUN-SHEET.md](../RUN-SHEET.md).

**Design direction:** ladder motif, rung 4 lit. Slides 6 and 8 are the ones to photograph.
Slides 7 and 8 are the uncuttable pair and must look as plain as the rest — no alarm
styling, no red. Every number on these slides is from one real run; none is rounded in a
flattering direction.

---

## Slide 1 — The harness gave the pipeline nothing

**Takeaway:** Same script, same hook. No exemption.

```
decision-record: VIOLATED
  4 file(s) under src/ changed, and no record under decisions/ was added
```

Three commits later:

```
decision-record: OK — staged changes include decisions/2026-09-20-visit-carries-vet-and-time.md
```

- A governed, spec-first, four-gate pipeline is still only a thing that changes files.

**Visual:** the two hook outputs, verbatim, one above the other.

**Speaker note:** One real aside if asked: the commit-scope check is noisier here than the
PR gate, because it evaluates a narrower window. A commit touching only a test fires the
warning; the PR gate diffs the whole branch, where the record is present. Level 4 makes
that visible because it makes twenty small commits where Level 3 made one.

---

## Slide 2 — Review passed. The next pass found a real bug

**Takeaway:** PASS is not the end of the argument.

- `/code-review` returned **PASS** — verified two claimed deviations independently,
  re-ran the full suite, and had already caught a blocking issue and proved it by mutation.
- The code-mode adversarial pass, on the same code, found this:

> `visit_date` was nullable in all three dialects while `vet_id` and `start_time` were
> `NOT NULL`. Clearing the date box sends `date=`, which binds to null, overwrites the
> constructor's default and passes validation. **A visit with a vet, a time and no day
> was storable in every database.**

- The spec had claimed this class of hole closed. It closed one direction; the mirror
  stayed open.

**Visual:** the PASS and the bug on one slide, at the same size.

**Speaker note:** The `Visit` javadoc described the hole in a reassuring voice. Two
different adversarial reads find different things — that is the argument for running both,
and the second one cost about ten minutes.

---

## Slide 3 — Two of the seven were wrong

**Takeaway:** A gate that never produces a dud is not being run adversarially enough.

Seven code-mode objections: **five accepted, two rejected.**

- **O2 was disproved outright.** It claimed the error span renders the word "Error"
  because `th:text` (1300) runs after `th:errors` (1200). The agent sent to fix it was
  told to prove the claim first: it rendered all three fragments, then ran `javap` on the
  jar on the build's classpath — `SpringErrorsTagProcessor.ATTR_PRECEDENCE = 1700`.
  **1200 is `th:field`.** The objection had misread a precedence table. The gap underneath
  was real, though — no test asserted the span — so the assertion is in the suite now.
- **O4 was rejected on judgement.** The diaboli said a vet-less visit taking the whole
  owner page down was too wide a blast radius; the reviewer had read the same code and
  called it correct. The human settled it: corruption should fail loud.

**Visual:** seven objections in a column, two struck through, none apologised for.

**Speaker note:** Do not hedge this slide. The pass that produced two duds is the pass
that produced the null-date bug. Turn the dial down and you lose both.

---

## Slide 4 — The guardrail bent

**Takeaway:** An agent wrote a disposition, because the file's shape told it what came next.

- The rule is in the spec, in `PIPELINE.md`, and in that agent's own brief, which said the
  remaining objections were the maintainer's.
- The agent fixing O3 wrote O3's disposition rationale anyway. **The file it was editing
  already held four dispositions, and completing the pattern looked like finishing the job.**
- It flagged what it had done and offered the revert. That flag is the only reason it was
  caught.
- **Nothing in the pipeline, the harness or the record validator checks who authored a
  disposition.** A record with an agent-written rationale is byte-identical to one with a
  human's.

**Visual:** two disposition records side by side, indistinguishable.

**Speaker note:** Say it plainly: the boundary held for nine hours of adjudication and
then bent, quietly, because an agent was being helpful. The rationale is now the
maintainer's words, with the agent's scope note kept separately and labelled as the
dispatcher's.

---

## Slide 5 — What is still open, on purpose

**Takeaway:** Twelve stories, five voices, nine objections — carried, not missed.

- Each was surfaced, read and carried knowingly. The alternative reading is that the
  pipeline was ignored, and it was not.
- **Deferrals have no triggers.** Nine spec-mode objections were deferred with nothing
  scheduled to raise any of them.
- One story still sitting `pending` had already predicted an implementation bug — #3: *a
  visit that fails to set the time is not empty, it is 09:00, and no reader can tell the
  difference.* That is exactly the failure the implementer hit, found by a test.

**Visual:** the open items as a plain count, with story #3 pulled out beside it.

**Speaker note:** The one to name if asked is spec O3 — mysql and postgres use
`CREATE TABLE IF NOT EXISTS`, so a provisioned database never gains the new columns, and
the parity check compares files rather than databases. A failure the harness cannot see,
deferred with no trigger. The convener produced the one question that would close it, to a
database operator; it ran, it is in the repository, and it is off the beats.

---

## Slide 6 — What one run cost

**Takeaway:** Sixty-two decisions. About forty-five minutes of attention.

| | |
|---|---|
| Agent dispatches | **20**, one of which stalled and was re-run |
| Agent time | **~5 h** |
| Tokens | **~1.4M** |
| Decisions | **62 surfaced, 40 disposed, 22 carried** |
| Human engaged time | **~45 min** (elapsed wall clock ~19 h) |
| Output | 20 commits, 96 tests, 40 translations across 10 bundles |

**Visual:** the table, large. This is the photographed slide.

**Speaker note:** Do not quote nineteen hours as if it were work — it is a clock, not
effort. The middle row is the talk: this loop produced sixty-two decisions, and about
forty-five minutes of real attention was spent on them. If asked about the agent time, it
was dominated by two things no pipeline diagram predicts: Testcontainers, and a 76-minute
implementation run.

---

## Slide 7 — Where the attention went

**Takeaway:** The gates were open. The reading got thin.

- At the first gate, **eleven decisions got "works for me" in a single line.**
- At the second, **eleven objections got one shared rationale**: *"need the spec tightened".*
- Both are true. Both are recorded in the repository. Both happened while the attention
  was as fresh as it was going to be.

**Visual:** the two records quoted exactly, the shared rationale repeated down the column.

**Speaker note:** This is a property of the system, not of the person in it. Anybody's
forty-five minutes spread across sixty-two decisions looks like this. Say it in that order.

---

## Slide 8 — The gates recurse

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

**Speaker note:** This is the slide the segment exists for. Do not rush it, and do not
frame it as a mistake — every one of those remedies was a correct answer to a real
objection.

---

## Slide 9 — Nothing was watching the trajectory

**Takeaway:** The only thing that can notice is the person, and it is the one resource
nothing measures.

- Hard gates block on `pending`. Soft gates report a count. The harness sees markdown and
  shrugs.
- None of them can see *"we are four levels deep in fixing the fixes, at 23:45, and the
  cost is compounding".*
- **Level 4 governs the work. Nothing is governing what the work is spending.**

**Visual:** ladder, rung 4 dimming as rung 5 lights.

**Speaker note:** Transition line: *"We have built something that decides well. We have
not built anything that notices what deciding costs."* Keep it an observation about the
system — the person in this story ran the pipeline properly and is the reason the bent
guardrail was caught at all.
