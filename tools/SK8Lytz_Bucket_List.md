# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

<!-- AUTO_SYNC_ERRORS_START -->
<!-- AUTO_SYNC_ERRORS_END -->

- [x] `feat/speed-tracking-telemetry` : Add real-time distance (miles), average speed (mph), average g-force and peak speed tracking to Street Mode and Crew Sessions and User Account Details new Statistics Tab, saving to Supabase and local storage and displaying in a post-session summary.

### Target: `epic/connection-reliability`

- [ ] `epic/connection-reliability` : Execute the multiphase connection reliability plan (see `docs/plans/epic-connection-reliability.md`). Stop dropping connections, fix lockups entering/leaving controller, and fix UI teardown on disconnect (resolves `fix/hardware-connection-drop`, `audit/connection-polling-logic`, and `fix/controller-navigation-lockup`).

- [ ] `fix/dynamic-arch-regressions` : Resolve 'isHaloz' ReferenceError in ProductVisualizer and perform a sanitization audit (DockedController, ZenggeProtocol, Setup Wizard) to remove remaining hardcoded binary logic.
- [ ] `feat/battery-health-predict` : Mathematical power modeling to predict battery life based on pattern draw; auto-dims to 20% at critical reserve.
- [ ] `hw-test/protocol-voltage-sniff` : Deep-dive into 0x63 response and other telemetry bytes to identify raw battery voltage / state-of-charge data.
- [x] `feat/voice-command-engine` : Implement core offline voice resolution and UI bridge natural language to BLE payloads for safe operation while skating.
- [ ] `feat/siri-google-assistant-integration` : Integrate Siri Shortcuts (iOS) and Google Assistant App Actions (Android) for phone-level voice control. When outside the app, dispatching commands like "Hey Siri, set SK8Lytz to red glow" should deep-link and trigger the BLE command. When inside the app, use the existing in-app voice engine instead.
- [x] `feat/voice-first-use-tooltip` : On first launch of the VoiceFAB, show a dismissible "What can I say?" tutorial popup listing example commands (modes, favorites by name, brightness, speed, spatial). Persist dismissed state to AsyncStorage.
- [ ] `feat/geofence-rink-sync` : GPS-based rink detection to auto-trigger Crew Hub discovery and session joining.

### Target: `epic/ui-refinement`

- [ ] `feat/modern-avatar-color-picker` : Replace legacy color dots with the new modern RGB slider for avatar color customization in Crew Hub and User Account screens.
- [ ] `gate-offline-mode` : Gate off online capabilities when in offline mode (Crew Hub, Community Favorites, SK8Lytz Picks). Ensure Crew Hub card stays on dashboard but displays an "Offline" warning.
- [ ] `feat/dashboard-offline-crew-card-teaser` : Shrink Crew Card on Dashboard when offline to act as an unavailable teaser instead of fully collapsing it.

### Target: `epic/device-registration`

- [x] `feat/empty-skates-setup-cta` : On the Dashboard, if no device groups exist (offline or online), render a contextual "Set Up Your Skates →" CTA button beneath the "My Skates" section header. The button should open the Hardware Setup Wizard and be completely hidden once at least one group/device is registered. Condition must work in both offline and authenticated states.

### Target: `epic/crew-hub-overhaul`

- [ ] `refactor-crew-modal` : #3 — `CrewModal.tsx` refactor — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] `feat/advanced-map-integration` : Verify and rework map integration in Crew Hub and "Live Near Me" (post-modal rebuild). Add a live map showing the selected radius and a dot for the session location. Also beef up map integration within the scheduler.

### Target: `epic/music-mode-parity`

- [ ] `lab-music-mode-parity` : #13 — Lab 0x73 Music Mode parity — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [ ] `fix-music-mode-color` : #14 — Music Mode: Sound column/drop color not applied — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied.

---

## 🟠 Medium Priority / Next Sprint

### Target: `epic/admin-tools`

- [ ] `feat/picks-scheduler-builder` : Revise the SK8Lytz picks scheduler algorithms to finalize the assignment mode logic, and integrate an administrative version of the array builder to create custom community picks on the fly. also allow access to patterns in program mode for assignment
- [x] `feat/product-catalog-icons` : Design unique brand icons/illustrations for HALOZ, SOULZ, and RAILZ products (SVG or PNG assets). Integrate them as selectable icon fields in the Product Manager (Admin Tools) and display them in product selector chips, the Setup Wizard product picker, and the Dashboard device card.

