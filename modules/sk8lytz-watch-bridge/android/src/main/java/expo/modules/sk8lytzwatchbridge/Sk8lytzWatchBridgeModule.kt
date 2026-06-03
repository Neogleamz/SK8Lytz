package expo.modules.sk8lytzwatchbridge

import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.MessageClient
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
 * Uses the Wearable Data Layer API to push state and metrics to the paired Wear OS watch.
 * - syncSessionState → DataClient.putDataItem (reliable, survives unreachable watch)
 * - sendMetricUpdate  → MessageClient.sendMessage (real-time, best-effort)
 * - Inbound commands (START/STOP from watch) → emitted as 'onWatchCommandReceived' JS events
 *
 * Path contract (must match WearableCommunicationService on the watch):
 *   /sk8lytz/state   — DataClient item (persistent state)
 *   /sk8lytz/command — MessageClient message (ephemeral commands watch→phone)
 */
class Sk8lytzWatchBridgeModule : Module() {

    private val TAG = "WatchBridge"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Wearable Data Layer path constants — must match watch-side service
    private val PATH_STATE   = "/sk8lytz/state"
    private val PATH_COMMAND = "/sk8lytz/command"
    private val PATH_METRICS = "/sk8lytz/metrics"

    override fun definition() = ModuleDefinition {
        Name("Sk8lytzWatchBridge")

        Events("onWatchCommandReceived")

        AsyncFunction("syncSessionState") { state: Map<String, Any>, promise: Promise ->
            scope.launch {
                runCatching {
                    val context = appContext.reactContext ?: return@launch
                    val request = PutDataMapRequest.create(PATH_STATE).apply {
                        dataMap.apply {
                            putString("status",    state["status"] as? String ?: "STOPPED")
                            putDouble("speed",     (state["speed"] as? Number)?.toDouble() ?: 0.0)
                            putInt("heartRate",    (state["heartRate"] as? Number)?.toInt() ?: 0)
                            putInt("calories",     (state["calories"] as? Number)?.toInt() ?: 0)
                            putString("startTime", state["startTime"] as? String ?: "")
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
                runCatching {
                    val context = appContext.reactContext ?: return@launch
                    val json = JSONObject(metrics as Map<*, *>).toString()
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
                runCatching {
                    val context = appContext.reactContext ?: run {
                        promise.resolve(false)
                        return@launch
                    }
                    val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                    promise.resolve(nodes.isNotEmpty())
                }.onFailure {
                    promise.resolve(false)
                }
            }
        }
    }
}
