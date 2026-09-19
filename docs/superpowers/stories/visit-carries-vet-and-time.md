---
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
date: 2026-09-19
mode: spec
cartographer_model: claude-opus-5
stories:
  - id: 1
    lens: [patterns, consequences]
    title: Visit reaches across into the vet package
    disposition: pending
    disposition_rationale: null
  - id: 2
    lens: [patterns, consequences]
    title: Required enforced in controller and schema, not entity
    disposition: pending
    disposition_rationale: null
  - id: 3
    lens: [defaults, consequences]
    title: Tomorrow at nine lives in the constructor
    disposition: pending
    disposition_rationale: null
  - id: 4
    lens: [forces, consequences]
    title: A visit is an instant, never an interval
    disposition: pending
    disposition_rationale: null
  - id: 5
    lens: [alternatives, consequences]
    title: Every vet is bookable by default
    disposition: pending
    disposition_rationale: null
  - id: 6
    lens: [forces, coherence]
    title: New labels keyed, their neighbours left English
    disposition: pending
    disposition_rationale: null
  - id: 7
    lens: [consequences]
    title: The confirmation still says booked, in English
    disposition: pending
    disposition_rationale: null
  - id: 8
    lens: [consequences, alternatives]
    title: Vets are written about, never written to
    disposition: pending
    disposition_rationale: null
  - id: 9
    lens: [alternatives, consequences]
    title: Seed data models a clinic nothing enforces
    disposition: pending
    disposition_rationale: null
  - id: 10
    lens: [coherence]
    title: Constraint spent on presence, not on truth
    disposition: accepted
    disposition_rationale: "Send #10 back to the spec — one sentence stating the position, so the later slices inherit it deliberately."
---

# Choice stories — visit carries a vet and a time of day

Spec mode, against the revised spec (D1–D7, FR-1..FR-17, AS-1..AS-13 and the
adjudication section), its plan, the accepted slice record, `AGENTS.md`,
`HARNESS.md`, `REFLECTION_LOG.md`, and the source the change touches.

The spec announces nine decisions — D1–D7 plus the two adjudications recorded in
*Adjudication of objections*. None of those are re-narrated here. Every story
below is a decision the spec makes without announcing it: implicit in a
requirement's wording, in a scenario's shape, in the plan's file table, or in
what the spec leaves untouched. Where a silent choice turned out to be a
restatement of a loud one it was dropped rather than padded in; two such drops
are named in the notes where they are most likely to be missed.

Every disposition is `pending`. Nothing here is a recommendation.

## Story #1 — Visit reaches across into the vet package

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-1, FR-11; plan, *Entity and controller*)
**Lens:** patterns, consequences
**Refs:** —

**Context.** FR-1 says a visit records exactly one vet and FR-11 makes it a
foreign key to `vets`. The plan turns that into `@ManyToOne @JoinColumn(name =
"vet_id") private Vet vet` on `owner/Visit.java`, plus a `VetRepository` in
`VisitController`'s constructor. That is the first time main-source code in the
`owner` package refers to the `vet` package: today the only main-source crossing
anywhere is `PetClinicRuntimeHints`, and the two feature packages are otherwise
strangers to each other.

**Forces.** Object navigation versus package independence. A `Vet` reference
gives the template `visit.vet.firstName` for free and lets Spring's conversion
service do the binding. An `int vetId` would keep the packages disjoint and
force a lookup wherever the name is needed. AGENTS.md directive 1 fixes the
layering but says nothing about edges *between* feature packages, so the spec
resolved a question the directives do not cover.

