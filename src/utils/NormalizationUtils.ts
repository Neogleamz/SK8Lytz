import { HW_SPEED_MAX } from '../constants/AppConstants';

/**
 * Normalizes a UI speed value (0-100) to hardware speed range (1-31).
 * Hardware does not accept 0 as a valid speed in this context.
 */
export function normalizeUISpeedToHardware(uiSpeed: number): number {
  if (uiSpeed <= 0) return 1;
  if (uiSpeed >= 100) return HW_SPEED_MAX;
  // Scale the value from 0-100 into 1-31 range.
  const scaled = Math.round((uiSpeed / 100) * HW_SPEED_MAX);
  // Ensure we never return 0 and never exceed the max limit.
  return Math.max(1, Math.min(HW_SPEED_MAX, scaled));
}
