export const Colors = {
  background: '#040508',
  surface: '#0F131D',
  surfaceHighlight: '#1A2133',
  primary: '#00F0FF',
  secondary: '#FF0055',
  accent: '#B026FF',
  text: '#FFFFFF',
  textMuted: '#6C7A9C',
  success: '#00E88F',
  error: '#FF3D71',
};

export const Typography = {
  header: { fontSize: 32, fontWeight: '900' as const, color: Colors.text, textTransform: 'uppercase' as const, letterSpacing: 2 },
  title: { fontSize: 22, fontWeight: '800' as const, color: Colors.text, letterSpacing: 0.5 },
  body: { fontSize: 16, color: Colors.text, fontWeight: '500' as const },
  caption: { fontSize: 13, color: Colors.textMuted, fontWeight: '600' as const },
};

export const Layout = {
  padding: 20,
  borderRadius: 24,
};
