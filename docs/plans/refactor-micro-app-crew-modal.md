# Implementation Plan: refactor/micro-app-crew-modal

> **Status:** Draft (Pending User Approval) | **Branch:** `refactor/micro-app-crew-modal`

## Introduction & Goal

`CrewModal.tsx` currently spans over 2,200 lines, bundling location services, BLE scene broadcasting, Supabase realtime subscriptions, WebSocket management, and 7 distinct screen UI states into one massive React component. This creates unpredictable render cycles, heavy prop-drilling, and poses a risk of dropped telemetry or BLE broadcast events when the UI is interacting.

The goal is to dismantle `CrewModal.tsx` into a strict "Micro-App" architecture utilizing cleanly separated domain hooks and isolated UI components, **without** causing any visual regressions or disrupting live syncing sessions.

## Architectural Strategy: The Strangler Fig Approach

To mitigate the risk of breaking active Bluetooth synchronizations or creating render cascades, this refactor will use a phased approach:

1. **State Isolation (No UI extraction yet)**
2. **Context Establishment**
3. **UI Surgery & Extraction**

---

### Phase 1: State Hooks Isolation (Pure Domain Logic)

We will extract the 30+ pieces of useState, useEffect, and service calls into decoupled hooks. These hooks will temporarily remain imported straight into `CrewModal.tsx` to ensure API parity.

#### [NEW] `src/hooks/useCrewHub.ts`

- **Responsibility:** Manage discovery, joining, and fetching of permanent crews and nearby sessions.
- **State moved:** `myCrews`, `nearbySessions`, `activeSessions`, `isGettingLocation`.
- **Logic moved:** locationService calls, `profileService.fetchMyCrews()`, auto-rejoin logic.

#### [NEW] `src/hooks/useCrewSession.ts`

- **Responsibility:** Manage the _active_ live session state, realtime subscriptions, member lists, and scene broadcasting.
- **State moved:** `currentSessionId`, `currentRole`, `sessionMembers`.
- **Logic moved:** `crewService.createSession()`, `subscribeAsLeader()`, `broadcastScene()`.

#### [NEW] `src/hooks/useCrewManage.ts`

- **Responsibility:** Creating new permanent crews, editing details, generating invite codes.
- **State moved:** Create/Edit form states, `newCrewName`, etc.

---

### Phase 2: Context Boundary

Once the domain hooks are proven stable, we must prevent prop-drilling hell.

#### [NEW] `src/context/CrewContext.tsx`

- **Responsibility:** Act as the single source of truth for the active Crew session.
- Wraps the interior of `CrewModal` allowing deep child components (like the list of active members or the join button) to pull `useCrewSession()` directly without passing props through 4 layers of views.

---

### Phase 3: Visual Surgery (Extracting the Monolith)

We will slice the 7 main render blocks currently sitting inside `CrewModal`'s conditional switch into standalone functional components.

#### [NEW] `src/components/crew/CrewLandingScreen.tsx`

- The "Hub" displaying My Crews and Live Near You.

#### [NEW] `src/components/crew/CrewManageScreen.tsx`

- UI for editing details and managing members of a permanent crew.

#### [NEW] `src/components/crew/CrewCreateSessionScreen.tsx`

- Custom time / location picker form for creating a scheduled or live session.

#### [NEW] `src/components/crew/CrewControllerScreen.tsx`

- The active live session view, handling leader broadcast controls or member syncing status.

#### [MODIFY] `src/components/CrewModal.tsx`

- Reduces from 2,200 lines to ~150 lines.
- Acts solely as the orchestrator/router that renders the Context Provider, the Animated slide-up container, and a simple switch statement rendering the clean sub-components based on `step`.

## Security & Performance Considerations

- **WebSocket/Realtime Persistence:** By defining the realtime subscription tightly inside `useCrewSession` with proper `useEffect` cleanup dependencies, we avoid duplicate channel bindings that cause memory leaks.
- **Memoization:** UI components like `<AvatarCluster />` or `<MemberListItem />` inside the extracted screens will be wrapped in `React.memo` to prevent a full 60 FPS drop when `lastLeaderScene` trickles through the app.
- **Location Permissions:** `useCrewHub` will cleanly isolate the foreground location logic, preventing double-prompting bugs.

## Verification Plan

### Automated

- Execute `npx tsc --noEmit` locally after every extraction phase to guarantee no dangling props or `any` typing crept in.

### UI / Functional

1. **The Safe Load:** Open Crew Hub → UI should match 1:1 with `master`.
2. **Session Lifeline:** Create a new live session → verify Leader UI shows broadcast toggle.
3. **Data Fetching:** Switch to "Live Near You" → confirm Location Service populates list correctly via the new `useCrewHub` hook.

---

> [!IMPORTANT]
> **User Review Required**  
> We have successfully mapped the extraction. **Type "proceed"** to authorize Phase 1 (State Hook Isolation) and begin execution.
