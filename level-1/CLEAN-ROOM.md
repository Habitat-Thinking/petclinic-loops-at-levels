# The clean room

Everyday Claude Code on this machine loads four plugins, a user settings file and
auto-mode environment context. Any of it would quietly supply the context Level 1 is
supposed to lack. This is the isolated profile that prevents that, and the evidence
that it works.

## The launch incantation

```sh
cd ~/code/Habitat-Thinking/petclinic-loops-at-levels
~/.claude-loops-at-levels/launch
```

That is the whole thing on stage. The script lives at `~/.claude-loops-at-levels/launch`
and a copy is in [launch](launch) in this directory. It is not in the repository, on any
level branch, deliberately.

What it does:

```sh
exec env -i \
  HOME="$HOME" LOGNAME="$LOGNAME" SHELL="$SHELL" PATH="$PATH" \
  TERM="${TERM:-xterm-256color}" LANG="${LANG:-en_GB.UTF-8}" TMPDIR="$TMPDIR" \
  COLORTERM="$COLORTERM" TERM_PROGRAM="$TERM_PROGRAM" \
  CLAUDE_CONFIG_DIR="$PROFILE" \
  CLAUDE_CODE_DISABLE_AUTO_MEMORY=1 \
  claude \
    --model claude-opus-5 --effort high --permission-mode auto \
    --setting-sources project,local \
    --settings "$PROFILE/pinned-settings.json" \
    --strict-mcp-config "$@"
```

### Why each piece is there

- `env -i`: starts from an empty environment. Without it, a terminal that already has
  `CLAUDE_*` variables set (any shell launched from inside Claude Code has them) leaks
  its own effort level and session identity into the run.
- **`USER` is deliberately not passed through.** This one is a trap. With a custom
  `CLAUDE_CONFIG_DIR`, a set `USER` — even an empty one — makes the keychain credential
  lookup fail and the session dies instantly with `Not logged in · Please run /login`.
  A normal terminal always sets `USER`, so without this the launcher fails on stage and
  not in rehearsal. Verified on Claude Code 2.1.272, macOS 26.6.2.
- `CLAUDE_CONFIG_DIR`: a dedicated profile, so no user `CLAUDE.md`, no installed plugins,
  no MCP servers and no session history from everyday work.
- `CLAUDE_CODE_DISABLE_AUTO_MEMORY=1`: without it, runs teach each other across takes and
  memory becomes an undeclared habitat. Belt and braces with `autoMemoryEnabled: false` in
  the pinned settings.
- `--setting-sources project,local`: excludes user-level settings. Project and local stay
  on, because from Level 2 onwards the repository's own config *is* the habitat.
- `--strict-mcp-config`: blocks the claude.ai account connectors (Gmail, Drive, Calendar),
  which otherwise arrive through the account rather than the config directory.
- `--model claude-opus-5 --effort high --permission-mode auto`: the pinned conditions.

### What must NOT be used

`--bare` and `--safe-mode` both look like the obvious "clean" flags and both are wrong
here. They disable `CLAUDE.md` discovery along with everything else, which would silence
the Level 2 habitat and make Level 2 look identical to Level 1. The isolation has to come
from an empty config directory, not from a flag that mutes the repository.

## First-time setup (already done)

```sh
env -i HOME="$HOME" PATH="$PATH" TERM="$TERM" \
  CLAUDE_CONFIG_DIR="$HOME/.claude-loops-at-levels" claude auth login
```

The clean profile has its own credential in the keychain
(`Claude Code-credentials-b0602542`), separate from the everyday one. It is the same
claude.ai account. If the profile is ever recreated, this login must be repeated, and
the first launch afterwards may need a keychain prompt answered.

## The verification

Run in the repository root, on `level-1-dictating`, asking the agent to enumerate every
source of instruction it loaded. The full reply is in
[clean-room-audit.txt](clean-room-audit.txt) and the startup debug log in
[clean-room-debug.log](clean-room-debug.log).

The agent reported: **no `CLAUDE.md`/`AGENTS.md` of any kind**, no MCP servers, no plugins
loaded, no user or project skills, five built-in subagents, and no project-specific
instruction about this repository.

The debug log independently confirms the runtime state, which matters more than the
agent's own account of itself:

```
Found 0 plugins (0 enabled, 0 disabled)
Total plugin skills loaded: 0 ... Total plugin agents loaded: 0
Registered 0 hooks from 0 plugins
[claudeai-mcp] Disabled: API-key auth precedence active
Watching for changes in setting files /Users/russellmiles/.claude-loops-at-levels/settings.json...
```

### Re-verified on 2026-09-19, Claude Code 2.1.275

Claude Code auto-updated twice in two days (2.1.273 → 2.1.274 → 2.1.275), so the clean
room was re-checked on the current build. It still holds: `Found 0 plugins (0 enabled,
0 disabled)`, no MCP servers, no `CLAUDE.md` or `AGENTS.md` seen at any depth, and
`claude-opus-5`. The Level 2 profile was checked in the same pass for the opposite
property — that its habitat still loads — and it reported all 18 directives, with
directive 6 quoted correctly.

**A first interactive launch after an upgrade can show a first-run screen** (the theme
picker appeared on one). A headless `-p` run never shows it, so rehearsal on its own
will not flush it out, and preflight cannot see it either. Launch each profile
interactively once after any upgrade, answer whatever it asks, and quit. Then the
launcher on stage goes straight to the prompt.

### The agent's self-report was wrong in one place

It listed `~/.claude/settings.json` as "in effect". It is not: `--setting-sources
project,local` excludes user settings, and the debug log shows only the clean profile's
own (non-existent) settings file being watched. What actually happened is that the agent
read the file off disk with `cat` and inferred it applied.

Worth keeping for the talk: **an agent asked to audit its own context got it wrong, in the
direction of over-claiming.** The check that held up was the debug log, not the
introspection. This is the difference between an agent's account of its habitat and the
habitat.

### One residual, accepted

The clean room isolates what is *loaded*, not what is *reachable*. The agent still runs as
you, so it can read `~/.claude/` if it goes looking, as it did here. Nothing from those
files enters its instructions, so Level 1 evidence is unaffected. Closing that gap would
need a container or a separate user account, which is not worth it for this talk.
