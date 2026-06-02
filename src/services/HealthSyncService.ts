import { Platform } from 'react-native';
import { AppLogger } from './AppLogger';
import { ISessionSnapshot } from './SpeedTrackingService';
import { checkPermission } from './PermissionService';

export const HealthSyncService = {
  async saveWorkout(snapshot: ISessionSnapshot) {
    if (snapshot.distanceMiles < 0.2 && snapshot.durationSec < 60) {
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
            AppLogger.error('Health sync failed', err, { platform: 'ios' });
            return;
          }
          AppLogger.log('APP_LOG', { platform: 'ios', result, event: 'HEALTH_SYNC_SUCCESS' });
        });

      } else if (Platform.OS === 'android') {
        const { insertRecords } = require('react-native-health-connect');

        const records = [
          {
            recordType: 'ExerciseSession',
            exerciseType: 71, // 71 = Skating in Health Connect API (approximate/closest match)
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
          } as any);
        }

        // Only add distance if > 0
        if (snapshot.distanceMiles > 0) {
           records.push({
             recordType: 'Distance',
             startTime: startDate.toISOString(),
             endTime: endDate.toISOString(),
             distance: { value: snapshot.distanceMiles * 1609.34, unit: 'meters' }
           } as any);
        }

        await insertRecords(records);
        AppLogger.log('APP_LOG', { platform: 'android', event: 'HEALTH_SYNC_SUCCESS' });
      }
    } catch (e: any) {
      AppLogger.error('Health sync failed', e);
    }
  }
};