**Options not taken.** Store `vetId` as a plain column on `Visit` and resolve
names at the controller, keeping `owner` free of `vet` types. Put the
association on the `vet` side (a vet's visits), making `vet` the depending
package instead. Introduce a small shared type in `model` — rejected in advance
by directive 1's "shared base types in `model`" being about base types, and by
"no abstraction for a single caller".

**Choice as written.** The visit holds the `Vet` object, so `owner` now depends
on `vet` and the dependency is one-way and permanent. The spec never states this
as a decision; it arrives as a consequence of choosing a foreign key.

**Consequences.** The owner page's fetch graph now reads owner → pets → visits →
vet → specialties, since `@ManyToOne` defaults to eager and `Vet.specialties` is
explicitly `FetchType.EAGER`. Two paths to the same `Vet` now coexist: the
`@Cacheable("vets")` `findAll()` used by the chooser, and the JPA-managed
instance loaded through the visit graph — equal by id, not necessarily the same
object, and not invalidated together. And when S1 introduces availability in the
`vet` package, it meets a `vet` package that the `owner` package already
compiles against, so changes to `Vet` now have a blast radius in both.

**Pattern.** Evans (*Domain-Driven Design*, 2003) on aggregate boundaries —
"reference other aggregates by identity" is the guidance this chooses against.
The visit is inside the Owner aggregate (it is cascaded from `Pet`), and it now
holds a direct object reference to the root of a different one.

**Notes.** —

## Story #2 — Required enforced in controller and schema, not entity

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-1, FR-2, FR-5, FR-6, FR-11; plan, *Algorithm notes*)
**Lens:** patterns, consequences
**Refs:** #1

**Context.** "A visit cannot be created without one" appears twice in the
requirements table, and it is realised in two places that are not the entity:
`NOT NULL` in three `schema.sql` files, and two `result.rejectValue(...,
"required")` calls in `VisitController`. `Visit` already carries `@NotBlank` on
`description`, so after this change the same entity states one of its invariants
about itself and two of them nowhere.

**Forces.** Error-message economics against invariant locality. The plan is
explicit about the first — bean validation would emit `NotNull.*` codes that no
locale bundle carries, whereas `rejectValue("vet", "required")` reuses an
already-translated key. Nothing weighed the second: that an entity which cannot
defend itself must be defended identically by every future creation path.

**Options not taken.** `@NotNull` on both fields with a `NotNull.visit.vet` key
added to eleven bundles. `@NotNull` plus a message-code resolver mapping
`NotNull` onto the existing `required` key — one line of configuration, no new
translations, invariant on the entity. A constructor or factory that refuses to
build a vet-less visit, which the form path would never exercise but S4's path
would inherit.

**Choice as written.** Requiredness lives at the two ends and not in the middle.
The spec chose this by writing FR-5 and FR-6 as statements about *the form*
("submitting with no vet chosen redisplays the form") rather than about the
visit.

