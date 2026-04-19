$ProjectRoot = Get-Location
$MasterRoot = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$JdkPath = Join-Path $MasterRoot ".local-builder\jdk\jdk-17.0.10+7"
$SdkPath = Join-Path $MasterRoot ".local-builder\android-sdk"

# Set session environment
$env:JAVA_HOME = $JdkPath
$env:ANDROID_HOME = $SdkPath
$env:PATH = "$JdkPath\bin;$SdkPath\cmdline-tools\latest\bin;$SdkPath\platform-tools;$env:PATH"

# Move to android directory
Set-Location android

Write-Host "### Starting Gradle Build (Release) ###"
.\gradlew.bat assembleRelease
if ($LASTEXITCODE -ne 0) {
    Set-Location ..
    Write-Error "### Gradle Build FAILED! Aborting. ###"
    exit $LASTEXITCODE
}

Set-Location ..

Write-Host "### Build Finished! APK is at: android\app\build\outputs\apk\release\SK8Lytz.apk ###"
