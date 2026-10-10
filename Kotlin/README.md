# Gilded Rose — Kotlin solution

Refactoring of the Gilded Rose kata in Kotlin, plus the new **Conjured** item category.
The brief says the existing code is functionally correct and that *"the existing implementation
should serve as your guide"* where the requirements can be read more than one way. Everything below
follows from that.

How the work was done with an AI assistant: [`AGENTS.md`](../AGENTS.md) (rules),
[`AI-LOG.md`](../AI-LOG.md) (what it produced, what I kept or rejected) and
[`ai-sessions/`](../ai-sessions/) (transcripts).

## Install

- A **JDK 8** that Gradle can find: the build pins a Java 8 toolchain (`build.gradle.kts`).
- Nothing else. The Gradle wrapper downloads Gradle 8.13, Kotlin 2.1.20 and JUnit 5 on first run.

All commands below run from this folder (`Kotlin/`).

## Commands

| What | Command |
| --- | --- |
| Run all tests | `./gradlew test` |
| Run all tests, ignoring Gradle's up-to-date cache | `./gradlew test --rerun-tasks` |
| Run one test class | `./gradlew test --tests '*GoldenMaster*'` |
| Print the 30-day text fixture | `./gradlew run --args 30` |
| Clean | `./gradlew clean` |
| Test report | `open build/reports/tests/test/index.html` |

## How to test

Four test classes, all plain JUnit 5, no extra dependencies:

| Class | What it protects |
| --- | --- |
| `GoldenMasterTest` | 31 days (0–30) of the kata fixture, compared byte for byte with `src/test/resources/ThirtyDays.approved.txt`. |
| `CharacterizationTest` | One-day cases the fixture never reaches: out-of-range input, near-miss names, legacy quirks. |
| `ConjuredTest` | The Conjured rule, one case per clause of the decision below. |
| `GildedRoseTest` | The kata's placeholder test, turned into a real assertion (quality floor on an expired item). |

**Golden master.** The approved file started as a byte-for-byte copy of the upstream
`texttests/ThirtyDays/stdout.gr`, so the first run checked the Kotlin code against an output it did
not generate. On a mismatch the test fails and writes the actual output to
`build/ThirtyDays.received.txt`; it never writes to `src/`. To accept a change on purpose:

```bash
diff src/test/resources/ThirtyDays.approved.txt build/ThirtyDays.received.txt   # read it first
cp build/ThirtyDays.received.txt src/test/resources/ThirtyDays.approved.txt
```

The approved file changed exactly once, in the Conjured commit (4 `Conjured Mana Cake` lines).
From that commit on it no longer matches upstream, which has no Conjured rule.

**Upstream TextTest.** The kata's external approval test (see the
[TextTest README](../texttests/README.md) and [config.gr](../texttests/config.gr)) is left as it was
upstream, but it is not part of this solution: `GoldenMasterTest` runs the same 30-day comparison
inside `./gradlew test`, with no Python or TextTest install. Its approved output,
`texttests/ThirtyDays/stdout.gr`, predates Conjured, so a TextTest run now reports the 4
`Conjured Mana Cake` lines as differences. That is expected.

**How the safety net was checked.** Before the refactoring, hand-applied mutations of
`GildedRose.kt` (boundary flips, a `coerceIn(0, 50)` clamp, widened name matching) each had to fail
the predicted tests. The results are in `AI-LOG.md`.

## Structure

```
src/main/kotlin/com/gildedrose/
  Item.kt            untouched (owned by the goblin)
  GildedRose.kt      categoryOf + one update function per category + guarded quality helpers
  TexttestFixture.kt untouched
src/test/kotlin/com/gildedrose/   the four test classes above
src/test/resources/               ThirtyDays.approved.txt
```

## Decisions

Each one as *X rather than Y, because Z*.

**Refactoring**

- **Characterization before refactoring**, rather than refactoring against the requirements text,
  because the brief says the code is the guide: the tests pin what the code does, quirks included.
- **The item name is read in one place only** (`categoryOf`, mapping it to an enum), rather than name
  checks spread through the logic, because `Item` exposes nothing but the name. If a name changes,
  one line changes. The `when` over the enum has no `else`, so a new category does not compile until
  it is handled.
- **Duplicate, then simplify**: each category first got an identical copy of the legacy body, then
  the checks that were always true or false for that category were removed, rather than rewriting
  each rule from the requirements, because every step stays mechanically checkable.
- **Guarded `increaseQuality` / `decreaseQuality`** (one step, only if below 50 / above 0), rather
  than `coerceIn(0, 50)`, because clamping would *correct* out-of-range input and changes 4 pinned
  behaviours (e.g. Aged Brie at 55 stays 55 today).
- **Backstage passes as a tiered increase** (3 / 2 / 1 for ≤ 5 / ≤ 10 / more days), rather than three
  overlapping `if`s, because the overlap made "+3" an implicit sum; `repeat` keeps each step under
  the 50 cap.
- **Aged Brie and normal items as an explicit rate** (1, or 2 once past the sell-by date), rather than
  a second conditional update, because it reads like the requirement and the quality and `sellIn`
  updates do not depend on each other.

**Conjured**

- **Conjured is its own category and overrides the base item's rules**, rather than a modifier on
  top of them, because a conjured item is a different magical object. So `Conjured Aged Brie` does
  not gain quality, `Conjured Sulfuras, Hand of Ragnaros` is sold and degrades, and a conjured
  backstage pass does not drop to 0 after the concert.
- **Twice as fast as a normal item**: −2 per day, −4 past the sell-by date, rather than a separate
  rate, because the requirement defines Conjured relative to normal items.
- **Recognised by the name prefix `"Conjured "`** (with the space, case-sensitive), rather than the
  exact name `Conjured Mana Cake`, because that is the naming of the only example given and it covers
  other conjured products from the same supplier. Exact special names are checked first.

## Assumptions

- Quality enters within 0–50 (80 for Sulfuras). The system **never moves a valid quality out of
  that range, but does not repair an invalid one**: a quality of 55 still degrades, −1 stays −1.
  This is the legacy behaviour for every category; for Conjured, which has no legacy code, it is a
  choice made for consistency.
- Conjured items are named `Conjured <product>`. A name that is exactly `Conjured`, has "Conjured"
  elsewhere, or uses another case is a normal item. If the supplier names them differently,
  `categoryOf` is the only place to change.
- Name matching is exact and case-sensitive, as in the original code: `Aged brie` or `Sulfuras` on
  its own is a normal item.

## Legacy behaviour kept on purpose

Pinned in `CharacterizationTest`, kept rather than fixed because the code is the guide:

- Aged Brie gains 2 per day once past the sell-by date.
- A backstage pass on concert day gains its +3, then drops to 0 in the same update.
- Quality above 50 or below 0 on input is not corrected.
- Sulfuras keeps whatever quality it has (it is not forced to 80).

## Known limits and next steps

- The rules still depend on the name strings; a real fix is a type or SKU from the supplier, out of
  reach while `Item` cannot change.
- The order "exact names, then the Conjured prefix" cannot be tested today: no special name starts
  with "Conjured".
- A randomized differential test against a frozen copy of the legacy code would add evidence beyond
  the hand-picked cases; not done for time.
