# /deepdive-docs — The Cartographers Fleet

**Description:** The Map-Reduce Cartography Fleet — Spawns 21 sub-agents to read the live codebase and rebuild the Master Reference without destroying historical data.
**Persona:** 📋 Docs — Avery

> **📋 Docs — Avery | Map-Reduce Orchestration Active**
> *I do not tolerate data loss. If code is removed, its history is archived, never deleted. We spin up the fleet, read the code, and compile the ultimate architectural truth into the Master Reference.*

---

### THE BOUNDARY MARKER LAW
The Master Reference contains historical decisions, condemned opcodes, and hardware constraints that MUST NOT be deleted. Sub-agents are **strictly forbidden** from overwriting the entire file. All injected content MUST be placed exactly between the `<!-- CARTOGRAPHER_START: [DOMAIN] -->` and `<!-- CARTOGRAPHER_END: [DOMAIN] -->` markers.

### THE 100% NON-DESTRUCTIVE ARCHIVAL PROTOCOL
If the Cartographers discover that a documented feature, hook, or opcode no longer exists in the live codebase, **they must NOT delete it**. They must flag it as `[MOVE_TO_ARCHIVE]`. Avery will physically move the dead entry to **Section 13: Historical Archive (The Graveyard)** at the bottom of the Master Reference.

---

### Phase 0 — Boundary Marker Verification

Before launching any sub-agents, Avery MUST:
1. View the Master Reference and verify that all 21 `<!-- CARTOGRAPHER_START: [DOMAIN] -->` / `<!-- CARTOGRAPHER_END: [DOMAIN] -->` marker pairs exist.
2. If ANY marker pair is missing, Avery MUST create it before launching the fleet.
3. Verify the output directory exists: `artifacts/deepdive_docs/`.

---

### Phase 1 — Fleet Partitioning

| # | Domain | Target Directories | Output Marker Name |
|---|---|---|---|
| 1 | **Identity & Auth** | `src/context/AuthContext.tsx`, `src/services/Auth*.ts`, `src/components/account/*`, `src/components/auth/*` | `IDENTITY` |
| 2 | **BLE Protocol Core** | `src/services/ble/*`, `src/services/Ble*.ts`, `src/hooks/useBLE.ts`, `src/hooks/ble/*`, `src/context/BLEContext.tsx` | `BLE_CORE` |
| 3 | **Group Sync & Swarm** | `src/services/GroupRepository.ts`, `src/services/CrewService.ts`, `src/components/crew/*`, `src/context/CrewContext.tsx`, `src/hooks/useCrewHub.ts` | `GROUP_SYNC` |
| 4 | **UI Screens & Dashboard** | `src/screens/*`, `src/components/dashboard/*`, `src/components/shared/*` | `UI_SCREENS` |
| 5 | **UI Docked Controller** | `src/components/DockedController.tsx`, `src/components/docked/*`, `src/hooks/useDashboardController.tsx` | `UI_DOCKED_CONTROLLER` |
| 6 | **UI Modals & Settings** | `src/components/AccountModal.tsx`, `src/components/DeviceSettingsModal.tsx`, `src/components/modals/*` | `UI_MODALS` |
| 7 | **UI Visualizer & Patterns** | `src/components/VisualizerUnit.tsx`, `src/components/ProductVisualizer.tsx`, `src/components/patterns/*` | `UI_VISUALIZER` |
| 8 | **Data Layer & Offline Sync** | `src/services/DeviceRepository.ts`, `src/services/TelemetryService.ts`, `src/types/supabase.ts`, `src/hooks/cloud/*` | `DATA_LAYER` |
| 9 | **Utilities & Types** | `src/utils/*`, `src/types/*` (except `supabase.ts`) | `UTILS` |
| 10 | **Native & WatchOS** | `android/*`, `ios/*`, `targets/watch/*` | `NATIVE_&_WATCH` |
| 11 | **Notifications & Routing** | `App.tsx`, `src/providers/*`, `src/services/NotificationService.ts`, `src/services/LocationService.ts` | `NOTIFICATIONS_&_ROUTING` |
| 12 | **Session Tracking** | `src/context/SessionContext.tsx`, `src/hooks/useSessionTracking.ts`, `src/hooks/useGlobalTelemetry.ts` | `SESSION_TRACKING` |
| 13 | **Protocol Core** | `src/protocols/ZenggeProtocol.ts`, `src/protocols/ZenggeAdapter.ts`, `src/protocols/BanlanxAdapter.ts`, `src/protocols/IControllerProtocol.ts`, `src/protocols/ControllerRegistry.ts` | `PROTOCOL_CORE` |
| 14 | **Pattern Engine** | `src/protocols/PatternEngine.ts`, `src/protocols/SpatialEngine.ts`, `src/protocols/SymphonyEngine.ts`, `src/protocols/VisualizerEngine.ts` | `PATTERN_ENGINE` |
| 15 | **Cloud Functions** | `supabase/functions/*`, `supabase/migrations/*` | `CLOUD_FUNCTIONS` |
| 16 | **Theme & Assets** | `src/theme/*`, `src/styles/*`, `src/constants/*`, `src/assets/*` | `THEME_&_ASSETS` |
| 17 | **Simulation & Mocks** | `src/mocks/*`, `src/__mocks__/*`, `__tests__/*` | `SIMULATION_&_MOCKS` |
| 18 | **Build Config** | `app.config.js`, `app.json`, `eas.json`, `metro.config.js`, `babel.config.js`, `tsconfig.json`, `jest.config.js`, `package.json` | `BUILD_CONFIG` |
| 19 | **OS Permissions** | `android/app/src/main/AndroidManifest.xml`, `ios/*/Info.plist` | `OS_PERMISSIONS` |
| 20 | **Admin & Telemetry** | `src/components/admin/*`, `src/services/AppLogger.ts`, `src/hooks/useAdminSettings.ts`, `src/hooks/useAdminTelemetry.ts` | `ADMIN_&_TELEMETRY` |
| 21 | **Dependency Audit** | `package.json`, `package-lock.json` | `DEPENDENCY_AUDIT` |

