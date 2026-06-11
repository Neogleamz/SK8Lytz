/**
 * ble-simulator.test.ts — Virtual BLE Protocol Lab Tests
 * 
 * Verifies the correctness of the Virtual BLE simulator's protocol parser,
 * V2 wrapping/unwrapping, checksum math, static colorful buffer lockout warning gate,
 * and connection status transitions.
 */

const http = require('http');
const path = require('path');

// Port dedicated to unit tests to prevent collisions with active dev systems
const TEST_PORT = 18081;

// Helper: Calculate Sum Checksum
function getChecksum(bytes: number[]): number {
  return bytes.reduce((sum: number, val: number) => sum + val, 0) & 0xFF;
}

// Helper: Wrap payload in V2
function wrapV2(bytes: number[], seq: number = 1): number[] {
  const len = bytes.length;
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
  return [...header, ...bytes];
}

let serverInstance: any;

beforeAll((done: jest.DoneCallback) => {
  // Clear require cache for the simulator to ensure clean boot
  const simulatorPath = path.resolve(__dirname, '../../../../tools/ble-simulator/ble_simulator.js');
  
  // Set environment port for the server boot
  process.env.PORT = String(TEST_PORT);
  
  // Require and boot the server
  // Intercept console.log to keep test outputs clean
  const originalLog = console.log;
  console.log = () => {};
  
  // We boot the server in-process and capture the exported server
  serverInstance = require(simulatorPath);
  
  console.log = originalLog;
  
  // Give it a brief moment to bind to the port
  // Simulator server boot bind delay (not GATT write timing) — intentionally preserved per R-16
  setTimeout(done, 500);
});

afterAll((done: jest.DoneCallback) => {
  if (serverInstance && typeof serverInstance.close === 'function') {
    serverInstance.close(() => {
      done();
    });
  } else {
    done();
  }
});


