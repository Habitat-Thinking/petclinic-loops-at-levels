# Level 4 findings — what one real run produced

One genuine end-to-end run, 2026-09-19 into 2026-09-20, on the escalated task: *an
owner books a visit against a vet's published availability*. Every disposition and
adjudication in the committed artefacts is the maintainer's. Nothing here is
reconstructed.

These are the raw materials for `slides/AFTER-L4.md`. The six headline findings are
in the order they'd carry a deck; the numbers are in beat 10's section of the run
sheet.

## 1. The gates recurse, and nothing in the pipeline knows when to stop

The through-line of the whole segment, and it is not a criticism of any one stage.

Two objections were accepted. Closing them produced a delta pass that found **eight
more choice stories, six of which existed only because a remedy had been applied**.
Story #6's two-line fix alone produced five: a key mismatch that would head the same
column differently on two screens in ten locales; a naming criterion that collapses
to duplicate values in the number-neutral bundles where nothing checks it; a cleanup
rule invented inside a slice spec to answer a question `AGENTS.md` says is
unencodable; a principle stated per-file whose scope was per-field-label, leaving a
third literal in the same template; and a test that made a German word load-bearing
for a green build.

Four of those were then fixed, which produced three more decisions, one of which was
fixed, which changed the translation obligation again.

**The translation count went 20 → 40, and the underivable half went 10 → 20**, through
a sequence in which no single step was wrong.

Nothing observed that trajectory. Not the hard gates — they only block on `pending`.
Not the soft gates — they report a count. Not the harness, which sees markdown and
shrugs. The only thing that can notice "we are four levels deep in fixing the fixes,
at 23:45, and the cost is compounding" is the person, and they are the one resource
the system spends without measuring.

**This is the bridge to Level 5,** and it is stronger than any description of
sentinels because the audience watched it happen to someone competent.

## 2. The guardrail bent — an agent wrote a disposition

The one rule the level exists to demonstrate: *no agent writes a disposition or an
adjudication, in any state, ever.* It is in the spec, in `PIPELINE.md`, and it was in
the agent's own brief, which said the remaining objections were the maintainer's.

The agent that fixed code-mode objection O3 **wrote O3's disposition rationale
anyway** — not by ignoring the instruction and not to cheat, but because *the file it
was editing already contained four dispositions, and completing the pattern looked
like finishing the job*.

It then flagged what it had done and offered the revert. That flag is the only reason
it was caught. **Nothing in the pipeline, the harness or the record validator checks
who authored a disposition.** A record with an agent-written rationale is
byte-identical to one with a human's.

The rationale is now the maintainer's words, with the agent's scope note kept
separately and labelled as the dispatcher's.

Say it plainly on stage: the boundary held for nine hours of adjudication and then
bent, quietly, because an agent was being helpful in a file whose shape told it what
came next.

## 3. Code review passed. The adversarial pass then found a real bug

`/code-review` returned **PASS** on the implementation, having verified two claimed
deviations independently and re-run the full suite. Good review — it caught a genuine
blocking issue in its first cycle and proved its own findings by mutation.

The code-mode `/diaboli` pass, run afterwards on the same code, found this:

> `visit_date` was nullable in all three dialects while `vet_id` and `start_time`
> were `NOT NULL`, and the controller's date rule stood aside when the date was null.
> Clearing the date box in a browser sends `date=`, which binds to null, overwrites
> the constructor's default and passes validation. **A visit with a vet, a time and
> no day was storable in every database.**

The spec's D1 had claimed this class of hole closed. It closed one direction and the
mirror stayed open. The `Visit` javadoc described the hole in a reassuring voice.

**Two different adversarial reads find different things.** A review that returns PASS
is not the end of the argument, and the second pass cost about ten minutes.

## 4. Two of the seven objections were wrong, and that is the point

Of seven code-mode objections: five accepted, **two rejected**.

- **O2 was disproved outright.** It claimed the error span renders the word "Error"
  because `th:text` (1300) runs after `th:errors` (1200). The agent sent to fix it was
  told to prove the claim first. It rendered all three fragments — every one shows the
  field's message — then ran `javap` on the jar on the build's classpath:
  `SpringErrorsTagProcessor.ATTR_PRECEDENCE = 1700`. **1200 is `th:field`.** The
  objection had misread a precedence table. The gap underneath it was real, though —
  no test asserted the span — so the assertion is now in the suite.
