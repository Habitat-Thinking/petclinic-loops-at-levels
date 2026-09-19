---
slice: S2
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
title: "Plan — visit carries a vet and a time of day"
date: 2026-09-19
revised: 2026-09-19 — choice story #6 accepted (D8 fixes the two new keys' English words; FR-18 keys the form's two existing labels); choice stories #12, #15 and #17 accepted (FR-18 re-keyed to `visitDate`; FR-19 keys the form heading; FR-20 keys the confirmation message; the stance subsection routed to the decision record); FR-19 re-keyed to a single `newVisit` heading key (two-key composition abandoned, `visit` dropped unused, the heading's `visit['new']` conditional removed, T-20 and the i18n cost table restated)
---

# Plan — visit carries a vet and a time of day

Implements the functional requirements in
[visit-carries-vet-and-time.md](visit-carries-vet-and-time.md). Nothing here
adds behaviour the spec does not state; where this file names a class or a
column it is an implementation choice, not a new requirement.

## Adjudication note (2026-09-19)

The spec was revised after the maintainer adjudicated
[the objection record](../objections/visit-carries-vet-and-time.md): O1 and O2
accepted, O3–O11 deferred. **Neither accepted objection changes this plan.** O1
narrowed the spec's user story only — no screen text, no new message key, so
the template and `messages*.properties` work below is unchanged. O2 kept
`NOT NULL` and a foreign key exactly as planned and only required the spec to
name what that forecloses for S4. Every file, test and mapping below stands as
written.

