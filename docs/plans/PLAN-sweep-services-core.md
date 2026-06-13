# Implementation Plan: BATCH:sweep-services-core

## Proposed Changes

### Domain: services-core

#### [MODIFY] src/services/AppLogger.ts
- Line 482 (R-08): Double cast 'as unknown as import' type laundering used to bypass type checking for Supabase insertion.
- Line 1 (R-23): Monolith Detection: File exceeds 30KB limit (33,315 bytes).
- Line 232 (R-26): Re-entrancy race condition in ensureLoaded. Multiple concurrent log calls will trigger simultaneous AsyncStorage reads, potentially wiping the buffer.
- Line 246 (R-06): Missing standard e instanceof Error unwrapping in catch block.
- Line 482 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 488 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 279 (R-11): Missing try/catch on async network/storage operation
- Line 658 (R-15): Direct supabase.auth.getUser() call bypasses AuthContext and executes an unnecessary network request. Services should accept user/userId as arguments from the caller or store it via a setter.
- Line 1 (R-23): File exceeds 30KB limit (33315 bytes) - flag for mandatory component extraction

#### [MODIFY] src/services/AppSettingsService.ts
- Line 19 (R-08): Use of 'any' in AppSettingsMap interface and updateSetting parameters.

#### [MODIFY] src/services/BleWriteDispatcher.ts
- Line 23 (R-08): bleManager and connectedDevices cast to any in parameter list
- Line 342 (R-08): Inline any cast on executeWrite function
- Line 147 (R-10): Sequential group write detected in loop instead of concurrent Promise.all mapping
- Line 208 (R-10): Sequential group write detected in executeWriteChunked instead of concurrent Promise.all mapping
- Line 307 (R-13): Promise.all used to parallelize per-device GATT writes. This causes GATT 133 collisions on Android when communicating with multiple devices.

#### [MODIFY] src/services/BlePingService.ts
- Line 16 (R-08): bleManager cast to any in parameter list
- Line 72 (R-08): any cast on error and char parameters in monitorCharacteristicForDevice callback
- Line 132 (R-16): Hardcoded setTimeout used instead of queue-managed delay

#### [MODIFY] src/services/BleCharacteristicCache.ts
- Line 12 (R-24): AsyncStorage Key used directly; potentially undocumented key collision

#### [MODIFY] src/services/ScenesService.ts
- Line 66 (R-06): Empty catch block swallowing errors completely
- Line 303 (R-08): Type laundering using as unknown as
- Line 70 (R-04): Error logged without payload_size or ssi context
- Line 107 (R-04): Error logged without payload_size or ssi context
- Line 141 (R-04): Error logged without payload_size or ssi context
- Line 159 (R-04): Error logged without payload_size or ssi context
- Line 173 (R-04): Error logged without payload_size or ssi context
- Line 187 (R-04): Error logged without payload_size or ssi context
- Line 208 (R-04): Error logged without payload_size or ssi context
- Line 292 (R-04): Error logged without payload_size or ssi context
- Line 328 (R-04): Error logged without payload_size or ssi context
- Line 354 (R-04): Error logged without payload_size or ssi context
- Line 404 (R-04): Error logged without payload_size or ssi context
- Line 303 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/services/SkateSpotsService.ts
- Line 43 (R-11): Missing try/catch or proper error handling on async storage operation

#### [MODIFY] src/services/DeviceRepository.ts
- Line 1 (R-23): File exceeds 30KB monolith limit (39KB)
- Line 692 (R-04): Error logged without payload_size or ssi context
- Line 752 (R-04): Error logged without payload_size or ssi context
- Line 1 (R-23): File exceeds 30KB limit (39286 bytes) - flag for mandatory component extraction

#### [MODIFY] src/services/supabaseClient.ts
- Line 78 (R-08): Type laundering using as unknown as on Supabase client creation
- Line 29 (R-04): Error logged without payload_size or ssi context
- Line 41 (R-04): Error logged without payload_size or ssi context
- Line 78 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/services/CrewService.ts
- Line 730 (R-22): Dangling setTimeout reference: this._lastScenePersistTimer is never cleared in the unsubscribe() cleanup method.
- Line 1 (R-23): File exceeds the 30KB limit (30,718 bytes), marking it as a monolith candidate for extraction.
- Line 503 (R-04): Error logged without payload_size or ssi context
- Line 553 (R-04): Error logged without payload_size or ssi context
- Line 571 (R-04): Error logged without payload_size or ssi context
- Line 740 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/services/AuthProfileService.ts
- Line 107 (R-06): Missing standard `e instanceof Error` unwrapping in catch block before throw.
- Line 79 (R-04): Error logged without payload_size or ssi context
- Line 107 (R-06): Missing standard e instanceof Error unwrapping, unsafe type cast instead.

