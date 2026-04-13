# [CHORE] Telemetry Efficiency & Deduplication Audit

## Design Decisions & Rationale
The current telemetry system utilizes multiple `parsed_*` tables (`parsed_logs`, `parsed_session_stats`, `parsed_session_devices`, etc.) to store data extracted from JSON log files. Initial investigation suggests potential data duplication between raw payloads, session summaries, and individual event logs. This audit aims to normalize the schema, identify redundant fields, and optimize the ingestion pipeline for higher performance and lower storage costs.

## Proposed Changes

### Database Re-evaluation
- **Normalization Analysis**: Compare `parsed_session_stats` vs `skate_sessions`. Are we storing the same metrics (speed, distance) twice?
- **Log Compression**: Evaluate if `parsed_logs` should store the full `hex_payload` alongside `raw_data` JSONB, or if one can be derived from the other.
- **Index Optimization**: Review indexes on `session_id` and `timestamp_ms` across all `parsed_` tables to ensure query performance.

### Ingestion Logic (`tools/ingestPicks.mjs` & Backend)
- **Atomic Upserts**: Ensure that re-uploading the same log file doesn't create duplicate entries (idempotency).
- **Batch Processing**: Optimize the `CHUNK_SIZE` and parallelize uploads where safe.

## Open Questions
- **Retention Policy**: Should `parsed_` telemetry be moved to a cold-storage solution after a certain period, or is permanent persistence required?
- **Granularity**: Do we need every single BLE protocol byte in the DB, or should we only store parsed business logic events?

## Verification Plan
1. **Redundancy Report**: Generate a comparison of data points across tables.
2. **Mock Ingestion Test**: Run ingestion on the same file twice and verify `COUNT(*)` remains stable.
3. **Query Performance Benchmarks**: measure execution time for standard telemetry dashboard queries.
