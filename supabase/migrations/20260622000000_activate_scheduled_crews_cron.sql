-- Migration: server-side activation of scheduled crew sessions
-- Mechanism: pg_cron (every 1 min) -> pg_net.http_post -> edge function
--            activate-scheduled-crews. See PLAN-feat-crew-scheduled-server-side.md.
-- BLOCKED-ON: SPIKE-1 (pg_cron/pg_net available), SPIKE-2 (CRON_SECRET set).
-- DEPLOY-TIME: replace <PROJECT_REF> and <CRON_SECRET> before `supabase db push`. See PLAN Step 2.

-- 1. Extensions (idempotent). On Supabase these install into the `extensions` schema.
create extension if not exists pg_cron;
create extension if not exists pg_net;

-- 2. Store the function URL + cron secret in Vault (do NOT hardcode in cron body
--    where it would leak via cron.job inspection by lower-priv roles).
--    Replace <PROJECT_REF> and <CRON_SECRET> with the real values at deploy time.
select vault.create_secret(
  'https://<PROJECT_REF>.supabase.co/functions/v1/activate-scheduled-crews',
  'activate_scheduled_crews_url'
);
select vault.create_secret(
  '<CRON_SECRET>',
  'activate_scheduled_crews_secret'
);

-- 3. Schedule the job: every minute, POST to the edge function with the secret header.
select cron.schedule(
  'activate-scheduled-crews-every-min',
  '* * * * *',
  $$
  select net.http_post(
    url := (select decrypted_secret from vault.decrypted_secrets
            where name = 'activate_scheduled_crews_url'),
    headers := jsonb_build_object(
      'Content-Type', 'application/json',
      'x-cron-secret', (select decrypted_secret from vault.decrypted_secrets
                        where name = 'activate_scheduled_crews_secret')
    ),
    body := '{}'::jsonb
  );
  $$
);
