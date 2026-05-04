# PLAN: fix/music-mode-mic-magnitude

## Problem Statement

App Mic in Music Mode is completely silent — the SpectrumAnalyzer bars never move and no BLE magnitude commands fire. Two stacked bugs confirmed via code audit.

## Root Cause: Two Stacked Bugs

### Bug 1 — dBFS Range Too Narrow (`useAppMicrophone.ts`, line 76)

```ts
// BROKEN: Maps only -60..0 dBFS to 0..1
const normalized = Math.max(0, Math.min(1, (metering + 60) / 60));
```

`expo-audio`'s `useAudioRecorder` returns `metering` in the **full -160..0 dBFS** range.
Real-world audio on Android sits between **-120 and -30 dBFS**. At -80 dBFS (ambient room sound):
`(-80 + 60) / 60 = -0.33` → clamped to `0` by `Math.max`. Result: magnitude is perpetually 0.

**Fix:** Use a practical range of **-100..0 dBFS**:
```ts
// FIXED: Maps -100..0 dBFS to 0..1 (covers real-world audio range)
const normalized = Math.max(0, Math.min(1, (metering + 100) / 100));
```

### Bug 2 — Double Normalization (`SpectrumAnalyzer.tsx`, line 117)

```tsx
// BROKEN: Expects 0-255, but hook returns 0-1 (already normalized)
const normalizedMag = Math.max(0, Math.min(1.0, audioMagnitude / 255));
```

`useAppMicrophone` returns `audioMagnitude` in the **0–1** range (per its JSDoc).
`SpectrumAnalyzer` treats it as **0–255** and divides again → result is always < 0.004. Even if Bug 1 were fixed, the bars would barely move.

**Fix:** Remove the `/255` division since the value is already normalized:
```tsx
// FIXED: audioMagnitude is already 0-1 from the hook
const normalizedMag = Math.max(0, Math.min(1.0, audioMagnitude));
```

## Target Files

| File | Line | Change |
|---|---|---|
| `src/hooks/useAppMicrophone.ts` | 76 | `(metering + 60) / 60` → `(metering + 100) / 100` |
| `src/components/docked/SpectrumAnalyzer.tsx` | 117 | `audioMagnitude / 255` → `audioMagnitude` |

## Collateral Damage Locks

- Do NOT touch `recorder.record()`, `getStatus()`, or the BLE write logic.
- Do NOT change the `deviceMag` calculation on line 83 — `Math.floor(normalized * 255)` is correct for the hardware 0x74 command.
- Do NOT touch `SpectrumAnalyzer` bar animation logic, only the magnitude mapping line.

## Verification

1. Open Music Mode on physical Android device with BLE connected.
2. Make sound near the phone — EQ bars must visibly react.
3. Loud clap → bars should spike near full height.
4. Silence → bars should drift back to floor (0.15 floor value).
5. Switch to Device Mic → ambient animation should run unaffected.
