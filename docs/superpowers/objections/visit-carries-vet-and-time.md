---
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
date: 2026-09-19
mode: spec
diaboli_model: claude-opus-5
objections:
  - id: O1
    category: premise
    severity: high
    claim: "The user story promises the owner knowledge of who they will see and when to arrive, but FR-17 explicitly declines to support that claim, so the change ships a display that reads as an appointment the clinic never agreed to."
    evidence: "User story: 'I want a visit to record which vet my pet will see and at what time of day, so that when I look at my pet's visits I know who we are seeing and when to arrive.' FR-17: 'No availability is consulted and no clash is prevented: any vet may be recorded at any time of day on any future date, including a time another visit already uses.'"
    disposition: pending
    disposition_rationale: null
  - id: O2
    category: scope
    severity: high
    claim: "D2 makes vet_id NOT NULL in three schemas, which settles a question the accepted slice record assigns to S4, and makes the S4 option 'the free-date form coexists' materially more expensive to choose."
    evidence: "Spec D2: 'The column is NOT NULL with a foreign key to vets, and no path in the application can create a visit without one.' Slice record S4 decision_focus: 'It decides whether the system has one booking concept or two, whether VisitController keeps its current POST and its typeMismatch.visitDate rejection, and whether the vet field from S2 can ever be required.'"
    disposition: pending
    disposition_rationale: null
  - id: O3
    category: scope
    severity: high
    claim: "FR-11 and AS-10 describe the visits table 'gaining' required columns, but mysql and postgres schema.sql use CREATE TABLE IF NOT EXISTS, so no already-provisioned database gains them, and the schema-parity check compares files rather than databases."
    evidence: "FR-11: 'The visits table in db/h2, db/mysql and db/postgres each gains a required start-time column and a required vet reference.' src/main/resources/db/mysql/schema.sql:50 'CREATE TABLE IF NOT EXISTS visits ('; src/main/resources/db/postgres/schema.sql:47 same. AS-10 verifies via 'the schema-parity check', and HARNESS.md scopes SchemaParityTest to 'Every column present in db/h2/schema.sql exists in db/mysql/schema.sql and db/postgres/schema.sql'."
    disposition: pending
    disposition_rationale: null
  - id: O4
    category: alternatives
    severity: high
    claim: "D2 weighs only 'required now' against 'optional forever' and does not weigh 'optional now, required when S4 decides', which delivers every observable outcome this spec claims at materially lower cost."
    evidence: "D2: 'the alternative leaves two kinds of visit in the system indefinitely — one that answers \"who will see the pet\" and one that cannot — and every screen then has to render the empty case forever.'"
    disposition: pending
    disposition_rationale: null
  - id: O5
    category: specification quality
    severity: high
    claim: "FR-3 specifies a vet chooser with no empty or placeholder option, so a browser preselects the first vet, making AS-5's precondition unreachable through the UI and silently assigning a vet the owner never chose."
    evidence: "FR-3: 'The new-visit form offers a choice of every vet in the clinic, each shown by first and last name.' AS-5: 'When I submit the new-visit form with a future date, a start time and a description but no vet chosen.' The existing fragment it extends emits no blank option — templates/fragments/selectField.html:14 '<option th:each=\"item : ${items}\" th:value=\"${item}\" th:text=\"${item}\">dog</option>'."
    disposition: pending
    disposition_rationale: null
  - id: O6
    category: specification quality
    severity: high
    claim: "FR-15 names a failure outcome — flag the missing locale for a human — without saying what becomes of this change when it fires, and HARNESS.md's 'Tests must pass' gate turns that flag into a blocked pull request."
    evidence: "FR-15: 'Any locale left without a trustworthy translation is flagged for a human.' AGENTS.md directive 4: 'add the key to messages.properties only, let the sync test fail, and flag the missing locales for a human.' HARNESS.md, Tests must pass: 'The project's test suite must pass with zero failures before any code is merged' — Enforcement deterministic, Scope pr."
    disposition: pending
    disposition_rationale: null
  - id: O7
    category: specification quality
    severity: medium
    claim: "FR-14's 'identically in every locale' is unachievable for the form's time control, whose rendered format is chosen by the browser locale and cannot be forced by the application."
    evidence: "FR-14: 'Dates display as yyyy-MM-dd and times as HH:mm, identically in every locale.' Plan, fragments/inputField.html: 'Add <input th:case=\"'time'\" class=\"form-control\" type=\"time\" th:field=\"*{__${name}__}\" /> to the existing th:switch.'"
    disposition: pending
    disposition_rationale: null
  - id: O8
    category: specification quality
    severity: medium
    claim: "AS-11 is not falsifiable under the project's stated test convention, because @OrderBy is a persistence-layer mechanism that never runs in a @WebMvcTest with a mocked repository, so the only test of FR-13 asserts the fixture's insertion order."
    evidence: "AS-11: 'Given a pet with two visits on the same date, one at 09:00 and one at 15:00 / When I open the owner's page / Then the 09:00 visit is listed before the 15:00 visit.' Plan: 'Ordering is delegated to @OrderBy on the association' and T-12 'ownerDetailsOrdersSameDayVisitsByTime — a pet with 09:00 and 15:00 visits on one date; the 09:00 row appears first in the rendered page' in OwnerControllerTests."
    disposition: pending
    disposition_rationale: null
  - id: O9
    category: specification quality
    severity: medium
    claim: "The spec asserts every requirement is testable, but FR-17 and AS-13 are satisfied by absence and left untested, so the deliberate decision not to prevent clashes is unprotected against silent reversal."
    evidence: "Spec, Functional requirements preamble: 'Each is testable and traces to at least one scenario above.' AS-13: 'It is asserted so that the absence of contention handling is a recorded decision rather than an oversight.' Plan FR mapping, FR-17: 'Asserted by absence; AS-13 is left untested at controller level because there is no code to test.'"
    disposition: pending
    disposition_rationale: null
  - id: O10
    category: implementation
    severity: medium
    claim: "D1 states one cost of the two-column representation but not the durable one: nothing binds visit_date and start_time together, so when S1's slots arrive there will be two independent descriptions of when a booking is, with no constraint reconciling them."
    evidence: "D1: 'The cost, stated honestly: two columns can in principle hold a date with no time. FR-2 and FR-11 close that by making the start time required in both the schema and the form.' Slice record S2 decision content: 'each puts the truth about a booking in a different place, and that determines how \"taken\" is computed and what happens when a published slot is withdrawn after someone has booked it.'"
    disposition: pending
    disposition_rationale: null
  - id: O11
    category: specification quality
    severity: low
    claim: "FR-9 adds two columns to the owner page's per-pet visits table without saying what happens to the two-cell action row that shares that table, leaving the rendered layout undetermined."
    evidence: "FR-9: 'The owner's page shows, for every visit of every pet, the visit's start time and the vet's name, alongside the existing date and description.' templates/owners/ownerDetails.html:71-74 places '<td><a ... th:text=\"#{editPet}\">Edit Pet</a></td><td><a ... th:text=\"#{addVisit}\">Add Visit</a></td>' inside the same table as the visit rows."
    disposition: pending
    disposition_rationale: null
