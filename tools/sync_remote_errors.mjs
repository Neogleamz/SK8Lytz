#!/usr/bin/env node
/**
 * sync_remote_errors.mjs — SK8Lytz Telemetry Error Sync
 *
 * Developer-side CI health tool. Queries the Supabase `telemetry_errors`
 * table and prints a triage summary to the console.
 *
 * Called by: /audit-codebase workflow, Step 6 (Telemetry Sync)
 * Auth:      EXPO_PUBLIC_SUPABASE_URL + EXPO_PUBLIC_SUPABASE_ANON_KEY from .env
 * Runtime:   Node.js 18+ (native fetch — zero external dependencies)
 *
 * Usage:
 *   node tools/sync_remote_errors.mjs
 *   node tools/sync_remote_errors.mjs --hours 48
 *   node tools/sync_remote_errors.mjs --json          (raw JSON output)
 *   node tools/sync_remote_errors.mjs --limit 50
 */

import { readFileSync } from 'fs';
import { resolve, dirname } from 'path';
import { fileURLToPath } from 'url';

// ── Config ────────────────────────────────────────────────────────────────────

const __dirname = dirname(fileURLToPath(import.meta.url));
const ROOT = resolve(__dirname, '..');

// Parse CLI args
const args = process.argv.slice(2);
const flagIndex = (flag) => args.indexOf(flag);
const getFlag = (flag, defaultVal) => {
  const i = flagIndex(flag);
  return i !== -1 && args[i + 1] ? args[i + 1] : defaultVal;
};

const HOURS_BACK = parseInt(getFlag('--hours', '24'), 10);
const LIMIT      = parseInt(getFlag('--limit', '100'), 10);
const JSON_MODE  = args.includes('--json');

// ── Load .env (native — no dotenv dependency) ─────────────────────────────────

function loadEnv() {
  const envPath = resolve(ROOT, '.env');
  try {
    const raw = readFileSync(envPath, 'utf-8');
    const vars = {};
    for (const line of raw.split('\n')) {
      const trimmed = line.trim();
      if (!trimmed || trimmed.startsWith('#')) continue;
      const eqIdx = trimmed.indexOf('=');
      if (eqIdx === -1) continue;
      const key = trimmed.slice(0, eqIdx).trim();
      const val = trimmed.slice(eqIdx + 1).trim().replace(/^["']|["']$/g, '');
      vars[key] = val;
    }
    return vars;
  } catch {
    return {};
  }
}

const env = { ...process.env, ...loadEnv() };
const SUPABASE_URL = env.EXPO_PUBLIC_SUPABASE_URL;
const SUPABASE_KEY = env.EXPO_PUBLIC_SUPABASE_ANON_KEY;

// ── Offline-safe guard ────────────────────────────────────────────────────────

if (!SUPABASE_URL || !SUPABASE_KEY) {
  console.warn('[sync_remote_errors] ⚠️  Skipped — EXPO_PUBLIC_SUPABASE_URL or EXPO_PUBLIC_SUPABASE_ANON_KEY not set.');
  // exitCode stays 0 — non-fatal, audit-codebase sweep continues
}

// ── Fetch from Supabase REST API (native fetch — Node 18+) ────────────────────

async function fetchErrors() {
  const since = new Date(Date.now() - HOURS_BACK * 60 * 60 * 1000).toISOString();

  // Supabase REST: GET /rest/v1/telemetry_errors?select=*&order=created_at.desc&limit=N
  const url = new URL(`${SUPABASE_URL}/rest/v1/telemetry_errors`);
  url.searchParams.set('select', 'id,created_at,session_id,event_type,error_message,stack_trace,raw_context');
  url.searchParams.set('order', 'created_at.desc');
  url.searchParams.set('limit', String(LIMIT));
  url.searchParams.set('created_at', `gte.${since}`);

  const res = await fetch(url.toString(), {
    headers: {
      'apikey':        SUPABASE_KEY,
      'Authorization': `Bearer ${SUPABASE_KEY}`,
      'Content-Type':  'application/json',
    },
  });

  if (!res.ok) {
    const body = await res.text();
    throw new Error(`Supabase REST error ${res.status}: ${body}`);
  }

  return res.json();
}

// ── Format triage report ──────────────────────────────────────────────────────

function formatReport(errors) {
  if (errors.length === 0) {
    return `✅  No remote errors in the last ${HOURS_BACK}h. Telemetry cache is clean.`;
  }

  // Group by event_type for frequency analysis
  const byType = {};
  for (const e of errors) {
    byType[e.event_type] = (byType[e.event_type] || 0) + 1;
  }
  const sorted = Object.entries(byType).sort((a, b) => b[1] - a[1]);

  const lines = [
    ``,
    `╔══════════════════════════════════════════════════════════╗`,
    `║  SK8Lytz Remote Error Triage — Last ${HOURS_BACK}h                 ║`,
    `╚══════════════════════════════════════════════════════════╝`,
    ``,
    `  Total errors: ${errors.length} (limit: ${LIMIT})`,
    `  Window:       ${new Date(Date.now() - HOURS_BACK * 60 * 60 * 1000).toISOString()} → now`,
    ``,
    `  ── Frequency by Event Type ───────────────────────────────`,
    ...sorted.map(([type, count]) =>
      `  ${String(count).padStart(4)}x  ${type}`
    ),
    ``,
    `  ── Latest ${Math.min(5, errors.length)} Errors ────────────────────────────────`,
    ...errors.slice(0, 5).map((e, i) => [
      ``,
      `  [${i + 1}] ${e.event_type}  |  ${e.created_at}`,
      `      Session: ${e.session_id}`,
      `      Message: ${(e.error_message || '(none)').slice(0, 120)}`,
      e.stack_trace
        ? `      Stack:   ${e.stack_trace.split('\n')[0].slice(0, 100)}...`
        : null,
    ].filter(Boolean).join('\n')),
    ``,
  ];

  return lines.join('\n');
}

// ── Main ──────────────────────────────────────────────────────────────────────

(async () => {
  try {
    console.log(`[sync_remote_errors] Querying Supabase telemetry_errors (last ${HOURS_BACK}h, limit ${LIMIT})...`);

    const errors = await fetchErrors();

    if (JSON_MODE) {
      console.log(JSON.stringify(errors, null, 2));
    } else {
      console.log(formatReport(errors));
    }

    // Set exit code without calling process.exit() — lets the event loop drain
    // naturally so libuv can close the native fetch handle cleanly (Windows fix).
    process.exitCode = errors.length > 0 ? 1 : 0;
  } catch (err) {
    // Non-fatal: print warning so audit-codebase sweep continues
    console.warn(`[sync_remote_errors] ⚠️  Could not fetch remote errors: ${err.message}`);
    console.warn(`[sync_remote_errors]    This is non-fatal — Supabase may be offline or RLS is blocking the anon key.`);
    // exitCode stays 0
  }
})();
