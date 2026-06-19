# Implementation Plan: fix/cloud-functions-security

## Goal
Fix CORS, RLS bypass, and type safety issues in Supabase edge functions.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster CLOUD_FUNCTIONS

## Findings to Resolve
1. Missing CORS headers on notify-crew-session edge function
2. Service role key used globally (bypasses RLS)
3. Implicit `any` types in function parameters
4. Unverified PostgREST join relationship

## Files to Create/Modify

### [MODIFY] [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/functions/notify-crew-session/index.ts) // SKIPPED: Fix already implemented in master (commit 6e79233d4b)
- Add proper CORS headers
- Scope service role key usage to minimum required operations
- Add explicit TypeScript types to all parameters
- Verify and fix PostgREST join

## Verification
- TypeScript compilation of edge function
- Manual: test with `supabase functions serve` locally

## Out of Scope
- Other edge functions
- Client-side Supabase queries
