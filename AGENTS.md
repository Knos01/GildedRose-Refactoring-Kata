# GildedRose kata — agent guide

Refactoring kata submitted to PTV Logistics. Solution language: **Kotlin** (`Kotlin/`).
All other language folders in this repo are upstream material and are not part of the submission.

This file is the instruction set I work under when using an AI assistant on this repository.
It was written before the first code commit and did not change to fit the result.

## Purpose and source-of-truth split

- **The existing implementation owns behavioural truth.** Where `GildedRoseRequirements.md` and
  `Kotlin/src/main/kotlin/com/gildedrose/GildedRose.kt` disagree, or where the requirements admit
  more than one reading, the code wins. The requirements are context, not specification.
- **This file owns how the assistant may be used.** `AI-LOG.md` owns what it actually produced.
- **The git history owns the order of work.** It is part of the deliverable and is never rewritten.
- **`Kotlin/README.md` owns the public decisions**, each as "X rather than Y because Z".

## Stack and commands

Kotlin on the JVM, Gradle wrapper, JUnit 5. Run everything from `Kotlin/`.

| Task | Command |
| --- | --- |
| Tests | `./gradlew test` |
| One test class | `./gradlew test --tests '*GoldenMaster*'` |
| Force re-run (defeats up-to-date checks) | `./gradlew test --rerun-tasks` |
| Clean | `./gradlew clean` |
| Failure detail | open `build/reports/tests/test/index.html` |

## Hard constraints

- **`Item.kt` is not modified.** The kata reserves it. Wrap it, never change it.
- **No new dependencies.** JUnit 5 and the Kotlin standard library are enough.
- **No behaviour change outside the `Conjured` commit.** Every refactoring commit leaves the
  golden master byte-identical.
- **No `--force`, no squash, no interactive rebase.** A commit that records a wrong turn is worth
  more than a tidy history that hides it.
- **Production code is not touched before a characterization test exists.**
- **The assistant never commits.** It may read files and, when I ask, edit them; it does not run
  `git commit`, `push`, `reset`, `rebase` or `stash`. Every commit is mine, made after I have
  reviewed the change.

## Division of work

The assistant does not choose the design. Where a design question arises, I state my decision
first and ask the assistant to attack it, rather than asking it to produce one.

- **Assistant may produce:** test harness and scaffolding, test data permutations, lists of
  mutations to try, idiomatic-Kotlin review of code I wrote, README structure, and corrections to
  the text of `AGENTS.md` and `AI-LOG.md` when I ask for them.
- **Assistant may not produce:** the structure of the refactoring, the implementation of
  `Conjured`, the resolution of an ambiguity in the requirements, or any `AI-LOG.md` verdict.

## Assistant modes

Modes are task-scoped, not standing roles. Each exists because of a property of this specific
kata, and each has its own done condition.

**Characterization.** Pins current behaviour before any production code moves.
Behaviour comes from the code, never from the requirements text. Must not edit production code.
Output must state which branch each case exercises and which branches remain uncovered.
*Done when:* changing one character of the snapshot fails the test, and a hand-applied mutation
fails the lines predicted for it.

**Adversarial review.** Entered only after I have stated a decision. Does not propose an
alternative design, does not comment on style. Looks for the input or state where my decision
breaks.
*Done when:* a concrete failing case is produced, or an explicit "none found" naming what was
checked.

**Kotlin idiom.** Review of language only: naming, scope functions, `when` exhaustiveness,
nullability, visibility. Must not restructure, rename abstractions, or propose a different
pattern. Exists because Kotlin is not my day-to-day language; TypeScript/Node is.
*Done when:* every remark is a language remark, and behaviour is provably unchanged.

One mode was considered and deliberately left out. **Performance:** this kata has no
performance dimension — no volume, no latency budget, no data structure under load — so a
performance mode is not needed.

## Commit and log discipline

- One idea per commit. A non-trivial commit message carries a second line in the form
  "X rather than Y because Z".
- Assistant output and my own design decisions never share a commit.
- Every assistant output that reaches the repository gets an `AI-LOG.md` entry, written at the
  time, committed separately as `docs(ai-log): entry NN — <topic>` immediately after the commit
  it refers to.
- The log records rejections and modifications, not only what was kept.

## Verification and handoff

An assistant-produced change is done only when all of the following hold:

- it was checked mechanically — line numbers compared against the file, test run, snapshot
  diffed, or a mutation applied by hand. "It reads correctly" is not a check;
- `./gradlew test` is green from a clean checkout;
- the `AI-LOG.md` entry carries a verdict and the check that produced it;
- I can explain the change without the assistant present.

## Stop conditions

Stop instead of guessing when: the requirements and the code disagree and the code's intent is
unclear; a refactoring step would change the golden master; a change would touch `Item.kt`; or a
suggestion cannot be verified by running something.

## Conflict rule

Where this file conflicts with a general-purpose assistant instruction, this file wins.
Where it conflicts with `GildedRoseRequirements.md` on behaviour, the existing code wins.
