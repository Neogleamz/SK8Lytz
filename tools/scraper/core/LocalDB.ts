import Database from 'better-sqlite3';
import path from 'path';
import fs from 'fs';

const DB_DIR = path.resolve(__dirname, '../../.scraper-data');
if (!fs.existsSync(DB_DIR)) {
  fs.mkdirSync(DB_DIR, { recursive: true });
}

const DB_PATH = path.join(DB_DIR, 'scraper.db');
export const db = new Database(DB_PATH, { verbose: process.env.SQLITE_VERBOSE === 'true' ? console.log : undefined });

// Configure for performance
db.pragma('journal_mode = WAL');
db.pragma('synchronous = NORMAL');
db.pragma('temp_store = MEMORY');

// Initialize schema
db.exec(`
  CREATE TABLE IF NOT EXISTS local_spots (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    lat REAL,
    lng REAL,
    city TEXT,
    state TEXT,
    zip TEXT,
    street_address TEXT,
    phone_number TEXT,
    website TEXT,
    google_place_id TEXT UNIQUE,
    google_maps_url TEXT,
    business_status TEXT,
    rating REAL,
    user_ratings_total INTEGER,
    opening_hours TEXT, -- JSON
    operator_description TEXT,
    facility_type TEXT,
    is_published INTEGER DEFAULT 0,
    verification_status TEXT,
    has_adult_night INTEGER DEFAULT 0,
    has_pro_shop INTEGER DEFAULT 0,
    is_deep_crawled INTEGER DEFAULT 0,
    raw_knowledge_panel TEXT, -- JSON
    photos TEXT, -- JSON
    candidate_photos TEXT, -- JSON
    candidate_links TEXT, -- JSON
    ai_metadata TEXT, -- JSON
    last_attempted_at TEXT,
    last_enriched_at TEXT,
    retry_count INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    surface_type TEXT,
    is_indoor INTEGER DEFAULT 0,
    adult_night_details TEXT,
    source TEXT,
    is_verified INTEGER DEFAULT 0,
    updated_at TEXT,
    updated_by TEXT,
    is_featured INTEGER DEFAULT 0,
    has_lights INTEGER,
    has_fee INTEGER,
    operator_name TEXT,
    has_rental INTEGER,
    is_wheelchair_accessible INTEGER,
    has_wifi INTEGER,
    has_toilets INTEGER,
    has_food INTEGER,
    has_ac INTEGER,
    has_lockers INTEGER,
    capacity INTEGER,
    hosts_derby INTEGER DEFAULT 0,
    surface_quality TEXT,
    vibe_score REAL,
    cultural_metadata TEXT, -- JSON
    instagram_url TEXT,
    facebook_url TEXT,
    tiktok_url TEXT,
    schedule_url TEXT,
    pricing_data TEXT, -- JSON
    special_events TEXT, -- JSON
    adult_night_schedule TEXT, -- JSON,
    
    -- Photo Categories
    facade_exterior TEXT,
    skate_floor TEXT,
    arcade_zone TEXT,
    snack_bar TEXT,
    interior TEXT,
    food TEXT,
    
    -- We can store the entire row payload here just in case we miss a column
    raw_data TEXT -- JSON
  );

  CREATE INDEX IF NOT EXISTS idx_state ON local_spots(state);
  CREATE INDEX IF NOT EXISTS idx_verification_status ON local_spots(verification_status);
  CREATE INDEX IF NOT EXISTS idx_last_attempted_at ON local_spots(last_attempted_at);

  CREATE TABLE IF NOT EXISTS scraper_config (
    id INTEGER PRIMARY KEY,
    config_json TEXT,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
  );

  CREATE TABLE IF NOT EXISTS scraper_blocklist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pattern TEXT,
    match_type TEXT,
    reason TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
  );

  CREATE TABLE IF NOT EXISTS scraper_blocklist_keywords (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    keyword TEXT UNIQUE,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
  );

  CREATE TABLE IF NOT EXISTS pipeline_field_registry (
    id TEXT PRIMARY KEY,
    field_name TEXT,
    phase_id INTEGER,
    display_label TEXT,
    data_type TEXT,
    sort_order INTEGER,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
  );
`);

// Check for sync_required column (migration)
const colCheck = db.prepare("PRAGMA table_info(local_spots)").all() as any[];
if (!colCheck.find(c => c.name === 'sync_required')) {
  db.exec(`ALTER TABLE local_spots ADD COLUMN sync_required INTEGER DEFAULT 1`);
}

