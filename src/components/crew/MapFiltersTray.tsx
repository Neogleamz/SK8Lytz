import React from 'react';
import { TouchableOpacity, StyleSheet, View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { MapFilterMatrix } from '../../hooks/useMapFilters';

interface Props {
  filters: MapFilterMatrix;
  toggleFilter: (key: keyof MapFilterMatrix) => void;
}

export function MapFiltersTray({ filters, toggleFilter }: Props) {
  const { Colors } = useTheme();

  // Active colors precisely matched to CrewLandingMap marker rendering logic
  const FILTER_OPTS = [
    { key: 'showRinks',        label: 'Rinks', icon: 'roller-skate',         activeColor: '#3B82F6' },
    { key: 'showParks',        label: 'Parks', icon: 'flag-triangle',         activeColor: '#92400E' },
    { key: 'showShops',        label: 'Shops', icon: 'storefront-outline',    activeColor: '#8B5CF6' },
    { key: 'showCrewSessions', label: 'Crews', icon: 'account-group-outline', activeColor: '#F97316' },
  ] as const;

  return (
    <View style={styles.tray}>
      {FILTER_OPTS.map((opt) => {
        const isActive = filters[opt.key as keyof MapFilterMatrix];
        return (
          <TouchableOpacity 
            key={opt.key}
            style={[
              styles.pill,
              { 
                backgroundColor: isActive ? opt.activeColor : 'rgba(255,255,255,0.05)',
                borderColor: isActive ? opt.activeColor : 'rgba(255,255,255,0.1)'
              }
            ]}
            onPress={() => toggleFilter(opt.key as keyof MapFilterMatrix)}
          >
            <MaterialCommunityIcons 
              name={opt.icon as any} 
              size={12} 
              color={isActive ? '#FFF' : Colors.textMuted} 
            />
            <Text style={[styles.pillText, { color: isActive ? '#FFF' : Colors.textMuted }]}>
               {opt.label}
            </Text>
          </TouchableOpacity>
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  tray: {
    flex: 1,
    flexDirection: 'row',
    gap: 4,
  },
  pill: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 6,
    paddingHorizontal: 2,
    borderRadius: 8,
    borderWidth: 1,
    gap: 4
  },
  pillText: {
    fontSize: 10,
    fontWeight: '700'
  }
});
