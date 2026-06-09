import { useState, useEffect, useMemo, useRef } from 'react';
import { WebVisualizerUnit } from './WebVisualizerUnit';
import { SK8LYTZ_TEMPLATES } from '../../../protocols/PatternEngine';

interface DeviceConfig {
  id?: string;
  name?: string;
  type?: string;
  points?: number;
  segments?: number;
}

interface WebProductVisualizerProps {
  product: string;
  color: string;
  mode: string;
  patternId: number | null;
  isPaired?: boolean;
  points?: number;
  hwSettings?: { ledPoints?: number; segments?: number };
  devices?: DeviceConfig[];
  fixedFgColor?: string;
  fixedBgColor?: string;
  brightness?: number;
  speed?: number;
  isPoweredOn?: boolean;
  audioMagnitude?: number;
  multiColors?: string[];
  multiTransition?: number;
  isStreetBraking?: boolean;
  streetCruiseColor?: string;
  motionState?: string;
  builderNodes?: any[];
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;
  builderDirection?: number;
  fixedDirection?: number;
  streetDistribution?: [number, number, number];
}

const EMPTY_MULTI_COLORS: string[] = [];
const EMPTY_BUILDER_NODES: any[] = [];
const DEFAULT_STREET_DISTRIBUTION: [number, number, number] = [0.3, 0.4, 0.3];

export const WebProductVisualizer = ({
  product, color, mode, patternId, isPaired, points, hwSettings, devices,
  fixedFgColor, fixedBgColor, brightness = 100, speed = 50, isPoweredOn = true,
  audioMagnitude = 0, multiColors = EMPTY_MULTI_COLORS, multiTransition = 0,
  isStreetBraking = false, streetCruiseColor = '#FF8C00', motionState = 'STOPPED',
  builderNodes = EMPTY_BUILDER_NODES, builderFillMode = 'GRADIENT',
  builderTransitionType = 0x01, builderDirection = 1, fixedDirection = 1,
  streetDistribution = DEFAULT_STREET_DISTRIBUTION
}: WebProductVisualizerProps) => {

  const [animTick, setAnimTick] = useState(0);
  const rafRef = useRef<number | null>(null);
  const startTimeRef = useRef(performance.now());
  const loopDurationRef = useRef(1000);

  useEffect(() => {
    if (isPoweredOn && (mode === 'BUILDER' || mode === 'STREET' || mode === 'FAVORITES' || mode === 'MUSIC' || mode === 'MULTIMODE')) {
      const isBreathing = patternId != null && (
        SK8LYTZ_TEMPLATES.find(t => t.id === patternId)?.group === 'Breathe' || 
        SK8LYTZ_TEMPLATES.find(t => t.id === patternId)?.name.includes('Breath')
      );

      const baseDuration =
        mode === 'MUSIC'    ? 800  :
        mode === 'BUILDER'  ? (builderTransitionType === 0x03 ? 350 : (isBreathing ? 3000 : 1500)) :
        mode === 'MULTIMODE'? 1500 :
        mode === 'STREET'   ? 1400 : (isBreathing ? 6000 : 3000);
      
      loopDurationRef.current = baseDuration / (0.4 + (speed / 100) * 2.1);
      startTimeRef.current = performance.now();

      const animate = (time: number) => {
        const elapsed = time - startTimeRef.current;
        let progress = (elapsed % loopDurationRef.current) / loopDurationRef.current;
        setAnimTick(progress);
        rafRef.current = requestAnimationFrame(animate);
      };

      rafRef.current = requestAnimationFrame(animate);
      return () => {
        if (rafRef.current) cancelAnimationFrame(rafRef.current);
      };
    } else {
      setAnimTick(1);
    }
  }, [product, mode, color, patternId, speed, isPoweredOn, JSON.stringify(multiColors), multiTransition, builderTransitionType]);

  const renderDevices = useMemo(() => {
    return (devices && devices.length > 0) ? devices : (
      isPaired
        ? [{ id: 'mock-left', name: `${product} Left`, type: product, points }, { id: 'mock-right', name: `${product} Right`, type: product, points }]
        : [{ id: 'mock-single', name: product, type: product, points }]
    );
  }, [devices, isPaired, product, points]);

  return (
    <div className="flex flex-col items-center justify-center bg-black rounded-xl border border-white/10 p-4 min-h-[160px] w-full">
      <div className="flex flex-row flex-wrap justify-center items-center gap-8 pt-4">
        {renderDevices.map((dev, index) => (
          <WebVisualizerUnit
            key={dev.id || index.toString()}
            device={dev}
            color={color}
            mode={mode}
            patternId={patternId}
            animTick={animTick}
            fallbackProduct={product}
            fallbackPoints={hwSettings?.ledPoints || points}
            hwSettings={hwSettings}
            fixedFgColor={fixedFgColor}
            fixedBgColor={fixedBgColor}
            brightness={brightness}
            isPoweredOn={isPoweredOn}
            audioMagnitude={audioMagnitude}
            multiColors={multiColors}
            multiTransition={multiTransition}
            isStreetBraking={isStreetBraking}
            streetCruiseColor={streetCruiseColor}
            motionState={motionState}
            builderNodes={builderNodes}
            builderFillMode={builderFillMode}
            builderTransitionType={builderTransitionType}
            builderDirection={builderDirection}
            fixedDirection={fixedDirection}
            streetDistribution={streetDistribution}
          />
        ))}
      </div>
    </div>
  );
}
