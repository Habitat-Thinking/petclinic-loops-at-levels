# Run sheet — Level 4: Orchestrating (10–12 minutes)

The largest segment in the talk, and the most dangerous one. It is where the room is
most likely to conclude that Level 4 is the destination. Beat 10 is what stops that,
and **beat 10 is not cuttable**. If you are over time, cut beats 6 and 9.

## Profile

Same as Levels 2 and 3 (`~/.claude-loops-at-levels-l2`). Nothing about the launch
changes. What changed is in the repository.

## The task changed, and say so

Levels 1–3 used "add an email address to owners". It was chosen because it had no
interesting decisions in it — which is exactly what made it a fair output comparison
and exactly what makes it useless here. Level 4 is about decisions, so the task had
to grow.

**The one sentence:** *the small task had no decisions in it; this level is about
decisions, so the task had to change.*

The escalated task is **an owner books a visit against a vet's published
availability**. Today `Visit` carries a pet, a bare `LocalDate` and a description —
no vet, no time of day — so it cannot be added as a column.

## Checkpoint branches

The full pipeline cannot run in stage time. Check out the state you need; do not wait
for a stage to run.

| Branch | State it holds |
|---|---|
| `l4-1-sliced` | Five slices, **every disposition `pending`**. The slice gate before a human touches it. |
| `l4-2-spec` | Dispositions written, S2 progressed, spec and plan produced. |
| `l4-3-objections` | Eleven objections, **all `pending`**. The objection gate before adjudication. |
| `l4-4-choices` | Objections adjudicated; ten choice stories and five consultation voices produced. |
| `l4-5-reviewed` | Implemented, 94 tests green, code review returned PASS. |
| `level-4-orchestrating` | The finished state: 96 tests, all seven code-mode objections disposed. |

Nothing is merged. PR #1 belongs to Level 3 and must stay open and red.

## Beats

1. **Announce the task change and why.** 30 s. The sentence above.
2. **Run `/carpaccio` live.** Rehearsed at 3 min 33 s of agent time. Have a cut point.
3. **Open the record, point at `pending`.** Every slice, waiting. The agent produced
   the slices and will not produce the dispositions.
4. **Disposition one slice, live.** Type it. Let it feel slow. This is the moment.
5. **Jump to `l4-2-spec`.** Do not watch a spec being written.
6. **Show the objections** from `l4-3-objections`. Each grounded in quoted spec text.
   *Cut this first if over time.*
7. **Adjudicate one objection live.** Then say it plainly: the point is not that
   objections get found, it is that a human engages with them.
8. **Show the choice stories.** Read one aloud that you would not have noticed.
   Story #10 — "every constraint this change buys is about a field being present,
   none about the value being true" — is the one.
9. **Jump to `l4-5-reviewed`,** show the review output. 30 s. *Cut this second.*
10. **The reckoning.** The numbers, then where you started skimming. **Uncuttable.**

## Beat 10 — the numbers, all real

- **20 agent dispatches**, one of which stalled and had to be re-run.
- **~5 h of agent time**, dominated by two things no pipeline diagram predicts:
  Testcontainers, and a 76-minute implementation run.
- **~1.4M tokens.**
- **62 decisions surfaced. 40 disposed. 22 carried knowingly.**
- **~45 minutes of human engaged time**, against ~19 hours of elapsed wall clock.
- **20 commits, 96 tests, 40 translations across 10 bundles.**

The honest framing is the middle number: *this loop produced sixty-two decisions and
about forty-five minutes of real attention was spent on them.* Do not quote the
elapsed wall clock as if it were work.

**Then say where the attention went.** At the first gate, eleven decisions got
"works for me" in a single line. At the second, eleven objections got one shared
rationale, "need the spec tightened". Both are true, both are recorded in the
repository, and both happened while the attention was as fresh as it was going to be.

## If the live `/carpaccio` wanders

`git switch l4-1-sliced` and say you have run it before. The rehearsed record is
what the audience sees either way.

## What not to do

- Do not merge anything. Do not open a pull request. PR #1 is Level 3's and is red on
  purpose.
- Do not show the convener. It ran, its record is in the repo, and it is off the
  beats deliberately.
- Do not claim the pipeline caught everything. Two of the seven code-mode objections
  were wrong, and that is in the notes because it is the stronger story.

## Slide decks

- `slides/BEFORE-L4.md` — before the demo.
- `slides/AFTER-L4.md` — after it. Built from [FINDINGS.md](FINDINGS.md), which holds
  the story this run actually produced.
