import { useMemo } from 'react';
import { Animated } from 'react-native';
import { getVisualizerFrame, getMusicVisualizerFrame, SK8LYTZ_TEMPLATES, PatternId, RGB } from '../../protocols/PatternEngine';
import { PositionalMathBuffer, BuilderNode } from '../../protocols/PositionalMathBuffer';

export function HSLToHex(h: number, s: number, l: number) {
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

export function useVisualizerPath(productProfile: any, numLeds: number, deviceSegments: number, product: string) {
  return useMemo(() => {
    const vizShape = productProfile.vizShape;
    const numSamples = 5000;
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
  }, [product, productProfile.vizShape, numLeds, deviceSegments, productProfile]);
}

export function useVisualizerLeds(options: any) {
  const { pathGeometry, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude, fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, speed, animTick, streetBrakeState, motionState, streetCruiseColor, builderNodes, builderFillMode, builderTransitionType, builderDirection, vizShape, deviceSegments, productProfile, animValue, fixedDirection, streetDistribution } = options;

  return useMemo(() => {
    const { pathSamples, totalLength } = pathGeometry;
    const list = [];
    let lastSampleIdx = 0;
    const renderLeds = vizShape === 'RING'
      ? Math.max(numLeds * 4, 64)
      : vizShape === 'DUAL_STRIP'
        ? Math.max(numLeds * 2, 60)
        : Math.max(numLeds * 2, 86);

    let hoistedFramePixels: RGB[] | null = null;
    let hoistedMusicFrame: { pixels: RGB[]; opacities: number[] } | null = null;
    let hoistedFgRgb: RGB = { r: 0, g: 0, b: 0 };
    let hoistedBgRgb: RGB = { r: 0, g: 0, b: 0 };
    let hoistedPid = Math.max(1, patternId || 1);

    if (isPoweredOn && (mode === 'STREET' || mode === 'MULTIMODE')) {
      let fgHex = fixedFgColor || color;
      let bgHex = fixedBgColor || '#000000';
      hoistedPid = Math.max(1, patternId || 1);
      if (mode === 'STREET') {
        const isActiveBraking = motionState === 'HARD_BRAKING' || motionState === 'STOPPED' || streetBrakeState === 'BRAKING';
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
      hoistedMusicFrame = getMusicVisualizerFrame(patternId || 1, numLeds, animTick, audioMagnitude, color);
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
      
      let rawFract = (segmentI / activeSegmentLeds); if (vizShape === 'RING' && i >= renderLeds / 2) { rawFract = 1 - rawFract - 0.0001; }

      const fract = Math.floor(rawFract * numLeds) / numLeds;
      const mirroredFract = fract <= 0.5 ? fract * 2 : (1 - fract) * 2;

      let dotColor: string | Animated.AnimatedInterpolation<string> = isPoweredOn ? color : '#333333';
      let dotOpacity: number | Animated.AnimatedInterpolation<number> = isPoweredOn ? 1 : 0.2;

      const segmentBoundary = vizShape === 'RING' ? rawFract : (rawFract * deviceSegments) % 1;
      const isGap = deviceSegments > 1 && (segmentBoundary < 0.015 || segmentBoundary > 0.985);

      if (isPoweredOn && isGap) {
        dotColor = '#111111';
        dotOpacity = Math.max(0.1, (brightness / 100) * 0.3);
      } else if (isPoweredOn) {
        if (mode === 'FAVORITES') {
          const rainbowShift = vizShape === 'RING' ? fract : mirroredFract;
          const rainbowColors = [0, 1 / 6, 2 / 6, 3 / 6, 4 / 6, 5 / 6, 1].map(v => HSLToHex((v - rainbowShift + 1) % 1 * 360, 100, 50));
          dotColor = animValue.interpolate({ inputRange: [0, 0.16, 0.33, 0.5, 0.66, 0.83, 1], outputRange: rainbowColors });
        } else if (mode === 'MUSIC') {
          const musicFrame = hoistedMusicFrame || getMusicVisualizerFrame(patternId || 1, numLeds, animTick, audioMagnitude, color);
          const mRawPos = rawFract * musicFrame.pixels.length;
          const mSlot = Math.floor(mRawPos) % Math.max(1, musicFrame.pixels.length);
          const mPx = musicFrame.pixels[mSlot] || { r: 255, g: 255, b: 255 };
          dotColor = `#${mPx.r.toString(16).padStart(2, '0')}${mPx.g.toString(16).padStart(2, '0')}${mPx.b.toString(16).padStart(2, '0')}`;
          dotOpacity = isPoweredOn ? (musicFrame.opacities[mSlot] ?? 1.0) * (brightness / 100) : 0;
        } else if (mode === 'STREET' || mode === 'MULTIMODE') {
          const fgRgb = hoistedFgRgb;
          const _bgRgb = hoistedBgRgb;
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
          const isStreetMode = pid >= 101 && pid <= 105; const applySymmetry = template?.supportsSegment && deviceSegments > 1 && productProfile.vizIsMirrored && !isStreetMode;
          
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
        } else if (mode === 'BUILDER') {
          let builderPixels: RGB[];
          if (!builderNodes || builderNodes.length === 0) {
            builderPixels = Array.from({ length: activeSegmentLeds }).map(() => ({ r: 255, g: 0, b: 0 }));
          } else {
            builderPixels = PositionalMathBuffer.generateArray(builderNodes as BuilderNode[], activeSegmentLeds, builderFillMode === 'GRADIENT');
          }

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

          const rawLedPos = rawFract * builderPixels.length;
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

          if (builderTransitionType === 0x01) {
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          } else if (builderTransitionType === 0x02) {
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          } else if (builderTransitionType === 0x03) {
            const strobeOn = animTick < 0.5 ? 1 : 0;
            dotOpacity = isPoweredOn ? Math.max(0.01, strobeOn) * (brightness / 100) : 0;
          } else if (builderTransitionType === 0x04) {
            const wave = 0.5 + 0.5 * Math.sin(animTick * Math.PI * 2 + fract * Math.PI * 4);
            dotOpacity = isPoweredOn ? (0.3 + wave * 0.7) * (brightness / 100) : 0;
          } else {
            dotOpacity = isPoweredOn ? (brightness / 100) : 0;
          }
        }
      }

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
  }, [pathGeometry, mode, color, numLeds, patternId, isPoweredOn, audioMagnitude, fixedFgColor, fixedBgColor, multiColors, multiTransition, brightness, speed, animTick, streetBrakeState, motionState, streetCruiseColor, builderNodes, builderFillMode, builderTransitionType, builderDirection, vizShape, deviceSegments, productProfile, animValue, fixedDirection, streetDistribution]);
}
