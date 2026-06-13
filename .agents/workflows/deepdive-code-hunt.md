---
description: The Map-Reduce QA Fleet (Mapper) — Orthogonal Auditing Hunt. Spawns 48 Domain Agents and Snipers to read the codebase and dump their findings to the filesystem.
persona_entry: "🕵️ QA — Blake"
team_roster: .agents/team-roster.md
---

# Deep-Dive Code Hunt (The Orthogonal QA Fleet - Mapper)

> **🕵️ QA — Blake | Map-Reduce Orchestration Active**
> *I am deploying the Tri-Vector Fleet: Domain Agents to audit silos, Rule Snipers to hunt specific anti-patterns globally, and Structural Snipers to detect code duplication. This is the Hunt phase. We offload reading to a heavy context model. Agents will write to disk, not to the chat window.*

---

### 🛑 PREREQUISITE GATE
You are STRICTLY FORBIDDEN from running /deepdive-code-hunt unless /deepdive-docs has been run recently and the `docs/SK8Lytz_App_Master_Reference.md` is guaranteed to be accurate. If the Master Reference is stale, my auditors will go rogue and report false positives.

**USER ACTION REQUIRED:** Ensure your active Model Selection is set to a model with a massive context window (e.g., Gemini 2.5 Flash/Pro) before triggering this workflow to save on costs and processing time.

---

### 🎯 Phase 0 — Model Verification & Output Directory

1. **Model Check:** Ask the user: *"Confirm your Model Selection is set to a high-context model (Gemini 2.5 Flash recommended). Type 'confirmed' to proceed."*
2. **Output Directory:** Verify `artifacts/deepdive_raw/` is available. Previous run outputs will be archived to `artifacts/deepdive_raw_archive_<timestamp>/` before the new run begins.
3. **Canary Validation:** Launch 1 Domain Agent (Domain 9: `UTILS` — smallest domain) and 1 Rule Sniper (`R-08: Type Safety`) as canaries. Wait for both to complete and verify output matches the required JSON schema below. If canary outputs are malformed → fix prompt and retry canaries. If valid → proceed to full fleet launch.

---

### 🗺️ Phase 1 — Tri-Vector Partitioning

The codebase is audited orthogonally.

#### Vector Alpha (Domain Agents — 25 agents)
1 agent per domain. They understand the holistic context of their silo and audit against ALL 30 Guardrails.

