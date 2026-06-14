import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useState, useMemo, useEffect } from 'react';
import { ActivityIndicator, Modal, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { AppLogger } from '../services/AppLogger';
import { SkateSpot, SkateSpotsService } from '../services/SkateSpotsService';
import { Spacing , ThemePalette } from '../theme/theme';
import { ErrorCard } from './ErrorCard';
import { useScreenPerformance } from '../hooks/useScreenPerformance';

interface BottomSheetProps {
  visible: boolean;
  onClose: () => void;
  spot: Partial<SkateSpot> | null;
  onSpotUpdated?: (spot: SkateSpot) => void;
}

const SurfaceChip = ({ type, label, selectedSurface, onSelect, styles }: { type: string, label: string, selectedSurface: string, onSelect: (type: string) => void, styles: ReturnType<typeof createStyles> }) => (
  <TouchableOpacity 
    style={[styles.chip, selectedSurface === type && styles.chipActive]}
    onPress={() => onSelect(type)}
  >
    <Text style={[styles.chipText, selectedSurface === type && styles.chipTextActive]}>{label}</Text>
  </TouchableOpacity>
);

export const SkateSpotBottomSheet: React.FC<BottomSheetProps> = ({ visible, onClose, spot, onSpotUpdated }) => {
  const { markFullyDrawn } = useScreenPerformance('SkateSpotBottomSheet');
  const { Colors } = useTheme();
  const styles = useMemo(() => createStyles(Colors), [Colors]);

  useEffect(() => {
    if (visible) {
      markFullyDrawn();
    }
  }, [visible, markFullyDrawn]);
  
  type StatusState = 'idle' | 'loading' | 'success' | 'error';
  const [status, setStatus] = useState<StatusState>('idle');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [selectedSurface, setSelectedSurface] = useState<string>(spot?.surface_type || 'unknown');
  const [isIndoor, setIsIndoor] = useState<boolean>(spot?.is_indoor ?? true);

  if (!spot) return null;

  const isVerifiedNative = spot.source === 'native' && spot.is_verified;

  const handleClaim = async () => {
    setStatus('loading');
    setErrorMessage(null);
    try {
      const result = await SkateSpotsService.claimAndUpdateSpot({
        ...spot,
        surface_type: selectedSurface as 'wood' | 'concrete' | 'asphalt' | 'sport_court' | 'unknown',
        is_indoor: isIndoor
      });
      
      if (result && onSpotUpdated) {
        onSpotUpdated(result);
      }
      setStatus('success');
      onClose();
    } catch (e: unknown) {
      AppLogger.warn('[SkateSpotBottomSheet] claimAndUpdateSpot failed', {
        error: e instanceof Error ? e.message : String(e),
        payload_size: 0,
        ssi: 0,
      });
      setErrorMessage('Failed to load. Tap to retry.');
      setStatus('error');
    }
  };

  return (
    <Modal visible={visible} transparent animationType="slide" onRequestClose={onClose}>
      <TouchableOpacity style={styles.overlay} activeOpacity={1} onPress={onClose}>
        <TouchableOpacity activeOpacity={1} style={styles.sheet}>
          <View style={styles.dragHandle} />
          
          <View style={styles.header}>
            <MaterialCommunityIcons 
              name={isVerifiedNative ? "check-decagram" : "help-circle-outline"} 
              size={24} 
              color={isVerifiedNative ? Colors.primary : Colors.textMuted} 
            />
            <Text style={styles.title} numberOfLines={1}>{spot.name}</Text>
          </View>
          
          <Text style={styles.subtitle}>
            {isVerifiedNative ? 'Verified Community Spot' : 'Unverified (Sourced from OSM)'}
          </Text>

          {status === 'error' && errorMessage && <View style={{ marginBottom: Spacing.xl }}><ErrorCard message={errorMessage} onRetry={handleClaim} /></View>}

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Surface Type</Text>
            <View style={styles.chipRow}>
              <SurfaceChip type="wood" label="Wood" selectedSurface={selectedSurface} onSelect={setSelectedSurface} styles={styles} />
              <SurfaceChip type="concrete" label="Concrete" selectedSurface={selectedSurface} onSelect={setSelectedSurface} styles={styles} />
              <SurfaceChip type="sport_court" label="Sport Court" selectedSurface={selectedSurface} onSelect={setSelectedSurface} styles={styles} />
              <SurfaceChip type="asphalt" label="Asphalt" selectedSurface={selectedSurface} onSelect={setSelectedSurface} styles={styles} />
            </View>
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Environment</Text>
            <View style={styles.chipRow}>
              <TouchableOpacity 
                style={[styles.chip, isIndoor && styles.chipActive]} 
                onPress={() => setIsIndoor(true)}
              >
                <Text style={[styles.chipText, isIndoor && styles.chipTextActive]}>Indoor Rink</Text>
              </TouchableOpacity>
              <TouchableOpacity 
                style={[styles.chip, !isIndoor && styles.chipActive]} 
                onPress={() => setIsIndoor(false)}
              >
                <Text style={[styles.chipText, !isIndoor && styles.chipTextActive]}>Outdoor Park</Text>
              </TouchableOpacity>
            </View>
          </View>

          <TouchableOpacity style={styles.actionButton} onPress={handleClaim} disabled={status === 'loading'}>
            {status === 'loading' ? (
              <ActivityIndicator color="#000" />
            ) : (
              <Text style={styles.actionText}>{isVerifiedNative ? 'SAVE CHANGES' : 'VERIFY & CLAIM SPOT'}</Text>
            )}
          </TouchableOpacity>
        </TouchableOpacity>
      </TouchableOpacity>
    </Modal>
  );
};

const createStyles = (Colors: ThemePalette) => StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  sheet: {
    backgroundColor: '#1C1C1E',
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    padding: Spacing.xl,
    paddingBottom: Spacing.xxxl,
    minHeight: 350,
  },
  dragHandle: {
    width: 40,
    height: 4,
    backgroundColor: '#3C3C3E',
    borderRadius: 2,
    alignSelf: 'center',
    marginBottom: Spacing.xl,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
    marginBottom: Spacing.xs,
  },
  title: {
    color: '#FFF',
    fontSize: 22,
    fontFamily: 'Righteous',
    flex: 1,
  },
  subtitle: {
    color: Colors.textMuted,
    fontSize: 13,
    marginBottom: Spacing.xl,
  },
  section: {
    marginBottom: Spacing.xl,
  },
  sectionTitle: {
    color: Colors.textMuted,
    fontSize: 12,
    fontWeight: '800',
    marginBottom: Spacing.md,
    letterSpacing: 1,
  },
  chipRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: Spacing.sm,
  },
  chip: {
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.md,
    borderRadius: 20,
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  chipActive: {
    backgroundColor: 'rgba(255,170,0,0.15)',
    borderColor: '#FFAA00',
  },
  chipText: {
    color: '#FFF',
    fontSize: 13,
    fontWeight: '600',
  },
  chipTextActive: {
    color: '#FFAA00',
  },
  actionButton: {
    backgroundColor: '#FFAA00',
    borderRadius: 12,
    height: 56,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: Spacing.md,
  },
  actionText: {
    color: '#000',
    fontSize: 15,
    fontWeight: '800',
    letterSpacing: 1,
  }
});
