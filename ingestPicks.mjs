import fs from 'fs';

const SUPABASE_URL = 'https://qefmeivpjyaukbwadgaz.supabase.co';
const SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZm1laXZwanlhdWtid2FkZ2F6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM0MzUyMjAsImV4cCI6MjA4OTAxMTIyMH0.TtBAAL7RPk-w8Q_IGbhPouBjcdjyCRXKy_D5YS4FQss';

async function ingestLogFile(filePath) {
    if (!fs.existsSync(filePath)) {
        console.error(`File not found: ${filePath}`);
        return;
    }

    console.log(`Reading JSON from ${filePath}...`);
    const fileContent = fs.readFileSync(filePath, 'utf-8');
    const parsed = JSON.parse(fileContent);

    // Create a unique session ID for this bulk upload
    const sessionId = `import_${Date.now()}`;
    const logs = parsed.logs;
    const devices = parsed.devices || [];
    
    if (!logs || !Array.isArray(logs)) {
        console.error('Invalid log format. Expected a "logs" array.');
        return;
    }

    // 1. Process and Upload Devices
    if (devices.length > 0) {
        console.log(`Mapping ${devices.length} devices for database insertion (Session: ${sessionId})...`);
        const devicePayload = devices.map(d => {
            const hw = d.hardwareSettings || {};
            // Parse firmware string if we stored it as string, else keep as json
            const fw = d.manufacturerData || {}; // Fallback mapping depending on how BLE library yields it
            return {
                session_id: sessionId,
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
    } else {
        console.log("No devices found in this log export to insert.");
    }

    // 2. Process and Upload Logs
    console.log(`Mapping ${logs.length} logs for database insertion...`);

    const dbPayload = logs.map(item => ({
        session_id: sessionId,
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

    console.log('🎉 Full JSON ingestion complete!');
}

const targetFile = process.argv[2] || 'c:\\Users\\Magma\\Downloads\\logs_unknown-device_2026-04-05T01-49-38-813Z.json';
ingestLogFile(targetFile);
