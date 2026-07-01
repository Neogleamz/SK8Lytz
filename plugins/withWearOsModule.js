const { withSettingsGradle, withAppBuildGradle, withAndroidManifest } = require('@expo/config-plugins');

/**
 * Expo Config Plugin: withWearOsModule
 *
 * Injects the :sk8lytzWear Gradle module into the Android project during
 * `npx expo prebuild`, so the Wear OS companion module survives full native
 * regeneration without manual Gradle edits.
 *
 * What it does:
 *  1. settings.gradle  → adds `include ':sk8lytzWear'` + project dir binding
 *  2. app/build.gradle → adds `wearApp project(':sk8lytzWear')` dependency so
 *                        the watch APK is bundled with the phone APK on install
 */
function withWearOsModule(config) {
  // Step 1: inject into settings.gradle
  config = withSettingsGradle(config, (gradleConfig) => {
    const contents = gradleConfig.modResults.contents;

    const WEAR_SETTINGS_BLOCK = `
// @generated begin wearos-module — expo config plugin (DO NOT MODIFY)
include ':sk8lytzWear'
project(':sk8lytzWear').projectDir = new File(rootProject.projectDir, 'sk8lytzWear')
// @generated end wearos-module
`;

    if (!contents.includes(':sk8lytzWear')) {
      gradleConfig.modResults.contents = contents + WEAR_SETTINGS_BLOCK;
    }

    return gradleConfig;
  });

  // Step 2: inject wearApp dependency into app/build.gradle
  config = withAppBuildGradle(config, (gradleConfig) => {
    const contents = gradleConfig.modResults.contents;

    const WEAR_DEP = `    wearApp project(':sk8lytzWear') // @generated wearos-module`;

    if (!contents.includes('wearApp project')) {
      // Insert after the `dependencies {` opening brace
      gradleConfig.modResults.contents = contents.replace(
        /dependencies\s*\{/,
        `dependencies {\n${WEAR_DEP}`
      );
    }

    return gradleConfig;
  });

  // Step 3: Inject Notifee foregroundServiceType
  config = withAndroidManifest(config, (manifestConfig) => {
    const androidManifest = manifestConfig.modResults.manifest;
    const application = androidManifest.application[0];
    
    if (!application.service) {
      application.service = [];
    }
    
    const notifeeService = application.service.find(s => s.$['android:name'] === 'app.notifee.core.ForegroundService');
    
    if (!notifeeService) {
      application.service.push({
        $: {
          'android:name': 'app.notifee.core.ForegroundService',
          'android:foregroundServiceType': 'location|health|connectedDevice|shortService|dataSync',
          'tools:replace': 'android:foregroundServiceType'
        }
      });
    } else {
      notifeeService.$['android:foregroundServiceType'] = 'location|health|connectedDevice|shortService|dataSync';
      notifeeService.$['tools:replace'] = 'android:foregroundServiceType';
    }

    return manifestConfig;
  });

  // Step 4: Inject foregroundServiceType on react-native-background-actions' service.
  // The library's own manifest declares <service ...RNBackgroundActionsTask /> with NO
  // foregroundServiceType. On targetSDK 34+ Android rejects starting it (type `none`) →
  // InvalidForegroundServiceTypeException force-close. We add 'connectedDevice' (BLE keep-alive)
  // here; the matching runtime type is passed from BackgroundBLEService options. (fix/fgs-type-crash)
  config = withAndroidManifest(config, (manifestConfig) => {
    const androidManifest = manifestConfig.modResults.manifest;
    const application = androidManifest.application[0];

    if (!application.service) {
      application.service = [];
    }

    const BG_ACTIONS_SERVICE = 'com.asterinet.react.bgactions.RNBackgroundActionsTask';
    const bgActionsService = application.service.find(s => s.$['android:name'] === BG_ACTIONS_SERVICE);

    if (!bgActionsService) {
      application.service.push({
        $: {
          'android:name': BG_ACTIONS_SERVICE,
          'android:foregroundServiceType': 'connectedDevice',
          'tools:replace': 'android:foregroundServiceType'
        }
      });
    } else {
      bgActionsService.$['android:foregroundServiceType'] = 'connectedDevice';
      bgActionsService.$['tools:replace'] = 'android:foregroundServiceType';
    }

    return manifestConfig;
  });

  return config;
}

module.exports = withWearOsModule;
