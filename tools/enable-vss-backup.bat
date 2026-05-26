@echo off
echo ============================================
echo  SK8Lytz - Enable Windows Shadow Copies
echo ============================================
echo.

:: Check for admin rights
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo ERROR: This script must be run as Administrator!
    echo Right-click the file and choose "Run as administrator"
    pause
    exit /b 1
)

echo [1/4] Starting VSS service...
sc config VSS start= auto
net start VSS 2>nul
echo Done.

echo.
echo [2/4] Setting up shadow storage on C: (10GB max)...
vssadmin add shadowstorage /for=C: /on=C: /maxsize=10GB 2>nul
if %errorLevel% neq 0 (
    echo Storage already configured - updating size...
    vssadmin resize shadowstorage /for=C: /on=C: /maxsize=10GB
)
echo Done.

echo.
echo [3/4] Creating snapshot RIGHT NOW...
vssadmin create shadow /for=C:
echo Done.

echo.
echo [4/4] Setting up daily automatic snapshot at 8am...
schtasks /create /tn "SK8Lytz-DailyShadowCopy" /tr "vssadmin create shadow /for=C:" /sc daily /st 08:00 /ru SYSTEM /f
echo Done.

echo.
echo ============================================
echo  SUCCESS! Existing shadow copies:
echo ============================================
vssadmin list shadows /for=C:

echo.
echo Shadow copies are now enabled and will run daily at 8am.
echo To restore a file: Right-click it ^> Properties ^> Previous Versions
pause
