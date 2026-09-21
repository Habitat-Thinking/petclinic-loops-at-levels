# Level 5 findings — what the instruments found, including about themselves

Built 2026-09-20 to 2026-09-21 on `level-5-supervising`. Every artefact cited
here is committed, verbatim and unedited: two `/assess` reports, a
`/diagnose` run, two reservoir readings, the trust-boundary capture, and the
descent measurement. Nothing was softened, and the places where the instruments
contradicted the person who dispatched them are the parts worth keeping.

These are the raw materials for `slides/BEFORE-L5.md` and `slides/AFTER-L5.md`.
Ordered as a deck would carry them.

## 1. The branch called `level-5-supervising` assesses as Level 4

The single most useful finding in the level, and it is against the project.

`/assess` run read-only against a detached checkout returned **Level 4 —
Specification Architecture**, with a Habitat Build Gap of **+1.0**, *"ambition
outpaces enablement"*. Not because the work is thin — it scores context
engineering **5/5** and calls the four-document habitat *"the best-designed
habitat surface I have assessed on this codebase"*. Because of this:

> Every pipeline gate is prose. A `grep` for `disposition|pending` across every
> script, workflow and habitat document returns nothing. `PIPELINE.md` names a
> merge-time constraint in `HARNESS.md` as its backstop and that constraint does
> not exist.

and this:

> Nothing this project built for itself has ever been stopped by the gate it
> built.

The blocking CI path is correctly gated on `pull_request`; the project has
opened none. It also counted what four gates actually produced on the feature
run — **46 dispositions, 17 still pending** — and named the eleven spec-mode
objections sharing one verbatim rationale as *"a bulk action wearing the costume
of eleven judgements"*.

**Why this is the right thing to show:** you cannot credibly say "Level 5 is not
a destination" from a repository that has just awarded itself Level 5. The
instrument disagreeing with the branch name is what earns the close.

**The Level 4/5 line, stated by the assessor:** ten agents and eight commands are
cited across the habitat; `.claude/` holds one 16-line settings file; nothing
names or pins the plugin. *Clone the repository and you cannot run its pipeline.*
What is in the repo is the pipeline's **output** and its **specification**, not
the pipeline.

## 2. The trust boundary is provable in one line each

`level-5/trust-boundary.txt`. Same plugin, same file format:

```
reservoir-warden:  tools: [Read, Glob, Grep, Bash]
mast:              tools: [Read, Glob, Grep, Bash]

spec-writer:       tools: [Read, Write, Edit, Glob, Grep]
tdd-agent:         tools: [Read, Write, Edit, Glob, Grep, Bash]
integration-agent: tools: [Read, Write, Edit, Bash]
harness-gc:        tools: ["Read", "Write", "Edit", "Glob", "Grep", "Bash"]
```

No Write. No Edit. No Agent. **A sentinel cannot change the thing it watches and
cannot delegate to something that can.** Fifteen seconds on screen, and it is
shown rather than claimed — which is the whole point, because a sentinel that
*could* edit the repository is a different animal wearing the same name.

## 3. The sentinel was taken, not overridden — and that is the harder story

The reservoir ran against the real state at the end of the Level 4 run. It fired
on two proxies, and then **argued against its own finding three times**: a
five-minute margin on the span, a 116-minute gap inside the window that any idle
cut would have split, and the possibility it was measuring elapsed rather than
engaged time.

It gave every reason to wave it through. The maintainer **agreed with it
entirely and stopped for the night.**

It also refused its own briefing. Told the time was "early afternoon", it ran
`date`, found 17:09, reported the clock, and listed the discrepancy as one of
three things it would not tidy away. *A sentinel that accepts its briefer's
version of reality is not watching anything.*

## 4. The override is real, and it comes from Level 4

Because the reservoir was taken, the override beat needs a genuine case of advice
declined — and there is one, with the reasoning committed within minutes of the
advice:

**Code-mode objection O4.** The adversarial reviewer said a vet-less visit taking
down the whole owner page was too wide a blast radius. **Rejected:** *"Fail
loud... A vet-less visit is corruption, and a blank cell would hide it."* Two
agents disagreed with each other; a human settled it against one of them.

If every piece of advice in the talk is shown being taken, the room will
reasonably conclude sentinels are gates with better manners.

## 5. The diagnosis found the comprehension gap it was run to find

`/diagnose` on the feature the pipeline built — 691 lines, unedited, both models
cross-checked. The line to read aloud:

