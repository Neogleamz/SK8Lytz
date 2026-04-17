# 🤖 SK8Lytz Discord Auto-Bot Bridge

A standalone Node.js microservice that links the Antigravity agent to your local Discord environment. This application watches your active agent session logs and dynamically pipes them back into a Discord channel, letting you control and monitor your AI pipeline away from your keyboard.

## 🚀 Setup & Installation

### 1. Discord Developer Portal Setup
1. Go to the [Discord Developer Portal](https://discord.com/developers/applications) and click **New Application**.
2. Navigate to the **Bot** tab on the left.
3. Scroll down to **Privileged Gateway Intents** and enable **MESSAGE CONTENT INTENT** (crucial for reading your replies).
4. Click **Reset Token** and copy your `Bot Token`.
5. Under the **OAuth2 > URL Generator** tab, select the `bot` scope and give it `Send Messages` and `Read Messages/View Channels` permissions. Paste the generated URL into your browser to invite the bot to your private server.

### 2. Environment Variables (.env)
In the `tools/discord-bridge` directory, copy `.env.example` to a new `.env` file and populate it:

\`\`\`env
DISCORD_BOT_TOKEN="YOUR_BOT_TOKEN_HERE"
DISCORD_CHANNEL_ID="YOUR_PRIVATE_CHANNEL_ID"
DISCORD_AUTHORIZED_USER_ID="YOUR_DISCORD_USER_ACCOUNT_ID"
\`\`\`

> **Note**: To get channel/user IDs, enable **Developer Mode** in your Discord Advanced Settings, right-click the channel or your user profile, and click "Copy Channel ID" / "Copy User ID".

### 3. Launching the Bridge
Ensure you're inside the bridge directory, then install the packages and run the daemon:

\`\`\`bash
cd SK8Lytz/tools/discord-bridge
npm install
node index.js
\`\`\`

---

## ⚡ How it Works

### 1. Telemetry Out (Agent -> Discord)
Upon initialization, `brain-tail.js` will scan your local Antigravity `.gemini/antigravity/brain` cache to find the latest active conversation stream. Using `chokidar`, it silently tails byte-additions mapping the massive prompt tokens down into sanitized markdown messages, stripping payload extremes to fit inside Discord's max 2000-character payload limits.

### 2. Keystroke In (Discord -> Machine)
Because the agent runs entirely through a local interface overlay, we don't use arbitrary REST bridging to intercept commands. When you type a message into the registered Discord channel, `powershell.js` constructs a native OS keystroke wrapper using `[System.Windows.Forms.SendKeys]::SendWait`.

**How to use the keystroke injector:**
1. Leave your mouse/cursor active inside your IDE prompt box where Antigravity is running (VS Code input, terminal, etc.).
2. From your phone (or second monitor) mapped to the `DISCORD_CHANNEL_ID`, type a message into Discord.
3. The Node script picks up the text, translates it immediately into brute-force keyboard presses, and submits an `{ENTER}` directly into your active window.
4. Your Discord client will react with a `✅` if the PowerShell command successfully fired, or a `❌` if it failed.

### ⚠️ Security Warning
The Bi-directional keystroke bridge equates to literal execution access over your system. Validate that `DISCORD_AUTHORIZED_USER_ID` strictly belongs only to you so no one else in your server can remotely type commands into your host machine.
