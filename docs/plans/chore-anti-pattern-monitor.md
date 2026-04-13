# Anti-Pattern Monitor & Proactive Tech Debt Logging

This plan implements a formal protocol for identifying, logging, and removing "bad practices" artifacts (monoliths, anti-patterns, etc.) as requested by the user.

## Design Decisions & Rationale
- **Proactive Detection**: Integrating automated size/complexity checks into the `tech-debt-janitor` workflow ensures regular visibility into architectural drift.
- **Immediate Logging**: Updating `clean-code.md` to mandate bucket list injection ensures that discovered debt is never forgotten, even if it cannot be addressed immediately (due to the [Surgical Strike Protocol](RULE[surgical-edits.md])).
- **Baseline Sweep**: A manual initial sweep identifies existing "monolithic" components that have grown beyond safe maintainability limits.

## User Review Required

> [!IMPORTANT]
> The threshold for a "monolith" is currently set to **> 500 lines** or **> 30KB** for React components. This triggers a mandatory extraction requirement.

## Proposed Changes

### [Agent Rules](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/)

#### [MODIFY] [tech-debt-janitor.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/tech-debt-janitor.md)
- Add Step 4: **Architectural Smell Scan**.
  - Scan for files exceeding 30KB or 500 lines.
  - Scan for "God Objects" (files with $> 10$ hooks or imports).
  - List these findings in the report and pipe them to the Bucket List.

#### [MODIFY] [clean-code.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/clean-code.md)
- Amend Section 4 (**Self-Correction Mandate**) to include "Discovery Logging":
  - If a file violates modularity standards (a Monolith) and cannot be fixed under the "Boy Scout" rule (too high risk/large), it **MUST** be added to the `tools/SK8Lytz_Bucket_List.md` as a `chore/refactor` task immediately.

---

### [Bucket List](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_Bucket_List.md)

#### [MODIFY] [SK8Lytz_Bucket_List.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_Bucket_List.md)
- Inject discovered baseline monoliths under `### 🟠 HIGH: Engineering Excellence`:
  - `chore/refactor-docked-controller` (156KB monolith)
  - `chore/refactor-dashboard-monolith` (102KB monolith)
  - `chore/refactor-account-modal` (63KB monolith)
  - `chore/refactor-diagnostic-lab` (61KB monolith)
  - `chore/refactor-admin-tools` (55KB monolith)

## Open Questions

1. **Threshold Tuning**: Is 30KB/500 lines the correct threshold for the "Monolith Warning"?
2. **Prioritization**: Should these new "monolith" tasks be placed in `### 🟠 HIGH: Engineering Excellence` or `### 🔴 CRITICAL`? I suggest HIGH unless they are actively causing crashes.

## Verification Plan

### Automated Tests
- Run the updated `tech-debt-janitor` workflow ("clean the house") and verify it detects and logs a known large file.

### Manual Verification
- Verify the Bucket List has been updated with the baseline findings.
