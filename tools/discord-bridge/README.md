# 🤖 SK8Lytz Discord Auto-Bot Bridge

A standalone Node.js microservice that links the Antigravity agent to your local Discord environment. This application watches your active agent session logs and dynamically pipes them back into a Discord channel, letting you control and monitor your AI pipeline away from your keyboard.

## 🚀 Setup & Installation

### 1. The Discord Developer Portal (Creating the Bot)
If you've never created a Discord bot before, follow these exact steps:

1. **Create the Application:**
   - Go to the [Discord Developer Portal](https://discord.com/developers/applications).
   - Click the **New Application** button in the top right.
   - Name it "Antigravity Bridge" (or whatever you like) and click **Create**.

2. **Enable Bot Privileges (Crucial):**
   - On the left sidebar, click on **Bot**.
   - Scroll down to the **Privileged Gateway Intents** section.
   - Flip the switch for **MESSAGE CONTENT INTENT** to ON. (Without this, the bot cannot read your replies!)
   - Click **Save Changes** at the bottom.
   
3. **Get Your Bot Token:**
   - Still on the **Bot** page, look underneath the bot's username and click the **Reset Token** button.
   - **Copy** the huge generated token. This is your `DISCORD_BOT_TOKEN`. Keep it secret!

4. **Invite the Bot to your Discord Server:**
   - On the left sidebar, click **OAuth2** -> **URL Generator**.
   - Under **Scopes**, check the box for `bot`.
   - Under **Bot Permissions** (which appears after checking bot), check `Send Messages` and `Read Messages/View Channels`.
   - Scroll to the bottom of the page and **Copy** the generated URL.
   - Paste that URL into your web browser, select your private Discord Server, and hit **Authorize**. Your bot is now in your server!

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
