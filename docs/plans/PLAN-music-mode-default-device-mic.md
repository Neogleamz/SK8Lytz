# Fix Music Mode default to Device Mic

Change the default microphone source setting in music mode to default to the physical device microphone (`'DEVICE'`) instead of the app/phone microphone (`'APP'`).

## User Review Required

> [!NOTE]
> This is a minor configuration shift. Existing persisted states will still load whatever the user previously configured, but new sessions or fresh installations will default to using the onboard device mic.

## Open Questions

None.

## Proposed Changes

### Core State Logic

#### [MODIFY] [useDockedControllerState.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDockedControllerState.ts#L79)
Modify the initial state value of the `micSource` state in `useDockedControllerState` from `'APP'` to `'DEVICE'`:
```typescript
-  const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('APP');
+  const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('DEVICE');
```

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TypeScript compilation and existing unit tests pass cleanly.

### Manual Verification
- Clear application state (or run fresh) and open the Docked Controller Music tab.
- Assert that the selected mic source is "Device Mic" by default.
- Toggle between App Mic and Device Mic, verify it functions and saves correctly to local persistence.
