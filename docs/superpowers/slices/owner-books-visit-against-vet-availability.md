---
task: "An owner books a visit online against a vet's published availability. From the pet's page, an owner picks a vet and an open slot and books a visit; someone publishes what is open. Today Visit carries only pet_id, a bare LocalDate visit_date and a description — there is no vet on a visit and no time of day, so this cannot be added as a column."
task_slug: owner-books-visit-against-vet-availability
date: 2026-09-19
carpaccio_model: claude-opus-5
inseparable: false
progressed_slice: null
slices:
  - id: S1
    title: "What published availability is — open slots as a modelled, visible concept"
    scope: >
      Introduce availability as a first-class concept in the `vet` package,
      following the project's controller → repository → entity shape with no
      service layer. Decide and land the model for what a vet publishes as
      open: discrete slot rows versus a recurring weekly pattern from which
      slots are derived; slot duration and granularity; how far ahead
      availability extends; and how a slot's open-or-taken state is
      represented. The new table lands in `db/h2`, `db/mysql` and
      `db/postgres` schema.sql in the same change with each data.sql seeded
      for the six existing vets, per AGENTS.md directive 2 and the
      SchemaParityTest constraint. Ships with a read-only surface so a human
      can see a vet's open slots — the concept is observable without any
      booking or publishing UI existing yet.
    decision_focus: >
      Vets have no availability concept at all today, so this slice decides
      what the word means in this system. Explicit published slot rows and a
      recurring availability rule produce visibly different downstream work —
      a different table, different queries, a different way to mark a slot
      taken, a different publishing surface, and a different linkage from
      Visit. Whether "open" is a stored flag on the slot or derived from the
      absence of a booking is the same decision seen from the other side.
      Slot duration and booking horizon are user-visible defaults that
      AGENTS.md directive 10 requires a human to settle and record.
    lens_used: decision-boundary
    disposition: pending
    disposition_rationale: null
    file_as_issue: pending
    issue_url: null
    merged_into: null

  - id: S2
    title: "What a booked visit becomes — vet, time of day, and the existing visit rows"
    scope: >
      Change Visit from `(pet_id, LocalDate visit_date, description)` to carry
      the vet and the time of day. Decide the representation — a single
      LocalDateTime start, a date plus a separate start-time column, or a
      foreign key to the S1 slot — decide whether a vet is required or
      optional on a visit, and decide what happens to visits that already
      exist: the four positional `INSERT INTO visits VALUES (...)` rows in
      each data.sql break the moment the column list changes. Schema change
      lands in all three databases with data.sql updated. Observable when the
      pet's visit list and the owner page show which vet and at what time.
    decision_focus: >
      This is the decision the task itself flags — the visit model cannot
      absorb this as a column. Whether a visit stores vet plus start time
      directly, or points at a published slot, decides which table owns the
      truth about a booking, how "taken" is computed, and what happens when a
      published slot is later withdrawn. Required-versus-optional vet is
      equally material: required forces a backfill or deletion of the legacy
      visit rows and closes off any path that creates a visit without a vet;
      optional leaves two kinds of visit in the system indefinitely. Both
      answers are defensible and they lead to different work.
    lens_used: decision-boundary
    disposition: pending
    disposition_rationale: null
    file_as_issue: pending
    issue_url: null
    merged_into: null

  - id: S3
    title: "Who publishes availability, and through what surface"
    scope: >
      The write path for availability. Decide whether open slots are published
      through a UI in the `vet` package — open to anyone with the URL, as every
      other page in this application is — or arrive only as seeded data for
      now. If a UI, decide its shape: bulk-publish a day or a week for one vet
      versus one slot at a time; how a vet withdraws a slot; and what happens
      to a withdrawn slot that someone has already booked. Display text comes
      from message keys, with the 10 locale bundles handled per AGENTS.md
      directives 3 and 4.
    decision_focus: >
      The task says "someone publishes what is open", and this codebase has no
      authentication, no roles and no notion of an acting user, so that
      sentence has no existing home. The three live options — an
      unauthenticated publishing page, seeded-only availability with no write
      path yet, or introducing some identity concept — differ enormously in
      cost and in how far they stretch the "no new architectural layers"
      directive. That is a call for a human before any spec is written, not
      something to settle inside an implementation.
    lens_used: decision-boundary
    disposition: pending
    disposition_rationale: null
    file_as_issue: pending
    issue_url: null
    merged_into: null

  - id: S4
    title: "The owner's booking journey, and the fate of today's free-date visit form"
    scope: >
      The headline path end to end: from the pet's page, pick a vet, see that
      vet's open slots, book one, and land back on the owner page with the
      visit shown. Decide the entry point and the order — pick a vet and then
      a slot, versus browse open slots across vets and pick one — and decide
      what becomes of the existing
      `/owners/{ownerId}/pets/{petId}/visits/new` form with its
      any-future-date, no-vet semantics: replaced by the slot flow, kept
      alongside it, or converted. Reuses `fragments/selectField`; behaviour
      proven with `@WebMvcTest` and MockMvc against status, view name, model
      attributes and rendered content.
    decision_focus: >
      Whether slot-booking replaces free-date visit creation or coexists with
      it is the user-visible decision with the widest blast radius. It decides
      whether the system has one booking concept or two, whether
      VisitController keeps its current POST and its
      `typeMismatch.visitDate` rejection, and whether the vet field from S2
      can ever be required. The vet-first versus slot-first ordering is a
      second material choice: it determines what the owner sees when their
      preferred vet has nothing open, which is the common case in a real
      clinic.
    lens_used: decision-boundary
    disposition: pending
    disposition_rationale: null
    file_as_issue: pending
    issue_url: null
    merged_into: null

  - id: S5
    title: "What happens when two owners want the same slot"
    scope: >
      Contention. The open-slot list an owner is looking at is already stale by
      the time they submit. Decide where the truth is enforced — a unique
      constraint landed in all three schema.sql files, a re-check at POST time
      before saving, or both — and decide what the losing owner sees: a
      rejected form carrying a translated error and a refreshed slot list, or
      a silently accepted double booking. Includes the message key and the
      locale-bundle handling for the rejection. Observable when two bookings
      of the same slot yield exactly one visit and a visible, translated
      message for the second.
    decision_focus: >
      This decides whether "open" is advisory or enforced, and clinics really
      do run both ways — some reject a collision outright, some permit a
      deliberate overbook. The enforcement choice is also a schema decision
      with a timing consequence: a unique constraint is far cheaper to land
      with the S1 table than to retrofit once double-booked rows exist, and it
      must land in h2, mysql and postgres together. A controller re-check
      alone leaves the race open under concurrency.
    lens_used: decision-boundary
    disposition: pending
    disposition_rationale: null
    file_as_issue: pending
    issue_url: null
    merged_into: null
