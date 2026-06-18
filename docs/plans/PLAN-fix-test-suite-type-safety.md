# Implementation Plan: fix/test-suite-type-safety

## Goal
Purge all `any` type annotations and `as any` casts from 22 test files. Replace with proper mock types, jest.mocked(), and global interface extensions.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters THE_TEST_SUITE + SIMULATION_&_MOCKS

## Common Patterns to Fix
- `(global as any).__DEV__` → Create `declare global { var __DEV__: boolean }` in a test setup file
- `as any` for mock objects → Use `jest.mocked()` or properly typed mock factories
- `: any` parameters → Use `unknown` or explicit function signatures
- `as unknown as Device` → Create typed mock factory functions

## Files to Create/Modify

### [NEW] [test-globals.d.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/__tests__/test-globals.d.ts) — Global type declarations for test environment
### [MODIFY] All 22 test files listed in the system audit report THE_TEST_SUITE findings

## Verification
- `npm run verify`
- Grep for `as any` and `: any` in __tests__/ — must be zero

## Out of Scope
- Production source files