// Check for importance_level column in field registry (migration)
const fieldRegCheck = db.prepare("PRAGMA table_info(pipeline_field_registry)").all() as any[];
if (!fieldRegCheck.find(c => c.name === 'importance_level')) {
  db.exec(`ALTER TABLE pipeline_field_registry ADD COLUMN importance_level INTEGER DEFAULT 0`);
  // Seed initial importance levels
  db.exec(`
    UPDATE pipeline_field_registry SET importance_level = 2 WHERE field_name IN ('name', 'address', 'lat', 'lng');
    UPDATE pipeline_field_registry SET importance_level = 1 WHERE field_name IN ('phone', 'website');
  `);
}

// Migrate field registry phase_ids from old 5-phase to new 4-phase numbering (ONE-TIME)
// Old: 1=Scout, 2=Spider(dead), 3=Detective, 4=Photographer, 5=Publisher
// New: 1=Scout, 2=Detective, 3=Photographer, 4=Publisher
// ONLY run when the old schema marker (phase_id=5) still exists — prevents clobbering new Phase 3/4 seeds.
const hasOldPhase5 = (db.prepare('SELECT COUNT(*) as cnt FROM pipeline_field_registry WHERE phase_id = 5').get() as any)?.cnt > 0;
if (hasOldPhase5) {
  db.prepare('UPDATE pipeline_field_registry SET phase_id = 4 WHERE phase_id = 5').run();
  db.prepare('UPDATE pipeline_field_registry SET phase_id = 3 WHERE phase_id = 4 AND id NOT LIKE \'p4_%\'').run();
  db.prepare('UPDATE pipeline_field_registry SET phase_id = 2 WHERE phase_id = 3 AND id NOT LIKE \'p3_%\'').run();
}

