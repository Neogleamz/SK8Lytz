---
trigger: always_on
---

# Code Discipline & Architecture Standard

Govern how to write, architect, and modify code, combining clean code best practices with safe surgical editing.

1. **Architecture & Modularity**:
   - Never write monolithic files. Separate hardware/business logic into isolated service files or decoupled custom domain hooks (e.g., `useCrewHub`). UI components must strictly focus on rendering.
   - Functions should do exactly one thing.
   - Use Finite State Machines (FSMs) or string unions for UI state. No generic boolean flags (`isLoading`, `isSuccess`).

2. **Modern Syntax & Clean Execution**:
   - Use modern ES6+ syntax and `async/await`.
   - Wrap all async operations in `try/catch`. Never swallow errors.
   - Leave no dead code or unneeded `console.log` statements. Use structured logging.
   - Use highly descriptive variable and function names.

3. **The Boy Scout (Cleanup) Rule**:
   - Fix exactly **one** small piece of technical debt (e.g., typing an `any`, removing dead code, renaming a confusing variable) in every file you touch.
   - *Exemption*: Suspend this rule for isolated tests or complex surgical strikes where scope drift is dangerous.

4. **Surgical Strike Protocol (Anti-Collision)**:
   - Make micro-edits only via native tools targeting specific 3-10 line chunks. Never overwrite files from memory.
   - Immediately verify your edits via `git diff HEAD` to ensure no related logic was accidentally deleted.
   - If a file is too complex, state we must extract the component first before editing. Do not attempt Boy Scout cleanups on massive fragile monoliths.

5. **Self-Review**: Look at your code with a security and performance lens before committing.
