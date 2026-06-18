# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🚧 ACTIVE SPRINT

*(Empty — ready for `/goal` execution)*

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`fix/gatt-conn-133-exception`**
  - **Tags:** `[📝 NEEDS PLAN]` `[LAB]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Plan:** 📎 [PLAN-telemetry-gatt-conn-133-exception.md](./plans/PLAN-telemetry-gatt-conn-133-exception.md)
  - **Goal:** Resolve automated telemetry crash: GattException: status 133 (0x85) during BLE scan discover for HALOZ
  - **Details:** Found crash telemetry with ID err_091a in file `src/hooks/ble/useBLEAutoRecovery.ts`. Trace: at useBLE.ts:321
at useBLESweeper.ts:145

---

## 🔥 ON DECK

### ⚡ [BATCH:deepdive-audit-mega-sweep] — `deepdive-audit-mega-sweep` — READY
> **Worktree**: Per-task isolated worktrees · **Type**: Parallel (5 Waves) · **Prerequisite**: None
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Deep-dive code synthesis from 55 Map-Reduce agents. ~170 verified findings across 22 clusters. AST collision matrix: 54 pairs → 5 waves.

#### 🌊 Wave 1 — Foundation & Quick Wins (7 parallel tasks, no prerequisite)

- [ ] **`fix/manifest-permissions`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix `neverForLocation` manifest regression that silently drops BLE scan results on Android 12+.
  - **Decision Log:** OS-PERM-001 from deep-dive audit — live production BLE scan bug confirmed on FCF1 hardware.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-manifest-permissions.md](./plans/PLAN-fix-manifest-permissions.md)
  - **Source of Truth:** 📖 [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
  - **Details:** CRITICAL severity. Single file change.

- [ ] **`fix/protocol-core-integrity`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix critical ZenggeAdapter sequence counter corruption and protocol correctness issues.
  - **Decision Log:** PROTOCOL_CORE-004 — sequence counter corruption can desync BLE commands on all connected devices.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-protocol-core-integrity.md](./plans/PLAN-fix-protocol-core-integrity.md)
  - **Source of Truth:** 📖 [ZenggeAdapter.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts) + [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)
  - **Details:** CRITICAL severity. Must cross-check all byte values against Protocol Bible.

- [ ] **`fix/dashboard-styles-perf`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix StyleSheet.create firing on every render cycle (up to 20Hz during telemetry).
  - **Decision Log:** THEME-001 — massive GC pressure from StyleSheet factory invoked in render body.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-dashboard-styles-perf.md](./plans/PLAN-fix-dashboard-styles-perf.md)
  - **Source of Truth:** 📖 [DashboardStyles.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/styles/DashboardStyles.ts)
  - **Details:** CRITICAL severity. Convert factory to static StyleSheet.create.

- [ ] **`fix/controller-dispatch-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix PII telemetry leaks, re-entrancy races, and type safety in controller dispatch pipeline.
  - **Decision Log:** R-09 PII leak — MAC addresses sent unscrubbed to Supabase telemetry (GDPR risk).
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-controller-dispatch-safety.md](./plans/PLAN-fix-controller-dispatch-safety.md)
  - **Source of Truth:** 📖 [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
  - **Details:** HIGH severity. 3 files, PII scrubbing + type safety.

- [ ] **`fix/data-layer-types`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[DB]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix type safety, error swallowing, offline-first violations, and AsyncStorage key drift in data layer.
  - **Decision Log:** R-05/R-08 — cloud-first reads in SpeedTrackingService break offline skating.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-data-layer-types.md](./plans/PLAN-fix-data-layer-types.md)
  - **Source of Truth:** 📖 [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts)
  - **Details:** MEDIUM severity. 7 files.

- [ ] **`fix/cloud-functions-security`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix CORS, RLS bypass, and type safety in Supabase edge functions.
  - **Decision Log:** CLOUD-001 — missing CORS headers + service role key bypasses RLS.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-cloud-functions-security.md](./plans/PLAN-fix-cloud-functions-security.md)
  - **Source of Truth:** 📖 [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/functions/notify-crew-session/index.ts)
  - **Details:** MEDIUM severity. 1 file.

