# Implementation Plan: deepdive-missing-snipers

## Goal Description
Add new rule snipers to `deepdive-code-hunt.md` to catch React Native specific rendering bottlenecks and brittle test patterns.

## Proposed Changes

### deepdive-code-hunt.md

#### [MODIFY] [deepdive-code-hunt.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-code-hunt.md)
1. **Update Vector Beta Table:** Increase the total Rule Snipers from 26 to 29.
2. **Add R-28 (FlatList Bottlenecks):**
   - What to Hunt: Inline arrow functions passed to `renderItem` or `keyExtractor` in `FlatList` components; missing `initialNumToRender` or `windowSize` on large lists.
3. **Add R-29 (Circular Dependencies):**
   - What to Hunt: Services importing other services that import them back (e.g. A -> B -> A), causing `undefined` module errors at runtime.
4. **Add R-30 (Zombie Tests):**
   - What to Hunt: Tests containing `.skip()`, `test.todo()`, or assertions that are commented out (`// expect(...)`).
5. **Update Phase 3 (Completion Detection):** Change the expected file count to reflect the new total of snipers.

## Verification Plan

### Manual Verification
- Review `deepdive-code-hunt.md` to ensure the table alignment is preserved.
- Run a dry-run of the deepdive fleet and verify R-28, R-29, and R-30 output valid JSON schemas.
