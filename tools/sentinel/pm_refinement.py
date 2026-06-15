#!/usr/bin/env python3
# pm_refinement.py
# Semantic Backlog Refinement (Autopilot PM)
# Reads backlogs, refines tickets, and automatically scaffolds engineering blueprints.

import os
import sys
import re
from google_antigravity import AgentCoordinator, LocalAgentConfig

class BacklogRefiner:
    def __init__(self, workspace_path, test_mode=False):
        self.workspace_path = workspace_path
        self.test_mode = test_mode
        self.config = LocalAgentConfig()
        self.bucket_list_path = os.path.join(workspace_path, "docs", "SK8Lytz_Bucket_List.md")
        self.plans_dir = os.path.join(workspace_path, "docs", "plans")

    def log(self, message):
        print(f"[PM-REFINER] {message}")

    def load_backlog(self):
        if not os.path.exists(self.bucket_list_path):
            self.log(f"Error: Backlog not found at: {self.bucket_list_path}")
            return None
        with open(self.bucket_list_path, "r", encoding="utf-8") as f:
            return f.read()

    def save_backlog(self, content):
        with open(self.bucket_list_path, "w", encoding="utf-8") as f:
            f.write(content)
        self.log("Backlog updated successfully! [SUCCESS]")

    def find_unrefined_tickets(self, backlog_content):
        # Scan for tasks matching pattern: - [ ] **`domain/slug-name`**
        # and checking if they contain [📝 NEEDS PLAN]
        task_regex = r"- \[ \] \*\*`([^`]+)`\*\*\n\s+- \*\*Tags:\*\* `([^`\n]+)`.*?(?=\n- \[ \] \*\*|\Z)"
        tasks = []
        for match in re.finditer(task_regex, backlog_content, re.DOTALL):
            slug = match.group(1)
            tags = match.group(2)
            full_block = match.group(0)
            
            if "[📝 NEEDS PLAN]" in tags:
                # Extract goal and details
                goal_match = re.search(r"- \*\*Goal:\*\* ([^\n]+)", full_block)
                details_match = re.search(r"- \*\*Details:\*\* ([^\n].*?)(?=\n\s*- \*\*Tags:|\n\s*- \*\*Goal:|\Z)", full_block, re.DOTALL)
                
                goal = goal_match.group(1).strip() if goal_match else "No goal defined."
                details = details_match.group(1).strip() if details_match else "No details defined."
                
                tasks.append({
                    "slug": slug,
                    "tags": tags,
                    "goal": goal,
                    "details": details,
                    "full_block": full_block
                })
        return tasks

    def refine_ticket(self, ticket):
        self.log(f"Refining backlog ticket: {ticket['slug']}...")
        
        # Spawn Antigravity Semantic PM Agent
        agent = AgentCoordinator.spawn_agent(
            role="Semantic Product Manager & Technical Architect",
            instruction=(
                "You are an expert product manager and technical director. "
                "You take raw engineering requirements, refine them into exact nested task descriptions, "
                "classify tags strictly according to the 5-tag Kanban Constitution, and generate "
                "a highly detailed engineering implementation plan template in markdown format."
            )
        )

        pm_prompt = f"""Ticket Slug: {ticket['slug']}
Current Tags: {ticket['tags']}
Goal: {ticket['goal']}
Details: {ticket['details']}

Please perform the following operations:
1. Refine the goal and details into a highly professional engineering description.
2. Determine if the tags are 100% correct according to the 5-tag Kanban Constitution. Ensure there is exactly one tag for:
   - [Status] -> Change to `[✅ READY]` since we are refining the plan now.
   - [Layer] -> Core / UI / Cloud / Lab
   - [Risk] -> L-RISK / M-RISK / H-RISK
   - [Size] -> Snack / Meal / Feast
   - [Cognitive Load] -> FLASH / PRO-HIGH / THINK
3. Output the exact Markdown replacement bullet for the ticket in the bucket list.
4. Output the full text of a rigorous, professional implementation plan blueprint file (PLAN-refine-<slug>.md) to guide a surgeon developer.
"""

        self.log("Requesting ticket enrichment from Google Antigravity...")
        refinement_response = agent.execute(pm_prompt)

        # Parse response into the refined task bullet and plan markdown
        # Basic parsing heuristics
        refined_bullet = ticket['full_block'] # fallback
        plan_content = ""

        # Attempt to parse markdown plan
        if "```markdown" in refinement_response:
            blocks = refinement_response.split("```markdown")
            for block in blocks[1:]:
                content = block.split("```")[0].strip()
                if "# Goal" in content or "# Implementation" in content or "## Proposed Changes" in content:
                    plan_content = content
                    break
        elif "# " in refinement_response:
            # Fallback if block formatting is loose
            plan_content = refinement_response

        # Parse refined bullet list
        bullet_match = re.search(r"(- \[ \] \*\*`[^`]+`\*\*.*?\n\s+- \*\*Details:\*\* [^\n].*?)(?=\n#|\n```|\Z)", refinement_response, re.DOTALL)
        if bullet_match:
            refined_bullet = bullet_match.group(1).strip()
        else:
            # Construct a polished bullet block if not isolated
            refined_bullet = f"""- [ ] **`{ticket['slug']}`**
  - **Tags:** `{ticket['tags'].replace('[📝 NEEDS PLAN]', '[✅ READY]')}`
  - **Plan:** 📎 [PLAN-{ticket['slug'].replace('/', '-')}.md](./plans/PLAN-{ticket['slug'].replace('/', '-')}.md)
  - **Goal:** {ticket['goal']}
  - **Details:** Refined by Autopilot PM. Plan drafted in plans folder."""

        # In mock mode, supply high-quality sample template
        if self.test_mode or not plan_content:
            plan_content = f"""# Implementation Plan: {ticket['slug']}

## Goal
{ticket['goal']}

## Details
{ticket['details']}

## Proposed Changes
### [Component]
- Automatically refined and structured by SDE Autopilot PM.
"""

        # Write plan file
        plan_filename = f"PLAN-{ticket['slug'].replace('/', '-')}.md"
        plan_path = os.path.join(self.plans_dir, plan_filename)
        os.makedirs(self.plans_dir, exist_ok=True)
        with open(plan_path, "w", encoding="utf-8") as f:
            f.write(plan_content)
        self.log(f"Scaffolded implementation plan at: {plan_path} [SUCCESS]")

        return refined_bullet

    def execute_refinement(self):
        backlog = self.load_backlog()
        if not backlog:
            return

        tickets = self.find_unrefined_tickets(backlog)
        if not tickets:
            self.log("No unrefined tickets needing plans found in bucket list. Backlog healthy! [SUCCESS]")
            return

        self.log(f"Found {len(tickets)} unrefined tickets. Commencing groom...")
        
        updated_backlog = backlog
        # For our active sprint, we groom the first unrefined ticket to avoid token overflow
        target_ticket = tickets[0]
        refined_bullet = self.refine_ticket(target_ticket)
        
        # Replace the original block with refined block in backlog content
        updated_backlog = updated_backlog.replace(target_ticket['full_block'], refined_bullet + "\n")
        
        self.save_backlog(updated_backlog)
        self.log("Ticket successfully groomed and updated in bucket list! [SUCCESS]")

if __name__ == "__main__":
    test_mode = len(sys.argv) > 1 and sys.argv[1] == "--test"
    workspace = "C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz"
    
    # Resolve workspace depending on active directory
    if "feat-sde-phase4" in os.getcwd():
        workspace = os.getcwd()
        
    refiner = BacklogRefiner(workspace, test_mode)
    refiner.execute_refinement()