#### 🌊 Wave 2 — BLE Core & Privacy (5 parallel tasks, prerequisite: Wave 1 merged)

- [ ] **`fix/ble-services-hardening`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Purge `any` types, fix error swallowing, and resolve hardcoded delays across the BLE service layer.
  - **Decision Log:** R-08/R-16 — 11 BLE service files with type safety violations and raw setTimeout bypassing BleWriteQueue.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-ble-services-hardening.md](./plans/PLAN-fix-ble-services-hardening.md)
  - **Source of Truth:** 📖 [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
  - **Details:** HIGH severity. 11 files. Prerequisite: Wave 1 fully merged.

- [ ] **`fix/dashboard-screen-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Fix type laundering, OS variance violations, event listener leaks, and FlatList bottlenecks.
  - **Decision Log:** R-08 — 10+ `as unknown as` type laundering instances in DashboardScreen.tsx.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-dashboard-screen-safety.md](./plans/PLAN-fix-dashboard-screen-safety.md)
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
  - **Details:** HIGH severity. 3 files. Prerequisite: Wave 1 fully merged.

- [ ] **`fix/pii-scrubber`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Scrub all PII (MAC addresses, user names) from telemetry logging in crew/admin components.
  - **Decision Log:** R-09 — GDPR/privacy violations: MAC addresses and user names sent to Supabase telemetry unscrubbed.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-pii-scrubber.md](./plans/PLAN-fix-pii-scrubber.md)
  - **Source of Truth:** 📖 [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
  - **Details:** HIGH severity. 4 files. Prerequisite: Wave 1 fully merged.

- [ ] **`fix/camera-visualizer-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Fix missing error handling, type laundering, and delete duplicate CustomEffectVisualizer component.
  - **Decision Log:** R-21 — CustomEffectVisualizer is functionally identical to LEDStripPreview (split-brain duplication).
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-camera-visualizer-safety.md](./plans/PLAN-fix-camera-visualizer-safety.md)
  - **Source of Truth:** 📖 [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
  - **Details:** HIGH severity. 5 files (1 deletion). Prerequisite: Wave 1 fully merged.

- [ ] **`fix/notifications-routing-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Fix floating promises, missing error handling, and telemetry context gaps in notification/routing services.
  - **Decision Log:** R-11 — unprotected Supabase query in LocationService can crash the crew landing screen.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-notifications-routing-safety.md](./plans/PLAN-fix-notifications-routing-safety.md)
  - **Source of Truth:** 📖 [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
  - **Details:** MEDIUM severity. 4 files (includes App.tsx). Prerequisite: Wave 1 fully merged.

#### 🌊 Wave 3 — Architecture & Test Safety (5 parallel tasks, prerequisite: Wave 2 merged)

- [ ] **`fix/docked-controller-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Extract 67KB DockedController monolith into sub-components, then fix re-entrancy races and context overload.
  - **Decision Log:** R-23 — DockedController.tsx at 67KB is a collision zone. S4 mandates extraction before editing.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-docked-controller-safety.md](./plans/PLAN-fix-docked-controller-safety.md)
  - **Source of Truth:** 📖 [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
  - **Details:** HIGH severity. Phase 1: extract components. Phase 2: fix violations. Prerequisite: Wave 2 fully merged.

- [ ] **`fix/session-context-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Fix re-entrancy races, floating promises, error swallowing, and type safety in session tracking.
  - **Decision Log:** R-26 — recover() fires concurrently from useEffect + AppState, causing duplicate END events.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-session-context-safety.md](./plans/PLAN-fix-session-context-safety.md)
  - **Source of Truth:** 📖 [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
  - **Details:** HIGH severity. 2 files. Prerequisite: Wave 2 fully merged.

- [ ] **`fix/crew-ui-types`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Replace all `any` type annotations in crew UI components with proper types.
  - **Decision Log:** R-08 — 8 `any` casts across 4 crew UI components.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-crew-ui-types.md](./plans/PLAN-fix-crew-ui-types.md)
  - **Source of Truth:** 📖 [CrewCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewCard.tsx)
  - **Details:** MEDIUM severity. 4 files. Prerequisite: Wave 2 fully merged.

- [ ] **`fix/onboarding-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Fix error swallowing, hardcoded delays, and OS variance in onboarding screens.
  - **Decision Log:** R-07/R-20 — empty catch in AuthScreen + Platform.OS ternaries instead of Platform.select.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-onboarding-safety.md](./plans/PLAN-fix-onboarding-safety.md)
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** MEDIUM severity. 2 files. Prerequisite: Wave 2 fully merged.

