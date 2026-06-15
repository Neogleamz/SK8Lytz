import { Platform, StyleSheet } from 'react-native';
import { Spacing, ThemePalette } from '../../theme/theme';

export const createStyles = (Colors: ThemePalette) => StyleSheet.create({
  overlay:    { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background ?? '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    height: '93%', paddingTop: Spacing.xl,
  },
  closeBtn:   { position: 'absolute', top: 16, right: 16, zIndex: 10, padding: Spacing.sm },
  sheetTitle: { color: Colors.text ?? '#FFF', fontSize: 22, fontWeight: '800', textAlign: 'center', marginBottom: Spacing.lg },

  // Tabs
  tabBar: {
    flexDirection: 'row', paddingHorizontal: Spacing.md, gap: Spacing.sm,
    marginBottom: Spacing.xs, paddingBottom: Spacing.xxs,
  },
  tabBtn: {
    flex: 1, flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: Spacing.xxs,
    paddingVertical: Spacing.sm, paddingHorizontal: Spacing.xs,
    borderRadius: 12, backgroundColor: 'rgba(255,255,255,0.06)',
  },
  tabBtnActive:     { backgroundColor: Colors.primary ?? '#FFAA00' },
  tabBtnText:       { color: Colors.textMuted ?? '#888', fontSize: 10, fontWeight: '700', textAlign: 'center' },
  tabBtnTextActive: { color: '#000' },

  // Body
  body:          { padding: Spacing.xl, paddingBottom: Spacing.xl },
  label:         { color: Colors.textMuted ?? '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginTop: Spacing.lg, marginBottom: Spacing.sm },
  sectionHeader: { color: Colors.textMuted ?? '#888', fontSize: 11, fontWeight: '800', letterSpacing: 1.5, marginTop: Spacing.sm, marginBottom: Spacing.md, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.06)', paddingBottom: Spacing.sm },
  hint:          { color: 'rgba(255,255,255,0.3)', fontSize: 11, lineHeight: 16 },
  errorText:     { color: '#FF4444', fontSize: 13, marginTop: Spacing.sm },

  // Inputs
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text ?? '#FFF', fontSize: 15,
    paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, width: '100%', marginBottom: Spacing.xs,
  },
  codeInput: {
    fontSize: 28, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.select({ ios: 'Courier New', default: 'monospace' }),
  },

  // Profile
  avatarCircle: { width: 80, height: 80, borderRadius: 40, alignItems: 'center', justifyContent: 'center', alignSelf: 'center', marginBottom: Spacing.xs },
  avatarText:   { color: '#000', fontSize: 28, fontWeight: '900' },
  emailDisplay: { color: Colors.textMuted ?? '#888', fontSize: 12, textAlign: 'center', marginBottom: Spacing.xs },
  usernameRow:  { flexDirection: 'row', alignItems: 'center', gap: Spacing.xs },
  atSign:       { color: Colors.textMuted ?? '#888', fontSize: 18, fontWeight: '700', paddingBottom: Spacing.xs },
  colorRow:     { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.md, marginBottom: Spacing.xs },
  colorSwatch:  { width: 30, height: 30, borderRadius: 15 },
  colorSwatchActive: { borderWidth: 3, borderColor: '#FFF' },
  statsRow:     { flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.xl },
  statCard:     { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg, alignItems: 'center' },
  statNum:      { color: Colors.text ?? '#FFF', fontSize: 26, fontWeight: '900' },
  statLabel:    { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },

  // History preview
  historyRow:   { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, paddingVertical: Spacing.sm, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  historyDot:   { width: 8, height: 8, borderRadius: 4 },
  historyName:  { color: Colors.text ?? '#FFF', fontSize: 13, fontWeight: '600' },
  historyDate:  { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: 1 },

  // Security
  msgBanner:    { borderRadius: 10, borderWidth: 1, padding: Spacing.md, marginBottom: Spacing.md },
  msgText:      { fontSize: 13, fontWeight: '600', lineHeight: 18 },
  pwdRow:       { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm },
  eyeBtn:       { padding: Spacing.md },
  currentEmail: { color: Colors.textMuted ?? '#888', fontSize: 12, marginBottom: Spacing.xs },

  // Crews
  crewCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14,
    padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  crewCardIcon:   { width: 40, height: 40, borderRadius: 20, backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' },
  crewCardName:   { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  crewCardCode:   { color: Colors.textMuted ?? '#888', fontSize: 12, marginTop: Spacing.xxs },
  ownerBadge:     { fontSize: 9, fontWeight: '800', letterSpacing: 1, color: '#FFD700', borderWidth: 1, borderColor: '#FFD700', borderRadius: 4, paddingHorizontal: Spacing.xs, paddingVertical: 1 },
  crewActionBtn:  { paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 20, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)' },
  crewActionText: { color: Colors.textMuted ?? '#888', fontSize: 12, fontWeight: '700' },

  // Devices
  deviceCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14,
    padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  deviceName:     { color: Colors.text ?? '#FFF', fontSize: 15, fontWeight: '700' },
  deviceMeta:     { color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },
  deviceIconBtn:  { padding: Spacing.sm, borderRadius: 8, backgroundColor: 'rgba(255,255,255,0.06)' },
  deviceSaveBtn:  { padding: Spacing.sm, borderRadius: 8, backgroundColor: Colors.primary ?? '#FFAA00' },

  // Settings
  settingRow:     { flexDirection: 'row', alignItems: 'center', paddingVertical: Spacing.lg, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  settingLabel:   { color: Colors.text ?? '#FFF', fontSize: 14, fontWeight: '600' },
  settingSubLabel:{ color: Colors.textMuted ?? '#888', fontSize: 11, marginTop: Spacing.xxs },
  signOutBtn:     { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, paddingVertical: Spacing.lg, borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.05)' },
  signOutText:    { color: '#FF4444', fontSize: 15, fontWeight: '700' },

  // Danger zone
  dangerZone:     { marginTop: Spacing.xl, borderWidth: 1, borderColor: 'rgba(255,68,68,0.25)', borderRadius: 14, padding: Spacing.lg },
  dangerHeader:   { color: '#FF4444', fontSize: 13, fontWeight: '800', letterSpacing: 1, marginBottom: Spacing.sm },
  dangerSub:      { color: Colors.textMuted ?? '#888', fontSize: 12, marginBottom: Spacing.lg, lineHeight: 17 },
  deleteAccountBtn: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, borderWidth: 1, borderColor: '#FF4444', borderRadius: 10, paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, alignSelf: 'flex-start' },
  deleteAccountText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // Buttons
  primaryBtn:     { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, backgroundColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: Spacing.lg, width: '100%', marginTop: Spacing.lg },
  primaryBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn:   { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, borderWidth: 1.5, borderColor: Colors.primary ?? '#FFAA00', borderRadius: 14, paddingVertical: Spacing.md, width: '100%', marginTop: Spacing.md },
  secondaryBtnText: { color: Colors.primary ?? '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn:        { flexDirection: 'row', alignItems: 'center', gap: Spacing.xs, marginBottom: Spacing.lg },
  backText:       { color: Colors.textMuted ?? '#888', fontSize: 14 },

  // Empty states
  emptyState:     { alignItems: 'center', paddingVertical: Spacing.xxxl },
  emptyTitle:     { color: Colors.text ?? '#FFF', fontSize: 18, fontWeight: '700', marginTop: Spacing.lg },
  emptySubtitle:  { color: Colors.textMuted ?? '#888', fontSize: 13, textAlign: 'center', marginTop: Spacing.sm, lineHeight: 20 },
});
