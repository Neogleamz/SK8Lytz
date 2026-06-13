// Acknowledged Monolith (S4): This file is a monolith > 30KB (approx. 60.5KB). Component extraction is flagged for future refactoring.
// TODO: extract
import type { RGB, PatternId, PatternOptions } from './PatternEngine';


export function buildNativeBreathe(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = Math.sin(tick * Math.PI) ** 2; // smooth power curve
  return Array(numLEDs).fill(blendRGB(fg, bg, brightness));
}


export function buildNativeSweep(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const pos = Math.floor(t * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < pos ? fg : bg);
}


export function buildNativeCenterOut(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const spread = Math.floor(tick * center);
  return Array.from({ length: numLEDs }, (_, i) => Math.abs(i - center) <= spread ? fg : bg);
}

export function dim(c: RGB, factor: number): RGB {
  return {
    r: Math.round(c.r * factor),
    g: Math.round(c.g * factor),
    b: Math.round(c.b * factor),
  };
}


export function lerpRGB(c1: RGB, c2: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(c1.r + (c2.r - c1.r) * t),
    g: Math.round(c1.g + (c2.g - c1.g) * t),
    b: Math.round(c1.b + (c2.b - c1.b) * t),
  };
}


export function hueToRGB(hue: number): RGB {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return { r: Math.round(f(5) * 255), g: Math.round(f(3) * 255), b: Math.round(f(1) * 255) };
}


export function blendRGB(a: RGB, b: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(a.r * t + b.r * (1 - t)),
    g: Math.round(a.g * t + b.g * (1 - t)),
    b: Math.round(a.b * t + b.b * (1 - t)),
  };
}


export function hsvToRgb(h: number, s: number, v: number): RGB {
  const i = Math.floor(h * 6);
  const f = h * 6 - i;
  const p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
  const [r, g, b] = [
    [v, q, p, p, t, v], [t, v, v, q, p, p], [p, p, t, v, v, q]
  ].map(ch => ch[i % 6]);
  return { r: Math.round(r * 255), g: Math.round(g * 255), b: Math.round(b * 255) };
}

// ─── GROUP 7 BUILDERS (ge.* HARDWARE-NATIVE EFFECTS) ────────────────────────────


export function buildColorFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phaseOffset = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phaseOffset) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}


export function buildColorBreathing(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = (Math.sin(tick * Math.PI * 2) * 0.5 + 0.5);
  const color = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  return Array(numLEDs).fill(color);
}


export function buildColorJump(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const color = tick < 0.5 ? fg : bg;
  return Array(numLEDs).fill(color);
}


export function buildRunningWater(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const BLOCK = 3;
  const offset = Math.floor((direction === 0 ? tick : 1 - tick) * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i + offset) % numLEDs;
    return pos < BLOCK ? fg : bg;
  });
}


export function buildStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const on = (tick * 10) % 1 < 0.3;
  const color = on ? fg : { r: 0, g: 0, b: 0 };
  return Array(numLEDs).fill(color);
}


export function buildColorWipe(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const fillCount = Math.floor(t * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}


export function buildFireworks(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const seed = Math.floor(tick * 8);
  const frame = Array(numLEDs).fill(bg);
  const center = Math.abs(Math.sin(seed * 1.618) * numLEDs) | 0;
  const spreadRadius = Math.floor((tick * 8 % 1) * (numLEDs / 4));
  for (let i = 0; i < spreadRadius; i++) {
    const brightness = 1 - (i / spreadRadius);
    const c = blendRGB(fg, bg, brightness);
    const posL = Math.max(0, center - i);
    const posR = Math.min(numLEDs - 1, center + i);
    if (posL >= 0) frame[posL] = c;
    if (posR < numLEDs) frame[posR] = c;
  }
  return frame;
}


export function buildOceanWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave1 = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 2);
    const wave2 = Math.sin(i / numLEDs * Math.PI * 6 + phase * Math.PI * 3 + 0.7);
    const combined = (wave1 + wave2) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}


export function buildLightning(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const flashSeed = Math.floor(tick * 20);
  const frame: RGB[] = Array(numLEDs).fill({ r: 0, g: 0, b: 0 });
  for (let j = 0; j < 3; j++) {
    const pos = Math.abs(Math.sin((flashSeed + j) * 2.718) * numLEDs) | 0;
    const brightness = Math.abs(Math.cos((flashSeed + j) * 1.414));
    frame[pos] = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  }
  return frame;
}


export function buildSnowfall(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  const frame = Array(numLEDs).fill(bg);
  for (let j = 0; j < 5; j++) {
    const speed = 0.3 + j * 0.15;
    const pos = Math.floor((((phase * speed + j * 0.2) % 1) + 1) % 1 * numLEDs);
    const brightness = 0.6 + Math.sin(phase * 5 + j) * 0.4;
    frame[pos] = blendRGB(fg, bg, brightness);
    if (pos + 1 < numLEDs) frame[pos + 1] = blendRGB(fg, bg, brightness * 0.3);
  }
  return frame;
}


export function buildCandle(fg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(tick * 7 + i * 1.3) * Math.cos(tick * 11 + i * 0.7);
    const brightness = 0.5 + noise * 0.3 + Math.sin(tick * 3) * 0.2;
    const b = Math.max(0, Math.min(1, brightness));
    return { r: Math.round(fg.r * b), g: Math.round(fg.g * b * 0.7), b: Math.round(fg.b * b * 0.3) };
  });
}


export function buildHeartbeat(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const t = tick % 1;
  let brightness: number;
  if (t < 0.1) brightness = Math.sin((t / 0.1) * Math.PI);
  else if (t < 0.3) brightness = Math.sin(((t - 0.15) / 0.15) * Math.PI) * 0.6;
  else brightness = 0;
  const b = Math.max(0, brightness);
  return Array(numLEDs).fill({ r: Math.round(fg.r * b), g: Math.round(fg.g * b), b: Math.round(fg.b * b) });
}


export function buildMeteor(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.25);
  const head = Math.floor((direction === 0 ? tick : 1 - tick) * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist > TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 2));
  });
}


export function buildAurora(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = (i / numLEDs * 0.4 + phase * 0.5 + 0.5) % 1;
    const brightness = (Math.sin(i / numLEDs * Math.PI * 3 + phase * Math.PI * 4) * 0.4 + 0.6);
    return hsvToRgb(hue, 0.8, brightness);
  });
}


export function buildLava(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const blob1 = Math.sin(i / numLEDs * Math.PI * 2 + phase * Math.PI * 1.3);
    const blob2 = Math.sin(i / numLEDs * Math.PI * 3.7 + phase * Math.PI * 0.8 + 1.2);
    const combined = (blob1 + blob2) * 0.5 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}


