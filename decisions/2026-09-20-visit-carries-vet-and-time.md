# A visit carries a vet and a time of day (slice S2)

Implements [`docs/superpowers/specs/visit-carries-vet-and-time.md`](../docs/superpowers/specs/visit-carries-vet-and-time.md)
and its [plan](../docs/superpowers/specs/visit-carries-vet-and-time-plan.md). The spec
and plan settled most of what is below; this record is the copy a future reader of the
*code* will find, so the decisions are restated here rather than only cited.

The plan named this file `2026-09-19-...`. It is dated the 20th because that is the day
the change landed.

## The decisions the spec took (D1–D8)

- **D1 — a date and a separate start time, both on the visit.**
  **Alternatives**: a single `LocalDateTime` start; a foreign key to an S1 slot.
  **Why**: the slot table does not exist, so the foreign key would make this slice
  undeliverable on its own. Between the other two, a separate `start_time` column is
  additive: `visit_date` keeps its `DATE` type in all three dialects, so the
  "must be in the future" rule, the `yyyy-MM-dd` format and every existing assertion
  about a visit's date survive untouched. The cost, stated rather than hidden: two
  columns can in principle hold a date with no time, which is why the start time is
  `NOT NULL` in the schema and required on the form.

- **D2 — a vet is required on every visit.** `vet_id` is `NOT NULL` with a foreign key
  to `vets`, and no path in the application creates a visit without one.
  **Alternatives**: nullable now, required when S4 decides.
  **Why**: the alternative leaves two kinds of visit in the system indefinitely, and
  every screen renders the empty case forever. A clinic visit without a vet is not a
  thing the product means to represent.

- **D2, what it forecloses, and at whose expense.** The slice record contradicts
  itself: S2's scope claims the required-versus-optional question, and S4's
  `decision_focus` claims the same question. **This change resolves it in S2's favour** —
  the vet is required, and it is required now. That is half of S4's stated question,
  decided here. If S4 later wants a free-date form that creates visits *without* a vet,
  that choice is no longer free: it costs dropping a `NOT NULL` and a foreign key in
  `db/h2`, `db/mysql` and `db/postgres` together, in one change, under the
  schema-parity rule. S4 is not blocked; it is made more expensive.
  The asymmetry, named rather than buried: D1 rejected a single timestamp partly
  because it would mean a three-dialect migration of an existing column, and D2 accepts
  a three-dialect migration as the acceptable price of reversal. The same cost is
  treated as prohibitive for one decision and acceptable for the other, in the same
  change. The maintainer adjudicated that knowingly (objection O2).

- **D3 — the four existing seeded visits are backfilled, not deleted.** Each `data.sql`
  keeps its four rows with the same pet, date and description, each given a vet and a
  start time, and the positional `INSERT INTO visits VALUES (...)` statements are
  rewritten with explicit column lists.
  **Alternatives**: delete the rows rather than invent two fields of sample data.
  **Why**: those rows are the visible content of the canonical demo screen, and the
  whole seed set is invented sample data already. Explicit column lists mean the next
  column change cannot silently shift a value into the wrong column.

- **D4 — any vet, any time; no availability is consulted.** Nothing checks whether the
  vet is free, and two visits may name the same vet at the same time. Availability (S1)
  and contention (S5) are separate decisions and are not pre-empted here.

- **D5 — display formats.** Time is `HH:mm`, 24-hour, identical in every locale; the
  date keeps `yyyy-MM-dd`.
  **Why**: it matches the fixed, locale-independent date format both screens already
  use. Locale-sensitive time formatting is a wider, separate change.

- **D6 — visits list date first, then start time.** Two visits on one day read in the
  order they happen.

