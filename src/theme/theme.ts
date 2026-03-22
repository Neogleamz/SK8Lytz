// SK8Lytz Theme — Dark & Light palettes
// SK8Lytz brand: Blue #1B4279 / #245596, Orange #FF5A00, Amber #FFB800

export type ThemePalette = typeof DarkColors;

export const DarkColors = {
  background: '#1B4279',
  surface: '#245596',
  surfaceHighlight: '#3172C9',
  primary: '#FF5A00',
  secondary: '#FFB800',
  accent: '#FF3300',
  text: '#FFFFFF',
  textMuted: '#A0B4CF',
  success: '#00E88F',
  error: '#FF3D71',
  isDark: true,
};

export const LightColors: ThemePalette = {
  background: '#F5F7FA',
  surface: '#FFFFFF',
  surfaceHighlight: '#E8EEF7',
  primary: '#FF5A00',
  secondary: '#FFB800',
  accent: '#1B4279',
  text: '#1A2A42',
  textMuted: '#5A7A9F',
  success: '#00C476',
  error: '#E03260',
  isDark: false,
};

// Default export stays as DarkColors so existing destructured imports don't break
export const Colors = DarkColors;

export const Typography = {
  header: { fontSize: 32, fontWeight: '900' as const, textTransform: 'uppercase' as const, letterSpacing: 2 },
  title: { fontSize: 22, fontWeight: '800' as const, letterSpacing: 0.5 },
  body: { fontSize: 16, fontWeight: '500' as const },
  caption: { fontSize: 13, fontWeight: '600' as const },
};

export const Layout = {
  padding: 20,
  borderRadius: 24,
};
