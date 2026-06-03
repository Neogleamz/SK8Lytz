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
