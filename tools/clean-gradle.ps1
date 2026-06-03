$MasterRoot = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$JdkPath = Join-Path $MasterRoot ".local-builder\jdk\jdk-17.0.10+7"
$SdkPath = Join-Path $MasterRoot ".local-builder\android-sdk"

# Set session environment
$env:JAVA_HOME = $JdkPath
$env:ANDROID_HOME = $SdkPath
$env:PATH = "$JdkPath\bin;$env:PATH"

# Move to android directory
Set-Location android

Write-Host "### Running Gradle Clean ###"
.\gradlew.bat clean

Set-Location ..
Write-Host "### Gradle Clean Finished! ###"
