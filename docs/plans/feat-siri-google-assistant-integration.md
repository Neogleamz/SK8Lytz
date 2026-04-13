# Plan: `feat/siri-google-assistant-integration`

### Design Decisions & Rationale

This is a two-platform native integration. On iOS, Siri Shortcuts via `expo-intent-launcher` or a custom native module allows the app to register custom Intent Definitions. On Android, Google Assistant App Actions hook into `android.app.action.MAIN`. Both paths deep-link into the app and trigger the in-app voice engine (`useVoiceControl`) to process the command string. When inside the app, the existing voice engine handles it directly.

---

## Proposed Changes

### [MODIFY] `app.json` / `expo.json`

- Register iOS Siri Intent Definitions (`.intentdefinition` file).
- Register Android App Actions (`shortcuts.xml`) with `android:action` pointing to the main activity.

### [NEW] `src/services/DeepLinkService.ts`

- Handles incoming deep-link URLs from Siri / Google Assistant.
- Parses the voice command string from the URL param (e.g. `sk8lytz://voice?cmd=set+red`).
- Passes command to the existing `VoiceCommandResolver`.

### [MODIFY] `App.tsx` / root navigator

- Add `Linking.addEventListener('url', DeepLinkService.handle)` on mount.
- Route deep-link intents to `DeepLinkService` before any navigation logic.

---

## Open Questions

- **Q:** Do we target Siri Shortcuts (manual user configuration in Settings app) or Siri Suggestions (proactive)? Recommendation: Shortcuts only for v1.
- **Q:** Does the BLE connection need to already be active when the Siri shortcut fires, or should the deep-link also auto-connect to last-known devices first?
- **Q:** Is this iOS 16+ only, or do we need iOS 14+ compatibility?

## Verification Plan

1. Register a Siri Shortcut for "Set SK8Lytz to red."
2. Invoke it while the app is in the background.
3. Verify the app opens and the red color command is dispatched via BLE within 3 seconds.
