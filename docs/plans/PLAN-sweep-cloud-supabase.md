# Implementation Plan

## Task: sweep-cloud-supabase
**Slug:** sweep-cloud-supabase  
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters  
**Size:** [Meal] — 7 files  
**Risk:** [H-RISK] — Security DEFINER SQL migrations, RLS policy  
**Status:** [✅ READY]  
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_CLOUD_FUNCTIONS_findings.json`

## Goal
Fix 8 HIGH-severity security vulnerabilities in Supabase migrations and edge functions. The primary goal is to harden all `SECURITY DEFINER` PostgreSQL functions with `SET search_path = ''` to eliminate SQL injection attack surface, fix the email domain validation regex, restrict the `scraper_blocklist` RLS policy, and add error handling to the Deno edge function.

## Decision Log
- **Why search_path matters**: Without `SET search_path = ''`, a SECURITY DEFINER function runs with the caller's search_path, allowing schema injection attacks.
- **Email LIKE pattern**: `%@sk8lytz.com` matches `evil@sk8lytz.com.attacker.com` — must anchor with `@sk8lytz.com` suffix check.
- **RLS scraper_blocklist**: Anonymous read of blocklist gives external actors intel on banned MACs.

## Files to Create/Modify

### [MODIFY] supabase/migrations/20260414_account_deletion_rpc.sql
- Add `SET search_path = ''` to `public.delete_account()` SECURITY DEFINER function
- Use fully-qualified schema references (`public.`, `auth.`)

### [MODIFY] supabase/migrations/20260607100000_fix_telemetry_schema.sql
- Add `SET search_path = ''` to `public.flush_telemetry()` SECURITY DEFINER function

### [MODIFY] supabase/migrations/20260609175500_restore_domain_admin_promotion.sql
- Add `SET search_path = ''` to `public.handle_auto_promotion` trigger function
- Fix email domain check: replace `email LIKE '%@sk8lytz.com'` with `email LIKE '%@sk8lytz.com' AND email NOT LIKE '%@sk8lytz.com.%'` or use `RIGHT(email, 12) = '@sk8lytz.com'`

### [MODIFY] supabase/migrations/20260609140000_live_debugger_views.sql
- Add `SET search_path = ''` to `public.resolve_crash_signature` RPC function

### [NEW] supabase/migrations/20260614000000_harden_rls_scraper_blocklist.sql
- Replace overly permissive anonymous RLS SELECT on `scraper_blocklist` with authenticated-only or role-scoped policy

### [MODIFY] supabase/functions/notify-crew-session/index.ts
- Wrap auth and DB calls in try/catch blocks
- Replace sequential `for` loop with `Promise.allSettled` for notification batches
- Fix consecutive DB calls: merge into single query with JOIN

### [MODIFY] src/services/supabaseClient.ts
- Remove `as unknown as ReturnType<...>` mock cast — use proper typed mock interface

## Out of Scope
- No changes to app-side BLE logic
- No React component changes
- No changes to other migration files not listed above

## Verification Plan
- `npx supabase db lint` (if available) or manual SQL review
- `npm run verify` — TSC must pass on `supabaseClient.ts`
- Manual review of each migration diff
