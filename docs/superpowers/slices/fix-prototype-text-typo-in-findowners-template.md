---
task: "Fix a typo in a Thymeleaf template. src/main/resources/templates/owners/findOwners.html line 12 reads `<label class=\"col-sm-2 control-label\" th:text=\"#{lastName}\">Last name </label>`. The prototype text says 'Last name ' — lowercase 'name', with a trailing space — where the message bundle says `lastName=Last Name`. Thymeleaf replaces the prototype text with the resolved message at runtime, so nothing a user ever sees is affected."
task_slug: fix-prototype-text-typo-in-findowners-template
date: 2026-09-21
carpaccio_model: claude-opus-5
inseparable: true
progressed_slice: S1
slices:
  - id: S1
    title: "Align the prototype text in findOwners.html with the message it stands in for"
    scope: >
      Change the prototype text of the `lastName` label in
      `src/main/resources/templates/owners/findOwners.html` line 12 from
      `Last name ` to `Last Name`, so the inert placeholder matches
      `lastName=Last Name` in `messages.properties`. One string literal, one
      line, one file. No message key is added, renamed or removed, so the 10
      locale bundles and `I18nPropertiesSyncTest` are untouched. Carries the
      decision record under `decisions/` that HARNESS.md requires of any change
      under `src/`, which in this case records that there were no judgement
      calls — the form AGENTS.md directive 10 explicitly provides for.
    decision_focus: >
      There is no material decision here, and that is the finding rather than a
      failure to find one. The prototype text has exactly one defensible value:
      the bundle's. An alternative would not produce visibly different
      downstream work; it would produce a different wrong answer. The two
      candidate decisions this record examined — whether to extend the fix to
      the one other instance of the same drift, and whether the change needs a
      decision record — are resolved by existing directives rather than by a
      human's judgement, and are recorded under `Explicitly not slicing on`
      with the evidence.
    lens_used: inseparability
    disposition: accepted
    disposition_rationale: "Accept, progress it."
    file_as_issue: false
    issue_url: null
    merged_into: null
---

## S1 — Align the prototype text in findOwners.html with the message it stands in for — inseparability

**Context**

`findOwners.html` line 12 is:

```html
<label class="col-sm-2 control-label" th:text="#{lastName}">Last name </label>
```

`th:text="#{lastName}"` resolves against `messages.properties`, which has
`lastName=Last Name` (line 12), plus ten locale bundles that all carry their own
translation of the key. At runtime Thymeleaf discards the element's body and
substitutes the resolved message, so `Last name ` never reaches a browser
serving the application.

Prototype text is not dead weight, though. It is Thymeleaf's natural-templating
design: the static file is meant to open directly in a browser and render as a
plausible page, and the prototype text is what shows when it does. That is the
one surface on which this line is observable, and the only thing the change
alters.

Two facts established by reading the repository rather than the task:

- `fragments/layout.html` line 48 carries the identical drift — prototype
  `Find owners` against `findOwners=Find Owners`. These two lines are the only
  case-mismatched prototypes in the template set.
- `messages.properties` line 42 is `new=New ` — a bundle value that genuinely
  ends in a space, matched by the `New ` prototype in
  `pets/createOrUpdatePetForm.html` line 8. The trailing space on line 12 is
  wrong because the bundle has none, not because trailing spaces are wrong.

No test references the string `Last name`.

**Decision content**

None. The prototype text stands in for a specific message, that message has a
specific value, and the correct prototype is that value. There is no second
answer a reasonable person would defend, so there is nothing here that an
alternative would make visibly different downstream.

This is worth stating rather than dressing up. The task was put through slicing
as a measurement; the honest reading of it under the decision-boundary lens
yields zero candidates, and manufacturing two so the record looks worked would
spend the human's attention on nothing — the precise failure the cadence-governor
role exists to prevent.

**Dependencies**

None. Nothing in the repository depends on the current value, nothing blocks the
change, and the change blocks nothing.

**Rationale**

The unit of change is one string literal, and there is no seam inside it. It
cannot be split into a capitalisation slice and a trailing-space slice: neither
half is independently meaningful, because the target is not "fewer defects in
this string" but "this string equals the message it stands in for", and a
half-applied edit leaves the file in a third state nobody would choose. The
accompanying decision record is bound to the same commit by
`scripts/check-decision-record.sh`, so separating the edit from its record is
refused by the harness, not merely inadvisable.

The change passes the end-to-end filter, but only on the narrow reading that the
static template opened directly is this slice's system edge. On the running
application there is nothing to observe, which the task states plainly and this
record does not dispute.

## Sequencing recommendation

Single slice. No ordering to recommend.

## Explicitly not slicing on

- **The one other instance of the same drift.** `fragments/layout.html` line 48
  reads `Find owners` against `findOwners=Find Owners`. It was considered as a
  second slice and rejected: it is the same question asked about a different
  line, not a second decision, and the task names one file. Slicing on it would
  have been scope creep wearing a slice's clothes. It is surfaced here because
  it is a fact the human would otherwise not have, and whether to fold it in is
  a scope call they may make at disposition — noting that AGENTS.md directive 9
  ("every changed hunk is needed by the change's stated purpose") and the
  "smallest diff that works" preference both point at leaving it alone, while
  the maintainer's own "Not encodable yet" entry flags exactly this boundary as
  unsettled and wanting worked examples.

- **The decision record as its own slice.** HARNESS.md requires a record under
  `decisions/` for any change touching `src/`, and this change touches `src/`.
  That is an obligation attached to the commit, not a decision, and directive 10
  explicitly allows a record that states there were no judgement calls. It
  travels inside S1.

- **A general prototype-text audit.** Sweeping every template for prototype text
  that disagrees with its bundle value is a plausible adjacent task and is not
  this one. The `new=New ` case above shows why it would need its own thinking
  rather than a regex: the bundle is the authority, and at least one bundle value
  legitimately ends in a space, so a blanket "strip trailing whitespace from
  prototype text" rule would introduce a mismatch while appearing to remove one.

- **i18n as a concern here at all.** No key is added, renamed or removed, so
  directive 4 and `I18nPropertiesSyncTest` are not engaged. Directive 3
  ("templates use message keys") is already satisfied by this line — the
  displayed text comes from `#{lastName}`, and the prototype body is inert. A
  reviewer should not read this change as directive-3 work.

- **Slicing on files, layers, commits or diff size.** The standing
  anti-patterns, and all four are moot at one line.

## Inseparability rationale

The claim is that further slicing would harm correctness, and here it would do
so by producing states no one would deliberately ship. The line's target value
is not a set of independent properties that can be converged on one at a time;
it is a single string that either equals `Last Name` or does not. Splitting the
capitalisation from the trailing space yields an intermediate — `Last Name ` or
`Last name` — that is neither the state the repository is in today nor the state
the task asks for, and that no reviewer could adjudicate on its own terms
because the only standard available is the bundle value, which the intermediate
by construction fails.

The harness reinforces the same boundary from outside. `check-decision-record.sh`
reads the changed-file list and requires a record under `decisions/` in the same
change as any edit under `src/`, so the edit and its record are a single
admissible unit whether or not slicing had an opinion.

It is worth being plain that this is an inexpensive inseparability claim. The
lens was written for atomic migrations, security patches and coherent
refactors — changes where the cost of a partial application is real. Here the
cost of a partial application is that a placeholder no user sees is wrong in a
different way than before. The claim is true, and the exercise of establishing
it was uninformative, which is itself the measurement this run was set up to
take. A record that had instead produced three or four slices would have been
easy to write, would have looked more diligent, and would have been false.
