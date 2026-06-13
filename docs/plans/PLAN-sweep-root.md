# Implementation Plan: BATCH:sweep-root

## Proposed Changes

### Domain: root

#### [MODIFY] package.json
- Line 51 (Anti-Bloat Protocol / R-21): pg (PostgreSQL Node.js client) is included in React Native client dependencies. It relies on Node core modules and is redundant (Split-Brain) since @supabase/supabase-js is already installed for database access.
- Line 92 (R-21): jest-expo version (~51.0.0) is out of sync with expo version (~55.0.8). This causes split-brain test environments where test mocks do not match runtime implementation.
- Line 78 (R-21): @types/jest (^30.0.0) is out of sync with jest core (^29.7.0). This version overhang causes duplicate and conflicting type definitions.
- Line 30 (Anti-Bloat Protocol): dotenv is a Node.js specific package listed in client dependencies. Expo handles environment variables natively. This is unnecessary bundle bloat.

#### [MODIFY] android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/HealthTracker.kt
- Line 47 (R-26): Re-entrancy Race on startTracking. isTracking is set asynchronously after awaiting startExerciseAsync, which allows multiple coroutines to launch if startTracking is called rapidly, causing Duplicate ExerciseClient requests or crashes.

#### [MODIFY] App.tsx
- Line 34 (R-08): Global object type laundering using as unknown as.

#### [MODIFY] src/providers/BluetoothGuard.tsx
- Line 22 (R-06): Empty catch block missing standard e instanceof Error unwrapping/logging.
- Line 16 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/providers/ComplianceGate.tsx
- Line 35 (R-24): Hardcoded AsyncStorage key bypassing src/constants/storageKeys.ts.
- Line 106 (R-04): Error logged without payload_size or ssi context
- Line 114 (R-15): Direct supabase.auth.signOut() bypasses AuthContext's signOut method.

#### [MODIFY] src/protocols/SpatialEngine.ts
- Line 1 (R-23): Monolith Detection: File exceeds the 30KB limit (approx. 60.5KB).
- Line 1 (R-23): File exceeds 30KB limit (60544 bytes) - flag for mandatory component extraction

#### [MODIFY] src/protocols/ZenggeProtocol.ts
- Line 1 (R-23): File is 53KB, exceeding the 30KB limit for monolith files
- Line 192 (R-06): Missing standard e instanceof Error unwrapping in catch block.
- Line 1 (R-23): File exceeds 30KB limit (53397 bytes) - flag for mandatory component extraction

#### [MODIFY] __tests__/services/SpeedTrackingService.offline.test.ts
- Line 128 (R-08): Usage of `as unknown as` type laundering to bypass TypeScript checks.
- Line 188 (R-16): Hardcoded delay using `setTimeout`.

#### [MODIFY] src/theme/theme.ts
- Line 71 (R-20): Platform.select for Shadows.soft is missing a default/web fallback and casted as ViewStyle. Will crash/undefined on unsupported platforms.
- Line 75 (R-20): Platform.select for Shadows.medium is missing a default/web fallback.
- Line 79 (R-20): Platform.select for Shadows.glow is missing a default/web fallback.

#### [MODIFY] src/constants/storageKeys.ts
- Line 1 (R-24): Multiple undocumented AsyncStorage keys defined in storageKeys.ts that are missing from Master Reference §2 AsyncStorage Key Registry (e.g. @Sk8lytz_offline_skip, @Sk8lytz_remember_creds, @Sk8lytz_demo_halo, @Sk8lytz_last_group_patterns, etc.)

#### [MODIFY] Unknown
- Line ? (Unknown): 
