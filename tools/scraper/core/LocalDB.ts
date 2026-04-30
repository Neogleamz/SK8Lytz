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
    
    -- We can store the entire row payload here just in case we miss a column
    raw_data TEXT -- JSON
  );

  CREATE INDEX IF NOT EXISTS idx_state ON local_spots(state);
  CREATE INDEX IF NOT EXISTS idx_verification_status ON local_spots(verification_status);
  CREATE INDEX IF NOT EXISTS idx_last_attempted_at ON local_spots(last_attempted_at);
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
  obj.raw_data = safeJsonParse(obj.raw_data);

  return obj;
};

/**
 * Upserts a spot into the local DB.
 */
export const upsertLocalSpot = (spot: any) => {
  const is_published = spot.is_published ? 1 : 0;
  const has_adult_night = spot.has_adult_night ? 1 : 0;
  const has_pro_shop = spot.has_pro_shop || spot.has_proshop ? 1 : 0;
  const is_deep_crawled = spot.is_deep_crawled ? 1 : 0;

  const stmt = db.prepare(`
    INSERT INTO local_spots (
      id, name, lat, lng, city, state, zip, street_address, phone_number, website,
      google_place_id, google_maps_url, business_status, rating, user_ratings_total,
      opening_hours, operator_description, facility_type, is_published, verification_status,
      has_adult_night, has_pro_shop, is_deep_crawled, raw_knowledge_panel, photos,
      candidate_photos, candidate_links, ai_metadata, last_attempted_at, last_enriched_at,
      retry_count, raw_data
    ) VALUES (
      @id, @name, @lat, @lng, @city, @state, @zip, @street_address, @phone_number, @website,
      @google_place_id, @google_maps_url, @business_status, @rating, @user_ratings_total,
      @opening_hours, @operator_description, @facility_type, @is_published, @verification_status,
      @has_adult_night, @has_pro_shop, @is_deep_crawled, @raw_knowledge_panel, @photos,
      @candidate_photos, @candidate_links, @ai_metadata, @last_attempted_at, @last_enriched_at,
      @retry_count, @raw_data
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
      raw_data = excluded.raw_data
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
    verification_status: spot.verification_status || null,
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
    raw_data: safeJsonStringify(spot)
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
  db.prepare(`DELETE FROM local_spots WHERE id = ?`).run(id);
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

  if (queryArgs.state) {
    query += ` AND state = ?`;
    params.push(queryArgs.state.toUpperCase());
  }

  if (queryArgs.has_photos) query += ` AND photos IS NOT NULL`;
  if (queryArgs.has_hours) query += ` AND opening_hours IS NOT NULL`;
  if (queryArgs.has_website) query += ` AND website IS NOT NULL`;
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

  if (queryArgs.state) {
    query += ` AND state = ?`;
    params.push(queryArgs.state.toUpperCase());
  }

  if (queryArgs.has_photos === 'true') query += ` AND photos IS NOT NULL`;
  if (queryArgs.has_hours === 'true') query += ` AND opening_hours IS NOT NULL`;
  if (queryArgs.has_website === 'true') query += ` AND website IS NOT NULL`;
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
