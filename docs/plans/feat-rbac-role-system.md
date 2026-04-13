# Implementation Plan: feat/rbac-role-system

### Design Decisions & Rationale
To support User, Moderator, Crew Leader, and Admin roles securely, we will implement Role-Based Access Control (RBAC) at the Supabase database level. We will build a `public.user_roles` table and use Postgres functions to inject custom claims into the JWT, or enforce RLS (Row Level Security) across the app using `auth.uid()` checks against the `user_roles` table. This prevents unauthorized administrative actions (like global bans or crew deletions) from being executed via intercepted client calls, ensuring a zero-trust model.

### Proposed Changes

#### 1. Supabase Database Schema Updates
- **[NEW] `user_roles` table**: Create a table mapping `user_id` to an `app_role` ENUM (`user`, `moderator`, `crew_leader`, `admin`).
- **[MODIFY] RLS Policies**: Update existing RLS policies on `crews`, `crew_sessions`, and `user_profiles` to allow `admin` or `moderator` to bypass standard ownership checks.

#### 2. Local Types Sync
- **[MODIFY] `src/types/supabase.ts`**: Run the `generate_typescript_types` MCP tool to ingest the new `app_role` enum and the `user_roles` table definition.

#### 3. Frontend Authentication Context
- **[MODIFY] Auth Context/Store**: Fetch the user's role on login and expose `isAdmin`, `isModerator`, and `isCrewLeader` booleans to the React component tree.
- **[NEW] Administrative UI Guards**: Wrap sensitive UI components (like the Admin Hub or Global Kick controls) in a `<RoleGuard expectedRole="admin">` wrapper.

### Open Questions
- Do we want to manage roles purely via Supabase Dashboard, or do we need an in-app Admin Hub UI specifically for changing another user's role?
- Should `crew_leader` be a global role, or strictly a situational role scoped only to `crew_memberships` (like it currently functions)?

### Verification Plan
- **Automated tests**: Verify RLS policies block standard users from updating other profiles.
- **Manual Verification**: Escalate a test account to `admin` in the Supabase Dashboard, then verify the Admin Hub becomes accessible in the Expo app without restarting the server.
