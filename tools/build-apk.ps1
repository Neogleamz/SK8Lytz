$ProjectRoot = Get-Location
$MasterRoot = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$JdkPath = Join-Path $MasterRoot ".local-builder\jdk\jdk-17.0.10+7"
$SdkPath = Join-Path $MasterRoot ".local-builder\android-sdk"

# Set session environment
$env:JAVA_HOME = $JdkPath
$env:ANDROID_HOME = $SdkPath
$env:PATH = "$JdkPath\bin;$SdkPath\cmdline-tools\latest\bin;$SdkPath\platform-tools;$env:PATH"

$PatcherProc = $null
try {
    # PRE-PATCH: Synchronously disable RERUN_CMAKE in ALL existing build.ninja files
    # before Gradle starts. This prevents Ninja's 100-retry infinite loop on cached
    # .cxx directories from prior builds (Windows filesystem tunneling bug).
    Write-Host "### Pre-patching existing build.ninja files... ###"
    $ninjaFiles = Get-ChildItem -Path node_modules, android -Recurse -File -Filter "build.ninja" -Force -ErrorAction SilentlyContinue
    foreach ($f in $ninjaFiles) {
        $path = $f.FullName
        $txt = [System.IO.File]::ReadAllText($path)
        if ($txt -match "(?m)^build build\.ninja: RERUN_CMAKE") {
            $newTxt = [Regex]::Replace($txt, '(?m)^build build\.ninja: RERUN_CMAKE[^\r\n]*(?:\r?\n[ \t]+[^\r\n]*)*', {
                param($m)
                $lines = $m.Value -split "\r?\n"
                $commentedLines = foreach ($line in $lines) { "# " + $line }
                return ($commentedLines -join "`r`n")
            })
            [System.IO.File]::WriteAllText($path, $newTxt)
            Write-Host "[Pre-Patch] Disabled RERUN_CMAKE in: $path"
        }
    }

    Write-Host "### Starting Ninja Patcher Daemon in background... ###"
    $PatcherProc = Start-Process powershell.exe -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$ProjectRoot\tools\ninja-patcher-daemon.ps1`" `"$ProjectRoot`"" -WindowStyle Hidden -PassThru

    # Move to android directory
    Set-Location android

    Write-Host "### Starting Gradle Build (Release) ###"
    .\gradlew.bat assembleRelease
    if ($LASTEXITCODE -ne 0) {
        Write-Error "### Gradle Build FAILED! Aborting. ###"
        exit $LASTEXITCODE
    }
} finally {
    if ($PatcherProc) {
        Write-Host "### Stopping Ninja Patcher Daemon... ###"
        Stop-Process -Id $PatcherProc.Id -Force -ErrorAction SilentlyContinue
    }
    Set-Location $ProjectRoot
}

Write-Host "### Build Finished! APK is at: android\app\build\outputs\apk\release\app-release.apk ###"