export function buildPlasma(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const v = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 6) * 
              Math.cos(i / numLEDs * Math.PI * 2 - phase * Math.PI * 2);
    return blendRGB(fg, bg, Math.abs(v));
  });
}


export function buildStarCluster(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  const frame = Array(numLEDs).fill(bg);
  for (let j = 0; j < 4; j++) {
    const center = Math.floor((((phase * 0.5 + j * 0.25) % 1) + 1) % 1 * numLEDs);
    for (let i = -2; i <= 2; i++) {
      const pos = center + i;
      if (pos >= 0 && pos < numLEDs) {
        const brightness = 1 - Math.abs(i) / 3;
        frame[pos] = blendRGB(fg, frame[pos], brightness);
      }
    }
  }
  return frame;
}


export function buildPoliceLights(numLEDs: number, tick: number): RGB[] {
  const red = { r: 255, g: 0, b: 0 };
  const blue = { r: 0, g: 0, b: 255 };
  const flashPhase = (tick * 8) % 1;
  const isOn = flashPhase < 0.5;
  const isRedTurn = (tick * 2) % 1 < 0.5;
  
  return Array.from({ length: numLEDs }, (_, i) => {
    if (!isOn) return { r: 0, g: 0, b: 0 };
    const isLeftHalf = i < numLEDs / 2;
    if (isRedTurn) return isLeftHalf ? red : { r: 0, g: 0, b: 0 };
    return !isLeftHalf ? blue : { r: 0, g: 0, b: 0 };
  });
}


export function buildRainbowBreathing(numLEDs: number, tick: number): RGB[] {
  const hue = tick % 1;
  const brightness = (Math.sin(tick * Math.PI * 4) * 0.5 + 0.5);
  const baseColor = hsvToRgb(hue, 1.0, 1.0);
  return Array(numLEDs).fill({
    r: Math.round(baseColor.r * brightness),
    g: Math.round(baseColor.g * brightness),
    b: Math.round(baseColor.b * brightness)
  });
}


export function buildColorBurst(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const burstPhase = tick % 1;
  const center = Math.floor(numLEDs / 2);
  const radius = Math.floor(burstPhase * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = Math.abs(i - center);
    if (dist < radius && dist > radius - 3) return fg;
    return bg;
  });
}


export function buildTwinkle(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let i = 0; i < numLEDs; i++) {
    const noise = Math.sin(i * 12.9898 + tick * 10) * 43758.5453;
    const val = noise - Math.floor(noise);
    if (val > 0.95) {
       const brightness = Math.sin(tick * 20 + i) * 0.5 + 0.5;
       frame[i] = blendRGB(fg, bg, brightness);
    }
  }
  return frame;
}


export function buildCrystalShimmer(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(i * 7.1 + phase * 15) * 0.5 + 0.5;
    const brightness = Math.pow(noise, 3);
    return {
      r: Math.round(150 * brightness),
      g: Math.round(200 * brightness),
      b: Math.round(255 * brightness)
    };
  });
}


export function buildGradientChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i / numLEDs + phase) % 1;
    return blendRGB(fg, bg, Math.sin(pos * Math.PI));
  });
}


export function buildCometDuo(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.2);
  const head1 = Math.floor(tick * (numLEDs + TAIL)) - TAIL;
  const head2 = Math.floor((1 - tick) * (numLEDs + TAIL)) - TAIL;
  
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist1 = head1 - i;
    const dist2 = i - head2;
    
    let c = bg;
    if (dist1 >= 0 && dist1 <= TAIL) {
      c = blendRGB(fg, c, 1 - (dist1 / TAIL));
    }
    if (dist2 >= 0 && dist2 <= TAIL) {
      c = blendRGB(fg, c, 1 - (dist2 / TAIL)); 
    }
    return c;
  });
}


export function buildFireFlame(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise1 = Math.sin(i * 0.5 + phase * 15);
    const noise2 = Math.sin(i * 1.3 - phase * 22 + 4.5);
    const noise3 = Math.sin(i * 3.7 + phase * 33);
    const flicker = (noise1 + noise2 + noise3) / 3 * 0.5 + 0.5;
    const heightGradient = 1 - (i / numLEDs); 
    const brightness = flicker * heightGradient * 1.5;
    return blendRGB(fg, bg, Math.min(1, brightness));
  });
}


export function buildCyberGlitch(numLEDs: number, tick: number): RGB[] {
  const c1 = { r: 0, g: 255, b: 255 };
  const c2 = { r: 255, g: 0, b: 255 };
  const frame = Array(numLEDs).fill({ r: 0, g: 0, b: 0 });
  const glitchBlockSize = Math.floor(Math.sin(tick * 50) * 4) + 1;
  const glitchPos = Math.floor(Math.sin(tick * 80) * numLEDs + numLEDs) % numLEDs;
  for (let i = 0; i < glitchBlockSize; i++) {
    if (glitchPos + i < numLEDs) {
      frame[glitchPos + i] = Math.random() > 0.5 ? c1 : c2;
    }
  }
  return frame;
}


export function buildNeonPulse(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  let pulse = Math.pow(Math.sin(tick * Math.PI * 2), 4);
  if (tick % 1 < 0.1) pulse = Math.random() * 0.5 + 0.5;
  return Array(numLEDs).fill(blendRGB(fg, bg, pulse));
}


export function buildRainbowChaser(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) * 3 + phase * 2) % 1.0;
    const brightness = Math.sin(i * 0.5 + phase * 10) * 0.5 + 0.5;
    const c = hsvToRgb(hue, 1.0, 1.0);
    return dim(c, brightness);
  });
}


export function buildMatrixRain(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  const frame = Array(numLEDs).fill(bg);
  const tailLength = Math.max(3, Math.floor(numLEDs / 4));
  for (let d = 0; d < 3; d++) {
    const dropPhase = (phase + d * 0.33) % 1;
    const head = Math.floor(dropPhase * (numLEDs + tailLength)) - tailLength;
    for (let i = 0; i <= tailLength; i++) {
      const pos = head + i;
      if (pos >= 0 && pos < numLEDs) {
        const brightness = i / tailLength;
        frame[pos] = blendRGB(fg, frame[pos], Math.pow(brightness, 2));
      }
    }
  }
  return frame;
}


export function buildSparkleFade(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let i = 0; i < numLEDs; i++) {
    const noise = Math.sin(i * 45.2 + tick * 2.1) * 43758.5453;
    const rand = noise - Math.floor(noise);
    if (rand > 0.98) frame[i] = fg;
    else if (rand > 0.90) frame[i] = blendRGB(fg, bg, 0.5);
  }
  return frame;
}


