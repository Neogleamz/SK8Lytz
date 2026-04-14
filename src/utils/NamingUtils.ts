/**
 * NamingUtils.ts — SK8Lytz Naming Identity Standardizer
 * 
 * Centralizer for deterministic fallback names so the UI and
 * the DB caches never drift or "hallucinate" random default names.
 */

export const getDefaultGroupName = (productType?: string): string => {
  if (!productType || productType === 'UNKNOWN') return 'Identifying...';
  return `My SK8Lytz ${productType}`;
};

export const getDefaultDeviceName = (macId: string): string => {
  if (!macId) return 'Unknown SK8Lytz';
  const cleanId = macId.replace(/:/g, '').slice(-4).toUpperCase();
  return `SK8Lytz-${cleanId}`;
};
