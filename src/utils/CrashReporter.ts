import { AppLogger } from '../services/AppLogger';

export const logFatalCrash = async (error: Error, stack: string) => {
  AppLogger.error('Fatal Crash', { message: error.message, stack });
  return 'dummy-event-id';
};
