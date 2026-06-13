# Implementation Plan: deepdive-missing-domains

## Goal Description
Expand `deepdive-code-hunt.md` to include 4 new critical domains: `DEVOPS_&_TOOLING`, `ANIMATION_&_PERFORMANCE`, `ACCESSIBILITY_&_I18N`, and `THE_TEST_SUITE`. Currently, the deepdive fleet is blind to these areas because the glob patterns don't cover them or they are lumped into other domains inappropriately.

## Proposed Changes

### deepdive-code-hunt.md

#### [MODIFY] [deepdive-code-hunt.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-code-hunt.md)
1. **Update Vector Alpha Table:** Increase the total Domain Agents from 21 to 25.
2. **Add Domain 22 (`DEVOPS_&_TOOLING`):**
   - Target Directories: `.github/workflows/*`, `tools/*`, `.husky/*`, `.agents/rules/*`
   - Purpose: Audit PowerShell scripts, GitHub Actions, and Agent Rules for correctness and security.
3. **Add Domain 23 (`ANIMATION_&_PERFORMANCE`):**
   - Target Directories: Files importing `react-native-reanimated`, `@shopify/react-native-skia`, `react-native-gesture-handler`.
   - Purpose: Specifically audit for frame drops, missing `worklet` directives, and main thread blocking.
4. **Add Domain 24 (`ACCESSIBILITY_&_I18N`):**
   - Target Directories: Global scan of `src/components/*` looking for `accessible={true}`, `accessibilityRole`, and `i18n.t()`.
   - Purpose: Ensure screen reader compatibility and correct translation keys.
5. **Add Domain 25 (`THE_TEST_SUITE`):**
   - Target Directories: `__tests__/*`, `src/**/__tests__/*`
   - Purpose: Audit the tests themselves. Look for brittle mocks, commented-out assertions, and missing cleanup blocks.
6. **Update Phase 3 (Completion Detection):** Change the expected file count from 48 to 52 (25 domain + 26 sniper + 1 structural).

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure no markdown syntax errors break the workflow parser.
### Manual Verification
- Trigger `/deepdive-code-hunt` with the canaries and verify the new domains are successfully invoked.