---

## S1 — What published availability is — open slots as a modelled, visible concept — decision-boundary

**Context**

The `vet` package today holds `Vet`, `Specialty`, `Vets`, `VetRepository` and
`VetController`, which renders a paginated list and a JSON view. There is no
availability concept anywhere in the schema — `db/h2/schema.sql` has `vets`,
`specialties` and `vet_specialties`, and nothing about when a vet is free. The
task assumes published availability exists before an owner can book against it,
so something has to define what that is.

**Decision content**

What does a vet publish? Discrete slot rows — one row per bookable window, each
explicitly published — or a recurring weekly pattern from which open slots are
derived on demand? The two produce visibly different downstream work: different
tables, different queries, a different mechanism for marking a slot taken, a
different publishing surface in S3, and a different linkage from Visit in S2.
Alongside it sit three defaults that are user-visible and therefore a human's to
set: how long a slot is, how far ahead availability extends, and whether "open"
is a stored state on the slot or simply the absence of a booking pointing at it.

**Dependencies**

None. It can land against the current schema on its own. Every other slice
consumes its answer, and S5's enforcement choice is cheapest to apply while this
table is being created.

**Rationale**

This is the concept the whole feature rests on and the one the codebase is
entirely missing. Deciding it first means S2's visit shape, S3's publishing
surface and S4's booking journey are all specced against a settled model rather
than each inventing one. It passes the end-to-end filter on its own: seeded
availability rendered on a read-only surface is observable from the system's
edge without any booking or publishing code existing.

---

## S2 — What a booked visit becomes — vet, time of day, and the existing visit rows — decision-boundary

**Context**

`Visit` extends `BaseEntity` and carries a `LocalDate date` defaulted to
tomorrow plus a `@NotBlank description`. The `visits` table is
`(id, pet_id, visit_date, description)`, and each data.sql seeds four visits
with positional `INSERT INTO visits VALUES (...)` statements that break as soon
as the column list changes. The task states plainly that a vet and a time of day
cannot be bolted on as a column.

