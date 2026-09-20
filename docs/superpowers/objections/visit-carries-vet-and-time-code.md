---
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
decision_record: decisions/2026-09-20-visit-carries-vet-and-time.md
date: 2026-09-20
mode: code
diaboli_model: claude-opus-5
objections:
  - id: O1
    category: implementation
    severity: high
    claim: "The change made start_time NOT NULL in all three dialects and left visit_date nullable, and the controller's date rule is skipped when the date is null, so an ordinary browser submission with the date field cleared creates a persisted visit with a vet and a start time and no date — the exact mirror of the hole D1 said FR-2 and FR-11 closed."
    evidence: "src/main/java/org/springframework/samples/petclinic/owner/VisitController.java:127 `if (visit.getDate() != null && !visit.getDate().isAfter(LocalDate.now()))`; lines 139 and 143 reject a null vet and a null startTime unconditionally. src/main/resources/db/h2/schema.sql:62-63 `visit_date  DATE,` / `start_time  TIME NOT NULL,` (mysql/schema.sql:54-55 and postgres/schema.sql:51-52 the same). src/main/resources/templates/fragments/inputField.html:14 emits the date control with no `required` attribute. No test in VisitControllerTests posts an empty `date` parameter."
    disposition: accepted
    disposition_rationale: "Fix the bug."
  - id: O2
    category: implementation
    severity: high
    claim: "The new fragment written by this change puts th:errors and th:text on the same span, so the text a user reads when the new vet or start-time field is rejected is the generic word from the `error` key, not the field's error message; nothing in the suite asserts rendered error text, and the comment that justifies the controller's two new guards asserts a rendering behaviour the template does not have."
    evidence: "src/main/resources/templates/fragments/selectVetField.html:26 `<span class=\"help-inline\" th:errors=\"*{__${name}__}\" th:text=\"#{error}\">Error</span>` — both attributes set the element body, and th:text (precedence 1300) runs after th:errors (1200). src/main/java/.../owner/VisitController.java:134-138 comment: `rejecting again would stack a second, misleading \"is required\" on a field the user did fill in`; src/test/java/.../owner/VisitControllerTests.java:228-229 `the controller's \"required\" rejection must stand aside or th:errors would render two errors for one bad value`. VisitControllerTests.java:206-220 and 232-246 assert against `BindingResult.MODEL_KEY_PREFIX + \"visit\"` pulled out of the ModelAndView, never against the response body."
    disposition: rejected
    disposition_rationale: "Reject O2, and pin it with a test. The premise is disproved: the span renders the field's message (`<span class=\"help-inline\">is required</span>`), th:errors is precedence 1700 not 1200 — 1200 is th:field — so th:errors runs after th:text and wins the body. Proved by rendering all three fragments and by javap on the jar on this build's classpath. The untested-span gap was real and is now pinned."
  - id: O3
    category: risk
    severity: high
    claim: "D2's NOT NULL on vet_id is the decision this change spends S4's option on, and no test anywhere asserts that either new constraint actually rejects anything; SchemaParityTest compares column presence and declared length only and never reads nullability, so a later change that drops NOT NULL from one dialect passes every gate in HARNESS.md."
    evidence: "src/test/java/.../harness/SchemaParityTest.java:55 `Pattern.compile(\"^\\\\s*([a-z_]+)\\\\s+([A-Za-z_]+)(?:\\\\((\\\\d+)\\\\))?\")` — name, type, length; line 148 `columns.put(name, column.group(3) == null ? null : Integer.valueOf(column.group(3)))`. ClinicServiceTests already owns the idiom at lines 279-281 (`assertThrows(DataIntegrityViolationException.class, ...)`) and does not use it for visits: shouldAddNewVisitForPet (220-240) and shouldFindVisitsByPetId (243-256) assert only that vet and startTime are non-null on rows that were written with them. FR-1 and FR-2: 'A visit cannot be created without one.'"
    disposition: accepted
    disposition_rationale: "Accept — teach SchemaParityTest nullability."
    disposition_note: "The parity half only. The second half of the claim — that no test asserts a database actually rejects a null — is not done: the check compares the three files to each other and starts no container. Spec O3 (provisioned databases never gaining the columns) is a different objection with the same number and stays deferred. This note is the dispatcher's record of scope, not the maintainer's rationale; the rationale line above is his words."
  - id: O4
    category: risk
    severity: medium
    claim: "Both screens dereference visit.vet with no safe navigation, on the line directly above one that keeps it for description, so a single visit row without a vet takes the whole owner page down with a template exception rather than rendering one empty cell."
    evidence: "src/main/resources/templates/owners/ownerDetails.html:72 `<td th:text=\"${visit.vet.firstName + ' ' + visit.vet.lastName}\"></td>` against line 73 `<td th:text=\"${visit?.description}\"></td>`. src/main/resources/templates/pets/createOrUpdateVisitForm.html:55 the same expression, with line 56 `<td th:text=\" ${visit.description}\"></td>` having lost the `?` the owner page kept."
    disposition: rejected
    disposition_rationale: "Fail loud, the reviewer is right. vet_id is NOT NULL in three dialects, the controller rejects a null vet, and O3 now guards the constraint itself. A vet-less visit is corruption, and a blank cell would hide it. The time cell is the lenient one, not the vet cell the strict one."
  - id: O5
    category: implementation
    severity: medium
    claim: "The fixture comment added to OwnerControllerTests states the principle that a visit fixture without a vet models a state the product forbids, and the next line leaves startTime null although start_time is NOT NULL in the same three schemas; showOwner then renders a visit row with an empty Visit Time cell and passes."
    evidence: "src/test/java/.../owner/OwnerControllerTests.java:105-110 `Visit visit = new Visit(); visit.setDate(LocalDate.now()); // A visit carries a vet from this slice on: vet_id is NOT NULL in all three / // schemas, so a fixture without one models a state the product forbids. visit.setVet(helenLeary()); george.getPet(\"Max\").getVisits().add(visit);`. ownerDetails.html:71 renders `${#temporals.format(visit.startTime, 'HH:mm')}`, which returns null for a null target. showOwner (252-265) renders that fixture and is green."
    disposition: accepted
    disposition_rationale: "Accept — finish the fixture."
  - id: O6
    category: implementation
    severity: medium
    claim: "FR-20's remedy puts a MessageSource into a controller and flashes an already-resolved string, making VisitController the only place in the codebase where user-visible text is composed outside a template; the decision record's account of what is left hardcoded names four remaining flash messages and omits three more."
    evidence: "src/main/java/.../owner/VisitController.java:24, 55, 57 and 153-154 `redirectAttributes.addFlashAttribute(\"message\", this.messages.getMessage(\"visitBooked\", null, LocaleContextHolder.getLocale()))` — no other controller imports MessageSource. owners/ownerDetails.html:10 renders it as `<span th:text=\"${message}\"></span>`, which cannot re-resolve a string. decisions/2026-09-20-visit-carries-vet-and-time.md:135-138: 'OwnerController and PetController carry the same hardcoded flash-message pattern in four more places ... one of the application's five flash messages is keyed and four are not' — against OwnerController.java:80, 148 and 154, three hardcoded English `error` flash strings the count does not reach."
    disposition: accepted
    disposition_rationale: "Accept — correct the count."
  - id: O7
    category: implementation
    severity: low
    claim: "FR-18's third line moved the last consumer of the `date` key to `visitDate`, leaving `date` present and translated in eleven properties files with nothing referencing it — the same condition the spec refused to create for the `visit` key — and I18nPropertiesSyncTest can only ever report keys that are missing, never keys that are unused."
    evidence: "No `#{date}` remains in src/main/resources/templates (grep). src/main/resources/messages/messages.properties:40 `date=Date`, messages_de.properties:40 `date=Datum`, and the same line in ru, tr, hi, es, pt, ja, fa, ko. Spec FR-19: '`visit` is therefore dropped from this requirement rather than written into eleven properties files with no consumer.' src/test/java/.../system/I18nPropertiesSyncTest.java:124-125 `Set<String> missingKeys = new TreeSet<>(baseKeys); missingKeys.removeAll(props.stringPropertyNames());`"
    disposition: accepted
    disposition_rationale: "Accept — note it, do not delete."
