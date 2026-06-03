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
  // Image assets bundled into the watch target's xcassets
  images: {
    "sk8lytz-logo": "../../Brand Materials/product specific/sk8lytz solid blue.png",
  },
});