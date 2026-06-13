# Command Center Device Simulator (feat/docker-web-demo)

## Goal
Embed the SK8Lytz Web Demo into the Command Center under a new "App Manager" tab called **SK8Lytz APP**, keeping it permanently running in Docker. We will wrap the app in dynamic device silhouettes (iPhone, Pixel) and capture live console logs directly from the iframe.

## User Review Required

> [!NOTE]
> Review the device silhouette options and the `postMessage` approach for the live console. This ensures we don't pollute your Expo production build with logging hacks while giving you exactly what you need in the Command Center.

## Proposed Changes

---

### 1. Docker Daemonization
**`[NEW] Dockerfile.web`**
- Base: `node:20-alpine` (standard Expo compatibility, avoids Bun CLI edge cases).
- Command: `npx expo start --web --port 8081 --host 0.0.0.0`
- Purpose: Installs dependencies and runs the web server.

**`[MODIFY] docker-compose.yml`**
- Add a new `webdemo` service running on port 8081.
- Volume mount `./src`, `./assets`, `app.config.js`, `babel.config.js`, `metro.config.js`, and `package.json` so hot-reloading works instantly.

---

### 2. Live Console Interception (The App Side)
**`[NEW] src/hooks/dev/useWebDemoConsoleBridge.ts`**
- A development-only hook that runs **only** if `__DEV__` and `Platform.OS === 'web'`.
- Intercepts `console.log`, `console.warn`, and `console.error`.
- Broadcasts the logs to the parent iframe using `window.parent.postMessage({ source: 'sk8lytz-demo', level: 'error', data: [...] }, '*')`.

**`[MODIFY] App.tsx`**
- Import and initialize `useWebDemoConsoleBridge` at the very top level inside a dev-only guard.

---

### 3. Command Center UI (The Dashboard Side)
**`[NEW] tools/command-center/src/components/widgets/DeviceSimulator.tsx`**
- Renders the `<iframe>` pointing to `http://localhost:8081`.
- Maintains state for `deviceType` (iPhone 15, Pixel 8, iPad Mini).
- Applies specific fixed width/height and border-radius/bezel styling based on the selected device to emulate a phone screen precisely.

**`[NEW] tools/command-center/src/components/widgets/ConsoleViewer.tsx`**
- A dark-mode terminal window UI.
- Adds an event listener for `window.addEventListener('message')`.
- Filters for `event.data.source === 'sk8lytz-demo'` and renders the logs with color-coding (red for errors, yellow for warnings).

**`[MODIFY] tools/command-center/src/App.tsx` (or AppManager layout)**
- Add the new "SK8Lytz APP" tab/route under the App Manager section.
- Layout the `DeviceSimulator` and `ConsoleViewer` side-by-side or stacked vertically.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure the hook and UI components pass TSC and Jest without breaking the app.

### Manual Verification
1. `docker-compose build webdemo && docker-compose up -d webdemo`
2. Open the Command Center at `localhost:5997`.
3. Navigate to App Manager -> SK8Lytz APP.
4. Verify the device can switch between iPhone and Pixel dimensions, correctly squishing the UI.
5. Trigger an error in the Expo app (e.g., clicking a button) and verify the red error appears instantly in the live Console Viewer component.
