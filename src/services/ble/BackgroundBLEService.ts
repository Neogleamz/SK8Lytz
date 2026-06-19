import BackgroundService from 'react-native-background-actions';
import { Platform } from 'react-native';
import { AppLogger } from '../appLogger';

const sleep = (time: number) => new Promise<void>((resolve) => setTimeout(() => resolve(), time));

const backgroundTask = async (taskDataArguments: any) => {
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
