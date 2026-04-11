# UI Refinements Batch: Avatar Picker & Offline Crew Teaser

## Goal Description
Enhance the SK8Lytz UI by implementing a modern avatar color picker mechanism using the new `NeonHueStrip` component, replacing legacy discrete color dots in the User Account screen and replacing the basic `CustomSlider` in the Crew Hub creation flow. Additionally, we will improve the offline dashboard experience by conditionally shrinking the "Crew Hub" slab when `isOfflineMode` is active, reclaiming vertical screen real-estate for the docked controller and skate controls.

## User Review Required
> [!WARNING]  
> The avatar color in `UserProfile` is stored as a hex string (e.g. `'#FF6B6B'`). The `NeonHueStrip` outputs a Hue value (0-360). I will use a standard HSL-to-Hex conversion function inline to ensure color persistency matches the legacy string format. Existing color strings in the database will display on the slider correctly *only* if we map hex back to hue, or we can just default the slider locally on first render if no direct hue conversion is implemented. I plan to construct a simple Hex->Hue utility so users don't lose their current color preview when opening the tab.

## Proposed Changes

### Design Decisions & Rationale
NeonHueStrip provides a more tactile, premium touch interface than simple circular swatches. I will extract Hex/HSL conversion into shared utilities to map the stored hex `avatar_color` backward to 0-360 hue to initialize the strip. For the Crew offline teaser, shrinking the padding from 40 to 20 dynamically prevents a massive block of empty space when offline.

### UI & Platform Strategy
- **Responsive Sizing**: The `NeonHueStrip` is inherently fluid and uses touch gestures mapped to generic widths, so it works flawlessly across iOS/Android screens.
- **Offline Padding**: The `DashboardScreen` Crew Slab padding will be reduced from `40` to `20` (and `marginTop: 8` layout adjustments) only when `isOfflineMode` is true, ensuring it collapses into a compact "teaser" notice.

---

### [UI/Frontend Components]

#### [MODIFY] [AccountModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- Import `NeonHueStrip` and utility conversions.
- Replace the `AVATAR_COLORS` array and discrete `<View>` mapping with a minimal `<NeonHueStrip>` slider.
- Manage a local `hue` state initialized from `hexToHue(profile?.avatar_color)`. 

#### [MODIFY] [CrewModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewModal.tsx)
- Replace the legacy `CustomSlider` under "ICON COLOR" with the `NeonHueStrip`.
- Update the layout wrapper to provide adequate touch target height for the `NeonHueStrip`.

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- Shrink `paddingVertical` from `40` to `16` dynamically: `paddingVertical: isOfflineMode ? 16 : 40`.
- Hide the `slabTitle` and its icon if offline, or compact them directly into the "NETWORK DISCONNECTED" warning row to make the element significantly smaller.

## Open Questions
- Should the `NETWORK DISCONNECTED` message in the Dashboard stay red (`#ff4444`), or would a muted grey/amber fit the "teaser" aesthetic better? I plan to keep the red error icon but shrink the whole card size.

## Verification Plan
### Automated Tests
- Run `tsc` to verify no prop type mismatches with `NeonHueStrip`.

### Manual Verification
- Render the `AccountModal.tsx`, slide the hue strip, and apply the Hex changes to profile config.
- Toggle device internet connection (Simulated Offline Mode) and observe the Crew Slab shrink smoothly.
