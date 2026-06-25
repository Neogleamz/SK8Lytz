import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { LOCAL_PRODUCT_CATALOG } from '../../constants/ProductCatalog';
import type { DockedControllerStyles } from '../controller/DockedController.styles';
import type { ThemePalette } from '../../theme/theme';

interface DockedHeaderProps {
  lockedProduct?: string;
  activeProduct: string;
  setActiveProduct: (id: string) => void;
  Colors: Pick<ThemePalette, 'primary' | 'accent'>;
  styles: DockedControllerStyles;
}

export default function DockedHeader({ lockedProduct, activeProduct, setActiveProduct, Colors, styles }: DockedHeaderProps) {
  if (lockedProduct) return null;

  return (
    <View style={styles.tabContainer}>
      {LOCAL_PRODUCT_CATALOG.filter(p => (p as { isActive?: boolean }).isActive !== false).map((profile) => (
        <TouchableOpacity
          key={profile.id}
          style={[styles.tab, activeProduct === profile.id && styles.activeTab]}
          onPress={() => setActiveProduct(profile.id)}
        >
          {activeProduct === profile.id && (
            <LinearGradient 
              colors={[profile.vizThemeColor || Colors.primary, Colors.accent]} 
              start={{ x: 0, y: 0 }} 
              end={{ x: 1, y: 1 }} 
              style={StyleSheet.absoluteFill} 
            />
          )}
          <Text style={[styles.tabText, activeProduct === profile.id && styles.activeTabText]}>
            {profile.displayName.replace('™', '')}
          </Text>
        </TouchableOpacity>
      ))}
    </View>
  );
}
