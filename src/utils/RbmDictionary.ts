/**
 * RBM Dictionary
 * Mathematically categorizes SymphonyBuild 1-102 into visual Animated Archetypes.
 */
export const RbmDictionary = {
  MARQUEE: [
    10, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
    30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
    47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61
  ],
  METEOR: [] as number[],
  BREATHING: [2, 11, 13],
  STROBE: [4, 5],
  OUTLIERS: [
    1, 3, 6, 7, 8, 9, 12, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
    73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88,
    89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102
  ]
};

export const getArchetypeFromId = (id: number): 'MARQUEE' | 'METEOR' | 'BREATHING' | 'STROBE' | 'OUTLIERS' => {
  if (RbmDictionary.MARQUEE.includes(id)) return 'MARQUEE';
  if (RbmDictionary.METEOR.includes(id)) return 'METEOR';
  if (RbmDictionary.BREATHING.includes(id)) return 'BREATHING';
  if (RbmDictionary.STROBE.includes(id)) return 'STROBE';
  return 'OUTLIERS';
};