export function buildDualScan(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const phase = (Math.sin(tick * Math.PI * 2) * 0.5 + 0.5);
  const pos1 = Math.floor(phase * (numLEDs - 1));
  const pos2 = numLEDs - 1 - pos1;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist1 = Math.abs(i - pos1);
    const dist2 = Math.abs(i - pos2);
    if (dist1 === 0 || dist2 === 0) return fg;
    if (dist1 === 1 || dist2 === 1) return blendRGB(fg, bg, 0.4);
    return bg;
  });
}


export function buildStarlight(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const twinkle = Math.sin(phase * 5 + i * 2.3) * Math.sin(phase * 2.1 + i * 1.1);
    const brightness = Math.max(0, twinkle);
    return blendRGB(fg, bg, Math.pow(brightness, 3));
  });
}


export function buildHyperspace(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  const center = Math.floor(numLEDs / 2);
  const speed = 4;
  const phase = (tick * speed) % 1;
  const dist = Math.floor(Math.pow(phase, 2) * center); 
  const p1 = center - dist;
  const p2 = center + dist;
  if (p1 >= 0 && p1 < numLEDs) frame[p1] = fg;
  if (p2 >= 0 && p2 < numLEDs) frame[p2] = fg;
  if (p1 + 1 < center) frame[p1 + 1] = blendRGB(fg, bg, 0.3);
  if (p2 - 1 > center) frame[p2 - 1] = blendRGB(fg, bg, 0.3);
  return frame;
}




// ─── GROUP 1–6 BUILDERS ──────────────────────────────────────────────────────────

// GROUP A: Static Placement

export function buildSolid(fg: RGB, numLEDs: number): RGB[] {
  return Array(numLEDs).fill(fg);
}


export function buildSplitColors(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const half = Math.floor(numLEDs / 2);
  return [
    ...Array(half).fill(fg),
    ...Array(numLEDs - half).fill(bg),
  ];
}


export function buildTrisection(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const third = Math.floor(numLEDs / 3);
  return [
    ...Array(third).fill(fg),
    ...Array(third).fill(bg),
    ...Array(numLEDs - third * 2).fill(fg),
  ];
}


export function buildQuartered(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const quarter = Math.floor(numLEDs / 4);
  return [
    ...Array(quarter).fill(fg),
    ...Array(quarter).fill(bg),
    ...Array(quarter).fill(fg),
    ...Array(numLEDs - quarter * 3).fill(bg),
  ];
}


export function buildCenterAccent(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const accentSize = Math.max(1, Math.floor(numLEDs * 0.25));
  const sideSize = Math.floor((numLEDs - accentSize) / 2);
  return [
    ...Array(sideSize).fill(bg),
    ...Array(accentSize).fill(fg),
    ...Array(numLEDs - sideSize - accentSize).fill(bg),
  ];
}

// GROUP B: Chases & Meteors

export function buildSingleDotChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const pos = Math.floor(t * numLEDs) % numLEDs;
  return Array.from({ length: numLEDs }, (_, i) => i === pos ? fg : bg);
}


export function buildTwinDotChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  // Double dots spaced evenly moving in the same direction (hardware compatible circular shift)
  const t = direction === 0 ? tick : 1 - tick;
  const posA = Math.floor(t * numLEDs) % numLEDs;
  const posB = (posA + Math.floor(numLEDs / 2)) % numLEDs;
  return Array.from({ length: numLEDs }, (_, i) =>
    (i === posA || i === posB) ? fg : bg
  );
}


export function buildCometChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.max(3, Math.floor(numLEDs * 0.2));
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 1.5));
  });
}


export function buildMeteorShower(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const METEOR_COUNT = 3;
  const TAIL = Math.floor(numLEDs * 0.15);
  const frame = Array(numLEDs).fill(bg);
  for (let m = 0; m < METEOR_COUNT; m++) {
    const offset = (m / METEOR_COUNT); // stagger start
    const t = direction === 0 ? (tick + offset) % 1 : 1 - ((tick + offset) % 1);
    const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
    for (let i = 0; i < numLEDs; i++) {
      const dist = head - i;
      if (dist >= 0 && dist < TAIL) {
        const brightness = 1 - (dist / TAIL);
        frame[i] = blendRGB(fg, frame[i], Math.pow(brightness, 2));
      }
    }
  }
  return frame;
}

// GROUP C: Marquees & Bands

export function buildMicroAnts(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * 2) % 2; // alternates every half-tick
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % 2 === 0 ? fg : bg);
}


export function buildTheaterChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // every 3rd LED lights
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD === 0 ? fg : bg);
}


export function buildDashedMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const DASH = 4; const GAP = 3; // 4 on, 3 off
  const PERIOD = DASH + GAP;
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD < DASH ? fg : bg);
}


export function buildBarberPole(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(2, Math.floor(numLEDs / 6));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2);
  return Array.from({ length: numLEDs }, (_, i) => {
    // Diagonal stripe: position + offset mod period
    return (i + offset) % (STRIPE * 2) < STRIPE ? fg : bg;
  });
}


export function buildBoldStripes(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(3, Math.floor(numLEDs / 5));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2); // scroll one full period
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.floor((i + offset) / STRIPE) % 2 === 0 ? fg : bg
  );
}

// ---------------------------------------------
// Group D: Math Waves (IDs 15-19)
// ---------------------------------------------


export function buildSinePulseWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 4 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}


export function buildWavePinch(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? 1 - tick : tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / (numLEDs - 1);
    const waveL = Math.sin(posNorm * Math.PI * 3 + phase * Math.PI * 2);
    const waveR = Math.sin((1 - posNorm) * Math.PI * 3 + phase * Math.PI * 2);
    const combined = (waveL + waveR) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}


export function buildBreathingWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 2 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}


export function buildCenterOutComet(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const TAIL = Math.floor(numLEDs * 0.15);
  const head = Math.floor(tick * (center + TAIL));
  return Array.from({ length: numLEDs }, (_, i) => {
    // Mirror: two comets going from center to edges
    const distL = head - (center - i);
    const distR = head - (i - center);
    const dist = Math.min(Math.abs(distL), Math.abs(distR));
    if (i < center) {
      return (head - (center - i) >= 0 && head - (center - i) < TAIL)
        ? blendRGB(fg, bg, 1 - dist / TAIL)
        : bg;
    } else {
      return (head - (i - center) >= 0 && head - (i - center) < TAIL)
        ? blendRGB(fg, bg, 1 - dist / TAIL)
        : bg;
    }
  });
}


