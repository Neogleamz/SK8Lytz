/**
 * MusicDictionary.ts
 * Maps Zengge Protocol Music Pattern IDs to specific Visualizer string animation Archetypes.
 * 
 * Hardware Protocol Map:
 * 1 = Rock
 * 2 = Normal
 * 3 = Jazz
 * 4 = Classical
 */

export type MusicArchetype = 'PULSE' | 'WAVE' | 'STROBE' | 'VU_METER';

export interface MusicPatternProfile {
    id: number;
    name: string;
    archetype: MusicArchetype;
}

export const MusicDictionary: Record<number, MusicPatternProfile> = {
    1: { id: 1, name: 'Rock', archetype: 'WAVE' },
    2: { id: 2, name: 'Normal', archetype: 'PULSE' },
    3: { id: 3, name: 'Jazz', archetype: 'VU_METER' },
    4: { id: 4, name: 'Classical', archetype: 'STROBE' },
};
