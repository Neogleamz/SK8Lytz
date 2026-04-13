# ⚡ Flash-Executable Implementation Plan: fix/voice-button-null-reference

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers. Use **Semantic Anchors**. All code snippets must be 100% complete and ready to replace via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

*   [ ] **Check 1:** Open `src/hooks/useVoiceControl.ts`. Search for semantic anchor: `export const useVoiceControl = (favorites: IFavoriteState[], onAction: (action: IVoiceAction) => void) => {`.
    *   *Expected state:* The `useVoiceControl` hook exists. 

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Graceful Native Module Check
- **Target File:** `src/hooks/useVoiceControl.ts`
- **Semantic Anchor / Target Content:** The try/catch block attempting to require Voice at the top of the file:
```typescript
let Voice: any;
try {
  if (Platform.OS !== 'web') {
    Voice = require('@react-native-voice/voice').default;
  }
} catch (e) {
  console.warn('Voice recognition native module not found');
}
```

**Exact Replacement Snippet:**
```typescript
import { NativeModules } from 'react-native';

// Safely handle native-only voice import for web compatibility
let Voice: any = null;
try {
  if (Platform.OS !== 'web') {
    // '@react-native-voice/voice' exports a JS wrapper, but its underlying native bridge is 'RCTVoice' or 'SpeechRecognizer'.
    // If the native module is null (like in standard Expo Go), Voice.start will crash.
    const rmVoice = require('@react-native-voice/voice');
    if (NativeModules.RCTVoice || NativeModules.Voice) {
      Voice = rmVoice.default;
    } else {
      console.warn('RCTVoice native module is missing. Running in Expo Go?');
      Voice = null;
    }
  }
} catch (e) {
  console.warn('Voice recognition native module require failed.');
}
```

### Step 2.2: Ensure robust error handling on `startListening`
- **Target File:** `src/hooks/useVoiceControl.ts`
- **Semantic Anchor / Target Content:** 
```typescript
  const startListening = useCallback(async () => {
    if (!isVoiceSupported) {
      setError('Voice control requires the native app (Android/iOS)');
      return;
    }
    try {
      setTranscript('');
      setError(null);
```

**Exact Replacement Snippet:**
```typescript
  const startListening = useCallback(async () => {
    if (!isVoiceSupported) {
      setError('Voice control requires the native app (Android/iOS)');
      return;
    }
    if (!Voice) {
      setError('Voice native module missing. Requires Development Build (APK).');
      return;
    }
    try {
      setTranscript('');
      setError(null);
```

---

## 3. Post-Execution Verification

*   [ ] **Command:** `npx tsc --noEmit`
    *   *Expected Output:* Clean exit (0 errors) relating to `useVoiceControl.ts`.
*   [ ] **Manual Step:** Ask user to tap the Voice Button. It should safely catch the missing bridge and display an error "Voice native module missing. Requires Development Build (APK)." instead of crashing with a "start speech of null" system error.

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `fix(voice): handle missing RCTVoice native bridge gracefully`
