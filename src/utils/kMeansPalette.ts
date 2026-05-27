export interface RGB {
  r: number;
  g: number;
  b: number;
}

/**
 * Calculates Euclidean distance between two colors in 3D RGB space.
 */
export function getDistance(c1: RGB, c2: RGB): number {
  'worklet';
  return Math.sqrt(
    Math.pow(c1.r - c2.r, 2) +
    Math.pow(c1.g - c2.g, 2) +
    Math.pow(c1.b - c2.b, 2)
  );
}

/**
 * Extracts k dominant colors from an array of RGB pixels using the K-Means clustering algorithm.
 * Optimized for performance: 5 iterations max, hard-coded 3 clusters.
 * Returns colors sorted from most dominant to least dominant.
 */
export function extractKMeansPalette(pixels: RGB[], k = 3, maxIterations = 5): RGB[] {
  'worklet';
  if (pixels.length === 0) {
    return [
      { r: 255, g: 0, b: 0 },
      { r: 0, g: 255, b: 0 },
      { r: 0, g: 0, b: 255 },
    ];
  }

  // 1. Initialize centroids by picking evenly-spaced samples from the dataset
  const centroids: RGB[] = [];
  for (let i = 0; i < k; i++) {
    const idx = Math.floor((i / k) * pixels.length);
    if (pixels[idx]) {
      centroids.push({ ...pixels[idx] });
    } else {
      centroids.push({ r: Math.random() * 255, g: Math.random() * 255, b: Math.random() * 255 });
    }
  }

  // Iteration loop
  for (let iter = 0; iter < maxIterations; iter++) {
    // Clusters holds pixel lists for each centroid
    const clusters: RGB[][] = Array.from({ length: k }, () => []);

    // 2. Assign each pixel to the nearest centroid
    for (let i = 0; i < pixels.length; i++) {
      const pixel = pixels[i];
      let minDistance = Infinity;
      let closestCentroidIndex = 0;

      for (let c = 0; c < k; c++) {
        const dist = getDistance(pixel, centroids[c]);
        if (dist < minDistance) {
          minDistance = dist;
          closestCentroidIndex = c;
        }
      }
      clusters[closestCentroidIndex].push(pixel);
    }

    // 3. Recalculate centroids as the mean of their assigned pixels
    let changed = false;
    for (let c = 0; c < k; c++) {
      const cluster = clusters[c];
      if (cluster.length === 0) continue;

      let sumR = 0;
      let sumG = 0;
      let sumB = 0;
      for (let i = 0; i < cluster.length; i++) {
        sumR += cluster[i].r;
        sumG += cluster[i].g;
        sumB += cluster[i].b;
      }

      const meanR = Math.round(sumR / cluster.length);
      const meanG = Math.round(sumG / cluster.length);
      const meanB = Math.round(sumB / cluster.length);

      if (meanR !== centroids[c].r || meanG !== centroids[c].g || meanB !== centroids[c].b) {
        centroids[c] = { r: meanR, g: meanG, b: meanB };
        changed = true;
      }
    }

    if (!changed) break; // Early exit if centroids stabilized
  }

  // Count the size of each cluster to sort them by dominance
  const clusterCounts = Array.from({ length: k }, () => 0);
  for (let i = 0; i < pixels.length; i++) {
    const pixel = pixels[i];
    let minDistance = Infinity;
    let closestCentroidIndex = 0;

    for (let c = 0; c < k; c++) {
      const dist = getDistance(pixel, centroids[c]);
      if (dist < minDistance) {
        minDistance = dist;
        closestCentroidIndex = c;
      }
    }
    clusterCounts[closestCentroidIndex]++;
  }

  // Combine centroids and their counts, then sort by count descending
  const sortedCentroids = centroids
    .map((c, i) => ({ color: c, count: clusterCounts[i] }))
    .sort((a, b) => b.count - a.count)
    .map(item => item.color);

  // Return exactly k elements, fallback if smaller
  while (sortedCentroids.length < k) {
    sortedCentroids.push({ r: 0, g: 0, b: 0 });
  }

  return sortedCentroids.slice(0, k);
}
