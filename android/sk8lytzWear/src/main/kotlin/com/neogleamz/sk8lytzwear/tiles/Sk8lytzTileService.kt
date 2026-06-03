package com.neogleamz.sk8lytzwear.tiles

import android.content.ComponentName
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.LayoutElementBuilders.Text
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.neogleamz.sk8lytzwear.MainActivity
import com.neogleamz.sk8lytzwear.presentation.SessionState
import com.neogleamz.sk8lytzwear.services.WearableCommunicationService

/**
 * Sk8lytzTileService — Glanceable tile for the Wear OS tile carousel.
 *
 * Shows the current session status (ACTIVE or idle), live speed when skating,
 * and the SK8Lytz brandmark. Tapping anywhere launches the main app.
 *
 * Registered in AndroidManifest.xml with the BIND_TILE_PROVIDER permission.
 * Refreshes every 30 s during active sessions, every 5 min when idle.
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
    }

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<TileBuilders.Tile> {

        val state    = WearableCommunicationService.currentState
        val speed    = WearableCommunicationService.currentSpeed
        val isActive = state == SessionState.ACTIVE
        val isPaused = state == SessionState.PAUSED

        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(buildLayout(isActive, isPaused, speed))
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

        return Futures.immediateFuture(tile)
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ListenableFuture<ResourceBuilders.Resources> {
        // No image resources — using text/emoji only for maximum compatibility
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
        speed: Double
    ): LayoutElementBuilders.LayoutElement {

        // Tap anywhere to open the app
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

        return Column.Builder()
            .setWidth(expand())
            .setModifiers(rootModifiers)
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)

            // ── App name ──────────────────────────────────────────────────
            .addContent(
                Text.Builder()
                    .setText("SK8Lytz")
                    .setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder()
                            .setSize(dp(18f))
                            .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                            .setColor(argb(ELECTRIC_CYAN))
                            .build()
                    ).build()
            )
            .addContent(Spacer.Builder().setHeight(dp(6f)).build())

            // ── Skate emoji ───────────────────────────────────────────────
            .addContent(
                Text.Builder()
                    .setText("🛼")
                    .setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder()
                            .setSize(dp(28f))
                            .build()
                    ).build()
            )
            .addContent(Spacer.Builder().setHeight(dp(6f)).build())

            // ── Session status label ──────────────────────────────────────
            .addContent(
                Text.Builder()
                    .setText(
                        when {
                            isActive -> "SKATING"
                            isPaused -> "⏸ PAUSED"
                            else     -> "TAP TO OPEN"
                        }
                    )
                    .setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder()
                            .setSize(dp(11f))
                            .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                            .setColor(
                                argb(when {
                                    isActive -> ELECTRIC_CYAN
                                    isPaused -> AMBER
                                    else     -> GRAY
                                })
                            )
                            .build()
                    ).build()
            )

            // ── Live speed (active sessions only) ────────────────────────
            .apply {
                if (isActive || isPaused) {
                    addContent(Spacer.Builder().setHeight(dp(4f)).build())
                    addContent(
                        Text.Builder()
                            .setText(String.format("%.1f mph", speed))
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(dp(14f))
                                    .setWeight(LayoutElementBuilders.FONT_WEIGHT_MEDIUM)
                                    .setColor(argb(WHITE))
                                    .build()
                            ).build()
                    )
                }
            }
            .build()
    }
}
