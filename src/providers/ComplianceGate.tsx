import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useEffect, useState } from 'react';
import { ActivityIndicator, Alert, View, Text } from 'react-native';
import EulaModal from '../components/modals/EulaModal';
import { useTheme } from '../context/ThemeContext';
import { AppSettingsService } from '../services/AppSettingsService';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';

import { useAuth } from '../context/AuthContext';

interface ComplianceGateProps {
  children: React.ReactNode;
}

export function ComplianceGate({ children }: ComplianceGateProps) {
  const { isOfflineMode, user } = useAuth();
  const { Colors } = useTheme();
  type ComplianceStatus = 'checking' | 'idle';
  const [status, setStatus] = useState<ComplianceStatus>('checking');
  const [requiresEula, setRequiresEula] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    checkCompliance();
  }, [isOfflineMode]);

  const checkCompliance = async () => {
    setStatus('checking');
    try {

      if (isOfflineMode) {
        // Offline users still require EULA acceptance — check the local AsyncStorage record.
        // This closes the bypass that previously let users skip legal compliance entirely.
        const offlineEula = await AsyncStorage.getItem('@Sk8lytz_offline_eula_accepted');
        if (!offlineEula) {
          setRequiresEula(true);
        }
        setStatus('idle');
        return;
      }

      if (!supabase) {
        setStatus('idle');
        return;
      }
      
      // 1. Fetch required version from app_settings
      const settings = await AppSettingsService.fetchAllSettings();
      const requiredVersion = parseInt(settings['required_eula_version'] || '1', 10);

      // 2. Fetch user's accepted version
      if (!user) {
        setStatus('idle');
        return;
      }

      const { data: profile } = await supabase
        .from('user_profiles')
        .select('accepted_eula_version')
        .eq('user_id', user.id)
        .single();
      
      const userVersion = profile?.accepted_eula_version || 0;

      if (userVersion < requiredVersion) {
        setRequiresEula(true);
      }
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[ComplianceGate] check error', { error: msg });
      setError(msg);
    } finally {
      setStatus('idle');
    }
  };

  const handleAccept = async () => {
    try {
      if (isOfflineMode) {
        // Offline acceptance: write versioned record to AsyncStorage.
        // No Supabase call — user has no account. ComplianceGate will pass through on next check.
        await AsyncStorage.setItem(
          '@Sk8lytz_offline_eula_accepted',
          JSON.stringify({ version: 1, acceptedAt: new Date().toISOString() }),
        );
        setRequiresEula(false);
        return;
      }

      // 1. Fetch current required version
      const settings = await AppSettingsService.fetchAllSettings();
      const requiredVersion = parseInt(settings['required_eula_version'] || '1', 10);

      // 2. Update user profile in Supabase
      if (!user) return;
      
      await supabase
        .from('user_profiles')
        .update({ accepted_eula_version: requiredVersion })
        .eq('user_id', user.id);
        
      setRequiresEula(false);
    } catch (e: unknown) {
       const msg = e instanceof Error ? e.message : String(e);
       AppLogger.error('[ComplianceGate] Accept failed', { error: msg });
       Alert.alert('Error', 'Could not save compliance status. Please try again.');
    }
  };

  const handleDecline = async () => {
    try {
      if (supabase) {
        await supabase.auth.signOut();
      }
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[ComplianceGate] Sign out failed', { error: msg });
    }
  };

  if (status === 'checking') {
    return (
      <View style={{ flex: 1, backgroundColor: Colors.background, justifyContent: 'center', alignItems: 'center' }}>
        <ActivityIndicator size="large" color={Colors.primary} />
      </View>
    );
  }

  if (requiresEula) {
    return (
      <View style={{ flex: 1, backgroundColor: Colors.background }}>
        <EulaModal visible={true} onAccept={handleAccept} onDecline={handleDecline} isViewOnly={false} />
      </View>
    );
  }

  if (error) {
    return (
      <View style={{ flex: 1, backgroundColor: Colors.background, justifyContent: 'center', alignItems: 'center', padding: 20 }}>
        <Text style={{ color: Colors.error || '#FF0000', marginBottom: 16, fontWeight: 'bold' }}>Failed to verify compliance status.</Text>
        <Text style={{ color: Colors.text, textAlign: 'center' }}>{error}</Text>
      </View>
    );
  }

  return <>{children}</>;
}
