#!/usr/bin/env python3
# debt_harvester.py
# Task-Debt Harvester (Vector 4 Backlog Autopilot)
# Recursively scans the codebase for TODOs, FIXMEs, and HACKs, and populates the triage queue.

import os
import sys
import re

# Add sentinel tools directory to system path to import google_antigravity
sentinel_dir = os.path.dirname(os.path.abspath(__file__))
if sentinel_dir not in sys.path:
    sys.path.append(sentinel_dir)

try:
    from google_antigravity import AgentCoordinator
except ImportError:
    AgentCoordinator = None

class DebtHarvester:
    def __init__(self, workspace_path, dry_run=False):
        self.workspace_path = workspace_path
        self.dry_run = dry_run
        self.bucket_list_path = os.path.join(workspace_path, "docs", "SK8Lytz_Bucket_List.md")
        self.src_dir = os.path.join(workspace_path, "src")

    def log(self, message):
        print(f"[DEBT-HARVESTER] {message}")

    def scan_codebase_for_debt(self):
        self.log(f"Crawling directory for technical debt: {self.src_dir}")
        if not os.path.exists(self.src_dir):
            self.log("Error: src/ directory not found.")
            return []

        # Regex to capture: // TODO: <text> or /* FIXME: <text> */ or // HACK: <text>
        debt_pattern = re.compile(r"(//|/\*)\s*(TODO|FIXME|HACK)\s*:\s*(.*?)(?:\*/|\n|$)")
        debt_items = []

        for root, dirs, files in os.walk(self.src_dir):
            # Ignore standard directories we don't want to scan
            if "node_modules" in root or "__tests__" in root or ".git" in root:
                continue
                
            for file in files:
                if not file.endswith((".ts", ".tsx", ".js", ".jsx")):
                    continue
                    
                file_path = os.path.join(root, file)
                relative_path = os.path.relpath(file_path, self.workspace_path)
                
                try:
                    with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                        for line_num, line in enumerate(f, 1):
                            match = debt_pattern.search(line)
                            if match:
                                category = match.group(2).upper()
                                comment_text = match.group(3).strip()
                                
                                # Ignore blank comments or standard annotations
                                if not comment_text:
                                    continue
                                    
                                debt_items.append({
                                    "file": file,
                                    "relative_path": relative_path,
                                    "absolute_path": file_path,
                                    "line": line_num,
                                    "category": category,
                                    "text": comment_text
                                })
                except Exception as e:
                    self.log(f"Skipping file {file} due to read error: {str(e)}")

        self.log(f"Scan complete. Discovered {len(debt_items)} codebase debt annotations.")
        return debt_items

    def harvest(self):
        debt_items = self.scan_codebase_for_debt()
        if not debt_items:
            self.log("Zero debt annotations found. Backlog is pristine! [SUCCESS]")
            return

        if not os.path.exists(self.bucket_list_path):
            self.log(f"Error: Bucket list ledger not found at: {self.bucket_list_path}")
            return

        try:
            with open(self.bucket_list_path, "r", encoding="utf-8") as f:
                bucket_content = f.read()
        except Exception as e:
            self.log(f"Failed to read bucket list: {str(e)}")
            return

        pm_agent = None
        if AgentCoordinator:
            try:
                # Spawn a PM Agent to draft AI-First Plans for discovered technical debt
                pm_agent = AgentCoordinator.spawn_agent(
                    role="Autopilot Technical Project Manager (TPM)",
                    instruction="You are an expert technical architect and TPM. Analyze code context surrounding technical debt annotations, and draft highly detailed AI-First Plans and specific Safety Rules following our rules systems (e.g. No any cast, explicit safe area UI, 3-slab layout rules, MTU negotiates, global BLE mutex)."
                )
                self.log("Successfully connected to Google Antigravity AI agent coordinator for backlog enrichment.")
            except Exception as e:
                self.log(f"Failed to instantiate SpawnerAgent: {str(e)}")

        new_tickets = []
        for item in debt_items:
            # Check for duplicate: search by filename and line number, or comment text
            unique_marker = f"{item['relative_path']}#L{item['line']}"
            cleaned_text_match = item['text'].replace("`", "").replace("'", "").replace('"', "")
            
            # Simple substring lookup to avoid duplicating existing entries
            if unique_marker in bucket_content or cleaned_text_match in bucket_content:
                continue

            # Scaffold ticket following strict 5-tag Kanban Constitution
            file_slug = item['file'].replace(".tsx", "").replace(".ts", "").replace(".jsx", "").replace(".js", "").lower()
            slug = f"chore/debt-{item['category'].lower()}-{file_slug}-{item['line']}"

            # Extract surrounding context (10 lines of code)
            context_lines = []
            try:
                with open(item['absolute_path'], "r", encoding="utf-8", errors="ignore") as f:
                    all_lines = f.readlines()
                    start = max(0, item['line'] - 6)
                    end = min(len(all_lines), item['line'] + 5)
                    for idx in range(start, end):
                        prefix = "--> " if idx + 1 == item['line'] else "    "
                        context_lines.append(f"{prefix}{idx+1}: {all_lines[idx].rstrip()}")
            except Exception:
                pass
            code_context = "\n".join(context_lines)

            ai_plan = ""
            if pm_agent:
                self.log(f"Querying Gemini API to draft AI-First Plan for {slug}...")
                prompt = f"""
We have discovered a technical debt annotation in the codebase:
Category: {item['category']}
Annotation: {item['text']}
File: {item['relative_path']}
Line: {item['line']}

Code Context:
```typescript
{code_context}
```

Please generate:
1. **Goal**: A clear, actionable goal statement.
2. **AI-First Plan**: A detailed, step-by-step technical plan for an AI developer to resolve this specific debt. Include details of files, methods, interfaces, or logic to change.
3. **Safety Rules**: Relevant safety rules from our engineering standards (e.g. "No any cast", "Strict BLE gates", "Maintain backward compatibility", "Do not add unneeded external dependencies").

Format your response strictly using markdown sub-items so it nests perfectly under a Markdown bullet list item.
For example, indent all lines with exactly two spaces:
  - **Goal**: ...
  - **Plan**: ...
  - **Safety Rules**: ...

Make it professional, precise, and immediately actionable for a sub-agent. Do not include markdown code block containers around the entire output, just the content directly.
"""
                try:
                    raw_plan = pm_agent.execute(prompt)
                    # Normalize indentation for direct injection into bullet list
                    ai_plan = "\n".join(f"  {line.lstrip()}" if line.strip() else "" for line in raw_plan.splitlines())
                except Exception as e:
                    self.log(f"Gemini API prompt failed: {str(e)}. Falling back to local plan template.")

            if not ai_plan:
                ai_plan = f"""  - **Goal:** Resolve code {item['category']} annotation in {item['file']}: "{item['text']}"
  - **Plan:**
    1. Inspect {item['relative_path'].replace('\\', '/')} at line {item['line']}.
    2. Analyze surrounding context: `{item['text']}`.
    3. Implement correct structural fix according to codebase conventions.
    4. Validate compilation cleanly with no implicit `any` type casting.
  - **Safety Rules:**
    1. **The No `any` Cast Law**: Banned from bypassing TS errors via `as any` or `@ts-ignore`.
    2. **Zero-Collateral Damage**: Ensure no regression on related state selectors or hooks."""

            # Form ticket markdown
            ticket_md = f"""- [ ] **`{slug}`**
  - **Tags:** `[📝 NEEDS PLAN]` `[CORE]` `[L-RISK]` `[Snack]` `[🤖 FLASH]`
  - **Details:** Found at [{item['relative_path'].replace('\\', '/')}:L{item['line']}](file:///{item['absolute_path'].replace('\\', '/')}#L{item['line']}).
{ai_plan}
"""
            new_tickets.append(ticket_md)

        if not new_tickets:
            self.log("All codebase debt annotations are already documented in the backlog. [SUCCESS]")
            return

        self.log(f"Preparing to inject {len(new_tickets)} new triage tickets into bucket list...")
        
        if self.dry_run:
            self.log("[DRY-RUN] Simulated tickets to inject:")
            for ticket in new_tickets:
                print(ticket)
            return

        # Insert tickets directly beneath the ## 🚑 TRIAGE QUEUE (Bugs & Hotfixes) header
        triage_header = "## 🚑 TRIAGE QUEUE (Bugs & Hotfixes)"
        if triage_header in bucket_content:
            injection_point = bucket_content.find(triage_header) + len(triage_header)
            
            # Form clean newline segment
            injected_segment = "\n\n" + "\n".join(new_tickets)
            updated_content = bucket_content[:injection_point] + injected_segment + bucket_content[injection_point:]
            
            try:
                with open(self.bucket_list_path, "w", encoding="utf-8") as f:
                    f.write(updated_content)
                self.log(f"Successfully harvested {len(new_tickets)} codebase debt annotations into the triage queue! [SUCCESS]")
            except Exception as e:
                self.log(f"Failed to write updates to bucket list: {str(e)}")
        else:
            self.log("Error: Could not locate '## 🚑 TRIAGE QUEUE' header in bucket list.")

if __name__ == "__main__":
    if hasattr(sys.stdout, "reconfigure"):
        sys.stdout.reconfigure(encoding="utf-8")
    dry_run = len(sys.argv) > 1 and sys.argv[1] == "--test"
    workspace = "C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz"
    
    # Auto-resolve workspace depending on active directory execution path
    if "feat-sde-autopilot" in os.getcwd():
        workspace = os.getcwd()
    elif "SK8Lytz-worktrees" in os.getcwd():
        # Extrapolate parent worktree directory
        workspace = os.getcwd()
        
    harvester = DebtHarvester(workspace, dry_run)
    harvester.harvest()
