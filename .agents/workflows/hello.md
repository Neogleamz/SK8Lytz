---
description: Fire up the environment, launch web demo, open it, and provide a status update
---

// turbo-all

1. Launch the Scraper Stack (CCTower, Dashboard, Discord Bridge via PM2 — no popup windows)
```powershell
powershell.exe -ExecutionPolicy Bypass -File "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\scraper\start-scraper-stack.ps1"
```

2. Synchronize Supabase TypeScript definitions
   - Execute the `mcp_supabase-mcp-server_generate_typescript_types` MCP tool using project ID `qefmeivpjyaukbwadgaz`.
   - Overwrite `src/types/supabase.ts` with the new definitions using `write_to_file`.

3. Launch the Expo web server
```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"; npx expo start --web
```

4. The AI must read `tools/SK8Lytz_Bucket_List.md` to determine the current project status.

5. Verify Database Backup Status
   - Use PowerShell to check the `backups/` directory for the most recent `.sql` file: `Get-ChildItem -Path C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\backups -Filter "data_*.sql" | Sort-Object LastWriteTime -Descending | Select-Object -First 1`
   - Note the timestamp and file size of the last backup.

6. Provide a friendly greeting ("Hello!"), summarize the top 3 open items from the Bucket List, report the status of the database type sync and backup, and ask the user what they would like to tackle next.
