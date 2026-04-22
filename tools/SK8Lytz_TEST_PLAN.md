# SK8Lytz — Exhaustive Test Plan

<!-- Last Run: YYYY-MM-DD | Runner: [name] | Build: [APK hash / expo start] -->
<!-- Run this plan after EVERY significant code change. Update ✅/❌ then commit. -->

---

## How to Run

**Browser (Expo Web)**

```
npx expo start --web
# Open http://localhost:8081 in Chrome/Firefox
# Resize to 390px width (iPhone sim) and 412px (Pixel 7 sim) for responsive checks
```

**Device (APK)**

```
/build-apk → /install-apk
# Run on physical Pixel 7 (primary Android target)
```

**Hardware Admin Hub**

- **Trigger**: Tap the SK8Lytz logo in the dashboard header exactly 10 times.
- **Passcode**: `0000`

**Browser Subagent Boot Protocol**
To properly execute this plan autonomously, you must boot the `browser_subagent` using the `default_api:browser_subagent` tool with the following `Task` prompt:

> "You are the SK8Lytz QA Subagent. Connect to the local Expo web interface at <http://localhost:8081>. Read the `tools/SK8Lytz_TEST_PLAN.md` file. Execute every applicable web-based test step under Section 1 (Auth), Section 3 (DockedController UI), Section 8 (Account Modal), and Section 9 (Responsive UI). Take a screenshot of every step. If any step fails, you must return a strict markdown list of the failures, detailing the exact step number, what you saw, and why it failed. If a step passes, take no action other than proceeding. Do NOT stop on the first failure. Finish the entire web sweep before returning."

When the subagent returns, the parent agent MUST read the report. For every `[❌] Fail` result in the report, the parent agent MUST immediately format it into a `fix/...` branch slug and add it to `tools/SK8Lytz_Bucket_List.md` under the CRITICAL section.

---

## Test Status Legend

- `[ ]` Not yet run
- `[✅]` Pass
- `[❌]` Fail — add note with what went wrong
- `[⚠️]` Partial / degraded
- `[N/A]` Not applicable to current build

---

## 1. Auth Flow

### 1.1 Login

| #     | Step                                  | Expected                                                    |
| ----- | ------------------------------------- | ----------------------------------------------------------- |
| 1.1.1 | Open app — not logged in              | AuthScreen renders with SK8Lytz logo, email/password fields |
| 1.1.2 | Type invalid email format, tap Log In | Alert: "Missing Fields" or validation error                 |
| 1.1.3 | Enter correct email + wrong password  | Alert: "Sign In Failed" from Supabase                       |
| 1.1.4 | Enter correct credentials, tap Log In | Navigates to Dashboard                                      |
| 1.1.5 | Theme toggle button visible top-right | Tapping switches dark/light mode                            |
| 1.1.6 | Help button (?) top-right             | Tapping shows support Alert                                 |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

### 1.2 Sign Up

| #     | Step                                                   | Expected                                               |
| ----- | ------------------------------------------------------ | ------------------------------------------------------ |
| 1.2.1 | Tap "Sign Up" link                                     | Form adds Username field, button says "Create Account" |
| 1.2.2 | Leave username blank, tap Create Account               | Alert: "Missing Field"                                 |
| 1.2.3 | Enter short/weak password                              | Password strength bar shows (red → green as you type)  |
| 1.2.4 | Enter a known-breached password (e.g., "Password123!") | Alert: "Compromised Password" with breach count        |
| 1.2.5 | Fill all valid fields, tap Create Account              | Alert: "Account Created — check email"                 |
| 1.2.6 | HIBP attribution text visible at bottom                | "Passwords are checked against HaveIBeenPwned..."      |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

### 1.3 Forgot Password

| #     | Step                        | Expected                    |
| ----- | --------------------------- | --------------------------- |
| 1.3.1 | Tap "Forgot Password?"      | Mode switches to reset form |
| 1.3.2 | Submit with blank email     | Alert: "Email Required"     |
| 1.3.3 | Submit with valid email     | Alert: "Email Sent"         |
| 1.3.4 | Tap "Log In" link to return | Returns to login form       |

**Results:** `[ ][ ][ ][ ]`

---

### 1.4 Offline Mode

