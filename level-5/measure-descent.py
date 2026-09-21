#!/usr/bin/env python3
"""Measure a pipeline run: tokens and list-price cost inside a time window.

Reads this session's transcript and every subagent transcript, filters to the
window, and prices at Opus 5 list rates. Nothing is estimated; every number
comes from a usage record written by the runtime.
"""
import json, glob, os, collections, datetime, sys

D = os.path.expanduser("~/.claude/projects/-Users-russellmiles-code")
S = "/private/tmp/claude-501/-Users-russellmiles-code/4682a30e-bdc7-416a-8b3a-7eecf3c73481/tasks"
SESSION = "4682a30e-bdc7-416a-8b3a-7eecf3c73481"

lo = datetime.datetime.fromisoformat(sys.argv[1])
hi = datetime.datetime.fromisoformat(sys.argv[2])


def ts(d):
    t = d.get("timestamp")
    if not t:
        return None
    try:
        return datetime.datetime.fromisoformat(str(t).replace("Z", "+00:00"))
    except Exception:
        return None


c = collections.Counter()
for f in [f"{D}/{SESSION}.jsonl"] + sorted(glob.glob(f"{S}/*.output")):
    for line in open(f, errors="ignore"):
        try:
            d = json.loads(line)
        except Exception:
            continue
        if not isinstance(d, dict):
            continue
        t = ts(d)
        if t is None or not (lo <= t <= hi):
            continue
        msg = d.get("message")
        u = (msg.get("usage") if isinstance(msg, dict) else None) or d.get("usage")
        if not isinstance(u, dict):
            continue
        for k in ("input_tokens", "output_tokens",
                  "cache_creation_input_tokens", "cache_read_input_tokens"):
            if isinstance(u.get(k), int):
                c[k] += u[k]
        cc = u.get("cache_creation")
        if isinstance(cc, dict):
            for k in ("ephemeral_5m_input_tokens", "ephemeral_1h_input_tokens"):
                if isinstance(cc.get(k), int):
                    c[k] += cc[k]

M = 1_000_000
IN, OUT, CR = 5.00, 25.00, 0.50        # Opus 5 list: $5 / $25 per MTok, cache read 0.1x
W5, W1H = 5.00 * 1.25, 5.00 * 2.00     # cache write: 1.25x (5m TTL), 2x (1h TTL)

w5, w1h = c["ephemeral_5m_input_tokens"], c["ephemeral_1h_input_tokens"]
unattr = c["cache_creation_input_tokens"] - (w5 + w1h)
write_cost = w5 / M * W5 + (w1h + unattr) / M * W1H
cost = (c["input_tokens"] / M * IN + c["output_tokens"] / M * OUT
        + write_cost + c["cache_read_input_tokens"] / M * CR)
total = sum(c[k] for k in ("input_tokens", "output_tokens",
                           "cache_creation_input_tokens", "cache_read_input_tokens"))

print(f"window      {lo:%H:%M} -> {hi:%H:%M} UTC   ({(hi - lo).total_seconds() / 60:.0f} min)")
print(f"input       {c['input_tokens']:>12,}   ${c['input_tokens'] / M * IN:>8,.2f}")
print(f"output      {c['output_tokens']:>12,}   ${c['output_tokens'] / M * OUT:>8,.2f}")
print(f"cache write {c['cache_creation_input_tokens']:>12,}   ${write_cost:>8,.2f}")
print(f"cache read  {c['cache_read_input_tokens']:>12,}   ${c['cache_read_input_tokens'] / M * CR:>8,.2f}")
print(f"TOTAL       {total:>12,}   ${cost:>8,.2f}")
