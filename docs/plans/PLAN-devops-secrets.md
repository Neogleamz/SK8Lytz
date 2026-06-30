# Implementation Plan

## Task: DevOps & Tooling Security Hardening

**Tags:** [Status: ✅ READY] [Verification Status: ✅ VERIFIED] [Layer: DEVOPS] [Risk: H-RISK] [Size: Meal] [Cognitive Load: HIGH] [WAVE:1]

**Decision Log:** Deep-dive audit (2026-06-30) surfaced hardcoded live credentials in source. Treat as immediate security remediation — no design review needed, action is clear. The JWT token expiry date is unknown; assume it is live until confirmed otherwise.

---

## Goal

Remove all hardcoded credentials and machine-absolute paths from the tools/ directory. Rotate the exposed Supabase JWT. Fix the double-execution bug in apply_migration.js.

---

## Source of Truth

`artifacts/deepdive_raw/DOMAIN_DEVOPS_AND_TOOLING_findings.json` — HIGH findings:
- `tools/createTestUser.js:4` — hardcoded live Supabase anon JWT
- `tools/createTestUser.js:12` — hardcoded plaintext password `Password!2026`
- `tools/apply_migration.js:31` — `run()` called twice — double-executes production migration
- `tools/ast_r11_scanner.js:5` — hardcoded absolute machine path
- `tools/ast_r14_scanner.js:5` — hardcoded absolute machine path
- `tools/find-r26.js:100` — hardcoded absolute machine path

---

## Files to Modify

| File | Lines | Change |
|---|---|---|
| `tools/createTestUser.js` | 4, 12 | Replace hardcoded token + password with `process.env` reads |
| `tools/apply_migration.js` | 29–31 | Remove duplicate `run()` call on line 31 |
| `tools/ast_r11_scanner.js` | 5 | Replace absolute path with `path.resolve(__dirname, '../src')` |
| `tools/ast_r14_scanner.js` | 5 | Replace absolute path with `path.resolve(__dirname, '../src')` |
| `tools/find-r26.js` | 100 | Replace absolute path with `path.resolve(__dirname, '../src')` |
| `.env.local` (create if absent) | — | Add `SUPABASE_TEST_USER_JWT=` and `TEST_USER_PASSWORD=` placeholders |
| `.gitignore` | — | Verify `.env.local` is already ignored |

---

## Steps

### Step 1 — Credential Rotation (BEFORE code changes)
1. Go to Supabase dashboard → Settings → API → Regenerate anon key (or confirm the committed key is the service_role key and revoke it).
2. Update `.env.local` with the new value. Do NOT commit the new key.
3. **Verify:** `grep -r "eyJ" tools/` returns no matches after rotation.

### Step 2 — Fix createTestUser.js
1. Read `tools/createTestUser.js`.
2. Replace line 4 (`const supabaseAnonKey = 'eyJ...'`) with:
   ```js
   const supabaseAnonKey = process.env.SUPABASE_TEST_USER_JWT ?? (() => { throw new Error('SUPABASE_TEST_USER_JWT env var required'); })();
   ```
3. Replace line 12 password literal with:
   ```js
   const testPassword = process.env.TEST_USER_PASSWORD ?? 'ChangeMe!LocalOnly';
   ```
4. **Verify:** `git diff HEAD tools/createTestUser.js` — no credential strings visible.

### Step 3 — Fix apply_migration.js double-execution
1. Read `tools/apply_migration.js` lines 25–35.
2. Delete the duplicate `run()` call on line 31. Keep only one.
3. **Verify:** `grep -n 'run()' tools/apply_migration.js` returns exactly 1 match.

### Step 4 — Fix hardcoded absolute paths
1. For each of `tools/ast_r11_scanner.js:5`, `tools/ast_r14_scanner.js:5`, `tools/find-r26.js:100`:
   - Add at top (if not present): `const path = require('path');`
   - Replace the hardcoded `'C:/Neogleamz/...'` string with `path.resolve(__dirname, '../src')`.
2. **Verify:** `node tools/ast_r11_scanner.js --help` executes without path errors on a clean clone.

### Step 5 — Git hygiene
1. Run `git log --oneline -10 | grep -i 'createTestUser\|jwt\|password'` to confirm no prior commits embedded the JWT in git history.
2. If found in history: notify the user — git history rewrite (filter-branch / BFG) is required and is OUT OF SCOPE for this plan. File a separate task.
3. **Verify:** `git diff --stat HEAD` shows only the 5 tool files changed.

---

## Out of Scope

- Git history rewrite (separate task if credentials appear in historical commits)
- CI/CD secret injection setup (separate DevOps task)
- Rotating Supabase service_role key (only anon key is in the file)
- Any changes to `src/` — tools only

---

## Verification

```powershell
# No credentials in source
grep -r "eyJ" tools/      # expect: no matches
grep -r "Password!2026" tools/  # expect: no matches

# No absolute paths
grep -r "C:\\\\Neogleamz" tools/  # expect: no matches

# apply_migration single-fire
grep -c "run()" tools/apply_migration.js  # expect: 1

# npm run verify still passes
npm run verify
```
