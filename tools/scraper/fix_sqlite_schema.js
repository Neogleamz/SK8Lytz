const Database = require('better-sqlite3');
const path = require('path');

const DB_PATH = path.resolve(__dirname, '../.scraper-data/scraper.db');
console.log('Using database at:', DB_PATH);
const db = new Database(DB_PATH, { verbose: console.log });

const newColumns = [
    { name: 'surface_type', type: 'TEXT' },
    { name: 'is_indoor', type: 'INTEGER DEFAULT 0' },
    { name: 'adult_night_details', type: 'TEXT' },
    { name: 'source', type: 'TEXT' },
    { name: 'is_verified', type: 'INTEGER DEFAULT 0' },
    { name: 'updated_at', type: 'TEXT' },
    { name: 'updated_by', type: 'TEXT' },
    { name: 'address', type: 'TEXT' },
    { name: 'phone', type: 'TEXT' },
    { name: 'vibe_rating', type: 'REAL' },
    { name: 'socials', type: 'TEXT' },
    { name: 'is_featured', type: 'INTEGER DEFAULT 0' },
    { name: 'has_lights', type: 'INTEGER' },
    { name: 'has_fee', type: 'INTEGER' },
    { name: 'operator_name', type: 'TEXT' },
    { name: 'has_rental', type: 'INTEGER' },
    { name: 'is_wheelchair_accessible', type: 'INTEGER' },
    { name: 'has_wifi', type: 'INTEGER' },
    { name: 'has_toilets', type: 'INTEGER' },
    { name: 'has_food', type: 'INTEGER' },
    { name: 'has_ac', type: 'INTEGER' },
    { name: 'has_lockers', type: 'INTEGER' },
    { name: 'capacity', type: 'INTEGER' },
    { name: 'hosts_derby', type: 'INTEGER DEFAULT 0' },
    { name: 'surface_quality', type: 'TEXT' },
    { name: 'vibe_score', type: 'REAL' },
    { name: 'cultural_metadata', type: 'TEXT' },
    { name: 'instagram_url', type: 'TEXT' },
    { name: 'facebook_url', type: 'TEXT' },
    { name: 'tiktok_url', type: 'TEXT' },
    { name: 'schedule_url', type: 'TEXT' },
    { name: 'pricing_data', type: 'TEXT' },
    { name: 'special_events', type: 'TEXT' },
    { name: 'adult_night_schedule', type: 'TEXT' }
];

const colCheck = db.prepare("PRAGMA table_info(local_spots)").all();
const existingCols = colCheck.map(c => c.name);

for (const col of newColumns) {
    if (!existingCols.includes(col.name)) {
        try {
            db.exec(`ALTER TABLE local_spots ADD COLUMN ${col.name} ${col.type}`);
            console.log(`Added column ${col.name}`);
        } catch (e) {
            console.error(`Failed to add column ${col.name}:`, e.message);
        }
    } else {
        console.log(`Column ${col.name} already exists.`);
    }
}

// Rebuild field registry
console.log('Rebuilding pipeline_field_registry...');
db.exec('DELETE FROM pipeline_field_registry');

