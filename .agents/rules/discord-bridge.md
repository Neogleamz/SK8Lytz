# Discord Bridge Operational Rule

To maintain real-time visibility into the agent's active workflow, you must proactively broadcast your status via the Discord Webhook bridge.

1. **Mandatory Check-ins**: You MUST send a push notification summarizing your status in the following scenarios:
   - At the end of executing ANY workflow script from the `/.agents/workflows/` directory (e.g., after `/focus`, `/git-snapshot`, `/db-sync`, etc.).
   - At the end of any significant manual operational phase (e.g., completing research, drafting a plan, finishing a Bucket List item).
   - Right before pivoting to a paused state to wait for user instructions or feedback on a blocking error.
2. **Execution Method**: Use the local helper script to dispatch your message. Use the `run_command` tool to execute: `powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "Your short summary here."`
3. **Format**: Keep summaries conversational and concise. Focus on what was achieved, what stage you are at, or the explicit question/error you are currently blocking on.
