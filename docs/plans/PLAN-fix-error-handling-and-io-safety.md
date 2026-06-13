# Implementation Plan: Fix Error Handling and IO Safety

## Goal
Standardize error unwrapping, catch unhandled IO rejections, and eradicate `any` bypasses across the application.

## Source of Truth
📖 [system_audit_report.md](../../C:/Users/Magma/.gemini/antigravity/brain/79cf6856-67a1-49d0-aadc-9079eee6c7ae/system_audit_report.md)

## Identified Issues
1. **[R-06] Error Unwrapping:** 51 files swallow `e: any` or blindly use `String(e)` instead of `e instanceof Error`.
2. **[R-11] Unhandled Rejections:** Auth components and `AsyncStorage` methods lack `try/catch` wrappers.
3. **[R-08] Type Bypasses:** Over 230 occurrences of `: any` definitions or explicit `as any` casting.

## Proposed Changes
- **`src/components/auth/*`**: Wrap all `supabase.auth` and `AsyncStorage` calls in `try/catch` and use `AppLogger.error` properly.
- **Global `catch` Refactor**: Execute `multi_replace_file_content` across core hooks and services to replace `catch (e: any)` with `catch (e) { const msg = e instanceof Error ? e.message : String(e); ... }`.
- **Boy Scout Type Pass**: Systematically remove `any` from props and function signatures, mapping them to `unknown` or properly generated Supabase database types.
