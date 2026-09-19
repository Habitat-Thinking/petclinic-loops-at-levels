---
slice: S2
slice_record: docs/superpowers/slices/owner-books-visit-against-vet-availability.md
title: "What a booked visit becomes — vet, time of day, and the existing visit rows"
date: 2026-09-19
revised: 2026-09-19 — objection adjudication (O1 and O2 closed; O3–O11 deferred); choice story #10 accepted (stance stated, no decision added); choice story #6 accepted (D8 fixes the English words behind the two new keys; FR-18 and AS-14 key the form's two existing labels); choice stories #12, #15 and #17 accepted (FR-18 re-keyed to `visitDate`; FR-19 keys the form heading; FR-20 keys the confirmation message; the stance subsection routed to the decision record); FR-19 re-keyed to a single `newVisit` heading key (the two-key composition abandoned, the `visit` key dropped unused, the `visit['new']` conditional removed from the heading)
objections: docs/superpowers/objections/visit-carries-vet-and-time.md
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
maintainer can overturn any of them at the plan-approval gate. D8 was added in a
later revision, from choice story #6, and settles a question the spec had left
undefined rather than one the slice record raised.

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

#### What this decision forecloses, and at whose expense

The slice record contradicts itself about who owns this question, and the
contradiction is named here rather than left for a later reader to find:

- S2's `scope` says this slice must "decide whether a vet is required or
  optional on a visit".
- S4's `decision_focus` says the same question is S4's: it decides "whether the
  vet field from S2 can ever be required".

Both entries were accepted. **This spec resolves the contradiction in S2's
favour**: the vet is required, and it is required now. That is not a
consequence of D2, it is half of S4's stated question, decided here.

What that costs if S4 chooses otherwise. S4 may still decide that slot-booking
replaces the free-date form, or that the two coexist. But if S4 chooses a
coexisting free-date form that creates visits *without* a vet, that choice is
no longer free: it requires dropping a `NOT NULL` constraint and a foreign key
in `db/h2`, `db/mysql` and `db/postgres` together, in one change, under the
schema-parity rule. S4 is not blocked; it is made more expensive, and the price
is a three-dialect schema change.

The inconsistency in this document, named rather than hidden. D1 rejected a
single-timestamp representation partly because it "would be a type migration of
an existing column across three dialects". D2 accepts a three-dialect migration
as the price S4 would pay to reverse it. The same cost is treated as
prohibitive for the representation decision and as acceptable for the
nullability decision, in the same spec. The maintainer has adjudicated this
knowingly and D2 stands as written; the asymmetry is recorded so that whoever
specs S4 sees the bill before they choose.

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

### D8 — The two new keys hold "Visit Time" and "Veterinarian"

FR-15 named two keys, `visitTime` and `vet`, and never said what English words
they hold — while obliging a trustworthy translation of those words into ten
locale bundles. AGENTS.md directive 10 names user-visible naming as a decision
that must be written down, so the words are decided here rather than left to
whoever types the properties file:

- **`visitTime` = `Visit Time`**
- **`vet` = `Veterinarian`**

Reasoning — translation tractability. Each string was chosen to mirror a key
that already exists, already translated, in all ten locale bundles, so a
translator derives the new phrase from one already in front of them instead of
inventing it:

- `visitTime` is an exact parallel of the existing `visitDate` = `Visit Date`.
  A translator holding `visitDate=Besuchsdatum` writes `Besuchszeit`; holding
  `visitDate=Fecha de visita` writes `Hora de visita`.
- `vet` mirrors the existing `vets` = `Veterinarians`, whose plural every bundle
  already carries (`Tierärzte`, `Veterinarios`, `Veterinerler`, …), so the
  singular is derivable rather than invented.

This is what keeps FR-15's obligation honest: twenty translations are still
twenty translations, but each one is a derivation of a phrase the bundle already
holds, not a fresh coinage a translator has to guess the intent of. Neither
string introduces a new concept name to the product's vocabulary.

