-- Fix telemetry overwrites and JSON parsing errors

-- 1. Add user_id to telemetry_snapshots with cascading delete
ALTER TABLE public.telemetry_snapshots 
ADD COLUMN IF NOT EXISTS user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE;

-- 2. Update the flush_telemetry function to safely cast JSON numeric values
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
        COALESCE((payload->>'total_app_time_sec')::NUMERIC::INT, 0),
        COALESCE((payload->>'total_distance_meters')::NUMERIC::FLOAT, 0),
        COALESCE((payload->>'lifetime_top_speed_mph')::NUMERIC::FLOAT, 0),
        COALESCE((payload->>'total_street_sessions')::NUMERIC::INT, 0),
        '{}'::jsonb,
        '{}'::jsonb,
        '{}'::jsonb
    )
    ON CONFLICT (user_id) DO UPDATE
    SET 
        total_app_time_sec = public.user_lifetime_stats.total_app_time_sec + COALESCE((payload->>'total_app_time_sec')::NUMERIC::INT, 0),
        total_distance_meters = public.user_lifetime_stats.total_distance_meters + COALESCE((payload->>'total_distance_meters')::NUMERIC::FLOAT, 0),
        lifetime_top_speed_mph = GREATEST(public.user_lifetime_stats.lifetime_top_speed_mph, COALESCE((payload->>'lifetime_top_speed_mph')::NUMERIC::FLOAT, 0)),
        total_street_sessions = public.user_lifetime_stats.total_street_sessions + COALESCE((payload->>'total_street_sessions')::NUMERIC::INT, 0),
        updated_at = NOW();

    -- 2. Fetch the newly upserted row to get the current JSONB state
    SELECT pattern_time_map, color_time_map, engagement_counters 
    INTO v_pattern_map, v_color_map, v_engagement 
    FROM public.user_lifetime_stats 
    WHERE user_id = v_user_id;

    -- 3. Merge pattern_time_map (Add seconds)
    FOR k, v IN SELECT * FROM jsonb_each_text(COALESCE(payload->'pattern_time_map', '{}'::jsonb))
    LOOP
        v_pattern_map := jsonb_set(
            v_pattern_map, 
            array[k], 
            to_jsonb(COALESCE((v_pattern_map->>k)::NUMERIC::FLOAT, 0) + v::NUMERIC::FLOAT)
        );
    END LOOP;

    -- 4. Merge color_time_map (Add seconds)
    FOR k, v IN SELECT * FROM jsonb_each_text(COALESCE(payload->'color_time_map', '{}'::jsonb))
    LOOP
        v_color_map := jsonb_set(
            v_color_map, 
            array[k], 
            to_jsonb(COALESCE((v_color_map->>k)::NUMERIC::FLOAT, 0) + v::NUMERIC::FLOAT)
        );
    END LOOP;

    -- 5. Merge engagement_counters (Add counts)
    FOR k, v IN SELECT * FROM jsonb_each_text(COALESCE(payload->'engagement_counters', '{}'::jsonb))
    LOOP
        v_engagement := jsonb_set(
            v_engagement, 
            array[k], 
            to_jsonb(COALESCE((v_engagement->>k)::NUMERIC::INT, 0) + v::NUMERIC::INT)
        );
    END LOOP;

    -- 6. Final Update with the merged JSONB state
    UPDATE public.user_lifetime_stats 
    SET 
        pattern_time_map = v_pattern_map,
        color_time_map = v_color_map,
        engagement_counters = v_engagement
    WHERE user_id = v_user_id;

END;
$$;
