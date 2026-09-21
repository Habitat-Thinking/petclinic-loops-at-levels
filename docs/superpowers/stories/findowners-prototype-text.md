---
spec: docs/superpowers/specs/findowners-prototype-text.md
date: 2026-09-21
mode: spec
cartographer_model: claude-opus-5
stories:
  - id: 1
    lens: [defaults, consequences]
    title: The bundle wins, and nothing says so
    disposition: pending
    disposition_rationale: null
  - id: 2
    lens: [alternatives, consequences]
    title: A defect class sized to one instance
    disposition: pending
    disposition_rationale: null
  - id: 3
    lens: [patterns, coherence]
    title: A rule enacted once, written down nowhere
    disposition: pending
    disposition_rationale: null
  - id: 4
    lens: [consequences]
    title: A measurement leaving no measurable trace
    disposition: pending
    disposition_rationale: null
  - id: 5
    lens: [forces, alternatives]
    title: A developer promoted to user, for the scenario
    disposition: pending
    disposition_rationale: null
---

# Choice stories — find-owners prototype text

Spec mode, against `docs/superpowers/specs/findowners-prototype-text.md`, its
plan, the accepted slicing record, the adjudicated objection record (O3
accepted; O1, O2, O4 deferred), `AGENTS.md`, `HARNESS.md`, `REFLECTION_LOG.md`,
the template set under `src/main/resources/templates/`, and
`src/main/resources/messages/messages.properties`.

**Reflection-log read mode:** bounded, and the bound did not bind — the file
holds one entry (2026-09-16), so it was read whole.

The change is twelve characters. Nothing below is about the twelve characters:
the diaboli could not construct an argument for a different string and neither
can I. Every story here is about the *rule* the edit enacts and the *artefacts*
the edit leaves behind, which are the two things this spec decides without
saying it is deciding them.

Stories #1, #2 and #3 are three facets of a single underlying rule — prototype
text should equal its bundle value — and that is stated plainly rather than
disguised, because they dispose differently: #1 is a candidate for promotion,
#2 is a correction to the spec's own scope section, #3 is a routing question.
A reader who wants one story should read #3.

The material finding that produced #1 and #2 is a fact no artefact in this
pipeline records: under FR-1's own byte-for-byte standard, the template set
contains at least five prototype/bundle mismatches, not the two the slicing
record counted. The spec's "the one other instance of the same drift" is true
only under a narrower definition than FR-1 uses.

Every disposition is `pending`. Nothing here is a recommendation, and nothing
here is a reason not to proceed.

## Story #1 — The bundle wins, and nothing says so

**Source:** `docs/superpowers/specs/findowners-prototype-text.md` (*Decisions*, FR-1, *Out of scope*)
**Lens:** defaults, consequences
**Refs:** O1

**Context.** A prototype text and a bundle value disagree. There are two ways to
end a disagreement between two strings, and the spec considers one: "The
prototype text stands in for one message, that message has one value, and the
correct prototype is that value." The bundle is designated authoritative in that
sentence, as a statement of fact rather than as a choice. The alternative — that
the bundle value is the wrong one — is never on the page.

**Forces.** For `lastName` the answer is not close: `lastName=Last Name` appears
in eleven bundles, is rendered to every user in every locale, and changing it
would be a user-visible i18n change under directive 4 rather than a twelve-
character edit. That asymmetry is real and it is why the spec is right about this
line. What the asymmetry conceals is that it is a property of *this* key, not a
property of the relationship — and the spec generalises from it without noticing.

**Options not taken.** Change the bundle instead, which is the correct answer
wherever the bundle value is the defective one. Adopt render-equivalence rather
than string-equality as the standard, under which neither side has to move when
the difference is whitespace. State the authority explicitly and bound it: the
bundle is authoritative for display text, and a bundle value that looks wrong is
a separate defect with its own change.

**Choice as written.** The spec chose bundle-authority by asserting it. The
clearest tell is the treatment of `messages.properties:42`, `new=New ` — the
spec calls it "a bundle value that legitimately ends in a space". It is
legitimate only because the bundle is authoritative. Under any other reading,
a trailing space on a display string is the defect, and the `New ` prototype in
`pets/createOrUpdatePetForm.html:8` is faithfully reproducing it.

