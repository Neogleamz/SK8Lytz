# Changelog

All notable changes to SK8Lytz are documented here.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [3.10.4] — 2026-07-01

### Fixed

- **fix/ble-scan-filter-regression** (`21009456`) — Restored `context.scanServiceUUIDs` to `null` (unfiltered) in `BleMachine.ts`. Zengge/FCF1 controllers advertise their controller ID in `mServiceData`, not `mServiceUuids`; OS-level UUID filtering was silently dropping them before GATT connect, causing hardware setup to find zero devices.
- **fix/ftue-group-not-persisted** (`1ae03e35`) — Added group entity persistence in `useDashboardGroups.ts` `handleRegistrationComplete` so the fleet survives the first-time hardware setup flow. Also hardened a stale closure in `useDashboardAutoConnect.ts` via `registeredDevicesRef`. Dashboard now correctly shows all registered devices and auto-connects on subsequent launches.
- **fix/fgs-type-crash** (`14eaf1f6`) — Added `foregroundServiceType: ['connectedDevice']` to `BackgroundBLEService.ts` options, injected the matching `android:foregroundServiceType` attribute via `withWearOsModule.js`, and committed the resulting `AndroidManifest.xml` patch. Android 14+ (targetSdkVersion 36) rejects FGS type `none` at the OS level, causing a native force-close on BLE connection that bypassed all JS `try/catch` handlers.
- **fix/camera-worklets-missing** (`9b3b6970`) — Migrated the worklets runtime from `react-native-worklets-core` (mrousavy fork, VisionCamera v4-era) to `react-native-worklets@0.10.1` (Software Mansion) and installed the matching `react-native-vision-camera-worklets@5.0.8` frame-processor bridge for VisionCamera v5. Camera mode no longer throws `react-native-vision-camera-worklets is not installed` on open.

## [3.10.2] — 2026-06-25

### Fixed
- **[F-003] MTU guard for 0x51 custom scene payloads** — Oversized payloads (>200B) now routed through `writeChunked` to prevent controller lockouts on BLE MTU-limited connections. (`src/hooks/useProtocolDispatch.ts`)
- **[F-005] Direction byte inversion in `setSettledMode`** — Corrected reverse/forward encoding per Protocol Bible §0x41. Previous code inverted the user's direction selection. (`src/protocols/handlers/dynamicEffectHandler.ts`)
- **[F-007] Stale closure in `useMusicMode`** — Programmatic brightness and sensitivity changes now correctly dispatch updated 0x73 music config to hardware. (`src/hooks/useMusicMode.ts`)
- **[F-001] Classic 0x63 binary parser** — Documented that the classic binary format carries no segment byte; `segments: 1` is correct and intentional for single-segment devices. Narrowed `_appLogger` from `any` to `ILogger | null`. (`src/protocols/handlers/hardwareSettingsHandler.ts`)
- **[F-002] Dead code removal in `BleWriteDispatcher`** — Removed permanently unreachable `padStaticColorfulPayload` call; V2-wrapped payloads start with `0x00`, not `0x59`. (`src/services/BleWriteDispatcher.ts`)
- **[F-004] `ZenggeAdapter` JSDoc correction** — Removed false claim that `prepareForTransmission` auto-chunks; accurately documents pass-through behavior and where MTU chunking actually occurs. (`src/protocols/ZenggeAdapter.ts`)
- **[F-006/F-008] `staticColorHandler` cleanup** — Speed range comment updated to oracle-confirmed hardware range 1–100 (was incorrectly stated as 1–31); `_appLogger` narrowed from `any` to `AppLoggerLike | null`. (`src/protocols/handlers/staticColorHandler.ts`)
