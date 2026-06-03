package com.neogleamz.sk8lytzwear.tiles

import android.content.ComponentName
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.Row
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.LayoutElementBuilders.Text
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.SettableFuture
import com.neogleamz.sk8lytzwear.MainActivity
import com.neogleamz.sk8lytzwear.presentation.SessionState
import com.neogleamz.sk8lytzwear.services.WearableCommunicationService
import java.time.Instant

/**
 * Sk8lytzTileService — Glanceable tile for the Wear OS tile carousel.
 *
 * Shows the current session status, live speed, HR, calories, and elapsed time.
 * Syncs perfectly with the phone via DataClient even if the watch app is closed.
 */
class Sk8lytzTileService : TileService() {

    companion object {
        private const val RESOURCES_VERSION = "1"

        // Brand colors (ARGB int literals)
        private const val ELECTRIC_CYAN = 0xFF00F0FF.toInt()
        private const val TRUE_BLACK    = 0xFF000000.toInt()
        private const val WHITE         = 0xFFFFFFFF.toInt()
        private const val GRAY          = 0xFF555555.toInt()
        private const val AMBER         = 0xFFFFAA00.toInt()
        private const val MAGENTA       = 0xFFFF00D4.toInt()
    }

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<TileBuilders.Tile> {
        
        val future = SettableFuture.create<TileBuilders.Tile>()

        Thread {
            try {
                // Default to in-memory variables
                var isActive = WearableCommunicationService.currentState == SessionState.ACTIVE
                var isPaused = WearableCommunicationService.currentState == SessionState.PAUSED
                var speed = WearableCommunicationService.currentSpeed
                var hr = 0
                var calories = 0
                var startTimeMs = WearableCommunicationService.sessionStartTimeMs

                // Overwrite with absolute truth from persistent data layer
                val dataItems = Tasks.await(Wearable.getDataClient(this).dataItems)
                android.util.Log.d("Sk8lytzTile", "Tasks.await succeeded. Items count: ${dataItems.count}")
                dataItems.firstOrNull { it.uri.path == "/sk8lytz/state" }?.let { dataItem ->
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val status = dataMap.getString("status", "STOPPED")
                    android.util.Log.d("Sk8lytzTile", "Tile parsed DataClient status: $status")
                    isActive = status == "ACTIVE"
                    isPaused = status == "PAUSED"
                    speed = dataMap.getDouble("speed", 0.0)
                    hr = dataMap.getInt("heartRate", 0)
                    calories = dataMap.getInt("calories", 0)
                    
                    val startTimeStr = dataMap.getString("startTime", "")
                    if (isActive && startTimeStr.isNotEmpty()) {
                        runCatching {
                            startTimeMs = Instant.parse(startTimeStr).toEpochMilli()
                        }
                    } else if (!isActive && !isPaused) {
                        startTimeMs = 0L
                    }
                }

                val elapsedSeconds = if (startTimeMs > 0L) {
                    ((System.currentTimeMillis() - startTimeMs) / 1000).toInt()
                } else 0

                val tile = TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.Builder()
                            .addTimelineEntry(
                                TimelineBuilders.TimelineEntry.Builder()
                                    .setLayout(
                                        LayoutElementBuilders.Layout.Builder()
                                            .setRoot(buildLayout(isActive, isPaused, speed, hr, calories, elapsedSeconds))
                                            .build()
                                    ).build()
                            ).build()
                    )
                    // Refresh aggressively during sessions; conserve battery when idle
                    .setFreshnessIntervalMillis(
                        when {
                            isActive -> 30_000L
                            isPaused -> 60_000L
                            else     -> 300_000L
                        }
                    )
                    .build()

                future.set(tile)
            } catch (e: Exception) {
                android.util.Log.e("Sk8lytzTile", "Tile rendering failed completely! Error: ${e.message}", e)
                // Fallback on total failure
                val tile = TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.Builder().addTimelineEntry(
                            TimelineBuilders.TimelineEntry.Builder().setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(buildLayout(false, false, 0.0, 0, 0, 0))
                                    .build()
                            ).build()
                        ).build()
                    ).build()
                future.set(tile)
            }
        }.start()

        return future
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
    }

    // ── Layout ───────────────────────────────────────────────────────────────

    private fun buildLayout(
        isActive: Boolean,
        isPaused: Boolean,
        speed: Double,
        hr: Int,
        calories: Int,
        elapsedSeconds: Int
    ): LayoutElementBuilders.LayoutElement {

        val clickable = ModifiersBuilders.Clickable.Builder()
            .setId("launch_sk8lytz")
            .setOnClick(
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setPackageName(packageName)
                            .setClassName(MainActivity::class.java.name)
                            .build()
                    ).build()
            ).build()

        val rootModifiers = ModifiersBuilders.Modifiers.Builder()
            .setClickable(clickable)
            .build()

        val col = Column.Builder()
            .setWidth(expand())
            .setModifiers(rootModifiers)
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)

        if (!isActive && !isPaused) {
            // IDLE VIEW
            col.addContent(
                Text.Builder().setText("SK8Lytz").setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder().setSize(sp(18f)).setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD).setColor(argb(ELECTRIC_CYAN)).build()
                ).build()
            )
            col.addContent(Spacer.Builder().setHeight(dp(6f)).build())
            col.addContent(
                Text.Builder().setText("🛼").setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder().setSize(sp(28f)).build()
                ).build()
            )
            col.addContent(Spacer.Builder().setHeight(dp(6f)).build())
            col.addContent(
                Text.Builder().setText("TAP TO OPEN").setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder().setSize(sp(11f)).setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD).setColor(argb(GRAY)).build()
                ).build()
            )
        } else {
            // ACTIVE/PAUSED VIEW
            
            // Timer
            val hMs = elapsedSeconds / 3600
            val mMs = (elapsedSeconds % 3600) / 60
            val sMs = elapsedSeconds % 60
            val timeString = if (hMs > 0) String.format("%d:%02d:%02d", hMs, mMs, sMs) else String.format("%02d:%02d", mMs, sMs)
            
            col.addContent(
                Text.Builder().setText(timeString).setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder().setSize(sp(16f)).setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD).setColor(argb(WHITE)).build()
                ).build()
            )
            col.addContent(Spacer.Builder().setHeight(dp(4f)).build())
            
            // Speed
            col.addContent(
                Text.Builder().setText(String.format("%.1f mph", speed)).setFontStyle(
                    LayoutElementBuilders.FontStyle.Builder().setSize(sp(22f)).setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD).setColor(argb(ELECTRIC_CYAN)).build()
                ).build()
            )
            col.addContent(Spacer.Builder().setHeight(dp(8f)).build())
            
            // HR & Calories Row
            val statsRow = Row.Builder()
                .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
                .addContent(
                    Text.Builder().setText("❤️ $hr").setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder().setSize(sp(12f)).setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD).setColor(argb(MAGENTA)).build()
                    ).build()
                )
                .addContent(Spacer.Builder().setWidth(dp(16f)).build())
                .addContent(
                    Text.Builder().setText("🔥 $calories").setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder().setSize(sp(12f)).setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD).setColor(argb(AMBER)).build()
                    ).build()
                )
            
            col.addContent(statsRow.build())
        }

        return col.build()
    }
}
