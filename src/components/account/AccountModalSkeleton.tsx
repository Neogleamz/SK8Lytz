import React, { useEffect, useRef } from 'react';
import { Animated, View } from 'react-native';
import { Spacing } from '../../theme/theme';

export default function AccountModalSkeleton() {
  const pulse = useRef(new Animated.Value(0.4)).current;

  useEffect(() => {
    const loop = Animated.loop(
      Animated.sequence([
        Animated.timing(pulse, { toValue: 1, duration: 700, useNativeDriver: true }),
        Animated.timing(pulse, { toValue: 0.4, duration: 700, useNativeDriver: true }),
      ])
    );
    loop.start();
    return () => loop.stop();
  }, [pulse]);

  const SkeletonBar = ({ w = '100%', h = 14, mb = 12, br = 8 }: { w?: string | number; h?: number; mb?: number; br?: number }) => (
    <Animated.View
      style={{
        width: w as import('react-native').DimensionValue, height: h, borderRadius: br,
        backgroundColor: 'rgba(255,255,255,0.08)',
        marginBottom: mb,
        opacity: pulse,
      }}
    />
  );

  return (
    <View style={{ padding: Spacing.xl, paddingTop: Spacing.lg }}>
      {/* Avatar placeholder */}
      <Animated.View style={{ width: 80, height: 80, borderRadius: 40, backgroundColor: 'rgba(255,255,255,0.08)', alignSelf: 'center', marginBottom: Spacing.xl, opacity: pulse }} />
      {/* Email line */}
      <SkeletonBar w="50%" h={10} mb={Spacing.xl} br={6} />
      {/* Field labels + inputs */}
      <SkeletonBar w="30%" h={10} mb={8} />
      <SkeletonBar w="100%" h={44} mb={Spacing.lg} br={12} />
      <SkeletonBar w="30%" h={10} mb={8} />
      <SkeletonBar w="100%" h={44} mb={Spacing.xl} br={12} />
      {/* Stats row */}
      <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.md }}>
        <SkeletonBar w="32%" h={70} mb={0} br={14} />
        <SkeletonBar w="32%" h={70} mb={0} br={14} />
        <SkeletonBar w="32%" h={70} mb={0} br={14} />
      </View>
    </View>
  );
}
