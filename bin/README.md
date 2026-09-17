# bin/demo — the demo assistant

Deterministic. No model in the loop, so it cannot improvise on stage.

```sh
./bin/demo profiles           # which profile is which, and are they logged in
./bin/demo preflight 3        # is this machine ready? --fix repairs what is safe
./bin/demo run 3              # teleprompt the segment, beat by beat (levels 1-3)
./bin/demo fallback 3         # abandon a wandering live run
```

## Teleprompter keys

`Enter` next · `r` repeat · `s` skip · `f` fallback · `q` quit

Each beat prints what to say, then its command. `MODE: run` executes it for you in the
repo. `MODE: copy` puts it on the clipboard to paste into the agent session — that is
how the frozen prompt is delivered, so it is never retyped. `MODE: manual` you type.

## Editing the beats

`steps-level-<n>.txt`. Keys: `### title`, `TIME:`, `SAY:` (repeatable), `CMD:`, `MODE:`.
No shell syntax, no escaping rules. Change the words without touching the script.

## What preflight checks

Docker running · Claude Code version matches the one the notes were verified against ·
launcher present and logged in · rehearsal state cleared · (L2) the pinned plugin is
installed and loaded · (L1) the launcher has *no* plugin · correct branch, clean tree ·
the branch is not checked out in another worktree · fallback branches pushed · Maven
cache warm.

Level 3 additionally checks that git hooks are enabled in this clone, that both
enforcement scripts are executable, and that PR #1 is open and red — the merge gate is
demonstrated from that pre-opened pull request, never a live CI wait.

It cannot check terminal font size, cleared scrollback, or notifications. Those are on
you.

Levels 2 and 3 share one launch profile: from Level 3 on, what changes is in the
repository, not the launcher.

## The agent-driven variant

`.claude/skills/demo-assistant/SKILL.md` wraps this script for a Claude Code session
opened **in this directory** — a second terminal, your everyday profile, never the demo
session. It reads the run sheets and the findings so it can answer "what do I say here?"
and "is that number right?" mid-demo. It defers to this script for anything mechanical.
