# ⚡ FLASH EXECUTABLE PLAN: chore/hide-voice-button
## Target: `main`
## Objective: Hide the Voice Command FAB from the Dashboard until the native engine is repaired.

## Execution Steps:
1. Open `src/screens/DashboardScreen.tsx`.
2. Locate the `<VoiceFAB />` rendering block near line 1983.
3. Modify the rendering condition so it evaluates to `false` or comment out the block entirely. Do not delete the code, as we will restore it in a subsequent task.
4. Run `npx tsc --noEmit` and verify locally.
