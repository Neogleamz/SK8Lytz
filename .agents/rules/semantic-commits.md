---
trigger: always_on
---

# Semantic Commits Enforcer

All commits must adhere to Conventional Commits: `type(scope): subject`

- **Types**: `feat` (new), `fix` (bug), `docs` (reference), `style` (formatting), `refactor` (restructure), `perf` (speed), `chore` (maintenance).
- **Scope**: Identify the module, e.g., `feat(ble-protocol)`.
- Use `!` for breaking changes: `feat(db)!:`
- Dynamically inject this format into automated commands like `git commit -m`.
