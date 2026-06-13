# Implementation Plan: Local Crash Decoder (Symbolication)

## Goal
Build a local developer CLI script that downloads minified stack traces from the Supabase `telemetry_errors` table and translates them back to readable TypeScript source code using release source maps. This avoids paying for a symbolication service like Sentry.

## 1. Build Configuration Adjustments
*   **Expo/Metro Config**: Ensure that production builds (`expo build:android` or `./gradlew assembleRelease`) always emit a source map for the bundle (`index.android.bundle.map`).
*   **Map Archival**: Create a local directory (e.g., `.sourcemaps/<version>/`) where the developer manually copies the `.map` file after every successful release build.

## 2. The CLI Script (`tools/symbolicate-crash.js`)
*   **Dependencies**: Require `source-map` (npm package) for mapping logic and `@supabase/supabase-js` for fetching crashes.
*   **CLI Arguments**: Allow passing a specific Crash ID from Supabase (`node tools/symbolicate-crash.js --id 123`) or automatically pulling the most recent un-symbolicated error (`--latest`).
*   **Version Matching**: The script must extract the app version from the Supabase error payload and look for the corresponding `.map` file in the `.sourcemaps/` directory.

## 3. The Symbolication Logic
*   **Parse Stack Trace**: Use Regex to extract the line and column numbers from the minified stack trace string (e.g., `bundle:1:14562`).
*   **Map Translation**: Load the `.map` file into the `source-map` `SourceMapConsumer`.
*   **Query Consumer**: Pass the minified line/col to `consumer.originalPositionFor({ line, column })`.
*   **Output**: Pretty-print the translated stack trace (Original File, Original Line, Method Name) to the terminal.

## Verification Plan
1.  **Generate Minified Error**: Manually throw an error in a production APK build.
2.  **Run Script**: Execute `node tools/symbolicate-crash.js --latest` against the live database.
3.  **Verify Output**: Confirm the terminal outputs the correct TypeScript file path and line number corresponding to the simulated error.
