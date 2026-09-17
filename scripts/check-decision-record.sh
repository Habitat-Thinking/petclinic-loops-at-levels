#!/bin/sh
# Constraint: "Decision record for source changes" (HARNESS.md).
#
# Any change that touches src/ must add or update a record under decisions/ in the
# same change. Deterministic: this reads the changed file list and looks for a file.
# It does not read the record, and it has no opinion about what is in it. An agent is
# not involved and cannot talk it round.
#
#   check-decision-record.sh --staged        advisory, what is staged right now
#   check-decision-record.sh <base-ref>      blocking, everything since <base-ref>
#
# Exit 0 = satisfied or not applicable. Exit 1 = violated.

set -eu

case "${1:---staged}" in
  --staged)
    changed=$(git diff --cached --name-only)
    where="staged changes"
    ;;
  *)
    base=$1
    changed=$(git diff --name-only "$base"...HEAD)
    where="changes since $base"
    ;;
esac

if [ -z "$changed" ]; then
  echo "decision-record: no changes to check"
  exit 0
fi

src_changed=$(printf '%s\n' "$changed" | grep '^src/' || true)
if [ -z "$src_changed" ]; then
  echo "decision-record: no files under src/ changed — constraint does not apply"
  exit 0
fi

record_changed=$(printf '%s\n' "$changed" | grep '^decisions/.*\.md$' || true)
if [ -n "$record_changed" ]; then
  echo "decision-record: OK — $where include $(printf '%s\n' "$record_changed" | tr '\n' ' ')"
  exit 0
fi

src_count=$(printf '%s\n' "$src_changed" | wc -l | tr -d ' ')
cat >&2 <<EOF
decision-record: VIOLATED

  $src_count file(s) under src/ changed, and no record under decisions/ was added
  or updated in the same change.

  Changed under src/:
$(printf '%s\n' "$src_changed" | sed 's/^/    /')

  Every judgement call the task did not state — required vs optional, nullability,
  limits, defaults, naming, error handling — belongs in a record that outlives the
  session. If there genuinely were none, say so in the record explicitly.

  Add decisions/<yyyy-mm-dd>-<slug>.md and include it in this change.
EOF
exit 1
