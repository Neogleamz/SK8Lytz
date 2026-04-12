# [PLAN] feat/eula-onboarding (The Legal Shield)

### Design Decisions & Rationale
To protect the brand and ensure user safety, we are implementing a mandatory **Legal Shield**. This replaces ad-hoc settings with a centralized **EULA Enforcement Engine**. The app will reference a `required_eula_version` from Supabase and compare it against the user's `accepted_eula_version`.

## User Review Required
> [!IMPORTANT]
> **Blocking Flow**: This will prevent ANY user from accessing the app until they accept the required EULA version.
> [!NOTE]
> **Draft Text**: This plan requires the final "Badass" EULA text which covers Kinetic Safety, Flash Sensitivity, Data, and Conduct.

## Proposed Changes

### [Component Name] DB & Governance

#### [NEW] [Migration: Add EULA Versioning]
```sql
ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS accepted_eula_version INT DEFAULT 0;
-- Update app_settings with the initial required version
INSERT INTO app_settings (setting_key, setting_value) 
VALUES ('required_eula_version', '1')
ON CONFLICT (setting_key) DO UPDATE SET setting_value = '1';
```

### [Component Name] UI & Onboarding

#### [NEW] [EulaModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/modals/EulaModal.tsx)
- Premium, scrollable modal displaying the "Badass" EULA text.
- Includes a primary "I ACCEPT" button that updates the user profile.

#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- Inject a `ComplianceSection` in the SIGNUP mode.
- Link: "By creating an account, you agree to the [SK8Lytz EULA]"
- Prevent `handleSignUp` from executing if the checkbox is not checked.

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)
- Add the **Required EULA Version** slider/input to the App Manager.
- Bump this to force all users to re-sign.

## The "Badass" EULA Components
- **Kinetic Safety**: Skating is inherently dangerous. SK8Lytz is a lighting enhancement, not PPE.
- **Visual Safety**: Photosensitivity warning for Symphony effects.
- **Conduct**: No harassment or non-skating content in Crew Hub.
- **Data**: Telemetry collected for hardware tuning and connectivity improvement.

## Verification Plan
1. **Onboarding Test**: Create a new account and verify that you cannot click "Create" until the terms are accepted.
2. **Migration Test**: Verify that the profile in Supabase shows `accepted_eula_version: 1` after signup.
3. **Enforcement Test**: Manually bump the global version to `2` and verify the Dashboard is blocked by the "Legal Update" modal.