---

# Objections — visit carries a vet and a time of day (code)

Code mode, against the implementation on `level-4-orchestrating`. Grounded in the
source, templates, schemas, seed files, message bundles and tests as they stand,
with the spec, the plan, `decisions/2026-09-20-visit-carries-vet-and-time.md`,
`AGENTS.md` and `HARNESS.md` read for intent.

This file numbers from O1 in its own right; it does **not** continue the spec-mode
numbering in `visit-carries-vet-and-time.md`. Where a spec-mode objection is
referred to below it is written as "spec O5", "spec O8" and so on.

Nothing already surfaced and knowingly disposed is re-raised. Specifically out of
scope here: the owner package's dependency on the vet package; `VetFormatter`
being a globally registered `Formatter<Vet>` for one call site; requiredness
enforced in the schema and the controller rather than on the entity; the absence
of a uniqueness constraint, a duration, a blank option in the chooser, and a fix
for the owner page's two-cell action row; the seed data not demonstrating
contention or same-day ordering; the date default sitting in `Visit`'s
constructor while the time default sits in the controller; and the nine deferred
spec-mode objections O3–O11. Two objections below stand *beside* disposed items
without reopening them, and each says so where it does.

**Tooling note, recorded because it bounds this record.** This pass ran with
Read, Glob and Grep and no Bash, so it could not run `git diff
level-3-regulating..HEAD` and cannot certify that every hunk in the change is
accounted for. Every quotation is from the working tree at its stated line. Two
consequences are named in *What was not challenged* at the end.

