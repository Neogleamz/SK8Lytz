# 🛡️ Observatory Pipeline Implementation Plan

**Goal:** Implement the 12 tasks for the `/self-heal` observatory pipeline, establishing the collection, analysis, and auto-triage layers backed by MMKV for persistence.
**Source Analysis:** 📊 [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md)

---

## Phase A: Foundation

### A1: `feat/observatory-schema`
- **Goal:** Create the `tools/observatory/` directory structure, the TypeScript interface for `UnifiedErrorRecord`, and the base `Collector` class.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §4 (Unified Error Schema)
- **Steps:**
  1. Create `tools/observatory/raw/`, `tools/observatory/processed/`, `tools/observatory/reports/`, and `tools/observatory/archive/` directories.
  2. Create `tools/observatory/types.ts` containing the `UnifiedErrorRecord`, `ErrorSource`, `ErrorType`, and `Breadcrumb` interfaces exactly as defined in the spec.
  3. Create `tools/observatory/collectors/BaseCollector.mjs` with an abstract `collect()` method and a `writeFindings(agentId, records)` method that validates against the `UnifiedErrorRecord` schema before writing to `raw/`.
- **Verify:** `node -e "require('./tools/observatory/collectors/BaseCollector.mjs')"` runs without syntax errors.
- **Out of Scope:** Implementing actual collectors.

### A2: `feat/observatory-local-collectors`
- **Goal:** Build the local file-reading and grep-scanning collectors (Sources 7-12).
- **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Sources 7-12)
- **Steps:**
  1. Create `naked_errors.mjs` (S8): Wraps `grep -rn "console.error" src/` and excludes `__DEV__` lines.
  2. Create `known_issues.mjs` (S9): Parses `tools/KNOWN_ISSUES.md` to extract VS-XXX IDs and patterns.
  3. Create `friction_events.mjs` (S10): Parses `tools/FRICTION_LEDGER.md` to extract active FRICTION-XXX patterns and occurrences.
  4. Create `session_errors.mjs` (S11): Parses the last 10 entries of `tools/SESSION_LOG.md` for `[DECISION]` and `[EVENT]` blocks containing error keywords.
  5. Create `sentinel_sweep.mjs` (S7): Wraps the execution of `tools/sentinel/*.js` and `*.py` and parses their standard outputs.
  6. Create `detox_failures.mjs` (S12): Parses the Detox JSON reporter output file.
- **Verify:** Run each collector script manually and verify a JSON file is created in `tools/observatory/raw/` with at least 0 findings and conforming to `UnifiedErrorRecord`.

### A3: `feat/observatory-build-collectors`
- **Goal:** Build collectors for Jest, TSC, and Web Console errors (Sources 3-5).
- **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Sources 3-5)
- **Steps:**
  1. Create `jest_failures.mjs` (S3): Parses the Jest JSON output file (`jest_results.json`).
  2. Create `tsc_errors.mjs` (S4): Runs `npx tsc --noEmit` and parses the stdout for `TS####` error codes, files, and lines.
  3. Create `web_console.mjs` (S5): Wraps the existing `tools/web-console-harvester.js` script and maps its output to the `UnifiedErrorRecord` schema.
- **Verify:** Run `node tools/observatory/collectors/tsc_errors.mjs` and verify it produces a valid `tsc_findings.json`.

---

## Phase B: Remote & Device

### B1: `feat/observatory-remote-collectors`
- **Goal:** Build collectors for Supabase telemetry (Sources 1-2).
- **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Sources 1-2)
- **Steps:**
  1. Extend the existing `tools/sync_remote_errors.mjs` to fetch from `telemetry_errors` (S1) and `crash_telemetry` (S2) using the Supabase REST API or CLI.
  2. Map the JSONB payloads from Supabase into the `UnifiedErrorRecord` schema.
  3. Ensure the MMKV backup logic is accounted for (if local MMKV `crash_telemetry_buffer` exists, read from it as well).
- **Verify:** Run `node tools/observatory/collectors/supabase_errors.mjs` (with a dummy token or mock response) and verify schema conformance.

### B2: `feat/observatory-device-collector`
- **Goal:** Build the ADB logcat parser for native device crashes (Source 6).
- **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Source 6)
- **Steps:**
  1. Create `adb_logcat.mjs` (S6).
  2. Execute `adb logcat -d -s "ReactNativeJS:V" "AndroidRuntime:E"`.
  3. Parse the output for fatal exceptions (Java stack traces or JS unhandled promises).
  4. Check `adb devices` first; if none connected, output 0 findings gracefully.
- **Verify:** Run script with no device connected -> 0 findings. Connect device, trigger error -> parses correctly.

---

## Phase C: Analysis Engine

