-- 1. Temporary table to hold the oldest ID per device_mac per user
CREATE TEMP TABLE ranked_devices AS
SELECT id
FROM (
  SELECT id,
         ROW_NUMBER() OVER (PARTITION BY user_id, device_mac ORDER BY registered_at ASC, created_at ASC) as rn
  FROM public.registered_devices
) t
WHERE t.rn = 1;

-- 2. Delete all devices that are NOT the oldest instance (removing duplicates)
DELETE FROM public.registered_devices
WHERE id NOT IN (SELECT id FROM ranked_devices);

-- 3. Drop temp table
DROP TABLE ranked_devices;

-- 4. Apply the strict UNIQUE constraint
ALTER TABLE public.registered_devices
ADD CONSTRAINT registered_devices_user_mac_key UNIQUE (user_id, device_mac);
