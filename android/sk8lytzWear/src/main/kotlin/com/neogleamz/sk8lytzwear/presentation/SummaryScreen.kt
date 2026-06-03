package com.neogleamz.sk8lytzwear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

/**
 * SummaryScreen — Post-session summary card displayed for 10 seconds after a session ends.
 *
 * Auto-dismisses after [AUTO_DISMISS_SEC] seconds or immediately on tap.
 * Driven by SUMMARY status pushed from the phone via WearableCommunicationService.
 *
 * @param durationSec   Total session duration in seconds.
 * @param distanceMiles Total distance skated in miles.
 * @param avgSpeedMph   Average speed across the session in mph.
 * @param calories      Active calories burned (kcal).
 * @param peakHR        Peak heart rate recorded (bpm). 0 = unavailable.
 * @param onDismiss     Callback invoked when the card is dismissed (tap or auto-dismiss).
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
    // Auto-dismiss after 10 seconds (phone pushes STOPPED after the same delay)
    LaunchedEffect(Unit) {
        delay(10_000L)
        onDismiss()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrueBlack)
            .clickable { onDismiss() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Text(
            text = "SESSION COMPLETE 🎉",
            color = ElectricCyan,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        // ── Metric rows ───────────────────────────────────────────────────────
        SummaryRow(
            label = "Duration",
            value = formatSummaryDuration(durationSec),
            valueColor = Color.White
        )
        SummaryRow(
            label = "Distance",
            value = String.format("%.2f mi", distanceMiles),
            valueColor = Color.White
        )
        SummaryRow(
            label = "Avg Speed",
            value = String.format("%.1f mph", avgSpeedMph),
            valueColor = ElectricCyan
        )
        SummaryRow(
            label = "Calories",
            value = "$calories kcal",
            valueColor = Color(0xFFFFAA00)   // Amber
        )
        SummaryRow(
            label = "Peak HR",
            value = if (peakHR > 0) "$peakHR bpm" else "-- bpm",
            valueColor = NeonMagenta
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "tap to dismiss",
            color = Color(0xFF555555),
            fontSize = 8.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color(0xFF888888), fontSize = 9.sp)
        Text(
            text = value,
            color = valueColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Formats total seconds as MM:SS (or H:MM:SS when session exceeded 59:59). */
private fun formatSummaryDuration(totalSeconds: Int): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m, s)
    else String.format("%02d:%02d", m, s)
}
