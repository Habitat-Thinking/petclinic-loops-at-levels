# Slide plan — after the Level 1 demo

**Purpose:** land the findings from three clean-room runs, including the one that
undercuts the easy story, and set up Level 2.
**Length:** 7 slides, ~5 minutes, immediately after the live run.
**Source evidence:** [../VARIANCE.md](../VARIANCE.md), [../runs/](../runs/),
[../CLEAN-ROOM.md](../CLEAN-ROOM.md).

**Design direction:** same ladder motif, rung 1 still lit. This deck is data-led: one real
number per slide, no decoration around it. Slides 2 and 5 are the ones people photograph,
so they get the most space and the largest type.

---

## Slide 1 — It worked

**Takeaway:** No context, no conventions, no memory — and the build is green.

What it changed, unprompted and correctly:

- All **three** database schemas (H2, MySQL, Postgres), not just the one the unit tests use
- Seed data rewritten in all three — those are positional `INSERT`s
- **10** locale bundles with *real* translations: `E-Mail`, `メールアドレス`,
  `Электронная почта`, Hindi correctly `\u`-escaped
- Tests added. `./mvnw verify`: **78 passed, 0 skipped**, database containers included

**Visual:** green build banner, with the file-type fan from the setup deck now all lit.

**Speaker note:** Concede this properly and without hedging. The argument is stronger if
the audience believes you'd have told them it failed.

---

## Slide 2 — Three runs. Same prompt. Same model.

**Takeaway:** Identical inputs, three different outcomes.

| Run | Time | Turns | Cost | Tests | Email required? |
|-----|------|-------|------|-------|-----------------|
| 1 | 294 s | 31 | $1.85 | 77 | **Required** |
| 2 | 91 s | 8 | $0.61 | 78 | Optional |
| 3 | 118 s | 10 | $0.76 | 78 | Optional |

- Same clean room, same commit, same wording.
- All three green. All three defensible.

**Visual:** the table, large. Highlight the last column in one accent colour, and the
time/cost columns in another. This is the photographed slide.

---

## Slide 3 — Why it did so well (the uncomfortable part)

**Takeaway:** The repository already had a harness. It just wasn't called one.

- PetClinic ships `I18nPropertiesSyncTest`: the build **fails** if any locale bundle is
  missing a key, or a template contains hardcoded text.
- So the agent didn't need a maintainer's knowledge about localisation.
  **A failing test told it.**
- That constraint was already there at Level 0, before anything in this talk was added.

**Visual:** the test's failure message as evidence, with an arrow to the 10 translated
bundles. Caption: *context that is enforced is context that survives*.

**Speaker note:** This is the honest finding and the strongest one. Level 3's whole
argument arrives early, from upstream, uninvited. Say that you expected a localisation
failure and didn't get one.

---

## Slide 4 — What actually varied

**Takeaway:** Not the code. The decisions nobody wrote down.

The prompt never said whether an owner *must* have an email.

- **Run 1:** required — "every other Owner field is `@NotBlank`"
- **Runs 2 & 3:** optional — "existing owners have none; requiring it blocks editing them"

Both reasonable. Each derived alone, from scratch, in a vacuum. Each reported at the end,
in chat, for a human who may or may not be reading.

**Visual:** the two one-line diffs side by side — `@NotBlank` present against absent. Tiny
change, opposite policy.

---

## Slide 5 — Where that decision lives now

**Takeaway:** Nowhere.

- It was made, argued for, and reported.
- The terminal closes.
- The next run starts the argument over. So does the next engineer.

**Visual:** the reasoning text fading to nothing between a "run 1" and "run 2" panel. This
is the emotional beat of the segment — give it a whole slide and a pause.

**Speaker note:** Call back to **evaporates** from the setup deck. Then ask the room: how
many decisions like this has your team made this quarter, and where are they written down?

---

## Slide 6 — Re-deriving isn't free

**Takeaway:** 3.2× the time and 3.0× the cost, for the same result.

- Run 1: 294 s, 31 turns, $1.85
- Run 2: 91 s, 8 turns, $0.61
- Same request. Same model. Same starting commit.

**Visual:** two bars, 3× apart. One number per bar.

**Speaker note:** For the engineering managers: the case for habitat isn't only quality,
it's variance. You cannot plan around a workflow whose cost swings 3× at random.

---

## Slide 7 — So what would Level 2 change?

**Takeaway:** Write the decision down once, and stop paying for it every run.

- Level 1's failure isn't capability. **It's that nothing accumulates.**
- Level 2 (Commanding) gives the repository a voice: the conventions and the decisions,
  written where the agent reads them.
- Same prompt. Same model. Watch what changes.

**Visual:** the ladder, rung 1 dimming as rung 2 lights.

**Speaker note:** Transition line: *"The model didn't need a better brain. It needed the
thing your team already knows and never wrote down."*
