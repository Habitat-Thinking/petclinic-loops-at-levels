# Decision records

One file per change that touches `src/`, named `<yyyy-mm-dd>-<slug>.md`.

It exists because of what happened at Level 2 of this repository's history: an agent was
told to surface every judgement call in "the PR description", did exactly that in its
final message, and the reasoning evaporated when the session closed. There was no PR.
Nothing in the repository remembered.

A directive could not fix that, because nothing checked where the decisions landed.
`HARNESS.md` → **Decision record for source changes** checks it now:
`scripts/check-decision-record.sh`, advisory at commit and blocking on a pull request.

The check is deliberately shallow. It asks whether a record is present, never whether it
is any good. Judging the content is a person's job, and pretending otherwise would make
the rule feel stronger than it is.

## Format

```markdown
# <what changed>

- **Decision**: <the choice made>
  **Alternatives**: <what else was considered>
  **Why**: <the reason, in a sentence>
```

If the change genuinely required no judgement calls, write that and say why.
