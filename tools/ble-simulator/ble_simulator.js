/**
 * ble_simulator.js — Virtual BLE Protocol Lab Daemon
 * 
 * An offline Node.js simulation daemon that virtualizes Neogleamz / Zengge Symphony
 * hardware controller opcodes, frame packing, checksum validation, and connection dropouts.
 * 
 * Uses 100% native Node.js APIs (zero dependencies) to conform to the Dependency Diet Protocol.
 */

const http = require('http');
const url = require('url');

const PORT = process.env.PORT || 18080;

// Central Mock Controller State (product_id: 163 = 0xA3)
let deviceState = {
  id: "08:65:F0:9A:C2:3C",
  name: "Ctrl_Mini_RGB_Symphony_new_0xA3",
  productId: 163,
  firmwareVer: 46,
  bleVersion: 5,
  ledVersion: 3,
  
  // Dynamic EEPROM Config State
  power: false,
  ledPoints: 8,       // Default for HALOZ ring segment canvas
  segments: 2,        // Default for HALOZ
  icType: 1,          // WS2812B
  colorSorting: 2,    // GRB
  
  // Lighting & Effect State
  colors: [],         // Array of RGBs
  transitionType: 1,  // Static/Freeze
  speed: 16,
  direction: 1,
  
  // Custom 0x51 steps
  customSteps: [],
  
  // Connection state
  isConnected: false,
  dropCounter: 0,
  
  // Telemetry event log
  logs: []
};

// Reassembly buffer for chunked writes (e.g. 0x51 Extended Scene 323B)
let chunkBuffer = {
  seq: null,
  expectedLen: 323,
  data: []
};

function logEvent(type, metadata = {}) {
  const logEntry = {
    timestamp: new Date().toISOString(),
    type,
    ...metadata
  };
  deviceState.logs.push(logEntry);
  if (deviceState.logs.length > 50) {
    deviceState.logs.shift();
  }
  console.log(`[SIMULATOR] ${type}:`, JSON.stringify(metadata));
}

// Calculate Sum Checksum: Sum of all bytes, masked to 8-bit
function calculateChecksum(bytes) {
  return bytes.reduce((sum, val) => sum + val, 0) & 0xFF;
}

// Wrap inner payload in standard V2 BLE framing
function wrapV2Command(innerPayload, seq = 0) {
  const len = innerPayload.length;
  const header = [
    0x00,
    seq & 0xFF,
    0x80,
    0x00,
    (len >> 8) & 0xFF,
    len & 0xFF,
    (len + 1) & 0xFF,
    0x0B
  ];
  return [...header, ...innerPayload];
}

// Parse incoming payload
function handleWritePayload(bytes) {
  // Check if it has the 0x40 chunk header
  if (bytes[0] === 0x40) {
    const seq = bytes[1];
    // Chunked header format: [0x40, seq, offset_lo, offset_hi, 0x01, 0x43, 0xBD, 0x0B, ...data]
    const chunkData = bytes.slice(8);
    
    if (chunkBuffer.seq !== seq) {
      logEvent('CHUNK_START', { seq, firstChunkSize: chunkData.length });
      chunkBuffer.seq = seq;
      chunkBuffer.data = [...chunkData];
    } else {
      chunkBuffer.data.push(...chunkData);
      logEvent('CHUNK_RECEIVE', { seq, accumulated: chunkBuffer.data.length });
    }
    
    // Check if we reached the expected 323 bytes for 0x51 Extended
    if (chunkBuffer.data.length >= chunkBuffer.expectedLen) {
      const fullPayload = chunkBuffer.data.slice(0, chunkBuffer.expectedLen);
      chunkBuffer = { seq: null, expectedLen: 323, data: [] };
      logEvent('CHUNK_REASSEMBLED', { totalBytes: fullPayload.length });
      return processInnerPayload(fullPayload);
    }
    return { status: 'PARTIAL_CHUNK', bytesReceived: chunkBuffer.data.length };
  }

  // Check standard V2 Wrapper: [0x00, Seq, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...inner]
  if (bytes[0] === 0x00 && bytes[2] === 0x80) {
    const innerLen = (bytes[4] << 8) | bytes[5];
    const innerPayload = bytes.slice(8, 8 + innerLen);
    return processInnerPayload(innerPayload);
  }

  // Fallback: direct inner payload
  return processInnerPayload(bytes);
}

