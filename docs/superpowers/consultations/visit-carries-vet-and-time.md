---
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
date: 2026-09-19
state: open
supersedes: null
voices:
  - voice: Clinic reception staff
    source_flag: inferred
    question: "when a visit records a named vet and a 09:00 start that nothing in the clinic has agreed to, is that something you would work from, or does it have to be visibly a request rather than a booking?"
    disposition: pending
    outcome: null
  - voice: Attending veterinarians
    source_flag: inferred
    question: "is a visit that names you at a time you never agreed to — including a second one at the same time — something you can live with until availability exists, or does your name on it need your agreement first?"
    disposition: pending
    outcome: null
  - voice: Locale translation contributors
    source_flag: inferred
    question: "for the two new labels, a Time column heading and a Vet column heading, which of the ten locales can you give a trustworthy translation for before this merges, and which cannot?"
    disposition: pending
    outcome: null
  - voice: MySQL and Postgres database operators
    source_flag: inferred
    question: "does any MySQL or Postgres database of this app exist that is not recreated from scratch on each start, and would dropping and reseeding it lose anything you need?"
    disposition: pending
    outcome: null
  - voice: Teaching-fork maintainers
    source_flag: inferred
    question: "does anything outside this repository — slides, screenshots, a demo script — depend on how Jean Coleman's owner page looks today, given it gains a Time and a Vet column and four rewritten seed rows?"
    disposition: pending
    outcome: null
---

# Consultation — visit carries a vet and a time of day

**This project declares no `## Stakeholders` section.** `HARNESS.md` carries
context, constraints, garbage collection and status, and names no people. Every
voice below is therefore **`inferred`** — derived from the change itself, not
declared by the project. The list is shorter and less certain than a declared
map would make it, and it is labelled honestly rather than dressed up.

Five voices. Four objections in the adjudicated record (O1, O3, O6 and the
data-handling reasoning behind FR-16) describe failure classes that overlap
what is asked here; they stay in the objection record. What is here is the
half the objection record cannot hold: **who to ask, and the one question**.
Under the Routing Rule's tie-break, a finding about a person who should be
asked is this record's even when it also names a failure class, because the
remedy is a conversation rather than a spec change.

## Clinic reception staff

> when a visit records a named vet and a 09:00 start that nothing in the clinic
> has agreed to, is that something you would work from, or does it have to be
> visibly a request rather than a booking?

O1 was accepted and closed by narrowing the user story — the spec no longer
claims the owner knows who they are seeing. But the *screens* were deliberately
left alone: the remedy of request-versus-confirmation wording was considered and
not chosen, and the owner page still shows a vet and a time beside the existing
"your visit has been booked" message. Whether that is workable or actively
harmful depends entirely on what the desk does with a booking record, and that
is procedural knowledge held by the people who work the desk. No amount of
reasoning inside the spec recovers whether a clinic treats an unconfirmed
request as a queue item or as a promise it must now honour.

## Attending veterinarians

> is a visit that names you at a time you never agreed to — including a second
> one at the same time — something you can live with until availability exists,
> or does your name on it need your agreement first?

FR-16 reasons that a vet's name is already published on the vets page and is
therefore fine to render here, and the Diaboli explicitly declined to object to
that. The reasoning is about *disclosure*, and the change introduces something
different: attribution. D4 and FR-17 consult nothing, AS-13 asserts that two
visits may name the same vet at the same time, and the slice record records that
this application has no authentication and no logged-in actor, so anyone with
the URL can do it. Whether a clinician objects to their name being attached to
commitments they did not make is a question only clinicians can answer, and it
could change D2 (a required vet) rather than merely soften it.

## Locale translation contributors

> for the two new labels, a Time column heading and a Vet column heading, which
> of the ten locales can you give a trustworthy translation for before this
> merges, and which cannot?

This is the one voice whose answer has a mechanical consequence. FR-15 and
AGENTS.md directive 4 forbid copying English into a locale bundle; the plan
assumes twenty trustworthy translations across de, en, es, fa, hi, ja, ko, pt,
ru and tr; `I18nPropertiesSyncTest` (T-18) must pass; and HARNESS.md's "Tests
must pass" gate is deterministic and blocks the PR. O6 named that conflict and
was deferred. The conflict only fires if a locale is actually short — which is a
fact about who is available to translate, not a fact the spec can derive. Asking
first turns a merge-time deadlock into a scheduling answer.

## MySQL and Postgres database operators

> does any MySQL or Postgres database of this app exist that is not recreated
> from scratch on each start, and would dropping and reseeding it lose anything
> you need?

O3 is a high-severity deferred objection: both non-H2 `schema.sql` files use
`CREATE TABLE IF NOT EXISTS` and both `data.sql` files guard their inserts, so a
provisioned database never gains `vet_id` or `start_time` and never receives the
backfill, while `SchemaParityTest` compares files and the suite runs against
fresh H2. Every gate passes and the failure surfaces only at runtime. Whether
that matters is a single fact nobody in the repository holds: does a long-lived
volume exist. If the answer is no, the objection can be closed with one line
under "Out of scope". If it is yes, a migration path is in scope.

## Teaching-fork maintainers

> does anything outside this repository — slides, screenshots, a demo script —
> depend on how Jean Coleman's owner page looks today, given it gains a Time and
> a Vet column and four rewritten seed rows?

D3 keeps the four seeded visits and backfills them, and the Diaboli explicitly
declined to object to that decision — correctly, on the reasoning given. The
unasked question is not whether D3 is right but what else is pinned to that
screen. This is a teaching fork whose canonical demo is exactly those rows, and
slides, recordings and workshop handouts live outside the repository where no
test and no gate can see them. A table that widens from two columns to four, and
an action row that O11 notes is a sibling of the visit rows, is precisely the
kind of change that invalidates a screenshot silently.
