import { Spacing } from '../../theme/theme';
/**
 * MySkatesSlab.tsx — "MY SKATES" Group Cards Slab
 *
 * SLAB 3 of the disconnected dashboard view. Renders the list of skate
 * groups (via SkateGroupCard) or an empty-state prompt if none exist.
 *
 * Extracted from DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import type { UserProfile } from '../../services/ProfileService';
import type { CustomGroup } from '../../types/dashboard.types';
import { getPatternColors } from '../../styles/DashboardStyles';
import { SkateGroupCard } from './SkateGroupCard';

interface MySkatesSlabProps {
  customGroups: CustomGroup[];
  lastGroupPatterns: Record<string, any>;
  allDevices: any[];
  registeredDevices: any[];
  powerStates: Record<string, boolean>;
  userProfile: UserProfile | null;
  onGroupPress: (group: CustomGroup) => void;
  onGroupLongPress: (groupId: string) => void;
  onSetupWizard: () => void;
  Colors: any;
  styles: any;
}

const MySkatesSlab = React.memo(({
  customGroups,
  lastGroupPatterns,
  allDevices,
  registeredDevices,
  powerStates,
  userProfile,
  onGroupPress,
  onGroupLongPress,
  onSetupWizard,
  Colors,
  styles,
}: MySkatesSlabProps) => (
  <View style={styles.slabContainer}>
    <View style={styles.slabHeader}>
      <Text style={styles.slabTitle}>MY SKATES</Text>
      <MaterialCommunityIcons name="lightning-bolt" size={14} color={Colors.primary} />
    </View>

    {customGroups.length > 0 ? (
      <View style={{ gap: Spacing.md }}>
        {customGroups.map((group) => {
          const lastPattern = lastGroupPatterns[group.id];
          const cardColors = getPatternColors(lastPattern, Colors);
          return (
            <SkateGroupCard
              key={group.id}
              group={group}
              colors={cardColors}
              lastPattern={lastPattern}
              userProfile={userProfile}
              powerStates={powerStates}
              Colors={Colors}
              styles={styles}
              onPress={() => onGroupPress(group)}
              onLongPress={() => onGroupLongPress(group.id)}
            />
          );
        })}
      </View>
    ) : (
      <View style={[styles.glassSlab, { alignItems: 'center', paddingVertical: Spacing.xl }]}>
        <Text style={styles.slabEmptyText}>
          {registeredDevices.length === 0
            ? 'No skates detected. Time to link your hardware!'
            : 'Create a group to control both skates at once.'}
        </Text>
        {registeredDevices.length === 0 && (
          <TouchableOpacity
            onPress={onSetupWizard}
            style={[styles.scanButton, { marginTop: Spacing.lg, width: '70%', backgroundColor: Colors.primary }]}
          >
            <Text style={styles.scanButtonText}>SET UP YOUR SKATES</Text>
          </TouchableOpacity>
        )}
      </View>
    )}
  </View>
));

export default MySkatesSlab;
