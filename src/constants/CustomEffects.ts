export interface CustomEffectMetadata {
  id: number;
  name: string;
  requiresForeground: boolean;
  requiresBackground: boolean;
  supportsSegment: boolean;
  supportsDirection: boolean;
}

export const ZENGGE_EFFECTS: CustomEffectMetadata[] = [
  { id: 1, name: "Change gradually", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 2, name: "Bright up and Fade gradually", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 3, name: "Change quickly", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 4, name: "Strobe-flash", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 5, name: "Running, 1 point", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 6, name: "Running, 1 point from middle", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 7, name: "Overlay", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 8, name: "Overlay from middle", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 9, name: "Fading and running, 1 point", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 10, name: "Olivary Flowing", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 11, name: "Running, 1 point with Background", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 12, name: "2 colors run, multi points with Background", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 13, name: "2 colors run alternately, fading", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 14, name: "2 colors run alternately, multi points", requiresForeground: true, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 15, name: "Fading out Flows", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 16, name: "7 colors run alternately, 1 point with multi background", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 17, name: "7 colors run alternately, 1 point", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 18, name: "7 colors run alternately, multi points", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 19, name: "7 colors overlay, multi points", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 20, name: "7 colors overlay from middle", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 21, name: "7 colors flow gradually", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 22, name: "Fading out run, 7 colors", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 23, name: "Runs in olivary, 7 colors", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 24, name: "Fading out run, 7 colors start with white color", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 25, name: "7 colors run with background", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: true },
  { id: 26, name: "7 colors run with black background", requiresForeground: false, requiresBackground: false, supportsSegment: false, supportsDirection: true },
  { id: 27, name: "7 colors run gradually + 7 colors run in olivary", requiresForeground: false, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 28, name: "7 colors run gradually + 7 colors change quickly", requiresForeground: false, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 29, name: "7 colors run gradually + 7 colors flash", requiresForeground: false, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 30, name: "7 colors run in olivary + 7 colors change quickly", requiresForeground: false, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 31, name: "7 colors run in olivary + 7 colors flash", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 32, name: "7 colors change quickly + 7 colors flash", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 33, name: "All 7 color effects mixed", requiresForeground: false, requiresBackground: true, supportsSegment: false, supportsDirection: false },
];
