---
description: "Use when asked to audit, hunt for bugs across or sweep a whole codebase: read-only agents write JSON findings to disk, a script merges, scores and clusters them, and the result is a draft plan with w…"
argument-hint: "[domains or rules to limit it to, optional]"
---
<!-- generated from HQ playbook audit-fleet v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Audit fleet: read-only agents by domain and by rule, merged into a plan with waves

**Role:** reviewer (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when asked to audit, hunt for bugs across or sweep a whole codebase: read-only agents write JSON findings to disk, a script merges, scores and clusters them, and the result is a draft plan with waves and a metrics record, never a pile of chat.

## Steps
1. Check the docs are fresh first: the architecture and reference docs the agents will lean on must describe the code as it is now (search_brain and the brain-lint command). Stale docs make agents report false positives.
   - Gate: Do not launch the fleet on stale docs: refresh or correct them first (propose_update).
   - Verify: Say which docs you checked and that they match the code.
2. Prepare the run: a findings folder outside the repo (your scratchpad directory, for example <scratchpad>/audit-<date>/raw) so nothing is committed. List the rule catalogue with: node scripts/audit-synth.mjs --rules. Choose the domains for sk8lytz (see the stack packs) and the rules to hunt (all of them by default).
   - Verify: A folder exists and the domain list and rule list are written down with their counts.
3. Canary first: launch ONE domain agent (the smallest domain) and ONE rule agent. Get each prompt with node scripts/audit-synth.mjs --prompt domain <name> --out <folder> or --prompt rule <rule id> --out <folder>, and run each as a read-only subagent. Then check their files: node scripts/audit-synth.mjs <folder> --expect 2.
   - Gate: If a canary file is malformed, fix the prompt and run the canaries again (at most twice). Never launch the full fleet on a malformed canary.
   - Verify: Both canary files validate and the summary shows no malformed file.
4. Launch the fleet within the owner's session limit: call get_session_policy for the number of agents you may run at once, report_session before each batch, and start no more at once than the limit. Every agent is read-only, gets one scope (a domain or a rule) and writes one JSON file to the folder.
   - Gate: At the wind-down point start no new agents: reduce what you have.
   - Verify: Each batch was reported and never exceeded the limit.
5. Poll for the expected file count: list the folder every minute or two until all N agent files exist. Set a limit (twenty minutes, or when the agents have all reported) and when it passes continue on the partial data on purpose; say which agents are missing.
   - Gate: Do not wait forever, and do not hide a partial run: the plan says it was built on partial data.
   - Verify: The count found and the count expected are both stated.
6. Reduce: node scripts/audit-synth.mjs <folder> --expect N --root . (add --known-issues <file> to tag known issues). It merges the same finding (exact, then within five lines), scores confidence (CONFIRMED when two or more agents agree, PROBABLE with one agent and a snippet, UNCERTAIN otherwise), scrubs false positives (tests and mocks, generated files, dev-only code) and clusters by file area. Read summary.md and metrics.json.
   - Verify: The metrics include the false-positive rate and the dedup ratio.
7. Spot-check before you trust it: open five CONFIRMED High or Medium findings and read the code at file:line. If more than one in five is wrong, say so in the plan and lower the claim.
   - Verify: Five findings checked, with how many were right.
8. Propose the plan, never a report: propose_plan with the title, body and steps of plan.json (one step per cluster, CONFIRMED High and Medium findings first, waves from file overlap and dependency). It stays a draft for the owner.
   - Gate: No fixes in this run: the audit changes no code.
   - Verify: The draft plan exists in HQ Plans.
9. Record the numbers: record_research with the question 'What did the audit fleet find in sk8lytz on <date>?', the metrics table as the summary, the findings folder path in the findings and metrics.json as the source.
   - Verify: The research entry exists and names the false-positive rate.

## Stack pack: supabase
- Domains: supabase/functions (brain-mcp is over 280 KB: give it several agents by section), supabase/migrations (one agent per thirty files).
- Rules: rls-every-table, coverage-class, io-without-try-catch, no-paid-ai.

## Output

## Audit fleet
**Agents:** N launched, N files found, N malformed
**Findings:** ingested, unique, scrubbed (false-positive rate), kept
**Confidence:** CONFIRMED / PROBABLE / UNCERTAIN
**Spot check:** x of 5 right
**Plan:** the draft plan id, clusters and waves
**Research record:** the id
**Partial data:** none, or which agents were missing