// ── Auto-seed missing field registry entries (ALL phases) ────────────────
// ON CONFLICT DO NOTHING preserves user-set importance_level.
const FIELD_SEEDS: { id: string; field_name: string; phase_id: number; display_label: string; data_type: string; sort_order: number }[] = [
  // ── Phase 1: Scout ──
  { id: 'candidate_links',   field_name: 'candidate_links',   phase_id: 1, display_label: 'Candidate Links',     data_type: 'jsonb',    sort_order: 125 },

  // ── Phase 2: Detective ──
  { id: 'has_fee',            field_name: 'has_fee',            phase_id: 2, display_label: 'Has Fee',              data_type: 'boolean',  sort_order: 231 },
  { id: 'has_lights',         field_name: 'has_lights',         phase_id: 2, display_label: 'Has Lights',           data_type: 'boolean',  sort_order: 232 },
  { id: 'has_wifi',           field_name: 'has_wifi',           phase_id: 2, display_label: 'Has WiFi',             data_type: 'boolean',  sort_order: 233 },
  { id: 'has_toilets',        field_name: 'has_toilets',        phase_id: 2, display_label: 'Has Toilets',          data_type: 'boolean',  sort_order: 234 },
  { id: 'surface_quality',    field_name: 'surface_quality',    phase_id: 2, display_label: 'Surface Quality',      data_type: 'text',     sort_order: 235 },
  { id: 'vibe_score',         field_name: 'vibe_score',         phase_id: 2, display_label: 'Vibe Score',           data_type: 'float',    sort_order: 236 },
  { id: 'capacity',           field_name: 'capacity',           phase_id: 2, display_label: 'Capacity',             data_type: 'integer',  sort_order: 237 },
  { id: 'schedule_url',       field_name: 'schedule_url',       phase_id: 2, display_label: 'Schedule URL',         data_type: 'text',     sort_order: 238 },
  { id: 'is_indoor',          field_name: 'is_indoor',          phase_id: 2, display_label: 'Is Indoor',            data_type: 'boolean',  sort_order: 170 },
  { id: 'operator_name',      field_name: 'operator_name',      phase_id: 2, display_label: 'Operator Name',        data_type: 'text',     sort_order: 360 },
  { id: 'opening_hours',      field_name: 'opening_hours',      phase_id: 2, display_label: 'Opening Hours',        data_type: 'json',     sort_order: 361 },
  { id: 'operator_description',field_name: 'operator_description',phase_id: 2, display_label: 'Operator Description', data_type: 'text',     sort_order: 362 },
  { id: 'facility_type',      field_name: 'facility_type',      phase_id: 2, display_label: 'Facility Type',        data_type: 'text',     sort_order: 363 },
  { id: 'has_adult_night',    field_name: 'has_adult_night',    phase_id: 2, display_label: 'Has Adult Night',      data_type: 'boolean',  sort_order: 364 },
  { id: 'has_pro_shop',       field_name: 'has_pro_shop',       phase_id: 2, display_label: 'Has Pro Shop',         data_type: 'boolean',  sort_order: 365 },
  { id: 'surface_type',       field_name: 'surface_type',       phase_id: 2, display_label: 'Surface Type',         data_type: 'text',     sort_order: 366 },
  { id: 'adult_night_details',field_name: 'adult_night_details',phase_id: 2, display_label: 'Adult Night Details',  data_type: 'text',     sort_order: 367 },
  { id: 'ai_metadata',        field_name: 'ai_metadata',        phase_id: 2, display_label: 'AI Metadata',          data_type: 'json',     sort_order: 368 },

  // ── Phase 3: Photographer ──
  { id: 'p3_candidate_photos', field_name: 'candidate_photos', phase_id: 3, display_label: 'Candidate Photos',     data_type: 'jsonb',    sort_order: 700 },
  { id: 'p3_photos',           field_name: 'photos',           phase_id: 3, display_label: 'Curated Photos',       data_type: 'jsonb',    sort_order: 710 },
  { id: 'p3_og_image',         field_name: 'og_image',         phase_id: 3, display_label: 'OG Image',             data_type: 'text',     sort_order: 720 },
  { id: 'p3_street_view',      field_name: 'street_view_url',  phase_id: 3, display_label: 'Street View URL',      data_type: 'text',     sort_order: 730 },
  { id: 'p3_dom_images',       field_name: 'dom_images',       phase_id: 3, display_label: 'DOM Images',           data_type: 'jsonb',    sort_order: 740 },
  { id: 'p3_flyer_urls',       field_name: 'flyer_urls',       phase_id: 3, display_label: 'Flyer URLs',           data_type: 'jsonb',    sort_order: 750 },
  { id: 'p3_photo_count',      field_name: 'photo_count',      phase_id: 3, display_label: 'Photo Count',          data_type: 'integer',  sort_order: 760 },
  { id: 'p3_photo_quality',    field_name: 'photo_quality',    phase_id: 3, display_label: 'Photo Quality Score',  data_type: 'float',    sort_order: 770 },

  // ── Phase 4: Publisher ──
  { id: 'p4_is_published',     field_name: 'is_published',     phase_id: 4, display_label: 'Is Published',         data_type: 'boolean',  sort_order: 800 },
  { id: 'p4_verification',     field_name: 'verification_status', phase_id: 4, display_label: 'Verification Status', data_type: 'text',  sort_order: 810 },
  { id: 'p4_sync_required',    field_name: 'sync_required',    phase_id: 4, display_label: 'Sync Required',        data_type: 'boolean',  sort_order: 820 },
  { id: 'p4_last_enriched',    field_name: 'last_enriched_at', phase_id: 4, display_label: 'Last Enriched',        data_type: 'text',     sort_order: 830 },
  { id: 'p4_publish_score',    field_name: 'publish_score',    phase_id: 4, display_label: 'Publish Score',        data_type: 'float',    sort_order: 840 },
  { id: 'p4_supabase_id',      field_name: 'supabase_id',      phase_id: 4, display_label: 'Supabase ID',          data_type: 'text',     sort_order: 850 },
  { id: 'p4_last_synced',      field_name: 'last_synced_at',   phase_id: 4, display_label: 'Last Synced At',       data_type: 'text',     sort_order: 860 },
];
const seedStmt = db.prepare(`
  INSERT INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, importance_level)
  VALUES (@id, @field_name, @phase_id, @display_label, @data_type, @sort_order, 0)
  ON CONFLICT(id) DO UPDATE SET
    phase_id = excluded.phase_id,
    display_label = excluded.display_label,
    data_type = excluded.data_type,
    sort_order = excluded.sort_order
`);
for (const seed of FIELD_SEEDS) {
  seedStmt.run(seed);
}

db.exec(`
  CREATE TRIGGER IF NOT EXISTS set_sync_required
  AFTER UPDATE ON local_spots
  FOR EACH ROW
  WHEN NEW.sync_required = OLD.sync_required OR OLD.sync_required IS NULL
  BEGIN
      UPDATE local_spots SET sync_required = 1 WHERE id = NEW.id;
  END;
  
  CREATE INDEX IF NOT EXISTS idx_sync_required ON local_spots(sync_required);
`);

/**
 * Safely parse a JSON string, or stringify an object before saving
 */
const safeJsonStringify = (val: any) => {
  if (val === null || val === undefined) return null;
  if (typeof val === 'string') return val;
  return JSON.stringify(val);
};

const safeJsonParse = (val: any) => {
  if (typeof val === 'string') {
    try { return JSON.parse(val); } catch (e) { return val; }
  }
  return val;
};

/**
 * Converts a database row to a JS object
 */