## O1 — implementation — high

### Claim

The stance this change is built on is that a booking's *shape* is guaranteed. The
code guarantees two thirds of that shape. `vet_id` and `start_time` are
`NOT NULL` in all three dialects and are rejected in the controller when absent.
`visit_date` is nullable in all three dialects, and the controller's date rule
stands aside when the date is null. An ordinary browser submission with the date
box cleared therefore persists a visit that has a vet and a start time and no
day.

### Evidence

`src/main/java/org/springframework/samples/petclinic/owner/VisitController.java:127-145`
— three null tests in a row, under two different policies:

```java
if (visit.getDate() != null && !visit.getDate().isAfter(LocalDate.now())) {
    result.rejectValue("date", "typeMismatch.visitDate");
}
...
if (visit.getVet() == null && !result.hasFieldErrors("vet")) {
    result.rejectValue("vet", "required");
}

if (visit.getStartTime() == null && !result.hasFieldErrors("startTime")) {
    result.rejectValue("startTime", "required");
}
```

`src/main/resources/db/h2/schema.sql:58-65`:

```sql
CREATE TABLE visits (
  id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  pet_id      INTEGER,
  vet_id      INTEGER NOT NULL,
  visit_date  DATE,
  start_time  TIME NOT NULL,
  description VARCHAR(255)
);
```

`db/mysql/schema.sql:54-55` and `db/postgres/schema.sql:51-52` are the same:
`visit_date` takes no `NOT NULL`, `start_time` does.

`src/main/resources/templates/fragments/inputField.html:14`:

```html
<input th:case="'date'" class="form-control" type="date" th:field="*{__${name}__}" th:min="${minVisitDate}" />
```

No `required` attribute, so a cleared date box submits `date=` — an empty string,
which Spring's parser converter turns into `null` without recording a field
error. `description` is protected by `@NotBlank` (`Visit.java:54`); `date` is
protected by nothing once it is null.

`Visit.java:57-66`, the Javadoc this change added, describes the hole it is
leaving:

