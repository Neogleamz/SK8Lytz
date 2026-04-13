# Refactor Account Devices Display

The **Account Manager > Devices** tab is currently displaying group names instead of individual device names due to an incorrect database property mapping (`customName: d.group_name` instead of `customName: d.custom_name`).

## Proposed Changes

We will modify how the device list maps database properties in both `AccountModal.tsx` and `DashboardScreen.tsx`.

### [MODIFY] AccountModal.tsx

- Update the API `select` query from `device_mac, device_name, product_type, position, group_name, created_at` to include `custom_name` instead of just `group_name`.
- Change the frontend mapping locally to `customName: d.custom_name ?? undefined`.

### [MODIFY] DashboardScreen.tsx

- In the `<AccountModal />` prop parameters, change `customName: d.group_name` to `customName: d.custom_name` to ensure the correct values are passed to the modal initially.

## Verification Plan

1. Open the Account Modal and navigate to the Devices Tab.
2. Verify that devices are listed with their actual individual custom names, rather than all sharing a single `group_name`.
