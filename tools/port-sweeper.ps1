param(
    [int[]]$Ports = @(8081, 5173, 3000)
)
Write-Host "🧹 Sweeping ghost node processes on ports: $($Ports -join ', ')..."
foreach ($Port in $Ports) {
    $Connections = netstat -ano | Select-String ":$Port"
    if ($Connections) {
        $Pids = $Connections | ForEach-Object {
            $parts = $_.Line.Trim() -split '\s+'
            $parts[-1]
        } | Select-Object -Unique
        
        foreach ($pid in $Pids) {
            if ($pid -ne "0") {
                Write-Host "💀 Killing PID $pid (holding port $Port)..." -ForegroundColor Yellow
                Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
            }
        }
    }
}
Write-Host "✅ Sweep complete. Clearing Metro Cache..." -ForegroundColor Green
$env:RCT_METRO_PORT=""
if (Test-Path -Path $env:TEMP\metro-cache) {
    Remove-Item -Recurse -Force $env:TEMP\metro-cache -ErrorAction SilentlyContinue
}