const ALL_FIELDS = [
    { field_name: 'id', phase_id: 1, display_label: 'ID', data_type: 'uuid', sort_order: 10 },
    { field_name: 'name', phase_id: 1, display_label: 'Name', data_type: 'text', sort_order: 20 },
    { field_name: 'lat', phase_id: 1, display_label: 'Latitude', data_type: 'float', sort_order: 30 },
    { field_name: 'lng', phase_id: 1, display_label: 'Longitude', data_type: 'float', sort_order: 40 },
    { field_name: 'city', phase_id: 1, display_label: 'City', data_type: 'text', sort_order: 50 },
    { field_name: 'state', phase_id: 1, display_label: 'State', data_type: 'text', sort_order: 60 },
    { field_name: 'zip', phase_id: 1, display_label: 'ZIP', data_type: 'text', sort_order: 70 },
    { field_name: 'street_address', phase_id: 1, display_label: 'Street Address', data_type: 'text', sort_order: 80 },
    { field_name: 'phone_number', phase_id: 1, display_label: 'Phone Number', data_type: 'text', sort_order: 90 },
    { field_name: 'website', phase_id: 1, display_label: 'Website', data_type: 'text', sort_order: 100 },
    { field_name: 'google_place_id', phase_id: 1, display_label: 'Google Place ID', data_type: 'text', sort_order: 110 },
    { field_name: 'google_maps_url', phase_id: 1, display_label: 'Google Maps URL', data_type: 'text', sort_order: 120 },
    { field_name: 'business_status', phase_id: 1, display_label: 'Business Status', data_type: 'text', sort_order: 130 },
    { field_name: 'rating', phase_id: 1, display_label: 'Rating', data_type: 'float', sort_order: 140 },
    { field_name: 'user_ratings_total', phase_id: 1, display_label: 'User Ratings Total', data_type: 'integer', sort_order: 150 },
    { field_name: 'source', phase_id: 1, display_label: 'Source', data_type: 'text', sort_order: 160 },
    { field_name: 'is_indoor', phase_id: 1, display_label: 'Is Indoor', data_type: 'boolean', sort_order: 170 },
    { field_name: 'surface_type', phase_id: 1, display_label: 'Surface Type', data_type: 'text', sort_order: 180 },
    { field_name: 'facility_type', phase_id: 1, display_label: 'Facility Type', data_type: 'text', sort_order: 190 },

    // Phase 2 (Spider / Operator)
    { field_name: 'facebook_url', phase_id: 2, display_label: 'Facebook URL', data_type: 'text', sort_order: 200 },
    { field_name: 'instagram_url', phase_id: 2, display_label: 'Instagram URL', data_type: 'text', sort_order: 210 },
    { field_name: 'tiktok_url', phase_id: 2, display_label: 'TikTok URL', data_type: 'text', sort_order: 220 },
    { field_name: 'candidate_links', phase_id: 2, display_label: 'Candidate Links', data_type: 'jsonb', sort_order: 230 },

    // Phase 3 (Deep Crawl / Indexer / Detective)
    { field_name: 'opening_hours', phase_id: 3, display_label: 'Opening Hours', data_type: 'jsonb', sort_order: 300 },
    { field_name: 'pricing_data', phase_id: 3, display_label: 'Pricing Data', data_type: 'jsonb', sort_order: 310 },
    { field_name: 'special_events', phase_id: 3, display_label: 'Special Events', data_type: 'jsonb', sort_order: 320 },
    { field_name: 'adult_night_details', phase_id: 3, display_label: 'Adult Night Details', data_type: 'text', sort_order: 330 },
    { field_name: 'adult_night_schedule', phase_id: 3, display_label: 'Adult Night Schedule', data_type: 'jsonb', sort_order: 340 },
    { field_name: 'has_adult_night', phase_id: 3, display_label: 'Has Adult Night', data_type: 'boolean', sort_order: 350 },
    { field_name: 'operator_name', phase_id: 3, display_label: 'Operator Name', data_type: 'text', sort_order: 360 },
    { field_name: 'operator_description', phase_id: 3, display_label: 'Operator Description', data_type: 'text', sort_order: 370 },
    { field_name: 'has_pro_shop', phase_id: 3, display_label: 'Has Pro Shop', data_type: 'boolean', sort_order: 380 },
    { field_name: 'has_rental', phase_id: 3, display_label: 'Has Rental', data_type: 'boolean', sort_order: 390 },
    { field_name: 'has_food', phase_id: 3, display_label: 'Has Food', data_type: 'boolean', sort_order: 400 },
    { field_name: 'has_ac', phase_id: 3, display_label: 'Has AC', data_type: 'boolean', sort_order: 410 },
    { field_name: 'has_lockers', phase_id: 3, display_label: 'Has Lockers', data_type: 'boolean', sort_order: 420 },
    { field_name: 'is_wheelchair_accessible', phase_id: 3, display_label: 'Wheelchair Accessible', data_type: 'boolean', sort_order: 430 },
    { field_name: 'raw_knowledge_panel', phase_id: 3, display_label: 'Knowledge Panel', data_type: 'jsonb', sort_order: 440 },
    { field_name: 'ai_metadata', phase_id: 3, display_label: 'AI Metadata', data_type: 'jsonb', sort_order: 450 },
    { field_name: 'cultural_metadata', phase_id: 3, display_label: 'Cultural Metadata', data_type: 'jsonb', sort_order: 460 },
    { field_name: 'hosts_derby', phase_id: 3, display_label: 'Hosts Derby', data_type: 'boolean', sort_order: 470 },
    { field_name: 'candidate_photos', phase_id: 3, display_label: 'Candidate Photos', data_type: 'jsonb', sort_order: 480 },

    // Phase 4 (Media Ready / Photographer)
    { field_name: 'photos', phase_id: 4, display_label: 'Photos', data_type: 'jsonb', sort_order: 500 },

    // Internal / Metadata (Phase 0 / Hidden)
    { field_name: 'is_published', phase_id: 5, display_label: 'Is Published', data_type: 'boolean', sort_order: 600 },
    { field_name: 'verification_status', phase_id: 5, display_label: 'Verification Status', data_type: 'text', sort_order: 610 },
    { field_name: 'last_attempted_at', phase_id: 5, display_label: 'Last Attempted', data_type: 'text', sort_order: 620 },
    { field_name: 'last_enriched_at', phase_id: 5, display_label: 'Last Enriched', data_type: 'text', sort_order: 630 },
    { field_name: 'retry_count', phase_id: 5, display_label: 'Retry Count', data_type: 'integer', sort_order: 640 },
    { field_name: 'created_at', phase_id: 5, display_label: 'Created At', data_type: 'text', sort_order: 650 },
    { field_name: 'updated_at', phase_id: 5, display_label: 'Updated At', data_type: 'text', sort_order: 660 },
    { field_name: 'raw_data', phase_id: 5, display_label: 'Raw Data Blob', data_type: 'jsonb', sort_order: 670 }
];

