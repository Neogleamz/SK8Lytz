// S4 Acknowledgement: This file is close to 30KB. Only specific plan line items are modified surgically.
import React, { useEffect, useMemo, useRef, useState } from 'react';
/* global requestAnimationFrame, cancelAnimationFrame */
import { Animated, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { useTheme } from '../context/ThemeContext';
import type { PatternId, RGB } from '../protocols/PatternEngine';
import { getVisualizerFrame, getMusicVisualizerFrame, SK8LYTZ_TEMPLATES } from '../protocols/PatternEngine';
import { PositionalMathBuffer, BuilderNode } from '../protocols/PositionalMathBuffer';
import { Spacing } from '../theme/theme';
import { DisplayDevice, IDeviceState } from '../types/dashboard.types';

import { useVisualizerPath, useVisualizerLeds } from './visualizer/VisualizerHooks';

export interface VisualizerUnitProps {
  device?: { type?: string; name?: string; points?: number; segments?: number };
  color: string;
  mode: string;
  patternId?: number | null;
  animValue: Animated.Value;
  fallbackProduct?: string;
  fallbackPoints?: number;
  hwSettings?: { ledPoints?: number; segments?: number };
  onLongPress?: (device: DisplayDevice & IDeviceState) => void;
  fixedFgColor?: string;
  fixedBgColor?: string;
  brightness?: number;
  speed?: number;
  isPoweredOn?: boolean;
  audioMagnitude?: number;
  multiColors?: string[];
  multiTransition?: number;
  streetBrakeState?: 'BRAKING' | 'CRUISING';
  streetCruiseColor?: string;
  motionState?: string;
  builderNodes?: BuilderNode[];
  builderFillMode?: string;
  builderTransitionType?: number;
  builderDirection?: number;
  fixedDirection?: number;
  streetDistribution?: [number, number, number];
}

export const VisualizerUnit = React.memo(({ device, color, mode, patternId, animValue, fallbackProduct, fallbackPoints, hwSettings, onLongPress, fixedFgColor, fixedBgColor, brightness = 100, speed = 50, isPoweredOn = true, audioMagnitude = 0, multiColors = [], multiTransition = 0, streetBrakeState = 'CRUISING', streetCruiseColor = '#FF8C00', motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT', builderTransitionType = 1, builderDirection = 1, fixedDirection = 1, streetDistribution = [0.3, 0.4, 0.3] }: VisualizerUnitProps) => {
  const { isDark: _isDark } = useTheme();
  // Guard against String(undefined)='undefined' which causes silent SOULZ fallback for HALOZ devices
  const product = (device?.type && device.type !== 'undefined') ? String(device.type) : String(fallbackProduct || 'SOULZ');

  // Resolve product profile from catalog — drives all geometry decisions.
  // Falls back to LOCAL_PRODUCT_CATALOG so this works fully offline.
  const productProfile = useMemo(() => {
    const byId = LOCAL_PRODUCT_CATALOG.find(p => p.id.toUpperCase() === product.toUpperCase());
    return byId ?? LOCAL_PRODUCT_CATALOG.find(p => p.id === 'SOULZ')!;
  }, [product]);
  const vizShape = productProfile.vizShape;
  const _isMirrored = productProfile.vizIsMirrored;

  // ── Animation tick — stored in a ref to avoid driving React re-renders ──
  // We still need a state value to trigger re-renders for pattern changes, but the
  // tick value itself is kept in a ref. requestAnimationFrame batches the updates.
  const [animTick, setAnimTick] = useState(0);
  const tickRef = useRef(0);
  const rafPendingRef = useRef<number | null>(null);
  const lastRenderTimeRef = useRef(0);

  useEffect(() => {
    const id = animValue.addListener(({ value }: { value: number }) => {
      tickRef.current = value;
      // Batch tick updates through RAF — prevents flooding the React reconciler
      if (!rafPendingRef.current) {
        rafPendingRef.current = requestAnimationFrame(() => {
          const now = Date.now();
          // Throttle to 30 FPS on Web to prevent MessageQueue flooding. 
          // Native runs at 60 FPS (or uncapped).
          const targetFps = Platform.OS === 'web' ? 30 : 60;
          const frameMs = 1000 / targetFps;

          if (now - lastRenderTimeRef.current >= frameMs) {
            setAnimTick(tickRef.current);
            lastRenderTimeRef.current = now;
          }

          rafPendingRef.current = null;
        });
      }
    });
    return () => {
      animValue.removeListener(id);
      if (rafPendingRef.current) cancelAnimationFrame(rafPendingRef.current);
    };
  }, [animValue]);

  // ── hwSettings-first LED geometry ────────────────────────────────────────
  // Priority order: authoritative probed hwSettings > device object fields > catalog fallback.
  // devicePoints = LEDs PER SEGMENT (the protocol canvas). HALOZ=8, SOULZ=43.
  // NEVER divide by segments here — ledPoints already represents the per-segment canvas.
  const devicePoints   = hwSettings?.ledPoints || device?.points || fallbackPoints || productProfile.defaultLedPoints;
  const deviceSegments = hwSettings?.segments  || device?.segments || productProfile.defaultSegments;
  const numLeds = Math.floor(devicePoints); // Protocol canvas: HALOZ=8 (hardware mirrors to seg2), SOULZ=43

  // ── PATH GEOMETRY (expensive) — only recomputes on shape/product change, NEVER on animTick ──
  const pathGeometry = useVisualizerPath(productProfile, numLeds, deviceSegments, product);

  const leds = useVisualizerLeds({
    pathGeometry, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude,
    fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, speed,
    animTick, streetBrakeState, motionState, streetCruiseColor, builderNodes,
    builderFillMode, builderTransitionType, builderDirection, vizShape,
    deviceSegments, productProfile, animValue, fixedDirection, streetDistribution
  });

  return (
    <TouchableOpacity
      activeOpacity={onLongPress ? 0.8 : 1}
      onLongPress={onLongPress ? () => onLongPress(device as Partial<DisplayDevice & IDeviceState> as DisplayDevice & IDeviceState) : undefined}
      style={{ alignItems: 'center', marginHorizontal: Spacing.md, paddingVertical: Spacing.xs }}
    >
      <View style={[
        vizShape === 'RING' ? styles.haloBase : vizShape === 'DUAL_STRIP' ? styles.railBase : styles.soulBase,
        { alignSelf: 'center', opacity: isPoweredOn ? (brightness === 0 ? 0 : Math.max(0.06, Math.pow(brightness / 100, 1.8))) : 0.15 }
      ]}>
        {/* Physical silicone track background — shown only on RING (HALOZ) */}
        {vizShape === 'RING' && (
          <View style={{
            position: 'absolute',
            width: 50 + 12,
            height: 75 + 12,
            borderWidth: 8,
            borderColor: 'rgba(255,255,255,0.06)',
            borderRadius: 20,
            alignSelf: 'center',
            top: '50%',
            marginTop: -(75 + 12) / 2,
            left: '50%',
            marginLeft: -(50 + 12) / 2
          }} />
        )}
        {/* DUAL_STRIP: subtle channel track behind each rail */}
        {vizShape === 'DUAL_STRIP' && (
          <>
            <View style={{ position: 'absolute', left: '22%', top: 0, width: 6, height: '100%', borderRadius: 3, backgroundColor: 'rgba(255,255,255,0.04)' }} />
            <View style={{ position: 'absolute', right: '22%', top: 0, width: 6, height: '100%', borderRadius: 3, backgroundColor: 'rgba(255,255,255,0.04)' }} />
          </>
        )}

        {leds.map(led => {
          const diam = productProfile.vizBlobDiameterMm;

          return (
            // Outer wrapper applies chipSoften (plain number) — compatible with Animated inner nodes.
            // Two-layer opacity in RN multiplies: chipSoften × activeOpacity per LED.
            <View key={led.key} style={[led.position, { width: diam, height: diam, alignItems: 'center', justifyContent: 'center', overflow: 'visible', zIndex: 10, opacity: led.chipSoften }]}>
              <Animated.View style={[{ position: 'absolute', width: '100%', height: '100%', alignItems: 'center', justifyContent: 'center', opacity: led.activeOpacity }]}>

                {/* ── Layer 3: Outer atmospheric scatter ─────────────────────────────── */}
                {/* Simulates light diffusing into surrounding air/surface (~5mm radius) */}
                <Animated.View style={{
                  position: 'absolute',
                  width: diam * 5.5, height: diam * 5.5,
                  borderRadius: (diam * 5.5) / 2,
                  backgroundColor: led.activeColor,
                  opacity: 0.03,
                }} />

                {/* ── Layer 2: Mid silicone bloom ─────────────────────────────────── */}
                {/* The silicone tube edge scatters light laterally — soft wide glow */}
                <Animated.View style={{
                  position: 'absolute',
                  width: diam * 3.2, height: diam * 3.2,
                  borderRadius: (diam * 3.2) / 2,
                  backgroundColor: led.activeColor,
                  opacity: 0.10,
                }} />

                {/* ── Layer 1: Inner diffuse scatter ──────────────────────────────── */}
                {/* Closest to the chip: concentrated inner halo through the silicone walls */}
                <Animated.View style={{
                  position: 'absolute',
                  width: diam * 1.7, height: diam * 1.7,
                  borderRadius: (diam * 1.7) / 2,
                  backgroundColor: led.activeColor,
                  opacity: 0.38,
                }} />

                {/* ── Main LED chip body ──────────────────────────────────────────── */}
                <Animated.View style={{
                  position: 'absolute', width: '100%', height: '100%', borderRadius: diam / 2,
                  backgroundColor: led.activeColor,
                }} />

                {/* ── Hot-spot chip centre ─────────────────────────────────────────── */}
                {/* Real LED phosphor package has a bright emitter core visible through silicone */}
                <View style={{
                  position: 'absolute',
                  width: diam * 0.32, height: diam * 0.32,
                  borderRadius: (diam * 0.32) / 2,
                  backgroundColor: 'rgba(255,255,255,0.55)',
                }} />

                {/* ── Frosty silicone exterior glaze ──────────────────────────────── */}
                <View style={{
                  position: 'absolute', width: '100%', height: '100%', borderRadius: diam / 2,
                  backgroundColor: 'rgba(255,255,255,0.09)',
                  borderWidth: 0.5, borderColor: 'rgba(255,255,255,0.05)',
                }} />

              </Animated.View>
            </View>
          );
        })}
      </View>
      <View style={{ marginTop: Spacing.lg, alignItems: 'center', zIndex: 10, width: 100 }}>
        <Text
          style={{ color: isPoweredOn ? 'white' : '#888', fontWeight: 'bold', fontSize: 11, textAlign: 'center', opacity: isPoweredOn ? 1.0 : 0.4 }}
          numberOfLines={2}
        >
          {device?.name || product}
        </Text>
      </View>
    </TouchableOpacity>
  );
});

const styles = StyleSheet.create({
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
