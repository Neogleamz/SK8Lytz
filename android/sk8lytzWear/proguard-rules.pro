# SK8Lytz Wear OS ProGuard Rules
# Keep Wearable API classes used by reflection
-keep class com.google.android.gms.wearable.** { *; }
# Keep Health Services API classes
-keep class androidx.health.services.client.** { *; }
