# Implementation Plan: Wear OS Always-On Display

## Goal
Add Always-On Display (ambient mode) for Wear OS showing dim speed/time during active sessions. Prevents the watch from going completely dark while skating — critical for glanceability.

## Proposed Changes

### Wear OS — MainActivity.kt

#### [MODIFY] [MainActivity.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/MainActivity.kt)

**Step 1:** Add `AmbientModeSupport` to the activity. Change the class declaration and add ambient callback:

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.ambient.AmbientModeSupport
import com.neogleamz.sk8lytzwear.presentation.DashboardScreen
import com.neogleamz.sk8lytzwear.presentation.theme.SK8LytzWearTheme

class MainActivity : ComponentActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ambientController = AmbientModeSupport.attach(this)

        setContent {
            SK8LytzWearTheme {
                DashboardScreen()
            }
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle?) {
                super.onEnterAmbient(ambientDetails)
                DashboardScreen.isAmbientMode = true
            }

            override fun onExitAmbient() {
                super.onExitAmbient()
                DashboardScreen.isAmbientMode = false
            }

            override fun onUpdateAmbient() {
                super.onUpdateAmbient()
                // System calls this ~1/min in ambient mode
                // DashboardScreen reads live values from WearableCommunicationService
            }
        }
    }
}
```

### Wear OS — DashboardScreen.kt

#### [MODIFY] [DashboardScreen.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt)

**Step 1:** Add ambient mode static flag in the companion (top of file, inside DashboardScreen or as a top-level object):

```kotlin
// Add at top level, before the DashboardScreen composable:
object DashboardScreenState {
    var isAmbientMode by mutableStateOf(false)
}
```

> Update `MainActivity` references to use `DashboardScreenState.isAmbientMode` instead.

**Step 2:** Add ambient view composable:

```kotlin
@Composable
private fun AmbientView(speed: Double, elapsedSeconds: Int) {
    // Monochrome, no animations, burn-in safe
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Speed — large, white, monospace
        Text(
            text = String.format("%.1f", speed),
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = "mph",
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Elapsed time
        Text(
            text = formatElapsed(elapsedSeconds),
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
```

**Step 3:** In the main `DashboardScreen` composable, wrap the `when(sessionState)` block to check ambient mode:

```kotlin
Box(...) {
    if (DashboardScreenState.isAmbientMode && sessionState == SessionState.ACTIVE) {
        AmbientView(speed = speed, elapsedSeconds = elapsedSeconds)
    } else {
        when (sessionState) {
            SessionState.IDLE -> IdleView(...)
            SessionState.ACTIVE -> ActiveView(...)
        }
    }
}
```

### AndroidManifest.xml

#### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/AndroidManifest.xml)

Add `android:keepScreenOn="true"` to the `<activity>` tag if not present (ambient mode requires this to work properly during active sessions):

```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:keepScreenOn="true"
    android:taskAffinity="">
```

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest pass

### Manual Verification
- Start session on watch → let screen timeout → should show dim monochrome speed/time
- Raise wrist / tap screen → should restore full-color ActiveView
- When IDLE, ambient mode should show nothing (screen off is fine)