```java
 * The date below has exactly that flaw and keeps it: a
 * submission that omits the date is silently accepted as tomorrow rather than
 * rejected, which is pre-existing behaviour no requirement in this slice reaches.
```

### Why this matters

The recorded reasoning is half right, and the half it misses is the damaging
half. A submission that *omits* the parameter is indeed accepted as tomorrow —
harmless. A submission that *sends an empty* parameter is accepted as `null`,
which is not tomorrow and is not any day, and the schema permits the row. The
comment tells the next reader that the worst case is a silently defaulted date;
the worst case is a booked visit with no date at all.

The spec's D1 named precisely this class of defect and claimed it closed:
"two columns can in principle hold a date with no time. FR-2 and FR-11 close that
by making the start time required." The mirror — a time with no date — was never
considered, and the implementation has now made it reachable from a browser and
storable in all three databases. It is also the one state that makes
`@OrderBy("date ASC, startTime ASC")` (`Pet.java:58`) order by a null, and the one
state in which `ownerDetails.html:70` renders a visit with a blank day beside a
populated time.

This is not the disposed "date default lives in the constructor" item. That item
is about *where* a default is applied. This is about a required-in-spirit field
having no server-side rejection at all, sitting between two fields that just
gained one, in the same method.

## O2 — implementation — high

### Claim

This change added two required fields to a form and, in the fragment it wrote for
one of them, copied a construction that prevents the user from being told what is
wrong with either. `th:errors` and `th:text` both set the element body; `th:text`
runs second. The span therefore renders the `error` key — the bare word "Error" —
in place of the field's message. No test in the suite looks at the rendered text
of a rejected field, and the comment that justifies the change's two new guards
rests on a rendering behaviour the template does not have.

### Evidence

`src/main/resources/templates/fragments/selectVetField.html:24-27`, a file this
change created:

```html
<th:block th:if="${!valid}">
  <span class="fa fa-remove form-control-feedback" aria-hidden="true"></span>
  <span class="help-inline" th:errors="*{__${name}__}" th:text="#{error}">Error</span>
</th:block>
```

The same pair is on `fragments/inputField.html:20` and
`fragments/selectField.html:19`, which is where it was copied from; the new field
labels this change keyed (`visitTime`, `vet`) resolve through those two files as
well. In Thymeleaf's Spring dialect `th:errors` carries attribute precedence
1200 and `th:text` 1300, and both call `setBody`, so the later one wins.

`VisitController.java:134-138`:

```java
// Both rejections are skipped when the binder has already recorded a field
// error: an unknown vet id or an unparseable time leaves a typeMismatch error
// and a null value, and rejecting again would stack a second, misleading
// "is required" on a field the user did fill in.
```

`VisitControllerTests.java:225-229` repeats the claim:

```java
// the controller's "required" rejection must stand aside or th:errors would
// render two errors for one bad value.
```

And the tests that pin it never render anything —
`VisitControllerTests.java:206-220` and `232-246` both reach into the model:

```java
.getModel()
.get(BindingResult.MODEL_KEY_PREFIX + "visit");

assertThat(result.getFieldErrors("vet")).as("errors on the vet field").hasSize(1);
```

There is also no `typeMismatch.startTime` or `typeMismatch.vet` key in
`messages.properties`, so on the branch where the binder rejects the value, the
message Spring would otherwise resolve is its own English default naming
`java.time.LocalTime` and `org.springframework.samples.petclinic.vet.Vet`.

### Why this matters

Two things are wrong and they compound.

First, the user-visible outcome of rejecting a vet or a start time is untested.
`AGENTS.md` "Should follow" 2 asks for tests that "assert on status, view name,
model attributes and rendered content"; these tests assert the first three and
deliberately avoid the fourth, on the two paths where the fourth is the whole
point. The reason the fourth is avoided is the defect itself — there is nothing
useful in the rendered content to assert.

