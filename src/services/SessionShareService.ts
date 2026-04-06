/**
 * SessionShareService.ts
 *
 * Builds share-ready text payloads for crew sessions and triggers
 * the native OS share sheet (which covers every social app the user
 * has installed — Instagram, TikTok, X, WhatsApp, Facebook, iMessage, etc.)
 *
 * Uses React Native's built-in Share API — zero external dependencies.
 */

import { Share, Platform } from 'react-native';
import { AppLogger } from './AppLogger';

// App store / download links — update when app is published
const APP_LINK_IOS     = 'https://apps.apple.com/app/sk8lytz';
const APP_LINK_ANDROID = 'https://play.google.com/store/apps/details?id=com.neogleamz.sk8lytz';
const APP_LINK = Platform.OS === 'ios' ? APP_LINK_IOS : APP_LINK_ANDROID;

// ─── Types ────────────────────────────────────────────────────────────────────

export interface ShareableSession {
  name: string;
  location_label?: string | null;
  invite_code?: string | null;
  scheduled_at?: string | null;
  leader_name?: string | null;
  crew_name?: string | null;
  status?: string;
}

// ─── Helper ───────────────────────────────────────────────────────────────────

function formatDateTime(iso: string): string {
  return new Date(iso).toLocaleString(undefined, {
    weekday: 'short',
    month: 'short',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
  });
}

// ─── Share builders ───────────────────────────────────────────────────────────

/**
 * Share a live / just-started session.
 * e.g. "🛼 Andy just started a SK8Lytz Crew Session at SkateCity OP!"
 */
export async function shareLiveSession(s: ShareableSession): Promise<void> {
  const lines: string[] = [];
  lines.push(`🛼 ${s.leader_name ?? 'Someone'} started a SK8Lytz Crew Session!`);
  if (s.name)           lines.push(`🎉 Session: ${s.name}`);
  if (s.location_label) lines.push(`📍 ${s.location_label}`);
  if (s.crew_name)      lines.push(`👥 Crew: ${s.crew_name}`);
  if (s.invite_code)    lines.push(`🔑 Join code: ${s.invite_code}`);
  lines.push('');
  lines.push(`Get SK8Lytz and join the crew: ${APP_LINK}`);

  await _share(lines.join('\n'), s);
}

/**
 * Share a scheduled session ahead of time.
 * e.g. "📅 SK8Lytz Crew Session scheduled for Saturday Apr 6 at 7 PM"
 */
export async function shareScheduledSession(s: ShareableSession): Promise<void> {
  const lines: string[] = [];
  lines.push(`🗓️ SK8Lytz Crew Session Scheduled!`);
  if (s.name)           lines.push(`🎉 ${s.name}`);
  if (s.scheduled_at)   lines.push(`⏰ ${formatDateTime(s.scheduled_at)}`);
  if (s.location_label) lines.push(`📍 ${s.location_label}`);
  if (s.crew_name)      lines.push(`👥 Crew: ${s.crew_name}`);
  if (s.leader_name)    lines.push(`👑 Led by: ${s.leader_name}`);
  if (s.invite_code)    lines.push(`🔑 Join code: ${s.invite_code}`);
  lines.push('');
  lines.push('Save the date! Download SK8Lytz:');
  lines.push(APP_LINK);

  await _share(lines.join('\n'), s);
}

/**
 * Share a generic session invite (for the controller card "Share" button).
 */
export async function shareSessionInvite(s: ShareableSession): Promise<void> {
  const isScheduled = !!s.scheduled_at;
  if (isScheduled) {
    return shareScheduledSession(s);
  }
  return shareLiveSession(s);
}

// ─── Core share call ──────────────────────────────────────────────────────────

async function _share(message: string, s: ShareableSession): Promise<void> {
  try {
    const result = await Share.share(
      {
        message,
        // iOS also shows a URL preview when url is set separately
        ...(Platform.OS === 'ios' ? { url: APP_LINK } : {}),
      },
      {
        // Android subject line (used in email / some apps)
        subject: `SK8Lytz Crew Session${s.location_label ? ` @ ${s.location_label}` : ''}`,
        dialogTitle: 'Share Session',
      }
    );

    if (result.action === Share.sharedAction) {
      AppLogger.log('CREW_SESSION_SHARED', {
        activityType: (result as any).activityType ?? 'unknown',
        sessionName: s.name,
        isScheduled: !!s.scheduled_at,
      });
    }
  } catch (e: any) {
    // User dismissed or share failed — not a fatal error, just log
    AppLogger.log('CREW_SESSION_SHARED', { error: e.message ?? 'dismissed', sessionName: s.name });
  }
}
