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

**What the assistant contributed:** a read-only pass (no file edited) over `AGENTS.md`,
`GildedRose.kt` and the requirements, plus `Item.kt`, the existing test, the text fixture,
`build.gradle.kts` and this log. It returned three things:

- **The constraints restated in 12 points**, each with the `AGENTS.md` line numbers it rests on:
  code over requirements, `Item.kt` frozen, no new dependencies (so the golden master is hand-written
  JUnit), behaviour changes only in the `Conjured` commit, characterization before any production
  edit, no history rewriting, no git writes by the assistant, the may/may-not split of work, the
  three modes and their done conditions, commit and log discipline, the definition of done, and
  `AGENTS.md` winning over general assistant instructions.
- **Five open points, left to me to decide:** the baseline suite is red (`GildedRoseTest.kt:13`
  asserts `"fixme"` on an item named `"foo"`), so "green from a clean checkout" cannot hold until
  that test changes, and the build pins a Java 8 toolchain; `AGENTS.md` is still untracked, so its
  "written before the first code commit" claim is only verifiable once it is committed ahead of any
  change to `Kotlin/src`; the requirements (line 31) also reserve the `Items` property, i.e.
  `GildedRose.items`, which `AGENTS.md` does not name; "golden master" is used but its scope
  (inputs, days, snapshot location) is undefined, and the upstream fixture misses some input cases
  (quality above 50, negative quality, the bare `"Sulfuras"` name); how a `Conjured` item is
  recognised (exact name, prefix, contains) and how it combines with the past-date rate and the
  zero floor is not settled by any code.
- **Seven predicted divergences between code and requirements**, each as input → output after one
  `updateQuality()` with the `GildedRose.kt` lines responsible: Brie past date gains 2; a backstage
  pass on its last day gains 3 then drops to 0 in the same update; the "10 days or less" step is
  tested on `sellIn` before the decrement; quality above 50 and below 0 on input is not corrected;
  Sulfuras quality is not forced to 80; plain `"Sulfuras"` is matched as a normal item. It also
  noted that the past-date check runs after the decrement, so the double rate starts on the update
  that takes `sellIn` from 0 to -1. These are predictions, to be confirmed or refuted by the first
  characterization run.

**How I checked it:** the 12 points compared one by one against the `AGENTS.md` lines they cite;
every line number in the divergence table compared against `GildedRose.kt`;
`GildedRoseTest.kt:13` and `GildedRoseRequirements.md:31` opened to confirm the two findings that
rest on them; `git status --short` still lists only `AGENTS.md`, `AI-LOG.md` and `ai-sessions/` as
untracked, so nothing was edited. All references matched. The divergence outputs themselves are
not verified by this check: the characterization test is what confirms them.

**Verdict:** Confirmed
**What I changed:** nothing. The five open points are design decisions and stay with me.

**Resulting commit:** none — nothing reached the repository.

---
