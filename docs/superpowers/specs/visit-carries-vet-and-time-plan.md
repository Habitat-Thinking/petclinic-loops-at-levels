---
slice: S2
spec: docs/superpowers/specs/visit-carries-vet-and-time.md
title: "Plan — visit carries a vet and a time of day"
date: 2026-09-19
revised: 2026-09-19 — choice story #6 accepted (D8 fixes the two new keys' English words; FR-18 keys the form's two existing labels)
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
  `pets/createOrUpdateVisitForm.html` for the existing `date` and `description`
  keys, closing a directive-3 violation in the file this change already edits.
  It is a stated exception to directive 9 and adds **no** new keys and **no**
  translations. It adds one test, T-19, and one requirement row to the mapping.

Nothing else in this plan changed. No test was renumbered.

## Constraints this plan is shaped by

- **Layering** stays controller → repository → entity. No service layer. The
  visit change lives in the `owner` package; the one new class that belongs to
  the vet concept lives in the `vet` package.
- **Schema parity**: all three `schema.sql` and all three `data.sql` change in
  the same commit; `SchemaParityTest` compares column presence across h2, mysql
  and postgres, and length limits where h2 and mysql both declare one.
- **Message keys, real translations**: two new keys and no more —
  `visitTime` = `Visit Time` and `vet` = `Veterinarian` (spec D8) — added to
  `messages.properties` and to the ten locale bundles with genuine
  translations. That is still twenty translations across ten bundles, but each
  one mirrors a phrase the bundle already carries: `visitTime` parallels the
  translated `visitDate` (`Besuchsdatum` → `Besuchszeit`, `Fecha de visita` →
  `Hora de visita`), and `vet` is the singular of the translated `vets`
  (`Tierärzte`, `Veterinarios`, …). `I18nPropertiesSyncTest` will fail for any
  locale left out — that failure is the flag for a human, and is never to be
  silenced by copying English. FR-18's relabelling of the form's existing date
  and description fields adds nothing to this count: `date` and `description`
  already exist and are already translated in all ten bundles.
- **Fragments**: extend `fragments/inputField.html` and
  `fragments/selectField.html` rather than adding new fragment files.
- **Decision record**: `decisions/2026-09-19-visit-carries-vet-and-time.md` is
  part of this change (HARNESS.md gate). It restates decisions D1–D8 from the
  spec, including D2's nullability choice *and* what that choice forecloses for
  S4 — AGENTS.md directive 10 names nullability as a decision that must be
  written down, and the foreclosure is part of the decision, not a footnote.
  Directive 10 also covers D8's user-visible naming (`Visit Time`,
  `Veterinarian`), and directive 9's exception route requires FR-18's reason —
  a directive-3 violation closed in the file this change edits, at the cost of
  two lines and no translations — to be recorded there too.

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
| `owner/VisitController.java` | Constructor also takes `VetRepository`. New `@ModelAttribute("vets") Collection<Vet> populateVets()` returning `vets.findAll()` for the chooser. In `processNewVisitForm`, alongside the existing date check, reject a null vet and a null start time. |
| `vet/VetFormatter.java` *(new)* | `Formatter<Vet>` mirroring `PetTypeFormatter`: `print` returns the vet's id as a string; `parse` loops `vets.findAll()` and matches on id, throwing `ParseException` for an unknown id. Lives in `vet` because it formats a `Vet`; `@Component`, so it is registered globally exactly as `PetTypeFormatter` is. |

`VetRepository` is **not** changed: `findAll()` already exists and is cached,
and six vets do not justify a new query method. This also keeps the standing
"interface is the implementation" exception from widening.

### Templates and messages

| File | Change |
|---|---|
| `fragments/inputField.html` | Add `<input th:case="'time'" class="form-control" type="time" th:field="*{__${name}__}" />` to the existing `th:switch`. |
| `fragments/selectField.html` | Add a second fragment in the same file, `selectVet (label, name, items)`, whose options carry `th:value="${item.id}"` and `th:text="${item.firstName + ' ' + item.lastName}"`. The existing `select` fragment cannot express a label that differs from its value, which is what a vet chooser needs. |
| `pets/createOrUpdateVisitForm.html` | Add the time input and the vet select to the form; add Time and Vet columns to the previous-visits table. Labels and headings via `#{visitTime}` and `#{vet}`. **Also (FR-18):** the two existing `fragments/inputField` calls on lines 32–33 have their literal labels `'Date'` and `'Description'` replaced with `#{date}` and `#{description}` — two lines, two keys that already exist and are already translated everywhere. |
| `owners/ownerDetails.html` | Add Time and Vet columns to each pet's visits table, headings via `#{visitTime}` and `#{vet}`. |
| `messages/messages.properties` + 10 bundles | Two new keys and no others: `visitTime=Visit Time` and `vet=Veterinarian` in `messages.properties` (D8), with a real translation of each in every bundle. No new error key: missing vet and missing start time are rejected with the existing, already-translated `required` code. FR-18 touches this file **not at all** — `date` and `description` are already present in all eleven files. |

Reusing `required` rather than inventing error keys keeps the i18n surface of
this change to two labels — the smallest translation obligation that satisfies
FR-15.

Passing a `#{...}` expression as the fragment's label argument is the idiom
`owners/createOrUpdateOwnerForm.html` already uses for all five of its fields,
so FR-18 introduces no new construction: `fragments/inputField` prints whatever
string it is handed, and the change is only in what is handed to it.
`pets/createOrUpdatePetForm.html` carries the same literal-label violation and
is **not** edited — it is not a file this change otherwise touches, so it stays
outside the directive-9 exception.

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
| T-19 | `visitFormLabelsAllComeFromMessageKeys` — GET the form with `Accept-Language: de`; rendered content contains the German label for the date field (`Datum`) and for the description field (`Beschreibung`), and contains neither `>Date<` nor `>Description<`. Covers FR-18 and AS-14. *(Numbered after the existing tests so nothing is renumbered; it belongs to this class.)* |

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
| T-18 | `I18nPropertiesSyncTest` — `visitTime` and `vet` present in every bundle, with real translations of `Visit Time` and `Veterinarian`. A failure here means a locale is missing a trustworthy translation and must be raised with a human, not patched with English. FR-18 cannot affect this test: it adds no key. |

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
| FR-8 | T-3, T-4 |
| FR-9 | T-11 |
| FR-10 | T-10 |
| FR-11 | T-17, plus T-15/T-16 booting against the real H2 schema |
| FR-12 | T-15, T-17 |
| FR-13 | T-12 |
| FR-14 | T-10, T-11 |
| FR-15 | T-18 |
| FR-16 | No new test; enforced by review — no owner field is added to any template or message in this change |
| FR-17 | Asserted by absence; AS-13 is left untested at controller level because there is no code to test. If the maintainer wants it pinned, add an integration test creating two visits with the same vet and time and asserting both persist. |
| FR-18 | T-19 |

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
- **T-19 needs the message source in the web slice.** `@WebMvcTest`
  auto-configures `MessageSource`, so `Accept-Language: de` should resolve
  `date` and `description` from `messages_de.properties`. If it does not in this
  project's setup, assert instead that the rendered form contains no label text
  that is absent from the bundles — but do not weaken T-19 into a test of the
  template's source text, which is structure rather than observable behaviour.
- **`start_time` as a reserved word.** It is not reserved in any of the three
  dialects, but `TIME` as a bare column name would be — hence `start_time`,
  which also leaves room for an end time later without renaming.