const rowToObj = (row: any) => {
  if (!row) return null;
  const obj = { ...row };
  
  // Convert 1/0 back to true/false
  obj.is_published = obj.is_published === 1;
  obj.has_adult_night = obj.has_adult_night === 1;
  obj.has_pro_shop = obj.has_pro_shop === 1;
  obj.is_deep_crawled = obj.is_deep_crawled === 1;

  // Parse JSON fields
  obj.opening_hours = safeJsonParse(obj.opening_hours);
  obj.raw_knowledge_panel = safeJsonParse(obj.raw_knowledge_panel);
  obj.photos = safeJsonParse(obj.photos);
  obj.candidate_photos = safeJsonParse(obj.candidate_photos);
  obj.candidate_links = safeJsonParse(obj.candidate_links);
  obj.ai_metadata = safeJsonParse(obj.ai_metadata);
  obj.cultural_metadata = safeJsonParse(obj.cultural_metadata);
  obj.pricing_data = safeJsonParse(obj.pricing_data);
  obj.special_events = safeJsonParse(obj.special_events);
  obj.adult_night_schedule = safeJsonParse(obj.adult_night_schedule);
  obj.raw_data = safeJsonParse(obj.raw_data);

  // Convert additional booleans
  obj.is_indoor = obj.is_indoor === 1;
  obj.is_verified = obj.is_verified === 1;
  obj.is_featured = obj.is_featured === 1;
  obj.hosts_derby = obj.hosts_derby === 1;
  obj.has_lights = obj.has_lights === 1 ? true : (obj.has_lights === 0 ? false : null);
  obj.has_fee = obj.has_fee === 1 ? true : (obj.has_fee === 0 ? false : null);
  obj.has_rental = obj.has_rental === 1 ? true : (obj.has_rental === 0 ? false : null);
  obj.is_wheelchair_accessible = obj.is_wheelchair_accessible === 1 ? true : (obj.is_wheelchair_accessible === 0 ? false : null);
  obj.has_wifi = obj.has_wifi === 1 ? true : (obj.has_wifi === 0 ? false : null);
  obj.has_toilets = obj.has_toilets === 1 ? true : (obj.has_toilets === 0 ? false : null);
  obj.has_food = obj.has_food === 1 ? true : (obj.has_food === 0 ? false : null);
  obj.has_ac = obj.has_ac === 1 ? true : (obj.has_ac === 0 ? false : null);
  obj.has_lockers = obj.has_lockers === 1 ? true : (obj.has_lockers === 0 ? false : null);
  obj.raw_data = safeJsonParse(obj.raw_data);

  return obj;
};

/**
 * Upserts a spot into the local DB.
 */
