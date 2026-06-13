# Implementation Plan: Cloud Security Hardening

## Overview
The deep-dive synthesis flagged two High severity security issues in the Supabase Cloud layer:
1. IDOR / Privilege Escalation in `notify-crew-session` Edge Function. It uses `SUPABASE_SERVICE_ROLE_KEY` to query `crew_memberships` without verifying if the authenticated caller belongs to the crew.
2. Search Path Hijacking Vulnerability in `admin_user_management.sql` migrations. `SECURITY DEFINER` functions omit `SET search_path = public`.

## Affected Files
- `supabase/functions/notify-crew-session/index.ts`
- `supabase/migrations/20260418061000_admin_user_management.sql`

## Rule Violations
- Security Guardrails (IDOR, Search Path Escalation, Unrestricted Inserts).

## Proposed Changes
1. Add `SET search_path = public` to all `SECURITY DEFINER` functions in the admin migration.
   - **Source:** `supabase/migrations/20260418061000_admin_user_management.sql:46`
   - **Verify:** Run `npm run verify` and manual inspection of the built SQL to ensure search path is set.
2. Implement Auth matching in the `notify-crew-session` function (verify JWT matches the crew).
   - **Source:** `supabase/functions/notify-crew-session/index.ts:45`
   - **Verify:** Write a test simulating an unauthorized JWT attempting to invoke the edge function. It must return a 403.

## Out of Scope
- Modifying other edge functions that do not have the IDOR vulnerability.
- Re-architecting the crew session schema.

## Kanban Tags
- [Status]: `[🔥 ON DECK]`
- [Verification Status]: `[✅ VERIFIED]`
- [Layer]: `[DOMAIN_CLOUD_FUNCTIONS]`
- [Risk]: `[H-RISK]`
- [Size]: `[Meal]`
- [Cognitive Load]: `[HIGH]`
- Source of Truth: `tools/SK8Lytz_App_Master_Reference.md` § Supabase Security Rules