// Main Opcode Resolver
function processInnerPayload(payload) {
  const opcode = payload[0];
  const checksumOffset = payload.length - 1;
  const calculatedCs = calculateChecksum(payload.slice(0, checksumOffset));
  const providedCs = payload[checksumOffset];

  if (calculatedCs !== providedCs) {
    logEvent('CHECKSUM_MISMATCH', { opcode: `0x${opcode.toString(16)}`, calculated: calculatedCs, provided: providedCs });
    return { error: 'Invalid Checksum', opcode, calculatedCs, providedCs };
  }

  logEvent('OPCODE_RECEIVED', { opcode: `0x${opcode.toString(16)}`, length: payload.length });

  switch (opcode) {
    case 0x71: { // Power ON/OFF
      const isON = payload[1] === 0x23;
      deviceState.power = isON;
      logEvent('POWER_TOGGLED', { power: isON });
      return { success: true, power: isON };
    }

    case 0x31: { // Solid Color
      const r = payload[1];
      const g = payload[2];
      const b = payload[3];
      const brightness = payload[4];
      deviceState.colors = [{ r, g, b }];
      logEvent('SOLID_COLOR', { r, g, b, brightness });
      return { success: true, color: { r, g, b }, brightness };
    }

    case 0x59: { // Static Colorful / Spatial Array
      const totalLen = (payload[1] << 8) | payload[2];
      // Colors are from payload[3] up to totalLen - 6
      const numColors = (totalLen - 9) / 3;
      
      // CRITICAL WARNING: Enforce the 12-pixel buffer overflow protection!
      // Payloads under 10 pixels cause physical controller EEPROM locks on 0xA3.
      if (numColors < 10) {
        logEvent('EEPROM_BUFFER_OVERFLOW_RISK', {
          msg: `CRITICAL: RGB array length is ${numColors} (< 10). This causes physical controller EEPROM lockouts on the 0xA3 chipset!`,
          numColors
        });
      }

      const colors = [];
      let idx = 3;
      for (let i = 0; i < numColors; i++) {
        colors.push({
          r: payload[idx++],
          g: payload[idx++],
          b: payload[idx++]
        });
      }

      const hwPoints = (payload[idx++] << 8) | payload[idx++];
      const transitionType = payload[idx++];
      const speed = payload[idx++];
      const direction = payload[idx++];

      deviceState.colors = colors;
      deviceState.transitionType = transitionType;
      deviceState.speed = speed;
      deviceState.direction = direction;

      logEvent('STATIC_COLORFUL_DISPATCH', {
        numColors,
        hwPoints,
        transitionType,
        speed,
        direction,
        lockoutRisk: numColors < 10
      });

      return {
        success: true,
        numColors,
        hwPoints,
        transitionType,
        speed,
        direction,
        warning: numColors < 10 ? 'EEPROM_LOCKOUT_RISK' : null
      };
    }

    case 0x51: { // Custom Scene Sequencer (9B compact or 10B slots)
      // Compact: [0x51, active, mode, speed, r1, g1, b1, r2, g2, b2, ..., 0x0F, cs]
      // Extended: 32 slots * 10B
      const steps = [];
      let idx = 1;
      while (idx < payload.length - 2) {
        const active = payload[idx++];
        if (active === 0xF0) {
          steps.push({
            mode: payload[idx++],
            speed: payload[idx++],
            color1: { r: payload[idx++], g: payload[idx++], b: payload[idx++] },
            color2: { r: payload[idx++], g: payload[idx++], b: payload[idx++] },
            // If extended format, there's a 10th direction byte
            dir: payload.length > 300 ? payload[idx++] : undefined
          });
        } else if (active === 0x0F) {
          // Terminator or inactive marker
          if (payload.length > 300) {
            // Extended zero padding takes 9 more bytes
            idx += 9;
          } else {
            break;
          }
        } else {
          // Unrecognized slot
          break;
        }
      }
      deviceState.customSteps = steps;
      logEvent('CUSTOM_SCENE_SAVED', { stepCount: steps.length });
      return { success: true, stepsSaved: steps.length };
    }

    case 0x63: { // EEPROM config query
      logEvent('EEPROM_QUERY_RECEIVED');
      
      // Construct a classic 12-byte configuration response
      // classic: [0x63, TIMING1, TIMING2, icType, T3, T4, T5, T6, pts_lo, pts_hi, colorSorting, cs]
      const classic = [
        0x63,
        0x12, 0x21, // legacy timings
        deviceState.icType,
        0x00, 0x00, 0x00, 0x00,
        deviceState.ledPoints & 0xFF,          // pts_lo (Little Endian!)
        (deviceState.ledPoints >> 8) & 0xFF,   // pts_hi
        deviceState.colorSorting,
        0xF0 // Reserved trigger
      ];
      classic.push(calculateChecksum(classic));
      
      // Wrap it in the standard V2 envelope
      const wrappedResponse = wrapV2Command(classic, 0);
      return {
        success: true,
        action: 'RESPOND_NOTIFY',
        payload: wrappedResponse
      };
    }

    case 0x62: { // EEPROM config write
      // Format: [0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, cs]
      const pts = (payload[1] << 8) | payload[2];
      const segs = (payload[3] << 8) | payload[4];
      const icType = payload[5];
      const sorting = payload[6];
      
      deviceState.ledPoints = pts;
      deviceState.segments = segs;
      deviceState.icType = icType;
      deviceState.colorSorting = sorting;
      
      logEvent('EEPROM_WRITE_SUCCESS', { pts, segs, icType, sorting });
      return { success: true, pts, segs, icType, sorting };
    }

    case 0x10:
    case 0x11: { // Time sync
      logEvent('TIME_SYNC_RECEIVED');
      return { success: true };
    }

    default:
      logEvent('UNKNOWN_OPCODE', { opcode: `0x${opcode.toString(16)}` });
      return { error: 'Unknown opcode', opcode };
  }
}

