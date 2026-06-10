import { AppLogger } from '../services/AppLogger';

export const logFatalCrash = async (error: Error, stack: string) => {
  AppLogger.error('Fatal Crash', { message: error.message, stack , payload_size: 0, ssi: 0 });
  return 'dummy-event-id';
};
