import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';

interface HeaderProps {
  title?: string;
}

export default function Header({ title = 'NEOGLEAMZ' }: HeaderProps) {
  return (
    <View style={styles.container}>
      <Text style={[Typography.header, styles.brand]}>{title}</Text>
      <View style={styles.underline} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Layout.padding,
    paddingTop: 60,
    paddingBottom: 20,
    backgroundColor: Colors.background,
    alignItems: 'center',
  },
  brand: {
    color: Colors.primary,
    textShadowColor: 'rgba(255, 0, 127, 0.5)',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 10,
  },
  underline: {
    height: 3,
    width: 40,
    backgroundColor: Colors.secondary,
    marginTop: 8,
    borderRadius: 2,
    shadowColor: Colors.secondary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.8,
    shadowRadius: 5,
  }
});
