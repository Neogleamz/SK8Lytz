package com.neogleamz.sk8lytzwear.services

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.neogleamz.sk8lytzwear.presentation.SessionState
import org.json.JSONObject

/**
 * WearableCommunicationService — phone → watch data receiver.
 *
 * Receives DataClient state changes (persistent session state) and
 * MessageClient metric updates (real-time speed/HR/calories) from the
 * phone-side bridge module (Sk8lytzWatchBridgeModule.kt).
 *
 * Path contract (must match phone-side):
 *   /sk8lytz/state    — DataClient: session ACTIVE/STOPPED
 *   /sk8lytz/metrics  — MessageClient: live speed/HR/calories
 */
class WearableCommunicationService : WearableListenerService() {

    companion object {
        private const val TAG = "WearCommsService"
        private const val PATH_STATE = "/sk8lytz/state"
        private const val PATH_METRICS = "/sk8lytz/metrics"

        // Thread-safe listener list for DashboardScreen to subscribe to
        private val stateListeners = mutableListOf<(SessionState, Double, Int, Int) -> Unit>()
        private var currentSpeed = 0.0
        private var currentHR = 0
        private var currentCalories = 0
        private var currentState = SessionState.IDLE

        fun addStateListener(listener: (SessionState, Double, Int, Int) -> Unit) {
            synchronized(stateListeners) {
                stateListeners.add(listener)
                // Immediately replay current state to new subscriber
                listener(currentState, currentSpeed, currentHR, currentCalories)
            }
        }

        fun removeStateListener(listener: (SessionState, Double, Int, Int) -> Unit) {
            synchronized(stateListeners) {
                stateListeners.remove(listener)
            }
        }

        private fun notifyListeners() {
            synchronized(stateListeners) {
                stateListeners.forEach { it(currentState, currentSpeed, currentHR, currentCalories) }
            }
        }
    }

    /**
     * Handles DataClient state changes from the phone's syncSessionState().
     * DataItems are persistent and survive watch disconnection → reconnection.
     */
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == PATH_STATE) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val status = dataMap.getString("status", "STOPPED")
                val speed = dataMap.getDouble("speed", 0.0)
                val hr = dataMap.getInt("heartRate", 0)
                val cal = dataMap.getInt("calories", 0)

                val previousState = currentState
                currentState = if (status == "ACTIVE") SessionState.ACTIVE else SessionState.IDLE
                currentSpeed = speed
                currentHR = hr
                currentCalories = cal

                // Start/stop HealthTracker when phone drives the session state
                if (currentState == SessionState.ACTIVE && previousState != SessionState.ACTIVE) {
                    HealthTracker.startTracking(this@WearableCommunicationService)
                } else if (currentState == SessionState.IDLE && previousState == SessionState.ACTIVE) {
                    HealthTracker.stopTracking()
                }

                Log.d(TAG, "DataClient state update: $status | speed=$speed hr=$hr cal=$cal")
                notifyListeners()
            }
        }
    }

    /**
     * Handles MessageClient metric pushes from the phone's sendMetricUpdate().
     * Messages are real-time, best-effort (not persisted).
     */
    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            PATH_METRICS -> {
                runCatching {
                    val json = JSONObject(String(messageEvent.data, Charsets.UTF_8))
                    currentSpeed = json.optDouble("speed", currentSpeed)
                    currentHR = json.optInt("heartRate", currentHR)
                    currentCalories = json.optInt("calories", currentCalories)

                    Log.d(TAG, "Metric update: speed=$currentSpeed hr=$currentHR cal=$currentCalories")
                    notifyListeners()
                }.onFailure {
                    Log.e(TAG, "Failed to parse metric message: ${it.message}", it)
                }
            }
            else -> Log.w(TAG, "Unknown message path: ${messageEvent.path}")
        }
    }
}
