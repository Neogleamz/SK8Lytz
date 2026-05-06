import React, { useEffect, useState } from 'react';
import { View, Text, ActivityIndicator } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { supabase } from '../../services/supabaseClient';
import { Spacing } from '../../theme/theme';

interface LifetimeStats {
  total_app_time_sec: number;
  total_distance_meters: number;
  lifetime_top_speed_mph: number;
  total_street_sessions: number;
  pattern_time_map: Record<string, number>;
  color_time_map: Record<string, number>;
}

export default function SkaterStatsPanel({ Colors }: { Colors: any }) {
  const [stats, setStats] = useState<LifetimeStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchStats() {
      if (!supabase) return;
      try {
        const { data: authData } = await supabase.auth.getUser();
        if (!authData.user) return;
        
        const { data, error } = await supabase
          .from('user_lifetime_stats')
          .select('*')
          .eq('user_id', authData.user.id)
          .single();
          
        if (data) setStats(data);
      } catch (e) {
        // Silent catch for offline or non-existent stats
      } finally {
        setLoading(false);
      }
    }
    fetchStats();
  }, []);

  if (loading) {
    return (
      <View style={{ padding: Spacing.xl, alignItems: 'center' }}>
        <ActivityIndicator color={Colors.primary} />
      </View>
    );
  }

  if (!stats) return null;

  // Convert distance to miles
  const distanceMiles = (stats.total_distance_meters / 1609.34).toFixed(1);
  const hoursSkated = Math.floor(stats.total_app_time_sec / 3600);
  const topSpeed = stats.lifetime_top_speed_mph.toFixed(1);

  // Find Signature Style (Pattern or Color)
  let bestStyle = "Rookie";
  let bestTime = 0;
  let isHex = false;

  Object.entries(stats.pattern_time_map || {}).forEach(([key, val]) => {
    if (val > bestTime) {
      bestTime = val;
      bestStyle = key.replace('pattern_', 'Pattern ');
      isHex = false;
    }
  });

  Object.entries(stats.color_time_map || {}).forEach(([key, val]) => {
    if (val > bestTime) {
      bestTime = val;
      bestStyle = key.toUpperCase();
      isHex = true;
    }
  });

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
    </View>
  );
}