- **D7 — a new visit is offered tomorrow at 09:00.** The form already pre-filled
  tomorrow's date; it now pre-fills a start time of 09:00 as well.
  **Why**: a pre-filled control a user can change is plainer than an empty required
  field. (*Where* that pre-fill is applied is an implementation call and moved off the
  plan's answer — see the judgement calls below.)

- **D8 — the user-visible names, under directive 10.** `visitTime` = **Visit Time**,
  `vet` = **Veterinarian**.
  **Why those words**: each mirrors a phrase every bundle already carries, so a
  translator derives rather than invents. `Visit Time` is an exact parallel of the
  existing `visitDate` = `Visit Date`; `Veterinarian` is the singular of the existing
  `vets` = `Veterinarians`. Neither introduces a new concept name to the product's
  vocabulary.

- **Two further names settled after D8, by the same directive.** `newVisit` =
  **New Visit** (FR-19), one key holding the whole form heading, replacing an earlier
  `#{new}` + `#{visit}` composition; and `visitBooked` = **Your visit has been booked**
  (FR-20), the existing controller sentence carried across word for word.
  **`newVisit` does not meet D8's criterion, and this is recorded rather than glossed.**
  D8's test is whether a translator can derive the string from one the bundle already
  holds. Both of `New Visit`'s words are in every bundle — `new` stands alone, the noun
  sits inside `addVisit` and `visitDate` — but the grammar that joins them is in none:
  German wants *Neuer Besuch*, not *Neu* + *Besuch*, and no two-key composition can
  produce the inflection. That is precisely why the heading is one key. D8 is unchanged
  and gains no clause; this is where FR-19 no longer meets it.

## The stance these decisions add up to: shape is guaranteed, meaning is not

Every constraint this change buys is about a field being **present** — a vet and a start
time on every visit, with a foreign key to `vets`, in all three dialects. None is about
the recorded value being **true**: nothing makes a vet-and-time pair unique, binds the
date to the time, bounds how far ahead a visit may be, or says anything about working
hours or duration.

That is deliberate. This change guarantees the *shape* of a booking and guarantees
nothing about its *meaning*. The slices that add meaning — S1 (availability) and S5
(contention) — inherit that as a stated position rather than as an accident of what was
cheap to land here, and S4 inherits it too.

This section deliberately carries **no D-number**. It is a summary of what D1–D8 add up
to, not a ninth decision. It is written here because this file is the artefact a future
reader of the code will find, and without it D1–D8 would reach that reader and the
sentence explaining them would not.

## Directive 9's exception, taken once and used three times

AGENTS.md directive 9 forbids unrelated reformatting. Three fixes in this change are
exceptions to it, taken on one stated ground: each closes a **directive 3** violation
(hardcoded display text) inside a file this change was already editing, not one it went
looking for.

- **FR-18** — `pets/createOrUpdateVisitForm.html` passed the literals `'Date'` and
  `'Description'` as its two existing field labels. They become `#{visitDate}` and
  `#{description}`. Cost: two lines, **no new key and no translation** — both keys
  already exist and are already translated in all ten bundles. `visitDate` rather than
  `date` because that is the key `owners/ownerDetails.html` already uses to head the
  same column, so both screens now name "the day a visit happens" identically in every
  locale instead of reading *Datum* against *Besuchsdatum*.
  One line of FR-18 closes no violation and is a consistency change to already-compliant
  markup: the form's previous-visits table heading moves from `#{date}` to `#{visitDate}`
  for the same reason. It is separable and is named separately so it could be declined.
- **FR-19** — the form's `<h2>` was half-keyed (`#{new}` plus the template text
  `Visit`), so German read *Neu Visit*. It becomes one `th:text="#{newVisit}"`.
  **This one is not free**: one new key and **ten translations**.
- **FR-20** — `VisitController` set the flash message to a hardcoded English sentence.
  It now resolves `visitBooked` against `LocaleContextHolder.getLocale()`.
  **Also not free**: one new key and **ten more translations, of a full sentence**.

What the exception deliberately does **not** reach: `pets/createOrUpdatePetForm.html`
carries the same literal-label violation, and `OwnerController` and `PetController`
carry the same hardcoded flash-message pattern in four more places. This change edits
none of those files, so they stay outside the boundary. After this change one of the
application's five flash messages is keyed and four are not. That is a real
inconsistency, smaller than the one FR-20 closes, and it is recorded here rather than
left to be discovered.

## The `visit['new']` conditional on the heading, removed knowingly

`VisitController` declares two mappings, a GET and a POST, both on
`/owners/{ownerId}/pets/{petId}/visits/new`, and the `@ModelAttribute("visit")` method
that runs before both constructs a fresh `Visit` every time. `visit['new']` was
therefore always true and the non-new heading was unreachable, so the condition goes
with the composition. No non-new heading is invented; a slice that edits an existing
visit pays for its own heading then.

Two neighbours checked and deliberately left alone: the `th:if="${!visit['new']}"` on
the previous-visits **rows** further down the same template stays — `th:each` binds
first, so its `visit` is the row being iterated, and it is what keeps the blank visit
out of the list — and the `new` key stays in all eleven bundles, because
`pets/createOrUpdatePetForm.html` still renders it.

## FR-20 and FR-19 enlarge deferred objection O6, twice, and O6 stays deferred

O6 objects that FR-15's "flag the missing locale for a human" becomes, under the
harness's *Tests must pass* gate, a blocked pull request, with no sanctioned route out.
This change adds four keys and forty translations. **Twenty are derivable and twenty are
not**: `visitTime` and `vet` are a mechanical step from an entry the translator is
already looking at; `newVisit`'s ten headings are composed from words every bundle holds
under grammar none of them supplies; and `visitBooked`'s ten sentences are composed from
nothing whatever.

