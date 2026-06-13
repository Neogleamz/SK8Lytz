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
  textDim: '#6B85A0',
  border: '#2E5FA3',
  success: '#00E88F',
  error: '#FF3D71',
  warning: '#FFB800',
  isDark: true,
};

export const LightColors: ThemePalette = {
  background: '#EAEFF5',
  surface: '#CBD6E2',
  surfaceHighlight: '#DDE5EE',
  primary: '#FF5A00',
  secondary: '#FFB800',
  accent: '#1B4279',
  text: '#0A1C38',
  textMuted: '#5C7491',
  textDim: '#8A9EB5',
  border: '#B0C0D0',
  success: '#00C476',
  error: '#FF3D71',
  warning: '#E07A00',
  isDark: false,
};

// Default export stays as DarkColors so existing destructured imports don't break
export const Colors = DarkColors;

export const Typography = {
  header: { fontSize: 24, fontWeight: 'normal' as const, textTransform: 'uppercase' as const, letterSpacing: 2, fontFamily: 'Righteous' },
  title: { fontSize: 16, fontWeight: 'normal' as const, letterSpacing: 0.5, fontFamily: 'Righteous' },
  body: { fontSize: 14, fontWeight: 'normal' as const, fontFamily: 'Righteous' },
  caption: { fontSize: 11, fontWeight: 'normal' as const, fontFamily: 'Righteous' },
};

export const Spacing = {
  xxs: 2,
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 24,
  xxl: 32,
  xxxl: 40,
  huge: 48,
  giant: 64,
};

export const Layout = {
  padding: Spacing.lg,
  borderRadius: Spacing.xl,
};

import { Platform, ViewStyle, TextStyle } from 'react-native';

export const Shadows = {
  soft: Platform.select<ViewStyle>({
    ios: { shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.15, shadowRadius: 4 },
    android: { elevation: 3 },
    web: { shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.15, shadowRadius: 4 },
    default: {}
  }) as ViewStyle,
  medium: Platform.select<ViewStyle>({
    ios: { shadowColor: '#000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.2, shadowRadius: 6 },
    android: { elevation: 5 },
    web: { shadowColor: '#000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.2, shadowRadius: 6 },
    default: {}
  }) as ViewStyle,
  glow: (color: string): ViewStyle => Platform.select<ViewStyle>({
    ios: { shadowColor: color, shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.8, shadowRadius: 8 },
    android: { shadowColor: color, elevation: 8 },
    web: { shadowColor: color, shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.8, shadowRadius: 8 },
    default: {}
  }) as ViewStyle,
};

export const TextShadows = {
  glow: (color: string, radius: number = 10): TextStyle => Platform.select<TextStyle>({
    web: { textShadow: `0 0 ${radius}px ${color}` } as TextStyle,
    default: { textShadowColor: color, textShadowRadius: radius, textShadowOffset: { width: 0, height: 0 } }
  }) as TextStyle
};
