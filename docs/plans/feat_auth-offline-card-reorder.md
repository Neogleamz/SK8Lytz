# Reorder Auth Screen Offline Card

This plan completes the `feat/auth-offline-card-reorder` ticket from the `epic/ui-refinement` Epic. The goal is to move the 'Continue Offline' card (and associated development/sandbox toggles) outside of the main `ScrollView`, anchoring it to the bottom of the `AuthScreen` to match the "Registered Devices Slab" pattern recently implemented on `DashboardScreen.tsx`.

## Proposed Changes

### `src/screens/AuthScreen.tsx`

We will modify the screen structure to extract the offline and sandbox UI elements from the `ScrollView` and place them inside a newly created docked bottom container.

#### [MODIFY] AuthScreen.tsx

- **Structure update**: Add a `<View style={styles.bottomDock}>` directly underneath the closing `</ScrollView>` tag but inside the `<KeyboardAvoidingView>`.
- **Refactoring UI Hierarchy**:
  - Locate the `Offline mode option` block (the "Continue Offline" button and "Remember my choice" checkbox).
  - Locate the `Sandbox mode option` blocks (the Developer Sandbox toggles & Virtual Skates Bypass).
  - Move these blocks out of the `ScrollView` and into the new `bottomDock` view.
- **Style Additions**:
  - Add `bottomDock` to `createStyles` with appropriate `paddingHorizontal`, `paddingBottom`, `paddingTop` (border radius/highlights to separate from the scroll view).
  - Adjust margins on the `ScrollView` so its content padding leaves room for the docked UI.

## Open Questions

- Should the docked container have a slight top border or background color variation relative to the rest of the screen to distinguish it, similar to the dashboard slab? I will match it to the standard `Colors.surfaceHighlight` or `backgroundColor` opacity that we used previously.

## UI Craftsmanship

- **Responsive Docking**: Extracting these out of the `<ScrollView>` directly resolves UX friction. On smaller screens, users previously had to scroll to discover the "Continue Offline" button. Using a flexible bottom dock ensures the primary action (Offline mode) is always glanceable and 1-tap ready, complying with our SIMULATED USER EXPERIENCE rules.

## Verification Plan

### Automated/Build Tests

- TypeScript Compiler (`npx tsc`) to ensure UI extraction has no prop or missing variable errors.

### Manual Verification

- We will view the login screen visually to confirm that the Offline button is perfectly centered at the floor of the screen and that the keyboard does not clip the form contents abruptly when open.
