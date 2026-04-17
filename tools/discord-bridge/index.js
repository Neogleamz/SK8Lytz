require('dotenv').config();
const { Client, GatewayIntentBits } = require('discord.js');
const { sendKeystrokes } = require('./powershell');
const { findLatestLogFile, watchLogFile, formatForDiscord } = require('./brain-tail');

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

if (!TOKEN || !CHANNEL_ID || !AUTH_USER_ID) {
  console.error("Missing required environment variables (.env). Please provide DISCORD_BOT_TOKEN, DISCORD_CHANNEL_ID, and DISCORD_AUTHORIZED_USER_ID.");
  process.exit(1);
}

client.once('ready', () => {
  console.log(`[Bridge] Logged in as ${client.user.tag}`);
  
  // Attempt to find the active conversation brain
  try {
    const activeLog = findLatestLogFile();
    if (!activeLog) {
      console.warn("[Bridge] Could not find an active overview.txt in the Gemini Brain.");
    } else {
      console.log(`[Bridge] Attached to log: ${activeLog}`);
      
      const channel = client.channels.cache.get(CHANNEL_ID);
      if (channel) {
        channel.send(`🔗 **Antigravity Bridge Connected!**\\nNow tailing active brain session...`);
      }

      // Start tailing the active file
      watchLogFile(activeLog, (rawChunk) => {
        const formatted = formatForDiscord(rawChunk);
        if (formatted && channel) {
          channel.send(`\`\`\`md\\n${formatted}\\n\`\`\``).catch(err => {
            console.error("[Bridge Discord Error] Failed to send log chunk.", err.message);
          });
        }
      });
    }
  } catch (error) {
    console.error("[Bridge] Failed to mount log watcher:", error);
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
