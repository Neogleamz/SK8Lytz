import { extractKMeansPalette, getDistance, RGB } from '../kMeansPalette';

describe('kMeansPalette', () => {
  describe('getDistance', () => {
    it('should calculate Euclidean distance between two colors correctly', () => {
      const c1: RGB = { r: 0, g: 0, b: 0 };
      const c2: RGB = { r: 255, g: 0, b: 0 };
      expect(getDistance(c1, c2)).toBe(255);

      const c3: RGB = { r: 100, g: 150, b: 200 };
      const c4: RGB = { r: 120, g: 130, b: 210 };
      // sqrt((120-100)^2 + (130-150)^2 + (210-200)^2) = sqrt(20^2 + (-20)^2 + 10^2) = sqrt(400 + 400 + 100) = sqrt(900) = 30
      expect(getDistance(c3, c4)).toBeCloseTo(30);
    });
  });

  describe('extractKMeansPalette', () => {
    it('should return default colors if pixel array is empty', () => {
      const result = extractKMeansPalette([]);
      expect(result).toHaveLength(3);
      expect(result).toEqual([
        { r: 255, g: 0, b: 0 },
        { r: 0, g: 255, b: 0 },
        { r: 0, g: 0, b: 255 },
      ]);
    });

    it('should extract dominant colors from simple clusters', () => {
      const pixels: RGB[] = [
        // Red cluster (dominant)
        { r: 255, g: 10, b: 10 },
        { r: 250, g: 5, b: 5 },
        { r: 255, g: 0, b: 0 },
        { r: 245, g: 15, b: 15 },
        { r: 255, g: 12, b: 12 },

        // Green cluster
        { r: 10, g: 255, b: 10 },
        { r: 5, g: 250, b: 5 },
        { r: 0, g: 255, b: 0 },

        // Blue cluster (least dominant)
        { r: 10, g: 10, b: 255 },
        { r: 5, g: 5, b: 250 },
      ];

      const result = extractKMeansPalette(pixels, 3, 5);
      expect(result).toHaveLength(3);

      // First color (most dominant) should be red-ish (approx r > 240, g < 20, b < 20)
      expect(result[0].r).toBeGreaterThan(240);
      expect(result[0].g).toBeLessThan(20);
      expect(result[0].b).toBeLessThan(20);

      // Second color should be green-ish
      expect(result[1].g).toBeGreaterThan(240);
      expect(result[1].r).toBeLessThan(20);
      expect(result[1].b).toBeLessThan(20);

      // Third color should be blue-ish
      expect(result[2].b).toBeGreaterThan(240);
      expect(result[2].r).toBeLessThan(20);
      expect(result[2].g).toBeLessThan(20);
    });

    it('should always return exactly k colors even if unique data points are fewer', () => {
      const pixels: RGB[] = [
        { r: 255, g: 0, b: 0 },
        { r: 255, g: 0, b: 0 },
      ];
      const result = extractKMeansPalette(pixels, 3, 5);
      expect(result).toHaveLength(3);
    });
  });
});