**Consequences.** The repository already contains the case where this rule gives
an answer a human may not want. `messages.properties:25` is `pages=pages`, all
lowercase; the prototypes at `owners/ownersList.html:32` and
`vets/vetList.html:27` both read `Pages:`. Bundle-authority says the fix is to
make both prototypes lowercase and drop the colon, which makes the static
preview read worse and leaves the rendered page saying `pages [1] [2]`. The
reading a maintainer would more likely want — that `pages=pages` is the mistake
— is unavailable under a rule this spec has established and not written down.
The general audit the spec defers inherits the rule along with the work, and
inherits the `new=New ` justification that only holds because of it.

**Pattern.** — The closest honest description is a canonical-source designation,
which is a decision about provenance rather than a pattern.

**Notes.** Deliberately not O1. O1 is about whether FR-1's standard is
verifiable on the surface AS-1 names. This is about which of the two strings
moves when they disagree — a question O1 does not reach, and which is settled
the same way whichever standard O1's disposition lands on.

## Story #2 — A defect class sized to one instance

**Source:** `docs/superpowers/specs/findowners-prototype-text.md` (FR-1, *Out of scope*); `docs/superpowers/slices/fix-prototype-text-typo-in-findowners-template.md` (*Context*)
**Lens:** alternatives, consequences
**Refs:** O1, O3, #1

**Context.** The spec's Out of scope names `fragments/layout.html:48` as "the one
other instance of the same drift". The slicing record is where that count comes
from: "These two lines are the only case-mismatched prototypes in the template
set." Note the definition doing the work — *case-mismatched*. FR-1, twenty lines
earlier in the spec, defines the thing being fixed differently: "byte-for-byte
the value of the `lastName` key".

**Forces.** A scope section needs a boundary and a boundary needs a countable
class. Case-mismatch is countable by eye and yields a tidy two. Byte-equality is
the standard the fix is actually written to, is mechanically checkable, and
yields a number nobody surveyed. The spec resolved toward the countable
definition for the inventory and the checkable one for the requirement, and the
two never appear on the same page.

**Options not taken.** Count by the standard FR-1 adopts and report the real
number, however inconvenient. Define the class as "renders differently from its
message", which is the narrowest honest class — and under which this change's own
trailing space drops out, which is O1's point arriving from the other direction.
Write "at least one other instance; the template set was not exhaustively
surveyed", which costs nothing and claims nothing false.

**Choice as written.** The spec chose the narrow definition by inheriting the
slicing record's count without re-deriving it against FR-1. The result is a scope
section that reads as a complete inventory of a small problem.

