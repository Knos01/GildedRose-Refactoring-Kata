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
**Verdicts:** Accepted = kept as produced · Modified = kept after my changes · Confirmed = a review
or reading whose findings I verified · Rejected = discarded.

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
| 02 | Characterization tests for cases the golden master misses | Modified | `b115799` |
| 03 | Adversarial review of the first three refactoring commits | Confirmed | none |
| 04 | Conjured rule attacked, 14 pending tests | Modified | `f9f6745` |

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

## 02 — Characterization tests for cases the golden master misses

**When:** 10 October 2026, before any production change.
**Transcript:** [`ai-sessions/session.md`](ai-sessions/session.md), from the characterization request.

**What I asked for:** `CharacterizationTest.kt` pinning current behaviour, quirks preserved, for
13 cases I chose (code quirks, out-of-range inputs, paths missing from the golden master, near-miss
names). Plus 6 single mutations of `GildedRose.kt` with the tests each must fail, and whether the
`coerceIn(0, 50)` clamp from entry 01 is now caught.

**What the assistant contributed:**

- The test file and, for each test, the `GildedRose.kt` path that produces its expected value.
- Three objections:
  1. My reason cites a brief that is not in the repository. **Rejected:** the sentence is in PTV's
     email, not in the repo.
  2. The Brie-at-49 case was labelled as a path the golden master loses at the `Conjured` commit;
     the golden master never reaches it. **Accepted:** label corrected.
  3. The near-miss names were only tested before the sell-by date. **Accepted:** 3 past-sell-by
     cases added (16 tests in total).
- Its prediction for M1 was wrong, and it flagged the error itself.

**How I checked it:**

1. Baseline: green, 18 tests (16 characterization, 1 golden master, 1 original).
2. The entry 01 clamp now fails 4 tests (the out-of-range non-Sulfuras ones); in entry 01 it
   passed.
3. M1–M6: all killed as predicted (suggested by AI, could be found in the transcript).
4. Line 41 inverted (`startsWith("Sulfuras")` without `!`): caught by the past-sell-by tests and by
   the golden master.

**Verdict:** Modified
**What I changed:** rejected objection 1; accepted 2 and 3, which changed the file.

**Resulting commit:** `b115799`.

---

## 03 — Adversarial review of the first three refactoring commits

**When:** 10 October 2026, after `e0fe293`, `043c97f` and `df7f230`.
**Transcript:** [`ai-sessions/session.md`](ai-sessions/session.md), from the review of my three
refactoring decisions.

**What I asked for:** attack three decisions of mine: the name read only in `categoryOf`, mapped to
an enum and dispatched through an exhaustive `when`; guarded increase/decrease helpers;
duplicate-then-simplify instead of rewriting from the requirements. For each, the strongest
alternative and one concrete case where my choice costs more. Also how `Conjured` fits under
exact-name, prefix and contains matching, including the pinned `Conjured Aged Brie, 5, 10 -> 4, 9`
and the branch order a prefix rule needs. Evidence as a diff or a test; the `Conjured` rule not
decided.

**What the assistant contributed:**

- **Flagged a conflict with `AGENTS.md` [65–66]:** adversarial mode does not propose alternatives.
  It kept each alternative to one named line, with no code and no recommendation.
- **Behaviour: no objection.** 16,380 states (9 names × 20 `sellIn` × 91 quality, `Int` extremes
  included) run through transcriptions of `043c97f` and `df7f230` against the original: 0
  differences.
- **Costs of my choices:**
  1. A single enum cannot express `Conjured` as a modifier ("Brie, conjured"); the exhaustive
     `when` does not protect name → category, since `else -> NORMAL` absorbs a misspelled name.
  2. Helpers: no case found in the requirements where they cost more.
  3. Duplicate-then-simplify gives nothing for `Conjured`, which has no legacy body.
