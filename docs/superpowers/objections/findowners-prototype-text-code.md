---
spec: docs/superpowers/specs/findowners-prototype-text.md
decision_record: decisions/2026-09-21-findowners-prototype-text.md
commit: 887f030
date: 2026-09-21
mode: code
diaboli_model: claude-opus-5
objections:
  - id: O1
    category: implementation
    severity: medium
    claim: "The decision record declares itself the durable written-down account of the prototype-text defect class, but its enumeration is drawn from a population of `th:text=\"#{...}\"` sites only. Four further sites carry prototype text that stands in for a message through a fragment parameter or a th:with, and are invisible to that sweep — including three where FR-1's byte-for-byte standard has no definable answer, because the fragment is parameterised and the prototype stands in for eight different messages depending on the call site."
    evidence: "Record: 'Under FR-1's own standard there are at least five mismatches in the template set, not two' and 'Recorded here as fact, verified in this working copy, because no other artefact that ships with the code carries it ... No issue and no TODO tracks it; this record is where it is written down.' Omitted sites: `src/main/resources/templates/fragments/inputField.html:10` `<label th:for=\"${name}\" class=\"col-sm-2 control-label\" th:text=\"${label}\">Label</label>`; `fragments/selectField.html:10` and `fragments/selectVetField.html:16`, identically; `owners/createOrUpdateOwnerForm.html:18-19` `<button th:with=\"text=${owner['new']} ? #{addOwner} : #{updateOwner}\" class=\"btn btn-primary\" type=\"submit\" th:text=\"${text}\">Add Owner</button>`. The `label` argument is a message at every call site that supplies one, e.g. `createOrUpdateOwnerForm.html:11` `<input th:replace=\"~{fragments/inputField :: input (#{lastName}, 'lastName', 'text')}\" />` — the same `lastName` key this change exists for."
    disposition: pending
    disposition_rationale: null
---

# Objections — find-owners prototype text (code)

One objection, medium, and it is not about the edit.

I re-ran the sweep rather than trusting it. The twelve-character change is correct
on both requirements: `th:text="#{lastName}"` is untouched, the prototype body is
now `Last Name`, and that is byte-for-byte the value of `lastName` in
`messages.properties`. Every factual claim in the decision record that I could
check, checked out — the five named mismatch locations are all real and all at
the stated lines, the directive-9 collision at `ownerDetails.html:36-39` is real
and is exactly as described, the bundle arithmetic is right, and the record's
correction of the choice-story's "eleven bundles" is itself correct. I found
nothing wrong with the change, the build, or the reviewer's conclusion.

What I found is a gap in the map the record draws, and it is a gap the reviewer's
sweep could not have found because the sweep and the record share the same
definition of the territory.

## O1 — implementation — medium

### Claim

The decision record does something the spec did not: it asserts an enumeration of
the defect class and positions itself as the only place that enumeration is
written down. That enumeration comes from one population — elements carrying
`th:text="#{...}"`. It omits a second population, in which prototype text stands
in for a message supplied as a fragment argument or a `th:with` variable. Four
such sites exist. Three of them are the case that breaks FR-1's standard
outright, and the record identifies a different case as "the sharp edge".

### Evidence

The record's framing of its own authority:

> Recorded here as fact, verified in this working copy, because no other artefact
> that ships with the code carries it. ... Under FR-1's own standard there are at
> least five mismatches in the template set, not two

and:

> No issue and no TODO tracks it; this record is where it is written down.

The omitted sites. `src/main/resources/templates/fragments/inputField.html:10`:

```html
<label th:for="${name}" class="col-sm-2 control-label" th:text="${label}">Label</label>
```

`fragments/selectField.html:10` and `fragments/selectVetField.html:16` carry the
same line. The prototype is the string `Label`, which is not the value of any key
in `messages.properties`, and `label` is a message at every call site that
supplies one:

