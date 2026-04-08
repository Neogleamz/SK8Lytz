export interface BuilderNode {
  id: string;
  position: number; // 0 to 100 percentage layout
  colorHex: string; // #RRGGBB format
}

export class PositionalMathBuffer {
  static hexToRgb(hex: string) {
    const h = (hex || '#000000').replace('#', '');
    return {
      r: parseInt(h.substring(0, 2), 16) || 0,
      g: parseInt(h.substring(2, 4), 16) || 0,
      b: parseInt(h.substring(4, 6), 16) || 0,
    };
  }

  /**
   * Generates a fully mathematically interpolated string of LED colors 
   * specifically built to bypass the positional math limits of the `0x59` hardware chunker.
   */
  static generateArray(nodes: BuilderNode[], totalLeds: number, isGradient: boolean): { r: number, g: number, b: number }[] {
    const defaultColor = { r: 0, g: 0, b: 0 };
    if (!nodes || nodes.length === 0) {
       return Array.from({ length: totalLeds }).map(() => defaultColor);
    }
    
    // Sort nodes securely from left to right (0 to 100%)
    const sorted = [...nodes].sort((a, b) => a.position - b.position);

    // If only one node exists, fill entire strip with it exactly
    if (sorted.length === 1) {
       const fillRgb = this.hexToRgb(sorted[0].colorHex);
       return Array.from({ length: totalLeds }).map(() => fillRgb);
    }

    // Map percentage nodes to explicit literal LED indexes safely bounded to (0...totalLeds - 1)
    interface MappedNode {
       index: number;
       rgb: { r: number, g: number, b: number };
    }
    
    const mapped: MappedNode[] = sorted.map(n => {
       const perc = Math.max(0, Math.min(100, n.position)) / 100.0;
       return {
          index: Math.max(0, Math.min(totalLeds - 1, Math.round(perc * (totalLeds - 1)))),
          rgb: this.hexToRgb(n.colorHex)
       };
    });

    const output: { r: number, g: number, b: number }[] = [];
    
    // Iterate and paint each LED slot up to totalLeds
    for (let i = 0; i < totalLeds; i++) {
        let leftNode = mapped[0];
        let rightNode = mapped[mapped.length - 1];

        // Find the tightest visual boundaries binding this specific LED index
        for (let j = 0; j < mapped.length; j++) {
            if (mapped[j].index <= i) leftNode = mapped[j];
        }
        for (let j = mapped.length - 1; j >= 0; j--) {
            if (mapped[j].index >= i) rightNode = mapped[j];
        }

        // Out-of-bounds safety paddings:
        if (i <= mapped[0].index) {
             output.push(mapped[0].rgb);
             continue;
        }
        if (i >= mapped[mapped.length - 1].index) {
             output.push(mapped[mapped.length - 1].rgb);
             continue;
        }

        // "Pure / Fill-In" Mode: Render hard blocks
        if (!isGradient) {
             output.push(leftNode.rgb);
             continue;
        }

        // "Gradual / Gradient" Mode: Float interpolate across the gap gapSize
        const gapSize = rightNode.index - leftNode.index;
        if (gapSize === 0) {
             output.push(leftNode.rgb);
        } else {
             const progress = (i - leftNode.index) / gapSize; 
             output.push({
                 r: Math.round(leftNode.rgb.r + (rightNode.rgb.r - leftNode.rgb.r) * progress),
                 g: Math.round(leftNode.rgb.g + (rightNode.rgb.g - leftNode.rgb.g) * progress),
                 b: Math.round(leftNode.rgb.b + (rightNode.rgb.b - leftNode.rgb.b) * progress),
             });
        }
    }
    
    return output;
  }
}
