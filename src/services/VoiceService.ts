import { Platform } from 'react-native';
import { AppLogger } from './AppLogger';
let Voice: any;
let SpeechResultsEvent: any;
let SpeechErrorEvent: any;
try {
  if (Platform.OS !== 'web') {
    const rmVoice = require('@react-native-voice/voice');
    Voice = rmVoice.default || rmVoice;
  }
} catch (e) {
  // Native module not linked
}

import * as Speech from 'expo-speech';
import * as stringSimilarity from 'string-similarity';
import type { IFavoriteState } from '../types/dashboard.types';
import { RBM_PATTERNS } from '../utils/RbmDictionary';

export interface IVoiceAction {
  type: 'MODE' | 'FAVORITE' | 'PATTERN' | 'BRIGHTNESS' | 'SPEED' | 'SPATIAL' | 'POWER' | 'UNKNOWN';
  value?: any;
  favoriteId?: string;
  favorite?: IFavoriteState;
  patternId?: number;
  segments?: ISpatialSegment[];
  originalText: string;
}

export interface ISpatialSegment {
  position: 'BACK' | 'MIDDLE' | 'FRONT' | 'ALL';
  color: string;
}

const COLORS: Record<string, string> = {
  red: '#FF0000',
  green: '#00FF00',
  blue: '#0066FF',
  yellow: '#FFFF00',
  white: '#FFFFFF',
  purple: '#AA00FF',
  cyan: '#00FFFF',
  orange: '#FF8C00',
  amber: '#FFD700',
  pink: '#FFC0CB',
  magenta: '#FF00FF',
};

const MODE_MAP: Record<string, string> = {
  street: 'STREET',
  camera: 'CAMERA',
  music: 'MUSIC',
  favorites: 'FAVORITES',
  styles: 'FAVORITES',
  multi: 'MULTIMODE',
  builder: 'BUILDER',
  programs: 'PROGRAMS',
};

class VoiceService {
  constructor() {
    if (Voice) {
      Voice.onSpeechResults = this.onSpeechResults.bind(this);
      Voice.onSpeechError = this.onSpeechError.bind(this);
    }
  }

  private onSpeechResults(e: any) {
    if (e.value) {
      AppLogger.log('VOICE_RESULT' as any, { transcript: e.value[0] });
    }
  }

  private onSpeechError(e: any) {
    AppLogger.log('VOICE_ERROR' as any, { error: e.error });
  }

  async startListening() {
    if (!Voice) return;
    try {
      await Voice.start('en-US');
    } catch (e) {
      AppLogger.log('VOICE_ERROR' as any, { error: e });
    }
  }

  async stopListening() {
    if (!Voice) return;
    try {
      await Voice.stop();
    } catch (e) {
      AppLogger.log('VOICE_ERROR' as any, { error: e });
    }
  }

  /**
   * TTS Feedback
   */
  async announce(text: string) {
    Speech.speak(text, { pitch: 1.1, rate: 1.0 });
  }

  /**
   * Main command resolver
   */
  resolveCommand(text: string, favorites: IFavoriteState[]): IVoiceAction {
    const t = text.toLowerCase();

    // 1. Check for Power commands
    if (t.includes('power off') || t.includes('lights off') || t.includes('kill lights')) {
      return { type: 'POWER', value: false, originalText: text };
    }
    if (t.includes('power on') || t.includes('lights on')) {
      return { type: 'POWER', value: true, originalText: text };
    }

    // 2. Check for Mode switches
    for (const [key, mode] of Object.entries(MODE_MAP)) {
      if (t.includes(`${key} mode`) || t === key) {
        return { type: 'MODE', value: mode, originalText: text };
      }
    }

    // 3. Check for Brightness/Speed
    const numMatch = t.match(/(\d+)/);
    if (t.includes('brightness') && numMatch) {
      return { type: 'BRIGHTNESS', value: parseInt(numMatch[0]), originalText: text };
    }
    if (t.includes('speed') && numMatch) {
      return { type: 'SPEED', value: parseInt(numMatch[0]), originalText: text };
    }
    
    // Relative changes
    if (t.includes('brighter') || t.includes('increase brightness')) return { type: 'BRIGHTNESS', value: '+20', originalText: text };
    if (t.includes('dimmer') || t.includes('decrease brightness')) return { type: 'BRIGHTNESS', value: '-20', originalText: text };
    if (t.includes('faster') || t.includes('speed up')) return { type: 'SPEED', value: '+20', originalText: text };
    if (t.includes('slower') || t.includes('slow down')) return { type: 'SPEED', value: '-20', originalText: text };

    // 4. Spatial Parsing (The "Complex" feature)
    const spatialSegments: ISpatialSegment[] = [];
    const colorsRegex = Object.keys(COLORS).join('|');
    const posRegex = 'back|middle|front|toe|heel|center';
    const matches = Array.from(t.matchAll(new RegExp(`(${colorsRegex})\\s+(in|at)\\s+(the\\s+)?(${posRegex})`, 'gi')));
    
    for (const match of matches) {
      const colorKey = match[1].toLowerCase();
      const posKey = match[4].toLowerCase();
      let position: 'BACK' | 'MIDDLE' | 'FRONT' = 'MIDDLE';
      if (posKey === 'back' || posKey === 'heel') position = 'BACK';
      if (posKey === 'front' || posKey === 'toe') position = 'FRONT';
      if (posKey === 'middle' || posKey === 'center') position = 'MIDDLE';
      
      spatialSegments.push({ position, color: COLORS[colorKey] });
    }

    if (spatialSegments.length > 0) {
      return { type: 'SPATIAL', value: spatialSegments, originalText: text };
    }

    // 5. Fuzzy Match Favorites
    const favoriteNames = favorites.map(f => f.name || f.customName || '');
    if (favoriteNames.length > 0) {
      const match = stringSimilarity.findBestMatch(t, favoriteNames);
      if (match.bestMatch.rating > 0.6) {
        const found = favorites[match.bestMatchIndex];
        return { type: 'FAVORITE', favoriteId: found.id, favorite: found, originalText: text };
      }
    }

    if (t.includes('pattern') || t.includes('show')) {
      const patternNames = RBM_PATTERNS.map(p => p.name.toLowerCase());
      const bestPattern = stringSimilarity.findBestMatch(t.replace('pattern ', '').replace('show ', ''), patternNames);
      if (bestPattern.bestMatch.rating > 0.5) {
        const targetPattern = RBM_PATTERNS[bestPattern.bestMatchIndex];
        return { type: 'PATTERN', patternId: targetPattern.id, originalText: text };
      }
    }

    return { type: 'UNKNOWN', originalText: text };
  }
}

export const voiceService = new VoiceService();
