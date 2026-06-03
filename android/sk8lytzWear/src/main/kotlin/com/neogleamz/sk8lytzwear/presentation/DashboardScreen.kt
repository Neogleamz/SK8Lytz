package com.neogleamz.sk8lytzwear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay
import com.neogleamz.sk8lytzwear.presentation.theme.ElectricCyan
import com.neogleamz.sk8lytzwear.presentation.theme.NeonMagenta
import com.neogleamz.sk8lytzwear.presentation.theme.TrueBlack
import com.neogleamz.sk8lytzwear.services.HealthTracker
import com.neogleamz.sk8lytzwear.services.WearableCommunicationService
import com.neogleamz.sk8lytzwear.services.OngoingActivityManager

/**
 * DashboardScreen — Single-screen Wear OS UI for SK8Lytz.
 *
 * Two states:
 *   IDLE   → SK8Lytz logo + "Start Session" button
 *   ACTIVE → Live speed/HR/calories chips + "Stop" button
 *
 * State is driven by DataClient updates from the phone app
 * OR local watch button taps (optimistic command via WearMessageSender).
 */
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    var sessionState by remember { mutableStateOf(SessionState.IDLE) }
    var speed by remember { mutableStateOf(0.0) }
    var heartRate by remember { mutableStateOf(0) }
    var calories by remember { mutableStateOf(0) }
    // Live elapsed duration — derived from phone-authoritative anchor timestamp
    var elapsedSeconds by remember { mutableStateOf(0) }

    // Subscribe to state changes pushed from the phone via DataClient
    DisposableEffect(Unit) {
        val listener: (SessionState, Double, Int, Int) -> Unit = { state, spd, hr, cal ->
            sessionState = state
            speed = spd
            heartRate = hr
            calories = cal
        }
        WearableCommunicationService.addStateListener(listener)

        // Wire HealthTracker live updates into composable state
        HealthTracker.onHealthUpdate = { hr, cal ->
            if (hr > 0) heartRate = hr
            if (cal > 0) calories = cal
        }

        onDispose {
            WearableCommunicationService.removeStateListener(listener)
            HealthTracker.onHealthUpdate = null
        }
    }

    // Tick elapsed time every second while ACTIVE, anchored to phone-authoritative start time.
    // Using System.currentTimeMillis() means the timer instantly recovers the correct value
    // if the watch app crashes or the user opens it mid-session.
    LaunchedEffect(sessionState) {
        if (sessionState == SessionState.ACTIVE) {
            // Set local anchor if phone hasn't synced startTime yet
            if (WearableCommunicationService.sessionStartTimeMs == 0L) {
                WearableCommunicationService.sessionStartTimeMs = System.currentTimeMillis()
            }
            while (sessionState == SessionState.ACTIVE) {
                val startMs = WearableCommunicationService.sessionStartTimeMs
                elapsedSeconds = if (startMs > 0L) {
                    ((System.currentTimeMillis() - startMs) / 1000).toInt()
                } else 0
                delay(1000L)
            }
        } else {
            elapsedSeconds = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack),
        contentAlignment = Alignment.Center
    ) {
        when (sessionState) {
            SessionState.IDLE -> IdleView(
                onStart = {
                    sessionState = SessionState.ACTIVE // Optimistic UI
                    // Anchor locally when session starts from the watch
                    WearableCommunicationService.sessionStartTimeMs = System.currentTimeMillis()
                    HealthTracker.startTracking(context)
                    OngoingActivityManager.startOngoingActivity(context)
                    WearMessageSender.sendCommand(context, "START_SESSION")
                }
            )
            SessionState.ACTIVE -> ActiveView(
                speed = speed,
                heartRate = heartRate,
                calories = calories,
                elapsedSeconds = elapsedSeconds,
                onStop = {
                    sessionState = SessionState.IDLE // Optimistic UI
                    WearableCommunicationService.sessionStartTimeMs = 0L
                    HealthTracker.stopTracking()
                    OngoingActivityManager.stopOngoingActivity(context)
                    WearMessageSender.sendCommand(context, "STOP_SESSION")
                }
            )
        }
    }
}

@Composable
private fun IdleView(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App title
        Text(
            text = "SK8Lytz",
            color = ElectricCyan,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // Inline skate emoji as icon
        Text(
            text = "🛼",
            fontSize = 36.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Start button
        Button(
            onClick = onStart,
            modifier = Modifier.size(width = 120.dp, height = 44.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = ElectricCyan)
        ) {
            Text(
                text = "▶  START",
                color = TrueBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ActiveView(
    speed: Double,
    heartRate: Int,
    calories: Int,
    elapsedSeconds: Int,
    onStop: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Session active indicator
        Text(
            text = "SESSION ACTIVE",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        // Elapsed duration — anchored to phone-authoritative start time
        Text(
            text = formatElapsed(elapsedSeconds),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Monospace
        )

        TelemetryChip(
            label = "SPEED",
            value = String.format("%.1f", speed),
            unit = "mph",
            valueColor = ElectricCyan
        )

        // HR and Calories row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TelemetryChip(
                label = "❤️ HR",
                value = if (heartRate > 0) heartRate.toString() else "--",
                unit = "bpm",
                valueColor = NeonMagenta
            )
            TelemetryChip(
                label = "🔥 CAL",
                value = calories.toString(),
                unit = "kcal",
                valueColor = Color(0xFFFFAA00)
            )
        }

        // Stop button
        Button(
            onClick = onStop,
            modifier = Modifier.size(width = 100.dp, height = 36.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF3B3B))
        ) {
            Text(
                text = "⏹  STOP",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TelemetryChip(
    label: String,
    value: String,
    unit: String,
    valueColor: Color
) {
    Chip(
        onClick = { },
        modifier = Modifier.height(48.dp),
        colors = ChipDefaults.chipColors(backgroundColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        label = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = label, color = Color.Gray, fontSize = 9.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        color = valueColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " $unit",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
        }
    )
}

/** Formats a raw second count into MM:SS (or H:MM:SS past 59:59). */
private fun formatElapsed(totalSeconds: Int): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, s)
    else String.format("%02d:%02d", m, s)
}