export function buildCenterOutMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const PERIOD = 6;
  const offset = Math.floor(tick * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    const distFromCenter = Math.abs(i - center);
    return (distFromCenter + offset) % PERIOD < 3 ? fg : bg;
  });
}

// ---------------------------------------------
// Group E: Temporal (IDs 20-24)
// ---------------------------------------------


export function buildSmoothBreath(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = Math.sin(tick * Math.PI) ** 2; // smooth power curve
  return Array(numLEDs).fill({
    r: Math.round(fg.r * brightness),
    g: Math.round(fg.g * brightness),
    b: Math.round(fg.b * brightness),
  });
}


export function buildHardJumpFlash(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Hard binary alternation - no crossfade
  return Array(numLEDs).fill(tick < 0.5 ? fg : bg);
}


export function buildTemporalStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // High-frequency flash (10x per cycle)
  const on = (tick * 10) % 1 < 0.2; // 20% duty cycle = sharp strobe
  return Array(numLEDs).fill(on ? fg : { r: 0, g: 0, b: 0 });
}


export function buildWipeFill(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  // Fill from one end, then wipe back - forward half fills, back half wipes
  const progress = t < 0.5 ? t * 2 : (1 - t) * 2;
  const fillCount = Math.floor(progress * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}


export function buildWipeCenterOut(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  // progress from 0 to 1 back to 0
  const progress = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  const radius = Math.floor(progress * center);
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.abs(i - center) <= radius ? fg : bg
  );
}

// ---------------------------------------------
// Group F: Generative / Rainbow (IDs 25-28)
// ---------------------------------------------


export function buildTrueRainbowFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phase) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}


export function buildRainbowMarquee(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // grouped blocks with rainbow hue
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    if ((i + offset) % PERIOD !== 0) return { r: 0, g: 0, b: 0 }; // gap
    const hue = (i / numLEDs + t) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}


export function buildRainbowComet(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.3);
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return { r: 0, g: 0, b: 0 };
    const brightness = 1 - (dist / TAIL);
    const hue = (i / numLEDs + t) % 1.0; // rainbow position along strip
    return hsvToRgb(hue, 1.0, brightness);
  });
}


export function buildCyberpunkShift(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / numLEDs;
    // Sharp digital glitch band scrolling
    const band = (posNorm + phase * 2) % 1;
    if (band < 0.08) return fg; // bright FG slice
    if (band < 0.12) return { r: 255, g: 255, b: 255 }; // white edge flash
    // Sub-bands of bg with slight brightness modulation
    const bgBrightness = 0.3 + Math.sin(posNorm * Math.PI * 8 + phase * Math.PI * 4) * 0.2;
    return blendRGB(bg, { r: 0, g: 0, b: 0 }, bgBrightness);
  });
}

// ─── GROUP 9 BUILDERS (NATIVE TEMPORAL VISUALIZERS) ─────────────────────────

export function buildStreetMode(patternId: PatternId, fg: RGB, bg: RGB, n: number, tick: number, options?: PatternOptions): RGB[] {
  const segments = Math.max(1, options?.segments || 1);
  const physicalLength = n * segments;

  const dist = options?.distribution || [0.3, 0.4, 0.3];
  const tailCount = Math.max(1, Math.round(n * dist[0]));
  const headCount = Math.max(1, Math.round(n * dist[2]));
  const midCount = Math.max(1, n - tailCount - headCount);
  
  const tailColor = fg; // Brake/Tail
  const cruiseColor = bg; // Cruise dot
  const headColor = { r: 255, g: 255, b: 255 }; // Headlights
  
  const rightSide: RGB[] = [];

  if (patternId === 101 || patternId === 104) {
    // STOPPED (101) or SLOWING DOWN (104)
    const dimBrake = { r: Math.round(tailColor.r * 0.5), g: Math.round(tailColor.g * 0.5), b: Math.round(tailColor.b * 0.5) };
    const tailPixels = Array(tailCount).fill(patternId === 104 ? dimBrake : tailColor);
    const midColor = patternId === 104 ? { r: 255, g: 100, b: 0 } : { r: 0, g: 0, b: 0 };
    const midPixels = Array(midCount).fill(midColor);
    const headPixels = Array(headCount).fill(headColor);
    rightSide.push(...tailPixels, ...midPixels, ...headPixels);
    
  } else if (patternId === 103) {
    // HARD BRAKING (103)
    const tailPixels = Array(tailCount).fill(tailColor);
    const midPixels = Array(midCount).fill(tailColor);
    const headPixels = Array(headCount).fill(headColor);
    rightSide.push(...tailPixels, ...midPixels, ...headPixels);
    
  } else {
    // CRUISING (102) or ACCELERATING (105)
    const dimBrake = { r: Math.round(tailColor.r * 0.5), g: Math.round(tailColor.g * 0.5), b: Math.round(tailColor.b * 0.5) };
    const tailPixels = Array(tailCount).fill(dimBrake);
    const headPixels = Array(headCount).fill(headColor);
    
    // Bouncing dot in the gap using tick (0 to 2)
    const bouncePhase = tick <= 1 ? tick : 2 - tick; // 0 to 1 to 0
    const dotPos = Math.round(bouncePhase * (midCount - 1));
    
    const dimCruise = { r: Math.round(cruiseColor.r * 0.2), g: Math.round(cruiseColor.g * 0.2), b: Math.round(cruiseColor.b * 0.2) };
    const midPixels = Array.from({ length: midCount }, (_, i) => {
      const d = Math.abs(i - dotPos);
      if (d === 0) return cruiseColor;
      if (d === 1) return { r: Math.round(cruiseColor.r * 0.6), g: Math.round(cruiseColor.g * 0.6), b: Math.round(cruiseColor.b * 0.6) };
      return dimCruise;
    });
    
    rightSide.push(...tailPixels, ...midPixels, ...headPixels);
  }

  // Palindrome bypass logic: If segments > 1 (e.g. HALOZ ring), we build the full physical array
  // by appending the reversed left side. If segments === 1 (e.g. SOULZ Y-split), we just return rightSide.
  if (segments === 1) {
    return rightSide;
  }

  const leftSide = [...rightSide].reverse();
  const fullPayload: RGB[] = [];
  for (let i = 0; i < segments; i++) {
    if (i % 2 === 0) fullPayload.push(...rightSide);
    else fullPayload.push(...leftSide);
  }
  return fullPayload.slice(0, physicalLength);
}


