$ProjectRoot = Get-Location
$AdbPath = "C:\Neogleamz\AG_SK8Lytz_App\Sk8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"
$ApkPath = Join-Path $ProjectRoot "android\app\build\outputs\apk\release\SK8Lytz.apk"
$PackageName = "com.neogleamz.sk8lytz"

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
    Write-Warning "Physical device $TargetDevice not found. Using $DeviceId instead."
}

if (-not (Test-Path $ApkPath)) {
    Write-Error "APK not found at $ApkPath. Please run build-apk.ps1 first."
    exit 1
}

Write-Host "### Performing Clean Deployment on Device ($DeviceId) ###"
Write-Host "### Uninstalling $PackageName... ###"
& $AdbPath -s $DeviceId uninstall $PackageName | Out-Null

Write-Host "### Installing New APK... ###"
& $AdbPath -s $DeviceId install -r $ApkPath
if ($LASTEXITCODE -ne 0) {
    Write-Error "### Install FAILED! ###"
    exit $LASTEXITCODE
}

Write-Host "### Launching SK8Lytz... ###"
& $AdbPath -s $DeviceId shell am start -n $PackageName/.MainActivity
if ($LASTEXITCODE -ne 0) {
    Write-Warning "### Launch failed. You may need to open the app manually. ###"
}

Write-Host "### Deployment Finished Successfully! ###"
