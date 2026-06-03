# Implementation Plan: Post-Session Summary

## Goal
Show a post-session summary screen on both watches after the user stops a session. Display total duration, distance, average speed, calories burned, and peak HR before returning to idle.

## Proposed Changes

### Phone — Push Summary Data

#### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)

Modify `endSession()` to push a `SUMMARY` status with aggregated metrics before pushing `STOPPED`:

```typescript
const endSession = useCallback(async () => {
    // 1. Push SUMMARY with final metrics to both watches
    WatchBridge.syncSessionState({
        status: 'SUMMARY',
        totalDuration: telemetry.sessionDurationSec,
        distance: telemetry.sessionDistanceMiles,
        avgSpeed: telemetry.sessionDistanceMiles > 0
            ? (telemetry.sessionDistanceMiles / (telemetry.sessionDurationSec / 3600))
            : 0,
        calories: health.activeCalories ?? 0,
        peakHR: health.peakBpm ?? 0,
    }).catch((err: unknown) =>
        AppLogger.warn('WATCH_BRIDGE', { event: 'summary_push_failed', error: String(err) })
    );

    // 2. Stop session after a brief delay to let watches receive SUMMARY
    setIsSkateSessionActive(false);
    try {
        await AsyncStorage.setItem('@sk8lytz_session_active', 'false');
    } catch (err) {
        AppLogger.error('Failed to save session state to AsyncStorage', err);
    }
    AppLogger.log('APP_LOG', { event: 'session_ended' });
    if (Platform.OS === 'android') {
        notifee.stopForegroundService();
    }

    // 3. Push STOPPED after 10s (auto-dismiss summary on watches)
    setTimeout(() => {
        WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});
    }, 10000);
}, [telemetry, health]);
```

### Wear OS — Summary Screen

#### [MODIFY] [SessionState.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/SessionState.kt)

Add SUMMARY state:

```kotlin
enum class SessionState {
    IDLE,
    ACTIVE,
    SUMMARY
}
```

#### [NEW] [SummaryScreen.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/SummaryScreen.kt)

```kotlin
package com.neogleamz.sk8lytzwear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.neogleamz.sk8lytzwear.presentation.theme.ElectricCyan
import com.neogleamz.sk8lytzwear.presentation.theme.NeonMagenta
import com.neogleamz.sk8lytzwear.presentation.theme.TrueBlack

/**
 * Post-session summary screen — shows final metrics for 10 seconds
 * after the user stops a skating session.
 */
@Composable
fun SummaryScreen(
    durationSec: Int,
    distanceMiles: Double,
    avgSpeedMph: Double,
    calories: Int,
    peakHR: Int,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Header
        Text(
            text = "SESSION COMPLETE 🎉",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )

        // Duration
        SummaryRow(label = "Duration", value = formatDuration(durationSec))

        // Distance
        SummaryRow(label = "Distance", value = String.format("%.2f mi", distanceMiles))

        // Avg Speed
        SummaryRow(label = "Avg Speed", value = String.format("%.1f mph", avgSpeedMph))

        // Calories
        SummaryRow(label = "Calories", value = "$calories kcal", valueColor = Color(0xFFFFAA00))

        // Peak HR
        SummaryRow(
            label = "Peak HR",
            value = if (peakHR > 0) "$peakHR bpm" else "-- bpm",
            valueColor = NeonMagenta
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color = Color.White
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

private fun formatDuration(totalSeconds: Int): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, s)
    else String.format("%02d:%02d", m, s)
}
```

#### [MODIFY] [DashboardScreen.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt)

Add summary state variables and display logic. Add state vars:

```kotlin
var summaryDuration by remember { mutableStateOf(0) }
var summaryDistance by remember { mutableStateOf(0.0) }
var summaryAvgSpeed by remember { mutableStateOf(0.0) }
var summaryCalories by remember { mutableStateOf(0) }
var summaryPeakHR by remember { mutableStateOf(0) }
```

