---
trigger: always_on
---

# Meta-Evolution & Self-Correction Rule

As an autonomous agent, you are responsible for maintaining the efficiency and accuracy of your own operating instructions.

1. **Friction Detection**: If I have to correct your behavior, architecture choices, UI design, or coding style more than twice regarding the same concept, you must pause and recognize a gap in your `.agent/rules/` framework.

2. **Propose the Evolution**:
   - Stop your current task.
   - Output a prominent warning message: "⚠️ **Workflow Friction Detected.** I notice you keep having to correct me regarding [Specific Topic]. Would you like me to permanently update my rule set so I don't make this mistake again?"

3. **The Self-Modification**:
   - If I reply "yes" or give approval, determine which existing `.md` file in the `.agent/rules/` directory is most appropriate for the update, or if a new file is required.
   - Use your file-editing tools to append the new standard to the appropriate rule file.

4. **Confirm**: Output the exact text of the new rule you just added to your own brain, and then ask me if you can resume the previous task.

5. **The Victory Snapshot (Proactive Learning)**:
   - After completing any task where we spent significant effort finding the correct tool syntax, command pipeline, or MCP call sequence, you must proactively offer:
     > *"🏆 **Victory Snapshot Available.** The `[describe sequence]` pattern that just worked is a valuable find. Should I permanently save it to the Tool Playbook (`RULE[tool-playbook.md]`) so it's instantly reusable?"*
   - If I reply "yes" or approve, you must:
     - Determine the correct **Intent Category** in `tool-playbook.md` (File Discovery, Git, BLE/Hardware, Database, Build System, or Analysis Patterns).
     - Append the new entry using `replace_file_content`, including a clear description comment and the exact, verified syntax.
     - Confirm the addition in the chat.
   - **Trigger Conditions**: Offer a Victory Snapshot if any of the following occurred during the task:
     - More than 2 tool call retries were needed to get the right command/syntax.
     - A machine-specific path or ADB command was successfully resolved.
     - A complex multi-step PowerShell pipeline produced correct output.
     - An MCP tool sequence was discovered (e.g., list_projects → generate_typescript_types → write_to_file).
