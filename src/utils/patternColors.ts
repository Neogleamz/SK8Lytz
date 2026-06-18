import type { ThemePalette } from '../theme/theme';

/**
 * Utility to generate premium gradient colors based on pattern name/state.
 * Moved from DashboardStyles.ts (THEME-003 fix: business logic out of styles file).
 */
export const getPatternColors = (patternName?: string, Colors?: ThemePalette): [string, string] => {
  if (!patternName) return [Colors?.primary ?? '#FF5A00', Colors?.secondary ?? '#FFB800'];

  const name = patternName.toLowerCase();

  if (name.includes('fire') || name.includes('flame')) return ['#FF4D00', '#FF9E00'];
  if (name.includes('water') || name.includes('ocean')) return ['#00B2FF', '#00FFF0'];
  if (name.includes('forest') || name.includes('nature')) return ['#00FF85', '#00A3FF'];
  if (name.includes('sunset') || name.includes('gold')) return ['#FFD600', '#FF00E5'];
  if (name.includes('nebula') || name.includes('space')) return ['#7000FF', '#00FFFF'];
  if (name.includes('neon') || name.includes('cyber')) return ['#FF00E5', '#00F0FF'];
  if (name.includes('police')) return ['#FF0000', '#0000FF'];
  if (name.includes('matrix')) return ['#00FF00', '#003300'];

  // Default to branding colors
  return [Colors?.primary ?? '#FF5A00', Colors?.secondary ?? '#FFB800'];
};