---

# Objections — visit carries a vet and a time of day

Spec mode. Grounded in the spec, its plan, the accepted slice record, `AGENTS.md`,
`HARNESS.md`, and the four source artefacts the spec changes. S1, S3, S4 and S5 are
accepted as out of scope; where a slice is named below it is because a choice in
*this* spec forecloses it or raises its cost, never because this spec does not
contain it.

## O1 — premise — high

### Claim

The spec's stated value is that an owner **knows** who they are seeing and when to
arrive. The spec's own FR-17 declines to support that. What the change actually
delivers is a self-asserted preference, rendered on two screens in a form
indistinguishable from a confirmed appointment — and the spec never narrows its
value claim to match.

### Evidence

> As a pet owner, I want a visit to record which vet my pet will see and at what
> time of day, so that when I look at my pet's visits I know who we are seeing and
> when to arrive, rather than only which day.

against

> **FR-17** | No availability is consulted and no clash is prevented: any vet may be
> recorded at any time of day on any future date, including a time another visit
> already uses.

and the surrounding system, per the slice record's S3 context: "This application has
no authentication, no roles, and no concept of a logged-in actor — every page is open
to anyone who has the URL."

### Why this matters

Nothing in the system agrees to the vet or the time. Anyone with the URL can record
`Helen Leary, 09:00` for any pet on any future date, and the owner page will then
display it beside the confirmation message "Your visit has been booked". The owner
learns a fact that is not one.

