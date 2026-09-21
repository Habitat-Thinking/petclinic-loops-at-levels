---
spec: docs/superpowers/specs/findowners-prototype-text.md
date: 2026-09-21
mode: spec
diaboli_model: claude-opus-5
objections:
  - id: O1
    category: specification quality
    severity: medium
    claim: "FR-1 adopts byte-for-byte equality with the bundle as the standard, but the user story and AS-1 justify and can verify only 'reads the same' — and the trailing space, which is half of this change, is not observable on the only surface the spec names as observable."
    evidence: "FR-1: 'is the string `Last Name`, byte-for-byte the value of the `lastName` key in `messages.properties`.' AS-1: 'Given ... opened directly, without the application running / When the last-name label is read / Then it reads `Last Name` ... and not `Last name ` .'"
    disposition: pending
    disposition_rationale: null
  - id: O2
    category: risk
    severity: medium
    claim: "The spec names AS-2 as 'the whole risk of this change' but neither the spec nor the repository verifies the most likely realisation of that risk: a `th:text` attribute silently removed or misnamed renders without error, and no existing test asserts the label's content."
    evidence: "Spec: 'AS-2 asserts that nothing changed. It is here because it is the whole risk of this change: an edit that disturbed `th:text` would be invisible in AS-1 and would break the page.' Plan T-2: 'a mistyped edit that broke the `th:text` attribute fails them.' The cited tests assert only `view().name(\"owners/findOwners\")` (OwnerControllerTests lines 153 and 206)."
    disposition: pending
    disposition_rationale: null
  - id: O3
    category: scope
    severity: medium
    claim: "The exclusion of `fragments/layout.html:48` is presented as resolved by existing directives when AGENTS.md itself records that exact boundary as not yet encodable, which means 'Decisions: None' will propagate into a decision record that claims no judgement calls were made."
    evidence: "Spec Out of scope: '`fragments/layout.html:48` ... Named in the slicing record, left alone under directive 9 and \"smallest diff that works\".' Spec Decisions: 'None. ... resolved by existing directives rather than by judgement.' AGENTS.md, Not encodable yet: '\"Needed by the change's purpose\" (the no-unrelated-reformatting rule) needs worked examples to settle where cleanup ends and noise begins.'"
    disposition: pending
    disposition_rationale: null
  - id: O4
    category: premise
    severity: low
    claim: "The user story's 'so that' clause claims a benefit this change does not deliver: after the edit, the static preview of findOwners.html still differs from the rendered page in larger and more visible ways than the capitalisation of one word."
    evidence: "Spec user story: '... so that the static preview shows the page as the application renders it.' `findOwners.html` lines 3 and 16–17: the file's layout is supplied by `th:replace=\"~{fragments/layout :: layout ...}\"`, which does not apply statically, and `<div th:if=\"${#fields.hasAnyErrors()}\"><p th:each=... >Error</p></div>` renders a literal 'Error' paragraph in the static preview that never appears in the rendered page."
    disposition: pending
    disposition_rationale: null
---

# Objections — find-owners prototype text

Four objections, none high, none critical. That is the honest result and it is
stated here rather than buried, because this spec was put through the pipeline
as a measurement and a padded record would corrupt the measurement it was run
to take.

Three of the four objections are about claims the spec makes, not about the
edit. The edit itself — `Last name ` becomes `Last Name` — survived every
attempt to falsify it: there is one message, it has one value, and the
prototype's only defensible target is that value. No objection below argues for
a different string, a different file, or a different sequencing. They argue
that the spec asserts more than its evidence carries, in three places, and that
one of those over-assertions will be copied into a harness-gated artefact.

No objection below is a reason not to proceed. O2 and O3 are the two worth a
minute of thought at the gate; O1 partially answers the plan's own open
question about T-1; O4 is informational.

## O1 — specification quality — medium

### Claim

FR-1 sets the standard for the prototype text at byte-for-byte equality with
the bundle value. The user story and AS-1 set it at "reads the same". These are
not the same standard, and the difference between them is precisely the trailing
space this change removes. On the only surface the spec names as observable, the
trailing space cannot be seen.

