# Changelog

All notable changes to SK8Lytz are documented here.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [3.10.2] — 2026-06-25

### Fixed
- **[F-003] MTU guard for 0x51 custom scene payloads** — Oversized payloads (>200B) now routed through `writeChunked` to prevent controller lockouts on BLE MTU-limited connections. (`src/hooks/useProtocolDispatch.ts`)
- **[F-005] Direction byte inversion in `setSettledMode`** — Corrected reverse/forward encoding per Protocol Bible §0x41. Previous code inverted the user's direction selection. (`src/protocols/handlers/dynamicEffectHandler.ts`)
- **[F-007] Stale closure in `useMusicMode`** — Programmatic brightness and sensitivity changes now correctly dispatch updated 0x73 music config to hardware. (`src/hooks/useMusicMode.ts`)
- **[F-001] Classic 0x63 binary parser** — Documented that the classic binary format carries no segment byte; `segments: 1` is correct and intentional for single-segment devices. Narrowed `_appLogger` from `any` to `ILogger | null`. (`src/protocols/handlers/hardwareSettingsHandler.ts`)
- **[F-002] Dead code removal in `BleWriteDispatcher`** — Removed permanently unreachable `padStaticColorfulPayload` call; V2-wrapped payloads start with `0x00`, not `0x59`. (`src/services/BleWriteDispatcher.ts`)
- **[F-004] `ZenggeAdapter` JSDoc correction** — Removed false claim that `prepareForTransmission` auto-chunks; accurately documents pass-through behavior and where MTU chunking actually occurs. (`src/protocols/ZenggeAdapter.ts`)
- **[F-006/F-008] `staticColorHandler` cleanup** — Speed range comment updated to oracle-confirmed hardware range 1–100 (was incorrectly stated as 1–31); `_appLogger` narrowed from `any` to `AppLoggerLike | null`. (`src/protocols/handlers/staticColorHandler.ts`)
