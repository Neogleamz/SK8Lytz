import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useState } from 'react';
import { ActivityIndicator, Modal, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { SkateSpot, SkateSpotsService } from '../services/SkateSpotsService';
import { Spacing } from '../theme/theme';

interface BottomSheetProps {
  visible: boolean;
  onClose: () => void;
  spot: Partial<SkateSpot> | null;
  onSpotUpdated?: (spot: SkateSpot) => void;
}

export const SkateSpotBottomSheet: React.FC<BottomSheetProps> = ({ visible, onClose, spot, onSpotUpdated }) => {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  
  const [isUpdating, setIsUpdating] = useState(false);
  const [selectedSurface, setSelectedSurface] = useState<string>(spot?.surface_type || 'unknown');
  const [isIndoor, setIsIndoor] = useState<boolean>(spot?.is_indoor ?? true);

  if (!spot) return null;

  const isVerifiedNative = spot.source === 'native' && spot.is_verified;

  const handleClaim = async () => {
    setIsUpdating(true);
    const result = await SkateSpotsService.claimAndUpdateSpot({
      ...spot,
      surface_type: selectedSurface as any,
      is_indoor: isIndoor
    });
    setIsUpdating(false);
    
    if (result && onSpotUpdated) {
      onSpotUpdated(result);
    }
    onClose();
  };

  const SurfaceChip = ({ type, label }: { type: string, label: string }) => (
    <TouchableOpacity 
      style={[styles.chip, selectedSurface === type && styles.chipActive]}
      onPress={() => setSelectedSurface(type)}
    >
      <Text style={[styles.chipText, selectedSurface === type && styles.chipTextActive]}>{label}</Text>
    </TouchableOpacity>
  );

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

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Surface Type</Text>
            <View style={styles.chipRow}>
              <SurfaceChip type="wood" label="Wood" />
              <SurfaceChip type="concrete" label="Concrete" />
              <SurfaceChip type="sport_court" label="Sport Court" />
              <SurfaceChip type="asphalt" label="Asphalt" />
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

          <TouchableOpacity style={styles.actionButton} onPress={handleClaim} disabled={isUpdating}>
            {isUpdating ? (
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

const createStyles = (Colors: any) => StyleSheet.create({
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
