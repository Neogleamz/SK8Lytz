import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

const supabase = createClient(
  Deno.env.get("SUPABASE_URL")!,
  Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
);

const EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

interface NotifyPayload {
  crewId: string;
  sessionId: string;
  sessionName: string;
  leaderName: string;
}

Deno.serve(async (req: Request) => {
  if (req.method !== "POST") {
    return new Response("Method not allowed", { status: 405 });
  }

  // Verify caller is authenticated (must pass JWT in Authorization header)
  const authHeader = req.headers.get("Authorization");
  if (!authHeader) return new Response("Unauthorized", { status: 401 });

  // EXEMPTION (R-15): This is a server-side Supabase Edge Function running in Deno.
  // It has no access to the React Native AuthContext, so it must authenticate the caller
  // directly by verifying the JWT via GoTrue server-side.
  let user;
  try {
    const { data, error: authErr } = await supabase.auth.getUser(
      authHeader.replace("Bearer ", ""),
    );
    if (authErr) return new Response("Unauthorized", { status: 401 });
    user = data.user;
  } catch (e) {
    return new Response(JSON.stringify({ error: "Auth exception" }), { status: 500, headers: { "Content-Type": "application/json" } });
  }

  if (!user) return new Response("Unauthorized", { status: 401 });

  let body: NotifyPayload;
  try {
    body = await req.json();
  } catch {
    return new Response("Invalid JSON", { status: 400 });
  }

  const { crewId, sessionId, sessionName, leaderName } = body;
  if (!crewId || !sessionId) {
    return new Response("Missing crewId or sessionId", { status: 400 });
  }

  // Verify caller is a member of the crew
  try {
    const { data: callerMembership, error: callerErr } = await supabase
      .from("crew_memberships")
      .select("role")
      .eq("crew_id", crewId)
      .eq("user_id", user.id)
      .single();

    if (callerErr || !callerMembership) {
      return new Response("Forbidden: Not a member of this crew", { status: 403 });
    }
  } catch (e) {
    return new Response(JSON.stringify({ error: "DB exception" }), { status: 500, headers: { "Content-Type": "application/json" } });
  }

  // 1 & 2. Get all permanent crew members and their push tokens
  let tokenRows;
  try {
    const { data, error } = await supabase
      .from("push_tokens")
      .select("token, crew_memberships!inner(crew_id)")
      .eq("crew_memberships.crew_id", crewId)
      .neq("user_id", user.id); // exclude caller (the leader)

    if (error) {
      return new Response(JSON.stringify({ sent: 0, reason: "DB error" }), { status: 500, headers: { "Content-Type": "application/json" } });
    }
    tokenRows = data;
  } catch (e) {
    return new Response(JSON.stringify({ error: "DB exception" }), { status: 500, headers: { "Content-Type": "application/json" } });
  }

  if (!tokenRows || tokenRows.length === 0) {
    return new Response(JSON.stringify({ sent: 0, reason: "No tokens" }), {
      headers: { "Content-Type": "application/json" },
    });
  }

  const messages = tokenRows.map((row: { token: string }) => ({
    to:    row.token,
    title: "🛼 Crew is Live!",
    body:  `${leaderName} started ${sessionName} — tap to join`,
    data:  { crewId, sessionId },
    sound: "default",
    channelId: "crew-alerts",
  }));

  // 3. Send via Expo Push API (batches of 100 max)
  const batchSize = 100;
  let sent = 0;
  try {
    const fetchPromises = [];
    for (let i = 0; i < messages.length; i += batchSize) {
      const batch = messages.slice(i, i + batchSize);
      fetchPromises.push(
        fetch(EXPO_PUSH_URL, {
          method:  "POST",
          headers: { "Content-Type": "application/json", "Accept": "application/json" },
          body:    JSON.stringify(batch),
        }).then(async (resp) => {
          if (resp.ok) {
            sent += batch.length;
          } else {
            console.error(`[notify-crew-session] Expo error:`, await resp.text());
          }
        })
      );
    }
    await Promise.allSettled(fetchPromises);
  } catch (error) {
    console.error(
      `[notify-crew-session] Network fetch failed:`,
      error instanceof Error ? error.message : String(error)
    );
  }

  return new Response(JSON.stringify({ sent }), {
    headers: { "Content-Type": "application/json" },
  });
});
