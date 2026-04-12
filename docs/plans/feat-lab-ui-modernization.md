# Plan: `feat/lab-ui-modernization`

### Design Decisions & Rationale
`Sk8LytzDiagnosticLab.tsx` at 63KB is functionally complete but aesthetically inconsistent with the rest of the app — it uses raw `TextInput`, flat `TouchableOpacity` buttons, and no dark glass treatment. This plan applies the standard SK8Lytz design system tokens (colors, border radii, typography, spacing) to bring it into visual parity without touching any of the protocol logic.

---

## Proposed Changes

### [MODIFY] `src/components/Sk8LytzDiagnosticLab.tsx`
- Replace all raw `style={{ ... }}` ad-hoc styles with design system tokens from `src/theme/`.
- Apply `borderRadius: 12`, `backgroundColor: 'rgba(255,255,255,0.05)'` glass card treatment to each lab section.
- Replace `TouchableOpacity` text buttons with the standard `CustomSlider` / pill button pattern used elsewhere.
- Apply `fontFamily: 'Outfit'` (or the project's standard font) to all `Text` elements.
- Enforce 8pt grid spacing (8, 16, 24, 32px) for all padding/margin.
- Add loading/empty/error states for the payload test outputs (currently raw text dumps).

---

## Open Questions
- **Q:** Should this be gated behind `__DEV__` only, or is the Diagnostic Lab accessible to end users via Admin Tools in production?
- **Q:** Is there a shared `theme/index.ts` or `StyleSheet` token file already in place in `src/theme/`?

## Verification Plan
1. Open Admin Tools > LED Diagnostic Lab.
2. Verify all sections use consistent dark glass cards, spacing, and typography.
3. Verify all protocol functionality (send payload, parse response) still works identically.
