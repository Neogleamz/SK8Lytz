# Implementation Plan

## Task: fix/device-settings-probe-fsm
**Cluster:** I — State Machine / Boolean Trap Refactors (Scoped task — DeviceSettingsModal only)  
**Batch:** `[BATCH:boolean-fsm-sweep]`  
**Rule Violated:** R-18  
**Severity:** HIGH (R18-005)  
**Risk:** M-RISK  
**Size:** Meal

## Cited Truth
- Audit R18-005: `src/components/DeviceSettingsModal.tsx` (or similar path) — `isProbing` boolean for multi-phase BLE probe lifecycle
- Audit report proposes union: `'idle' | 'connecting' | 'reading' | 'writing' | 'disconnecting' | 'complete' | 'error'`
- Source: `artifacts/system_audit_report.md` Cluster I lines 319–329

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster I, R18-005
- File to read before editing: locate `DeviceSettingsModal.tsx` via grep — `grep -rn "isProbing" src/`
- Protocol Bible: `tools/ZENGGE_PROTOCOL_BIBLE.md` — BLE probe sequence

## Problem
`isProbing: boolean` in `DeviceSettingsModal` models a multi-phase BLE operation as a single binary flag. This allows illegal intermediate states:
- `isProbing=true` — but which phase? connecting? reading settings? writing a change? user doesn't know
- Cannot surface granular progress (spinner shows same for all phases)
- Cannot handle intermediate errors (connecting succeeded but reading failed → `isProbing=false` with no distinction)

## Implementation Steps

### Step 1 — Discover actual file and lines (P1 mandatory)
```bash
grep -rn "isProbing" src/ --include="*.ts" --include="*.tsx"
```
Use `view_file` on the result to read the exact current state management.

### Step 2 — Define ProbeStatus union
Add to file top (or to a shared `src/types/uiState.ts`):
```ts
type ProbeStatus = 'idle' | 'connecting' | 'reading' | 'writing' | 'disconnecting' | 'complete' | 'error';
```

### Step 3 — Replace `isProbing` with `probeStatus`
- `const [probeStatus, setProbeStatus] = useState<ProbeStatus>('idle');`
- Add backwards-compatible computed: `const isProbing = probeStatus !== 'idle' && probeStatus !== 'complete' && probeStatus !== 'error';`
- Update all `setIsProbing(true/false)` call sites to the appropriate `setProbeStatus('connecting' / 'idle')` transitions

### Step 4 — Update UI rendering
Replace binary spinner `{isProbing && <ActivityIndicator />}` with:
```tsx
{probeStatus !== 'idle' && probeStatus !== 'complete' && (
  <View>
    <ActivityIndicator />
    <Text>{probeStatusLabel[probeStatus]}</Text>
  </View>
)}
```

## Note on fix/auth-context-fsm
The larger `fix/auth-context-fsm` task (Cluster I, R18-001) — refactoring `AuthContext` 3 boolean flags — is tagged `[H-RISK, Feast]` and goes to ROADMAP. It must NOT be combined with this task. They are separate worktrees.

## Verification Plan
- TSC passes — ProbeStatus union enforces type safety at all call sites
- Manual: Step through device settings probe (connect → read settings → write change) and verify each phase shows correct label
- `npm run verify`
