# AI-LOG

How I used AI on this kata, what I kept, and what I threw away.

**Solution language:** Kotlin (`Kotlin/`). Not my day-to-day language — I work in TypeScript/Node —
chosen deliberately to match the stack you described.
**Assistant used:** Claude Opus 5.5 (Anthropic), in Claude Cowork on the desktop, with a shell on my machine
that can read and edit files in this repository. The assistant never commits: every commit in the
history is mine, made after I have reviewed the change.
**Transcript:** all the work happens in one AI session; its full transcript is
[`ai-sessions/session.md`](ai-sessions/session.md). Each entry below summarises one part of it, and
this log does not replace it.
**Agent configuration:** [`AGENTS.md`](AGENTS.md), in the repository root. It was written before
the first code commit and sets the constraints, the division of work, and the three task-scoped
modes the assistant is used in.

## Rules I set for myself before writing any code

1. **The assistant does not choose the design.** The refactoring structure, and the reasoning
   behind it, are mine. On a design question I state my decision first and ask the assistant to
   attack it, rather than asking it to produce one.
2. **The existing implementation is the source of truth** for anything the requirements leave
   ambiguous. Where the assistant "fixed" a behaviour by reading the requirements text instead of
   the code, the output was rejected.
3. **`Item` is not modified.**
4. **No assistant output reaches a commit before I have checked it mechanically** — line numbers
   compared against the file, test run, snapshot diffed, or a mutation applied by hand. "It looked
   right" is not a check.
5. **The log is written as I go**, and each entry is committed separately, immediately after the
   commit it refers to.

## Summary

| # | Purpose | Verdict | Commit |
|---|---------|---------|--------|
| 00 | Session start: constraints restated before any code | Confirmed | none |
| 01 | Golden master scaffolding over the upstream 30-day output | Accepted | `0ea38e4` |

---

## 00 — Session start: constraints restated before any code

**When:** 9 October 2026, before the first commit.
**Transcript:** [`ai-sessions/session.md`](ai-sessions/session.md), from the opening message.
**Context:** the approach was settled and `AGENTS.md` drafted in a planning session held before
this one. This log starts with the first session run under `AGENTS.md`.
**Why I asked:** before touching anything, I wanted the assistant to read `AGENTS.md`,
`GildedRose.kt` and the requirements and tell me how it understood the constraints, so that a
misreading would surface before it could reach the code.

**What I asked for:** read those three files and restate the constraints; follow `AGENTS.md` for the
rest of the conversation and tell me when I ask for something it forbids. Four working rules on
top: I state my decision first and the assistant attacks it; every output comes with a way to check
it mechanically; it does not write the refactoring structure or the `Conjured` implementation; it
reminds me if I move on without logging an output here.

**What the assistant contributed:** the constraints restated in 12 points with `AGENTS.md` line
references; five open points left to me (red baseline test, `AGENTS.md` untracked, `Items` property
also reserved, golden master scope undefined, `Conjured` matching rule); seven predicted
code-vs-requirements divergences with `GildedRose.kt` line references. Read-only, nothing edited.

**How I checked it:** every cited line compared against `AGENTS.md`, `GildedRose.kt`,
`GildedRoseTest.kt:13` and `GildedRoseRequirements.md:31`; `git status --short` unchanged. All
matched. The divergences are left to the characterization test.

**Verdict:** Confirmed
**What I changed:** nothing.

**Resulting commit:** none.

---

## 01 — Golden master scaffolding over the upstream 30-day output

**When:** 9 October 2026, before any production change.
**Transcript:** [`ai-sessions/session.md`](ai-sessions/session.md), from the golden master request.

**What I asked for:** test scaffolding for my decision: a byte-for-byte copy of
`texttests/ThirtyDays/stdout.gr` as the approved file, days 0–30, received output to `build/` only.
Plus the ways the test could pass falsely or fail spuriously, and the exact lines that fail if line
18 of `GildedRose.kt` becomes `sellIn < 10`.

**What the assistant contributed:**

- **Corrected my motivation.** I chose the upstream copy "because a snapshot of the code would also
  approve its bugs"; `stdout.gr` comes from the same logic and approves the same bugs. Its real
  value is that the code under test did not generate it.
- `ThirtyDays.approved.txt` and `GoldenMasterTest.kt`, plus the risk list and the mutation
  prediction (10 lines, 81–189).
- **Deviation from my spec:** the file ends in `\n\n`, so joining lines with `'\n'` falls one byte
  short; every line is terminated with `'\n'` instead (11,337 bytes).
- It could not run the test: Gradle download blocked (403) in its shells. All runs below are mine.

**How I checked it:**

1. Untouched code: green.
2. One character changed in the approved file: red.
3. Line 18 mutated to `sellIn < 10`: red on exactly the 10 predicted lines.
4. Quality clamped to 0–50 for non-Sulfuras items: still green, as predicted. Declared gap: the
   fixture never feeds out-of-range inputs.

**Verdict:** Accepted
**What I changed:** my motivation; accepted the `'\n'` deviation.

**Resulting commit:** `0ea38e4`.

---