*(Source: choice story #6, "New labels keyed, their neighbours left English", in
[`docs/superpowers/stories/visit-carries-vet-and-time.md`](../stories/visit-carries-vet-and-time.md),
which recorded that twenty translations were required of a string nobody had
decided.)*

### The stance these decisions add up to: shape is guaranteed, meaning is not

Every constraint this change buys is about a field being **present** — a vet and
a start time on every visit, with a foreign key to `vets`, in all three
dialects. None is about the recorded value being **true**: nothing makes a
vet-and-time pair unique, binds the date to the time, bounds how far ahead a
visit may be, or says anything about working hours or duration.

That is deliberate. This change guarantees the *shape* of a booking and
guarantees nothing about its *meaning*. The slices that add meaning — S1
(availability) and S5 (contention) — inherit that as a stated position rather
than as an accident of what was cheap to land here, and S4 inherits it too.

This subsection deliberately has **no D-number**: it is a summary of what D1–D8
add up to, not a ninth decision, and numbering it would have renumbered nothing
but implied otherwise. Being unnumbered is not the same as being unrouted,
though, so the plan's decision-record list now names this subsection explicitly
alongside D1–D8. `decisions/2026-09-19-visit-carries-vet-and-time.md` is the
artefact a future reader of the *code* will find, and it must carry the sentence
that explains the eight decisions as well as the eight decisions themselves.

