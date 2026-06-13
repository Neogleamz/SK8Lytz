# Plan: `fix/auth-page-scrolling`

### Design Decisions & Rationale

The auth screen layout overflows vertically because the `KeyboardAvoidingView` + `ScrollView` container does not use `flex: 1` to constrain to the safe-area height. The fix enforces a strict `flex: 1` tree so content fills exactly the screen height and the "Continue offline" section compresses rather than pushing content off-screen.

---

## Proposed Changes

### Target File: `src/screens/AuthScreen.tsx`

#### [MODIFY] Root container

- Change any top-level container `height: '100%'` or unconstrained `style` to `{ flex: 1 }` so it anchors to the safe-area inset.

#### [MODIFY] "Continue offline" section

- Remove fixed height or padding from the offline CTA block.
- Replace with `paddingVertical: 8` and `marginBottom: 0` so it hugs the bottom without extra push.

#### [MODIFY] ScrollView / KeyboardAvoidingView

- Wrap content in `<ScrollView contentContainerStyle={{ flexGrow: 1 }}>` so it only scrolls if the keyboard is open; otherwise it fills the space exactly.

---

## Open Questions

- None — purely a layout fix with no business logic impact.

## Verification Plan

1. Open the Auth screen on a short-screen device (e.g. Pixel 4a).
2. Verify that no vertical scrollbar or bounce appears when tapping fields without the keyboard open.
3. Open the keyboard and confirm the layout gracefully compresses.
