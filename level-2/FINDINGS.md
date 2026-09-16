# Level 2 findings

## What changed between Level 1 and Level 2

Same prompt, same model (`claude-opus-5`, effort high), same machine, clean source both
times. The only difference: the repository now says what it expects.

| | Level 1 | Level 2 |
|---|---------|---------|
| Time | 118 s | 120 s |
| Turns | 10 | 10 |
| Cost | $0.76 | $1.06 |
| Tests | 78 green | 78 green |
| Files changed | 21 | 22 |

Cost rose ~40% for the larger prompt. Time did not move.

**What the directives actually bought** (`git diff level-1-dictating-after..level-2-commanding-after -- src/`):

1. **Personal data handled deliberately.** Level 1 put `email` into `Owner.toString()`.
   Level 2 left it out, citing directive 6, and flagged that directive 6's list does not
   yet name email — it asked rather than assumed.
2. **The new column proved on real engines.** Level 2 extended `MySqlIntegrationTests`
   and `PostgresIntegrationTests` so the column is exercised on MySQL and Postgres, not
   just H2 (directive 9). Level 1 did not touch them.
3. **Decisions surfaced as a numbered list** (directive 12) — six of them, including the
   required-vs-optional question that Level 1 answered differently on different runs.
4. **Translations claimed honestly.** Both levels wrote real translations; Level 2 stated
   they were its own work and asked for a native-speaker check (directive 4).

## The drift: what context did not prevent

### Primary — the Level 3 seed

**Directive 12 was followed in letter and defeated in practice.** It says every judgement
call is listed in a "Decisions" section of *the PR description*. The run produced six
numbered decisions — into the chat transcript. There is no PR. Nothing in the working
tree records them.

Close the terminal and the reasoning evaporates, exactly as it did at Level 1. The
directive named a destination that does not exist, and **nothing checked where the
decisions actually landed**.

This is the failure that becomes a constraint at Level 3. The reflection entry already
proposes the fix: name a destination that always exists — a decisions record committed
with the change — and check that it is there.

### Secondary

**It knowingly shipped an inconsistency.** The column is `VARCHAR(255)` on H2 and MySQL;
the entity has no `@Size`. An over-length email is therefore a database error rather than
a form error. The run identified this and listed it as Decision 2 rather than fixing it.

Advisory context can *note* a defect. It cannot *stop* one.

## Findings about the tooling itself

### `AGENTS.md` is not loaded automatically — the pointer silently failed

The most dangerous finding of this level, and invisible to inspection.

Claude Code 2.1.272 auto-loads `CLAUDE.md` and nothing else. With `CLAUDE.md` written as
a plain link to `AGENTS.md`, a fresh session reported **zero directives** — it knew only
the file's name. The habitat looked complete on disk and was doing nothing.

The full audit is in [agents-md-not-loaded-audit.txt](agents-md-not-loaded-audit.txt).
Had this not been checked, the Level 2 run would have behaved like Level 1 and the whole
comparison would have been worthless.

**Fix:** `@AGENTS.md` in `CLAUDE.md` imports the content at startup. Re-verified: the
agent then reports all 12 directives without opening a file. `AGENTS.md` stays the single
source.

**For the stage:** this is the concrete case for "verify empirically, not by inspection".
A pointer file that a tool ignores is indistinguishable from one that works, right up
until the demo.

### The plugin's own model is the inverse of ours

In `ai-literacy-superpowers`, `AGENTS.md` means *compound learning* (style, gotchas,
decisions) and `CLAUDE.md` holds the conventions. `/extract-conventions` duly wrote the
substance into `CLAUDE.md` and `HARNESS.md`. We relocated it to `AGENTS.md` and left
pointers, per the standing convention. Both commits are on the branch, in order, so the
relocation is visible rather than hidden.

### The Level 2 commands require a Level 3 artefact

`/extract-conventions` and `/convention-sync` both read `HARNESS.md` and refuse without
it. So a constraint-free `HARNESS.md` exists at this level: stack facts, conventions
pointing at `AGENTS.md`, and an explicit statement that there are no constraints. Level 3
adds constraints to a file that is already there.

Worth naming on stage if asked: the framework assumes enforcement early, and we
deliberately did not take it.

### `/convention-sync` wrote a pointer, not a copy

Because `HARNESS.md`'s conventions section is itself a pointer,
`.github/copilot-instructions.md` came out as a pointer — so the single-source claim
holds without needing a "generated file" disclaimer.

It also flagged a real limitation unprompted: Copilot's coding agent reads `AGENTS.md`
itself, but Copilot Chat in some IDEs reads only `copilot-instructions.md` and will not
follow the link. There, Copilot sees the stack and none of the rules.

### The agent escalates if you answer vaguely

During extraction, a vague "carry on" was taken as consent: it committed, then launched
`/harness-audit` — a Level 3 command — and spawned subagents running the full test suite.
Answer crisply on stage, and say "no, stop there" when it offers to go further.

### The plugin writes into the working tree

A `PostToolUse` hook records `observability/affordance-invocations.json` in any Level 2
session. Untracked, excluded from every commit here. 17 hooks register from the plugin —
worth knowing, since the level is meant to have no enforcement.
