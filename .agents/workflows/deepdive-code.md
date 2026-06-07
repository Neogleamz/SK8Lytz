---
description: The Map-Reduce QA Fleet — Dual Vector Orthogonal Auditing. Spawns Domain Agents (1 per architecture silo) AND Rule Snipers (1 per guardrail) to create an inescapable matrix of code quality enforcement.
persona_entry: "?? QA — Blake"
team_roster: .agents/team-roster.md
---

# Deep-Dive Code (The Orthogonal QA Fleet)

> **?? QA — Blake | Map-Reduce Orchestration Active**
> *Now that Avery has perfectly hydrated the Master Reference, we use it as a weapon. I am deploying a Dual Vector Fleet: Domain Agents to audit silos, and Rule Snipers to hunt specific anti-patterns globally. This matrix ensures no regression survives.*

---

### ? PREREQUISITE GATE
You are STRICTLY FORBIDDEN from running /deepdive-code unless /deepdive-docs has been run recently and the 	ools/SK8Lytz_App_Master_Reference.md is guaranteed to be accurate. If the Master Reference is stale, my auditors will go rogue and report false positives.

---

### ? Phase 1 — Dual Vector Partitioning

The codebase is audited orthogonally. 

#### Vector Alpha (Domain Agents)
1 agent per domain (currently 16). They understand the holistic context of their silo.
1. IDENTITY | 2. BLE_CORE | 3. GROUP_SYNC | 4. UI_CONTROLS | 5. DATA_LAYER | 6. UTILS | 7. NATIVE_&_WATCH | 8. NOTIFICATIONS_&_ROUTING | 9. SESSION_TRACKING | 10. HARDWARE_PROTOCOLS | 11. CLOUD_FUNCTIONS | 12. THEME_&_ASSETS | 13. SIMULATION_&_MOCKS | 14. BUILD_CONFIG_&_OTA | 15. OS_PERMISSIONS | 16. DEPENDENCY_AUDIT

#### Vector Beta (Rule Snipers)
1 agent per rule (currently 21 and growing). They scan the ENTIRE codebase globally for their specific anti-pattern.
- **[R-01] Queue Enforcement**: Bypassing BleWriteQueue.
- **[R-02] Fire-and-Forget**: Missing WRITE_TYPE_NO_RESPONSE.
- **[R-03] Auto-Reconnects**: Missing backoff/jitter.
- **[R-04] Telemetry Context**: Errors logged without payload_size/ssi.
- **[R-05] Offline-First**: Bypassing AsyncStorage caching.
- **[R-06] Error Handling**: Missing standard e instanceof Error unwrapping.
- **[R-07] Performance Guardrails**: Inline functions/styles in FlatList.
- **[R-08] Type Safety**: Hunting ny casts or @ts-ignore.
- **[R-09] PII Scrubbing**: Leaking emails/names in AppLogger.
- **[R-10] Group Connectivity**: Sequential group writes instead of concurrent mapped writes.
- **[R-11] Promise/IO Safety**: Missing 	ry/catch on async networks.
- **[R-12] Stale Closures**: Missing useRef in intervals/listeners.
- **[R-13] GATT Collision**: Promise.all used on device connections.
- **[R-14] State Matrix**: Missing Loading/Error/Empty UI states.
- **[R-15] Auth Context Bypassing**: Direct supabase.auth.getUser() calls.
- **[R-16] Hardcoded Delays**: setTimeout used instead of queue delays.
- **[R-17] Event Listener Leaks**: Missing useEffect cleanups.
- **[R-18] Boolean Traps**: Scattered booleans instead of FSMs (isConnecting && !hasError).
- **[R-19] HAL Enclosure**: Raw byte arrays ( x59) constructed outside src/protocols/.
- **[R-20] OS Variance Parity**: Missing Platform.select(), blind cross-platform assumptions (e.g., assuming Android's 20-byte MTU applies to iOS, or missing Foreground Service checks).
- **[R-21] Split-Brain & Duplication**: Hunting for duplicate functions, hooks, state variables, or redundant API calls. Flag legacy/abandoned implementations, ensure only the single source of truth is used, and queue tasks to consolidate or delete stale duplicates.

---

### ? Phase 2 — The Fleet Launch

Blake executes invoke_subagent twice: once for Vector Alpha, once for Vector Beta.

**Vector Alpha Directive (Domain Agents):**
> You are a QA Auditor Node assigned to the [DOMAIN_NAME] domain. Read the Protocol Bibles. View EVERY file in your domain. Audit them against ALL 21 Guardrails. Output a strict Bug Checklist.

**Vector Beta Directive (Rule Snipers):**
> You are a QA Sniper Node. Your ONLY target is Rule [R-XX]. Use grep_search and AST analysis across the ENTIRE src/ directory to hunt for this exact anti-pattern. You do not care about context; you are a ruthless bounty hunter for this specific violation. Output a strict Bug Checklist.

---

### ? Phase 3 — The Triage Synthesis

Once all sub-agents (Domains + Snipers) report back:
1. Blake dedupes the findings (Snipers and Domain Agents will often flag the same exact line).
2. Blake synthesizes the unique findings into system_audit_report.md.
3. Blake drafts a formatted [BATCH:deep-dive-remediation] containing ALL identified issues. Do NOT append tasks directly to the Bucket List. You must first route all findings through the `/intake` workflow or explicitly generate `PLAN-*.md` files in `docs/plans/` before appending verified tasks to the Triage Queue.
