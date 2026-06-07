---
description: The Map-Reduce Cartography Fleet — Spawns 21 sub-agents to read the live codebase and rebuild the Master Reference without destroying historical data.
persona_entry: "📋 Docs — Avery"
team_roster: .agents/team-roster.md
---

# Deep-Dive Docs (The Cartographers)

> **📋 Docs — Avery | Map-Reduce Orchestration Active**
> *I do not tolerate data loss. If code is removed, its history is archived, never deleted. We spin up the fleet, read the code, and compile the ultimate architectural truth into the Master Reference.*

---

### ⛔ THE BOUNDARY MARKER LAW
The Master Reference contains historical decisions, condemned opcodes, and hardware constraints that MUST NOT be deleted. Sub-agents are **strictly forbidden** from overwriting the entire file. All injected content MUST be placed exactly between the `<!-- CARTOGRAPHER_START: [DOMAIN] -->` and `<!-- CARTOGRAPHER_END: [DOMAIN] -->` markers.

### ⛔ THE 100% NON-DESTRUCTIVE ARCHIVAL PROTOCOL
If the Cartographers discover that a documented feature, hook, or opcode no longer exists in the live codebase, **they must NOT delete it**. They must flag it as `[MOVE_TO_ARCHIVE]`. Avery will physically move the dead entry to **Section 13: Historical Archive (The Graveyard)** at the bottom of the Master Reference.

---

### ⚡ Phase 0 — Boundary Marker Verification

Before launching any sub-agents, Avery MUST:
1. `view_file` the Master Reference and verify that all 21 `<!-- CARTOGRAPHER_START: [DOMAIN] -->` / `<!-- CARTOGRAPHER_END: [DOMAIN] -->` marker pairs exist.
2. If ANY marker pair is missing, Avery MUST create it in the correct section before launching the fleet.
3. Also verify the output directory exists: `list_dir` on `artifacts/deepdive_docs/`. If it doesn't exist, it will be created automatically by the first sub-agent's `write_to_file`.

---

### ⚡ Phase 1 — Fleet Partitioning

Avery divides the codebase into 21 domains for independent auditing.

