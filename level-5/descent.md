# The descent — measured, not estimated

A genuinely trivial change put through the entire Level 5 habitat: every stage,
every gate, every sentinel. Run on 2026-09-21, 20:07:26 to 20:52:09 BST, on
`level-5-supervising`. Every figure below comes from a usage record or a
timestamp. Nothing here is estimated.

## The change

```diff
-        <label class="col-sm-2 control-label" th:text="#{lastName}">Last name </label>
+        <label class="col-sm-2 control-label" th:text="#{lastName}">Last Name</label>
```

`src/main/resources/templates/owners/findOwners.html`, line 12. Prototype text
that Thymeleaf replaces at runtime, so **no user of the running application can
observe it, before or after**. Twelve characters, counted: `>Last name <`, the
text node plus its delimiters. The code-mode objection pass derived that figure
independently rather than repeating it.

## Two ways to make the same change

| | Just make it | Through the habitat |
|---|---|---|
| Wall clock | under a minute | **45 minutes** |
| Machine time | 5.9 s (one targeted test) | **25 min of agent time** |
| Tokens | 0 | **74,258,121** |
| Cost at list price | **$0.00** | **$52.16** |
| Gates requiring a human | 0 | **4** |
| Agents involved | 0 | **9** |
| Human decisions | 1 (do it) | **5** |
| Files changed | 1 | 3 |
| Lines of artefact | 0 | **1,457** |

The habitat cost **$52.16** to change twelve characters nobody can see.

### Where the $52.16 went

| | Tokens | Cost | Share |
|---|---|---|---|
| Output | 193,352 | $4.83 | 9% |
| Cache write | 1,678,057 | $11.13 | 21% |
| Cache read | 72,386,000 | $36.19 | **69%** |
| Uncached input | 712 | $0.00 | 0% |

Same shape as the feature run, more extreme: **nine percent of the bill is the
thinking, sixty-nine percent is re-reading the accumulated record.** Each stage
loads the artefacts the previous stages wrote. The pipeline's cost is dominated
by carrying its own output, and that cost is indifferent to the size of the
change underneath it.

## What ran

| Stage | Agent time | Outcome |
|---|---|---|
| carpaccio | 2 min 0 s | 1 slice, `inseparable: true` |
| spec-writer | 1 min 25 s | 82 + 73 lines |
| advocatus-diaboli (spec) | 3 min 51 s | 4 objections, none high |
| convener | 1 min 50 s | 2 voices, 5 candidates dropped by name |
| choice-cartographer | 6 min 4 s | 5 stories |
| implementer | 1 min 36 s | the 12 characters + an 84-line decision record |
| code-reviewer | 1 min 43 s | **PASS**, no findings |
| advocatus-diaboli (code) | 4 min 6 s | 1 objection, medium |
| reservoir-warden | 2 min 50 s | advisory fired |

Four hard gates were opened and closed by a person: slice adjudication,
objection adjudication, plan approval, integration approval.

## The part that makes this hard to dismiss

**Every stage was explicitly told that returning nothing was a valid,
publishable result.** Each was told that padding would corrupt the measurement.
None of them padded — and every one of them found something true:

- **A coverage gap.** The plan claimed the existing tests would catch a damaged
  `th:text`. They assert `view().name()` only: delete the attribute and the page
  renders cleanly, showing prototype text as if it were the message.
- **A false claim in a harness-gated artefact.** The decision record was about to
  assert that no judgement calls were made. One had been — leaving
  `layout.html:48` alone — against a boundary `AGENTS.md` itself files under
  *Not encodable yet*.
- **A falsified scope claim.** The spec said `layout.html:48` was "the one other
  instance". Under the spec's own byte-for-byte standard there are **five**.
  Verified independently, twice.
- **A standard in conflict with a directive.** Satisfying byte-equality on the
  wrapped `ownerDetails.html` pair requires unwrapping lines that already render
  correctly — which directive 9 forbids.
- **A standard with no defined value at all.** `inputField.html:10` holds one
  prototype, `Label`, standing in for eight different messages depending on call
  site. Byte-for-byte equality has no answer there. Only the last stage found
  this, because every earlier sweep had defined the territory as
  `th:text="#{...}"`.
- **A question nobody asked.** `findOwners.html` is an upstream Spring PetClinic
  file. Fixing it upstream would cover both drifting lines and every other fork
  instead of creating a local divergence.

A reviewer would want all six. **The ceremony was proportionate, the findings
were genuine, and it still cost fifty-two dollars and forty-five minutes to
correct a string nobody can see.**

That is the honest version, and it is the one that survives someone in the room
saying *"but the objections were useful"*. They were. That is not the point. The
point is that the habitat has one setting, and the work does not.

## What the sentinel could and could not see

The reservoir fired an advisory during this run. Asked directly whether its
proxies could distinguish a gate on a typo from a gate on consequential work, it
answered:

> My proxies cannot distinguish a gate on a trivial change from a gate on
> consequential work. They cannot, and I will not pretend otherwise.

It could see the diffstat — one insertion, one deletion — and refused to use it,
on the grounds that diff size measures the change rather than the adjudication
and would be *"a precise answer to the wrong question"*.

So the instrument built to watch the human counted eight approvals in this window
and could not tell that seven of them were ceremony. **Nothing in five levels of
habitat can answer "was this the mode the work deserved?" That question is the
one thing here that stays with a person.**

## Reproducing it

Baseline: `sed -i '' 's|>Last name </label>|>Last Name</label>|' src/main/resources/templates/owners/findOwners.html`

The habitat run: commits `da9bfe5` through `ab236db` on `level-5-supervising`,
with the artefacts under `docs/superpowers/` and
`decisions/2026-09-21-findowners-prototype-text.md`.

Measurement method: token counts summed from the session transcript and all
subagent transcripts within the window, priced at Opus 5 list rates ($5/$25 per
MTok, cache writes at 1.25x for 5-minute TTL and 2x for 1-hour, cache reads at
0.1x). The script is `measure-descent.py`. As with the feature run, **$52.16 is
the API list-price equivalent; the marginal cash cost on a Max subscription was
£0.00.** Quote both or neither.