| #     | Step                                             | Expected                                          |
| ----- | ------------------------------------------------ | ------------------------------------------------- |
| 1.4.1 | Tap "Continue Offline"                           | Navigates to Dashboard without auth               |
| 1.4.2 | Offline mode: Crew tab disabled/limited          | Crew features show login prompt or disabled state |
| 1.4.3 | Offline mode: Account modal shows sign-in prompt | Not full profile options                          |

**Results:** `[ ][ ][ ]`

---

## 2. Dashboard & BLE Scanner

### 2.1 Initial Load

| #     | Step                         | Expected                                                   |
| ----- | ---------------------------- | ---------------------------------------------------------- |
| 2.1.1 | Dashboard loads after login  | DockedController visible at bottom, no crash               |
| 2.1.2 | Status bar: "Tap ⚡ to scan" | Visible in connected device area                           |
| 2.1.3 | No hardcoded pixel overflow  | Layout fills screen edge-to-edge without horizontal scroll |

**Results:** `[ ][ ][ ]`

---

### 2.2 BLE Scan

| #     | Step                                     | Expected                                                       |
| ----- | ---------------------------------------- | -------------------------------------------------------------- |
| 2.2.1 | Tap scan button (⚡)                     | Scan animation starts, nearby BLE devices appear in list       |
| 2.2.2 | Device list shows RSSI, name, type badge | Cards render correctly                                         |
| 2.2.3 | Tap a device card                        | Connect attempt, device card shows "Connecting..."             |
| 2.2.4 | Successful connect                       | Card turns active color, status bar says "Connected to [name]" |
| 2.2.5 | Tap connected device → disconnect        | Device removes from active list                                |
| 2.2.6 | Scan with no devices nearby              | Empty state shows gracefully                                   |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

## 3. DockedController — Tab Navigation

| #   | Step                                                               | Expected                             |
| --- | ------------------------------------------------------------------ | ------------------------------------ |
| 3.1 | All tabs visible: Fixed / Dynamic / Music / Crew / Sparkle / HaloZ | Correct icons, correct labels        |
| 3.2 | Tap each tab                                                       | Content panel switches without crash |
| 3.3 | Panel animates open on first tap                                   | Smooth slide-up animation            |
| 3.4 | Tap active tab again                                               | Panel collapses                      |
| 3.5 | Close button (×) on panel                                          | Collapses panel                      |
| 3.6 | Panel height doesn't overflow screen on 390px viewport             | No clipping                          |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

## 4. Fixed Mode

| #   | Step                                        | Expected                                            |
| --- | ------------------------------------------- | --------------------------------------------------- |
| 4.1 | Select a solid color from color picker      | LED visualizer updates immediately, BLE write fires |
| 4.2 | Drag RGB sliders                            | Color preview and visualizer update live            |
| 4.3 | Hex input: type valid hex → Enter           | Color applied                                       |
| 4.4 | Hex input: type invalid hex                 | Input rejects or shows error                        |
| 4.5 | Select each of the 10 fixed patterns        | Visualizer shows the correct pattern                |
| 4.6 | Brightness slider — min                     | Visualizer dims, BLE write fires 0%                 |
| 4.7 | Brightness slider — max                     | Full brightness, write fires 100%                   |
| 4.8 | All 10 patterns scroll/select without crash | Pattern wheel works on both platforms               |

**Results:** `[ ][ ][ ][ ][ ][ ][ ][ ]`

---

## 5. Dynamic Mode (RBM Patterns)

| #   | Step                                     | Expected                                             |
| --- | ---------------------------------------- | ---------------------------------------------------- |
| 5.1 | Dynamic tab opens with pattern list grid | All 210 patterns visible in grid                     |
| 5.2 | Tap a pattern                            | Visualizer updates, BLE write fires                  |
| 5.3 | Speed slider                             | Speed changes reflected in visualizer animation rate |
| 5.4 | Brightness slider                        | Dims/brightens visualizer                            |
| 5.5 | Pattern search (if implemented)          | Filters list correctly                               |
| 5.6 | Scroll to bottom of pattern list         | No layout overflow                                   |
| 5.7 | Long-press on pattern                    | Context menu or save-to-favorite (if implemented)    |

**Results:** `[ ][ ][ ][ ][ ][ ][ ]`

