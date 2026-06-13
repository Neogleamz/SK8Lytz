# Implementation Plan

## Task: fix/pii-scrubber-hardening
## Cluster: TC-10
## Risk: [H-RISK] | Size: [Snack] | Layer: [SERVICE]
## Status: [⚪ READY]
## Worktree: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\pii-scrubber-hardening\`
## Branch: `fix/pii-scrubber-hardening`

---

## Problem Statement

`AppLogger.ts` PII scrubber has three critical gaps (all HIGH severity):

1. **Missing location keys** (`lat`, `lng`, `latitude`, `longitude`) — location data logged in plaintext
2. **Arrays ignored entirely** — object arrays containing PII pass through unredacted
3. **Partial key bypass** — strict `.has()` exact match misses `accessToken`, `refreshToken`, `auth_token`, etc.

`LocationService.ts` logs a `label` key (precise street addresses) not caught by the scrubber.

**CRITICAL:** `android/app/src/main/AndroidManifest.xml:29` has hardcoded Google Maps API key `AIzaSyBfvwN5fcyDbzUZp2Q7c2OfMLPFajVRPwA` committed to git history.

---

## Source of Truth

- `src/services/AppLogger.ts:394` — `const piiKeys = new Set([...])` — exact-match set, misses partials and location keys
- `src/services/AppLogger.ts:402` — `!Array.isArray(obj[key])` guard — blocks array traversal entirely
- `src/services/LocationService.ts:60` — logs `label` key (street address) not in PII set
- `android/app/src/main/AndroidManifest.xml:29` — hardcoded API key
- `app.config.js:49-50` — `googleMaps: { apiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY }` ✅ Prebuild injection confirmed — safe to delete manifest key

---

## Execution Steps

### Step 1 — `src/services/AppLogger.ts` (in worktree)

**View first:** Lines 390–420 (obfuscate function + PII set)

**Edit 1a — Line 394:** Replace `piiKeys` Set with `PII_KEY_PATTERNS` array + substring match
```typescript
// BEFORE:
const piiKeys = new Set(['email', 'name', 'password', 'token', 'phone', 'address', 'fullname']);
// and the check: piiKeys.has(key)

// AFTER:
const PII_KEY_PATTERNS = [
  'email', 'name', 'password', 'token', 'phone', 'address', 'fullname',
  'lat', 'lng', 'latitude', 'longitude', 'label',
  'auth', 'refresh', 'access', 'secret', 'credential',
];
// check: PII_KEY_PATTERNS.some(pattern => key.toLowerCase().includes(pattern))
```

**Edit 1b — Line 402:** Replace `!Array.isArray` guard with recursion into arrays
```typescript
// BEFORE:
} else if (obj[key] && typeof obj[key] === 'object' && !Array.isArray(obj[key])) {
  obfuscated[key] = obfuscate(obj[key] as Record<string, any>);
}

// AFTER:
} else if (obj[key] && typeof obj[key] === 'object') {
  if (Array.isArray(obj[key])) {
    obfuscated[key] = (obj[key] as unknown[]).map(
      item => (item && typeof item === 'object') ? obfuscate(item as Record<string, unknown>) : item
    );
  } else {
    obfuscated[key] = obfuscate(obj[key] as Record<string, unknown>);
  }
}
```

**Boy Scout:** Change `Record<string, any>` → `Record<string, unknown>` on function signature (line ~404)

### Step 2 — `src/services/LocationService.ts` (in worktree)

**View first:** Line 55–65

**Edit:** Rename `label` to `address` in the log context object so PII scrubber catches it:
```typescript
// BEFORE:
AppLogger.log('...', { label, ... });
// AFTER:
AppLogger.log('...', { address: label, ... });
```

### Step 3 — `android/app/src/main/AndroidManifest.xml` (in worktree)

**View first:** Line 25–35

**Edit:** Remove the hardcoded API key meta-data tag at line 29:
```diff
- <meta-data android:name="com.google.android.geo.API_KEY" android:value="AIzaSyBfvwN5fcyDbzUZp2Q7c2OfMLPFajVRPwA"/>
```
Expo prebuild injects this from `app.config.js:49-50` → `process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` ✅

---

## ⚠️ Post-Merge Manual Action Required

The API key `AIzaSyBfvwN5fcyDbzUZp2Q7c2OfMLPFajVRPwA` must be **rotated in Google Cloud Console** after merge — it's in git history. Update `.env` with the new key value.

---

## Verification Plan

### Automated
- `npm run verify` — TSC + Jest clean
- Add/update unit test in `ZenggeProtocol.test.ts` or a new `AppLogger.test.ts`:
  ```
  obfuscate({ email: 'a@b.com', locations: [{ lat: 1.0, lng: 2.0 }] })
  → { email: '[REDACTED]', locations: [{ lat: '[REDACTED]', lng: '[REDACTED]' }] }
  ```

### Manual
- Trigger location feature → check AppLogger output → `lat`/`lng` must show `[REDACTED]`
