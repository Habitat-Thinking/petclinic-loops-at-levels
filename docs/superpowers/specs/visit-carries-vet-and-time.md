---
slice: S2
slice_record: docs/superpowers/slices/owner-books-visit-against-vet-availability.md
title: "What a booked visit becomes — vet, time of day, and the existing visit rows"
date: 2026-09-19
status: draft — awaiting maintainer adjudication of the decisions below
---

# Visit carries a vet and a time of day

## Scope

This spec covers slice **S2 only** from
`docs/superpowers/slices/owner-books-visit-against-vet-availability.md`.

In scope: a visit records **which vet** and **at what time of day**, in the
entity, in all three schemas, in all three seed data files, and on the two
screens where visits are shown.

Explicitly **not** in this spec, and not assumed to exist:

- **S1** — published availability / open slots. There is no slot table and
  there will not be one in this change.
- **S3** — any publishing surface for availability.
- **S4** — the vet-first or slot-first booking journey, and the question of
  whether the existing free-date visit form is replaced.
- **S5** — contention between two owners wanting the same time. Nothing here
  prevents two visits with the same vet at the same time; that is deliberate
  and deferred.

## Decisions taken

The slice record names three decisions that must be settled before a coherent
spec can exist. They are settled here, with the reasoning visible, so a
maintainer can overturn any of them at the plan-approval gate.

### D1 — A visit stores a date and a separate start time, on the visit itself

The slice offered three representations: a single `LocalDateTime` start, a date
plus a separate start-time column, or a foreign key to the S1 slot.

**The foreign-key option is unavailable.** S1 is not being built, so there is no
slot table to point at. Choosing it would make this slice undeliverable on its
own, and the slice record's own sequencing note says S2 can land before S1
precisely when the visit carries a vet and a start time of its own. That is what
this spec does.

Between the remaining two, the visit keeps its existing `visit_date` date and
gains a separate start time. Reasoning:

- It is additive. `visit_date` keeps its `DATE` type in all three databases, so
  the existing "date must be in the future" rule, the `yyyy-MM-dd` display
  format and every existing assertion about a visit's date survive untouched. A
  single timestamp would be a type migration of an existing column across three
  dialects and would rewrite rules that are not the subject of this change.
- The form can express it with the fragments already in the repository — a date
  control and a time control — rather than a `datetime-local` control and a new
  format pattern.
- It matches the project's preference for the smallest diff and for the plain
  construction a newcomer can read.

The cost, stated honestly: two columns can in principle hold a date with no
time. FR-2 and FR-11 close that by making the start time required in both the
schema and the form.

### D2 — A vet is required on every visit

Every visit has exactly one vet. The column is `NOT NULL` with a foreign key to
`vets`, and no path in the application can create a visit without one.

Reasoning: the alternative leaves two kinds of visit in the system
indefinitely — one that answers "who will see the pet" and one that cannot —
and every screen then has to render the empty case forever. A clinic visit
without a vet is not a thing the product means to represent.

Consequence, stated plainly because it constrains S4: the existing
`/owners/{ownerId}/pets/{petId}/visits/new` form must be able to supply a vet,
so it gains a vet chooser in this slice (FR-3). This spec does **not** decide
whether that form survives S4 — only that, for as long as it is the only way to
create a visit, it can create a valid one.

### D3 — The four existing visits are backfilled, not deleted

Each seed file keeps its four visit rows, with the same pet, the same date and
the same description, each given a vet and a start time. The positional
`INSERT INTO visits VALUES (...)` statements are rewritten with explicit column
lists so the next column change does not silently break them.

