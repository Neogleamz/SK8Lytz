---
description: Visual Smoke Test Workflow
---

# Visual Smoke Test Workflow -- "/smoke-test"

When invoked via `/smoke-test`, dispatch the headless browser subagent to rapidly verify the structural integrity of the React Native Web representation of the application. The primary goal is to conclusively prove that the application does NOT crash to a white screen or a fatal exception on initial render.

**Location:** Execute from the root directory bounding your current environment (either master or worktree).

### Subagent Instructions
Trigger the `browser_subagent` tool with the following rigorous instructions:
1. Ensure the Expo web server is running. If not, spin it up (`npx expo start --web`).
2. Navigate the browser directly to `http://localhost:8081` (or the active port).
3. **Wait for JS Bundle:** Wait a minimum of 2-3 seconds for React to finish hydrating the DOM. 
4. **Inspect the Mount Root:** Target the `<div id="root">` element. Verify that it contains child nodes and successfully mounted UI components.
5. **Redbox Hunt:** Explicitly scan the DOM for "Uncaught Error", "Check your terminal", or any classic Expo Redbox fatal crash indicators.
6. **Visual Proof:** The subagent will automatically capture a WebP screenshot/video of the browser window as an artifact.
7. Return a summary verifying if the app rendered cleanly or if it white-screened.

If the subagent returns a white screen or error, immediately inform the user, execute `/debug`, and HALT any release/commit processes.
