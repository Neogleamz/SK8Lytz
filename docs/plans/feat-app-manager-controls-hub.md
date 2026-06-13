# Plan: feat/app-manager-controls-hub
> Status: BLOCKED — Awaiting table isolation prerequisite
> Last updated: 2026-04-20

## Goal
Rebuild the App Manager into a tabbed, registry-driven Controls Hub that is the single source of truth for all SK8Lytz admin-facing feature toggles and governance policies.

---

## ⛔ CRITICAL BLOCKER: Shared `app_settings` Table

**Discovery (2026-04-20):** The `app_settings` Supabase table is shared between the **SK8Lytz app** and the **Neogleamz Inventory web app**. This is an architectural violation that must be resolved before any Controls Hub work can proceed.

**Evidence:** Live query of `app_settings` returned:
- `ui_prefs`, `ui_prefs_v2` — Inventory app UI column orderings
- `paper_profiles` — Dymo/shipping label profiles for inventory
- `global_haptics_enabled`, `global_optimistic_ui_enabled`, `required_eula_version` — **SK8Lytz settings**

**Risk:** Building the Controls Hub on a shared table would expose cross-product data pollution in the admin UI and create tight coupling between two independent products.

### Prerequisite: `chore/sk8lytz-settings-table-isolation`
1. Create a new, dedicated `sk8lytz_app_settings` table with proper RLS
2. Migrate all SK8Lytz-specific keys from `app_settings` into the new table
3. Update `AppSettingsService.ts` to point to the new table
4. Add `updated_at TIMESTAMPTZ` + auto-update trigger to the new table (for audit display)
5. Confirm the Neogleamz Inventory app is fully decoupled (does not read SK8Lytz keys)
6. Leave `app_settings` untouched for the Inventory app

---

## Phase 0: Fix SSOT Bypass (Prerequisite 2)

**Ticket:** `fix/hw-notifications-ssot-bypass`

Currently, hardware notification settings are written directly via raw `AsyncStorage.setItem()`, bypassing `AppSettingsService`. These must be routed through `AppSettingsService.updateSetting()` before the Controls Hub can claim to surface "all settings."

- Add `'global_notifications_enabled'` to `AppSettingKey` union type
- Route raw AsyncStorage write through `AppSettingsService.updateSetting()`

---

## Phase 1: Formalize CONTROLS_REGISTRY

Add a typed `CONTROLS_REGISTRY` array to `AppSettingsService.ts`:

```ts
type ControlType = 'toggle' | 'stepper' | 'destructive_action';
type ControlGroup = 'social' | 'hardware' | 'notifications' | 'maps_geo' | 'legal';

interface ControlDef {
  key: AppSettingKey;
  group: ControlGroup;
  label: string;
  description: string;
  type: ControlType;
  dangerous: boolean;           // triggers Safety Lock confirm modal
  default: boolean | string | number;
  badgeColor?: string;
}
```

All 5 category tabs render by filtering: `CONTROLS_REGISTRY.filter(c => c.group === activeTab)`

This means adding new toggles forever requires only adding one object to the registry — zero JSX edits.

---

## Phase 2: Rebuild AppManager.tsx (UI)

Replace the current flat scroll list with a category-tab screen.

### Tab Taxonomy

| Tab | Controls |
|---|---|
| **SOCIAL** | CREWZ Hub global lock, CREWZ hide offline, Community Hub lock, Community hide offline |
| **HARDWARE** | Ghost/Optimistic UI, Haptics |
| **NOTIFICATIONS** | Global push notifications *(unblocked by Phase 0)* |
| **MAPS & GEO** | Skate Maps lock, Maps hide offline, Telemetry upload toggle |
| **LEGAL** | EULA version stepper + bump action |

### Functional Richness Features

1. **Per-control save state** — each toggle row shows `cloud-sync` spinner while in-flight, then `check-circle` for 1.5s on success
2. **Last enforced timestamp** — `updated_at` from new table surfaced as sub-label on each row (e.g. "Last enforced: 5 hours ago")
3. **Danger Zone visual treatment** — `dangerous: true` controls get red left-border accent; confirm modal via existing `handlePolicyToggle`
4. **Audit trail** — `SETTING_CHANGED` event type added to `AppLogger`; appears in Admin Tools timeline tab
5. **Search bar** — filter `CONTROLS_REGISTRY` by label/description; renders above tab bar

### Explicit Non-Scope
- ❌ No user-facing settings (admin-only)
- ❌ No Diagnostic Lab changes
- ❌ No DashboardScreen or BLE core changes
- ❌ No new navigation layers

---

## Future Backlog (Post-Shipping)

- `chore/app-settings-namespace-cleanup` — Evaluate whether to add `sk8lytz_` prefix to all keys for future-proofing
- `chore/master-ref-storage-key-sync` — Document new `@sk8lytz_app_settings` cache key in Master Reference AsyncStorage Key Registry

---

## Verification Plan

1. Every toggle in each tab fires optimistic update and confirms via cloud sync spinner
2. `updated_at` timestamp updates correctly after each save (verify in Supabase table)
3. `SETTING_CHANGED` appears in Admin Tools → Timeline tab after each change
4. No Inventory app settings (`ui_prefs`, `paper_profiles`) visible in the hub
5. Dangerous toggles (CREWZ lock, Maps lock) require confirm modal before applying
6. TypeScript: `npx tsc --noEmit` passes with zero errors
