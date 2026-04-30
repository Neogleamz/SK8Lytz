"use strict";
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getPipelineStats = exports.addBlocklistKeyword = exports.getBlocklistKeywords = exports.deleteBlocklist = exports.addBlocklist = exports.getBlocklist = exports.updateConfig = exports.getConfig = exports.getSpotsToSync = exports.markSpotSynced = exports.getClosestLocalSpot = exports.getLocalCount = exports.getLocalSpots = exports.deleteLocalSpot = exports.getLocalSpot = exports.updateLocalSpot = exports.upsertLocalSpot = exports.db = void 0;
exports.getFieldRegistry = getFieldRegistry;
exports.upsertFieldRegistryItem = upsertFieldRegistryItem;
var better_sqlite3_1 = __importDefault(require("better-sqlite3"));
var path_1 = __importDefault(require("path"));
var fs_1 = __importDefault(require("fs"));
var DB_DIR = path_1.default.resolve(__dirname, '../../.scraper-data');
if (!fs_1.default.existsSync(DB_DIR)) {
    fs_1.default.mkdirSync(DB_DIR, { recursive: true });
}
var DB_PATH = path_1.default.join(DB_DIR, 'scraper.db');
exports.db = new better_sqlite3_1.default(DB_PATH, { verbose: process.env.SQLITE_VERBOSE === 'true' ? console.log : undefined });
// Configure for performance
exports.db.pragma('journal_mode = WAL');
exports.db.pragma('synchronous = NORMAL');
exports.db.pragma('temp_store = MEMORY');
// Initialize schema
exports.db.exec("\n  CREATE TABLE IF NOT EXISTS local_spots (\n    id TEXT PRIMARY KEY,\n    name TEXT NOT NULL,\n    lat REAL,\n    lng REAL,\n    city TEXT,\n    state TEXT,\n    zip TEXT,\n    street_address TEXT,\n    phone_number TEXT,\n    website TEXT,\n    google_place_id TEXT UNIQUE,\n    google_maps_url TEXT,\n    business_status TEXT,\n    rating REAL,\n    user_ratings_total INTEGER,\n    opening_hours TEXT, -- JSON\n    operator_description TEXT,\n    facility_type TEXT,\n    is_published INTEGER DEFAULT 0,\n    verification_status TEXT,\n    has_adult_night INTEGER DEFAULT 0,\n    has_pro_shop INTEGER DEFAULT 0,\n    is_deep_crawled INTEGER DEFAULT 0,\n    raw_knowledge_panel TEXT, -- JSON\n    photos TEXT, -- JSON\n    candidate_photos TEXT, -- JSON\n    candidate_links TEXT, -- JSON\n    ai_metadata TEXT, -- JSON\n    last_attempted_at TEXT,\n    last_enriched_at TEXT,\n    retry_count INTEGER DEFAULT 0,\n    created_at TEXT DEFAULT CURRENT_TIMESTAMP,\n    \n    -- We can store the entire row payload here just in case we miss a column\n    raw_data TEXT -- JSON\n  );\n\n  CREATE INDEX IF NOT EXISTS idx_state ON local_spots(state);\n  CREATE INDEX IF NOT EXISTS idx_verification_status ON local_spots(verification_status);\n  CREATE INDEX IF NOT EXISTS idx_last_attempted_at ON local_spots(last_attempted_at);\n\n  CREATE TABLE IF NOT EXISTS scraper_config (\n    id INTEGER PRIMARY KEY,\n    config_json TEXT,\n    updated_at TEXT DEFAULT CURRENT_TIMESTAMP\n  );\n\n  CREATE TABLE IF NOT EXISTS scraper_blocklist (\n    id INTEGER PRIMARY KEY AUTOINCREMENT,\n    pattern TEXT,\n    match_type TEXT,\n    reason TEXT,\n    created_at TEXT DEFAULT CURRENT_TIMESTAMP\n  );\n\n  CREATE TABLE IF NOT EXISTS scraper_blocklist_keywords (\n    id INTEGER PRIMARY KEY AUTOINCREMENT,\n    keyword TEXT UNIQUE,\n    created_at TEXT DEFAULT CURRENT_TIMESTAMP\n  );\n\n  CREATE TABLE IF NOT EXISTS pipeline_field_registry (\n    id TEXT PRIMARY KEY,\n    field_name TEXT,\n    phase_id INTEGER,\n    display_label TEXT,\n    data_type TEXT,\n    sort_order INTEGER,\n    created_at TEXT DEFAULT CURRENT_TIMESTAMP\n  );\n");
// Check for sync_required column (migration)
var colCheck = exports.db.prepare("PRAGMA table_info(local_spots)").all();
if (!colCheck.find(function (c) { return c.name === 'sync_required'; })) {
    exports.db.exec("ALTER TABLE local_spots ADD COLUMN sync_required INTEGER DEFAULT 1");
}
exports.db.exec("\n  CREATE TRIGGER IF NOT EXISTS set_sync_required\n  AFTER UPDATE ON local_spots\n  FOR EACH ROW\n  WHEN NEW.sync_required = OLD.sync_required OR OLD.sync_required IS NULL\n  BEGIN\n      UPDATE local_spots SET sync_required = 1 WHERE id = NEW.id;\n  END;\n  \n  CREATE INDEX IF NOT EXISTS idx_sync_required ON local_spots(sync_required);\n");
/**
 * Safely parse a JSON string, or stringify an object before saving
 */
