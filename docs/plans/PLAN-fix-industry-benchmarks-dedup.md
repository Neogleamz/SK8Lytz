# Implementation Plan

## Task: `fix/industry-benchmarks-dedup`
**Goal:** Remove the duplicate content in `tools/INDUSTRY_BENCHMARKS.md`. The entire file content (lines 1-49) is repeated verbatim from lines 50 onwards due to a prior double-write bug.

---

## Files to Create/Modify

- **[MODIFY]** `tools/INDUSTRY_BENCHMARKS.md` — remove duplicate second copy of all content

---

## Execution Steps

**Step 1 — Confirm Duplicate**
- `view_file` `tools/INDUSTRY_BENCHMARKS.md` in full (125 lines)
- Confirm lines 1-49 == lines 50-99 (both copies of header + 4 benchmark entries)
- Lines 100-125 appear to be additional unique content (High-Density Grouping, Hardware Mocks, DB PII Encryption entries) — preserve these
- Source: `tools/INDUSTRY_BENCHMARKS.md:L1-L125`

**Step 2 — Deduplicate**
- Remove the second header block (lines ~50-56: duplicate `# Industry Benchmarks` + vault intro)
- Keep only one copy of: header, vault intro, format template, BLE Connectivity benchmark, OS-Specific BLE benchmark, OS Background Execution benchmark
- Preserve unique entries at bottom: High-Density Grouping (L100), Hardware Mocks (L109), DB PII Encryption (L117)
- Result: ~75 lines (no content loss, pure dedup)

**Step 3 — Commit**
- `git add tools/INDUSTRY_BENCHMARKS.md`
- `git commit -m "fix(docs): remove duplicate content block in INDUSTRY_BENCHMARKS.md"`
- Verify: line count drops from 125 to ~75

---

## Out of Scope
- Adding new benchmark entries (separate task via `/intake`)
- Any changes to `.ts`/`.tsx` source files
