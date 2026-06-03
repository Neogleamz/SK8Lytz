# Refined native cache cleaner
# Targets ONLY native compilation folders to avoid deleting JavaScript builds of build plugins

Write-Host "### Searching for native cache directories... ###"

$deletePaths = @()

# Add target root app build paths
$deletePaths += Join-Path (Get-Location) "android/app/build"
$deletePaths += Join-Path (Get-Location) "android/app/.cxx"
$deletePaths += Join-Path (Get-Location) "android/build"
$deletePaths += Join-Path (Get-Location) "android/.gradle"

# Scan node_modules specifically for android subfolders containing .cxx or build
if (Test-Path "node_modules") {
  $androidDirs = Get-ChildItem -Path node_modules -Recurse -Directory -Filter "android" -ErrorAction SilentlyContinue
  foreach ($dir in $androidDirs) {
    # Check for .cxx or build inside this android directory
    $cxx = Join-Path $dir.FullName ".cxx"
    $build = Join-Path $dir.FullName "build"
    if (Test-Path $cxx) { $deletePaths += $cxx }
    if (Test-Path $build) { $deletePaths += $build }
  }
}

# Deduplicate
$deletePaths = $deletePaths | Select-Object -Unique

# 1. Delete caches
foreach ($p in $deletePaths) {
  if (Test-Path $p) {
    try {
      Remove-Item -Path $p -Recurse -Force -ErrorAction SilentlyContinue
      Write-Host "Removed cache: $p"
    } catch {
      Write-Warning "Could not remove: $p"
    }
  }
}

# 2. Touch files in node_modules and android to 30 seconds in the past
$touchDirs = @(
  "android",
  "node_modules"
)

$pastTime = (Get-Date).AddSeconds(-30)
Write-Host "### Synchronizing timestamps (touching files) to past time: $pastTime ###"
foreach ($dir in $touchDirs) {
  if (Test-Path $dir) {
    Write-Host "Synchronizing timestamps for: $dir..."
    Get-ChildItem -Path $dir -Recurse -File -ErrorAction SilentlyContinue | ForEach-Object {
      try {
        $_.LastWriteTime = $pastTime
      } catch {}
    }
    Write-Host "Finished synchronizing: $dir"
  }
}

Write-Host "### Clean & Clock Sync Finished! ###"