The obligation rose twice in one sitting — first from twenty strings to forty when FR-19
and FR-20 were added, then again when FR-19 was re-keyed from the extractable word
`visit` to the composed phrase `newVisit`, which doubled the underivable half without
moving the count. The maintainer accepted both increases knowing each enlarged O6's
exposure, and **O6 was left deferred after both, with no stated trigger**. Recorded here
so that whoever meets a blocked merge over `visitBooked` or `newVisit` can see it was
foreseen.

## Judgement calls made while implementing, which the spec and plan did not state

- **Decision**: reorder the H2 `DROP TABLE` statements so `visits` is dropped before
  `vets`.
  **Alternatives**: leave the order and drop the foreign key; use `DROP ... CASCADE`.
  **Why**: `fk_visits_vets` makes the old order illegal — H2 refuses to drop a table a
  live foreign key references — so the application would fail on its second boot against
  a persistent H2 file. Moving one line is the smallest fix and changes nothing else.
  This was the plan's named risk and it was real.

- **Decision**: reject a missing vet and a missing start time in the controller with
  `result.rejectValue(..., "required")`, not with `@NotNull` on the entity.
  **Alternatives**: bean validation annotations.
  **Why**: `required` is an error code every one of the eleven bundles already
  translates. `@NotNull` would produce `NotNull.visit.vet` codes that no bundle carries,
  adding error text to a translation bill this change is already straining.

- **Decision**: the missing-vet rejection is skipped when the binder has already
  recorded a field error on `vet`.
  **Alternatives**: reject unconditionally.
  **Why**: an unknown vet id fails in `VetFormatter.parse`, which leaves a
  `typeMismatch` error and a null vet. Rejecting again would stack a second, misleading
  "is required" on a field the user did fill in. The user-visible outcome of an unknown
  id is unchanged — back to the form with an error on the vet field.

- **Decision**: `VetFormatter` parses by **id**, not by displayed name.
  **Alternatives**: mirror `PetTypeFormatter` exactly and parse the name.
  **Why**: pet type names are unique by construction; two vets can share a name, so
  parsing by name would silently pick one of them. The id is the only stable handle a
  form control can carry. `print` returns the id for the same reason, and returns the
  empty string for a vet with no id rather than `PetTypeFormatter`'s `<null>` literal,
  because this value is a select option's `value` attribute rather than display text.

- **Decision**: the vet chooser is a new fragment file, `fragments/selectVetField.html`,
  and **not** a second fragment inside `fragments/selectField.html` as the plan
  specified.
  **Alternatives**: the plan's second fragment in the same file; generalising the
  existing `select` fragment with value and label expressions.
  **Why**: the plan's version was written, and it broke the *pet* form. A Thymeleaf
  fragment expression like `~{fragments/selectField :: select (...)}` is a markup
  selector: it matches the `th:fragment` of that name **and every `<select>` element in
  the file**. One `<select>` lived inside the `select` fragment and was harmless; a
  second one, in a sibling fragment, became a second match, so
  `createOrUpdatePetForm.html` rendered the vet chooser's `<option>` against its pet
  types and eleven `PetControllerTests` failed with
  `Exception evaluating SpringEL expression: "item.firstName + ' ' + item.lastName"`.
  The only fixes that keep both fragments in one file change a call site in
  `createOrUpdatePetForm.html`, which is outside this change's directive-9 boundary.
  Generalising the existing fragment changes it for the pet form's benefit too, which is
  none. So this is a deliberate exception to the "extend an existing fragment file"
  preference, on the directive's own escape hatch: a vet chooser — an option whose label
  differs from the value it posts — is a genuinely new kind of field. The reason is
  written into the new file as well, because the next person to add a second `<select>`
  to a fragment file will hit the same thing.

- **Decision**: the 09:00 pre-fill is set in `VisitController.initNewVisitForm`, not in
  the `Visit` constructor as the plan specified.
  **Alternatives**: the plan's constructor default; `WebDataBinder.setRequiredFields`.
  **Why**: the plan's version was written, and FR-4 and FR-6 cannot both hold under it.
  A constructor default is indistinguishable from a submitted value — a POST that omits
  `startTime` leaves 09:00 in place, so the visit is accepted and FR-6's rejection never
  fires. Applying the default where the form is rendered keeps FR-4 (the GET shows
  09:00) and lets FR-6 mean what it says. The visible asymmetry this leaves — the date
  default in the constructor, the time default in the controller — is left alone
  deliberately: the date's behaviour is pre-existing and no requirement in this slice
  reaches it, and directive 9 says not to change what the change does not need.