### Evidence

FR-1:

> The prototype text of the `lastName` label in `owners/findOwners.html` is the
> string `Last Name`, byte-for-byte the value of the `lastName` key in
> `messages.properties`. *(AS-1)*

The scenario FR-1 cites:

> **AS-1 — The static template previews the label as the application renders it**
> Given `src/main/resources/templates/owners/findOwners.html` opened directly,
> without the application running
> When the last-name label is read
> Then it reads `Last Name` — the value of `lastName` in `messages.properties` —
> and not `Last name ` .

And the user story it serves:

> I want the label's prototype text to read the same as the message it stands
> in for, so that the static preview shows the page as the application renders
> it.

"Opened directly" and "read" describe a browser. In a browser, HTML whitespace
collapses: the text node `Last name ` is laid out inside an inline `<label>`
whose next sibling is a block-level `<div class="col-sm-10">`, so the trailing
space is not rendered. What a reader can discriminate on that surface is the
capital `N` and nothing else. `Last Name` and `Last Name ` are indistinguishable
there; so, for the trailing space alone, are the before and after states of this
change.

### Why this matters

Two consequences, one immediate and one for later.

Immediately, AS-1 is ambiguous between two readings — read as rendered, or read
as file text — and only the second can verify FR-1 as written. This is not a
pedantic distinction: it settles half of the open question the plan raises at
the approval gate. The plan asks whether T-1 sits awkwardly against should-follow
2 because "under natural templating the file's text *is* the observable
artefact". O1 says the file's text and the file's rendering are different
artefacts, and FR-1 is a claim about the text that the rendering cannot check.
If T-1 is kept, it must read the file as text, and the spec should say so rather
than leaving the implementer to infer it.

Later, the byte-for-byte standard is the one that generalises badly, and the
slicing record already found the case that breaks it: `new=New ` is a bundle
value that legitimately ends in a space, so byte-for-byte compliance requires a
prototype with a trailing space that renders identically to one without. The
spec puts the general audit out of scope for exactly this reason but adopts the
standard anyway for this one line, without noting that it is adopting the
stricter of two available standards or why.

The narrow version of this objection: FR-1 requires something AS-1 cannot see.
The human may reasonably answer that byte-identity is the right target because
it is the only mechanically checkable one, which is a good answer — it just is
not in the spec.

## O2 — risk — medium

### Claim

The spec elevates one risk above all others and then leaves it covered by an
assertion rather than a control. The class of edit most likely to realise that
risk — the `th:text` attribute deleted or misspelled rather than malformed —
produces a page that renders without error, and no existing test asserts
anything about the label's content.

### Evidence

The spec:

> AS-2 asserts that nothing changed. It is here because it is the whole risk of
> this change: an edit that disturbed `th:text` would be invisible in AS-1 and
> would break the page.

The plan's coverage claim for it:

> T-2 | The existing `OwnerControllerTests` cases that render
> `owners/findOwners` (lines 153 and 206). They pass unchanged; a mistyped edit
> that broke the `th:text` attribute fails them.

What those tests actually assert, in
`src/test/java/org/springframework/samples/petclinic/owner/OwnerControllerTests.java`
(`@WebMvcTest(OwnerController.class)`):

```java
mockMvc.perform(get("/owners/find"))
    .andExpect(status().isOk())
    .andExpect(model().attributeExists("owner"))
    .andExpect(view().name("owners/findOwners"));
```

and at line 206, likewise, `.andExpect(view().name("owners/findOwners"))` after
model-error assertions. Status, model, view name. Nothing about rendered
content. The spec's own "Sections with nothing to say" does not list testing,
and the spec nowhere states how AS-2 is verified.

### Why this matters

"A mistyped edit that broke the `th:text` attribute fails them" is true for the
subset of breakages that make Thymeleaf throw — a malformed expression, an
unclosed brace. It is not true for the subset that does not:

