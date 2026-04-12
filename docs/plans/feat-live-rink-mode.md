# [FEAT] Live Rink Mode (ShazamKit)

## Goal
[Live Rink Mode] — ShazamKit/ACRCloud periodic background scanning (45s).

## Proposed Changes
- Integrate `react-native-shazamkit`.
- Implement background timer (45s) for audio fingerprinting.
- Map matched track BPM to hardware `0x73` music mode.

## Verification Plan
- Play music in Lab.
- Verify app identifies song and updates lights.
