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
import kotlinx.coroutines.tasks.await
import com.neogleamz.sk8lytzwear.presentation.WearMessageSender

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
        trackingContext = context.applicationContext
        scope.launch {
            runCatching {
                val client = HealthServices.getClient(context).exerciseClient
                exerciseClient = client

                // Check capabilities — does this watch support inline skating?
                val capabilities = client.getCapabilitiesAsync().await()
                val supported = capabilities.supportedExerciseTypes.contains(ExerciseType.INLINE_SKATING)
                val exerciseType = if (supported) ExerciseType.INLINE_SKATING else ExerciseType.OTHER_WORKOUT

                Log.d(TAG, "Using exercise type: $exerciseType (inline skating supported: $supported)")

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
            val hrPoints = update.latestMetrics[DataType.HEART_RATE_BPM]
            val calPoints = update.latestMetrics[DataType.CALORIES_TOTAL]

            val hr = hrPoints?.lastOrNull()?.value?.toInt() ?: 0
            val cal = calPoints?.lastOrNull()?.value?.toInt() ?: 0

            if (hr > 0 || cal > 0) {
                onHealthUpdate?.invoke(hr, cal)
                // Relay health data back to the phone (5s throttled)
                trackingContext?.let { ctx ->
                    WearMessageSender.sendHealthUpdate(ctx, hr, cal)
                }
            }
        }

        override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) {
            // Not used for skating — no lap concept
        }

        override fun onAvailabilityChanged(dataType: DataType<*, *>, availability: Availability) {
            if (availability is DataTypeAvailability) {
                Log.d(TAG, "Availability for $dataType: ${availability.availability}")
            }
        }

        override fun onRegistered() {
            Log.d(TAG, "Exercise callback registered")
        }

        override fun onRegistrationFailed(throwable: Throwable) {
            Log.e(TAG, "Exercise callback registration failed: ${throwable.message}", throwable)
        }
    }
}