**Consequences.** S4's booking journey, whatever shape it takes, must re-derive
both checks or it will produce a `PropertyValueException` from Hibernate instead
of a field error — the database will catch what the model did not. This is the
exact shape `REFLECTION_LOG.md` already recorded on 2026-09-16: an
`Owner.email` column limited in the schema with no matching entity constraint,
so over-length input failed as a database error rather than a form error. The
proposed constraint ("every length-limited column has a matching entity
constraint") was captured and never accepted, so nothing in the harness notices
the same shape recurring here.

**Pattern.** Anemic Domain Model with Transaction Script validation (Fowler,
*PoEAA* 2002, and "AnemicDomainModel" 2003). It is the house style — the
existing controller validates the date the same way — so this is the pattern
being extended rather than introduced.

**Notes.** —

## Story #3 — Tomorrow at nine lives in the constructor

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (D7, FR-4, AS-1; plan, `owner/Visit.java`)
**Lens:** defaults, consequences
**Refs:** #2

**Context.** D7 is announced as a decision about the *form*: "The form pre-fills
tomorrow's date … and pre-fills a start time of 09:00." The plan implements it in
`Visit`'s no-arg constructor, alongside the `date = LocalDate.now().plusDays(1)`
that is already there. The unannounced part is the move from "the form offers"
to "every visit is born".

**Forces.** Symmetry with the existing line against the domain meaning of a
default. The inherited constructor already puts a presentation default in the
entity, so adding a second one is the smallest diff and the plainest read —
directly in line with the style directives. Against that: `new Visit()` stops
being a neutral act, and an entity that always has a plausible-looking time is
harder to distinguish from one that was given a real one.

**Options not taken.** Seed the defaults in `VisitController.loadPetWithVisit`,
where the form model is assembled, leaving the entity blank. Seed them in the
template via `th:value`, which is where AS-1 actually observes them. Leave the
time blank and let the required-field error teach the user, which is the option
D7's own rationale rejects.

**Choice as written.** The default is a property of the type. The provenance is
upstream Spring PetClinic's `Visit()` constructor, not a decision this team
made; the spec extends an inherited default without naming it as inherited.

**Consequences.** Every `Visit` constructed anywhere in the system — controller,
unit test, integration fixture, and whatever S4 builds — starts life at 09:00
tomorrow unless something overwrites it. A slot-booked visit in S4 that fails to
set the time is not empty, it is 09:00, and no reader downstream can tell the
difference between a default that survived and a time someone meant.

**Pattern.** — The closest honest description is "inherited upstream convention",
which is a provenance rather than a pattern.

**Notes.** There is a further finding here that is failure-shaped rather than
choice-shaped — what a constructor default does to a scenario whose precondition
is an absent field — and it is adjacent to O5's reasoning about AS-5. It is
named, not elaborated, because it belongs in the objection record and not in a
choice story.

## Story #4 — A visit is an instant, never an interval

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (D1, FR-2, FR-14; plan, *Schema*)
**Lens:** forces, consequences
**Refs:** O10, #1

**Context.** D1 settles the representation question as "date plus a separate
start time" and stops there. `TIME NOT NULL` in three dialects, `LocalTime` on
the entity, `HH:mm` on screen. Nothing in the spec gives a visit a length, an end,
or a granularity — a visit is a point on the clock, to whatever precision the
column and the browser happen to allow.

**Forces.** Additive cheapness against schedule arithmetic. D1's whole argument
is about migration cost, and one column is cheaper than two. But a clinic's
schedule is made of intervals: "is this vet free at 14:30" is unanswerable from
start times alone, and it is the question S1 and S5 both exist to answer.

**Options not taken.** A duration or end-time column alongside the start,
defaulted to the clinic's standard appointment length. A stated granularity —
the `step` attribute on the time control, or a check constraint — so that starts
land on slot boundaries before slot boundaries exist. Recording nothing but
deferring explicitly, which is what the spec does for contention (AS-13) and
does not do here.

**Choice as written.** The spec chose "a visit is an instant" by not addressing
duration. The plan half-notices — "`start_time` … also leaves room for an end
time later without renaming" — which records the column-naming convenience and
not the modelling decision it implies.

**Consequences.** When S1 sets a slot duration, every visit created between now
and then is a start with no extent, and there is nothing to say whether a 14:07
start is legal, or which slot it belongs to. "Taken" cannot be computed from
`visits` alone at any granularity, which means S1's availability model and S5's
contention rule both have to invent the missing half retroactively, for rows
that already exist in three databases.

**Pattern.** Fowler's *Analysis Patterns* (1996) Range / Time Period — the
pattern for "a booking occupies an interval" is well established and the spec
models the degenerate case instead.

**Notes.** This is deliberately not O10. O10 objects that `visit_date` and
`start_time` are not bound to each other and will have to be matched to slots by
value. This story is about the booking having no extent at all, which is a
different gap and is still open even if O10's remedy is applied.

## Story #5 — Every vet is bookable by default

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-3, AS-1, D4)
**Lens:** alternatives, consequences
**Refs:** O5

**Context.** FR-3 says the chooser offers "every vet in the clinic", and AS-1
repeats it: "listing every vet in the clinic by name". The plan realises it with
`vets.findAll()`. The unannounced decision is that the staff list *is* the
booking vocabulary — the system gains no way to express a clinician who is not
taking bookings.

