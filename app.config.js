module.exports = {
  expo: {
    name: "SK8Lytz",
    slug: "sk8lytz",
    scheme: "sk8lytz",
    version: "3.10.0",
    orientation: "portrait",
    icon: "./assets/icon.png",
    userInterfaceStyle: "light",
    splash: {
      image: "./assets/splash-icon.png",
      resizeMode: "contain",
      backgroundColor: "#ffffff"
    },
    ios: {
      supportsTablet: true,
      infoPlist: {
        UIBackgroundModes: ["location", "bluetooth-central"],
        NSMicrophoneUsageDescription: "SK8Lytz needs microphone access to synchronize your lights to ambient music.",
        NSCameraUsageDescription: "SK8Lytz needs camera access to sample colors from your environment for LED synchronization.",
        NSHealthShareUsageDescription: "SK8Lytz reads step count and heart rate to synchronize light patterns with your fitness metrics.",
        NSHealthUpdateUsageDescription: "SK8Lytz writes your inline skating sessions to Apple Health to track fitness activity.",
        NSLocationWhenInUseUsageDescription: "SK8Lytz uses your location to discover nearby skate spots and map your skating routes.",
        NSLocationAlwaysAndWhenInUseUsageDescription: "SK8Lytz tracks your location in the background to map your route even when your phone is in your pocket.",
        NSLocationAlwaysUsageDescription: "SK8Lytz tracks your location in the background to map your route even when your phone is in your pocket."
      },
      buildNumber: "19",
      config: {
        googleMapsApiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY
      }
    },
    android: {
      versionCode: 41,
      predictiveBackGestureEnabled: false,
      allowBackup: false,
      permissions: [
        "android.permission.BLUETOOTH",
        "android.permission.BLUETOOTH_ADMIN",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_SCAN",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.RECORD_AUDIO",
        "android.permission.CAMERA",
        "android.permission.POST_NOTIFICATIONS",
        "android.permission.ACTIVITY_RECOGNITION",
        "android.permission.FOREGROUND_SERVICE",
        "android.permission.FOREGROUND_SERVICE_LOCATION",
        "android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE",
        "android.permission.INTERNET",
        "android.permission.VIBRATE",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.health.READ_HEART_RATE",
        "android.permission.health.READ_STEPS",
        "android.permission.health.READ_ACTIVE_CALORIES_BURNED",
        "android.permission.health.WRITE_EXERCISE",
        "android.permission.health.WRITE_TOTAL_CALORIES_BURNED",
        "android.permission.health.WRITE_DISTANCE"
      ],
      package: "com.neogleamz.sk8lytz",
      config: {
        googleMaps: {
          apiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY
        }
      }
    },
    web: {
      favicon: "./assets/favicon.png"
    },
    plugins: [
      "@config-plugins/detox",
      "react-native-health",
      "react-native-health-connect",
      [
        "expo-build-properties",
        {
          "android": {
            "enableJetifier": false,
            "minSdkVersion": 26,
            "compileSdkVersion": 36,
            "targetSdkVersion": 36,
            "extraProguardRules": "-keep class com.polidea.reactnativeble.** { *; }\n-dontwarn com.polidea.reactnativeble.**\n-keep class com.polidea.rxandroidble2.** { *; }\n-dontwarn com.polidea.rxandroidble2.**\n-keep class com.mrousavy.camera.** { *; }\n-dontwarn com.mrousavy.camera.**\n-keep class com.mrousavy.nitro.** { *; }\n-dontwarn com.mrousavy.nitro.**"
          }
        }
      ],
      [
        "react-native-ble-plx",
        {
          isBackgroundEnabled: true,
          modes: [
            "peripheral",
            "central"
          ],
          bluetoothAlwaysPermission: "Allow Neogleamz App to connect to your Zengge roller skate controllers.",
          bluetoothPeripheralPermission: "Allow Neogleamz App to connect to your Zengge roller skate controllers."
        }
      ],
      "@bacons/apple-targets",
      "./plugins/withWearOsModule"
    ],
    extra: {
      eas: {
        projectId: "30f5cc5f-d918-40ea-b095-420e8355a3f8"
      }
    },
    owner: "neogleamz"
  }
};
