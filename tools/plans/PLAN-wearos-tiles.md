# Implementation Plan: Wear OS Tiles

## Goal
Add a Wear OS Tile for quick-launch in the watch tile carousel. Tiles provide a glanceable entry point without opening the full app.

## Proposed Changes

### Wear OS — New TileService

#### [NEW] [Sk8lytzTileService.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/tiles/Sk8lytzTileService.kt)

```kotlin
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
 * Shows session status and provides one-tap launch into the app.
 */
class Sk8lytzTileService : TileService() {

    companion object {
        private const val RESOURCES_VERSION = "1"
        // Brand colors
        private const val ELECTRIC_CYAN = 0xFF00F0FF.toInt()
        private const val NEON_MAGENTA  = 0xFFFF00D4.toInt()
        private const val TRUE_BLACK    = 0xFF000000.toInt()
        private const val WHITE         = 0xFFFFFFFF.toInt()
        private const val GRAY          = 0xFF888888.toInt()
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        val isActive = WearableCommunicationService.currentState == SessionState.ACTIVE
        val speed = WearableCommunicationService.currentSpeed

        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(buildLayout(isActive, speed))
                                    .build()
                            ).build()
                    ).build()
            )
            // Refresh every 30 seconds during active session
            .setFreshnessIntervalMillis(if (isActive) 30_000L else 300_000L)
            .build()

        return Futures.immediateFuture(tile)
    }

    override fun onTileResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .build()
        )
    }

    private fun buildLayout(isActive: Boolean, speed: Double): LayoutElementBuilders.LayoutElement {
        // Tap anywhere to launch app
        val clickable = ModifiersBuilders.Clickable.Builder()
            .setOnClick(
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setPackageName(packageName)
                            .setClassName(MainActivity::class.java.name)
                            .build()
                    ).build()
            )
            .setId("launch_sk8lytz")
            .build()

        val modifiers = ModifiersBuilders.Modifiers.Builder()
            .setClickable(clickable)
            .build()

        return Column.Builder()
            .setWidth(expand())
            .setModifiers(modifiers)
            .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
            // App name
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
            .addContent(Spacer.Builder().setHeight(dp(8f)).build())
            // Skate emoji
            .addContent(
                Text.Builder()
                    .setText("🛼")
                    .setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder()
                            .setSize(dp(32f))
                            .build()
                    ).build()
            )
            .addContent(Spacer.Builder().setHeight(dp(8f)).build())
            // Status
            .addContent(
                Text.Builder()
                    .setText(if (isActive) "SKATING" else "TAP TO START")
                    .setFontStyle(
                        LayoutElementBuilders.FontStyle.Builder()
                            .setSize(dp(12f))
                            .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                            .setColor(argb(if (isActive) ELECTRIC_CYAN else GRAY))
                            .setLetterSpacing(2f)
                            .build()
                    ).build()
            )
            // Speed (only when active)
            .apply {
                if (isActive) {
                    addContent(Spacer.Builder().setHeight(dp(4f)).build())
                    addContent(
                        Text.Builder()
                            .setText(String.format("%.1f mph", speed))
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(dp(14f))
                                    .setColor(argb(WHITE))
                                    .build()
                            ).build()
                    )
                }
            }
            .build()
    }
}
```

#### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/AndroidManifest.xml)

Add inside `<application>` tag, after the existing `<service>` for `WearableCommunicationService`:

```xml
<!-- Tile Service for watch tile carousel -->
<service
    android:name=".tiles.Sk8lytzTileService"
    android:label="SK8Lytz"
    android:permission="com.google.android.wearable.permission.BIND_TILE_PROVIDER"
    android:exported="true">
    <intent-filter>
        <action android:name="androidx.wear.tiles.action.BIND_TILE_PROVIDER" />
    </intent-filter>
    <meta-data
        android:name="androidx.wear.tiles.PREVIEW"
        android:resource="@mipmap/ic_launcher" />
</service>
```

#### [MODIFY] [build.gradle](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/build.gradle)

Add to `dependencies` block:

```groovy
implementation "androidx.wear.tiles:tiles:1.4.1"
implementation "androidx.wear.protolayout:protolayout:1.2.1"
implementation "androidx.wear.protolayout:protolayout-material:1.2.1"
implementation "com.google.guava:guava:33.0.0-android"
```

#### [MODIFY] [WearableCommunicationService.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt)

Make `currentState` and `currentSpeed` `public` (they already are via companion — just verify they're accessible from the tile service). No code change needed — the companion object fields are already package-accessible.

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest pass

### Manual Verification
- Build and install Wear OS APK
- Swipe left/right on watch face to tile carousel → SK8Lytz tile should appear
- Tap tile → launches app
- Start session from phone → tile should update to show "SKATING" + speed
