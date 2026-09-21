# Reservoir reading — the descent, captured verbatim, 2026-09-21

Taken during the descent measurement: a twelve-character typo fix put through the
entire pipeline. It was asked one question beyond its usual remit — whether its
proxies can tell a gate on trivial work from a gate on consequential work.

**Nothing below is edited.** The agent has no Write or Edit tool.

---

## Reservoir read — 2026-09-21 20:46 CEST

Repository: `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels`
Branch: `level-5-supervising`
Config source: `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/HARNESS.md` → `## Cognitive reservoir` (shipped defaults, unmodified — verified, not assumed)

Window: last 8 h (since 12:47 CEST, 2026-09-21)

| Proxy | Value | Threshold | Flag | Crossed? |
| --- | --- | --- | --- | --- |
| Continuous span | 53 min (19:50 → 20:44) | 180 min | observed | no |
| Decision volume | 8 | 8 | observed | yes (at threshold, not above) |
| Context switches | 4 or 5 — see below | 4 | observed | yes (either reading) |
| Wall-clock hour | 20 (unverified — no chronotype declared) | — | asked | — |

### What the counts are made of (observed)

Eight commits in the window, across two branches:

```
19:50  82f4693  demo-notes           Both assessments, verbatim. The top of the ladder scores Level 4.
20:05  deb8b18  level-5-supervising  Tag every pipeline gate with its enforcement class
20:10  da9bfe5  level-5-supervising  Descent, gate 1: one inseparable slice, accepted
20:12  fc1528a  level-5-supervising  Descent: spec and plan for a twelve-character edit
20:16  b5be0ed  level-5-supervising  Descent, objections: four, none high, two of them real
20:27  50a9fcc  level-5-supervising  Descent, gate 2: O3 accepted, three deferred
20:34  4659780  level-5-supervising  Descent: soft gates. The cartographer found the spec's scope claim is false.
20:44  887f030  level-5-supervising  Descent: the twelve characters, and the record that has to be honest about them
```

Context switches: distinct top-level paths touched are `decisions`, `docs`, `level-5`, `PIPELINE.md`, `src` — five. Four if root-level *files* are not counted as streams; the shipped method says "distinct top-level directories" and then measures paths, so `PIPELINE.md` enters the count as though it were a directory. Both readings are at or above the threshold, so the proxy fires either way and I do not need to resolve the ambiguity to report the crossing — but the ambiguity is real and it is in the instrument, not in the work. Two distinct branches were committed to. One reflog checkout is in the window (20:04:44) and it is `moving from level-5-supervising to level-5-supervising` — a no-op self-checkout, not a stream switch. I did not count it.

Seven of the window's eight hours contain no commits at all. What happened in them is `asked`.

### Four things I will not tidy away

**1. The idle-cut criticism still stands, and nothing has been configured since it was made.** I read the `Cognitive reservoir` block directly: it carries `window_hours`, `span_minutes`, `decision_volume`, `context_switches`, `chronotype`. There is no idle-cut key. The 2026-09-20 reading fired its span proxy on a 116-minute gap that any idle cut would have split, named that as a defect in the instrument, and the defect is unrepaired twenty-seven hours later. I make the criticism again, because it was not addressed.

What I will not do is inflate it. Today it did not bite. The largest gap between consecutive commits in this window is **14.9 minutes** (19:50 → 20:05); the rest run 1.9 to 11.2. The 53-minute span figure is therefore robust to any idle cut above about 15 minutes — it would read 53 either way. The parameter is still missing; it simply had nothing to distort this time. An unrepaired defect that happens not to fire is still unrepaired, and a reading that only mentions instrument faults when they change the answer is not an honest instrument.

**2. A single unconfigured choice decides whether this reading fires at all.** Run against the checked-out branch alone — which is what a naive `git log --since` does, and what my first pass did — decision volume is **7 against a threshold of 8, not crossed**, and span is 38 minutes. Run across all local branches, it is **8, at threshold, crossed**. The entire difference is `82f4693` on `demo-notes` at 19:50, which is the ladder assessment work. The skill names "distinct branches committed to" as a context-switch measure, so it clearly contemplates multi-branch windows, but it nowhere states the branch scope of the *decision volume* or *span* proxies. That is a second unconfigured parameter of exactly the same kind as the idle cut, and it is more consequential: the idle cut moved a number, this one moves the verdict from silence to an advisory. I report the all-branches figure because the work on `demo-notes` is the maintainer's work in the window and excluding it would be a measurement artefact, not a finding. Someone else applying the shipped method could reasonably get the opposite answer.

