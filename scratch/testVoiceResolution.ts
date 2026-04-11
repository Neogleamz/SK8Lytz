/**
 * Scratch script to verify VoiceService command resolution logic.
 */
import { resolveCommand } from './src/services/VoiceService';

const mockFavorites = [
  { id: 'fav1', name: 'Rainbow Blast', mode: 'MULTIMODE' },
  { id: 'fav2', name: 'Night Rider', mode: 'PROGRAMS' }
];

const testCommands = [
  "Street mode",
  "Switch to music",
  "Rainbow blast",
  "Set brightness to 80%",
  "Go faster",
  "I want red in the back and white in the front"
];

console.log("--- VOICE COMMAND RESOLUTION TEST ---");

testCommands.forEach(cmd => {
  const resolved = resolveCommand(cmd, mockFavorites as any);
  console.log(`Input: "${cmd}"`);
  console.log(`Resolved:`, JSON.stringify(resolved, null, 2));
  console.log("-------------------");
});
