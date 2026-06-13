# Implementation Plan: PII Scrubbing Leaks (R-09)

## 📖 Source of Truth
Source: `docs/audits/system_audit_report_2026_06_07.md`:[Line 30]

## 🎯 Goal
Harden `AppLogger.formatPayload` to sanitize string values and catch non-standard keys to prevent PII leakage to the cloud.

## 🛠️ Proposed Changes

### [MODIFY] `src/utils/AppLogger.ts`
- Expand `piiKeys` blocklist to a RegEx pattern matcher (e.g., `/name|email|username|leader/i`) rather than an exact-match Set.
- Introduce string value sanitation for common error payloads (e.g., regex masking `[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}` in `error.message`).

### [MODIFY] `src/components/admin/tools/UserManagementPanel.tsx`
- Destructure or map the `data` export to explicitly strip `username` and `display_name` before passing to `AppLogger.log('DATA_EXPORT', ...)`.

## ✅ Verify
- Write an isolated Jest test for `AppLogger.formatPayload` passing a mock payload containing `newLeaderName: "Magma"` and `message: "Failed auth for magma@example.com"`. Assert the output is `[REDACTED]`.

## 🛑 Out of Scope
- Refactoring the entire Supabase analytics pipeline or retroactively cleaning old PII from the database.
