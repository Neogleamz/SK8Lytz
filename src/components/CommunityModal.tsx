import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import {
    ActivityIndicator, Alert,
    Animated, Easing,
    FlatList,
    Modal,
    StyleSheet,
    Text,
    TouchableOpacity,
    View
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';
import { ICloudScene, Scene, ScenesService } from '../services/ScenesService';
import { AppLogger } from '../services/AppLogger';
import { Layout, Spacing, Typography , ThemePalette } from '../theme/theme';

interface ScenePayload extends Partial<Scene> {
  activeMode?: string;
  fixedSubMode?: string;
  multiColors?: string[];
  selectedColor?: string;
  musicPrimaryColor?: string;
  musicSecondaryColor?: string;
}
import { ErrorCard } from './ErrorCard';
import { EmptyState } from './EmptyState';

interface Props {
  isOfflineMode?: boolean;
  isVisible: boolean;
  onClose: () => void;
  onApplyScene: (payload: Scene | Record<string, unknown>) => void;
}

// --- Animated LED Strip Preview ---
function LedStripPreview({ colors, mode }: { colors: string[], mode: string }) {
  const anim = useRef(new Animated.Value(0)).current;
  const isAnimated = ['MULTI', 'MUSIC', 'MULTIMODE'].includes(mode);

  useEffect(() => {
    if (!isAnimated || colors.length === 0) return;
    const loop = Animated.loop(
      Animated.timing(anim, {
        toValue: 1,
        duration: 2000,
        easing: Easing.linear,
        useNativeDriver: false,
      })
    );
    loop.start();
    return () => loop.stop();
  }, [colors, isAnimated]);

  const displayColors = colors.length > 0 ? colors : ['#1a1a1a', '#2a2a2a', '#1a1a1a'];
  const segments = Math.max(displayColors.length, 8);
  const repeated: string[] = [];
  while (repeated.length < segments * 2) {
    repeated.push(...displayColors);
  }

  const translateX = anim.interpolate({
    inputRange: [0, 1],
    outputRange: [0, -(100 / segments) * displayColors.length],
  });

  return (
    <View style={{ height: 18, borderRadius: 9, overflow: 'hidden', flexDirection: 'row' }}>
      <Animated.View
        style={{
          flexDirection: 'row',
          width: '200%',
          height: '100%',
          transform: isAnimated ? [{ translateX: translateX as Animated.AnimatedInterpolation<number> }] : [],
        }}
      >
        {repeated.map((color, idx) => (
          <View key={idx} style={{ flex: 1, backgroundColor: color, height: '100%' }} />
        ))}
      </Animated.View>
    </View>
  );
}

const SceneCard = React.memo(({
  item,
  activeTab,
  processingId,
  Colors,
  styles,
  onUpvote,
  onDelete,
  onApply,
}: {
  item: ICloudScene;
  activeTab: 'COMMUNITY' | 'PERSONAL';
  processingId: string | null;
  Colors: ThemePalette;
  styles: ReturnType<typeof createStyles>;
  onUpvote: (id: string) => void;
  onDelete: (id: string) => void;
  onApply: (scene: ICloudScene) => void;
}) => {
  const handleUpvote = useCallback(() => {
    onUpvote(item.id);
  }, [onUpvote, item.id]);

  const handleDelete = useCallback(() => {
    onDelete(item.id);
  }, [onDelete, item.id]);

  const handleApply = useCallback(() => {
    onApply(item);
  }, [onApply, item]);

  const p = (item.scene_payload as ScenePayload) || {};
  const mode: string = p.activeMode || p.fixedSubMode || 'UNKNOWN';
  const isOwner = activeTab === 'PERSONAL';

  // Build color array for preview
  let previewColors: string[] = [];
  if (p.multiColors && p.multiColors.length > 0) previewColors = p.multiColors;
  else if (p.selectedColor) previewColors = [p.selectedColor, p.selectedColor];
  else if (p.musicPrimaryColor) previewColors = [p.musicPrimaryColor, p.musicSecondaryColor || '#000'];

  const modeLabel = mode.toUpperCase();
  const paramCount = Object.keys(p).length;

  return (
    <View style={styles.card}>
      {/* Header row */}
      <View style={styles.cardHeader}>
        <Text style={styles.sceneName} numberOfLines={1}>{item.name}</Text>
        <View style={[styles.badge, { backgroundColor: item.is_public ? 'rgba(0,200,80,0.15)' : 'rgba(255,255,255,0.08)' }]}>
          <MaterialCommunityIcons
            name={item.is_public ? 'earth' : 'lock-outline'}
            size={10}
            color={item.is_public ? '#00C853' : '#888'}
            style={{ marginRight: Spacing.xs }}
          />
          <Text style={[styles.badgeText, { color: item.is_public ? '#00C853' : '#888' }]}>{modeLabel}</Text>
        </View>
      </View>

      <Text style={styles.authorText}>By {item.author_username}  ·  {paramCount} params</Text>

      {/* LED Strip Preview */}
      <View style={styles.stripContainer}>
        <LedStripPreview colors={previewColors} mode={mode} />
      </View>

      {/* Actions */}
      <View style={styles.cardActions}>
        <View style={styles.statsRow}>
          <TouchableOpacity
            style={styles.statItem}
            onPress={handleUpvote}
            disabled={processingId === item.id || activeTab === 'PERSONAL'}
          >
            <MaterialCommunityIcons name="heart" size={15} color={activeTab === 'COMMUNITY' ? Colors.primary : '#555'} />
            <Text style={styles.statText}>{item.upvotes}</Text>
          </TouchableOpacity>
          <View style={styles.statItem}>
            <MaterialCommunityIcons name="download-outline" size={15} color="#555" />
            <Text style={styles.statText}>{item.downloads}</Text>
          </View>
        </View>

        <View style={styles.actionButtons}>
          {isOwner && (
            <TouchableOpacity
              style={[styles.iconButton, { borderColor: 'rgba(255,60,60,0.4)' }]}
              onPress={handleDelete}
              disabled={processingId === item.id}
            >
              <MaterialCommunityIcons name="trash-can-outline" size={17} color="#FF4444" />
            </TouchableOpacity>
          )}
          <TouchableOpacity style={styles.applyButton} onPress={handleApply}>
            <Text style={styles.applyButtonText}>Apply</Text>
            <MaterialCommunityIcons name="lightning-bolt" size={16} color="#000" />
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
});

// --- Main Component ---
export default function CommunityModal({ isOfflineMode = false, isVisible, onClose, onApplyScene }: Props) {
  const { Colors } = useTheme();
  const { user } = useAuth();
  const styles = React.useMemo(() => createStyles(Colors), [Colors]);

  const [activeTab, setActiveTab] = useState<'COMMUNITY' | 'PERSONAL'>(isOfflineMode ? 'PERSONAL' : 'COMMUNITY');
  const [scenes, setScenes] = useState<ICloudScene[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [processingId, setProcessingId] = useState<string | null>(null);
  const isFetchingRef = useRef(false);

  useEffect(() => {
    if (isVisible) {
      if (isOfflineMode && activeTab === 'COMMUNITY') setActiveTab('PERSONAL');
      fetchScenes();
    }
  }, [isVisible, activeTab, isOfflineMode]);

  const fetchScenes = async () => {
    if (isFetchingRef.current) return;
    isFetchingRef.current = true;
    setLoading(true);
    setError(null);
    if (activeTab === 'COMMUNITY' && isOfflineMode) {
      setScenes([]);
      setLoading(false);
      isFetchingRef.current = false;
      return;
    }
    try {
      const data = activeTab === 'COMMUNITY'
        ? await ScenesService.getPublicScenes()
        : await ScenesService.getMyScenes(user?.id ?? '');
      setScenes(data);
    } catch (e: unknown) {
      AppLogger.warn('[CommunityModal] failed to fetch scenes', { error: e instanceof Error ? e.message : String(e) });
      setError('Failed to load. Tap to retry.');
    } finally {
      setLoading(false);
      isFetchingRef.current = false;
    }
  };

  const handleApply = async (scene: ICloudScene) => {
    onApplyScene(scene.scene_payload);
    Alert.alert('✅ Applied!', `"${scene.name}" has been loaded onto your hardware.`);
    if (activeTab === 'COMMUNITY') ScenesService.downloadScene(scene.id);
    onClose();
  };

  const handleUpvote = async (sceneId: string) => {
    setProcessingId(sceneId);
    const success = await ScenesService.upvoteScene(sceneId);
    if (success) setScenes(prev => prev.map(s => s.id === sceneId ? { ...s, upvotes: s.upvotes + 1 } : s));
    setProcessingId(null);
  };

  const handleDelete = (sceneId: string) => {
    Alert.alert('Delete Scene', 'This will permanently remove this scene from your cloud saves.', [
      { text: 'Cancel', style: 'cancel' },
      { text: 'Delete', style: 'destructive', onPress: async () => {
        setProcessingId(sceneId);
        const success = await ScenesService.deleteScene(sceneId);
        if (success) {
          setScenes(prev => prev.filter(s => s.id !== sceneId));
        } else {
          Alert.alert('Error', 'Failed to delete scene.');
        }
        setProcessingId(null);
      }}
    ]);
  };

  const renderItem = useCallback(({ item }: { item: ICloudScene }) => {
    return (
      <SceneCard
        item={item}
        activeTab={activeTab}
        processingId={processingId}
        Colors={Colors}
        styles={styles}
        onUpvote={handleUpvote}
        onDelete={handleDelete}
        onApply={handleApply}
      />
    );
  }, [activeTab, processingId, Colors, styles, handleUpvote, handleDelete, handleApply]);

  const keyExtractorScene = useCallback((item: ICloudScene) => item.id, []);

  return (
    <Modal visible={isVisible} animationType="slide" presentationStyle="pageSheet" onRequestClose={onClose}>
      <SafeAreaView style={styles.container}>
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="close" size={26} color={Colors.text} />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={styles.headerTitle}>Cloud Scenes</Text>
            <Text style={styles.headerSub}>Browse and share LED lighting setups</Text>
          </View>
          <TouchableOpacity onPress={fetchScenes} style={styles.refreshBtn}>
            <MaterialCommunityIcons name="refresh" size={22} color={Colors.primary} />
          </TouchableOpacity>
        </View>

        {/* Tabs */}
        <View style={styles.tabsContainer}>
          {(['COMMUNITY', 'PERSONAL'] as const)
            .filter(tab => !(isOfflineMode && tab === 'COMMUNITY'))
            .map(tab => (
            <TouchableOpacity
              key={tab}
              style={[styles.tab, activeTab === tab && styles.activeTab]}
              onPress={() => setActiveTab(tab)}
            >
              <MaterialCommunityIcons
                name={tab === 'COMMUNITY' ? 'earth' : 'account-circle'}
                size={16}
                color={activeTab === tab ? Colors.primary : '#666'}
                style={{ marginRight: Spacing.sm }}
              />
              <Text style={[styles.tabText, activeTab === tab && styles.activeTabText]}>
                {tab === 'COMMUNITY' ? 'Community' : 'My Saves'}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        {loading ? (
          <View style={styles.centerLoading}>
            <ActivityIndicator size="large" color={Colors.primary} />
            <Text style={{ color: Colors.textMuted, marginTop: Spacing.md, fontSize: 13 }}>Loading scenes...</Text>
          </View>
        ) : error ? (
          <View style={{ padding: Spacing.xl }}>
            <ErrorCard message={error} onRetry={fetchScenes} />
          </View>
        ) : (
          <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
            data={scenes}
            keyExtractor={keyExtractorScene}
            renderItem={renderItem}
            contentContainerStyle={styles.listContainer}
            showsVerticalScrollIndicator={false}
            ListEmptyComponent={() => (
              <EmptyState message={
                activeTab === 'COMMUNITY'
                  ? 'Be the first to publish a scene to the community!'
                  : 'Save a preset to the cloud from the controller.'
              } />
            )}
          />
        )}
      </SafeAreaView>
    </Modal>
  );
}

const createStyles = (Colors: ThemePalette) => StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.xl,
    paddingVertical: Spacing.lg,
    borderBottomWidth: 1,
    borderBottomColor: Colors.surfaceHighlight,
  },
  closeBtn: { marginRight: Spacing.lg },
  refreshBtn: { padding: Spacing.xs },
  headerTitle: { ...Typography.header, fontSize: 22, color: Colors.text },
  headerSub: { fontSize: 11, color: Colors.textMuted, marginTop: 1 },
  tabsContainer: {
    flexDirection: 'row',
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.md,
    gap: Spacing.md,
  },
  tab: {
    flex: 1,
    flexDirection: 'row',
    paddingVertical: Spacing.md,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: Layout.borderRadius,
    backgroundColor: Colors.surface,
    borderWidth: 1,
    borderColor: 'transparent',
  },
  activeTab: {
    borderColor: Colors.primary,
    backgroundColor: Colors.surfaceHighlight,
  },
  tabText: { fontWeight: 'bold', color: '#666', fontSize: 13 },
  activeTabText: { color: Colors.primary },
  listContainer: { padding: Spacing.lg, paddingBottom: Spacing.huge, gap: Spacing.lg },
  card: {
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    padding: Spacing.lg,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.xxs,
  },
  sceneName: {
    ...Typography.title,
    color: Colors.text,
    fontSize: 16,
    flex: 1,
    marginRight: Spacing.sm,
  },
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.sm,
    paddingVertical: Spacing.xxs,
    borderRadius: 10,
  },
  badgeText: { fontSize: 10, fontWeight: 'bold' },
  authorText: { fontSize: 11, color: Colors.textMuted, marginBottom: Spacing.md },
  stripContainer: {
    marginBottom: Spacing.md,
    borderRadius: 9,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOpacity: 0.3,
    shadowRadius: 4,
    elevation: 3,
  },
  cardActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  statsRow: { flexDirection: 'row', gap: Spacing.lg },
  statItem: { flexDirection: 'row', alignItems: 'center', gap: Spacing.xs },
  statText: { color: '#666', fontSize: 13, fontWeight: 'bold' },
  actionButtons: { flexDirection: 'row', gap: Spacing.sm, alignItems: 'center' },
  iconButton: {
    width: 34,
    height: 34,
    borderRadius: 17,
    backgroundColor: 'rgba(255,255,255,0.06)',
    borderWidth: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  applyButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.primary,
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.sm,
    borderRadius: 18,
    gap: Spacing.xs,
  },
  applyButtonText: { color: '#000', fontWeight: 'bold', fontSize: 13 },
  centerLoading: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  emptyContainer: { marginTop: Spacing.giant, alignItems: 'center', paddingHorizontal: Spacing.xxl },
  emptyTitle: { ...Typography.title, color: Colors.text, marginTop: Spacing.lg, fontSize: 18 },
  emptyText: { color: Colors.textMuted, textAlign: 'center', marginTop: Spacing.sm, fontSize: 13, lineHeight: 20 },
});
