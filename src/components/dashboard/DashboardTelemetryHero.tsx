import React, { useEffect, useRef, useState } from 'react';
import { View, Text, StyleSheet, Dimensions, Animated, Platform } from 'react-native';
import { Layout, Spacing } from '../../theme/theme';
import { useTheme } from '../../context/ThemeContext';
import Svg, { Circle, Line, Text as SvgText } from 'react-native-svg';
import { LinearGradient } from 'expo-linear-gradient';

// Wrapper to strip the 'collapsable' prop injected by Animated which causes DOM errors on Web
const CircleWrapper = React.forwardRef((props: any, ref) => {
  const { collapsable, ...rest } = props;
  return <Circle ref={ref} {...rest} />;
});
const AnimatedCircle = Animated.createAnimatedComponent(CircleWrapper);
interface DashboardTelemetryHeroProps {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  sessionPeakSpeed: number;
  sessionAvgSpeed: number;
  healthBpm?: number | null;
  healthCalories?: number | null;
}

// ─── Constants ────────────────────────────────────────────────────────────────
const MAX_SPEED  = 25;
const OUTER_SW   = 18;   // outer ring stroke width
const BLOOM      = 16;   // bloom extra (tight to cut dead space)
const BLEED      = OUTER_SW / 2 + BLOOM / 2 + 6; // top padding for glow
const TICK_MPH   = [0, 5, 10, 15, 20, 25];

// Angle math: at ratio t (0→1), standard-math angle goes π→0 (left→right through top)
// SVG tip coords: x = cx + r*cos(θ_math), y = cy - r*sin(θ_math)
const angleForRatio = (t: number) => (1 - t) * Math.PI;