---

## 6. Music Mode

| #   | Step                         | Expected                                   |
| --- | ---------------------------- | ------------------------------------------ |
| 6.1 | Music tab opens              | Mic visualizer renders, no crash           |
| 6.2 | Tap "Enable Mic"             | Permission prompt shows                    |
| 6.3 | Grant mic permission         | Visualizer becomes reactive to audio input |
| 6.4 | Make sound near device       | Visualizer bars respond                    |
| 6.5 | Deny mic permission          | Graceful fallback message shown            |
| 6.6 | BPM display (when connected) | Shows current detected BPM                 |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

## 7. Crew Tab

### 7.1 Live Session — Create

| #     | Step                              | Expected                                    |
| ----- | --------------------------------- | ------------------------------------------- |
| 7.1.1 | Tap Crew tab                      | Discover / Schedule / Join options visible  |
| 7.1.2 | Tap "Start Now" / Create          | Session name input appears                  |
| 7.1.3 | Enter session name, tap Start     | Session created, invite code shown          |
| 7.1.4 | Crew invite share sheet           | Tapping Share opens OS share or copies code |
| 7.1.5 | Session is listed in Discover tab | Visible in "LIVE NOW" section               |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 7.2 Live Session — Join

| #     | Step                                 | Expected                          |
| ----- | ------------------------------------ | --------------------------------- |
| 7.2.1 | Tap "Join with Code"                 | Code input field appears          |
| 7.2.2 | Enter valid code                     | Joins session, session card shows |
| 7.2.3 | Enter invalid/expired code           | Error message shown               |
| 7.2.4 | Join public session from discover    | One-tap join, no code needed      |
| 7.2.5 | Joined session shows leader, members | Member list updates in real time  |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 7.3 Schedule a Session

| #     | Step                         | Expected                                                             |
| ----- | ---------------------------- | -------------------------------------------------------------------- |
| 7.3.1 | Tap "Schedule"               | Schedule form opens                                                  |
| 7.3.2 | Permanent crew chips visible | Each saved crew chip is tappable                                     |
| 7.3.3 | Select a crew chip           | Crew highlighted, session linked to that crew                        |
| 7.3.4 | Select "New" chip            | Session name input appears                                           |
| 7.3.5 | Pick date via calendar       | Date updates in picker button                                        |
| 7.3.6 | Pick time via clock          | Time updates                                                         |
| 7.3.7 | Tap location detect          | Uses GPS, shows address chip                                         |
| 7.3.8 | Tap "Schedule & Notify Crew" | Session scheduled, push notification sent, 15-min reminder scheduled |

**Results:** `[ ][ ][ ][ ][ ][ ][ ][ ]`

---

### 7.4 Discover Feed

| #     | Step                            | Expected                                         |
| ----- | ------------------------------- | ------------------------------------------------ |
| 7.4.1 | Discover tab — LIVE NOW section | Shows active sessions sorted by proximity        |
| 7.4.2 | PUBLIC CREWS section            | Shows discoverable permanent crews               |
| 7.4.3 | Tap a live session → join       | Joins without code                               |
| 7.4.4 | No sessions → empty state       | "No live sessions near you" graceful empty state |

**Results:** `[ ][ ][ ][ ]`

---

## 8. Account Modal

### 8.1 Profile Tab

| #     | Step                                  | Expected                                             |
| ----- | ------------------------------------- | ---------------------------------------------------- |
| 8.1.1 | Tap user avatar / account button      | AccountModal opens on Profile tab                    |
| 8.1.2 | Display name field pre-filled         | Shows current name                                   |
| 8.1.3 | Change display name → Save            | Success Alert, profile updates in DB                 |
| 8.1.4 | Tap avatar circle                     | Image picker opens                                   |
| 8.1.5 | Pick a photo                          | Optimistic preview shows immediately                 |
| 8.1.6 | Upload completes                      | Photo persists in Supabase Storage, avatar_url saved |
| 8.1.7 | Reopen modal                          | Avatar photo still shows (not just color initials)   |
| 8.1.8 | Color swatches                        | Tap changes avatar background color                  |
| 8.1.9 | Stats row: Crews / Sessions / Devices | Show correct counts                                  |