- [ ] **`fix/test-suite-type-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🥩 Feast]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Purge all `any` casts from 22 test files. Create global type declarations for test environment.
  - **Decision Log:** R-08 — 60+ `any`/`as any` casts across test suite violate type safety rules.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-test-suite-type-safety.md](./plans/PLAN-fix-test-suite-type-safety.md)
  - **Source of Truth:** 📖 [BleMachine.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/BleMachine.test.ts)
  - **Details:** LOW severity. 22 test files + 1 new type declarations file. Prerequisite: Wave 2 fully merged.

#### 🌊 Wave 4 — Services & Cleanup (4 parallel tasks, prerequisite: Wave 3 merged)

- [ ] **`fix/crew-services-hardening`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[☁️ CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix circular dependencies, error swallowing, floating promises, memory leaks, and offline-first violations across crew services.
  - **Decision Log:** R-29 — 3 circular dependency cycles in CrewService domain prevent clean imports.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-crew-services-hardening.md](./plans/PLAN-fix-crew-services-hardening.md)
  - **Source of Truth:** 📖 [CrewSessionManager.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService/CrewSessionManager.ts)
  - **Details:** HIGH severity. 8 files. Prerequisite: Wave 3 fully merged.

- [ ] **`fix/scanner-ble-hooks`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix error swallowing, type safety, and hardcoded delays in BLE scanner and device state hooks.
  - **Decision Log:** R-07 — empty catch blocks in useBLEScanner silently drop scan errors.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-scanner-ble-hooks.md](./plans/PLAN-fix-scanner-ble-hooks.md)
  - **Source of Truth:** 📖 [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
  - **Details:** HIGH severity. 5 files. Prerequisite: Wave 3 fully merged.

- [ ] **`fix/memory-leak-sweep`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix useEffect cleanup omissions, stale closures, and OS variance issues.
  - **Decision Log:** R-22 — 6 components with useEffect lacking cleanup functions, causing memory leaks on unmount.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-memory-leak-sweep.md](./plans/PLAN-fix-memory-leak-sweep.md)
  - **Source of Truth:** 📖 [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
  - **Details:** MEDIUM severity. 7 files. Prerequisite: Wave 3 fully merged.

- [ ] **`fix/ui-misc-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix type safety, hardcoded delays, and fire-and-forget BLE streaming in miscellaneous UI components.
  - **Decision Log:** R-02 — Oracle53LiveStream violates playback engine model with fire-and-forget 0x53 frames.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-ui-misc-safety.md](./plans/PLAN-fix-ui-misc-safety.md)
  - **Source of Truth:** 📖 [Oracle53LiveStream.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/oracle/Oracle53LiveStream.tsx)
  - **Details:** LOW severity. 8 files. Prerequisite: Wave 3 fully merged.

#### 🌊 Wave 5 — Engine Refactor (1 solo task, prerequisite: Wave 4 merged)

