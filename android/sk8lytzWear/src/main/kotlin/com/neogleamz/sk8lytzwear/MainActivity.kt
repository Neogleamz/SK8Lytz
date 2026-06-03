package com.neogleamz.sk8lytzwear

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
        setContent {
            SK8LytzWearTheme {
                DashboardScreen()
            }
        }
    }
}