export const upsertLocalSpot = (spot: any) => {
  let is_published = spot.is_published ? 1 : 0;
  const has_adult_night = spot.has_adult_night ? 1 : 0;
  const has_pro_shop = spot.has_pro_shop || spot.has_proshop ? 1 : 0;
  const is_deep_crawled = spot.is_deep_crawled ? 1 : 0;
  let verification_status = spot.verification_status || null;

  if (spot.name) {
    const spotName = spot.name.toLowerCase();
    const keywords = db.prepare('SELECT keyword FROM scraper_blocklist_keywords').all() as any[];
    const bl = db.prepare('SELECT pattern FROM scraper_blocklist').all() as any[];
    
    const isBlocked = keywords.some(k => spotName.includes(k.keyword.toLowerCase())) || 
                      bl.some(b => spotName.includes(b.pattern.toLowerCase()));
                      
    if (isBlocked) {
      verification_status = 'REJECTED';
      is_published = 0;
    }
  }

  const stmt = db.prepare(`
    INSERT INTO local_spots (
      id, name, lat, lng, city, state, zip, street_address, phone_number, website,
      google_place_id, google_maps_url, business_status, rating, user_ratings_total,
      opening_hours, operator_description, facility_type, is_published, verification_status,
      has_adult_night, has_pro_shop, is_deep_crawled, raw_knowledge_panel, photos,
      candidate_photos, candidate_links, ai_metadata, last_attempted_at, last_enriched_at,
      last_enriched_at, retry_count, raw_data,
      surface_type, is_indoor, adult_night_details, source, is_verified, updated_at,
      updated_by, is_featured, has_lights,
      has_fee, operator_name, has_rental, is_wheelchair_accessible, has_wifi,
      has_toilets, has_food, has_ac, has_lockers, capacity, hosts_derby, surface_quality,
      vibe_score, cultural_metadata, instagram_url, facebook_url, tiktok_url, schedule_url,
      pricing_data, special_events, adult_night_schedule
    ) VALUES (
      @id, @name, @lat, @lng, @city, @state, @zip, @street_address, @phone_number, @website,
      @google_place_id, @google_maps_url, @business_status, @rating, @user_ratings_total,
      @opening_hours, @operator_description, @facility_type, @is_published, @verification_status,
      @has_adult_night, @has_pro_shop, @is_deep_crawled, @raw_knowledge_panel, @photos,
      @candidate_photos, @candidate_links, @ai_metadata, @last_attempted_at, @last_enriched_at,
      @retry_count, @raw_data,
      @surface_type, @is_indoor, @adult_night_details, @source, @is_verified, @updated_at,
      @updated_by, @is_featured, @has_lights,
      @has_fee, @operator_name, @has_rental, @is_wheelchair_accessible, @has_wifi,
      @has_toilets, @has_food, @has_ac, @has_lockers, @capacity, @hosts_derby, @surface_quality,
      @vibe_score, @cultural_metadata, @instagram_url, @facebook_url, @tiktok_url, @schedule_url,
      @pricing_data, @special_events, @adult_night_schedule
    )
    ON CONFLICT(id) DO UPDATE SET
      name = excluded.name,
      lat = excluded.lat,
      lng = excluded.lng,
      city = excluded.city,
      state = excluded.state,
      zip = excluded.zip,
      street_address = excluded.street_address,
      phone_number = excluded.phone_number,
      website = excluded.website,
      google_place_id = excluded.google_place_id,
      google_maps_url = excluded.google_maps_url,
      business_status = excluded.business_status,
      rating = excluded.rating,
      user_ratings_total = excluded.user_ratings_total,
      opening_hours = excluded.opening_hours,
      operator_description = excluded.operator_description,
      facility_type = excluded.facility_type,
      is_published = excluded.is_published,
      verification_status = excluded.verification_status,
      has_adult_night = excluded.has_adult_night,
      has_pro_shop = excluded.has_pro_shop,
      is_deep_crawled = excluded.is_deep_crawled,
      raw_knowledge_panel = excluded.raw_knowledge_panel,
      photos = excluded.photos,
      candidate_photos = excluded.candidate_photos,
      candidate_links = excluded.candidate_links,
      ai_metadata = excluded.ai_metadata,
      last_attempted_at = excluded.last_attempted_at,
      last_enriched_at = excluded.last_enriched_at,
      retry_count = excluded.retry_count,
      raw_data = excluded.raw_data,
      surface_type = excluded.surface_type,
      is_indoor = excluded.is_indoor,
      adult_night_details = excluded.adult_night_details,
      source = excluded.source,
      is_verified = excluded.is_verified,
      updated_at = excluded.updated_at,
      updated_by = excluded.updated_by,
      is_featured = excluded.is_featured,
      has_lights = excluded.has_lights,
      has_fee = excluded.has_fee,
      operator_name = excluded.operator_name,
      has_rental = excluded.has_rental,
      is_wheelchair_accessible = excluded.is_wheelchair_accessible,
      has_wifi = excluded.has_wifi,
      has_toilets = excluded.has_toilets,
      has_food = excluded.has_food,
      has_ac = excluded.has_ac,
      has_lockers = excluded.has_lockers,
      capacity = excluded.capacity,
      hosts_derby = excluded.hosts_derby,
      surface_quality = excluded.surface_quality,
      vibe_score = excluded.vibe_score,
      cultural_metadata = excluded.cultural_metadata,
      instagram_url = excluded.instagram_url,
      facebook_url = excluded.facebook_url,
      tiktok_url = excluded.tiktok_url,
      schedule_url = excluded.schedule_url,
      pricing_data = excluded.pricing_data,
      special_events = excluded.special_events,
      adult_night_schedule = excluded.adult_night_schedule
  `);

  const id = spot.id || Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);

  stmt.run({
    id,
    name: spot.name,
    lat: spot.lat || null,
    lng: spot.lng || null,
    city: spot.city || null,
    state: spot.state || null,
    zip: spot.zip || null,
    street_address: spot.street_address || null,
    phone_number: spot.phone_number || null,
    website: spot.website || null,
    google_place_id: spot.google_place_id || null,
    google_maps_url: spot.google_maps_url || null,
    business_status: spot.business_status || null,
    rating: spot.rating || null,
    user_ratings_total: spot.user_ratings_total || null,
    opening_hours: safeJsonStringify(spot.opening_hours),
    operator_description: spot.operator_description || null,
    facility_type: spot.facility_type || null,
    is_published,
    verification_status,
    has_adult_night,
    has_pro_shop,
    is_deep_crawled,
    raw_knowledge_panel: safeJsonStringify(spot.raw_knowledge_panel),
    photos: safeJsonStringify(spot.photos),
    candidate_photos: safeJsonStringify(spot.candidate_photos),
    candidate_links: safeJsonStringify(spot.candidate_links),
    ai_metadata: safeJsonStringify(spot.ai_metadata),
    last_attempted_at: spot.last_attempted_at || null,
    last_enriched_at: spot.last_enriched_at || null,
    retry_count: spot.retry_count || 0,
    raw_data: safeJsonStringify(spot),
    surface_type: spot.surface_type || null,
    is_indoor: spot.is_indoor ? 1 : 0,
    adult_night_details: spot.adult_night_details || null,
    source: spot.source || null,
    is_verified: spot.is_verified ? 1 : 0,
    updated_at: spot.updated_at || null,
    updated_by: spot.updated_by || null,
    address: spot.address || null,
    phone: spot.phone || null,
    vibe_rating: spot.vibe_rating || null,
    socials: safeJsonStringify(spot.socials),
    is_featured: spot.is_featured ? 1 : 0,
    has_lights: spot.has_lights === true ? 1 : (spot.has_lights === false ? 0 : null),
    has_fee: spot.has_fee === true ? 1 : (spot.has_fee === false ? 0 : null),
    operator_name: spot.operator_name || null,
    has_rental: spot.has_rental === true ? 1 : (spot.has_rental === false ? 0 : null),
    is_wheelchair_accessible: spot.is_wheelchair_accessible === true ? 1 : (spot.is_wheelchair_accessible === false ? 0 : null),
    has_wifi: spot.has_wifi === true ? 1 : (spot.has_wifi === false ? 0 : null),
    has_toilets: spot.has_toilets === true ? 1 : (spot.has_toilets === false ? 0 : null),
    has_food: spot.has_food === true ? 1 : (spot.has_food === false ? 0 : null),
    has_ac: spot.has_ac === true ? 1 : (spot.has_ac === false ? 0 : null),
    has_lockers: spot.has_lockers === true ? 1 : (spot.has_lockers === false ? 0 : null),
    capacity: spot.capacity || null,
    hosts_derby: spot.hosts_derby ? 1 : 0,
    surface_quality: spot.surface_quality || null,
    vibe_score: spot.vibe_score || null,
    cultural_metadata: safeJsonStringify(spot.cultural_metadata),
    instagram_url: spot.instagram_url || null,
    facebook_url: spot.facebook_url || null,
    tiktok_url: spot.tiktok_url || null,
    schedule_url: spot.schedule_url || null,
    pricing_data: safeJsonStringify(spot.pricing_data),
    special_events: safeJsonStringify(spot.special_events),
    adult_night_schedule: safeJsonStringify(spot.adult_night_schedule)
  });

  return id;
};

