# Plan: `feat/account-devices-management`

### Design Decisions & Rationale
The Account modal's Devices tab currently shows registered devices but has no group management. We add a "My Device Groups" collapsible section below the device list. Deletions use a two-phase atomic approach: first delete from Supabase (if online), then immediately remove from `AsyncStorage` — both paths are required to ensure the device truly disappears from the UI even offline.

---

## Proposed Changes

### [MODIFY] `src/components/AccountModal.tsx` — Devices Tab
- Add a "My Groups" section header with chevron toggle.
- Render each `ng_custom_groups` entry as a card showing group name + device count.
- Each card has an Edit (pencil) and Delete (trash) icon on the right.

#### Delete Group Flow:
1. Confirm dialog: "Delete [Group Name]? This will remove [N] devices."
2. On confirm:
   - Call `supabase.from('registered_devices').delete().eq('group_name', groupName)` (if online).
   - Call `AsyncStorage.removeItem('ng_device_configs')` — then rewrite the key without the deleted devices.
   - Update `ng_custom_groups` in AsyncStorage.
   - Emit a local event (or context update) to force DashboardScreen to re-render.

#### Edit Group Flow:
- Opens a simple bottom sheet with a text input to rename the group.
- Updates the group name in Supabase (`registered_devices.group_name`) and local `ng_custom_groups`.

---

## Open Questions
- **Q:** When a group is deleted, do we also delete the individual device records from the DB, or just disassociate them? (Recommendation: full delete — a group without devices is useless.)
- **Q:** What happens to the `ng_processed_devices` cache key after a deletion — does it need manual pruning too?

## Verification Plan
1. Register 2 devices in a group.
2. Navigate to Account > Devices > My Groups.
3. Delete the group.
4. Return to Dashboard and confirm both devices are gone from the hardware fleet slab.
5. Toggle airplane mode and repeat — confirm offline deletion also works.
