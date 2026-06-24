package expo.modules.sk8lytzwatchbridge

import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

/**
 * Sk8lytzWatchBridgeModule — Android side of the watch bridge.
 *
 * Uses the Wearable Data Layer API to push state and metrics to the paired Wear OS watch
 * AND receive inbound commands (START_SESSION/STOP_SESSION) from the watch.
 *
 * - syncSessionState → DataClient.putDataItem (reliable, survives unreachable watch)
 * - sendMetricUpdate  → MessageClient.sendMessage (real-time, best-effort)
 * - Inbound commands (START/STOP from watch) → emitted as 'onWatchCommandReceived' JS events
 *
 * Path contract (must match WearableCommunicationService on the watch):
 *   /sk8lytz/state   — DataClient item (persistent state, phone → watch)
 *   /sk8lytz/metrics — MessageClient message (real-time telemetry, phone → watch)
 *   /sk8lytz/command — MessageClient message (ephemeral commands, watch → phone)
 */
class Sk8lytzWatchBridgeModule : Module() {

    private val TAG = "WatchBridge"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Wearable Data Layer path constants — must match watch-side service
    private val PATH_STATE   = "/sk8lytz/state"
    private val PATH_COMMAND = "/sk8lytz/command"
    private val PATH_METRICS = "/sk8lytz/metrics"
    private val PATH_HEALTH  = "/sk8lytz/health"

    // Inbound message listener — receives commands from the watch
    private var messageListener: MessageClient.OnMessageReceivedListener? = null

    override fun definition() = ModuleDefinition {
        Name("Sk8lytzWatchBridge")

        Events("onWatchCommandReceived", "onWatchHealthUpdate")

        // Register the inbound MessageClient listener explicitly from JS
        Function("startListening") {
            val context = appContext.reactContext ?: return@Function null
            if (messageListener == null) {
                messageListener = MessageClient.OnMessageReceivedListener { messageEvent ->
                    handleInboundMessage(messageEvent)
                }
                Wearable.getMessageClient(context).addListener(messageListener!!)
                Log.d(TAG, "Registered MessageClient listener for watch → phone commands")
            }
        }

        // Clean up listener on module destroy to prevent leaks
        OnDestroy {
            val context = appContext.reactContext ?: return@OnDestroy
            messageListener?.let {
                Wearable.getMessageClient(context).removeListener(it)
                Log.d(TAG, "Removed MessageClient listener")
            }
            messageListener = null
        }

        AsyncFunction("syncSessionState") { state: Map<String, Any>, promise: Promise ->
            scope.launch {
                val context = appContext.reactContext
                if (context == null) {
                    promise.reject("NO_CONTEXT", "React context not available", null)
                    return@launch
                }
                runCatching {
                    val request = PutDataMapRequest.create(PATH_STATE).apply {
                        dataMap.apply {
                            putString("status",        state["status"] as? String ?: "STOPPED")
                            putDouble("speed",         (state["speed"] as? Number)?.toDouble() ?: 0.0)
                            putInt("heartRate",        (state["heartRate"] as? Number)?.toInt() ?: 0)
                            putInt("calories",         (state["calories"] as? Number)?.toInt() ?: 0)
                            putString("startTime",     state["startTime"] as? String ?: "")
                            // Summary-only fields (non-zero only when status == "SUMMARY")
                            putInt("totalDuration",    (state["totalDuration"] as? Number)?.toInt() ?: 0)
                            putDouble("distance",      (state["distance"] as? Number)?.toDouble() ?: 0.0)
                            putDouble("avgSpeed",      (state["avgSpeed"] as? Number)?.toDouble() ?: 0.0)
                            putInt("peakHR",           (state["peakHR"] as? Number)?.toInt() ?: 0)
                            // Force update even if data is identical (timestamp acts as dirty flag)
                            putLong("timestamp", System.currentTimeMillis())
                        }
                    }.asPutDataRequest().setUrgent()

                    Wearable.getDataClient(context).putDataItem(request).await()
                    Log.d(TAG, "syncSessionState: pushed to DataClient")
                    promise.resolve(null)
                }.onFailure {
                    Log.e(TAG, "syncSessionState failed: ${it.message}", it)
                    promise.reject("SYNC_FAILED", it.message, it)
                }
            }
        }

        AsyncFunction("sendMetricUpdate") { metrics: Map<String, Any>, promise: Promise ->
            scope.launch {
                val context = appContext.reactContext
                if (context == null) {
                    promise.reject("NO_CONTEXT", "React context not available", null)
                    return@launch
                }
                runCatching {
                    val jsonMap = mutableMapOf<String, Any?>()
                    metrics.forEach { (key, value) -> jsonMap[key] = value }
                    val json = JSONObject(jsonMap).toString()

                    val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                    nodes.forEach { node ->
                        Wearable.getMessageClient(context)
                            .sendMessage(node.id, PATH_METRICS, json.toByteArray(Charsets.UTF_8))
                            .await()
                    }
                    Log.d(TAG, "sendMetricUpdate: pushed to ${nodes.size} node(s)")
                    promise.resolve(null)
                }.onFailure {
                    Log.e(TAG, "sendMetricUpdate failed: ${it.message}", it)
                    promise.reject("METRIC_FAILED", it.message, it)
                }
            }
        }

        AsyncFunction("isWatchReachable") { promise: Promise ->
            scope.launch {
                val context = appContext.reactContext
                if (context == null) {
                    promise.resolve(false)
                    return@launch
                }
                runCatching {
                    val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                    promise.resolve(nodes.isNotEmpty())
                }.onFailure {
                    promise.resolve(false)
                }
            }
        }
    }

    /**
     * Handles inbound messages from the Wear OS watch.
     * Commands arrive on PATH_COMMAND as UTF-8 strings: "START_SESSION" or "STOP_SESSION".
     * Health data arrives on PATH_HEALTH as JSON: {"heartRate": N, "calories": N}.
     */
    private fun handleInboundMessage(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            PATH_COMMAND -> {
                val payloadStr = String(messageEvent.data, Charsets.UTF_8)
                Log.d(TAG, "Received command from watch: $payloadStr")
                runCatching {
                    val json = JSONObject(payloadStr)
                    val map = mutableMapOf<String, Any>()
                    val keys = json.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        map[key] = json.get(key)
                    }
                    sendEvent("onWatchCommandReceived", map)
                }.onFailure {
                    // Legacy fallback
                    if (payloadStr == "START_SESSION" || payloadStr == "STOP_SESSION") {
                        sendEvent("onWatchCommandReceived", mapOf("type" to payloadStr))
                    } else {
                        Log.e(TAG, "Failed to parse watch command: ${it.message}", it)
                    }
                }
            }
            PATH_HEALTH -> {
                runCatching {
                    val json = JSONObject(String(messageEvent.data, Charsets.UTF_8))
                    val hr = json.optInt("heartRate", 0)
                    val cal = json.optInt("calories", 0)
                    val status = json.optString("status", "")
                    val startTimeMs = json.optLong("startTimeMs", 0L)
                    Log.d(TAG, "Received health from watch: hr=$hr cal=$cal status=$status")
                    sendEvent("onWatchHealthUpdate", mapOf(
                        "heartRate" to hr,
                        "calories" to cal,
                        "status" to status,
                        "startTimeMs" to startTimeMs
                    ))
                }.onFailure {
                    Log.e(TAG, "Failed to parse watch health: ${it.message}", it)
                }
            }
            else -> Log.w(TAG, "Unknown inbound path: ${messageEvent.path}")
        }
    }
}
