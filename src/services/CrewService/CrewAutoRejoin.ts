import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import { CrewSession, CrewRole } from './types';
import type { CrewService } from './CrewService';

export class CrewAutoRejoin {
  constructor(private service: CrewService) {}

  async tryAutoRejoin(displayName: string, userId?: string): Promise<{ session: CrewSession; role: CrewRole } | null> {
    try {
      const storedId = await AsyncStorage.getItem(this.service.STORAGE_LAST_SESSION_ID);
      const storedExp = await AsyncStorage.getItem(this.service.STORAGE_LAST_SESSION_EXP);

      if (!storedId || !storedExp || new Date(storedExp).getTime() < Date.now()) {
        try {
          await AsyncStorage.multiRemove([this.service.STORAGE_LAST_SESSION_ID, this.service.STORAGE_LAST_SESSION_EXP]);
        } catch (err: unknown) {
           AppLogger.warn('[CrewService] Failed to multiRemove on autoRejoin expiry', { error: err instanceof Error ? err.message : String(err)  });
        }
        return null;
      }

      if (!userId) return null;

      const { data, error } = await supabase
        .from('crew_sessions')
        .select('*')
        .eq('id', storedId)
        .eq('is_active', true)
        .single();

      if (error || !data) {
        try {
          await AsyncStorage.multiRemove([this.service.STORAGE_LAST_SESSION_ID, this.service.STORAGE_LAST_SESSION_EXP]);
        } catch (err: unknown) {
           AppLogger.warn('[CrewService] Failed to multiRemove on autoRejoin not-found', { error: err instanceof Error ? err.message : String(err)  });
        }
        return null;
      }

      const session = data as CrewSession;

      // Ensure membership still exists
      const { data: membership } = await supabase
        .from('crew_members')
        .select('id')
        .eq('session_id', storedId)
        .eq('user_id', userId)
        .single();

      if (!membership) {
        // Re-join as member
        await supabase.from('crew_members').upsert({
          session_id: storedId,
          user_id: userId,
          display_name: displayName,
        }, { onConflict: 'session_id,user_id' });
      }

      this.service.currentSession = session;
      this.service.currentSessionId = session.id;
      this.service.currentRole = userId === session.leader_user_id ? 'leader' : 'member';
      this.service.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
      
      this.service.emit();

      return { session, role: this.service.currentRole };
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] tryAutoRejoin failed', { error: msg , payload_size: 0, ssi: 0 });
      return null;
    }
  }
}
