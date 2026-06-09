import { StyleSheet } from 'react-native';
import { Spacing } from '../../theme/theme';

export const adminStyles = StyleSheet.create({
  root: { flex: 1 },
  modalHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingHorizontal: Spacing.xl, paddingBottom: Spacing.md, borderBottomWidth: 1,
  },
  title: { fontSize: 18, fontWeight: '900', letterSpacing: 1, textTransform: 'uppercase' },
  subtitle: { fontSize: 11, marginTop: Spacing.xxs },
  headerActions: { flexDirection: 'row', alignItems: 'center' },
  actionBtn: { padding: Spacing.sm, marginLeft: Spacing.xxs },
  tabs: { flexDirection: 'row', borderBottomWidth: 1 },
  tabBtn: { flex: 1, paddingVertical: Spacing.md, alignItems: 'center', borderBottomWidth: 2, borderBottomColor: 'transparent' },
  tabLabel: { fontSize: 12, fontWeight: '700', letterSpacing: 0.5, textTransform: 'uppercase' },
  tabContent: { flex: 1, padding: Spacing.lg },
  logRow: {
    flexDirection: 'row', alignItems: 'flex-start',
    paddingVertical: Spacing.md, paddingHorizontal: Spacing.lg, borderBottomWidth: StyleSheet.hairlineWidth,
  },
  logIcon: { marginTop: Spacing.xxs, marginRight: Spacing.md, width: 20 },
  logBody: { flex: 1 },
  logHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  logType: { fontSize: 13, fontWeight: '700' },
  logTime: { fontSize: 10 },
  logPayload: { fontSize: 12, marginTop: Spacing.xxs },
  colorSwatch: { width: 16, height: 8, borderRadius: 3, marginTop: Spacing.xs, borderWidth: 1, borderColor: '#000' },
  emptyText: { textAlign: 'center', fontSize: 14, marginTop: Spacing.lg },
  deviceCard: {
    flexDirection: 'row', alignItems: 'center', borderRadius: 12,
    padding: Spacing.lg, marginBottom: Spacing.md, borderWidth: 1,
  },
  deviceName: { fontSize: 15, fontWeight: '700' },
  deviceDetail: { fontSize: 12, marginTop: Spacing.xxs },
  statSection: { fontSize: 13, fontWeight: '900', marginTop: Spacing.lg, marginBottom: Spacing.sm, letterSpacing: 1, textTransform: 'uppercase' },
  statCard: { borderRadius: 12, padding: Spacing.lg, marginBottom: Spacing.xs, borderWidth: 1 },
  statRow: { flexDirection: 'row', justifyContent: 'space-between', paddingVertical: Spacing.xs },
  statLabel: { fontSize: 13 },
  statValue: { fontSize: 13, fontWeight: '700' },
});
