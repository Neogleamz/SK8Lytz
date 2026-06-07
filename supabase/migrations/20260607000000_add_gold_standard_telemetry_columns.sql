ALTER TABLE telemetry_errors
ADD COLUMN payload_size integer,
ADD COLUMN operation_type text;
