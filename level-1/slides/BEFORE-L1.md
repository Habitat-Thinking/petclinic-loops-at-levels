# Slide plan — before the Level 1 demo

**Purpose:** set up the cold open. Explain what Dictating is, prove the demo is honest
before it runs, and tell the audience what to watch for.

**Handed the room by** [../../opening/slides/OPENING.md](../../opening/slides/OPENING.md)
— 5 slides, 3 minutes, the hinge from the keynote. That deck has already put the ladder on
screen, named all five levels and made the live-versus-recorded contract, so **slide 1
below does not re-introduce the ladder and slides 3–4 are the first and only time the
clean room is explained.** If the opener is not being used, slide 1 needs one extra line
naming the five levels.
**Length:** 6 slides, ~4 minutes, immediately before the live run.
**Source evidence:** [../CLEAN-ROOM.md](../CLEAN-ROOM.md), [../VARIANCE.md](../VARIANCE.md).

**Design direction for the deck:** high contrast, one idea per slide, numbers large enough
to read from the back. No stock imagery. The recurring visual motif is a **ladder with six
rungs**, Level 1 lit and the rest greyed — reused at every level, so the audience builds a
mental map. Monospace for anything that is literally typed on stage.

---

## Slide 1 — Level 1: Dictating

**Takeaway:** You already work this way. Today you'll see what it costs.

- Level 1 of 5 on the ladder.
- One human, one prompt, one turn.
- The most common way people use AI to write software right now.

**Visual:** the six-rung ladder, rung 1 lit.

**Speaker note:** Don't be superior about Level 1. Most of the room lives here, and it
works. The talk earns its argument by showing Level 1 doing *well*, not badly.

---

## Slide 2 — What "Dictating" means

**Takeaway:** Everything the human knows stays in the human.

- You hold the context: the conventions, the decisions, the reasons.
- You transmit a slice of it, in a prompt, by hand, every time.
- The agent produces work. The knowledge does not persist.

**Visual:** a head with knowledge inside, a thin arrow to a terminal, and nothing flowing
back. The asymmetry is the whole slide.

**Speaker note:** The word to plant here is **evaporates**. It pays off after the demo.

---

## Slide 3 — The clean room

**Takeaway:** For this to be evidence rather than theatre, the agent must start with nothing.

What was removed for this run:

| Everyday setup | In the demo |
|---|---|
| 4 plugins (52 skills, 38 agents) | 0 |
| User settings, auto-mode context | none loaded |
| MCP servers / account connectors | 0 |
| `CLAUDE.md`, memory, session history | none |

- Verified in the startup log, not by taking the agent's word for it.
- Same account, same machine, separate profile.

**Visual:** two-column before/after, the right column almost empty. Let the emptiness carry it.

**Speaker note:** If asked how: a dedicated config directory and a scrubbed environment,
one shell script. Offer the repo link rather than reading flags aloud.

---

## Slide 4 — Two traps worth knowing

**Takeaway:** Isolation is harder than it looks, and both traps fail silently.

- **The obvious "clean" flags are wrong.** `--bare` and `--safe-mode` also disable project
  context discovery — that would mute Level 2's habitat and make the levels look identical.
- **The environment leaks.** With a custom profile, merely having `USER` set breaks the
  credential lookup. A normal terminal always sets it. This would have failed on stage and
  never in rehearsal.
- **The agent's own audit of its context was wrong** — it over-claimed. The log was right.

**Visual:** three short "gotcha" cards. Keep it fast.

**Speaker note:** This is a credibility slide, and it's also a preview of the whole talk:
you cannot verify a habitat by asking the agent about it.

---

## Slide 5 — The task

**Takeaway:** A small, real, honestly under-specified request.

> Add an email address to owners. It should be shown on the owner details page and be
> editable when creating or editing an owner.

- Spring PetClinic, untouched upstream. 21 files legitimately need to change.
- Reused **verbatim** at all five levels. Only the habitat changes.
- Deliberately silent on one thing a maintainer would know: **must every owner have one?**

**Visual:** the prompt, monospace, large, alone. Below it a faint fan of the file types it
touches: entity, 3 schemas, 3 seed files, 10 translations, 2 templates, tests.

**Speaker note:** Say the prompt was frozen before any run, and that it was not narrowed
afterwards to make the model look bad. Someone will wonder.

---

## Slide 6 — What to watch for

**Takeaway:** Don't watch whether it works. Watch what happens to the reasoning.

Three questions to hold while it runs:

1. **Does it do the job?** (Be ready to be impressed.)
2. **What does it decide that nobody asked it to decide?**
3. **Where does that decision live when the terminal closes?**

- It's been run three times already. You'll see the other two afterwards.

**Visual:** the three questions, numbered, nothing else.

**Speaker note:** Set expectation for duration: observed 91 s to 294 s. If it runs long,
narrate question 2 while waiting. Fallback branch is `level-1-dictating-after`.
