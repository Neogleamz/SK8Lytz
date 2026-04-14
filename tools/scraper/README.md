# SK8Lytz Zero-API Skate Spot Scraper

This is a localized ETL (Extract, Transform, Load) pipeline that populates the `skate_spots` Supabase database table with absolutely zero commercial API usage. It relies on the OpenStreetMap public API to grab coordinates, and a stealthy Headless Chrome instance to harvest addresses, phone numbers, and ratings from Google Knowledge Panels.

---

## 🧰 Step 0: Initial Setup

You must provide the system with Supabase access so it can push data to your database at the end of the pipeline.

1. Create a `.env` file right next to this `README.md` (inside `tools/scraper`).
2. Add your **service role key** and project URL (found in your Supabase dashboard):
   ```env
   EXPO_PUBLIC_SUPABASE_URL=https://qefmeivpjyaukbwadgaz.supabase.co
   SUPABASE_SERVICE_ROLE_KEY=your_secret_service_role_key_here
   ```
3. Run `npm install` inside `tools/scraper` just to be safe.

---

## 🌱 Step 1: Seed the Data

We must first figure out **where** skate venues are located globally using OpenStreetMap. This script pulls raw GPS coordinate bounding boxes.

- Run: `npm run seed`

**What it does:** It generates `seed.json` in this folder containing hundreds of raw names and GPS coordinates for roller rinks. 

*Optional Check: You can manually open `seed.json` and trim down the array if you just want to test 5-10 spots instead of hundreds!*

---

## 🕵️‍♂️ Step 2: The Background Stealth Crawl

Now we need to get rich details (hours, phone numbers, address, proshop presence) for each location without manually doing it. 

Because Google aggressively IP-bans API-less traffic, we launch this job in **stealth background mode**. It will randomly sleep for 30–90 seconds between queries. It takes roughly **1 Hour per 60 locations**.

- Run: `npm run start:bg`

**What it does:**
1. This launches a background PowerShell job (it will disappear from your terminal immediately).
2. You can freely close the terminal or keep working on the app.
3. It incrementally saves progress into an `enriched.json` file.
4. If you want to see what it is doing, open the `scraper.log` text file in this folder to see the live console logs.

*(If you ever need to forcefully stop the background processor early, open Windows Task Manager and end the background `node.exe` tasks, or close your code editor.)*

---

## 🚀 Step 3: Push to Supabase Database

Once the crawler finishes (or whenever you decide `enriched.json` has enough data to send):

- Run: `npm run push`

**What it does:** 
It reads everything generated inside `enriched.json` and securely UPSERTS it directly to your `skate_spots` cloud table, matching the schema flawlessly. It automatically prevents duplicates based on the venue's name.

> **Done!** You now have hundreds of beautifully detailed locations ready to display on the actual in-app interactive map.
