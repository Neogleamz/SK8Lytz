# Implementation Plan: fix/sweeper-gatt-discovery-skip

## Goal
Fix the third and final copy of the GATT discovery-on-cache-hit bug in `useBLESweeper.interrogateDevice()`. This is the **single root cause** of all current BLE identification failures.

## Corrected Analysis — We Already Have Everything

After tracing every flow end-to-end, all reconnection and identification mechanisms are correctly wired:

| Mechanism | File | Status |
|:----------|:-----|:-------|
| **Stale connection audit on wake** | [useBLE.ts L214-231](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L214-L231) | ✅ WORKING — `isDeviceConnected()` audit fires on AppState→active, prunes zombies |
| **Eager hwCache hydration** | [useBLESweeper.ts L107-131](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts#L107-L131) | ✅ WORKING — loads all `@sk8_hw_*` keys from AsyncStorage on mount |
| **Auto-reconnect on wake** | [DashboardScreen.tsx L392](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L392) | ✅ WORKING — `retriggerAutoConnect()` fires on foreground |
| **Burst scan on retrigger** | [useDashboardAutoConnect.ts L270-278](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L270-L278) | ✅ WORKING — 8s high-power scan on retrigger |
| **Observer auto-connect** | [useDashboardAutoConnect.ts L83-139](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L83-L139) | ✅ WORKING — connects queued MACs as they appear in scan |
| **AutoRecovery with native audit** | [useBLEAutoRecovery.ts L166](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L166) | ✅ WORKING — `isDeviceConnected()` check before each retry |
| **Sweeper restart on foreground** | [DashboardScreen.tsx L389](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L389) | ✅ WORKING — `startSweeper()` on AppState→active |
| **hwCache fed to Scanner→Wizard** | [useBLEScanner.ts L120](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L120) | ✅ WORKING — `mapDeviceToRegistration(d, i, hwCache)` |

**The entire chain works — EXCEPT the Interrogator silently fails because it skips GATT discovery on cache hit, so all writes to the device fail silently, and `hwCache` never gets populated for that device.**

## Root Cause (Cited Truth)

```typescript
// useBLESweeper.ts L247-268 (CURRENT — BROKEN)
// ── HAL: Resolve adapter from service UUIDs & Caching ─────────────────
let interrogatorAdapter: IControllerProtocol | null = null;

const cachedGatt = await BleCharacteristicCache.get(mac);
if (cachedGatt) {
  interrogatorAdapter = getProtocolById(cachedGatt.protocolId);
}

if (!interrogatorAdapter) {
  await conn.discoverAllServicesAndCharacteristics();  // ← ONLY runs on cache MISS
  try {
    const svcs = await conn.services();
    ...
  }
} else {
  AppLogger.log('BLE_STATE_CHANGE', { event: 'gatt_cache_hit', ... });
  // ← On cache HIT: skips discovery entirely → characteristic handles are empty
  // → monitorCharacteristicForDevice fails silently → hwConfig = null → device = Unknown
}
```

The identical bug was already fixed in:
- `useBLE.ts handshakeDevice()` (commit `cfc8951a`)
- `useBLE.ts pingDevice()` (this session)
- `useBLEAutoRecovery.ts initiateRecovery()` (commit `cfc8951a`)

## Proposed Change

### [MODIFY] [useBLESweeper.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts)

Hoist `discoverAllServicesAndCharacteristics()` ABOVE the cache lookup so it ALWAYS runs:

```diff
       // ── HAL: Resolve adapter from service UUIDs & Caching ─────────────────
+      // CRITICAL INVARIANT: discoverAllServicesAndCharacteristics MUST always run
+      // on every new GATT session, even on cache hits. The native BLE stack uses
+      // it to populate internal characteristic handle maps. Skipping it leaves
+      // the maps empty → all writeCharacteristic/monitorCharacteristic calls
+      // silently fail. This bug was fixed in handshakeDevice, pingDevice, and
+      // initiateRecovery — this is the LAST unfixed copy.
+      await conn.discoverAllServicesAndCharacteristics();
+
       let interrogatorAdapter: IControllerProtocol | null = null;

       const cachedGatt = await BleCharacteristicCache.get(mac);
       if (cachedGatt) {
         interrogatorAdapter = getProtocolById(cachedGatt.protocolId);
       }

       if (!interrogatorAdapter) {
-        await conn.discoverAllServicesAndCharacteristics();
         try {
           const svcs = await conn.services();
```

**Surgical scope:** Move 1 line, add 6 comment lines. Zero new logic. Zero new dependencies.

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest must pass clean.

### Manual Verification (LAB)
- Deploy APK to physical device.
- Power on 2+ SK8Lytz controllers.
- Watch logcat for `sweeper_cache_loaded` → `gatt_cache_hit` → `interrogator_complete`.
- Confirm devices resolve with correct `ledPoints` (8=HALOZ, 43=SOULZ).
- Close phone for 5 min, reopen → verify auto-reconnect fires and devices are still identified.
