# [FEAT] Zero-Touch Crew Sync

## Goal

Geofence-based 'Hive Mind' synchronization.

## Proposed Changes

- Implement Geofencing around active sessions.
- Auto-prompt 'Join Crew' when within 50m of a session host.
- Synchronize 0x51 effects across the geofence.

## Verification Plan

- Simulate location change.
- Verify auto-sync trigger.