export function generateArray(patternId: PatternId, fg: RGB, bg: RGB, n: number, tick: number = 0, direction: 0 | 1 = 1, options?: PatternOptions): RGB[] {
  const _arr: RGB[] = Array(n).fill(bg);

  switch (patternId) {
    // ── GROUP 9: NATIVE TEMPORAL ──
    case 70: return buildNativeBreathe(fg, bg, n, tick);
    case 71: return buildNativeSweep(fg, bg, n, tick, direction);
    case 72: return buildNativeCenterOut(fg, bg, n, tick);

    // ── GROUP 1: SOLID & STATIC ──
    case 1: return buildSolid(fg, n);
    case 2: return buildSplitColors(fg, bg, n);
    case 3: return buildTrisection(fg, bg, n);
    case 4: return buildQuartered(fg, bg, n);
    case 5: return buildCenterAccent(fg, bg, n);

    // ── GROUP 2: CHASES & METEORS ──
    case 6: return buildSingleDotChase(fg, bg, n, tick, direction);
    case 7: return buildTwinDotChase(fg, bg, n, tick, direction);
    case 8: return buildCometChase(fg, bg, n, tick, direction);
    case 9: return buildMeteorShower(fg, bg, n, tick, direction);

    // ── GROUP 3: MARQUEES & BANDS ──
    case 10: return buildMicroAnts(fg, bg, n, tick, direction);
    case 11: return buildTheaterChase(fg, bg, n, tick, direction);
    case 12: return buildDashedMarquee(fg, bg, n, tick, direction);
    case 13: return buildBoldStripes(fg, bg, n, tick, direction);     // ID 13 = BoldStripes (BarberPole was removed)

    // ── GROUP 4: MATH WAVES ──
    case 14: return buildSinePulseWave(fg, bg, n, tick, direction);   // was 15
    case 15: return buildWavePinch(fg, bg, n, tick, direction);                  // was 16
    case 16: return buildBreathingWave(fg, bg, n, tick, direction);   // was 17

    // ── GROUP 5a: TEMPORAL FULL-STRIP (0x51) ──
    case 17: return buildSmoothBreath(fg, n, tick);                   // was 20

    // ── GROUP 5b: WIPE / FILL ──
    case 18: return buildWipeFill(fg, bg, n, tick, direction);        // was 23

    // ── GROUP 6: GENERATIVE RAINBOWS ──
    case 19: return buildTrueRainbowFlow(n, tick, direction);         // was 25
    case 20: return buildRainbowMarquee(n, tick, direction);          // was 26
    case 21: return buildRainbowComet(n, tick, direction);            // was 27
    case 22: return buildCyberpunkShift(fg, bg, n, tick, direction);  // was 28

    // ── GROUP 7: ge.* PHASE 1A REVERSALS ──
    case 23: return buildColorFlow(n, tick, direction);               // ge.ColorFlowEffect — was 29
    case 24: return buildColorBreathing(fg, n, tick);                 // ge.BreathEffect    — was 30
    case 25: return buildRunningWater(fg, bg, n, tick, direction);    // ge.RunningWaterEffect — was 32
    case 26: return buildStrobe(fg, n, tick);                         // ge.StrobeEffect    — was 33
    case 27: return buildOceanWave(fg, bg, n, tick, direction);       // ge.OceanWaveEffect — was 36
    case 28: return buildLightning(fg, n, tick);                      // ge.LightningEffect — was 37
    case 29: return buildSnowfall(fg, bg, n, tick, direction);                   // ge.SnowfallEffect  — was 38
    case 30: return buildHeartbeat(fg, n, tick);                      // ge.HeartbeatEffect — was 40
    case 31: return buildMeteor(fg, bg, n, tick, direction);          // ge.MeteorEffect    — was 41
    case 32: return buildAurora(n, tick, direction);                  // ge.AuroraEffect    — was 42
    case 33: return buildLava(fg, bg, n, tick, direction);                       // ge.LavaEffect      — was 43
    case 34: return buildPlasma(fg, bg, n, tick, direction);          // ge.PlasmaEffect    — was 44
    case 35: return buildStarCluster(fg, bg, n, tick, direction);               // ge.StarClusterEffect — was 45
    case 36: return buildRainbowBreathing(n, tick);                   // was 47
    case 37: return buildCrystalShimmer(n, tick, direction);                    // was 50
    case 38: return buildGradientChase(fg, bg, n, tick, direction);   // was 51
    case 39: return buildFireFlame(fg, bg, n, tick, direction);                  // was 53
    case 40: return buildNeonPulse(fg, bg, n, tick);                  // was 55
    case 41: return buildRainbowChaser(n, tick, direction);           // was 56
    case 42: return buildMatrixRain(fg, bg, n, tick, direction);      // was 57
    case 43: return buildStarlight(fg, bg, n, tick, direction);                  // was 60

    // ── 0x41 Hardware Parity (Modes 201-233) ──
    case 201: {
      // Large FG chunk scrolling back to front (~50% width) | FG only (BG is hardcoded White)
      const offset = Math.floor(tick * n);
      const chunk = Math.max(1, Math.floor(n / 2));
      return Array.from({ length: n }, (_, i) => {
        const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
        return pos < chunk ? fg : { r: 255, g: 255, b: 255 };
      });
    }
    case 202: {
      // Scrolling chunk (FG tips, BG center) over Black background
      const offset = Math.floor(tick * n);
      const chunk = Math.max(1, Math.floor(n / 2));
      return Array.from({ length: n }, (_, i) => {
        const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
        if (pos >= chunk) return { r: 0, g: 0, b: 0 }; 
        const t = Math.abs((pos / (chunk - 1)) * 2 - 1); 
        return blendRGB(fg, bg, t);
      });
    }
    case 203: {
      return buildSingleDotChase(fg, bg, n, tick, direction);
    }
    case 204: {
      // Alternating Bounce Fill (FG fills Fwd, BG fills Rev)
      const isFwd = tick < 0.5;
      const fillLevel = isFwd ? tick * 2 : (1 - tick) * 2;
      const cutoff = Math.floor(fillLevel * n);
      return Array.from({ length: n }, (_, i) => {
        if (isFwd) return i <= cutoff ? fg : bg;
        return i >= n - 1 - cutoff ? bg : fg;
      });
    }
    case 205: {
      // Single FG dot bouncing back and forth over solid BG
      const maxPos = n - 1;
      const pos = Math.floor((tick < 0.5 ? tick * 2 : (1 - tick) * 2) * maxPos);
      return Array.from({ length: n }, (_, i) => (i === pos ? fg : bg));
    }
    case 206: {
      // Marching Ants (Alternating FG/BG pixels flowing forward)
      const offset = Math.floor(tick * n * 2); 
      return Array.from({ length: n }, (_, i) => ((i + offset) % 2 === 0 ? fg : bg));
    }
    case 207: {
      // Breathing (Fade In/Out) | FG only
      return buildNativeBreathe(fg, { r: 0, g: 0, b: 0 }, n, tick);
    }
    case 208: {
      // 3-Color RGB Breathing Cycle (R -> G -> B)
      const colors = [{r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255}];
      const c = colors[Math.floor(tick * 3) % 3];
      const subTick = (tick * 3) % 1;
      const brightness = Math.sin(subTick * Math.PI) ** 2;
      return Array(n).fill({ r: Math.round(c.r * brightness), g: Math.round(c.g * brightness), b: Math.round(c.b * brightness) });
    }
    case 209: {
      return buildRainbowBreathing(n, tick);
    }
    case 210: {
      // 3-Color RGB Strobe (Jump)
      const colors = [{r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255}];
      const phase = (tick * 6) % 1; 
      const isOn = phase < 0.5;
      const c = colors[Math.floor(tick * 3) % 3];
      return Array(n).fill(isOn ? c : {r:0,g:0,b:0});
    }
    case 211: {
      // 7-Color Breathing Cycle
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const c = colors[Math.floor(tick * 7) % 7];
      const subTick = (tick * 7) % 1;
      const brightness = Math.sin(subTick * Math.PI) ** 2;
      return Array(n).fill({ r: Math.round(c.r * brightness), g: Math.round(c.g * brightness), b: Math.round(c.b * brightness) });
    }
    case 212: {
      // 7-Color Crossfade
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const idx = Math.floor(tick * 7) % 7;
      const nextIdx = (idx + 1) % 7;
      const t = (tick * 7) % 1;
      return Array(n).fill(blendRGB(colors[nextIdx], colors[idx], t)); 
    }
    case 213: {
      // 7-Color Hard Jump
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      return Array(n).fill(colors[Math.floor(tick * 7) % 7]);
    }
    case 214: {
      // Irregular Flashing Strobe (Jumps between colors) | FG + BG
      const isFlash = (Math.sin(tick * 50) * 43758) % 1 > 0.5;
      const isBg = (Math.cos(tick * 30) * 12345) % 1 > 0.5;
      return Array(n).fill(isFlash ? (isBg ? bg : fg) : {r:0,g:0,b:0});
    }
    case 215: {
      // 3-Color RGB Strobe | Hardcoded
      const colors = [{r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255}];
      const isOn = (tick * 10) % 1 < 0.5;
      return Array(n).fill(isOn ? colors[Math.floor(tick * 3) % 3] : {r:0,g:0,b:0});
    }
    case 216: {
      // 7-Color Strobe | Hardcoded
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const isOn = (tick * 14) % 1 < 0.5;
      return Array(n).fill(isOn ? colors[Math.floor(tick * 7) % 7] : {r:0,g:0,b:0});
    }
    case 217:
    case 218: {
      return buildCometChase(fg, bg, n, tick, direction);
    }
    case 219: {
      return buildSingleDotChase(fg, bg, n, (tick * 2) % 1, direction);
    }
    case 220: {
      // Static Partial Rainbow Gradient (Blue ends, rainbow center)
      return Array.from({ length: n }, (_, i) => {
        const t = Math.abs((i / (n - 1)) * 2 - 1); 
        const hue = 0.66 * t; 
        return hsvToRgb(hue, 1.0, 1.0);
      });
    }
    case 221: {
      // 7-Color Multi-Comet Flow (Train of fading chunks)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const COMET_LEN = Math.min(n, 8); // Bible: fixed 8px comet chunks for ID 221
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const cometIdx = Math.floor(pos / COMET_LEN);
        const wrapped = (i + offset) >= n ? 1 : 0;
        const c = colors[(cometIdx + loopCount + wrapped) % 7];
        const dist = pos % COMET_LEN;
        const brightness = dist / (COMET_LEN - 1);
        return blendRGB(c, {r:0,g:0,b:0}, brightness);
      });
    }
    case 222:
    case 223: {
      // 7-Color Sweep / Wipe (Sequential fills from back to front)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const activeColor = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const wipeProgress = (tick * 7) % 1;
      const fillCount = Math.floor(wipeProgress * n);
      return Array.from({ length: n }, (_, i) => i <= fillCount ? activeColor : prevColor);
    }
    case 224: {
      // 7-Color Stacker / Building Blocks (Drops one full color stack, then next)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const activeColor = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const stackProgress = (tick * 7) % 1;
      const stackLevel = Math.floor(stackProgress * n); 
      const dropPos = Math.floor(((stackProgress * n) % 1) * (n - stackLevel)); 
      return Array.from({ length: n }, (_, i) => {
        if (i >= n - stackLevel) return activeColor; 
        if (i === dropPos) return activeColor; 
        return prevColor; // Background is the previous stacked color
      });
    }
    case 225: {
      // Alternating Fading Chunks (FG chunk then BG chunk flowing forward)
      const CHUNK = Math.max(3, Math.floor(n / 4));
      const offset = Math.floor(tick * n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const chunkIdx = Math.floor(pos / CHUNK);
        const activeColor = chunkIdx % 2 === 0 ? fg : bg;
        const dist = pos % CHUNK;
        const brightness = dist / (CHUNK - 1);
        return blendRGB(activeColor, {r:0,g:0,b:0}, brightness);
      });
    }
    case 226: {
      // 7-Color Center-In Fill (Fills from edges to center, collapses, changes color)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const c = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const progress = (tick * 7) % 1;
      const fillLen = Math.floor(progress * (n / 2));
      return Array.from({ length: n }, (_, i) => {
        const distFromEdge = Math.min(i, n - 1 - i);
        return distFromEdge <= fillLen ? c : prevColor;
      });
    }
    case 227: {
      // 7-Color Large Multi-Comet Flow
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const COMET_LEN = Math.max(5, Math.floor(n * 0.3)); // Bible: ~30% large comet chunks for ID 227
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const cometIdx = Math.floor(pos / COMET_LEN);
        const wrapped = (i + offset) >= n ? 1 : 0;
        const c = colors[(cometIdx + loopCount + wrapped) % 7];
        const dist = pos % COMET_LEN;
        const brightness = dist / (COMET_LEN - 1);
        return blendRGB(c, {r:0,g:0,b:0}, brightness);
      });
    }
    case 228: {
      // Flowing Fire Effect (White -> Yellow -> Orange -> Red gradient flow)
      const colors = [
        {r:255,g:255,b:255}, 
        {r:255,g:255,b:0},   
        {r:255,g:100,b:0},   
        {r:255,g:0,b:0}      
      ];
      const offset = tick * 4;
      return Array.from({ length: n }, (_, i) => {
        const t = (i / n * 4 - offset) % 4;
        const p = t < 0 ? t + 4 : t;
        const idx1 = Math.floor(p);
        const idx2 = (idx1 + 1) % 4;
        return blendRGB(colors[idx2], colors[idx1], p % 1);
      });
    }
    case 229: {
      // 7-Color Large Block Flow (50% block of color flows from back to front)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const BLOCK = Math.floor(n / 2);
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        const isBlock = pos < BLOCK;
        const wrapped = (i + offset) >= n ? 1 : 0;
        const colorIdx = (loopCount + wrapped) % 7;
        return isBlock ? colors[colorIdx] : {r:0,g:0,b:0};
      });
    }
    case 230: {
      // 7-Color Alternating Center Fill (Fills Center-Out, then fills Edges-In)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const colorPhase = Math.floor(tick * 7);
      const c = colors[colorPhase % 7];
      const prevColor = colors[(colorPhase + 6) % 7];
      const subTick = (tick * 7) % 1;
      const isCenterOut = subTick < 0.5;
      const progress = isCenterOut ? subTick * 2 : (subTick - 0.5) * 2;
      const fillLen = Math.floor(progress * (n / 2));
      return Array.from({ length: n }, (_, i) => {
        const distFromCenter = Math.abs(i - Math.floor(n / 2));
        const distFromEdge = Math.min(i, n - 1 - i);
        if (isCenterOut) return distFromCenter <= fillLen ? c : prevColor;
        else return distFromEdge <= fillLen ? c : prevColor;
      });
    }
    case 231: {
      // Custom Marquee Chunk ([FG, BG, Black, BG, FG] scrolling forward over BG)
      const offset = Math.floor(tick * n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        if (pos === 0) return fg;
        if (pos === 1) return bg;
        if (pos === 2) return {r:0,g:0,b:0};
        if (pos === 3) return bg;
        if (pos === 4) return fg;
        return bg;
      });
    }
    case 232: {
      // Glitch / Twitch Marquee (Alternates small ants and chunky twitches)
      const isTwitch = (Math.sin(tick * 15) * 43758) % 1 > 0.5;
      const offset = Math.floor(tick * n * (isTwitch ? 3 : 1));
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        if (isTwitch) return pos % 8 < 4 ? fg : bg;
        return pos % 2 === 0 ? fg : bg;
      });
    }
    case 233: {
      // 7-Color Dot Stream over custom BG (Dots separated by BG spaces)
      const colors = [
        {r:255,g:0,b:0}, {r:255,g:255,b:0}, {r:0,g:255,b:0}, {r:0,g:255,b:255}, 
        {r:0,g:0,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
      ];
      const totalOffset = tick * n * 7;
      const offset = Math.floor(totalOffset) % n;
      const loopCount = Math.floor(totalOffset / n);
      return Array.from({ length: n }, (_, i) => {
        const pos = (i + offset) % n;
        if (pos % 3 !== 0) return bg;
        const wrapped = (i + offset) >= n ? 1 : 0;
        const colorIdx = (loopCount + wrapped) % 7;
        return colors[colorIdx];
      });
    }

    // ── GROUP 8: STREET MODES ──
    case 101:
    case 102:
    case 103:
    case 104:
    case 105:
      return buildStreetMode(patternId, fg, bg, n, tick, options);

    default:
      return Array(n).fill(fg);
  }
}



