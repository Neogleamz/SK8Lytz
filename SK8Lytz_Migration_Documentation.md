# SK8Lytz Migration Documentation

**Date:** March 2026
**Target:** New environment / Antigravity Agent initialization

This document provides an extremely detailed, step-by-step account of the development process for the SK8Lytz mobile app, along with all relevant information needed to seamlessly transfer this project to a new computer or agent environment. 

---

## 1. Project Overview & Environment

- **Project Location (Source):** `C:\Users\Magma\Downloads\SK8Lytz`
- **Antigravity Brain Files Location (Source):** `C:\Users\Magma\.gemini\antigravity\brain`
- **Framework:** React Native (Expo)
- **Primary Languages:** TypeScript / React
- **Key Dependencies:** 
  - `react-native-ble-plx` (Bluetooth Low Energy)
  - `expo-av`, `expo-camera`, `expo-image-manipulator`
  - `@supabase/supabase-js`
- **Build Target:** Android (Local Windows build flow) and Web (Demo mode)

---

## 2. Chronological Development Steps Taken

To bring a new agent up to speed, here is the exact sequence of technical steps and problem-solving actions we have completed:

### A. Core Architecture & Protocol Definition
1. **Bootstrapped Expo app** mapping TypeScript interfaces to specific SK8Lytz hardware setups (HALOZ and SOULZ).
2. **Integrated Zengge/Magic Home V2 BLE Protocol**: We reverse-engineered the hex commands used by the LED controllers. 
   - A detailed Master Reference was compiled describing `0x81` (hardware config), `0x31` (color config), `0x42` (patterns), and `0x73`/`0x74` (mic sync & audio magnitude).
   - This knowledge was hardcoded into `ZenggeProtocol.ts` to execute exact array buffers over BLE.

### B. Controller & Visualizer Development
3. **Built `Sk8lytzController.tsx`**: We developed a robust state manager that maintains a unified map of connected peripheral connections, grouping logic, and `react-native-ble-plx` scanning algorithms.
4. **Created LED Visualizer**: We engineered a `ProductVisualizer` running on React Native components to simulate physical LEDs. It accurately mimics symphony music modes using an `audioMagnitude` math interpolation corresponding to `ModeType 0x27` equations.

### C. Advanced State Handling & Bug Fixes
5. **Resolved Infinite React Recursion**: We debugged severe `Maximum update depth exceeded` errors. The root cause was auto-grouping logic firing prematurely before devices were completely named and scanned.
6. **Fixed Hardware Telemetry & Grouping**: We forced named provisioning (e.g., `"SOULZ Left Skate"`) and ensured proper UI state collapsing upon successful grouping.

### D. UI/UX Refinement
7. **Theming & Aesthetic Optimization**: Designed an ultra-premium aesthetic utilizing dark mode, neon accents, and centralized styling in `src/theme/theme.ts`. We centered branding logos and placed system toggle buttons effectively.
8. **Logging Console**: Upgraded the internal telemetry system, creating a long-press developer console that stores logs (including hardware info, battery levels, selected colors, and pattern names) across reloads using `AsyncStorage`.

### E. Build Pipeline (Local APK)
9. **Escaped WSL Instability**: Moved Android APK compilation natively to the Windows host. We created `build-local-apk.bat` directly in the project root to handle gradle memory settings, environment path syncing, and SDK validations to prevent Java heap space errors encountered in WSL.

---

## 3. Package Contents (Migration Artifact)

This migration package (`SK8Lytz_Migration_Package.zip`) directly contains two critical directories:

1. **`SK8Lytz` Directory**: The entirety of the mobile application source code.
   - Includes standard Expo structure (`src/`, `assets/`, `App.tsx`)
   - Includes `SK8Lytz_App_Master_Reference.txt` (the most vital exhaustive documentation of the Zenngge BLE hooks).
   - Includes custom CLI build scripts (`build-local-apk.bat`).
2. **`brain` Directory**: A copy of the `.gemini\antigravity\brain` folder.
   - Includes all conversational contexts, artifacts, logs, and state files representing everything the AI agent knows and learned about this project.

## 4. How to Initialize the New Environment

1. **Extract** `SK8Lytz_Migration_Package.zip` onto the target machine (e.g., into `Downloads` or `Documents`).
2. **Restore Brain Context**: Copy the `brain` folder's contents into the new machine's Antigravity brain directory (typically `C:\Users\<User>\.gemini\antigravity\brain`).
3. **Restore App Project**: Place the `SK8Lytz` folder in the desired development path and launch your terminal there.
4. **Agent Briefing**: Ask the newly initialized Antigravity agent to read BOTH `SK8Lytz_Migration_Documentation.md` and `SK8Lytz_App_Master_Reference.txt` to seamlessly pick up where we left off.
5. **Install Dependencies**: Run `npm install` inside the `SK8Lytz` directory to rebuild `node_modules`.

---
*End of Migration Protocol.*