| # | Domain | Target Directories |
|---|---|---|
| 1 | **IDENTITY** | `src/context/AuthContext.tsx`, `src/services/AuthProfileService.ts`, `src/services/AuthUtils.ts`, `src/services/ProfileService.ts`, `src/services/ProfileService.types.ts`, `src/components/account/*`, `src/components/auth/*`, `src/hooks/useAccountOverview.ts`, `src/hooks/useDashboardProfile.ts`, `src/hooks/useRegistration.ts` |
| 2 | **BLE_CORE** | `src/services/ble/*`, `src/services/Ble*.ts` (BleConnectionManager, BleLifecycleManager, BlePingService, BleSessionFactory, BleWriteDispatcher, BleWriteQueue, BleCharacteristicCache), `src/hooks/useBLE.ts`, `src/hooks/ble/*`, `src/hooks/useOptimisticBLE.ts`, `src/context/BLEContext.tsx` |
| 3 | **GROUP_SYNC** | `src/services/GroupRepository.ts`, `src/services/CrewService.ts`, `src/services/CrewProfileService.ts`, `src/components/crew/*`, `src/components/CrewModal.tsx`, `src/components/CrewMemberDashboard.tsx`, `src/context/CrewContext.tsx`, `src/hooks/useCrewHub.ts`, `src/hooks/useCrewManage.ts`, `src/hooks/useCrewSession.ts`, `src/hooks/useCrewProximityRadar.ts`, `src/hooks/useDashboardCrew.ts`, `src/hooks/useDashboardGroups.ts` |
| 4 | **UI_SCREENS** | `src/screens/*`, `src/components/dashboard/*`, `src/components/shared/*`, `src/components/DeviceItem.tsx`, `src/components/LocationPicker*.tsx`, `src/components/SkateSpotBottomSheet.tsx` |
| 5 | **UI_DOCKED_CONTROLLER** | `src/components/DockedController.tsx`, `src/components/docked/*`, `src/hooks/useDashboardController.tsx`, `src/hooks/useDockedControllerState.ts`, `src/hooks/useControllerDispatch.ts`, `src/hooks/useControllerAnalytics.ts` |
| 6 | **UI_MODALS** | `src/components/AccountModal.tsx`, `src/components/DeviceSettingsModal.tsx`, `src/components/CommunityModal.tsx`, `src/components/GroupSettingsModal.tsx`, `src/components/SessionSummaryModal.tsx`, `src/components/modals/*`, `src/components/CustomSlider.tsx`, `src/components/TacticalSlider.tsx`, `src/components/MarqueeText.tsx`, `src/components/ConnectionStrengthBadge.tsx` |
| 7 | **UI_VISUALIZER** | `src/components/VisualizerUnit.tsx`, `src/components/ProductVisualizer.tsx`, `src/components/LEDStripPreview.tsx`, `src/components/CustomEffectVisualizer.tsx`, `src/components/NeonHueStrip.tsx`, `src/components/PositionalGradientBuilder.tsx`, `src/components/VerticalPatternDrum.tsx`, `src/components/patterns/*`, `src/components/CameraTracker.*` |
| 8 | **DATA_LAYER** | `src/services/DeviceRepository.ts`, `src/services/TelemetryService.ts`, `src/services/ScenesService.ts`, `src/services/SpeedTrackingService.ts`, `src/services/GradientsService.ts`, `src/services/SkateSpotsService.ts`, `src/services/SessionShareService.ts`, `src/types/supabase.ts`, `src/services/supabaseClient.ts`, `src/hooks/cloud/*`, `src/hooks/useFavorites.ts`, `src/hooks/useScenes.ts`, `src/hooks/useCuratedPicks.ts`, `src/hooks/useGradients.ts`, `src/hooks/useSkateStats.ts`, `src/hooks/useRecentSpots.ts`, `src/hooks/useMapFilters.ts`, `src/context/FavoritesContext.tsx` |
| 9 | **UTILS** | `src/utils/*`, `src/types/*` (except `supabase.ts`) |
| 10 | **NATIVE_&_WATCH** | `android/*`, `ios/*`, `targets/watch/*` |
| 11 | **NOTIFICATIONS_&_ROUTING** | `App.tsx`, `src/providers/*`, `src/services/NotificationService.ts`, `src/services/PushTokenService.ts`, `src/services/LocationService.ts`, `src/hooks/useHardwareNotifications.ts` |
| 12 | **SESSION_TRACKING** | `src/context/SessionContext.tsx`, `src/hooks/useSessionTracking.ts`, `src/hooks/useGlobalTelemetry.ts`, `src/hooks/useHealthTelemetry.ts`, `src/hooks/useTelemetryLedger.ts`, `src/hooks/useDeviceStateLedger.ts`, `src/services/HealthSyncService.ts` |
| 13 | **PROTOCOL_CORE** | `src/protocols/ZenggeProtocol.ts`, `src/protocols/ZenggeAdapter.ts`, `src/protocols/BanlanxAdapter.ts`, `src/protocols/IControllerProtocol.ts`, `src/protocols/ControllerRegistry.ts`, `src/hooks/useProtocolDispatch.ts`, `src/hooks/useProtocolBuilder.ts`, `src/hooks/useProductCatalog.ts`, `src/hooks/useProductManager.ts`, `src/constants/ProductCatalog.ts` |
| 14 | **PATTERN_ENGINE** | `src/protocols/PatternEngine.ts`, `src/protocols/SpatialEngine.ts`, `src/protocols/SymphonyEngine.ts`, `src/protocols/VisualizerEngine.ts`, `src/protocols/PositionalMathBuffer.ts`, `src/hooks/useStreetMode.ts`, `src/hooks/useMusicMode.ts`, `src/hooks/useAppMicrophone.ts` |
| 15 | **CLOUD_FUNCTIONS** | `supabase/functions/*`, `supabase/migrations/*` |
| 16 | **THEME_&_ASSETS** | `src/theme/*`, `src/styles/*`, `src/constants/*` (except `ProductCatalog.ts`), `src/assets/*` |
| 17 | **SIMULATION_&_MOCKS** | `src/mocks/*`, `src/__mocks__/*`, `__tests__/*` |
| 18 | **BUILD_CONFIG** | `app.config.js`, `app.json`, `eas.json`, `metro.config.js`, `babel.config.js`, `tsconfig.json`, `jest.config.js`, `package.json`, `.husky/*` |
| 19 | **OS_PERMISSIONS** | `android/app/src/main/AndroidManifest.xml`, `ios/*/Info.plist` |
| 20 | **ADMIN_&_TELEMETRY** | `src/components/admin/*`, `src/services/AppLogger.ts`, `src/services/AppSettingsService.ts`, `src/hooks/useAdminSettings.ts`, `src/hooks/useAdminTelemetry.ts`, `src/hooks/useDiagnosticLog.ts` |
| 21 | **DEPENDENCY_AUDIT** | `package.json`, `package-lock.json` |
| 22 | **DEVOPS_&_TOOLING** | `.github/workflows/*`, `tools/*`, `.husky/*`, `.agents/rules/*` |
| 23 | **ANIMATION_&_PERFORMANCE** | Files importing `react-native-reanimated`, `@shopify/react-native-skia`, `react-native-gesture-handler` |
| 24 | **ACCESSIBILITY_&_I18N** | Global scan of `src/components/*` looking for `accessible={true}`, `accessibilityRole`, and `i18n.t()` |
| 25 | **THE_TEST_SUITE** | `__tests__/*`, `src/**/__tests__/*` |

