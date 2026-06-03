package com.neogleamz.sk8lytzwear.services

import android.content.Context
import android.util.Log
import androidx.health.services.client.ExerciseClient
import androidx.health.services.client.ExerciseUpdateCallback
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseLapSummary
import androidx.health.services.client.data.ExerciseType
import androidx.health.services.client.data.ExerciseUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.guava.await
import com.neogleamz.sk8lytzwear.presentation.WearMessageSender
import com.neogleamz.sk8lytzwear.presentation.SessionState
import com.neogleamz.sk8lytzwear.services.WearableCommunicationService

/**
 * HealthTracker — wraps Health Services ExerciseClient for live HR, calories,
 * and distance tracking during an active skating session.
 *
 * Uses ExerciseType.INLINE_SKATING (type 101) for accurate MET/calorie estimation.
 * Data flows to DashboardScreen via the companion object's callback.
 */
object HealthTracker {

    private const val TAG = "HealthTracker"
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var exerciseClient: ExerciseClient? = null
    private var trackingContext: Context? = null
    private var isTracking = false

    /** Callback for live health data — consumed by DashboardScreen */
    var onHealthUpdate: ((heartRate: Int, calories: Int) -> Unit)? = null

    /**
     * Start a Health Services exercise session (INLINE_SKATING).
     * Requests HEART_RATE_BPM and CALORIES_TOTAL data types.
     */
    fun startTracking(context: Context) {
        if (isTracking) return
        
        // Prevent NoClassDefFoundError on Wear OS 2 (API < 30)
        if (android.os.Build.VERSION.SDK_INT < 30) {
            Log.w(TAG, "Health Services API requires Wear OS 3+ (API 30+). Skipping tracking.")
            isTracking = true
            return
        }
        
        trackingContext = context.applicationContext
        scope.launch {
            runCatching {
                val client = HealthServices.getClient(context).exerciseClient
                exerciseClient = client

                // Removed capability check due to Health Services alpha API differences.
                // We'll catch UnsupportedExerciseTypeException in runCatching if not supported.
                val exerciseType = ExerciseType.INLINE_SKATING

                Log.d(TAG, "Using exercise type: $exerciseType")

                val config = ExerciseConfig.builder(exerciseType)
                    .setDataTypes(
                        setOf(
                            DataType.HEART_RATE_BPM,
                            DataType.CALORIES_TOTAL
                        )
                    )
                    .setIsAutoPauseAndResumeEnabled(false)
                    .build()

                client.setUpdateCallback(exerciseCallback)
                client.startExerciseAsync(config).await()
                isTracking = true
                Log.d(TAG, "Exercise session started")
            }.onFailure {
                Log.e(TAG, "Failed to start exercise tracking: ${it.message}", it)
            }
        }
    }

    /** Stop the Health Services exercise session. */
    fun stopTracking() {
        if (!isTracking) return
        
        if (android.os.Build.VERSION.SDK_INT < 30) {
            isTracking = false
            return
        }
        
        scope.launch {
            runCatching {
                exerciseClient?.endExerciseAsync()?.await()
                exerciseClient?.clearUpdateCallbackAsync(exerciseCallback)?.await()
                isTracking = false
                trackingContext = null
                Log.d(TAG, "Exercise session ended")
            }.onFailure {
                Log.e(TAG, "Failed to stop exercise tracking: ${it.message}", it)
            }
        }
    }

    private val exerciseCallback = object : ExerciseUpdateCallback {
        override fun onExerciseUpdateReceived(update: ExerciseUpdate) {
            val hrPoints = update.latestMetrics.getData(DataType.HEART_RATE_BPM)
            val calTotal = update.latestMetrics.getData(DataType.CALORIES_TOTAL)

            val hr = hrPoints.lastOrNull()?.value?.toInt() ?: 0
            val cal = when (calTotal) {
                is Number -> calTotal.toInt()
                is List<*> -> (calTotal.lastOrNull() as? androidx.health.services.client.data.SampleDataPoint<*>)?.value?.let { (it as? Number)?.toInt() } ?: 0
                else -> 0
            }

            if (hr > 0 || cal > 0) {
                onHealthUpdate?.invoke(hr, cal)
                // Relay health data back to the phone (5s throttled)
                trackingContext?.let { ctx ->
                    val statusStr = when (WearableCommunicationService.currentState) {
                        SessionState.ACTIVE -> "ACTIVE"
                        SessionState.PAUSED -> "PAUSED"
                        SessionState.SUMMARY -> "SUMMARY"
                        else -> "IDLE"
                    }
                    WearMessageSender.sendHealthUpdate(ctx, hr, cal, statusStr, WearableCommunicationService.sessionStartTimeMs)
                }
            }
        }

        override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) {
            // Not used for skating — no lap concept
        }

        override fun onAvailabilityChanged(dataType: DataType<*, *>, availability: Availability) {
            Log.d(TAG, "Availability changed for $dataType")
        }

        override fun onRegistered() {
            Log.d(TAG, "Exercise callback registered")
        }

        override fun onRegistrationFailed(throwable: Throwable) {
            Log.e(TAG, "Exercise callback registration failed: ${throwable.message}", throwable)
        }
    }
}
