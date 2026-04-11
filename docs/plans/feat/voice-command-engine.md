# Implementation Plan: Voice Command Engine (`feat/voice-command-engine`)

This feature enables hands-free control of the SK8Lytz application, allowing skaters to switch modes, apply favorites, and adjust lighting settings using natural language.

## Design Decisions & Rationale
We are using `react-native-voice` as it provides a direct bridge to the device's native high-performance STT engines (SFSpeechRecognizer on iOS, Google Speech on Android). To avoid dependence on spotty cellular signals at skating rinks, we will prioritize on-device recognition and use a local fuzzy-string matching library to resolve commands instantly without cloud round-trips.

## User Review Required

> [!IMPORTANT]
> **Offline Processing**: While `react-native-voice` supports on-device recognition, the availability of "Offline Data Packs" depends on the user's system settings (Google App settings on Android, and SFSpeechRecognizer on iOS). I will implement a "Connectivity-Agnostic" fallback, but users may need to download these packs for 100% offline reliability.

> [!IMPORTANT]
> **Fuzzy Matching**: To ensure "Red Glow" matches a favorite named "Red Glow," I am introducing a simple local matching weight system rather than a cloud-based LLM to maintain zero-latency and privacy.

## Proposed Changes

---

### [Component] Core Business Logic & Services

#### [NEW] [VoiceService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/VoiceService.ts)
- A singleton service wrapping `@react-native-voice/voice`.
- Implements `processCommand(transcript: string, context: IVoiceContext)` logic.
- Maps keywords (e.g., "brighter", "dimmer", "mode", "symphony") to specific method calls.

#### [NEW] [useVoiceControl.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useVoiceControl.ts)
- Custom hook to manage recording state (`isListening`), transcription chunks, and the trigger/stop lifecycle.

---

### [Component] UI / UX Layer

#### [NEW] [VoiceCommandModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VoiceCommandModal.tsx)
- A high-fidelity modal overlay.
- Features: 
  - **Large Mic Button**: 120x120px touch target for easy access with wrist guards.
  - **Animated Waveform**: Visual feedback showing audio input magnitude.
  - **Glassmorphic Feedback Card**: Displays what the app "heard" and the action it's taking.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Add a mic shortcut icon next to the "Favorites" header or in the top-right tray.
- Integrate the `VoiceCommandModal` and hook it into the main controller state.

---

### [Component] Configuration & Scaling

#### [MODIFY] [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json)
- Add `@react-native-voice/voice`.
- Add `string-similarity` for keyword weight resolution.

## Open Questions

1. **Voice Feedback (TTS)**: After a command succeeds, should the app speak back to you (e.g., "Switching to Fire Mode") via the phone speaker, or is visual feedback on the screen enough?
2. **"Builder" Complexity**: For the requested "I want red in the back with white in the middle..." feature—do you want this in V1, or should we stick to "Favorite/Pattern/Mode" switching first? (The complex spatial parsing is significantly more difficult).
3. **Button Placement**: Should the Voice Button also be available as a Floating Action Button (FAB) on the main Dashboard, or only when the Controller is open?

## Verification Plan

### Automated Tests
- `npm test src/services/VoiceService.test.ts`: Verify that strings like "set to red" correctly return the color change action.

### Manual Verification
1. Open the Voice Modal.
2. Tap the mic and say "Turn up brightness."
3. Verify the brightness slider in `DockedController` increments.
4. Say "Apply Fire Mode" (assuming a favorite named Fire exists).
5. Verify the profile switches instantly.
