/**
 * Hash a PII value (MAC address, display name) for safe telemetry logging.
 * Returns first 8 chars of a custom hash for log correlation without exposure.
 */
export const scrubPII = (value: string): string => {
  if (!value) return 'UNKNOWN';
  // React Native lacks built-in crypto — use a simple deterministic hash
  let hash = 0;
  for (let i = 0; i < value.length; i++) {
    hash = ((hash << 5) - hash) + value.charCodeAt(i);
    hash |= 0; // Convert to 32-bit int
  }
  return `scrubbed_${Math.abs(hash).toString(16).padStart(8, '0')}`;
};