Second, the justification written into both the controller and the test is a
claim about what `th:errors` renders, and it cannot be true of a span whose body
`th:text` overwrites. The guards may still be the right code — one error in the
model beats two — but the reason on the record is not the reason that holds, and
the next person to touch this will reason from a comment that does not describe
their template. If the precedence reading is wrong, the comment is still
unverified by any test, and two body-setting attributes on one element is still
an ambiguity nobody has pinned.

The cheapest disproof or confirmation is one assertion on the response body of
the T-5 path. It does not exist.

## O3 — risk — high

### Claim

`vet_id NOT NULL` is the single most consequential thing this change puts in the
ground: it is the constraint D2 spends S4's option on, and the one whose reversal
the spec prices at a three-dialect migration. Nothing in the suite asserts that
it, or `start_time NOT NULL`, rejects anything. The harness constraint that exists
to stop dialect drift reads column names, types and lengths, and never reads
nullability.

### Evidence

`src/test/java/org/springframework/samples/petclinic/harness/SchemaParityTest.java:54-56`:

```java
/** A column line: name, type, and a length when the dialect declares one. */
private static final Pattern COLUMN = Pattern.compile("^\\s*([a-z_]+)\\s+([A-Za-z_]+)(?:\\((\\d+)\\))?",
        Pattern.CASE_INSENSITIVE);
```

and line 148, which is everything the parser retains:

```java
columns.put(name, column.group(3) == null ? null : Integer.valueOf(column.group(3)));
```

`NOT NULL` is never captured, so `everyColumnExistsInEveryDialect` and
`lengthLimitsAgreeWhereBothDeclareThem` are both satisfied by a `visits` table
that declares `vet_id INT` in mysql and `vet_id INT NOT NULL` in h2.

The project already owns the idiom for asserting a constraint, in the file that
runs against the real database —
`src/test/java/.../service/ClinicServiceTests.java:279-281`:

```java
assertThrows(DataIntegrityViolationException.class, () -> {
    this.owners.saveAndFlush(owner);
});
```

The two visit tests in that same class do not use it.
`shouldAddNewVisitForPet` (220-240) and `shouldFindVisitsByPetId` (243-256)
assert only `allMatch(value -> value.getVet() != null)` on rows that were written
with a vet.

Spec FR-1 and FR-2: "A visit cannot be created without one."

### Why this matters

"Cannot be created" is currently verified for exactly one path — the controller —
and the controller's guard is a nullity test in Java that a later change can
delete in one line. The database constraint that would catch that deletion is
asserted by nothing, and the harness check that would catch it being dropped from
one dialect is structurally blind to it.

That matters more here than it would for an ordinary column, because the spec
made this constraint load-bearing for a decision it took away from another slice.
D2's whole argument is that the cost of reversal is a visible, expensive,
three-dialect migration. If nullability can drift out of one dialect without any
gate noticing, the reversal is not expensive and not visible, and D2's argument
has nothing behind it.

This is not spec O3 (already-provisioned databases never gaining the columns).
That objection is about `CREATE TABLE IF NOT EXISTS` and is deferred. This one is
about the files themselves: what the harness reads out of them, and what no test
asserts about the constraint they declare.

## O4 — risk — medium

### Claim

Both screens dereference `visit.vet` two levels deep with no safe navigation, on
the line directly above one that keeps safe navigation for `description`. A
single visit row with no vet does not render a blank cell; it throws out of the
template and takes the whole owner page — every pet, every visit, the owner's own
details — with it.

### Evidence

`src/main/resources/templates/owners/ownerDetails.html:69-74`:

```html
<tr th:each="visit : ${pet.visits}">
  <td th:text="${#temporals.format(visit.date, 'yyyy-MM-dd')}"></td>
  <td th:text="${#temporals.format(visit.startTime, 'HH:mm')}"></td>
  <td th:text="${visit.vet.firstName + ' ' + visit.vet.lastName}"></td>
  <td th:text="${visit?.description}"></td>
</tr>
```

`src/main/resources/templates/pets/createOrUpdateVisitForm.html:52-57` is the same
row, and there the `?` on description did not survive the rewrite:

