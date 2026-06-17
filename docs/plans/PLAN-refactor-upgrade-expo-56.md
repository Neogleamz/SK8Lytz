# Implementation Plan: Upgrade Expo SDK 56

## Objective
Execute a major dependency upgrade to migrate the codebase from Expo SDK 55 to Expo SDK 56 (including React Native 0.85 and React 19.2), while resolving the outstanding NPM vulnerabilities and tech debt backlog.

## Files to Create/Modify
- `package.json`
- `package-lock.json`
- `app.json`

## Execution Steps

1. **Dependency Alignment**
   - Run `npx expo upgrade` to align core packages with Expo SDK 56 requirements.
   - Run `npm install` to enforce the new lockfile.
   - *Verify: Ensure `package.json` reflects `"expo": "^56.0.0"`.*

2. **Vulnerability Remediation**
   - Run `npm audit fix --force` to crush the remaining moderate vulnerabilities (`js-yaml`) in the testing and compilation dependencies.
   - *Verify: `npm audit` reports 0 vulnerabilities.*

3. **Code Mod / Migration**
   - Run `npx expo-codemod sdk-56-expo-router-react-navigation-replace src/` to resolve the breaking separation between Expo Router and React Navigation.
   - *Verify: No manual `@react-navigation/*` imports remain where prohibited.*

4. **Verification Gate**
   - Execute `npm run verify`.
   - *Verify: TSC compiler is green and Jest unit tests pass with the new Babel and Jest versions.*

## Out of Scope
- Rewriting or refactoring individual components or hooks unless explicitly broken by the React Native 0.85 upgrade.
- Backend Supabase architecture changes.