describe('Virtual BLE Protocol Lab (ble_simulator.js)', () => {
  const baseUrl = `http://localhost:${TEST_PORT}`;

  async function postRequest(endpoint: string, body: Record<string, unknown>): Promise<any> {
    try {
      const response = await fetch(`${baseUrl}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });
      return response.json();
    } catch (error) {
      console.error(`postRequest failed for ${endpoint}:`, error);
      throw error;
    }
  }

  async function getRequest(endpoint: string): Promise<any> {
    try {
      const response = await fetch(`${baseUrl}${endpoint}`);
      return response.json();
    } catch (error) {
      console.error(`getRequest failed for ${endpoint}:`, error);
      throw error;
    }
  }

  it('should return mock advertisement data via /adv', async () => {
    const data = await getRequest('/adv');
    expect(data.id).toBe("08:65:F0:9A:C2:3C");
    expect(data.name).toBe("Ctrl_Mini_RGB_Symphony_new_0xA3");
    expect(data.productId).toBe(163); // 0xA3
  });

  it('should manage connection state (/connect and /disconnect)', async () => {
    let state = await getRequest('/state');
    expect(state.isConnected).toBe(false);

    // Connect
    const connRes = await postRequest('/connect', {});
    expect(connRes.success).toBe(true);
    expect(connRes.state.isConnected).toBe(true);

    // Verify state
    state = await getRequest('/state');
    expect(state.isConnected).toBe(true);

    // Disconnect
    const discRes = await postRequest('/disconnect', {});
    expect(discRes.success).toBe(true);
    expect(discRes.state.isConnected).toBe(false);
    expect(discRes.state.dropCounter).toBe(1);
  });

  describe('Protocol Commands (/write)', () => {
    beforeEach(async () => {
      // Ensure connected state for all writes
      await postRequest('/connect', {});
    });

    afterEach(async () => {
      // Disconnect
      await postRequest('/disconnect', {});
    });

    it('should reject writes if not connected', async () => {
      await postRequest('/disconnect', {});
      const res = await postRequest('/write', { bytes: [0x71, 0x23, 0x0F, 0xA3] });
      expect(res.error).toBe('GATT not connected');
    });

    it('should process 0x71 Power ON/OFF command correctly', async () => {
      // Power ON: [0x71, 0x23, 0x0F, 0xA3]
      const rawPayload = [0x71, 0x23, 0x0f];
      rawPayload.push(getChecksum(rawPayload));
      const wrapped = wrapV2(rawPayload);

      const res = await postRequest('/write', { bytes: wrapped });
      expect(res.success).toBe(true);
      expect(res.power).toBe(true);

      const state = await getRequest('/state');
      expect(state.power).toBe(true);

      // Power OFF: [0x71, 0x24, 0x0F, 0xA4]
      const rawPayloadOff = [0x71, 0x24, 0x0f];
      rawPayloadOff.push(getChecksum(rawPayloadOff));
      const wrappedOff = wrapV2(rawPayloadOff);

      const resOff = await postRequest('/write', { bytes: wrappedOff });
      expect(resOff.success).toBe(true);
      expect(resOff.power).toBe(false);
    });

    it('should respond to 0x63 EEPROM hardware settings query', async () => {
      // Send 0x63: [0x63, 0x12, 0x21, 0x0F, 0xA5]
      const rawPayload = [0x63, 0x12, 0x21, 0x0F];
      rawPayload.push(getChecksum(rawPayload));
      const wrapped = wrapV2(rawPayload);

      // Override current state for validation
      await postRequest('/state', { ledPoints: 43, colorSorting: 3, icType: 6 });

      const res = await postRequest('/write', { bytes: wrapped });
      expect(res.success).toBe(true);
      expect(res.action).toBe('RESPOND_NOTIFY');

      // Unwrap response
      const responseBytes = res.payload as number[];
      expect(responseBytes[0]).toBe(0x00); // V2 start
      const innerPayload = responseBytes.slice(8);
      
      expect(innerPayload[0]).toBe(0x63);
      expect(innerPayload[3]).toBe(6); // icType = SK6812 (6)
      expect(innerPayload[10]).toBe(3); // colorSorting = GBR (3)
      
      // Little-endian verification: pts = (payload[9] << 8) | payload[8]
      const pts_lo = innerPayload[8];
      const pts_hi = innerPayload[9];
      const parsedPts = (pts_hi << 8) | pts_lo;
      expect(parsedPts).toBe(43); // SOULZ default count
    });

    it('should enforce 12-pixel buffer lockout warning on 0x59 Static Colorful', async () => {
      // 0x59 static colorful payload builder:
      // [0x59, lenHi, lenLo, ...R,G,B..., ptsHi, ptsLo, trans, speed, dir, cs]
      
      // 1. Build a high-risk 8-pixel array (under 10 pixel lockout limit)
      const badPixels = Array(8).fill({ r: 255, g: 0, b: 0 });
      const totalLen = (8 * 3) + 9;
      const payload = new Array(totalLen).fill(0);
      payload[0] = 0x59;
      payload[1] = (totalLen >> 8) & 0xFF;
      payload[2] = totalLen & 0xFF;
      let idx = 3;
      for (const p of badPixels) {
        payload[idx++] = p.r;
        payload[idx++] = p.g;
        payload[idx++] = p.b;
      }
      payload[idx++] = 0; // ptsHi
      payload[idx++] = 8; // ptsLo
      payload[idx++] = 1; // transition
      payload[idx++] = 16; // speed
      payload[idx++] = 1; // direction
      payload[idx] = getChecksum(payload.slice(0, totalLen - 1));

      const wrappedBad = wrapV2(payload);
      const resBad = await postRequest('/write', { bytes: wrappedBad });
      expect(resBad.success).toBe(true);
      expect(resBad.warning).toBe('EEPROM_LOCKOUT_RISK');

      // 2. Build a safe 12-pixel array (satisfies minimum boundary check)
      const safePixels = Array(12).fill({ r: 0, g: 255, b: 0 });
      const totalLenSafe = (12 * 3) + 9;
      const payloadSafe = new Array(totalLenSafe).fill(0);
      payloadSafe[0] = 0x59;
      payloadSafe[1] = (totalLenSafe >> 8) & 0xFF;
      payloadSafe[2] = totalLenSafe & 0xFF;
      let idxS = 3;
      for (const p of safePixels) {
        payloadSafe[idxS++] = p.r;
        payloadSafe[idxS++] = p.g;
        payloadSafe[idxS++] = p.b;
      }
      payloadSafe[idxS++] = 0;
      payloadSafe[idxS++] = 12;
      payloadSafe[idxS++] = 1;
      payloadSafe[idxS++] = 16;
      payloadSafe[idxS++] = 1;
      payloadSafe[idxS] = getChecksum(payloadSafe.slice(0, totalLenSafe - 1));

      const wrappedSafe = wrapV2(payloadSafe);
      const resSafe = await postRequest('/write', { bytes: wrappedSafe });
      expect(resSafe.success).toBe(true);
      expect(resSafe.warning).toBeNull();
    });
  });
});
