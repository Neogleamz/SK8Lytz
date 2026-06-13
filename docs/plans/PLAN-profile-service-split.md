# ProfileService Split — Meal 1 of God-Object Decomposition

Split `ProfileService.ts` (700 lines, one giant class) into 3 focused service files + a shared types file.
A barrel re-export makes this a **zero call-site-change** refactor — every existing import path continues to work unchanged.

## Architecture Decision

> [!IMPORTANT]
> **Barrel-first strategy**: `ProfileService.ts` becomes a pure re-export aggregator. No consumer file needs to be touched.
> All 12 importing files keep their existing `from '../services/ProfileService'` paths.

## New File Structure

```
src/services/
├── ProfileService.ts          ← MODIFY: becomes barrel re-export only
├── ProfileService.types.ts    ← NEW: all shared interfaces/types
├── AuthProfileService.ts      ← NEW: auth/profile CRUD
├── CrewProfileService.ts      ← NEW: crew management + stats
└── PushTokenService.ts        ← NEW: push token register/unregister
```

## Proposed Changes

---

### Shared Types

#### [NEW] `ProfileService.types.ts`
Move all exported interfaces here so they can be imported by the 3 service files without circular deps:
- `UserProfile`
- `PermanentCrew`
- `CrewMemberDisplay`
- `CrewMemberFull`
- `SessionHistoryItem`

---

### Auth / Profile Domain

#### [NEW] `AuthProfileService.ts`
Methods from the current `ProfileService` class:
- `fetchOrCreateProfile(cachedUser?)` — fetch or auto-create profile, self-heal display_name/username
- `updateProfile(fields)` — update display_name, avatar_color, username, avatar_url
- `getSessionHistory(cachedUserId?)` — last 20 crew sessions

Export: `export const authProfileService = new AuthProfileService();`

---

### Crew Management Domain

#### [NEW] `CrewProfileService.ts`
Methods from the current `ProfileService` class:
- `getMyCrew(updatedSince?, cachedUserId?)` — list permanent crews + delta sync
- `createPermanentCrew(name, opts?)` — create + auto-join creator
- `joinPermanentCrew(inviteCode)` — join by invite code
- `joinPublicCrewById(crewId)` — join public crew directly
- `leavePermanentCrew(crewId)` — remove membership
- `getCrewMemberCount(crewId)` — member count
- `getCrewMembersForDisplay(crewId)` — count + avatar colors (privacy-safe)
- `updateCrew(crewId, fields)` — owner-only update
- `getPublicCrews()` — discover public crews
- `deleteCrew(crewId)` — owner-only, broadcasts session_ended, cascades memberships
- `getCrewStats(crewId)` — session count, last active, top scene, distance/speed/gforce stats
- `getCrewMembersWithNames(crewId)` — full member list with display names + roles
- `assignCrewOwner(crewId, targetUserId)` — promote to owner
- `revokeCrewOwner(crewId, targetUserId)` — demote to member
- `removeCrewMember(crewId, targetUserId)` — kick member
- `addCrewMembers(crewId, userIds[])` — bulk add
- `searchUsers(query)` — search by username or display_name

Export: `export const crewProfileService = new CrewProfileService();`

---

### Push Token Domain

#### [NEW] `PushTokenService.ts`
Methods from the current `ProfileService` class:
- `registerPushToken(token, platform)` — store/update Expo push token
- `unregisterPushToken(token)` — remove on logout/revoke

Export: `export const pushTokenService = new PushTokenService();`

---

### Barrel Re-export

#### [MODIFY] `ProfileService.ts` (becomes barrel)
```typescript
// ProfileService.ts — Barrel re-export (maintained for zero call-site changes)
// All consumers continue to import from this path unchanged.

export type {
  UserProfile, PermanentCrew, CrewMemberDisplay,
  CrewMemberFull, SessionHistoryItem,
} from './ProfileService.types';

export { authProfileService } from './AuthProfileService';
export { crewProfileService } from './CrewProfileService';
export { pushTokenService } from './PushTokenService';

// profileService singleton: unified facade preserving the original API surface.
// All existing `profileService.xyz()` call-sites continue to work.
import { authProfileService } from './AuthProfileService';
import { crewProfileService } from './CrewProfileService';
import { pushTokenService } from './PushTokenService';

export const profileService = {
  ...authProfileService,
  ...crewProfileService,
  ...pushTokenService,
} as typeof authProfileService & typeof crewProfileService & typeof pushTokenService;
```

> [!IMPORTANT]
> The `profileService` singleton facade is the critical compatibility bridge.
> Every existing call `profileService.getMyCrew()` continues to resolve correctly.

---

## Execution Checklist

**Step 1**: Create `ProfileService.types.ts` — copy all 5 interfaces from ProfileService.ts verbatim.

**Step 2**: Create `AuthProfileService.ts` — copy `fetchOrCreateProfile`, `updateProfile`, `getSessionHistory`. Add `import { supabase }` and `import type { UserProfile, SessionHistoryItem }`.

**Step 3**: Create `CrewProfileService.ts` — copy all 16 crew methods. Add `import { supabase }` and `import type { PermanentCrew, CrewMemberDisplay, CrewMemberFull }`.

**Step 4**: Create `PushTokenService.ts` — copy `registerPushToken`, `unregisterPushToken`. Add `import { supabase }`.

**Step 5**: Overwrite `ProfileService.ts` with the barrel re-export pattern above.

**Step 6**: Run `npx tsc --noEmit` from master. Zero new errors is the exit condition.

**Step 7**: Verify the 12 call-site files — none should need changes. If TSC is clean, they're all good.

---

## Verification Plan

### Automated
```powershell
# Run from master (has node_modules)
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```
Exit condition: **zero new errors vs pre-refactor baseline.**

### Manual
- Check that `profileService.fetchOrCreateProfile()`, `profileService.getMyCrew()`, and `profileService.registerPushToken()` all resolve in editor without red underlines.
- Confirm none of the 12 importing files need modification.

---

## Rollback Strategy
If TSC fails after barrel construction: `git checkout -- src/services/ProfileService.ts` restores original. New files can simply be deleted. Zero risk to any consumer.

---

## Known Traps (from Devil's Advocate)
1. **Class spread limitation**: Spreading class instances (`{ ...instance }`) does NOT transfer prototype methods — only own enumerable properties. The facade must use explicit method delegation instead of spread. See barrel step for the correct pattern.
2. **Shared type imports**: All 3 service files import types from `ProfileService.types.ts` — not from each other — preventing circular deps.
3. **Barrel export order**: Types must be exported before the singleton facades that reference them.