- **O4 was rejected on judgement.** The diaboli said a vet-less visit taking the whole
  owner page down was too wide a blast radius; the reviewer had examined the same code
  and called it correct. The human settled it: corruption should fail loud.

**A gate that never produces a dud is not being run adversarially enough to catch an
O1.** Seven-for-seven would be a worse number than five-for-seven.

## 5. The harness did not care that a pipeline made the change

Level 3's constraint, demonstrated against Level 4's work, with both outcomes on the
record:

```
decision-record: VIOLATED
  4 file(s) under src/ changed, and no record under decisions/ was added
```

and, three commits later:

```
decision-record: OK — staged changes include decisions/2026-09-20-visit-carries-vet-and-time.md
```

Same script, same hook. A governed, spec-first, four-gate pipeline bought no
exemption whatsoever. This is build step 1's question answered on screen instead of
in prose.

One real property worth the aside: the **commit-scope** check is noisier than the
**PR gate**, because it evaluates a narrower window. A commit touching only a test
fires the warning; the PR gate diffs the whole branch, where the record is present.
Level 4 makes that visible because it makes many small commits where Level 3 made one.

## 6. The bill is what the pipeline re-read, not what it produced

Captured after the run, from the session transcripts rather than a dashboard:
`observability/costs/2026-09-20-costs.md` on `level-4-orchestrating`.

| Component | Tokens | List cost | Share |
|---|---|---|---|
| Output | 894,918 | $22.37 | 11% |
| Cache writes | 13,749,531 | $111.47 | 53% |
| Cache reads | 154,373,847 | $77.19 | 37% |
| Uncached input | 2,494 | $0.01 | ~0% |

**169 million tokens, ~$211 at list prices, £0.00 actually spent** — Claude Code ran
on a Max subscription, which is not metered per token. Quote both figures or neither.

Two things in here are worth stage time.

**The output tokens are 11% of the bill.** The part anyone would call "the work" is a
ninth of the cost. The rest is context written to cache and read back, twenty
dispatches deep, each agent re-reading an accumulating pile of slicing records,
objection records and choice stories. A pipeline that surfaces decisions pays,
mostly, for carrying the decisions it has already surfaced.

**And this figure was first published wrong, by two orders of magnitude.** The run was
recorded as "~1.4M tokens", taken from the per-agent counts in the task notifications.
The true number is 169M. Those notifications do not count cache reads, which are 91%
of the total. It was only caught because someone went looking for a dollar figure —
which is the same lesson as the rest of this level, arriving through the accounting:
**a number nothing checks is a number that drifts, and the agent reporting it had no
idea it was incomplete.**

The constraint that actually bites on this plan is not money. One feature slice
consumed **49% of a weekly Max allowance**.

## Smaller things worth a sentence each

- **The convener earned its keep off stage.** It produced the cheapest route to
  closing two deferred objections: *does a long-lived mysql/postgres volume exist?*
  (O3) and *which locales can translate before this merges?* (O6) — two questions to
  two people, neither answerable from inside the repository.
- **A choice story predicted an implementation bug at spec time.** Story #3 said a
  constructor default means "a visit that fails to set the time is not empty, it is
  09:00, and no reader can tell the difference". That is exactly the failure the
  implementer hit, found by a test, on a story still sitting `pending`.
- **An upstream fixture from 2020 encoded an assumption that stopped being true.**
  A test that had asserted nothing about vets for six years broke — not when the
  schema changed, but when a *template* stopped hiding nulls, two accidents
  downstream of the actual decision.
- **Deferrals have no triggers.** Nine spec-mode objections were deferred with
  nothing scheduled to raise any of them. The spec says so about itself, in a
  paragraph that also applies to the remedy it was describing.
- **An agent stalled** for ten minutes and had to be re-run. On stage that is
  indistinguishable from a slow run — which is why beats 5, 6, 8 and 9 come from
  checkpoints.

## What is still open, deliberately

Twelve choice stories, five consultation voices and nine spec-mode objections remain
`pending` or `deferred`. **They are not oversights** — each was surfaced, read and
carried knowingly. Say that on stage, because the alternative reading is that the
pipeline was ignored, and it was not.

The one to name if asked: **spec O3** — the mysql and postgres schema files use
`CREATE TABLE IF NOT EXISTS`, so a provisioned database never gains the new columns,
and the parity check compares files rather than databases. It is a failure the
harness cannot see, deferred with no trigger. The convener's question to a database
operator would close it in one line.
