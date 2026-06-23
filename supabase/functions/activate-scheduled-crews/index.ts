import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

// activate-scheduled-crews
// Invoked ONLY by pg_cron via pg_net.http_post (see migration
// 20260622000000_activate_scheduled_crews_cron.sql). NOT user-facing.
// Auth model: shared CRON_SECRET header (x-cron-secret) — NOT a user JWT,
// because cron has no authenticated user. Mirrors the Expo send logic from
// notify-crew-session/index.ts:192-217.

const EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

const adminClient = createClient(
  Deno.env.get("SUPABASE_URL")!,
  Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
);

interface DueSession {
  id: string;
  name: string;
  crew_id: string | null;
  leader_user_id: string | null;
}

interface ExpoMessage {
  to: string;
  title: string;
  body: string;
  data: { crewId: string; sessionId: string };
  sound: string;
  channelId: string;
}

Deno.serve(async (req: Request): Promise<Response> => {
  // ── Auth: shared secret only ───────────────────────────────────────────────
  const provided = req.headers.get("x-cron-secret");
  const expected = Deno.env.get("CRON_SECRET");
  if (!expected || provided !== expected) {
    return new Response("Unauthorized", { status: 401 });
  }

  const nowIso = new Date().toISOString();

  // ── 1. Find due scheduled sessions ─────────────────────────────────────────
  let due: DueSession[];
  try {
    const { data, error } = await adminClient
      .from("crew_sessions")
      .select("id, name, crew_id, leader_user_id")
      .eq("status", "scheduled")
      .lte("scheduled_at", nowIso);
    if (error) {
      console.error("[activate-scheduled-crews] select error:", error.message);
      return new Response(JSON.stringify({ activated: 0, error: "select" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
      });
    }
    due = (data ?? []) as DueSession[];
  } catch (e: unknown) {
    console.error(
      "[activate-scheduled-crews] select exception:",
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ activated: 0, error: "select-exc" }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }

  if (due.length === 0) {
    return new Response(JSON.stringify({ activated: 0, sent: 0 }), {
      headers: { "Content-Type": "application/json" },
    });
  }

  // ── 2. Flip them to active (single batched UPDATE) ─────────────────────────
  const dueIds = due.map((s: DueSession) => s.id);
  try {
    const { error } = await adminClient
      .from("crew_sessions")
      .update({ status: "active", is_active: true, updated_at: nowIso })
      .in("id", dueIds)
      .eq("status", "scheduled"); // guard against double-activation race
    if (error) {
      console.error("[activate-scheduled-crews] update error:", error.message);
      return new Response(JSON.stringify({ activated: 0, error: "update" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
      });
    }
  } catch (e: unknown) {
    console.error(
      "[activate-scheduled-crews] update exception:",
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ activated: 0, error: "update-exc" }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }

  // ── 3. Build push messages per session for crew members ────────────────────
  const messages: ExpoMessage[] = [];
  for (const s of due) {
    if (!s.crew_id) continue; // ad-hoc sessions w/o a crew have no persistent roster to notify
    try {
      // push_tokens JOIN crew_memberships ON user_id; !inner narrows to this crew.
      // Mirror of notify-crew-session/index.ts:160-166.
      const { data, error } = await adminClient
        .from("push_tokens")
        .select("token, user_id, crew_memberships!inner(crew_id)")
        .eq("crew_memberships.crew_id", s.crew_id);
      if (error) {
        console.error(
          `[activate-scheduled-crews] token fetch error (crew ${s.crew_id}):`,
          error.message,
        );
        continue;
      }
      const rows = (data ?? []) as { token: string; user_id: string }[];
      for (const row of rows) {
        if (row.user_id === s.leader_user_id) continue; // skip the leader
        messages.push({
          to: row.token,
          title: "🛼 Crew is Live!",
          body: `${s.name} is starting now — tap to join`,
          data: { crewId: s.crew_id, sessionId: s.id },
          sound: "default",
          channelId: "crew-alerts",
        });
      }
    } catch (e: unknown) {
      console.error(
        `[activate-scheduled-crews] token fetch exception (crew ${s.crew_id}):`,
        e instanceof Error ? e.message : String(e),
      );
    }
  }

  // ── 4. Send via Expo Push API (batches of 100) ─────────────────────────────
  const batchSize = 100;
  let sent = 0;
  if (messages.length > 0) {
    const fetchPromises: Promise<void>[] = [];
    for (let i = 0; i < messages.length; i += batchSize) {
      const batch = messages.slice(i, i + batchSize);
      fetchPromises.push(
        fetch(EXPO_PUSH_URL, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
          body: JSON.stringify(batch),
        }).then(async (resp: Response): Promise<void> => {
          if (resp.ok) {
            sent += batch.length;
          } else {
            console.error(
              "[activate-scheduled-crews] Expo error:",
              await resp.text(),
            );
          }
        }),
      );
    }
    const results = await Promise.allSettled(fetchPromises);
    for (const r of results) {
      if (r.status === "rejected") {
        console.error("[activate-scheduled-crews] fetch rejected:", r.reason);
      }
    }
  }

  console.log(
    `[activate-scheduled-crews] activated=${due.length} sent=${sent}`,
  );
  return new Response(JSON.stringify({ activated: due.length, sent }), {
    headers: { "Content-Type": "application/json" },
  });
});
