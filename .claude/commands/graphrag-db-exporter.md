# /graphrag-db-exporter — GraphRAG DB Exporter

**Description:** Export SK8Lytz scraper SQLite DB and Cloud Supabase DB into markdown for GraphRAG indexing.
**Persona:** 💾 Data Engineer

> **💾 Data Engineer | GraphRAG DB Exporter**
> *Keep the GraphRAG indexer fed with fresh, structured data from the scraper database and the cloud Supabase database.*

---

### Trigger Conditions

`/graphrag-db-exporter` runs when:
- User clicks the workflow in the UI
- User types `/graphrag-db-exporter` directly
- Before a full GraphRAG indexing run

---

### Phase 1 — Export Local Scraper Database

Run the export script to convert local SQLite data into markdown files:
```powershell
python D:\graphrag-brain\scripts\export_scraper_db.py
```

### Phase 2 — Export Cloud Supabase Database

Run the export scripts to bypass RLS and pull all cloud relational data, specifically formatted as narrative markdown for GraphRAG:
```powershell
python D:\graphrag-brain\scripts\export_supabase_db.py
python D:\graphrag-brain\scripts\export_special_tables.py
python D:\graphrag-brain\scripts\export_all_supabase_db.py
```

The scripts will:
1. Export all 2,195+ skate spots from local SQLite to `D:\graphrag-brain\input\scraper_export\`
2. Export core Supabase tables (users, devices, spots, sessions) using narrative prose to `D:\graphrag-brain\input\supabase_export\`
3. Export new special tables (crew tables, orders, products) natively.
4. Dump all remaining 55+ generic tables dynamically.

> **💾 Data Engineer | Export Complete.**
> *All database sources (local scraper + cloud Supabase) are now formatted and waiting in the input folder. Hit 'Run Indexing' in the GraphRAG dashboard when ready.*
