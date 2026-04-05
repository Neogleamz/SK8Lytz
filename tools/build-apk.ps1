$ProjectRoot = Get-Location
$JdkPath = Join-Path $ProjectRoot ".local-builder\jdk\jdk-17.0.10+7"
$SdkPath = Join-Path $ProjectRoot ".local-builder\android-sdk"
$AdbPath = Join-Path $SdkPath "platform-tools\adb.exe"
$ApkPath = Join-Path $ProjectRoot "android\app\build\outputs\apk\release\SK8Lytz.apk"

# Set session environment
$env:JAVA_HOME = $JdkPath
$env:ANDROID_HOME = $SdkPath
$env:PATH = "$JdkPath\bin;$SdkPath\cmdline-tools\latest\bin;$SdkPath\platform-tools;$env:PATH"

# Move to android directory
Set-Location android

Write-Host "### Starting Gradle Build (Release) ###"
./gradlew assembleRelease

Set-Location ..

Write-Host "### Build Finished! ###"

# Auto install + launch if a device is connected
$devices = & $AdbPath devices | Select-String "device$"
if ($devices) {
    Write-Host "### Device detected — installing APK... ###"
    & $AdbPath install -r $ApkPath
    if ($LASTEXITCODE -eq 0) {
        Write-Host "### Install successful — launching SK8Lytz... ###"
        & $AdbPath shell monkey -p com.anonymous.MobileApp -c android.intent.category.LAUNCHER 1
        Write-Host "### App launched on device! ###"
    } else {
        Write-Host "### Install failed. Check device connection. ###"
    }
} else {
    Write-Host "### No device connected — skipping install. APK is at: $ApkPath ###"
}
