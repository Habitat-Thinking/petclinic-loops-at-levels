---
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
date: 2026-09-19
revised: 2026-09-19 — delta pass addendum over the post-record revisions (stories #11-#18)
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
    disposition: accepted
    disposition_rationale: "Labels decided: visitTime = 'Visit Time', vet = 'Veterinarian'. And: key the adjacent Date and Description labels in the same change — keys exist, no new translations."
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
  - id: 11
    lens: [patterns, defaults]
    title: Parsing a vet becomes an application-wide fact
    disposition: pending
    disposition_rationale: null
  - id: 12
    lens: [consequences, coherence]
    title: Visit Time beside Date, on one screen only
    disposition: pending
    disposition_rationale: null
  - id: 13
    lens: [forces, alternatives]
    title: English chosen for how well it translates
    disposition: pending
    disposition_rationale: null
  - id: 14
    lens: [patterns, consequences]
    title: A cleanup rule invented, used once, left in a spec
    disposition: pending
    disposition_rationale: null
  - id: 15
    lens: [consequences]
    title: The rule stops at the field labels
    disposition: pending
    disposition_rationale: null
  - id: 16
    lens: [consequences, patterns]
    title: A German word becomes a build-gating fact
    disposition: pending
    disposition_rationale: null
  - id: 17
    lens: [coherence, consequences]
    title: A stance stated where nothing carries it
    disposition: pending
    disposition_rationale: null
  - id: 18
    lens: [consequences, alternatives]
    title: The slice record still says the opposite
    disposition: pending
    disposition_rationale: null
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

---

# Addendum — 2026-09-19, delta pass over three revisions

Composed pass, not a plugin mode. The `/choice-cartograph` command runs once in
spec mode and overwrites its record on re-dispatch, which would have discarded
the dispositions on #6 and #10. This addendum was dispatched instead: the same
agent, briefed to answer what is new, what is now stale, and what the remedies
cost. The original record above is unaltered.

**Read mode for `REFLECTION_LOG.md`:** bounded (whole file scanned; it holds one
entry, 2026-09-16, already cited by story #2 — nothing new since the original
record).

**A correction that governs this whole pass, stated first because it changes what
"new" means.** The briefing lists the objection closure (O1/O2) as revision 1 of
three. It was not new to this record. The original preamble reads "against the
revised spec (D1–D7, FR-1..FR-17, AS-1..AS-13 and the adjudication section)";
story #7 quotes the O1 remedy's "was considered and **not** chosen" clause
verbatim, and story #10 says the D1/D2 cost asymmetry "is now named in the spec,
post-O2". Stories #1–#10 were written against the post-adjudication spec. Only
two revisions are genuinely new to me: story #10's stance subsection and story
#6's D8 / FR-18 / AS-14 / T-19 package, plus the plan as it now stands, which I
had seen only in its pre-VetFormatter form. Section C therefore reports one
choice arising from O2 that the original pass had available and did not surface
— recorded as a miss, not as a novelty.

Eight new stories. All `pending`. Nothing here revisits #6's or #10's
disposition.

# A. What is new

## Story #11 — Parsing a vet becomes an application-wide fact

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time-plan.md` (*Entity and controller*, `vet/VetFormatter.java`; *Algorithm notes*, "How a chosen vet becomes a `Vet`"; *Risks*)
**Lens:** patterns, defaults
**Refs:** #1, #5

**Context.** The plan now introduces a class the spec never mentions:
`vet/VetFormatter.java`, a `Formatter<Vet>` that prints a vet as its id and
parses an id back by looping `vets.findAll()`. It is annotated `@Component`,
and the plan is explicit about what that means — "registered globally exactly as
`PetTypeFormatter` is". One form needs to turn a submitted id into a `Vet`; the
mechanism chosen to do it is an entry in the application's conversion service,
which is ambient and applies to every binding of the `Vet` type anywhere in the
application, now and afterwards.

**Forces.** Symmetry against scope. `PetTypeFormatter` exists, does exactly this
shape of work, and is already wired into a `@WebMvcTest` by `includeFilters` —
so mirroring it is the plainest construction a newcomer can read and the
smallest thing to explain. Against that: a global type converter is the widest
possible registration for a need that has exactly one call site, and AGENTS.md's
"no abstraction for a single caller" is a directive this plan satisfies only by
pointing at the neighbour that broke it first.

**Options not taken.** An `@InitBinder` in `VisitController` registering the
same conversion for that controller alone, which keeps the blast radius at one
form. A plain `Integer vetId` form field resolved by `vets.findById` in
`processNewVisitForm` — the same option story #1 named, which would have kept
both the package edge and the converter out of the change. Putting the formatter
in `owner` beside the form that needs it, which is where `PetTypeFormatter`
sits relative to its consumer, and which the plan silently declines by choosing
"lives with the type" over "lives with the caller".

**Choice as written.** `Vet` gains one canonical wire form — its id — decided
once, globally, in a class whose stated justification is that another class in
the codebase looks like it. The spec does not mention the formatter at all, so
this arrives entirely through the plan.

**Consequences.** Three things become true that were not. First, `print` returns
an id, so any future `th:field` over a `Vet` renders a number; the vet's *name*
appears only where a template asks for it explicitly, as the plan's `selectVet`
fragment does. Two printed forms of the same type now coexist, chosen per call
site. Second, S4 inherits the converter: whatever its booking journey posts, if
it posts a `Vet` it posts an id, and a slot-keyed or name-keyed binding has to
override a global registration rather than choose its own. Third, the test slice
crosses packages — `VisitControllerTests`, an `owner` test, must name a `vet`
class in its `includeFilters` clause, which is the first time a test in one
feature package has had to reach into another. Read with #1, the two packages
are now joined at compile time, at fetch time and at test-configuration time.
And `parse` scans the `@Cacheable("vets")` collection on every submission, which
is fine at six vets (#5) and is a linear scan of a cache whose invalidation
nobody owns.

**Pattern.** Spring's `Formatter` SPI is a Strategy (GoF 1995) selected by target
type from an ambient registry — which makes the registry a Service Locator
(Fowler, "Inversion of Control Containers and the Dependency Injection Pattern",
2004) for conversions. The choice worth naming is registry-scoped over
call-site-scoped: the binding is resolved by type lookup, so no reader of
`VisitController` can see where a submitted id becomes a `Vet`.

**Notes.** The plan's risk note — "A `Formatter` is not in the web slice by
default; T-1..T-10 need the `includeFilters` clause, or every vet parameter
binds to null and the failures look like validation failures" — is
failure-shaped and belongs to the diaboli, not here. It is named because it is
the tell: a mechanism whose absence is indistinguishable from a validation
result is a mechanism that is invisible at the call site, which is this story's
subject.

## Story #12 — Visit Time beside Date, on one screen only

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (D8, FR-15, FR-18); `src/main/resources/templates/owners/ownerDetails.html:63`, `pets/createOrUpdateVisitForm.html:32,48`
**Lens:** consequences, coherence
**Refs:** #6

**Context.** D8 picked `visitTime` = `Visit Time` precisely because it is "an
exact parallel of the existing `visitDate` = `Visit Date`". FR-18, landing in the
same revision, keys the visit form's date label to the *other* existing key,
`date` = `Date`. The two screens do not use the same key for the same column
today: `ownerDetails.html:63` heads it `#{visitDate}`, and
`createOrUpdateVisitForm.html:48` heads it `#{date}`. So the mirror D8 reasons
from holds on one screen and breaks on the other.

**Forces.** Translation tractability against label coherence, resolved for the
first and not weighed for the second. D8's whole argument is about what a
translator can derive; FR-18's whole argument is about closing a directive-3
violation at zero translation cost. Both are sound in isolation. Neither asked
what the four headings read like in a row once both have landed, which is the
only place a user meets them.

**Options not taken.** Key FR-18's date label to `visitDate` rather than `date`
— still an existing key, still translated in all ten bundles, still zero new
translations, and the form's label and heading would then match the owner page's.
Key the new time to `time` rather than `visitTime`, mirroring the form's existing
generic pair instead of the owner page's specific one. State in the spec which
of the two existing idioms is the project's, and let the odd one out be a known
inconsistency rather than a silently extended one.

**Choice as written.** The spec chose a specific-noun key for the new column and
a generic-noun key for the old one, on the same form, by deciding each in a
different revision for a different reason and never putting them side by side.

**Consequences.** After this change the visit form's previous-visits table reads
`Date | Description | Visit Time | Veterinarian` while the owner page's reads
`Visit Date | Description | Visit Time | Veterinarian`. In German that is
`Datum | Beschreibung | Besuchszeit | …` against
`Besuchsdatum | Beschreibung | Besuchszeit | …` — the divergence survives
translation and is visible in every locale, because it is a key choice and not a
wording one. FR-18's own justification was that shipping two idioms on one form
"explains itself to nobody"; the change closes the keyed-versus-literal idiom
split and opens a specific-versus-generic one in the same two lines. It is
cheaper to fix now, while the bundles are being edited anyway, than after two
keys have been translated twenty ways.

**Pattern.** — The closest named thing is the Ubiquitous Language test (Evans
2003): one concept, one name, everywhere it appears. The concept here is "the
day a visit happens" and it has two names in one codebase.

**Notes.** Deliberately not a restatement of #6. #6 was about which text is keyed
at all, and is disposed. This is about *which key*, a question that only came
into existence when #6's remedy chose `date` over `visitDate`.

## Story #13 — English chosen for how well it translates

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (D8); plan, *Constraints this plan is shaped by*
**Lens:** forces, alternatives
**Refs:** O6, #6

**Context.** D8 decides the two English strings and gives one reason for both:
"translation tractability". `Visit Time` because a translator holding
`visitDate=Besuchsdatum` can write `Besuchszeit`; `Veterinarian` because every
bundle already carries the plural `vets`. The criterion is not what the clinic
calls the thing, what the codebase calls it, or what fits on the form — it is
how cheaply the word can be derived from a word already in the bundle.

**Forces.** The cost of twenty trustworthy translations, under a directive that
forbids the cheap way out (directive 4) and a harness gate that blocks the change
if any bundle is short (O6). That pressure is real, and D8 is a genuinely clever
answer to it. What it trades away, unnamed: the English word is now downstream of
the translation problem rather than upstream of it, and the product's vocabulary
is set by what is already in `messages*.properties`.

**Options not taken.** Name the concept first — the spec, the slice record, the
schema column, the entity field and the Java package all say *vet*, and
`vet=Vet` would have matched every other artefact in the project. Reuse
`visitDate`'s sibling only where it is already used, and accept a wider
translation ask for a better word. Take the other half of the route O6 pointed
at — derive from existing keys hard enough that no new key is needed at all,
which D8 comes within one step of and does not take.

**Choice as written.** The spec chose its user-visible words by translatability
and said so plainly, which is more than most specs do; what it did not say is
that it has thereby set a precedent for every naming decision after it. Directive
10 requires naming to be written down; it says nothing about the criterion, and
this is the first one the project has recorded.

**Consequences.** The owner now sees "Veterinarian" on a form whose every other
artefact says "vet", so a reader tracing the label to the column
(`vet_id`), the key (`vet`), the field (`visit.vet`) and the package (`vet`)
meets a fifth word at the surface. The derivation strategy also collapses in the
number-neutral bundles: `vets` is `獣医師` in Japanese, `수의사` in Korean and
`पशु चिकित्सक` in Hindi, none of which inflects for number, so `vet` and `vets`
will hold byte-identical values in at least three of the ten files. Nothing
catches that — directive 4 and `I18nPropertiesSyncTest` forbid copying *English*
into a bundle, not duplicating a string within one — and a reviewer seeing two
identical values cannot tell a correct translation from a lazy one. The strategy
that makes twenty translations affordable is the same strategy that makes three
of them unverifiable.

**Pattern.** — Suspected: this is Whole Value naming (Cunningham,
*The CHECKS Pattern Language*, 1994) inverted — the representation's
constraints choosing the concept's name rather than the other way round.

**Notes.** O6 is deferred and remains live. D8 reduces the probability O6 fires
without addressing what happens when it does; that route out is still unwritten,
and this story does not claim otherwise.

## Story #14 — A cleanup rule invented, used once, left in a spec

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (*Note on FR-18 — a directive-3 violation closed, as a stated exception to directive 9*); `AGENTS.md`, *Not encodable yet*
**Lens:** patterns, consequences
**Refs:** #6

**Context.** FR-18's note does something no other artefact in this project has
done: it states a general rule for where cleanup ends. The violation is fixed
because "the violation is inside the file this change edits, not somewhere it was
gone looking for", and `createOrUpdatePetForm.html` is left alone because "it is
not a file this change otherwise touches". That is a boundary criterion, stated
in the abstract and then applied — and AGENTS.md's *Not encodable yet* section
says, in terms, that this is the thing the project does not have: "'Needed by the
change's purpose' (the no-unrelated-reformatting rule) needs worked examples to
settle where cleanup ends and noise begins."

**Forces.** A spec needs to justify one exception; the project needs a rule. The
spec wrote the rule because justifying the exception honestly required one, and
then had nowhere to put it — AGENTS.md is not this change's to edit, and the
decision record is per-change. So the first worked example the project has ever
produced for its own acknowledged gap is filed under a functional requirement in
an S2 spec.

**Options not taken.** Route the criterion back to AGENTS.md as the worked
example the file asks for, in the same change that produced it — the file names
the need, so this is arguably within the change's purpose by the rule's own test.
Record it in `REFLECTION_LOG.md`, which exists precisely for "a rule we could not
write before and can now", and let a human decide whether it becomes a
constraint. Justify FR-18 on its own narrow facts (two lines, no translations,
same file) without generalising, leaving the boundary question untouched.

**Choice as written.** The spec chose to answer a standing project-level question
inside a slice-level document, by writing the answer as the reason for a single
exception. Nothing marks it as reusable and nothing marks it as not.

**Consequences.** The next person who edits a template with a literal label has a
precedent they will not find: it lives in
`docs/superpowers/specs/visit-carries-vet-and-time.md`, under a heading about
FR-18, and AGENTS.md still says the question is unencodable. The likely outcome
is that the rule is re-derived, differently, the next time it is needed — which
is exactly the re-litigation this record exists to prevent. There is a second-order
effect worth naming: the criterion "files this change already touches" is a good
rule with a sharp edge, because it makes cleanup obligations a function of a
change's file list, and a change's file list is the most volatile thing about it.

**Pattern.** The Boy Scout Rule (Martin, *Clean Code*, 2008) given an explicit
scope boundary — which is the useful form of it, and the form this project has
been missing. Naming it is the cheapest thing available here: the rule has a name
and a literature, and the spec re-derived it from first principles.

**Notes.** —

## Story #15 — The rule stops at the field labels

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (FR-18, AS-14, and the FR-18 note); `src/main/resources/templates/pets/createOrUpdateVisitForm.html:7-10`
**Lens:** consequences
**Refs:** #6, #14

**Context.** FR-18 closes two hardcoded English literals in
`createOrUpdateVisitForm.html`. There is a third, eight lines above them:

```html
<h2>
  <th:block th:if="${visit['new']}" th:text="#{new}">New </th:block>
  Visit
</h2>
```

`Visit` on line 9 is rendered text, not a Thymeleaf default — the `th:text`
covers only the word `New`. In German the page heading reads *Neu Visit*. It is a
directive-3 violation, in the same file, in the same change's diff radius, and
FR-18 does not touch it.

**Forces.** AS-14 is scoped to *field labels*, so by its own terms the heading is
out of scope, and the change stays small. Against that, FR-18's stated purpose is
not "key the labels" — it is that the alternative "is a form that demonstrates
both idioms at once with no explanation". After FR-18, the form still
demonstrates both idioms; the survivor has just moved from the labels to the
title.

**Options not taken.** Extend FR-18 by one line to the heading, which needs no new
key — `visit` is not in the bundles, but the heading could take `#{visits}`'s
sibling or a third existing key, and if none fits, the honest finding is that this
one *does* cost a translation and the exception should stop there deliberately.
Say in the FR-18 note that the heading is a known remaining violation and why it
is out of the exception, which is the third option #6 listed and which nothing
now records. Scope AS-14 to "the form" rather than "every field label", making
the heading's status a test failure rather than a silence.

**Choice as written.** The spec chose a per-element boundary (field labels) while
arguing for a per-file one (#14's criterion), and the mismatch between the
argument and the scope is not noted. The strongest reading is that AS-14 was
written to match FR-18 rather than FR-18 to match a principle.

**Consequences.** The form ships half-fixed in a way that is harder to see than
the state #6 described, because the surviving literal is in the heading rather
than in the field group — a reader checking "did we key the labels?" gets yes.
T-19 will not catch it: its negative assertion is "contains neither `>Date<` nor
`>Description<`", and `Visit` appears in neither. The next reader inherits a file
that has been deliberately tidied and is still non-compliant, with a note
explaining why the tidying was in scope and nothing explaining why it stopped.

**Pattern.** —

**Notes.** Whether a half-keyed heading is a defect is a judgement a human should
make, not an objection I should raise; this story records that the judgement was
never put. The same `<h2>` construction appears in other PetClinic forms, so a
maintainer may reasonably decide it is upstream's problem and out of #14's rule —
that is a disposition, and it is theirs.

## Story #16 — A German word becomes a build-gating fact

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time-plan.md` (T-19; *Risks*, "T-19 needs the message source in the web slice")
**Lens:** consequences, patterns
**Refs:** O6

**Context.** T-19 gets the form with `Accept-Language: de` and asserts the
rendered content contains `Datum` and `Beschreibung`. Until now the project's
tests have asserted that keys are *present* in every bundle
(`I18nPropertiesSyncTest`) and that rendered pages contain *English* or
data-derived strings. T-19 is the first test that asserts the value of a
translation. `messages_de.properties:40-41` are now load-bearing for a green
build.

**Forces.** Observability against coupling. AGENTS.md "should follow" 2 asks for
tests on behaviour a user can observe, and the only user-observable form of "this
label is translated" is a translated label appearing on a page — so T-19 is the
faithful reading of the house style. What is not weighed: a translated string is
data maintained by translators under directive 4, and pinning one makes a
translator's improvement a test failure in a file about visits.

**Options not taken.** Assert resolution rather than content — request `de` and
assert the rendered labels differ from the English ones, or that no `??key_de??`
marker appears, which fails for exactly the defect FR-18 addresses and does not
pin a word. Assert against the bundle rather than a literal — read
`messages_de.properties` in the test and assert the page contains whatever it
holds, which survives a retranslation. Pick the locale deliberately and say why;
the plan picks German without stating that German is now the canonical proof of
the form's i18n for the whole project.

**Choice as written.** The plan chose to verify a translation by quoting it. The
spec's AS-14 does not say how the scenario is observed, so this is the plan
deciding, and it decides against the separation the project has kept so far
between "keys are in sync" (a test's business) and "words are right" (a human's).

**Consequences.** Two test classes now depend on the bundles for different
reasons, and only one of them is named in the i18n discussion — `I18nPropertiesSyncTest`
is T-18 and is discussed at length; T-19's dependency on bundle *content* is not
mentioned in the plan's i18n constraints section, which still says FR-18 "touches
this file not at all". A German retranslation therefore breaks a visit test with
no line in the plan connecting the two. Wider: this establishes that translated
values are assertable, which is a useful convention if the project wants it and an
accident if it does not.

**Notes.** The plan's fallback — "If it does not in this project's setup, assert
instead that the rendered form contains no label text that is absent from the
bundles" — leaves AS-14's only verification in two possible shapes proving two
different things, settled by whoever runs the test first. I considered emitting
that as a separate story and dropped it: the finding's force is that the fallback
assertion would pass against unresolved messages, which is a class of failures
undetected, and the Routing Rule sends it to the diaboli. It is named here so the
drop is visible rather than silent.

**Pattern.** — Closest: Fragile Test / Sensitive Equality (Meszaros, *xUnit Test
Patterns*, 2007) — an assertion coupled to a value whose owner has no reason to
know the assertion exists.

## Story #17 — A stance stated where nothing carries it

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (*The stance these decisions add up to*); plan, *Constraints this plan is shaped by* → *Decision record*
**Lens:** coherence, consequences
**Refs:** #10

**Context.** The stance subsection says S1, S4 and S5 "inherit that as a stated
position rather than as an accident of what was cheap to land here". That is the
only cross-slice commitment this spec makes. It is placed under *Decisions taken*
without a D-number, and the plan's decision-record paragraph — which enumerates
what `decisions/2026-09-19-visit-carries-vet-and-time.md` must carry — lists
"decisions D1–D8", D2's foreclosure, D8's naming and FR-18's directive-9 reason.
The stance is not among them.

**Forces.** Not renumbering against being carried. Giving the stance a D-number
would have made it a ninth decision in a document that had just promised
"Nothing was renumbered" twice, and would have implied it is a decision rather
than a summary of eight. Leaving it unnumbered keeps the document stable, and
costs it every mechanism that attaches to a numbered decision — the plan's
decision-record list, the FR trace, and anything a later spec would cite.

**Options not taken.** Number it D9 and let the decision record carry it, which
is what directive 10's "every choice the task didn't state" appears to require of
a position this load-bearing. Write it into the slice record against S1, S4 and
S5, where the slices that must inherit it will actually be read. Add it to the
plan's decision-record list by name even unnumbered, which is a one-line change
and the cheapest of the three.

**Choice as written.** The spec chose to state the position and not to route it,
by placing it where a summary goes rather than where a decision goes. The
subsection says the later slices inherit it; nothing makes that happen.

**Consequences.** This is the same shape the spec itself names two pages later
about the nine deferrals — "The deferrals carry no stated trigger… Nothing in
this spec, the plan or the harness will raise them again" — and it applies to the
remedy as much as to the deferrals. Concretely: `decisions/2026-09-19-...md` is
the artefact the harness checks for and the artefact a future reader of the
*code* will find; after this revision it will carry D1–D8 and not the sentence
that explains what D1–D8 add up to. The most portable thing the spec produced is
the thing least likely to travel.

**Pattern.** — Alexander's coherence question turned on the record rather than
the design: the spec now tells a coherent story, and the story is stored in the
one place that does not outlive the spec.

**Notes.** This story is about where #10's remedy landed, not about whether it
was right. #10's disposition stands and is not in question.

## Story #18 — The slice record still says the opposite

**Source:** `docs/superpowers/specs/visit-carries-vet-and-time.md` (D2, *What this decision forecloses, and at whose expense*); `docs/superpowers/slices/owner-books-visit-against-vet-availability.md:114-116`
**Lens:** consequences, alternatives
**Refs:** O2, #10

**Context.** D2's new subsection names a contradiction in the accepted slice
record — S2's `scope` and S4's `decision_focus` both claim the
required-versus-optional question — and resolves it: "**This spec resolves the
contradiction in S2's favour**". The slice record was not amended. Line 116 still
reads "whether the vet field from S2 can ever be required", with disposition
`accepted`, and the spec's own sentence "Both entries were accepted" is written
in the present tense because both still are.

**Forces.** Authority against reach. A spec can say what it decided; amending an
accepted upstream record is a different act with a different gate, and the spec
had no mandate to perform it. So the resolution was recorded in the only document
the author owned. Against that: the resolution's entire purpose is to reach S4,
and S4's author will start from the slice record.

**Options not taken.** Amend the slice record's S4 entry to strike the clause and
point at this spec, as a separate human-gated change — the honest fix, and the
one that puts the information where it is read. Record the resolution in the slice
record's own revision history without touching the accepted entries, which is
weaker but still on the reader's path. Leave the resolution open and let S4
decide, which is what the slice record as it stands still promises.

**Choice as written.** The spec chose to resolve a contradiction in a document it
does not own by recording the resolution downstream, and to leave the upstream
statement intact. The choice is invisible from either document alone: the spec
reads as though the matter is settled, and the slice record reads as though it is
S4's.

**Consequences.** Whoever specs S4 reads `decision_focus`, sees a question
assigned to them, and decides it — and then meets `NOT NULL` in three dialects,
which is the bill D2 wrote out for exactly this reader. The cost is not that the
information is missing; it is that the reader is told, by an accepted record,
that they have a choice they no longer freely have. The gap is also durable in a
way the spec is not: the slice record spans five slices and will outlive this
document's readership.

**Pattern.** —

**Notes.** This choice arises from revision 1, which the original pass had in
front of it (see the preamble above) and did not surface — #10 treated the O2
closure only as evidence for the presence-versus-truth stance. Recorded as a
miss. It is not a restatement of O2: O2 objected that the foreclosure existed and
was accepted; this is about where the resolution was written down.

# B. What is now stale

Keyed by existing story number. Two revisions bear on these; the objection
closure does not, because #1–#10 were written against the post-adjudication spec.

- **#1 — Visit reaches across into the vet package.** Unchanged as a choice, and
  now better evidenced. The plan's `vet/VetFormatter.java` adds a second and a
  third crossing of the same boundary — a `vet` class whose only consumer is an
  `owner` form, and an `owner` test that must name a `vet` component in
  `includeFilters`. #1's argument stands exactly as written; #11 extends it
  rather than replacing it. Not moot.

- **#2 — Required enforced in controller and schema, not entity.** Unchanged.
  Nothing in the three revisions touches validation placement; the plan still
  specifies `result.rejectValue("vet", "required")` and still gives
  error-message economics as the reason. O4 remains deferred. Fully live.

- **#3 — Tomorrow at nine lives in the constructor.** Unchanged. The plan still
  puts `startTime = LocalTime.of(9, 0)` in `Visit`'s no-arg constructor. Live.

- **#4 — A visit is an instant, never an interval.** Weakened in one specific
  way, not moot. The stance subsection now says in the spec's own voice that
  nothing here "says anything about working hours or duration", so #4's
  framing — "the spec chose this by not addressing duration" — is no longer
  literally true; the spec addresses it in a clause and declines. What survives
  is everything the stance does not supply: the Range / Time Period pattern, the
  granularity question, and the concrete cost to S1 of rows that exist in three
  databases with a start and no extent. If disposed, that framing sentence is the
  part to amend.

- **#5 — Every vet is bookable by default.** Unchanged. D8 changes what the
  chooser is *called*, not what it contains; FR-3 still says every vet and the
  plan still says `findAll()`. Live, and #11 adds a second consumer of the same
  unordered cached collection.

- **#7 — The confirmation still says booked, in English.** Changed, and
  sharpened rather than weakened. #7's defence of the status quo was that
  "directive 3 is scoped to templates, so no rule is broken — which is precisely
  why nothing will surface it". FR-18's note has now surfaced a rule that would
  have: close a hardcoded-English violation in the file this change is already
  editing. `VisitController.java:110` — `addFlashAttribute("message", "Your
  visit has been booked")` — is in a file this change does edit (constructor,
  `populateVets`, two `rejectValue` calls). The spec has adopted the principle
  and applied it to a template while a literal English sentence in the controller
  it is modifying goes untouched and unmentioned. #7 is now a stronger story than
  when it was written, against a rule the spec itself supplied. Read with #15.

- **#8 — Vets are written about, never written to.** Unchanged. No revision adds
  a vet-facing surface or a non-requirement covering its absence. D8 means the
  clinician is now labelled "Veterinarian" on a screen they still cannot see.

- **#9 — Seed data models a clinic nothing enforces.** Marginally weakened,
  still live. The stance subsection converts "nothing makes a vet-and-time pair
  unique" from a silence into a stated position, which takes some force out of
  #9's closing argument about a later unique index going in green. Untouched:
  the seed content itself, and the fact that D6's ordering rule still has no
  seeded example anywhere in the running application. That half of #9 is
  unaffected by all three revisions.

Nothing in #1–#9 is moot.

# C. What the revisions cost

Six of the eight new stories exist only because a remedy was applied. Mapped to
their source:

**From story #6's remedy (D8, FR-18, AS-14, T-19) — five new choices.** This is
the expensive one, and the pattern is consistent: a two-line fix with a
well-argued note produced four decisions the note did not notice it was making.
#12 — FR-18 keyed the form's date label to `date` while D8 derived `visitTime`
from `visitDate`, so the mirror D8 reasons from breaks on the one screen FR-18
edits, and the two screens will head the same column differently in all ten
locales. #13 — D8 set a criterion for choosing English words (derivability from
an existing bundle entry) that nothing marks as general or as local, and that
collapses to duplicate values in the number-neutral bundles, where nothing checks
it. #14 — the FR-18 note invented the worked example AGENTS.md explicitly says
the project lacks, and filed it under a functional requirement in a slice spec.
#15 — the note's principle is per-file and its scope is per-field-label, and the
third literal in the same template, the `Visit` in the `<h2>`, survives
untouched and unmentioned. #16 — T-19 made a German word a build-gating fact,
the first test in the project to assert the content of a translation rather than
the presence of a key.

**From story #10's remedy — one new choice.** #17: the stance was written as an
unnumbered subsection, which kept the renumbering promise and cost it the
decision record. The plan's decision-record list enumerates D1–D8 and three
specific additions; the stance is in none of them. The one portable, cross-slice
position this spec states is the one thing that will not reach
`decisions/2026-09-19-visit-carries-vet-and-time.md`.

**From the O1/O2 adjudication — one choice, and it is a miss rather than a
novelty.** #18: D2's new subsection resolves a contradiction in the accepted
slice record without the slice record changing, so S4's author will read an
accepted document telling them they own a question this spec has decided. As set
out in the preamble, this was available to the original pass and was not
surfaced; it is recorded now rather than attributed to a revision it did not come
from. The O1 remedy introduced nothing new — #7 was written against it, and the
revision's effect on #7 is reported in section B.

**Candidates considered and dropped.** The plan's T-19 fallback assertion (it
would pass against unresolved messages — a failure class, routed to the diaboli;
named in #16's Notes). The freezing of AS and T identifiers across revisions
(AS-14 beside AS-12, T-19 after T-18) — a real and twice-repeated convention
choice, dropped as immaterial: the consequence is a non-monotonic table, not a
foreclosed option. The plan's assertion that neither accepted objection changes
it — true, and a restatement of the adjudication rather than a choice.
