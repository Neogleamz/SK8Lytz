# Implementation Plan: sweep-src-components-crew

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-crew` domain cluster.

## Batch & Wave
- **Wave:** 2
- **Prerequisite:** Wave 1 fully merged

## Proposed Changes
### [MODIFY] CrewCard.tsx
- Line 112: Fix `R-25` violation. Multiple action chips, copy-to-clipboard, expand/collapse chevron, and join session touchable elements in CrewCard lack accessibility labels and roles, preventing screen reader users from managing or interacting with crews. (Suggested: Add accessibilityRole="button" and descriptive accessibilityLabel attributes (e.g., 'Copy invite code', 'Expand crew details') to all TouchableOpacity components.)
- Line 11: Fix `R-08` violation. Props styles, Colors, cardMembers, formState, setStep, and profileService are typed as any or any[], bypassing compiler checks and violating codebase type safety rules. (Suggested: Replace any with strict typing or imported interfaces from models/theme/services (e.g., StyleProp<ViewStyle>, ThemePalette, etc.).)
- Line 30: Fix `R-08` violation. Use of explicit 'any' type bypasses compiler type checking for profileService in CrewCard component. (Suggested: Import the actual CrewProfileService type from services and use it for type annotations instead of 'any'.)
- Line 117: Fix `R-07` violation. Inline arrow function in rendering cycle of crew cards. (Suggested: Use useCallback to generate stable reference handlers.)
### [MODIFY] CrewDetailStats.tsx
- Line 7: Fix `R-08` violation. Props for CrewDetailStats use explicit 'any' type definitions for stats, styles, and Colors parameters, bypassing TypeScript validation. (Suggested: Create interface definitions representing actual stats shape and import proper types for styles/Colors.)
### [MODIFY] CrewDetailEditForm.tsx
- Line 7: Fix `R-08` violation. CrewDetailEditForm props define multiple variables (crew, styles, Colors, cardMembers, profileService, selectedMembers, search results) as 'any' or arrays of 'any'. (Suggested: Replace 'any' annotations with formal type imports.)
### [MODIFY] CrewJoinScreen.tsx
- Line 36: Fix `R-08` violation. Mapping of updated crews uses explicit 'any' casting on array elements. (Suggested: Cast or type updatedCrews using PermanentCrew[] from ProfileService types.)
- Line 124: Fix `R-07, R-28` violation. An inline hook call (`React.useCallback`) is invoked directly inside the JSX prop expression of the `keyExtractor` prop of `FlatList`. This violates the React Rules of Hooks (hooks should never be called inside conditional blocks or nested JSX expressions), and it defeats the purpose of the hook because it executes on every render cycle. This can lead to unexpected React runtime hook ordering bugs. (Suggested: Move the `keyExtractor` definition to the top level of the component as a standard hook: `const keyExtractor = useCallback((s: CrewSession) => s.id, []);`)
- Line 33: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
- Line 56: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'sess.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(sess.name) or log the unique ID instead of name.)
### [MODIFY] CrewScheduleScreen.tsx
- Line 134: Fix `R-08` violation. DateTimePicker onChange and mapped spots are typed with explicit 'any', disabling compiler checks. (Suggested: Use the proper event types from @react-native-community/datetimepicker packages.)
- Line 49: Fix `R-09` violation. PII key 'sessionId' passed with unscrubbed value 'newSession.id' in AppLogger call. (Suggested: Wrap value in scrubPII(newSession.id) if it contains sensitive user information.)
### [MODIFY] CrewLandingScreen.tsx
- Line 169: Fix `R-16` violation. Presence of over 20 inline style structures violates style co-location and optimization protocols. (Suggested: Move all inline styles to a StyleSheet.create declaration.)
- Line 65: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'sessionData.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(sessionData.name) or log the unique ID instead of name.)
- Line 99: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
- Line 120: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
- Line 139: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
- Line 187: Fix `R-14` violation. CrewLandingScreen renders personal crews list from hub.myCrews but lacks any loading or error UI feedback on personal crew queries. Network or query failure results in a silent fallback to an empty state with no retry option. (Suggested: Track loading and error states for personal crews explicitly. Render an ActivityIndicator during fetches, and an ErrorCard retry button if profileService.getMyCrew fails.)
### [MODIFY] CrewManageScreen.tsx
- Line 101: Fix `R-16` violation. Significant inline styles and arrow functions inside scroll container rendering cycle. (Suggested: Move styling to CrewStyles.ts sheet.)
- Line 65: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
### [MODIFY] CrewLandingMap.tsx
- Line 97: Fix `R-16` violation. Inline styles constructed inside map loops render performance hits to marker rendering on map updates. (Suggested: Use StyleSheet styles or externalize properties which do not depend on loop variables.)
### [MODIFY] MapFiltersTray.tsx
- Line 32: Fix `R-16` violation. Inline style construction inside rendering loop of filter buttons. (Suggested: Predefine style variations in StyleSheet.create.)
### [MODIFY] CrewCreateScreen.tsx
- Line 54: Fix `R-09` violation. PII key 'sessionId' passed with unscrubbed value 'newSession.id' in AppLogger call. (Suggested: Wrap value in scrubPII(newSession.id) if it contains sensitive user information.)
### [MODIFY] CrewDetailScreen.tsx
- Line 50: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
- Line 62: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)
- Line 121, 124: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'editCrewName.trim()' in AppLogger call. (Suggested: Wrap resource name in scrubPII(editCrewName.trim()) or log the unique ID instead of name.)
- Line 131: Fix `R-14` violation. CrewDetailScreen assumes the crew is already resolved and lacks local loading/error state handling for sub-queries like fetching members or session statistics, failing silently if they fail to resolve. (Suggested: Track local loading and error states for stats and member fetching, rendering local loaders and retry error cards.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