// HTTP Server setup
const server = http.createServer((req, res) => {
  const parsedUrl = url.parse(req.url, true);
  const path = parsedUrl.pathname;
  
  // Set CORS headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    res.writeHead(200);
    res.end();
    return;
  }

  // ── Route: /state (GET current simulator state, POST mock overrides)
  if (path === '/state') {
    if (req.method === 'GET') {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify(deviceState));
    } else if (req.method === 'POST') {
      let body = '';
      req.on('data', chunk => { body += chunk; });
      req.on('end', () => {
        try {
          const overrides = JSON.parse(body);
          deviceState = { ...deviceState, ...overrides };
          logEvent('STATE_OVERRIDDEN', overrides);
          res.writeHead(200, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ success: true, state: deviceState }));
        } catch (e) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ error: 'Invalid JSON body' }));
        }
      });
    }
  }
  
  // ── Route: /adv (Return mock advertising packet for scan)
  else if (path === '/adv') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      id: deviceState.id,
      name: deviceState.name,
      productId: deviceState.productId,
      manufacturerData: "AFpWBQhl8JrCPACjLgMBAiMkAR8AAP8AAwALAAA=" // base64 payload matching product_id=163 (0xA3)
    }));
  }

  // ── Route: /connect (Simulate GATT connection)
  else if (path === '/connect') {
    deviceState.isConnected = true;
    logEvent('GATT_CONNECTED');
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ success: true, state: deviceState }));
  }

  // ── Route: /disconnect (Simulate dropout/organic disconnect)
  else if (path === '/disconnect') {
    deviceState.isConnected = false;
    deviceState.dropCounter++;
    logEvent('GATT_DISCONNECTED', { dropCounter: deviceState.dropCounter });
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ success: true, state: deviceState }));
  }

  // ── Route: /write (Accepts decimal array and processes it)
  else if (path === '/write') {
    let body = '';
    req.on('data', chunk => body += chunk);
    req.on('end', () => {
      try {
        const payload = JSON.parse(body);
        let bytes = [];
        
        if (payload.bytes && Array.isArray(payload.bytes)) {
          bytes = payload.bytes;
        } else if (payload.base64) {
          bytes = Array.from(Buffer.from(payload.base64, 'base64'));
        } else if (payload.hex) {
          bytes = payload.hex.split(' ').map(h => parseInt(h, 16));
        } else {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ error: 'Missing bytes, hex, or base64 field' }));
          return;
        }

        if (!deviceState.isConnected) {
          res.writeHead(400, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ error: 'GATT not connected' }));
          return;
        }

        const result = handleWritePayload(bytes);
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify(result));
      } catch (e) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: 'Malformed request: ' + e.message }));
      }
    });
  }

  // Unknown route
  else {
    res.writeHead(404, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ error: 'Not found' }));
  }
});

server.listen(PORT, () => {
  console.log(`[SIMULATOR] Virtual BLE Protocol Lab listening on port ${PORT}`);
  console.log(`[SIMULATOR] Mocking Device 0xA3: MAC ${deviceState.id}`);
});

module.exports = server;