- **`Conjured` matching:** the golden master cannot tell the three rules apart; only the pinned
  `Conjured Aged Brie` test separates exact from prefix/contains, and no test separates prefix from
  contains. A prefix rule needs a `when` without subject, exact names first. No rule chosen.
- **Four problems in the record:**
  1. No test run on disk after `e0fe293`.
  2. None of the three commits has the "X rather than Y because Z" line ([82–83]).
  3. `043c97f` says "four copies of the legacy body" but the copies are already pruned: the
     duplicate step is not visible in the history.
  4. `df7f230` holds four ideas (helpers, `sellIn` decrement moved, `< 11`/`< 6` rewritten as
     `<= 10`/`<= 5`, constants), against [82].

**How I checked it:**

- `git log -3 --format='%h [%b]'`: empty bodies on all three commits.
- `git show 043c97f:…/GildedRose.kt | grep -n 'Category\.'`: matches only the mapping and the
  dispatch, so the copies were pruned.
- `git show df7f230`: the four changes are in the same diff.
- Test results now on disk: a run on `df7f230` (compiled class contains `increaseQuality`), 18
  tests, 0 failures.
- The 16,380-state comparison is on transcriptions, not on the Kotlin; the Gradle run on `df7f230`
  is the real check.

**Verdict:** Confirmed
**What I changed:** nothing in the code. The history cannot be rewritten: the four problems stay
recorded here.

**Resulting commit:** none.

---

## 04 — Conjured rule attacked, 14 pending tests

**When:** 10 October 2026, after `a46419b`, before the `Conjured` commit.
**Transcript:** [`ai-sessions/session.md`](ai-sessions/session.md), from my `Conjured` decision.

**What I asked for:** attack my rule (`Conjured` is a category that overrides the base item's
rules, recognised by a case-sensitive prefix checked after the exact special names, −2 / −4 past
sell-by, same guarded floor). Then JUnit tests for my 11-row table, marked
`@Disabled("pending Conjured")`, with each expected value derived from the rule and its arithmetic
shown, and the exact golden-master lines that change. Then rows 12–14.

**What the assistant contributed:**

- `ConjuredTest.kt`: 14 tests, all disabled; all rows agree with the rule.
- Golden master: exactly 4 lines change (24, 36, 48, 60, Conjured Mana Cake days 1–4).
- Attack:
  1. A conjured backstage pass keeps value after the concert (`0, 20 → -1, 16`, plain pass
     `-1, 0`). **Rejected:** `Conjured` overrides every base rule by assumption; pinned as rows
     12–13.
  2. Rows 5, 6, 9 produce quality outside 0–50, and for `Conjured` no legacy code justifies it.
     I answered that it never happens; it replied that row 9 is a realistic input (Sulfuras at 80)
     and stays above 50 for 9 updates. **Open.**
  3. The prefix has no word boundary (`ConjuredX`). **Accepted:** row 14, prefix becomes
     `"Conjured "`.
  4. Branch order is untested: no special name starts with `Conjured`.
  5. The pinned `Conjured Aged Brie, 5, 10 → 4, 9` contradicts row 7; it changes to `4, 8` in the
     `Conjured` commit.
- **Refused my request** to drop the `@Disabled` markers so the tests could go in my `Conjured`
  commit: `AGENTS.md` [84]. It proposed two commits that keep main green, since skipped tests do
  not fail.
- A mutation list for the tests once enabled.

**How I checked it:**

- `grep -c '@Disabled("pending Conjured")' ConjuredTest.kt`: 14.
- Local run with my implementation in the working tree: `ConjuredTest` 14 skipped; only the golden
  master and the pinned `Conjured Aged Brie` test fail, as expected before the `Conjured` commit.
- The arithmetic of the 14 rows redone against the rule.

**Verdict:** Modified
**What I changed:** rejected objection 1 and pinned it (rows 12–13); accepted 3 (row 14, prefix
`"Conjured "`); two commits instead of one, as `AGENTS.md` requires. Objection 2 is still open.

**Resulting commit:** `f9f6745`

---
