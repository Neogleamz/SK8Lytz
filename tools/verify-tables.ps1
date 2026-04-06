$key  = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZm1laXZwanlhdWtid2FkZ2F6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM0MzUyMjAsImV4cCI6MjA4OTAxMTIyMH0.TtBAAL7RPk-w8Q_IGbhPouBjcdjyCRXKy_D5YS4FQss'
$base = 'https://qefmeivpjyaukbwadgaz.supabase.co/rest/v1'
$hdr  = @{ apikey = $key; Authorization = "Bearer $key" }

$tables = @('user_profiles','crews','crew_memberships','push_tokens','crew_sessions')

foreach ($t in $tables) {
    try {
        $r = Invoke-WebRequest -Uri "$base/$t`?limit=1" -Headers $hdr -Method GET -UseBasicParsing -TimeoutSec 10
        Write-Host "$t : $($r.StatusCode) OK" -ForegroundColor Green
    } catch {
        $code = $_.Exception.Response.StatusCode.value__
        Write-Host "$t : $code FAIL" -ForegroundColor Red
    }
}
