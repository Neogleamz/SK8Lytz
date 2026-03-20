export const Colors = {
  background: '#090B10',
  surface: '#121622',
  surfaceHighlight: '#1E2535',
  primary: '#FF6E00', // Neogleamz Orange
  secondary: '#FF9F0A', // Vibrant Orange
  accent: '#FFD60A', // Gold/Yellow Accent
  text: '#FFFFFF',
  textMuted: '#A0A4B8',
  success: '#32D74B',
  error: '#FF453A',
};

export const Typography = {
  header: { fontSize: 28, fontWeight: '900' as const, color: Colors.text, textTransform: 'uppercase' as const, letterSpacing: 1.5 },
  title: { fontSize: 20, fontWeight: '700' as const, color: Colors.text },
  body: { fontSize: 16, color: Colors.text, fontWeight: '500' as const },
  caption: { fontSize: 13, color: Colors.textMuted },
};

export const Layout = {
  padding: 20,
  borderRadius: 16,
};