**Decision content**

How is the booking recorded? A single `LocalDateTime` start, a date plus a
separate start-time column, or a foreign key to the published slot from S1 —
each puts the truth about a booking in a different place, and that determines
how "taken" is computed and what happens when a published slot is withdrawn
after someone has booked it. Then: is a vet required on a visit or optional?
Required means backfilling or removing the four legacy rows and closing off any
path that creates a vet-less visit. Optional means two kinds of visit live in
the system permanently. The legacy rows must be dealt with either way, in all
three data.sql files.

**Dependencies**

Coupled to S1 in one direction only: if the answer is a foreign key to a slot,
S1's model must exist first. If the answer is vet plus start time held on the
visit, this slice can land before S1 and is independently observable through the
existing visit form.

**Rationale**

This is the decision the task explicitly raises, and it is the one that is most
expensive to reverse — it changes an entity, a table in three databases, the
seed data, and the form that creates visits today. Separating it from the
booking journey in S4 lets the model and the migration of existing rows be
reviewed on their own, before a new UI depends on the answer.

---

## S3 — Who publishes availability, and through what surface — decision-boundary

**Context**

The task says "someone publishes what is open". This application has no
authentication, no roles, and no concept of a logged-in actor — every page is
open to anyone who has the URL, and AGENTS.md directive 1 fixes the layering at
controller → repository → entity with no new architectural layers. So the
sentence, as written, does not yet have anywhere to live.

**Decision content**

Is there a publishing surface in this scope at all? The options are genuinely
different pieces of work: an unauthenticated publishing page under the `vet`
package, consistent with how the rest of the application behaves; seeded
availability only, with the write path deferred; or introducing some notion of
identity so that "a vet publishes" means something. If a page, its shape is also
a decision — bulk-publish a day or a week for one vet, or one slot at a time —
along with how a slot is withdrawn and what withdrawal does to a slot someone
has already booked.

**Dependencies**

Depends on S1 for the model it writes to. Independent of S2, S4 and S5 — booking
can be built and demonstrated against seeded availability.

**Rationale**

This is where the task's language quietly assumes a capability the system does
not have. Left unsliced, it would be resolved silently inside an implementation,
and the third option in particular would stretch the layering directive without
anyone deciding to. As its own slice, a human can accept the read-only path and
defer the write surface, or commit to a publishing page, on its own merits.

---

## S4 — The owner's booking journey, and the fate of today's free-date visit form — decision-boundary

**Context**

`VisitController` exposes one booking path today:
`GET`/`POST /owners/{ownerId}/pets/{petId}/visits/new`, rendering
`pets/createOrUpdateVisitForm`, defaulting the date to tomorrow, rejecting any
date not after today with `typeMismatch.visitDate`, then calling
`owner.addVisit(petId, visit)` and redirecting to the owner page with a "Your
visit has been booked" flash message. The task describes a different journey
starting from the same place: pick a vet, pick an open slot, book.

**Decision content**

Does slot-booking replace free-date visit creation, or coexist with it? That
single answer decides whether the system ends up with one booking concept or
two, whether the current POST and its date validation survive, and whether S2's
vet field can ever be made required. Second: pick a vet and then see their open
slots, or browse open slots across all vets and pick one — which determines what
an owner sees when their preferred vet has nothing open, and whether a vet can
be chosen at all when they have published nothing.

**Dependencies**

Depends on S1 (slots to show) and S2 (a visit that can record a vet and a time).
Independent of S3 — the journey works against seeded availability. S5 is layered
on top of this path rather than blocking it.

**Rationale**

This is the slice that delivers the task's headline sentence and the one an
owner would recognise. It is sliced apart from S2 because the model change and
the journey are separately reviewable, and because the coexistence question —
one booking concept or two — is a user-visible commitment that deserves its own
decision rather than being settled as a side effect of a form rewrite.

---

## S5 — What happens when two owners want the same slot — decision-boundary

**Context**

Once slots are published and bookable, the list an owner is looking at is a
snapshot. Between render and submit, another owner can take the same slot. The
current controller has no concept of a contended resource — it validates a date
and saves. There is no unique constraint on `visits` beyond the primary key, and
adding one later, across h2, mysql and postgres, is much harder once conflicting
rows exist.

