# ⚡ Flash-Executable Implementation Plan: Remove Voice Command Engine

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

- [ ] **Check 1:** Check if `src/hooks/useVoiceControl.ts` exists.
- [ ] **Check 2:** Check if `@react-native-voice/voice` is in `package.json`.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Purge Dependencies
- **Command**: `npm uninstall @react-native-voice/voice`

### Step 2.2: Purge Core Files
Execute a `run_command` in powershell:
- `Remove-Item src/hooks/useVoiceControl.ts -Force`
- `Remove-Item src/hooks/useDashboardVoice.ts -Force`
- `Remove-Item src/services/VoiceService.ts -Force`
- `Remove-Item src/components/Voice -Recurse -Force`

### Step 2.3: Strip Dashboard Hook Injection
- **Target File:** `src/screens/DashboardScreen.tsx`
- **Action:** Mult-Replace to remove imports.
- **Anchor 1:** Find Voice Component imports (`VoiceCommandModal`, `VoiceFAB`, `VoiceTutorialModal`, `useDashboardVoice`) and replace with empty string.
- **Anchor 2:** Find `const { isVoiceModalVisible... } = useDashboardVoice({ dockedControllerRef });` and remove the entire destructured block.

### Step 2.4: Strip Dashboard JSX Elements
- **Target File:** `src/screens/DashboardScreen.tsx`
- **Action:** Multi-Replace to remove JSX.
- **Anchor 1:** Find `{/* ──── VOICE COMMAND ENGINE UI ──── */}` and the 3 Voice components following it, up to `</ScreenLayout>`. Remove them completely.

### Step 2.5: Strip AppLogger Types
- **Target File:** `src/services/AppLogger.ts`
- **Action:** Replace `| 'VOICE_RESULT'\n  | 'VOICE_ERROR'\n  | 'VOICE_SPATIAL_APPLIED'` with empty string.

### Step 2.6: Strip `app.json` Permissions
- **Target File:** `app.json`
- **Action:** Check and remove `NSSpeechRecognitionUsageDescription` and `NSMicrophoneUsageDescription` (if the MIC isn't strictly needed natively, but confirm Music Mode still works, maybe keep Mic).

---

## 3. Post-Execution Verification

- [ ] **Command:** `npx tsc --noEmit`
  - _Expected Output:_ Clean exit proving no dangling Dashboard imports.

---

**Completion:** `chore(ui): completely surgically remove voice command engine and native dependencies`
