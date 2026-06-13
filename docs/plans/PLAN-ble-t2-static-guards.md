# Implementation Plan: ble-t2-static-guards

## Goal
Add two new static AST guards to `tools/verifiable-check-runner.js` that permanently prevent the two most dangerous regression patterns in the XState BLE pipeline:

1. **Guard A:** Direct `bleManager.startDeviceScan()` calls outside `BleMachine.ts` → dual-scan bug
2. **Guard B:** `onOrganicDisconnect` missing from `useMachine` input block → silent recovery death

## Source Audit Finding
- **Audit:** `tools/BLE_AUDIT_2/07_scanner.md` — confirmed zero direct `startDeviceScan` calls in scanner hooks
- **Bug history:** `tools/SESSION_LOG.md` [DECISION] 2026-06-10 — organic disconnect was a silent no-op for unknown duration. A static gate would have caught this at the first commit.
- **Gap report:** `ble_test_gap_analysis.md` §"Missing — Static git gate"

## Source of Truth
- `tools/verifiable-check-runner.js` (add new check after astStatus block)
- `src/services/ble/BleMachine.ts` (only allowed file for startDeviceScan)
- `src/hooks/useBLE.ts` L182–187 (onOrganicDisconnect must be present)

## Implementation

### Guard A — startDeviceScan Bypass Detector
```javascript
// After existing astStatus block in verifiable-check-runner.js
console.log('⏳ Running BLE Architecture Invariant Guard...');
let bleArchStatus = 'FAILED';
try {
  const { execSync } = require('child_process');
  // Grep for startDeviceScan in all TS files EXCEPT BleMachine.ts
  const result = execSync(
    'git grep -rn "startDeviceScan" -- "*.ts" "*.tsx" ":!src/services/ble/BleMachine.ts"',
    { stdio: 'pipe', encoding: 'utf8', cwd: WORKTREE_ROOT }
  );
  if (result.trim().length > 0) {
    throw new Error(`Direct startDeviceScan call found outside BleMachine.ts:\n${result}`);
  }
  bleArchStatus = 'SUCCESS';
  console.log('✅ BLE Architecture Invariant Guard passed!');
} catch (e) {
  if (e.status === 1) {
    // git grep exit 1 = no matches = GOOD
    bleArchStatus = 'SUCCESS';
    console.log('✅ BLE Architecture Invariant Guard passed!');
  } else {
    console.error('❌ BLE Architecture violation:', e.message);
    process.exit(1);
  }
}
```

### Guard B — onOrganicDisconnect Presence Check
```javascript
console.log('⏳ Running Organic Disconnect Wiring Guard...');
try {
  const useBLEContent = fs.readFileSync(
    path.join(WORKTREE_ROOT, 'src/hooks/useBLE.ts'), 'utf8'
  );
  if (!useBLEContent.includes('onOrganicDisconnect')) {
    throw new Error(
      'CRITICAL: onOrganicDisconnect is missing from useBLE.ts. ' +
      'Organic device drops will silently die — recovery will never start. ' +
      'See SESSION_LOG [DECISION] 2026-06-10T08:38.'
    );
  }
  console.log('✅ Organic disconnect wiring guard passed!');
} catch (e) {
  console.error('❌', e.message);
  process.exit(1);
}
```

## Verification
Both guards must:
- Pass on current codebase (`npm run verify` green)
- FAIL if `startDeviceScan` is added to any hook file
- FAIL if `onOrganicDisconnect` is removed from useBLE.ts
