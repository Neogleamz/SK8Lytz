# Implementation Plan — `refactor/boolean-fsm-admin-tools`

## Goal
Collapse the 11 independent boolean visibility states in `AdminToolsModal.tsx` into a single `activePanel: AdminPanel | null` union type, eliminating the boolean trap and making panel navigation deterministic.

## Source of Truth
- `artifacts/deepdive_raw/R-18_findings.json` lines 59–121
- `artifacts/system_audit_report.md` §R-18
- `src/components/admin/AdminToolsModal.tsx`

## Out of Scope
- Changing panel content or business logic
- Touching other files with boolean traps (DashboardScreen, HardwareSetupWizard — separate tasks)
- Any BLE or protocol changes

---

## Step 1 — View current state in `AdminToolsModal.tsx`
- **Action:** `view_file src/components/admin/AdminToolsModal.tsx` — read lines 60–80 (boolean declarations) and all usages
- **Verify:** Confirm 11 booleans as documented. Map each boolean to the panel it controls.

## Step 2 — Define the `AdminPanel` union type
- **Action:** Add above the component:
```typescript
type AdminPanel =
  | 'productManager'
  | 'picksScheduler'
  | 'appManager'
  | 'userManagement'
  | 'programmer'
  | 'lab'
  | 'roster'
  | 'audit'
  | 'blacklist'
  | 'featureFlags'
  | 'globalAnalytics';
```
- **Verify:** Type covers all 11 panels.

## Step 3 — Replace 11 booleans with single state
- **Action:** 
  ```typescript
  // REMOVE 11x: const [isXVisible, setIsXVisible] = useState(false);
  // ADD: const [activePanel, setActivePanel] = useState<AdminPanel | null>(null);
  ```
- **Verify:** Zero `isXVisible` state declarations remain.

## Step 4 — Update all panel open/close call sites
- **Action:** Replace `setIsXVisible(true)` → `setActivePanel('x')` and `setIsXVisible(false)` → `setActivePanel(null)`
- **Verify:** No `setIsXVisible` calls remain in file.

## Step 5 — Update conditional renders
- **Action:** Replace `{isXVisible && <XPanel />}` → `{activePanel === 'x' && <XPanel />}`
- **Verify:** Each of 11 panels renders correctly based on `activePanel` equality check.

## Step 6 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero errors. No missing state variables.

## Step 7 — Boy Scout cleanup
- **Action:** While in file, remove any dead imports or stale comments per Boy Scout rule.
- **Verify:** File size reduced.
