-- Migration: God-Tier Telemetry Architecture (user_lifetime_stats)
-- Description: Creates the telemetry aggregator table and the SECRUITY DEFINER flush RPC.

CREATE TABLE IF NOT EXISTS public.user_lifetime_stats (
    user_id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    total_app_time_sec INT DEFAULT 0,
    total_distance_meters FLOAT DEFAULT 0.0,
    lifetime_top_speed_mph FLOAT DEFAULT 0.0,
    total_street_sessions INT DEFAULT 0,
    pattern_time_map JSONB DEFAULT '{}'::jsonb,
    color_time_map JSONB DEFAULT '{}'::jsonb,
    engagement_counters JSONB DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- RLS setup
ALTER TABLE public.user_lifetime_stats ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can read their own stats"
ON public.user_lifetime_stats FOR SELECT
USING (auth.uid() = user_id);

CREATE POLICY "Admins can read all stats"
ON public.user_lifetime_stats FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.user_profiles
        WHERE user_id = auth.uid() AND role = 'admin'
    )
);

-- We DO NOT allow direct inserts/updates from the client.
-- All writes must go through the flush_telemetry RPC to enforce the addition logic.

CREATE OR REPLACE FUNCTION public.flush_telemetry(payload JSONB)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    v_user_id UUID;
    v_pattern_map JSONB;
    v_color_map JSONB;
    v_engagement JSONB;
    
    k TEXT;
    v NUMERIC;
BEGIN
    v_user_id := auth.uid();
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'Not authenticated';
    END IF;

    -- 1. Upsert the base row with simple INT/FLOAT addition
    INSERT INTO public.user_lifetime_stats (
        user_id, 
        total_app_time_sec, 
        total_distance_meters, 
        lifetime_top_speed_mph, 
        total_street_sessions,
        pattern_time_map,
        color_time_map,
        engagement_counters
    )
    VALUES (
        v_user_id,
        COALESCE((payload->>'total_app_time_sec')::INT, 0),
        COALESCE((payload->>'total_distance_meters')::FLOAT, 0),
        COALESCE((payload->>'lifetime_top_speed_mph')::FLOAT, 0),
        COALESCE((payload->>'total_street_sessions')::INT, 0),
        '{}'::jsonb,
        '{}'::jsonb,
        '{}'::jsonb
    )
    ON CONFLICT (user_id) DO UPDATE
    SET 
        total_app_time_sec = public.user_lifetime_stats.total_app_time_sec + COALESCE((payload->>'total_app_time_sec')::INT, 0),
        total_distance_meters = public.user_lifetime_stats.total_distance_meters + COALESCE((payload->>'total_distance_meters')::FLOAT, 0),
        lifetime_top_speed_mph = GREATEST(public.user_lifetime_stats.lifetime_top_speed_mph, COALESCE((payload->>'lifetime_top_speed_mph')::FLOAT, 0)),
        total_street_sessions = public.user_lifetime_stats.total_street_sessions + COALESCE((payload->>'total_street_sessions')::INT, 0),
        updated_at = NOW();

    -- 2. Fetch the newly upserted row to get the current JSONB state
    SELECT pattern_time_map, color_time_map, engagement_counters 
    INTO v_pattern_map, v_color_map, v_engagement 
    FROM public.user_lifetime_stats 
    WHERE user_id = v_user_id;

    -- 3. Merge pattern_time_map (Add seconds)
    IF payload ? 'pattern_time_map' THEN
        FOR k, v IN SELECT * FROM jsonb_each_text(payload->'pattern_time_map') LOOP
            v_pattern_map := jsonb_set(v_pattern_map, ARRAY[k], to_jsonb(COALESCE((v_pattern_map->>k)::NUMERIC, 0) + v::NUMERIC));
        END LOOP;
    END IF;

    -- 4. Merge color_time_map (Add seconds)
    IF payload ? 'color_time_map' THEN
        FOR k, v IN SELECT * FROM jsonb_each_text(payload->'color_time_map') LOOP
            v_color_map := jsonb_set(v_color_map, ARRAY[k], to_jsonb(COALESCE((v_color_map->>k)::NUMERIC, 0) + v::NUMERIC));
        END LOOP;
    END IF;

    -- 5. Merge engagement_counters (Add counts)
    IF payload ? 'engagement_counters' THEN
        FOR k, v IN SELECT * FROM jsonb_each_text(payload->'engagement_counters') LOOP
            v_engagement := jsonb_set(v_engagement, ARRAY[k], to_jsonb(COALESCE((v_engagement->>k)::NUMERIC, 0) + v::NUMERIC));
        END LOOP;
    END IF;

    -- 6. Update row with the merged JSONB objects
    UPDATE public.user_lifetime_stats
    SET 
        pattern_time_map = v_pattern_map,
        color_time_map = v_color_map,
        engagement_counters = v_engagement
    WHERE user_id = v_user_id;
END;
$$;

-- Global Admin Read Access RPC
CREATE OR REPLACE FUNCTION public.admin_get_global_telemetry()
RETURNS JSONB
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    v_admin_check BOOLEAN;
    v_total_distance FLOAT;
    v_total_app_time INT;
    v_total_street_sessions INT;
    v_result JSONB;
BEGIN
    -- Only admins can call this
    SELECT EXISTS (
        SELECT 1 FROM public.user_profiles 
        WHERE user_id = auth.uid() AND role = 'admin'
    ) INTO v_admin_check;
    
    IF NOT v_admin_check THEN
        RAISE EXCEPTION 'Access denied: Requires admin role';
    END IF;

    -- Aggregate sums
    SELECT 
        SUM(total_distance_meters),
        SUM(total_app_time_sec),
        SUM(total_street_sessions)
    INTO 
        v_total_distance, 
        v_total_app_time, 
        v_total_street_sessions
    FROM public.user_lifetime_stats;

    v_result := jsonb_build_object(
        'fleet_total_distance_meters', COALESCE(v_total_distance, 0),
        'fleet_total_app_time_sec', COALESCE(v_total_app_time, 0),
        'fleet_total_street_sessions', COALESCE(v_total_street_sessions, 0)
    );

    RETURN v_result;
END;
$$;
