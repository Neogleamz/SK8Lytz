---
description: Ensure dependencies are critically justified before addition
---

Whenever a plan requires adding a new NPM dependency, execute this check:

1. **Native Alternative Check**: Can this be solved using native Node.js APIs, ES6+, or CSS? If yes, propose the native resolution instead of importing a library.
2. **The 3-Point Justification**: If a library is unavoidable, output a "Dependency Proposal" with:
   - **Weight**: Approx unpacked size.
   - **Activity**: Last commit date.
   - **Necessity**: Why native code fails here.
3. **The Micro-Alternative**: Propose a smaller, zero-dependency alternative alongside standard choices.
4. **Approval Gate**: Explicitly ask "Do I have permission to add this dependency?" Do not proceed until approved.
