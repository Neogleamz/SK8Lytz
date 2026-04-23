import { useState, useCallback } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { hexToRgb } from '../utils/ColorUtils';
import { ScenesService, Scene } from '../services/ScenesService';
import { useAuth } from './useAuth';

export interface SceneStep {
  id: string;
  mode: 'pattern' | 'symphony';
  effectId: number;
  fg: string;
  bg: string;
  speed: number;
  duration: number;
  direction: 0 | 1;
}

export function useSceneBuilder(writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>) {
  const [steps, setSteps] = useState<SceneStep[]>([]);
  const [sceneName, setSceneName] = useState<string>('New Scene');
  const [sceneId, setSceneId] = useState<string | null>(null);
  
  const { session } = useAuth();
  const userId = session?.user?.id;

  const addStep = useCallback((step: Omit<SceneStep, 'id'>) => {
    if (steps.length >= 32) return;
    setSteps(prev => [...prev, { ...step, id: Math.random().toString(36).substr(2, 9) }]);
  }, [steps]);

  const updateStep = useCallback((id: string, updates: Partial<SceneStep>) => {
    setSteps(prev => prev.map(s => s.id === id ? { ...s, ...updates } : s));
  }, []);

  const removeStep = useCallback((id: string) => {
    setSteps(prev => prev.filter(s => s.id !== id));
  }, []);

  const reorderSteps = useCallback((startIndex: number, endIndex: number) => {
    setSteps(prev => {
      const result = Array.from(prev);
      const [removed] = result.splice(startIndex, 1);
      result.splice(endIndex, 0, removed);
      return result;
    });
  }, []);

  const fireToSkates = useCallback(() => {
    if (!writeToDevice || steps.length === 0) return;

    // Use setCustomModeExtended for 32-slot (10-byte slots)
    // The plan specifies 0x51 payload via writeChunked.
    // Our existing setCustomModeExtended generates the full 323-byte payload.
    // The BLE layer will handle chunking if necessary.
    const payloadSteps = steps.map(s => ({
      mode: s.effectId,
      speed: s.speed, // Hardware speed 1-100 will be clamped internally
      color1: hexToRgb(s.fg),
      color2: hexToRgb(s.bg),
      dir: s.direction
    }));

    const payload = ZenggeProtocol.setCustomModeExtended(payloadSteps);
    writeToDevice(payload);
  }, [steps, writeToDevice]);

  const saveScene = useCallback(async (nameToSave: string, isPublic: boolean = false) => {
    try {
      const newId = sceneId || Math.random().toString(36).substring(2, 15);
      const scenePayload: Scene = {
        id: newId,
        name: nameToSave,
        steps,
        created_at: new Date().toISOString(),
        user_id: userId
      };

      // 1. Save Locally and to User Cloud
      await ScenesService.saveScene(scenePayload, userId);

      // 2. Publish to Community Cloud
      if (isPublic) {
        await ScenesService.publishScene(nameToSave, scenePayload, isPublic);
      }

      setSceneId(newId);
      setSceneName(nameToSave);
      return true;
    } catch (err) {
      console.error('[useSceneBuilder] Save failed:', err);
      return false;
    }
  }, [steps, sceneId, userId]);

  const loadScene = useCallback((scene: Scene) => {
    setSceneId(scene.id);
    setSceneName(scene.name);
    setSteps(scene.steps);
  }, []);

  return {
    steps,
    sceneName,
    setSceneName,
    addStep,
    updateStep,
    removeStep,
    reorderSteps,
    fireToSkates,
    saveScene,
    loadScene
  };
}
