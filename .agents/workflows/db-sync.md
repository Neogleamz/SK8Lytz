---
description: Generate fresh Supabase TypeScript types and inject them into src/types/supabase.ts
---

> NOTE: This workflow MUST be automatically triggered after executing database table changes or RLS migrations via MCP tools, ensuring TS parity.

// turbo-all

1. Verify the project compiles cleanly before the sync

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 10
Write-Host "Pre-sync TSC check complete."
```

2. Use the `mcp_supabase-mcp-server_generate_typescript_types` MCP tool to pull the latest schema.
   - The project ID can be retrieved via the `mcp_supabase-mcp-server_list_projects` tool if unknown.
   - Save the output to `src/types/supabase.ts` using the `write_to_file` tool with `Overwrite: true`.

3. Verify the sync by counting the generated types

```powershell
$lines = (Get-Content "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\types\supabase.ts" | Measure-Object -Line).Lines
Write-Host "supabase.ts synced: $lines lines generated."
```

4. Run TypeScript check to ensure no regressions from the new types

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 20
Write-Host "Post-sync TSC check complete."
```