```html
<!-- owners/createOrUpdateOwnerForm.html:10-14 -->
<input th:replace="~{fragments/inputField :: input (#{firstName}, 'firstName', 'text')}" />
<input th:replace="~{fragments/inputField :: input (#{lastName}, 'lastName', 'text')}" />
<input th:replace="~{fragments/inputField :: input (#{address}, 'address', 'text')}" />
<input th:replace="~{fragments/inputField :: input (#{city}, 'city', 'text')}" />
<input th:replace="~{fragments/inputField :: input (#{telephone}, 'telephone', 'text')}" />
```

with `#{visitDate}`, `#{visitTime}`, `#{vet}` and `#{description}` likewise at
`pets/createOrUpdateVisitForm.html:29-32`. The fourth site,
`owners/createOrUpdateOwnerForm.html:18-19`:

```html
<button th:with="text=${owner['new']} ? #{addOwner} : #{updateOwner}" class="btn btn-primary" type="submit"
  th:text="${text}">Add Owner</button>
```

None of the four matches `th:text="#{`. All four are prototype text standing in
for a message, which is the class FR-1 is about.

### Why this matters

Three things, in ascending order.

First, the count. "At least five" is hedged and therefore not false, and I am not
objecting to the arithmetic. But the record's list is offered as an enumeration —
five bullets, each with a file and a line — and a later reader working the
deferred audit will start from it. Four sites are missing from it, and one of
them concerns `lastName`, the very key this change exists for. The prototype
divergence for `lastName` at `createOrUpdateOwnerForm.html:11` → `inputField.html:10`
is `Label` against `Last Name`, which is larger than the one this commit fixed,
and it is in a file nobody in this pipeline opened.

Second, the standard. The record names the wrapped `ownerDetails` pair as "the
sharp edge" — the place where byte-equality and directive 9 collide, and it is a
real collision. But that collision is resolvable in principle: unwrap the lines
and byte-equality is achievable, the only cost being a directive-9 argument. The
parameterised-label case is not resolvable at all. `inputField.html:10` has one
prototype and eight possible messages behind it across its call sites. No string
placed there can be byte-for-byte equal to the value it stands in for, because
there is no single value it stands in for. That is not a conflict between two
directives; it is FR-1's standard having no defined answer for a third of the
template set's message-backed prototypes. The record, which explicitly argues
that the general audit needs its own thinking, has not recorded the strongest
reason that is true.

Third, and this is the part that decides the severity rather than adding to it:
the boundary the sweep drew is also hiding a must-follow breach on the other side
of it. `pets/createOrUpdatePetForm.html:20-22` calls the same fragments with
hardcoded display text rather than message keys —

```html
<input th:replace="~{fragments/inputField :: input ('Name', 'name', 'text')}" />
<input th:replace="~{fragments/inputField :: input ('Birth Date', 'birthDate', 'date')}" />
<input th:replace="~{fragments/selectField :: select ('Type', 'type', ${types})}" />
```

— with `th:with="text=${pet['new']} ? 'Add Pet' : 'Update Pet'"` at line 26, against
AGENTS.md must-follow 3 ("No hardcoded display text in templates"), and with
`name`, `birthDate` and `type` all present in the bundle. That is pre-existing, it
is untouched by this commit, and I am **not** raising it as an objection to this
commit — see the disclosure below. I cite it here only to make the point that the
omitted population is not more of the same. It is territory with a different kind
of problem in it, and the record's "this record is where it is written down"
draws a line around it without saying so.

### What would close it

Nothing in the code. Two or three sentences in the record: name the second
population, name the parameterised-fragment case as the one FR-1's standard
cannot express, and let the deferred audit inherit an accurate map instead of a
partial one. The alternative disposition — that the record is about `th:text`
prototypes, has always been about `th:text` prototypes, and the audit will
redefine its own scope when someone picks it up — is defensible, and if the
maintainer takes it, the useful residue of this objection is the specific fact
that byte-for-byte is not a standard the audit can adopt wholesale.

## Explicitly not objecting to

