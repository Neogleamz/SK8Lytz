/**
 * Adds randomized jitter to a base delay to decohere simultaneous retry storms.
 * @param baseMs The minimum delay in milliseconds
 * @param jitterMs The maximum random jitter to add in milliseconds
 * @returns A randomized delay between baseMs and (baseMs + jitterMs)
 */
export const jitteredDelay = (baseMs: number, jitterMs = 500): number =>
  baseMs + Math.floor(Math.random() * jitterMs);
