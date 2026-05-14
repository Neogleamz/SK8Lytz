# Implementation Plan: Babel Syntax Gate

## Goal
Add Babel/Metro syntax parsing to the Husky pre-commit hooks.

## Why this is necessary
Husky missed the previous syntax bug because `tsc --noEmit` did not throw on a mismatched bracket in a promise chain (TypeScript parsed it but loosely allowed it). However, Babel (used by Metro) threw a runtime error. By adding a Babel syntax check, we will catch missing brackets and mismatched AST elements before allowing a commit.

## Proposed Changes

### Target: `SK8Lytz/.husky/pre-commit`
- **[MODIFY]** Append a new command to the `pre-commit` hook.
- **Change:** Add `npx expo export --platform web` or a similar fast Babel AST compilation step.
  - Since Expo export might take ~30 seconds, we need to balance safety and commit speed.
  - An alternative is to add `@babel/eslint-parser` to an ESLint run if configured, or just run the Metro dry-run.
  - We will implement the Metro dry run to be completely certain we catch the exact AST errors Metro uses.
  - Update: we will try to use a faster syntax-only check if available, or simply `npx tsc --noEmit` plus `npx eslint --ext .ts,.tsx src/` if ESLint is configured to catch syntax. 
  - Actually, `babel-eslint` is deprecated. We will add a simple `npx babel src --no-babelrc --dry-run` or similar if possible. Let's just use the bulletproof option: `npx expo export --platform web`.

## Verification Plan
1. Manually introduce a syntax error (e.g., dropping a closing bracket `});`) in a test file.
2. Attempt to commit the file.
3. Verify that the Husky pre-commit hook fails and prevents the commit.
4. Remove the syntax error and verify the commit succeeds.
