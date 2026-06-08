import React, { useEffect, useState } from 'react';
import { View, Text, ActivityIndicator } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../../services/supabaseClient';
import { useAuth } from '../../context/AuthContext';
import { Spacing } from '../../theme/theme';
import type { Tables } from '../../types/supabase';
import { AppLogger } from '../../services/AppLogger';

// Use the generated Supabase Row type directly — stays in sync with schema automatically.
type LifetimeStats = Tables<'user_lifetime_stats'>;

// Narrow the Json map fields for local computation
type JsonMap = Record<string, number>;
const toMap = (v: LifetimeStats['pattern_time_map']): JsonMap =>
  (v && typeof v === 'object' && !Array.isArray(v) ? v : {}) as JsonMap;

export default function SkaterStatsPanel({ Colors }: { Colors: { background: string; surface: string; surfaceHighlight: string; primary: string; secondary: string; accent: string; text: string; textMuted: string; textDim: string; border: string; success: string; error: string; warning: string; isDark: boolean; } }) {
  const { user } = useAuth();
  const [stats, setStats] = useState<LifetimeStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) {
      setLoading(false);
      return;
    }

    let isActive = true;

    async function fetchStats() {
      const CACHE_KEY = '@sk8lytz_lifetime_stats_cache';
      
      // 1. Instantly load from cache for offline-first zero-latency
      try {
        const cached = await AsyncStorage.getItem(CACHE_KEY);
        if (cached && isActive) setStats(JSON.parse(cached));
      } catch (e: unknown) {
        AppLogger.warn('Failed to load skater stats cache from AsyncStorage', e instanceof Error ? e.message : String(e));
      }

      if (!user || !supabase) {
        if (isActive) setLoading(false);
        return;
      }

      try {
        const { data, error } = await supabase
          .from('user_lifetime_stats')
          .select('*')
          .eq('user_id', user.id)
          .single();
          
        if (data && !error && isActive) {
          setStats(data);
          // 2. Save fresh cloud data to offline cache
          await AsyncStorage.setItem(CACHE_KEY, JSON.stringify(data)).catch(e => AppLogger.warn('Failed to cache skater stats', e));
        }
      } catch (e: unknown) {
        AppLogger.error('Failed to load skater stats from Supabase', e instanceof Error ? e.message : String(e));
      } finally {
        if (isActive) setLoading(false);
      }
    }
    fetchStats();
    return () => { isActive = false; };
  }, [user]);


  if (loading) {
    return (
      <View style={{ padding: Spacing.xl, alignItems: 'center' }}>
        <ActivityIndicator color={Colors.primary} />
      </View>
    );
  }

  // Resolve nullable numeric fields — DB returns null for new users
  const distanceMiles = ((stats?.total_distance_meters ?? 0) / 1609.34).toFixed(1);
  const hoursSkated = Math.floor((stats?.total_app_time_sec ?? 0) / 3600);
  const topSpeed = (stats?.lifetime_top_speed_mph ?? 0).toFixed(1);

  // Resolve Json map fields with runtime type narrowing
  const patternTimeMap = toMap(stats?.pattern_time_map ?? null);
  const colorTimeMap   = toMap(stats?.color_time_map ?? null);
  const engagementMap  = toMap(stats?.engagement_counters ?? null);

  // Find Signature Style (Pattern or Color)
  let bestStyle = "Rookie — Go Skate!";
  let bestTime = 0;
  let isHex = false;

  Object.entries(patternTimeMap).forEach(([key, val]) => {
    if (val > bestTime) {
      bestTime = val;
      bestStyle = key.replace('pattern_', 'Pattern ');
      isHex = false;
    }
  });

  Object.entries(colorTimeMap).forEach(([key, val]) => {
    if (val > bestTime) {
      bestTime = val;
      bestStyle = key.toUpperCase();
      isHex = true;
    }
  });

  const topPatterns = Object.entries(patternTimeMap)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 3)
    .map(p => ({ name: p[0].replace('pattern_', 'Pattern '), time: p[1] }));

  const topColors = Object.entries(colorTimeMap)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 3)
    .map(c => ({ hex: c[0].toUpperCase(), time: c[1] }));

  const formatTime = (sec: number) => {
    if (sec < 60) return `${Math.floor(sec)}s`;
    const m = Math.floor(sec / 60);
    if (m < 60) return `${m}m`;
    const h = Math.floor(m / 60);
    return `${h}h ${m % 60}m`;
  };

  // ── Mode Split Calculation ──
  const modeStreet = engagementMap['mode_STREET_sec'] || 0;
  const modeMusic = engagementMap['mode_MUSIC_sec'] || 0;
  const modeSolid = engagementMap['mode_SOLID_sec'] || 0;
  const modeMultimode = engagementMap['mode_MULTIMODE_sec'] || 0;
  const totalModeTime = modeStreet + modeMusic + modeSolid + modeMultimode;
  const hasModeData = totalModeTime > 0;

  // ── Explorer Badge ──
  const patternCount = Object.keys(patternTimeMap).length;
  let patternBadge = 'The Enthusiast';
  let badgeDesc = `${patternCount} patterns explored`;
  if (patternCount > 20) {
    patternBadge = 'The Explorer';
  } else if (patternCount > 0 && patternCount < 5) {
    patternBadge = 'The Purist';
  }

  // ── Color Aura ──
  let auraTitle = 'No Aura Yet';
  let auraColor = 'rgba(255,255,255,0.1)';
  if (topColors.length > 0) {
    auraColor = topColors[0].hex;
    // Extremely rudimentary "Aura" text generator based on Hex string
    if (auraColor.toUpperCase().includes('FF00') || auraColor.toUpperCase().includes('F0F')) {
      auraTitle = 'Cyberpunk Neon';
    } else if (auraColor.toUpperCase().includes('00FF') || auraColor.toUpperCase().includes('0F0')) {
      auraTitle = 'Toxic Green';
    } else if (auraColor.toUpperCase().includes('FF') && !auraColor.toUpperCase().includes('00')) {
      auraTitle = 'Bright & Loud';
    } else {
      auraTitle = 'Late Night Chill';
    }
  }

  const favoritesCreated = engagementMap['favorites_created'] || 0;
  const hardwareConnections = engagementMap['hardware_connections'] || 0;

  return (
    <View style={{ marginTop: Spacing.xl, marginBottom: Spacing.lg }}>
      <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginBottom: Spacing.sm }}>
        YOUR SK8LYTZ WRAPPED
      </Text>
      
      {/* Hero Stats */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg, alignItems: 'center' }}>
          <Text style={{ color: Colors.text, fontSize: 24, fontWeight: '900' }}>{distanceMiles}</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 10, marginTop: Spacing.xxs }}>MILES</Text>
        </View>
        
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg, alignItems: 'center' }}>
          <Text style={{ color: Colors.text, fontSize: 24, fontWeight: '900' }}>{topSpeed}</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 10, marginTop: Spacing.xxs }}>TOP MPH</Text>
        </View>
        
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg, alignItems: 'center' }}>
          <Text style={{ color: Colors.text, fontSize: 24, fontWeight: '900' }}>{hoursSkated}h</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 10, marginTop: Spacing.xxs }}>SKATED</Text>
        </View>
      </View>

      {/* Signature Style Badge */}
      <View style={{ 
        flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
        marginTop: Spacing.sm, backgroundColor: 'rgba(255,255,255,0.05)', 
        borderRadius: 14, padding: Spacing.lg 
      }}>
        <View>
          <Text style={{ color: Colors.textMuted, fontSize: 11, fontWeight: '700' }}>SIGNATURE STYLE</Text>
          <Text style={{ color: Colors.text, fontSize: 16, fontWeight: '800', marginTop: 2 }}>{bestStyle}</Text>
        </View>
        {isHex ? (
          <View style={{ width: 36, height: 36, borderRadius: 18, backgroundColor: bestStyle, borderWidth: 2, borderColor: '#FFF' }} />
        ) : (
          <MaterialCommunityIcons name="star-shooting-outline" size={28} color={Colors.primary} />
        )}
      </View>

      {/* Mode Split Bar */}
      {hasModeData && (
        <View style={{ marginTop: Spacing.xl }}>
          <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginBottom: Spacing.sm }}>
            YOUR RIDER PERSONA
          </Text>
          <View style={{ height: 16, borderRadius: 8, flexDirection: 'row', overflow: 'hidden', backgroundColor: 'rgba(255,255,255,0.05)' }}>
            <View style={{ width: `${(modeStreet / totalModeTime) * 100}%`, backgroundColor: '#FF4444' }} />
            <View style={{ width: `${(modeMusic / totalModeTime) * 100}%`, backgroundColor: '#4444FF' }} />
            <View style={{ width: `${(modeSolid / totalModeTime) * 100}%`, backgroundColor: '#44FF44' }} />
            <View style={{ width: `${(modeMultimode / totalModeTime) * 100}%`, backgroundColor: '#FFFF44' }} />
          </View>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: Spacing.xs }}>
            <Text style={{ color: Colors.textMuted, fontSize: 10 }}>Street: {Math.round((modeStreet / totalModeTime) * 100)}%</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 10 }}>Pro: {Math.round((modeMultimode / totalModeTime) * 100)}%</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 10 }}>Music: {Math.round((modeMusic / totalModeTime) * 100)}%</Text>
          </View>
        </View>
      )}

      {/* Vibe Analysis Grid */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.xl }}>
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg }}>
          <MaterialCommunityIcons name="compass-outline" size={24} color={Colors.primary} style={{ marginBottom: Spacing.xs }} />
          <Text style={{ color: Colors.text, fontSize: 14, fontWeight: '800' }}>{patternBadge}</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 2 }}>{badgeDesc}</Text>
        </View>
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg }}>
          <View style={{ width: 24, height: 24, borderRadius: 12, backgroundColor: auraColor, marginBottom: Spacing.xs }} />
          <Text style={{ color: Colors.text, fontSize: 14, fontWeight: '800' }}>Color Aura</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 2 }}>{auraTitle}</Text>
        </View>
      </View>

      {/* Engagement Grid */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.sm }}>
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg }}>
          <Text style={{ color: Colors.text, fontSize: 20, fontWeight: '900' }}>{favoritesCreated}</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 2 }}>Favorites Built</Text>
        </View>
        <View style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 14, padding: Spacing.lg }}>
          <Text style={{ color: Colors.text, fontSize: 20, fontWeight: '900' }}>{hardwareConnections}</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 2 }}>Skate Connections</Text>
        </View>
      </View>

      {/* Top 3 Patterns */}
      {topPatterns.length > 0 && (
        <View style={{ marginTop: Spacing.xl }}>
          <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginBottom: Spacing.sm }}>
            TOP PATTERNS
          </Text>
          {topPatterns.map((p, i) => (
            <View key={i} style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', backgroundColor: 'rgba(255,255,255,0.03)', borderRadius: 10, padding: Spacing.md, marginBottom: Spacing.xs }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                <Text style={{ color: Colors.textMuted, fontSize: 12, fontWeight: '800', width: 20 }}>#{i + 1}</Text>
                <Text style={{ color: Colors.text, fontSize: 14, fontWeight: '600' }}>{p.name}</Text>
              </View>
              <Text style={{ color: Colors.primary, fontSize: 12, fontWeight: '700' }}>{formatTime(p.time)}</Text>
            </View>
          ))}
        </View>
      )}

      {/* Top 3 Colors */}
      {topColors.length > 0 && (
        <View style={{ marginTop: Spacing.xl }}>
          <Text style={{ color: Colors.textMuted, fontSize: 10, fontWeight: '700', letterSpacing: 1.2, marginBottom: Spacing.sm }}>
            TOP COLORS
          </Text>
          <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
            {topColors.map((c, i) => (
              <View key={i} style={{ flex: 1, backgroundColor: 'rgba(255,255,255,0.03)', borderRadius: 10, padding: Spacing.md, alignItems: 'center' }}>
                <View style={{ width: 24, height: 24, borderRadius: 12, backgroundColor: c.hex, borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginBottom: Spacing.xs }} />
                <Text style={{ color: Colors.text, fontSize: 11, fontWeight: '700' }}>{c.hex}</Text>
                <Text style={{ color: Colors.textMuted, fontSize: 10, marginTop: 2 }}>{formatTime(c.time)}</Text>
              </View>
            ))}
          </View>
        </View>
      )}

    </View>
  );
}
