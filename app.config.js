module.exports = {
  expo: {
    name: "SK8Lytz",
    slug: "sk8lytz",
    scheme: "sk8lytz",
    version: "3.8.1",
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
        NSMicrophoneUsageDescription: "SK8Lytz needs microphone access to synchronize your lights to ambient music."
      },
      buildNumber: "15",
      config: {
        googleMapsApiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || "AIzaSyBfvwN5fcyDbzUZp2Q7c2OfMLPFajVRPwA"
      }
    },
    android: {
      versionCode: 37,
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
        "android.permission.FOREGROUND_SERVICE_LOCATION"
      ],
      package: "com.neogleamz.sk8lytz",
      config: {
        googleMaps: {
          apiKey: process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || "AIzaSyBfvwN5fcyDbzUZp2Q7c2OfMLPFajVRPwA"
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
            "enableJetifier": true,
            "minSdkVersion": 26,
            "compileSdkVersion": 36,
            "targetSdkVersion": 36
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
          neverForLocation: true
        }
      ]
    ],
    extra: {
      eas: {
        projectId: "30f5cc5f-d918-40ea-b095-420e8355a3f8"
      }
    },
    owner: "neogleamz"
  }
};
