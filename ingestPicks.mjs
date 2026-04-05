import fs from 'fs';

const SUPABASE_URL = 'https://qefmeivpjyaukbwadgaz.supabase.co';
const SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZm1laXZwanlhdWtid2FkZ2F6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM0MzUyMjAsImV4cCI6MjA4OTAxMTIyMH0.TtBAAL7RPk-w8Q_IGbhPouBjcdjyCRXKy_D5YS4FQss';

async function ingestParsedLog(parsed, fileName) {
    console.log(`Processing payload from bucket file: ${fileName}...`);

    // Create a unique session ID for this bulk upload
    const sessionId = `import_${Date.now()}`;
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
            host_device_id: hostDeviceId,
            ble_mac: bleMac,
            devices_discovered: stats.devicesDiscovered || 0,
            total_events: stats.totalEvents || 0,
            storage_bytes_estimate: stats.storageBytesEstimate || 0,
            average_load_time_ms: stats.averageLoadTimeMs || 0,
            battery_level: stats.batteryLevel || -1,
            is_low_power_mode: stats.isLowPowerMode || false,
            mode_usage: stats.modeUsage || {},
            pattern_usage: stats.finalPatternUsage || stats.patternUsage || {},
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
                host_device_id: hostDeviceId,
                device_id: d.id,
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

    const dbPayload = logs.map(item => ({
        session_id: sessionId,
        host_device_id: hostDeviceId,
        timestamp_ms: item.t,
        event_type: item.e,
        direction: item.d?.dir || null,
        hex_payload: item.d?.hex || null,
        device_id: item.d?.deviceId || null,
        raw_data: item.d || {} 
    }));

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
