$BuilderDir = "$PSScriptRoot\.local-builder"
if (!(Test-Path $BuilderDir)) { New-Item -ItemType Directory -Path $BuilderDir }

# URLs
$JdkUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.10_7.zip"
$SdkUrl = "https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip"

# Paths
$JdkZip = "$BuilderDir\jdk.zip"
$SdkZip = "$BuilderDir\sdk.zip"
$JdkPath = "$BuilderDir\jdk"
$SdkPath = "$BuilderDir\android-sdk"

# Download JDK
if (!(Test-Path "$JdkPath\jdk-17.0.2")) {
    Write-Host "Downloading JDK 17..."
    Invoke-WebRequest -Uri $JdkUrl -OutFile $JdkZip
    Write-Host "Extracting JDK..."
    Expand-Archive -Path $JdkZip -DestinationPath $JdkPath -Force
    Remove-Item $JdkZip
}

# Download Android SDK Command-line Tools
if (!(Test-Path "$SdkPath\cmdline-tools")) {
    Write-Host "Downloading Android SDK Command-line Tools..."
    Invoke-WebRequest -Uri $SdkUrl -OutFile $SdkZip
    Write-Host "Extracting Android SDK..."
    Expand-Archive -Path $SdkZip -DestinationPath "$SdkPath\temp" -Force
    
    # Structure requirement: cmdline-tools/latest/bin...
    mkdir "$SdkPath\cmdline-tools\latest" -Force
    Move-Item "$SdkPath\temp\cmdline-tools\*" "$SdkPath\cmdline-tools\latest" -Force
    Remove-Item "$SdkPath\temp" -Recurse -Force
    Remove-Item $SdkZip
}

# Setup Environment for following steps
$env:JAVA_HOME = "$JdkPath\jdk-17.0.2"
$env:ANDROID_HOME = $SdkPath
$env:PATH = "$env:JAVA_HOME\bin;$SdkPath\cmdline-tools\latest\bin;$env:PATH"

# Accept Licenses
Write-Host "Accepting Android Licenses..."
$yes = "y`ny`ny`ny`ny`ny`ny`n"
$yes | & "$SdkPath\cmdline-tools\latest\bin\sdkmanager.bat" --sdk_root=$SdkPath --licenses

# Install Build Tools and Platforms
Write-Host "Installing Android Platform 34 and Build Tools 34.0.0..."
& "$SdkPath\cmdline-tools\latest\bin\sdkmanager.bat" --sdk_root=$SdkPath "platform-tools" "platforms;android-34" "build-tools;34.0.0"

Write-Host "Setup Complete!"