*(Source: choice story #10, "Constraint spent on presence, not on truth", in
[`docs/superpowers/stories/visit-carries-vet-and-time.md`](../stories/visit-carries-vet-and-time.md).
The full argument lives there and is not restated here. The routing into the
decision record is from choice story #17, "A stance stated where nothing carries
it", which named it as the cheapest of three options and the one taken.)*

## User story

> As a pet owner, I want a visit to record which vet I am asking for and at what
> time of day, so that the visit carries that vet and that time rather than only
> a date.

The story claims only what this change delivers. It does **not** claim that the
owner knows who they will see or when to arrive: FR-17 consults no availability
and prevents no clash, so nothing in the system agrees to the recorded vet or
time. Whether a recorded visit ever becomes an agreed appointment is S1's and
S4's question, and this spec does not answer it.

This narrowing is confined to the spec. No screen gains request-versus-
confirmation wording, and no key is added to express it. FR-20 moves the
confirmation sentence out of the controller and into a message key, but it
carries the sentence across **word for word**: the screen still says the visit
has been booked. Where that string lives has changed; what it claims has not,
and the O1 adjudication's refusal of request-versus-confirmation wording stands
untouched.

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

**AS-15 — The confirmation message is translated, not hardcoded**
Given a user whose locale is one of the translated locales
When I submit the new-visit form successfully
Then the confirmation message shown on the owner's page is in that locale
And no English confirmation text is hardcoded in the application.
(AS-2 asserts only that a confirmation is shown. This asserts where its words
come from. The words themselves are unchanged — see FR-20.)

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

**AS-14 — Every label on the new-visit form is translated, not hardcoded**
Given a user whose locale is one of the translated locales
When I open the new-visit form
Then every field label on the form — date, description, start time and vet —
is shown in that locale
And no English label text is hardcoded in the template.
(AS-12 covers table *headings* only. The date and description labels on this
form are passed as English literals today; see the note under the requirements
table.)

**AS-16 — The new-visit form's page heading is translated, not hardcoded**
Given a user whose locale is one of the translated locales
When I open the new-visit form
Then the heading at the top of the page is shown entirely in that locale, with
no English word left in it
And no English heading text is hardcoded in the template.
(AS-14 covers *field labels* and AS-12 covers table *headings*. Neither reaches
the page's own heading, which today is half-keyed: the word before the noun
comes from a key and the noun does not. See the note under FR-19.)

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
| **FR-8** | A valid submission redirects to the owner's page with the existing confirmation message — unchanged in wording, now resolved from a key per FR-20 — and the visit is recorded with its vet and start time. *(AS-2)* |
| **FR-9** | The owner's page shows, for every visit of every pet, the visit's start time and the vet's name, alongside the existing date and description. *(AS-3)* |
| **FR-10** | The pet's previous-visits list on the visit form shows each visit's start time and vet name. *(AS-4)* |
| **FR-11** | The `visits` table in `db/h2`, `db/mysql` and `db/postgres` each gains a required start-time column and a required vet reference constrained as a foreign key to `vets`, landed in the same change. *(D1, D2, AS-10)* |
| **FR-12** | Each `data.sql` keeps its four visits — same pet, same date, same description as today — each given a vet and a start time, written with an explicit column list. *(D3, AS-9, AS-10)* |
| **FR-13** | Visits listed for a pet are ordered by date ascending, then start time ascending. *(D6, AS-11)* |
| **FR-14** | Dates display as `yyyy-MM-dd` and times as `HH:mm`, identically in every locale. *(D5, AS-3)* |
| **FR-15** | All new display text comes from message keys. Two keys are added for the two new labels: `visitTime`, holding the English `Visit Time`, for the time heading and label; and `vet`, holding the English `Veterinarian`, for the vet heading and label. Both are added to `messages.properties` and to every locale bundle only with a genuine translation; English text is never copied into a locale bundle to satisfy the sync check. Any locale left without a trustworthy translation is flagged for a human. FR-19 and FR-20 each add one further key on their own grounds, so this change adds **four** keys in total and forty translations across the ten bundles; the same never-copy-English rule governs all four. *(D8, AS-12)* |
| **FR-16** | No owner personal data is added to any output. The vet's first and last name is vet data, already published on the vets page, and is the only new personal-ish field rendered. |
| **FR-17** | No availability is consulted and no clash is prevented: any vet may be recorded at any time of day on any future date, including a time another visit already uses. *(D4, AS-13)* |
| **FR-18** | The two labels already on the new-visit form — its date field and its description field — come from existing message keys instead of the hardcoded English literals `Date` and `Description` they are given today. The date field uses `visitDate`, the same key the owner's page already uses for that column; the description field uses `description`. The form's own previous-visits table heads its date column with that same `visitDate` key, so the day a visit happens is named identically on both screens. No key is added and no translation is written: both keys already exist in `messages.properties` and are already translated in all ten locale bundles. *(AS-14)* |
| **FR-19** | The new-visit form's page heading comes entirely from **one** message key, with no English word rendered as template text and no heading assembled from separate keys. This adds one key, `newVisit`, holding the English `New Visit`, to `messages.properties` and to every locale bundle with a genuine translation **of the whole heading**, under the same never-copy-English rule as FR-15. The form creates only new visits, so the heading has no second form: the condition that today selects the word `New` is removed with the composition, and no `visit` key is added. *(AS-16)* |
| **FR-20** | The confirmation message shown after a visit is recorded comes from a message key rather than a hardcoded English sentence in the controller. This adds one key, `visitBooked`, holding the existing English sentence `Your visit has been booked`, to `messages.properties` and to every locale bundle with a genuine translation, under the same never-copy-English rule as FR-15. The sentence's wording is carried across unchanged. *(AS-15, AS-2)* |

### Note on FR-18 — a directive-3 violation closed, as a stated exception to directive 9

The new-visit form passes the literals `Date` and `Description` as the labels of
its two existing fields. That is hardcoded display text in a template, which
AGENTS.md directive 3 forbids, and it sits in the one template this change is
already editing. Shipping FR-3's two new fields with keyed labels beside two
literal ones would leave the form half-translated, with the newer half keyed —
an inversion that reads as deliberate and explains itself to nobody.

FR-18 is therefore a **deliberate exception to directive 9** ("no unrelated
reformatting — every changed hunk is needed by the change's stated purpose").
The exception is taken because:

- the violation is inside the file this change edits, not somewhere it was gone
  looking for;
- the fix is two lines, swapping two literals for two key references;
- it costs no translation work at all — `visitDate` and `description` are
  already present and already translated in every one of the ten bundles, so
  FR-18 adds nothing to this change's translation obligation; and
- it removes the alternative, which is a form that demonstrates both idioms at
  once with no explanation.

**Which key the date label uses, and why it is `visitDate` and not `date`.**
Both keys exist and both are translated in all ten bundles, so the choice costs
the same either way. It is `visitDate` because that is the key the owner's page
already uses to head this column (`#{visitDate}`), and because D8 chose the
English `Visit Time` precisely by mirroring `visitDate` = `Visit Date`. Keying
the form's date label to `date` would have left the mirror holding on one screen
and broken on the other: the form would head the column `Date` and the owner's
page `Visit Date`, in all ten locales — `Datum` against `Besuchsdatum` in
German — because a key choice survives translation in a way a wording choice
does not. **Both screens now key this column identically**, so the concept "the
day a visit happens" has one name wherever a user meets it.

For that to be true of what a user actually sees, FR-18 reaches one line
further than the two literals. The form's date *field label* is one place the
column is named; its previous-visits *table heading* is the other, and that
heading is already keyed — to `date`, not to `visitDate`. Leaving it would have
fixed the label and left the heading directly above the rows still reading
`Datum` against the owner page's `Besuchsdatum`, which is the divergence this
fix exists to remove. So that heading moves to `visitDate` too. It is a third
line, in the same file, at the same zero translation cost, and it is the only
part of FR-18 that is not closing a directive-3 violation — it is a
consistency change to already-compliant markup, and it is named separately here
so a maintainer can decline it without disturbing the rest of FR-18.

*(Source: choice story #12, "Visit Time beside Date, on one screen only", which
named keying the date label to `visitDate` as the option that costs no
translation and keeps the two screens in step. That is the option taken.)*

Directive 9's own exception route applies: this reason belongs in the change's
decision record. Nothing else in the template, and no other file, is tidied on
the same grounds — the same violation exists on the pet form, which this change
does not otherwise edit and therefore leaves alone.

*(Source: choice story #6, which named the options not taken — key the two
existing labels, leave all four, or record the violation as a known exception.
The first is the one taken.)*

### Note on FR-19 — the third literal in the same template, and its one new key

FR-18 closed two literals in `pets/createOrUpdateVisitForm.html`. A third sits
eight lines above them, in the page's own `<h2>`: only the word `New` is keyed
(`#{new}`), and the noun after it is rendered template text. In German the
heading therefore reads *Neu Visit* — half the heading translated, half not, on
the page this change is adding two fields to. It is the same directive-3
violation in the same file, and it falls inside the same boundary FR-18's note
draws: a violation in a file this change already edits, not one gone looking
for. FR-19 closes it on exactly that ground, and the exception to directive 9 is
the one already stated for FR-18, not a second one.

**This one is not free.** No `newVisit` key exists in `messages.properties`
today, so FR-19 adds a key and therefore ten translations. That cost is named
here rather than buried: the honest alternative was to record the heading as a
known remaining violation and stop, and it was not taken.

**One key, not two — and why the composition was abandoned.** An earlier
revision of FR-19 keyed this heading as `#{new}` + a new `#{visit}`, and named,
as a translator's problem it was leaving open, that a heading assembled from two
keys inherits the grammar of neither: English `New` + `Visit` reads correctly,
German `Neu` + `Besuch` gives *Neu Besuch* where German wants *Neuer Besuch*,
because the adjective inflects before the noun. That is not an implementation
defect that a better composition would fix — **no two-key composition can
express the inflection**, in German or in any other language that inflects a
word before a noun. So the maintainer has taken the alternative that revision
recorded as wider than the change: **the whole heading is one key per locale.**
`newVisit` holds the English `New Visit`, each bundle holds its own whole
heading — `Neuer Besuch` — and the grammar defect is closed rather than
documented.

**The English words are `New Visit`, and D8's criterion is met only in part.**
D8 chose its two words by asking whether a translator can derive the new string
from one the bundle already holds, and FR-19's earlier `Visit` passed that test
by extraction: every bundle carries the noun inside `addVisit` and `visitDate`.
`New Visit` does not pass it as cleanly. The *vocabulary* is present — a German
translator has `new=Neu` and `Besuch` twice over — but the *grammar* that joins
them is in no bundle, and getting `Neuer` rather than `Neu` is a judgement only
a speaker of the language can make. So `newVisit` sits between D8's two words
and FR-20's sentence: dearer than a noun, cheaper than a claim, and **not
derivable in D8's sense**. D8 is unchanged and gains no clause; this records
where FR-19 no longer meets it.

**The `visit['new']` conditional does not survive, and this was checked before
it was decided.** Today the heading reads
`<th:block th:if="${visit['new']}" th:text="#{new}">New </th:block> Visit`, a
construction that distinguishes a new visit from an existing one. **That
distinction is unreachable.** `VisitController` declares exactly two mappings, a
GET and a POST, both on `/owners/{ownerId}/pets/{petId}/visits/new`, and the
`@ModelAttribute("visit")` method that runs before both constructs a brand-new
`Visit` on every request. No path in the application renders this form against a
persisted visit, so the condition is always true and the heading's other form
has never been rendered by anything. **The conditional is therefore removed**
along with the composition, and the heading is unconditionally the one key.
There is no non-new heading and this requirement does not invent one: if a later
slice ever edits an existing visit, that slice needs a heading of its own and
pays for it then.

Two consequences of removing `#{new}` from this template, both checked:
`pets/createOrUpdatePetForm.html` still uses the `new` key, so the key is not
orphaned in eleven bundles and this change does not touch that file; and the
`${!visit['new']}` condition further down this same template, on the
previous-visits rows, is untouched and unaffected — its `visit` is the row
being iterated, not the model attribute, which is precisely how the blank
visit the controller attaches is kept out of the list.

**The `visit` key is not added.** The previous revision added `visit` = `Visit`
for this heading and for nothing else. With the heading resolving from
`newVisit`, nothing in this change references it: the form's previous-visits
table heads its columns with `visitDate` (FR-18), `description`, `visitTime` and
`vet`; its button uses `addVisit`; its section uses `previousVisits`; and the
owner's page adds no heading beyond `visitTime` and `vet`. `visit` is therefore
**dropped from this requirement** rather than written into eleven properties
files with no consumer. The change still adds four keys and no more:
`visitTime`, `vet`, `newVisit`, `visitBooked`.

**This is the second increase to this change's translation obligation in one
sitting, and it enlarges deferred objection O6 again.** The count does not move:
four keys and forty translations, as the previous revision left it. What moves
is what those forty cost. Before this revision, thirty of them were derivable
from something a bundle already held and ten were not. Now **twenty are
derivable and twenty are not** — `visitBooked`'s ten sentences, composed from
nothing at all, and `newVisit`'s ten headings, whose words every bundle holds
but whose grammar none of them supplies. The share of this change's translation
bill that no translator can derive has **doubled**, in the same sitting in which
the bill itself first doubled from twenty strings to forty. O6 objects that a
missing translation becomes a blocked pull request under the harness's *Tests
must pass* gate, with no sanctioned route out; every string a translator must
compose rather than derive is a string likelier to arrive late or wrong, so this
revision widens exactly the surface O6 names, for the second time today.
**O6 is not closed, reduced or worked around by this revision either.** It
remains deferred, with no stated trigger, and the maintainer has accepted this
second increase knowing that.

*(Source: choice story #15, "The rule stops at the field labels", which named
extending FR-18 by one line to the heading, and named honestly that if no
existing key fits then this one does cost a translation. It does, and it is
taken anyway. The single-key form of that fix was named in the previous
revision — in this note and in the plan's risk list — as an option wider than
the change; the maintainer has now taken it, on the grammar ground stated
above.)*

### Note on FR-20 — ten fresh sentence translations, and what that does to O6

`VisitController.java:110` reads
`redirectAttributes.addFlashAttribute("message", "Your visit has been booked")`.
That is a hardcoded English sentence in a file this change already edits — the
same file gains a `VetRepository`, a `populateVets` method and two
`rejectValue` calls — so it sits inside FR-18's stated boundary. It is the only
user-visible string in this flow living outside the message-key system, and it
is the strongest claim the product makes about what just happened. FR-20 brings
it under a key.

**The cost, stated plainly and not minimised.** This is nothing like FR-18 and
nothing like FR-19. It is a **full sentence**, and it is **not derivable from
any existing entry in any bundle**. D8's tractability criterion does not reach
it: no bundle holds "Your visit has been booked", or a phrase containing it, or
an inflection of it. `visitBooked` is a fresh coinage in ten languages, and a
sentence is harder to translate well than a noun — it carries tense, agency and
a claim about what the clinic has agreed to. FR-20 therefore adds **ten fresh
sentence translations** to this change. Counted against the rest: FR-18 adds
zero, FR-15's two keys add twenty derivable words, FR-19 adds ten headings whose
words are in every bundle but whose grammar is in none, and FR-20 adds ten
sentences nobody can derive at all. Forty translations in total, of which twenty
are derivable and twenty are not; these ten are the only ones a translator must
compose from nothing whatever. *(FR-19's line in this count was ten derivable,
extractable words when this note was first written; the later re-keying of
FR-19 to a single `newVisit` heading moved them, and the note under FR-19
records that as the second increase in the same sitting.)*

**This raises O6's exposure, and O6 is still deferred.** O6 objects that
FR-15's "flag the missing locale for a human" becomes, under the harness's
*Tests must pass* gate, a blocked pull request — and that the spec offers no
sanctioned route out. Every key added widens the surface on which that can fire,
and this key widens it by the most: it is the one whose translations are least
likely to arrive quickly and least likely to be got right first time.
`I18nPropertiesSyncTest` will fail until all ten exist, and directive 4 forbids
the cheap way out. **O6 is not closed, reduced or worked around by this
revision.** The maintainer adjudicated it `deferred` and has accepted FR-20
knowing it makes the deferred risk larger. That trade is recorded here so that
whoever meets a blocked merge over `visitBooked` can see it was foreseen.

**What FR-20 does not do.** It does not change a word of the sentence. The O1
adjudication considered request-versus-confirmation wording on the screens and
explicitly declined it; that decision stands. FR-20 is about *where the string
lives*, not about *what it claims*. The screen will still say the visit has been
booked while, per FR-17, nothing in the system has agreed to it — the gap named
under the user story is unchanged in size and merely moves from a controller
literal into eleven properties files.

*(Source: choice story #7, "The confirmation still says booked, in English",
which named replacing the literal with a key as one option and separating the
i18n question from the claim question as another. Both are taken: the string is
keyed, the claim is untouched.)*

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

## Adjudication of objections

Eleven objections were raised against this spec and its plan and are recorded in
[`docs/superpowers/objections/visit-carries-vet-and-time.md`](../objections/visit-carries-vet-and-time.md).
The maintainer adjudicated all eleven on 2026-09-19. Two were accepted and are
closed by this revision. Nine were deferred and are deliberately untouched: the
spec still says what it said when they were raised.

### Accepted and closed here

| Id | Objection | Remedy chosen by the maintainer, and what changed |
|---|---|---|
| **O1** | The user story promised the owner knows who they are seeing and when to arrive, while FR-17 declines to support that. | Narrow the user story. It now claims only that the visit records the vet being asked for and the time of day; the knowledge claim is gone, and the note under it points at FR-17. The alternative remedy — request-versus-confirmation wording on the screens — was considered and **not** chosen: no screen text and no message key changes. |
| **O2** | A `NOT NULL` vet settles half of the question the slice record assigns to S4. | Keep `NOT NULL`, name the foreclosure. D2's design is unchanged. D2 now states that the slice record contradicts itself (S2's scope and S4's `decision_focus` both claim the required-versus-optional question), that this spec resolves it in S2's favour, what reversal costs S4 (dropping a `NOT NULL` and a foreign key across h2, mysql and postgres together), and that D1 and D2 apply opposite cost standards to the same three-dialect migration. |

No acceptance scenario, no functional requirement and no other decision
(D1, D3–D7) was changed by the objection revision. Nothing was renumbered.
(The later choice-story revision that added D8, FR-18 and AS-14 left D1–D7,
FR-1..FR-14, FR-16, FR-17 and AS-1..AS-13 as they were, and renumbered nothing:
AS-14 is placed beside AS-12, which it extends, rather than at the end.)

(The revision after that — choice stories #12, #15 and #17 — added FR-19, FR-20,
AS-15 and AS-16, re-keyed FR-18's date label from `date` to `visitDate`, and
amended FR-8's and FR-15's wording to stay consistent with FR-20 and with the
new key count. D1–D8 are untouched, nothing is renumbered, and again the new
scenarios are placed beside their relatives: AS-15 beside AS-2, AS-16 beside
AS-14.)

(The revision after *that* changed FR-19 only. The heading now resolves from a
single `newVisit` key instead of a `#{new}` + `#{visit}` composition, the
`visit` key is dropped unused rather than written into eleven files with no
consumer, and the heading's `visit['new']` condition goes with the composition
because `VisitController` can only ever render this form for a new visit.
AS-16 is unmoved and unchanged, D1–D8 are untouched, no other FR's subject
changes — FR-20's note has one sentence restated so its count of what is
derivable stays true — and nothing is renumbered. Four keys and forty
translations still; what changed is that **twenty of the forty are now
underivable where ten were**. That is the second enlargement of O6's exposure in
one sitting, and **O6 is still deferred**. See the note under FR-19.)

### Deferred — not fixed, by decision

Each of the nine below was adjudicated `deferred` with the maintainer's
rationale recorded verbatim as **"need the spec tightened"**.

| Id | Severity | In one line |
|---|---|---|
| **O3** | high | mysql and postgres `schema.sql` use `CREATE TABLE IF NOT EXISTS`, so an already-provisioned database never gains the new columns, and the parity check compares files rather than databases. |
| **O4** | high | D2 weighs "required now" against "optional forever" and does not weigh "optional now, required when S4 decides". |
| **O5** | high | FR-3's vet chooser specifies no blank or placeholder option, so a browser preselects the first vet and AS-5's precondition is unreachable through the UI. |
| **O6** | high | FR-15 says a missing translation is flagged for a human, but the harness's "Tests must pass" gate turns that flag into a blocked pull request, and the spec offers no sanctioned route out. *(Still deferred, and its exposure is now larger: FR-19 and FR-20 add two more keys, and FR-20's is a sentence no bundle can derive. See the note under FR-20.)* |
| **O7** | medium | FR-14's "identically in every locale" cannot hold for the form's `type="time"` control, whose painted format the browser chooses. |
| **O8** | medium | AS-11 is placed where `@OrderBy` never runs — a `@WebMvcTest` with a mocked repository — so the only test of FR-13 asserts the fixture's insertion order. |
| **O9** | medium | FR-17 and AS-13 are satisfied by absence and left untested, against a preamble that claims every requirement is testable. |
| **O10** | medium | D1 does not state the durable cost: nothing binds `visit_date` and `start_time` together, so S1's slots will meet two unreconciled descriptions of when a booking is. |
| **O11** | low | FR-9 widens the owner page's per-pet visits table without saying what happens to the two-cell action row inside the same table. |

**The deferrals carry no stated trigger.** The adjudication records a rationale
but no condition, date, slice or gate at which any of these nine is revisited.
Nothing in this spec, the plan or the harness will raise them again. Whoever
specs S1, S3, S4 or S5 — and whoever implements this one — should read the
objection record as live, not closed.