var safeJsonStringify = function (val) {
    if (val === null || val === undefined)
        return null;
    if (typeof val === 'string')
        return val;
    return JSON.stringify(val);
};
var safeJsonParse = function (val) {
    if (typeof val === 'string') {
        try {
            return JSON.parse(val);
        }
        catch (e) {
            return val;
        }
    }
    return val;
};
/**
 * Converts a database row to a JS object
 */
var rowToObj = function (row) {
    if (!row)
        return null;
    var obj = __assign({}, row);
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
var upsertLocalSpot = function (spot) {
    var is_published = spot.is_published ? 1 : 0;
    var has_adult_night = spot.has_adult_night ? 1 : 0;
    var has_pro_shop = spot.has_pro_shop || spot.has_proshop ? 1 : 0;
    var is_deep_crawled = spot.is_deep_crawled ? 1 : 0;
    var stmt = exports.db.prepare("\n    INSERT INTO local_spots (\n      id, name, lat, lng, city, state, zip, street_address, phone_number, website,\n      google_place_id, google_maps_url, business_status, rating, user_ratings_total,\n      opening_hours, operator_description, facility_type, is_published, verification_status,\n      has_adult_night, has_pro_shop, is_deep_crawled, raw_knowledge_panel, photos,\n      candidate_photos, candidate_links, ai_metadata, last_attempted_at, last_enriched_at,\n      retry_count, raw_data\n    ) VALUES (\n      @id, @name, @lat, @lng, @city, @state, @zip, @street_address, @phone_number, @website,\n      @google_place_id, @google_maps_url, @business_status, @rating, @user_ratings_total,\n      @opening_hours, @operator_description, @facility_type, @is_published, @verification_status,\n      @has_adult_night, @has_pro_shop, @is_deep_crawled, @raw_knowledge_panel, @photos,\n      @candidate_photos, @candidate_links, @ai_metadata, @last_attempted_at, @last_enriched_at,\n      @retry_count, @raw_data\n    )\n    ON CONFLICT(id) DO UPDATE SET\n      name = excluded.name,\n      lat = excluded.lat,\n      lng = excluded.lng,\n      city = excluded.city,\n      state = excluded.state,\n      zip = excluded.zip,\n      street_address = excluded.street_address,\n      phone_number = excluded.phone_number,\n      website = excluded.website,\n      google_place_id = excluded.google_place_id,\n      google_maps_url = excluded.google_maps_url,\n      business_status = excluded.business_status,\n      rating = excluded.rating,\n      user_ratings_total = excluded.user_ratings_total,\n      opening_hours = excluded.opening_hours,\n      operator_description = excluded.operator_description,\n      facility_type = excluded.facility_type,\n      is_published = excluded.is_published,\n      verification_status = excluded.verification_status,\n      has_adult_night = excluded.has_adult_night,\n      has_pro_shop = excluded.has_pro_shop,\n      is_deep_crawled = excluded.is_deep_crawled,\n      raw_knowledge_panel = excluded.raw_knowledge_panel,\n      photos = excluded.photos,\n      candidate_photos = excluded.candidate_photos,\n      candidate_links = excluded.candidate_links,\n      ai_metadata = excluded.ai_metadata,\n      last_attempted_at = excluded.last_attempted_at,\n      last_enriched_at = excluded.last_enriched_at,\n      retry_count = excluded.retry_count,\n      raw_data = excluded.raw_data\n  ");
    var id = spot.id || Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
    stmt.run({
        id: id,
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
        is_published: is_published,
        verification_status: spot.verification_status || null,
        has_adult_night: has_adult_night,
        has_pro_shop: has_pro_shop,
        is_deep_crawled: is_deep_crawled,
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
exports.upsertLocalSpot = upsertLocalSpot;
var updateLocalSpot = function (id, updates) {
    var keys = Object.keys(updates);
    if (keys.length === 0)
        return;
    var setClauses = [];
    var params = { id: id };
    for (var _i = 0, keys_1 = keys; _i < keys_1.length; _i++) {
        var k = keys_1[_i];
        if (k === 'id')
            continue;
        var val = updates[k];
        // Normalize booleans
        if (typeof val === 'boolean')
            val = val ? 1 : 0;
        // Serialize objects
        if (val !== null && typeof val === 'object')
            val = JSON.stringify(val);
        // map has_proshop to has_pro_shop just in case
        var colName = k === 'has_proshop' ? 'has_pro_shop' : k;
        setClauses.push("".concat(colName, " = @").concat(colName));
        params[colName] = val;
    }
    var stmt = exports.db.prepare("UPDATE local_spots SET ".concat(setClauses.join(', '), " WHERE id = @id"));
    stmt.run(params);
};
exports.updateLocalSpot = updateLocalSpot;
var getLocalSpot = function (id) {
    var row = exports.db.prepare("SELECT * FROM local_spots WHERE id = ?").get(id);
    return rowToObj(row);
};
exports.getLocalSpot = getLocalSpot;
var deleteLocalSpot = function (id) {
    exports.db.prepare("DELETE FROM local_spots WHERE id = ?").run(id);
};
exports.deleteLocalSpot = deleteLocalSpot;
var getLocalSpots = function (queryArgs) {
    var _a;
    if (queryArgs === void 0) { queryArgs = {}; }
    var query = 'SELECT * FROM local_spots WHERE 1=1';
    var params = [];
    if (queryArgs.status && queryArgs.status !== 'ALL') {
        if (queryArgs.status === 'UNVERIFIED' || queryArgs.status === 'PENDING') {
            query += " AND (verification_status = 'PENDING' OR verification_status IS NULL)";
        }
        else {
            query += " AND verification_status = ?";
            params.push(queryArgs.status);
        }
    }
    if (queryArgs.search) {
        query += " AND (name LIKE ? OR city LIKE ? OR state LIKE ?)";
        params.push("%".concat(queryArgs.search, "%"), "%".concat(queryArgs.search, "%"), "%".concat(queryArgs.search, "%"));
    }
    if (queryArgs.state) {
        query += " AND state = ?";
        params.push(queryArgs.state.toUpperCase());
    }
    if (queryArgs.has_photos)
        query += " AND photos IS NOT NULL";
    if (queryArgs.has_hours)
        query += " AND opening_hours IS NOT NULL";
    if (queryArgs.has_website)
        query += " AND website IS NOT NULL";
    if (queryArgs.has_adult_night)
        query += " AND has_adult_night = 1";
    if (queryArgs.has_pro_shop)
        query += " AND has_pro_shop = 1";
    if (queryArgs.is_published === true)
        query += " AND is_published = 1";
    if (queryArgs.is_published === false)
        query += " AND (is_published = 0 OR is_published IS NULL)";
    if (queryArgs.is_deep_crawled)
        query += " AND is_deep_crawled = 1";
    var ALLOWED_SORT = ['last_attempted_at', 'last_enriched_at', 'name', 'rating', 'user_ratings_total', 'state', 'city', 'verification_status', 'created_at'];
    var safeSort = ALLOWED_SORT.includes(queryArgs.sortCol) ? queryArgs.sortCol : 'last_attempted_at';
    var dir = queryArgs.sortDir === 'asc' ? 'ASC' : 'DESC';
    query += " ORDER BY ".concat(safeSort, " ").concat(dir, " NULLS LAST");
    if (queryArgs.limit) {
        query += " LIMIT ? OFFSET ?";
        params.push(Number(queryArgs.limit), Number(queryArgs.offset || 0));
    }
    var rows = (_a = exports.db.prepare(query)).all.apply(_a, params);
    return rows.map(rowToObj);
};
exports.getLocalSpots = getLocalSpots;
var getLocalCount = function (queryArgs) {
    var _a;
    if (queryArgs === void 0) { queryArgs = {}; }
    var query = 'SELECT COUNT(*) as cnt FROM local_spots WHERE 1=1';
    var params = [];
    if (queryArgs.status && queryArgs.status !== 'ALL') {
        if (queryArgs.status === 'UNVERIFIED' || queryArgs.status === 'PENDING') {
            query += " AND (verification_status = 'PENDING' OR verification_status IS NULL)";
        }
        else {
            query += " AND verification_status = ?";
            params.push(queryArgs.status);
        }
    }
    if (queryArgs.search) {
        query += " AND (name LIKE ? OR city LIKE ? OR state LIKE ?)";
        params.push("%".concat(queryArgs.search, "%"), "%".concat(queryArgs.search, "%"), "%".concat(queryArgs.search, "%"));
    }
    if (queryArgs.state) {
        query += " AND state = ?";
        params.push(queryArgs.state.toUpperCase());
    }
    if (queryArgs.has_photos === 'true')
        query += " AND photos IS NOT NULL";
    if (queryArgs.has_hours === 'true')
        query += " AND opening_hours IS NOT NULL";
    if (queryArgs.has_website === 'true')
        query += " AND website IS NOT NULL";
    if (queryArgs.has_adult_night === 'true')
        query += " AND has_adult_night = 1";
    if (queryArgs.has_pro_shop === 'true')
        query += " AND has_pro_shop = 1";
    if (queryArgs.is_published === 'true' || queryArgs.is_published === true)
        query += " AND is_published = 1";
    if (queryArgs.is_published === 'false' || queryArgs.is_published === false)
        query += " AND (is_published = 0 OR is_published IS NULL)";
    if (queryArgs.is_deep_crawled === 'true')
        query += " AND is_deep_crawled = 1";
    var res = (_a = exports.db.prepare(query)).get.apply(_a, params);
    return res.cnt;
};
exports.getLocalCount = getLocalCount;
// Haversine distance in meters
var getDistanceFromLatLonInM = function (lat1, lon1, lat2, lon2) {
    var R = 6371e3; // Radius of the earth in m
    var dLat = (lat2 - lat1) * (Math.PI / 180);
    var dLon = (lon2 - lon1) * (Math.PI / 180);
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c; // Distance in m
};
var getClosestLocalSpot = function (lat, lng, radiusMeters) {
    // Approx 1 degree = 111,000 meters
    var delta = radiusMeters / 111000;
    // Bounding box query
    var candidates = exports.db.prepare("\n    SELECT id, name, lat, lng FROM local_spots \n    WHERE lat BETWEEN ? AND ? AND lng BETWEEN ? AND ?\n  ").all(lat - delta, lat + delta, lng - delta, lng + delta);
    var closestSpot = null;
    var minDistance = Infinity;
    for (var _i = 0, candidates_1 = candidates; _i < candidates_1.length; _i++) {
        var c = candidates_1[_i];
        if (!c.lat || !c.lng)
            continue;
        var dist = getDistanceFromLatLonInM(lat, lng, c.lat, c.lng);
        if (dist <= radiusMeters && dist < minDistance) {
            minDistance = dist;
            closestSpot = { spot_id: c.id, distance_meters: dist, name: c.name };
        }
    }
    return closestSpot ? [closestSpot] : null;
};
exports.getClosestLocalSpot = getClosestLocalSpot;
var markSpotSynced = function (id) {
    exports.db.prepare("UPDATE local_spots SET sync_required = 0 WHERE id = ?").run(id);
};
exports.markSpotSynced = markSpotSynced;
var getSpotsToSync = function (limit) {
    if (limit === void 0) { limit = 50; }
    var rows = exports.db.prepare("SELECT * FROM local_spots WHERE sync_required = 1 LIMIT ?").all(limit);
    return rows.map(rowToObj);
};
exports.getSpotsToSync = getSpotsToSync;
// --- CONFIG METHODS ---
var getConfig = function () {
    var row = exports.db.prepare("SELECT config_json FROM scraper_config WHERE id = 1").get();
    if (!row) {
        exports.db.prepare("INSERT INTO scraper_config (id, config_json) VALUES (1, '{}')").run();
        row = exports.db.prepare("SELECT config_json FROM scraper_config WHERE id = 1").get();
    }
    return row && row.config_json ? JSON.parse(row.config_json) : {};
};
exports.getConfig = getConfig;
var updateConfig = function (payload) {
    var existing = (0, exports.getConfig)();
    var newConfig = __assign(__assign({}, existing), payload);
    exports.db.prepare("UPDATE scraper_config SET config_json = ?, updated_at = CURRENT_TIMESTAMP WHERE id = 1").run(JSON.stringify(newConfig));
};
exports.updateConfig = updateConfig;
// --- BLOCKLIST METHODS ---
var getBlocklist = function () {
    return exports.db.prepare("SELECT * FROM scraper_blocklist ORDER BY created_at DESC").all();
};
exports.getBlocklist = getBlocklist;
var addBlocklist = function (pattern, match_type, reason) {
    if (match_type === void 0) { match_type = 'name'; }
    if (reason === void 0) { reason = ''; }
    var stmt = exports.db.prepare("INSERT INTO scraper_blocklist (pattern, match_type, reason) VALUES (?, ?, ?)");
    stmt.run(pattern, match_type, reason);
};
exports.addBlocklist = addBlocklist;
var deleteBlocklist = function (id) {
    exports.db.prepare("DELETE FROM scraper_blocklist WHERE id = ?").run(id);
};
exports.deleteBlocklist = deleteBlocklist;
var getBlocklistKeywords = function () {
    return exports.db.prepare("SELECT keyword FROM scraper_blocklist_keywords ORDER BY created_at DESC").all();
};
exports.getBlocklistKeywords = getBlocklistKeywords;
var addBlocklistKeyword = function (keyword) {
    exports.db.prepare("INSERT INTO scraper_blocklist_keywords (keyword) VALUES (?)").run(keyword);
};
exports.addBlocklistKeyword = addBlocklistKeyword;
// --- PIPELINE STATS METHODS ---
var getPipelineStats = function (states) {
    var _a;
    if (states === void 0) { states = []; }
    var whereClause = '1=1';
    var params = [];
    if (states.length > 0) {
        whereClause = "state IN (".concat(states.map(function () { return '?'; }).join(','), ")");
        params = states;
    }
    var query = "\n    SELECT \n      COUNT(*) as total,\n      SUM(CASE WHEN verification_status = 'SEEDED' THEN 1 ELSE 0 END) as seeded,\n      SUM(CASE WHEN verification_status = 'ENRICHED' THEN 1 ELSE 0 END) as enriched,\n      SUM(CASE WHEN verification_status = 'DEEP_CRAWLED' THEN 1 ELSE 0 END) as deep_crawled_count,\n      SUM(CASE WHEN verification_status = 'MEDIA_READY' THEN 1 ELSE 0 END) as media_ready,\n      SUM(CASE WHEN is_published = 1 THEN 1 ELSE 0 END) as published,\n      SUM(CASE WHEN website IS NOT NULL AND website != '' THEN 1 ELSE 0 END) as has_website,\n      SUM(CASE WHEN verification_status = 'PENDING' OR verification_status IS NULL THEN 1 ELSE 0 END) as spider_queue,\n      SUM(CASE WHEN verification_status = 'SEEDED' THEN 1 ELSE 0 END) as detective_queue,\n      SUM(CASE WHEN candidate_photos IS NOT NULL AND photos IS NULL THEN 1 ELSE 0 END) as has_candidates,\n      SUM(CASE WHEN candidate_photos IS NOT NULL AND photos IS NULL THEN 1 ELSE 0 END) as photographer_queue,\n      SUM(CASE WHEN photos IS NOT NULL THEN 1 ELSE 0 END) as has_photos\n    FROM local_spots\n    WHERE ".concat(whereClause, "\n  ");
    var result = (_a = exports.db.prepare(query)).get.apply(_a, params);
    return result;
};
exports.getPipelineStats = getPipelineStats;
// --- Pipeline Field Registry ---
function getFieldRegistry() {
    return exports.db.prepare('SELECT * FROM pipeline_field_registry ORDER BY sort_order ASC').all();
}
function upsertFieldRegistryItem(item) {
    var stmt = exports.db.prepare("\n    INSERT INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order)\n    VALUES (@id, @field_name, @phase_id, @display_label, @data_type, @sort_order)\n    ON CONFLICT(id) DO UPDATE SET\n      field_name=excluded.field_name,\n      phase_id=excluded.phase_id,\n      display_label=excluded.display_label,\n      data_type=excluded.data_type,\n      sort_order=excluded.sort_order\n  ");
    stmt.run(item);
}
