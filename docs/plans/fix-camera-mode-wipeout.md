# Implementation Plan: Standardized Camera Layout & Auth Branding

The goal is to normalize the Camera Mode layout to match the application's standard flexible architecture and implement the requested branding on the Auth screen.

## User Review Required

> [!IMPORTANT]
> **Layout Standardization**: I will remove the specific `CAMERA` height/flex overrides. Camera Mode will now behave exactly like the other modes, using all available flex space between the visualizer and the bottom dock/sliders.

## Proposed Changes

### [UI Layout]

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)

- **Controls Layout**:
  - Remove line 1452 override: `<View style={[styles.controlsContainer, { padding: 4, overflow: 'hidden' }]}>`.
- **Camera Block Render**:
  - Ensure the Camera render block uses `flex: 1` without hardcoded heights or min-heights.

#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)

- **Branding Link**:
  - Add a small `TouchableOpacity` below the logo.
  - Logic: `Linking.openURL('https://neogleamz.com')`.
  - Style: `fontSize: 10`, `color: Colors.textMuted`, `alignSelf: 'flex-end'`, `marginRight: '15%'`.

## Verification Plan

### Automated/Subagent Tests

- `browser_subagent`: Verify Camera Mode container renders with same flex footprint as Favorites or Street mode.
- `browser_subagent`: Verify "by neogleamz.com" link is visible on Auth screen.

### Manual Verification

- Confirm the Camera view is correctly positioned between the visualizer and the sliders.