**Decision content**

Where is the truth enforced, and what does the loser see? A unique constraint in
all three schemas, a re-check at POST time before saving, or both — and then,
separately, whether a collision is an error at all. Rejecting with a translated
message and a refreshed slot list is one product; permitting a deliberate
overbook is another, and real clinics do both. The rejection path also brings a
new message key and the 10 locale bundles into scope under AGENTS.md directives
3 and 4.

**Dependencies**

Assumes the S4 booking path exists to contend over, and the S1 model to enforce
against. Its enforcement half has a timing dependency worth noting: if the
answer is a unique constraint, it is markedly cheaper to land with S1's table
creation than to retrofit.

**Rationale**

Contention is the difference between availability that means something and
availability that is decorative, and it is the part most likely to be quietly
dropped if it is not named as its own decision. It passes the end-to-end filter
cleanly — two bookings of the same slot producing exactly one visit and one
visible, translated error is observable behaviour a MockMvc test can assert.

---

## Sequencing recommendation

S1 is the foundation: it defines what availability is, and S3, S4 and S5 all
consume that answer.

S2 is coupled to S1 in one direction only. If a visit points at a published
slot, S1 must land first. If a visit carries a vet and a start time of its own,
S2 can land before S1 and is independently observable through today's visit
form — which makes S2 a viable first slice if the human wants the model change
settled before any new concept enters the codebase.

S4 needs both S1 and S2. S3 needs only S1 and is independent of S4: the booking
journey can be built and demonstrated against seeded availability, and the
publishing surface can be built and demonstrated without anyone booking. Either
can be deferred without blocking the other.

S5 sits on top of S4 for its observable behaviour, but carries one ordering
warning: if its answer is a database unique constraint, that constraint is far
cheaper to land while S1 is creating the table than to retrofit across three
schemas once double-booked rows exist. Worth settling S5's enforcement question
before S1 is specced, even if S5 itself ships last.

Recommended chain if shipping incrementally: S1 → S2 → S4 (the headline
journey) → S5 → S3, with S3 pulled forward if the publishing surface is judged
part of the minimum rather than seeded data.

## Explicitly not slicing on

- **One slice per database.** AGENTS.md directive 2 and the `SchemaParityTest`
  constraint in HARNESS.md make h2, mysql, postgres and their data.sql files a
  single obligation inside whichever slice changes the schema. Splitting them
  would yield three slices, two shipping nothing observable and one failing the
  harness.

- **One slice per layer.** Entity, repository, controller, template is a
  code-organisation cut, not a decision cut, and each piece ships nothing an
  owner or a vet could see. Rejected by the end-to-end lens and at odds with the
  project's controller → repository → entity shape.

- **i18n as its own slice.** Message keys and the 10 locale bundles are a
  per-change obligation under AGENTS.md directives 3 and 4, including the rule
  that a missing trustworthy translation goes into `messages.properties` only,
  is allowed to fail `I18nPropertiesSyncTest`, and is flagged for a human. That
  is a step inside each slice, not a decision of its own.

- **Decision records and formatting as slices.** `decisions/<date>-<slug>.md`
  accompanies every change touching `src/` under HARNESS.md, and
  spring-javaformat runs on every build. These attach to each slice's pull
  request.

- **Clustered into S1:** slot duration, booking horizon, and how a slot is
  marked taken were each plausible candidates. All three are field-level
  consequences of the availability model and share its decision, so they are
  folded in rather than restated as separate slices.

- **Clustered into S2:** showing the vet's name and the time on the pet's visit
  list and the owner page. That display is the observable surface of the same
  model change, not an independent decision.

- **Considered and rejected — an inseparability claim over S1 and S2.** The
  visit shape and the availability model are genuinely coupled, but the coupling
  is sequential rather than atomic: each ships something observable on its own,
  and a human can settle one and then the other without correctness suffering.
  Claiming inseparability here would have collapsed two real decisions into one
  oversized proposal.

- **Dropped as adjacent features, not this task:** cancelling or rescheduling a
  booked visit, notifying an owner that a booking was made, vet working-hours
  and holiday calendars, and choosing a vet by specialty. The task covers
  publishing availability and booking against it; each of these deserves its own
  task rather than being smuggled in.

- **Not slicing on commits or pull-request size.** Neither is a cognitive-budget
  boundary.