> Two things a reader of this class cannot see from it. First, what it saves is
> not a Visit... The Owner is the unit of persistence; a Visit reaches the
> database by cascade from `Pet.visits`. There is no `VisitRepository`. **A
> reader looking for where a visit is written will not find it in this package
> under that name.**

About code the maintainer specified, adjudicated and approved without writing a
line of it.

It reached the null-safety argument **cold, from a third direction** —
*"defensive syntax is present where it cannot help and absent where a reader
would look for it"* — corroborating a finding the reviewer and the objection pass
had already fought over, having seen neither.

And it disclosed its limits rather than smoothing them: two elements at medium
confidence, each naming exactly what it could not establish (it cannot run `git`;
it has not observed a rendered page).

## 6. The descent: $52.16 and 45 minutes for twelve invisible characters

Full detail in `level-5/descent.md`; the script that produces the number ships
beside it.

| | Just make it | Through the habitat |
|---|---|---|
| Wall clock | under a minute | **45 minutes** |
| Cost at list price | **$0.00** | **$52.16** |
| Tokens | 0 | **74,258,121** |
| Human gates | 0 | **4** |
| Agents | 0 | **9** |
| Lines of artefact | 0 | **1,457** |

**69% of the bill is cache reads** — each stage re-loading what the previous
stages wrote. Output is 9%. The pipeline's cost is dominated by carrying its own
record, and that is indifferent to the size of the change underneath it.

### The twist that makes it undismissable

Every stage was told, explicitly, that returning nothing was a valid and
publishable result, and that padding would corrupt the measurement. **None
padded. Every one found something true:**

- a **coverage gap** — the existing tests assert `view().name()` only, so a
  deleted `th:text` renders cleanly and passes
- a **false claim in a harness-gated artefact** — the decision record was about
  to assert no judgement calls were made; one had been
- a **falsified scope claim** — "the one other instance" is five, under the
  spec's own standard, verified twice
- a **standard colliding with a directive** — byte-equality requires unwrapping
  lines directive 9 protects
- a **standard with no defined value at all** — one prototype, eight messages,
  found only by the last stage because every earlier sweep shared the same blind
  definition
- an **unasked question** — it is an upstream file; fix it upstream and every
  fork gets it

A reviewer would want all six. **The ceremony was proportionate, the findings
were genuine, and it still cost fifty-two dollars to fix a string nobody can
see.** That is the version that survives *"but the objections were useful"*.

## 7. The sentinel cannot tell ceremony from consequence, and said so

Asked directly, during the descent, whether its proxies could distinguish a gate
on a typo from a gate on consequential work:

> My proxies cannot distinguish a gate on a trivial change from a gate on
> consequential work. They cannot, and I will not pretend otherwise.

It could see the diffstat — one insertion, one deletion — and **refused to use
it**, because diff size measures the change rather than the adjudication and
would be *"a precise answer to the wrong question"*.

Two more limits it volunteered:

- **One unconfigured parameter decides its own verdict.** Scoped to the checked-out
  branch, decision volume is 7 and nothing fires; across all branches it is 8 and
  the advisory fires. *"Someone else applying the shipped method could reasonably
  get the opposite answer."*
- **It cannot see who decided.** *"Git records who committed, not who decided; an
  agent-produced disposition committed by a human and a human-reasoned
  disposition are byte-identical in the log."*

That last one lands directly on Level 4's finding, where an agent wrote a
disposition and only its own flag caught it. **The instrument built to watch the
human cannot tell whether the human did the deciding.**

## 8. The instrument's own write surface failed in the register it warns about

Found while authoring the pact the level argues for — not in the repository, in the
plugin.

`/mast tune` is the only sanctioned path that creates `~/.claude/pacts.md`. Its
command file tells the caller to **source** `hooks/scripts/lib/pact-write.sh`. That
library resolves its own directory through `BASH_SOURCE`, which **zsh does not set**,
and the caller here is a Claude session's Bash tool running the user's login shell —
zsh, on every macOS since Catalina.

Same inputs, two shells:

| | exit code | `block_state` | file |
|---|---|---|---|
| bash | 0 | `declared` | 45 lines |
| **zsh** | **0** | **`malformed`** | **10 lines** |

The zsh file is missing the template preamble, the field notes, and the mandatory
governing clause. **It returns success.**

The writer's own header comment names this exact outcome:

> THE READER AND THE WRITER ARE ONE CONTRACT. Whatever this emits, `block_state` must
> call `declared`. Getting that wrong fails in the quietest possible way: the block
> reads `malformed`, every consumer drops to observe-only per S1's Null Object
> contract, and nothing tells the human which sentence is missing.

