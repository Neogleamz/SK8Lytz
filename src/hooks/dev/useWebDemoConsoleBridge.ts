import { useEffect } from 'react';
import { Platform } from 'react-native';

/**
 * useWebDemoConsoleBridge
 * 
 * Intercepts console logs ONLY when running in the Web Demo (Platform.OS === 'web' && __DEV__).
 * It sends postMessage to the parent iframe (Command Center) for live console viewing.
 */
export function useWebDemoConsoleBridge() {
  useEffect(() => {
    if (!__DEV__ || Platform.OS !== 'web' || typeof window === 'undefined') {
      return;
    }

    // Only patch if we are inside an iframe
    if (window === window.parent) {
      return;
    }

    const originalLog = console.log;
    const originalWarn = console.warn;
    const originalError = console.error;

    const sendMessage = (level: 'log' | 'warn' | 'error', args: any[]) => {
      try {
        const parsedArgs = args.map(arg => {
          if (typeof arg === 'object') {
            try {
              return JSON.stringify(arg, null, 2);
            } catch (e) {
              return String(arg);
            }
          }
          return String(arg);
        });

        window.parent.postMessage({
          source: 'sk8lytz-demo',
          level,
          data: parsedArgs
        }, '*');
      } catch (err) {
        // Silently fail if postMessage throws (e.g., disconnected parent)
      }
    };

    console.log = (...args) => {
      sendMessage('log', args);
      originalLog.apply(console, args);
    };

    console.warn = (...args) => {
      sendMessage('warn', args);
      originalWarn.apply(console, args);
    };

    console.error = (...args) => {
      sendMessage('error', args);
      originalError.apply(console, args);
    };

    return () => {
      console.log = originalLog;
      console.warn = originalWarn;
      console.error = originalError;
    };
  }, []);
}
