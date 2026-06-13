# Implementation Plan: deepdive-ast-collision

## Goal Description
Enhance Phase 2.75 of `deepdive-code-synthesis.md` to build an Abstract Syntax Tree (AST) dependency graph to verify parallel wave safety based on import trees, not just raw file lists.

## Proposed Changes

### deepdive-code-synthesis.md

#### [MODIFY] [deepdive-code-synthesis.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-code-synthesis.md)
1. **Update Phase 2.75 (Parallel Wave Assignment):**
   - **Step 1 (File Manifest):** Currently, it just looks at `Affected Files`. Change this to require building a deep dependency tree.
   - **New Step:** Use `grep_search` or an AST parsing script (`node tools/ast-parser.js`) to find all files that `import` the affected files.
   - **Step 2 (Collision Matrix):** Define a collision not just as "Task A and Task B edit the same file", but "Task A edits File X, and Task B edits File Y, but File Y depends on File X." If an import path overlaps, it is a collision pair.
   - **Safety Benefit:** This prevents a scenario where a UI agent safely refactors a screen, but a BLE agent simultaneously refactors a hook that the screen relies on, causing a build failure upon merge.

## Verification Plan

### Manual Verification
- Review the instructions in Phase 2.75 to ensure they are actionable by the agent executing the synthesis.
- Ensure the AST parsing does not blow up the context window by strictly limiting it to immediate parent/child imports.
