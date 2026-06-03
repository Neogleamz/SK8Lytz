/** @type {import('@bacons/apple-targets/app.plugin').ConfigFunction} */
module.exports = _config => ({
  type: "watch",
  // Local brand asset — no roller-skating SF Symbol exists in Apple's library
  icon: "../../assets/icon.png",
  colors: {
    $accent: "darkcyan",
  },
  // watchOS 10.0+ ensures full SwiftUI support and modern SF Symbol availability
  deploymentTarget: "10.0",
  entitlements: {
    // Required for HKWorkoutSession access on the watch target
    "com.apple.developer.healthkit": true,
    "com.apple.developer.healthkit.access": [],
  },
  // Required Info.plist keys for HealthKit — Apple rejects without these
  infoPlist: {
    NSHealthShareUsageDescription:
      "SK8Lytz reads your heart rate during skating sessions to display live BPM on your wrist and sync it to your session history.",
    NSHealthUpdateUsageDescription:
      "SK8Lytz saves skating workouts to Apple Health so your activity rings reflect your skate sessions.",
  },
  // Image assets bundled into the watch target's xcassets
  images: {
    "sk8lytz-logo": "../../Brand Materials/product specific/sk8lytz solid blue.png",
  },
});