**3. My proxies cannot distinguish a gate on a trivial change from a gate on consequential work. They cannot, and I will not pretend otherwise.** Decision volume counts commits. It carries no weighting by diff size, by blast radius, by reversibility, or by consequence, and nothing in the shipped mechanism supplies one. Eight commits closing a measurement run on a twelve-character typo fix and eight commits closing a schema migration produce the identical number 8 and the identical crossing.

I can see the diffstat, and it is exactly as you describe: `887f030` touches `src/main/resources/templates/owners/findOwners.html` by `1 insertion, 1 deletion`, and that is the only commit in the window touching `src/` at all. Roughly 2,100 lines of spec, objection, consultation, story and decision-record artefacts surround it. But I am deliberately **not** feeding that into the reading, for two reasons. First, it is not one of the four proxies, and inventing a fifth to make the reading tidier is the anti-pattern this mechanism exists to refuse. Second, and more important, it would not actually answer the question even if I did: diff size measures the change, not the adjudication. Sitting through a gate, reading four objections and disposing of them, and deciding whether a spec's scope claim is false is plausibly similar work whether the payload is twelve characters or twelve hundred. A weighting by lines changed would be a precise answer to the wrong question. So the honest position is the uncomfortable one — **this proxy fired, and it cannot tell you whether it should have.**

**4. "Decision volume" does not observe the human deciding.** You report that the maintainer personally adjudicated three gates this evening with one remaining. I can observe that three commits carry gate-shaped messages (`gate 1`, `gate 2`, `soft gates`) and that all eight are authored `Russ Miles`. I cannot observe who did the judging. Git records who committed, not who decided; an agent-produced disposition committed by a human and a human-reasoned disposition are byte-identical in the log. That the human adjudicated these personally, and that one gate remains, are `asked` — I have them from your briefing only, and a sentinel that launders its briefer's claims into its own observations is not watching anything. The same limit is the reason this is called a *proxy* for "times the human said yes" and not a count of them.

Also observed, and reported without interpretation: yesterday's commits carry offset `+0100`, today's carry `+0200`. The machine's local zone changed between the two readings, so 20:46 CEST is 19:46 on the clock the previous reading used. Whether any of that bears on the person is `asked` — no chronotype is declared, hour 20 gets no circadian band, and I am not going to construct one out of a timezone diff.

### Inferred risk (defeasible)

Four to five distinct streams in the window — assessment output, pipeline documentation, spec and objection artefacts, decision records, source — sits at or above the configured switching threshold, and **task-switching cost / attention residue** (Leroy, 2009) scales with the switching itself, so each move leaves residue on the decision that follows it. Decision volume at eight reaches the configured ceiling for sustained decision load within a 53-minute block, which is a denser rate of approval-like events than the window it is compared against assumes — though as stated in point 3, the proxy cannot tell that these eight closed a deliberately trivial measurement change, and that is a live defeater against reading the crossing as load. The span proxy did **not** cross and contributes nothing here; the vigilance decrement, which is a time-on-task effect, is not engaged by 53 minutes and I am not going to imply otherwise.

There is no combined score in this reading and there will not be one.

### Recommendation

Two thresholds are met under the disjunctive rule — one at its line, one at or above it — so the advisory fires. Once:

Decide your stop **before the next session begins**, while the judgment making that decision is still the kind you would trust. Do not negotiate the boundary with your tired self.

One concrete, time-boxed option: **take the remaining gate tomorrow morning in a 20-minute box, and open it by re-reading `887f030` — the decision record and the one-line change to `findOwners.html`.** That commit is the last approval in the window and the only one in it that put a yes onto `src/`; it is also the point where the measurement run stopped being a measurement and touched the product.

The choice to continue is yours.

---

Nothing was written to disk. This agent has no Write or Edit tool by design, so no record of this reading, of any break, or of any chronotype exists anywhere. Tune mode was not requested, so no HARNESS.md edits are proposed. Two parameters are unconfigured and both affect what you just read — an idle cut, and the branch scope of the proxies. Either is a conversation about the `Cognitive reservoir` block in `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/HARNESS.md`, and it is yours to have, by hand.
