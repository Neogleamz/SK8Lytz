import { Platform } from 'react-native';
import { AppLogger } from './AppLogger';
import { ISessionSnapshot } from './SpeedTrackingService';
import { checkPermission } from './PermissionService';

let isSavingWorkout = false;

export const HealthSyncService = {
  async saveWorkout(snapshot: ISessionSnapshot) {
    if (isSavingWorkout) return;
    isSavingWorkout = true;
    if (snapshot.distanceMiles < 0.1 && snapshot.durationSec < 60) {
      AppLogger.log('APP_LOG', { reason: 'session too short/short distance' });
      return;
    }

    try {
      const hasPermission = await checkPermission('HEALTH');
      if (!hasPermission) {
        AppLogger.log('APP_LOG', { reason: 'no health sync permission' });
        return;
      }

      const endDate = new Date();
      const startDate = new Date(endDate.getTime() - snapshot.durationSec * 1000);

      if (Platform.OS === 'web') {
        return;
      }

      if (Platform.OS === 'ios') {
        const AppleHealthKit = require('react-native-health').default;

        const options = {
          type: 'SkatingSports', // HKWorkoutActivityTypeSkatingSports
          startDate: startDate.toISOString(),
          endDate: endDate.toISOString(),
          energyBurned: snapshot.healthCalories || 0,
          energyBurnedUnit: 'calorie',
          distance: snapshot.distanceMiles,
          distanceUnit: 'mile'
        };

        AppleHealthKit.saveWorkout(options, (err: Object, result: Object) => {
          if (err) {
            const errMsg = err instanceof Error ? err.message : (err && typeof (err as Record<string, unknown>).message === 'string' ? (err as Record<string, unknown>).message as string : JSON.stringify(err));
            AppLogger.error('Health sync failed', errMsg, { platform: 'ios' , payload_size: 0, ssi: 0 });
            return;
          }
          AppLogger.log('APP_LOG', { platform: 'ios', result, event: 'HEALTH_SYNC_SUCCESS' });
        });

      } else if (Platform.OS === 'android') {
        const { initialize: initHC, insertRecords } = require('react-native-health-connect');
        await initHC();

        // R-08 fix: react-native-health-connect does not export a typed Record interface.
        // Record<string, unknown> captures the heterogeneous shape while eliminating the `any` cast.
        const records: Array<Record<string, unknown>> = [
          {
            recordType: 'ExerciseSession',
            exerciseType: 60, // 60 = Skating in Health Connect API
            startTime: startDate.toISOString(),
            endTime: endDate.toISOString(),
            startZoneOffset: null,
            endZoneOffset: null,
            title: 'SK8Lytz Session',
            notes: `Distance: ${snapshot.distanceMiles.toFixed(2)} mi | Avg Speed: ${snapshot.avgSpeedMph} mph`
          }
        ];

        // Only add active calories if we recorded any
        if (snapshot.healthCalories && snapshot.healthCalories > 0) {
          records.push({
            recordType: 'TotalCaloriesBurned',
            startTime: startDate.toISOString(),
            endTime: endDate.toISOString(),
            energy: { value: snapshot.healthCalories, unit: 'kilocalories' }
          });
        }

        // Only add distance if > 0
        if (snapshot.distanceMiles > 0) {
           records.push({
             recordType: 'Distance',
             startTime: startDate.toISOString(),
             endTime: endDate.toISOString(),
            distance: { value: snapshot.distanceMiles * 1609.34, unit: 'meters' }
           });
        }

        await insertRecords(records);
        AppLogger.log('APP_LOG', { platform: 'android', event: 'HEALTH_SYNC_SUCCESS' });
      }
    } catch (e: unknown) {
      AppLogger.error('Health sync failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
    } finally {
      isSavingWorkout = false;
    }
  }
};