### Two correct behaviours composing into a silent wrong one

`_pw_template_prose` starts `[ -f "$_PW_TEMPLATE" ] || return 0` — empty prose, exit 0.
`_pw_seed_file` falls through to a hardcoded two-line preamble. Each is defensible
alone; the clause is *derived* from the template rather than restated, which is the
right call. Together, on a template that cannot be found, they emit a plausible file
with no governing sentence and no error.

### The test suite structurally cannot catch it

`tdad_tests/layer0_deterministic/test-pact-write.sh` is `#!/usr/bin/env bash`, and T1
**is** the reader/writer round-trip guard. It passes — because it runs the writer in
the one shell where the path resolves. The suite exercises the library. The bug is in
the path the command tells a model to take to reach it.

### The blast radius is one line, and it is the worst one

Four commands instruct sourcing a library:

| Command | Library | Affected |
|---|---|---|
| `mast.md:22` | `pact-blocks.sh` | no |
| `wip.md:33` | `pact-blocks.sh` | no |
| `coda.md:88` | `mast-notes-read.sh` | no |
| **`mast.md:100`** | **`pact-write.sh`** | **yes** |

Every read surface is clean. The single write surface a command sources is the broken
one, and it breaks at **first authorship** — the one moment the Mast's whole argument
rests on. The other ~22 files using `BASH_SOURCE` run with a `bash` shebang and are
correct.

Filed as [ai-literacy-superpowers#617](https://github.com/Habitat-Thinking/ai-literacy-superpowers/issues/617).
The pact written for this talk is sound: it was written under `bash -c` and validated
— `declared`, clause present, `21:30` intact, one heading.

### Why this is a Level 5 finding and not a bug report

Levels 1–4 supervise the code. Level 5 supervises whether the person can still answer
for it, and it does that through instruments. This is an instrument's own write path
failing silently, at the moment of authorship, in a plugin whose tests are unusually
good — and **nothing in the harness, the pipeline or the sentinels could have found
it.** It was found by running the command instead of reading it.

That is the same shape as the `/assess` finding that produced the enforcement column,
and the same shape as beat 4's 133-column table. Three times in this build, the defect
was invisible to reading and obvious to execution.

## 9. Which is the close

Nothing in five levels of habitat answers *"was this the mode the work
deserved?"*. The harness checks the code. The pipeline sequences the work. The
sentinels watch the load. None of them can weigh whether the ceremony was worth
running, and the one that comes closest says so in its own output.

The mapping is in `level-5/repertoire.md`. One question — *if this turns out to
be wrong, what does it take to put right?* — and four classes: **Undo, Repair,
Migrate, Answer**.

The typo is **Undo** and got four gates. The booking feature is **Migrate** in
the code and **Answer** in the product, and the code answer is the cheaper one:
ask only "can I revert it?" and you never reach the clinician whose name is on a
commitment they never made. **The class that picks the mode is the most expensive
one the work touches.**

## Smaller things worth a sentence

- **The `/assess` finding was acted on inside fifteen minutes.** It called
  `PIPELINE.md`'s gates over-claiming; every gate is now tagged with an
  enforcement class, six read `human`, and the false backstop sentence is
  corrected rather than reworded. Shortest instrument-to-action loop in the build.
- **The Level 1 assessor caught a false positive the setup created.**
  `core.hooksPath` returned `.githooks` inside a worktree, inherited through the
  worktree link, pointing at a directory that does not exist there. Trusting it
  *"would have been enough to manufacture habitat where the entire point is that
  there is none."*
- **It also admitted it could not prove what it was asked to find:** *"the
  assessor can prove the absence of Levels 2–5... but cannot prove the presence
  of Level 1 — prompting leaves no trace."*
- **The descent had to be moved off the ladder.** It is evidence produced *by*
  Level 5, not an artefact *of* it, and it made the level diff show a template
  typo. It lives on `level-5-supervising-descent`, by revert rather than
  force-push, and the ladder carries two files.
- **Six times in this build, a downstream agent corrected an upstream one** —
  the reviewer's Thymeleaf premise, the delta pass's account of its own scope, a
  token figure wrong by two orders of magnitude, the "early afternoon" briefing,
  the "eleven bundles" claim, and the sweep that defined its own territory too
  narrowly. None was caught by a gate. All were caught by the next agent doing
  its job honestly.
