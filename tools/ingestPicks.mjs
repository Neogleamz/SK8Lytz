import fs from 'fs';



const SUPABASE_URL = process.env.SUPABASE_URL || process.env.EXPO_PUBLIC_SUPABASE_URL;
const SUPABASE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY;

if (!SUPABASE_URL || !SUPABASE_KEY) {
    console.error("Missing process.env.SUPABASE_URL or SUPABASE_KEY.");
    process.exit(1);
}

async function ingestParsedLog(parsed, fileName) {
    console.log(`Processing payload from bucket file: ${fileName}...`);

    // Create a unique session ID based on file name for idempotency
    const sessionId = fileName.replace('.json', '');

    // Check if session has already been ingested
    try {
        const existingRes = await fetch(`${SUPABASE_URL}/rest/v1/parsed_session_stats?session_id=eq.${sessionId}`, {
            headers: {
                'apikey': SUPABASE_KEY,
                'Authorization': `Bearer ${SUPABASE_KEY}`
            }
        });
        const existing = await existingRes.json();
        if (existing && existing.length > 0) {
            console.log(`  ⏭️ Skipping ${fileName}: Already ingested`);
            return;
        }
    } catch (e) {
        console.warn(`Idempotency check failed for ${fileName}:`, e.message);
    }

    // Extract universal tracking keys
    const hostDeviceId = parsed.hostDeviceId || 'unknown-host';
    const bleMac = parsed.bleMac || 'unpaired-host';

    const logs = parsed.logs;
    const devices = parsed.devices || [];
    const stats = parsed.stats;
    
    if (!logs || !Array.isArray(logs)) {
        console.error('Invalid log format. Expected a "logs" array.');
        return;
    }

    // 1. Process and Upload Session Stats
    if (stats) {
        console.log(`Mapping session statistics for database insertion (Session: ${sessionId})...`);
        const statsPayload = [{
            session_id: sessionId,
            device_id: bleMac,
            host_device_id: hostDeviceId,
            timestamp_ms: logs.length > 0 ? logs[logs.length - 1].t : Date.now(),
            devices_discovered: stats.devicesDiscovered || 0,
            total_events: stats.totalEvents || 0,
            storage_bytes_estimate: stats.storageBytesEstimate || 0,
            average_load_time_ms: stats.averageLoadTimeMs || 0,
            battery_level: stats.batteryLevel || -1,
            is_low_power_mode: stats.isLowPowerMode || false,
            mode_usage: stats.modeUsage || {},
            color_usage: stats.colorUsage || {}
        }];

        try {
            const response = await fetch(`${SUPABASE_URL}/rest/v1/parsed_session_stats`, {
                method: 'POST',
                headers: {
                    'apikey': SUPABASE_KEY,
                    'Authorization': `Bearer ${SUPABASE_KEY}`,
                    'Content-Type': 'application/json',
                    'Prefer': 'return=minimal'
                },
                body: JSON.stringify(statsPayload)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Stats Supabase Error ${response.status}: ${errorText}`);
            }
            console.log(`  ✓ Inserted global session stats.`);
        } catch (err) {
            console.error(`❌ Failed inserting session stats:`, err.message);
        }
    }

    // 2. Process and Upload Devices
    if (devices.length > 0) {
        console.log(`Mapping ${devices.length} devices for database insertion...`);
        const devicePayload = devices.map(d => {
            const hw = d.hardwareSettings || {};
            const fw = d.manufacturerData || {}; 
            return {
                session_id: sessionId,
                device_id: d.id,
                timestamp_ms: Date.now(),
                host_device_id: hostDeviceId,
                name: d.name,
                rssi: d.rssi,
                firmware_ver: d.firmwareVer ? { fw: d.firmwareVer, led: d.ledVersion, ble: d.bleVersion } : fw,
                ic_type: hw.icName || null,
                led_points: hw.ledPoints || null,
                segments: hw.segments || null,
                color_sorting: hw.colorSortingName || null
            };
        });

        try {
            const response = await fetch(`${SUPABASE_URL}/rest/v1/parsed_session_devices`, {
                method: 'POST',
                headers: {
                    'apikey': SUPABASE_KEY,
                    'Authorization': `Bearer ${SUPABASE_KEY}`,
                    'Content-Type': 'application/json',
                    'Prefer': 'return=minimal'
                },
                body: JSON.stringify(devicePayload)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Device Supabase Error ${response.status}: ${errorText}`);
            }
            console.log(`  ✓ Inserted ${devicePayload.length} session devices.`);
        } catch (err) {
            console.error(`❌ Failed inserting devices:`, err.message);
        }
    }

    // 3. Process and Upload Logs
    console.log(`Mapping ${logs.length} logs for database insertion...`);

    const dbPayload = logs.map(item => {
        // Strip duplicate properties out of the raw_data JSON to save storage
        const cleanRaw = { ...item.d };
        delete cleanRaw.dir;
        delete cleanRaw.hex;
        delete cleanRaw.deviceId;

        return {
            session_id: sessionId,
            host_device_id: hostDeviceId,
            timestamp_ms: item.t,
            event_type: item.e,
            direction: item.d?.dir || null,
            hex_payload: item.d?.hex || null,
            device_id: item.d?.deviceId || null,
            raw_data: cleanRaw
        };
    });

    const CHUNK_SIZE = 1000;
    console.log(`Starting log ingest in chunks of ${CHUNK_SIZE}...`);

    for (let i = 0; i < dbPayload.length; i += CHUNK_SIZE) {
        const chunk = dbPayload.slice(i, i + CHUNK_SIZE);
        
        try {
            const response = await fetch(`${SUPABASE_URL}/rest/v1/parsed_logs`, {
                method: 'POST',
                headers: {
                    'apikey': SUPABASE_KEY,
                    'Authorization': `Bearer ${SUPABASE_KEY}`,
                    'Content-Type': 'application/json',
                    'Prefer': 'return=minimal'
                },
                body: JSON.stringify(chunk)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Supabase Error ${response.status}: ${errorText}`);
            }

            console.log(`  ✓ Inserted ${i + chunk.length} / ${dbPayload.length}`);
        } catch (err) {
            console.error(`❌ Failed inserting chunk starting at index ${i}:`, err.message);
            return;
        }
    }

    console.log(`🎉 Ingestion complete for ${fileName}!`);
}

async function runBucketSync() {
    console.log('Fetching file list from sk8lytz-logs bucket...');
    try {
        const listRes = await fetch(`${SUPABASE_URL}/storage/v1/object/list/sk8lytz-logs`, {
            method: 'POST',
            headers: {
                'apikey': SUPABASE_KEY,
                'Authorization': `Bearer ${SUPABASE_KEY}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ prefix: '', limit: 100, offset: 0, sortBy: { column: 'name', order: 'asc' } })
        });
        
        if (!listRes.ok) throw new Error(`Bucket List Error: ${await listRes.text()}`);
        
        const files = await listRes.json();
        const jsonFiles = files.filter(f => f.name.endsWith('.json'));
        
        console.log(`Found ${jsonFiles.length} JSON telemetry file(s) in sk8lytz-logs.`);
        
        for (const file of jsonFiles) {
            console.log(`\n================================`);
            console.log(`Downloading ${file.name}...`);
            const dlRes = await fetch(`${SUPABASE_URL}/storage/v1/object/authenticated/sk8lytz-logs/${file.name}`, {
                headers: { 'apikey': SUPABASE_KEY, 'Authorization': `Bearer ${SUPABASE_KEY}` }
            });
            if (!dlRes.ok) {
                console.error(`Failed to download ${file.name}: ${dlRes.status}`);
                continue;
            }
            const data = await dlRes.json();
            await ingestParsedLog(data, file.name);
        }
        
        console.log(`\n✅ Bucket Sync Finished successfully.`);
    } catch(err) {
        console.error('Bucket Sync Failed:', err);
    }
}

runBucketSync();
