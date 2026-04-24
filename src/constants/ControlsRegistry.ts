export type ControlRiskLevel = 'normal' | 'warning' | 'danger';
export type ControlType = 'switch' | 'action';

export interface ControlEntry {
  key: string;
  label: string;
  subLabel: string;
  type: ControlType;
  riskLevel: ControlRiskLevel;
  defaultValue?: string | boolean;
  /** For switches that require a safety confirmation alert */
  confirmTitle?: string;
  confirmMsg?: string;
  /** For action buttons */
  actionLabel?: string;
}

export const CONTROLS_REGISTRY: Record<string, ControlEntry[]> = {
  Governance: [
    {
      key: 'global_crew_hub_locked',
      label: 'Global Lock (Crew Hub)',
      subLabel: 'Completely disable the CREWZ HUB for everyone.',
      type: 'switch',
      riskLevel: 'danger',
      defaultValue: false,
      confirmTitle: 'Lock Crew Hub?',
      confirmMsg: 'This disables the entire social network. Proceed?'
    },
    {
      key: 'global_community_hub_locked',
      label: 'Global Lock (Community Hub)',
      subLabel: 'Disable Community Picks and Favorites.',
      type: 'switch',
      riskLevel: 'danger',
      defaultValue: false,
      confirmTitle: 'Lock Community?',
      confirmMsg: 'This disables global community picks. Proceed?'
    },
    {
      key: 'global_maps_locked',
      label: 'Lock Skate Maps',
      subLabel: 'Disable all mapping functionality.',
      type: 'switch',
      riskLevel: 'danger',
      defaultValue: false,
      confirmTitle: 'Lock Skate Maps?',
      confirmMsg: 'This disables location and map features. Proceed?'
    }
  ],
  Hardware: [
    {
      key: 'global_optimistic_ui_enabled',
      label: 'Ghost State (Optimistic UI)',
      subLabel: 'Instantly update the UI before Bluetooth confirmation.',
      type: 'switch',
      riskLevel: 'normal',
      defaultValue: true
    },
    {
      key: 'global_haptics_enabled',
      label: 'Physical Haptics',
      subLabel: 'Vibrate the device on Bluetooth command successes/failures.',
      type: 'switch',
      riskLevel: 'normal',
      defaultValue: true
    }
  ],
  Behavior: [
    {
      key: 'offline_crew_hub_hidden',
      label: 'Hide Crew Hub When Offline',
      subLabel: 'Hide the tab completely if the device drops connection.',
      type: 'switch',
      riskLevel: 'normal',
      defaultValue: false
    },
    {
      key: 'offline_community_hub_hidden',
      label: 'Hide Community Hub When Offline',
      subLabel: 'Remove access to cached Community picks.',
      type: 'switch',
      riskLevel: 'normal',
      defaultValue: false
    },
    {
      key: 'offline_maps_hidden',
      label: 'Hide Maps When Offline',
      subLabel: 'Remove Maps access when offline.',
      type: 'switch',
      riskLevel: 'normal',
      defaultValue: false
    }
  ],
  DangerZone: [
    {
      key: 'global_telemetry_enabled',
      label: 'Telemetry Uploads',
      subLabel: 'Enable or disable crash telemetry ingest.',
      type: 'switch',
      riskLevel: 'normal',
      defaultValue: true
    },
    {
      key: 'required_eula_version',
      label: 'Required EULA Version',
      subLabel: 'Users with lower accepted versions will be gated upon login.',
      type: 'action',
      riskLevel: 'danger',
      defaultValue: '1',
      actionLabel: 'BUMP VERSION',
      confirmTitle: 'Bump EULA Version?',
      confirmMsg: 'Are you sure you want to enforce the next version? All active users must re-accept the terms.'
    }
  ]
};
