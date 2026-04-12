# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

<!-- AUTO_SYNC_ERRORS_START -->
<!-- AUTO_SYNC_ERRORS_END -->

- [ ] `fix/dynamic-arch-regressions` : Resolve 'isHaloz' ReferenceError in ProductVisualizer and perform a sanitization audit (DockedController, ZenggeProtocol, Setup Wizard) to remove remaining hardcoded binary logic.
- [ ] `fix/tsc-errors-audit` : Fix TypeScript errors remaining from dynamic-arch-regressions (Audio namespace, missing EventType for 'BUILDER_PRESET_SAVED' in DockedController, IVoiceAction/Typography Subheader in DashboardScreen).
- [ ] `fix/rls-telemetry-block` : [CRITICAL] Resolve 403 Forbidden errors on `parsed_session_stats`, `device_diagnostics`, and `parsed_session_devices`. Likely RLS policy mismatch for authenticated telemetry sinks. → [Plan](docs/plans/fix-rls-telemetry-block.md)
- [ ] `fix/device-group-fk-integrity` : [CRITICAL] Resolve foreign key constraint violations in `registered_devices` group_id. Upsert logic is referencing non-existent groups. → [Plan](docs/plans/fix-device-group-fk-integrity.md)
- [ ] `feat/eula-onboarding` : Implement mandatory EULA acceptance in Auth flow and global version enforcement → [Plan](docs/plans/feat-eula-onboarding.md)
- [ ] `feat/admin-app-manager` : Finalized Governance Hub with Safety Locks (Consolidated Scope) → [Plan](docs/plans/feat-admin-app-manager.md)

### Target: `epic/ui-refinement`

- [ ] `gate-offline-mode` : Gate off online capabilities when in offline mode (Crew Hub, Community Favorites, SK8Lytz Picks). Ensure Crew Hub card stays on dashboard but displays an "Offline" warning. we need to reall talk about this!!!! and build a great plan.

### Target: `epic/crew-hub-overhaul`

- [ ] `refactor-crew-modal` : #3 — `CrewModal.tsx` refactor — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] `feat/advanced-map-integration` : Verify and rework map integration in Crew Hub and "Live Near Me" (post-modal rebuild). Add a live map showing the selected radius and a dot for the session location. Also beef up map integration within the scheduler.

---

## 🟠 Medium Priority / Next Sprint

### Target: `epic/admin-tools`

- [x] `fix/admin-modal-ts-debt` : Fix 4 pre-existing TS errors in AdminToolsModal.tsx: (1) EVENT_META missing new event types, (2) blankProfile() schema drift, (3) missing supabase import, (4) dead tab logic.

### Target: `epic/camera-mode`

- [x] `fix/camera-mode-ux-permissions` : Fix camera mode permission flow and expand camera viewport layout → [Plan](docs/plans/fix-camera-mode-ux-permissions.md)

### Target: `epic/visualizer-parity`

- [ ] `tune-visualizer-pro-effects` : #15 — Visualizer Parity: Pro Effects Patterns — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer.

### Target: `epic/device-management`
- [ ] `audit/device-group-persistence` : Audit device group state handling to prevent duplicate/ghost groups and ensure seamless offline-to-online persistence. → [Plan](docs/plans/audit-device-group-persistence.md)

### Target: `epic/protocol-integration`

- [ ] `feat/hardware-abstraction-layer` : Architect a hardware controller abstraction layer to decouple the UI from explicit `ZenggeProtocol` functions, preparing the app to support and dynamically map new OEM hardware controllers to the existing UI.

### Target: `epic/ui-refinement`

- [ ] `audit/offline-profile-settings` : Audit whether all user profile settings (display name, avatar, preferences) are fully cached locally for offline use. Implement offline-safe username rename that queues the update to Supabase on reconnect — same pattern as device registration sync.
- [ ] `feat/app-wide-ux-tips` : Design and implement a contextual tips system that surfaces short, dismissible tooltips throughout the app at key friction points (e.g. first Bluetooth scan, first controller open, first crew join). Tips should only show once per user (AsyncStorage) and feel native to the SK8Lytz aesthetic.

---

## 🟡 Backlog

### Target: `epic/telemetry-audit`

- [ ] `feat/street-mode-telemetry-overhaul` : Overhaul Street Mode with always-on car-dashboard telemetry, data-dense metrics grid, and auto-scaling gauges. → [Plan](docs/plans/feat-street-mode-telemetry-overhaul.md)

### Target: `epic/security-audit`

- [ ] `audit-rls-performance` : #20 — Security & Performance Review — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.
- [ ] `fix/typescript-debt-audit` : Resolve pre-existing TS errors across the codebase: dead state vars (`setDemoHaloQueued/SoulQueued`) in DashboardScreen, `CustomGroup` type drift, `'UNKNOWN'` product type overlap in HardwareSetupWizard, missing `group_id` in useRegistration legacy migration helper, missing EventType entries in PositionalGradientBuilder, implicit `any` params in LocationService, missing `IVoiceAction` type in DashboardScreen, missing `useRegistration` import in `Sk8LytzDiagnosticLab_old`, and narrow `DeviceSettingsModal` type props.

### Target: `epic/community-hub`

- [ ] `integrate-builder-presets` : #27 — Community Hub: Builder Preset Integration — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library.

### Target: `epic/skate-spot-discovery`

- [ ] `feat/usa-skate-spots-dataset` : Build a live, US-only dataset of rinks and parks (hours, adult nights, surface types) for map overlays and a new 'Find a Place to Skate' discovery feature integrated into Crew Hub and the Main Dashboard. [Governance Gated]
- [ ] `feat/interactive-skate-spot-map` : Implement a high-density, interactive skate spot map using react-native-maps and clustering. Feature verified custom DB rinks vs. unverified Google Places fallbacks, including a 'Claim & Complete' bottom sheet UI to crowdsource specialized metadata (surface types, adult nights). [Governance Gated] → [Plan](docs/plans/feat-interactive-skate-spot-map.md)

