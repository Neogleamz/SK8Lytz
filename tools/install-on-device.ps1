$ProjectRoot = Get-Location
$SdkPath = Join-Path $ProjectRoot ".local-builder\android-sdk"
$env:PATH = "$SdkPath\platform-tools;$env:PATH"

$ApkPath = Join-Path $ProjectRoot "android\app\build\outputs\apk\release\SK8Lytz.apk"
$DeviceId = "31040DLH20024H"

Write-Host "### Installing APK on Physical Device ($DeviceId) ###"
& adb -s $DeviceId install -r $ApkPath

Write-Host "### Launching App on Physical Device ###"
& adb -s $DeviceId shell am start -n com.anonymous.MobileApp/com.anonymous.MobileApp.MainActivity

Write-Host "### Deployment Finished! ###"
