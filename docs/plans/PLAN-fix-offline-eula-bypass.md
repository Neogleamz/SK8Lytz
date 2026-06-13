# Implementation Plan

## Goal
Show a simplified inline EULA acceptance prompt before granting Continue Offline access, so users cannot use the app indefinitely without any EULA agreement.

## Source of Truth
- `src/components/auth/AuthFooterActions.tsx` — "Continue Offline" button press handler and `STORAGE_OFFLINE_SKIP` write
- `src/providers/ComplianceGate.tsx`:29-31 — offline bypass that passes users through without EULA check

## Steps

### Step 1 — Read AuthFooterActions and ComplianceGate
- Action: `view_file src/components/auth/AuthFooterActions.tsx` fully; then `view_file src/providers/ComplianceGate.tsx L24-60` — map the "Continue Offline" button press handler, the `STORAGE_OFFLINE_SKIP` key, and the ComplianceGate's offline pass-through condition
- Source: `src/components/auth/AuthFooterActions.tsx`:1-end, `src/providers/ComplianceGate.tsx`:24-60
- Verify: Know the exact press handler logic and ComplianceGate bypass condition before writing any code

### Step 2 — Add eulaAccepted local state to AuthFooterActions
- Action: In `AuthFooterActions.tsx`, add `const [eulaAccepted, setEulaAccepted] = useState(false)` and `const [showEulaPrompt, setShowEulaPrompt] = useState(false)` local state
- Source: `src/components/auth/AuthFooterActions.tsx` — component body
- Verify: State variables declared; TypeScript compiles; no existing state removed

### Step 3 — Gate Continue Offline on EULA acceptance
- Action: In the "Continue Offline" button `onPress` handler: instead of immediately writing `STORAGE_OFFLINE_SKIP` and proceeding, first read `AsyncStorage.getItem('@Sk8lytz_offline_eula_accepted')` — if null: set `showEulaPrompt(true)` and return early. If non-null (already accepted): proceed directly with `STORAGE_OFFLINE_SKIP` write
- Source: `src/components/auth/AuthFooterActions.tsx` — "Continue Offline" press handler
- Verify: First press with no stored EULA shows prompt; subsequent presses with EULA stored bypass the prompt

### Step 4 — Render inline EULA prompt
- Action: In `AuthFooterActions.tsx` JSX, conditionally render (when `showEulaPrompt` is true) an inline `View` below the Continue Offline button containing: a short EULA summary text (~2 sentences: "By continuing you agree to the SK8Lytz Terms of Service. Full terms available at [link]."), a `CheckBox` or `TouchableOpacity` checkbox UI toggling `eulaAccepted`, and a "Continue" button enabled only when `eulaAccepted === true`
- Source: `src/components/auth/AuthFooterActions.tsx` — JSX render block
- Verify: EULA prompt renders below button; checkbox toggles `eulaAccepted`; Continue button is visually disabled when unchecked

### Step 5 — Write EULA acceptance to AsyncStorage on confirmation
- Action: In the EULA prompt "Continue" button press handler: write `AsyncStorage.setItem('@Sk8lytz_offline_eula_accepted', JSON.stringify({ version: 1, acceptedAt: new Date().toISOString() }))`, then proceed with the normal `STORAGE_OFFLINE_SKIP` write and offline navigation
- Source: `src/components/auth/AuthFooterActions.tsx` — EULA confirmation handler
- Verify: After acceptance, `AsyncStorage.getItem('@Sk8lytz_offline_eula_accepted')` returns the versioned JSON object

### Step 6 — Add EULA check to ComplianceGate offline bypass
- Action: In `ComplianceGate.tsx`, in the `isOfflineMode` branch that currently passes users through silently (L29-31): add `AsyncStorage.getItem('@Sk8lytz_offline_eula_accepted')` check — if null, render `<EulaModal onAccept={() => { write acceptance; setEulaBlocking(false) }} />` blocking the app content; if non-null, render children as before
- Source: `src/providers/ComplianceGate.tsx`:29-31
- Verify: ComplianceGate blocks on EULA absent; passes through on EULA present

### Step 7 — Verify first-run and subsequent-launch behavior
- Action: Run `npm run verify`; fresh install test: tap Continue Offline → EULA prompt appears → accept → app proceeds. Second launch test: tap Continue Offline → app proceeds immediately without re-showing EULA
- Source: `src/components/auth/AuthFooterActions.tsx`, `src/providers/ComplianceGate.tsx`
- Verify: `npm run verify` exits 0; EULA shown exactly once per install; not shown on subsequent launches

## Out of Scope
- Online (authenticated) EULA flow
- EULA version bump / re-acceptance logic for updated terms
- EULA full-text screen or webview
- Any BLE or device layer changes
