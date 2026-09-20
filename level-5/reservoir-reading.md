# Reservoir reading — captured verbatim, 2026-09-20

Run against the real state at the end of the genuine Level 4 run, on
`level-5-supervising`, with the `## Cognitive reservoir` block active in
`HARNESS.md` at the shipped defaults.

**Nothing below is edited.** The agent has no Write or Edit tool, so it wrote
nothing to disk; this file is its output pasted whole. The maintainer's response
is in a separate section at the end, and is his own words.

One thing worth noticing before reading it: the dispatch briefing told this agent the
time was "early afternoon". It ran `date`, found 17:09, and reported the clock rather
than the briefing — then listed the discrepancy as one of three things it would not
tidy away. A sentinel that accepts its briefer's version of reality is not watching
anything.

---

## Reservoir read — 2026-09-20 17:09 BST

Repository: `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels`
Branch: `level-5-supervising`
Config source: `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/HARNESS.md` → `## Cognitive reservoir` (shipped defaults, unmodified)

Window: last 8 h (since ≈09:09 BST, 2026-09-20)

| Proxy | Value | Threshold | Flag | Crossed? |
| --- | --- | --- | --- | --- |
| Continuous span | 185 min (10:10 → 13:15) | 180 min | observed | yes |
| Decision volume | 6 | 8 | observed | no |
| Context switches | 4 | 4 | observed | yes (at threshold, not above) |
| Wall-clock hour | 17 (unverified — no chronotype declared) | — | asked | — |

### What the counts are made of (observed)

Six commits in the window, all on `level-4-orchestrating`:

```
10:10 d943593  Review cycle 1: guard the second rejection, drop the false null-safety
10:16 49efea8  Pin the vet guard with the assertion that was missing
10:29 324daff  Code-mode objection record: seven objections against the built code
12:25 5ceee2c  Fix O1: a visit could be saved with no date
12:48 b20868a  Integration gate: all seven code-mode objections disposed
13:15 2e242a2  Cost snapshot: 2026-09-20
```

Context switches = 4 distinct top-level directories touched: `decisions`, `docs`, `observability`, `src`. One branch switch is also in the window — `checkout: moving from level-4-orchestrating to level-5-supervising` at 17:08 — which I did **not** add to the count, to avoid double-counting streams already represented by the directories. Counted additively the value would be 5; I report the conservative 4.

### Three things I will not tidy away

1. **The span crossing is thin and the measurement is contestable.** 185 min against a 180 min threshold is a 5-minute margin. More importantly, the window's commits contain a **116-minute gap** (10:29 → 12:25). No idle cut is configured in HARNESS.md, so the shipped method — first commit to last commit — treats that gap as continuous. Under any idle cut below 116 minutes the span decomposes into 19 min and 50 min, and **nothing would cross**. The span proxy is firing on a single unconfigured parameter. That is a fact about the instrument, not about the maintainer.
2. **The span proxy may be counting elapsed time, not time-on-task.** You report ~45 minutes of engaged decision-making inside a 19-hour Level 4 run. `asked` — I cannot observe it. If it holds, the vigilance-decrement basis, which is a *time-on-task* effect, does not straightforwardly apply to 185 minutes of wall clock that were largely agent-elapsed. I record the crossing as the method produces it and record the defeater alongside it rather than suppressing either.
3. **The clock reads 17:09 BST, not early afternoon.** The briefing gave early afternoon; `date` gives 17:09. I report the clock. With no `chronotype` declared, hour 17 gets no circadian band — it is `asked`/unverified, not a finding.

Also observed and outside the window, so excluded from every count above: commits at **00:02** and **01:40** on 2026-09-20 (`RED`/`GREEN` for S2). Whether the hours between them and 10:10 were rest is `asked`, not observed. An 8-hour window starting at 09:09 cannot see them; that is a limit of the configured instrument, which a reading of it should state.

### Inferred risk (defeasible)

Four distinct streams touched in eight hours — decision records, docs, observability and source — sits at the configured switching threshold, and **task-switching cost / attention residue** (Leroy, 2009) scales with the switching itself, so the next decision after each move carries residue from the last. The 185-minute span nominally engages the **vigilance decrement**, but as noted above that inference is materially weakened by the 116-minute gap inside it and by the reported 45-minute engaged figure; I hold it as fired-but-contested rather than as risk established. Decision volume, the proxy for sustained decision load, did **not** cross — six approvals against a threshold of eight.

Reported but **not a proxy** and therefore changing nothing above: 62 decisions surfaced, 40 disposed, 22 carried as pending or deferred. `asked`. The reservoir mechanism counts four things and this is not one of them; I note it because it was given, not because it contributes to a threshold. There is no combined score here, and there will not be one.

### Recommendation

One threshold is clearly at its line (context switches) and one is marginally over on a contestable measurement (span), so under the disjunctive rule the advisory fires. Once:

Decide your stop **before the next session begins**, while the judgment making that decision is still the kind you would trust. Do not negotiate the boundary with your tired self — and Level 5 has just been checked out at 17:08, so that next session is the one being decided now.

One concrete, time-boxed option: **tomorrow morning, in a 20-minute box before Level 5 work starts, re-review the two approvals that closed this window — `5ceee2c` (Fix O1: a visit could be saved with no date) and `b20868a` (integration gate, all seven code-mode objections disposed).** Those are the two points in the window where you said *yes* to code and to a gate, and they sit on the far side of the 116-minute gap and the fourth stream switch.

The choice to continue is yours.

---

Nothing was written to disk. No Write or Edit tool is available to this agent by design; no record of this reading, of any break, or of any chronotype exists anywhere. Tune mode was not requested, so no HARNESS.md edits are proposed — if you want the span proxy to stop firing on gaps of this kind, that is a conversation about an idle cut, and it is yours to make in `HARNESS.md` by hand.

---

## What the maintainer said

**Agreed entirely. Stopped there, and resumed the next day.**

Recorded on 2026-09-20 at 17:15 BST, six minutes after the reading, as the answer to
two questions put separately: whether he agreed with it, and what he actually did.

Worth being precise about what that means for the talk, because it is the *less*
theatrical outcome and it is the true one. The sentinel argued against its own
finding in three places — the five-minute margin, the 116-minute gap that any idle
cut would have split, the possibility that it was measuring elapsed rather than
engaged time. It offered every reason to wave it through. The maintainer agreed with
it anyway and stopped.

So this artefact is **not** the override case. Build step 6 needs a sentinel being
declined, and this was a sentinel being taken.

### Where the override actually is

Two real cases already exist in the ladder, both from the Level 4 run, both with the
maintainer's reasoning recorded beside the advice at the time it was given:

- **Code-mode objection O4**, in `docs/superpowers/objections/visit-carries-vet-and-time-code.md`
  on `level-4-orchestrating`. The adversarial reviewer advised that a vet-less visit
  taking down the whole owner page was too wide a blast radius. The maintainer
  **rejected** it: *"Fail loud, the reviewer is right. vet_id is NOT NULL in three
  dialects... A vet-less visit is corruption, and a blank cell would hide it."* Two
  agents disagreed with each other and a human settled it against one of them.
- **The carpaccio's sequencing recommendation**, in
  `docs/superpowers/slices/owner-books-visit-against-vet-availability.md`. It
  recommended shipping S1 first — "S1 is the foundation... S3, S4 and S5 all consume
  that answer" — and the maintainer progressed **S2** instead.

Both are stronger than a staged override would be, because neither was produced to
illustrate anything. Use O4 for the beat: the advice, the rejection, and the reason,
all committed within minutes of each other.
