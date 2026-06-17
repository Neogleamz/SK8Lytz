# Implementation Plan: Bidirectional Watch Bridge (Phone-as-Gateway)

This plan outlines the architecture and steps required to upgrade the `sk8lytz-watch-bridge` module from a one-way telemetry HUD into a fully bidirectional remote control. In accordance with the **Phone-as-Gateway Architecture**, the watch will *not* connect to the skates directly via BLE. Instead, it will send generic JSON payloads (e.g., `WRITE_COLOR`) over the existing Apple `WCSession` and Google Wearable `MessageClient`, which the phone will route to the `BleWriteDispatcher`.

## Proposed Changes

### Native Bridge Modules (iOS / Android)

#### [MODIFY] [Sk8lytzWatchBridgeModule.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/android/src/main/java/expo/modules/sk8lytzwatchbridge/Sk8lytzWatchBridgeModule.kt)
- **Line 161**: Update `handleInboundMessage` for `PATH_COMMAND`.
- Remove the strict `if (command == "START_SESSION" || command == "STOP_SESSION")` gate.
- Parse the incoming UTF-8 byte array as a `JSONObject`.
- Emit the entire mapped dictionary to JS via `sendEvent("onWatchCommandReceived", mappedMap)`.

#### [MODIFY] [Sk8lytzWatchBridgeModule.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/ios/Sk8lytzWatchBridgeModule.swift)
- **Line 74**: Update `didReceiveMessage` and `didReceiveApplicationContext`.
- Remove the cast constraint that requires `message["command"]` to be a `String`.
- If `message["type"]` exists (our new schema), emit the entire `message` dictionary directly to `onWatchCommandReceived`.

### TypeScript Bridge Layer

#### [MODIFY] [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/src/index.ts)
- **Line 37**: Refactor `WatchCommand` from a string union to a discriminated union interface.
- **Line 113**: Update `addWatchCommandListener` to validate the new object payload instead of checking for hardcoded strings.

### Wear OS Native Client

#### [MODIFY] [WearMessageSender.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt)
- Add a new method `sendCommandPayload(context: Context, jsonPayload: JSONObject)`.
- Convert the JSON object to a UTF-8 byte array and dispatch it over `PATH_COMMAND`.

### watchOS Native Client

#### [MODIFY] [WatchConnectivityManager.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift)
- Add new methods `sendWriteColor(r: Int, g: Int, b: Int)` and `sendExecutePattern(...)`.
- These methods will build standard dictionaries `["type": "WRITE_COLOR", "r": r, "g": g, "b": b]` and pass them to the existing `send()` method.

### Phone-Side Orchestrator

#### [MODIFY] `src/services/ble/BleWriteDispatcher.ts` or `src/screens/DashboardScreen.tsx`
- Subscribe to `WatchBridge.addWatchCommandListener`.
- Parse commands (`WRITE_COLOR`, `EXECUTE_PATTERN`) and route them to `BleWriteDispatcher.enqueueWrite(...)`.
