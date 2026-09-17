# Slide plan — after the Level 3 demo

**Purpose:** land the catch, show that enforcement changed the work and not just the
gate, admit the ceiling, and hand over to Level 4.
**Length:** 8 slides, ~5 minutes.
**Source evidence:** [../FINDINGS.md](../FINDINGS.md), [../health.txt](../health.txt),
[../gc.txt](../gc.txt), [PR #1](https://github.com/Habitat-Thinking/petclinic-loops-at-levels/pull/1).

**Design direction:** ladder motif, rung 3 lit. Slides 3 and 6 are the ones to
photograph. Slide 7 is deliberately uncomfortable and should look as plain as the rest —
no visual apology.

---

## Slide 1 — The failure came back. It got caught.

**Takeaway:** Same violation as Level 2. Different ending.

- A change under `src/`, no decision record — exactly what slipped through before.
- Advisory warned. The merge gate blocked. The test failed on its own.

**Visual:** the Level 2 failure and the Level 3 catch, side by side, same diff.

---

## Slide 2 — One violation, three loops

**Takeaway:** Where you are decides what happens to you.

| Loop | Consequence |
|---|---|
| Edit / commit | warned twice — **commit succeeded** |
| Merge | **blocked**, exit 1, in real CI |
| Tool alone | `owners.email exists in h2 but not in mysql` |

**Visual:** three terminal excerpts, escalating in severity down the slide.

**Speaker note:** The commit succeeding is not a weakness. Interrupting someone
mid-thought costs more than it saves; the gate is where it matters.

---

## Slide 3 — With nothing intelligent watching

**Takeaway:** The rule survives the agent being absent, wrong, or replaced.

```
$ ./mvnw test -Dtest=SchemaParityTest

Schema parity violated — a change landed in one database and not the others:
  owners.email exists in h2 but not in mysql
  owners.email exists in h2 but not in postgres
```

- No prompt. No context. No model. A test reads three files and fails.

**Visual:** the failure output, alone, large.

**Speaker note:** This is the slide for the room's sceptic. If every rule needs an agent
to evaluate it, the harness is worth very little — so the headline rules do not.

---

## Slide 4 — Who made the rule durable

**Takeaway:** The reflection proposes. The human decides. Only then is it a rule.

- The agent drafted the constraint and **wrote nothing** until it was accepted.
- That pause is not ceremony. It is where authority lives.

**Visual:** the exchange, with the approval line highlighted.

---

## Slide 5 — Enforcement changed the work, not just the gate

**Takeaway:** The most interesting effect was upstream of the check.

Re-running the identical task under Level 3:

- It wrote `decisions/2026-09-17-owner-email.md` **unprompted**
- It added the missing length validation **nobody asked for** — closing Level 2's second
  drift by itself, and explaining why in the record
- All three schemas moved together, proved by the test

**Visual:** the decision record it wrote, as a real file in a real tree.

**Speaker note:** This is the argument for constraints over review: the rule's existence
changed how carefully it worked, before anything ran.

---

## Slide 6 — Four levels, one prompt

**Takeaway:** Each level buys something. None of it is free.

| | L1 | L2 | L3 |
|---|---|---|---|
| Time | 118 s | 120 s | 143 s |
| Cost | $0.76 | $1.06 | $1.25 |
| Tests | 78 | 78 | 81 |
| Decisions durable? | no | no | **yes** |

**Visual:** the table, large. This is the photographed slide.

---

## Slide 7 — The instruments, on a one-day-old harness

**Takeaway:** It had already drifted from its own description. In one day.

They were told not to invent findings. They found:

- a badge claiming **2/2 enforced** against six constraints
- a constraint declaring commit scope that **nothing ran at commit**
- a GC rule nothing ran, while CI ran three nobody had written down
- a hook timeout shorter than a cold build — it would have **failed silently**

**Visual:** the four findings as a plain list. No alarm styling.

**Speaker note:** Do not say "your repo is rotting" — this one is a day old. Say: none of
that was visible by reading the files, and the time to own an instrument is before month
six.

---

## Slide 8 — The ceiling

**Takeaway:** Everything here bounds what *may* happen. Nothing here decides what *should*.

- The harness cannot tell you the field was worth adding.
- It cannot choose between two designs.
- It cannot decide what to build next.
- **It says no.** That is all it can say.

**Visual:** ladder, rung 3 dimming as rung 4 lights.

**Speaker note:** Transition line: *"We have built something that can stop us being
wrong. It has nothing whatsoever to say about being right."*
