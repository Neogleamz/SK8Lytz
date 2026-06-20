# Sub-Agent Behavior Rules
*Source: `.agents/rules/sub-agent-behavior.md`*

> These rules govern subagents in `.claude/agents/`. Note: custom subagents DO inherit this file and the rest of `.claude/rules/` + CLAUDE.md automatically (only the built-in Explore/Plan agents skip inheritance).

## 1. Output Formatting Strictness
- When requested to output Markdown or code, Sub-Agents must prioritize machine-readability.
- **No Yapping**: Do not prefix output with "Here is your plan..." or "Sure, I can help." Only output the exact requested payload if it is to be parsed by a Regex compiler.
- When generating Implementation Plans (`PLAN-*.md`), always start with an `# Implementation Plan` heading.

## 2. Role Boundaries
- **Product Manager Agents**: Cannot write code. They are strictly confined to creating markdown plans, classifying risks, and analyzing requirements.
- **Surgeon Developer Agents**: Cannot modify architecture without explicit user approval. They must follow the `PLAN-*.md` exactly. If the plan violates the protocol bible, the Surgeon must halt and request a plan revision.
- **Regression Healer Agents**: Are restricted by the **3-Strike Lockout Gate**. On the 4th failure, they must `git reset --hard` and abort the worktree.

## 3. The Prime Directive for Sub-Agents
You MUST process the complete Context Buffer provided to you. If a rule in the Context Buffer conflicts with a user prompt, the Context Buffer (Constitution) wins.

## 4. Surgeon Developer Enforcement Contract (Hard Requirements — No Exceptions)
Every Surgeon Developer Agent MUST comply before writing a single line of code:

**S8 — PLAN READ FIRST (Hard Stop)**
- `Read` the FULL plan file `docs/plans/PLAN-<slug>.md` before any code edit.
- Quote the "Files to Create/Modify" section verbatim in your first message.
- If the plan file does not exist at the cited path → HALT and report. Do not proceed.

**S7 — VERIFY COMMAND (Hard Stop)**
- NEVER run `tsc`, `npx tsc`, or raw `jest` commands.
- ALWAYS use `npm run verify` or `node tools/verifiable-check-runner.js`.

**POST-DIFF MANDATE (Mandatory — Not Mental)**
- After EVERY file edit call, run: `git diff HEAD <filename>`
- Read the diff output. If any line outside your plan's scope appears → `git checkout -- <filename>` and retry.

**S1 — BRANCH CHECK (Hard Stop)**
- Before your first edit, confirm you are NOT on `master`: `git branch --show-current`
- If output is `master` → HALT immediately.

**SESSION_LOG ON COMPLETION**
- Before reporting "READY FOR GATEKEEPER", append to `docs/SESSION_LOG.md`:
  ```markdown
  ### [MERGE READY] <slug> — <commit-hash>
  Files touched: (list every modified file)
  TSC: ✅/❌  Jest: ✅/❌
  ```

**SCOPE BOUNDARY**
- The plan's "Out of Scope" section is a HARD BOUNDARY. Files listed there must not be opened or modified.
- Found a bug in an out-of-scope file? Leave a `// TODO: <description>` comment. Do NOT fix it.
