import type { RGB } from './shared/engineTypes';
import { hexToRgb } from './shared/engineUtils';
import { lerpRGB } from './shared/spatialMath';
import { hsvToRgb,
  buildColorJump, buildStrobe, buildSingleDotChase, 
  buildWipeFill, buildCometChase, buildSinePulseWave, buildDashedMarquee, 
  buildBreathingWave, buildMeteorShower, buildRainbowMarquee, buildRainbowComet, 
  buildTrueRainbowFlow, buildRainbowBreathing, buildRainbowChaser,
  buildNativeBreathe, buildNativeCenterOut,
  buildLargeChunkScroll, buildGradientChunk, buildPingPongFill, buildPingPongMarquee, buildRandomStrobe,
  buildStaticPartialRainbow, buildTetrisStacker, buildAlternatingComet, buildPingPongCenterFill,
  buildCustomArrayScroll, buildGlitchMarquee, buildMicroAnts, buildWipeCenterOut, buildFireFlame
} from './SpatialEngine';
// ─── GENERATORS ───────────────────────────────────────────────────────────────


export function getMusicPaletteAt(palette: RGB[], tick: number): RGB {
  if (palette.length === 0) return { r: 0, g: 0, b: 0 };
  return palette[Math.floor(tick * palette.length) % palette.length];
}


export function getMusicPaletteSmooth(palette: RGB[], tick: number): RGB {
  if (palette.length <= 1) return palette[0] ?? { r: 0, g: 0, b: 0 };
  const pos = (tick * palette.length) % palette.length;
  return lerpRGB(palette[Math.floor(pos) % palette.length], palette[(Math.floor(pos) + 1) % palette.length], pos - Math.floor(pos));
}


const MUSIC_RAINBOW7: RGB[] = ['#FF0000','#FF6600','#FFFF00','#00FF00','#00FFFF','#0066FF','#AA00FF'].map(hexToRgb);

/**
 * Get a hardware-accurate pixel + opacity array for MUSIC mode patterns 1–13.
 *
 * Audio-reactive: `magnitude` (0.0–1.0) gates intensity, fill depth, and flash thresholds.
 * The visualizer applies `opacities[i]` as per-LED brightness multiplier.
 *
 * Migrated from RbmSimulator.getRbmMusicFrame [BATCH:P1]. PatternEngine is now SSOT.
 *
 * @param musicPatternId - Music pattern ID 1–13
 * @param numLEDs        - LED count for the segment
 * @param animTick       - Animation tick 0.0–1.0 (looping)
 * @param magnitude      - Live audio amplitude 0.0–1.0
 * @param baseColorHex   - User-selected base color e.g. '#FF0000'
 */