**Forces.** Six seeded vets and `findAll()` already existing, against the fact
that "who works here" and "who can be booked" are different questions in any
clinic. The spec resolved toward the existing query without naming the second
question, and D4 ("any vet, any time") reads as though it were only about
availability when it is also about eligibility.

**Options not taken.** A `bookable` flag on `vets`, landed in the same
three-dialect change while the table is being touched anyway — the same timing
argument the slice record makes for S5's unique constraint. Filtering the chooser
by something already modelled, e.g. specialty, which the slice record explicitly
dropped as an adjacent feature. Deferring the chooser's contents to S1, so that a
vet appears once they have published availability — which is exactly what a
slot-first journey would do.

**Choice as written.** Every row in `vets` is a bookable option, forever, by
virtue of existing. The spec wrote it as a requirement rather than a decision,
which makes it harder to revisit: changing the chooser's population later means
changing FR-3.

**Consequences.** When S1 lands, there are two competing definitions of who can
be asked for — "every vet" (this spec) and "every vet with an open slot" (S1's
natural reading) — and this one is already in a functional requirement and a
passing test. A vet who leaves the clinic can only be removed from the chooser by
deleting the row, which the new foreign key now forbids while any visit
references it.

**Pattern.** Suspected rather than certain: this is the absence of Fowler's
*Analysis Patterns* Party/Accountability distinction — `Vet` is treated as a
party with exactly one implicit, permanent role, so role-scoped questions have
nowhere to attach.

**Notes.** O5 is about a different property of the same control — whether the
chooser has a blank option and therefore whether "no vet chosen" is reachable.
This story is about what the chooser contains and who decides it; both can be
true at once.

## Story #6 — New labels keyed, their neighbours left English

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-15, AS-12; plan, *Templates and messages*)
**Lens:** forces, coherence
**Refs:** O6

**Context.** FR-15 and AS-12 require the new time and vet text to come from
message keys. The form they are being added to already carries two hardcoded
English labels: `createOrUpdateVisitForm.html` lines 32–33 pass the literals
`'Date'` and `'Description'` into `fragments/inputField`. AS-12 is scoped to
table *headings*, which do use keys, so the change ships a form where the two new
fields are internationalised and the two existing ones beside them are not.

**Forces.** AGENTS.md directive 3 (no hardcoded display text in templates) pulls
one way; directive 9 (no unrelated reformatting — every changed hunk needed by
the change's purpose) and "smallest diff" pull the other. Two literals and two
existing keys would have been a two-line fix, so the cost is not what decided
it; the spec resolved a standing directive conflict silently, in favour of
directive 9.

**Options not taken.** Key the two existing labels in the same change and say so
in the decision record, treating a directive-3 violation in the file you are
editing as within the change's purpose. Leave all four as they are, accepting an
untranslated form as the honest status quo. Record the existing violation as a
known exception rather than passing over it.

**Choice as written.** The spec chose "the new text is keyed, the adjacent text
stays as it is" without noting that the adjacent text is a directive-3 violation
sitting in the file the change edits. A future reader of this form sees two
idioms and no explanation.

**Consequences.** The form is now half-translated, and the half that is
translated is the newer half — an inversion of the usual drift that makes the
violation look deliberate. There is a second, quieter gap in the same area: the
spec and the plan name the *keys* `visitTime` and `vet` and never state the
English words they hold, while FR-15 obliges a trustworthy translation of those
words into ten bundles and AGENTS.md directive 10 names user-visible naming as a
decision that must be written down. Twenty translations are required of a string
nobody has decided.

**Pattern.** —

**Notes.** O6 covers what happens to the change when a translation is not
available. This story is about which text is in the system at all, and about the
words themselves being undefined; the two meet if a translator asks what `vet`
means and finds no answer in the spec.

## Story #7 — The confirmation still says booked, in English

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-8, *User story*, *Adjudication of objections*)
**Lens:** consequences
**Refs:** O1, #6

