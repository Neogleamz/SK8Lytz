# [FEAT] App-Wide UX Tips

## Goal
Contextual tips system for key friction points.

## Proposed Changes
- Implement `ToolTip` component.
- Add 'First Time' tips for `DockedController` and `CrewHub`.
- Persist 'Dismissed' state in `AsyncStorage`.

## Verification Plan
- Clear Cache.
- Verify tips appear on first boot.
