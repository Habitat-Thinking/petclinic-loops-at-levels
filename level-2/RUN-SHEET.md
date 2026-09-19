# Run sheet — Level 2: Commanding (8–10 minutes)

The segment that proves the talk's claim. Same model, same prompt, different habitat.

## Profiles — do not mix these up

| Level | Config dir | Launcher | What's in it |
|-------|-----------|----------|--------------|
| 1 | `~/.claude-loops-at-levels` | `~/.claude-loops-at-levels/launch` | Clean room, **no plugin** |
| 2 | `~/.claude-loops-at-levels-l2` | `~/.claude-loops-at-levels-l2/launch` | Clean room **+ ai-literacy-superpowers 0.92.0** |

Both scrub the environment identically and pin `claude-opus-5`, `--effort high`,
`--permission-mode auto`. The Level 2 launcher differs by exactly one flag:
`--plugin-dir …/ai-literacy-superpowers/0.92.0`. Each profile has its own login.

The plugin version is pinned deliberately at 0.92.0 (2026-09-17). It is the first
release where the plugin's edit hook is a script rather than a model call. Under 0.91.0
that hook claimed HARNESS.md did not exist, and in a repository without one it could end
the turn mid-task (#615, fixed in #616). Do not "update" again before the talk without
re-running the Level 2 and Level 3 smoke checks.

## Before you walk on

- [ ] Docker running.
- [ ] Repo on `level-2-commanding`, tree clean.
- [ ] Both profiles logged in: `claude auth status` with each `CLAUDE_CONFIG_DIR`.
- [ ] `rm -rf ~/.claude-loops-at-levels-l2/projects` to clear rehearsal state.
- [ ] Diff commands in shell history (below). Never typed live.
- [ ] Both `-after` branches pushed.
- [ ] Your extraction answers to hand (`extract-conventions-exchange.md`) — read them if
      the room makes you improvise.

## Beats

### 1. Show the gap, not the fix (10 s)

```sh
git show level-1-dictating-after --stat | head
```

Name one specific miss: at Level 1 the decision on whether email is required was made
alone, differently on different runs, and reported only in chat.

**If asked which run this is:** `level-1-dictating-after` holds **run 3 of 3** — the
typical one (email optional, 118 s, $0.76, 78 tests). Runs 1 and 2 are on `demo-notes`
under `level-1/runs/`. Run 1 is the outlier that made email *required* and cost $1.85.
Say so plainly if the 3× spread came up in the Level 1 segment: you are comparing
against the median run, not the flattering one.

### 2. Run `/extract-conventions` live (2–3 min)

```sh
cd ~/code/Habitat-Thinking/petclinic-loops-at-levels
~/.claude-loops-at-levels-l2/launch
```

Then `/extract-conventions`. Five questions, one at a time, each with a follow-up and a
categorisation to confirm.

- **Rehearsed agent time: 187 s across 14 turns.** Add your own talking time.
- **Cut point:** if you're past ~3 minutes at question 3, say "I've answered the rest of
  these already" and switch to the prepared `AGENTS.md`. The audience has the point by
  then: the questions are about *their* project, not about AI.
- **Answer crisply.** In rehearsal a vague "carry on" made it commit and then launch
  `/harness-audit` — a Level 3 command — on its own initiative. If it offers to go
  further, say **"no, stop there"**.

### 3. Open the files (20 s)

```sh
cat AGENTS.md | head -30      # the substance
cat CLAUDE.md                 # 7 lines, imports AGENTS.md
cat .github/copilot-instructions.md   # points at the same source
```

The tool-independence argument, made without a slide.

### 3b. Aside — the pointer that did nothing (45 s, optional but strong)

Use it if the room is technical, or hold it for Level 3.

`CLAUDE.md` started as a plain link to `AGENTS.md`. A fresh session then reported
**zero directives** — it knew the filename and nothing else. Claude Code auto-loads
`CLAUDE.md` only. The habitat looked complete on disk and was doing nothing; the Level 2
run would have behaved exactly like Level 1 and this entire comparison would have been
worthless.

The fix is the `@AGENTS.md` import line they just saw in `CLAUDE.md`.

**The point:** a pointer file a tool ignores is indistinguishable from one that works,
right up until the demo. You cannot verify a habitat by looking at it. Evidence:
[agents-md-not-loaded-audit.txt](agents-md-not-loaded-audit.txt).

### 4. Re-run the identical prompt (2 min)

Fresh launch in the Level 2 profile, paste the frozen prompt from
`../level-1/PROMPT.txt`. Say the words while it runs: same model, same prompt, same
machine — the only change is what the repository says about itself.

**Rehearsed: 120 s, 10 turns, $1.06, 78 tests green.**

### 5. Diff the two outputs (the moment)

```sh
git diff level-1-dictating-after..level-2-commanding-after -- src/
```

9 files, 29 insertions, 28 deletions. Then stop talking for a beat.

What to point at:
- Email kept **out of `Owner.toString()`** because it is personal data (directive 6) —
  Level 1 put it in.
- `MySqlIntegrationTests` and `PostgresIntegrationTests` extended so the new column is
  proved on both engines (directive 9).
- Six numbered **Decisions** surfaced (directive 12) — where Level 1 decided silently.

And the habitat that caused it:

```sh
git diff level-1-dictating..level-2-commanding --stat
```

6 files, 190 insertions, **zero source files**.

### 5b. What it cost (20 s)

Someone will ask, and it is better volunteered than extracted.

- **$0.76 at Level 1, $1.06 at Level 2** — about 40% more, for the larger prompt.
- **Time did not move:** 118 s against 120 s.
- Compare with Level 1's own spread for the *same* prompt: **$0.61 to $1.85**.

**The point:** habitat is not free and it is not slow. It costs less than the variance
it removes. You are buying determinism, not speed.

### 6. Then show the drift (1 min) — while the win is warm

The decisions went into the chat. There is no PR. **Nothing in the working tree records
them.** Close the terminal and the reasoning is gone — the Level 1 failure, wearing a
compliance badge.

Second, if time: it knowingly shipped an inconsistency (`VARCHAR(255)` in the schema, no
`@Size` on the entity, so an over-length email is a database error rather than a form
error). It surfaced it as Decision 2 instead of fixing it. **Advice can note a defect. It
cannot stop one.**

### 7. `/reflect` on the drift (30 s)

Already captured: `REFLECTION_LOG.md` and
`reflections/active/2026-09-16-add-email-field-to-owner.md`.

End on the entry sitting in the log. It already proposes the two checks that become
constraints at Level 3. Say: *we will come back for this.*

## Diff commands, for history

```sh
git diff level-1-dictating..level-2-commanding --stat                 # habitat only
git diff level-1-dictating-after..level-2-commanding-after -- src/    # output only
git show level-2-commanding-after --stat
```

The `-- src/` restriction is what keeps the output comparison honest: habitat files live
outside `src/`, so they can never flatter the source diff.

## If the live run wanders

```sh
git switch level-2-commanding-after
```

Unedited, builds green, 78 tests.

**If the live run is *better* than the rehearsed one:** diff what actually happened and
narrate the difference. Do not show a prepared diff that no longer matches the screen.

## Known wrinkle

The plugin's hooks write `observability/affordance-invocations.json` into the working
tree during any Level 2 session. It is untracked and harmless. If someone spots it, it is
an honest preview of Level 3: the plugin is already recording what tools were invoked.

## Slide decks for this segment

- [slides/BEFORE-L2.md](slides/BEFORE-L2.md) — before the demo.
- [slides/AFTER-L2.md](slides/AFTER-L2.md) — after it.

Slide plans for Claude Design: takeaway, content, visual and speaker note per slide,
with deck-level design direction at the top.
