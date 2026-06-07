---
description: The Map-Reduce QA Fleet — Spawns 16 sub-agents to enforce React Native Guardrails, prevent BLE dropping, and audit logic against the Master Reference.
persona_entry: "🔬 QA — Blake"
team_roster: .agents/team-roster.md
---

# Deep-Dive Code (The 16-Node QA Fleet)

> **🔬 QA — Blake | Map-Reduce Orchestration Active**
> *Now that Avery has perfectly hydrated the Master Reference, we use it as a weapon. I am deploying 16 strict QA Auditor nodes across all domains. They will enforce our React Native guardrails and hunt down the specific regressions that plague us: BLE dropping, auto-reconnect failures, missing telemetry, and broken group connectivity.*

---

### ⛔ PREREQUISITE GATE
You are STRICTLY FORBIDDEN from running `/deepdive-code` unless `/deepdive-docs` has been run recently and the `tools/SK8Lytz_App_Master_Reference.md` is guaranteed to be accurate. If the Master Reference is stale, my auditors will go rogue and report false positives.

---

### ⚡ Phase 1 — Fleet Partitioning

The codebase is partitioned into the **16 Elite Architecture Domains**:
1. `IDENTITY`
2. `BLE_CORE`
3. `GROUP_SYNC`
4. `UI_CONTROLS`
5. `DATA_LAYER`
6. `UTILS`
7. `NATIVE_&_WATCH`
8. `NOTIFICATIONS_&_ROUTING`
9. `SESSION_TRACKING`
10. `HARDWARE_PROTOCOLS`
11. `CLOUD_FUNCTIONS`
12. `THEME_&_ASSETS`
13. `SIMULATION_&_MOCKS`
14. `BUILD_CONFIG_&_OTA`
15. `OS_PERMISSIONS`
16. `DEPENDENCY_AUDIT`

---

### ⚡ Phase 2 — The Fleet Launch

Blake executes `invoke_subagent` to spawn **16 `research` sub-agents simultaneously**, passing each node their specific target domain.

**The Sub-Agent Directive (Strict SDE QA Constraints):**
> You are a Google Antigravity QA Auditor Node assigned to the [DOMAIN_NAME] domain.
> 1. Read the `tools/SK8Lytz_App_Master_Reference.md`, `tools/INDUSTRY_BENCHMARKS.md`, and any relevant Protocol Bibles (`tools/ZENGGE_PROTOCOL_BIBLE.md` / `tools/BANLANX_PROTOCOL_BIBLE.md`).
> 2. Read EVERY SINGLE FILE in your assigned domain using `list_dir` and `view_file`. Do not skip any files.
> 3. **The Immutable Regression & Guardrail Checklist**. You MUST audit every file against EVERY ONE of these points. No exceptions.
>    - **[R-01] Queue Enforcement**: Is ANY code bypassing the `BleWriteQueue` strict FIFO serial queue? Are direct GATT writes used outside of critical power/recovery pings?
>    - **[R-02] Fire-and-Forget**: Are UI controls waiting on BLE hardware acks? They MUST use `WRITE_TYPE_NO_RESPONSE` and Optimistic UI.
>    - **[R-03] Auto-Reconnects**: Are exponential backoff and jitter implemented correctly? Are we cleanly capturing organic disconnect events?
>    - **[R-04] Telemetry Context**: Are errors (especially BLE status 133/8) being logged with full context (`payload_size`, `operation_type`, `rssi`) via `TelemetryService.extractBleContext`?
>    - **[R-05] Offline-First**: Are core reads/writes utilizing `AsyncStorage` and asynchronous `Supabase` sync? Is offline data dropped?
>    - **[R-06] Error Handling**: Are all catch blocks using strict standard error handling (`e instanceof Error ? e.message : String(e)`)?
>    - **[R-07] Performance Guardrails**: Are there inline functions or unmemoized components in `FlatList` renderers? Are there inline styles instead of `StyleSheet.create`? Are `useCallback`/`useMemo` missing?
>    - **[R-08] Type Safety**: Are there ANY `any` casts or `@ts-ignore` directives? (Strictly forbidden).
>    - **[R-09] PII Scrubbing**: Is logging leaking PII? Are logs sanitized before being written?
>    - **[R-10] Group Connectivity**: Does group sync reliably and concurrently propagate payloads?
>    - **[R-11] Promise/IO Safety**: Are ALL `AsyncStorage` and `supabase` network calls wrapped in `try/catch` or explicitly chained with `.catch()` to prevent silent unhandled promise rejections?
>    - **[R-12] Stale Closures**: Are `setInterval`, `setTimeout`, and BLE event listeners properly using `useRef` or dependency arrays to prevent stale state captures?
>    - **[R-13] GATT Connection Safety**: Are multiple BLE device connections initiated sequentially (to prevent GATT 133 collisions) rather than concurrently via `Promise.all`?
>    - **[R-14] State Matrix Completeness**: Do all data views explicitly handle the 4-state matrix (Loading, Error, Empty, Success), or do they fail to blank screens?
>    - **[R-15] Auth Context Bypassing**: Are any hooks or services manually calling `supabase.auth.getUser()` instead of consuming the provided globally-synced `AuthContext`?
>    - **[R-16] Hardcoded Delays**: Are there any manual `setTimeout` delays being used to throttle BLE commands instead of relying on the centralized `BleWriteQueue`?
>    - **[R-17] Event Listener Leaks**: Are all `DeviceEventEmitter`, `bleManager.onDeviceDisconnected`, and `Supabase.channel` subscriptions properly torn down in `useEffect` cleanups or destructors?
>    - **[R-18] Boolean Traps**: Are complex states modeled using fragile scattered booleans (`isConnecting && !hasError`) instead of robust string unions/FSMs (`'IDLE' | 'CONNECTING' | 'ERROR'`)?
> 5. Output a strict Markdown Bug Checklist. Format MUST be exactly:
>    - `[ ]` **`[domain]/[brief-slug-name]`**
>      - **Tags:** `[Status]` `[Verify]` `[Layer]` `[Risk]` `[Size]` `[CogLoad]`
>      - **Goal:** [Reason and fix recommendation]
>      - **Decision Log:** [Why this violates Rule 16 or Gold Standards]
>      - **Analysis:** 📊 Source: `[system_audit_report.md]`
>      - **Source of Truth:** 📖 `src/file.tsx:L45`
>    If your domain is completely clean, output exactly: `[DOMAIN_CLEAN]`.
> 6. Do not yap. Output only the structured list.

---

### ⚡ Phase 3 — The Triage Synthesis

Once all 16 sub-agents report back:
1. Blake synthesizes the 16 Bug Checklists into a single artifact: `system_audit_report.md`.
2. Blake drafts a formatted `[BATCH:deep-dive-remediation]` containing ALL identified issues (not just criticals) and pipes them directly into `tools/SK8Lytz_Bucket_List.md` under the TRIAGE QUEUE. This batch MUST strictly follow the Kanban Constitution and intake rules, ensuring every task has all 6 required tags and a proper Source of Truth field.