**Context.** The O1 remedy narrowed the user story: the spec now claims only that
a visit records the vet being asked for, and explicitly declines the alternative
remedy — "The alternative remedy — request-versus-confirmation wording on the
screens — was considered and **not** chosen". FR-8 keeps "the existing
confirmation message". That message is
`redirectAttributes.addFlashAttribute("message", "Your visit has been booked")`
at `VisitController.java:110` — a hardcoded English sentence, not a message key.

**Forces.** Minimum diff and an untouched existing behaviour against the fact
that this one sentence is the system's only assertion about what just happened.
The spec weighed screen wording once, at the adjudication, and decided against
changing it; what it did not weigh is that the sentence in question is also the
only user-visible string in this flow living outside the message-key system.

**Options not taken.** Replace the literal with a key, which brings the sentence
under FR-15 and under the narrowed story in the same move. Leave the wording and
key it anyway, separating the i18n question from the claim question. Record it as
a known exception in the decision record, so that the next reader knows it was
seen.

**Choice as written.** The spec chose, by silence, that the strongest claim the
product makes ("booked") is the one piece of text this change does not govern.
Directive 3 is scoped to templates, so no rule is broken — which is precisely why
nothing will surface it.

**Consequences.** After this change the owner page shows a vet's name, a time,
and an English sentence saying the visit has been booked, while the spec's own
narrative says the system agreed to none of it. The narrowing lives in a markdown
file and the claim lives on the screen. Whoever specs S4 or S1 and wants to
introduce request-versus-confirmation wording starts from a hardcoded literal in
a controller rather than a key, which is a slightly wider change than they will
expect.

**Pattern.** —

**Notes.** —

## Story #8 — Vets are written about, never written to

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-9, FR-10, FR-16, *Scope*)
**Lens:** consequences, alternatives
**Refs:** #1

**Context.** The change makes the system hold, for the first time, a statement
about a named clinician's day. The two screens FR-9 and FR-10 name are both
owner-facing. The `vet` package's surfaces — a paginated list and a JSON view —
are untouched. Nothing anywhere shows a vet what has been recorded against them.

**Forces.** The slice's observability requirement was satisfiable entirely on the
owner side, and every acceptance scenario is written from the owner's seat. A
vet-facing view would have been new work in a slice whose whole argument is
additive cheapness. Against that: the data now exists, it concerns a person, and
that person has no way to see it.

**Options not taken.** Add a vet's-day column or page to `VetController` in the
same change, which needs no new model. State in the non-requirements list that
no vet-facing surface exists and name the slice that will own it. Record the
question as one for a human to route, since none of S1, S3, S4 or S5 currently
owns "what a vet sees" — S1 and S3 are about publishing availability, which is
the opposite direction.

**Choice as written.** The spec chose a one-sided audience by scoping every
scenario to the owner. It is not listed among the non-requirements, so the gap
reads as an oversight rather than a decision — which is precisely the failure
mode AS-13 was written to avoid for contention.

**Consequences.** Booked visits reach the vet out of band or not at all, and the
first slice that wants a vet's schedule inherits the whole surface. There is a
smaller consequence worth recording alongside it: `Vet` extends the same `Person`
base as `Owner`, so the `firstName`/`lastName` fields that directive 6 keeps out
of all output are now rendered to customers under a different role. FR-16 reasons
this through by precedent — the name is already on the vets page — and the
project ends up with a rule scoped to the owner role and no stated rule about
rendering a clinician's name to a customer.

**Pattern.** —

**Notes.** —

## Story #9 — Seed data models a clinic nothing enforces

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (D3, FR-12, AS-9; plan, *Seed data*)
**Lens:** alternatives, consequences
**Refs:** O9, #5

**Context.** D3 decides that the four seeded visits are backfilled rather than
deleted — that part is loud, and the diaboli explicitly declined to object to it.
The silent part is the content of the backfill. The plan assigns vets 1, 2, 3 and
4 and times 09:00, 10:30, 14:00 and 15:30: one visit per vet, no two visits
sharing a vet and time, no pet with two visits on one date, and two of the six
vets (Henry Stevens, Sharon Jenkins) never appearing at all.