export function getMusicVisualizerFrame(
  musicPatternId: number,
  numLEDs: number,
  animTick: number,
  magnitude: number,
  baseColorHex: string
): { pixels: RGB[]; opacities: number[] } {
  const n = Math.max(1, numLEDs);
  const base = hexToRgb(baseColorHex);
  const white: RGB = { r: 255, g: 255, b: 255 };
  const black: RGB = { r: 0, g: 0, b: 0 };
  const mag = Math.max(0, Math.min(1, magnitude));

  const pixels: RGB[] = Array(n).fill(null).map(() => ({ ...base }));
  const opacities: number[] = Array(n).fill(1.0);

  switch (musicPatternId) {
    case 1: { // Soft — whole strip breathe, depth = magnitude
      const breathe = 0.2 + mag * 0.8;
      const color = lerpRGB(black, base, breathe);
      for (let i = 0; i < n; i++) { pixels[i] = { ...color }; opacities[i] = breathe; }
      break;
    }
    case 2: { // Cheerful — sparkle white on peaks
      for (let i = 0; i < n; i++) {
        if (mag > 0.7 && (i + Math.floor(animTick * 7)) % 3 === 0) {
          pixels[i] = { ...white }; opacities[i] = 1.0;
        } else { pixels[i] = { ...base }; opacities[i] = 0.3 + mag * 0.5; }
      }
      break;
    }
    case 3: { // Energy — magnitude-gated strobe flash
      const on = mag > 0.5 && Math.floor(animTick * 10) % 2 === 0;
      const c = getMusicPaletteAt(MUSIC_RAINBOW7, animTick);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = on ? 1.0 : 0.05; }
      break;
    }
    case 4: { // Relax — slow smooth rainbow fade
      const c = getMusicPaletteSmooth(MUSIC_RAINBOW7, animTick * 0.3);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = 0.5 + mag * 0.5; }
      break;
    }
    case 5: { // Passion — overlay expand center→ends; fill level = magnitude
      const center = Math.floor(n / 2);
      const reach = Math.floor(mag * center);
      for (let i = 0; i < n; i++) {
        pixels[i] = { ...base };
        opacities[i] = Math.abs(i - center) <= reach ? 1.0 : 0.05;
      }
      break;
    }
    case 6: { // Brisk — alternating segments jump on beat
      const step = Math.floor(animTick * 4) % 2;
      for (let i = 0; i < n; i++) {
        pixels[i] = { ...base };
        opacities[i] = ((Math.floor(i / 3) % 2) === step) && mag > 0.3 ? 1.0 : 0.1;
      }
      break;
    }
    case 7: { // Rhythm — VU meter bottom→top, fill = magnitude
      for (let i = 0; i < n; i++) {
        const fract = i / n;
        opacities[i] = fract < mag ? 1.0 : 0.05;
        pixels[i] = fract > 0.85 ? { r: 255, g: 0, b: 0 } : fract > 0.65 ? { r: 255, g: 200, b: 0 } : { ...base };
      }
      break;
    }
    case 8: { // Rolling — rainbow flow, magnitude controls opacity
      for (let i = 0; i < n; i++) {
        pixels[i] = getMusicPaletteSmooth(MUSIC_RAINBOW7, ((i / n) + animTick) % 1);
        opacities[i] = 0.2 + mag * 0.8;
      }
      break;
    }
    case 9: { // Flicker — deterministic twinkling dots on magnitude
      for (let i = 0; i < n; i++) {
        const seed = Math.sin(i * 127.1 + animTick * 311.7) * 43758.5;
        opacities[i] = (seed - Math.floor(seed)) < mag ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 10: { // Accumulate — overlay from both ends to center, fill = magnitude
      const reach = Math.floor(mag * (n / 2));
      for (let i = 0; i < n; i++) {
        opacities[i] = Math.min(i, n - 1 - i) < reach ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 11: { // Shuttle — bright dot bouncing in sync with animTick
      const bouncedPos = animTick < 0.5 ? animTick * 2 : (1 - animTick) * 2;
      const head = Math.floor(bouncedPos * n);
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - head);
        opacities[i] = dist === 0 ? 1.0 : dist === 1 ? 0.4 * mag : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 12: { // Fireworks — expand from center, burst flash on peak
      const center = Math.floor(n / 2);
      const burst = mag > 0.8;
      const reach = burst ? n : Math.floor(animTick * (n / 2));
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - center);
        if (burst) { pixels[i] = { ...white }; opacities[i] = 1.0; }
        else { pixels[i] = { ...base }; opacities[i] = dist <= reach ? (1 - dist / (n / 2)) * mag : 0.02; }
      }
      break;
    }
    case 13: { // Snow — drifting dots falling end→start
      for (let i = 0; i < n; i++) {
        const snowflake = Math.abs(Math.sin(i * 47.3 + Math.floor(animTick * 3) * 1234.5)) > (1 - mag * 0.7);
        pixels[i] = snowflake ? { ...white } : { ...base };
        opacities[i] = snowflake ? 1.0 : 0.1;
      }
      break;
    }
    default: {
      for (let i = 0; i < n; i++) { pixels[i] = { ...base }; opacities[i] = mag; }
    }
  }

  return { pixels, opacities };
}

// ─── 0x51 SYMPHONY VISUALIZER ENGINE ─────────────────────────────────────────

/**
 * Get the visualizer pixel array for a native 0x51 Symphony Effect.
 * This is COMPLETELY ISOLATED from the 0x59 PatternEngine. It uses the 44
 * hardware IDs defined by ZENGGE firmware.
 *
 * @param symphonyId 1-44 (The exact ZENGGE hardware effect ID)
 * @param fg Foreground color 
 * @param bg Background color
 * @param numLEDs Number of pixels per segment
 * @param animTick 0.0 to 1.0 driving the animation
 */

