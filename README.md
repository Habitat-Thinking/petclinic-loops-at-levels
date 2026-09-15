# demo-notes

Orphan branch with no shared history with the ladder. It holds the scaffolding for the
talk "(Agentic) Loops At Levels, Live": run sheets, timings, prompts, fallback
instructions and the recorded toolchain.

It lives off the ladder on purpose. Anything committed to a level branch shows up in
the level-to-level diffs the audience sees.

- [TOOLCHAIN.md](TOOLCHAIN.md): the verified JDK, Maven and OS, plus build timings.
- [LADDER.md](LADDER.md): branch ladder, parentage rules and the `-after` sibling convention.

Work on this branch in its own worktree so the ladder checkout is never disturbed:

```sh
git worktree add ../petclinic-loops-at-levels-demo-notes demo-notes
```