#### [MODIFY] src/services/LocationService.ts
- Line 64 (R-09): Reverse-geocoded address label logged to AppLogger, potentially leaking residential PII.
- Line 158 (R-08): any cast used when mapping private session row data from Supabase.
- Line 178 (R-08): any cast used when mapping public session row data from Supabase.
- Line 268 (R-08): Type laundering rawSpots to Record<string, unknown>[] and asserting fields.
- Line 265 (R-04): Error logged without payload_size or ssi context
- Line 91 (R-06): Missing standard e instanceof Error unwrapping in catch block.

#### [MODIFY] src/services/NotificationService.ts
- Line 84 (R-21): Calling legacy profileService.registerPushToken instead of newly extracted pushTokenService. Missed during God Object Decomposition.

#### [MODIFY] src/services/HealthSyncService.ts
- Line 48 (R-08): Use of explicit `any` cast in array definition, violating strict type safety.
- Line 38 (R-04): Error logged without payload_size or ssi context
- Line 85 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/services/BleSessionFactory.ts
- Line 127 (R-03): Missing jitter in GATT 133 connection retry loop. Uses static [500, 1500] backoff without randomization, risking reconnect storms.

#### [MODIFY] src/services/BleWriteQueue.ts
- Line 205 (R-03): Transient GATT error write retry loop lacks backoff and jitter. Uses hardcoded 100ms delay.

#### [MODIFY] src/services/CrewProfileService.ts
- Line 60 (R-04): Error logged without payload_size or ssi context
- Line 109 (R-04): Error logged without payload_size or ssi context
- Line 138 (R-04): Error logged without payload_size or ssi context
- Line 169 (R-04): Error logged without payload_size or ssi context
- Line 188 (R-04): Error logged without payload_size or ssi context
- Line 204 (R-04): Error logged without payload_size or ssi context
- Line 231 (R-04): Error logged without payload_size or ssi context
- Line 266 (R-04): Error logged without payload_size or ssi context
- Line 291 (R-04): Error logged without payload_size or ssi context
- Line 347 (R-04): Error logged without payload_size or ssi context
- Line 429 (R-04): Error logged without payload_size or ssi context
- Line 468 (R-04): Error logged without payload_size or ssi context
- Line 508 (R-04): Error logged without payload_size or ssi context
- Line 528 (R-04): Error logged without payload_size or ssi context
- Line 548 (R-04): Error logged without payload_size or ssi context
- Line 599 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/services/GradientsService.ts
- Line 25 (R-04): Error logged without payload_size or ssi context
- Line 105 (R-04): Error logged without payload_size or ssi context
- Line 140 (R-04): Error logged without payload_size or ssi context
- Line 114 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/services/PushTokenService.ts
- Line 31 (R-04): Error logged without payload_size or ssi context
- Line 48 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/services/SpeedTrackingService.ts
- Line 472 (R-05): Bypasses AsyncStorage caching for lifetime stats, making it unavailable offline. Directly accesses Supabase without local cache fallback.
- Line 392 (R-05): Bypasses AsyncStorage caching for historical skate sessions. Fallback only reads pending queue, not previously synced sessions.
- Line 193 (R-05): Bypasses AsyncStorage offline queue (PENDING_SESSION_QUEUE_KEY) for AUTHENTICATED users. If offline but authenticated, the Supabase insert fails and the session is permanently lost.

#### [MODIFY] src/services/__tests__/GroupRepository.test.ts
- Line 71 (R-11): Missing try/catch on async network/storage operation
- Line 120 (R-11): Missing try/catch on async network/storage operation
- Line 141 (R-11): Missing try/catch on async network/storage operation
- Line 217 (R-11): Missing try/catch on async network/storage operation
- Line 232 (R-11): Missing try/catch on async network/storage operation
- Line 245 (R-11): Missing try/catch on async network/storage operation
