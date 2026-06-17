import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useEffect, useState, useRef } from 'react';
import { ActivityIndicator, Alert, View, Text, Button } from 'react-native';
import EulaModal from '../components/modals/EulaModal';
import { useTheme } from '../context/ThemeContext';
import { AppSettingsService } from '../services/AppSettingsService';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/appLogger';
import { useAuth } from '../context/AuthContext';
import { STORAGE_EULA_ACCEPTED } from '../constants/storageKeys';

interface ComplianceGateProps {
  children: React.ReactNode;
}

export function ComplianceGate({ children }: ComplianceGateProps) {
  const { isOfflineMode, user, signOut } = useAuth();
  const { Colors } = useTheme();
  const mountedRef = useRef(true);
  type ComplianceStatus = 'checking' | 'idle';
  const [status, setStatus] = useState<ComplianceStatus>('checking');
  const [requiresEula, setRequiresEula] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const isProcessingRef = useRef(false);

  useEffect(() => {
    mountedRef.current = true;
    checkCompliance();
    return () => {
      mountedRef.current = false;
    };
  }, [isOfflineMode]);

  const checkCompliance = async () => {
    setStatus('checking');
    try {

      if (isOfflineMode) {
        // Offline users still require EULA acceptance — check the local AsyncStorage record.
        // This closes the bypass that previously let users skip legal compliance entirely.
        const offlineEula = await AsyncStorage.getItem(STORAGE_EULA_ACCEPTED);
        if (!mountedRef.current) return;
        if (!offlineEula) {
          setRequiresEula(true);
        }
        setStatus('idle');
        return;
      }

      if (!supabase) {
        if (mountedRef.current) setStatus('idle');
        return;
      }
      
      // 1. Fetch required version from app_settings
      const settings = await AppSettingsService.fetchAllSettings();
      const requiredVal = settings['required_eula_version'];
      const requiredVersion = parseInt(typeof requiredVal === 'string' ? requiredVal : '1', 10);

      // 2. Fetch user's accepted version
      if (!user) {
        if (mountedRef.current) setStatus('idle');
        return;
      }

      const { data: profile, error: queryError } = await supabase
         .from('user_profiles')
         .select('accepted_eula_version')
         .eq('user_id', user.id)
         .single();
      
      if (!mountedRef.current) return;

      if (queryError) {
        AppLogger.warn('[ComplianceGate] EULA query failed', { error: queryError.message, payload_size: 0, ssi: 0 });
      }

      const userVersion = profile?.accepted_eula_version || 0;

      if (userVersion < requiredVersion) {
        setRequiresEula(true);
      }
    } catch (e: unknown) {
      if (!mountedRef.current) return;
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[ComplianceGate] check error', { error: msg, payload_size: 0, ssi: 0 });
      setError(msg);
    } finally {
      if (mountedRef.current) {
        setStatus('idle');
      }
    }
  };

  const handleAccept = async () => {
    if (isProcessingRef.current) return;
    isProcessingRef.current = true;
    try {
      if (isOfflineMode) {
        // Offline acceptance: write versioned record to AsyncStorage.
        // No Supabase call — user has no account. ComplianceGate will pass through on next check.
        await AsyncStorage.setItem(
          STORAGE_EULA_ACCEPTED,
          JSON.stringify({ version: 1, acceptedAt: new Date().toISOString() }),
        );
        setRequiresEula(false);
        return;
      }

      // 1. Fetch current required version
      const settings = await AppSettingsService.fetchAllSettings();
      const requiredVal = settings['required_eula_version'];
      const requiredVersion = parseInt(typeof requiredVal === 'string' ? requiredVal : '1', 10);

      // 2. Update user profile in Supabase
      if (!user) return;
      
      const { error } = await supabase
        .from('user_profiles')
        .update({ accepted_eula_version: requiredVersion })
        .eq('user_id', user.id);
        
      if (error) throw error;
        
      setRequiresEula(false);
    } catch (e: unknown) {
       const msg = e instanceof Error ? e.message : String(e);
       AppLogger.error('[ComplianceGate] Accept failed', { error: msg, payload_size: 0, ssi: 0 });
       Alert.alert('Error', 'Could not save compliance status. Please try again.');
    } finally {
      isProcessingRef.current = false;
    }
  };

  const handleDecline = async () => {
    if (isProcessingRef.current) return;
    isProcessingRef.current = true;
    try {
      await signOut();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[ComplianceGate] Sign out failed', { error: msg, payload_size: 0, ssi: 0 });
    } finally {
      isProcessingRef.current = false;
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
        <Text style={{ color: Colors.text, textAlign: 'center', marginBottom: 20 }}>{error}</Text>
        <Button title="Retry" onPress={() => { setError(null); checkCompliance(); }} color={Colors.primary} />
        <View style={{ marginTop: 10 }}>
           <Button title="Sign Out" onPress={handleDecline} color={Colors.text} />
        </View>
      </View>
    );
  }

  return <>{children}</>;
}
