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
echo Generating native Android code (if not already present)...
call npx expo prebuild --platform android

echo.
echo Starting Gradle Local Build for Android (Release APK)
echo You will see all logs and prompts live in this window!
echo.

cd android
call gradlew assembleRelease

echo.
echo Build process finished!
echo Your APK should be located in:
echo MobileApp\android\app\build\outputs\apk\release\app-release.apk
echo.
pause
