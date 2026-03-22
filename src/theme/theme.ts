export const Colors = {
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