- the attribute deleted outright, leaving `<label class="col-sm-2 control-label">Last Name</label>`,
  which renders cleanly and shows the prototype text as if it were the message;
- the attribute renamed (`th:txt`), same outcome;
- the key misspelled (`#{lastNam}`), which Thymeleaf renders as `??lastNam_en??`
  rather than failing.

All three pass `view().name("owners/findOwners")`. The first two are the plausible
accidents when someone is editing that element's body, which is the only thing
this change does. So the stated whole risk of the change is covered against the
failure modes that announce themselves and uncovered against the ones that do
not.

The honest scale of this: the edit is twelve characters and a careful human or
agent will not delete the attribute. But the spec chose to name this as the
whole risk, and having named it, the coverage claim standing behind it is
weaker than the plan states. The remedy is small — a `content().string(containsString("Last Name"))`
on the existing find-form test would close it, and would be a test of rendered
behaviour rather than of file contents, which sits better against should-follow
2 than T-1 does. Whether that is worth a hunk on a change this size is a
judgement for the human, not for me.

## O3 — scope — medium

### Claim

The spec closes the `fragments/layout.html:48` question by citing directives,
and separately declares that there were no judgement calls. AGENTS.md records
this exact boundary as one its own directives cannot yet settle. A scope call
was therefore made, and the decision record this change is required to carry
will state that none was.

### Evidence

The spec's Out of scope:

> `fragments/layout.html:48` (`Find owners` against `findOwners=Find Owners`),
> the one other instance of the same drift. Named in the slicing record, left
> alone under directive 9 and "smallest diff that works".

The spec's Decisions section:

> None. ... The slicing record reached the same finding and recorded the two
> candidate questions it examined — extending the fix to `fragments/layout.html:48`,
> and whether a decision record is needed — as resolved by existing directives
> rather than by judgement. Neither is re-opened here.

AGENTS.md, "Not encodable yet":

> **"Needed by the change's purpose"** (the no-unrelated-reformatting rule)
> needs worked examples to settle where cleanup ends and noise begins.

And the slicing record, which was more careful about this than the spec is:

> ... noting that AGENTS.md directive 9 ... and the "smallest diff that works"
> preference both point at leaving it alone, while the maintainer's own "Not
> encodable yet" entry flags exactly this boundary as unsettled and wanting
> worked examples.

The slicing record also says the question is one "the human would otherwise not
have, and whether to fold it in is a scope call they may make at disposition".

### Why this matters

Directive 9 is not self-applying here. It says every changed hunk must be needed
by the change's stated purpose — so it resolves the question only once the
purpose has been scoped to findOwners.html, which is the thing being decided.
Cited as the reason for the scope, it is circular: the scope justifies the
directive's application and the directive is then offered as justifying the
scope. That is not a fatal error — narrow is the right default, and both the
slicing record and I would land in the same place — but it is a judgement
wearing a directive's clothes, and the repository's own directives say so in as
many words.

The consequence is concrete rather than philosophical. Directive 10 requires a
decision record naming every judgement call the task did not state, or stating
explicitly that there were none. The plan's module table commits to the second
form:

> Under AGENTS.md directive 10 it states explicitly that there were no judgement
> calls ...

HARNESS.md is clear that the gate cannot catch this: "It reads the changed file
list and nothing else ... It does not check what the record says." So a record
asserting "no judgement calls" on a change where a scope call was made against
an admittedly-unsettled boundary will pass every deterministic check in the
repository. Only a person reading it will notice, and the spec is currently
telling that person there is nothing to notice.

The cheapest disposition that closes this is not to change the scope. It is to
let the decision record say what the slicing record already said: the other
instance exists, leaving it alone is the call, and the directive that supports
it is one the project has flagged as needing worked examples. That costs two
sentences and converts a false "none" into a true small one — and, incidentally,
supplies one of the worked examples AGENTS.md says it is waiting for.

