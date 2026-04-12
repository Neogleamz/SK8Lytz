# Plan: `feat/app-wide-ux-tips`

### Design Decisions & Rationale
Contextual tooltips are implemented as a lightweight overlay system backed by an AsyncStorage dismissal registry. No external library — pure React Native `Animated` + `Portal`-style absolute positioning. Tips are keyed by ID and render once unless manually reset. They conform to the SK8Lytz aesthetic (dark glass card, neon accent).

---

## Proposed Changes

### [NEW] `src/components/UxTip.tsx`
```typescript
interface IUxTipProps {
  tipId: string;           // unique key in registry
  title: string;
  body: string;
  targetRef?: RefObject<View>; // optional — positions near target element
  position?: 'above' | 'below' | 'left' | 'right';
}
```
- Checks `AsyncStorage.getItem('@sk8lytz_tips_dismissed')` on mount. If the `tipId` is already in the dismissed set, renders nothing.
- Shows a glass card with dismiss "X" button. On dismiss, writes the `tipId` to the dismissed set.

### [NEW] `src/constants/UxTips.ts`
- Registry of all tip IDs and their content strings (to keep them out of JSX).

### [MODIFY] Friction Point Integrations:
| Location | Tip ID | Trigger |
|---|---|---|
| `ScannerAnimation.tsx` | `first-ble-scan` | First render of scanner |
| `DockedController.tsx` | `first-controller-open` | First open of controller |
| `CrewModal.tsx` | `first-crew-join` | First view of crew discovery tab |

---

## Open Questions
- **Q:** Should tips be resettable from Admin Tools for QA purposes?
- **Q:** Should the tooltip arrow point at a specific UI element, or just appear as a floating card?

## Verification Plan
1. Fresh install (or clear `@sk8lytz_tips_dismissed` from AsyncStorage).
2. Trigger the first BLE scan — verify "first-ble-scan" tip appears.
3. Dismiss it and re-trigger — verify it does NOT appear again.