| # | Domain | Target Directories | Output Marker Name |
|---|---|---|---|
| 1 | **Identity & Auth** | `src/context/AuthContext.tsx`, `src/services/AuthProfileService.ts`, `src/services/AuthUtils.ts`, `src/services/ProfileService.ts`, `src/services/ProfileService.types.ts`, `src/components/account/*`, `src/components/auth/*`, `src/hooks/useAccountOverview.ts`, `src/hooks/useDashboardProfile.ts`, `src/hooks/useRegistration.ts` | `IDENTITY` |
| 2 | **BLE Protocol Core** | `src/services/ble/*`, `src/services/Ble*.ts` (BleConnectionManager, BleLifecycleManager, BlePingService, BleSessionFactory, BleWriteDispatcher, BleWriteQueue, BleCharacteristicCache), `src/hooks/useBLE.ts`, `src/hooks/ble/*`, `src/hooks/useOptimisticBLE.ts`, `src/context/BLEContext.tsx` | `BLE_CORE` |
| 3 | **Group Sync & Swarm** | `src/services/GroupRepository.ts`, `src/services/CrewService.ts`, `src/services/CrewProfileService.ts`, `src/components/crew/*`, `src/components/CrewModal.tsx`, `src/components/CrewMemberDashboard.tsx`, `src/context/CrewContext.tsx`, `src/hooks/useCrewHub.ts`, `src/hooks/useCrewManage.ts`, `src/hooks/useCrewSession.ts`, `src/hooks/useCrewProximityRadar.ts`, `src/hooks/useDashboardCrew.ts`, `src/hooks/useDashboardGroups.ts` | `GROUP_SYNC` |
| 4 | **UI Screens & Dashboard** | `src/screens/*`, `src/components/dashboard/*`, `src/components/shared/*`, `src/components/DeviceItem.tsx`, `src/components/LocationPicker*.tsx`, `src/components/SkateSpotBottomSheet.tsx` | `UI_SCREENS` |
| 5 | **UI Docked Controller** | `src/components/DockedController.tsx`, `src/components/docked/*`, `src/hooks/useDashboardController.tsx`, `src/hooks/useDockedControllerState.ts`, `src/hooks/useControllerDispatch.ts`, `src/hooks/useControllerAnalytics.ts` | `UI_DOCKED_CONTROLLER` |
| 6 | **UI Modals & Settings** | `src/components/AccountModal.tsx`, `src/components/DeviceSettingsModal.tsx`, `src/components/CommunityModal.tsx`, `src/components/GroupSettingsModal.tsx`, `src/components/SessionSummaryModal.tsx`, `src/components/modals/*`, `src/components/CustomSlider.tsx`, `src/components/TacticalSlider.tsx`, `src/components/MarqueeText.tsx`, `src/components/ConnectionStrengthBadge.tsx` | `UI_MODALS` |
| 7 | **UI Visualizer & Patterns** | `src/components/VisualizerUnit.tsx`, `src/components/ProductVisualizer.tsx`, `src/components/LEDStripPreview.tsx`, `src/components/CustomEffectVisualizer.tsx`, `src/components/NeonHueStrip.tsx`, `src/components/PositionalGradientBuilder.tsx`, `src/components/VerticalPatternDrum.tsx`, `src/components/patterns/*`, `src/components/CameraTracker.*` | `UI_VISUALIZER` |
| 8 | **Data Layer & Offline Sync** | `src/services/DeviceRepository.ts`, `src/services/TelemetryService.ts`, `src/services/ScenesService.ts`, `src/services/SpeedTrackingService.ts`, `src/services/GradientsService.ts`, `src/services/SkateSpotsService.ts`, `src/services/SessionShareService.ts`, `src/types/supabase.ts`, `src/services/supabaseClient.ts`, `src/hooks/cloud/*`, `src/hooks/useFavorites.ts`, `src/hooks/useScenes.ts`, `src/hooks/useCuratedPicks.ts`, `src/hooks/useGradients.ts`, `src/hooks/useSkateStats.ts`, `src/hooks/useRecentSpots.ts`, `src/hooks/useMapFilters.ts`, `src/context/FavoritesContext.tsx` | `DATA_LAYER` |
| 9 | **Utilities & Types** | `src/utils/*`, `src/types/*` (except `supabase.ts`) | `UTILS` |
| 10 | **Native & WatchOS** | `android/*`, `ios/*`, `targets/watch/*` | `NATIVE_&_WATCH` |
| 11 | **Notifications & Routing** | `App.tsx`, `src/providers/*`, `src/services/NotificationService.ts`, `src/services/PushTokenService.ts`, `src/services/LocationService.ts`, `src/hooks/useHardwareNotifications.ts` | `NOTIFICATIONS_&_ROUTING` |
| 12 | **Session Tracking** | `src/context/SessionContext.tsx`, `src/hooks/useSessionTracking.ts`, `src/hooks/useGlobalTelemetry.ts`, `src/hooks/useHealthTelemetry.ts`, `src/hooks/useTelemetryLedger.ts`, `src/hooks/useDeviceStateLedger.ts`, `src/services/HealthSyncService.ts` | `SESSION_TRACKING` |
| 13 | **Protocol Core** | `src/protocols/ZenggeProtocol.ts`, `src/protocols/ZenggeAdapter.ts`, `src/protocols/BanlanxAdapter.ts`, `src/protocols/IControllerProtocol.ts`, `src/protocols/ControllerRegistry.ts`, `src/hooks/useProtocolDispatch.ts`, `src/hooks/useProtocolBuilder.ts`, `src/hooks/useProductCatalog.ts`, `src/hooks/useProductManager.ts`, `src/constants/ProductCatalog.ts` | `PROTOCOL_CORE` |
| 14 | **Pattern Engine** | `src/protocols/PatternEngine.ts`, `src/protocols/SpatialEngine.ts`, `src/protocols/SymphonyEngine.ts`, `src/protocols/VisualizerEngine.ts`, `src/protocols/PositionalMathBuffer.ts`, `src/hooks/useStreetMode.ts`, `src/hooks/useMusicMode.ts`, `src/hooks/useAppMicrophone.ts` | `PATTERN_ENGINE` |
| 15 | **Cloud Functions** | `supabase/functions/*`, `supabase/migrations/*` | `CLOUD_FUNCTIONS` |
| 16 | **Theme & Assets** | `src/theme/*`, `src/styles/*`, `src/constants/*` (except `ProductCatalog.ts`), `src/assets/*` | `THEME_&_ASSETS` |
| 17 | **Simulation & Mocks** | `src/mocks/*`, `src/__mocks__/*`, `__tests__/*` | `SIMULATION_&_MOCKS` |
| 18 | **Build Config** | `app.config.js`, `app.json`, `eas.json`, `metro.config.js`, `babel.config.js`, `tsconfig.json`, `jest.config.js`, `package.json`, `.husky/*` | `BUILD_CONFIG` |
| 19 | **OS Permissions** | `android/app/src/main/AndroidManifest.xml`, `ios/*/Info.plist` | `OS_PERMISSIONS` |
| 20 | **Admin & Telemetry** | `src/components/admin/*`, `src/services/AppLogger.ts`, `src/services/AppSettingsService.ts`, `src/hooks/useAdminSettings.ts`, `src/hooks/useAdminTelemetry.ts`, `src/hooks/useDiagnosticLog.ts` | `ADMIN_&_TELEMETRY` |
| 21 | **Dependency Audit** | `package.json`, `package-lock.json` | `DEPENDENCY_AUDIT` |

---

### ⚡ Phase 2 — The Fleet Launch

Avery executes `invoke_subagent` to spawn 21 `research` sub-agents simultaneously.

