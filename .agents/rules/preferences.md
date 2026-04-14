---
trigger: always_on
---

# Project Preferences

Core behavioral non-defaults optimized for SK8Lytz:

1. **State**: Use FSM patterns or string unions for UI state. Avoid generic boolean flags (`isLoading`, etc).
2. **UI Safety**: Account for iOS/Android Safe Areas explicitly in layouts.
3. **UI States**: Design full 4-state matrix (Loading, Error, Empty, Success) for all data views.
4. **Cleanliness**: Perform one localized cleanup (boy-scout rule) in every file you modify, unless working on an isolated/surgical edit.
5. **Commits**: Strictly use Conventional Commits (`type(scope): subject`, with `!` for breaking DB/BLE changes).
6. **Modularity**: Domain logic belongs in custom hooks (`useXyz`), not UI files. Use the `/scaffold-hook` workflow when extracting logic.
