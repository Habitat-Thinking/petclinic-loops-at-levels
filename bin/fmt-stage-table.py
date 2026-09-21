#!/usr/bin/env python3
"""Render PIPELINE.md's stage table narrow enough for a projector.

Reads the raw markdown table on stdin, writes an aligned plain-text table.

Why this exists. The raw table is up to 133 columns wide, and the Enforcement
column — the one the beat tells you to point at — is the rightmost, so it is
the first thing to fold onto the next line. A wrapped table is not a smaller
table; it is an unreadable one, and it fails exactly where the demo needs it
sharp. Found by timing the segment's commands, not by reading them.

Two columns are dropped rather than shrunk: `Produces`, whose long file paths
are what push the width out and which the beat never refers to, and the
workflow filename trailing `deterministic`, which is said aloud instead.

Padding is by character count, not bytes: the table is full of em-dashes and a
prime (`1b′`), and %-Ns in awk or printf pads by bytes, which silently
misaligns every row containing one.
"""

import re
import sys

rows = []
for n, line in enumerate(sys.stdin):
    if n == 1:  # the |---|---| separator
        continue
    cells = [c.strip().replace("**", "") for c in line.strip().strip("|").split("|")]
    if len(cells) < 5:
        continue
    num, stage, _produces, gate, enforcement = cells[:5]
    rows.append([num, stage, gate, re.sub(r" — `.*", "", enforcement)])

if not rows:
    sys.exit("fmt-stage-table: no table rows on stdin")

widths = [max(len(r[i]) for r in rows) for i in range(4)]
for r in rows:
    print("  ".join(r[i].ljust(widths[i]) for i in range(4)).rstrip())