export const updateLocalSpot = (id: string, updates: any) => {
  const keys = Object.keys(updates);
  if (keys.length === 0) return;

  const setClauses = [];
  const params: any = { id };

  for (const k of keys) {
    if (k === 'id') continue;
    let val = updates[k];
    
    // Normalize booleans
    if (typeof val === 'boolean') val = val ? 1 : 0;
    // Serialize objects
    if (val !== null && typeof val === 'object') val = JSON.stringify(val);
    
    // map has_proshop to has_pro_shop just in case
    const colName = k === 'has_proshop' ? 'has_pro_shop' : k;

    setClauses.push(`${colName} = @${colName}`);
    params[colName] = val;
  }

  const stmt = db.prepare(`UPDATE local_spots SET ${setClauses.join(', ')} WHERE id = @id`);
  stmt.run(params);
};

export const getLocalSpot = (id: string) => {
  const row = db.prepare(`SELECT * FROM local_spots WHERE id = ?`).get(id);
  return rowToObj(row);
};

export const deleteLocalSpot = (id: string) => {
  db.prepare(`UPDATE local_spots SET verification_status = 'REJECTED', is_published = 0 WHERE id = ?`).run(id);
};

export const getLocalSpots = (queryArgs: any = {}) => {
  let query = 'SELECT * FROM local_spots WHERE 1=1';
  const params: any[] = [];

  if (queryArgs.status && queryArgs.status !== 'ALL') {
    if (queryArgs.status === 'UNVERIFIED' || queryArgs.status === 'PENDING') {
      query += ` AND (verification_status = 'PENDING' OR verification_status IS NULL)`;
    } else {
      query += ` AND verification_status = ?`;
      params.push(queryArgs.status);
    }
  }

  if (queryArgs.search) {
    query += ` AND (name LIKE ? OR city LIKE ? OR state LIKE ?)`;
    params.push(`%${queryArgs.search}%`, `%${queryArgs.search}%`, `%${queryArgs.search}%`);
  }

  if (queryArgs.pipeline_status) {
    query += ` AND pipeline_status = ?`;
    params.push(queryArgs.pipeline_status);
  }

  if (queryArgs.state) {
    query += ` AND state = ?`;
    params.push(queryArgs.state.toUpperCase());
  }

  if (queryArgs.has_photos) query += ` AND photos IS NOT NULL AND photos != '[]' AND photos != 'null'`;
  if (queryArgs.has_hours) query += ` AND opening_hours IS NOT NULL AND opening_hours != '{}' AND opening_hours != 'null'`;
  if (queryArgs.has_website) query += ` AND website IS NOT NULL AND website != ''`;
  if (queryArgs.has_adult_night) query += ` AND has_adult_night = 1`;
  if (queryArgs.has_pro_shop) query += ` AND has_pro_shop = 1`;
  if (queryArgs.is_published === true) query += ` AND is_published = 1`;
  if (queryArgs.is_published === false) query += ` AND (is_published = 0 OR is_published IS NULL)`;
  if (queryArgs.is_deep_crawled) query += ` AND is_deep_crawled = 1`;

  const ALLOWED_SORT = ['last_attempted_at', 'last_enriched_at', 'name', 'rating', 'user_ratings_total', 'state', 'city', 'verification_status', 'created_at'];
  const safeSort = ALLOWED_SORT.includes(queryArgs.sortCol) ? queryArgs.sortCol : 'last_attempted_at';
  const dir = queryArgs.sortDir === 'asc' ? 'ASC' : 'DESC';

  query += ` ORDER BY ${safeSort} ${dir} NULLS LAST`;

  if (queryArgs.limit) {
    query += ` LIMIT ? OFFSET ?`;
    params.push(Number(queryArgs.limit), Number(queryArgs.offset || 0));
  }

  const rows = db.prepare(query).all(...params);
  return rows.map(rowToObj);
};

