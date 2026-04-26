import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Animated, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { useTheme } from '../context/ThemeContext';
import type { PatternId, RGB } from '../protocols/PatternEngine';
import { getVisualizerFrame, getMusicVisualizerFrame, SK8LYTZ_TEMPLATES } from '../protocols/PatternEngine';
import { PositionalMathBuffer } from '../protocols/PositionalMathBuffer';
import { Spacing } from '../theme/theme';

interface DeviceConfig {
  id?: string;
  name?: string;
  /** Product type string. Must match a `ProductProfile.id` in the catalog. */
  type?: string;
  points?: number;
  segments?: number;
}

interface ProductVisualizerProps {
  product: string;
  color: string;
  mode: string;
  patternId: number | null;
  isPaired?: boolean;
  points?: number;
  /** Authoritative hardware settings from DashboardScreen — drives segment/ledPoints geometry */
  hwSettings?: { ledPoints?: number; segments?: number };
  devices?: DeviceConfig[];
  onLongPressDevice?: (device: any) => void;
  fixedFgColor?: string;
  fixedBgColor?: string;
  brightness?: number;
  speed?: number;
  isPoweredOn?: boolean;
  audioMagnitude?: number;
  multiColors?: string[];
  multiTransition?: number;
  isStreetBraking?: boolean;
  streetCruiseColor?: string;
  motionState?: string;
  builderNodes?: any[];
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;
  builderDirection?: number;
  fixedDirection?: number;
  streetDistribution?: [number, number, number];
}

import { VisualizerUnit } from './VisualizerUnit';

const ProductVisualizer = ({ product, color, mode, patternId, isPaired, points, hwSettings, devices, fixedFgColor, fixedBgColor, onLongPressDevice, brightness = 100, speed = 50, isPoweredOn = true, audioMagnitude = 0, multiColors, multiTransition, isStreetBraking = false, streetCruiseColor = '#FF8C00', motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT', builderTransitionType = 0x01, builderDirection = 1, fixedDirection = 1, streetDistribution = [0.3, 0.4, 0.3] }: ProductVisualizerProps) => {
  const { isDark } = useTheme();
  const animValue = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    animValue.stopAnimation();

      if (isPoweredOn && (mode === 'BUILDER' || mode === 'STREET' || mode === 'FAVORITES' || mode === 'MUSIC' || mode === 'MULTIMODE')) {
      animValue.setValue(0);
      const baseDuration =
        mode === 'MUSIC'    ? 800  :
        mode === 'BUILDER'  ? (builderTransitionType === 0x03 ? 350 : 1500) :
        mode === 'MULTIMODE'? 1500 :
        mode === 'STREET'   ? 1400 : 3000;
      const duration = baseDuration / (0.4 + (speed / 100) * 2.1);

      Animated.loop(
        Animated.timing(animValue, {
          toValue: 1,
          duration: duration,
          useNativeDriver: false,
        })
      ).start();
    } else {
      animValue.setValue(1);
    }
  }, [product, mode, color, patternId, speed, isPoweredOn, JSON.stringify(multiColors), multiTransition, builderTransitionType]);

  // Fallback if no specific devices array is provided: construct one or two devices based on isPaired flag
  // Stable array reference for devices to prevent VisualizerUnit React.memo breakage
  const renderDevices = useMemo(() => {
    return (devices && devices.length > 0) ? devices : (
      isPaired
        ? [{ id: 'mock-left', name: `${product} Left`, type: product, points }, { id: 'mock-right', name: `${product} Right`, type: product, points }]
        : [{ id: 'mock-single', name: product, type: product, points }]
    );
  }, [devices, isPaired, product, points]);

  return (
    <View style={[styles.container, { backgroundColor: '#000000', borderColor: 'rgba(255,255,255,0.12)' }]}>
      <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', flexWrap: 'wrap', paddingTop: Spacing.xl }}>
        {renderDevices.map((dev, index) => (
          <VisualizerUnit
            key={dev.id || index.toString()}
            device={dev}
            color={color}
            mode={mode}
            patternId={patternId}
            animValue={animValue}
            fallbackProduct={product}
            fallbackPoints={hwSettings?.ledPoints || points}
            hwSettings={hwSettings}
            fixedFgColor={fixedFgColor}
            fixedBgColor={fixedBgColor}
            onLongPress={onLongPressDevice}
            brightness={brightness}
            isPoweredOn={isPoweredOn}
            audioMagnitude={audioMagnitude}
            multiColors={multiColors || []}
            multiTransition={multiTransition || 0}
            isStreetBraking={isStreetBraking}
            streetCruiseColor={streetCruiseColor}
            motionState={motionState}
            builderNodes={builderNodes}
            builderFillMode={builderFillMode}
            builderTransitionType={builderTransitionType}
            builderDirection={builderDirection}
            fixedDirection={fixedDirection}
            streetDistribution={streetDistribution}
          />
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: Spacing.sm,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#000000',
    borderRadius: 20,
    marginBottom: 0,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.12)',
    minHeight: 110,
    width: '100%',
  },
  haloBase: {
    width: 60, height: 90,
  },
  soulBase: {
    width: 55, height: 115,
  },
  railBase: {
    // RAILZ: dual vertical strips — wider canvas to hold two separated rails
    width: 80, height: 120,
  },

});

export default React.memo(ProductVisualizer);