#### Vector Beta (Rule Snipers — 29 agents)
1 agent per rule. They scan the ENTIRE codebase globally for their specific anti-pattern.

| ID | Rule | What to Hunt |
|---|---|---|
| **R-01** | Queue Enforcement | Bypassing `BleWriteQueue` — direct `writeCharacteristic` calls |
| **R-02** | Fire-and-Forget | Missing `WRITE_TYPE_NO_RESPONSE` on BLE writes |
| **R-03** | Auto-Reconnects | Missing backoff/jitter in retry loops |
| **R-04** | Telemetry Context | Errors logged without `payload_size`/`ssi` context |
| **R-05** | Offline-First | Bypassing AsyncStorage caching for data that should work offline |
| **R-06** | Error Handling | Missing standard `e instanceof Error` unwrapping in catch blocks |
| **R-07** | Performance Guardrails | Inline functions/styles in FlatList `renderItem`, missing `useCallback`/`useMemo` |
| **R-08** | Type Safety | Hunting `any` casts, `@ts-ignore`, `as unknown as` type laundering |
| **R-09** | PII Scrubbing | Leaking emails/names/MACs in `AppLogger` telemetry payloads |
| **R-10** | Group Connectivity | Sequential group writes instead of concurrent `Promise.all` mapped writes |
| **R-11** | Promise/IO Safety | Missing `try/catch` on async network/storage operations |
| **R-12** | Stale Closures | Missing `useRef` forwarding for callbacks used in `setInterval`/event listeners |
| **R-13** | GATT Collision | `Promise.all` used on sequential device connections (must be serial) |
| **R-14** | State Matrix | Missing Loading/Error/Empty UI states in data-driven components |
| **R-15** | Auth Context Bypassing | Direct `supabase.auth.getUser()` calls instead of consuming `AuthContext` |
| **R-16** | Hardcoded Delays | `setTimeout` used instead of queue-managed delays |
| **R-17** | Event Listener Leaks | Missing `useEffect` cleanup for subscriptions/listeners |
| **R-18** | Boolean Traps | Scattered `isX`/`hasY` booleans instead of FSM state unions |
| **R-19** | HAL Enclosure | Raw byte arrays (`0x59`, `0x55`, etc.) constructed outside `src/protocols/` |
| **R-20** | OS Variance Parity | Missing `Platform.select()`, blind cross-platform assumptions |
| **R-22** | Memory Leak Patterns | `setInterval` without `clearInterval`, `addEventListener` without `removeEventListener`, `subscribe` without `unsubscribe`, `Animated.Value` listeners without cleanup |
| **R-23** | Monolith Detection | Any `.tsx` or `.ts` file exceeding 30KB — flag for mandatory component extraction |
| **R-24** | AsyncStorage Key Collision | Duplicate `AsyncStorage.setItem()`/`getItem()` calls using the same key from different files. Cross-reference against Master Reference §2 AsyncStorage Key Registry for undocumented keys |
| **R-25** | Unguarded Platform API | Code accessing platform-specific APIs (`requestMTU`, `requestConnectionPriority`, `Foreground Service`, `CBCentralManager`) without a `Platform.OS` or `Platform.select()` guard |
| **R-26** | Re-entrancy Races | Async functions called from `setInterval` or `useEffect` without a boolean re-entrancy guard (e.g., `_isFlushing` pattern). These cause double-INSERT and duplicate network request bugs |
| **R-27** | Context Consumer Depth | Components that consume 4+ React Contexts directly — performance risk, every context change re-renders the component. Flag for `useMemo` wrapping or context consolidation |
| **R-28** | FlatList Bottlenecks | Inline arrow functions passed to `renderItem` or `keyExtractor` in `FlatList` components; missing `initialNumToRender` or `windowSize` on large lists |
| **R-29** | Circular Dependencies | Services importing other services that import them back (e.g. A -> B -> A), causing `undefined` module errors at runtime |
| **R-30** | Zombie Tests | Tests containing `.skip()`, `test.todo()`, or assertions that are commented out (`// expect(...)`) |

#### Vector Gamma (Structural Snipers — 1 agent)
1 agent to detect Split-Brain Code Duplication across the entire codebase.

| ID | Rule | What to Hunt |
|---|---|---|
| **R-21** | Split-Brain & Duplication | Duplicate functions, hooks, state variables, or redundant API calls. Flag legacy/abandoned implementations, ensure only the single source of truth is used, and queue tasks to consolidate or delete stale duplicates |