/**
 * Rotate a pixel array by `offset` positions (for visualizer scroll simulation).
 */

export function getHardwarePixelArray(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  options?: PatternOptions
): RGB[] | null {
  // All 61 patterns now use 0x59. commandType selects the hardware behavior:
  //   IDs 20-22, 30-31, 33, 37, 39-40, 46-47, 54-55 use Breathe/Jump/Strobe commandTypes.
  //   The pixel array is the color seed; hardware animates it autonomously.
  //
  // Use tick=0.33 for most patterns so the initial hardware seed frame is visually rich.
  // EXCEPTION: Jump (0x04) and Strobe (0x03) patterns, plus some Breathe patterns
  // These builders include an isOn phase-gate that can return all-black at tick=0.33.
  // Since hardware handles flash/jump autonomously once the array is received,
  // we need a fully-lit seed. We use tick=0.0 for strobes/jumps, and tick=0.25 for breathe.
  // Use tick=0.33 for most patterns so the initial hardware seed frame is visually rich.
  // EXCEPTION: Jump (0x04) and Strobe (0x03) patterns, plus some Breathe patterns
  // These builders include an isOn phase-gate that can return all-black at tick=0.33.
  // Since hardware handles flash/jump autonomously once the array is received,
  // we need a fully-lit seed. We use tick=0.0 for strobes/jumps, and tick=0.25 for breathe.
  const ZERO_TICK_IDS = new Set([26, 28, 31, 46, 54]); // Strobe/Jump
  const QUARTER_TICK_IDS = new Set([24, 30, 36, 40]); // Breathe (sin wave peak at PI/2)
  const seedTick = ZERO_TICK_IDS.has(patternId) ? 0.0 : (QUARTER_TICK_IDS.has(patternId) ? 0.25 : 0.33);
  return generateArray(patternId, fg, bg, Math.max(1, numLEDs), seedTick, 1, options);

}