This is not "the spec does not do S1". It is that the spec ships the *display* of an
appointment before anything makes it an appointment, and the display is the part that
is expensive to take back. When S1 and S4 land, they must either honour every
free-typed vet/time pair already created through this form and seeded into three
databases, or visibly withdraw meaning that owners have already been shown. Either is
more expensive than not having made the claim.

The cheap remedies are all at spec level and none of them require S1: narrow the user
story to what the change delivers ("records which vet we are asking for"), or state in
the spec that the displayed vet and time are a request rather than a confirmation, or
accept the claim knowingly and write down that it is carried until S4.

## O2 — scope — high

### Claim

D2 makes the vet `NOT NULL` across three schemas. The accepted slice record assigns
the question of whether the vet field "can ever be required" to S4, not to S2. The
spec is aware it is constraining S4 and says so, but treats the constraint as a
consequence rather than as the pre-emption of a decision a human accepted as S4's.

### Evidence

Spec, D2:

> Every visit has exactly one vet. The column is `NOT NULL` with a foreign key to
> `vets`, and no path in the application can create a visit without one.

and:

> Consequence, stated plainly because it constrains S4: the existing
> `/owners/{ownerId}/pets/{petId}/visits/new` form must be able to supply a vet, so
> it gains a vet chooser in this slice (FR-3).

Slice record, S4 `decision_focus` (disposition `accepted`):

> Whether slot-booking replaces free-date visit creation or coexists with it is the
> user-visible decision with the widest blast radius. It decides whether the system
> has one booking concept or two, whether `VisitController` keeps its current POST
> and its `typeMismatch.visitDate` rejection, and **whether the vet field from S2 can
> ever be required**.

Note that the slice record's S2 `scope` also says "decide whether a vet is required
or optional on a visit". The two entries contradict each other; the spec resolves the
contradiction in S2's favour without naming that it exists.

### Why this matters

If S4 later chooses "the free-date form coexists, with no vet", that choice now
requires dropping a `NOT NULL` constraint and a foreign key in h2, mysql and postgres
together — precisely the three-dialect migration D1 rejected as too expensive when the
column in question was `visit_date`. The spec applies one cost standard to the
representation decision and a different one to the nullability decision, in the same
document.

The narrower problem: "This spec does **not** decide whether that form survives S4"
is not quite true once the column is `NOT NULL`. It does not decide whether the form
survives; it does decide that whatever survives must supply a vet. That is one of the
two halves of S4's question, decided here.

## O3 — scope — high

### Claim

FR-11 and AS-10 are written as though a schema file is a migration. For h2 that is
close enough — the database is in-memory and rebuilt every boot. For mysql and
postgres it is not: both `schema.sql` files use `CREATE TABLE IF NOT EXISTS`, so an
already-provisioned database never gains the columns. The spec includes no migration
and does not state that existing databases are out of scope.

### Evidence

> **FR-11** | The `visits` table in `db/h2`, `db/mysql` and `db/postgres` each gains
> a required start-time column and a required vet reference constrained as a foreign
> key to `vets`, landed in the same change.

`src/main/resources/db/mysql/schema.sql:50`:

```sql
CREATE TABLE IF NOT EXISTS visits (
```

`src/main/resources/db/postgres/schema.sql:47` is the same shape. `application.properties`
sets `spring.jpa.hibernate.ddl-auto=none`, so Hibernate will not add the columns either.

AS-10's verification is the parity check:

> When the schema-parity check runs / Then the `visits` table declares the same
> columns in all three

and `HARNESS.md` scopes that check to files: "Every column present in
`db/h2/schema.sql` exists in `db/mysql/schema.sql` and `db/postgres/schema.sql`".

The seed side compounds it. `db/mysql/data.sql:50` is
`INSERT IGNORE INTO visits VALUES (1, 7, '2010-03-04', 'rabies shot');` — against a
database that already holds row 1, the rewritten insert is silently skipped, so FR-12's
backfill does not happen there either. `db/postgres/data.sql:50` has the same property
via its `WHERE NOT EXISTS (SELECT * FROM visits WHERE id=1)` guard.