**Results:** `[ ][ ][ ][ ][ ][ ][ ][ ][ ]`

---

### 8.2 Security Tab

| #     | Step                        | Expected                           |
| ----- | --------------------------- | ---------------------------------- |
| 8.2.1 | Enter mismatched passwords  | Error: "Passwords do not match"    |
| 8.2.2 | Enter password < 8 chars    | Error: "min 8 characters"          |
| 8.2.3 | Valid new password → Update | Success message                    |
| 8.2.4 | Invalid new email → Send    | Error: "valid email"               |
| 8.2.5 | Valid new email → Send      | Success: "Confirmation sent to..." |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 8.3 Crews Tab

| #     | Step                              | Expected                          |
| ----- | --------------------------------- | --------------------------------- |
| 8.3.1 | Shows list of permanent crews     | Owner crown badge, member icon    |
| 8.3.2 | Create Crew → enter name → Create | Appears in list                   |
| 8.3.3 | Join by Code → enter code → Join  | Crew appears in list              |
| 8.3.4 | Invalid join code                 | Error: "Failed to join crew"      |
| 8.3.5 | Owner: tap Delete                 | Confirmation Alert → removes crew |
| 8.3.6 | Member: tap Leave                 | Confirmation Alert → leaves crew  |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

### 8.4 Devices Tab

| #     | Step                         | Expected                                      |
| ----- | ---------------------------- | --------------------------------------------- |
| 8.4.1 | Lists registered BLE devices | Name, type, MAC suffix, paired date           |
| 8.4.2 | Tap pencil (rename)          | TextInput appears inline                      |
| 8.4.3 | Submit rename                | Device name updates                           |
| 8.4.4 | Tap trash (forget)           | Confirmation Alert → removes from list and DB |
| 8.4.5 | No devices → empty state     | "No Registered Devices" + hint text           |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 8.5 Settings Tab

| #     | Step                         | Expected                                              |
| ----- | ---------------------------- | ----------------------------------------------------- |
| 8.5.1 | Push notification toggles    | Toggling saves to AsyncStorage                        |
| 8.5.2 | Dark/Light theme toggle      | Switches theme app-wide                               |
| 8.5.3 | Sign Out button              | Confirmation Alert → signs out, returns to AuthScreen |
| 8.5.4 | Delete Account (Danger Zone) | Double-confirm Alert → submits request                |

**Results:** `[ ][ ][ ][ ]`

---

## 9. Responsive UI Checks

Run at **390px width (iPhone 14)** and **412px width (Pixel 7)**:

| #    | Element                | Check                                                            |
| ---- | ---------------------- | ---------------------------------------------------------------- |
| 9.1  | AuthScreen             | No clipped fields, keyboard avoids inputs                        |
| 9.2  | DockedController panel | Doesn't overflow or clip                                         |
| 9.3  | Crew Modal             | Discover feed scrolls, no horizontal overflow                    |
| 9.4  | Account Modal          | All 5 tabs fit in tab bar                                        |
| 9.5  | Color picker           | Full width, no overflow                                          |
| 9.6  | Schedule form          | Date/time pickers visible, location button reachable             |
| 9.7  | Auth top buttons       | Not behind notch / status bar                                    |
| 9.8  | Bottom sheet padding   | Home indicator not covered on iPhone                             |
| 9.9  | Soft-Grey Palette      | Verify contrast in Light Mode (Background #F0F0F0, Text #2A2A2A) |
| 9.10 | Brand Orange           | Highlights/Toggles use #FF8C00 with visible focus states         |

**Results:** `[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]`

---

## 10. Navigation & Stability

| #    | Step                                     | Expected                                    |
| ---- | ---------------------------------------- | ------------------------------------------- |
| 10.1 | Open/close Account Modal 5× fast         | No crash, no stuck state                    |
| 10.2 | Open/close Crew Modal 5× fast            | No crash                                    |
| 10.3 | Switch all DockedController tabs rapidly | No crash, no blank panels                   |
| 10.4 | Background app → foreground              | App resumes cleanly, session still active   |
| 10.5 | Kill app → reopen                        | Returns to Dashboard if session still valid |
| 10.6 | No TypeScript errors in build            | `tsc --noEmit` exits clean                  |
| 10.7 | No red JS errors in console              | Check Expo logs during flow                 |

**Results:** `[ ][ ][ ][ ][ ][ ][ ]`

---

## 11. Notifications

| #    | Step                                         | Expected                                  |
| ---- | -------------------------------------------- | ----------------------------------------- |
| 11.1 | Create a scheduled session 2 mins away       | 15-min reminder notification registered   |
| 11.2 | Check device notification tray               | "Heads up!" reminder notification visible |
| 11.3 | Tap notification                             | App opens (deep link, future)             |
| 11.4 | Session Live Alert fires when session starts | Notification fires at session start time  |

**Results:** `[ ][ ][ ][ ]`

---

## 12. Dynamic Product Identification (FTUE)

| #    | Step                           | Expected                                         |
| ---- | ------------------------------ | ------------------------------------------------ |
| 12.1 | Launch "Add New Skates" wizard | Proximity scan starts automatically              |
| 12.2 | Bring SOULZ device near        | Auto-detected as "SOULZ" (43 pts / 1 seg)        |
| 12.3 | Bring HALOZ device near        | Auto-detected as "HALOZ" (16 pts / 1 seg)        |
| 12.4 | Tap "Identity Blink"           | Hardware flashes white to confirm physical match |
| 12.5 | Finish Registration            | Device synced to Supabase `registered_devices`   |

**Results:** `[ ][ ][ ][ ][ ]`

---

## 13. Hardware Admin Tools (The Command Center)

| #    | Step                          | Expected                                                    |
| ---- | ----------------------------- | ----------------------------------------------------------- |
| 13.1 | 10-tap SK8Lytz logo in Header | Modal opens; requests passcode                              |
| 13.2 | Enter passcode `0000`         | Admin Tools Hub unlocks                                     |
| 13.3 | TIMELINE Tab                  | Real-time BLE logs visible; scrolling is performant         |
| 13.4 | DEVICE Tab                    | Shows hardware EEPROM dump (IC Type, sorting, points)       |
| 13.5 | LAB: DIY Payload              | Sending 0x59 manually targets the active device             |
| 13.6 | LAB: Pattern Builder          | Modifying pins updates visualizer and hardware in real-time |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

## 14. Telemetry & Cloud Sync

| #    | Step                                             | Expected                                            |
| ---- | ------------------------------------------------ | --------------------------------------------------- |
| 14.1 | Trigger non-fatal error (e.g., failed BLE write) | Local log buffer updates in Admin Timeline          |
| 14.2 | Background app → wait 60s                        | Background sync fires (if enabled)                  |
| 14.3 | Check Admin Stats                                | "Logs Synced" count increments                      |
| 14.4 | Verify Supabase `telemetry_errors`               | Error data matches local trace (stack, device info) |

**Results:** `[ ][ ][ ][ ]`

---

## 15. Hardware Reliability (Pro Protocols)

| #    | Step                             | Expected                                                  |
| ---- | -------------------------------- | --------------------------------------------------------- |
| 15.1 | Select Symphony Effect #31 (Pro) | 0x51 Variable-length payload fires; no device lockup      |
| 15.2 | Rapidly switch effects           | No BLE buffer overflow or MTU errors                      |
| 15.3 | RF Remote Pairing                | Tap "Pair Remote" in Settings → Press RF button → Success |
| 15.4 | RF Auth Mode: Block All          | Hardware ignores RF remote commands                       |

**Results:** `[ ][ ][ ][ ]`

---

## 16. FSM State Machine (refactor/state-machine-standard)

> Tests for the `bleState` and `viewState` finite state machines introduced in the state-machine-standard refactor.
> These tests verify deterministic transitions and elimination of illegal states.

### 16.1 Dashboard `viewState` Router

| #      | Step                                                             | Expected                                                              |
| ------ | ---------------------------------------------------------------- | --------------------------------------------------------------------- |
| 16.1.1 | First launch — no registered devices                             | `LOADING_REGS` spinner shown briefly, then transitions to `SETUP_WIZARD` |
| 16.1.2 | First launch — registered devices exist                          | `LOADING_REGS` spinner shown briefly, then transitions to `DASHBOARD` |
| 16.1.3 | Tap "SET UP YOUR SKATES" on Groups empty state                   | View transitions to `SETUP_WIZARD` deterministically                   |
| 16.1.4 | Tap "ADD DEVICE" in Registered Devices header                    | View transitions to `SETUP_WIZARD` deterministically                   |
| 16.1.5 | Tap "START SETUP" in empty registered devices state              | View transitions to `SETUP_WIZARD` deterministically                   |
| 16.1.6 | Complete setup wizard successfully                               | View transitions back to `DASHBOARD` via `onSetupComplete`             |

**Results:** `[ ][ ][ ][ ][ ][ ]`

---

### 16.2 `bleState` FSM Guards

| #      | Step                                                     | Expected                                                                    |
| ------ | -------------------------------------------------------- | --------------------------------------------------------------------------- |
| 16.2.1 | While `bleState === 'DISCONNECTING'`, press hardware back | Back press is intercepted — no double-disconnect fired                      |
| 16.2.2 | While `bleState === 'DISCONNECTING'`, swipe-to-back       | Swipe ignored — `handleDisconnect` not re-invoked                           |
| 16.2.3 | While `bleState === 'DISCONNECTING'`                      | Teardown overlay (spinner + "Disconnecting...") renders over DockedController |
| 16.2.4 | While `bleState === 'SCANNING'`, tap Scan button          | Scan button is a no-op — no duplicate scan initiated                        |
| 16.2.5 | After scan completes, `bleState` returns to `IDLE`        | Scan animation stops, device list updates                                   |
| 16.2.6 | While `bleState === 'DISCONNECTING'`, DockedController writes | Write is short-circuited — no BLE payload dispatched to hardware         |
| 16.2.7 | While `bleState === 'IDLE'`, DockedController writes      | Write is short-circuited — device is disconnected, no crash                 |
| 16.2.8 | After connect, `bleState === 'CONNECTED'`                 | DockedController writes fire normally                                        |

**Results:** `[ ][ ][ ][ ][ ][ ][ ][ ]`

---

### 16.3 AdminTools Modal FSM Display

| #      | Step                           | Expected                                      |
| ------ | ------------------------------ | --------------------------------------------- |
| 16.3.1 | Open Admin Tools while scanning | `bleState` shows `SCANNING` in the modal UI  |
| 16.3.2 | Open Admin Tools while connected | `bleState` shows `CONNECTED` in the modal UI |
| 16.3.3 | Open Admin Tools while idle     | `bleState` shows `IDLE` in the modal UI      |

**Results:** `[ ][ ][ ]`

---


<!-- Fill in after each release build. Keep last 5 runs. -->

| Date       | APK Build | Auth  | Dashboard | Crew  | Account | Responsive | Result    |
| ---------- | --------- | ----- | --------- | ----- | ------- | ---------- | --------- |
| YYYY-MM-DD | hash      | ✅/❌ | ✅/❌     | ✅/❌ | ✅/❌   | ✅/❌      | PASS/FAIL |

---

## Known Issues / Exceptions

<!-- Document known failures that are deferred, not bugs -->

- Offline mode: Crew tab partially disabled — intentional
- Delete Account: queues a support request, not instant deletion — intentional
- BLE scan: only works on physical device, not web/simulator — expected

---

## 17. BLE Group Stress Test (`fix/hw-stress-test-validation`)

> [!IMPORTANT]
> **Requires:** 2+ physical Zengge LED controllers, Android device with BLE. This section validates
> the entire gate semaphore + AutoRecovery + partial write pipeline end-to-end.

### Prerequisites

- [ ] 2 SK8Lytz devices (HALOZ or SOULZ) powered on and in range
- [ ] Android device with BLE enabled
- [ ] Fresh APK installed (`/build-apk` → `/install-apk`)
- [ ] Admin Tools accessible (10-tap logo + passcode `0000`)
- [ ] Admin Timeline tab open to observe real-time telemetry

### 17.1 Group Connection Sequence

| #      | Step                                           | Expected                                                              | Telemetry Event                    |
| ------ | ---------------------------------------------- | --------------------------------------------------------------------- | ---------------------------------- |
| 17.1.1 | Tap ⚡ scan — discover 2 devices               | Both devices appear in scan list                                      | `SCAN_STARTED`, `DEVICE_DISCOVERED` ×2 |
| 17.1.2 | Tap first device to connect                    | `bleGateRef` transitions: `IDLE` → `CONNECTING` → `IDLE`             | `DEVICE_CONNECTED`                 |
| 17.1.3 | Tap second device to connect (group)           | Both devices listed as connected, group formed                        | `DEVICE_CONNECTED`                 |
| 17.1.4 | Send solid RED to both (color picker)          | **Both** devices illuminate RED simultaneously                        | `RAW_PAYLOAD` ×1 (group write)     |
| 17.1.5 | Verify `writeToDevice` return                  | Returns `true` (not `'partial'`) when all devices online              | — (check console)                  |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 17.2 Forced Disconnect → AutoRecovery

| #      | Step                                                    | Expected                                                                 | Telemetry Event                        |
| ------ | ------------------------------------------------------- | ------------------------------------------------------------------------ | -------------------------------------- |
| 17.2.1 | **Power OFF** Device 1 physically (pull battery/switch) | Device 1 drops from connected list within 5-10s                          | `DEVICE_DISCONNECTED`, `AUTO_RECOVERY_STARTED` |
| 17.2.2 | Observe ghosted state                                   | Device 1 ID appears in `ghostedDeviceIds`; UI shows recovery indicator   | `AUTO_RECOVERY_GATE_WAIT` (if gate busy) |
| 17.2.3 | Send GREEN to devices while Device 1 is ghosted         | **Device 2** turns GREEN; Device 1 write **skipped**                      | `RAW_PAYLOAD` with partial write       |
| 17.2.4 | Verify `writeToDevice` return                           | Returns `'partial'` — some devices skipped but healthy ones succeeded    | — (check return value)                 |
| 17.2.5 | Verify `bleGateRef` stays `IDLE` during recovery        | Gate does NOT lock to `RECOVERING` permanently                            | `AUTO_RECOVERY_GATE_WAIT`              |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 17.3 AutoRecovery → Reconnect

| #      | Step                                           | Expected                                                               | Telemetry Event                    |
| ------ | ---------------------------------------------- | ---------------------------------------------------------------------- | ---------------------------------- |
| 17.3.1 | **Power ON** Device 1 again                    | AutoRecovery loop detects device, starts reconnect                     | `AUTO_RECOVERY_SUCCESS`            |
| 17.3.2 | Wait for reconnect (up to 30s with backoff)    | Device 1 reappears in connected list; ghosted ID removed               | `DEVICE_CONNECTED`                 |
| 17.3.3 | Send BLUE to both devices after recovery       | **Both** devices illuminate BLUE — group is fully restored             | `RAW_PAYLOAD` ×1 (group write)     |
| 17.3.4 | Verify `writeToDevice` return post-recovery    | Returns `true` (not `'partial'`) — all devices healthy                 | —                                  |
| 17.3.5 | Verify no duplicate recovery loops             | Only 1 recovery loop per device — no exponential fan-out               | Check for duplicate `AUTO_RECOVERY_STARTED` |

**Results:** `[ ][ ][ ][ ][ ]`

---

### 17.4 Gate Semaphore Safety Under Load

| #      | Step                                                    | Expected                                                              | Telemetry Event                    |
| ------ | ------------------------------------------------------- | --------------------------------------------------------------------- | ---------------------------------- |
| 17.4.1 | While recovering, tap ⚡ scan                           | Scan **rejected** — gate is not IDLE                                   | `SCAN_STARTED` not fired           |
| 17.4.2 | Rapidly send 10 color commands in 2 seconds             | All 10 commands queued via `writeMutex` — no GATT collision            | 10× `RAW_PAYLOAD` in sequence      |
| 17.4.3 | While connected, disconnect all → immediately reconnect | Disconnect completes fully before connect starts                       | `bleGateRef`: `DISCONNECTING` → `IDLE` → `CONNECTING` |
| 17.4.4 | AdminTools Timeline: verify no `BLE_WRITE_ERROR`        | Zero write errors during normal operation                              | No `BLE_WRITE_ERROR` entries       |

**Results:** `[ ][ ][ ][ ]`

---

### 17.5 MAX_RECOVERY_ATTEMPTS Ejection

| #      | Step                                                    | Expected                                                              |
| ------ | ------------------------------------------------------- | --------------------------------------------------------------------- |
| 17.5.1 | Power OFF Device 1, wait for 8 recovery attempts        | After 8 failed attempts, device ejected from connected list           |
| 17.5.2 | Verify UI cleans up                                     | No permanent "ghosted" indicator stuck — device fully removed         |
| 17.5.3 | Verify remaining device still works                     | Device 2 continues receiving commands normally                        |

**Results:** `[ ][ ][ ]`

---

### 17.6 Rapid Group Cycling (Chaos Test)

| #      | Step                                                             | Expected                                      |
| ------ | ---------------------------------------------------------------- | --------------------------------------------- |
| 17.6.1 | Connect 2 devices → disconnect all → connect both → repeat ×5   | No crash, no stuck gate, all connects succeed |
| 17.6.2 | Connect 2 → power off 1 → power on 1 → power off 2 → repeat   | Recovery handles alternating dropouts cleanly |
| 17.6.3 | While recovering, switch DockedController tabs rapidly           | No React error boundary triggered             |
| 17.6.4 | Final state: both devices connected and responding               | Group write returns `true`                    |

**Results:** `[ ][ ][ ][ ]`

---


_Last updated: 2026-04-22 | Maintained by: AG + Andy_

---

## 18. Emergency Pattern Hardware Correctness (`fix/led-count-segments-consistency`)

> [!IMPORTANT]
> **Requires:** Physical HALOZ device connected via BLE. Test 18.1 is the hardware gate for confirming segment mirror behavior — results must be recorded in `ZENGGE_PROTOCOL_BIBLE.md` before considering this fix complete.

### 18.1 HALOZ — Segment Mirror Verification (Hardware Gate)

| #      | Step                                                                       | Expected                                                                                 |
| ------ | -------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
| 18.1.1 | Connect HALOZ device. Open Admin Tools → LAB → 0x59 builder                | Lab panel opens with numPoints field                                                     |
| 18.1.2 | Set `numPoints = 8`, fill first 4 as RED `FF0000`, last 4 as BLUE `0000FF` | 8-element array set up                                                                   |
| 18.1.3 | Set `transitionType = 0x01` (FREEZE), send to device                       | **Both** physical segments show [R,R,R,R,B,B,B,B] — identical, mirrored pattern          |
| 18.1.4 | If segment 2 shows different colors from segment 1                          | FAIL — auto-mirror assumption is wrong. Halt and re-open `PLAN-led-count-segments-consistency.md` |

**Results:** `[ ][ ][ ][ ]`

---

### 18.2 HALOZ — Emergency Pattern (After 18.1 confirmed)

| #      | Step                                                                 | Expected                                                                   |
| ------ | -------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| 18.2.1 | Connect HALOZ device. Trigger Emergency Pattern button in controller  | Hardware receives exactly **8 elements** via `0x59`                        |
| 18.2.2 | Observe physical hardware                                            | Both left + right wheel rings show **identical** hazard pattern            |
| 18.2.3 | Verify pattern content: first 2 LEDs red, mid yellow/off, last white | [R,R,Y,_,Y,_,W,W] pattern visible on the 8-LED canvas                     |
| 18.2.4 | Adjust brightness slider to 50%                                      | All colors scale proportionally, pattern shape unchanged                   |

**Results:** `[ ][ ][ ][ ]`

---

### 18.3 SOULZ — Emergency Pattern (Scaled to ledPoints)

| #      | Step                                                                                     | Expected                                                    |
| ------ | ---------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| 18.3.1 | Connect SOULZ device (default `ledPoints = 43`). Trigger Emergency Pattern               | Pattern fills all 43 LEDs proportionally in 3 zones         |
| 18.3.2 | In HW Settings, reduce `ledPoints` to 24 (simulating cut strip). Trigger pattern again   | Pattern fills exactly 24 LEDs — rear RED, mid YELLOW/OFF, front WHITE |
| 18.3.3 | Reduce `ledPoints` to 9 (minimum viable). Trigger pattern                                | Pattern still renders — zone minimum protection (Math.max(1, floor)) prevents zero-length zones |
| 18.3.4 | Restore `ledPoints` to 43                                                                | Pattern returns to full 43-LED fill                         |

**Results:** `[ ][ ][ ][ ]`