export const getLocalCount = (queryArgs: any = {}) => {
  let query = 'SELECT COUNT(*) as cnt FROM local_spots WHERE 1=1';
  const params: any[] = [];

  if (queryArgs.status && queryArgs.status !== 'ALL') {
    if (queryArgs.status === 'UNVERIFIED' || queryArgs.status === 'PENDING') {
      query += ` AND (verification_status = 'PENDING' OR verification_status IS NULL)`;
    } else {
      query += ` AND verification_status = ?`;
      params.push(queryArgs.status);
    }
  }

  if (queryArgs.search) {
    query += ` AND (name LIKE ? OR city LIKE ? OR state LIKE ?)`;
    params.push(`%${queryArgs.search}%`, `%${queryArgs.search}%`, `%${queryArgs.search}%`);
  }

  if (queryArgs.pipeline_status) {
    query += ` AND pipeline_status = ?`;
    params.push(queryArgs.pipeline_status);
  }

  if (queryArgs.state) {
    query += ` AND state = ?`;
    params.push(queryArgs.state.toUpperCase());
  }

  if (queryArgs.has_photos === 'true') query += ` AND photos IS NOT NULL AND photos != '[]' AND photos != 'null'`;
  if (queryArgs.has_hours === 'true') query += ` AND opening_hours IS NOT NULL AND opening_hours != '{}' AND opening_hours != 'null'`;
  if (queryArgs.has_website === 'true') query += ` AND website IS NOT NULL AND website != ''`;
  if (queryArgs.has_adult_night === 'true') query += ` AND has_adult_night = 1`;
  if (queryArgs.has_pro_shop === 'true') query += ` AND has_pro_shop = 1`;
  if (queryArgs.is_published === 'true' || queryArgs.is_published === true) query += ` AND is_published = 1`;
  if (queryArgs.is_published === 'false' || queryArgs.is_published === false) query += ` AND (is_published = 0 OR is_published IS NULL)`;
  if (queryArgs.is_deep_crawled === 'true') query += ` AND is_deep_crawled = 1`;

  const res: any = db.prepare(query).get(...params);
  return res.cnt;
};

// Haversine distance in meters
const getDistanceFromLatLonInM = (lat1: number, lon1: number, lat2: number, lon2: number) => {
  const R = 6371e3; // Radius of the earth in m
  const dLat = (lat2 - lat1) * (Math.PI / 180);  
  const dLon = (lon2 - lon1) * (Math.PI / 180); 
  const a = 
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) * 
    Math.sin(dLon / 2) * Math.sin(dLon / 2); 
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); 
  return R * c; // Distance in m
};

export const getClosestLocalSpot = (lat: number, lng: number, radiusMeters: number) => {
  // Approx 1 degree = 111,000 meters
  const delta = radiusMeters / 111000;
  
  // Bounding box query
  const candidates = db.prepare(`
    SELECT id, name, lat, lng FROM local_spots 
    WHERE lat BETWEEN ? AND ? AND lng BETWEEN ? AND ?
  `).all(lat - delta, lat + delta, lng - delta, lng + delta) as any[];

  let closestSpot: any = null;
  let minDistance = Infinity;

  for (const c of candidates) {
    if (!c.lat || !c.lng) continue;
    const dist = getDistanceFromLatLonInM(lat, lng, c.lat, c.lng);
    if (dist <= radiusMeters && dist < minDistance) {
      minDistance = dist;
      closestSpot = { spot_id: c.id, distance_meters: dist, name: c.name };
    }
  }

  return closestSpot ? [closestSpot] : null;
};

export const markSpotSynced = (id: string) => {
  db.prepare(`UPDATE local_spots SET sync_required = 0 WHERE id = ?`).run(id);
};

