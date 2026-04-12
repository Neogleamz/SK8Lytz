# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

<!-- AUTO_SYNC_ERRORS_START -->
<!-- AUTO_SYNC_ERRORS_END -->

- [ ] `fix/ble-queue-and-timeouts` : Implement a global Mutex/Promise queue for all BLE writes to prevent GATT internal crashes. Add hard timeouts (e.g. 5000ms) to all device connection attempts to prevent infinite freeze. Complete the implementation of the `isDisconnecting` latch. → [Plan](docs/plans/fix-ble-queue-and-timeouts.md)
- [ ] `fix/auth-page-scrolling` : NO vertical scrolling on auth page. Ensure the layout stops overflowing vertically, and shrink the "Continue offline" section to fit. → [Plan](docs/plans/fix-auth-page-scrolling.md)
- [ ] `fix/dynamic-arch-regressions` : Resolve 'isHaloz' ReferenceError in ProductVisualizer and perform a sanitization audit (DockedController, ZenggeProtocol, Setup Wizard) to remove remaining hardcoded binary logic. → [Plan](docs/plans/fix-dynamic-arch-regressions.md)
- [ ] `fix/tsc-errors-audit` : Fix TypeScript errors remaining from dynamic-arch-regressions (Audio namespace, missing EventType for 'BUILDER_PRESET_SAVED' in DockedController, IVoiceAction/Typography Subheader in DashboardScreen). → [Plan](docs/plans/fix-typescript-debt-audit.md)
- [ ] `feat/battery-health-predict` : Mathematical power modeling to predict battery life based on pattern draw; auto-dims to 20% at critical reserve. → [Plan](docs/plans/feat-battery-health-predict.md)
- [ ] `hw-test/protocol-voltage-sniff` : Deep-dive into 0x63 response and other telemetry bytes to identify raw battery voltage / state-of-charge data. → [Plan](docs/plans/hw-test-protocol-voltage-sniff.md)
- [ ] `feat/siri-google-assistant-integration` : Integrate Siri Shortcuts (iOS) and Google Assistant App Actions (Android) for phone-level voice control. When outside the app, dispatching commands like "Hey Siri, set SK8Lytz to red glow" should deep-link and trigger the BLE command. When inside the app, use the existing in-app voice engine instead. → [Plan](docs/plans/feat-siri-google-assistant-integration.md)
- [ ] `feat/geofence-rink-sync` : GPS-based rink detection to auto-trigger Crew Hub discovery and session joining. → [Plan](docs/plans/feat-geofence-rink-sync.md)

### Target: `epic/ui-refinement`

- [x] `feat/modern-avatar-color-picker` : Replace legacy color dots with the new modern RGB slider for avatar color customization in Crew Hub and User Account screens.
- [ ] `gate-offline-mode` : Gate off online capabilities when in offline mode (Crew Hub, Community Favorites, SK8Lytz Picks). Ensure Crew Hub card stays on dashboard but displays an "Offline" warning. → [Plan](docs/plans/gate-offline-mode.md)
- [x] `feat/dashboard-offline-crew-card-teaser` : Shrink Crew Card on Dashboard when offline to act as an unavailable teaser instead of fully collapsing it.

### Target: `epic/crew-hub-overhaul`

- [ ] `refactor-crew-modal` : #3 — `CrewModal.tsx` refactor — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase. → [Plan](docs/plans/refactor-crew-modal.md)
- [ ] `feat/advanced-map-integration` : Verify and rework map integration in Crew Hub and "Live Near Me" (post-modal rebuild). Add a live map showing the selected radius and a dot for the session location. Also beef up map integration within the scheduler. → [Plan](docs/plans/feat-advanced-map-integration.md)

### Target: `epic/music-mode-parity`

- [x] `lab-music-mode-parity` : #13 — Lab 0x73 Music Mode parity — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source. → [Plan](docs/plans/lab-music-mode-parity.md)
- [ ] `fix-music-mode-color` : #14 — Music Mode: Sound column/drop color not applied — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied. → [Plan](docs/plans/fix-music-mode-color.md)

