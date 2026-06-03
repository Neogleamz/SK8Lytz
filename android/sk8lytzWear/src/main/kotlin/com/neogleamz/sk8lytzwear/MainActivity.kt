package com.neogleamz.sk8lytzwear

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.neogleamz.sk8lytzwear.presentation.DashboardScreen
import com.neogleamz.sk8lytzwear.presentation.theme.SK8LytzWearTheme

/**
 * MainActivity — Wear OS entry point.
 * Launches the single-screen dashboard (DashboardScreen).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request required runtime permissions
        val permissions = mutableListOf(
            android.Manifest.permission.BODY_SENSORS,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        requestPermissions(permissions.toTypedArray(), 100)

        setContent {
            SK8LytzWearTheme {
                DashboardScreen()
            }
        }
    }
}