/**
 * Get the 0x59 transition (commandType) byte for a pattern.
 *
 * SINGLE SOURCE OF TRUTH: ZENGGE_PROTOCOL_BIBLE.md
 * 0x01=Static, 0x02=Running, 0x03=Strobe, 0x04=Jump, 0x05=Breathe, 0x06=Twinkle
 *
 * Previous bug: was returning 0x00 for animated patterns. 0x00 is NOT a valid
 * commandType — it maps to nothing. Hardware received undefined byte → no animation.
 */

export function getPatternTransitionType(patternId: PatternId): number {
  // ── GROUP 1: STATIC (IDs 1–5) ──────────────────────────────────────────────
  if (patternId >= 1 && patternId <= 5) return 0x01; // Static — hardware freezes array

  // ── GROUP 5a: HARDWARE TEMPORAL (IDs 20–22) ────────────────────────────────
  if (patternId === 17) return 0x05; // Breathe  — hardware pulses brightness

  // ── GROUP 7: ge.* BREATHE EFFECTS (IDs 30, 39, 40, 47, 55) ───────────────
  // Hardware receives the pixel array and pulses its brightness autonomously.
  // Visualizer uses buildColorBreathing / buildSmoothBreath / buildRainbowBreathing / buildNeonPulse at animTick.
  if (patternId === 24) return 0x05; // Color Breathing   — FG_ONLY
  if (patternId === 30) return 0x05; // Heartbeat Pulse   — FG_ONLY (breathe pulse)
  if (patternId === 36) return 0x05; // Rainbow Breathing — GENERATIVE
  if (patternId === 40) return 0x05; // Neon Pulse        — FG_BG (NeonPulse math uses buildNeonPulse)

  // ── GROUP 7: ge.* JUMP EFFECTS (IDs 31, 46, 54) ──────────────────────────
  // Hardware receives the pixel array and hard-cuts states autonomously.
  // Visualizer uses buildColorJump / buildPoliceLights / buildCyberGlitch at animTick.

  // ── GROUP 7: ge.* STROBE EFFECTS (IDs 33, 37) ────────────────────────────
  // Hardware receives the pixel array and flashes it on/off autonomously.
  // Visualizer uses buildStrobe / buildLightning at animTick.
  if (patternId === 26) return 0x03; // Strobe Flash      — FG_ONLY
  if (patternId === 28) return 0x03; // Lightning Strike  — FG_ONLY (random flash)

  // ── ALL OTHER ge.* SCROLL EFFECTS (IDs 29, 32, 34–36, 38, 41–45, 48–53, 56–61) ──
  // Hardware scrolls the pre-computed pixel array continuously via 0x02 Running.
  // NOTE: Twinkle-style patterns (35, 45, 49, 50, 58, 60) intentionally use 0x02 (Running)
  // because 0x06 (Twinkle) hardware support is incomplete.
  
  // ── GROUP 8: STREET MODES (101-105) ──────────────────────────────────────
  if (patternId === 101 || patternId === 104) return 0x01; // STOPPED/SLOWING — STATIC
  if (patternId === 103) return 0x02; // HARD BRAKING — FLASH/STROBE
  // CRUISING (102) & ACCELERATING (105) use 0x01 because the hardware cannot 
  // autonomously bounce the dot. It is just a static array we update periodically.
  if (patternId >= 100) return 0x01; 

  return 0x02; // Running — continuous hardware scroll
}

