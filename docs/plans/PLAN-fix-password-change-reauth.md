# Implementation Plan

## Goal
Require current password re-entry before allowing password change to prevent unauthorized changes if a session token is compromised.

## Source of Truth
- `src/components/account/AccountModal.tsx`:307 — `_currentPwd` state declared with `_` prefix (unused/suppressed)
- `src/components/account/AccountModal.tsx`:314 — `_setShowCurrentPwd` declared with `_` prefix (unused/suppressed)
- `src/components/account/AccountModal.tsx`:338-345 — password change handler that calls `supabase.auth.updateUser({ password })` without current password verification
- `src/components/account/tabs/AccountTabSecurity.tsx` — UI for the security tab where current password field will be added

## Steps

### Step 1 — Read AccountModal and AccountTabSecurity fully
- Action: `view_file src/components/account/AccountModal.tsx L300-365`; then `view_file src/components/account/tabs/AccountTabSecurity.tsx` — map the complete state declarations, the password change handler, and the current UI layout of the security tab
- Source: `src/components/account/AccountModal.tsx`:300-365, `src/components/account/tabs/AccountTabSecurity.tsx`:1-end
- Verify: Know exact variable names, handler names, and JSX structure before any edit

### Step 2 — Activate the unused currentPwd state
- Action: In `AccountModal.tsx`, rename `_currentPwd` → `currentPwd` and `_setShowCurrentPwd` → `setShowCurrentPwd` (remove the `_` prefix suppression). Also rename any paired `_showCurrentPwd` state variable if present
- Source: `src/components/account/AccountModal.tsx`:307, 314
- Verify: `git diff` shows only prefix removal; no logic changed; TypeScript compiles

### Step 3 — Add Current Password field to AccountTabSecurity UI
- Action: In `AccountTabSecurity.tsx`, add a `TextInput` with `secureTextEntry` and label "Current Password" positioned above the "New Password" field. Wire its `onChangeText` to the `setCurrentPwd` setter passed from `AccountModal`. Add matching show/hide toggle using `setShowCurrentPwd`
- Source: `src/components/account/tabs/AccountTabSecurity.tsx` — password fields block
- Verify: Current Password field renders above New Password in the security tab; show/hide toggle works

### Step 4 — Add re-auth call before password update
- Action: In the password change handler at `AccountModal.tsx L338`, before calling `supabase.auth.updateUser({ password: newPwd })`, add: `const { error: reAuthError } = await supabase.auth.signInWithPassword({ email: user.email!, password: currentPwd })`. If `reAuthError` is non-null → call `Alert.alert('Error', 'Current password is incorrect')` and `return` without calling `updateUser`
- Source: `src/components/account/AccountModal.tsx`:338-345
- Verify: `git diff` shows only the insertion of the re-auth guard; no surrounding handler logic altered

### Step 5 — Proceed with password update on re-auth success
- Action: Confirm that the existing `supabase.auth.updateUser({ password: newPwd })` call and its success/error handling remain unchanged after the guard; the guard only adds a conditional early return on re-auth failure
- Source: `src/components/account/AccountModal.tsx`:338-345
- Verify: `git diff` — `updateUser` call is identical to pre-edit; only the re-auth guard lines are new

### Step 6 — Clear currentPwd after attempt
- Action: In both the success and error paths of the password change handler, call `setCurrentPwd('')` to clear the current password field (don't leave it populated after an attempt)
- Source: `src/components/account/AccountModal.tsx` — handler success/error branches
- Verify: After any password change attempt (success or fail), the current password TextInput is cleared

### Step 7 — Verify re-auth guard works
- Action: Run `npm run verify`; manual test: navigate to Account → Security → enter wrong current password → confirm "Current password is incorrect" alert; enter correct current password → confirm password changes successfully
- Source: `src/components/account/AccountModal.tsx`:338-365
- Verify: `npm run verify` exits 0; wrong password is blocked; correct password succeeds

## Out of Scope
- Email change flow re-auth (separate concern)
- Account deletion flow re-auth (separate concern)
- Biometric re-auth alternative
- Password strength rules or complexity requirements
- Any BLE or device layer changes