- **Decision**: the form's field order is date, start time, vet, description.
  **Alternatives**: append the two new fields after description, the smaller diff.
  **Why**: date and time belong beside each other, and the description reads as the
  last thing a user writes. No requirement fixes the order.

- **Decision**: the vet chooser has **no blank or placeholder option**.
  **Alternatives**: add one, so a user can decline to choose.
  **Why**: this is deferred objection **O5**, which says exactly that a browser will
  preselect the first vet and make AS-5's precondition unreachable through the UI. O5
  was adjudicated deferred and the plan says to leave it. It is left, and the missing
  option is a known hole, not an oversight: the rejection path is real and tested, but
  only a request that omits the parameter can reach it.

- **Decision**: `@OrderBy("date ASC, startTime ASC")` is added to `Pet.visits` although
  no test can fail without it.
  **Alternatives**: leave the association ordered by date alone.
  **Why**: FR-13 requires it. The only test of the ordering (T-12) runs under
  `@WebMvcTest` with a mocked repository, where `@OrderBy` never executes and the
  assertion is really about the fixture's insertion order — which is deferred objection
  **O8**, firing exactly as O8 predicted. The annotation is added because the
  requirement says so, and the fact that nothing checks it is written down here.

- **Decision**: the two-cell action row inside the owner page's per-pet visits table is
  left at two cells while the header and data rows grow to four.
  **Alternatives**: pad it to four cells, or give it a `colspan`.
  **Why**: this is deferred objection **O11** and the plan does not resolve it. The row
  renders; the table is ragged. Changing it is a presentation decision nobody has taken.

- **Decision**: seeded visits get vets 1–4 and times 09:00, 10:30, 14:00, 15:30, as the
  plan's table specifies, and each `data.sql` keeps its own existing dates (h2's 2013
  dates, mysql's and postgres's 2008–2011 dates).
  **Why**: the plan fixed the values; keeping each file's dates keeps the diff to the
  two new columns.

## Translations — what was written, and what a human should still check

All four keys are translated in all nine non-English bundles; `messages_en.properties`
is intentionally empty and falls back to `messages.properties`. **No English text was
copied into any locale bundle** (directive 4), and `I18nPropertiesSyncTest` is green
because the translations are real, not because the gap was papered over.

| Key | de | es | fa | hi | ja | ko | pt | ru | tr |
|---|---|---|---|---|---|---|---|---|---|
| `visitTime` | Besuchszeit | Hora de visita | ساعت ویزیت | यात्रा का समय | 診察時間 | 방문 시간 | Hora da visita | Время визита | Ziyaret Saati |
| `vet` | Tierarzt | Veterinario | دامپزشک | पशु चिकित्सक | 獣医師 | 수의사 | Veterinário | Ветеринар | Veteriner |
| `newVisit` | Neuer Besuch | Nueva visita | ویزیت جدید | नई यात्रा | 新規診察 | 새 방문 | Nova visita | Новый визит | Yeni Ziyaret |
| `visitBooked` | Ihr Besuch wurde gebucht | Su visita ha sido reservada | ویزیت شما رزرو شد | आपकी यात्रा बुक कर दी गई है | 診察のご予約が完了しました | 방문이 예약되었습니다 | Sua visita foi agendada | Ваш визит забронирован | Ziyaretiniz rezerve edildi |

Each locale's `newVisit` is the inflected whole heading, not two words stuck together —
*Neuer Besuch*, *Nueva visita*, *Новый визит* — which is the defect the single key
exists to close, and which T-20 pins in German only.

**What a native speaker should still confirm.** These are translations, not copies, and
none is a guess at meaning. What a review would add is register rather than correctness,
and it is worth asking for on the four `visitBooked` sentences in particular:

- **ru** — `Ваш визит забронирован` is literal and correct; a Russian clinic might more
  naturally say *Вы записаны на приём*.
- **tr** — `Ziyaretiniz rezerve edildi` is correct; *Randevunuz oluşturuldu* is the more
  idiomatic clinic phrasing.
- **ja** — `診察のご予約が完了しました` follows the bundle's use of 診察 for a visit and
  is natural, but it is a polite-form sentence where the English is plain.
- **hi** — `आपकी यात्रा बुक कर दी गई है` uses the bundle's own यात्रा for "visit", which
  reads oddly for a clinic appointment; the whole bundle has that quirk and this change
  does not fix it.

The spec's own words apply: a failing sync test reported honestly would have been the
correct outcome. It was not needed — but these four are where a human should look first.
