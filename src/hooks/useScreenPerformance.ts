import { useEffect, useRef, useCallback } from 'react';
import { InteractionManager } from 'react-native';
import { AppLogger } from '../services/AppLogger';

/**
 * useScreenPerformance
 * 
 * Surgical performance telemetry hook for tracking page load times.
 * - TTID (Time to Initial Display): Measured automatically when InteractionManager clears.
 * - TTFD (Time to Fully Drawn): Measured manually when the component invokes `markFullyDrawn()`.
 */
export function useScreenPerformance(screenName: string) {
  const mountTime = useRef(Date.now());
  const ttidLogged = useRef(false);
  const ttfdLogged = useRef(false);

  useEffect(() => {
    // Re-initialize on mount if screenName changes
    mountTime.current = Date.now();
    ttidLogged.current = false;
    ttfdLogged.current = false;

    // TTID is calculated when React Native finishes the initial render interactions.
    const interactionTask = InteractionManager.runAfterInteractions(() => {
      if (!ttidLogged.current) {
        const ttidMs = Date.now() - mountTime.current;
        AppLogger.log('SCREEN_LOAD_TTID', { screen: screenName, ttidMs });
        ttidLogged.current = true;
      }
    });

    return () => {
      interactionTask.cancel();
    };
  }, [screenName]);

  // Expose a manual trigger for when data fetching resolves (e.g., Supabase, BLE)
  const markFullyDrawn = useCallback(() => {
    if (!ttfdLogged.current) {
      const ttfdMs = Date.now() - mountTime.current;
      AppLogger.log('SCREEN_LOAD_TTFD', { screen: screenName, ttfdMs });
      ttfdLogged.current = true;
    }
  }, [screenName]);

  return { markFullyDrawn };
}
