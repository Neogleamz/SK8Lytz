import React from 'react';
import { TouchableOpacity, StyleSheet, View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { ThemePalette } from '../../theme/theme';
import { MapFilterMatrix } from '../../hooks/useMapFilters';

interface Props {
  filters: MapFilterMatrix;
  toggleFilter: (key: keyof MapFilterMatrix) => void;
}

// Active colors precisely matched to CrewLandingMap marker rendering logic
const FILTER_OPTS = [
  { key: 'showRinks',        label: 'Rinks', icon: 'roller-skate',         activeColor: '#3B82F6' },
  { key: 'showParks',        label: 'Parks', icon: 'flag-triangle',         activeColor: '#92400E' },
  { key: 'showShops',        label: 'Shops', icon: 'storefront-outline',    activeColor: '#8B5CF6' },
  { key: 'showCrewSessions', label: 'Crewz', icon: 'account-group-outline', activeColor: '#F97316' },
] as const;

export function MapFiltersTray({ filters, toggleFilter }: Props) {
  const { Colors } = useTheme();

  return (
    <View style={styles.tray}>
      {FILTER_OPTS.map((opt) => (
        <FilterPill 
          key={opt.key}
          opt={opt}
          isActive={filters[opt.key as keyof MapFilterMatrix]}
          onToggle={toggleFilter}
          Colors={Colors}
        />
      ))}
    </View>
  );
}

const FilterPill = React.memo(({ 
  opt, 
  isActive, 
  onToggle, 
  Colors 
}: { 
  opt: typeof FILTER_OPTS[number], 
  isActive: boolean, 
  onToggle: (k: keyof MapFilterMatrix) => void,
  Colors: ThemePalette
}) => {
  const handleToggle = React.useCallback(() => onToggle(opt.key), [onToggle, opt.key]);

  return (
    <TouchableOpacity 
      style={[
        styles.pill,
        { 
          backgroundColor: isActive ? opt.activeColor : 'rgba(255,255,255,0.05)',
          borderColor: isActive ? opt.activeColor : 'rgba(255,255,255,0.1)'
        }
      ]}
      onPress={handleToggle}
    >
      <MaterialCommunityIcons 
        name={opt.icon as keyof typeof MaterialCommunityIcons.glyphMap} 
        size={12} 
        color={isActive ? '#FFF' : Colors.textMuted} 
      />
      <Text style={[styles.pillText, { color: isActive ? '#FFF' : Colors.textMuted }]}>
         {opt.label}
      </Text>
    </TouchableOpacity>
  );
});

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