const stmt = db.prepare(`
    INSERT INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order)
    VALUES (@id, @field_name, @phase_id, @display_label, @data_type, @sort_order)
`);

for (const field of ALL_FIELDS) {
    const id = field.field_name;
    stmt.run({ ...field, id });
}

console.log('Registry rebuilt with ' + ALL_FIELDS.length + ' fields.');

// Repopulate missing fields from raw_data
console.log('Repopulating missing fields from raw_data...');
const spots = db.prepare('SELECT id, raw_data FROM local_spots WHERE raw_data IS NOT NULL').all();
let updated = 0;

const updateStmt = db.prepare(`
    UPDATE local_spots SET
        surface_type = @surface_type,
        is_indoor = @is_indoor,
        adult_night_details = @adult_night_details,
        source = @source,
        is_verified = @is_verified,
        updated_at = @updated_at,
        updated_by = @updated_by,
        address = @address,
        phone = @phone,
        vibe_rating = @vibe_rating,
        socials = @socials,
        is_featured = @is_featured,
        has_lights = @has_lights,
        has_fee = @has_fee,
        operator_name = @operator_name,
        has_rental = @has_rental,
        is_wheelchair_accessible = @is_wheelchair_accessible,
        has_wifi = @has_wifi,
        has_toilets = @has_toilets,
        has_food = @has_food,
        has_ac = @has_ac,
        has_lockers = @has_lockers,
        capacity = @capacity,
        hosts_derby = @hosts_derby,
        surface_quality = @surface_quality,
        vibe_score = @vibe_score,
        cultural_metadata = @cultural_metadata,
        instagram_url = @instagram_url,
        facebook_url = @facebook_url,
        tiktok_url = @tiktok_url,
        schedule_url = @schedule_url,
        pricing_data = @pricing_data,
        special_events = @special_events,
        adult_night_schedule = @adult_night_schedule
    WHERE id = @id
`);

for (const row of spots) {
    try {
        const data = JSON.parse(row.raw_data);
        
        updateStmt.run({
            id: row.id,
            surface_type: data.surface_type || null,
            is_indoor: data.is_indoor ? 1 : 0,
            adult_night_details: data.adult_night_details || null,
            source: data.source || null,
            is_verified: data.is_verified ? 1 : 0,
            updated_at: data.updated_at || null,
            updated_by: data.updated_by || null,
            address: data.address || null,
            phone: data.phone || null,
            vibe_rating: data.vibe_rating || null,
            socials: data.socials ? JSON.stringify(data.socials) : null,
            is_featured: data.is_featured ? 1 : 0,
            has_lights: data.has_lights === true ? 1 : (data.has_lights === false ? 0 : null),
            has_fee: data.has_fee === true ? 1 : (data.has_fee === false ? 0 : null),
            operator_name: data.operator_name || null,
            has_rental: data.has_rental === true ? 1 : (data.has_rental === false ? 0 : null),
            is_wheelchair_accessible: data.is_wheelchair_accessible === true ? 1 : (data.is_wheelchair_accessible === false ? 0 : null),
            has_wifi: data.has_wifi === true ? 1 : (data.has_wifi === false ? 0 : null),
            has_toilets: data.has_toilets === true ? 1 : (data.has_toilets === false ? 0 : null),
            has_food: data.has_food === true ? 1 : (data.has_food === false ? 0 : null),
            has_ac: data.has_ac === true ? 1 : (data.has_ac === false ? 0 : null),
            has_lockers: data.has_lockers === true ? 1 : (data.has_lockers === false ? 0 : null),
            capacity: data.capacity || null,
            hosts_derby: data.hosts_derby ? 1 : 0,
            surface_quality: data.surface_quality || null,
            vibe_score: data.vibe_score || null,
            cultural_metadata: data.cultural_metadata ? JSON.stringify(data.cultural_metadata) : null,
            instagram_url: data.instagram_url || null,
            facebook_url: data.facebook_url || null,
            tiktok_url: data.tiktok_url || null,
            schedule_url: data.schedule_url || null,
            pricing_data: data.pricing_data ? JSON.stringify(data.pricing_data) : null,
            special_events: data.special_events ? JSON.stringify(data.special_events) : null,
            adult_night_schedule: data.adult_night_schedule ? JSON.stringify(data.adult_night_schedule) : null
        });
        updated++;
    } catch (e) {
        console.error('Failed to update row', row.id, e.message);
    }
}
console.log('Repopulated', updated, 'rows.');
db.close();
