import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Animated, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { useTheme } from '../context/ThemeContext';
import type { PatternId, RGB } from '../protocols/PatternEngine';
import { getVisualizerFrame, getMusicVisualizerFrame } from '../protocols/PatternEngine';
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
}

// Convert HSL to Hex manually as React Native Interpolate handles strict string maps better
function HSLToHex(h: number, s: number, l: number) {
  h = (h % 360 + 360) % 360; // Normalize [0-360]
  s /= 100; l /= 100;
  let c = (1 - Math.abs(2 * l - 1)) * s,
    x = c * (1 - Math.abs((h / 60) % 2 - 1)),
    m = l - c / 2,
    r = 0, g = 0, b = 0;

  if (0 <= h && h < 60) { r = c; g = x; b = 0; }
  else if (60 <= h && h < 120) { r = x; g = c; b = 0; }
  else if (120 <= h && h < 180) { r = 0; g = c; b = x; }
  else if (180 <= h && h < 240) { r = 0; g = x; b = c; }
  else if (240 <= h && h < 300) { r = x; g = 0; b = c; }
  else if (300 <= h && h <= 360) { r = c; g = 0; b = x; }

  r = Math.round((r + m) * 255);
  g = Math.round((g + m) * 255);
  b = Math.round((b + m) * 255);

  const toHex = (n: number) => { const hex = n.toString(16); return hex.length === 1 ? '0' + hex : hex; };
  return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}

