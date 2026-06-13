# Implementation Plan
# refactor/delete-legacy-hooks — Delete Legacy Session Hooks + Wire Notifee Background Handler

**Wave:** 3A (parallel-safe with 3B and 3C — no shared files)
**Worktree:** `delete-legacy-hooks`
**Type:** 2 file deletions + 1 file modification (App.tsx)

## Source of Truth
- `src/hooks/useGlobalTelemetry.ts` — target for deletion (confirmed: only imported by SessionContext.tsx)
- `src/hooks/useHealthTelemetry.ts` — target for deletion (confirmed: only imported by SessionContext.tsx)
- `App.tsx` or `index.js` — app root where `notifee.onBackgroundEvent` must be registered
- AST confirmation: `useGlobalTelemetry.ts` `imported_by: [SessionContext.tsx]` only
- AST confirmation: `useHealthTelemetry.ts` `imported_by: [SessionContext.tsx]` only

## Steps

### Step 1 — Confirm zero remaining imports before deleting
```powershell
Get-ChildItem -Recurse -Include "*.ts","*.tsx" | Select-String "useGlobalTelemetry"
Get-ChildItem -Recurse -Include "*.ts","*.tsx" | Select-String "useHealthTelemetry"
```
- Verify: Only results are the files themselves (no remaining imports after Wave 2 merged)
- If any import found: HALT. Do not delete. File friction event.

### Step 2 — Delete `src/hooks/useGlobalTelemetry.ts`
```powershell
Remove-Item src/hooks/useGlobalTelemetry.ts
```
- Verify: File no longer exists

### Step 3 — Delete `src/hooks/useHealthTelemetry.ts`
```powershell
Remove-Item src/hooks/useHealthTelemetry.ts
```
- Verify: File no longer exists

### Step 4 — Find app root file
- Read `App.tsx` or `index.js` to locate where app-level event handlers are registered
- Source: Root of `src/` or project root
- Verify: File identified before editing

### Step 5 — Register Notifee background handler in app root
Add after existing imports (surgical insert — do not touch any other code):
```ts
import notifee, { EventType } from '@notifee/react-native';
import { SessionBridge } from './src/services/session/SessionBridge';

notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS) {
    const id = detail.pressAction?.id;
    if (id === 'end-session')    SessionBridge.send({ type: 'END' });
    if (id === 'pause-session')  SessionBridge.send({ type: 'PAUSE' });
    if (id === 'resume-session') SessionBridge.send({ type: 'RESUME' });
  }
});
```
- Source: `SessionContext.tsx:441–448` — foreground handler pattern (background mirrors it)
- Verify: No duplicate `notifee.onBackgroundEvent` registration in codebase
- Verify: `git diff HEAD` shows only the 3 new lines added

### Step 6 — Run verify
```powershell
npm run verify
```
- Verify: TSC 0 errors (deleted files leave no dangling imports)
- Verify: Jest passes

## Out of Scope
- UI changes (Wave 3B, 3C)
- Any changes to SessionContext.tsx (Wave 2 — already merged)
- Any changes to SessionMachine.ts or services (Wave 1 — already merged)
