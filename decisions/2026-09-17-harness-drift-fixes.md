# Fix the drift the first /harness-health run found

The harness was one day old and the instruments still found four mismatches. None of
it was manufactured: it is what a harness looks like after a day.

- **Decision**: correct the README badge from "2/2 enforced" to "5 enforced, 1 advisory".
  **Alternatives**: generate the badge from HARNESS.md so it cannot drift.
  **Why**: generating it is the right answer and it is not this level's work. A badge
  that lies about the harness is worse than no badge, so the count is corrected now and
  the generation left as known debt.

- **Decision**: run `SchemaParityTest` from the pre-commit hook, but only when a schema
  file is staged.
  **Alternatives**: run it on every commit; drop `commit` from the constraint's scope.
  **Why**: the constraint declared `commit, pr` scope and nothing ran it at commit —
  the rule was documentation, not enforcement. Running Maven on every commit would make
  the hook slow enough that people disable it, which costs more than it catches.

- **Decision**: list the scheduled sweep's three checks in HARNESS.md.
  **Alternatives**: leave CI as the source of truth for what the sweep does.
  **Why**: HARNESS.md claimed one GC rule that nothing ran, while CI ran three that
  HARNESS.md never mentioned. Either half alone gives a false account of the harness.

- **Decision**: raise the edit-time hook timeout from 20s to 120s.
  **Alternatives**: keep 20s and accept silent timeouts.
  **Why**: a cold Maven start exceeds 20s, and the hook would fail silently — the
  failure mode this whole level exists to argue against.
