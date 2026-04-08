import React, { useEffect, useRef, useMemo, useState } from 'react';
import { View, StyleSheet, Animated, Text, TouchableOpacity } from 'react-native';
import { getRbmVisualizerFrame, getRbmMusicFrame, rgbToHex } from '../utils/RbmSimulator';
import { useTheme } from '../context/ThemeContext';
import { getVisualizerFrame } from '../protocols/PatternEngine';
import type { RGB, PatternId } from '../protocols/PatternEngine';
import { ZenggeVisualizerMath } from '../protocols/ZenggeVisualizerMath';
import { PositionalMathBuffer } from '../protocols/PositionalMathBuffer';

interface DeviceConfig {
  id?: string;
  name?: string;
  type?: 'HALOZ' | 'SOULZ';
  points?: number;
}

interface ProductVisualizerProps {
  product: 'HALOZ' | 'SOULZ';
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
  statusText?: string;
  audioMagnitude?: number;
  rawHexPayload?: number[];
  multiColors?: string[];
  multiTransition?: number;
  isStreetBraking?: boolean;
  streetCruiseColor?: string;
  motionState?: string;
  builderNodes?: any[];
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;
  builderDirection?: number;
}

// Convert HSL to Hex manually as React Native Interpolate handles strict string maps better
function HSLToHex(h: number, s: number, l: number) {
  h = (h % 360 + 360) % 360; // Normalize [0-360]
  s /= 100; l /= 100;
  let c = (1 - Math.abs(2 * l - 1)) * s,
      x = c * (1 - Math.abs((h / 60) % 2 - 1)),
      m = l - c/2,
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

const VisualizerUnit = React.memo(({ device, color, mode, patternId, animValue, fallbackProduct, fallbackPoints, onLongPress, fixedFgColor, fixedBgColor, brightness = 100, speed = 50, isPoweredOn = true, audioMagnitude = 0, multiColors = [], multiTransition = 0, rawHexPayload: _rawHexPayload, simMode: _simMode, isStreetBraking = false, streetCruiseColor = '#FF8C00', motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT', builderTransitionType = 1, builderDirection = 1 }: any) => {
  const { isDark } = useTheme();
  const product = String(device.type || fallbackProduct);
  const isHaloz = !product.toLowerCase().includes('soul');

  // Track animValue tick (0.0–1.0) for PatternEngine frame generation
  const [animTick, setAnimTick] = useState(0);
  useEffect(() => {
    const id = animValue.addListener(({ value }: { value: number }) => setAnimTick(value));
    return () => animValue.removeListener(id);
  }, [animValue]);

  // HALOZ actual hardware is composed of two segments making a ring of 16 LEDs.
  // SOULZ visualizer uses the pointsPerSide * 2 logic to visually double the strip.
  const devicePoints = device?.points || fallbackPoints || (isHaloz ? 16 : 43);
  const deviceSegments = device?.segments || 1;
  const numLeds = Math.floor(devicePoints / deviceSegments); // 16 or 43

  const leds = useMemo(() => {
    const list = [];
    const numSamples = 5000;
    const pathSamples: any[] = [];
    let totalLength = 0;
    
    // SCALE FACTOR (SHRUNK)
    const S = 0.38;

    for (let i = 0; i <= numSamples; i++) {
        let left = 0;
        let top = 0;

        if (isHaloz) {
            let angle = 0;
            const half = Math.floor(numSamples / 2);
            if (i < half) {
               // Segment 1: Bottom Middle (π/2) ascending Right side to Top Middle (-π/2)
               const fraction = i / Math.max(1, half - 1); 
               angle = (Math.PI / 2) - fraction * Math.PI; 
            } else {
               // Segment 2: Top Middle (-π/2) descending Left side to Bottom Middle (-3π/2)
               const fraction = (i - half) / Math.max(1, (numSamples - half) - 1); 
               angle = - (Math.PI / 2) - fraction * Math.PI; 
            }

            // Superellipse implementation for a rounded rectangle frame.
            const n = 4; // Flatness / Boxiness factor
            const power = 2 / n;
            const cosT = Math.cos(angle);
            const sinT = Math.sin(angle);
            const x = 70 * Math.sign(cosT) * Math.pow(Math.abs(cosT), power);
            const y = 110 * Math.sign(sinT) * Math.pow(Math.abs(sinT), power);

            left = (80 + x) * S;
            top = (120 + y) * S;
        } else {
            // SOULZ: Maintain the exact classic U/oval contour, but mathematically trace the coordinates 
            // from bottom-to-top on BOTH halves so the physical point array matches the 2 identical upward strips.
            const half = Math.floor(numSamples / 2);
            if (i < half) {
                // Left side: trace from bottom center (π/2) ascending up the left edge to top center (3π/2)
                const fract = i / Math.max(1, half - 1);
                const angle = (Math.PI / 2) + fract * Math.PI;
                top = (150 + Math.sin(angle) * 150) * S;
                const verticalPos = Math.sin(angle);
                const pinch = 1 - 0.3 * Math.exp(-Math.pow(verticalPos - 0.1, 2) * 5); 
                left = (70 + (Math.cos(angle) * 70 * pinch)) * S;
            } else {
                // Right side: trace from bottom center (π/2) ascending up the right edge to top center (-π/2)
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
            const dist = Math.sqrt(dx*dx + dy*dy);
            // Ignore large jump (returning to the bottom center for the right strip)
            if (dist < 100 * S) {
                 totalLength += dist;
            }
        }
        pathSamples.push({ top, left, length: totalLength });
    }

    let lastSampleIdx = 0;
    // We strictly use dense rendering (64 points) to draw the perfectly smooth physical silicone casing, 
    // but the `isHotspot` flag ensures ONLY the true 16 LEDs actually fire inside it!
    const renderLeds = isHaloz ? Math.max(numLeds * 4, 64) : Math.max(numLeds * 2, 86);
    for (let i = 0; i < renderLeds; i++) {
        let left = 0;
        let top = 0;
        const outerDiam = isHaloz ? 16 : 12; // Reverted to physical 6mm strip dimensions

        const offset = outerDiam / 2;

        const targetLength = (i / renderLeds) * totalLength;
        let p1 = pathSamples[lastSampleIdx];
        let p2 = pathSamples[lastSampleIdx + 1] || p1;
        
        for (let j = lastSampleIdx; j < pathSamples.length - 1; j++) {
            if (pathSamples[j+1].length >= targetLength) {
                p1 = pathSamples[j];
                p2 = pathSamples[j+1];
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
        // HALOZ 2-segment mirror: Segment 2 (i >= renderLeds/2) runs the same pattern
        // in REVERSE relative to its own start point (front of box → back of box).
        // This mirrors Segment 1 (back→front) creating bilateral symmetry on the ring.
        const isHalozSeg2 = isHaloz && (i >= renderLeds / 2);
        // mirrorFrameSlot: given a frame of segLen, returns the mirrored index for Seg2
        const mirrorSlot = (slot: number, segLen: number) =>
          isHalozSeg2 ? Math.max(0, segLen - 1 - slot) : slot;
        
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
          dotOpacity = Math.max(0.1, (brightness/100) * 0.3);
        } else if (isPoweredOn) {
          if (mode === 'CANDLE') {
             dotColor = color;
             const offset = (i * 0.15) % 1;
             dotOpacity = animValue.interpolate({
                 inputRange: [0, 0.25, 0.5, 0.75, 1],
                 outputRange: [
                     (brightness/100) * (0.6 + (Math.sin(offset * Math.PI) * 0.1)), 
                     (brightness/100) * (0.8 + (Math.cos(offset * Math.PI) * 0.2)), 
                     (brightness/100) * 0.4, 
                     (brightness/100) * 0.9, 
                     (brightness/100) * 0.6
                 ],
             });
          } else if (mode === 'MULTICOLOR') {
             
             if (multiTransition === 0) { // Static
                const segmentIdx = Math.floor(mirroredFract * multiColors.length);
                const safeIdx = Math.min(Math.max(0, segmentIdx), multiColors.length - 1);
                dotColor = multiColors[safeIdx];
                dotOpacity = 1.0;
             } else if (multiTransition === 1) { // Gradual crossfade
                const inputR = multiColors.map((_: any, i: number) => i / Math.max(1, multiColors.length - 1));
                if (multiColors.length === 1) {
                   dotColor = multiColors[0];
                } else {
                   dotColor = animValue.interpolate({ inputRange: inputR, outputRange: multiColors });
                }
                dotOpacity = 1.0;
             } else if (multiTransition === 2) { // Strobe
                const segmentIdx = Math.floor(mirroredFract * multiColors.length);
                const safeIdx = Math.min(Math.max(0, segmentIdx), multiColors.length - 1);
                dotColor = multiColors[safeIdx];
                dotOpacity = animValue.interpolate({ inputRange: [0, 0.49, 0.5, 0.99, 1], outputRange: [1, 1, 0.1, 0.1, 1] });
             } else if (multiTransition === 3 || multiTransition === undefined) { // Running Water
                // We create a shifting offset using animValue mapped from 0 to 1
                // We calculate a virtual position for each LED based on fract and animValue offset
                if (multiColors.length === 1) {
                   dotColor = multiColors[0];
                } else {
                   // Create an infinite loop array wrapping
                   const doubleColors = [...multiColors, ...multiColors, ...multiColors];
                   const maxLenStr = doubleColors.length - 1;
                   const _pointsArr = Array.from({length: maxLenStr + 1}, (_, idx) => idx / maxLenStr);
                   
                   // Fract controls spatial shift, animValue controls temporal shift
                   // We want spatial gradients that physically slide over time
                   // Since React Native Animated doesn't allow `+` easily within interpolate without creating new Animated.add nodes globally,
                   // we can simulate running water using an explicit mathematical gradient fallback
                   dotColor = animValue.interpolate({
                      inputRange: [0, 1],
                      outputRange: [
                         multiColors[Math.min(multiColors.length - 1, Math.floor(((mirroredFract + 0) % 1) * multiColors.length))], 
                         multiColors[Math.min(multiColors.length - 1, Math.floor(((mirroredFract + 0.99) % 1) * multiColors.length))]
                      ]
                   });
                }
                // To simulate running water properly, we use native opacity fading mimicking waves
                dotOpacity = animValue.interpolate({
                   inputRange: [0, Math.max(0, mirroredFract - 0.2), mirroredFract, Math.min(1, mirroredFract + 0.2), 1],
                   outputRange: [0.2, 0.2, 1, 0.2, 0.2]
                });
             }
          } else if (mode === 'FAVORITES') {
             const rainbowColors = [0, 1/6, 2/6, 3/6, 4/6, 5/6, 1].map(v => HSLToHex((v - mirroredFract + 1) % 1 * 360, 100, 50));
             dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
          } else if (mode === 'PROGRAMS') {
             const pid = patternId || 1;

             if (pid === 100) {
               // ── Emergency pattern: 3-zone layout (custom SK8Lytz protocol) ──
               // Zone 0–20%: Red (heel zone)
               // Zone 20–80%: Bouncing yellow dot in middle
               // Zone 80–100%: White (toe zone)
               if (mirroredFract > 0.8) {
                 dotColor = '#FFFFFF'; dotOpacity = 1.0;
               } else if (mirroredFract < 0.2) {
                 dotColor = '#FF0000'; dotOpacity = 1.0;
               } else {
                 const target = (mirroredFract - 0.2) / 0.6;
                 const t1 = (1 + target) / 2;
                 const t2 = (1 - target) / 2;
                 const rawInputs = [0, Math.max(0, t2-0.08), t2, Math.min(1, t2+0.08), Math.max(0, t1-0.08), t1, Math.min(1, t1+0.08), 1].sort((a,b) => a-b);
                 const safeInputs = [rawInputs[0]];
                 for (let k = 1; k < rawInputs.length; k++) {
                     if (rawInputs[k] > safeInputs[safeInputs.length - 1] + 0.001) safeInputs.push(rawInputs[k]);
                 }
                 dotOpacity = animValue.interpolate({ inputRange: safeInputs, outputRange: safeInputs.map(v => (Math.abs(v-t2) < 0.005 || Math.abs(v-t1) < 0.005) ? 1.0 : 0.1) });
                 dotColor = animValue.interpolate({ inputRange: safeInputs, outputRange: safeInputs.map(v => (Math.abs(v-t2) < 0.005 || Math.abs(v-t1) < 0.005) ? '#FFFF00' : '#111111') });
               }
             } else {
               // ── All other RBM patterns: hardware-accurate pixel simulation ──
               // HALOZ: generates 8-LED (per-segment) frame; Seg2 mirrors it reversed.
               const rbmSegLeds = isHaloz ? Math.ceil(numLeds / 2) : numLeds;
               const rbmFrame = getRbmVisualizerFrame(pid, rbmSegLeds, animTick);
               const rawLedPos = (segmentI / activeSegmentLeds) * rbmFrame.length;
               const rSlot0Raw = Math.floor(rawLedPos) % Math.max(1, rbmFrame.length);
               const rSlot0 = mirrorSlot(rSlot0Raw, rbmFrame.length);
               const rSlotT = rawLedPos - Math.floor(rawLedPos);
               const RDIFF = 0.30;
               const rBoundary = Math.pow(Math.abs(rSlotT - 0.5) * 2, 2);
               const rBlend = RDIFF * rBoundary;
               const rCurr = rbmFrame[rSlot0] || rbmFrame[0] || { r: 255, g: 0, b: 0 };
               const rAdjIdx = mirrorSlot(
                 isHalozSeg2
                   ? Math.max(0, rSlot0Raw - 1)   // mirrored: adjacent is previous in raw
                   : (rSlot0Raw + 1) % rbmFrame.length,
                 rbmFrame.length
               );
               const rAdj = rbmFrame[Math.min(rbmFrame.length - 1, Math.max(0, rAdjIdx))] || rCurr;
               dotColor = `#${Math.round(rCurr.r*(1-rBlend)+rAdj.r*rBlend).toString(16).padStart(2,'0')}${Math.round(rCurr.g*(1-rBlend)+rAdj.g*rBlend).toString(16).padStart(2,'0')}${Math.round(rCurr.b*(1-rBlend)+rAdj.b*rBlend).toString(16).padStart(2,'0')}`;
               dotOpacity = isPoweredOn ? Math.max(0.02, brightness / 100) : 0;
             }
          } else if (mode === 'MUSIC') {
             // ── Hardware-accurate music mode simulation ──
             // HALOZ: generates 8-LED (per-segment) frame; Seg2 mirrors it reversed.
             const musicSegLeds = isHaloz ? Math.ceil(numLeds / 2) : numLeds;
             const musicFrame = getRbmMusicFrame(patternId || 1, musicSegLeds, animTick, audioMagnitude, color);
             const mRawPos = (segmentI / activeSegmentLeds) * musicFrame.pixels.length;
             const mSlotRaw = Math.floor(mRawPos) % Math.max(1, musicFrame.pixels.length);
             const mSlot = mirrorSlot(mSlotRaw, musicFrame.pixels.length);
             const mPx = musicFrame.pixels[mSlot] || { r: 255, g: 255, b: 255 };
             dotColor = rgbToHex(mPx);
             dotOpacity = isPoweredOn ? (musicFrame.opacities[mSlot] ?? 1.0) * (brightness / 100) : 0;
          } else if (mode === 'STREET') {
             // ── Street Mode: zone-based car-light layout ──
             // fract 0.0–0.3  → TAIL  (red: 50% cruising, 100% braking) [#10]
             // fract 0.3–0.7  → CRUISE (user color or amber if slowing, animated bounce) [#9,#11]
             // fract 0.7–1.0  → HEAD  (warm white, always steady)
             const isActiveBraking = motionState === 'HARD_BRAKING' || motionState === 'STOPPED' || isStreetBraking;
             const isSlowing = motionState === 'SLOWING_DOWN';
             // Cruise zone color: amber when slowing, user color when cruising/accelerating
             const vizCruiseColor = isSlowing ? '#FFAA00' : streetCruiseColor;

             if (fract < 0.3) {
               // Taillights: ABSOLUTE — 100% braking, 50% cruising (no brightness slider in street mode)
               dotColor = '#FF2200';
               dotOpacity = isActiveBraking ? 1.0 : 0.5;
             } else if (fract >= 0.7) {
               dotColor = '#FFF5E0';
               dotOpacity = brightness / 100;
             } else {
               dotColor = vizCruiseColor;
               const cruiseFract = (fract - 0.3) / 0.4;
               
               // Only animate bounce during CRUISING/ACCELERATING; freeze dim otherwise
               const shouldBounce = motionState === 'CRUISING' || motionState === 'ACCELERATING';
               if (shouldBounce) {
                 // Triangle wave: goes 0 to 1 back to 0, creating a bouncing pulse [#9]
                 const bounceT = animTick <= 0.5 ? animTick * 2 : (1 - animTick) * 2;
                 const dist = Math.abs(cruiseFract - bounceT);
                 if (dist < 0.18) {
                     const glow = 1 - (dist / 0.18); 
                     dotOpacity = 0.3 + glow * ((brightness / 100) - 0.3);
                 } else {
                     dotOpacity = 0.3;
                 }
               } else {
                 // Stopped or slowing — steady dim amber/red
                 dotOpacity = (brightness / 100) * 0.6;
               }
             }
          } else if (mode === 'MULTIMODE') {
             const fgHex = fixedFgColor || color;
             const bgHex = fixedBgColor || '#000000';
             const pid = Math.max(1, patternId || 1);

             // Parse fg/bg hex strings to RGB objects for PatternEngine
             const fgRgb: RGB = {
               r: parseInt(fgHex.slice(1,3), 16) || 0,
               g: parseInt(fgHex.slice(3,5), 16) || 0,
               b: parseInt(fgHex.slice(5,7), 16) || 0,
             };
             const bgRgb: RGB = {
               r: parseInt(bgHex.slice(1,3), 16) || 0,
               g: parseInt(bgHex.slice(3,5), 16) || 0,
               b: parseInt(bgHex.slice(5,7), 16) || 0,
             };

             // Get the full per-LED pixel array from the pattern engines at the current animation tick
             // HALOZ: generates 8-LED (per-segment) frame; Seg2 slot is mirrored.
             const mmSegLeds = isHaloz ? Math.ceil(numLeds / 2) : numLeds;
             
             let framePixels: RGB[];
             if (pid <= 34) {
                 // Use the new mathematically accurate 34-effect Visualizer Math
                 const base16 = ZenggeVisualizerMath.getVisualizerDots(pid, fgRgb, bgRgb, animTick, true, deviceSegments > 1);
                 
                 // Stretch the 16 native hardware dots into the Visualizer box segment
                 framePixels = [];
                 const dotsPerSegment = Math.max(1, Math.floor(mmSegLeds / Math.max(1, deviceSegments)));
                 for (let i = 0; i < mmSegLeds; i++) {
                    const segmentLocalIndex = i % dotsPerSegment;
                    framePixels.push(base16[segmentLocalIndex % 16]);
                 }
             } else {
                 // Legacy fallback
                 framePixels = getVisualizerFrame(pid as PatternId, fgRgb, bgRgb, mmSegLeds, animTick);
             }

             // ── Diffusion blending: blend adjacent LED colors near chip boundaries ──
             const rawLedPos = (segmentI / activeSegmentLeds) * framePixels.length;
             const slot0Raw = Math.floor(rawLedPos) % Math.max(1, framePixels.length);
             const slot0 = mirrorSlot(slot0Raw, framePixels.length);
             const slotT = rawLedPos - Math.floor(rawLedPos);
             const DIFF = 0.35;
             const boundaryProx = Math.pow(Math.abs(slotT - 0.5) * 2, 2);
             const blendAmt = DIFF * boundaryProx;
             const pCurr = framePixels[slot0] || fgRgb;
             const adjIdx = mirrorSlot(
               isHalozSeg2
                 ? Math.max(0, slot0Raw - 1)
                 : (slot0Raw + 1) % framePixels.length,
               framePixels.length
             );
             const pAdj = framePixels[Math.min(framePixels.length - 1, Math.max(0, adjIdx))] || fgRgb;
             const pr = Math.round(pCurr.r * (1 - blendAmt) + pAdj.r * blendAmt);
             const pg = Math.round(pCurr.g * (1 - blendAmt) + pAdj.g * blendAmt);
             const pb = Math.round(pCurr.b * (1 - blendAmt) + pAdj.b * blendAmt);
             dotColor = `#${pr.toString(16).padStart(2,'0')}${pg.toString(16).padStart(2,'0')}${pb.toString(16).padStart(2,'00')}`;
             dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          } else if (mode === 'BUILDER') {
              let builderPixels: RGB[];
              if (!builderNodes || builderNodes.length === 0) {
                 builderPixels = Array.from({ length: activeSegmentLeds }).map(() => ({ r: 255, g: 0, b: 0 }));
              } else {
                 builderPixels = PositionalMathBuffer.generateArray(builderNodes, activeSegmentLeds, builderFillMode === 'GRADIENT');
              }
              
              if (builderTransitionType === 4 || builderTransitionType === 5) {
                 const steps = activeSegmentLeds;
                 const directionMultiplier = builderDirection === 0 ? -1 : 1;
                 let shiftAmount = animTick * steps * directionMultiplier;
                 if (builderTransitionType === 5) {
                     shiftAmount = Math.floor(shiftAmount);
                 }
                 const newArr = [];
                 for (let k = 0; k < steps; k++) {
                     let sourceIdx = Math.floor(k - shiftAmount) % steps;
                     if (sourceIdx < 0) sourceIdx += steps;
                     newArr.push(builderPixels[sourceIdx]);
                 }
                 builderPixels = newArr;
              }

              const rawLedPos = (segmentI / activeSegmentLeds) * builderPixels.length;
              const slot0Raw = Math.floor(rawLedPos) % Math.max(1, builderPixels.length);
              const slot0 = mirrorSlot(slot0Raw, builderPixels.length);
              const slotT = rawLedPos - Math.floor(rawLedPos);
              const DIFF = 0.35;
              const boundaryProx = Math.pow(Math.abs(slotT - 0.5) * 2, 2);
              const blendAmt = DIFF * boundaryProx;
              const pCurr = builderPixels[slot0] || {r:0,g:0,b:0};
              const adjIdx = mirrorSlot(
                isHalozSeg2
                  ? Math.max(0, slot0Raw - 1)
                  : (slot0Raw + 1) % builderPixels.length,
                builderPixels.length
              );
              const pAdj = builderPixels[Math.min(builderPixels.length - 1, Math.max(0, adjIdx))] || pCurr;
              const pr = Math.round(pCurr.r * (1 - blendAmt) + pAdj.r * blendAmt);
              const pg = Math.round(pCurr.g * (1 - blendAmt) + pAdj.g * blendAmt);
              const pb = Math.round(pCurr.b * (1 - blendAmt) + pAdj.b * blendAmt);
              dotColor = `#${pr.toString(16).padStart(2,'0')}${pg.toString(16).padStart(2,'0')}${pb.toString(16).padStart(2,'00')}`;
              
              if (builderTransitionType === 1) { // Static
                  dotOpacity = isPoweredOn ? (brightness / 100) : 0;
              } else if (builderTransitionType === 2) { // Gradual breath
                  const t = animTick < 0.5 ? animTick * 2 : (1 - animTick) * 2; // Triangle wave 0-1
                  dotOpacity = isPoweredOn ? (0.2 + t * 0.8) * (brightness / 100) : 0;
              } else if (builderTransitionType === 3) { // Strobe
                  const t = animTick < 0.5 ? 1 : 0; // Hard step
                  dotOpacity = isPoweredOn ? Math.max(0.01, t) * (brightness / 100) : 0;
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
  }, [product, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude, fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, speed, animTick, isStreetBraking, motionState, streetCruiseColor, builderNodes, builderFillMode, builderTransitionType, builderDirection]);

  return (
    <TouchableOpacity 
      activeOpacity={onLongPress ? 0.8 : 1}
      onLongPress={onLongPress ? () => onLongPress(device) : undefined}
      style={{ alignItems: 'center', marginHorizontal: 12, paddingVertical: 4 }}
    >
      <View style={[
        isHaloz ? styles.haloBase : styles.soulBase, 
        { alignSelf: 'center', opacity: isPoweredOn ? (brightness === 0 ? 0 : Math.max(0.06, Math.pow(brightness / 100, 1.8))) : 0.15 }
      ]}>
         {/* Physical unlit silicone background track simulation */}
         {isHaloz && (
            <View style={{
               position: 'absolute',
               width: 50 + 12, 
               height: 75 + 12, 
               borderWidth: 8, // Creates a very thin hollow track
               borderColor: 'rgba(255,255,255,0.06)',
               borderRadius: 20, 
               alignSelf: 'center',
               top: '50%',
               marginTop: -(75 + 12) / 2,
               left: '50%',
               marginLeft: -(50 + 12) / 2
            }}/>
         )}

         {leds.map(led => {
            const diam = isHaloz ? 7.6 : 5.7;

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
      <View style={{ marginTop: 16, alignItems: 'center', zIndex: 10, width: 100 }}>
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
const ProductVisualizer = ({ product, color, mode, patternId, isPaired, points, devices, fixedFgColor, fixedBgColor, onLongPressDevice, brightness = 100, speed = 50, isPoweredOn = true, statusText: _statusText, audioMagnitude = 0, rawHexPayload, multiColors, multiTransition, isStreetBraking = false, streetCruiseColor = '#FF8C00', motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT', builderTransitionType = 1, builderDirection = 1 }: ProductVisualizerProps) => {
  const { isDark } = useTheme();
  const animValue = useRef(new Animated.Value(0)).current;

  // VISUALIZER PROTOCOL SYNCHRONIZATION ENGINE
  // Derive 1:1 hardware simulation states natively out of raw Bluetooth Payload Hex
  let simMode = mode;
  let simPatternId = patternId;
  let simSpeed = speed;
  let simBrightness = brightness;
  let simColor = color;
  let simAudioMag = audioMagnitude;
  let simMultiColors: string[] = multiColors || [];
  let simMultiTransition = multiTransition || 0;

  const activeDevice = devices && devices.length > 0 ? devices[0] : null; 
  const hwSorting = (activeDevice as any)?.sorting || 'GRB';

  const applySorting = (rBytes: number, gBytes: number, bBytes: number) => {
      const order = hwSorting.toUpperCase();
      let shiftR = rBytes, shiftG = gBytes, shiftB = bBytes;
      if (order === 'GRB') { shiftR = gBytes; shiftG = rBytes; shiftB = bBytes; }
      else if (order === 'GBR') { shiftR = bBytes; shiftG = rBytes; shiftB = gBytes; }
      else if (order === 'BRG') { shiftR = gBytes; shiftG = bBytes; shiftB = rBytes; }
      else if (order === 'BGR') { shiftR = bBytes; shiftG = gBytes; shiftB = rBytes; }
      else if (order === 'RBG') { shiftR = rBytes; shiftG = bBytes; shiftB = gBytes; }
      return { r: shiftR, g: shiftG, b: shiftB };
  };

  if (rawHexPayload && rawHexPayload.length > 5 && isPoweredOn) {
      let payloadOffset = 0;
      let op = rawHexPayload[0];
      
      // Auto-strip Symphony V2 Protocol Wrapping (00 XX 80 00 ...)
      if (rawHexPayload[0] === 0x00 && rawHexPayload[2] === 0x80 && rawHexPayload.length > 8) {
          op = rawHexPayload[8];
          payloadOffset = 8;
      }

      if (op === 0x59) {
          simMode = mode === 'MULTICOLOR' ? 'MULTICOLOR' : mode === 'BUILDER' ? 'BUILDER' : mode === 'STREET' ? 'STREET' : 'MULTIMODE';
          if (mode === 'MULTICOLOR') {
             simPatternId = 1;
             
             // BLE MTU drops payloads > 20 bytes into chunks, so rawHexPayload may literally only be the first 20 bytes.
             // If we already have the complete array from our props Native React State, DO NOT overwrite it with a truncated hex parse!
             if (!multiColors || multiColors.length === 0) {
                 // 0x59 format: [opcode(1), totalLen(2), R,G,B × numPixels, numPts(2), transType(1), speed(1), dir(1), checksum(1)]
                 // Pixel data: indices [3+offset .. length-7] (7 tail bytes: numPts×2, transType, speed, dir, checksum)
                 // After wrapping: header is 8 bytes so payloadOffset=8 for wrapped packets.
                 const pixelEnd = rawHexPayload.length - 7;
                 const colors = [];
                 for (let i = 3 + payloadOffset; i + 2 <= pixelEnd; i += 3) {
                    const rRaw = rawHexPayload[i] || 0;
                    const gRaw = rawHexPayload[i+1] || 0;
                    const bRaw = rawHexPayload[i+2] || 0;
                    const { r, g, b } = applySorting(rRaw, gRaw, bRaw);
                    colors.push(`#${r.toString(16).padStart(2,'0')}${g.toString(16).padStart(2,'0')}${b.toString(16).padStart(2,'0')}`);
                 }
                 simMultiColors = colors;
                 simColor = colors[0] || color;
             }
          }
          simSpeed = rawHexPayload[rawHexPayload.length - 3] || speed;
      } else if (op === 0x42) {
          simMode = 'PROGRAMS';
          simPatternId = rawHexPayload[1 + payloadOffset] || patternId;
          simSpeed = rawHexPayload[2 + payloadOffset] || speed;
          simBrightness = rawHexPayload[3 + payloadOffset] || brightness;
      } else if (op === 0x73) {
          simMode = 'MUSIC';
          simPatternId = rawHexPayload[3 + payloadOffset] || patternId;
          simAudioMag = audioMagnitude; 
          simSpeed = rawHexPayload[11 + payloadOffset] || speed;
          simBrightness = rawHexPayload[12 + payloadOffset] || brightness;
          const rRaw = rawHexPayload[4 + payloadOffset]||0, gRaw = rawHexPayload[5 + payloadOffset]||0, bRaw = rawHexPayload[6 + payloadOffset]||0;
          const { r, g, b } = applySorting(rRaw, gRaw, bRaw);
          simColor = `#${r.toString(16).padStart(2,'0')}${g.toString(16).padStart(2,'0')}${b.toString(16).padStart(2,'0')}`;
      } else if (op === 0x39 && rawHexPayload[1 + payloadOffset] === 0xD1) {
          simMode = 'CANDLE';
          simPatternId = 1;
          const rRaw = rawHexPayload[2 + payloadOffset]||0, gRaw = rawHexPayload[3 + payloadOffset]||0, bRaw = rawHexPayload[4 + payloadOffset]||0;
          const { r, g, b } = applySorting(rRaw, gRaw, bRaw);
          simColor = `#${r.toString(16).padStart(2,'0')}${g.toString(16).padStart(2,'0')}${b.toString(16).padStart(2,'0')}`;
          simBrightness = rawHexPayload[6 + payloadOffset] || 100;
          // Candle Amplitude stored at index 7 controls visualizer flicker speed natively
          const candleAmp = rawHexPayload[7 + payloadOffset] || 2;
          simSpeed = candleAmp === 1 ? -9 : (candleAmp === 2 ? 40 : 100);
      } else if (op === 0x51) {
          // Pro Effects payload (0x51 DIY Custom Mode — 32 steps × 9 bytes)
          // Step 0 starts at byte [1 + payloadOffset]:
          //   [0] = 0xF0 (active) or 0x0F (inactive)
          //   [1] = hwEffectId (1-33 for Pro Effects)
          //   [2] = speed
          //   [3..5] = FG color R,G,B
          //   [6..8] = BG color R,G,B
          simMode = 'MULTIMODE';
          const step0ActiveFlag = rawHexPayload[1 + payloadOffset];
          if (step0ActiveFlag === 0xF0) {
              const hwEffectId = rawHexPayload[2 + payloadOffset] || 1; // hardware 1-33
              simPatternId = hwEffectId + 1; // map hardware 1-33 → UI 2-34
              simSpeed = rawHexPayload[3 + payloadOffset] || simSpeed;
              // Restore FG color for visualizer from the payload
              const fgR = rawHexPayload[4 + payloadOffset] || 0;
              const fgG = rawHexPayload[5 + payloadOffset] || 0;
              const fgB = rawHexPayload[6 + payloadOffset] || 0;
              simColor = `#${fgR.toString(16).padStart(2,'0')}${fgG.toString(16).padStart(2,'0')}${fgB.toString(16).padStart(2,'0')}`;
          }
      }
  }

  useEffect(() => {
    animValue.stopAnimation();
    
    if (isPoweredOn && (simMode === 'BUILDER' || simMode === 'STREET' || simMode === 'MULTICOLOR' || simMode === 'FAVORITES' || simMode === 'PROGRAMS' || simMode === 'MUSIC' || simMode === 'MULTIMODE' || simMode === 'CANDLE')) {
      animValue.setValue(0);
      const baseDuration = (simMode === 'MUSIC') ? 800 : (simMode === 'PROGRAMS' ? 2000 : (simMode === 'MULTICOLOR' || simMode === 'BUILDER' ? (simMultiTransition === 2 || builderTransitionType === 3 ? 350 : 1500) : (simMode === 'MULTIMODE' ? 1500 : (simMode === 'CANDLE' ? 400 : (simMode === 'STREET' ? 1400 : 3000)))));
      // Speed 0 = 5x slower, Speed 100 = 0.4x faster
      const duration = baseDuration / (0.4 + (simSpeed / 100) * 2.1); 
      
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
  }, [product, simMode, simColor, simPatternId, simSpeed, isPoweredOn, rawHexPayload, JSON.stringify(simMultiColors), simMultiTransition]);

  // Fallback if no specific devices array is provided: construct one or two devices based on isPaired flag
  const renderDevices = (devices && devices.length > 0) ? devices : (
    isPaired 
      ? [{ name: `${product} Left`, type: product, points }, { name: `${product} Right`, type: product, points }]
      : [{ name: product, type: product, points }]
  );

  return (
    <View style={[styles.container, { backgroundColor: isDark ? '#000000' : '#FFFFFF', borderColor: isDark ? 'rgba(255,255,255,0.12)' : 'rgba(0,0,0,0.08)' }]}>
      <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', flexWrap: 'wrap', paddingTop: 24 }}>
         {renderDevices.map((dev, index) => (
           <VisualizerUnit 
             key={dev.id || index.toString()} 
             device={dev}
             color={simColor}
             mode={simMode}
             patternId={simPatternId}
             animValue={animValue}
             fallbackProduct={product}
             fallbackPoints={points}
             fixedFgColor={fixedFgColor}
             fixedBgColor={fixedBgColor}
             onLongPress={onLongPressDevice}
             brightness={simBrightness}
             isPoweredOn={isPoweredOn}
             audioMagnitude={simAudioMag}
             multiColors={simMultiColors}
             multiTransition={simMultiTransition}
             isStreetBraking={isStreetBraking}
             streetCruiseColor={streetCruiseColor}
             motionState={motionState}
             builderNodes={builderNodes}
             builderFillMode={builderFillMode}
             builderTransitionType={builderTransitionType}
             builderDirection={builderDirection}
           />
         ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 6,
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
  ledDot: {
    width: 16,
    height: 16,
    borderRadius: 8,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 24,
    elevation: 12,
  },
  ledDotSmall: {
    width: 12,
    height: 12,
    borderRadius: 6,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 18,
    elevation: 10,
  }
});

export default React.memo(ProductVisualizer);
