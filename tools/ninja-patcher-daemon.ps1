# Ninja Patcher Daemon to disable RERUN_CMAKE block in build.ninja files dynamically.
# This prevents infinite CMake configure loops on Windows due to filesystem tunneling.

Write-Host "### Starting Ninja Patcher Daemon... ###"
$ProjectRoot = Resolve-Path ".." # Assuming run from tools/ or project root
if ($args[0]) {
  $ProjectRoot = Resolve-Path $args[0]
}

Write-Host "Monitoring path: $ProjectRoot"
Set-Location $ProjectRoot

while ($true) {
  try {
    # Scan for build.ninja files in node_modules and android
    if (Test-Path "node_modules") {
      $files = Get-ChildItem -Path node_modules, android -Recurse -File -Filter "build.ninja" -Force -ErrorAction SilentlyContinue
      foreach ($f in $files) {
        $path = $f.FullName
        $txt = [System.IO.File]::ReadAllText($path)
        
        # Comment out the RERUN_CMAKE trigger block (including any indented variables/pools underneath it)
        if ($txt -match "(?m)^build build\.ninja: RERUN_CMAKE") {
          $newTxt = [Regex]::Replace($txt, '(?m)^build build\.ninja: RERUN_CMAKE[^\r\n]*(?:\r?\n[ \t]+[^\r\n]*)*', {
            param($m)
            $lines = $m.Value -split "\r?\n"
            $commentedLines = foreach ($line in $lines) { "# " + $line }
            return ($commentedLines -join "`r`n")
          })
          [System.IO.File]::WriteAllText($path, $newTxt)
          Write-Host "[Patcher] Disabled RERUN_CMAKE block in: $path"
        }
      }
    }
  } catch {
    # File might be temporarily locked during CMake write, ignore and retry next tick
  }
  Start-Sleep -Milliseconds 50
}