---

### Phase 2 — The Fleet Launch

**⛔ ANTI-CONTEXT-EXPLOSION RULE (CRITICAL):**
> Do NOT stream your findings inline back to the parent agent (the parent receives your final summary automatically when you return).
> Instead, MUST use the Write tool. Write findings to `artifacts/deepdive_docs/<Your_DOMAIN_Marker>_cartography.md`. Once the file is written, return only a one-line confirmation of the path.

**The Base Sub-Agent Directive (All Nodes):**
1. Read EVERY file in your assigned domain. Do NOT modify any code.
2. Build 5 "Elite Architecture" base sections: File Manifest, Blast Radius, Context Matrix, Hook/Service I/O Registry, OS Variance Matrix.
3. **ARCHIVAL INSTRUCTION**: If you find stale documentation for your domain, tag it with `[MOVE_TO_ARCHIVE]`.
4. **ARCHITECTURAL IMPACT FLAGS**: Append flags if your domain's architectural surface changes:
   - `[IMPACTS_USER_JOURNEY]`, `[IMPACTS_C4_CONTEXT]`, `[IMPACTS_STATE_CHART]`
5. **SEQUENCE DIAGRAM**: If your domain contains a complex multi-step process, generate a Mermaid Sequence Diagram.
6. Write final Markdown Payload to `artifacts/deepdive_docs/<DOMAIN_MARKER>_cartography.md`.

**Domain-Specific Elite Directives:**
- **Node 2 (`BLE_CORE`)**: Must append State Machine (FSM) Map as a Mermaid Diagram. Must map the full BLE transport pipeline.
- **Node 5 (`UI_DOCKED_CONTROLLER`)**: Map every hook, ref, and callback in the `DockedController.tsx` monolith.
- **Node 8 (`DATA_LAYER`)**: Must append Database Schema & RLS Policies and the offline sync queue architecture.

---

### Phase 2.5 — Completion Detection

Expected: **21 files** (one per domain).

---

### Phase 3 — The Synthesis

Once all 21 sub-agents have written their files:
1. Avery reads and verifies all 21 payloads.
2. Avery extracts all `[MOVE_TO_ARCHIVE]` items and safely appends them to Section 13 (The Graveyard).
3. Avery injects the 21 markdown payloads into their respective `<!-- CARTOGRAPHER_START -->` boundaries in `SK8Lytz_App_Master_Reference.md`.

---

### Phase 4 — High-Level Documentation Synthesis (Unconditional)

After the Master Reference is updated:
1. Open `docs/State_Charts_UX.md`. Update any XState or UX charts.
2. Open `docs/User_Journey_Maps.md`. Update any step-by-step flows that changed.
3. Open `docs/System_Context_Diagram.md`. Update if new external systems or hardware interfaces were added.

---

### Phase 5 — ADR Sync

1. Read `docs/SESSION_LOG.md` entirely for `[DECISION]` entries.
2. Filter for major architectural shifts.
3. Open `docs/Architecture_Decision_Records.md`.
4. Inject new ADR entries following the established format.

**MANDATORY FINAL STEP:** After Phase 4 and Phase 5 are complete, run `git add docs/*.md` and `git commit -m "docs: cartographer rebuild and satellite sync"`.

---

### Phase 6 — Final Summary & Attestation

Output a clear summary covering:
1. **Changes/Updates**: Which of the 21 domains had active code modifications.
2. **Archivals**: Any stale references flagged with `[MOVE_TO_ARCHIVE]`.
3. **Satellite Syncs**: Which high-level documents were verified or updated.
