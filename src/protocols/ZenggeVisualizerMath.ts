/**
 * Zengge Protocol Visualizer Math Engine
 * Allows pixel-perfect local simulation of the 33 Custom Step Effects (0x51).
 * 
 * Re-engineered from the Zengge APK to accurately reflect hardware 
 * mathematically, rather than porting bloated Java arrays.
 *
 * Effect IDs 1–33 correspond DIRECTLY to hardware SymphonyEffect IDs (no offset).
 * APK source: ed/c.java, COMM/Protocol/x.java, symphony_SymphonyEffect1–33.
 */

export interface RGB {
  r: number;
  g: number;
  b: number;
}

export class ZenggeVisualizerMath {
  /**
   * Helper to interpolate between two colors
   */
  private static interpolate(c1: RGB, c2: RGB, factor: number): RGB {
    const clamp = (v: number) => Math.max(0, Math.min(255, Math.round(v)));
    return {
      r: clamp(c1.r + (c2.r - c1.r) * factor),
      g: clamp(c1.g + (c2.g - c1.g) * factor),
      b: clamp(c1.b + (c2.b - c1.b) * factor),
    };
  }

  /**
   * Helper to rotate/scroll an array
   */
  private static rotate(arr: RGB[], tick: number, reverse: boolean): RGB[] {
    if (arr.length === 0) return arr;
    const len = arr.length;
    let offset = Math.floor(tick * len) % len;
    if (reverse) offset = len - offset;
    if (offset === len) offset = 0;
    return [...arr.slice(offset), ...arr.slice(0, offset)];
  }

