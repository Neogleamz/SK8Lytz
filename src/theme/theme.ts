export const Colors = {
  background: '#0A0E17',
  surface: '#1A1D2D',
  surfaceHighlight: '#262B42',
  primary: '#FF007F', // Neon Pink
  secondary: '#00F0FF', // Neon Blue
  accent: '#B500FF', // Neon Purple
  text: '#FFFFFF',
  textMuted: '#8E92A4',
  success: '#00E676',
  error: '#FF3D00',
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
