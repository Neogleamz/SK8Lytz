# Light Mode Rework (feat/light-mode-rework)

This plan details the process for redesigning the Light Mode palette to align with the Neogleamz identity. Currently, light mode uses stark `#FFFFFF` and generic blacks which feels harsh ("bad"). We will replace these with softer greys, brand dark blues, and high-energy oranges to deliver a premium, curated aesthetic.

## User Review Required

> [!IMPORTANT]
> Please review the proposed palette below. Do these hex codes align with the soft grey and dark blue tones you had in mind for light mode? (I've included a range of soft grey-blues to maintain brand consistency).

## Proposed Changes

### `src/theme/theme.ts`

#### [MODIFY] `theme.ts`

We will overhaul `LightColors` to eliminate the stark white backgrounds and generic black text, replacing them as follows:

```typescript
export const LightColors: ThemePalette = {
  background: "#EAEFF5", // Very Soft Greyish-Blue (No more stark white)
  surface: "#CBD6E2", // Elevated soft grey/blue for cards
  surfaceHighlight: "#DDE5EE",
  primary: "#FF5A00", // Brand Orange (No change, maintaining high visibility)
  secondary: "#FFB800", // Brand Amber
  accent: "#1B4279", // Brand Dark Blue
  text: "#0A1C38", // Deep Dark Blue (Replacing harsh black)
  textMuted: "#5C7491", // Smooth Blue-Grey for secondary text
  success: "#00C476",
  error: "#FF3D71",
  isDark: false,
};
```

### Component Triage (Removing Hardcoded Colors)

Several components have hardcoded fallback logic like `isDark ? '#141829' : '#ffffff'` (specifically `Sk8LytzDiagnosticLab.tsx` and `Sk8LytzProgrammerModal.tsx`) which explicitly forces stark white.

#### [MODIFY] `src/components/Sk8LytzDiagnosticLab.tsx`

- Replace hardcoded `#ffffff` card backgrounds with `Colors.surface`.
- Replace hardcoded `#111827` (almost black) text with `Colors.text`.

#### [MODIFY] `src/components/Sk8LytzProgrammerModal.tsx`

- Replace hardcoded `#ffffff` card backgrounds with `Colors.surface`.
- Replace hardcoded `#111827` text with `Colors.text`.

#### [MODIFY] `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`

- Ensure all text elements use `Colors.text` rather than explicitly checking `#000` to fix contrast issues when the theme updates.

## Open Questions

> [!WARNING]
> While we change `theme.ts`, should the Dashboard "Registered Devices" slab keep an orange accent header, or should the background of major cards utilize the new `surface` grey with orange reserved strictly for buttons/toggles?

## Verification Plan

### Manual Verification

- We will rebuild the app locally.
- Test the Auth Screen, Dashboard, and Admin Tools in Light Mode.
- Verify typography maintains strong contrast (4.5:1 ratio) against the soft grey.
- Identify any floating "stark white" components that didn't automatically inherit the new theme.
