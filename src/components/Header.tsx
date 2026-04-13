import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { Colors, Layout, Spacing } from '../theme/theme';

interface HeaderProps {
  title?: string;
}

export default function Header({}: HeaderProps) {
  return (
    <View style={styles.container}>
      <Image 
        source={require('../../assets/logo.png')} 
        style={styles.logo} 
        resizeMode="contain"
        tintColor={Colors.text}
      />
      <View style={styles.underline} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Layout.padding,
    paddingTop: Spacing.xl,
    paddingBottom: Spacing.sm,
    backgroundColor: Colors.background,
    alignItems: 'center',
  },
  logo: {
    width: 120,
    height: 36,
  },
  underline: {
    height: 3,
    width: 40,
    backgroundColor: Colors.secondary,
    marginTop: Spacing.sm,
    borderRadius: 2,
    shadowColor: Colors.secondary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.8,
    shadowRadius: 5,
  }
});
