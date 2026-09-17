# Slide plan — before the Level 3 demo

**Purpose:** frame regulation as the answer to a failure the audience has already seen,
not as new tooling for its own sake.
**Length:** 5 slides, ~3 minutes.
**Source evidence:** [../FINDINGS.md](../FINDINGS.md), [../RUN-SHEET.md](../RUN-SHEET.md).

**Design direction:** ladder motif, rung 3 lit. This deck should feel like a consequence
rather than a new chapter — carry the Level 2 reflection entry across as a visual thread.

---

## Slide 1 — Level 3: Regulating

**Takeaway:** Rules that hold when nobody is watching.

- Level 2 wrote things down. They were followed — mostly, and only while someone cared.
- Level 3 makes the important ones hold on their own.

**Visual:** ladder, rung 3 lit, with the Level 2 reflection entry carried over from the
previous deck's last slide.

---

## Slide 2 — The thing that got past us

**Takeaway:** We are not inventing a rule. We are promoting a failure.

> Directive 12 said the decisions go in the PR description. The agent complied. There
> was no PR. Nothing in the repository remembered.

- Still sitting in `REFLECTION_LOG.md`, where Level 2 left it.

**Visual:** the reflection entry, with the "Surprise" line pulled out large.

**Speaker note:** Fifteen seconds. Resist re-explaining Level 2 — the room was there.

---

## Slide 3 — Loop versus turn

**Takeaway:** Two files, two jobs. The distinction does work, not filing.

| `AGENTS.md` | `HARNESS.md` |
|---|---|
| Governs the **turn** | Governs the **loop** |
| What to do while making a change | What must be true before a change is allowed in |
| Read by an agent | Run by a tool |

- Three directives moved when the harness was built, because each named a loop artefact.
- One of them **changed as it moved** — that change is the demo.

**Visual:** two boxes, with three labelled arrows crossing from left to right.

---

## Slide 4 — Three loops, three consequences

**Takeaway:** The same violation should meet you differently depending on where you are.

| Loop | When | What it does |
|---|---|---|
| Edit / commit | while you work | **warns** — and lets you carry on |
| Merge | pull request | **blocks** |
| Scheduled | Mondays | **investigates** what accumulated |

**Visual:** three concentric loops around the change, tightening left to right.

**Speaker note:** The middle row is where the teeth are. The outer row is the one most
teams never build, and the one that catches what no per-change gate can see.

---

## Slide 5 — What to watch for

**Takeaway:** Watch the same mistake meet a different fate.

1. Where does the rule become **durable**? (Watch who says yes.)
2. Same violation, two loops — **two different consequences**.
3. Does it still hold with **no agent anywhere near it**?

**Visual:** three questions, the third emphasised.

**Speaker note:** Question 3 is the one the sceptics came for. Promise it here and pay it
off in the segment: a test that reads three files and fails, with no model involved.
