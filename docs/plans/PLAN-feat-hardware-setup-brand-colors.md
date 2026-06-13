# Implementation Plan

Update Hardware Setup Wizard to use Neogleamz brand colors (Blue and Orange) instead of generic Red and Green for skate identification.

## Proposed Changes

### src/screens/Onboarding/HardwareSetupWizardScreen.tsx
- **Goal:** Replace Red/Green identification colors with Neogleamz brand Blue (`#1B4279`) and Orange (`#F79320`).
- **[MODIFY] src/screens/Onboarding/HardwareSetupWizardScreen.tsx**
  - **Hardware Flash Colors:** In `fireOrientationTest`, update the payload color logic so that 'Left' flashes Blue `{ r: 27, g: 66, b: 121 }` and 'Right' flashes Orange `{ r: 247, g: 147, b: 32 }`.
  - **UI Button Text/Icons:** Update the `IDENTIFY PORT/STARBOARD` and `SWAP LEFT/RIGHT` buttons to reference (BLUE/ORANGE) instead of (RED/GREEN).
  - **Blink Button Colors:** Update the dynamic blink button logic in Step 3 so that Left matches Blue (`#1B4279`) and Right matches Orange (`#F79320`).

## Verification Plan
### Manual Verification
- Run `/dev-server`, open the app to the Hardware Setup Wizard.
- Proceed to Step 3.
- Verify the Swap button says "SWAP LEFT/RIGHT (BLUE/ORANGE)".
- Tap the Blink buttons and verify they use the brand colors instead of Red/Green.

## Out of Scope
- Changing global theme colors in `theme.ts` (this is specific to the hardware identification step).
