---
slice: S1
spec: docs/superpowers/specs/findowners-prototype-text.md
title: "Plan — find-owners prototype text"
date: 2026-09-21
---

# Plan — find-owners prototype text

Implements [findowners-prototype-text.md](findowners-prototype-text.md).

## Module structure

| File | Change |
|---|---|
| `src/main/resources/templates/owners/findOwners.html` | Line 12: the label's body becomes `Last Name`. The `class` and `th:text` attributes are untouched. |
| `decisions/2026-09-21-findowners-prototype-text.md` *(new)* | Required by HARNESS.md → **Decision record for source changes**, because the change touches `src/`. Under AGENTS.md directive 10 it states explicitly that there were no judgement calls, and why: the prototype text stands in for one message and the bundle supplies its value. It should also record the two questions the slicing record closed against existing directives — `fragments/layout.html:48` left alone under directive 9, and i18n not engaged because no key moves — so a later reader of the code finds them without the slicing record. |

No other file changes. No Java, no properties file, no schema, no CI.

## Algorithm notes

Nothing to say. There is no algorithm, no data, and no decision.

## Test cases

The only surface on which FR-1 is observable is the static file's own text, so
any test of it is a test that reads the template as a file. That sits awkwardly
against AGENTS.md should-follow 2 ("tests check behaviour a user can observe"),
and the awkwardness is real rather than avoidable: under natural templating the
file's text *is* the observable artefact, but a test of it is still a test of a
source file's contents. **This is the one open question at the plan-approval
gate**, and it is a question about testing, not about the change.

| # | Test | Status |
|---|---|---|
| T-1 | `findOwnersPrototypeTextMatchesTheBundle` — read `owners/findOwners.html`, extract the body of the element carrying `th:text="#{lastName}"`, assert it equals the value of `lastName` in `messages.properties`. Covers FR-1 / AS-1. | **Recommended, for the maintainer to confirm or drop.** |
| T-2 | The existing `OwnerControllerTests` cases that render `owners/findOwners` (lines 153 and 206). They pass unchanged; a mistyped edit that broke the `th:text` attribute fails them. Covers FR-2 / AS-2. | Existing, unchanged, no new test needed. |

Arguments both ways on T-1, stated once so the maintainer can decide without
re-deriving them:

- **For**: it is the only thing that would catch this drift returning, it is
  four lines, and it pins the prototype to the bundle rather than to a literal
  a future edit could retype.
- **Against**: it tests one line of one file, and the general version of it —
  every prototype against every bundle value — is the audit the spec puts out
  of scope, for the reason the slicing record gives (`new=New ` is a bundle
  value that legitimately ends in a space). A test that checks this one line and
  no other is the narrowest possible instance of a rule nobody has written.

If T-1 is dropped, FR-1 is covered by review only, and that should be said in
the decision record rather than left implicit.

## FR mapping

| FR | Covered by |
|---|---|
| FR-1 | T-1, if kept; otherwise review |
| FR-2 | T-2 (existing) |

## Risks

One, and it is small: the edit must not disturb `th:text="#{lastName}"`. T-2
catches that.

## Sections with nothing to say

**Schema parity**, **i18n**, **fragments**, **layering**, **dependencies** and
**rollout** are all unengaged by this change. They are listed so their absence
is visibly deliberate. Formatting and the decision-record gate apply as they do
to any change; the decision record is in the module table above because it is a
file this change adds.