export const getSpotsToSync = (limit: number = 50) => {
  const rows = db.prepare(`SELECT * FROM local_spots WHERE sync_required = 1 LIMIT ?`).all(limit);
  return rows.map(rowToObj);
};

// --- CONFIG METHODS ---
export const getConfig = () => {
  let row = db.prepare(`SELECT config_json FROM scraper_config WHERE id = 1`).get() as any;
  if (!row) {
    db.prepare(`INSERT INTO scraper_config (id, config_json) VALUES (1, '{}')`).run();
    row = db.prepare(`SELECT config_json FROM scraper_config WHERE id = 1`).get() as any;
  }
  return row && row.config_json ? JSON.parse(row.config_json) : {};
};

export const updateConfig = (payload: any) => {
  const existing = getConfig();
  const newConfig = { ...existing, ...payload };
  db.prepare(`UPDATE scraper_config SET config_json = ?, updated_at = CURRENT_TIMESTAMP WHERE id = 1`).run(JSON.stringify(newConfig));
};

// --- BLOCKLIST METHODS ---
export const getBlocklist = () => {
  return db.prepare(`SELECT * FROM scraper_blocklist ORDER BY created_at DESC`).all();
};

export const addBlocklist = (pattern: string, match_type: string = 'name', reason: string = '') => {
  const existing = db.prepare(`SELECT * FROM scraper_blocklist WHERE pattern = ?`).get(pattern);
  if (!existing) {
    const stmt = db.prepare(`INSERT INTO scraper_blocklist (pattern, match_type, reason) VALUES (?, ?, ?)`);
    stmt.run(pattern, match_type, reason);
  }
};

export const deleteBlocklist = (id: number | string) => {
  db.prepare(`DELETE FROM scraper_blocklist WHERE id = ?`).run(id);
};

export const getBlocklistKeywords = () => {
  return db.prepare(`SELECT keyword FROM scraper_blocklist_keywords ORDER BY created_at DESC`).all();
};

export const addBlocklistKeyword = (keyword: string) => {
  db.prepare(`INSERT INTO scraper_blocklist_keywords (keyword) VALUES (?)`).run(keyword);
};

// --- PIPELINE STATS METHODS ---
export const getPipelineStats = (states: string[] = []) => {
  let whereClause = '1=1';
  let params: any[] = [];
  
  if (states.length > 0) {
    whereClause = `state IN (${states.map(() => '?').join(',')})`;
    params = states;
  }

  const query = `
    SELECT 
      COUNT(*) as total,
      SUM(CASE WHEN verification_status = 'SEEDED' THEN 1 ELSE 0 END) as seeded,
      SUM(CASE WHEN verification_status = 'DEEP_CRAWLED' THEN 1 ELSE 0 END) as deep_crawled_count,
      SUM(CASE WHEN verification_status = 'MEDIA_READY' THEN 1 ELSE 0 END) as media_ready,
      SUM(CASE WHEN is_published = 1 THEN 1 ELSE 0 END) as published,
      SUM(CASE WHEN website IS NOT NULL AND website != '' THEN 1 ELSE 0 END) as has_website,
      SUM(CASE WHEN verification_status = 'SEEDED' THEN 1 ELSE 0 END) as detective_queue,
      SUM(CASE WHEN verification_status = 'DEEP_CRAWLED' THEN 1 ELSE 0 END) as photographer_queue,
      SUM(CASE WHEN candidate_photos IS NOT NULL AND candidate_photos != '[]' AND candidate_photos != 'null' AND (photos IS NULL OR photos = '[]' OR photos = 'null') THEN 1 ELSE 0 END) as has_candidates,
      SUM(CASE WHEN verification_status = 'MEDIA_READY' AND is_published = 0 THEN 1 ELSE 0 END) as publisher_queue,
      SUM(CASE WHEN photos IS NOT NULL AND photos != '[]' AND photos != 'null' THEN 1 ELSE 0 END) as has_photos
    FROM local_spots
    WHERE ${whereClause}
  `;
  
  const result = db.prepare(query).get(...params);
  return result;
};

// --- Pipeline Field Registry ---
export function getFieldRegistry() {
  return db.prepare('SELECT * FROM pipeline_field_registry ORDER BY sort_order ASC').all();
}

export function upsertFieldRegistryItem(item: any) {
  const stmt = db.prepare(`
    INSERT INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, importance_level)
    VALUES (@id, @field_name, @phase_id, @display_label, @data_type, @sort_order, COALESCE(@importance_level, 0))
    ON CONFLICT(id) DO UPDATE SET
      field_name=excluded.field_name,
      phase_id=excluded.phase_id,
      display_label=excluded.display_label,
      data_type=excluded.data_type,
      sort_order=excluded.sort_order,
      importance_level=excluded.importance_level
  `);
  stmt.run(item);
}
