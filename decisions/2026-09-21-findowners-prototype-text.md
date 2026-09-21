# The find-owners last-name label's prototype text matches its message

Implements [`docs/superpowers/specs/findowners-prototype-text.md`](../docs/superpowers/specs/findowners-prototype-text.md)
and its [plan](../docs/superpowers/specs/findowners-prototype-text-plan.md).

The change is twelve characters in
`src/main/resources/templates/owners/findOwners.html` line 12: the prototype text
`Last name ` becomes `Last Name`, which is the value of `lastName` in
`messages.properties`. The `th:text="#{lastName}"` attribute is untouched, so
nothing the application renders changes — only what the file shows when opened
directly, under Thymeleaf's natural-templating property.

## The decision the change embodies

- **Decision**: the prototype text takes the bundle value, byte-for-byte.
  **Alternatives**: change the bundle value instead; adopt render-equivalence
  rather than string-equality, under which the trailing space needs no fix.
  **Why**: `lastName` is defined in ten bundles — a real translation in each of the
  nine non-English locales, and `Last Name` in `messages.properties`, which is what
  English resolves to because `messages_en.properties` is empty. That value is what
  every English user sees; moving it would be a user-visible i18n change under
  directive 4, not a prototype fix. The prototype is the side that is inert.

  (The choice-story record says the value "appears in eleven bundles". Checked
  while writing this: it appears in one, and the key in ten.)

## The judgement call this change made (directive 10)

There was one, and it is a scope call: `fragments/layout.html:48` carries the same
drift (`Find owners` against `findOwners=Find Owners`) and has been left alone, on
directive 9 ("no unrelated reformatting") and the "smallest diff that works"
preference. That is a judgement rather than a directive applying itself, because
`AGENTS.md` lists this exact boundary under *Not encodable yet* — "'Needed by the
change's purpose' … needs worked examples to settle where cleanup ends and noise
begins" — so the directive supporting the call is one the project has flagged as
unsettled and wanting worked examples. This is one of them.

This record does not say there were no judgement calls. An earlier draft of the
plan would have; objection O3 in
[`docs/superpowers/objections/findowners-prototype-text.md`](../docs/superpowers/objections/findowners-prototype-text.md)
was accepted specifically to stop it.

## The defect class is larger than the spec claims

Recorded here as fact, verified in this working copy, because no other artefact
that ships with the code carries it. The spec calls `fragments/layout.html:48`
"the one other instance of the same drift". That count comes from the slicing
record and uses a narrower definition — *case-mismatched* — than FR-1's
byte-for-byte standard. Under FR-1's own standard there are at least five
mismatches in the template set, not two:

- `owners/findOwners.html:12` — fixed by this change.
- `fragments/layout.html:48` — `Find owners` against `findOwners=Find Owners`.
- `owners/ownersList.html:32` — `Pages:` against `pages=pages`.
- `vets/vetList.html:27` — `Pages:` against `pages=pages`.
- `owners/ownerDetails.html:36-39` — the prototype bodies `Edit\n    Owner` against
  `editOwner=Edit Owner`, and `Add\n    New Pet` against `addNewPet=Add New Pet`.

The wrapped pair is the sharp edge: satisfying byte-equality there would mean
unwrapping source lines that already render correctly, which is precisely the
unrelated reformatting directive 9 forbids. The byte-for-byte standard and
directive 9 therefore conflict in that file. That conflict is **unresolved and
deferred** — it belongs to the general prototype-text audit the spec puts out of
scope, along with the `pages=pages` case, where the bundle value rather than the
prototype is the more likely defect. No issue and no TODO tracks it; this record
is where it is written down.

## i18n is not engaged

No message key is added, renamed or removed, and no bundle file changes, so
`I18nPropertiesSyncTest` and directive 4 are not in play. Recorded because the
slicing record closed this question and a later reader of the code should find
the answer without it.

## Not done, deliberately

No test was added. The plan raised T-1 — a test asserting the contents of a source
file — as an open question against `AGENTS.md` should-follow 2 ("tests check
behaviour a user can observe"), and the maintainer approved the plan without it.
FR-1 is covered by review. The existing `OwnerControllerTests` cases that render
`owners/findOwners` pass unchanged, and objection O2 (deferred) records accurately
that they assert status, model and view name, not rendered content — so they are
not the coverage the plan claimed for AS-2.