The nine deferred objections are not addressed here. Several name this plan
directly — O5 (the vet chooser's missing blank option), O7 (`type="time"` and
FR-14), O8 (T-12's placement in `OwnerControllerTests`), O9 (FR-17 untested) —
and the plan is deliberately left as it was. See the spec's *Adjudication of
objections* section; the deferrals have no stated trigger.

## Revision note (2026-09-19, choice story #6)

Two narrow additions, both from choice story #6 and both settled by the
maintainer in the spec:

- **D8** fixes the English behind the two new keys: `visitTime` = `Visit Time`
  and `vet` = `Veterinarian`. This changes no file in the table below, only what
  goes in the properties values.
- **FR-18** swaps the hardcoded `'Date'` and `'Description'` literals in
  `pets/createOrUpdateVisitForm.html` for existing keys, closing a directive-3
  violation in the file this change already edits. It is a stated exception to
  directive 9 and adds **no** new keys and **no** translations. It adds one
  test, T-19, and one requirement row to the mapping.

Nothing else in this plan changed. No test was renumbered.

## Revision note (2026-09-19, choice stories #12, #15 and #17)

Four narrow changes, all settled by the maintainer in the spec. Three of them
move the translation count; one moves nothing but a list.

- **FR-18 is re-keyed** (#12). The form's date label takes `visitDate`, not
  `date`. Both keys already exist and are already translated in all ten
  bundles, so the cost is still zero — but `visitDate` is the key the owner's
  page already uses for the same column, so after this change the two screens
  head it identically in every locale instead of reading `Datum` against
  `Besuchsdatum`. The form's previous-visits table heading (line 48, already
  keyed to `date`) moves with the label for the same reason; that one line is
  consistency rather than compliance and is flagged as separable. T-19's German
  assertion changes with it.
- **FR-19 keys the form's `<h2>`** (#15). One new key, `visit` = `Visit`.
  **+10 translations.** Derivable by extraction — every bundle that translates
  `addVisit` and `visitDate` already contains the word inside a phrase.
- **FR-20 keys the confirmation message** (#7, via the #15 boundary argument).
  One new key, `visitBooked` = `Your visit has been booked`. **+10
  translations, and these are the expensive ones**: a full sentence, derivable
  from nothing any bundle already holds. This is the line item that raises
  deferred objection O6's exposure; see the i18n constraint below.
- **The stance subsection joins the decision record** (#17). A list entry, no
  new decision, no D-number, nothing renumbered.

The change's translation obligation therefore goes from **twenty strings to
forty**, across four keys and ten bundles. Two new tests, T-20 and T-21, and one
new constructor dependency on `VisitController`. Nothing else changed and no
test was renumbered.

*(The `visit` = `Visit` line above is superseded by the next revision note. It
is left here because it is the record of what the #15 revision decided, not a
statement of what this plan now specifies.)*

## Revision note (2026-09-19, FR-19 re-keyed to one heading key)

One change, to FR-19 only, and the maintainer settled it in the spec.

- **The `<h2>` resolves from a single key.** `newVisit` = `New Visit`, one key
  holding the whole heading, replaces the `#{new}` + `#{visit}` composition this
  plan specified. The reason is the one this plan already wrote down as a known
  consequence and did not fix: a heading assembled from two keys inherits the
  grammar of neither, so German gets *Neu Besuch* where it wants *Neuer Besuch*,
  and no composition of two keys can produce the inflection. The plan's own
  risk list named the per-locale `newVisit` key as the fix and called it wider
  than this change. It has been taken.
- **The `visit` key is dropped.** It had exactly one consumer, this heading.
  Nothing else in this change references it (the form's headings are
  `visitDate`, `description`, `visitTime`, `vet`; its button is `addVisit`; its
  section is `previousVisits`), so it is not added to eleven properties files
  with nothing to use it.
- **The heading's `visit['new']` conditional is removed.** `VisitController`
  has two mappings, both `/owners/{ownerId}/pets/{petId}/visits/new`, and its
  `@ModelAttribute("visit")` builds a fresh `Visit` before each, so the
  condition is always true and the non-new heading is unreachable. The heading
  becomes an unconditional `th:text="#{newVisit}"`. The `${!visit['new']}`
  condition on the previous-visits rows further down the same template is a
  different thing — its `visit` is `th:each`'s loop variable — and is not
  touched. `#{new}` keeps a consumer in `pets/createOrUpdatePetForm.html`, which
  this change does not edit, so the `new` key is not orphaned.
- **T-20's assertion changes** to the single key. T-18's key list changes with
  it. No test is renumbered and no test is added.

**The count does not move; the cost does.** Still four keys and forty
translations. But `newVisit` is not derivable the way `visit` was: every bundle
holds both words, none holds the grammar that joins them. So the derivable share
falls from thirty to twenty and the underivable share rises from ten to twenty.
**This is the second increase to this change's translation obligation in one
sitting** — the first took it from twenty strings to forty — and it enlarges
deferred objection **O6** again. O6 remains deferred and unaddressed.

## Constraints this plan is shaped by

- **Layering** stays controller → repository → entity. No service layer. The
  visit change lives in the `owner` package; the one new class that belongs to
  the vet concept lives in the `vet` package.
- **Schema parity**: all three `schema.sql` and all three `data.sql` change in
  the same commit; `SchemaParityTest` compares column presence across h2, mysql
  and postgres, and length limits where h2 and mysql both declare one.
- **Message keys, real translations**: **four** new keys and no more, added to
  `messages.properties` and to the ten locale bundles with genuine
  translations — **forty translations in total**, up from twenty. They are not
  equally expensive, and the difference matters more than the count:

  | Key | English | From | Cost per bundle |
  |---|---|---|---|
  | `visitTime` | `Visit Time` | FR-15, D8 | Derivable: parallels the translated `visitDate` (`Besuchsdatum` → `Besuchszeit`, `Fecha de visita` → `Hora de visita`) |
  | `vet` | `Veterinarian` | FR-15, D8 | Derivable: the singular of the translated `vets` (`Tierärzte`, `Veterinarios`, …) |
  | `newVisit` | `New Visit` | FR-19 | **Not derivable.** Both words are in every bundle — `new` stands alone, the noun sits inside `addVisit` and `visitDate` — but the grammar that joins them is in none: German needs `Neuer Besuch`, not `Neu` + `Besuch`. A translator must compose the phrase, not extract it |
  | `visitBooked` | `Your visit has been booked` | FR-20 | **Not derivable.** A full sentence, composed from nothing the bundle already holds |

  **The total translation obligation is forty strings across four keys and ten
  bundles: twenty derivable and twenty not.** The twenty derivable ones are
  `visitTime` and `vet`, each a mechanical step from an entry the translator is
  already looking at. Of the twenty that are not, ten (`visitBooked`) are
  composed from nothing whatever, and ten (`newVisit`) are composed from words
  the bundle holds under grammar it does not.

  **This is the second increase to that obligation in one sitting.** The first
  took it from twenty strings to forty, by adding FR-19's and FR-20's keys. The
  second is this one: the count stayed at forty but the underivable half
  doubled, because FR-19's key went from the extractable word `visit` to the
  composed phrase `newVisit`. An unchanged number is not an unchanged bill.

  `I18nPropertiesSyncTest` will fail for any locale left out of any of the four
  — that failure is the flag for a human, and is never to be silenced by
  copying English (directive 4).

  **This is where deferred objection O6 bites hardest.** O6 says that flag
  becomes a blocked pull request under the harness's *Tests must pass* gate,
  with no sanctioned route out. `visitBooked` is still the key most likely to
  fire it: ten sentence translations that no translator can derive, and that a
  reviewer cannot sanity-check against a neighbouring entry. `newVisit` now
  sits behind it rather than beside the cheap two — a reviewer can check that
  the words are right but not that the grammar is, which is the part that
  actually goes wrong. **O6's exposure has therefore risen twice today**, and
  O6 remains **deferred and unaddressed** after both. The maintainer accepted
  FR-20 knowing it enlarged the exposure, and has now accepted FR-19's re-keying
  knowing it enlarges it again. Implementers should expect `visitBooked` and
  `newVisit` to be the last keys to go green and should raise both with a human
  early rather than at the gate.

  FR-18's relabelling of the form's existing date and description fields adds
  nothing to this count: `visitDate` and `description` already exist and are
  already translated in all ten bundles.
- **Fragments**: extend `fragments/inputField.html` and
  `fragments/selectField.html` rather than adding new fragment files.
- **Decision record**: `decisions/2026-09-19-visit-carries-vet-and-time.md` is
  part of this change (HARNESS.md gate). It must carry, by name:

  1. **Decisions D1–D8** from the spec, including D2's nullability choice *and*
     what that choice forecloses for S4 — AGENTS.md directive 10 names
     nullability as a decision that must be written down, and the foreclosure is
     part of the decision, not a footnote.
  2. **The spec's stance subsection, *"The stance these decisions add up to:
     shape is guaranteed, meaning is not"*** — reproduced as the spec states it,
     and kept **unnumbered**: it is a summary of what D1–D8 add up to, not a
     ninth decision, and it gets no D-number. It is listed here because it is
     the one cross-slice position this spec takes, and the decision record is
     the artefact a future reader of the *code* will find. Without this entry,
     D1–D8 would reach that reader and the sentence explaining them would not.
  3. **D8's user-visible naming** (`Visit Time`, `Veterinarian`) under directive
     10, and with it the two further names settled since: `newVisit` =
     `New Visit` (FR-19 — a whole heading in one key, replacing an earlier
     two-key composition that no locale's grammar could survive) and
     `visitBooked` = `Your visit has been booked` (FR-20, wording carried across
     unchanged from the existing controller literal).
  4. **Directive 9's exception**, which FR-18's reason supplies — a directive-3
     violation closed in the file this change edits, at the cost of two lines
     and no translations. FR-19 and FR-20 are closed on the same boundary and
     under the same exception, so record with them the part FR-18 did not have
     to: FR-19 costs ten translations and FR-20 costs ten more, of a sentence.
  5. **That FR-20 knowingly enlarges deferred objection O6's exposure**, and
     that O6 was left deferred anyway — and that FR-19's re-keying to
     `newVisit` enlarged it a second time in the same sitting, with O6 left
     deferred after that too.

## Module structure

### Schema — the same change in three dialects

Canonical `visits` column order in all three files:
`id, pet_id, vet_id, visit_date, start_time, description`.

| File | Change |
|---|---|
| `src/main/resources/db/h2/schema.sql` | `visits` gains `vet_id INTEGER NOT NULL` and `start_time TIME NOT NULL`; add `ALTER TABLE visits ADD CONSTRAINT fk_visits_vets FOREIGN KEY (vet_id) REFERENCES vets (id);`. The `DROP TABLE` order already drops `visits` before `vets` is recreated — verify the new FK does not break drop order on restart. |
| `src/main/resources/db/mysql/schema.sql` | `vet_id INT(4) UNSIGNED NOT NULL`, `start_time TIME NOT NULL`, plus a `FOREIGN KEY (vet_id) REFERENCES vets(id)` clause in the same style as the existing pet FK. |
| `src/main/resources/db/postgres/schema.sql` | `vet_id INT NOT NULL REFERENCES vets (id)`, `start_time TIME NOT NULL`. Postgres declares no lengths, per the parity rule. |

Type notes: `TIME` is accepted by all three and maps to `java.time.LocalTime`.
No length is declared anywhere, so the parity length comparison is not engaged.
`vet_id` follows exactly the shape of the existing `pet_id` column in each
dialect.

### Seed data — backfill, keep the dates each file already has

Each `data.sql` keeps its own existing dates (h2 uses 2013 dates, mysql and
postgres use 2008–2011 dates). Only the vet and the time are added. Positional
inserts become explicit-column inserts, in the visits block only — the rest of
each file is untouched (no unrelated reformatting).

| Row | pet | description | vet_id | start_time |
|---|---|---|---|---|
| 1 | 7 | rabies shot | 1 (James Carter) | `09:00:00` |
| 2 | 8 | rabies shot | 2 (Helen Leary) | `10:30:00` |
| 3 | 8 | neutered | 3 (Linda Douglas) | `14:00:00` |
| 4 | 7 | spayed | 4 (Rafael Ortega) | `15:30:00` |

- h2: `INSERT INTO visits (pet_id, vet_id, visit_date, start_time, description) VALUES (...)` — the identity column is omitted and defaults.
- mysql: keeps its explicit ids and `INSERT IGNORE`, with a column list added.
- postgres: already uses a column list and a `WHERE NOT EXISTS` guard; extend the list and the select.

### Entity and controller

| File | Change |
|---|---|
| `owner/Visit.java` | Add `@Column(name = "start_time") @DateTimeFormat(pattern = "HH:mm") private LocalTime startTime;` and `@ManyToOne @JoinColumn(name = "vet_id") private Vet vet;` with plain accessors. The no-arg constructor keeps `date = tomorrow` and adds `startTime = LocalTime.of(9, 0)` (FR-4). |
| `owner/Pet.java` | `@OrderBy("date ASC, startTime ASC")` on the visits association (FR-13). |
| `owner/VisitController.java` | Constructor also takes `VetRepository` and (FR-20) a `MessageSource`. New `@ModelAttribute("vets") Collection<Vet> populateVets()` returning `vets.findAll()` for the chooser. In `processNewVisitForm`, alongside the existing date check, reject a null vet and a null start time. **Also (FR-20):** line 110's `addFlashAttribute("message", "Your visit has been booked")` becomes `addFlashAttribute("message", messageSource.getMessage("visitBooked", null, LocaleContextHolder.getLocale()))`. One line, one new key, and the sentence itself is unchanged. |
| `vet/VetFormatter.java` *(new)* | `Formatter<Vet>` mirroring `PetTypeFormatter`: `print` returns the vet's id as a string; `parse` loops `vets.findAll()` and matches on id, throwing `ParseException` for an unknown id. Lives in `vet` because it formats a `Vet`; `@Component`, so it is registered globally exactly as `PetTypeFormatter` is. |

`VetRepository` is **not** changed: `findAll()` already exists and is cached,
and six vets do not justify a new query method. This also keeps the standing
"interface is the implementation" exception from widening.

### Templates and messages

| File | Change |
|---|---|
| `fragments/inputField.html` | Add `<input th:case="'time'" class="form-control" type="time" th:field="*{__${name}__}" />` to the existing `th:switch`. |
| `fragments/selectField.html` | Add a second fragment in the same file, `selectVet (label, name, items)`, whose options carry `th:value="${item.id}"` and `th:text="${item.firstName + ' ' + item.lastName}"`. The existing `select` fragment cannot express a label that differs from its value, which is what a vet chooser needs. |
| `pets/createOrUpdateVisitForm.html` | Add the time input and the vet select to the form; add Time and Vet columns to the previous-visits table. Labels and headings via `#{visitTime}` and `#{vet}`. **Also (FR-18):** the two existing `fragments/inputField` calls on lines 32–33 have their literal labels `'Date'` and `'Description'` replaced with `#{visitDate}` and `#{description}` — two lines, two keys that already exist and are already translated everywhere. `visitDate`, not `date`, so the form's label matches the heading `owners/ownerDetails.html:63` already uses for the same column. **And one line further (FR-18, consistency not compliance):** the previous-visits table heading on line 48 moves from `#{date}` to `#{visitDate}` as well — already-keyed markup, so this closes no violation; it is changed so the column is named identically on both screens in what a user actually sees, at zero translation cost. It is the one part of FR-18 a maintainer can drop without affecting the rest. **Also (FR-19):** the `<h2>` on lines 7–10 becomes a single `th:text="#{newVisit}"` — the literal `Visit`, the `#{new}` block and the `th:if="${visit['new']}"` condition all go, because the whole heading is now one key and the form only ever renders the new case. |
| `owners/ownerDetails.html` | Add Time and Vet columns to each pet's visits table, headings via `#{visitTime}` and `#{vet}`. The existing `#{visitDate}` heading on line 63 is unchanged — FR-18 moves the *form* onto this key, not the other way round. |
| `messages/messages.properties` + 10 bundles | **Four** new keys and no others: `visitTime=Visit Time` and `vet=Veterinarian` (D8, FR-15), `newVisit=New Visit` (FR-19) and `visitBooked=Your visit has been booked` (FR-20), with a real translation of each in every bundle. No `visit` key: it was specified by an earlier revision of FR-19 for the heading alone, and the heading no longer uses it, so it is not added. The existing `new` key is left in place — `pets/createOrUpdatePetForm.html` still uses it — and is not removed from any bundle. No new error key: missing vet and missing start time are rejected with the existing, already-translated `required` code. FR-18 touches these files **not at all** — `visitDate` and `description` are already present in all eleven. See the i18n constraint above for which of the four are derivable and which is not. |

Reusing `required` rather than inventing error keys still keeps error text off
the translation bill entirely; the four keys above are all labels and one
sentence, and there is no fifth.

Passing a `#{...}` expression as the fragment's label argument is the idiom
`owners/createOrUpdateOwnerForm.html` already uses for all five of its fields,
so FR-18 introduces no new construction: `fragments/inputField` prints whatever
string it is handed, and the change is only in what is handed to it.
`pets/createOrUpdatePetForm.html` carries the same literal-label violation and
is **not** edited — it is not a file this change otherwise touches, so it stays
outside the directive-9 exception.

**How the FR-19 heading resolves, and why it is one key.** The `<h2>` becomes a
single keyed element — `<h2 th:text="#{newVisit}">New Visit</h2>` — replacing
four lines of conditional composition with one. The two-key form this plan
previously specified (`#{new}` + a new `#{visit}`) was the smaller diff, but it
inherited a translator's problem that no implementation of it could fix: a
heading assembled from two keys has the grammar of neither. English `New ` +
`Visit` reads correctly; German `Neu` + `Besuch` gives *Neu Besuch* where German
wants *Neuer Besuch*, because the adjective inflects before the noun, and the
same class of problem appears in other bundles. **One key per locale is what
this plan now specifies**: each bundle holds its own whole heading and composes
it under its own grammar. The price is named in the i18n constraint above —
`newVisit` is a composed phrase, not a derivable word — and it is paid here
rather than left as a defect for a translator to report.

**The `visit['new']` conditional on the heading goes with the composition, and
this was verified in the controller before it was specified.** `VisitController`
declares only `initNewVisitForm` (GET) and `processNewVisitForm` (POST), both
mapped to `/owners/{ownerId}/pets/{petId}/visits/new`, and the
`@ModelAttribute("visit") loadPetWithVisit` method that Spring MVC runs before
both does `Visit visit = new Visit(); pet.addVisit(visit); return visit;`. The
model's `visit` is therefore new on every render of this template, `visit['new']`
is always true, and the branch that would have produced a non-new heading is
unreachable. There is no non-new heading to key, and none is invented. Two
things that look like fallout and are not: the `th:if="${!visit['new']}"` on the
previous-visits rows lower down the same template keeps working, because
`th:each` binds first and its `visit` is the row being iterated, which is
exactly how the blank visit `loadPetWithVisit` attaches is kept out of the list;
and the `new` key survives in the bundles because
`pets/createOrUpdatePetForm.html` still renders `#{new}`, and this change does
not edit that file.

T-20 asserts the absence of the English heading and the presence of the
translated one. It still does not assert the heading's grammar — but under one
key there is nothing left for grammar to go wrong *between*, so what it declines
to assert is now the translator's wording rather than a defect the construction
guarantees.

## Algorithm notes

- **Validation order in `processNewVisitForm`.** The existing date check runs
  first and is untouched. Two rejections are added in the same style:
  `result.rejectValue("vet", "required")` when the bound vet is null, and
  `result.rejectValue("startTime", "required")` when the bound start time is
  null. Bean-validation annotations are deliberately not used for these, so the
  error codes match the project's existing, translated `required` key rather
  than introducing `NotNull.*` codes that no bundle carries.
- **How a chosen vet becomes a `Vet`.** The select posts a vet id; Spring's
  conversion service calls `VetFormatter.parse`. An id that matches no vet
  produces a binding error on the `vet` field, which lands the user back on the
  form — the same outcome as choosing nothing, and no extra code path.
- **Why the id, not the name.** `PetTypeFormatter` parses by displayed name
  because pet type names are unique by construction. Two vets can share a name,
  so parsing by name would silently pick one. The id is the only stable handle.
- **Ordering** is delegated to `@OrderBy` on the association rather than sorted
  in a controller or template, which keeps the ordering true for every reader
  of `pet.getVisits()`.
- **Nothing checks availability.** There is no lookup of other visits at POST
  time. FR-17 is satisfied by absence, and AS-13 asserts that absence so it is
  not mistaken for a missing check later.
- **Where the confirmation message is resolved (FR-20).** In the controller, at
  the point the flash attribute is set, against
  `LocaleContextHolder.getLocale()` — **not** in the template. The template that
  renders it, `owners/ownerDetails.html:9-10`, displays whatever `${message}`
  holds, and four other flash messages reach that same span from
  `OwnerController` (lines 85, 160) and `PetController` (135, 177), all of them
  still hardcoded English. Putting a `#{...}` lookup in the template would
  therefore break the other four. Resolving in the controller keeps FR-20 to one
  line in one file. The cost is that the locale is captured at POST time rather
  than at render time, which for a redirect-and-render in a single request is
  the same locale.
- **The other four flash literals are not touched.** `OwnerController` and
  `PetController` carry the same hardcoded-English pattern, and this change
  edits neither file — so by the boundary criterion FR-18's note states, they
  stay outside the exception, exactly as `pets/createOrUpdatePetForm.html`
  does. After this change, one of the application's five flash messages is
  keyed and four are not, which is a smaller inconsistency than the one FR-20
  closes but is a real one, and it belongs in the decision record beside FR-20's
  reason.

## Test cases

Java, JUnit 5, `@WebMvcTest` + MockMvc for anything a user can see, in the
existing test classes wherever one already covers the surface.

`VisitControllerTests` — `@WebMvcTest(value = VisitController.class,
includeFilters = @ComponentScan.Filter(VetFormatter.class))`, with
`@MockitoBean VetRepository vets` stubbed to return a small, obviously
fictional vet list. (The `includeFilters` pattern is already used by
`PetControllerTests` for `PetTypeFormatter`.)

| # | Test |
|---|---|
| T-1 | `initNewVisitFormListsVetsAndOffersATimeField` — GET renders the form, model holds `vets`, rendered content contains a time input and an option for each vet's full name. |
| T-2 | `initNewVisitFormDefaultsToTomorrowAtNine` — GET; rendered date value is tomorrow and time value is `09:00`. |
| T-3 | `processNewVisitFormSucceedsWithVetAndTime` — POST date, `startTime`, `vet` (an id) and description; expects 3xx to `redirect:/owners/{ownerId}`. |
| T-4 | `processNewVisitFormRecordsTheChosenVetAndTime` — POST as T-3; the `Owner` saved through `OwnerRepository` carries a visit whose vet and start time are the submitted ones. |
| T-5 | `processNewVisitFormRejectsMissingVet` — POST without `vet`; 200, form view, field error `vet` with code `required`, nothing saved. |
| T-6 | `processNewVisitFormRejectsMissingStartTime` — POST without `startTime`; 200, form view, field error `startTime` with code `required`, nothing saved. |
| T-7 | `processNewVisitFormRejectsUnknownVet` — POST `vet=999`; 200, form view, field error on `vet`. |
| T-8 | `processNewVisitFormHasErrorsWhenVisitDateIsNotInFuture` — existing test, extended with the new params; still expects `typeMismatch.visitDate`. |
| T-9 | `processNewVisitFormRejectsBlankDescription` — existing error test, extended with the new params; still 200 and form view. |
| T-10 | `previousVisitsShowVetAndTime` — GET for a pet with an existing visit; rendered content contains `14:30` and the vet's full name. |
| T-19 | `visitFormLabelsAllComeFromMessageKeys` — GET the form with `Accept-Language: de`; rendered content contains the German label for the date field (`Besuchsdatum`, the value of `visitDate`) and for the description field (`Beschreibung`), and contains neither `>Date<` nor `>Description<`. `Besuchsdatum` should appear twice — once as the field label and once as the previous-visits table heading — and the rendered page should contain no `>Datum<`, which is what pins the form to the same key as the owner page. Match on `>Datum<` and not on `Datum`: `Geburtsdatum` (the pet table's `birthDate` heading, eight lines up) contains the substring and must not trip the assertion. Covers FR-18 and AS-14. *(Numbered after the existing tests so nothing is renumbered; it belongs to this class.)* |
| T-20 | `visitFormHeadingComesEntirelyFromOneMessageKey` — GET the form with `Accept-Language: de`; extract the rendered `<h2>` and assert it equals the German value of the single `newVisit` key (`Neuer Besuch`) — the **whole** heading, not merely that it contains `Besuch` — and that it contains neither `New` nor `Visit`. Keep the assertion scoped to the `<h2>` and not to the page: `Besuch` also appears in the `addVisit` button (`Besuch hinzufügen`) and inside `Besuchsdatum` (twice, per T-19), so a page-wide contains-check would pass with the heading still in English. Asserting the whole `<h2>` against the bundle value, rather than a substring of it, is what pins the heading to one key: a two-key composition would render `Neu Besuch` and fail. Covers FR-19 and AS-16. |
| T-21 | `confirmationMessageComesFromAMessageKey` — POST a valid visit with `Accept-Language: de`; the flash attribute `message` equals the German value of `visitBooked` and is **not** the English literal `Your visit has been booked`. Covers FR-20 and AS-15. |

`OwnerControllerTests`

| # | Test |
|---|---|
| T-11 | `ownerDetailsShowsVisitTimeAndVet` — GET `/owners/{id}`; rendered content contains the visit's `HH:mm` time and the vet's full name. |
| T-12 | `ownerDetailsOrdersSameDayVisitsByTime` — a pet with 09:00 and 15:00 visits on one date; the 09:00 row appears first in the rendered page. |

`VetFormatterTests` (new, mirroring `PetTypeFormatterTests`)

| # | Test |
|---|---|
| T-13 | `parsesAVetById` — `parse("2")` returns the vet with id 2. |
| T-14 | `rejectsAnIdThatIsNotAVet` — `parse("999")` throws `ParseException`. |

`ClinicServiceTests` (integration, against the seeded H2 database)

| # | Test |
|---|---|
| T-15 | `shouldFindVisitsByPetId` — existing test, extended: pet 7 still has two visits and each has a vet and a start time. |
| T-16 | `shouldAddNewVisitForPet` — existing test, extended: the new visit is given a vet and a start time and persists with both. |

Harness tests — no new test files, but both must pass unchanged:

| # | Test |
|---|---|
| T-17 | `SchemaParityTest` — `visits` declares `vet_id` and `start_time` in h2, mysql and postgres. |
| T-18 | `I18nPropertiesSyncTest` — all four new keys (`visitTime`, `vet`, `newVisit`, `visitBooked`) present in every bundle, with real translations of `Visit Time`, `Veterinarian`, `New Visit` and `Your visit has been booked`. A failure here means a locale is missing a trustworthy translation and must be raised with a human, not patched with English. FR-18 cannot affect this test: it adds no key. FR-19 and FR-20 each can, and `visitBooked` is the likeliest to hold the change up — see the i18n constraint and O6. |

## FR mapping

| FR | Covered by |
|---|---|
| FR-1 | T-4, T-5, T-7, T-16 |
| FR-2 | T-4, T-6, T-16 |
| FR-3 | T-1 |
| FR-4 | T-2 |
| FR-5 | T-5, T-7 |
| FR-6 | T-6 |
| FR-7 | T-8, T-9 |
| FR-8 | T-3, T-4, T-21 |
| FR-9 | T-11 |
| FR-10 | T-10 |
| FR-11 | T-17, plus T-15/T-16 booting against the real H2 schema |
| FR-12 | T-15, T-17 |
| FR-13 | T-12 |
| FR-14 | T-10, T-11 |
| FR-15 | T-18 (all four keys present and translated); T-19, T-20, T-21 (each new string reaching a rendered page from a key) |
| FR-16 | No new test; enforced by review — no owner field is added to any template or message in this change |
| FR-17 | Asserted by absence; AS-13 is left untested at controller level because there is no code to test. If the maintainer wants it pinned, add an integration test creating two visits with the same vet and time and asserting both persist. |
| FR-18 | T-19 |
| FR-19 | T-20, T-18 |
| FR-20 | T-21, T-18 |

## Risks and things to check while implementing

- **H2 drop order.** `schema.sql` drops `vets` before `visits`. Adding
  `fk_visits_vets` may make that order fail on a second boot; if so, move the
  `DROP TABLE visits` above `DROP TABLE vets`. This is the one place the change
  can break something unrelated to visits.
- **`@WebMvcTest` and the formatter.** A `Formatter` is not in the web slice by
  default; T-1..T-10 need the `includeFilters` clause, or every vet parameter
  binds to null and the failures look like validation failures.
- **Existing POSTs in tests.** Every existing test that posts a visit will fail
  until it supplies `vet` and `startTime`. That is expected and is the point of
  D2; updating them is part of this change, not a separate cleanup.
- **T-19 and T-20 need the message source in the web slice.** `@WebMvcTest`
  auto-configures `MessageSource`, so `Accept-Language: de` should resolve
  `visitDate`, `description` and `newVisit` from `messages_de.properties`. If it
  does not in this project's setup, assert instead that the rendered form
  contains no label text that is absent from the bundles — but do not weaken
  T-19 or T-20 into a test of the template's source text, which is structure
  rather than observable behaviour.
- **T-21 needs the message source in the controller, not just the view.** FR-20
  resolves the sentence in `VisitController`, so `@WebMvcTest` must supply a
  `MessageSource` bean to the controller's constructor as well as to the view
  layer. If the slice does not inject one, the controller will fail to
  construct and every test in the class fails at startup rather than at the
  assertion — a failure that looks nothing like the thing it is.
- **`visitBooked` will be the last key to go green.** Ten sentence
  translations, derivable from nothing (see the i18n constraint). Under the
  harness's *Tests must pass* gate this blocks the merge, which is deferred
  objection O6 firing exactly as O6 predicted. Raise the translations with a
  human at the **start** of implementation, not when `I18nPropertiesSyncTest`
  goes red. Do not, under any circumstances, satisfy the sync test by copying
  the English sentence into a bundle — directive 4 forbids it and the whole
  point of FR-20's cost being written down is that this shortcut is the
  tempting one.
- **`newVisit` must be translated as a whole heading, not as two words.** This
  key exists precisely because `#{new}` + `#{visit}` gave *Neu Besuch* in German
  where *Neuer Besuch* is correct. A translator handed `New Visit` alongside an
  already-translated `new=Neu` will be tempted to reuse it; the value wanted is
  the inflected phrase the locale actually uses for this heading. Say so when
  the translations are requested. T-20 catches the two-word form in German —
  it asserts the whole `<h2>` equals the bundle value — but only in German, so
  the other nine depend on the request being clear.
- **Do not delete the `new` key while removing `#{new}` from this template.**
  `pets/createOrUpdatePetForm.html` still renders it, and that file is outside
  this change. Removing the key would break a page this change never touched,
  and `I18nPropertiesSyncTest` would not necessarily catch it — the key would
  be absent consistently across all eleven files.
- **Do not remove the other `visit['new']` condition.** The heading's condition
  goes; the one on the previous-visits rows stays. They look identical and are
  not: `th:each` binds before `th:if`, so the row condition tests the visit
  being iterated, and it is what keeps the blank visit `loadPetWithVisit`
  attaches out of the previous-visits list. Removing it puts an empty row on
  the form and would show up as a failure in T-10, not in T-20.
- **`start_time` as a reserved word.** It is not reserved in any of the three
  dialects, but `TIME` as a bare column name would be — hence `start_time`,
  which also leaves room for an end time later without renaming.