---

### 📋 Required Output Schema (ALL Agents)

**EVERY sub-agent MUST write its output as a valid JSON file conforming to this schema.** Non-conforming output will be rejected by the Synthesis workflow.

```json
{
  "agent_id": "DOMAIN_BLE_CORE | R-08 | R-21",
  "agent_type": "domain | sniper | structural",
  "domain_or_rule": "BLE Protocol Core | Type Safety | Split-Brain",
  "files_scanned": 42,
  "completion_status": "COMPLETE | PARTIAL | ERROR",
  "error_details": null,
  "findings": [
    {
      "id": "BLE_CORE-001",
      "file": "src/hooks/useBLE.ts",
      "line": 47,
      "rule_violated": "R-08",
      "severity": "HIGH | MEDIUM | LOW",
      "description": "any cast on writeToDevice return value",
      "code_snippet": "const result = await writeToDevice(payload) as any;",
      "suggested_fix": "Replace with Promise<boolean | 'partial'>",
      "false_positive_risk": "LOW | MEDIUM | HIGH"
    }
  ],
  "summary": {
    "total_findings": 3,
    "high": 1,
    "medium": 1,
    "low": 1,
    "false_positives_noted": 0
  }
}
```

**File naming convention:** `artifacts/deepdive_raw/<agent_id>_findings.json`
- Domain agents: `DOMAIN_BLE_CORE_findings.json`
- Rule snipers: `R-08_findings.json`
- Structural snipers: `R-21_findings.json`

---

### 🚀 Phase 2 — The Fleet Launch

Blake executes `invoke_subagent` for all Domains and Snipers.

**CRITICAL MANDATE FOR ALL SUBAGENTS (ANTI-CONTEXT-EXPLOSION RULE):**
> You are STRICTLY FORBIDDEN from using the `send_message` tool to report your findings back to the parent agent. Reporting back via message will crash the context limit.
> Instead, you MUST use the `write_to_file` tool. Output your findings as a **strict JSON object conforming to the Required Output Schema above** and save it to `artifacts/deepdive_raw/<Your_Agent_ID>_findings.json`. Once the file is written, silently terminate.

**Vector Alpha Directive (Domain Agents):**
> You are a QA Auditor Node assigned to the `[DOMAIN_NAME]` domain. Read the Master Reference and Protocol Bible for context. View EVERY file in your domain. Audit them against ALL 30 Guardrails (R-01 through R-30). Write a strict JSON Bug Checklist conforming to the Required Output Schema to `artifacts/deepdive_raw/DOMAIN_[DOMAIN_NAME]_findings.json`. Do NOT use `send_message`.

**Vector Beta Directive (Rule Snipers):**
> You are a QA Sniper Node. Your ONLY target is Rule `[R-XX]`: `[RULE_DESCRIPTION]`. Use `grep_search` and AST analysis across the ENTIRE `src/` directory to hunt for this exact anti-pattern. You do not care about domain context; you are a ruthless bounty hunter for this specific violation. Write a strict JSON Bug Checklist conforming to the Required Output Schema to `artifacts/deepdive_raw/R-XX_findings.json`. Do NOT use `send_message`.

**Vector Gamma Directive (Structural Snipers):**
> You are a QA Structural Sniper Node. Your ONLY target is detecting Split-Brain overlaps (Rule R-21). Cross-reference similar hooks, services, and state derivations across the entire `src/` directory. Write a strict JSON redundancy map conforming to the Required Output Schema to `artifacts/deepdive_raw/R-21_findings.json`. Do NOT use `send_message`.

---

### ⏱️ Phase 3 — Completion Detection

After dispatching the fleet:

1. Set a **5-minute timer** using the `schedule` tool.
2. On wake: `list_dir` on `artifacts/deepdive_raw/`.
3. Count output files. **Expected: 55 files** (25 domain + 29 sniper + 1 structural).
4. If count < 55: identify which agents are missing, set another **3-minute timer**, and repeat.
5. If count == 55: notify the user and proceed to the model switch step.
6. If **3 polling cycles** pass with no new files appearing: report the incomplete agents to the user and proceed with available data.

**Verification:** Before declaring the hunt complete, spot-check 3 random output files to verify they conform to the Required Output Schema. If any are malformed, log the agent ID and note it for the Synthesis workflow.

---

### 🏁 Phase 4 — Switch to Synthesis
Once the fleet has completed and all outputs are verified:
1. Notify the user: *"Hunt complete. [X]/55 agents reported. [Y] findings total across all reports. Ready for synthesis."*
2. Instruct the user to switch their global Model Selection to a reasoning-heavy model (e.g., Claude Sonnet 4).
3. Execute `/deepdive-code-synthesis` to complete the workflow.
