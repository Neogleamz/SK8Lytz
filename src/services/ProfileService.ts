/**
 * ProfileService.ts — Barrel Re-export (Zero call-site changes)
 *
 * This file is intentionally a re-export aggregator after the
 * epic/god-object-decomposition Meal 1 refactor.
 *
 * All 12 consumer files continue to import from this path unchanged:
 *   import { profileService, UserProfile, PermanentCrew } from '../services/ProfileService'
 *
 * The `profileService` singleton preserves the full original API surface
 * via explicit method delegation — NOT object spread, which does not
 * transfer class prototype methods.
 *
 * Domain ownership:
 *   AuthProfileService   — fetchOrCreateProfile, updateProfile, getSessionHistory
 *   CrewProfileService   — all crew CRUD, stats, member management, search
 *   PushTokenService     — registerPushToken, unregisterPushToken
 */

// ── Types (re-exported for backward compatibility) ────────────────────────────
export type {
  UserProfile,
  PermanentCrew,
  CrewMemberDisplay,
  CrewMemberFull,
  SessionHistoryItem,
} from './ProfileService.types';

// ── Focused service singletons (available for direct import if needed) ─────────
export { authProfileService } from './AuthProfileService';
export { crewProfileService } from './CrewProfileService';
export { pushTokenService } from './PushTokenService';

// ── Unified `profileService` facade (preserves full original API surface) ──────
import { authProfileService } from './AuthProfileService';
import { crewProfileService } from './CrewProfileService';
import { pushTokenService } from './PushTokenService';

export const profileService = {
  // ── Auth / Profile ──────────────────────────────────────────────────────────
  fetchOrCreateProfile:    authProfileService.fetchOrCreateProfile.bind(authProfileService),
  updateProfile:           authProfileService.updateProfile.bind(authProfileService),
  getSessionHistory:       authProfileService.getSessionHistory.bind(authProfileService),

  // ── Permanent Crews ─────────────────────────────────────────────────────────
  getMyCrew:               crewProfileService.getMyCrew.bind(crewProfileService),
  createPermanentCrew:     crewProfileService.createPermanentCrew.bind(crewProfileService),
  joinPermanentCrew:       crewProfileService.joinPermanentCrew.bind(crewProfileService),
  joinPublicCrewById:      crewProfileService.joinPublicCrewById.bind(crewProfileService),
  leavePermanentCrew:      crewProfileService.leavePermanentCrew.bind(crewProfileService),
  getCrewMemberCount:      crewProfileService.getCrewMemberCount.bind(crewProfileService),
  getCrewMembersForDisplay:crewProfileService.getCrewMembersForDisplay.bind(crewProfileService),
  updateCrew:              crewProfileService.updateCrew.bind(crewProfileService),
  getPublicCrews:          crewProfileService.getPublicCrews.bind(crewProfileService),
  deleteCrew:              crewProfileService.deleteCrew.bind(crewProfileService),
  getCrewStats:            crewProfileService.getCrewStats.bind(crewProfileService),
  getCrewMembersWithNames: crewProfileService.getCrewMembersWithNames.bind(crewProfileService),
  assignCrewOwner:         crewProfileService.assignCrewOwner.bind(crewProfileService),
  revokeCrewOwner:         crewProfileService.revokeCrewOwner.bind(crewProfileService),
  removeCrewMember:        crewProfileService.removeCrewMember.bind(crewProfileService),
  addCrewMembers:          crewProfileService.addCrewMembers.bind(crewProfileService),
  searchUsers:             crewProfileService.searchUsers.bind(crewProfileService),

  // ── Push Tokens ─────────────────────────────────────────────────────────────
  registerPushToken:       pushTokenService.registerPushToken.bind(pushTokenService),
  unregisterPushToken:     pushTokenService.unregisterPushToken.bind(pushTokenService),
};
