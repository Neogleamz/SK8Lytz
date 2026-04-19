export default () => {
  const googleMapsApiKey = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || "";

  if (!googleMapsApiKey) {
    console.warn("⚠️ EXPO_PUBLIC_GOOGLE_MAPS_API_KEY is not set in .env. Maps may crash nateively on Android!");
  }

  return {
    "name": "SK8Lytz",
    "slug": "sk8lytz",
    "scheme": "sk8lytz",
    "version": "1.12.3",
    "orientation": "portrait",
    "icon": "./assets/icon.png",
    "userInterfaceStyle": "light",
    "splash": {
      "image": "./assets/splash-icon.png",
      "resizeMode": "contain",
      "backgroundColor": "#ffffff"
    },
    "ios": {
      "supportsTablet": true,
      "infoPlist": {
        "NSMicrophoneUsageDescription": "SK8Lytz needs microphone access to synchronize your lights to ambient music."
      },
      "buildNumber": "3",
      "config": {
        "googleMapsApiKey": googleMapsApiKey
      }
    },
    "android": {
      "versionCode": 25,
      "predictiveBackGestureEnabled": false,
      "allowBackup": false,
      "permissions": [
        "android.permission.BLUETOOTH",
        "android.permission.BLUETOOTH_ADMIN",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_SCAN",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.RECORD_AUDIO",
        "android.permission.CAMERA",
        "android.permission.POST_NOTIFICATIONS"
      ],
      "package": "com.neogleamz.sk8lytz",
      "config": {
        "googleMaps": {
          "apiKey": googleMapsApiKey
        }
      }
    },
    "web": {
      "favicon": "./assets/favicon.png"
    },
    "plugins": [
      "expo-audio",
      [
        "expo-build-properties",
        {
          "android": {
            "enableJetifier": true,
            "compileSdkVersion": 36,
            "targetSdkVersion": 36
          }
        }
      ],
      [
        "react-native-ble-plx",
        {
          "isBackgroundEnabled": true,
          "modes": [
            "peripheral",
            "central"
          ],
          "bluetoothAlwaysPermission": "Allow Neogleamz App to connect to your Zengge roller skate controllers.",
          "neverForLocation": true
        }
      ]
    ],
    "extra": {
      "eas": {
        "projectId": "30f5cc5f-d918-40ea-b095-420e8355a3f8"
      }
    },
    "owner": "neogleamz"
  };
};
