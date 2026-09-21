# The repertoire close — the four classes, and the two tasks mapped

The frame the whole session resolves into. Short on purpose: it has to be
deliverable from memory, with the terminal off.

**The wording below is a draft for Russ to correct.** The classes are his; this
file records the mapping and the evidence behind each line so the reasoning is
checkable, not so it is recited.

## The question

> **If this turns out to be wrong, what does it take to put right?**

One question. It is asked *before* the work, and the answer picks the mode.

Not "how big is it?" — the typo and the booking feature are both small in the
diff. Not "how confident am I?" — confidence is the thing being tested. The
question is about the cost of being wrong, because that is what the ceremony is
buying down, and it is the only thing that tells you how much ceremony the work
can justify.

## The four classes

| Class | What putting it right takes | What the mode should be |
|---|---|---|
| **Undo** | A revert. Nothing moved, nobody acted on it. | Just do it. Ceremony here is pure cost. |
| **Repair** | Fix forward. Live but bounded; ordinary work, nothing to reconcile. | Tests and review. A gate if it is shared. |
| **Migrate** | State has already moved. Rows exist in the wrong shape, or code depends on the choice. Putting it right means moving what moved, everywhere it landed. | Spec first. Gates. Write the decision down. |
| **Answer** | Someone outside the team was told something. No amount of code puts it right on its own. | Every gate, and ask the people before you build. |

The classes are about **reversal cost**, not size, difficulty or risk of being
wrong. A one-line change can be an Answer. A thousand-line refactor can be Undo.

## The two tasks

### The typo → **Undo**

> *Prototype text a user never sees. Revert the line and it is as if it never
> happened.*

Evidence, from the descent: Thymeleaf replaces the element body at runtime, so
the rendered page is byte-identical before and after. Nothing reads it, nothing
stored it, no one acted on it. Reversal cost: one `git revert`, zero
reconciliation, zero people to tell.

It was put through **four human gates, nine agents and $52.16** of ceremony. The
class says the correct mode was: make the change.

### The booking feature → **Migrate**, and also **Answer**

This is the part worth slowing down for, because one task lands in two classes
and *the cheaper answer is the one you would have reached for*.

> **Migrate** — *`vet_id NOT NULL` and `start_time NOT NULL` are in three
> dialects with a foreign key, twelve seeded rows were rewritten, and the `owner`
> package now compiles against `vet`. Undoing it means dropping a constraint and
> a foreign key across h2, MySQL and Postgres in one change.*

That is precisely the bill D2 wrote out when it took the vet-required decision
away from S4. Rows exist. Code depends on it. Reversal is a planned migration,
not a revert.

> **Answer** — *The owner's page shows a named clinician and a time beside "Your
> visit has been booked", and nothing in the clinic agreed to either.*

This is objection O1, which was accepted. If that reached a real clinic: a vet's
name was attached to a commitment they never made, and an owner was told a fact
that was not one. No migration fixes that. Someone has to be told.

The convener drafted exactly that question for the attending vets — *"is a visit
that names you at a time you never agreed to something you can live with?"* — and
it is still `pending`. **Nobody has asked it.**

## The line the close turns on

Ask only *"can I revert it?"* and the booking feature reads as Migrate: expensive,
plannable, a database problem. That answer is true and it is not the whole
answer.

**The class that decides the mode is the most expensive one the work touches**,
and for this feature that is Answer — which is why the gates were worth it, and
why the one question still outstanding is a conversation rather than a commit.

Meanwhile the typo is Undo, and it got the same treatment.

> **Level 5 is not a destination. It is one setting on an instrument that has
> several, and the skill is choosing the one the work deserves.**

## What this is not

It is not a maturity model, and it does not replace the levels. The levels are
what a team *can* do; the classes are what a *piece of work* needs. A team at
Level 5 that runs every change at Level 5 has bought a repertoire and is playing
one note.

It is also not a scoring rubric. The question takes ten seconds and is answered
out loud, before the work. If it needs a spreadsheet, it has been misunderstood.