I am not adjudicating between that and the alternative (fold layout.html:48 in,
one record covering both, zero known drift remaining). Both are defensible. The
objection is to the claim that neither required deciding.

## O4 — premise — low

### Claim

The user story's "so that" clause states a benefit the change does not deliver.
After this edit the static preview of `findOwners.html` still does not show the
page as the application renders it, and the remaining divergences on that same
surface are larger and more visible than the one being fixed.

### Evidence

The spec:

> As a developer opening `findOwners.html` directly in a browser, I want the
> label's prototype text to read the same as the message it stands in for, so
> that the static preview shows the page as the application renders it.

`src/main/resources/templates/owners/findOwners.html`, line 3:

```html
<html xmlns:th="https://www.thymeleaf.org" th:replace="~{fragments/layout :: layout (~{::body},'owners')}">
```

Opened directly, no layout is substituted: no head, no stylesheet, no
navigation, no Bootstrap. The `col-sm-2` and `form-horizontal` classes style
nothing. And lines 15–19:

```html
<span class="help-inline">
  <div th:if="${#fields.hasAnyErrors()}">
    <p th:each="err : ${#fields.allErrors()}" th:text="${err}">Error</p>
  </div>
</span>
```

Statically, `th:if` does not suppress anything, so the preview shows a literal
`Error` paragraph under the input — content that never appears on the rendered
page in its normal state. That divergence is a whole spurious line; the one
being fixed is the case of one letter.

### Why this matters

Very little, on its own — which is why this is `low` and not higher. The change
is still an improvement and nothing about it becomes wrong if this objection
stands. But the "so that" clause is the spec's entire justification for doing
the work at all, and as written it is a claim about the whole preview that only
holds for one word of it. A defensible version would be narrower: *so that this
label does not misstate the message it stands in for*. That claim is true after
the change and does not depend on the rest of the preview being faithful.

The reason to bother saying so: the broad version of the claim licenses exactly
the follow-on work the spec puts out of scope in the next section. If the goal
is that the static preview matches the rendered page, the `Error` paragraph and
the missing layout are larger failures of that goal than the prototype text was,
and a later reader who takes the user story at face value has been handed a
rationale for work this spec declined. The narrow version does not have that
problem. This is the same over-assertion as O1 and O3 in a third place, and that
pattern — the artefacts claiming slightly more than the evidence carries — is
the only thing I found here that recurs.

## Explicitly not objecting to

- **The change itself.** `Last name ` → `Last Name` is correct, it has one
  defensible target value, and I could not construct an argument for any other
  string that a reasonable person would defend.
- **"Decisions: None" as a form.** AGENTS.md directive 10 explicitly provides
  for a record that states there were no judgement calls, and the spec is right
  to write the section rather than omit it. O3 objects to the accuracy of the
  "none" on one point, not to the form.
- **The single-slice / inseparability finding.** The slicing record's argument
  that the string has no internal seam is correct, and its own admission that
  this is "an inexpensive inseparability claim" is more candid than anything I
  would have said about it.
- **Excluding the general prototype-text audit.** The `new=New ` case is a real
  reason the audit needs its own thinking rather than a regex, and the spec
  cites it accurately. Deferring it is right.
- **Excluding i18n.** Verified: no key is added, renamed or removed;
  `messages_en.properties` is intentionally empty so English resolves to
  `messages.properties`; `I18nPropertiesSyncTest` is genuinely not engaged. The
  spec's claim here is exactly true.
- **The plan's open question on T-1.** The plan flags it, states the arguments
  both ways, and routes it to the human rather than deciding it. That is correct
  handling and re-raising it as an objection would be adversarial theatre. O1
  bears on it but does not duplicate it.
- **The "Sections with nothing to say" device.** Listing the empty sections
  rather than omitting them is the right call and costs nothing.
- **The spec's length relative to the change.** The obvious objection —
  eighty-three lines of spec for twelve characters of edit — is a finding about
  the pipeline, which is what this run exists to measure, not a defect in the
  spec. Raising it here would be answering a question nobody asked me.