```html
<tr th:if="${!visit['new']}" th:each="visit : ${pet.visits}">
  <td th:text="${#temporals.format(visit.date, 'yyyy-MM-dd')}"></td>
  <td th:text="${#temporals.format(visit.startTime, 'HH:mm')}"></td>
  <td th:text="${visit.vet.firstName + ' ' + visit.vet.lastName}"></td>
  <td th:text=" ${visit.description}"></td>
</tr>
```

The two temporal cells degrade gracefully — `#temporals.format` returns null for
a null target and the cell renders empty. The vet cell does not.

### Why this matters

The failure modes of the four cells in one row are now three different things: a
null date or time is a blank cell, a null description is a blank cell on one
screen and a thrown expression on the other, and a null vet is a thrown
expression on both. Nobody chose that distribution; it is what fell out of which
expressions got rewritten.

Reachability is genuinely constrained by `vet_id NOT NULL` — and I am not
re-raising O3's point about databases that never gained the column. What makes
this worth a maintainer's attention anyway is the blast radius. Every other
degradation on this page costs one cell. This one costs the page. Given that the
same change left `visit_date` nullable (O1) and left nullability unasserted (O3),
the page's robustness is resting on a constraint whose enforcement nothing checks.

It is also the reason the fixture in O5 was corrected exactly as far as it was and
no further: the vet had to be added or the test would have failed; the start time
did not, so it was not.

## O5 — implementation — medium

### Claim

The comment this change added to the shared `OwnerControllerTests` fixture states
a principle — a visit fixture without a vet models a state the product forbids —
and the line it is attached to breaks the same principle for the sibling column.
`start_time` is `NOT NULL` in the same three schemas; the fixture leaves it null.
The canonical owner-page test then renders a visit row with an empty Visit Time
cell and passes.

### Evidence

`src/test/java/org/springframework/samples/petclinic/owner/OwnerControllerTests.java:105-110`:

```java
Visit visit = new Visit();
visit.setDate(LocalDate.now());
// A visit carries a vet from this slice on: vet_id is NOT NULL in all three
// schemas, so a fixture without one models a state the product forbids.
visit.setVet(helenLeary());
george.getPet("Max").getVisits().add(visit);
```

against `db/h2/schema.sql:63` `start_time  TIME NOT NULL`, and against the
helper twenty lines further down (`OwnerControllerTests.java:299-306`) which does
set all four fields and is used only by the two new tests.

`showOwner` (`OwnerControllerTests.java:252-265`) renders that fixture through
`ownerDetails.html:71`, which formats a null `startTime` to an empty cell, and
asserts only that `pets` has a visit.

### Why this matters

The comment is the strongest possible statement that this fixture was thought
about, which makes it the most misleading place for the thought to have stopped
one field short. A reader who trusts it will believe the fixture now models a
legal visit. It models a visit that no database in this project would accept.

The consequence is a live coverage hole, not a tidiness complaint: the suite's
most-used owner-page fixture exercises the FR-9 row with the Visit Time cell
empty, so no assertion anywhere fails if the owner page stops rendering start
times. FR-9 says the page shows the start time "for every visit of every pet";
the default fixture demonstrates a visit for which it shows nothing, and that is
green.

Worth stating plainly because it explains the shape of the mistake: the fixture
was corrected exactly as far as the template's null-safety forced it (O4), and
not one field further. That is the template's inconsistency selecting the tests'
fidelity.

## O6 — implementation — medium

### Claim

FR-20's remedy moved the confirmation sentence out of a controller literal and
into a controller *lookup*. `VisitController` is now the only place in the
application that injects a `MessageSource`, resolves display text in Java and
flashes the rendered result — text the receiving template cannot re-resolve. The
decision record's inventory of what deliberately stays hardcoded undercounts what
is actually left.

### Evidence

`src/main/java/.../owner/VisitController.java:153-154`:

```java
redirectAttributes.addFlashAttribute("message",
        this.messages.getMessage("visitBooked", null, LocaleContextHolder.getLocale()));
```

