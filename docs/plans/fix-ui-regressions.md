# Fix UI Regressions (Dashboard & Music Mode)

Both of the requested high-priority bucket list items are straightforward UI regressions. We'll knock them both out in this single branch (`fix/ui-regressions`).

### Design Decisions & Rationale
We are combining these two minor UI fixes into a single `fix/ui-regressions` branch to streamline deployment, since both modify basic component rendering without touching the underlying Bluetooth protocols or database logic. Reattaching `onLongPress` and reorganizing the Grid components will provide immediate quality of life improvements without adding tech debt.

## Proposed Changes

### Dashboard: Group Long-Press Fix
#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- The group card `TouchableOpacity` (in Slab #3) currently only has an `onPress` event handler.
- We will add an `onLongPress` prop to the container that maps to the existing, intact group-editing state logic:
  ```ts
  onLongPress={() => {
    setGroupModalMode('rename');
    setEditingGroupId(group.id);
    setIsGroupModalVisible(true);
  }}
  ```

---

### Music Mode: Duplicate Toggles Cleanup
#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- In the `MUSIC` mode layout tree, two sets of Matrix Style (Light Screen / Light Bar) toggles currently exist.
- The original/old set exists at the very top of the section (around line 1686).
- The newly styled set (added during the Lab Parity update, showing hex values like `0x27 / 0x26`) exists lower down (around line 1772).
- We will fully delete the old block, and **move the new block** up to the top of the section as requested.

## Verification Plan

### Manual Verification
1. Open the App in Dev mode.
2. In the Dashboard, hold a long-press on any existing Device Group to ensure the edit modal pops up (or verify the element via code since it's hard to test on PC without the phone).
3. Open the universal `DockedController` and switch to Music Mode. Verify that there is only one "Light Screen / Light Bar" matrix selector prominently at the top of the view.
