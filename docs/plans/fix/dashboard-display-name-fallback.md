# ⚡ Flash-Executable Implementation Plan: Fix Dashboard Display Name Fallback (`fix/dashboard-display-name-fallback`)

> **WARNING**: This is a Snack-level UI fix designed for zero-bypass execution.

---

## 1. Pre-Flight Context Check (Drift Verification)

- [ ] **Check 1:** Open `src/screens/DashboardScreen.tsx`.
  - _Search for semantic anchor:_ `const name = userProfile?.display_name || userProfile?.username || session.user.email?.split('@')[0] || 'GUEST';`
  - _Expected state:_ Within the `checkAndSyncRegistrations` function (or similar), there is an attempt to use `userProfile` synchronously after `refreshProfile()`.
  - _Abort Condition:_ If the state logic has already been refactored or extracted into a separate hook, halt execution.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Convert Display Name to Reactive Hook

- **Target File:** `src/screens/DashboardScreen.tsx`
- **Semantic Anchor / Target Content:**

```typescript
// Fetch profile and set auth display name
try {
  await refreshProfile();
  const name =
    userProfile?.display_name ||
    userProfile?.username ||
    session.user.email?.split("@")[0] ||
    "GUEST";
  setAuthUsername(name);
  AsyncStorage.setItem("@Sk8lytz_auth_username", name).catch(() => {});
} catch {
  const fallback = session.user.email?.split("@")[0] || "GUEST";
  setAuthUsername(fallback);
  AsyncStorage.setItem("@Sk8lytz_auth_username", fallback).catch(() => {});
}
```

**Exact Replacement Snippet:**

```typescript
// Just trigger the fetch; local state is updated reactively below.
try {
  await refreshProfile();
} catch (e) {
  AppLogger.warn("Failed to refresh profile on dashboard load", {
    error: String(e),
  });
}
```

### Step 2.2: Add Reactive Effect for Username

- **Target File:** `src/screens/DashboardScreen.tsx`
- **Semantic Anchor / Target Content:**
  _(Below the other state declarations near line 450, right before the Dashboard `useEffect` that checks state)_
  `  // AppState Telemetry`

**Exact Replacement Snippet:**

```typescript
// Derive username reactively from userProfile context
useEffect(() => {
  supabase?.auth.getSession().then(({ data: { session } }) => {
    const dbDisplay = userProfile?.display_name?.trim();
    const dbUser = userProfile?.username?.trim();

    const sessionEmailPrefix = session?.user?.email?.split("@")[0];
    const fallback = dbDisplay || dbUser || sessionEmailPrefix || "GUEST";

    setAuthUsername(fallback);
    AsyncStorage.setItem("@Sk8lytz_auth_username", fallback).catch(() => {});
  });
}, [userProfile]);

// AppState Telemetry
```

---

## 3. Post-Execution Verification

- [ ] **Command:** `npx tsc --noEmit`
  - _Expected Output:_ 0 errors.
- [ ] **Manual Step:** Open the dashboard. Log into an account with a display name set. Ensure the pill in the top left says the display name. Change the display name in account settings and return. Ensure it instantaneously reflects the new name.

---

**Completion:** `fix(dashboard): use reactive effect to resolve userProfile race condition for username`
