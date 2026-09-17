#!/bin/sh
# Edit-time loop: advisory, fast, never blocks.
#
# Runs after the agent writes a file, so a violation is named while the change is still
# in hand rather than at merge. It reports and exits 0 — telling you early is the whole
# job; stopping you is the merge gate's job.

set -u
out=""

# Decision record: compare the working tree against HEAD, since nothing is staged yet.
changed=$(git diff --name-only HEAD 2>/dev/null; git ls-files --others --exclude-standard 2>/dev/null)
if printf '%s\n' "$changed" | grep -q '^src/'; then
  if ! printf '%s\n' "$changed" | grep -q '^decisions/.*\.md$'; then
    out="${out}  HARNESS advisory: src/ has changed with no record under decisions/.
    Blocking at merge. Add decisions/<yyyy-mm-dd>-<slug>.md before you push.
"
  fi
fi

# Schema parity: only worth running when a schema file moved.
if printf '%s\n' "$changed" | grep -q '^src/main/resources/db/.*schema\.sql$'; then
  if ! ./mvnw -B -q test -Dtest=SchemaParityTest -DfailIfNoTests=false >/dev/null 2>&1; then
    out="${out}  HARNESS advisory: SchemaParityTest fails — a column is missing from one
    of the three schemas, or h2 and mysql disagree on a length. Run:
      ./mvnw test -Dtest=SchemaParityTest
"
  fi
fi

[ -n "$out" ] && printf '\n%s\n' "$out"
exit 0
