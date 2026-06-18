import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

// ─── CORS ────────────────────────────────────────────────────────────────────
// Required for browser-based Supabase clients and the Expo JS runtime when
// invoking edge functions directly. Wildcard origin is safe here because every
// request is still authenticated via the caller's JWT in the Authorization header.
const corsHeaders: Record<string, string> = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers":
    "authorization, x-client-info, apikey, content-type",
};

// ─── CLIENTS ─────────────────────────────────────────────────────────────────
// adminClient — uses the SERVICE_ROLE key. Must only be used for operations
// that legitimately need to bypass RLS (fetching push tokens of other users).
const adminClient = createClient(
  Deno.env.get("SUPABASE_URL")!,
  Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
);

const EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

// ─── TYPES ───────────────────────────────────────────────────────────────────
interface NotifyPayload {
  crewId: string;
  sessionId: string;
  sessionName: string;
  leaderName: string;
}

/** Shape of a single row returned by the push_tokens + crew_memberships join.
 *  PostgREST join relationship: push_tokens.user_id → crew_memberships.user_id
 *  (foreign-key: push_tokens has user_id FK referencing auth.users, and
 *   crew_memberships also references auth.users.id — the !inner filter narrows
 *   to tokens whose owner is a member of the target crew_id). */
interface PushTokenRow {
  token: string;
  crew_memberships: { crew_id: string }[];
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
  // ── CORS preflight ──────────────────────────────────────────────────────────
  if (req.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders });
  }

  if (req.method !== "POST") {
    return new Response("Method not allowed", {
      status: 405,
      headers: corsHeaders,
    });
  }

  // ── Auth — verify caller JWT via anon client scoped to the caller's token ──
  // EXEMPTION (R-15): This is a server-side Supabase Edge Function running in Deno.
  // It has no access to the React Native AuthContext, so it must authenticate the
  // caller directly by verifying the JWT via GoTrue server-side. We use the anon
  // client here (not service role) so RLS is enforced for this auth check.
  const authHeader = req.headers.get("Authorization");
  if (!authHeader) {
    return new Response("Unauthorized", { status: 401, headers: corsHeaders });
  }

  const callerJwt = authHeader.replace("Bearer ", "");

  // Scoped client — authenticated as the caller, respects RLS.
  const callerClient = createClient(
    Deno.env.get("SUPABASE_URL")!,
    Deno.env.get("SUPABASE_ANON_KEY")!,
    { global: { headers: { Authorization: `Bearer ${callerJwt}` } } },
  );

  let user: { id: string } | null = null;
  try {
    // getUser validates the JWT signature server-side via GoTrue — safe against spoofing.
    const { data, error: authErr } = await callerClient.auth.getUser(callerJwt);
    if (authErr || !data.user) {
      return new Response("Unauthorized", {
        status: 401,
        headers: corsHeaders,
      });
    }
    user = data.user;
  } catch (e: unknown) {
    console.error(
      `[notify-crew-session] Auth exception:`,
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ error: "Auth exception" }), {
      status: 500,
      headers: { ...corsHeaders, "Content-Type": "application/json" },
    });
  }

  // ── Parse & validate body ──────────────────────────────────────────────────
  let body: NotifyPayload;
  try {
    body = (await req.json()) as NotifyPayload;
  } catch {
    return new Response("Invalid JSON", { status: 400, headers: corsHeaders });
  }

  const { crewId, sessionId, sessionName, leaderName } = body;
  if (
    !crewId ||
    typeof crewId !== "string" ||
    !sessionId ||
    typeof sessionId !== "string"
  ) {
    return new Response("Missing or invalid crewId/sessionId", {
      status: 400,
      headers: corsHeaders,
    });
  }

  // ── Verify caller membership — uses callerClient (RLS-scoped) ─────────────
  // RLS on crew_memberships only exposes rows where user_id = auth.uid(), so
  // callerClient is sufficient and correct here — no service role needed.
  try {
    const { data: callerMembership, error: callerErr } = await callerClient
      .from("crew_memberships")
      .select("role")
      .eq("crew_id", crewId)
      .eq("user_id", user.id)
      .single();

    if (callerErr || !callerMembership) {
      return new Response("Forbidden: Not a member of this crew", {
        status: 403,
        headers: corsHeaders,
      });
    }
  } catch (e: unknown) {
    console.error(
      `[notify-crew-session] DB exception:`,
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ error: "DB exception" }), {
      status: 500,
      headers: { ...corsHeaders, "Content-Type": "application/json" },
    });
  }

  // ── Fetch push tokens — REQUIRES adminClient (service role) ───────────────
  // We need tokens for *other* users in the crew. RLS on push_tokens only
  // exposes the caller's own rows, so the anon/callerClient cannot see other
  // members' tokens. adminClient bypasses RLS for this read-only operation only.
  // PostgREST join: push_tokens JOIN crew_memberships ON push_tokens.user_id = crew_memberships.user_id
  // !inner ensures only rows with a matching crew_memberships entry are returned.
  let tokenRows: PushTokenRow[];
  try {
    const { data, error } = await adminClient
      .from("push_tokens")
      .select("token, crew_memberships!inner(crew_id)")
      .eq("crew_memberships.crew_id", crewId)
      .neq("user_id", user.id); // exclude caller (the session leader)

    if (error) {
      return new Response(JSON.stringify({ sent: 0, reason: "DB error" }), {
        status: 500,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      });
    }
    tokenRows = (data ?? []) as PushTokenRow[];
  } catch (e: unknown) {
    console.error(
      `[notify-crew-session] DB exception (push tokens):`,
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ error: "DB exception" }), {
      status: 500,
      headers: { ...corsHeaders, "Content-Type": "application/json" },
    });
  }

  if (tokenRows.length === 0) {
    return new Response(JSON.stringify({ sent: 0, reason: "No tokens" }), {
      headers: { ...corsHeaders, "Content-Type": "application/json" },
    });
  }

  const messages: ExpoMessage[] = tokenRows.map(
    (row: PushTokenRow): ExpoMessage => ({
      to: row.token,
      title: "🛼 Crew is Live!",
      body: `${leaderName} started ${sessionName} — tap to join`,
      data: { crewId, sessionId },
      sound: "default",
      channelId: "crew-alerts",
    }),
  );

  // ── Send via Expo Push API (batches of 100 max) ───────────────────────────
  const batchSize = 100;
  let sent = 0;
  try {
    const fetchPromises: Promise<void>[] = [];
    for (let i = 0; i < messages.length; i += batchSize) {
      const batch: ExpoMessage[] = messages.slice(i, i + batchSize);
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
              `[notify-crew-session] Expo error:`,
              await resp.text(),
            );
          }
        }),
      );
    }
    const results = await Promise.allSettled(fetchPromises);
    for (const result of results) {
      if (result.status === "rejected") {
        console.error(`[notify-crew-session] Fetch rejected:`, result.reason);
      }
    }
  } catch (error: unknown) {
    console.error(
      `[notify-crew-session] Network fetch failed:`,
      error instanceof Error ? error.message : String(error),
    );
  }

  return new Response(JSON.stringify({ sent }), {
    headers: { ...corsHeaders, "Content-Type": "application/json" },
  });
});
