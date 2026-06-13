# Implementation Plan

## Task: `docs/cartographer-rebuild-and-harden`
**Goal:** Run the full 21-node `/deepdive-docs` cartographer fleet, update all 5 Tier-3 satellite docs, inject 3 missing ADRs, and surgically harden 4 workflow files to prevent Phase 4 from being silently skipped again.

---

## Files to Create/Modify

### Phase A — Run Cartographer + Inject ADRs (Avery orchestrates)
- **[MODIFY]** `tools/SK8Lytz_App_Master_Reference.md` — 21 domain cartography injections via CARTOGRAPHER_START/END markers (non-destructive)
- **[MODIFY]** `tools/State_Charts_UX.md` — Add State Chart 4: sessionMachine lifecycle (IDLE→WARMING_UP→ACTIVE→PAUSED→COMMITTED)
- **[MODIFY]** `tools/User_Journey_Maps.md` — Update Journey 3 for bidirectional watch START/STOP command flow
- **[MODIFY]** `tools/Architecture_Decision_Records.md` — Inject 3 missing ADRs (ADR-007, ADR-008, ADR-009) from SESSION_LOG entries dated 2026-06-11

### Phase B — Pipeline Hardening (Surgical edits to 4 workflow files)
- **[MODIFY]** `.agents/workflows/deepdive-docs.md` — Phase 4: unconditional satellite doc update + Phase 5: mandatory ADR sync block + git commit step
- **[MODIFY]** `.agents/workflows/start-task.md` — Docs Gate: add satellite doc trigger rules for XState/WatchBridge/external system changes
- **[MODIFY]** `.agents/workflows/wind-down.md` — Step 4.75: Alex satellite doc completeness check

---

## Execution Steps

### Phase A: Cartographer Run

**Step 1 — Boundary Marker Verification**
- `view_file` `tools/SK8Lytz_App_Master_Reference.md` and verify all 21 CARTOGRAPHER_START/END pairs exist
- Source: `tools/SK8Lytz_App_Master_Reference.md` (460KB)
- Verify: 21 pairs confirmed before fleet launch

**Step 2 — Launch 21 Sub-Agent Fleet** (per `/deepdive-docs` Phase 2)
- Invoke 21 `research` sub-agents simultaneously per domain partition table in `deepdive-docs.md:L35-L57`
- Each writes to `artifacts/deepdive_docs/<DOMAIN_MARKER>_cartography.md`
- Anti-context-explosion: sub-agents write to file, do NOT send_message
- Source: `.agents/workflows/deepdive-docs.md:L35-L84`
- Verify: 21 files written to `artifacts/deepdive_docs/`

**Step 3 — Poll and Synthesize** (per Phase 2.5 + Phase 3)
- Set 5-min timer. Count files on wake. Expect 21.
- Read all 21 payloads. Inject into Master Reference via CARTOGRAPHER markers.
- Archive any `[MOVE_TO_ARCHIVE]` items to Section 13.
- Verify: `grep -c "CARTOGRAPHER_END" SK8Lytz_App_Master_Reference.md` == 21

**Step 4 — Phase 4: Satellite Doc Update (Unconditional)**
- Open `tools/State_Charts_UX.md` — add sessionMachine chart using XState v5 states from `src/services/session/sessionMachine.ts`
  - States: IDLE, WARMING_UP, ACTIVE, PAUSED, COMMITTED
  - Source: `src/services/session/sessionMachine.ts` (read in full before writing)
- Open `tools/User_Journey_Maps.md` — update Journey 3 sections 1+3 to show bidirectional watch commands
  - Source: `src/context/SessionContext.tsx` (Wave 2 version — read in full)
- Open `tools/System_Context_Diagram.md` — diff vs Phase 3 findings; update if new external system added
- Verify: Each satellite doc has a meaningful change committed

**Step 5 — Phase 5: ADR Sync (Mandatory)**
- Read SESSION_LOG entries at lines 40-52 (3 pending ADRs)
- Inject as ADR-007, ADR-008, ADR-009 into `tools/Architecture_Decision_Records.md`
  - ADR-007: Session State → XState sessionMachine Architecture (2026-06-11T16:17)
  - ADR-008: Session Never Ends on BLE Disconnect (2026-06-11T16:17)
  - ADR-009: WatchBridge Distance Field = `"distance"` (2026-06-11T21:27)
- Source: `tools/SESSION_LOG.md:L40-L52`
- Verify: `grep "ADR-007\|ADR-008\|ADR-009" tools/Architecture_Decision_Records.md` → 3 matches

**Step 6 — npm run verify + Commit**
- `npm run verify` (verify passes — these are markdown files, TSC/Jest unaffected but Blast Radius check runs)
- `git add tools/ && git commit -m "docs(cartography): full 21-node rebuild + Phase 4+5 satellite sync + 3 ADRs injected"`
- Write SESSION_LOG [ARTIFACT] entry

### Phase B: Pipeline Hardening

**Step 7 — Harden deepdive-docs.md Phase 4**
- Read `.agents/workflows/deepdive-docs.md:L121-L129` (Phase 4 as-is)
- Change "if [IMPACTS_X] is flagged" → "unconditionally open each satellite doc and diff"
- Add Phase 5 block (ADR sync — 4 explicit steps)
- Add mandatory `git add` + commit of satellite docs at end of Phase 4+5
- Source: `.agents/workflows/deepdive-docs.md:L121-L129` + `agent-behavior.md Rule 11`
- Verify: `view_file` confirms Phase 4 no longer uses conditional language

**Step 8 — Harden start-task.md Docs Gate**
- Read the current Phase 5 Docs Gate section in `start-task.md`
- Add satellite doc trigger rules after the Master Reference gate:
  - sessionMachine/XState touches → State_Charts_UX.md
  - WatchBridge/session flow touches → User_Journey_Maps.md
  - New external system → System_Context_Diagram.md
- Source: `.agents/workflows/start-task.md` Phase 5 section (read before editing)
- Verify: `view_file` confirms satellite doc triggers present

**Step 9 — Add Step 4.75 to wind-down.md**
- Read `.agents/workflows/wind-down.md` current Step 4 section
- Insert Step 4.75 between Step 4 and Step 5: satellite doc completeness check
  - Check git log for last commit to each satellite doc vs SESSION_LOG [DECISION] count
- Source: `.agents/workflows/wind-down.md` (read before editing)
- Verify: Step 4.75 present in file

**Step 10 — Commit hardening edits**
- `git add .agents/workflows/deepdive-docs.md .agents/workflows/start-task.md .agents/workflows/wind-down.md`
- `git commit -m "chore(system): harden doc pipeline — Phase 4 unconditional + Phase 5 ADR sync + satellite doc triggers"`
- `npm run verify`
- Write SESSION_LOG [MERGE] entry

---

## Out of Scope
- Updating `tools/BLE_AUDIT_REPORT.md` — separate `/audit-ble-pipeline` run
- Updating `tools/SK8Lytz_TEST_PLAN.md` — separate task `docs/test-plan-session-machine`
- Updating `tools/SCRAPER_PIPELINE_BIBLE.md` — separate audit
- Updating `docs/PERSISTENCE_CONTRACT.md` — separate audit
- Updating `tools/ARCH_DEPENDENCY_MAP.json` — auto-generated by `ast-parser.js`
- Any changes to `.ts`/`.tsx` source files
- Any changes to `docs/plans/` PLAN files
