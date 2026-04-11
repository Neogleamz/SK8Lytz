# Implementation Plan: Product Manager Reorganization & Enhancement

Relocating the 'Products' tab to the 'Tools' sub-tab, renaming it to 'Product Manager', and implementing a dropdown-based selection system to allow seamless switching between SOULZ, HALOZ, and RAILZ profiles.

## User Review Required

> [!IMPORTANT]
> **Navigation Change**: The top-level 'Products' tab will be removed. You will now find the 'Product Manager' inside the **Tools** tab alongside the LED Lab and Picks Scheduler.

## Proposed Changes

### [Admin Tools UI]

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)
- **Tab Hierarchy**: 
    - Remove `'products'` from the `Tab` type union.
    - Remove the 'Products' button from the `View style={[styles.tabs, ... ]}` render block.
- **Product Manager Trigger**:
    - Add a `Product Manager` button in `renderAdminTab`.
- **Product Manager View**:
    - Replace the current list-based `renderProductsTab` with a **Dropdown-First Editor**.
    - Top section: A horizontal scroll selector (pills) for all loaded profiles + a "+" icon for new.
    - Mid section: The active profile editor (already implemented, but now driven by selector).
    - Back button: To return to the main Tools menu.

### [Data Handling]

#### [STAY] [useProductCatalog.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductCatalog.ts)
- Existing logic correctly fetches all profiles; the UI reorganization will leverage the existing `allProfiles` array more effectively.

## Open Questions

- **Dropdown Style**: I propose using a horizontal row of "pills" at the top representing each product (HALOZ, SOULZ, RAILZ). Is this acceptable, or would you prefer a standard vertical select list?
- **Persistence**: Should the Product Manager remember the last selected product when you close and reopen the modal, or always start with a clean "Select Product" screen?

## Verification Plan

### Manual Verification
1. Open Admin Tools (10-tap logo + 0000).
2. Tap **Tools** tab.
3. Tap **Product Manager**.
4. Verify all products (SOULZ, HALOZ, RAILZ) are selectable from the top bar.
5. Verify that editing one product and switching to another correctly updates the fields.
6. Verify that adding a new product (+ icon) works and saves to Supabase.
