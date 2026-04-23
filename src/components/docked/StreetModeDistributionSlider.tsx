import React, { useRef, useState, useMemo } from 'react';
import { View, Text, PanResponder, useWindowDimensions, Animated } from 'react-native';

interface StreetModeDistributionSliderProps {
  distribution: [number, number, number]; // [tail, cruise, head]
  onChange: (newDist: [number, number, number]) => void;
  tailColor: string;
  cruiseColor: string;
  headColor: string;
  isShort?: boolean;
}

const MIN_PERCENT = 0.1; // Minimum 10% for any zone

const StreetModeDistributionSlider: React.FC<StreetModeDistributionSliderProps> = ({
  distribution,
  onChange,
  tailColor,
  cruiseColor,
  headColor,
  isShort = false,
}) => {
  const [width, setWidth] = useState(0);
  
  // Track thumb positions as percentages [0..1]
  const thumb1Ref = useRef(distribution[0]);
  const thumb2Ref = useRef(distribution[0] + distribution[1]);

  const [t1Pos, setT1Pos] = useState(thumb1Ref.current);
  const [t2Pos, setT2Pos] = useState(thumb2Ref.current);

  const panResponder1 = useMemo(() => PanResponder.create({
    onStartShouldSetPanResponder: () => true,
    onMoveShouldSetPanResponder: () => true,
    onPanResponderGrant: () => {},
    onPanResponderMove: (_, gestureState) => {
      if (width === 0) return;
      let newT1 = thumb1Ref.current + gestureState.dx / width;
      // Clamp between minimum constraint and thumb2
      newT1 = Math.max(MIN_PERCENT, Math.min(newT1, t2Pos - MIN_PERCENT));
      setT1Pos(newT1);
    },
    onPanResponderRelease: (_, gestureState) => {
      if (width === 0) return;
      let newT1 = thumb1Ref.current + gestureState.dx / width;
      newT1 = Math.max(MIN_PERCENT, Math.min(newT1, t2Pos - MIN_PERCENT));
      thumb1Ref.current = newT1;
      setT1Pos(newT1);
      onChange([newT1, t2Pos - newT1, 1 - t2Pos]);
    },
  }), [width, t2Pos, onChange]);

  const panResponder2 = useMemo(() => PanResponder.create({
    onStartShouldSetPanResponder: () => true,
    onMoveShouldSetPanResponder: () => true,
    onPanResponderGrant: () => {},
    onPanResponderMove: (_, gestureState) => {
      if (width === 0) return;
      let newT2 = thumb2Ref.current + gestureState.dx / width;
      // Clamp between thumb1 and max constraint
      newT2 = Math.max(t1Pos + MIN_PERCENT, Math.min(newT2, 1 - MIN_PERCENT));
      setT2Pos(newT2);
    },
    onPanResponderRelease: (_, gestureState) => {
      if (width === 0) return;
      let newT2 = thumb2Ref.current + gestureState.dx / width;
      newT2 = Math.max(t1Pos + MIN_PERCENT, Math.min(newT2, 1 - MIN_PERCENT));
      thumb2Ref.current = newT2;
      setT2Pos(newT2);
      onChange([t1Pos, newT2 - t1Pos, 1 - newT2]);
    },
  }), [width, t1Pos, onChange]);

  const height = isShort ? 22 : 26;
  const thumbSize = height + 10;

  return (
    <View 
      style={{ width: '100%', paddingVertical: 10 }}
      onLayout={(e) => setWidth(e.nativeEvent.layout.width)}
    >
      <View style={{ height, borderRadius: height / 2, overflow: 'hidden', flexDirection: 'row', backgroundColor: '#000', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)' }}>
        
        {/* Tail Zone */}
        <View style={{ width: `${t1Pos * 100}%`, backgroundColor: tailColor, justifyContent: 'center', alignItems: 'center' }}>
          {t1Pos > 0.15 && <Text allowFontScaling={false} style={{ color: '#FFF', fontSize: 9, fontWeight: '800' }}>TAIL ({Math.round(t1Pos * 100)}%)</Text>}
        </View>

        {/* Cruise Zone */}
        <View style={{ width: `${(t2Pos - t1Pos) * 100}%`, backgroundColor: cruiseColor, justifyContent: 'center', alignItems: 'center', opacity: 0.9 }}>
          {(t2Pos - t1Pos) > 0.2 && <Text allowFontScaling={false} style={{ color: '#000', fontSize: 9, fontWeight: '800' }}>CRUISE ({Math.round((t2Pos - t1Pos) * 100)}%)</Text>}
        </View>

        {/* Head Zone */}
        <View style={{ width: `${(1 - t2Pos) * 100}%`, backgroundColor: headColor, justifyContent: 'center', alignItems: 'center' }}>
          {(1 - t2Pos) > 0.15 && <Text allowFontScaling={false} style={{ color: '#333', fontSize: 9, fontWeight: '800' }}>HEAD ({Math.round((1 - t2Pos) * 100)}%)</Text>}
        </View>

      </View>

      {/* Thumb 1 */}
      {width > 0 && (
        <View 
          {...panResponder1.panHandlers}
          style={{
            position: 'absolute',
            top: 10 - (thumbSize - height) / 2,
            left: t1Pos * width - thumbSize / 2,
            width: thumbSize,
            height: thumbSize,
            borderRadius: thumbSize / 2,
            backgroundColor: '#FFF',
            borderWidth: 2,
            borderColor: '#333',
            shadowColor: '#000',
            shadowOpacity: 0.5,
            shadowRadius: 5,
            elevation: 5,
            justifyContent: 'center',
            alignItems: 'center',
          }}
        >
          <View style={{ width: 2, height: 12, backgroundColor: '#888', borderRadius: 1 }} />
        </View>
      )}

      {/* Thumb 2 */}
      {width > 0 && (
        <View 
          {...panResponder2.panHandlers}
          style={{
            position: 'absolute',
            top: 10 - (thumbSize - height) / 2,
            left: t2Pos * width - thumbSize / 2,
            width: thumbSize,
            height: thumbSize,
            borderRadius: thumbSize / 2,
            backgroundColor: '#FFF',
            borderWidth: 2,
            borderColor: '#333',
            shadowColor: '#000',
            shadowOpacity: 0.5,
            shadowRadius: 5,
            elevation: 5,
            justifyContent: 'center',
            alignItems: 'center',
          }}
        >
          <View style={{ width: 2, height: 12, backgroundColor: '#888', borderRadius: 1 }} />
        </View>
      )}
    </View>
  );
};

export default StreetModeDistributionSlider;
