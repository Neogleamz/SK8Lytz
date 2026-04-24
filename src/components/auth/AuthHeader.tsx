import React from 'react';
import { Image, Linking, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';

export function AuthHeader() {
  const { Colors, isDark } = useTheme();

  return (
    <View style={styles.headerContainer}>
      <Image 
        source={require('../../../assets/logo.png')} 
        style={{ width: '80%', maxWidth: 300, height: 80 }}
        tintColor={isDark ? '#FFFFFF' : undefined} 
        resizeMode="contain" 
      />
      <TouchableOpacity 
        onPress={() => Linking.openURL('https://neogleamz.com')}
        style={{ alignSelf: 'flex-end', marginTop: -15, marginRight: '16%', padding: Spacing.xs }}
      >
        <Text style={{ fontSize: 9, fontWeight: '800', color: Colors.textMuted, letterSpacing: 1 }}>
          by neogleamz.com
        </Text>
      </TouchableOpacity>
      <Text style={[styles.subtitle, { marginTop: Spacing.md, letterSpacing: 1.5, fontSize: 13, color: Colors.textMuted }]}>
        Glow your way.
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  headerContainer: { alignItems: 'center', marginBottom: Spacing.xxxl },
  subtitle: { textAlign: 'center' },
});