/**
 * Build the full 0x59 hardware command.
 *
 * @param numLEDs           - Number of pixels in the math array (may be capped by MAX_PIXELS).
 * @param hardwareLedPoints - Actual LED count on the physical strip. Written into the 0x59
 *                            spatial footer so the controller maps pixels to the full strip.
 *                            Defaults to numLEDs if not provided (backwards-compatible).
 */
// ─── NEW 0x41 NATIVE EFFECT GENERATORS (Visualizer Math Parity) ─────────────────

export function buildLargeChunkScroll(fg: RGB, bg: RGB, n: number, tick: number, direction: 0 | 1): RGB[] {
  const CHUNK = Math.max(1, Math.floor(n / 3)); 
  const offset = Math.floor(tick * n);
  return Array.from({ length: n }, (_, i) => {
    const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
    return (pos % (CHUNK * 2) < CHUNK) ? fg : bg;
  });
}

export function buildGradientChunk(fg: RGB, bg: RGB, n: number, tick: number, direction: 0 | 1): RGB[] {
  const offset = tick * Math.PI * 2;
  return Array.from({ length: n }, (_, i) => {
    const pos = direction === 1 ? i : n - 1 - i;
    const ratio = (Math.sin((pos / n) * Math.PI * 4 + offset) + 1) / 2;
    return blendRGB(bg, fg, ratio);
  });
}

export function buildPingPongFill(fg: RGB, bg: RGB, n: number, tick: number): RGB[] {
  const fillLevel = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  const cutoff = Math.floor(fillLevel * n);
  return Array.from({ length: n }, (_, i) => (i < cutoff ? fg : bg));
}

export function buildPingPongMarquee(fg: RGB, bg: RGB, n: number, tick: number): RGB[] {
  const SIZE = Math.max(1, Math.floor(n / 6));
  const maxPos = n - SIZE;
  const pos = tick < 0.5 ? (tick * 2) * maxPos : ((1 - tick) * 2) * maxPos;
  const start = Math.floor(pos);
  return Array.from({ length: n }, (_, i) => (i >= start && i < start + SIZE ? fg : bg));
}

export function buildRandomStrobe(fg: RGB, bg: RGB, n: number, tick: number): RGB[] {
  const phase = Math.floor(tick * 20);
  return Array.from({ length: n }, (_, i) => {
    const pseudo = (Math.sin(phase * i * 12.9898) * 43758.5453) % 1;
    return pseudo > 0.5 ? fg : bg;
  });
}

export function buildStaticPartialRainbow(n: number): RGB[] {
  return Array.from({ length: n }, (_, i) => hsvToRgb((i / n), 1.0, 1.0));
}

export function buildTetrisStacker(n: number, tick: number): RGB[] {
  const dropPos = Math.floor(tick * n);
  const hue = tick % 1.0;
  return Array.from({ length: n }, (_, i) => {
    if (i === dropPos) return hsvToRgb(hue, 1.0, 1.0);
    if (i > n * 0.8) return hsvToRgb((i / n) % 1.0, 1.0, 0.5);
    return { r: 0, g: 0, b: 0 };
  });
}

export function buildAlternatingComet(fg: RGB, bg: RGB, n: number, tick: number, direction: 0 | 1): RGB[] {
  const headColor = (tick * 10) % 2 < 1 ? fg : { r: 255, g: 255, b: 255 };
  return buildCometChase(headColor, bg, n, tick, direction);
}

export function buildPingPongCenterFill(n: number, tick: number): RGB[] {
  const fg = { r: 255, g: 255, b: 255 };
  const bg = { r: 0, g: 0, b: 0 };
  const fillLevel = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  const cutoff = Math.floor(fillLevel * (n / 2));
  return Array.from({ length: n }, (_, i) => {
    const distFromEdge = Math.min(i, n - 1 - i);
    return distFromEdge < cutoff ? fg : bg;
  });
}

export function buildCustomArrayScroll(fg: RGB, bg: RGB, n: number, tick: number, direction: 0 | 1): RGB[] {
  const offset = Math.floor(tick * n);
  return Array.from({ length: n }, (_, i) => {
    const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
    if (pos % 5 === 0) return fg;
    if (pos % 5 === 2) return bg;
    return { r: 0, g: 0, b: 0 };
  });
}

export function buildGlitchMarquee(fg: RGB, bg: RGB, n: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = Math.floor(tick * 30);
  const offset = Math.floor(tick * n);
  return Array.from({ length: n }, (_, i) => {
    const pos = direction === 1 ? (i + offset) % n : (i - offset + n) % n;
    const isGlitch = (Math.sin(phase * i) * 43758.5453) % 1 > 0.85;
    if (isGlitch) return { r: 255, g: 255, b: 255 };
    return (pos % 4 < 2) ? fg : bg;
  });
}
