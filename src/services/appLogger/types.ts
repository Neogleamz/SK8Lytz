export type EventType =
  | 'APP_OPENED'
  | 'SCAN_STARTED'
  | 'SCAN_COMPLETED'
  | 'SCAN_FILTER_MATCH'
  | 'SCAN_FILTER_REJECT'
  | 'DEVICE_DISCOVERED'
  | 'DEVICE_CONNECTED'
  | 'DEVICE_DISCONNECTED'
  | 'DEVICE_RENAMED'
  | 'PRODUCT_CONFIRMED'
  | 'MODE_CHANGED'
  | 'PATTERN_CHANGED'
  | 'COLOR_CHANGED'
  | 'BRIGHTNESS_CHANGED'
  | 'SPEED_CHANGED'
  | 'HARDWARE_CONFIG_CHANGED'
  | 'CREW_SESSION_CREATED'
  | 'CREW_SESSION_JOINED'
  | 'CREW_SESSION_LEFT'
  | 'CREW_LEFT'
  | 'CREW_DELETED'
  | 'CREW_ERROR'
  | 'PROTOCOL_ERROR'
  | 'BLE_WRITE_ERROR'
  | 'BLE_CONNECTION_ERROR'
  | 'BLE_QUEUE_REPLAY'
  | 'RAW_PAYLOAD'
  | 'SCENE_CREATED'
  | 'GRADIENT_SYNC_FAIL'
  | 'GRADIENT_LOCAL_READ_FAIL'
  | 'GRADIENT_LOCAL_SAVE_FAIL'
  | 'GRADIENT_CLOUD_SAVE_FAIL'
  | 'GRADIENT_CLOUD_DEL_FAIL'
  | 'GRADIENT_LOCAL_DEL_FAIL'
  | 'BLE_AUTO_REQUEST_RESULT'
  // ── HAL / Protocol Debug ────────────────────────────────
  | 'ZENGGE_MUSIC_CONFIG'
  | 'ZENGGE_MUSIC_CONFIG_13B'
  | 'ZENGGE_CUSTOM_MODE_COMPACT'
  | 'ZENGGE_CUSTOM_MODE_EXT_COMPACT'
  | 'ZENGGE_CUSTOM_MODE_323B'
  | 'ZENGGE_SETTLED_MODE_0x41'
  | 'ZENGGE_EFFECT_SEQ_0x43_DIAGNOSTIC_ONLY'
  | 'SCREEN_OPENED'
  | 'SCREEN_LOAD_TTID'
  | 'SCREEN_LOAD_TTFD'
  | 'APP_BACKGROUNDED'
  | 'APP_FOREGROUNDED'
  | 'ERROR_CAUGHT'
  | 'PERFORMANCE_METRIC'
  | 'AUTO_RECOVERY_STARTED'
  | 'AUTO_RECOVERY_SUCCESS'
  | 'AUTO_RECOVERY_FAILED'
  | 'MOUNT'
  | 'UNMOUNT'
  | 'SYNC'
  | 'REJOIN'
  | 'FTUE'
  | 'GLOBAL_MODAL_MOUNTED'
  | 'EULA_ACCEPTED'
  | 'PERMISSION_OPT_IN'
  | 'PERMISSION_OPT_OUT'
  | 'CREW_PERMANENT_DELETED'
  // ── Crew Extended Events (unique — SESSION_CREATED/JOINED/LEFT/ERROR declared above) ──
  | 'CREW_SESSION_SCHEDULED'
  | 'CREW_SESSION_ENDED'
  | 'CREW_DISCOVERY_FETCH'
  | 'CREW_SESSION_SHARED'
  | 'CREW_LEADERSHIP_TRANSFERRED'
  | 'CREW_SCENE_BROADCAST'
  | 'CREW_SCENE_RECEIVED'
  | 'CREW_AUTO_REJOINED'
  // ── Street Mode ───────────────────────────────────────────
  | 'STREET_MODE_ACTIVATED'
  | 'STREET_MODE_DEACTIVATED'
  | 'STREET_JERK_DETECTED'
  | 'STREET_SENSITIVITY_CHANGED'
  // ── Push Notifications ────────────────────────────────────
  | 'PUSH_TOKEN_REGISTERED'
  | 'PUSH_NOTIFICATION_TAPPED'
  | 'PUSH_NOTIFICATION_SENT'
  // ── Profile & Crew Management ─────────────────────────────
  | 'PROFILE_UPDATED'
  | 'CREW_PERMANENT_CREATED'
  | 'CREW_PERMANENT_JOINED'
  | 'CREW_PERMANENT_LEFT'
  | 'CREW_PERMANENT_UPDATED'
  | 'CREW_MEMBERS_ADDED'
  | 'PUSH_TOKEN_UNREGISTERED'
  // ── Speed Tracking ────────────────────────────────────────
  | 'SESSION_SAVED'
  | 'SPEED_REACTIVE_ENABLED'
  | 'SPEED_REACTIVE_DISABLED'
  | 'ACCOUNT_MODAL_LOAD_START'
  | 'ACCOUNT_MODAL_DATA_RESOLVED'
  // ── Audio ─────────────────────────────────────────
  | 'MUSIC_CONFIG_REQUESTED'
  // ── Builder & Favorites ───────────────────────────────────
  | 'BUILDER_PRESET_SAVED'
  | 'BUILDER_PRESET_DELETED'
  | 'BUILDER_PRESET_LOADED'
  | 'BUILDER_UI_TOGGLED'
  | 'FAVORITE_LOADED'
  | 'FAVORITE_RENDERED'
  | 'PICK_LOADED'
  | 'PICK_SELECTED'
  | 'MIC_SENSITIVITY_CHANGED'
  // ── Map/Locations ─────────────────────────────────────────
  | 'ERROR'
  // ── Telemetry & System ────────────────────────────────────
  | 'APP_LOG'
  // ── Hardware Watchdog ──────────────────────────────────────
  | 'BLE_STATE_CHANGE'
  | 'BLE_RESTORE_STATE'
  | 'WATCHDOG_MISS'
  | 'WATCHDOG_RELATCH'
  // ── Auto-Recovery Extended ─────────────────────────────────
  | 'AUTO_RECOVERY_CANCELLED'
  | 'AUTO_RECOVERY_GATE_WAIT'
  | 'AUTO_RECOVERY_ADAPTER'
  // ── Telemetry Hardening ─────────────────────────────────────────
  | 'SCREEN_ERROR'
  | 'PROMISE_REJECTION'
  // ── Music Mode ─────────────────────────────────────────────────────────
  | 'MUSIC_MODE_EXIT'
  // ── BLE Hardening (PR-B) ───────────────────────────────────────────────
  | 'BLE_TIME_SYNC'
  | 'BLE_CHUNKED_WRITE'
  // ── Device State Ledger ─────────────────────────────────────────────────────
  | 'LEDGER_RECONNECT_REPLAY'
  // ── Crew Session Lifecycle ──────────────────────────────────────────────────
  | 'CREW_END_SESSION'
  | 'CREW_CLEANUP'
  | 'CREW_DB_UPDATE'
  // ── Navigation & Deep Links ─────────────────────────────────────────────────
  | 'DEEP_LINK'
  | 'DASHBOARD_STATE'
  // ── Global Telemetry Session ────────────────────────────────────────────────
  | 'GLOBAL_SESSION_SAVED'
  | 'GLOBAL_SESSION_DISCARDED'
  | 'GLOBAL_TELEMETRY_STARTED'
  | 'GLOBAL_TELEMETRY'
  | 'AUTO_PAUSE_TOGGLED'
  // ── Admin Tools ─────────────────────────────────────────────────────────────
  | 'DATA_EXPORT'
  // ── Domain Service Telemetry ────────────────────────────────────────────────
  | 'NOTIFICATION_SERVICE'
  | 'PERSISTENCE'
  | 'PERMISSION_SERVICE'
  | 'CREW_PROFILE'
  | 'SCENE_BUILDER'
  | 'SCENE_SERVICE'
  | 'BUILDER_PANEL'
  | 'ACCOUNT_MGMT'
  | 'CREW_SESSION'
  | 'BLE_TRANSPORT'
  // ── FTUE (First-Time User Experience) ─────────────────────────────────────
  | 'FTUE_PHASE_3_COMPLETE'
  | 'FTUE_HARDWARE_WRITE'
  | 'FTUE_HARDWARE_VERIFIED'
  // ── BLE Group Dropout Coordinator ────────────────────────────────────────
  | 'AUTO_RECOVERY_GROUP_COORDINATOR'
  // ── Recovery Telemetry Aggregation ──────────────────────────────────────────
  | 'AUTO_RECOVERY_SUMMARY'
  | 'AUTO_RECOVERY'
  // ── BLE Write Queue ──────────────────────────────────────────────────────────
  | 'BLE_WRITE_QUEUE'
  // ── GATT Mutex ───────────────────────────────────────────────────────────────
  | 'GATT_LOCK_CONTENTION';




export interface LogEntry {
  t: number;        // timestamp ms
  e: EventType;     // event type
  d: Record<string, any>; // payload
}