### Why this matters

The change will pass every gate in `HARNESS.md` — parity compares the files, and the
test suite runs against fresh H2 — and then fail at runtime against any mysql or
postgres database with a retained volume, with a Hibernate mapping that names columns
the table does not have. That failure is invisible to the loop, which is the exact
condition the schema-parity constraint exists to prevent ("a column added to one
database and forgotten in the others is invisible until something runs against that
database").

This is a scope objection rather than a risk one because the remedy is a scope
decision a human can take in one line: either put an `ALTER TABLE` path in scope, or
state in "Out of scope, restated as non-requirements" that existing mysql/postgres
databases must be recreated and that this is accepted.

## O4 — alternatives — high

### Claim

D2 presents a binary: required now, or optional forever. There is a third option the
spec does not weigh — land both columns nullable in this slice, and make them required
in S4 when the booking journey decides what creates a visit. It delivers every
observable outcome this spec claims, at strictly lower cost, and it is the option the
slice record's S4 reserves.

### Evidence

D2's reasoning, in full:

> Reasoning: the alternative leaves two kinds of visit in the system indefinitely —
> one that answers "who will see the pet" and one that cannot — and every screen then
> has to render the empty case forever. A clinic visit without a vet is not a thing
> the product means to represent.

"Indefinitely" and "forever" are doing the work in that sentence, and they are the
part that the third option removes.

### Why this matters

Under nullable-now, every acceptance scenario that describes something an owner can
see still holds: AS-1 (the form offers a vet and a time), AS-3, AS-4, AS-9, AS-11 and
AS-12 are untouched. The form can still *require* a vet at the controller (AS-5, AS-6
survive as validation, which is where the plan implements them anyway — see the plan's
`result.rejectValue("vet", "required")`). What changes is what does *not* land: no
`NOT NULL` in three dialects, so O2's foreclosure disappears; no obligation on D3's
backfill to be correct on the first attempt, because a null is legal; and every
existing test that posts a visit keeps working, rather than the plan's "Every existing
test that posts a visit will fail until it supplies `vet` and `startTime`."

D2's cost is real, but the spec has not shown it is a cost that must be paid *now*.
The objection is not that required is wrong — it is that the spec's reasoning does not
distinguish "required" from "required in this slice", and those have different prices.

## O5 — specification quality — high

### Claim

FR-3 specifies the vet chooser's contents and nothing else. An HTML `<select>` with no
empty option preselects its first entry, so a user who never touches the control
submits a vet. AS-5's precondition — "no vet chosen" — is then unreachable through the
interface FR-3 describes, and the failure mode is not a rejected form, it is a visit
silently booked with the wrong vet.

### Evidence

> **FR-3** | The new-visit form offers a choice of every vet in the clinic, each shown
> by first and last name, and a control for entering a start time.

> **AS-5 — A visit with no vet is rejected**
> ... When I submit the new-visit form with a future date, a start time and a
> description but no vet chosen

The fragment the plan extends carries no blank option —
`src/main/resources/templates/fragments/selectField.html:14`:

```html
<option th:each="item : ${items}" th:value="${item}" th:text="${item}">dog</option>
```

and the plan's replacement is described the same way: "a second fragment in the same
file, `selectVet (label, name, items)`, whose options carry `th:value="${item.id}"`
and `th:text="${item.firstName + ' ' + item.lastName}"`". Neither mentions a
placeholder.

There is a second, sharper inconsistency in the same area. D7's stated rationale is:

> a pre-filled control that a user can change is plainer than an empty required field.

That rationale applies verbatim to the vet field, and the spec neither applies it nor
says why it stops at the time. If the maintainer's answer is "pre-filling a vet would
be wrong, because it puts a clinician's name on a booking nobody chose" — which seems
right — then D7's rationale is weaker than stated for the time as well, and the
spec has not said which way it cuts.

### Why this matters

Two reasonable implementers will read FR-3 differently. One adds
`<option value="">` and AS-5 is reachable; one does not, and AS-5 can only be
provoked by a non-browser client. In the second implementation, every owner who does
not open the dropdown books with James Carter, and nothing in the spec, the plan's
tests (T-5 posts *without* the `vet` parameter, which is the other implementation's
world) or the harness detects it. That is a user-visible wrong outcome produced by an
implementation that satisfies every requirement as written.

## O6 — specification quality — high

### Claim

FR-15 states what to do when a translation is not available — flag it for a human —
but not what happens to *this change* while the flag is up. `HARNESS.md` answers that
question with a blocked pull request. The spec inherits a standing conflict between
directive 4 and the "Tests must pass" gate without naming it, and the plan quietly
assumes the conflict will not fire.

### Evidence

> **FR-15** | All new display text ... comes from message keys. New keys are added to
> `messages.properties` and to every locale bundle only with a genuine translation;
> English text is never copied into a locale bundle to satisfy the sync check. Any
> locale left without a trustworthy translation is flagged for a human.

`AGENTS.md` directive 4:

> If a trustworthy translation isn't available, add the key to `messages.properties`
> only, let the sync test fail, and flag the missing locales for a human.

`HARNESS.md`, **Tests must pass**:

> **Rule**: The project's test suite must pass with zero failures before any code is
> merged — **Enforcement**: deterministic — **Scope**: pr

The plan does not treat this as a live possibility: "two new keys, added to
`messages.properties` and to the ten locale bundles with genuine translations", with
`I18nPropertiesSyncTest` listed as T-18, a test that "must pass unchanged". There are
ten bundles (`de, en, es, fa, hi, ja, ko, pt, ru, tr`) and two new keys, so twenty
translations must all be trustworthy for this change to merge as planned.

### Why this matters

If even one locale lacks a trustworthy translation, the directive-compliant action —
leave the bundle alone and flag it — puts the branch in a state the PR gate rejects,
and the spec offers no route out. The predictable failure is that whoever is holding
the change at that moment reaches for the thing that makes the build green, which is
exactly the English-copying that directive 4 and FR-15 both forbid. A rule with no
sanctioned way to comply is a rule that gets routed around.

The spec is the right place to settle it, because the answer is a scope decision: does
this change merge with a red i18n test and a recorded flag, does it wait, or does it
use an existing already-translated key? Note the third is partly available — the plan
already does it for errors ("No new error key: missing vet and missing start time are
rejected with the existing, already-translated `required` code") and
`messages.properties` already carries `vets=Veterinarians` and `visitDate=Visit Date`
in every bundle. The spec does not consider whether the two new labels can be derived
from keys that already exist.

## O7 — specification quality — medium

### Claim

FR-14 requires times to display as `HH:mm` "identically in every locale". For the
read-only screens that is achievable. For the form control it is not: `<input
type="time">` renders in the browser's locale — `2:30 PM` for a US-English browser —
and no server-side format pattern changes that. FR-14 is therefore false of one of the
two surfaces this change touches, and the spec does not distinguish them.

### Evidence

> **FR-14** | Dates display as `yyyy-MM-dd` and times as `HH:mm`, identically in every
> locale. *(D5, AS-3)*

D5's reasoning:

> Time is shown as `HH:mm`, 24-hour, the same for every locale.

The plan's implementation:

> `fragments/inputField.html` | Add `<input th:case="'time'" class="form-control"
> type="time" th:field="*{__${name}__}" />` to the existing `th:switch`.

and AS-1 requires that control to be pre-filled: "the date is pre-filled with tomorrow
and the start time with 09:00."

### Why this matters

An implementer who takes FR-14 literally will conclude that `type="time"` is
non-compliant and reach for a text input with a pattern — which is a worse control, and
a change D1 explicitly wanted to avoid ("The form can express it with the fragments
already in the repository"). An implementer who does not will ship something that
contradicts a requirement, and AS-1 has no way to tell, because the value a MockMvc
test sees in the rendered HTML is always `09:00` regardless of what a browser paints.

The likely correct answer is that FR-14 governs *rendered display of stored visits* and
the wire format of the control, not the control's painted appearance. That is a
one-clause clarification, and without it the requirement is stated more broadly than it
can hold.

## O8 — specification quality — medium

### Claim

AS-11 is the only acceptance scenario for FR-13, and it is not falsifiable under the
project's own test conventions. `@OrderBy` is applied by the persistence provider when
a collection is loaded; in a `@WebMvcTest` with a mocked repository, the visits come
from a hand-built fixture and appear in insertion order. The test asserts the fixture,
not the mechanism.

### Evidence

> **AS-11 — Same-day visits read in time order**
> Given a pet with two visits on the same date, one at 09:00 and one at 15:00
> When I open the owner's page
> Then the 09:00 visit is listed before the 15:00 visit.

Plan, algorithm notes:

> **Ordering** is delegated to `@OrderBy` on the association rather than sorted in a
> controller or template

Plan, `owner/Pet.java`: "`@OrderBy("date ASC, startTime ASC")` on the visits
association (FR-13)". The field is
`private final Set<Visit> visits = new LinkedHashSet<>();` (`Pet.java:56-59`).

Plan, T-12, in `OwnerControllerTests`: "`ownerDetailsOrdersSameDayVisitsByTime` — a pet
with 09:00 and 15:00 visits on one date; the 09:00 row appears first in the rendered
page." `OwnerControllerTests` is a `@WebMvcTest`; `AGENTS.md` "Should follow" 2 makes
that the house style.

### Why this matters

A test that passes because the fixture was built in the right order will keep passing
if `@OrderBy` is removed, misspelled, or silently ignored. FR-13 then has the
appearance of coverage and none of the substance — and the ordering it protects is
only observable in production, against a real database, by a reader who happens to
have two same-day visits.

This one has a clean resolution: AS-11 belongs in `ClinicServiceTests`, the integration
class the plan already touches for T-15/T-16, where a real H2 load exercises the real
annotation. The objection is that the spec's acceptance scenario does not say where it
must be observed, and the plan then placed it where it cannot be.

## O9 — specification quality — medium

### Claim

The spec asserts that every functional requirement is testable. FR-17 is not tested,
by the plan's own admission, and AS-13 exists specifically so the absence of contention
handling is "a recorded decision rather than an oversight" — a purpose that a record
alone does not serve once someone changes the code.

### Evidence

Spec, preamble to the requirements table:

> Each is testable and traces to at least one scenario above.

AS-13's own justification:

> (This is the behaviour S5 will revisit. It is asserted so that the absence of
> contention handling is a recorded decision rather than an oversight.)

Plan, FR mapping:

> FR-17 | Asserted by absence; AS-13 is left untested at controller level because there
> is no code to test. If the maintainer wants it pinned, add an integration test
> creating two visits with the same vet and time and asserting both persist.

### Why this matters

AS-13's stated job is to stop a later reader mistaking the gap for an oversight. A
markdown file does that for a reader who finds the file. A test does it for everyone,
including the change that adds a unique index on `(vet_id, visit_date, start_time)`
because it looked obviously missing — which is exactly the change the slice record
anticipates ("a unique constraint is far cheaper to land with S1's table creation than
to retrofit once double-booked rows exist"). Without a test, that change goes in green
and S5's decision has been made by accident.

The plan already knows the remedy and has left it as a maintainer's option. The
objection is that the spec asserted universal testability and this is the one place the
assertion does not hold, so the maintainer should decide rather than inherit.

## O10 — implementation — medium

### Claim

D1 states one cost of two columns — that they can hold a date with no time — and
closes it. It does not state the cost that outlasts this slice: nothing ties
`visit_date` and `start_time` to each other or to anything else, so when S1's slots
arrive the system has two independent, unreconciled descriptions of when a booking is.

### Evidence

D1, the cost as stated:

> The cost, stated honestly: two columns can in principle hold a date with no time.
> FR-2 and FR-11 close that by making the start time required in both the schema and
> the form.

Slice record, S2 decision content, on the same choice:

> each puts the truth about a booking in a different place, and that determines how
> "taken" is computed and what happens when a published slot is withdrawn after someone
> has booked it.

And the state this spec leaves behind, FR-17: "any vet may be recorded at any time of
day on any future date."

### Why this matters

D1's argument for two columns is about the *migration* being cheap — additive columns,
no type change, existing rules survive. That argument is sound and I am not objecting
to it. The gap is that the spec then treats the representation question as fully
settled, when the slice record framed it as a question about *where the truth lives*,
and that half is not answered by "two columns are cheaper to add".

Concretely: when S1 lands, either a visit's `(vet_id, visit_date, start_time)` must be
matched back to a slot row by value — with no constraint, no index and no guarantee the
granularities agree — or the visit gains a `slot_id` and the three columns become a
denormalised copy that can drift. Both are more work than S2 has accounted for, and
neither is visible from D1 as written.

I am not asking this spec to build S1. I am asking it to record that the alignment
problem exists and that S2's answer is "by value, reconciled later", so that S1 is
specced knowing it.

## O11 — specification quality — low

### Claim

FR-9 widens the per-pet visits table on the owner page from two columns to four and
says nothing about the two-cell action row that shares that table, leaving the rendered
result undetermined.

### Evidence

> **FR-9** | The owner's page shows, for every visit of every pet, the visit's start
> time and the vet's name, alongside the existing date and description.

`src/main/resources/templates/owners/ownerDetails.html:67-74` — the visit rows and the
action row are siblings in one table:

```html
<tr th:each="visit : ${pet.visits}">
  <td th:text="${#temporals.format(visit.date, 'yyyy-MM-dd')}"></td>
  <td th:text="${visit?.description}"></td>
</tr>
<tr>
  <td><a ... th:text="#{editPet}">Edit Pet</a></td>
  <td><a ... th:text="#{addVisit}">Add Visit</a></td>
</tr>
```

### Why this matters

Low severity because the consequence is cosmetic and a human will see it. It is here
because no acceptance scenario would catch it: AS-3 asserts that `14:30` and
`Helen Leary` appear in the rendered content, which is true whether or not the Edit
Pet / Add Visit row now sits under the wrong headings. The spec should say whether the
action row spans the new width, or the decision should be recorded as the implementer's
to make.

## Explicitly not objecting to

- **D3 — backfilling the four seed visits rather than deleting them**: the reasoning
  given ("Deleting them would remove observable content to avoid inventing two fields
  of sample data, in a codebase whose entire seed set is invented sample data") is
  sound and self-aware, and the count of those rows is depended on by existing tests.
- **The h2 `DROP TABLE` ordering**: `db/h2/schema.sql` drops `vets` (line 2) before
  `visits` (line 4), which a new `fk_visits_vets` would make illegal — but the plan
  already names it as the one thing to verify, and H2 here is an in-memory database
  with a generated name, so the drops never encounter an existing table. Flagged and
  inert is not an objection.
- **Reusing the existing `required` error code instead of inventing error keys**: it
  is the smallest translation obligation that satisfies FR-15 and it matches the
  project's existing rejection style; it also happens to be the only part of the i18n
  surface that O6 does not put at risk.
- **Parsing the chosen vet by id rather than by name**: the plan's reasoning ("Two
  vets can share a name, so parsing by name would silently pick one") is correct and
  the id is the only stable handle.
- **The unspecified ordering of the vet dropdown**: `VetRepository.findAll()` declares
  no order and is `@Cacheable("vets")`, so the option order is whatever the database
  returns — but this is pre-existing behaviour of an existing method with six rows
  behind it, and the spec is not the right place to fix it.
- **The seeded visits all being in the past** (`2013-01-0x` in h2, `2008`–`2011` in
  mysql and postgres) while FR-7 requires new visits to be in the future: the seed rows
  have always been unmakeable through the form, and this change does not make that
  worse.
- **FR-16 and owner personal data**: the reasoning is correct — the vet's name is
  already published on the vets page, and no owner field is added to any output.
- **D5's choice of a 24-hour clock for display**: `14:30` is legible in every locale
  the project ships, and locale-sensitive time formatting genuinely is a wider change;
  my objection (O7) is about the *scope of the claim*, not the format.
- **D6 — ordering date first, then start time**: it is the only ordering that reads
  correctly, and it extends the `@OrderBy("date ASC")` already on `Pet.visits`; O8 is
  about how it is verified, not whether it is right.
- **That this spec does not build S1, S3, S4 or S5**: the slice record accepts all
  five slices separately and sanctions S2 landing first ("which makes S2 a viable first
  slice if the human wants the model change settled before any new concept enters the
  codebase"). Every objection above that names another slice names a foreclosure or a
  cost, not an absence.
