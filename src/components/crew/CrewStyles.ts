import { StyleSheet, Dimensions, Platform } from 'react-native';
const { width } = Dimensions.get('window');

export const createStyles = (Colors: any) => StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: Colors.background || '#0D0D0D',
    borderTopLeftRadius: 26, borderTopRightRadius: 26,
    maxHeight: '92%', minHeight: '55%', paddingTop: 10,
    flex: 1,  // ← Required: allows controller's ScrollView+footer to flex properly;
              //   maxHeight:'92%' still caps the sheet height correctly in RN
  },
  closeBtn: { position: 'absolute', top: 10, right: 16, zIndex: 10, padding: 8 },
  body: { alignItems: 'center', paddingHorizontal: 24, paddingBottom: 32, paddingTop: 6 },

  // Typography
  titleLarge: { color: Colors.text || '#FFF', fontSize: 24, fontWeight: '800', marginBottom: 8, textAlign: 'center' },
  subtitle: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center', lineHeight: 19, marginBottom: 20 },

  // ── Crew Hub styles ──────────────────────────────────────────────────────
  hubHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    width: '100%', marginBottom: 18, paddingTop: 0,
  },
  hubTitle: { color: Colors.text || '#FFF', fontSize: 22, fontWeight: '900' },
  hubSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  hubStartBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 5,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: 14, paddingVertical: 9,
  },
  hubStartBtnText: { color: '#000', fontSize: 13, fontWeight: '800' },

  hubSectionLabel: {
    color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700',
    letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: 8, marginTop: 4,
  },
  hubSectionRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    width: '100%', marginTop: 20, marginBottom: 8,
  },

  hubEmptyCard: {
    width: '100%', alignItems: 'center', padding: 20,
    backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 16,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)', marginBottom: 8,
  },
  hubEmptyText: { color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center' },

  hubCrewCard: {
    width: '100%', backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 16, padding: 14, marginBottom: 10,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.09)',
  },
  hubCrewCardTop: { flexDirection: 'row', alignItems: 'flex-start' },
  hubCrewAvatar: {
    width: 40, height: 40, borderRadius: 20,
    alignItems: 'center', justifyContent: 'center',
  },
  hubCrewName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  hubCrewMeta: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  hubOwnerBadge: { backgroundColor: 'rgba(255,170,0,0.15)', borderRadius: 6, paddingHorizontal: 6, paddingVertical: 2 },
  hubOwnerBadgeText: { color: Colors.primary || '#FFAA00', fontSize: 9, fontWeight: '800' },
  hubCrewManageBtn: { padding: 6 },

  hubLiveSessionRow: {
    flexDirection: 'row', alignItems: 'center', gap: 10,
    marginTop: 10, padding: 10,
    backgroundColor: 'rgba(0,230,118,0.08)',
    borderRadius: 12, borderWidth: 1, borderColor: 'rgba(0,230,118,0.2)',
  },
  hubLiveDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00E676' },
  hubLiveSessionName: { color: Colors.text || '#FFF', fontSize: 13, fontWeight: '700' },
  hubLiveSessionMeta: { color: Colors.textMuted || '#888', fontSize: 10, marginTop: 1 },
  hubJoinPill: {
    flexDirection: 'row', alignItems: 'center', gap: 3,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 20, paddingHorizontal: 10, paddingVertical: 6,
  },
  hubJoinPillText: { color: '#000', fontSize: 11, fontWeight: '900' },

  hubNoSessionRow: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    marginTop: 8, paddingTop: 8,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  hubNoSessionText: { color: Colors.textMuted || '#888', fontSize: 12 },

  hubCrewActions: {
    flexDirection: 'row', gap: 8, width: '100%', marginTop: 4, marginBottom: 4,
  },
  hubCrewActionBtn: {
    flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 5,
    borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.12)',
    borderRadius: 12, paddingVertical: 10,
  },
  hubCrewActionBtnText: { color: Colors.primary || '#FFAA00', fontSize: 11, fontWeight: '700' },

  hubActionChip: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.35)',
    borderRadius: 20, paddingHorizontal: 8, paddingVertical: 4,
  },
  hubActionChipText: { color: Colors.primary || '#FFAA00', fontSize: 10, fontWeight: '700' },

  hubCodeEntry: {
    width: '100%', marginTop: 8, marginBottom: 4,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 14, padding: 14,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
  },
  label: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: 5, marginTop: 12 },

  // Buttons
  primaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    backgroundColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 14, paddingHorizontal: 24, width: '100%', marginTop: 16,
  },
  primaryBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
  secondaryBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
    borderWidth: 1.5, borderColor: Colors.primary || '#FFAA00',
    borderRadius: 14, paddingVertical: 13, paddingHorizontal: 24, width: '100%', marginTop: 10,
  },
  secondaryBtnText: { color: Colors.primary || '#FFAA00', fontSize: 15, fontWeight: '700' },
  backBtn: { flexDirection: 'row', alignItems: 'center', alignSelf: 'flex-start', marginBottom: 10, gap: 2 },
  backText: { color: Colors.textMuted || '#888', fontSize: 14 },

  // Input
  input: {
    backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
    color: Colors.text || '#FFF', fontSize: 15,
    paddingHorizontal: 14, paddingVertical: 11, width: '100%', marginBottom: 2,
  },
  codeInput: {
    fontSize: 26, fontWeight: '900', letterSpacing: 8, textAlign: 'center',
    fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace'
  },
  errorText: { color: '#FF4444', fontSize: 12, marginTop: 6, textAlign: 'center' },

  // Location
  locationBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 8, width: '100%',
    borderWidth: 1, borderColor: 'rgba(255,170,0,0.3)', borderRadius: 10, borderStyle: 'dashed',
    paddingVertical: 10, paddingHorizontal: 14, marginBottom: 2,
  },
  locationBtnText: { color: Colors.primary || '#FFAA00', fontSize: 13, fontWeight: '600' },
  locationChip: {
    flexDirection: 'row', alignItems: 'center', gap: 6, width: '100%',
    backgroundColor: 'rgba(255,170,0,0.1)', borderRadius: 10,
    padding: 10, borderWidth: 1, borderColor: 'rgba(255,170,0,0.25)',
  },
  locationChipText: { flex: 1, color: Colors.primary || '#FFAA00', fontSize: 13 },

  // Crew picker chips
  crewPickerRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 14, alignSelf: 'flex-start' },
  crewChip: { flexDirection: 'row', alignItems: 'center', gap: 5, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 7, maxWidth: 160 },
  crewChipActive: { backgroundColor: '#FFAA00', borderColor: '#FFAA00' },
  crewChipText: { color: '#bbb', fontSize: 12, fontWeight: '700', flexShrink: 1 },
  crewChipTextActive: { color: '#000' },

  // Public/Private visibility toggle
  visibilityRow: { flexDirection: 'row', gap: 10, marginBottom: 6, width: '100%' },
  visibilityBtn: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 6, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 12, paddingVertical: 10 },
  visibilityBtnActive: { backgroundColor: 'rgba(255,255,255,0.12)', borderColor: 'rgba(255,255,255,0.5)' },
  visibilityBtnPublic: { backgroundColor: '#00CC66', borderColor: '#00CC66' },
  visibilityBtnText: { color: '#999', fontSize: 13, fontWeight: '700' },
  visibilityBtnTextActive: { color: '#fff' },
  hintText: { color: '#777', fontSize: 12, marginBottom: 12, alignSelf: 'flex-start' },

  // Calendar date/time picker buttons
  datePickerBtn: { flexDirection: 'row', alignItems: 'center', gap: 10, width: '100%', borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.14)', borderRadius: 12, paddingVertical: 12, paddingHorizontal: 14, marginBottom: 10 },
  datePickerBtnText: { flex: 1, color: '#fff', fontSize: 14, fontWeight: '600' },

  // Join — sessions browser
  sessionCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 14, padding: 12, marginBottom: 8,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  sessionCardName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },
  sessionCardMeta: { color: Colors.textMuted || '#888', fontSize: 11 },
  sessionCardRight: { flexDirection: 'row', alignItems: 'center', gap: 6 },
  joinPill: { backgroundColor: Colors.primary || '#FFAA00', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 5 },
  joinPillText: { color: '#000', fontSize: 12, fontWeight: '800' },
  divider: { flexDirection: 'row', alignItems: 'center', marginVertical: 14, width: '100%', gap: 8 },
  dividerLine: { flex: 1, height: 1, backgroundColor: 'rgba(255,255,255,0.08)' },
  dividerText: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700', letterSpacing: 1 },

  // Controller card
  controllerCard: {
    margin: 16,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 18, padding: 16,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  controllerCrewName: { color: Colors.text || '#FFF', fontSize: 20, fontWeight: '900', flex: 1 },
  controllerRole: { color: Colors.textMuted || '#888', fontSize: 12, marginBottom: 10 },
  livePill: {
    flexDirection: 'row', alignItems: 'center', gap: 5,
    backgroundColor: 'rgba(0,230,118,0.12)',
    borderRadius: 20, paddingHorizontal: 10, paddingVertical: 4,
  },
  liveDot: { width: 7, height: 7, borderRadius: 4, backgroundColor: '#00E676' },
  liveText: { color: '#00E676', fontSize: 11, fontWeight: '800', letterSpacing: 1 },
  metaRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginBottom: 10 },
  metaChip: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 20,
    paddingHorizontal: 10, paddingVertical: 4,
  },
  metaChipText: { color: Colors.textMuted || '#888', fontSize: 11 },
  modeRow: {
    flexDirection: 'row', alignItems: 'center', gap: 6,
    backgroundColor: 'rgba(255,170,0,0.08)', borderRadius: 10,
    paddingHorizontal: 10, paddingVertical: 8, marginBottom: 6,
  },
  modeText: { color: Colors.primary || '#FFAA00', fontSize: 12, fontWeight: '700' },
  inviteCodeRow: {
    flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 8,
    backgroundColor: 'rgba(255,170,0,0.08)',
    borderRadius: 10, padding: 10,
    borderWidth: 1, borderColor: 'rgba(255,170,0,0.2)',
  },
  inviteCodeLabel: { color: 'rgba(255,170,0,0.6)', fontSize: 10, fontWeight: '700', letterSpacing: 1 },
  inviteCode: { color: '#FFD700', fontSize: 22, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace', letterSpacing: 4, flex: 1 },
  syncRow: {
    flexDirection: 'row', alignItems: 'center', gap: 8, marginTop: 8,
    backgroundColor: 'rgba(0,170,255,0.08)', borderRadius: 10, padding: 10,
  },
  syncDot: { width: 8, height: 8, borderRadius: 4, backgroundColor: '#00AAFF' },
  syncText: { color: '#00AAFF', fontSize: 11, fontWeight: '600', flex: 1 },

  // Member rows
  memberRow: {
    flexDirection: 'row', alignItems: 'center', paddingVertical: 9,
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
    flexDirection: 'row', alignItems: 'center', gap: 7,
    borderWidth: 1.5, borderColor: '#FFD700', borderRadius: 10,
    paddingVertical: 9, paddingHorizontal: 14,
  },
  handoffToggleActive: { backgroundColor: '#FFD700' },
  handoffToggleText: { color: '#FFD700', fontSize: 13, fontWeight: '700' },
  handoffBtn: {
    flexDirection: 'row', alignItems: 'center', gap: 4,
    backgroundColor: '#FFD700', borderRadius: 20, paddingHorizontal: 10, paddingVertical: 5,
  },
  handoffBtnText: { color: '#000', fontSize: 11, fontWeight: '800' },

  // Footer
  controllerFooter: {
    flexDirection: 'row', gap: 10, paddingHorizontal: 16, paddingBottom: 28, paddingTop: 8,
    borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.06)',
  },
  doneBtn: { flex: 2, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 14, paddingVertical: 14, alignItems: 'center' },
  doneBtnText: { color: '#000', fontSize: 14, fontWeight: '800' },
  endBtn: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 4, borderWidth: 1.5, borderColor: '#FF4444', borderRadius: 14, paddingVertical: 14 },
  endBtnText: { color: '#FF4444', fontSize: 13, fontWeight: '700' },

  // ── Manage Crews Hub styles ────────────────────────────────────────────────
  mgTabBar: { flexDirection: 'row', paddingHorizontal: 12, gap: 6, marginBottom: 4 },
  mgTab: { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 4, paddingVertical: 8, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.06)' },
  mgTabActive: { backgroundColor: Colors.primary || '#FFAA00' },
  mgTabText: { fontSize: 11, fontWeight: '700', color: Colors.textMuted || '#888' },
  mgEmptyText: { color: Colors.textMuted || '#888', fontSize: 14, textAlign: 'center', marginTop: 40, lineHeight: 22 },
  mgSearchInput: { backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 10, borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)', color: Colors.text || '#FFF', fontSize: 14, paddingHorizontal: 12, paddingVertical: 9, marginBottom: 6 },
  mgHint: { color: Colors.textMuted || '#888', fontSize: 11, lineHeight: 16 },

  // Crew card
  mgCrewCard: { flexDirection: 'row', alignItems: 'center', gap: 12, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: 14, marginBottom: 10 },
  mgAvatar: { width: 44, height: 44, borderRadius: 22, alignItems: 'center', justifyContent: 'center' },
  mgAvatarImg: { width: 44, height: 44, borderRadius: 22 },
  mgCrewName: { color: Colors.text || '#FFF', fontSize: 15, fontWeight: '700' },
  mgCrewSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  mgAvatarRow: { flexDirection: 'row', alignItems: 'center', marginTop: 4 },
  mgMemberDot: { width: 18, height: 18, borderRadius: 9 },
  mgMemberCount: { color: Colors.textMuted || '#888', fontSize: 11, marginLeft: 6 },
  mgOwnerBadge: { backgroundColor: 'rgba(255,170,0,0.15)', borderRadius: 6, paddingHorizontal: 8, paddingVertical: 3 },
  mgBadgeText: { color: Colors.primary || '#FFAA00', fontSize: 10, fontWeight: '800' },

  // Create crew form
  mgPhotoBtn: { height: 80, borderRadius: 12, borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.2)', alignItems: 'center', justifyContent: 'center', gap: 6, marginBottom: 4, overflow: 'hidden' },
  mgPhotoBtnImg: { width: '100%', height: '100%', resizeMode: 'cover' },
  mgPhotoBtnText: { color: Colors.textMuted || '#888', fontSize: 13 },
  mgColorRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 10, marginBottom: 4 },
  mgColorSwatch: { width: 32, height: 32, borderRadius: 16 },
  mgColorActive: { borderWidth: 3, borderColor: '#FFF' },
  mgIconRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 4 },
  mgIconBtn: { width: 44, height: 44, borderRadius: 10, alignItems: 'center', justifyContent: 'center', backgroundColor: 'rgba(255,255,255,0.07)' },

  // Crew detail
  mgCodeBox: { backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 12, padding: 14, alignItems: 'center', gap: 4 },
  mgCodeText: { color: Colors.text || '#FFF', fontSize: 28, fontWeight: '900', letterSpacing: 6, fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace' },

  // Danger actions
  dangerBtn: { flexDirection: 'row', alignItems: 'center', gap: 8, borderWidth: 1.5, borderColor: 'rgba(255,68,68,0.4)', borderRadius: 12, padding: 14 },
  dangerBtnText: { color: '#FF4444', fontSize: 14, fontWeight: '700' },

  // Edit actions
  editBtn: { flexDirection: 'row', alignItems: 'center', gap: 8, borderWidth: 1.5, borderColor: 'rgba(255,170,0,0.35)', borderRadius: 12, padding: 14 },
  editBtnText: { color: Colors.primary || '#FFAA00', fontSize: 14, fontWeight: '700' },

  // Share invite
  shareBtn: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, backgroundColor: Colors.primary || '#FFAA00', borderRadius: 12, paddingVertical: 13, marginTop: 8 },
  shareBtnText: { color: '#000', fontSize: 14, fontWeight: '800' },

  // Crew stats row
  statsRow: { flexDirection: 'row', gap: 8, marginTop: 14, marginBottom: 4 },
  statCard: { flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 12, padding: 12, alignItems: 'center' },
  statNum: { color: Colors.text || '#FFF', fontSize: 16, fontWeight: '800', textAlign: 'center' },
  statLabel: { color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '600', marginTop: 2, textAlign: 'center' },

  // Nearby sessions
  nearbySessionCard: { flexDirection: 'row', alignItems: 'center', gap: 10, backgroundColor: 'rgba(255,255,255,0.06)', borderRadius: 12, padding: 12, marginBottom: 8 },
  nearbySessionName: { color: Colors.text || '#FFF', fontSize: 14, fontWeight: '700' },
  nearbySessionSub: { color: Colors.textMuted || '#888', fontSize: 11, marginTop: 2 },
  nearbyJoinBtn: { backgroundColor: Colors.primary || '#FFAA00', borderRadius: 10, paddingHorizontal: 14, paddingVertical: 8, minWidth: 56, alignItems: 'center' },
  nearbyJoinText: { color: '#000', fontSize: 13, fontWeight: '900' },

  // Radius pill selector
  radiusPillRow: { flexDirection: 'row', gap: 6, paddingHorizontal: 2, paddingBottom: 2 },
  radiusPill: { borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.15)', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 6 },
  radiusPillActive: { backgroundColor: Colors.primary || '#FFAA00', borderColor: Colors.primary || '#FFAA00' },
  radiusPillText: { color: Colors.textMuted || '#888', fontSize: 12, fontWeight: '700' },
  radiusPillTextActive: { color: '#000', fontSize: 12, fontWeight: '700' },
  controlRow: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12, marginTop: 12 },
});