- [ ] **`refactor/spatial-pattern-engines`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:5]`
  - **Goal:** Resolve circular dependencies between pattern engine files and extract SpatialEngine (61KB) monolith.
  - **Decision Log:** R-23/R-29 — 3 circular dependency cycles + SpatialEngine exceeds 30KB S4 safety threshold.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-refactor-spatial-pattern-engines.md](./plans/PLAN-refactor-spatial-pattern-engines.md)
  - **Source of Truth:** 📖 [SpatialEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SpatialEngine.ts)
  - **Details:** MEDIUM severity. 4 existing files + 5 new extracted modules. Prerequisite: Wave 4 fully merged.

---

##  ❄️ Icebox / Backburner (Manual Trigger Only)

### 🧹 Tech Debt & Upgrades
### ⚡ [BATCH:refactor/upgrade-expo-56] — `refactor/upgrade-expo-56` — IN PROGRESS
> **Worktree**: `refactor/upgrade-expo-56` · **Type**: Sequential · **Prerequisite**: None
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/fb5fb761-e7be-4241-a902-3cb07dca3307/implementation_plan.md) — User explicitly requested forcing a major dependency update (Expo 55->56) mid-release to resolve deep-tree js-yaml vulnerabilities.

- [ ] **`refactor/upgrade-expo-56`**
  - **Tags:** `[✅ READY]` `[☁️ CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 M132]` `[BATCH:refactor/upgrade-expo-56]`
  - **Goal:** Upgrade the project from Expo SDK 55 to 56, including React Native 0.85, to wipe out all outstanding NPM vulnerabilities.
  - **Decision Log:** User explicitly requested during `/ship-it` to override release freeze and force a full dependency update.
  - **Analysis:** 📊 Plan: [PLAN-refactor-upgrade-expo-56.md](./plans/PLAN-refactor-upgrade-expo-56.md)
  - **Source of Truth:** 📖 [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json)
  - **Details:** High risk of breaking custom native modules and legacy UI components due to React Native 0.85 bridging changes.

### 🎵 Epic: Music Mode

- [ ] `feat/music-intel-phase-1` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 50k] [⏱️ 6h] [📅 2026-04-14] [🧠 THINK] [Spotify Sync] — OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] [Media Access] — Android MediaSession detection (YouTube, Pandora, etc.). → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [🧪 LAB] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] [Live Rink Mode] — ShazamKit/ACRCloud periodic background scanning (45s). → [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] [Apple Music] — MusicKit integration for native iOS BPM. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 45k] [⏱️ 6h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🧠 THINK] [Crew Party Sync] — Master BPM Choreography Engine with Realtime crew sync. → [Plan](docs/plans/feat-music-integration-master.md)

- [ ] `feat/google-oauth-integration` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [⏱️ 6h] [📅 2026-04-14] [🧠 THINK] Integrate Google OAuth as an auth provider. (Requires Google Cloud Console setup + Supabase config). → [Plan](docs/plans/feat-google-oauth-integration.md)
- [ ] `feat/spatial-beat-mapping` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 18k] [⏱️ 3h] [🧠 THINK] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). → [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Transform Dashboard into palette-synced dynamic backgrounds. → [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [🧪 LAB] [✅ L-RISK] [🍱 Meal] [🪙 10k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Pattern selection (Strobe, Blink, Static) + music slider fix. → [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/impact-sentinel-safety` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🧠 THINK] [Pillar 13] Fall Detection — triggers white 'Flare' strobe on impact. → [Plan](docs/plans/feat-impact-sentinel-safety.md)
- [ ] `feat/kinetic-brake-lights` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🧠 THINK] [Pillar 12] Kinetic Safety — phone accelerometer pulse RED for braking. → [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [⏱️ 6h] [🧠 THINK] Geofence-based 'Hive Mind' synchronization. → [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 8k] [⏱️ 3h] [🤖 FLASH] [📝️ NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 25k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [☁️ CLOUD] [⚠️ H-RISK] [🍱 Meal] [🪙 20k] [⏱️ 3h] [🧠 THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 12k] [⏱️ 3h] [🤖 FLASH] [📝️ NEEDS-PLAN] Card Swipe Navigation.
