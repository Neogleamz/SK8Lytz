import React, { useMemo } from 'react';
import { LOCAL_PRODUCT_CATALOG } from '../../../constants/ProductCatalog';
import type { PatternId, RGB } from '../../../protocols/PatternEngine';
import { getVisualizerFrame, SK8LYTZ_TEMPLATES } from '../../../protocols/PatternEngine';

function HSLToHex(h: number, s: number, l: number) {
  h = (h % 360 + 360) % 360; 
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

export interface WebVisualizerUnitProps {
  device?: { type?: string; name?: string; points?: number; segments?: number };
  color: string;
  mode: string;
  patternId?: number | null;
  animTick: number;
  fallbackProduct?: string;
  fallbackPoints?: number;
  hwSettings?: { ledPoints?: number; segments?: number };
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
  builderFillMode?: string;
  builderTransitionType?: number;
  builderDirection?: number;
  fixedDirection?: number;
  streetDistribution?: [number, number, number];
}

export const WebVisualizerUnit = React.memo(({
  device, color, mode, patternId, animTick, fallbackProduct, fallbackPoints, hwSettings,
  fixedFgColor, fixedBgColor, brightness = 100, isPoweredOn = true, audioMagnitude = 0,
  multiColors = [], multiTransition = 0, isStreetBraking = false, streetCruiseColor = '#FF8C00',
  motionState = 'STOPPED', builderNodes = [], builderFillMode = 'GRADIENT',
  builderTransitionType = 1, builderDirection = 1, fixedDirection = 1, streetDistribution = [0.3, 0.4, 0.3]
}: WebVisualizerUnitProps) => {

  const product = (device?.type && device.type !== 'undefined') ? String(device.type) : String(fallbackProduct || 'SOULZ');

  const productProfile = useMemo(() => {
    const byId = LOCAL_PRODUCT_CATALOG.find(p => p.id.toUpperCase() === product.toUpperCase());
    return byId ?? LOCAL_PRODUCT_CATALOG.find(p => p.id === 'SOULZ')!;
  }, [product]);

  const vizShape = productProfile.vizShape;

  const devicePoints = hwSettings?.ledPoints || device?.points || fallbackPoints || productProfile.defaultLedPoints;
  const deviceSegments = hwSettings?.segments || device?.segments || productProfile.defaultSegments;
  const numLeds = Math.floor(devicePoints);

  const pathGeometry = useMemo(() => {
    const numSamples = 2000;
    const pathSamples: { top: number; left: number; length: number }[] = [];
    let totalLength = 0;
    const S = 0.38;

    for (let i = 0; i <= numSamples; i++) {
      let left = 0;
      let top = 0;

      if (vizShape === 'RING') {
        let angle = 0;
        const half = Math.floor(numSamples / 2);
        if (i < half) {
          const fraction = i / Math.max(1, half - 1);
          angle = (Math.PI / 2) - fraction * Math.PI;
        } else {
          const fraction = (i - half) / Math.max(1, (numSamples - half) - 1);
          angle = (Math.PI / 2) + fraction * Math.PI; 
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
        const sep = productProfile.vizStripSeparation ?? 32;
        const half = Math.floor(numSamples / 2);
        const stripHeight = productProfile.vizBaseHeight * S;
        const centreX = (productProfile.vizBaseWidth / 2) * S;
        if (i < half) {
          const fract = i / Math.max(1, half - 1);
          left = centreX - (sep / 2) * S;
          top = fract * stripHeight;
        } else {
          const fract = (i - half) / Math.max(1, (numSamples - half) - 1);
          left = centreX + (sep / 2) * S;
          top = fract * stripHeight;
        }
      } else {
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
        if (dist < 100 * S) {
          totalLength += dist;
        }
      }
      pathSamples.push({ top, left, length: totalLength });
    }
    return { pathSamples, totalLength };
  }, [vizShape, productProfile]);

  const leds = useMemo(() => {
    const { pathSamples, totalLength } = pathGeometry;
    const list = [];
    const renderLeds = vizShape === 'RING'
      ? Math.max(numLeds * 4, 64)
      : vizShape === 'DUAL_STRIP'
        ? Math.max(numLeds * 2, 60)
        : Math.max(numLeds * 2, 86);
    
    let lastSampleIdx = 0;

    let hoistedFramePixels: RGB[] | null = null;
    let hoistedMusicFrame: { pixels: RGB[]; opacities: number[] } | null = null;
    let hoistedFgRgb: RGB = { r: 0, g: 0, b: 0 };
    let hoistedBgRgb: RGB = { r: 0, g: 0, b: 0 };
    let hoistedPid = Math.max(1, patternId || 1);

    if (isPoweredOn && (mode === 'STREET' || mode === 'MULTIMODE' || mode === 'FIXED' || mode === 'GENERATIVE')) {
      let fgHex = fixedFgColor || color;
      let bgHex = fixedBgColor || '#000000';
      if (mode === 'STREET') {
        const isActiveBraking = motionState === 'HARD_BRAKING' || motionState === 'STOPPED' || isStreetBraking;
        const isSlowing = motionState === 'SLOWING_DOWN';
        fgHex = '#FF2200';
        bgHex = isSlowing ? '#FFAA00' : streetCruiseColor;
        if (isActiveBraking || motionState === 'HARD_BRAKING') hoistedPid = 103;
        else if (motionState === 'STOPPED') hoistedPid = 101;
        else if (motionState === 'SLOWING_DOWN') hoistedPid = 104;
        else if (motionState === 'ACCELERATING') hoistedPid = 105;
        else hoistedPid = 102;
      }
      hoistedFgRgb = { r: parseInt(fgHex.slice(1, 3), 16) || 0, g: parseInt(fgHex.slice(3, 5), 16) || 0, b: parseInt(fgHex.slice(5, 7), 16) || 0 };
      hoistedBgRgb = { r: parseInt(bgHex.slice(1, 3), 16) || 0, g: parseInt(bgHex.slice(3, 5), 16) || 0, b: parseInt(bgHex.slice(5, 7), 16) || 0 };
      
      hoistedFramePixels = getVisualizerFrame(
        hoistedPid as PatternId,
        hoistedFgRgb,
        hoistedBgRgb,
        numLeds,
        animTick,
        fixedDirection as 0 | 1,
        mode === 'STREET' ? { distribution: streetDistribution } : undefined
      );
    } else if (isPoweredOn && mode === 'MUSIC') {
      hoistedMusicFrame = { pixels: [], opacities: [] };
    }

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
      
      let rawFract = (segmentI / activeSegmentLeds); 
      if (vizShape === 'RING' && i >= renderLeds / 2) { rawFract = 1 - rawFract - 0.0001; }

      const fract = Math.floor(rawFract * numLeds) / numLeds;
      const mirroredFract = fract <= 0.5 ? fract * 2 : (1 - fract) * 2;

      let dotColor = isPoweredOn ? color : '#333333';
      let dotOpacity = isPoweredOn ? 1 : 0.2;

      const segmentBoundary = vizShape === 'RING' ? rawFract : (rawFract * deviceSegments) % 1;
      const isGap = deviceSegments > 1 && (segmentBoundary < 0.015 || segmentBoundary > 0.985);

      if (isPoweredOn && isGap) {
        dotColor = '#111111';
        dotOpacity = Math.max(0.1, (brightness / 100) * 0.3);
      } else if (isPoweredOn) {
        if (mode === 'FAVORITES') {
          const rainbowShift = vizShape === 'RING' ? fract : mirroredFract;
          // Just manually interpolate based on animTick for Favorites
          const h = (animTick + rainbowShift) % 1 * 360;
          dotColor = HSLToHex(h, 100, 50);
        } else if (mode === 'MUSIC') {
          const musicFrame = hoistedMusicFrame || { pixels: [], opacities: [] };
          const mRawPos = rawFract * musicFrame.pixels.length;
          const mSlot = Math.floor(mRawPos) % Math.max(1, musicFrame.pixels.length);
          const mPx = musicFrame.pixels[mSlot] || { r: 255, g: 255, b: 255 };
          dotColor = `#${mPx.r.toString(16).padStart(2, '0')}${mPx.g.toString(16).padStart(2, '0')}${mPx.b.toString(16).padStart(2, '0')}`;
          dotOpacity = isPoweredOn ? (musicFrame.opacities[mSlot] ?? 1.0) * (brightness / 100) : 0;
        } else if (mode === 'STREET' || mode === 'MULTIMODE' || mode === 'FIXED' || mode === 'GENERATIVE') {
          const fgRgb = hoistedFgRgb;
          const pid = hoistedPid;
          const framePixels = hoistedFramePixels || [];
          
          const ledFract = vizShape === 'RING' ? rawFract : (segmentI / activeSegmentLeds);
          const rawLedPos = ledFract * framePixels.length;
          const slot0 = Math.floor(rawLedPos) % Math.max(1, framePixels.length);
          const slotT = rawLedPos - Math.floor(rawLedPos);
          const DIFF = 0.35;
          const boundaryProx = Math.pow(Math.abs(slotT - 0.5) * 2, 2);
          const blendAmt = DIFF * boundaryProx;
          
          const template = SK8LYTZ_TEMPLATES.find(t => t.id === pid);
          const isStreetMode = pid >= 101 && pid <= 105; 
          const applySymmetry = template?.supportsSegment && deviceSegments > 1 && productProfile.vizIsMirrored && !isStreetMode;
          
          let pSlot0 = slot0;
          if (applySymmetry) {
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
          
          if (mode === 'STREET' && pSlot0 < framePixels.length * streetDistribution[0]) {
            dotOpacity = (pid === 101 || pid === 103) ? 1.0 : 0.5;
          } else {
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          }
        }
      }

      const subLedPos = (rawFract * numLeds) - Math.floor(rawFract * numLeds);
      const chipSoften = 1 - 0.18 * Math.pow(Math.abs(2 * subLedPos - 1), 2);

      list.push({
        key: `led_${i}`,
        top, left,
        activeColor: dotColor,
        activeOpacity: dotOpacity * chipSoften,
        diam: outerDiam
      });
    }
    return list;
  }, [pathGeometry, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude, fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, animTick, isStreetBraking, motionState, streetCruiseColor, builderNodes, builderFillMode, builderTransitionType, builderDirection]);

  // Adjust container width/height dynamically
  const containerW = vizShape === 'RING' ? 60 : vizShape === 'DUAL_STRIP' ? 80 : 55;
  const containerH = vizShape === 'RING' ? 90 : vizShape === 'DUAL_STRIP' ? 120 : 115;

  return (
    <div className="flex flex-col items-center mx-2 py-2">
      <div 
        style={{ 
          width: containerW, 
          height: containerH, 
          position: 'relative', 
          opacity: isPoweredOn ? (brightness === 0 ? 0 : Math.max(0.06, Math.pow(brightness / 100, 1.8))) : 0.15 
        }}
      >
        {vizShape === 'RING' && (
          <div style={{
            position: 'absolute', width: 62, height: 87, borderWidth: 8, borderColor: 'rgba(255,255,255,0.06)',
            borderRadius: 20, top: '50%', marginTop: -43.5, left: '50%', marginLeft: -31
          }} />
        )}
        
        {vizShape === 'DUAL_STRIP' && (
          <>
            <div style={{ position: 'absolute', left: '22%', top: 0, width: 6, height: '100%', borderRadius: 3, backgroundColor: 'rgba(255,255,255,0.04)' }} />
            <div style={{ position: 'absolute', right: '22%', top: 0, width: 6, height: '100%', borderRadius: 3, backgroundColor: 'rgba(255,255,255,0.04)' }} />
          </>
        )}

        {leds.map(led => (
          <div 
            key={led.key} 
            style={{ 
              position: 'absolute', 
              top: led.top, 
              left: led.left, 
              width: led.diam, 
              height: led.diam,
              opacity: led.activeOpacity,
              backgroundColor: led.activeColor,
              borderRadius: '50%',
              // Exact 3-Layer Photorealistic Bloom mapped to box-shadow!
              boxShadow: `
                0 0 ${led.diam * 0.3}px ${led.activeColor},
                0 0 ${led.diam * 1.5}px ${led.activeColor},
                0 0 ${led.diam * 3.5}px ${led.activeColor}
              `
            }}
          >
            {/* Bright center hot-spot */}
            <div style={{
              position: 'absolute',
              top: '34%', left: '34%',
              width: '32%', height: '32%',
              backgroundColor: 'rgba(255,255,255,0.55)',
              borderRadius: '50%'
            }} />
          </div>
        ))}
      </div>
      <div className="mt-4 text-center w-24">
        <span className={`text-[11px] font-bold ${isPoweredOn ? 'text-white opacity-100' : 'text-slate-500 opacity-40'}`}>
          {device?.name || product}
        </span>
      </div>
    </div>
  );
});
