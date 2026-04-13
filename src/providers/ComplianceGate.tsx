import React, { useEffect, useState } from 'react';
import { View, ActivityIndicator, Alert } from 'react-native';
import { supabase } from '../services/supabaseClient';
import EulaModal from '../components/modals/EulaModal';
import { AppSettingsService } from '../services/AppSettingsService';
import { useTheme } from '../context/ThemeContext';

interface ComplianceGateProps {
  children: React.ReactNode;
  isOfflineMode: boolean;
}

export function ComplianceGate({ children, isOfflineMode }: ComplianceGateProps) {
  const { Colors } = useTheme();
  const [loading, setLoading] = useState(true);
  const [requiresEula, setRequiresEula] = useState(false);

  useEffect(() => {
    if (isOfflineMode) {
      setLoading(false);
      return;
    }
    
    checkCompliance();
  }, [isOfflineMode]);

  const checkCompliance = async () => {
    setLoading(true);
    try {
      if (!supabase) {
        setLoading(false);
        return;
      }
      
      // 1. Fetch required version from app_settings
      const settings = await AppSettingsService.fetchAllSettings();
      const requiredVersion = parseInt(settings['required_eula_version'] || '1', 10);

      // 2. Fetch user's accepted version
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) {
        setLoading(false);
        return;
      }

      const { data: profile } = await supabase
        .from('user_profiles')
        .select('accepted_eula_version')
        .eq('id', user.id)
        .single();
      
      const userVersion = profile?.accepted_eula_version || 0;

      if (userVersion < requiredVersion) {
        setRequiresEula(true);
      }
    } catch (e) {
      console.warn("Compliance check error", e);
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async () => {
    try {
      // 1. Fetch current required version
      const settings = await AppSettingsService.fetchAllSettings();
      const requiredVersion = parseInt(settings['required_eula_version'] || '1', 10);

      // 2. Update user profile
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      
      await supabase
        .from('user_profiles')
        .update({ accepted_eula_version: requiredVersion })
        .eq('id', user.id);
        
      setRequiresEula(false);
    } catch (e) {
       Alert.alert("Error", "Could not save compliance status. Please try again.");
    }
  };

  const handleDecline = async () => {
    if (supabase) {
      await supabase.auth.signOut();
    }
  };

  if (loading) {
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

  return <>{children}</>;
}