- **The edit.** Verified independently against the bundle: `findOwners.html:12`
  now reads `<label class="col-sm-2 control-label" th:text="#{lastName}">Last Name</label>`,
  and `messages.properties:12` is `lastName=Last Name`. FR-1 and FR-2 are both
  satisfied and the attribute is intact. There is nothing to say against it.
- **The record's five named locations and the directive-9 collision.** I checked
  each. `layout.html:48`, `ownersList.html:32`, `vetList.html:27` and the wrapped
  pair at `ownerDetails.html:36-39` are all exactly as stated, and the claim that
  byte-equality at the wrapped pair would require unwrapping lines that already
  render correctly is true as written. `createOrUpdatePetForm.html:8`
  (`th:text="#{new}">New </th:block>`) is a genuine byte-for-byte *match* against
  `new=New `, trailing space included, and the record is right not to list it.
- **The bundle counts.** `lastName` appears in ten files — `messages.properties`
  plus nine locales, each with a real translation and none with English copied in
  — and `messages_en.properties` contains only the comment explaining that it is
  intentionally empty. The record's correction of the choice-story's "eleven
  bundles" is itself correct; eleven is the file count including the empty `_en`.
- **The scope call on `layout.html:48`.** The record names it as a judgement
  rather than a directive applying itself, which is what O3 asked for, and it is
  the right call. I will add a fact that strengthens it rather than an objection:
  the `<span>` at line 48 sits inside `<li th:replace="~{::menuItem (...)}">`, so
  it is replaced wholesale at render time and is purely natural-templating
  content. Leaving it is even safer than the record claims.
- **"The change is twelve characters."** I checked this, because the figure
  appears in six artefacts and is derived in none. The text node is ten
  characters before and nine after, and a minimal edit touches two. It reconciles
  as `>Last name <` — the node plus its delimiters — which is exactly twelve. The
  claim survives; I am recording that I tested it so the human does not have to.
- **The hardcoded labels in `createOrUpdatePetForm.html`.** A real must-follow-3
  breach, cited in O1 as evidence about the sweep's boundary, deliberately not
  raised as its own objection. It predates this commit, it is in a file this
  commit does not touch, and turning a twelve-character change into a vehicle for
  unrelated template work is the thing directive 9 exists to stop. Inflating this
  record's count on a run that exists to measure the pipeline's cost would be
  worse than leaving it unrecorded. It is now recorded, here, as not-an-objection.
- **The absence of a test.** Spec-mode O2 is deferred and adjudicated; the record
  restates the gap accurately, including that the existing `OwnerControllerTests`
  cases assert view name rather than rendered content. Re-raising it in code mode
  would be relitigating a closed disposition.
- **The record's length and the pipeline's cost on this change.** Eighty-four
  lines of decision record for twelve characters is a measurement, not a defect,
  and this run is the instrument. Saying so is not my job twice.

---

**Summary**

- Objections by category: implementation 1. (premise 0, scope 0, risk 0, alternatives 0, specification quality 0.)
- By severity: medium 1. critical 0, high 0, low 0.
- Any high or critical: **no**.
- Slug: `findowners-prototype-text`
- Mode: `code`
- Output path: `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/docs/superpowers/objections/findowners-prototype-text-code.md`
- Disposition state: the single objection is `pending`, rationale `null`.

On your measurement question, plainly: the edit is clean and I could not construct
an honest objection to it, to the build, or to the reviewer's PASS. The one
objection I am raising is against the decision record's enumeration of the
deferred defect class, and it is a finding neither the spec pass nor the code
review could have made, because both defined the territory as `th:text="#{...}"`
and the missing sites are the ones that definition excludes. If you judge that a
medium against a deferred-work note does not clear the bar on a change this size,
publishing this as an effectively empty result is defensible and I would not
argue with it.

Key files: `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/src/main/resources/templates/fragments/inputField.html` (line 10), `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/src/main/resources/templates/owners/createOrUpdateOwnerForm.html` (lines 10-14, 18-19), `/Users/russellmiles/code/Habitat-Thinking/petclinic-loops-at-levels/src/main/resources/templates/pets/createOrUpdatePetForm.html` (lines 20-22, 26).
