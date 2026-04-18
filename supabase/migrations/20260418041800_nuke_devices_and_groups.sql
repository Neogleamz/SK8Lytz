-- FLUSH SCRIPT: Start Clean (Requested via Brainstorm/Execution)

-- 1. Wipe all registered devices across all users
DELETE FROM public.registered_devices;

-- 2. Wipe all registered custom groups across all users
DELETE FROM public.registered_groups;
