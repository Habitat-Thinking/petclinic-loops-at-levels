# Run sheet — Level 1: Dictating (3 minutes)

## Before you walk on

- [ ] Docker Desktop running (`docker info` returns). Without it the demo still works, but
      2 tests skip and the "78 tests" claim becomes 76.
- [ ] Maven cache warm: run `./mvnw -B verify` once on `level-0-baseline` (9–21 s warm,
      72–134 s cold).
- [ ] Repo on `level-1-dictating`, tree clean: `git switch level-1-dictating && git status`
- [ ] Clean profile logged in: `env -i HOME="$HOME" PATH="$PATH" CLAUDE_CONFIG_DIR="$HOME/.claude-loops-at-levels" claude auth status`
      should say `"loggedIn": true`. If not, see [CLEAN-ROOM.md](CLEAN-ROOM.md).
- [ ] Wipe any state from rehearsal runs: `rm -rf ~/.claude-loops-at-levels/projects`
- [ ] Terminal font large. `git diff` pager set, or pipe to `cat`.

## The incantation

```sh
cd ~/code/Habitat-Thinking/petclinic-loops-at-levels
~/.claude-loops-at-levels/launch
```

Say what it is while it starts: a Claude Code with no memory file, no plugins, no MCP
servers, no project context. Nothing but the model and the repository.

## The prompt — paste verbatim, do not improvise

```
Add an email address to owners. It should be shown on the owner details page and be editable when creating or editing an owner.
```

This exact wording is reused at every level. Changing a word breaks the comparison.

## What to expect

| | Observed across 3 runs |
|---|---|
| Duration | **91 s – 294 s** (median 118 s) |
| Turns | 8 – 31 |
| Cost | $0.61 – $1.85 |
| Result | Green build every time, 77–78 tests |

**Plan for two minutes and be ready for five.** If it runs long, talk over it: the model is
re-deriving what your team already knows, and you are paying for that rediscovery.

## What to point at

1. **It did the work well.** All three schemas, seed data, real translations in 10 locale
   bundles, tests. Do not undersell this — the audience will not believe a rigged demo.
2. **Why it did so well:** `I18nPropertiesSyncTest` already fails the build on an
   unlocalised string. The repository taught it. Enforced context is the context that
   survives — and that is Level 3 arriving early.
3. **The decision it made alone.** Scroll to the final message: it chose whether email is
   required, decided by itself, and told you in chat. Runs 1 and 2 decided the *opposite
   way* from each other.
4. **Where that decision lives now.** Nowhere. Close the terminal and it's gone. The next
   run starts the argument again.
5. **The cost of re-deriving:** 3.2× time and 3.0× cost between two runs of the same prompt.

## If the live run wanders

```sh
git switch level-1-dictating-after
git show --stat
```

`level-1-dictating-after` holds run 3's output, committed unedited. It builds green
(78 tests). Use it and say plainly that it's a recording — the audience forgives a
fallback, not a fiction.

## For the Level 2 comparison

Restrict the diff to source, so the demo scaffolding never appears:

```sh
git diff level-1-dictating-after..level-2-commanding-after -- src/
```

`src/` is the only path Level 1 touched (21 files). Any Level 2 artefacts — `CLAUDE.md`
and friends — live outside `src/` and show up in the branch-to-branch diff instead:

```sh
git diff level-1-dictating..level-2-commanding
```

## After the talk

`rm -rf ~/code/Habitat-Thinking/l1-runs` — the three scratch clones used to capture the
runs. The evidence is already committed here.

## Slide decks for this segment

- [slides/BEFORE-L1.md](slides/BEFORE-L1.md) — 6 slides, run immediately before the demo:
  what Dictating is, the clean room, the frozen prompt, what to watch for.
- [slides/AFTER-L1.md](slides/AFTER-L1.md) — 7 slides, run immediately after: the three-run
  evidence, the `I18nPropertiesSyncTest` finding, the evaporated decision, the cost spread,
  and the handover to Level 2.

Both are slide *plans* — takeaway, content and speaker note per slide, with design
direction at the top — written to be handed to Claude Design to produce the deck.
