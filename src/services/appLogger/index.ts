import { AppLoggerService } from './AppLoggerService';
import { EventType, LogEntry } from './types';

const AppLogger = new AppLoggerService();

export { AppLogger, AppLoggerService, EventType, LogEntry };
