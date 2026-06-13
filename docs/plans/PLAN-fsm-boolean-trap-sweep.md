# Implementation Plan

## PLAN-fsm-boolean-trap-sweep — Boolean Traps / FSM State Unions
*Source: `/deepdive-code-hunt` fleet | Rules: R-18 | Date: 2026-06-10*

### Problem
18 files use 3+ scattered boolean flags (`isLoading`, `isError`, `isSubmitting`, `isSaving`...) to represent state. These allow logically impossible combined states (e.g. `isLoading && isError && isSuccess` all `true`), cause race conditions, and make the UI harder to reason about.

### Fix Pattern
Replace scattered booleans with a single string union FSM:
```typescript
// Before
const [isLoading, setIsLoading] = useState(false);
const [isError, setIsError] = useState(false);
const [isSuccess, setIsSuccess] = useState(false);

// After
type Status = 'idle' | 'loading' | 'error' | 'success';
const [status, setStatus] = useState<Status>('idle');
```

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-03

### Affected Files
| File | FSM Suggestion |
|---|---|
| `src/components/AccountModal.tsx:143` | `'idle' \| 'saving' \| 'error' \| 'success'` |
| `src/components/auth/AuthFormSignUp.tsx:37` | `'idle' \| 'submitting' \| 'error' \| 'success'` |
| `src/components/CrewModal.tsx:76` | `'idle' \| 'loading' \| 'error'` |
| `src/hooks/useAccountOverview.ts:39` | `'idle' \| 'loading' \| 'error' \| 'loaded'` |
| `src/screens/DashboardScreen.tsx:178` | `'idle' \| 'loading' \| 'error' \| 'ready'` |
| `src/providers/BluetoothGuard.tsx:14` | `'checking' \| 'granted' \| 'denied'` |
| `src/providers/ComplianceGate.tsx:19` | `'loading' \| 'requires_eula' \| 'accepted' \| 'error'` |
| `src/components/DockedController.tsx:95` | Modal open state → `'none' \| 'presets' \| 'palette' \| 'scenes'` |
| `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:53-55` | `'idle' \| 'claiming' \| 'identifying' \| 'done'` |
| `src/hooks/useCrewHub.ts:33` | `'idle' \| 'joining' \| 'active' \| 'error'` |
| `src/hooks/useCrewManage.ts:30` | `'idle' \| 'loading' \| 'error'` |
| `src/hooks/useDashboardGroups.ts:278` | `'idle' \| 'loading' \| 'error' \| 'loaded'` |
| `src/hooks/useDashboardProfile.ts:59` | `'idle' \| 'loading' \| 'error' \| 'loaded'` |
| `src/hooks/useDockedControllerState.ts:102` | `'idle' \| 'dispatching' \| 'error'` |
| `src/hooks/useProtocolBuilder.ts:73` | `'idle' \| 'building' \| 'error' \| 'ready'` |
| `src/components/admin/tools/FeatureFlagsPanel.tsx:48` | `'idle' \| 'saving' \| 'error'` |
| `src/components/admin/tools/HardwareBlacklistPanel.tsx:46` | `'idle' \| 'loading' \| 'error'` |
| `src/components/patterns/GradientLibraryTab.tsx:46` | `'idle' \| 'loading' \| 'error' \| 'loaded'` |

### Implementation Steps
1. For each file: view the current boolean cluster, define the narrowest FSM union that covers all states
2. Replace `useState(false)` triples with a single `useState<Status>('idle')`
3. Update all setter call sites to transition states correctly
4. Update all conditional render checks to use the new status value
5. Run `npm run verify` after each file

### Verify
- `npm run verify`
- Manual: walk through loading → error → success state in each affected screen
