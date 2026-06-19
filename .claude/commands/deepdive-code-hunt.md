# /deepdive-code-hunt — Map-Reduce QA Fleet (Mapper)

**Description:** The Map-Reduce QA Fleet (Mapper) — Orthogonal Auditing Hunt. Spawns 55 Domain Agents and Snipers to read the codebase and dump their findings to the filesystem.
**Persona:** 🔬 QA — Blake

> **🔬 QA — Blake | Map-Reduce Orchestration Active**
> *I am deploying the Tri-Vector Fleet: Domain Agents to audit silos, Rule Snipers to hunt specific anti-patterns globally, and Structural Snipers to detect code duplication. This is the Hunt phase. Agents will write to disk, not to the chat window.*

---

### PREREQUISITE GATE
You are STRICTLY FORBIDDEN from running /deepdive-code-hunt unless /deepdive-docs has been run recently and the `docs/SK8Lytz_App_Master_Reference.md` is guaranteed to be accurate. If the Master Reference is stale, my auditors will go rogue and report false positives.

---

### Phase 0 — Model Verification & Output Directory

1. **Model Check:** Ask the user: *"Confirm your Model Selection is set to a high-context model (Gemini 2.5 Flash recommended). Type 'confirmed' to proceed."*
2. **Output Directory:** Verify `artifacts/deepdive_raw/` is available. Previous run outputs will be archived to `artifacts/deepdive_raw_archive_<timestamp>/` before the new run begins.
3. **Canary Validation:** Launch 1 Domain Agent (Domain 9: `UTILS` — smallest domain) and 1 Rule Sniper (`R-08: Type Safety`) as canaries. Wait for both to complete and verify output matches the required JSON schema below.

---

### Phase 1 — Tri-Vector Partitioning

#### Vector Alpha (Domain Agents — 25 agents)
1 agent per domain. They audit against ALL 30 Guardrails.

| # | Domain | Target Directories |
|---|---|---|
| 1 | **IDENTITY** | `src/context/AuthContext.tsx`, `src/services/AuthProfileService.ts`, `src/services/AuthUtils.ts`, `src/services/ProfileService.ts`, `src/components/account/*`, `src/components/auth/*`, `src/hooks/useAccountOverview.ts`, `src/hooks/useDashboardProfile.ts`, `src/hooks/useRegistration.ts` |
| 2 | **BLE_CORE** | `src/services/ble/*`, `src/services/Ble*.ts`, `src/hooks/useBLE.ts`, `src/hooks/ble/*`, `src/hooks/useOptimisticBLE.ts`, `src/context/BLEContext.tsx` |
| 3 | **GROUP_SYNC** | `src/services/GroupRepository.ts`, `src/services/CrewService.ts`, `src/components/crew/*`, `src/context/CrewContext.tsx`, `src/hooks/useCrewHub.ts`, `src/hooks/useCrewSession.ts` |
| 4 | **UI_SCREENS** | `src/screens/*`, `src/components/dashboard/*`, `src/components/shared/*`, `src/components/DeviceItem.tsx` |
| 5 | **UI_DOCKED_CONTROLLER** | `src/components/DockedController.tsx`, `src/components/docked/*`, `src/hooks/useDashboardController.tsx`, `src/hooks/useDockedControllerState.ts` |
| 6 | **UI_MODALS** | `src/components/AccountModal.tsx`, `src/components/DeviceSettingsModal.tsx`, `src/components/modals/*` |
| 7 | **UI_VISUALIZER** | `src/components/VisualizerUnit.tsx`, `src/components/ProductVisualizer.tsx`, `src/components/patterns/*` |
| 8 | **DATA_LAYER** | `src/services/DeviceRepository.ts`, `src/services/TelemetryService.ts`, `src/types/supabase.ts`, `src/services/supabaseClient.ts`, `src/hooks/cloud/*` |
| 9 | **UTILS** | `src/utils/*`, `src/types/*` (except `supabase.ts`) |
| 10 | **NATIVE_&_WATCH** | `android/*`, `ios/*`, `targets/watch/*` |
| 11 | **NOTIFICATIONS_&_ROUTING** | `App.tsx`, `src/providers/*`, `src/services/NotificationService.ts`, `src/services/LocationService.ts` |
| 12 | **SESSION_TRACKING** | `src/context/SessionContext.tsx`, `src/hooks/useSessionTracking.ts`, `src/hooks/useGlobalTelemetry.ts` |
| 13 | **PROTOCOL_CORE** | `src/protocols/ZenggeProtocol.ts`, `src/protocols/ZenggeAdapter.ts`, `src/protocols/BanlanxAdapter.ts`, `src/protocols/IControllerProtocol.ts`, `src/protocols/ControllerRegistry.ts` |
| 14 | **PATTERN_ENGINE** | `src/protocols/PatternEngine.ts`, `src/protocols/SpatialEngine.ts`, `src/protocols/SymphonyEngine.ts`, `src/protocols/VisualizerEngine.ts` |
| 15 | **CLOUD_FUNCTIONS** | `supabase/functions/*`, `supabase/migrations/*` |
| 16 | **THEME_&_ASSETS** | `src/theme/*`, `src/styles/*`, `src/constants/*`, `src/assets/*` |
| 17 | **SIMULATION_&_MOCKS** | `src/mocks/*`, `src/__mocks__/*`, `__tests__/*` |
| 18 | **BUILD_CONFIG** | `app.config.js`, `app.json`, `eas.json`, `metro.config.js`, `babel.config.js`, `tsconfig.json`, `jest.config.js`, `package.json` |
| 19 | **OS_PERMISSIONS** | `android/app/src/main/AndroidManifest.xml`, `ios/*/Info.plist` |
| 20 | **ADMIN_&_TELEMETRY** | `src/components/admin/*`, `src/services/AppLogger.ts`, `src/hooks/useAdminSettings.ts` |
| 21 | **DEPENDENCY_AUDIT** | `package.json`, `package-lock.json` |
| 22 | **DEVOPS_&_TOOLING** | `.github/workflows/*`, `tools/*`, `.husky/*` |
| 23 | **ANIMATION_&_PERFORMANCE** | Files importing `react-native-reanimated`, `@shopify/react-native-skia`, `react-native-gesture-handler` |
| 24 | **ACCESSIBILITY_&_I18N** | Global scan of `src/components/*` looking for `accessible={true}`, `accessibilityRole`, and `i18n.t()` |
| 25 | **THE_TEST_SUITE** | `__tests__/*`, `src/**/__tests__/*` |

