# Refactor AuthContext to strict Finite State Machine

Refactor the `AuthContext` from using multiple overlapping boolean flags (`sessionLoaded`, `isOfflineMode`, `sessionExpired`) into a strict Finite State Machine (FSM) using a union type `AuthStatus`. This will eliminate impossible state combinations and simplify the authentication logic.

## Proposed Changes

### Core

#### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)

1.  **Define `AuthStatus` Union Type**:
    ```typescript
    export type AuthStatus = 'checking' | 'authenticated' | 'expired' | 'offline' | 'unauthenticated';
    ```
2.  **Add `status` to `AuthContextValue` interface**:
    Expose the FSM status directly to consumers who wish to use it.
3.  **Refactor internal state**:
    Remove `sessionLoaded`, `isOfflineMode`, and `sessionExpired` state variables. Replace with:
    ```typescript
    const [status, setStatus] = useState<AuthStatus>('checking');
    ```
4.  **Update `init()` function**:
    - Supabase not configured: `setStatus('offline')`
    - Continue offline skipped: `setStatus('offline')`
    - Session exists: `setStatus('authenticated')`
    - Token expired (last email exists): `setStatus('expired')`
    - No prior session: `setStatus('unauthenticated')`
5.  **Update reactive listener**:
    - Signed out: `setStatus('unauthenticated')`
    - Signed in: `setStatus('authenticated')`
6.  **Maintain Backwards Compatibility (Zero Blast Radius)**:
    Compute the legacy boolean flags from the new FSM state before returning the context value, ensuring that none of the components consuming `useAuth()` break:
    ```typescript
    const isOfflineMode = status === 'offline';
    const sessionLoaded = status !== 'checking';
    const sessionExpired = status === 'expired';
    const isAuthenticated = status === 'authenticated' || status === 'offline' || !supabase;
    ```
7.  **Update Actions**:
    - `setIsOfflineMode(true)` becomes `setStatus('offline')`.
    - `clearOfflineMode()` becomes `setStatus(session ? 'authenticated' : 'unauthenticated')`.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure TypeScript compilation passes.
- The unit tests should pass since the external API of `useAuth` is preserved.

### Manual Verification
- Launch the application and observe the transition from 'checking' to the appropriate state.
- Ensure "Continue Offline" sets the state correctly and bypasses login.
- Ensure logging out transitions correctly.