### C1: `feat/observatory-dedup-engine`
- **Goal:** Implement the 4-pass deduplication pipeline and urgency scoring algorithm.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §5.1, §5.2
- **Steps:**
  1. Create `tools/observatory/analyzer/dedup.mjs`.
  2. Implement Pass 1 (Exact Match): merge by `file:line:message`.
  3. Implement Pass 2 (Fuzzy Match): merge by file + ±5 lines + error code.
  4. Implement Pass 3 (Root Cause): merge by deepest common stack frame.
  5. Implement Pass 4 (False Positive Scrub): remove test file errors, platform mismatches, etc.
  6. Implement the Urgency Scoring Algorithm: `(Severity × 0.30) + (Frequency × 0.20) + (Trend × 0.20) + (Domain × 0.15) + (UserImpact × 0.15)`.
- **Verify:** Feed mock duplicate records into the engine; verify they emerge as a single record with incremented `occurrences`.

### C2: `feat/observatory-crossref-engine`
- **Goal:** Implement cross-referencing against KNOWN_ISSUES, FRICTION_LEDGER, and SESSION_LOG, plus regression detection.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §5.3, §5.4
- **Steps:**
  1. Create `tools/observatory/analyzer/crossref.mjs`.
  2. Load the outputs from S9, S10, and S11.
  3. For each deduplicated error, check if its fingerprint matches any known issue (VS-XXX).
  4. If match, set `trend = 'REGRESSION'`, add 30 to urgency, and set `knownIssueMatch`.
  5. Check against friction patterns.
- **Verify:** Feed a mock record matching a known VS-001 pattern; verify its urgency score increases by 30 and trend is REGRESSION.

---

## Phase D: Auto-Triage

### D1: `feat/observatory-task-generator`
- **Goal:** Group analyzed errors into task clusters and generate Kanban task entries following schema rules.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §6.1, §6.2
- **Steps:**
  1. Create `tools/observatory/action/task_generator.mjs`.
  2. Group analyzed errors by root cause/file.
  3. For each cluster, generate a slug (e.g., `fix/auto-<hash>`).
  4. Assign tags: `[✅ READY] [✅ VERIFIED] [<DOMAIN>] [<RISK>] [<SIZE>] [🤖 MODEL] [BATCH:self-heal-YYYY-MM-DD]`.
  5. Format the markdown string strictly according to the SK8Lytz Kanban Schema.
- **Verify:** Output a generated task string and visually confirm it matches the nested list structure required by the bucket list.

### D2: `feat/observatory-report-generator`
- **Goal:** Generate the daily triage report artifact presenting findings to the user.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §8 (Triage Report Format)
- **Steps:**
  1. Create `tools/observatory/action/report_generator.mjs`.
  2. Read the analyzed JSON and generated task strings.
  3. Sort task clusters by max urgency score (CRITICAL, HIGH, MEDIUM).
  4. Format the Markdown report `tools/observatory/reports/YYYY-MM-DD/report.md`.
  5. Include trend analysis tables and institutional memory update summaries.
- **Verify:** Feed mock data and verify the markdown report is generated with the correct sections and formatting.

---

## Phase E: Workflow & Auto-Heal

### E1: `feat/observatory-workflow`
- **Goal:** Create the `/self-heal` workflow markdown file defining the 5-phase agent orchestration.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §8 (Workflow Specification)
- **Steps:**
  1. Create `.agents/workflows/self-heal.md`.
  2. Include the YAML frontmatter with trigger, description, and model requirements.
  3. Document Phase 0 (River), Phase 1 (Blake), Phase 2 (Blake), Phase 3 (Reyes), Phase 4 (Jordan), and Phase 5 (Alex).
  4. Define handoff blocks exactly as specified.
  5. Update `.agents/workflows/audit-codebase.md` to add the DEPRECATED warning and redirect stub.
- **Verify:** Read the generated file to ensure all personas and handoffs are correctly placed.

### E2: `feat/observatory-auto-heal-library`
- **Goal:** Implement the auto-heal pattern library (AH-001 through AH-009) and proposal engine.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §7
- **Steps:**
  1. Create `tools/observatory/action/auto_healer.mjs`.
  2. Define the 9 patterns using regex/AST matching criteria.
  3. If an error matches a HIGH confidence pattern and is NOT in `[BLE]` or `[CORE]` domain, generate a `.patch` file or code replacement proposal.
  4. Flag these specific tasks with `[🔧 AUTO-HEAL PROPOSED: AH-XXX]`.
- **Verify:** Feed a dummy record of a naked `console.error`; verify it gets flagged as `AH-001`.

---

## Phase F: Hardening

### F1: `feat/observatory-tests`
- **Goal:** Write Jest unit tests for the collection, dedup, scoring, and task generation engines.
- **Source of Truth:** 📖 `self_healing_audit_system.md` §11
- **Steps:**
  1. Create `tools/observatory/__tests__/dedup.test.js` to verify exact and fuzzy matching.
  2. Create `tools/observatory/__tests__/scoring.test.js` to verify urgency weight calculation.
  3. Create `tools/observatory/__tests__/task_generator.test.js` to verify kanban schema compliance.
- **Verify:** Run `npm run verify` to ensure tests pass and contribute to the overall test count.
