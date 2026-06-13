# Implementation Plan
# PLAN-DEAD-DEPENDENCY-PRUNE

## Summary
9 unused or incorrectly declared dependencies found in `package.json`. Removing them reduces bundle size, eliminates maintenance surface, and cleans up the module graph. These are pure removals with zero code changes required.

**Batch:** `BATCH:dep-diet-sweep`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `package.json` — Lines 23, 34, 43, 50, 53, 62, 63, 69, 73, 74 (DEPENDENCY_AUDIT-001 to -009)
- Raw audit: `DOMAIN_DEPENDENCY_AUDIT_findings.json`

---

## Findings

| ID | Package | Line | Verdict | Action |
|----|---------|------|---------|--------|
| AUDIT-001 | `string-similarity` | 73 | Never imported | Remove |
| AUDIT-002 | `supercluster` | 74 | Transitive dep only | Remove from direct deps |
| AUDIT-003 | `jpeg-js` | 53 | Never imported | Remove |
| AUDIT-004 | `react-native-vision-camera-worklets` | 69 | Needs verification | Verify then remove |
| AUDIT-005 | `react-native-nitro-image` | 62 | Never imported | Remove |
| AUDIT-006 | `react-native-nitro-modules` | 63 | Never imported | Remove |
| AUDIT-007 | `expo-speech` | 50 | Never imported | Remove |
| AUDIT-008 | `expo-image-manipulator` | 43 | Never imported | Remove |
| AUDIT-009 | `expo-blur` | 34 | Never imported | Remove |

> [!NOTE]
> AUDIT-010 (`@notifee/react-native` + `expo-notifications` overlap) is a HIGH false-positive — both serve different purposes (foreground service vs. push token). Do NOT remove either. This is by design.

---

## Pre-Removal Verification (Before Each Removal)

For each package, verify with a final grep before removing:
```powershell
grep -rn "string-similarity" src/ android/ ios/
grep -rn "jpeg-js" src/ android/ ios/
grep -rn "nitro-image\|nitro-modules" src/ android/ ios/
grep -rn "expo-speech" src/ android/ ios/
grep -rn "expo-image-manipulator" src/ android/ ios/
grep -rn "expo-blur" src/ android/ ios/
```

For `react-native-vision-camera-worklets`:
```powershell
grep -rn "vision-camera-worklets" src/ babel.config.js metro.config.js
```

---

## Implementation Steps

### Step 1 — Remove confirmed unused packages
```bash
npm uninstall string-similarity jpeg-js react-native-nitro-image react-native-nitro-modules expo-speech expo-image-manipulator expo-blur
```

### Step 2 — Remove supercluster from direct deps
```bash
npm uninstall supercluster
```
(It remains in package-lock.json as a transitive dep of react-native-map-clustering)

### Step 3 — Verify react-native-vision-camera-worklets
Check babel.config.js and metro.config.js for plugin references. If not referenced: `npm uninstall react-native-vision-camera-worklets`. If referenced as babel plugin: leave it.

### Step 4 — Rebuild to confirm no missing module errors
```bash
npm run verify
```
Then attempt a Gradle sync:
```bash
cd android && ./gradlew dependencies --configuration debugRuntimeClasspath | grep "nitro\|string-sim\|jpeg-js"
```

---

## Verification
- `npm run verify` — no missing module errors
- `npm audit` — run health sweep
- Bundle size check: `npx expo export --platform android --dump-sourcemap 2>&1 | tail -5`

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Build]`
- `[Risk: L-RISK]`
- `[Size: Snack]`
- `[Cognitive Load: Low]`
- `[BATCH: dep-diet-sweep]`
