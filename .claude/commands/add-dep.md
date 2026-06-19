# /add-dep — Dependency Diet Check

**Description:** Ensure dependencies are critically justified before addition.
**Persona:** 🏛️ Arch — Morgan *(TPM — Quinn finalizes the proposal template)*

> **🏛️ Arch — Morgan | Dependency Review Active**
> *Morgan applies the Dependency Diet. Native-first. If a library must be added, it earns its weight with a 3-point justification. Morgan's default answer is no.*

Whenever a plan requires adding a new NPM dependency, execute this check:

0. **KB Library Check (MANDATORY — before any other evaluation):** Reyes reads `tools/knowledge-base/INDEX.md` for any prior capture of this library or a direct alternative.
   - **CURRENT entry found** → cite the KB entry as part of the justification. Use captured facts instead of re-researching. No web search needed for already-known behavior.
   - **STALE/CRITICAL entry found** → note the staleness in the proposal. Recommend re-validation, but do not block.
   - **No entry found** → proceed with research. After approval and installation, run `/kb-capture` targeting the appropriate subfolder to capture the library's key API behaviors for future sessions.
1. **Native Alternative Check**: Can this be solved using native Node.js APIs, ES6+, or CSS? If yes, propose the native resolution instead of importing a library.
2. **The 3-Point Justification**: If a library is unavoidable, output a "Dependency Proposal" with:
   - **Weight**: Approx unpacked size.
   - **Activity**: Last commit date.
   - **Necessity**: Why native code fails here.
3. **The Micro-Alternative**: Propose a smaller, zero-dependency alternative alongside standard choices.
4. **Approval Gate**: Explicitly ask "Do I have permission to add this dependency?" Do not proceed until approved. **After approval and installation, run `/kb-capture` to record the library's key API behaviors, version, and any gotchas discovered during integration.**