---

## 🟠 Medium Priority / Next Sprint

### Target: `epic/admin-tools`

- [ ] `feat/picks-scheduler-builder` : Revise the SK8Lytz picks scheduler algorithms to finalize the assignment mode logic, and integrate an administrative version of the array builder to create custom community picks on the fly. also allow access to patterns in program mode for assignment → [Plan](docs/plans/build-picks-scheduler.md)
- [x] `feat/product-catalog-icons` : Design unique brand icons/illustrations for HALOZ, SOULZ, and RAILZ products (SVG or PNG assets). Integrate them as selectable icon fields in the Product Manager (Admin Tools) and display them in product selector chips, the Setup Wizard product picker, and the Dashboard device card.

### Target: `epic/camera-mode`

### Target: `epic/visualizer-parity`

- [ ] `tune-visualizer-pro-effects` : #15 — Visualizer Parity: Pro Effects Patterns — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer. → [Plan](docs/plans/tune-visualizer-pro-effects.md)

### Target: `epic/device-management`

- [ ] `feat/account-devices-management` : Account Manager - Add a 'Registered Device Groups' section in the Devices tab. Allow users to view, edit, and delete device groups. Group deletions must correctly purge the devices from the database (if online) and `AsyncStorage` (whether offline or online) to ensure they actually disappear. → [Plan](docs/plans/feat-account-devices-management.md)

### Target: `epic/protocol-integration`

- [ ] `feat/hardware-abstraction-layer` : Architect a hardware controller abstraction layer to decouple the UI from explicit `ZenggeProtocol` functions, preparing the app to support and dynamically map new OEM hardware controllers to the existing UI. → [Plan](docs/plans/feat-hardware-abstraction-layer.md)

### Target: `epic/ui-refinement`

- [ ] `fix/setup-wizard-button-overlap` : Fix the "Complete Setup" button overlapping the device card on the "Name Your Skates" page so users can change the LED count. → [Plan](docs/plans/fix-setup-wizard-button-overlap.md)

- [ ] `audit/offline-profile-settings` : Audit whether all user profile settings (display name, avatar, preferences) are fully cached locally for offline use. Implement offline-safe username rename that queues the update to Supabase on reconnect — same pattern as device registration sync. → [Plan](docs/plans/audit-offline-profile-settings.md)
- [ ] `feat/app-wide-ux-tips` : Design and implement a contextual tips system that surfaces short, dismissible tooltips throughout the app at key friction points (e.g. first Bluetooth scan, first controller open, first crew join). Tips should only show once per user (AsyncStorage) and feel native to the SK8Lytz aesthetic. → [Plan](docs/plans/feat-app-wide-ux-tips.md)

---

## 🟡 Backlog

### Target: `epic/telemetry-audit`

### Target: `epic/security-audit`

- [ ] `audit-rls-performance` : #20 — Security & Performance Review — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges. → [Plan](docs/plans/audit-rls-performance.md)
- [ ] `fix/typescript-debt-audit` : Resolve pre-existing TS errors across the codebase: dead state vars (`setDemoHaloQueued/SoulQueued`) in DashboardScreen, `CustomGroup` type drift, `'UNKNOWN'` product type overlap in HardwareSetupWizard, missing `group_id` in useRegistration legacy migration helper, missing EventType entries in PositionalGradientBuilder, implicit `any` params in LocationService, missing `IVoiceAction` type in DashboardScreen, missing `useRegistration` import in `Sk8LytzDiagnosticLab_old`, and narrow `DeviceSettingsModal` type props. → [Plan](docs/plans/fix-typescript-debt-audit.md)

### Target: `epic/community-hub`

- [ ] `integrate-builder-presets` : #27 — Community Hub: Builder Preset Integration — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library. → [Plan](docs/plans/integrate-builder-presets.md)

