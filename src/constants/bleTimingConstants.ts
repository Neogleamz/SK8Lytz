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

  /**
   * Heartbeat ping interval.
   * HeartbeatService.ts:10
   */
  HEARTBEAT_INTERVAL_MS: 45_000,

  /**
   * Probe timeout for hardware interrogation.
   * InterrogatorService.ts:25, BlePingService.ts:71,80
   */
  PROBE_TIMEOUT_MS: 3500,

  /**
   * Probe queue delay for normal interrogation.
   * InterrogatorService.ts:26
   */
  PROBE_QUEUE_DELAY_MS: 2000,

  /**
   * Probe queue delay during FTUE.
   * InterrogatorService.ts:27
   */
  PROBE_QUEUE_DELAY_MS_FTUE: 500,

  /**
   * Delay between staggered interrogations.
   * InterrogatorService.ts:28
   */
  INTERROGATION_STAGGER_MS: 500,

  /**
   * Base delay for GATT retries.
   * BleWriteQueue.ts:213
   */
  GATT_RETRY_BASE_MS: 100,

  /**
   * Jitter for GATT retries.
   * BleWriteQueue.ts:213
   */
  GATT_RETRY_JITTER_MS: 50,

  /**
   * Backoff delays for GATT connection retries.
   * ConnectService.ts:147
   */
  GATT_CONNECT_BACKOFF_MS: [500, 1500, 4000] as readonly number[],

  /**
   * Backoff delays for GATT session creation.
   * BleSessionFactory.ts:93
   */
  GATT_SESSION_BACKOFF_MS: [500, 1500] as readonly number[],

  /**
   * Base delay for recovery attempts.
   * RecoveryService.ts:12
   */
  RECOVERY_BASE_MS: 1500,

  /**
   * Max delay for recovery attempts.
   * RecoveryService.ts:13
   */
  RECOVERY_MAX_MS: 30_000,

  /**
   * Max attempts for recovery Phase 1.
   * RecoveryService.ts:14
   */
  RECOVERY_PHASE_1_MAX_ATTEMPTS: 12,

  /**
   * Max attempts for recovery Phase 2.
   * RecoveryService.ts:15
   */
  RECOVERY_PHASE_2_MAX_ATTEMPTS: 5,

  /**
   * Backoff delay for recovery Phase 2.
   * RecoveryService.ts:16
   */
  RECOVERY_PHASE_2_BACKOFF_MS: 20_000,

  /**
   * Poll interval for recovery Phase 3.
   * RecoveryService.ts:17
   */
  RECOVERY_PHASE_3_POLL_INTERVAL_MS: 5_000,

  /**
   * Max polls for recovery Phase 3.
   * RecoveryService.ts:18
   */
  RECOVERY_PHASE_3_MAX_POLLS: 120,

  /**
   * Debounce delay for scanner.
   * useBLEScanner.ts:26
   */
  SCAN_DEBOUNCE_MS: 1500,

  /**
   * Debounce delay for scanner in FTUE.
   * useBLEScanner.ts:27
   */
  SCAN_DEBOUNCE_MS_FTUE: 800,

  /**
   * Flush interval for scanner telemetry.
   * useBLEScanner.ts:309
   */
  SCAN_TELEMETRY_FLUSH_MS: 60_000,

  /**
   * Flush deferral for offline telemetry.
   * useBLEScanner.ts:107
   */
  SCAN_OFFLINE_FLUSH_DEFER_MS: 5000,

  /**
   * Discovery delay for mock scanner in sandbox.
   * useBLEScanner.ts:348
   */
  SANDBOX_MOCK_DISCOVERY_DELAY_MS: 1000,

  /**
   * Stop delay for mock scanner in sandbox.
   * useBLEScanner.ts:361
   */
  SANDBOX_MOCK_SCAN_STOP_MS: 5000,

  /**
   * Manual scan timeout delay.
   * useBLEScanner.ts:384
   */
  MANUAL_SCAN_TIMEOUT_MS: 5000,

  /**
   * Throttle scan ON interval.
   * useBLEBatterySweep.ts:12
   */
  THROTTLE_SCAN_ON_MS: 10_000,

  /**
   * Throttle scan OFF interval.
   * useBLEBatterySweep.ts:13
   */
  THROTTLE_SCAN_OFF_MS: 20_000,


  /**
   * Scan budget window duration.
   * useBLEBatterySweep.ts:41
   */
  SCAN_BUDGET_WINDOW_MS: 30_000,

  /**
   * RSSI polling interval for live signal-strength monitoring.
   * RSSIService.ts — setInterval period in startRSSIPolling().
   */
  RSSI_POLL_INTERVAL_MS: 30_000,

  /**
   * Optimistic write confirmation UI reset delay.
   * useOptimisticBLE.ts — resets write status from CONFIRMED → IDLE.
   */
  OPTIMISTIC_CONFIRM_RESET_MS: 300,

  /**
   * Optimistic write reconciliation UI reset delay.
   * useOptimisticBLE.ts — resets write status from RECONCILED → IDLE.
   */
  OPTIMISTIC_RECONCILE_RESET_MS: 1000,

  /**
   * Debounced AsyncStorage write delay for device state ledger.
   * useDeviceStateLedger.ts — coalesces rapid slider changes into one write.
   */
  LEDGER_WRITE_DEBOUNCE_MS: 500,
} as const;

export type BleTiming = typeof BLE_TIMING;
