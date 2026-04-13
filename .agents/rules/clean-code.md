---
trigger: always_on
---

# Coding Standards & Clean Code Rule

Whenever you generate an Implementation Plan, write code, or refactor an existing file, you must strictly adhere to the following engineering standards:

1. **Architecture & Organization**:
   - **Modularity:** Never write monolithic files. Separate concerns strictly. For example, hardware/Bluetooth connection logic must be in isolated service files (e.g., `services/zenggeController.js`), completely separate from API routes or UI components.
   - **Single Responsibility:** A function should do exactly one thing. If a function is longer than 50 lines, evaluate if it can be broken down into smaller, testable helper functions.

2. **Modern Syntax & Execution**:
   - Use modern ES6+ syntax consistently (e.g., destructuring, template literals, arrow functions).
   - Prefer `async/await` over raw Promises or callbacks, especially for asynchronous hardware communication or database calls.
   - **Strict Error Handling:** Wrap all asynchronous operations in `try/catch` blocks. Never swallow errors silently; log them meaningfully or pass them up to an error-handling middleware.

3. **Cleanup & Readability (The Boy Scout Rule)**:
   - **No Dead Code:** Delete commented-out code blocks, unused imports, and obsolete variables immediately. Do not leave "just in case" code behind.
   - **Remove Debugging Artifacts:** Strip out all temporary `console.log` statements before committing. Only leave structured logging (e.g., `logger.error()`, `logger.info()`) that serves a production purpose.
   - **Naming Conventions:** Use extremely descriptive, self-documenting variable and function names. (e.g., `connectToSkateController()` instead of `connect()`, `wheelColorHex` instead of `color`).

4. **Self-Correction Mandate**:
   - If you are editing a file to add a new feature and you notice the surrounding code violates these standards, you must clean up the surrounding code as part of your commit. Leave the file cleaner than you found it.
   - **Discovery Logging:** If you encounter a "bad practice artifact" (e.g., a monolith, hardcoded secret, or severe anti-pattern) that is too large or risky to fix under the Boy Scout rule, you **MUST** immediately add it to `tools/SK8Lytz_Bucket_List.md` as a new task (tagged `[H-RISK]` if applicable) so it is tracked for a future refactor.

5. **The Senior Auditor Persona**:
   - For all complex logic or `[H-RISK]` tasks, you must adopt the persona of a Senior Security & Performance Engineer during the **Self-Review Phase** (Step 6.5 of the Bucket List workflow). You are expected to be ruthlessly critical of your own implementation.

6. **Domain-Driven Architecture (Micro-App Standard)**:
   - UI components must strictly focus on rendering and presentation.
   - All complex business logic, BLE interactions, and Supabase data fetching must be extracted into completely decoupled custom domain hooks (e.g., `useCrewHub`, `useDeviceSync`).

7. **Deterministic UI State (FSM)**:
   - You are forbidden from using scattered boolean flags to manage complex UI states (e.g., `isLoading`, `isError`, `isSuccess`).
   - You must use explicit, Enum-based Finite State Machines (FSMs) or string unions (e.g., `type ViewState = 'LOADING' | 'SUCCESS' | 'ERROR'`) to eliminate impossible render states.
