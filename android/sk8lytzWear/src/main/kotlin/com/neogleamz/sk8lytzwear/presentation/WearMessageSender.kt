package com.neogleamz.sk8lytzwear.presentation

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * WearMessageSender — sends optimistic commands from the watch to the phone.
 *
 * Path contract (must match phone-side bridge module):
 *   /sk8lytz/command — ephemeral commands (watch → phone)
 *
 * Each command includes haptic feedback for physical confirmation.
 */
object WearMessageSender {

    private const val TAG = "WearMessageSender"
    private const val PATH_COMMAND = "/sk8lytz/command"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Send a command to the phone app (START_SESSION / STOP_SESSION).
     * Fires haptic buzz for physical confirmation on the wrist.
     */
    fun sendCommand(context: Context, command: String) {
        // Haptic feedback — immediate wrist confirmation
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

    private fun triggerHaptic(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        vibrator.vibrate(
            VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
}