export const DashboardTelemetryHero: React.FC<DashboardTelemetryHeroProps> = React.memo(({
  gpsSpeed, peakGForce, sessionDistanceMiles,
  sessionDurationSec, sessionPeakSpeed, sessionAvgSpeed,
  healthBpm, healthCalories
}) => {
  const { Colors } = useTheme();
  const windowWidth = Dimensions.get('window').width;
  const isWeb       = windowWidth > 600;

  // ─── Geometry ─────────────────────────────────────────────────────────────
  const svgWidth = isWeb ? 320 : Math.min(windowWidth - Spacing.md * 2, 340);
  const outerR   = svgWidth / 2 - BLEED;
  const midR     = outerR - 28;   // inner cyan ring
  const thin1R   = outerR - 46;   // decorative line 1
  const thin2R   = outerR - 54;   // decorative line 2
  const thin3R   = outerR - 61;   // decorative line 3
  const needleR  = outerR - 14;   // needle length (slightly inside outer ring)

  const cx       = svgWidth / 2;
  const cy       = outerR + BLEED; // center at BOTTOM → zero wasted space above arc
  const svgH     = cy;             // SVG height = exactly cy

  const halfDash = (r: number) => `${Math.PI * r} ${2 * Math.PI * r}`;
  const rot      = `rotate(180 ${cx} ${cy})`;

  // ─── Animation ────────────────────────────────────────────────────────────
  const speedRatio = Math.min(Math.max(gpsSpeed / MAX_SPEED, 0), 1);
  const animRatio  = useRef(new Animated.Value(0)).current;

  // Needle state — updated via addListener so it follows the spring smoothly
  const [needleAngle, setNeedleAngle] = useState(angleForRatio(0));

  useEffect(() => {
    Animated.spring(animRatio, {
      toValue: speedRatio, friction: 6, tension: 45, useNativeDriver: false,
    }).start();
  }, [speedRatio]);

  useEffect(() => {
    const id = animRatio.addListener(({ value }) => setNeedleAngle(angleForRatio(value)));
    return () => animRatio.removeListener(id);
  }, [animRatio]);

  const offsetFor = (r: number) =>
    animRatio.interpolate({ inputRange: [0, 1], outputRange: [Math.PI * r, 0] });

  const outerOff = offsetFor(outerR);
  const midOff   = offsetFor(midR);

  // ─── Needle tip coordinates ────────────────────────────────────────────────
  const tipX = cx + needleR * Math.cos(needleAngle);
  const tipY = cy - needleR * Math.sin(needleAngle);

  // ─── Tick mark coordinates ─────────────────────────────────────────────────
  const MAJOR_TICKS = [0, 10, 20, 25];
  const TICK_OUTER  = outerR + 4;
  const TICK_INNER  = outerR - 12;
  const LABEL_R     = outerR + 16;

  const tickElements = TICK_MPH.map((mph) => {
    const ratio = mph / MAX_SPEED;
    const θ     = angleForRatio(ratio);
    const isMajor = MAJOR_TICKS.includes(mph);
    const innerR  = isMajor ? TICK_INNER - 4 : TICK_INNER;
    return {
      mph,
      x1: cx + TICK_OUTER * Math.cos(θ),
      y1: cy - TICK_OUTER * Math.sin(θ),
      x2: cx + innerR * Math.cos(θ),
      y2: cy - innerR * Math.sin(θ),
      lx: cx + LABEL_R * Math.cos(θ),
      ly: cy - LABEL_R * Math.sin(θ),
      isMajor,
    };
  });

  // ─── Helpers ──────────────────────────────────────────────────────────────
  const formatDuration = (rawSecs: number) => {
    const total = Math.floor(rawSecs);
    const h = Math.floor(total / 3600);
    const m = Math.floor((total % 3600) / 60);
    const s = total % 60;
    if (h > 0) {
      return `${h}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
    }
    return `${m}:${s.toString().padStart(2, '0')}`;
  };
  const kcalBurned = healthCalories !== undefined && healthCalories !== null 
    ? healthCalories 
    : Math.round(sessionDistanceMiles * 65);

  return (
    <View style={styles.container}>

      {/* ─── GAUGE SVG ──────────────────────────────────────────────────────── */}
      <View style={{ alignSelf: 'center', width: svgWidth, height: svgH }}>
        <Svg width={svgWidth} height={svgH} viewBox={`0 0 ${svgWidth} ${svgH}`}>

          {/* ── TICK MARKS + LABELS ── */}
          {tickElements.map(({ mph, x1, y1, x2, y2, lx, ly, isMajor }) => (
            <React.Fragment key={mph}>
              <Line
                x1={x1} y1={y1} x2={x2} y2={y2}
                stroke={isMajor ? '#00FFFF' : 'rgba(0,255,255,0.4)'}
                strokeWidth={isMajor ? 2 : 1}
              />
              {isMajor && (
                <SvgText
                  x={lx} y={ly}
                  fill="#00FFFF"
                  fontSize={9}
                  fontFamily="Righteous"
                  textAnchor="middle"
                  alignmentBaseline="middle"
                  opacity={0.7}
                >
                  {mph}
                </SvgText>
              )}
            </React.Fragment>
          ))}

          {/* ── OUTER MAGENTA RING ── */}
          {/* dim track */}
          <Circle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FF00FF" strokeWidth={OUTER_SW} strokeOpacity={0.06}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={0}
            strokeLinecap="round" transform={rot} />
          {/* bloom */}
          <AnimatedCircle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FF00FF" strokeWidth={OUTER_SW + BLOOM} strokeOpacity={0.2}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={outerOff}
            strokeLinecap="round" transform={rot} />
          {/* core */}
          <AnimatedCircle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FF00FF" strokeWidth={OUTER_SW} strokeOpacity={1}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={outerOff}
            strokeLinecap="round" transform={rot} />
          {/* white filament */}
          <AnimatedCircle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FFFFFF" strokeWidth={2} strokeOpacity={0.65}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={outerOff}
            strokeLinecap="round" transform={rot} />

          {/* ── INNER CYAN RING ── */}
          {/* dim track */}
          <Circle cx={cx} cy={cy} r={midR} fill="none"
            stroke="#00FFFF" strokeWidth={8} strokeOpacity={0.06}
            strokeDasharray={halfDash(midR)} strokeDashoffset={0}
            strokeLinecap="round" transform={rot} />
          {/* bloom */}
          <AnimatedCircle cx={cx} cy={cy} r={midR} fill="none"
            stroke="#00FFFF" strokeWidth={20} strokeOpacity={0.18}
            strokeDasharray={halfDash(midR)} strokeDashoffset={midOff}
            strokeLinecap="round" transform={rot} />
          {/* core */}
          <AnimatedCircle cx={cx} cy={cy} r={midR} fill="none"
            stroke="#00FFFF" strokeWidth={8} strokeOpacity={1}
            strokeDasharray={halfDash(midR)} strokeDashoffset={midOff}
            strokeLinecap="round" transform={rot} />

          {/* ── 3 INNER DECORATIVE STATIC LINES ── */}
          <Circle cx={cx} cy={cy} r={thin1R} fill="none"
            stroke="#00FFFF" strokeWidth={1.5} strokeOpacity={0.4}
            strokeDasharray={halfDash(thin1R)} strokeDashoffset={0}
            strokeLinecap="butt" transform={rot} />
          <Circle cx={cx} cy={cy} r={thin2R} fill="none"
            stroke="#FFFFFF" strokeWidth={1} strokeOpacity={0.2}
            strokeDasharray={halfDash(thin2R)} strokeDashoffset={0}
            strokeLinecap="butt" transform={rot} />
          <Circle cx={cx} cy={cy} r={thin3R} fill="none"
            stroke="#8A2BE2" strokeWidth={1} strokeOpacity={0.3}
            strokeDasharray={halfDash(thin3R)} strokeDashoffset={0}
            strokeLinecap="butt" transform={rot} />

          {/* ── NEEDLE ── */}
          {/* needle glow */}
          <Line
            x1={cx} y1={cy} x2={tipX} y2={tipY}
            stroke="#FFFFFF" strokeWidth={6} strokeOpacity={0.15}
            strokeLinecap="round"
          />
          {/* needle core */}
          <Line
            x1={cx} y1={cy} x2={tipX} y2={tipY}
            stroke="#FFFFFF" strokeWidth={2} strokeOpacity={0.9}
            strokeLinecap="round"
          />
          {/* needle tip dot */}
          <Circle cx={tipX} cy={tipY} r={4}
            fill="#FFFFFF" opacity={0.9} />
          {/* pivot dot */}
          <Circle cx={cx} cy={cy} r={6}
            fill="#1A1A2E" stroke="#FF00FF" strokeWidth={2} />

        </Svg>

        {/* Speed text — inside the arc */}
        <View style={[styles.speedOverlay, { width: svgWidth, height: svgH }]}>
          <Text style={[styles.speedValue, { color: Colors.primary, ...(Platform.OS === 'web' ? { textShadow: `0 0 28px ${Colors.primary}` } : { textShadowColor: Colors.primary }) }]}>{gpsSpeed.toFixed(1)}</Text>
          <Text style={styles.speedUnit}>MPH</Text>
          
          {/* Heart Rate Indicator overlay */}
          {healthBpm && healthBpm > 0 && (
            <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 12, backgroundColor: 'rgba(255,0,0,0.1)', paddingHorizontal: 12, paddingVertical: 4, borderRadius: 12, borderWidth: 1, borderColor: 'rgba(255,0,0,0.3)' }}>
              <Text style={{ fontSize: 16, marginRight: 6 }}>❤️</Text>
              <Text style={{ fontFamily: 'Righteous', color: '#FF4D4D', fontSize: 14 }}>{healthBpm} BPM</Text>
            </View>
          )}
        </View>
      </View>

      {/* ─── 6-METRIC GRID ────────────────────────────────────────────────────── */}
      <View style={styles.metricsGrid}>
        <View style={styles.metricsRow}>
          <TelemetryPill label="DISTANCE" value={sessionDistanceMiles.toFixed(2)} unit="mi"    accent="#00FFFF" />
          <TelemetryPill label="G-FORCE"  value={peakGForce.toFixed(1)}           unit="g"     accent="#FF00FF" />
          <TelemetryPill label="TIME"     value={formatDuration(sessionDurationSec)} unit=""   accent="#FFD600" />
        </View>
        <View style={styles.metricsRow}>
          <TelemetryPill label="AVG SPD"  value={sessionAvgSpeed.toFixed(1)}       unit="mph"  accent="#00FF85" />
          <TelemetryPill label="TOP SPD"  value={sessionPeakSpeed.toFixed(1)}      unit="mph"  accent="#FF4D00" />
          <TelemetryPill label="BURN"     value={kcalBurned.toString()}             unit="kcal" accent="#FF4D4D" />
        </View>
      </View>

    </View>
  );
});

// ─── Glass Pill ───────────────────────────────────────────────────────────────
const TelemetryPill = ({ label, value, unit, accent }:
  { label: string; value: string; unit: string; accent: string }) => (
  <View style={styles.pillContainer}>
    <LinearGradient
      colors={['rgba(255,255,255,0.07)', 'rgba(0,0,0,0.5)']}
      style={StyleSheet.absoluteFillObject}
    />
    <View style={[styles.accentTick, { backgroundColor: accent, shadowColor: accent }]} />
    <Text style={styles.pillLabel}>{label}</Text>
    <View style={styles.pillValueRow}>
      <Text style={[styles.pillValue, Platform.OS === 'web' ? { textShadow: `0 0 14px ${accent}` } as any : { textShadowColor: accent, textShadowRadius: 14 }]}>
        {value}
      </Text>
      {unit !== '' && <Text style={styles.pillUnit}>{unit}</Text>}
    </View>
  </View>
);

// ─── Styles ───────────────────────────────────────────────────────────────────
const styles = StyleSheet.create({
  container: {
    marginHorizontal: Layout.padding,
    marginBottom: Spacing.xxl,
    marginTop: -Spacing.md, // pull up tight against Crewz Hub
  },
  speedOverlay: {
    position: 'absolute',
    top: 0, left: 0,
    alignItems: 'center',
    justifyContent: 'flex-end',
    paddingBottom: 16,
  },
  speedValue: {
    fontFamily: 'Righteous',
    fontSize: 60,
    lineHeight: 66,
    ...(Platform.OS !== 'web' ? {
      textShadowRadius: 28,
      textShadowOffset: { width: 0, height: 0 },
    } : {}),
  },
  speedUnit: {
    fontFamily: 'Righteous',
    fontSize: 13,
    letterSpacing: 4,
    color: '#00FFFF',
    marginTop: -2,
    ...(Platform.OS === 'web'
      ? { textShadow: '0 0 10px #00FFFF' }
      : { textShadowColor: '#00FFFF', textShadowRadius: 10 }),
  },
  metricsGrid: {
    paddingHorizontal: Spacing.xs,
    paddingTop: Spacing.sm,
    gap: Spacing.xs,
  },
  metricsRow: {
    flexDirection: 'row',
    gap: Spacing.xs,
  },
  pillContainer: {
    flex: 1,
    paddingVertical: Spacing.sm,
    paddingHorizontal: Spacing.xs,
    borderRadius: 8,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.04)',
    borderTopColor: 'rgba(255,255,255,0.14)',
    overflow: 'hidden',
  },
  accentTick: {
    position: 'absolute',
    bottom: 0, left: '20%', right: '20%',
    height: 2,
    ...(Platform.OS === 'web'
      ? { boxShadow: '0px -2px 5px currentColor' }
      : { shadowOffset: { width: 0, height: -2 }, shadowOpacity: 1, shadowRadius: 5, elevation: 4 }),
  },
  pillLabel: {
    fontSize: 8,
    fontFamily: 'Righteous',
    letterSpacing: 1.5,
    color: 'rgba(255,255,255,0.4)',
    marginBottom: 2,
  },
  pillValueRow: {
    flexDirection: 'row',
    alignItems: 'baseline',
    gap: 2,
  },
  pillValue: {
    fontFamily: 'monospace',
    fontSize: 18,
    fontWeight: '900',
    color: '#FFFFFF',
  },
  pillUnit: {
    fontFamily: 'Righteous',
    fontSize: 9,
    color: 'rgba(255,255,255,0.5)',
  },
});