**Consequences.** By FR-1's standard the count is at least five, not two.
`owners/ownersList.html:32` and `vets/vetList.html:27` carry `Pages:` against
`pages=pages` — a mismatch in case *and* punctuation, twice, and the one case
where the bundle is the more likely culprit (#1). `owners/ownerDetails.html:36-37`
has the prototype body `Edit\n    Owner` against `editOwner=Edit Owner`, and
lines 38-39 `Add\n    New Pet` against `addNewPet=Add New Pet` — byte-unequal
purely because the line is wrapped. That last pair is the sharp edge: satisfying
byte-equality there means unwrapping source lines that render correctly, which is
precisely the unrelated reformatting directive 9 forbids, so the standard and the
directive collide in a file neither the spec nor the plan looks at. The deferred
audit is therefore handed forward at the wrong size and with an unresolved
conflict inside it. Nothing will raise it: the slicing record carries
`file_as_issue: false`, no issue exists, no TODO exists, and `HARNESS.md` has no
constraint in this area.

**Pattern.** Cunningham's technical-debt metaphor (1992) in its weakest form — a
debt acknowledged in prose, with no principal stated and no repayment scheduled.

**Notes.** This is not O3. O3 objects that the *reason* given for excluding
`layout.html:48` is circular and propagates into a false "no judgement calls".
This is about the exclusion's *extent* — the inventory is incomplete under the
spec's own standard — and it stands whichever way O3's accepted remedy is
written.

## Story #3 — A rule enacted once, written down nowhere

**Source:** `docs/superpowers/specs/findowners-prototype-text.md` (*Decisions*, *Out of scope*); `AGENTS.md` (*Not encodable yet*); `docs/superpowers/stories/visit-carries-vet-and-time.md` (story #14)
**Lens:** patterns, coherence
**Refs:** O3, #1, #2

**Context.** What justifies this edit is a general proposition: *a prototype text
should equal the bundle value it stands in for*. The spec never states it as a
proposition. It states the instance — "the correct prototype is that value" —
and files the general form under Out of scope as "a general prototype-text
audit". Meanwhile the spec goes out of its way to disclaim the one written rule
that touches this ground: "Directive 3 is already satisfied by this line; this is
not directive-3 work." That disclaimer is correct, and it leaves the change with
no written rule behind it at all.

**Forces.** Writing the rule down would oblige the audit, or at least oblige an
explicit statement that the project holds a rule it is not applying. Not writing
it down keeps the change at twelve characters. The spec resolved toward the small
change — which is the right call for the diff and the unexamined call for the
repository, because the cost of the second option was never priced.

**Options not taken.** Add it to `AGENTS.md`, where directive 3 already governs
the neighbouring question, scoped honestly to "when a prototype and its bundle
value disagree, the bundle is authoritative" (#1). Record it in
`REFLECTION_LOG.md`, which exists for exactly this — its single entry is a rule
the project could not write before and could afterwards, and its *Constraint*
field is where candidates wait for a human. State it in the spec as a local rule
with a named scope, the way the visit spec's FR-18 note did for cleanup
boundaries.

**Choice as written.** The spec chose to apply a rule it declines to name, and to
say "Decisions. None" about a change whose entire content is that rule.

**Consequences.** `AGENTS.md` ends with a section titled *Not encodable yet* that
says, in terms, that the project knows it is short of worked examples for exactly
this kind of boundary. This change produces one and files it in a slice spec.
That is the second time: story #14 of
`docs/superpowers/stories/visit-carries-vet-and-time.md` — "A cleanup rule
invented, used once, left in a spec" — records the same shape, and its
disposition is still `pending`. So the project is about to hold two unadjudicated
stories saying the same thing about two different specs, while `AGENTS.md` still
records the gap as open. The predictable outcome is that the rule is re-derived,
differently, by whoever picks up `layout.html:48` or the audit — which is the
re-litigation this record exists to prevent.

**Pattern.** — The shape is already named in this repository, by this role, one
spec ago. Its recurrence is the finding, and recurrence across two specs is the
evidence a promotion decision normally waits for.

**Notes.** O3's accepted remedy moves one sentence into the decision record
about `layout.html:48`. It does not reach the rule. The two are compatible: O3
makes the decision record true, and this story asks where the rule that made the
decision necessary should live.

## Story #4 — A measurement leaving no measurable trace

**Source:** `docs/superpowers/specs/findowners-prototype-text.md` (whole document); plan, *Module structure*
**Lens:** consequences
**Refs:** —

**Context.** Every upstream artefact says plainly what this run is. The slicing
record: "The task was put through slicing as a measurement", and again, "which is
itself the measurement this run was set up to take". The objection record: "this
spec was put through the pipeline as a measurement and a padded record would
corrupt the measurement it was run to take." The spec says nothing. It reads as
an ordinary spec for an ordinary change.

**Forces.** A spec describes a change, not the circumstances of its own
production, and adding a paragraph about why the spec exists is exactly the kind
of self-reference that usually belongs in a reflection rather than a
requirement. Against that: the circumstance here is load-bearing, because it is
the only thing that makes eighty-three lines of spec for twelve characters of
edit a sensible act rather than a symptom.

**Options not taken.** One sentence in the spec's Scope naming the run as a
deliberate cost measurement. A `REFLECTION_LOG.md` entry, which is the artefact
built for it — the log has one entry and a `Signal` field, and "what the full
pipeline costs on trivial work" is precisely a `workflow` signal. Name it in the
decision record, where the plan already specifies three other things the record
must carry.

**Choice as written.** The spec chose, by silence, to leave the instrument out
of the record of the experiment.

**Consequences.** The artefact that survives is
`decisions/2026-09-21-findowners-prototype-text.md`. It ships with the code, it
sits beside two prior records in `decisions/`, and it is what the harness gate
checks for; `docs/superpowers/` is a pipeline working area that no constraint
protects. Per the plan's module table that record will carry the directive-10
statement and the two questions the slicing record closed, and nothing about why
this change attracted a spec, a plan, an objection pass and a story record. A
future reader of `decisions/` — and, more consequentially, a future *agent*
reading `decisions/` for precedent, which is what a directory of prior decisions
is for — will infer that this is the expected weight for a typo. The run is
being conducted to find out whether that weight is proportionate, and the only
place the answer will be legible is the place it is not written.

**Pattern.** — Suspected rather than named: a measurement apparatus left inside
the sample, with nothing marking it as apparatus.

**Notes.** The diaboli explicitly declined the adjacent finding — "eighty-three
lines of spec for twelve characters of edit" — as a finding about the pipeline
rather than a defect in the spec, and was right to. This story is not that. It
takes no position on whether the ceremony was proportionate; it records that the
spec decided not to say what the ceremony was for, and where that decision lands.

## Story #5 — A developer promoted to user, for the scenario

**Source:** `docs/superpowers/specs/findowners-prototype-text.md` (*User story*, AS-1); plan, *Test cases*
**Lens:** forces, alternatives
**Refs:** O1, O4

**Context.** A consistency fix to an inert placeholder has no user. The spec
supplies one: "As a developer opening `findOwners.html` directly in a browser".
That is not decoration — it is what lets the change have a user story, an
acceptance scenario, and a verification surface, and it is chosen in a single
sentence presented as a fact about Thymeleaf: "This is Thymeleaf's
natural-templating property, and it is the only surface on which this change is
observable."

**Forces.** The spec form requires a beneficiary and the change does not have
one, so the choice is between finding a beneficiary and admitting the form does
not fit. Natural templating is a genuine property of the view technology and it
supplies a beneficiary honestly. What was not weighed: once a developer's
browser preview is the system edge, everything downstream must be verifiable
*there*, and nothing is.

**Options not taken.** Justify the change as internal consistency with no user
story at all, and say the template does not fit a change of this kind — which is
information this run exists to collect. Frame the beneficiary as a maintainer
*reading* the file rather than previewing it, which is the reader T-1 actually
serves. Scope AS-1 to the rendered static page and accept that the trailing
space then falls out of the change entirely.

**Choice as written.** The spec chose the browser-previewing developer. The plan
then needs a test, and the only test available reads the file as text — a
different developer from the one the user story names.

**Consequences.** This is where the plan's single open question comes from. The
plan flags it — T-1 "sits awkwardly against AGENTS.md should-follow 2" — and
routes it to the human without tracing it back to the user story that generated
it, so the maintainer meets the question at the gate without the reason it
exists. If T-1 is kept, the project gains its first test that asserts the
contents of a source file, which is a new category of test here, arriving without
being named as one. That is the second time in two specs: story #16 of
`visit-carries-vet-and-time` records T-19 introducing the first test to assert
the content of a translation, and it is also still `pending`. If T-1 is dropped,
the acceptance scenario that justified promoting the developer to a user is
verified by review only — and the spec will have spent its user story on a
surface nothing checks.

**Pattern.** — Natural templating is itself the named thing; the choice worth
recording is treating a design property of the view technology as a product
surface, which makes a developer-facing artefact subject to the acceptance-
scenario contract.

**Notes.** Deliberately not O4. O4 disputes whether the "so that" clause is true
— it argues the preview still diverges in larger ways. This story disputes
nothing and takes no position on O4's disposition; it records that a
justification frame was selected over an available alternative, and that the
plan's only open question is the bill for it.

## Candidates considered and dropped

Named so the drops are visible rather than silent, which matters more than usual
on a run whose purpose is to measure what the pipeline finds.

- **"Decisions: None" as a form.** The diaboli explicitly cleared the form and
  O3 owns the accuracy of the "none". Emitting it would be a restatement.
- **FR-2 / AS-2 as a characterisation requirement.** There is a real unnamed
  pattern here (Feathers, *Working Effectively with Legacy Code*, 2004) — a
  requirement asserting that nothing changed, satisfied by the diff rather than
  by the system. Dropped because the half that matters is the coverage gap, and
  that is O2, deferred. What remains after removing O2's content is a label.
- **The spec's length relative to the change.** A pipeline finding, not a spec
  defect; the diaboli declined it for that reason. The only part of it that is a
  decision the spec made is in #4.
- **The single-slice inseparability claim.** Announced in the slicing record and
  more candidly self-critiqued there than I would have put it ("an inexpensive
  inseparability claim"). Nothing silent to surface.
- **`layout.html:48` as a story of its own** — a known defect documented, left,
  with no issue and no trigger. Folded into #2's consequences rather than emitted
  separately; as a standalone it was a third layer on the same rule and would
  have been padding.
- **The provenance of `Last name `** — whether the drift is inherited from
  upstream Spring PetClinic, which would make it a `defaults`-lens story about
  diverging from an upstream a teaching fork tracks. Dropped because I could not
  evidence it: this working copy has no git history available to me, and a
  provenance claim I would have had to guess at fails the citation test.
