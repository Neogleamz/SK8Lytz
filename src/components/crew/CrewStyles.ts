import { Dimensions, Platform, StyleSheet } from 'react-native';
import { Spacing } from '../../theme/theme';
const { width } = Dimensions.get('window');

export const createStyles = (Colors: any) => StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background || '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    maxHeight: '92%', minHeight: '55%', paddingTop: Spacing.md,
    flex: 1,  // ← Required: allows controller's ScrollView+footer to flex properly;
              //   maxHeight:'92%' still caps the sheet height correctly in RN
  },
  closeBtn: { position: 'absolute', top: 10, right: 16, zIndex: 10, padding: Spacing.sm },
  body: { alignItems: 'center', paddingHorizontal: Spacing.xl, paddingBottom: Spacing.xxl, paddingTop: Spacing.sm },

  // Typography
  titleLarge: { color: Colors.text || '#FFF', fontSize: 24, fontWeight: '800', marginBottom: Spacing.sm, textAlign: 'center' },
  subtitle: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center', lineHeight: 19, marginBottom: Spacing.xl },

  // ── Crew Hub styles ──────────────────────────────────────────────────────
  hubHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    width: '100%', marginBottom: Spacing.lg, paddingTop: 0,
  },
  hubTitle: { color: Colors.text || '#FFF', fontSize: 22, fontWeight: '900' },
  hubSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: Spacing.xxs },
  hubStartBtn: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.xs,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm,
  },
  hubStartBtnText: { color: '#000', fontSize: 13, fontWeight: '800' },

  hubSectionLabel: {
    color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700',
    letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: Spacing.sm, marginTop: Spacing.xs,
  },
  hubSectionRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    width: '100%', marginTop: Spacing.xl, marginBottom: Spacing.sm,
  },

  hubEmptyCard: {
    width: '100%', alignItems: 'center', padding: Spacing.xl,
    backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 16,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)', marginBottom: Spacing.sm,
  },
  hubEmptyText: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center' },

  hubCrewCard: {
    width: '100%', backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 16, padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.09)',
  },
  hubCrewCardTop: { flexDirection: 'row', alignItems: 'flex-start' },
  hubCrewAvatar: {
    width: 40, height: 40, borderRadius: 20,
    alignItems: 'center', justifyContent: 'center',
  },
  hubCrewName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  hubCrewMeta: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: Spacing.xxs },
  hubOwnerBadge: { backgroundColor: 'rgba(255,170,0,0.15)', borderRadius: 6, paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs },
  hubOwnerBadgeText: { color: Colors.primary || '#FFAA00', fontSize: 9, fontWeight: '800' },
  hubCrewManageBtn: { padding: Spacing.sm },

  hubLiveSessionRow: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.md,
    marginTop: Spacing.md, padding: Spacing.md,
    backgroundColor: 'rgba(0,230,118,0.08)',
    borderRadius: 12, borderWidth: 1, borderColor: 'rgba(0,230,118,0.2)',
  },
  hubLiveDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00E676' },
  hubLiveSessionName: { color: Colors.text || '#FFF', fontSize: 13, fontWeight: '700' },
  hubLiveSessionMeta: { color: Colors.textMuted || '#888', fontSize: 10, marginTop: 1 },
  hubJoinPill: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm,
  },
  hubJoinPillText: { color: '#000', fontSize: 11, fontWeight: '900' },

  hubNoSessionRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    marginTop: Spacing.sm, paddingTop: Spacing.sm,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  hubNoSessionText: { color: Colors.textMuted || '#888', fontSize: 12 },

  hubCrewActions: {
    flexDirection: 'row', gap: Spacing.sm, width: '100%', marginTop: Spacing.xs, marginBottom: Spacing.xs,
  },
  hubCrewActionBtn: {
    flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.xs,
    borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.12)',
    borderRadius: 12, paddingVertical: Spacing.md,
  },
  hubCrewActionBtnText: { color: Colors.primary || '#FFAA00', fontSize: 11, fontWeight: '700' },

  hubActionChip: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.xs,
    borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.35)',
    borderRadius: 20, paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xs,
  },
  hubActionChipText: { color: Colors.primary || '#FFAA00', fontSize: 10, fontWeight: '700' },

  hubCodeEntry: {
    width: '100%', marginTop: Spacing.sm, marginBottom: Spacing.xs,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 14, padding: Spacing.lg,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
  },
  label: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: Spacing.xs, marginTop: Spacing.md },

  // Buttons
  primaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: Spacing.lg, paddingHorizontal: Spacing.xl, width: '100%', marginTop: Spacing.lg,
  },
  primaryBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
    borderWidth: 1.5, borderColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: Spacing.md, paddingHorizontal: Spacing.xl, width: '100%', marginTop: Spacing.md,
  },
  secondaryBtnText: { color: Colors.primary || '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn: { flexDirection: 'row', alignItems: 'center', alignSelf: 'flex-start', marginBottom: Spacing.md, gap: Spacing.xxs },
  backText: { color: Colors.textMuted || '#888', fontSize: 14 },

  // Input
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text || '#FFF', fontSize: 15,
    paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, width: '100%', marginBottom: Spacing.xxs,
  },
  codeInput: {
    fontSize: 26, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace'
  },
  errorText: { color: '#FF4444', fontSize: 12, marginTop: Spacing.sm, textAlign: 'center' },

  // Location
  locationBtn: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, width: '100%',
    borderWidth: 1, borderColor: 'rgba(255,170,0,0.3)', borderRadius: 10, borderStyle: 'dashed',
    paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, marginBottom: Spacing.xxs,
  },
  locationBtnText: { color: Colors.primary || '#FFAA00', fontSize: 13, fontWeight: '600' },
  locationChip: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, width: '100%',
    backgroundColor: 'rgba(255,170,0,0.1)', borderRadius: 10,
    padding: Spacing.md, borderWidth: 1, borderColor: 'rgba(255,170,0,0.25)',
  },
  locationChipText: { flex: 1, color: Colors.primary || '#FFAA00', fontSize: 13 },

  // Crew picker chips
  crewPickerRow: { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg, alignSelf: 'flex-start' },
  crewChip: { flexDirection: 'row', alignItems: 'center', gap: Spacing.xs, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 20, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, maxWidth: 160 },
  crewChipActive: { backgroundColor: '#FFAA00', borderColor: '#FFAA00' },
  crewChipText: { color: '#bbb', fontSize: 12, fontWeight: '700', flexShrink: 1 },
  crewChipTextActive: { color: '#000' },

  // Public/Private visibility toggle
  visibilityRow: { flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.sm, width: '100%' },
  visibilityBtn: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 12, paddingVertical: Spacing.md },
  visibilityBtnActive: { backgroundColor: 'rgba(255,255,255,0.12)', borderColor: 'rgba(255,255,255,0.5)' },
  visibilityBtnPublic: { backgroundColor: '#00CC66', borderColor: '#00CC66' },
  visibilityBtnText: { color: '#999', fontSize: 13, fontWeight: '700' },
  visibilityBtnTextActive: { color: '#fff' },
  hintText: { color: '#777', fontSize: 12, marginBottom: Spacing.md, alignSelf: 'flex-start' },

  // Calendar date/time picker buttons
  datePickerBtn: { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, width: '100%', borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.14)', borderRadius: 12, paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, marginBottom: Spacing.md },
  datePickerBtnText: { flex: 1, color: '#fff', fontSize: 14, fontWeight: '600' },

  // Join — sessions browser
  sessionCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 14, padding: Spacing.md, marginBottom: Spacing.sm,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  sessionCardName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },
  sessionCardMeta: { color: Colors.textMuted || '#888', fontSize: 11 },
  sessionCardRight: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm },
  joinPill: { backgroundColor: Colors.primary || '#FFAA00', borderRadius: 20, paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs },
  joinPillText: { color: '#000', fontSize: 12, fontWeight: '800' },
  divider: { flexDirection: 'row', alignItems: 'center', marginVertical: Spacing.lg, width: '100%', gap: Spacing.sm },
  dividerLine: { flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.08)' },
  dividerText: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1 },

  // Controller card
  controllerCard: {
    margin: Spacing.lg,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 18, padding: Spacing.lg,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  controllerCrewName: { color: Colors.text || '#FFF', fontSize: 20, fontWeight: '900', flex: 1 },
  controllerRole: { color: Colors.textMuted || '#888', fontSize: 12, marginBottom: Spacing.md },
  livePill: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.xs,
    backgroundColor: 'rgba(0,230,118,0.12)',
    borderRadius: 20, paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs,
  },
  liveDot: { width: 7, height: 7, borderRadius: 4, backgroundColor: '#00E676' },
  liveText: { color: '#00E676', fontSize: 11, fontWeight: '800', letterSpacing: 1 },
  metaRow: { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.md },
  metaChip: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.xs,
    backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 20,
    paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs,
  },
  metaChipText: { color: Colors.textMuted || '#888', fontSize: 11 },
  modeRow: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm,
    backgroundColor: 'rgba(255,170,0,0.08)', borderRadius: 10,
    paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, marginBottom: Spacing.sm,
  },
  modeText: { color: Colors.primary || '#FFAA00', fontSize: 12, fontWeight: '700' },
  inviteCodeRow: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.sm,
    backgroundColor: 'rgba(255,170,0,0.08)',
    borderRadius: 10, padding: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,170,0,0.2)',
  },
  inviteCodeLabel: { color: 'rgba(255,170,0,0.6)', fontSize: 10, fontWeight: '700', letterSpacing: 1 },
  inviteCode: { color: '#FFD700', fontSize: 22, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace', letterSpacing: 4, flex: 1 },
  syncRow: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.sm,
    backgroundColor: 'rgba(0,170,255,0.08)', borderRadius: 10, padding: Spacing.md,
  },
  syncDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00AAFF' },
  syncText: { color: '#00AAFF', fontSize: 11, fontWeight: '600', flex: 1 },

  // Member rows
  memberRow: {
    flexDirection: 'row', alignItems: 'center', paddingVertical: Spacing.sm,
    borderBottomWidth: 1, borderBottomColor: 'rgba(255,255,255,0.04)',
  },
  memberAvatar: {
    width: 36, height: 36, borderRadius: 18, backgroundColor: 'rgba(255,255,255,0.1)',
    alignItems: 'center', justifyContent: 'center',
  },
  memberAvatarText: { color: '#FFF', fontSize: 15, fontWeight: '700' },
  memberName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '600' },
  memberLeaderBadge: { color: '#FFD700', fontSize: 10, marginTop: 1 },
  handoffToggle: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.sm,
    borderWidth: 1.5, borderColor: '#FFD700', borderRadius: 10,
    paddingVertical: Spacing.sm, paddingHorizontal: Spacing.lg,
  },
  handoffToggleActive: { backgroundColor: '#FFD700' },
  handoffToggleText: { color: '#FFD700', fontSize: 13, fontWeight: '700' },
  handoffBtn: {
    flexDirection: 'row', alignItems: 'center', gap: Spacing.xs,
    backgroundColor: '#FFD700', borderRadius: 20, paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs,
  },
  handoffBtnText: { color: '#000', fontSize: 11, fontWeight: '800' },

  // Footer
  controllerFooter: {
    flexDirection: 'row', gap: Spacing.md, paddingHorizontal: Spacing.lg, paddingBottom: Spacing.xxl, paddingTop: Spacing.sm,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  doneBtn: { flex: 2, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 14, paddingVertical: Spacing.lg, alignItems: 'center' },
  doneBtnText: { color: '#000', fontSize: 14, fontWeight: '800' },
  endBtn: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.xs, borderWidth: 1.5, borderColor: '#FF4444', borderRadius: 14, paddingVertical: Spacing.lg },
  endBtnText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // ── Manage Crews Hub styles ────────────────────────────────────────────────
  mgTabBar: { flexDirection: 'row', paddingHorizontal: Spacing.md, gap: Spacing.sm, marginBottom: Spacing.xs },
  mgTab: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.xs, paddingVertical: Spacing.sm, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.06)' },
  mgTabActive: { backgroundColor: Colors.primary || '#FFAA00' },
  mgTabText: { fontSize: 11, fontWeight: '700', color: Colors.textMuted || '#888' },
  mgEmptyText: { color: Colors.textMuted || '#888', fontSize: 14, textAlign: 'center', marginTop: Spacing.xxxl, lineHeight: 22 },
  mgSearchInput: { backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 10, borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)', color: Colors.text || '#FFF', fontSize: 14, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, marginBottom: Spacing.sm },
  mgHint: { color: Colors.textMuted || '#888', fontSize: 11, lineHeight: 16 },

  // Crew card
  mgCrewCard: { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg, marginBottom: Spacing.md },
  mgAvatar: { width: 44, height: 44, borderRadius: 22, alignItems: 'center', justifyContent: 'center' },
  mgAvatarImg: { width: 44, height: 44, borderRadius: 22 },
  mgCrewName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  mgCrewSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: Spacing.xxs },
  mgAvatarRow: { flexDirection: 'row', alignItems: 'center', marginTop: Spacing.xs },
  mgMemberDot: { width: 18, height: 18, borderRadius: 9 },
  mgMemberCount: { color: Colors.textMuted || '#888', fontSize: 11, marginLeft: Spacing.sm },
  mgOwnerBadge: { backgroundColor: 'rgba(255,170,0,0.15)', borderRadius: 6, paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs },
  mgBadgeText: { color: Colors.primary || '#FFAA00', fontSize: 10, fontWeight: '800' },

  // Create crew form
  mgPhotoBtn: { height: 80, borderRadius: 12, borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.2)', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, marginBottom: Spacing.xs, overflow: 'hidden' },
  mgPhotoBtnImg: { width: '100%', height: '100%', resizeMode: 'cover' },
  mgPhotoBtnText: { color: Colors.textMuted || '#888', fontSize: 13 },
  mgColorRow: { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.md, marginBottom: Spacing.xs },
  mgColorSwatch: { width: 32, height: 32, borderRadius: 16 },
  mgColorActive: { borderWidth: 3, borderColor: '#FFF' },
  mgIconRow: { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.xs },
  mgIconBtn: { width: 44, height: 44, borderRadius: 10, alignItems: 'center', justifyContent: 'center', backgroundColor: 'rgba(255,255,255,0.07)' },

  // Crew detail
  mgCodeBox: { backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 12, padding: Spacing.lg, alignItems: 'center', gap: Spacing.xs },
  mgCodeText: { color: Colors.text || '#FFF', fontSize: 28, fontWeight: '900', letterSpacing: 6, fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' },

  // Danger actions
  dangerBtn: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, borderWidth: 1.5, borderColor: 'rgba(255,68,68,0.4)', borderRadius: 12, padding: Spacing.lg },
  dangerBtnText: { color: '#FF4444', fontSize: 14, fontWeight: '700' },

  // Edit actions
  editBtn: { flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.35)', borderRadius: 12, padding: Spacing.lg },
  editBtnText: { color: Colors.primary || '#FFAA00', fontSize: 14, fontWeight: '700' },

  // Share invite
  shareBtn: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 12, paddingVertical: Spacing.md, marginTop: Spacing.sm },
  shareBtnText: { color: '#000', fontSize: 14, fontWeight: '800' },

  // Crew stats row
  statsRow: { flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.lg, marginBottom: Spacing.xs },
  statCard: { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12, padding: Spacing.md, alignItems: 'center' },
  statNum: { color: Colors.text || '#FFF', fontSize: 16, fontWeight: '800', textAlign: 'center' },
  statLabel: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '600', marginTop: Spacing.xxs, textAlign: 'center' },

  // Nearby sessions
  nearbySessionCard: { flexDirection: 'row', alignItems: 'center', gap: Spacing.md, backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 12, padding: Spacing.md, marginBottom: Spacing.sm },
  nearbySessionName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },
  nearbySessionSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: Spacing.xxs },
  nearbyJoinBtn: { backgroundColor: Colors.primary || '#FFAA00', borderRadius: 10, paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm, minWidth: 56, alignItems: 'center' },
  nearbyJoinText: { color: '#000', fontSize: 13, fontWeight: '900' },

  // Radius pill selector
  radiusPillRow: { flexDirection: 'row', gap: Spacing.sm, paddingHorizontal: Spacing.xxs, paddingBottom: Spacing.xxs },
  radiusPill: { borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.15)', borderRadius: 20, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm },
  radiusPillActive: { backgroundColor: Colors.primary || '#FFAA00', borderColor: Colors.primary || '#FFAA00' },
  radiusPillText: { color: Colors.textMuted || '#888', fontSize: 12, fontWeight: '700' },
  radiusPillTextActive: { color: '#000', fontSize: 12, fontWeight: '700' },
  controlRow: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.md, marginTop: Spacing.md },
});
