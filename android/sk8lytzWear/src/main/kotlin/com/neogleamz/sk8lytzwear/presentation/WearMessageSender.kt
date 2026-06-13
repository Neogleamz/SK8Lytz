package com.neogleamz.sk8lytzwear.presentation

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

/**
 * WearMessageSender — sends optimistic commands and health telemetry
 * from the watch to the phone.
 *
 * Path contract (must match phone-side bridge module):
 *   /sk8lytz/command — ephemeral commands (watch → phone)
 *   /sk8lytz/health  — periodic health relay (watch → phone, 5s throttle)
 *
 * Each command includes haptic feedback for physical confirmation.
 */
object WearMessageSender {

    private const val TAG = "WearMessageSender"
    private const val PATH_COMMAND = "/sk8lytz/command"
    private const val PATH_HEALTH  = "/sk8lytz/health"
    private const val HEALTH_THROTTLE_MS = 5000L
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var lastHealthSendMs = 0L

    /**
     * Send a command to the phone app (START_SESSION / STOP_SESSION).
     * Fires haptic buzz for physical confirmation on the wrist.
     */
    fun sendCommand(context: Context, command: String) {
        triggerHaptic(context)
        scope.launch {
            runCatching {
                val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                val payload = command.toByteArray(Charsets.UTF_8)
                nodes.forEach { node ->
                    Wearable.getMessageClient(context)
                        .sendMessage(node.id, PATH_COMMAND, payload)
                        .await()
                    Log.d(TAG, "Sent '$command' to ${node.displayName}")
                }
            }.onFailure {
                Log.e(TAG, "Failed to send command '$command': ${it.message}", it)
            }
        }
    }

    private const val PREFS_NAME = "telemetry_buffer_prefs"
    private const val KEY_BUFFER = "health_buffer"

    private fun getPendingTelemetry(context: Context): org.json.JSONArray {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_BUFFER, "[]")
        return try { org.json.JSONArray(jsonStr) } catch (e: Exception) { org.json.JSONArray() }
    }

    private fun savePendingTelemetry(context: Context, jsonArray: org.json.JSONArray) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BUFFER, jsonArray.toString()).apply()
    }

    private fun clearPendingTelemetry(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_BUFFER).apply()
    }

    /**
     * Relay live health data from the watch's ExerciseClient back to the phone.
     * Throttled to max once per 5 seconds to match watchOS relay interval
     * and protect watch battery.
     */
    fun sendHealthUpdate(context: Context, heartRate: Int, calories: Int, sessionState: String, startTimeMs: Long) {
        val now = System.currentTimeMillis()
        if (now - lastHealthSendMs < HEALTH_THROTTLE_MS) return
        lastHealthSendMs = now

        scope.launch {
            runCatching {
                val jsonObj = JSONObject().apply {
                    put("heartRate", heartRate)
                    put("calories", calories)
                    put("status", sessionState)
                    put("startTimeMs", startTimeMs)
                }

                val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                if (nodes.isEmpty()) {
                    val queueSize = synchronized(this) {
                        val buffer = getPendingTelemetry(context)
                        buffer.put(jsonObj)
                        savePendingTelemetry(context, buffer)
                        buffer.length()
                    }
                    Log.d(TAG, "Phone disconnected. Buffered health update. Queue size: $queueSize")
                } else {
                    val buffer = synchronized(this) {
                        val b = getPendingTelemetry(context)
                        b.put(jsonObj)
                        b
                    }
                    
                    var allSent = true
                    for (i in 0 until buffer.length()) {
                        val payload = buffer.getJSONObject(i).toString().toByteArray(Charsets.UTF_8)
                        nodes.forEach { node ->
                            Wearable.getMessageClient(context)
                                .sendMessage(node.id, PATH_HEALTH, payload)
                                .await()
                        }
                    }
                    if (allSent) {
                        synchronized(this) { clearPendingTelemetry(context) }
                        Log.d(TAG, "Health relay flushed ${buffer.length()} items → ${nodes.size} node(s)")
                    }
                }
            }.onFailure {
                Log.e(TAG, "Health relay failed: ${it.message}", it)
            }
        }
    }

    private fun triggerHaptic(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        vibrator.vibrate(
            VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
}

