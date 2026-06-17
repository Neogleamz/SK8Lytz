# Goal Description
The `AppPerformanceWidget` on the Web Command Center at `localhost:5997/performance` is not receiving any telemetry metrics. 

I've discovered the root cause: The app IS measuring `SCREEN_LOAD_TTID` and `SCREEN_LOAD_TTFD` under the hood. However, `AppLoggerService.ts` buffers all non-whitelisted telemetry events into a single-slot `pendingLogQueue` with a 100ms delay. Since screen hydration happens concurrently with other app startup events (e.g. `APP_OPENED`), the performance metrics are instantly overwritten by subsequent logs before they have a chance to be synced to Supabase.

## Proposed Changes
### src/services/appLogger/AppLoggerService.ts
Add `SCREEN_LOAD_TTID` and `SCREEN_LOAD_TTFD` to the immediate-push array, ensuring they bypass the lossy correlation queue and are instantly persisted to storage so they can be dispatched to the command center.

#### [MODIFY] [AppLoggerService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerService.ts)

## Verification Plan
### Automated Tests
- `npm run verify` to ensure the TypeScript compiler and all safety gates pass.

### Manual Verification
- Once merged, the Command Center's performance widget will automatically start tracking screen load times.
