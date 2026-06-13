-- Add indexes for fast grouping
CREATE INDEX IF NOT EXISTS idx_crash_telemetry_signature ON public.crash_telemetry(error_signature);
CREATE INDEX IF NOT EXISTS idx_crash_telemetry_status ON public.crash_telemetry(status);

-- Create aggregated view for Crash Autopsy
CREATE OR REPLACE VIEW public.view_crash_aggregates AS
SELECT 
    error_signature,
    status,
    COUNT(*) as crash_count,
    COUNT(DISTINCT user_id) as affected_users,
    MIN(created_at) as first_seen,
    MAX(created_at) as last_seen
FROM public.crash_telemetry
GROUP BY error_signature, status;

-- Grant permissions on view
GRANT SELECT ON public.view_crash_aggregates TO authenticated;
GRANT SELECT ON public.view_crash_aggregates TO service_role;

-- Create RPC for bulk resolution
CREATE OR REPLACE FUNCTION public.resolve_crash_signature(target_signature TEXT, resolver_id UUID DEFAULT NULL)
RETURNS VOID AS $$
BEGIN
    UPDATE public.crash_telemetry
    SET status = 'RESOLVED',
        resolved_at = NOW(),
        resolved_by = resolver_id
    WHERE error_signature = target_signature AND status != 'RESOLVED';
END;
$$ LANGUAGE plpgsql SECURITY DEFINER SET search_path = '';

-- Grant execute permissions
GRANT EXECUTE ON FUNCTION public.resolve_crash_signature(TEXT, UUID) TO authenticated;
GRANT EXECUTE ON FUNCTION public.resolve_crash_signature(TEXT, UUID) TO service_role;
