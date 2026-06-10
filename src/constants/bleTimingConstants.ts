/**
 * bleTimingConstants.ts — BLE Pipeline Timing Registry
 *
 * All hardcoded millisecond delay values in the BLE write and connection
 * pipeline are centralized here. Changing a value in this file propagates
 * to all consumers without hunting magic numbers across 5 service files.
 *
 * Values are hardware-empirical — derived from testing against the ZENGGE
 * 0xA3 chipset. Do NOT change without a corresponding BLE lab validation
 * (see /ble-lab workflow).
 *
 * Added: fix/ble-delay-constants | 2026-06-08
 * Source of Truth: artifacts/deepdive_raw/R-16_findings.json (R-16 sweep)
 */

export const BLE_TIMING = {
  /**
   * Post-stale-device-flush settle before reconnect.
   * BleConnectionManager — after cancelDeviceConnection on stale devices.
   * Gives Android BT stack time to release the GATT handle before re-connecting.
   */
  STALE_FLUSH_SETTLE_MS: 100,

  /**
   * MTU retry settle between failed MTU negotiation attempts.
   * BleConnectionManager — Android-only MTU negotiation retry loop.
   * Prevents back-to-back MTU requests overwhelming the GATT layer.
   */
  MTU_RETRY_SETTLE_MS: 200,

  /**
   * Post-disconnect settle after GATT cancellation on all stale devices.
   * BleLifecycleManager — after cancelDeviceConnection loop completes.
   * Ensures GATT handles are fully released before state is cleared.
   */
  DISCONNECT_SETTLE_MS: 250,

  /**
   * Recovery ping pre-delay in Phase 2 of auto-recovery.
   * useBLEAutoRecovery — before sending 0x63 queryHardwareSettings ping.
   * Lets the newly re-connected GATT session stabilize before the first write.
   */
  RECOVERY_PING_SETTLE_MS: 600,

  /**
   * Inter-device gap in group write loops.
   * BleWriteDispatcher — between consecutive GATT writes to different devices.
   * Prevents GATT 133 on Android: writeCharacteristicWithoutResponse resolves
   * when SENT (not received). Without gap, device 1's incoming notification
   * collides with device 2's in-flight write → buffer overflow → cascade.
   */
  INTER_DEVICE_WRITE_GAP_MS: 50,

  /**
   * Write debounce delay for pattern/color writes (0x59, 0x51, 0x40).
   * BleWriteDispatcher — coalesces rapid UI changes into a single GATT write.
   */
  WRITE_DEBOUNCE_MS: 50,

  /**
   * Inter-chunk gap within a single chunked 0x40-framed payload.
   * BleWriteDispatcher — between sequential ZENGGE 0x40 chunk writes.
   * Shorter than inter-device gap: same device, contiguous transaction.
   */
  WRITE_CHUNK_INTER_GAP_MS: 8,

  /**
   * Post-chunk-sequence settle after the final chunk is sent.
   * BleWriteDispatcher — after the full chunked write completes.
   */
  WRITE_CHUNK_FINAL_SETTLE_MS: 50,

  /**
   * Post-connection GATT settle time before flashing.
   * Sk8LytzProgrammer — gives device time to stabilize after connection.
   */
  FLASH_SETTLE_MS: 600,

  /**
   * Post-flash settle time to let the write transaction land in EEPROM.
   * Sk8LytzProgrammer — prevents disconnecting before flash saves.
   */
  FLASH_WRITE_LAND_MS: 400,

  /**
   * Gap between consecutive device flash operations.
   * Sk8LytzProgrammer — lets the BT adapter cool down between units.
   */
  FLASH_DISCONNECT_GAP_MS: 400,
} as const;

export type BleTiming = typeof BLE_TIMING;
