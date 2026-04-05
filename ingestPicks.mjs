import fs from 'fs';

const SUPABASE_URL = 'https://qefmeivpjyaukbwadgaz.supabase.co';
// Using the anon key found in your .env
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
    
    if (!logs || !Array.isArray(logs)) {
        console.error('Invalid log format. Expected a "logs" array.');
        return;
    }

    console.log(`Mapping ${logs.length} logs for database insertion (Session: ${sessionId})...`);

    // Flatten and map the JSON structure to match our Postgres table columns
    const dbPayload = logs.map(item => ({
        session_id: sessionId,
        timestamp_ms: item.t,
        event_type: item.e,
        // Optional chaining in case 'd' doesn't exist for some events
        direction: item.d?.dir || null,
        hex_payload: item.d?.hex || null,
        device_id: item.d?.deviceId || null,
        // Store the entire 'd' object in the JSONB raw_data column for flexible queries
        raw_data: item.d || {} 
    }));

    // Ingest into Supabase in chunks to avoid HTTP 413 Payload Too Large errors
    const CHUNK_SIZE = 1000;
    console.log(`Starting ingest in chunks of ${CHUNK_SIZE}...`);

    for (let i = 0; i < dbPayload.length; i += CHUNK_SIZE) {
        const chunk = dbPayload.slice(i, i + CHUNK_SIZE);
        
        try {
            const response = await fetch(`${SUPABASE_URL}/rest/v1/parsed_logs`, {
                method: 'POST',
                headers: {
                    'apikey': SUPABASE_KEY,
                    'Authorization': `Bearer ${SUPABASE_KEY}`,
                    'Content-Type': 'application/json',
                    'Prefer': 'return=minimal' // minimal response to save bandwidth
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
            // Optional: you can choose to halt or continue on chunk failure
            return;
        }
    }

    console.log('🎉 Full JSON ingestion complete!');
}

// Read filename from command arguments, or use the one you have open
const targetFile = process.argv[2] || 'c:\\Users\\Magma\\Downloads\\logs_unknown-device_2026-04-05T01-49-38-813Z.json';
ingestLogFile(targetFile);
