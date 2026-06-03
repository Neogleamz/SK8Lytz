# Implementation Plan: Wear OS OngoingActivity

## Goal
Show a system-level tappable indicator in the Wear OS watch face tray during active sessions — identical to how Strava shows a green dot during a run.

## Proposed Changes

### Wear OS — OngoingActivity Integration

The `wear-ongoing` dependency is already in `build.gradle`. No new dependency needed.

#### [MODIFY] [DashboardScreen.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt)

**Step 1:** Add imports at top of file:

```kotlin
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.neogleamz.sk8lytzwear.MainActivity
```

**Step 2:** Add OngoingActivity start/stop helper functions (top-level, after `formatElapsed`):

```kotlin
private const val ONGOING_CHANNEL_ID = "sk8lytz_session"
private const val ONGOING_NOTIFICATION_ID = 101

private fun startOngoingActivity(context: android.content.Context) {
    // Create notification channel (idempotent)
    val channel = NotificationChannel(
        ONGOING_CHANNEL_ID,
        "Skate Session",
        NotificationManager.IMPORTANCE_LOW
    ).apply {
        description = "Active skating session indicator"
    }
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)

    // PendingIntent to reopen the app
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Build the notification
    val notification = NotificationCompat.Builder(context, ONGOING_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_media_play)
        .setContentTitle("SK8Lytz")
        .setContentText("Skating session active")
        .setOngoing(true)
        .setCategory(NotificationCompat.CATEGORY_WORKOUT)
        .setContentIntent(pendingIntent)
        .build()

    // Build OngoingActivity
    val ongoingActivity = OngoingActivity.Builder(context, ONGOING_NOTIFICATION_ID, notification)
        .setStaticIcon(android.R.drawable.ic_media_play)
        .setTouchIntent(pendingIntent)
        .setStatus(
            Status.Builder()
                .addTemplate("Skating")
                .build()
        )
        .build()

    ongoingActivity.apply(context)
    notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
}

private fun stopOngoingActivity(context: android.content.Context) {
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.cancel(ONGOING_NOTIFICATION_ID)
}
```

**Step 3:** Call from the `onStart` and `onStop` lambdas in `DashboardScreen()`:

In the `IdleView` `onStart` lambda:
```kotlin
onStart = {
    sessionState = SessionState.ACTIVE
    HealthTracker.startTracking(context)
    startOngoingActivity(context)  // ← ADD THIS
    WearMessageSender.sendCommand(context, "START_SESSION")
}
```

In the `ActiveView` `onStop` lambda:
```kotlin
onStop = {
    sessionState = SessionState.IDLE
    HealthTracker.stopTracking()
    stopOngoingActivity(context)  // ← ADD THIS
    WearMessageSender.sendCommand(context, "STOP_SESSION")
}
```

#### [MODIFY] [WearableCommunicationService.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt)

Also start/stop OngoingActivity when the phone drives the session. In `onDataChanged()`, after the HealthTracker start/stop block:

```kotlin
// Start/stop OngoingActivity when phone drives session
if (currentState == SessionState.ACTIVE && previousState != SessionState.ACTIVE) {
    HealthTracker.startTracking(this@WearableCommunicationService)
    startOngoingActivity(this@WearableCommunicationService)  // ← ADD
} else if (currentState == SessionState.IDLE && previousState == SessionState.ACTIVE) {
    HealthTracker.stopTracking()
    stopOngoingActivity(this@WearableCommunicationService)  // ← ADD
}
```

> **Note:** Import `startOngoingActivity` / `stopOngoingActivity` from the DashboardScreen file, or extract them into a shared `OngoingActivityManager` object.

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest pass

### Manual Verification
- Start session on watch → navigate to watch face → green dot + "Skating" chip should appear
- Tap the chip → should reopen SK8Lytz app
- Stop session → green dot disappears
- Start session from phone → same green dot should appear on watch face