No other file under `src/main/java` imports `MessageSource` (grep). The receiving
end, `owners/ownerDetails.html:9-11`:

```html
<div th:if="${message}" class="alert alert-success" id="success-message">
  <span th:text="${message}"></span>
</div>
```

`decisions/2026-09-20-visit-carries-vet-and-time.md:135-139`:

> `OwnerController` and `PetController` carry the same hardcoded flash-message
> pattern in four more places. This change edits none of those files ... After
> this change one of the application's five flash messages is keyed and four are
> not.

The count is right for `message` (`PetController.java:135` and `:177`,
`OwnerController.java:85` and `:160`). It does not reach
`OwnerController.java:80`, `:148` and `:154` — three hardcoded English `error`
flash strings rendered through the same page, by `ownerDetails.html:13-15`. The
real figure is one keyed and seven not.

### Why this matters

Two costs, both landing on whoever does the other six.

The construction: the application now demonstrates two ways of getting
user-visible text to a screen, and the new one is the one directive 3 does not
govern, because directive 3 is about templates. Anyone closing the remaining
seven has to pick, and this change shows only the pick that puts an English-
resolving `MessageSource` into every controller. Flashing the *key* and resolving
it in `ownerDetails.html` would have left all display text in the layer the
directive names and would not have needed a new constructor argument; that
alternative is not weighed anywhere. It is also the difference between a flash
attribute whose locale is frozen at POST time and one resolved when the page is
drawn.

The count: the decision record exists so a future reader finds the honest
account. Its account of the residual inconsistency is off by three in the
direction that makes the residue look smaller.

## O7 — implementation — low

### Claim

FR-18's third line — the one the spec named separately so it could be declined —
moved the last consumer of the `date` message key to `visitDate`. `date` is now
present and translated in eleven properties files with nothing referencing it:
the condition the same spec refused to create for the `visit` key, created for a
different key in the same sitting, unremarked. `I18nPropertiesSyncTest` is
structurally incapable of reporting it.

### Evidence

