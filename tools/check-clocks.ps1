$now = Get-Date
Write-Host "Current System Time: $now"

# Safely scan .local-builder directory for future-dated files
$futureFiles = Get-ChildItem -Path .local-builder/ -Recurse -File -ErrorAction SilentlyContinue | 
  Where-Object { $_.LastWriteTime -gt $now }

if ($futureFiles) {
  Write-Host "Found $($futureFiles.Count) files with future modification timestamps in .local-builder!"
  $futureFiles | Select-Object -First 25 | ForEach-Object {
    Write-Host "$($_.FullName) (LastWriteTime: $($_.LastWriteTime))"
  }
} else {
  Write-Host "No future files found in .local-builder."
}
