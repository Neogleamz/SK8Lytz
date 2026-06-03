package com.neogleamz.sk8lytzwear

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.wear.ambient.AmbientModeSupport
import com.neogleamz.sk8lytzwear.presentation.DashboardScreen
import com.neogleamz.sk8lytzwear.presentation.DashboardScreenState
import com.neogleamz.sk8lytzwear.presentation.SessionState
import com.neogleamz.sk8lytzwear.presentation.theme.SK8LytzWearTheme
import com.neogleamz.sk8lytzwear.services.WearableCommunicationService

/**
 * MainActivity — Wear OS entry point.
 * Launches the single-screen dashboard (DashboardScreen) and handles Always-On Display.
 */
class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private lateinit var ambientController: AmbientModeSupport.AmbientController

    // Track session state changes to dynamically toggle FLAG_KEEP_SCREEN_ON.
    // This allows the watch screen to sleep normally when IDLE (saving battery),
    // but keeps it awake (or ambient) when ACTIVE during a session.
    private val stateListener = { state: SessionState, _: Double, _: Int, _: Int ->
        runOnUiThread {
            if (state == SessionState.ACTIVE) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Attach Ambient Mode support
        ambientController = AmbientModeSupport.attach(this)

        // Register session state observer for screen-on policy
        WearableCommunicationService.addStateListener(stateListener)

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

    override fun onDestroy() {
        WearableCommunicationService.removeStateListener(stateListener)
        super.onDestroy()
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle?) {
                super.onEnterAmbient(ambientDetails)
                DashboardScreenState.isAmbientMode = true
            }

            override fun onExitAmbient() {
                super.onExitAmbient()
                DashboardScreenState.isAmbientMode = false
            }

            override fun onUpdateAmbient() {
                super.onUpdateAmbient()
                // Called ~1/min by Wear OS during AOD to refresh content
            }
        }
    }
}
