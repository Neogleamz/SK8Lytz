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

  const { data: { user }, error: authErr } = await supabase.auth.getUser(
    authHeader.replace("Bearer ", ""),
  );
  if (authErr || !user) return new Response("Unauthorized", { status: 401 });

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

  // 1. Get all permanent crew members (exclude the leader)
  const { data: members, error: membersErr } = await supabase
    .from("crew_memberships")
    .select("user_id")
    .eq("crew_id", crewId)
    .neq("user_id", user.id); // exclude caller (the leader)

  if (membersErr || !members || members.length === 0) {
    return new Response(JSON.stringify({ sent: 0, reason: "No members" }), {
      headers: { "Content-Type": "application/json" },
    });
  }

  const memberIds = members.map((m: { user_id: string }) => m.user_id);

  // 2. Get their push tokens
  const { data: tokenRows, error: tokensErr } = await supabase
    .from("push_tokens")
    .select("token")
    .in("user_id", memberIds);

  if (tokensErr || !tokenRows || tokenRows.length === 0) {
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
  for (let i = 0; i < messages.length; i += batchSize) {
    const batch = messages.slice(i, i + batchSize);
    const resp = await fetch(EXPO_PUSH_URL, {
      method:  "POST",
      headers: { "Content-Type": "application/json", "Accept": "application/json" },
      body:    JSON.stringify(batch),
    });

    if (resp.ok) sent += batch.length;
    else console.error(`[notify-crew-session] Expo error:`, await resp.text());
  }

  return new Response(JSON.stringify({ sent }), {
    headers: { "Content-Type": "application/json" },
  });
});
