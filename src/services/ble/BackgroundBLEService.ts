import BackgroundService from 'react-native-background-actions';
import { Platform } from 'react-native';
import { AppLogger } from '../appLogger';

const backgroundTask = async (_taskData?: { delay: number }) => {
    // Keep the service alive indefinitely
    await new Promise(() => {});
};

const options = {
    taskName: 'SK8LytzBLEKeepAlive',
    taskTitle: 'SK8Lytz — Connected to your skates',
    taskDesc: 'Maintaining background connection to your LED skates.',
    taskIcon: {
        name: 'ic_launcher',
        type: 'mipmap',
    },
    color: '#F79320', // SK8Lytz brand orange
    // Android 14+ (targetSDK 34+) prohibits starting a foreground service with type `none`.
    // react-native-background-actions passes this to startForeground(); omitting it sends 0
    // → InvalidForegroundServiceTypeException → native force-close (bypasses JS try/catch).
    // 'connectedDevice' matches the BLE keep-alive purpose; FOREGROUND_SERVICE_CONNECTED_DEVICE
    // is declared in app.config.js, and the manifest <service> type is injected by
    // plugins/withWearOsModule.js. (fix/fgs-type-crash)
    foregroundServiceType: ['connectedDevice' as const],
    parameters: {
        delay: 1000,
    },
};

export class BackgroundBLEService {
    static async startKeepAlive() {
        if (Platform.OS !== 'android') return;
        try {
            if (!BackgroundService.isRunning()) {
                await BackgroundService.start(backgroundTask, options);
            }
        } catch (e) {
            AppLogger.warn('[BackgroundBLE] Failed to start foreground service', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
        }
    }

    static async stopKeepAlive() {
        if (Platform.OS !== 'android') return;
        try {
            if (BackgroundService.isRunning()) {
                await BackgroundService.stop();
            }
        } catch (e) {
            AppLogger.warn('[BackgroundBLE] Failed to stop foreground service', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
        }
    }
}
