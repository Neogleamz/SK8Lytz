# SK8Lytz Master Bucket List

This document tracks all active, pending, and completed tasks, feature requests, and bugs across the SK8Lytz application based on conversation history and rules. Items are prioritized and updated regularly.

## 🔴 High Priority / Current Sprint
- [x] **Location Autocomplete Integration**: Integrate `LocationPicker` with OSM Nominatim into "Schedule a Session" flow.
- [x] **Start Session Parity**: Integrate the exact same Location Autocomplete into "Start a Session" (`renderCreate`) flow.
- [x] **Remove Visibility Toggle**: Remove "Public / Private" toggle from the "Start a Session" panel.
- [x] **Web Build Stability**: Fix white-screen crash on `npm run web` caused by `react-native-maps` by splitting Native/Web map components.
- [ ] **End Session / UI Locking Issue**: Fix issue where a started session locks the user out of Crew Hub. (Only shows 'end' and 'back to skating', needs 'back to crew hub' button).
- [ ] **Live Near You Filtering**: Ensure the "LIVE NEAR YOU" section in Crew Hub accurately shows all public & private sessions for crews the user is a member of, and *all* public sessions within the Discover Radius.
- [ ] **Scheduler Date/Time Buttons**: Fix issue where the date and time selection buttons in the Scheduler do nothing when clicked.

## 🟡 Medium Priority
- [ ] **Web E2E Verification**: Full verification of Map static thumbnails and address autocomplete in the browser environment.
- [ ] **Native Platform Verification**: Test MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] **Crew Hub Layout**: Continue refining dashboard, spacing, and Crew Hub UI components as the platform grows.

## 🟢 Low Priority / Backlog
- [ ] **Automated Testing Suite**: Expand E2E testing for the newly migrated Location flows.
- [ ] **Security & Performance**: Conduct routine security reviews on Supabase queries and optimize React Native render cycles for dashboard gauges. 

---
*Created per `bucketlist.md` rule directive.*
