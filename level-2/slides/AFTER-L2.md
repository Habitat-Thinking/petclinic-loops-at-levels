# Slide plan — after the Level 2 demo

**Purpose:** land what context bought, then name what it could not do — and hand that
failure forward as the reason Level 3 exists.
**Length:** 7 slides, ~5 minutes, immediately after the segment.
**Source evidence:** [../FINDINGS.md](../FINDINGS.md), [../runs/](../runs/).

**Design direction:** data-led. Slides 2 and 5 carry the argument and should be the
largest. Slide 5 is the emotional pivot of the whole talk — give it room.

---

## Slide 1 — Same prompt. Different work.

**Takeaway:** Nothing about the model changed. The output did.

- 120 s, 10 turns, 78 tests green — near-identical mechanics to Level 1
- The difference is in *what it chose to do*

**Visual:** the two runs side by side, timings almost identical, so the eye goes to the
diff rather than the numbers.

---

## Slide 2 — Three things the directives bought

**Takeaway:** Each one traces to a line somebody wrote down.

| What changed | Directive |
|---|---|
| Email kept **out of `toString()`** as personal data (Level 1 put it in) | 6 |
| **Both database integration tests extended**, proving the column on MySQL and Postgres | 9 |
| **Six judgement calls surfaced** as a numbered list, where Level 1 decided silently | 12 |

- It also flagged that directive 6 did not *name* email — and asked rather than assumed.

**Visual:** the three diffs, small but real, each annotated with its directive number.

**Speaker note:** The asking-rather-than-assuming detail is the one that lands with
senior engineers. It is the behaviour of someone who knows where the rules end.

---

## Slide 3 — What it cost

**Takeaway:** Habitat costs less than the variance it removes.

- Level 1: **$0.76** · Level 2: **$1.06** (about 40% more)
- Time unchanged: 118 s → 120 s
- Level 1's own spread, same prompt: **$0.61 – $1.85**

**Visual:** three bars: L1 low, L2 slightly higher, and the L1 *range* drawn as a tall
error bar dwarfing both.

---

## Slide 4 — And the file that did nothing

**Takeaway:** You cannot verify a habitat by looking at it.

- `CLAUDE.md` first pointed at `AGENTS.md` with a plain link.
- A fresh session reported **zero directives**. It knew the filename. Nothing else.
- On disk it looked complete. Level 2 would have silently behaved like Level 1.
- Fixed with an import. **Verified by asking the agent what it had, not by reading files.**

**Visual:** two identical-looking file listings, one annotated "works", one "does
nothing". They look the same. That is the point.

**Speaker note:** Optional if time is tight, but it is the best evidence in the talk for
verifying rather than assuming — and it sets up Level 3's whole argument.

---

## Slide 5 — What context could not prevent

**Takeaway:** The directive was followed. The failure happened anyway.

Directive 12: *judgement calls go in a "Decisions" section of the PR description.*

- It wrote six numbered decisions. ✓
- Into the chat. There was no PR.
- **Nothing in the repository recorded them.**
- Close the terminal: the reasoning is gone.

**Visual:** the six decisions fading out, with an empty repository tree beside them.

**Speaker note:** Say it plainly: this is the Level 1 failure again, wearing a
compliance badge. Pause before the next slide.

---

## Slide 6 — Why advice was never going to be enough

**Takeaway:** The rule named a destination that did not exist, and nothing checked.

- No tool asked *where* the decisions landed.
- It also **knowingly shipped** a defect — a length limit in the schema with no matching
  validation — and reported it rather than fixing it.
- **Advice can note a defect. It cannot stop one.**

**Visual:** the directive text with "the PR description" circled, and an arrow into empty
space.

---

## Slide 7 — So we wrote it down. Again.

**Takeaway:** The failure becomes a rule — but only once something can check it.

- The reflection is in the log now, proposing exactly that.
- Level 3: rules that hold whether or not anyone is watching.
- Watch for this same failure to come back — and get caught.

**Visual:** ladder, rung 2 dimming as rung 3 lights; the reflection entry pinned beside
it as the thread being carried forward.

**Speaker note:** Transition line: *"Everything we just added is advice. Advice is what
you give people you cannot supervise."*