#### Vector Beta (Rule Snipers — 29 agents)

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
| **R-22** | Memory Leak Patterns | `setInterval` without `clearInterval`, `addEventListener` without `removeEventListener` |
| **R-23** | Monolith Detection | Any `.tsx` or `.ts` file exceeding 30KB |
| **R-24** | AsyncStorage Key Collision | Duplicate `AsyncStorage.setItem()`/`getItem()` calls using the same key from different files |
| **R-25** | Unguarded Platform API | Code accessing platform-specific APIs without a `Platform.OS` guard |
| **R-26** | Re-entrancy Races | Async functions called from `setInterval` or `useEffect` without a boolean re-entrancy guard |
| **R-27** | Context Consumer Depth | Components that consume 4+ React Contexts directly |
| **R-28** | FlatList Bottlenecks | Inline arrow functions passed to `renderItem` or `keyExtractor` in `FlatList` |
| **R-29** | Circular Dependencies | Services importing other services that import them back |
| **R-30** | Zombie Tests | Tests containing `.skip()`, `test.todo()`, or commented-out assertions |

#### Vector Gamma (Structural Snipers — 1 agent)

| ID | Rule | What to Hunt |
|---|---|---|
| **R-21** | Split-Brain & Duplication | Duplicate functions, hooks, state variables, or redundant API calls |

---

### Required Output Schema (ALL Agents)

**EVERY sub-agent MUST write its output as a valid JSON file conforming to this schema:**

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

---

### Phase 2 — The Fleet Launch

**CRITICAL MANDATE FOR ALL SUBAGENTS:**
> You are STRICTLY FORBIDDEN from using `send_message` to report findings back to the parent agent. Instead, MUST use the write tool. Output findings as a **strict JSON object** conforming to the Required Output Schema and save it to `artifacts/deepdive_raw/<Your_Agent_ID>_findings.json`. Once the file is written, silently terminate.

---

### Phase 3 — Completion Detection

After dispatching the fleet:
1. Expected: **55 files** (25 domain + 29 sniper + 1 structural).
2. If count < 55: identify which agents are missing, wait and repeat.
3. If count == 55: notify the user and proceed to the model switch step.
4. If 3 polling cycles pass with no new files: report the incomplete agents and proceed with available data.

---

### Phase 4 — Switch to Synthesis
Once the fleet has completed:
1. Notify the user: *"Hunt complete. [X]/55 agents reported. [Y] findings total across all reports. Ready for synthesis."*
2. Instruct the user to switch their global Model Selection to a reasoning-heavy model (e.g., Claude Sonnet 4).
3. Execute `/deepdive-code-synthesis` to complete the workflow.
