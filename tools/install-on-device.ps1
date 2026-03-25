$ProjectRoot = Get-Location
$AdbPath = "C:\Neogleamz\AG_SK8Lytz_App\Sk8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"
$ApkPath = Join-Path $ProjectRoot "android\app\build\outputs\apk\release\SK8Lytz.apk"

# Preferred device if available, otherwise use any connected device
$TargetDevice = "31040DLH20024H"
$ConnectedDevices = & $AdbPath devices | Select-String -Pattern "\tdevice$"

if (-not $ConnectedDevices) {
    Write-Error "No Android devices or emulators found. Please connect your phone."
    exit 1
}

$IsTargetConnected = $ConnectedDevices | Select-String -Pattern $TargetDevice
if ($IsTargetConnected) {
    $DeviceId = $TargetDevice
} else {
    $DeviceId = ($ConnectedDevices[0].ToString().Split("`t")[0]).Trim()
    Write-Warning "Physical device $TargetDevice not found. Installing on $DeviceId instead."
}

Write-Host "### Installing APK on Device ($DeviceId) ###"
& $AdbPath -s $DeviceId install -r $ApkPath

Write-Host "### Launching App on Device ($DeviceId) ###"
& $AdbPath -s $DeviceId shell am start -n com.anonymous.MobileApp/com.anonymous.MobileApp.MainActivity

Write-Host "### Deployment Finished! ###"
