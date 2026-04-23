---
trigger: always_on
---

# Project Preferences

Core behavioral non-defaults optimized for SK8Lytz:

1. **State**: Use FSM patterns or string unions for UI state. Avoid generic boolean flags (`isLoading`, etc).
2. **UI Safety**: Account for iOS/Android Safe Areas explicitly in layouts.
3. **UI States**: Design full 4-state matrix (Loading, Error, Empty, Success) for all data views.
4. **Zero-Collateral Damage**: DO NOT perform unsolicited "boy-scout" refactors or cleanups. You must touch ONLY the exact lines of code required for the active sprint. Tech debt must be reported to the Bucket List, never silently fixed.
5. **Commits**: Strictly use Conventional Commits (`type(scope): subject`, with `!` for breaking DB/BLE changes).
6. **Modularity**: Domain logic belongs in custom hooks (`useXyz`), not UI files. Use the `/scaffold-hook` workflow when extracting logic.
