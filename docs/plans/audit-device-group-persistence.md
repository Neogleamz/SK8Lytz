# Resolve Ghost Device Groups & Enforce Offline Persistence

Audit findings indicate that SK8Lytz is suffering from a "Split-Brain" architecture regarding Device Groups (Fleets).
Currently, the application tries to maintain dynamic group mappings inside `DashboardScreen.tsx` via legacy `ng_custom_groups` storage, while simultaneously using `useRegistration.ts` to assign `group_id` locally and to Supabase. This mismatch causes locally created groups to vanish when offline, and "ghost" groups to prevent syncing.

## Design Decisions & Rationale

We will completely eradicate the `ng_custom_groups` and `@Sk8lytz_custom_groups` independent storage arrays. `DashboardScreen` must maintain a Single Source of Truth (SSOT). By dynamically reducing `registeredDevices` from `useRegistration` into the `customGroups` UI layout, the offline queues and cloud sync flows will instantly apply to the dashboard effortlessly, with zero chance of un-tracked groupings.

> [!WARNING]
> This requires surgically mapping legacy `addGroup` / `deleteGroup` commands inside `DashboardScreen.tsx` to iterate over local devices and update them individually via `saveRegisteredDevice`.

## Proposed Changes

### Core Storage

#### [DELETE] `ng_custom_groups` / `@Sk8lytz_custom_groups` AsyncStorage References

- Remove all `AsyncStorage.getItem('ng_custom_groups')` from AutoConnect logic in `DashboardScreen.tsx`.
- AutoConnect will instead map across `registeredDevices`.

### UI Component Updates

#### [MODIFY] `src/screens/DashboardScreen.tsx`

- Replace `const [customGroups, setCustomGroups] = useState<CustomGroup[]>([])` derivation logic.
- Introduce `useEffect` that listens to `registeredDevices` and dynamically packs them into `CustomGroup[]` formatting.
- Refactor `saveGroup` to filter target `registeredDevices` and `await saveRegisteredDevice({...d, group_id, group_name})`.
- Refactor `handleGroupDelete` to iterate over group devices and rewrite their `group_id` back to "default-fleet".
- Fix "Gear Icon" assignment (`selectedDeviceForSettings`) to use `saveRegisteredDevice()`.

#### [MODIFY] `src/hooks/useRegistration.ts`

- Clean up legacy code `migrateLegacyGroups` reading `ng_custom_groups` if they prevent clean loads.
- Strip any unnecessary local fetches to `ng_custom_groups` inside `useRegistration.ts`.

## Open Questions

> [!IMPORTANT]
> If a user manually groups devices that have NOT been fully registered yet (e.g. they skipped the setup wizard), this refactor requires that they be pushed into `saveRegisteredDevice`. Does restricting grouping _only_ to registered devices align with the product experience? (Given that we gate telemetry on registration, this seems favorable).

## Verification Plan

### Automated Tests

- Run `tsc --noEmit` to verify type integrity after deleting manual `CustomGroup` states.

### Manual Verification

1. Open App, block internet access.
2. Group two devices together into a new Fleet.
3. Validate Fleet appears on Dashboard immediately without ghosting.
4. Turn on internet, validate Supabase `registered_groups` tables correctly receive the new Upserts without 409 constraints.
