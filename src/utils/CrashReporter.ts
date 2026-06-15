import { AppLogger } from '../services/appLogger';

export const logFatalCrash = async (error: Error, stack: string) => {
  AppLogger.error('Fatal Crash', error, { stack, payload_size: 0, ssi: 0 });
  return 'dummy-event-id';
};
