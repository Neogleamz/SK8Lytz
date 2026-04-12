# Implementation Plan: Bucket List Efficiency Upgrades & Rule Verification

This plan institutionalizes three "Power Upgrades" to our workflow: **Maintenance Sweeps (Batching)**, **Visual Epic Progress Tracking**, and **Sentinel Sync (Auto-Sourcing Bugs)**.

## User Review Required

> [!IMPORTANT]
> **Batching Risk**: Maintenance sweeps compress multiple changes into one PR. While efficient, it increases the footprint of a single commit. I will still perform a "Self-Review & Refactor" phase on the entire batch to ensure cleanliness.

## Proposed Changes

### [Rules & Protocols]
Updating the agentic brain to handle high-velocity operations.

#### [MODIFY] [.agents/rules/bucketlist.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/bucketlist.md)
- Add "Section 6: The Maintenance Sweep Protocol".
- Define `sweep[ID1, ID2, ID3]` trigger.

#### [MODIFY] [.agents/rules/status-update.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/status-update.md)
- Update SITREP format to include **Epic Progress Bars**.

#### [MODIFY] [.agents/rules/tech-debt-janitor.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/tech-debt-janitor.md)
- Add **Sentinel Sync** (Log Triage) to the mandatory health check workflow.

---

### [Tooling]
Automating the feedback loop from production logs.

#### [NEW] [sync_remote_errors.mjs](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/sync_remote_errors.mjs)
- Script to leverage the Supabase MCP server `get_logs` tool.
- Filters for `api` and `postgres` service errors (status >= 400).
- Proposes new `fix/...` tickets for the Bucket List.

---

### [Bucket List]
Integrating visual feedback.

#### [MODIFY] [SK8Lytz_Bucket_List.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_Bucket_List.md)
- Inject initial progress tracking metrics.

## Verification Plan

### Automated Tests
- Run `node tools/sync_remote_errors.mjs` (Simulation/Dry Run).
- Run `status update` command to verify the new SITREP format.

### Manual Verification
- User review of the new rule files to confirm "Stability-First" alignment.