  /**
   * Primary Math Engine for the 33 Effects.
   * Given an effect ID (1-33 — matches hardware SymphonyEffect IDs directly),
   * Foreground/Background colors, and an animation tick (0.0 - 1.0),
   * mathematically compute the 16 virtual dots for the visualizer.
   */
  public static getVisualizerDots(
    effectId: number, 
    fg: RGB, 
    bg: RGB, 
    tick: number,
    direction: boolean = true, // true = forward, false = reverse
    segmented: boolean = false
  ): RGB[] {
    const dots = new Array<RGB>(16).fill({ r: 0, g: 0, b: 0 });
    
    // Safety clamp
    const safeTick = Math.max(0, Math.min(1, tick));

    // Effect IDs 1-33 map directly to hardware
    switch (effectId) {
      case 1: // Change Gradually (CROSSFADE) — symphony_SymphonyEffect1
        // Hardware fades the entire array smoothly from FG to BG and back
        const tFade = Math.sin(safeTick * Math.PI); // 0 -> 1 -> 0
        return dots.fill(this.interpolate(bg, fg, tFade));
      
      case 2: // Bright Up and Fade Gradually (BREATHING) — symphony_SymphonyEffect2
        const tBreath = Math.abs(Math.sin(safeTick * Math.PI));
        return dots.fill(this.interpolate({ r: 0, g: 0, b: 0 }, fg, tBreath));
      
      case 3: // Change Quickly (JUMP) — symphony_SymphonyEffect3
        return dots.fill(safeTick < 0.5 ? fg : bg);
        
      case 4: // Strobe Flash — symphony_SymphonyEffect4
        return dots.fill(Math.floor(safeTick * 10) % 2 === 0 ? fg : { r: 0, g: 0, b: 0 });

      case 5: // Running, 1pt — Start to End — symphony_SymphonyEffect5
      case 6: // Running, 1pt — End to Start — symphony_SymphonyEffect6
        const run1PointDir = effectId === 5 ? direction : !direction;
        const index1 = Math.floor(safeTick * 16) % 16;
        for (let i = 0; i < 16; i++) {
          dots[i] = (i === index1) ? fg : bg;
        }
        return this.rotate(dots, 0, !run1PointDir);

      case 7: // Running, 1pt — Middle to Ends — symphony_SymphonyEffect7
      case 8: // Running, 1pt — Ends to Middle — symphony_SymphonyEffect8
        const midDir = effectId === 7 ? direction : !direction;
        const midIdx = Math.floor(safeTick * 8) % 8;
        for (let i = 0; i < 16; i++) {
          dots[i] = (i === midIdx || i === 15 - midIdx) ? fg : bg;
        }
        return midDir ? dots : dots.reverse();

      case 9:  // Overlay — Start to End — symphony_SymphonyEffect9
      case 10: // Overlay — End to Start — symphony_SymphonyEffect10
        const stackDir = effectId === 9 ? direction : !direction;
        const fillAmount = Math.floor(safeTick * 16);
        for (let i = 0; i < 16; i++) {
          dots[i] = i < fillAmount ? fg : bg;
        }
        return stackDir ? dots : dots.reverse();

      case 11: // Overlay — Middle to Ends — symphony_SymphonyEffect11
      case 12: // Overlay — Ends to Middle — symphony_SymphonyEffect12
        const midFill = Math.floor(safeTick * 8);
        for (let i = 0; i < 16; i++) {
          const dist = Math.abs(i - 7.5);
          dots[i] = dist >= (8 - midFill) ? fg : bg;
        }
        return effectId === 12 ? dots.reverse() : dots;

      case 13: // Fading Run, 1pt — Start to End (Comet) — symphony_SymphonyEffect13
      case 14: // Fading Run, 1pt — End to Start — symphony_SymphonyEffect14
        const cometDir = effectId === 13 ? direction : !direction;
        const baseComet = [
          fg, 
          this.interpolate(fg, bg, 0.3),
          this.interpolate(fg, bg, 0.6),
          this.interpolate(fg, bg, 0.8),
          ...Array(12).fill(bg)
        ];
        return this.rotate(baseComet, safeTick, !cometDir);

      case 15: // Olivary Flow — Start to End — symphony_SymphonyEffect15
      case 16: // Olivary Flow — End to Start — symphony_SymphonyEffect16
        const olivaryDir = effectId === 15 ? direction : !direction;
        const oliv = [
          bg, bg, this.interpolate(fg, bg, 0.5), fg, fg, this.interpolate(fg, bg, 0.5), bg, bg,
          bg, bg, bg, bg, bg, bg, bg, bg
        ];
        return this.rotate(oliv, safeTick, !olivaryDir);

      case 17: // Running w/BG, 1pt — Start to End — symphony_SymphonyEffect17
      case 18: // Running w/BG, 1pt — End to Start — symphony_SymphonyEffect18
        const runBgDir = effectId === 17 ? direction : !direction;
        const runBgIdx = Math.floor(safeTick * 16) % 16;
        for (let i = 0; i < 16; i++) dots[i] = (i === runBgIdx) ? fg : bg;
        return this.rotate(dots, 0, !runBgDir);

      case 19: // 2-Color Run, Multi — Start to End — symphony_SymphonyEffect19
      case 20: // 2-Color Run, Multi — End to Start — symphony_SymphonyEffect20
        const run2Dir = effectId === 19 ? direction : !direction;
        const dash2 = [fg, fg, fg, fg, bg, bg, bg, bg, fg, fg, fg, fg, bg, bg, bg, bg];
        return this.rotate(dash2, safeTick, !run2Dir);

      case 21: // 2-Color Fade Alt — Start to End — symphony_SymphonyEffect21
      case 22: // 2-Color Fade Alt — End to Start — symphony_SymphonyEffect22
        const fadeAltDir = effectId === 21 ? direction : !direction;
        const tAlt = Math.abs(Math.sin(safeTick * Math.PI));
        const fadeAltPattern = [
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
          this.interpolate(fg, bg, tAlt), this.interpolate(bg, fg, tAlt),
        ];
        return this.rotate(fadeAltPattern, safeTick * 0.2, !fadeAltDir);

      case 23: // 2-Color Multi Alt — Start to End — symphony_SymphonyEffect23
      case 24: // 2-Color Multi Alt — End to Start — symphony_SymphonyEffect24
        const multiAltDir = effectId === 23 ? direction : !direction;
        const dashAlt = [fg, fg, fg, bg, bg, bg, fg, fg, fg, bg, bg, bg, fg, fg, fg, bg];
        return this.rotate(dashAlt, safeTick, !multiAltDir);

      case 25: // Fading Out Flow — Start to End — symphony_SymphonyEffect25
      case 26: // Fading Out Flow — End to Start — symphony_SymphonyEffect26
        const fadeFlowDir = effectId === 25 ? direction : !direction;
        const fadeFlow = [
          fg,
          this.interpolate(fg, bg, 0.15),
          this.interpolate(fg, bg, 0.35),
          this.interpolate(fg, bg, 0.55),
          this.interpolate(fg, bg, 0.75),
          this.interpolate(fg, bg, 0.9),
          ...Array(10).fill(bg)
        ];
        return this.rotate(fadeFlow, safeTick, !fadeFlowDir);

      // --- 7 Colors Auto Effects (27–33): hardware ignores FG/BG, uses built-in 7-color cycle ---
      case 27: // 7-Color Run, 1pt Multi BG — Start to End — symphony_SymphonyEffect27
      case 28: // 7-Color Run, 1pt Multi BG — End to Start — symphony_SymphonyEffect28
      case 29: // 7-Color Run, 1pt — Start to End — symphony_SymphonyEffect29
      case 30: // 7-Color Run, 1pt — End to Start — symphony_SymphonyEffect30
      case 31: // 7-Color Run, Multi — Start to End — symphony_SymphonyEffect31
      case 32: // 7-Color Run, Multi — End to Start — symphony_SymphonyEffect32
      case 33: // 7-Color Overlay, Multi — Start to End — symphony_SymphonyEffect33
      default: {
        const C7 = [
          {r:255,g:0,b:0}, {r:0,g:255,b:0}, {r:0,g:0,b:255},
          {r:255,g:255,b:0}, {r:0,g:255,b:255}, {r:255,g:0,b:255}, {r:255,g:255,b:255}
        ];
        const auto7Dir = (effectId % 2 === 0) ? !direction : direction; // even IDs = reverse
        for (let i = 0; i < 16; i++) dots[i] = C7[i % 7];
        return this.rotate(dots, safeTick, !auto7Dir);
      }
    }
  }
}