Update the DataClient listener to capture summary data and set SUMMARY state. In `WearableCommunicationService`, parse the summary fields from the DataMap.

Add SUMMARY case to the `when` block:

```kotlin
when (sessionState) {
    SessionState.IDLE -> IdleView(onStart = { ... })
    SessionState.ACTIVE -> ActiveView(...)
    SessionState.SUMMARY -> SummaryScreen(
        durationSec = summaryDuration,
        distanceMiles = summaryDistance,
        avgSpeedMph = summaryAvgSpeed,
        calories = summaryCalories,
        peakHR = summaryPeakHR,
        onDismiss = { sessionState = SessionState.IDLE }
    )
}
```

#### [MODIFY] [WearableCommunicationService.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt)

Parse `SUMMARY` status and extract metrics from DataMap:

```kotlin
currentState = when (status) {
    "ACTIVE" -> SessionState.ACTIVE
    "SUMMARY" -> SessionState.SUMMARY
    else -> SessionState.IDLE
}

// Extract summary metrics when status is SUMMARY
if (status == "SUMMARY") {
    currentSummaryDuration = dataMap.getInt("totalDuration", 0)
    currentSummaryDistance = dataMap.getDouble("distance", 0.0)
    currentSummaryAvgSpeed = dataMap.getDouble("avgSpeed", 0.0)
    currentSummaryCalories = dataMap.getInt("calories", 0)
    currentSummaryPeakHR = dataMap.getInt("peakHR", 0)
}
```

Add companion fields for summary data.

### watchOS — Summary View

#### [MODIFY] [ContentView.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/ContentView.swift)

**Step 1:** Add summary state:

```swift
@State private var showingSummary = false
@State private var summaryDuration: Int = 0
@State private var summaryDistance: Double = 0
@State private var summaryAvgSpeed: Double = 0
@State private var summaryCalories: Int = 0
@State private var summaryPeakHR: Int = 0
```

**Step 2:** Modify `stopSession()` to show summary:

```swift
private func stopSession() {
    // Capture metrics before stopping
    summaryDuration = elapsedSeconds
    summaryCalories = Int(healthManager.activeCalories)
    summaryPeakHR = Int(healthManager.currentHeartRate) // approximate — use peak if tracked
    showingSummary = true

    healthManager.stopWorkout()
    watchManager.sendStopSession()

    // Auto-dismiss after 10 seconds
    DispatchQueue.main.asyncAfter(deadline: .now() + 10) {
        showingSummary = false
    }
}
```

**Step 3:** Add `summaryView` computed property:

```swift
private var summaryView: some View {
    VStack(spacing: 8) {
        Text("SESSION COMPLETE 🎉")
            .font(.caption)
            .foregroundColor(.cyan)

        HStack {
            Text("Duration").font(.caption2).foregroundColor(.secondary)
            Spacer()
            Text(formatElapsed(summaryDuration)).font(.callout).bold()
        }
        HStack {
            Text("Calories").font(.caption2).foregroundColor(.secondary)
            Spacer()
            Text("\(summaryCalories) kcal").font(.callout).bold().foregroundColor(.orange)
        }
        HStack {
            Text("Peak HR").font(.caption2).foregroundColor(.secondary)
            Spacer()
            Text(summaryPeakHR > 0 ? "\(summaryPeakHR) bpm" : "-- bpm")
                .font(.callout).bold().foregroundColor(.red)
        }
    }
    .padding()
    .onTapGesture { showingSummary = false }
}
```

**Step 4:** Update body to show summary:

```swift
var body: some View {
    VStack(spacing: 16) {
        if showingSummary {
            summaryView
        } else if watchManager.isSessionActive {
            activeSessionView
        } else {
            idleView
        }
    }
    ...
}
```

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest pass

### Manual Verification
- Start session → skate → stop → watch should show summary for 10 seconds
- Summary should display duration, distance, avg speed, calories, peak HR
- After 10 seconds → auto-dismiss to idle view
- Tap summary → immediate dismiss
- Verify phone HUD/history still records the full session
