# ⚡ Flash-Executable Implementation Plan: fix/voice-button-null-reference

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

Before executing any file modifications, execute the following strict checks using `view_file` or `grep_search`. Do NOT guess if the file looks different.

*   [ ] **Check 1:** Open `src/hooks/useVoiceControl.ts`. Search for semantic anchor: `const isVoiceSupported = Platform.OS !== 'web';`.
    *   *Expected state:* The variable `isVoiceSupported` exists and is checking `Platform.OS`.
    *   *Abort Condition:* If the function has been refactored or removed, **HALT** and instruct the user: *"Codebase has drifted. This plan is stale and must be recompiled by a THINK model."*
*   [ ] **Check 2:** Open `src/hooks/useVoiceControl.ts`. Search for semantic anchor: `Voice.onSpeechResults = onResults;`.
    *   *Expected state:* The variables `onSpeechResults` and `onSpeechError` are directly assigned to the `Voice` object without a null check.
    *   *Abort Condition:* If there exists a null check already, **HALT** and notify the user that the bug might have been resolved or drifted.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Fix the `isVoiceSupported` Constant
- **Target File:** `src/hooks/useVoiceControl.ts`
- **Semantic Anchor / Target Content:** 
```typescript
/** Voice recognition is only available on native iOS/Android platforms. */
const isVoiceSupported = Platform.OS !== 'web';
```

**Exact Replacement Snippet:**
```typescript
/** Voice recognition is only available on native iOS/Android platforms where the bridge exists. */
const isVoiceSupported = Platform.OS !== 'web' && !!Voice;
```

### Step 2.2: Safe Assign `Voice` Listeners in `useEffect`
- **Target File:** `src/hooks/useVoiceControl.ts`
- **Semantic Anchor / Target Content:**
```typescript
    Voice.onSpeechResults = onResults;
    Voice.onSpeechError = onError;

    return () => {
      try {
        Voice.destroy().then(Voice.removeAllListeners);
      } catch {
        // Native module may not be available during cleanup
      }
    };
```

**Exact Replacement Snippet:**
```typescript
    if (Voice) {
      Voice.onSpeechResults = onResults;
      Voice.onSpeechError = onError;
    }

    return () => {
      try {
        if (Voice) {
          Voice.destroy().then(Voice.removeAllListeners);
        }
      } catch {
        // Native module may not be available during cleanup
      }
    };
```

---

## 3. Post-Execution Verification

*   [ ] **Command:** `npx tsc --noEmit src/hooks/useVoiceControl.ts`
    *   *Expected Output:* Clean exit (0 errors) relating to the modified files.
*   [ ] **Manual Step:** Start Expo Go (`npx expo start --clear`) or run in simulator without the native module bridge. Clicking the Voice FAB should now gracefully return the error "Voice control requires the native app (Android/iOS)" or safely ignore it instead of throwing a red fatal error screen.

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `fix(voice): handle missing native module bridge gracefully to prevent null reference crash`