**Forces.** Plausibility against demonstrativeness. Tidy, non-colliding sample
data reads as a well-run clinic and makes the canonical demo screen look right.
But this seed set is also the project's shared fixture — `ClinicServiceTests`
runs against it — and the behaviours the spec deliberately permits are the ones
the data never shows.

**Options not taken.** Seed one deliberate collision — two visits, same vet, same
date and time — so that FR-17's permissiveness is visible on a screen and in a
fixture rather than only in AS-13's parenthesis. Seed two visits for one pet on
the same date, so that D6's ordering rule is observable in the demo at all.
Spread the four visits across fewer vets so the repeated-vet case exists.

**Choice as written.** The seed depicts a schedule obeying rules the system does
not have: one vet per slot, no double-booking, evenly spread. The spec chose this
by delegating the content to the plan's table and never saying what the sample
data should demonstrate.

**Consequences.** D6 ships with no seeded example, so nobody opening the running
application will ever see same-day time ordering. FR-17 ships with no seeded
example either, which compounds O9's point from the other direction: neither a
test nor the demo data shows that collisions are allowed, so a later change
adding a unique index on `(vet_id, visit_date, start_time)` finds nothing —
green tests and clean demo data — standing in its way. Sample data is how most
readers infer a schema's rules, and this sample data teaches invariants that do
not exist.

**Pattern.** Standard Fixture (Meszaros, *xUnit Test Patterns*, 2007) doing
double duty as demonstration data — one artefact serving a teaching purpose and a
verification purpose, with no statement of which one governs its content.

**Notes.** —

## Story #10 — Constraint spent on presence, not on truth

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (whole document)
**Lens:** coherence
**Refs:** O1, O2, O9, #2, #4, #9

**Context.** Count what this change adds to the database: `vet_id NOT NULL`,
`start_time NOT NULL`, and a foreign key to `vets`, in three dialects. Now count
what it declines to add: no uniqueness on vet-and-time, no binding between date
and time, no bound on how far ahead a visit may be, no notion of working hours,
no duration. Every constraint the spec buys is about a field being *present*;
none is about the value being *true*.

**Forces.** Integrity constraints cost the same to add regardless of which
question they answer, and the spec is otherwise ruthless about cost — D1 rejects
a three-dialect type migration as too expensive. The asymmetry between D1's cost
standard and D2's is now named in the spec, post-O2. What is not named is the
asymmetry this story is about: the constraint effort lands exactly where it makes
S4 more expensive, and declines exactly where the product risk sits.

**Options not taken.** Spend the same three-dialect change on a constraint that
protects a product rule — the slice record's own argument is that a unique
constraint is far cheaper to land while a table is being altered than to
retrofit. Spend none of it and land both columns nullable, leaving the rules at
the controller, which is O4's third option. Spend it as written and say, in the
spec, that presence is being guaranteed and meaning is not.

**Choice as written.** Strict about shape, silent about meaning — and both
adjudications pushed the same way. O2 was closed by keeping `NOT NULL` and naming
the cost; O1 was closed by narrowing the prose and explicitly leaving the screens
saying what they said. The model got tighter; what the system claims to be true
did not change at all.

**Consequences.** The database will guarantee, forever and in three dialects,
that every visit names a vet and a time, and will guarantee nothing about whether
that pair means anything — the pair has no extent (#4), no enforcement (O9), no
demonstration (#9), and is defended only by a controller (#2). That is a
coherent position, but it is a position, and the spec does not state it. The
cheapest thing to do with this story is to make the sentence explicit in the spec
so S1, S4 and S5 inherit a stance rather than an accident.

**Pattern.** — This is the coherence lens in Alexander's sense: the decisions are
individually defensible and the story they tell together was never written down.

**Notes.** This is the only coherence story in the record, used once, deliberately.