const VisualizerUnit = React.memo(({ device, color, mode, patternId, animValue, fallbackProduct, fallbackPoints, onLongPress, fixedFgColor, fixedBgColor, brightness = 100, speed = 50, isPoweredOn = true, audioMagnitude = 0, multiColors = [], multiTransition = 0, isStreetBraking = false, streetCruiseColor = '#FF8C00', motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT', builderTransitionType = 1, builderDirection = 1, fixedDirection = 1 }: any) => {
  const { isDark } = useTheme();
  const product = String(device.type || fallbackProduct);

  // Resolve product profile from catalog — drives all geometry decisions.
  // Falls back to LOCAL_PRODUCT_CATALOG so this works fully offline.
  const productProfile = useMemo(() => {
    const byId = LOCAL_PRODUCT_CATALOG.find(p => p.id.toUpperCase() === product.toUpperCase());
    return byId ?? LOCAL_PRODUCT_CATALOG.find(p => p.id === 'SOULZ')!;
  }, [product]);
  const vizShape = productProfile.vizShape;
  const isMirrored = productProfile.vizIsMirrored;

  // Track animValue tick (0.0–1.0) for PatternEngine frame generation
  const [animTick, setAnimTick] = useState(0);
  useEffect(() => {
    const id = animValue.addListener(({ value }: { value: number }) => setAnimTick(value));
    return () => animValue.removeListener(id);
  }, [animValue]);

  // Resolve default LED count from catalog profile.
  // Legacy path: if still binary, catalog provides the same numbers (16 / 43).
  const devicePoints = device?.points || fallbackPoints || productProfile.vizDefaultPoints;
  const deviceSegments = device?.segments || 1;
  const numLeds = Math.floor(devicePoints / deviceSegments); // 16 or 43

  // ── PATH GEOMETRY (expensive) — only recomputes on shape/product change, NEVER on animTick ──
  const pathGeometry = useMemo(() => {
    const numSamples = 5000;
    const pathSamples: any[] = [];
    let totalLength = 0;

    // SCALE FACTOR (SHRUNK)
    const S = 0.38;

    for (let i = 0; i <= numSamples; i++) {
      let left = 0;
      let top = 0;

      if (vizShape === 'RING') {
        // ── HALOZ: Superellipse rounded rectangle ring ─────────────────────
        // Two segment path: Seg1 = right arc (bottom→top), Seg2 = left arc (top→bottom).
        let angle = 0;
        const half = Math.floor(numSamples / 2);
        if (i < half) {
          const fraction = i / Math.max(1, half - 1);
          angle = (Math.PI / 2) - fraction * Math.PI;
        } else {
          const fraction = (i - half) / Math.max(1, (numSamples - half) - 1);
          angle = -(Math.PI / 2) - fraction * Math.PI;
        }
        const n = 4;
        const power = 2 / n;
        const cosT = Math.cos(angle);
        const sinT = Math.sin(angle);
        const x = 70 * Math.sign(cosT) * Math.pow(Math.abs(cosT), power);
        const y = 110 * Math.sign(sinT) * Math.pow(Math.abs(sinT), power);
        left = (80 + x) * S;
        top = (120 + y) * S;

      } else if (vizShape === 'DUAL_STRIP') {
        // ── RAILZ: Two vertical parallel straight strips ───────────────────
        // Left strip: indices 0 to numSamples/2 — traces top→bottom on left rail.
        // Right strip: indices numSamples/2 to numSamples — traces top→bottom on right rail.
        const sep = productProfile.vizStripSeparation ?? 32;
        const half = Math.floor(numSamples / 2);
        const stripHeight = productProfile.vizBaseHeight * S;
        const centreX = (productProfile.vizBaseWidth / 2) * S;
        if (i < half) {
          // Left strip: traces top to bottom
          const fract = i / Math.max(1, half - 1);
          left = centreX - (sep / 2) * S;
          top = fract * stripHeight;
        } else {
          // Right strip: traces top to bottom (same direction, mirrors physical LED layout)
          const fract = (i - half) / Math.max(1, (numSamples - half) - 1);
          left = centreX + (sep / 2) * S;
          top = fract * stripHeight;
        }

      } else {
        // ── SOULZ (OVAL): Classic U/oval contour ──────────────────────────
        // Both halves trace bottom-to-top to match the 2 identical upward physical strips.
        const half = Math.floor(numSamples / 2);
        if (i < half) {
          const fract = i / Math.max(1, half - 1);
          const angle = (Math.PI / 2) + fract * Math.PI;
          top = (150 + Math.sin(angle) * 150) * S;
          const verticalPos = Math.sin(angle);
          const pinch = 1 - 0.3 * Math.exp(-Math.pow(verticalPos - 0.1, 2) * 5);
          left = (70 + (Math.cos(angle) * 70 * pinch)) * S;
        } else {
          const fract = (i - half) / Math.max(1, (numSamples - half) - 1);
          const angle = (Math.PI / 2) - fract * Math.PI;
          top = (150 + Math.sin(angle) * 150) * S;
          const verticalPos = Math.sin(angle);
          const pinch = 1 - 0.3 * Math.exp(-Math.pow(verticalPos - 0.1, 2) * 5);
          left = (70 + (Math.cos(angle) * 70 * pinch)) * S;
        }
      }

      if (i > 0) {
        const prev = pathSamples[i - 1];
        const dx = left - prev.left;
        const dy = top - prev.top;
        const dist = Math.sqrt(dx * dx + dy * dy);
        // Ignore large jump (returning to the bottom center for the right strip)
        if (dist < 100 * S) {
          totalLength += dist;
        }
      }
      pathSamples.push({ top, left, length: totalLength });
    }
    return { pathSamples, totalLength };
  }, [product, vizShape, numLeds, deviceSegments, productProfile]);

  const leds = useMemo(() => {
    const { pathSamples, totalLength } = pathGeometry;
    const list = [];
    let lastSampleIdx = 0;
    // We strictly use dense rendering (64 points) to draw the perfectly smooth physical silicone casing, 
    // but the `isHotspot` flag ensures ONLY the true 16 LEDs actually fire inside it!
    // DUAL_STRIP uses 2× dense sampling for smooth straight lines; RING uses 4× for arc smoothness.
    const renderLeds = vizShape === 'RING'
      ? Math.max(numLeds * 4, 64)
      : vizShape === 'DUAL_STRIP'
        ? Math.max(numLeds * 2, 60)
        : Math.max(numLeds * 2, 86);
    for (let i = 0; i < renderLeds; i++) {
      let left = 0;
      let top = 0;
      const outerDiam = productProfile.vizBlobDiameterMm > 6 ? 16 : 12;

      const offset = outerDiam / 2;

      const targetLength = (i / renderLeds) * totalLength;
      let p1 = pathSamples[lastSampleIdx];
      let p2 = pathSamples[lastSampleIdx + 1] || p1;

      for (let j = lastSampleIdx; j < pathSamples.length - 1; j++) {
        if (pathSamples[j + 1].length >= targetLength) {
          p1 = pathSamples[j];
          p2 = pathSamples[j + 1];
          lastSampleIdx = j;
          break;
        }
      }

      const segmentLength = p2.length - p1.length;
      const t = segmentLength <= 0.0001 ? 0 : (targetLength - p1.length) / segmentLength;
      left = p1.left + (p2.left - p1.left) * t - offset;
      top = p1.top + (p2.top - p1.top) * t - offset;

      const segmentI = i % (renderLeds / 2);
      const activeSegmentLeds = renderLeds / 2;
      
      // Raw smooth path interval
      const rawFract = (segmentI / activeSegmentLeds);

      // QUANTIZATION: Forces the perfectly smooth line to illuminate in exactly 16 discrete hardware blocks!
      // Instead of lighting sliding across the shape fluidly, it snaps instantly inside the boundaries of the physical LED chip.
      const fract = Math.floor(rawFract * numLeds) / numLeds;
      const mirroredFract = fract <= 0.5 ? fract * 2 : (1 - fract) * 2;

      let dotColor: any = isPoweredOn ? color : '#333333';
      let dotOpacity: any = isPoweredOn ? 1 : 0.2;

      const segmentBoundary = (rawFract * deviceSegments) % 1;
      const isGap = deviceSegments > 1 && (segmentBoundary < 0.015 || segmentBoundary > 0.985);

      if (isPoweredOn && isGap) {
        dotColor = '#111111';
        dotOpacity = Math.max(0.1, (brightness / 100) * 0.3);
      } else if (isPoweredOn) {
        if (mode === 'FAVORITES') {
          const rainbowColors = [0, 1 / 6, 2 / 6, 3 / 6, 4 / 6, 5 / 6, 1].map(v => HSLToHex((v - mirroredFract + 1) % 1 * 360, 100, 50));
          dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
        } else if (mode === 'MUSIC') {
          // ── Hardware-accurate music mode simulation ──
          const musicFrame = getMusicVisualizerFrame(patternId || 1, numLeds, animTick, audioMagnitude, color);
          const mRawPos = (segmentI / activeSegmentLeds) * musicFrame.pixels.length;
          const mSlot = Math.floor(mRawPos) % Math.max(1, musicFrame.pixels.length);
          const mPx = musicFrame.pixels[mSlot] || { r: 255, g: 255, b: 255 };
          dotColor = `#${mPx.r.toString(16).padStart(2, '0')}${mPx.g.toString(16).padStart(2, '0')}${mPx.b.toString(16).padStart(2, '0')}`;
          dotOpacity = isPoweredOn ? (musicFrame.opacities[mSlot] ?? 1.0) * (brightness / 100) : 0;
        } else if (mode === 'STREET' || mode === 'MULTIMODE') {
          let fgHex = fixedFgColor || color;
          let bgHex = fixedBgColor || '#000000';
          let pid = Math.max(1, patternId || 1);

          if (mode === 'STREET') {
            const isActiveBraking = motionState === 'HARD_BRAKING' || motionState === 'STOPPED' || isStreetBraking;
            const isSlowing = motionState === 'SLOWING_DOWN';
            fgHex = '#FF2200';
            bgHex = isSlowing ? '#FFAA00' : streetCruiseColor;
            
            if (isActiveBraking || motionState === 'HARD_BRAKING') pid = 103;
            else if (motionState === 'STOPPED') pid = 101;
            else if (motionState === 'SLOWING_DOWN') pid = 104;
            else if (motionState === 'ACCELERATING') pid = 105;
            else pid = 102; // CRUISING
          }

          // Parse fg/bg hex strings to RGB objects for PatternEngine
          const fgRgb: RGB = {
            r: parseInt(fgHex.slice(1, 3), 16) || 0,
            g: parseInt(fgHex.slice(3, 5), 16) || 0,
            b: parseInt(fgHex.slice(5, 7), 16) || 0,
          };
          const bgRgb: RGB = {
            r: parseInt(bgHex.slice(1, 3), 16) || 0,
            g: parseInt(bgHex.slice(3, 5), 16) || 0,
            b: parseInt(bgHex.slice(5, 7), 16) || 0,
          };

          // Get the full per-LED pixel array from the pattern engines at the current animation tick
          // ── Directly leverage PatternEngine continuous simulation ──
          const framePixels = getVisualizerFrame(
            pid as PatternId,
            fgRgb,
            bgRgb,
            activeSegmentLeds,
            animTick,
            fixedDirection as 0 | 1,
            mode === 'STREET' ? { distribution: streetDistribution } : undefined
          );

          // ── Diffusion blending: blend adjacent LED colors near chip boundaries ──
          const rawLedPos = (segmentI / activeSegmentLeds) * framePixels.length;
          const slot0 = Math.floor(rawLedPos) % Math.max(1, framePixels.length);
          const slotT = rawLedPos - Math.floor(rawLedPos);
          const DIFF = 0.35;
          const boundaryProx = Math.pow(Math.abs(slotT - 0.5) * 2, 2);
          const blendAmt = DIFF * boundaryProx;
          
          // PatternEngine handles its own segment mirroring math for visualizer
          const template = SK8LYTZ_TEMPLATES.find(t => t.id === pid);
          const applySymmetry = template?.supportsSegment && deviceSegments > 1 && productProfile.isMirrored;
          
          let pSlot0 = slot0;
          if (applySymmetry) {
            // Re-map index for mirrored geometry (e.g. HALOZ inner ring)
            pSlot0 = Math.floor(mirroredFract * framePixels.length);
            pSlot0 = Math.min(pSlot0, framePixels.length - 1);
          }
          
          const pCurr = framePixels[pSlot0] || fgRgb;
          const adjIdx = (pSlot0 + 1) % framePixels.length;
          const pAdj = framePixels[Math.min(framePixels.length - 1, Math.max(0, adjIdx))] || fgRgb;
          const pr = Math.round(pCurr.r * (1 - blendAmt) + pAdj.r * blendAmt);
          const pg = Math.round(pCurr.g * (1 - blendAmt) + pAdj.g * blendAmt);
          const pb = Math.round(pCurr.b * (1 - blendAmt) + pAdj.b * blendAmt);
          dotColor = `#${pr.toString(16).padStart(2, '0')}${pg.toString(16).padStart(2, '0')}${pb.toString(16).padStart(2, '00')}`;
          
          // Special exception: Street Mode taillights stay fully bright, head and cruise dim with brightness slider
          if (mode === 'STREET' && pSlot0 < framePixels.length * streetDistribution[0]) {
            dotOpacity = (pid === 101 || pid === 103) ? 1.0 : 0.5; // Taillights max bright on brake, dim otherwise
          } else {
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          }
        } else if (mode === 'BUILDER') {
          let builderPixels: RGB[];
          if (!builderNodes || builderNodes.length === 0) {
            builderPixels = Array.from({ length: activeSegmentLeds }).map(() => ({ r: 255, g: 0, b: 0 }));
          } else {
            builderPixels = PositionalMathBuffer.generateArray(builderNodes, activeSegmentLeds, builderFillMode === 'GRADIENT');
          }

          // 0x02=Running, 0x04=Jump → scroll the pixel array using animTick
          if (builderTransitionType === 0x02 || builderTransitionType === 0x04) {
            const steps = activeSegmentLeds;
            const directionMultiplier = builderDirection === 0 ? -1 : 1;
            const shiftAmount = animTick * steps * directionMultiplier;
            const newArr = [];
            for (let k = 0; k < steps; k++) {
              let sourceIdx = Math.floor(k - shiftAmount) % steps;
              if (sourceIdx < 0) sourceIdx += steps;
              newArr.push(builderPixels[sourceIdx]);
            }
            builderPixels = newArr;
          }

          const rawLedPos = (segmentI / activeSegmentLeds) * builderPixels.length;
          const slot0 = Math.floor(rawLedPos) % Math.max(1, builderPixels.length);
          const slotT = rawLedPos - Math.floor(rawLedPos);
          const DIFF = 0.35;
          const boundaryProx = Math.pow(Math.abs(slotT - 0.5) * 2, 2);
          const blendAmt = DIFF * boundaryProx;
          const pCurr = builderPixels[slot0] || { r: 0, g: 0, b: 0 };
          const adjIdx = (slot0 + 1) % builderPixels.length;
          const pAdj = builderPixels[Math.min(builderPixels.length - 1, Math.max(0, adjIdx))] || pCurr;
          const pr = Math.round(pCurr.r * (1 - blendAmt) + pAdj.r * blendAmt);
          const pg = Math.round(pCurr.g * (1 - blendAmt) + pAdj.g * blendAmt);
          const pb = Math.round(pCurr.b * (1 - blendAmt) + pAdj.b * blendAmt);
          dotColor = `#${pr.toString(16).padStart(2, '0')}${pg.toString(16).padStart(2, '0')}${pb.toString(16).padStart(2, '00')}`;

          // APK-PROVEN commandType — StaticColorfulMode.java:
          if (builderTransitionType === 0x01) {        // Static — freeze, no animation
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          } else if (builderTransitionType === 0x02) { // Running — scrolling (opacity stays solid)
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          } else if (builderTransitionType === 0x03) { // Strobe — hard on/off flash
            const strobeOn = animTick < 0.5 ? 1 : 0;
            dotOpacity = isPoweredOn ? Math.max(0.01, strobeOn) * (brightness / 100) : 0;
          } else if (builderTransitionType === 0x04) { // Jump — scrolling + opacity wave
            const wave = 0.5 + 0.5 * Math.sin(animTick * Math.PI * 2 + fract * Math.PI * 4);
            dotOpacity = isPoweredOn ? (0.3 + wave * 0.7) * (brightness / 100) : 0;
          } else {
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          }
        }
      }

      // ── LED Chip Boundary Softening (ALL modes, ALL shapes) ──────────────────
      // Sub-LED position: 0=chip leading edge, 0.5=chip centre, 1=chip trailing edge.
      // Quadratic bell-curve dims render points near chip boundaries ~18%,
      // simulating the slight dark gap between physical LED chips through silicone.
      const subLedPos = (rawFract * numLeds) - Math.floor(rawFract * numLeds);
      const chipSoften = 1 - 0.18 * Math.pow(Math.abs(2 * subLedPos - 1), 2);

      list.push({
        key: `led_${i}`,
        position: { top, left, position: 'absolute' as const },
        activeColor: dotColor,
        activeOpacity: dotOpacity,
        chipSoften,
      });
    }
    return list;
  }, [pathGeometry, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude, fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, speed, animTick, isStreetBraking, motionState, streetCruiseColor, builderNodes, builderFillMode, builderTransitionType, builderDirection]);

  return (
    <TouchableOpacity
      activeOpacity={onLongPress ? 0.8 : 1}
      onLongPress={onLongPress ? () => onLongPress(device) : undefined}
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
                  backgroundColor: led.activeColor as any,
                  opacity: 0.03,
                }} />

                {/* ── Layer 2: Mid silicone bloom ─────────────────────────────────── */}
                {/* The silicone tube edge scatters light laterally — soft wide glow */}
                <Animated.View style={{
                  position: 'absolute',
                  width: diam * 3.2, height: diam * 3.2,
                  borderRadius: (diam * 3.2) / 2,
                  backgroundColor: led.activeColor as any,
                  opacity: 0.10,
                }} />

                {/* ── Layer 1: Inner diffuse scatter ──────────────────────────────── */}
                {/* Closest to the chip: concentrated inner halo through the silicone walls */}
                <Animated.View style={{
                  position: 'absolute',
                  width: diam * 1.7, height: diam * 1.7,
                  borderRadius: (diam * 1.7) / 2,
                  backgroundColor: led.activeColor as any,
                  opacity: 0.38,
                }} />

                {/* ── Main LED chip body ──────────────────────────────────────────── */}
                <Animated.View style={{
                  position: 'absolute', width: '100%', height: '100%', borderRadius: diam / 2,
                  backgroundColor: led.activeColor as any,
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
          style={{ color: isPoweredOn ? (isDark ? 'white' : 'black') : (isDark ? '#888' : '#666'), fontWeight: 'bold', fontSize: 11, textAlign: 'center', opacity: isPoweredOn ? 1.0 : 0.4 }}
          numberOfLines={2}
        >
          {device.name || product}
        </Text>
      </View>
    </TouchableOpacity>
  );
});

const ProductVisualizer = ({ product, color, mode, patternId, isPaired, points, devices, fixedFgColor, fixedBgColor, onLongPressDevice, brightness = 100, speed = 50, isPoweredOn = true, audioMagnitude = 0, multiColors, multiTransition, isStreetBraking = false, streetCruiseColor = '#FF8C00', motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT', builderTransitionType = 0x01, builderDirection = 1, fixedDirection = 1 }: ProductVisualizerProps) => {
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
  const renderDevices = (devices && devices.length > 0) ? devices : (
    isPaired
      ? [{ name: `${product} Left`, type: product, points }, { name: `${product} Right`, type: product, points }]
      : [{ name: product, type: product, points }]
  );

  return (
    <View style={[styles.container, { backgroundColor: isDark ? '#000000' : '#FFFFFF', borderColor: isDark ? 'rgba(255,255,255,0.12)' : 'rgba(0,0,0,0.08)' }]}>
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
            fallbackPoints={points}
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
