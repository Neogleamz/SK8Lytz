---
description: "Use before a release or a push to main, or when asked for a health check: npm audit and npm outdated plus the Supabase security and performance advisors, with a halt matrix."
---
<!-- generated from HQ playbook health-sweep v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Health sweep: dependencies and database advisors before a release

**Role:** debugger (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use before a release or a push to main, or when asked for a health check: npm audit and npm outdated plus the Supabase security and performance advisors, with a halt matrix.

## Steps
1. Run the dependency checks together: npm audit --audit-level=moderate and npm outdated. Keep the exact output.
   - Verify: Both outputs are quoted.
2. Run the database advisors for the project (the Supabase get_advisors tool, type security and then type performance). Use only the project that belongs to sk8lytz (HQ: lwercbzktneesehqdscu).
   - Gate: Never run it against another project.
   - Verify: Both advisor lists are quoted, or the connector's refusal is.
3. Apply the halt matrix. npm audit critical or high: HALT, no release. npm audit moderate only: record it and open a decision (open_decision) for a person to approve. Any security advisor: HALT. Performance advisors: file a task each. npm outdated by two or more major versions: file a tech-debt task.
   - Gate: HALT means nothing is pushed or released until a person answers or the finding is fixed.
   - Verify: Each finding has an action: halt, decision, task or none.
4. Report the table and the verdict: PROCEED when clean or approved, BLOCKED when anything halts.
   - Verify: The verdict matches the matrix.

## Output

## Health sweep
| Check | Status | Notes |
|---|---|---|
| npm audit | clean / critical / high / moderate | count |
| npm outdated | current / N behind | packages |
| Security advisors | clean / found | details |
| Performance advisors | clean / found | tasks filed |

**Verdict: PROCEED or BLOCKED**