Reasoning: those rows are the visible content of the canonical demo screen
(Jean Coleman's two pets) and existing behaviour depends on their count.
Deleting them would remove observable content to avoid inventing two fields of
sample data, in a codebase whose entire seed set is invented sample data.

### D4 — Any vet, any time; no availability is consulted

A visit may name any vet at any time of day on any future date. Nothing checks
whether that vet is free, and two visits may name the same vet at the same
time. Availability (S1) and contention (S5) are separate decisions and are not
pre-empted here.

### D5 — Display formats

Time is shown as `HH:mm`, 24-hour, the same for every locale. Date keeps its
existing `yyyy-MM-dd`. Reasoning: it matches the fixed, locale-independent date
format already used on both screens; introducing locale-sensitive time
formatting would be a separate, wider change.

### D6 — Visits are listed date first, then start time

Where visits are listed for a pet, they are ordered by date ascending and then
by start time ascending, so two visits on the same day read in the order they
happen.

### D7 — A new visit is offered tomorrow at 09:00

The form pre-fills tomorrow's date, as it does today, and pre-fills a start
time of 09:00. Reasoning: consistency with the existing default, and a
pre-filled control that a user can change is plainer than an empty required
field.

## User story

> As a pet owner, I want a visit to record which vet my pet will see and at what
> time of day, so that when I look at my pet's visits I know who we are seeing
> and when to arrive, rather than only which day.

## Acceptance scenarios

### Recording a vet and a time

**AS-1 — The new-visit form offers a vet and a time**
Given an owner with a pet
When I open the new-visit form for that pet
Then I see a control for choosing a vet, listing every vet in the clinic by
name, and a control for entering a start time
And the date is pre-filled with tomorrow and the start time with 09:00.

**AS-2 — Booking with a vet and a time succeeds**
Given an owner with a pet
When I submit the new-visit form with a future date, a start time, a chosen vet
and a description
Then I am redirected to the owner's page
And the confirmation message is shown.

**AS-3 — The booked visit shows the vet and the time on the owner's page**
Given a pet with a visit at 14:30 with the vet Helen Leary
When I open that owner's page
Then the pet's visit row shows `14:30` and `Helen Leary` alongside the visit's
date and description.

**AS-4 — The booked visit shows the vet and the time on the pet's visit list**
Given a pet with an existing visit at 14:30 with the vet Helen Leary
When I open the new-visit form for that pet
Then the previous-visits list shows `14:30` and `Helen Leary` for that visit.

### Rejecting an incomplete booking

**AS-5 — A visit with no vet is rejected**
Given an owner with a pet
When I submit the new-visit form with a future date, a start time and a
description but no vet chosen
Then the form is redisplayed with a validation error against the vet field
And no visit is created.

**AS-6 — A visit with no start time is rejected**
Given an owner with a pet
When I submit the new-visit form with a future date, a chosen vet and a
description but no start time
Then the form is redisplayed with a validation error against the start-time
field
And no visit is created.

**AS-7 — The existing date rule is unchanged**
Given an owner with a pet
When I submit the new-visit form with today's date, a start time, a vet and a
description
Then the form is redisplayed with the existing `typeMismatch.visitDate` error
against the date field
And no visit is created.

**AS-8 — A description is still required**
Given an owner with a pet
When I submit the new-visit form with a future date, a start time and a vet but
a blank description
Then the form is redisplayed with a validation error against the description
And no visit is created.

### The visits that already exist

**AS-9 — Seeded visits survive the change**
Given the application started against a freshly seeded database
When I open the owner's page for the owner whose pets have seeded visits
Then the same four seeded visits are present, with their original pets, dates
and descriptions
And each one shows a vet name and a start time.

**AS-10 — Every database carries the same visit shape**
Given the H2, MySQL and Postgres schema files
When the schema-parity check runs
Then the `visits` table declares the same columns in all three, including the
start time and the vet reference
And each seed file creates its four visits with a vet and a start time.

### Ordering and presentation

**AS-11 — Same-day visits read in time order**
Given a pet with two visits on the same date, one at 09:00 and one at 15:00
When I open the owner's page
Then the 09:00 visit is listed before the 15:00 visit.

**AS-12 — Column headings are translated, not hardcoded**
Given the owner's page and the new-visit form
When the visit tables are rendered
Then the time and vet headings come from message keys, and no English heading
text is hardcoded in the template.

### What is deliberately not prevented

**AS-13 — Two visits may name the same vet at the same time**
Given a visit already exists with vet Helen Leary at 14:30 on a given date
When I submit a second visit for a different pet with vet Helen Leary at 14:30
on the same date
Then the second visit is accepted and both exist.
(This is the behaviour S5 will revisit. It is asserted so that the absence of
contention handling is a recorded decision rather than an oversight.)

## Functional requirements

Each is testable and traces to at least one scenario above.

| # | Requirement |
|---|---|
| **FR-1** | A visit records exactly one vet. A visit cannot be created without one. *(D2, AS-5)* |
| **FR-2** | A visit records a start time of day, separate from its date. A visit cannot be created without one. *(D1, AS-6)* |
| **FR-3** | The new-visit form offers a choice of every vet in the clinic, each shown by first and last name, and a control for entering a start time. *(AS-1)* |
| **FR-4** | The form pre-fills tomorrow's date and a start time of 09:00. *(D7, AS-1)* |
| **FR-5** | Submitting with no vet chosen redisplays the form with a validation error against the vet field and creates nothing. *(AS-5)* |
| **FR-6** | Submitting with no start time redisplays the form with a validation error against the start-time field and creates nothing. *(AS-6)* |
| **FR-7** | The existing rules are unchanged: the date must be after today, rejected with error code `typeMismatch.visitDate`, and the description must not be blank. *(AS-7, AS-8)* |
| **FR-8** | A valid submission redirects to the owner's page with the existing confirmation message, and the visit is recorded with its vet and start time. *(AS-2)* |
| **FR-9** | The owner's page shows, for every visit of every pet, the visit's start time and the vet's name, alongside the existing date and description. *(AS-3)* |
| **FR-10** | The pet's previous-visits list on the visit form shows each visit's start time and vet name. *(AS-4)* |
| **FR-11** | The `visits` table in `db/h2`, `db/mysql` and `db/postgres` each gains a required start-time column and a required vet reference constrained as a foreign key to `vets`, landed in the same change. *(D1, D2, AS-10)* |
| **FR-12** | Each `data.sql` keeps its four visits — same pet, same date, same description as today — each given a vet and a start time, written with an explicit column list. *(D3, AS-9, AS-10)* |
| **FR-13** | Visits listed for a pet are ordered by date ascending, then start time ascending. *(D6, AS-11)* |
| **FR-14** | Dates display as `yyyy-MM-dd` and times as `HH:mm`, identically in every locale. *(D5, AS-3)* |
| **FR-15** | All new display text — the time heading/label and the vet heading/label — comes from message keys. New keys are added to `messages.properties` and to every locale bundle only with a genuine translation; English text is never copied into a locale bundle to satisfy the sync check. Any locale left without a trustworthy translation is flagged for a human. *(AS-12)* |
| **FR-16** | No owner personal data is added to any output. The vet's first and last name is vet data, already published on the vets page, and is the only new personal-ish field rendered. |
| **FR-17** | No availability is consulted and no clash is prevented: any vet may be recorded at any time of day on any future date, including a time another visit already uses. *(D4, AS-13)* |

## Out of scope, restated as non-requirements

- No slot, availability or calendar concept is introduced (S1).
- No publishing surface is introduced (S3).
- The booking journey is not changed beyond adding the two fields the model now
  requires: no vet-first navigation, no slot list, no change to the form's URL
  or its place in the flow (S4).
- No uniqueness constraint and no clash re-check (S5).
- No cancellation, rescheduling, notification, working-hours or
  choose-by-specialty behaviour (dropped as adjacent features in the slice
  record).