### Target: `epic/skate-spot-discovery`

- [ ] `feat/usa-skate-spots-dataset` : Build a live, US-only dataset of rinks and parks (hours, adult nights, surface types) for map overlays and a new 'Find a Place to Skate' discovery feature integrated into Crew Hub and the Main Dashboard. → [Plan](docs/plans/feat-usa-skate-spots-dataset.md)
- [ ] `feat/interactive-skate-spot-map` : Implement a high-density, interactive skate spot map using react-native-maps and clustering. Feature verified custom DB rinks vs. unverified Google Places fallbacks, including a 'Claim & Complete' bottom sheet UI to crowdsource specialized metadata (surface types, adult nights). → [Plan](docs/plans/feat-interactive-skate-spot-map.md)

### Target: `epic/ui-refinement`

- [ ] `feat/lab-ui-modernization` : Modernize and style the LED Diagnostic Lab layout to match the aesthetics, typography, and input styling of the rest of the app. → [Plan](docs/plans/feat-lab-ui-modernization.md)
- [ ] `feat/neogleamz-brand-presence` : Integrate Neogleamz parent brand identity into the app — e.g. "SK8Lytz by Neogleamz" wordmark, prominent Neogleamz branding on the Auth/Welcome screen, app store identity alignment. Design direction TBD — will brainstorm placement and treatment before executing. → [Plan](docs/plans/feat-neogleamz-brand-presence.md)
- [x] `feat/support-store-link` : Add a "Visit Store" link to the support/help section of the app (e.g. the support icon menu or Account screen). Should open the Neogleamz Shopify store URL via Linking.openURL in the native browser.
- [x] `feat/shopify-support-form` : Embed or deep-link to the Neogleamz Shopify contact/support form within the app's support flow. Evaluate whether a WebView embed or a native Linking.openURL to the Shopify contact page is the right approach based on authentication requirements.

---

## ❄️ Icebox / Backburner (Manual Trigger Only)

*Items placed here are explicitly ignored by the automatic queue. They will act as a catalog for future features that require manual authorization to begin work on.*

- [ ] `add-swipe-nav` : #34 — Card Swipe Navigation — Add the ability to swipe left and right to navigate back and forth between cards (Favorites, Picks, Presets) for a more fluid mobile UX. → [Plan](docs/plans/add-swipe-nav.md)

---

## ✅ Completed This Session (Apr 2026)

- [x] `epic/connection-reliability` : Execute the multiphase connection reliability plan (see `docs/plans/epic-connection-reliability.md`). Stop dropping connections, fix lockups entering/leaving controller, and fix UI teardown on disconnect (resolves `fix/hardware-connection-drop`, `audit/connection-polling-logic`, and `fix/controller-navigation-lockup`).
- [x] `fix-ble-audit` : Debug and resolve BLE connection regression. Devices not found or connecting slowly.
- [x] `feat/speed-tracking-telemetry` : Complete end-to-end session telemetry — SpeedTrackingService, SessionSummaryModal with dynamic speed-zone accent colour, Statistics tab in AccountModal (lifetime grid + recent session history), DockedController RECORD/SAVE button with live GPS accumulation.
- [x] `feat/voice-command-engine` : Implement core offline voice resolution and UI bridge natural language to BLE payloads for safe operation while skating.
- [x] `feat/voice-first-use-tooltip` : On first launch of the VoiceFAB, show a dismissible "What can I say?" tutorial popup listing example commands (modes, favorites by name, brightness, speed, spatial). Persist dismissed state to AsyncStorage.
- [x] `feat/empty-skates-setup-cta` : On the Dashboard, if no device groups exist (offline or online), render a contextual "Set Up Your Skates →" CTA button beneath the "My Skates" section header. The button should open the Hardware Setup Wizard and be completely hidden once at least one group/device is registered. Condition must work in both offline and authenticated states.

## ✅ Completed Previously

---
*Last updated: 2026-04-09 | This session: IDE Rules configured for auto-branching.*
