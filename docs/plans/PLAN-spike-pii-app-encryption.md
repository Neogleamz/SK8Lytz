# Implementation Plan

# Spike: Application-Level PII Encryption

Investigate and draft an implementation strategy for client-side encryption of PII data before syncing to Supabase, pivoting away from database-level TCE (`pgsodium`).

## User Review Required
This is a spike. The outcome will be an Architecture Decision Record (ADR) on which crypto library to use and how to handle key management, which will require user review before implementing the actual encryption.

## Open Questions
- Should the client-side encryption key be derived from the user's password, or generated and stored in a secure enclave (Keychain/Keystore) via `expo-secure-store`?

## Proposed Changes

### [Spike Execution]
- **Step 1:** Evaluate `libsodium-wrappers` vs `react-native-quick-crypto` for React Native / Expo compatibility and performance.
- **Step 2:** Prototype a minimal wrapper utility (e.g., `CryptoService.ts`) that can encrypt/decrypt a string.
- **Step 3:** Document the key-management strategy for offline users. Where does the key live? How is it recovered if the user reinstalls the app?
- **Step 4:** Outline the migration path for existing plaintext PII in the Supabase database.
  - **Source:** [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md):L1

## Verification Plan

### Manual Verification
- Produce an Architecture Decision Record (ADR) detailing the chosen library, key management strategy, and migration path.
- The spike is complete when the ADR is approved by the user.
