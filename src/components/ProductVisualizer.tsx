import React, { useEffect, useRef, useMemo } from 'react';
import { View, StyleSheet, Animated, Text, TouchableOpacity } from 'react-native';
import { getArchetypeFromId } from '../utils/RbmDictionary';
import { MusicDictionary } from '../utils/MusicDictionary';
import { useTheme } from '../context/ThemeContext';

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

const VisualizerUnit = ({ device, color, mode, patternId, animValue, fallbackProduct, fallbackPoints, onLongPress, fixedFgColor, fixedBgColor, brightness = 100, speed = 50, isPoweredOn = true, audioMagnitude = 0, multiColors = [], multiTransition = 0, rawHexPayload, simMode }: any) => {
  const { isDark } = useTheme();
  const product = String(device.type || fallbackProduct);
  const isHaloz = !product.toLowerCase().includes('soul');

  // HALOZ actual hardware is composed of two segments making a ring of 16 LEDs.
  // SOULZ visualizer uses the pointsPerSide * 2 logic to visually double the strip.
  const devicePoints = device?.points || fallbackPoints || (isHaloz ? 16 : 43);
  const deviceSegments = device?.segments || 1;
  const numLeds = isHaloz ? Math.floor(devicePoints / deviceSegments) : Math.floor(devicePoints / deviceSegments) * 2;

  const leds = useMemo(() => {
    const list = [];
    const numSamples = 5000;
    const pathSamples: any[] = [];
    let totalLength = 0;
    
    // SCALE FACTOR
    const S = 0.55;

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
            const fract = i / numSamples;
            const angle = (fract * 2 * Math.PI) + (Math.PI / 2);
            top = (150 + Math.sin(angle) * 150) * S;
            const verticalPos = Math.sin(angle);
            const pinch = 1 - 0.3 * Math.exp(-Math.pow(verticalPos - 0.1, 2) * 5); 
            left = (70 + (Math.cos(angle) * 70 * pinch)) * S;
        }

        if (i > 0) {
            const prev = pathSamples[i - 1];
            const dx = left - prev.left;
            const dy = top - prev.top;
            totalLength += Math.sqrt(dx*dx + dy*dy);
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

        const segmentI = isHaloz ? (i % (renderLeds / 2)) : i;
        const activeSegmentLeds = isHaloz ? (renderLeds / 2) : renderLeds;
        
        // Raw smooth path interval
        const rawFract = (segmentI / activeSegmentLeds);
        
        // QUANTIZATION: Forces the perfectly smooth line to illuminate in exactly 16 discrete hardware blocks!
        // Instead of lighting sliding across the shape fluidly, it snaps instantly inside the boundaries of the physical LED chip.
        const fract = Math.floor(rawFract * numLeds) / numLeds;
        const mirroredFract = fract <= 0.5 ? fract * 2 : (1 - fract) * 2;
        
        let dotColor: any = isPoweredOn ? color : '#333333';
        let dotOpacity: any = isPoweredOn ? 1 : 0.2;

        if (isPoweredOn) {
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
                   const pointsArr = Array.from({length: maxLenStr + 1}, (_, idx) => idx / maxLenStr);
                   
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
          } else if (mode === 'PRESETS') {
             const rainbowColors = [0, 1/6, 2/6, 3/6, 4/6, 5/6, 1].map(v => HSLToHex((v - mirroredFract + 1) % 1 * 360, 100, 50));
             dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
          } else if (mode === 'RBM') {
             const pid = patternId || 1;
             if (pid === 103) { // Emergency (Bouncing logic)
               const isTop = mirroredFract > 0.8;
               const isBottom = mirroredFract < 0.2;
               if (isTop) {
                 dotColor = '#FFFFFF';
                 dotOpacity = 1.0;
               } else if (isBottom) {
                 dotColor = '#FF0000';
                 dotOpacity = 1.0;
               } else {
                 // Calculate t1 and t2 for the bounce logic to avoid using __getValue()
                 const target = (mirroredFract - 0.2) / 0.6;
                 const t1 = (1 + target) / 2;
                 const t2 = (1 - target) / 2;
                 const rawInputs = [0, Math.max(0, t2-0.08), t2, Math.min(1, t2+0.08), Math.max(0, t1-0.08), t1, Math.min(1, t1+0.08), 1].sort((a,b) => a-b);
                 
                 // React Native strictly enforces perfectly monotonically increasing bounds
                 const safeInputs = [rawInputs[0]];
                 for (let k = 1; k < rawInputs.length; k++) {
                     if (rawInputs[k] > safeInputs[safeInputs.length - 1] + 0.001) {
                         safeInputs.push(rawInputs[k]);
                     }
                 }
                 
                 dotOpacity = animValue.interpolate({
                   inputRange: safeInputs,
                   outputRange: safeInputs.map(v => 
                      (Math.abs(v-t2) < 0.005 || Math.abs(v-t1) < 0.005) ? 1.0 : 0.1
                   )
                 });
                 dotColor = animValue.interpolate({
                   inputRange: safeInputs,
                   outputRange: safeInputs.map(v => 
                      (Math.abs(v-t2) < 0.005 || Math.abs(v-t1) < 0.005) ? '#FFFF00' : '#111111'
                   )
                 });
               }
             } else {
                 const archetype = getArchetypeFromId(pid);
                 const rainbowColors = [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1].map(v => HSLToHex(v * 360, 100, 50));
                 
                 if (archetype === 'MARQUEE') {
                     dotOpacity = animValue.interpolate({
                         inputRange: [0, Math.max(0, mirroredFract - 0.2), mirroredFract, Math.min(1, mirroredFract + 0.2), 1],
                         outputRange: [0.3, 0.3, 1, 0.3, 0.3]
                     });
                     dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
                 } else if (archetype === 'BREATHING') {
                     dotOpacity = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [0.2, 1, 0.2] });
                     dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
                 } else if (archetype === 'STROBE') {
                     dotOpacity = animValue.interpolate({ inputRange: [0, 0.49, 0.5, 0.99, 1], outputRange: [1, 1, 0.1, 0.1, 1] });
                     dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
                 } else if (archetype === 'METEOR') { // Trail mapping
                     dotOpacity = animValue.interpolate({
                         inputRange: [0, Math.max(0, fract - 0.3), fract, Math.min(1, fract + 0.05), 1],
                         outputRange: [0.1, 0.1, 1, 0.1, 0.1]
                     });
                     dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
                 } else { // OUTLIERS fallback
                     dotOpacity = animValue.interpolate({
                         inputRange: [0, Math.max(0, mirroredFract - 0.2), mirroredFract, Math.min(1, mirroredFract + 0.2), 1],
                         outputRange: [0.3, 0.3, 1, 0.3, 0.3]
                     });
                     dotColor = color; // Fallback to base UI explicitly
                 }
             }
          } else if (mode === 'MUSIC') {
             const h = 1 - mirroredFract;
             const colorVal = parseInt(color.replace('#',''), 16) || 0;
             const r = (colorVal >> 16) & 255;
             const g = (colorVal >> 8) & 255;
             const b = colorVal & 255;
             const compHex = '#' + ((255-r)<<16 | (255-g)<<8 | (255-b)).toString(16).padStart(6, '0');
             const mag = 0.1 + (audioMagnitude * 0.9);

             const profile = MusicDictionary[patternId] || MusicDictionary[2];
             const arch = profile.archetype;
             
             if (arch === 'VU_METER') {
                 // Classic volume bar
                 dotOpacity = (fract < audioMagnitude) ? 1.0 : 0.1;
                 dotColor = (fract > 0.8) ? '#FF0000' : (fract > 0.6 ? '#FFFF00' : color);
             } else if (arch === 'PULSE') {
                 // Soft breathing matched to audio amplitudes
                 dotOpacity = (audioMagnitude > 0.5) ? 1.0 : (0.2 + 0.3 * mag);
                 dotColor = color;
             } else if (arch === 'STROBE') {
                 // Hard flashing threshold clipping
                 dotOpacity = (audioMagnitude > 0.7) ? 1.0 : 0.1;
                 dotColor = (audioMagnitude > 0.8 && i % 3 === 0) ? '#FFFFFF' : color;
             } else if (arch === 'WAVE') {
                 // Rainbow colors shifting over the animation loop explicitly bridging audio magnitude
                 const rainbow = [0, 1/6, 2/6, 3/6, 4/6, 5/6, 1].map(v => HSLToHex((v) % 1 * 360, 100, 50));
                 dotOpacity = animValue.interpolate({
                     inputRange: [0, Math.max(0, mirroredFract - 0.2), mirroredFract, Math.min(1, mirroredFract + 0.2), 1],
                     outputRange: [0.1, 0.1, mag, 0.1, 0.1]
                 });
                 dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbow });
             } else {
                 dotOpacity = mag;
                 dotColor = color;
             }
          } else if (mode === 'FIXED') {
             const fg = fixedFgColor || color;
             const bg = fixedBgColor || '#000000';
             const pid = patternId || 1;
             
             if (pid === 1) { // Solid
               dotColor = fg;
               dotOpacity = 1.0;
             } else if (pid === 2) { // Single Dot
               dotColor = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.05), mirroredFract, Math.min(1, mirroredFract + 0.05), 1],
                  outputRange: [bg, bg, fg, bg, bg]
               });
               dotOpacity = 1.0;
             } else if (pid === 3) { // Comet
               dotColor = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.3), mirroredFract, Math.min(1, mirroredFract + 0.05), 1],
                  outputRange: [bg, bg, fg, bg, bg]
               });
               dotOpacity = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.3), mirroredFract, Math.min(1, mirroredFract + 0.05), 1],
                  outputRange: [0.3, 0.3, 1, 0.3, 0.3]
               });
             } else if (pid === 4) { // Dashed
               dotColor = (Math.floor(i / 4) % 2 === 0) ? fg : bg;
               dotOpacity = 1.0;
             } else if (pid === 5) { // Alternating
               dotColor = (Math.floor(i / 2) % 2 === 0) ? fg : bg;
               dotOpacity = 1.0;
             } else if (pid === 6) { // Breath
               dotColor = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [bg, fg, bg] });
               dotOpacity = 1.0;
             } else if (pid === 7) { // Flash
               dotColor = animValue.interpolate({ inputRange: [0, 0.49, 0.5, 0.99, 1], outputRange: [fg, fg, bg, bg, fg] });
               dotOpacity = 1.0;
             } else if (pid === 8) { // Strobe
               dotColor = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [fg, bg, fg] });
               dotOpacity = animValue.interpolate({ inputRange: [0, 0.5, 1], outputRange: [1, 0, 1] });
             } else if (pid === 9) { // Wave
               dotColor = animValue.interpolate({
                  inputRange: [0, Math.max(0, mirroredFract - 0.2), mirroredFract, Math.min(1, mirroredFract + 0.2), 1],
                  outputRange: [bg, bg, fg, bg, bg]
               });
               dotOpacity = 1.0;
             } else if (pid === 10) { // Pinch
               dotColor = animValue.interpolate({
                  inputRange: [0, Math.max(0, (1 - mirroredFract) - 0.2), (1 - mirroredFract), Math.min(1, (1-mirroredFract) + 0.2), 1],
                  outputRange: [bg, bg, fg, bg, bg]
               });
               dotOpacity = 1.0;
             }
          }
        }

        list.push({
           key: `led_${i}`,
           position: { top, left, position: 'absolute' as const },
           activeColor: dotColor,
           activeOpacity: dotOpacity,
        });
    }
    return list;
  }, [product, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude, fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, speed]);

  return (
    <TouchableOpacity 
      activeOpacity={onLongPress ? 0.8 : 1}
      onLongPress={onLongPress ? () => onLongPress(device) : undefined}
      style={{ alignItems: 'center', marginHorizontal: 12, paddingVertical: 4 }}
    >
      <View style={[
        isHaloz ? styles.haloBase : styles.soulBase, 
        { alignSelf: 'center', opacity: isPoweredOn ? (brightness === 0 ? 0 : 0.08 + Math.pow(brightness / 100, 0.6) * 0.92) : 0.2 }
      ]}>
         {/* Physical unlit silicone background track simulation */}
         {isHaloz && (
            <View style={{
               position: 'absolute',
               width: 77 + 16, 
               height: 121 + 16, 
               borderWidth: 10, // Creates a very thin hollow 6mm track
               borderColor: 'rgba(255,255,255,0.06)',
               borderRadius: 24, 
               alignSelf: 'center',
               top: '50%',
               marginTop: -(121 + 16) / 2,
               left: '50%',
               marginLeft: -(77 + 16) / 2
            }}/>
         )}

         {leds.map(led => {
            const diam = isHaloz ? 16 : 12; // Perfectly smooth continuous tube geometry

            return (
               <Animated.View key={led.key} style={[
                  led.position, 
                  { width: diam, height: diam, alignItems: 'center', justifyContent: 'center', overflow: 'visible', zIndex: 10, opacity: led.activeOpacity }
               ]}>
                  {/* CHROMATIC BLOOM TRACK (Retains sharp silicone shape while glowing dynamically) */}
                  <Animated.View style={{
                    position: 'absolute', width: diam * 2.4, height: diam * 2.4, borderRadius: (diam * 2.4)/2,
                    backgroundColor: led.activeColor as any, 
                    opacity: 0.12, 
                  }} />

                  {/* Perfectly Smooth Continuous Silicone Tube */}
                  <Animated.View style={{
                    position: 'absolute', width: '100%', height: '100%', borderRadius: diam/2,
                    backgroundColor: led.activeColor as any, 
                  }} />
                  {/* Frosty Silicone Exterior Glaze */}
                  <View style={{
                    position: 'absolute', width: '100%', height: '100%', borderRadius: diam/2,
                    backgroundColor: 'rgba(255, 255, 255, 0.12)', // Milky texture
                    borderWidth: 0.5, borderColor: 'rgba(255,255,255,0.05)'
                  }} />
               </Animated.View>
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
};
export default function ProductVisualizer({ product, color, mode, patternId, isPaired, points, devices, fixedFgColor, fixedBgColor, onLongPressDevice, brightness = 100, speed = 50, isPoweredOn = true, statusText, audioMagnitude = 0, rawHexPayload, multiColors, multiTransition }: ProductVisualizerProps) {
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
          simMode = mode === 'MULTICOLOR' ? 'MULTICOLOR' : 'FIXED';
          if (mode === 'MULTICOLOR') {
             simPatternId = 1;
             
             // BLE MTU drops payloads > 20 bytes into chunks, so rawHexPayload may literally only be the first 20 bytes.
             // If we already have the complete array from our props Native React State, DO NOT overwrite it with a truncated hex parse!
             if (!multiColors || multiColors.length === 0) {
                 const colors = [];
                 for (let i = 3 + payloadOffset; i < rawHexPayload.length - 6; i += 3) {
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
          simMode = 'RBM';
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
      }
  }

  useEffect(() => {
    animValue.stopAnimation();
    
    if (isPoweredOn && (simMode === 'MULTICOLOR' || simMode === 'PRESETS' || simMode === 'RBM' || simMode === 'MUSIC' || simMode === 'FIXED' || simMode === 'CANDLE')) {
      animValue.setValue(0);
      const baseDuration = (simMode === 'MUSIC') ? 800 : (simMode === 'RBM' ? 2000 : (simMode === 'MULTICOLOR' ? (simMultiTransition === 2 ? 350 : 1500) : (simMode === 'FIXED' ? 1500 : (simMode === 'CANDLE' ? 400 : 3000))));
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
    width: 88, height: 132,
  },
  soulBase: {
    width: 77, height: 165,
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
