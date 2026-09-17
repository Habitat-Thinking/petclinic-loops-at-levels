# Slide plan — before the Level 2 demo

**Purpose:** set up the talk's central claim before the audience sees it tested. Same
model, same prompt, different habitat.
**Length:** 5 slides, ~3 minutes, immediately before the segment.
**Source evidence:** [../FINDINGS.md](../FINDINGS.md), [../RUN-SHEET.md](../RUN-SHEET.md).

**Design direction:** the six-rung ladder motif continues, rung 2 lighting as rung 1
dims. This deck is the hinge of the talk, so it is the plainest: few words, large type,
no decoration competing with the claim.

---

## Slide 1 — Level 2: Commanding

**Takeaway:** Stop repeating yourself. Write it down where the agent reads it.

- Level 1's failure was not capability. Nothing accumulated.
- Level 2 gives the repository a voice.

**Visual:** ladder, rung 2 lit.

---

## Slide 2 — The experiment

**Takeaway:** One variable changes. That is what makes this evidence rather than a demo.

| Held constant | Changed |
|---|---|
| The prompt, word for word | What the repository says about itself |
| `claude-opus-5`, effort high | |
| Clean room, same machine | |
| Starting commit | |

**Visual:** two columns, the right one almost empty. The asymmetry is the argument.

**Speaker note:** Say the word **verbatim** about the prompt. Someone will assume you
improved it, and the whole comparison dies if they believe that.

---

## Slide 3 — Where the knowledge goes

**Takeaway:** One source. Every tool points at it.

```
AGENTS.md          ← the directives. The only place anything is written.
  ↑
CLAUDE.md          ← 7 lines. Imports it.
.github/copilot-instructions.md  ← points at the same file.
```

- Not a Claude file. A repository file that Claude happens to read.
- Swap the tool, keep the habitat.

**Visual:** one box with three arrows pointing *in*, not out.

**Speaker note:** This is the slide that answers "so I'd be locked into one vendor?"
before anyone asks it.

---

## Slide 4 — Where it came from: five questions

**Takeaway:** The conventions were extracted from a person, not invented by a model.

1. What should never be left to individual judgment?
2. What do you keep correcting when AI writes code here?
3. What security checks do you apply instinctively?
4. What triggers immediate rejection in review?
5. Where is the line between clean refactoring and over-engineering?

**Visual:** the five questions, numbered, nothing else.

**Speaker note:** The demo runs this live. Flag that the questions are about *their*
project — no AI jargon in any of them — because that is what makes the exercise portable
to the room's Monday morning.

---

## Slide 5 — What to watch for

**Takeaway:** Watch what the agent does differently, and watch what it still gets away with.

1. Does the same prompt produce different work?
2. What does written-down context actually buy?
3. **What does it fail to prevent?**

- We will look at all three, including the third.

**Visual:** three questions, the third emphasised.

**Speaker note:** Plant the third question deliberately. The drift is coming, and the
segment is more honest if the room was told to look for it rather than shown it after
the fact.
