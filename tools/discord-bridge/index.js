require('dotenv').config();
const { Client, GatewayIntentBits } = require('discord.js');
const { sendKeystrokes } = require('./powershell');
const http = require('http');

const client = new Client({
  intents: [
    GatewayIntentBits.Guilds,
    GatewayIntentBits.GuildMessages,
    GatewayIntentBits.MessageContent,
  ]
});

const TOKEN = process.env.DISCORD_BOT_TOKEN;
const CHANNEL_ID = process.env.DISCORD_CHANNEL_ID;
const AUTH_USER_ID = process.env.DISCORD_AUTHORIZED_USER_ID;
const PORT = process.env.PORT || 3050;

if (!TOKEN || !CHANNEL_ID || !AUTH_USER_ID) {
  console.error("Missing required environment variables (.env). Please provide DISCORD_BOT_TOKEN, DISCORD_CHANNEL_ID, and DISCORD_AUTHORIZED_USER_ID.");
  process.exit(1);
}

// Global reference to the output channel
let bridgeChannel = null;

client.once('ready', () => {
  console.log(`[Bridge] Logged in as ${client.user.tag}`);
  
  bridgeChannel = client.channels.cache.get(CHANNEL_ID);
  if (bridgeChannel) {
    bridgeChannel.send(`🔗 **Antigravity Bridge Connected!**\n*Webhook mode active on port ${PORT}...*`);
  } else {
    console.error(`[Bridge] Failed to find channel ${CHANNEL_ID}`);
  }
});

client.on('messageCreate', async (message) => {
  // Ignore bots and unauthorized channels/users
  if (message.author.bot) return;
  if (message.channelId !== CHANNEL_ID) return;
  if (message.author.id !== AUTH_USER_ID) {
    message.reply("🚫 Unauthorized access. Only the designated operator may pipe context.");
    return;
  }

  // If the user sends a message, intercept it and push it to the active prompt.
  const text = message.cleanContent;
  console.log(`[Inject] Pushing payload: "${text}"`);
  
  try {
    // Attempt to inject keystrokes via Powershell SendKeys
    await sendKeystrokes(text);
    message.react('✅');
  } catch (error) {
    console.error('[Inject] Failed to dispatch keystrokes:', error);
    message.react('❌');
  }
});

client.login(TOKEN).catch((err) => {
  console.error('[Bridge] Fatal login error:', err.message);
});

// --- Simple HTTP Webhook Server ---
const server = http.createServer((req, res) => {
  // Always reply with CORS headers if needed, though usually localhost curl is fine.
  if (req.method === 'POST' && req.url === '/hook') {
    let body = '';
    req.on('data', chunk => {
      body += chunk.toString();
    });
    
    req.on('end', () => {
      try {
        const payload = JSON.parse(body);
        if (payload.message && bridgeChannel) {
          
          let cleanTxt = payload.message.substring(0, 1900);
          bridgeChannel.send(`\`\`\`md\n${cleanTxt}\n\`\`\``).catch(err => {
            console.error("[Bridge Discord Error] Failed to send webhook chunk.", err.message);
          });
          
          res.writeHead(200, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({ status: 'ok' }));
        } else {
          res.writeHead(400);
          res.end("Bad request or Discord not ready.");
        }
      } catch (err) {
        console.error("[Webhook Error] Parse failed:", err.message);
        res.writeHead(400);
        res.end("Invalid JSON payload.");
      }
    });
  } else {
    res.writeHead(404);
    res.end("Not found");
  }
});

server.listen(PORT, () => {
  console.log(`[Webhook] Listening for Antigravity outputs on http://localhost:${PORT}/hook`);
});
