# Implementation Plan

## Goal
Resolve two critical regressions that occurred tonight:
1. **BLE Write Failures**: The app connects but payloads do not physically fire on the hardware.
2. **Web Demo Crash**: The web build white-screens on startup.

## Discovery & Root Cause (VERIFIED)

> [!CONFIDENCE: VERIFIED]
> Source: ADB Logcat (`task-2678.log`), `src/hooks/useBLE.ts#L670`, and `src/hooks/ble/useBLEAutoRecovery.ts#L189`.

**1. The BLE Payload Regression:**
The ADB logs reveal the exact issue: `connect()` and `configureMTU()` are firing, but `discoverServices()` is **missing** on subsequent auto-recoveries and reloads. 
This is because of the `BleCharacteristicCache`. Introduced recently, the code attempts to optimize connect times by skipping `conn.discoverAllServicesAndCharacteristics()` if the protocol ID is cached. 
However, `react-native-ble-plx` **mandates** that `discoverAllServicesAndCharacteristics()` is called on *every new native connection session*. If skipped, the native characteristic maps are empty, and all `writeCharacteristic` calls silently vanish into the void.

**2. The Web Demo Crash:**
`src/components/crew/CrewLandingMap.web.tsx` was generated with `export default function CrewLandingMap`, but the rest of the app imports it as a named export `{ CrewLandingMap }`. Metro bundler fails to resolve it on web, causing a fatal white screen.

## Proposed Changes

### Layer 1: Core (BLE Fix)

#### [MODIFY] [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line 669**: Move `await conn.discoverAllServicesAndCharacteristics();` OUTSIDE the `if (!adapter)` block. It must run unconditionally for every connection.
- `BleCharacteristicCache` will still be used to skip the slow `conn.services()` map iteration and adapter lookup, preserving performance without breaking GATT.

#### [MODIFY] [useBLEAutoRecovery.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts)
- **Line 189**: Move `await conn.discoverAllServicesAndCharacteristics();` OUTSIDE the `if (!recoveryAdapter)` block so the recovered device's GATT handles are properly initialized before we ping it.

### Layer 2: UI (Web Crash Fix)

#### [MODIFY] [CrewLandingMap.web.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingMap.web.tsx)
- Change `export default function CrewLandingMap` to `export function CrewLandingMap`.

## User Review Required
> [!IMPORTANT]  
> The BLE caching logic was meant as a performance optimization but fundamentally violates how the native iOS/Android BLE stacks map characteristics per-session. I will enforce the mandatory discovery step while preserving the cache lookup for the adapter schema. Please review and approve this surgical strike.
