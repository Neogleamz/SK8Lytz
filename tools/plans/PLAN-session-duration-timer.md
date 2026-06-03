# Implementation Plan: Session Duration Timer

## Goal
Show elapsed session time (MM:SS or HH:MM:SS) on both watchOS and Wear OS active session screens. Users universally expect a duration timer on fitness watch apps — Strava/Nike both show this.

## Proposed Changes

### watchOS — ContentView.swift

#### [MODIFY] [ContentView.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/ContentView.swift)

**Step 1:** Add elapsed time state and timer to `ContentView`:

```swift
// Add these @State properties below the existing @ObservedObject declarations (after line 6):
@State private var elapsedSeconds: Int = 0
@State private var sessionTimer: Timer?
```

**Step 2:** Add timer start/stop logic. Modify `startSession()` (line ~98):

```swift
private func startSession() {
    elapsedSeconds = 0
    sessionTimer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { _ in
        elapsedSeconds += 1
    }
    healthManager.startWorkout()
    watchManager.sendStartSession()
}
```

**Step 3:** Modify `stopSession()` (line ~103):

```swift
private func stopSession() {
    sessionTimer?.invalidate()
    sessionTimer = nil
    healthManager.stopWorkout()
    watchManager.sendStopSession()
}
```

**Step 4:** Add elapsed time display in `activeSessionView` (after the speed VStack, before the Spacer at line ~38):

```swift
// Elapsed time — between speed and HR row
Text(formatElapsed(elapsedSeconds))
    .font(.system(size: 20, weight: .medium, design: .monospaced))
    .foregroundColor(.white)
```

**Step 5:** Add the formatter helper at the bottom of the struct:

```swift
private func formatElapsed(_ totalSeconds: Int) -> String {
    let h = totalSeconds / 3600
    let m = (totalSeconds % 3600) / 60
    let s = totalSeconds % 60
    if h > 0 {
        return String(format: "%d:%02d:%02d", h, m, s)
    }
    return String(format: "%02d:%02d", m, s)
}
```

---

### Wear OS — DashboardScreen.kt

#### [MODIFY] [DashboardScreen.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt)

**Step 1:** Add elapsed time state. After `var calories by remember { mutableStateOf(0) }` (line ~55):

```kotlin
var elapsedSeconds by remember { mutableStateOf(0) }
```

**Step 2:** Add a `LaunchedEffect` that ticks every second when ACTIVE. After the `DisposableEffect(Unit)` block (after line ~69):

```kotlin
// Tick elapsed time every second while session is ACTIVE
LaunchedEffect(sessionState) {
    elapsedSeconds = 0
    while (sessionState == SessionState.ACTIVE) {
        kotlinx.coroutines.delay(1000L)
        elapsedSeconds++
    }
}
```

**Step 3:** Pass `elapsedSeconds` to `ActiveView`. Modify the call (line ~84):

```kotlin
SessionState.ACTIVE -> ActiveView(
    speed = speed,
    heartRate = heartRate,
    calories = calories,
    elapsedSeconds = elapsedSeconds,
    onStop = { ... }
)
```

**Step 4:** Update `ActiveView` signature and add timer display (line ~138):

```kotlin
@Composable
private fun ActiveView(
    speed: Double,
    heartRate: Int,
    calories: Int,
    elapsedSeconds: Int,
    onStop: () -> Unit
) {
    Column(...) {
        // Session active indicator
        Text("SESSION ACTIVE", ...)

        // Elapsed time
        Text(
            text = formatElapsed(elapsedSeconds),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )

        // Speed chip (existing)
        TelemetryChip(label = "SPEED", ...)
        ...
    }
}
```

**Step 5:** Add top-level format helper:

```kotlin
private fun formatElapsed(totalSeconds: Int): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, s)
    else String.format("%02d:%02d", m, s)
}
```

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest must pass (no TS files touched, but validates no regressions)

### Manual Verification
- watchOS: Start session → timer should tick MM:SS. After 1 hour → HH:MM:SS. Stop → timer resets.
- Wear OS: Same behavior. Verify `LaunchedEffect` resets on state change.
