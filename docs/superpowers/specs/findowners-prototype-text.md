---
slice: S1
slice_record: docs/superpowers/slices/fix-prototype-text-typo-in-findowners-template.md
title: "The prototype text on the find-owners form matches the message it stands in for"
date: 2026-09-21
status: draft — awaiting plan approval
---

# Find-owners prototype text

## Scope

Slice **S1** from the slicing record, which is the whole task. One string
literal on one line of `src/main/resources/templates/owners/findOwners.html`.

## User story

> As a developer opening `findOwners.html` directly in a browser, I want the
> label's prototype text to read the same as the message it stands in for, so
> that the static preview shows the page as the application renders it.

This is Thymeleaf's natural-templating property, and it is the only surface on
which this change is observable. On the running application `th:text="#{lastName}"`
replaces the element's body, so nothing a user of the application sees changes.

## Acceptance scenarios

**AS-1 — The static template previews the label as the application renders it**
Given `src/main/resources/templates/owners/findOwners.html` opened directly,
without the application running
When the last-name label is read
Then it reads `Last Name` — the value of `lastName` in `messages.properties` —
and not `Last name ` .

**AS-2 — The rendered page is unchanged**
Given the application running
When the find-owners page is requested
Then the last-name label is resolved from the `lastName` message key exactly as
it is today.

AS-2 asserts that nothing changed. It is here because it is the whole risk of
this change: an edit that disturbed `th:text` would be invisible in AS-1 and
would break the page.

## Functional requirements

| # | Requirement |
|---|---|
| **FR-1** | The prototype text of the `lastName` label in `owners/findOwners.html` is the string `Last Name`, byte-for-byte the value of the `lastName` key in `messages.properties`. *(AS-1)* |
| **FR-2** | The label's `th:text="#{lastName}"` attribute is unchanged, so the rendered page is unaffected. *(AS-2)* |

## Decisions

None. The prototype text stands in for one message, that message has one value,
and the correct prototype is that value. The slicing record reached the same
finding and recorded the two candidate questions it examined — extending the fix
to `fragments/layout.html:48`, and whether a decision record is needed — as
resolved by existing directives rather than by judgement. Neither is re-opened
here.

This section says "none" rather than being omitted because AGENTS.md directive
10 requires the change's decision record to state explicitly that there were no
judgement calls, and this is where that statement comes from.

## Out of scope

- `fragments/layout.html:48` (`Find owners` against `findOwners=Find Owners`),
  the one other instance of the same drift. Named in the slicing record, left
  alone under directive 9 and "smallest diff that works".
- A general prototype-text audit across the template set. The slicing record
  explains why it needs its own thinking: `messages.properties` line 42 is
  `new=New `, a bundle value that legitimately ends in a space.
- i18n. No key is added, renamed or removed, so directive 4 and
  `I18nPropertiesSyncTest` are not engaged. Directive 3 is already satisfied by
  this line; this is not directive-3 work.

## Sections with nothing to say

**Non-functional requirements**, **data**, **error handling**, **security**,
**personal data**, **migration** and **rollout** all have nothing to say about
this change, and are listed rather than written so that their absence is
deliberate rather than an omission.