No `#{date}` remains anywhere in `src/main/resources/templates` (grep for
`#\{date\}` returns nothing; the only surviving `#{new}` is
`pets/createOrUpdatePetForm.html:8`). The key is still everywhere:
`messages.properties:40 date=Date`, `messages_de.properties:40 date=Datum`,
`messages_ru.properties:40 date=Дата`, and the same line in tr, hi, es, pt, ja,
fa and ko. (`typeMismatch.date` on line 7 is a different key, still resolved for
the date field's binder errors.)

Spec FR-19, on the key it declined to add:

> `visit` is therefore **dropped from this requirement** rather than written into
> eleven properties files with no consumer.

`src/test/java/.../system/I18nPropertiesSyncTest.java:124-125`:

```java
Set<String> missingKeys = new TreeSet<>(baseKeys);
missingKeys.removeAll(props.stringPropertyNames());
```

The check runs in one direction only. A key with no consumer is invisible to it,
for ever.

### Why this matters

Low severity because nothing breaks and no user sees it. It is here because of
where it sits. This change reasoned carefully and publicly about not leaving a
key without a consumer, then left one, and the decision record — which does
narrate FR-18's third line at `decisions/2026-09-20-...:123-125` — does not
mention that the line orphaned anything.

The cost is a small, permanent ambiguity for the next person to touch these
bundles: `date` is now indistinguishable from a key held in reserve, in eleven
files, with a tool that will never tell them otherwise. The remedy is one line of
disclosure, or eleven deletions.

## Explicitly not objecting to

- **The vet chooser living in its own fragment file.** The reasoning in
  `decisions/2026-09-20-...:209-229` is correct about the mechanism — a fragment
  expression is a markup selector and matches every `<select>` in the file — and
  the escape hatch it invokes ("a genuinely new kind of field") is the right one.
  I checked whether the fix is protected against being undone and concluded that
  it is: a second `<select>` back in `selectField.html` would render
  `item.firstName` against a `PetType` and `PetControllerTests` fails with a
  SpringEL evaluation error, exactly as the record describes. A comment plus a
  loud test failure is adequate.
- **`VetFormatter` parsing by id, and `print` returning the empty string for a
  vet with no id.** `src/main/java/.../vet/VetFormatter.java:44-59`. Two vets can
  share a name; the id is the only stable handle, and the empty string is correct
  for a `value` attribute where `PetTypeFormatter`'s `<null>` literal would not
  be. The per-parse `findAll()` scan is over a `@Cacheable("vets")` collection of
  six rows (`VetRepository.java:44-46`), so it costs nothing.
- **The H2 `DROP TABLE` reorder.** `db/h2/schema.sql:1-7` now drops `visits`
  before `vets`. This was the plan's named risk, it was real, and moving one line
  is the smallest fix that works.
- **The two `hasFieldErrors` guards as model-level behaviour.**
  `VisitController.java:139` and `:143`. One error per bad value is the right
  outcome and `VisitControllerTests` pins the count rather than the presence,
  which is the assertion that stops the guard being deleted. My O2 is about the
  *reason recorded* for them and about what a user reads, not about whether the
  guards belong.
- **`@OrderBy("date ASC, startTime ASC")` added with nothing able to fail without
  it** (`Pet.java:58`). That is deferred spec O8 firing as predicted, the
  implementer wrote it down, and re-raising it would be re-litigating a disposed
  item. The annotation itself is right and `ORDERED_SET` semantics preserve the
  order through the `Set`.
- **The seed rewrite.** `db/h2/data.sql:50-53`, `db/mysql/data.sql:50-53`,
  `db/postgres/data.sql:50-53` all carry explicit column lists, keep their four
  rows, their pets, their dates and their descriptions, and h2 omitting `id` while
  mysql includes it is each dialect's own identity idiom, not drift.
- **The four translations.** `messages*.properties` carry real, inflected strings
  in all nine non-English bundles — `Neuer Besuch`, not `Neu Besuch` — and T-20
  pins the German heading against the key rather than against a literal. Directive
  4 was honoured and the record names, unprompted, the four `visitBooked`
  sentences where a native speaker should check register.
- **The `th:if`/`th:each` reasoning on the previous-visits rows.**
  `createOrUpdateVisitForm.html:52`. `th:each` does bind first, the `visit` in the
  condition is the row, and the blank model visit is correctly kept out of the
  list. The record checked this before relying on it and the check is sound.
- **The form's field order, and the `'time'` case added to the shared
  `inputField.html`.** Date beside time, description last, is a reasonable call
  that no requirement fixes; extending the existing `th:switch` is the smallest
  diff and touches no other call site's behaviour.

## What was not challenged, and why

- Everything on the disposed list, including the nine deferred spec-mode
  objections. O3 and O4 each state where they stop short of spec O3 and of the
  disposed requiredness item.
- The `CREATE TABLE IF NOT EXISTS` migration gap (spec O3, deferred), the absent
  blank option in the vet chooser (spec O5, deferred) and `@OrderBy` being
  untestable under `@WebMvcTest` (spec O8, deferred).
- The twelve pending choice stories and O6's translation-gate exposure.
- **Bounded by tooling.** Without Bash this pass could not diff against
  `level-3-regulating`, so it cannot certify that every hunk is accounted for; in
  particular it could not establish whether the `visit.getDate() != null` guard in
  O1 was written by this change or inherited. O1 is framed to stand either way —
  the asymmetry between that guard and the two written beside it, and the `Visit`
  constructor Javadoc that mis-describes the hole, are unambiguously this change's
  work.
- **O2's precedence reading** (`th:text` 1300 after `th:errors` 1200) is from the
  Thymeleaf Spring dialect's published attribute precedences, not from a rendered
  page. The second half of O2 — that no test asserts rendered error text, and that
  the recorded justification is therefore unverified — does not depend on it.
