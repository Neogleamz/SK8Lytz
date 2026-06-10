export interface UnifiedErrorRecord {
  // === IDENTITY ===
  id: string;                    // SHA-256 fingerprint (file + line + message hash)
  fingerprint: string;           // Fuzzy fingerprint for dedup (file + errorCode + messagePattern)
  
  // === SOURCE ===
  source: ErrorSource;           // Which of the 12 collectors found this
  sourceTable?: string;          // Supabase table name if remote
  sourceFile?: string;           // Collector script that generated this record
  collectedAt: string;           // ISO timestamp of collection
  
  // === LOCATION ===
  file: string;                  // Absolute path to source file
  line?: number;                 // Line number (if available)
  column?: number;               // Column number (if available)
  functionName?: string;         // Function/method name (if available)
  componentName?: string;        // React component name (if available)
  
  // === ERROR DETAILS ===
  errorCode?: string;            // TS#### for TSC, Jest test name, etc.
  message: string;               // The error message
  stackTrace?: string;           // Full stack trace (if available)
  breadcrumbs?: Breadcrumb[];    // FlightRecorder breadcrumbs (if crash)
  
  // === CLASSIFICATION ===
  severity: 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'INFO';
  domain: 'BLE' | 'UI' | 'DB' | 'CLOUD' | 'CORE' | 'SCRAPER' | 'AUTH' | 'TELEMETRY';
  errorType: ErrorType;          // CRASH | TYPE_ERROR | RUNTIME | TEST_FAILURE | LINT | REGRESSION | PATTERN_VIOLATION | OPERATIONAL
  
  // === FREQUENCY ===
  occurrences: number;           // How many times this exact error has been seen
  firstSeen: string;             // ISO timestamp
  lastSeen: string;              // ISO timestamp
  trend: 'NEW' | 'RECURRING' | 'REGRESSION' | 'RESOLVED';
  
  // === CROSS-REFERENCE ===
  knownIssueMatch?: string;      // VS-XXX if matches a known issue
  frictionMatch?: string;        // FRICTION-XXX if matches a friction event
  relatedErrors?: string[];      // IDs of related errors (same root cause cluster)
  
  // === TRIAGE ===
  urgencyScore: number;          // 0-100 composite score (see §5 scoring algorithm)
  autoHealCandidate: boolean;    // Can this be auto-fixed?
  proposedFix?: string;          // Suggested fix (if auto-heal candidate)
  requiredExpertise: string[];   // Which personas should handle this
  
  // === METADATA ===
  rawPayload: unknown;           // Original collector output (for audit trail)
}

export type ErrorSource =
  | 'SUPABASE_ERRORS'      // Source 1
  | 'SUPABASE_CRASHES'     // Source 2
  | 'JEST_FAILURES'        // Source 3
  | 'TSC_ERRORS'           // Source 4
  | 'WEB_CONSOLE'          // Source 5
  | 'ADB_LOGCAT'           // Source 6
  | 'SENTINEL'             // Source 7
  | 'NAKED_CONSOLE_ERROR'  // Source 8
  | 'KNOWN_ISSUES'         // Source 9
  | 'FRICTION_LEDGER'      // Source 10
  | 'SESSION_LOG'          // Source 11
  | 'DETOX_E2E';           // Source 12

export type ErrorType =
  | 'CRASH'                // App crash (fatal)
  | 'TYPE_ERROR'           // TypeScript compilation error
  | 'RUNTIME'              // Runtime exception (non-fatal)
  | 'TEST_FAILURE'         // Jest/Detox test failure
  | 'LINT'                 // Static analysis violation
  | 'REGRESSION'           // Previously-fixed error recurring
  | 'PATTERN_VIOLATION'    // Violated a project rule (naked console.error, any cast, etc.)
  | 'OPERATIONAL';         // Agent/workflow friction event

export interface Breadcrumb {
  category: 'NAVIGATION' | 'ACTION' | 'BLE' | 'NETWORK' | 'ERROR' | 'INFO';
  message: string;
  timestamp: string;
}
