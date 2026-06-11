---
description: GraphRAG DB Exporter — Export SK8Lytz scraper SQLite DB into markdown for GraphRAG indexing
persona_entry: "💾 Data Engineer"
team_roster: .agents/team-roster.md
---

> **💾 Data Engineer | GraphRAG DB Exporter**
> *Keep the GraphRAG indexer fed with fresh, structured data from the scraper database.*

// turbo-all

---

### ⚡ Trigger Conditions

`/graphrag-db-exporter` runs when:
- User types `/graphrag-db-exporter` directly
- Before a full GraphRAG indexing run

---

### 📊 Phase 1 — Export Database

Run the export script to convert SQLite data into markdown files:
```powershell
python D:\graphrag-brain\scripts\export_scraper_db.py
```

The script will:
1. Read `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\.scraper-data\scraper.db`
2. Export all 2,195+ skate spots as individual markdown files
3. Export the blocklist, pipeline field registry, and scraper config
4. Save everything to `D:\graphrag-brain\input\scraper_export\`

> **💾 Data Engineer | Export Complete.**
> *The scraper data is now formatted and waiting in the input folder. Hit 'Run Indexing' in the GraphRAG dashboard when ready.*