**⛔ ANTI-CONTEXT-EXPLOSION RULE (CRITICAL):**
> You are STRICTLY FORBIDDEN from using the `send_message` tool to report your findings back to the parent agent. Sending full Markdown payloads via message WILL crash the parent context.
> Instead, you MUST use the `write_to_file` tool. Write your findings to `artifacts/deepdive_docs/<Your_DOMAIN_Marker>_cartography.md`. Once the file is written, silently terminate.

**The Base Sub-Agent Directive (All Nodes):**
> You are an SDE Cartographer Node.
> 1. Read EVERY file in your assigned domain. Do NOT modify any code.
> 2. Build 5 "Elite Architecture" base sections for your domain:
>    - **File Manifest**: Every file listed with a 1-sentence architectural purpose.
>    - **Blast Radius**: What this domain imports, and what imports it.
>    - **Context Matrix**: What React Contexts are consumed or provided.
>    - **Hook/Service I/O Registry**: Inputs, outputs, and side-effects.
>    - **OS Variance Matrix**: Explicitly document any code paths that branch between iOS and Android (e.g., `Platform.OS === 'ios'`, native module bridging, MTU negotiations, foreground services).
> 3. **ARCHIVAL INSTRUCTION**: If you find stale documentation for your domain in the Master Reference, tag it with `[MOVE_TO_ARCHIVE]`.
> 4. **SEQUENCE DIAGRAM**: If your domain contains a complex multi-step process (e.g., Crew Creation, BLE Connection, OTA Update), you MUST generate a Mermaid Sequence Diagram (`sequenceDiagram`) detailing the exact actor-to-actor flow.
> 5. Write your final Markdown Payload to `artifacts/deepdive_docs/<DOMAIN_MARKER>_cartography.md` using the `write_to_file` tool. Do NOT use `send_message`.

**Domain-Specific Elite Directives:**
- **Node 2 (`BLE_CORE`)**: Must append **State Machine (FSM) Map**. Generate a Mermaid Diagram (`mermaid`) visualizing the exact XState transitions found in `BleMachine.ts`. Must also map the full BLE transport pipeline: `BleWriteQueue → BleWriteDispatcher → BleConnectionManager → GATT`.
- **Node 5 (`UI_DOCKED_CONTROLLER`)**: This domain contains the 67KB `DockedController.tsx` monolith. Map every hook it consumes, every ref it holds, and every callback it threads. Flag any component extraction opportunities.
- **Node 8 (`DATA_LAYER`)**: Must append **Database Schema & RLS Policies** and the **Environment/Secrets Manifest**. Must also map the offline sync queue architecture (ScenesService + SpeedTrackingService + useOfflineSyncWorker).
- **Nodes 4, 6, 7, 9, 16 (`UI_SCREENS`, `UI_MODALS`, `UI_VISUALIZER`, `UTILS`, `THEME_&_ASSETS`)**: Must append **Design System & Token Manifest**.
- **Node 13 (`PROTOCOL_CORE`)**: Must meticulously map the exact byte offsets, parsing arrays, and hardware capabilities defined in the adapter engines. Cross-reference against `ZENGGE_PROTOCOL_BIBLE.md`.
- **Node 14 (`PATTERN_ENGINE`)**: Must catalogue every pattern template in `SK8LYTZ_TEMPLATES`, its tier, colorMode, and the math engine function that generates it.
- **Node 18 (`BUILD_CONFIG`)**: Must map the release channel configurations, EAS update logic, native module requirements, and TypeScript compiler flags.
- **Node 20 (`ADMIN_&_TELEMETRY`)**: Must map the full AppLogger pipeline: event types, batching, debounce, Supabase upload, offline persist, and log clearing.

---

### ⚡ Phase 2.5 — Completion Detection

After dispatching all 21 sub-agents:

1. Set a 5-minute timer using the `schedule` tool.
2. On wake: `list_dir` on `artifacts/deepdive_docs/`.
3. Count output files. **Expected: 21 files** (one per domain, named `<DOMAIN_MARKER>_cartography.md`).
4. If count < 21: identify missing domains, set another 3-minute timer, and repeat.
5. If count == 21: proceed to Phase 3.
6. If 3 polling cycles pass with no new files: report the incomplete agents to the user and proceed with available data.

---

### ⚡ Phase 3 — The Synthesis

Once all 21 sub-agents have written their files to `artifacts/deepdive_docs/`:
1. Avery reads and verifies all 21 payloads using `view_file`.
2. Avery extracts all `[MOVE_TO_ARCHIVE]` items and safely appends them to Section 13 (The Graveyard).
3. Avery injects the 21 markdown payloads into their respective `<!-- CARTOGRAPHER_START -->` boundaries in `SK8Lytz_App_Master_Reference.md` using precise `replace_file_content` calls between the boundary markers. If a payload is too large for a single edit, Avery uses `write_to_file` with Overwrite to rebuild the section.
4. Avery outputs a summary of what was corrected, archived, or newly discovered.