### Target: `epic/camera-mode`

### Target: `epic/visualizer-parity`

- [ ] `tune-visualizer-pro-effects` : #15 — Visualizer Parity: Pro Effects Patterns — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer.

### Target: `epic/device-management`

### Target: `epic/protocol-integration`

- [ ] `feat/hardware-abstraction-layer` : Architect a hardware controller abstraction layer to decouple the UI from explicit `ZenggeProtocol` functions, preparing the app to support and dynamically map new OEM hardware controllers to the existing UI.

### Target: `epic/ui-refinement`

- [ ] `audit/offline-profile-settings` : Audit whether all user profile settings (display name, avatar, preferences) are fully cached locally for offline use. Implement offline-safe username rename that queues the update to Supabase on reconnect — same pattern as device registration sync.
- [ ] `feat/app-wide-ux-tips` : Design and implement a contextual tips system that surfaces short, dismissible tooltips throughout the app at key friction points (e.g. first Bluetooth scan, first controller open, first crew join). Tips should only show once per user (AsyncStorage) and feel native to the SK8Lytz aesthetic.

---

## 🟡 Backlog

### Target: `epic/telemetry-audit`

### Target: `epic/security-audit`

- [ ] `audit-rls-performance` : #20 — Security & Performance Review — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.
- [ ] `fix/typescript-debt-audit` : Resolve pre-existing TS errors across the codebase: dead state vars (`setDemoHaloQueued/SoulQueued`) in DashboardScreen, `CustomGroup` type drift, `'UNKNOWN'` product type overlap in HardwareSetupWizard, missing `group_id` in useRegistration legacy migration helper, missing EventType entries in PositionalGradientBuilder, implicit `any` params in LocationService, missing `IVoiceAction` type in DashboardScreen, missing `useRegistration` import in `Sk8LytzDiagnosticLab_old`, and narrow `DeviceSettingsModal` type props.

### Target: `epic/community-hub`

- [ ] `integrate-builder-presets` : #27 — Community Hub: Builder Preset Integration — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library.

### Target: `epic/skate-spot-discovery`

- [ ] `feat/usa-skate-spots-dataset` : Build a live, US-only dataset of rinks and parks (hours, adult nights, surface types) for map overlays and a new 'Find a Place to Skate' discovery feature integrated into Crew Hub and the Main Dashboard.

### Target: `epic/ui-refinement`

- [ ] `feat/lab-ui-modernization` : Modernize and style the LED Diagnostic Lab layout to match the aesthetics, typography, and input styling of the rest of the app.
- [ ] `feat/neogleamz-brand-presence` : Integrate Neogleamz parent brand identity into the app — e.g. "SK8Lytz by Neogleamz" wordmark, prominent Neogleamz branding on the Auth/Welcome screen, app store identity alignment. Design direction TBD — will brainstorm placement and treatment before executing.
- [x] `feat/support-store-link` : Add a "Visit Store" link to the support/help section of the app (e.g. the support icon menu or Account screen). Should open the Neogleamz Shopify store URL via Linking.openURL in the native browser.
- [x] `feat/shopify-support-form` : Embed or deep-link to the Neogleamz Shopify contact/support form within the app's support flow. Evaluate whether a WebView embed or a native Linking.openURL to the Shopify contact page is the right approach based on authentication requirements.

---

## ❄️ Icebox / Backburner (Manual Trigger Only)

*Items placed here are explicitly ignored by the automatic queue. They will act as a catalog for future features that require manual authorization to begin work on.*

- [ ] `add-swipe-nav` : #34 — Card Swipe Navigation — Add the ability to swipe left and right to navigate back and forth between cards (Favorites, Picks, Presets) for a more fluid mobile UX.

---

## ✅ Completed This Session (Apr 2026)

- [x] `feat/speed-tracking-telemetry` : Complete end-to-end session telemetry — SpeedTrackingService, SessionSummaryModal with dynamic speed-zone accent colour, Statistics tab in AccountModal (lifetime grid + recent session history), DockedController RECORD/SAVE button with live GPS accumulation.

## ✅ Completed Previously

---
*Last updated: 2026-04-09 | This session: IDE Rules configured for auto-branching.*
