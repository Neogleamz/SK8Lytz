#!/usr/bin/env pwsh
# start-web-demo.ps1 — Starts the Expo web demo under PM2 supervision
# PM2 will restart Metro automatically if it crashes.
# Run this script instead of `npx expo start` directly.

Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$env:EXPO_NO_TELEMETRY = "1"
npx expo start --web