### Target: `epic/ui-refinement`

---

## ❄️ Icebox / Backburner (Manual Trigger Only)

*Items placed here are explicitly ignored by the automatic queue. They will act as a catalog for future features that require manual authorization to begin work on.*

- [ ] `feat/neogleamz-brand-presence` : Integrate Neogleamz parent brand identity into the app — e.g. "SK8Lytz by Neogleamz" wordmark, prominent Neogleamz branding on the Auth/Welcome screen, app store identity alignment. Design direction TBD — will brainstorm placement and treatment before executing.
- [ ] `feat/battery-health-predict` : Mathematical power modeling to predict battery life based on pattern draw; auto-dims to 20% at critical reserve.
- [ ] `hw-test/protocol-voltage-sniff` : Deep-dive into 0x63 response and other telemetry bytes to identify raw battery voltage / state-of-charge data.
- [ ] `feat/siri-google-assistant-integration` : Integrate Siri Shortcuts (iOS) and Google Assistant App Actions (Android) for phone-level voice control. When outside the app, dispatching commands like "Hey Siri, set SK8Lytz to red glow" should deep-link and trigger the BLE command. When inside the app, use the existing in-app voice engine instead.
- [ ] `feat/geofence-rink-sync` : GPS-based rink detection to auto-trigger Crew Hub discovery and session joining.
- [ ] `add-swipe-nav` : #34 — Card Swipe Navigation — Add the ability to swipe left and right to navigate back and forth between cards (Favorites, Picks, Presets) for a more fluid mobile UX.

---

## ✅ Completed Previously

- [x] `feat/admin-hub-design-system` : Standardize all 6 admin sub-tool headers — canonical back arrow (→ AdminTools hub), dark-mode `useTheme()` theming throughout. Extracted App Settings and Product Manager from inline `formSheet` Modals to proper `fullScreen` Modals. Replaced crude button list with glass-morphism `ToolCard` grid on the TOOLS tab. Eliminated all hardcoded `#FFFFFF`/`#CCCCCC` light theme values.
- [x] `chore/delete-orphan-backup` : Delete `src/components/Sk8LytzDiagnosticLab_old.tsx` — orphaned backup file causing 11 TS compile errors. It is unreferenced by any import and should be permanently removed.
- [x] `lab-music-mode-parity` : #13 — Lab 0x73 Music Mode parity — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [x] `epic/connection-reliability` : Execute the multiphase connection reliability plan (see `docs/plans/epic-connection-reliability.md`). Stop dropping connections, fix lockups entering/leaving controller, and fix UI teardown on disconnect (resolves `fix/hardware-connection-drop`, `audit/connection-polling-logic`, and `fix/controller-navigation-lockup`).
- [x] `fix-ble-audit` : Debug and resolve BLE connection regression. Devices not found or connecting slowly.
- [x] `feat/speed-tracking-telemetry` : Complete end-to-end session telemetry — SpeedTrackingService, SessionSummaryModal with dynamic speed-zone accent colour, Statistics tab in AccountModal (lifetime grid + recent session history), DockedController RECORD/SAVE button with live GPS accumulation.
- [x] `feat/voice-command-engine` : Implement core offline voice resolution and UI bridge natural language to BLE payloads for safe operation while skating.
- [x] `feat/voice-first-use-tooltip` : On first launch of the VoiceFAB, show a dismissible "What can I say?" tutorial popup listing example commands (modes, favorites by name, brightness, speed, spatial). Persist dismissed state to AsyncStorage.
- [x] `feat/empty-skates-setup-cta` : On the Dashboard, if no device groups exist (offline or online), render a contextual "Set Up Your Skates →" CTA button beneath the "My Skates" section header. The button should open the Hardware Setup Wizard and be completely hidden once at least one group/device is registered. Condition must work in both offline and authenticated states.
- [x] `feat/modern-avatar-color-picker` : Replace legacy color dots with the new modern RGB slider for avatar color customization in Crew Hub and User Account screens.
- [x] `fix-music-mode-color` : #14 — Music Mode: Sound column/drop color not applied — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied.
- [x] `feat/picks-scheduler-builder` : Revise the SK8Lytz picks scheduler algorithms to finalize the assignment mode logic, and integrate an administrative version of the array builder to create custom community picks on the fly. also allow access to patterns in program mode for assignment → [Plan](docs/plans/feat-picks-scheduler-builder.md)
- [x] `feat/product-catalog-icons` : Design unique brand icons/illustrations for HALOZ, SOULZ, and RAILZ products (SVG or PNG assets). Integrate them as selectable icon fields in the Product Manager (Admin Tools) and display them in product selector chips, the Setup Wizard product picker, and the Dashboard device card.
- [x] `feat/account-devices-management` : Account Manager - Add a 'Registered Device Groups' section in the Devices tab. Allow users to view, edit, and delete device groups. Group deletions must correctly purge the devices from the database (if online) and `AsyncStorage` (whether offline or online) to ensure they actually disappear.
- [x] `feat/support-store-link` : Add a "Visit Store" link to the support/help section of the app (e.g. the support icon menu or Account screen). Should open the Neogleamz Shopify store URL via Linking.openURL in the native browser.
- [x] `feat/shopify-support-form` : Embed or deep-link to the Neogleamz Shopify contact/support form within the app's support flow. Evaluate whether a WebView embed or a native Linking.openURL to the Shopify contact page is the right approach based on authentication requirements.

---
*Last updated: 2026-04-12 | This session: Core features and admin hub stabilization complete.*
