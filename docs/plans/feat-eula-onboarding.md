# [PLAN] feat/eula-onboarding (The Legal Shield)

### Design Decisions & Rationale
To protect the brand and ensure user safety, we are implementing a mandatory **Legal Shield**. This replaces ad-hoc settings with a centralized **EULA Enforcement Engine**. The app will reference a `required_eula_version` from Supabase and compare it against the user's `accepted_eula_version`. This is a "Rock Solid" implementation designed to cover kinetic safety, sensory risk (strobes), and hardware-specific disclaimers (BLE reliability).

## User Review Required
> [!IMPORTANT]
> **Blocking Flow**: This will prevent ANY user from accessing the app until they accept the required EULA version.
> [!CAUTION]
> **Safety Liability**: This prose is designed by AI to be "rock solid" for a hardware/skating context, but the user should have it reviewed by a human legal professional before a production release.

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
- Premium, scrollable modal displaying the **Rock Solid EULA** text.
- **Scroll-to-Accept**: The "I ACCEPT" button remains `disabled` until the User has reached the end of the scrollable text.
- Dispatched via `ComplianceGate` provider that wraps the main Dashboard.

#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- Inject a mandatory `ComplianceCheckbox` in the SIGNUP mode.
- Link: "By creating an account, you agree to the [SK8Lytz EULA]" (opens EulaModal).
- Record the `accepted_eula_version` (1) during the Supabase registration call.

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)
- Add a "Global EULA Version" control to the App Manager tab.
- Bumping this version will trigger a re-acceptance gate for all users during their next session.

## 📝 The SK8Lytz "Rock Solid" EULA Prose

**Section 1: Acceptance**
By downloading or using SK8Lytz, you agree to be bound by these terms. This App controls high-intensity lighting hardware; your use signifies your understanding of the physical risks involved.

**Section 2: Physical Safety & Waiver**
SK8Lytz is an entertainment platform, NOT safety gear. The lights do not replace helmets, pads, or reflectors. You acknowledge that skating is inherently dangerous and you "skate at your own risk." NEVER modify app settings while in motion.

**Section 3: Photosensitivity Warning**
App permits high-frequency flashing/strobing (especially Symphony modes). Individuals with epilepsy should consult a physician before use. Discontinue if you experience dizziness or disorientation.

**Section 4: Hardware & BLE Disclaimer**
App provided "As-Is." BLE connections may drop due to interference. Battery modeling is an approximation; Neogleamz is not liable for unexpected hardware darkness.

**Section 5: Data & Privacy**
By using Street Mode/Crew Hub, you consent to real-time GPS coordinates transmission for discovery. Data is used for connectivity tuning and will NOT be sold to 3rd party advertisers.

**Section 6: Limitation of Liability**
Neogleamz is NOT liable for direct or indirect damages (including personal injury) resulting from app use, hardware disconnects, or failure of lights during navigation.

**Section 7: Mandatory Arbitration**
Any disputes shall be resolved through binding arbitration. You waive the right to class-action lawsuits.

## Verification Plan
1. **Onboarding Test**: Create a new account; verify that the registration is gated by the EULA checkbox.
2. **Scroll Test**: Open the EulaModal; verify the "I ACCEPT" button only activates at the bottom of the scroll.
3. **Database Guard**: Manually set a user's `accepted_eula_version` to `0` in Supabase and verify they are blocked upon dashboard entry.
4. **Admin Bump**: Increment `required_eula_version` in the Admin Hub and verify active sessions are redirected to the EULA gate.
