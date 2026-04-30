import { createClient } from '@supabase/supabase-js';
import { db } from './core/LocalDB';
import dotenv from 'dotenv';
import path from 'path';

// Load environment variables
dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL;
const supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY;

if (!supabaseUrl || !supabaseKey) {
  console.error("❌ Missing Supabase URL or Key in environment variables.");
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseKey);

async function migrateData() {
  console.log("🚀 Starting data migration from Supabase to Local SQLite...");

  try {
    // 1. Migrate scraper_config
    console.log("Fetching scraper_config...");
    const { data: configData, error: configErr } = await supabase.from('scraper_config').select('*').eq('id', 1).single();
    
    if (configErr && configErr.code !== 'PGRST116') { // PGRST116 means no rows
      console.error("Error fetching scraper_config:", configErr);
    } else if (configData) {
      console.log(`Found scraper_config. Migrating...`);
      const { id, updated_at, ...payload } = configData;
      const keys = Object.keys(payload);
      if (keys.length > 0) {
        db.prepare(`DROP TABLE IF EXISTS scraper_config`).run();
        db.prepare(`CREATE TABLE IF NOT EXISTS scraper_config (id INTEGER PRIMARY KEY, config_json TEXT, updated_at TEXT DEFAULT CURRENT_TIMESTAMP)`).run();
        db.prepare(`INSERT INTO scraper_config (id, config_json) VALUES (1, ?)`).run(JSON.stringify(payload));
        console.log(`✅ Migrated scraper_config.`);
      }
    } else {
        console.log(`ℹ️ No scraper_config found in Supabase.`);
    }

    // 2. Migrate scraper_blocklist
    console.log("Fetching scraper_blocklist...");
    const { data: blocklistData, error: blocklistErr } = await supabase.from('scraper_blocklist').select('*');
    if (blocklistErr) {
        console.error("Error fetching scraper_blocklist:", blocklistErr);
    } else if (blocklistData && blocklistData.length > 0) {
        console.log(`Found ${blocklistData.length} records in scraper_blocklist. Migrating...`);
        const insertBlocklist = db.prepare(`INSERT INTO scraper_blocklist (pattern, match_type, reason, created_at) VALUES (@pattern, @match_type, @reason, @created_at)`);
        
        db.transaction(() => {
            for (const item of blocklistData) {
                insertBlocklist.run({
                    pattern: item.pattern,
                    match_type: item.match_type || 'name',
                    reason: item.reason || '',
                    created_at: item.created_at || new Date().toISOString()
                });
            }
        })();
        console.log(`✅ Migrated ${blocklistData.length} records into scraper_blocklist.`);
    } else {
        console.log(`ℹ️ No records found in scraper_blocklist.`);
    }

    // 3. Migrate scraper_blocklist_keywords
    console.log("Fetching scraper_blocklist_keywords...");
    const { data: keywordsData, error: keywordsErr } = await supabase.from('scraper_blocklist_keywords').select('*');
    if (keywordsErr) {
        console.error("Error fetching scraper_blocklist_keywords:", keywordsErr);
    } else if (keywordsData && keywordsData.length > 0) {
        console.log(`Found ${keywordsData.length} records in scraper_blocklist_keywords. Migrating...`);
        const insertKeyword = db.prepare(`INSERT INTO scraper_blocklist_keywords (keyword, created_at) VALUES (@keyword, @created_at)`);
        
        db.transaction(() => {
            for (const item of keywordsData) {
                insertKeyword.run({
                    keyword: item.keyword,
                    created_at: item.created_at || new Date().toISOString()
                });
            }
        })();
        console.log(`✅ Migrated ${keywordsData.length} records into scraper_blocklist_keywords.`);
    } else {
        console.log(`ℹ️ No records found in scraper_blocklist_keywords.`);
    }

    // 4. Migrate pipeline_field_registry
    console.log("Fetching pipeline_field_registry...");
    const { data: fieldRegistryData, error: fieldRegistryErr } = await supabase.from('pipeline_field_registry').select('*');
    if (fieldRegistryErr) {
        console.error("Error fetching pipeline_field_registry:", fieldRegistryErr);
    } else if (fieldRegistryData && fieldRegistryData.length > 0) {
        console.log(`Found ${fieldRegistryData.length} records in pipeline_field_registry. Migrating...`);
        db.prepare(`DROP TABLE IF EXISTS pipeline_field_registry`).run();
        
        // Ensure table is created properly with TEXT id before inserting
        db.prepare(`
          CREATE TABLE IF NOT EXISTS pipeline_field_registry (
            id TEXT PRIMARY KEY,
            field_name TEXT,
            phase_id INTEGER,
            display_label TEXT,
            data_type TEXT,
            sort_order INTEGER,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP
          )
        `).run();
        
        const insertFieldRegistry = db.prepare(`
            INSERT INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, created_at) 
            VALUES (@id, @field_name, @phase_id, @display_label, @data_type, @sort_order, @created_at)
            ON CONFLICT(id) DO NOTHING
        `);
        
        db.transaction(() => {
            for (const item of fieldRegistryData) {
                insertFieldRegistry.run({
                    id: item.id,
                    field_name: item.field_name,
                    phase_id: item.phase_id,
                    display_label: item.display_label,
                    data_type: item.data_type,
                    sort_order: item.sort_order,
                    created_at: item.created_at || new Date().toISOString()
                });
            }
        })();
        console.log(`✅ Migrated ${fieldRegistryData.length} records into pipeline_field_registry.`);
    } else {
        console.log(`ℹ️ No records found in pipeline_field_registry.`);
    }

    console.log("🎉 Migration completed successfully.");

  } catch (e) {
    console.error("❌ Fatal error during migration:", e);
  }
}

migrateData();
