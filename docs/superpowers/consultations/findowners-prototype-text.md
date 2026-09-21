---
spec: docs/superpowers/specs/findowners-prototype-text.md
date: 2026-09-21
state: open
supersedes: null
voices:
  - voice: Developers who work on this repository's Thymeleaf templates
    source_flag: inferred
    question: "when you work on these templates, do you ever open the .html files directly in a browser, or is the running application the only way you ever look at them?"
    disposition: pending
    outcome: null
  - voice: Upstream Spring PetClinic maintainers
    source_flag: inferred
    question: "would you take a one-line fix for the `Last name ` prototype text in findOwners.html upstream, or is the prototype text deliberately left as it is?"
    disposition: pending
    outcome: null
---

# Consultation — find-owners prototype text

**This project declares no `## Stakeholders` section.** `HARNESS.md` carries
context, constraints, garbage collection, observability, a cognitive reservoir
and status, and names no people. Both voices below are therefore **`inferred`**
— derived from the change itself, not declared by the project. That makes this
list shorter and less certain than a declared map would make it, and it is
labelled honestly rather than dressed up.

**Two voices, and the shortness is the finding.** This change is twelve
characters of prototype text on a line whose body `th:text="#{lastName}"`
replaces at runtime. No user of the running application can observe it; the
spec says so itself and AS-2 exists to assert that nothing they see changes.
The candidates a change to this repository would normally raise were considered
and dropped under the Routing Rule rather than emitted for appearance:
**locale translation contributors** (no key is added, renamed or removed, so
there is nothing to translate and nothing to ask), **clinic reception staff and
attending veterinarians** (no behaviour and no screen reaches them),
**database operators** (no schema, no seed data), and **workshop or
teaching-fork users** who depend on how a page looks (the rendered page is
byte-identical before and after). Each of those would have cost a mandatory
disposition at merge time and returned nothing. This record was produced on a
deliberately trivial change to measure what the pipeline costs, and inventing
five stakeholders for an invisible string would have corrupted the measurement
it was run to take.

The two that survive share a property the dropped ones lack: each holds a fact
that decides something about this change, and neither fact exists anywhere in
the repository to be reasoned out.

## Developers who work on this repository's Thymeleaf templates

> when you work on these templates, do you ever open the .html files directly
> in a browser, or is the running application the only way you ever look at
> them?

The spec's entire justification is the natural-templating surface: "As a
developer opening `findOwners.html` directly in a browser". Whether anybody
ever does that is a fact about working practice, held by the people with the
practice, and it is not recoverable by reading the repository — no test, no
script and no config records it. If the answer is that nobody has opened these
files statically, the stated benefit of this change is zero, and whatever
reason remains for making it (tidiness, or the file's text agreeing with the
bundle it stands in for) is a different reason than the one the spec gives.
That matters beyond this line, because the same premise is what would justify
the general prototype-text audit the spec puts out of scope.

Objection O4 already observed that the static preview diverges from the
rendered page in larger ways than this — no layout, a literal `Error`
paragraph. O4 argues the spec claims too much; it cannot establish whether the
surface is used at all. That is why this is a conversation and not an
objection.

## Upstream Spring PetClinic maintainers

> would you take a one-line fix for the `Last name ` prototype text in
> findOwners.html upstream, or is the prototype text deliberately left as it
> is?

`findOwners.html` is an upstream file, and this repository still carries
upstream's unmodified README and build badges. Nothing in the spec, the plan,
the slicing record or the objection record considers where this fix belongs:
all four treat the choice as being between this file and
`fragments/layout.html:48` inside this repository. The option none of them
names is fixing it once upstream, where it would cover both lines and every
other fork, instead of creating a local divergence in a file this project does
not otherwise own.

The answer can redirect the work rather than merely annotate it, which is what
makes it worth one line of someone's attention on a change this small. It is
also the cheapest voice to decline: if this fork does not track upstream, the
disposition is one sentence saying so — and that sentence records a fact about
the fork's relationship to upstream that currently appears nowhere in the
repository.
