@echo off
echo =======================================================
echo          SK8LYTZ LOCAL APK BUILD LAUNCHER
echo =======================================================

:: Set up isolated environment variables
set "BUILDER_DIR=%~dp0.local-builder"
set "JAVA_HOME=%BUILDER_DIR%\jdk\jdk-17.0.2"
set "ANDROID_HOME=%BUILDER_DIR%\android-sdk"

:: Update Path to use isolated Java and Android SDK tools
set "PATH=%PATH%;%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools"

echo JAVA_HOME is set to: %JAVA_HOME%
echo ANDROID_HOME is set to: %ANDROID_HOME%
echo.
echo Starting Expo Local Build for Android (Profile: preview)
echo You will see all logs and prompts live in this window!
echo.

call eas build --platform android --local --profile preview

echo.
echo Build process finished!
pause