export function getSymphonyVisualizerFrame(
  symphonyId: number,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  animTick: number
): RGB[] {
  const n = Math.max(1, numLEDs);
  const black: RGB = { r: 0, g: 0, b: 0 };

  switch (symphonyId) {
    // ── Single/Dual Color Modifiable (1-34) ──
    case 1: return buildNativeBreathe(fg, bg, n, animTick); // Change gradually
    case 2: return buildNativeBreathe(fg, black, n, animTick); // Bright up and Fade gradually
    case 3: return buildColorJump(fg, bg, n, animTick); // Change quickly
    case 4: return buildStrobe(fg, n, animTick); // Strobe-flash
    
    // Running 1 point
    case 5: return buildSingleDotChase(fg, bg, n, animTick, 1); // start to end
    case 6: return buildSingleDotChase(fg, bg, n, animTick, 0); // end to start
    case 7: return buildNativeCenterOut(fg, bg, n, animTick); // middle to both ends
    case 8: return buildNativeCenterOut(fg, bg, n, 1 - animTick); // both ends to middle

    // Overlay
    case 9: return buildWipeFill(fg, bg, n, animTick, 1);
    case 10: return buildWipeFill(fg, bg, n, animTick, 0);
    case 11: return buildNativeCenterOut(fg, bg, n, animTick); // Overlay middle to ends
    case 12: return buildNativeCenterOut(fg, bg, n, 1 - animTick);

    // Fading and running 1 point (Comet)
    case 13: return buildCometChase(fg, bg, n, animTick, 1);
    case 14: return buildCometChase(fg, bg, n, animTick, 0);

    // Olivary Flowing (Wave)
    case 15: return buildSinePulseWave(fg, bg, n, animTick, 1);
    case 16: return buildSinePulseWave(fg, bg, n, animTick, 0);

    // Running 1 point w/ background
    case 17: return buildSingleDotChase(fg, bg, n, animTick, 1);
    case 18: return buildSingleDotChase(fg, bg, n, animTick, 0);

    // 2 colors run, multi points w/black background
    case 19: return buildDashedMarquee(fg, black, n, animTick, 1);
    case 20: return buildDashedMarquee(fg, black, n, animTick, 0);

    // 2 colors run alternately, fading
    case 21: return buildBreathingWave(fg, bg, n, animTick, 1);
    case 22: return buildBreathingWave(fg, bg, n, animTick, 0);

    // 2 colors run alternately, multi points
    case 23: return buildDashedMarquee(fg, bg, n, animTick, 1);
    case 24: return buildDashedMarquee(fg, bg, n, animTick, 0);

    // Fading out Flows (Meteor Shower)
    case 25: return buildMeteorShower(fg, bg, n, animTick, 1);
    case 26: return buildMeteorShower(fg, bg, n, animTick, 0);

    // 7 colors run alternately, 1 point with multi points background
    case 27: {
      const frame = buildRainbowMarquee(n, animTick, 1);
      return frame.map(p => (p.r === 0 && p.g === 0 && p.b === 0) ? bg : p);
    }
    case 28: {
      const frame = buildRainbowMarquee(n, animTick, 0);
      return frame.map(p => (p.r === 0 && p.g === 0 && p.b === 0) ? bg : p);
    }

    // 7 colors run alternately, 1 point
    case 29: return buildRainbowMarquee(n, animTick, 1);
    case 30: return buildRainbowMarquee(n, animTick, 0);

    // 7 colors run alternately, multi points
    case 31: return buildRainbowComet(n, animTick, 1);
    case 32: return buildRainbowComet(n, animTick, 0);

    // 7 colors overlay, multi points
    case 33: return buildTrueRainbowFlow(n, animTick, 1); 
    case 34: return buildTrueRainbowFlow(n, animTick, 0);

    // ── 7-Color Generative / No-Color (35-44) ──
    case 35: return buildRainbowBreathing(n, animTick); 
    case 36: return buildRainbowBreathing(n, animTick); 
    
    // 7 colors flow gradually
    case 37: return buildTrueRainbowFlow(n, animTick, 1);
    case 38: return buildTrueRainbowFlow(n, animTick, 0);

    // Fading out run, 7 colors
    case 39: return buildRainbowComet(n, animTick, 1);
    case 40: return buildRainbowComet(n, animTick, 0);

    // Runs in olivary, 7 colors
    case 41: return buildTrueRainbowFlow(n, animTick, 1); 
    case 42: return buildTrueRainbowFlow(n, animTick, 0);

    // Fading out run, 7 colors start with white color
    case 43: return buildRainbowChaser(n, animTick, 1);
    case 44: return buildRainbowChaser(n, animTick, 0);

    // ── 0x41 Hardware Parity (Modes 201-233) ──
    case 201: return buildLargeChunkScroll(fg, { r: 255, g: 255, b: 255 }, n, animTick, 1);
    case 202: return buildGradientChunk(fg, bg, n, animTick, 1);
    case 203: return buildSingleDotChase(fg, bg, n, animTick, 1);
    case 204: return buildPingPongFill(fg, bg, n, animTick);
    case 205: return buildPingPongMarquee(fg, bg, n, animTick);
    case 206: return buildMicroAnts(fg, bg, n, animTick, 1);
    case 207: return buildNativeBreathe(fg, { r: 0, g: 0, b: 0 }, n, animTick);
    case 208: {
      const frame = buildRainbowBreathing(n, animTick);
      return frame.map(p => {
        const maxC = Math.max(p.r, p.g, p.b);
        return {
          r: p.r === maxC ? p.r : 0,
          g: p.g === maxC && p.r !== maxC ? p.g : 0,
          b: p.b === maxC && p.r !== maxC && p.g !== maxC ? p.b : 0
        };
      });
    }
    case 209: return buildRainbowBreathing(n, animTick);
    case 210: {
      const phase = (animTick * 3) % 3;
      const c = phase < 1 ? {r: 255, g: 0, b: 0} : phase < 2 ? {r: 0, g: 255, b: 0} : {r: 0, g: 0, b: 255};
      return Array(n).fill(c);
    }
    case 211: return buildRainbowBreathing(n, animTick);
    case 212: {
      const hue = animTick % 1.0;
      return Array(n).fill(hsvToRgb(hue, 1.0, 1.0));
    }
    case 213: {
      const hue = Math.floor(animTick * 7) / 7;
      return Array(n).fill(hsvToRgb(hue, 1.0, 1.0));
    }
    case 214: return buildRandomStrobe(fg, bg, n, animTick);
    case 215: {
      const phase = (animTick * 3) % 3;
      const c = phase < 1 ? {r: 255, g: 0, b: 0} : phase < 2 ? {r: 0, g: 255, b: 0} : {r: 0, g: 0, b: 255};
      return buildStrobe(c, n, animTick);
    }
    case 216: {
      const hue = Math.floor(animTick * 7) / 7;
      return buildStrobe(hsvToRgb(hue, 1.0, 1.0), n, animTick);
    }
    case 217:
    case 218: return buildCometChase(fg, bg, n, animTick, 1);
    case 219: return buildSingleDotChase(fg, bg, n, (animTick * 2) % 1, 1);
    case 220: return buildStaticPartialRainbow(n);
    case 221: return buildRainbowComet(n, animTick, 1);
    case 222:
    case 223: {
      const fillCount = Math.floor(animTick * n);
      return Array.from({ length: n }, (_, i) => {
        if (i < fillCount) return hsvToRgb((i / n) % 1.0, 1.0, 1.0);
        return { r: 0, g: 0, b: 0 };
      });
    }
    case 224: return buildTetrisStacker(n, animTick);
    case 225: return buildAlternatingComet(fg, bg, n, animTick, 1);
    case 226: {
      const hue = animTick % 1.0;
      return buildWipeCenterOut(hsvToRgb(hue, 1.0, 1.0), { r: 0, g: 0, b: 0 }, n, animTick);
    }
    case 227: return buildRainbowComet(n, animTick, 1); // Large rainbow comet
    case 228: return buildFireFlame({r: 255, g: 100, b: 0}, { r: 0, g: 0, b: 0 }, n, animTick, 1);
    case 229: {
      const CHUNK = Math.floor(n * 0.5);
      const head = Math.floor(animTick * n);
      return Array.from({ length: n }, (_, i) => {
        const dist = (i - head + n) % n;
        if (dist < CHUNK) return hsvToRgb((i / n + animTick) % 1.0, 1.0, 1.0);
        return { r: 0, g: 0, b: 0 };
      });
    }
    case 230: return buildPingPongCenterFill(n, animTick);
    case 231: return buildCustomArrayScroll(fg, bg, n, animTick, 1);
    case 232: return buildGlitchMarquee(fg, bg, n, animTick, 1);
    case 233: {
      const frame = buildRainbowMarquee(n, animTick, 1);
      return frame.map(p => (p.r === 0 && p.g === 0 && p.b === 0) ? bg : p);
    }

    // Fallback
    default: return Array(n).fill(black);
  }
}
