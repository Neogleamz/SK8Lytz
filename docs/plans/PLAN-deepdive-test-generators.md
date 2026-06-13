# Implementation Plan: deepdive-test-generators

## Goal Description
Modify `deepdive-code-synthesis.md` to autonomously spawn TDD test-writer agents for confirmed HIGH/MEDIUM bugs. Instead of just creating a KanBan task to "fix the bug", it will generate a failing regression test so the developer must make the test pass to complete the task.

## Proposed Changes

### deepdive-code-synthesis.md

#### [MODIFY] [deepdive-code-synthesis.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-code-synthesis.md)
1. **Add new Phase 2.85 — Vector Delta (The Test Generators):**
   - Insert this phase after Wave Assignment and before Triage Routing.
   - For every finding marked `HIGH` or `MEDIUM` with `CONFIRMED` confidence:
     - Invoke a sub-agent (`invoke_subagent` using the `TDD` profile).
     - Task the sub-agent to write a failing Jest or Detox test case to `__tests__/` that explicitly targets the `rule_violated` in the specific `file`.
     - The sub-agent must commit the failing test to the worktree.
2. **Update Phase 3 (Triage Routing):**
   - Alter the KanBan task generation format. The `Goal:` must now state: "Fix the bug to make the auto-generated failing regression test pass."
   - Add `Source of Truth: [Test File]` to the task schema.

## Verification Plan

### Manual Verification
- Ensure the prompt for the Vector Delta sub-agents explicitly forbids them from fixing the code themselves (they must ONLY write the failing test).
- Run the `/deepdive-code-synthesis` workflow and verify it successfully spawns the test generators.
