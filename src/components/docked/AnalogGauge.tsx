/**
 * AnalogGauge.tsx — SVG-based analog gauge with needle, ticks, and danger zones.
 *
 * Extracted from DockedController.tsx to eliminate god-object coupling.
 * Used in Street Mode to display GPS speed and G-force telemetry.
 */
import React from 'react';
import { View, Text } from 'react-native';
import Svg, { Path, Circle, Defs, LinearGradient as SvgLinearGradient, Stop } from 'react-native-svg';
import { Spacing } from '../../theme/theme';

export interface IAnalogGaugeProps {
  value: number;
  min: number;
  max: number;
  label: string;
  unit?: string;
  defaultColor?: string;
  size?: number;
  dangerVal?: number;
  criticalVal?: number;
}

const AnalogGauge = React.memo(({
  value,
  min,
  max,
  label,
  unit = '',
  defaultColor = '#00F0FF',
  size = 140,
  dangerVal,
  criticalVal
}: IAnalogGaugeProps) => {
  const radius = size * 0.42;
  const center = size / 2;
  const angleRange = 260;
  const startAngle = -220; // Starts bottom-left

  const clampedVal = Math.min(Math.max(value, min), max);
  const percent = (clampedVal - min) / (max - min);
  const currentAngle = startAngle + (percent * angleRange);

  let activeColor = defaultColor;
  if (criticalVal !== undefined && clampedVal >= criticalVal) activeColor = '#FF0000';
  else if (dangerVal !== undefined && clampedVal >= dangerVal) activeColor = '#FF8C00';

  const polarToCartesian = (centerX: number, centerY: number, r: number, angleInDegrees: number) => {
    const angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;
    return { x: centerX + (r * Math.cos(angleInRadians)), y: centerY + (r * Math.sin(angleInRadians)) };
  };

  const describeArc = (x: number, y: number, r: number, sAngle: number, eAngle: number) => {
    if (Math.abs(eAngle - sAngle) < 0.1) return "";
    const start = polarToCartesian(x, y, r, eAngle);
    const end = polarToCartesian(x, y, r, sAngle);
    const largeArcFlag = eAngle - sAngle <= 180 ? "0" : "1";
    return ["M", start.x, start.y, "A", r, r, 0, largeArcFlag, 0, end.x, end.y].join(" ");
  };

  const trackPath = describeArc(center, center, radius, startAngle, startAngle + angleRange);
  const fillPath = describeArc(center, center, radius, startAngle, currentAngle);

  let dangerPath = "";
  let criticalPath = "";
  if (dangerVal !== undefined && criticalVal !== undefined) {
    const dPercent = Math.max(0, Math.min(1, (dangerVal - min) / (max - min)));
    const cPercent = Math.max(0, Math.min(1, (criticalVal - min) / (max - min)));
    const dAngle = startAngle + (dPercent * angleRange);
    const cAngle = startAngle + (cPercent * angleRange);
    dangerPath = describeArc(center, center, radius, dAngle, cAngle);
    criticalPath = describeArc(center, center, radius, cAngle, startAngle + angleRange);
  }

  const numTicks = 8;
  const ticks = Array.from({ length: numTicks + 1 }).map((_, i) => {
    const p = i / numTicks;
    const a = startAngle + (p * angleRange);
    const rad = (a - 90) * Math.PI / 180;
    const isMajor = i % 2 === 0;
    const innerRadius = radius - (isMajor ? 8 : 4);
    return {
      x1: center + radius * Math.cos(rad),
      y1: center + radius * Math.sin(rad),
      x2: center + innerRadius * Math.cos(rad),
      y2: center + innerRadius * Math.sin(rad),
      isMajor
    };
  });

  return (
    <View style={{ alignItems: 'center', marginHorizontal: Spacing.xxs }}>
      <View style={{ width: size, height: size, justifyContent: 'center', alignItems: 'center' }}>
        <Svg width={size} height={size}>
          <Defs>
            <SvgLinearGradient id="grad" x1="0" y1="1" x2="1" y2="0">
              <Stop offset="0" stopColor={activeColor} stopOpacity="1" />
              <Stop offset="1" stopColor={activeColor} stopOpacity="0.4" />
            </SvgLinearGradient>
          </Defs>
          {/* Background track */}
          <Path d={trackPath} stroke="rgba(255,255,255,0.08)" strokeWidth={10} fill="none" strokeDasharray="6 4" strokeLinecap="butt" />

          {dangerPath ? <Path d={dangerPath} stroke="rgba(255,140,0,0.3)" strokeWidth={10} fill="none" strokeDasharray="6 4" /> : null}
          {criticalPath ? <Path d={criticalPath} stroke="rgba(255,0,0,0.35)" strokeWidth={10} fill="none" strokeDasharray="6 4" /> : null}

          {/* Active fill */}
          {fillPath ? <Path d={fillPath} stroke="url(#grad)" strokeWidth={10} fill="none" strokeDasharray="6 4" strokeLinecap="butt" /> : null}

          {/* Tick marks */}
          {ticks.map((tick, i) => (
            <Path key={i} d={`M ${tick.x1} ${tick.y1} L ${tick.x2} ${tick.y2}`} stroke={tick.isMajor ? "rgba(255,255,255,0.4)" : "rgba(255,255,255,0.15)"} strokeWidth={tick.isMajor ? 2 : 1} />
          ))}

          {/* Center Hub */}
          <Circle cx={center} cy={center} r={6} fill="#222" stroke="rgba(255,255,255,0.2)" strokeWidth={2} />
        </Svg>

        {/* Animated Needle */}
        <View style={{ position: 'absolute', width: size, height: size, justifyContent: 'center', alignItems: 'center', transform: [{ rotate: `${currentAngle}deg` }] }}>
          <View style={{
            width: 4, height: radius * 0.90,
            backgroundColor: '#FF8C00',
            position: 'absolute',
            top: center - (radius * 0.90),
            borderTopLeftRadius: 2,
            borderTopRightRadius: 2,
            shadowColor: '#FF8C00',
            shadowOpacity: 1,
            shadowRadius: 10,
            elevation: 8
          }} />
        </View>

        {/* Digital display */}
        <View style={{ position: 'absolute', right: size * 0.15, top: size * 0.32, alignItems: 'flex-end' }}>
          <Text style={{ color: '#FFF', fontSize: size * 0.22, fontWeight: '900', fontVariant: ['tabular-nums'], textShadowColor: activeColor !== '#00F0FF' ? activeColor : '#00F0FF', textShadowRadius: 16 }}>{Math.floor(value)}</Text>
          {unit ? <Text style={{ color: 'rgba(255,255,255,0.5)', fontSize: size * 0.08, fontWeight: '800', marginTop: -4 }}>{unit}</Text> : null}
        </View>
      </View>
      <Text style={{ color: 'rgba(255,255,255,0.4)', fontSize: 11, fontWeight: '900', letterSpacing: 2, marginTop: -4 }}>{label}</Text>
    </View>
  );
});

export default AnalogGauge;
