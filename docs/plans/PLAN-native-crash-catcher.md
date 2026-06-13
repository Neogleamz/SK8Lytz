# Implementation Plan: Native Crash Catcher

## Goal
Implement a custom Android `UncaughtExceptionHandler` to catch fatal native crashes (OOM, C++ exceptions, BLE lib failures) locally and upload them to Supabase on the next app boot, completely bypassing third-party SDKs like Sentry to adhere to the Anti-Bloat Protocol.

## 1. Native Android Implementation (`MainApplication.kt` / `MainActivity.kt`)
*   **Create `NativeCrashHandler.kt`**: A class implementing `Thread.UncaughtExceptionHandler`.
*   **Initialization**: In `MainApplication.onCreate()`, capture the default handler and set the custom handler.
*   **Crash Interception**: When `uncaughtException(thread, throwable)` is fired:
    *   Extract the stack trace into a String using `Log.getStackTraceString(throwable)`.
    *   Save this String, along with a timestamp, to `SharedPreferences` (e.g., key `LAST_FATAL_CRASH`).
    *   Call the default uncaught exception handler to allow the app to gracefully die.

## 2. React Native Bridge (`NativeCrashModule.kt`)
*   **Expose Native Module**: Create a custom React Native module that exposes a method `getAndClearLastCrash()`.
*   **Implementation**: This method checks `SharedPreferences` for `LAST_FATAL_CRASH`. If it exists, it returns the string and immediately deletes it from `SharedPreferences`.

## 3. JavaScript Telemetry Integration (`src/services/AppLogger.ts`)
*   **Boot Sequence**: In the root `App.tsx` or `AppLogger.init()` sequence, call `NativeCrashModule.getAndClearLastCrash()`.
*   **Upload to Supabase**: If a crash string is returned, wrap it in the standard `telemetry_errors` schema (flagged as `is_native_fatal: true`) and immediately push it to the Supabase "VIP Fast-Lane".

## Verification Plan
1.  **Manual Trigger**: Write a temporary native bridge method that throws a fatal `RuntimeException` to simulate a crash.
2.  **App Restart**: Restart the app and verify the Supabase `telemetry_errors` table immediately receives the stack trace payload.
3.  **Memory Leak Check**: Ensure the handler does not hold strong references to `Activity` context.
