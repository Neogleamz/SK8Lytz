# Implementation Plan: Screen-Level TTID & TTFD Performance Charts

## Goal
Replace the two generic average load time and battery charts in the Command Center's `AppPerformanceWidget` with a grid of screen-specific cards. Each card will render a Recharts timeline graph tracking Time to Initial Display (TTID) and Time to Fully Drawn (TTFD) to visually expose the "latency gap" (hydration delay) per screen.

## Proposed Changes

### Command Center Web Dashboard
#### [MODIFY] [AppPerformanceWidget.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/command-center/src/components/widgets/AppPerformanceWidget.tsx)

1. **Verify Database Telemetry Rows:**
   - Confirm telemetry logs record `event_type` as `'SCREEN_LOAD_TTID'` and `'SCREEN_LOAD_TTFD'` with JSONB `metadata` fields containing `screen`, `ttidMs`, and `ttfdMs` respectively.
   - Source: [20260414_consolidate_telemetry.sql:L1-8](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260414_consolidate_telemetry.sql#L1-L8)

2. **Query Telemetry Table:**
   - Replace the query targeting `parsed_session_stats` with a query targeting `telemetry_snapshots` for `'SCREEN_LOAD_TTID'` and `'SCREEN_LOAD_TTFD'` events.
   - Source: [AppPerformanceWidget.tsx:L21-33](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/command-center/src/components/widgets/AppPerformanceWidget.tsx#L21-L33)
   - Code:
     ```typescript
     const { data, error } = await supabase
       .from('telemetry_snapshots')
       .select('created_at, event_type, metadata')
       .in('event_type', ['SCREEN_LOAD_TTID', 'SCREEN_LOAD_TTFD'])
       .order('created_at', { ascending: true })
       .limit(500);
     ```

3. **In-Memory Grouping & Parsing:**
   - Group the telemetry records in-memory by screen name (`item.metadata.screen`).
   - For each screen, correlate the chronological occurrences of `ttidMs` and `ttfdMs`. Map data to Recharts compatible format: `{ time: string, ttid: number, ttfd: number }`.

4. **Grid Layout & Screen Cards:**
   - Replace the main return block with a responsive CSS grid: `grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6`.
   - Render a card for each screen discovered in the telemetry payload.
   - Display key metric banners inside each card:
     - Median TTID (Cyan color)
     - Median TTFD (Emerald color)
     - Average Latency Gap (difference between TTFD and TTID)

5. **Recharts Visualizations:**
   - Render an `AreaChart` inside each screen card.
   - Define a shared timeline XAxis (formatted `created_at` timestamp).
   - Plot `ttid` using area fill `#22d3ee` (Cyan, opacity 0.6).
   - Plot `ttfd` using area fill `#10b981` (Emerald, opacity 0.4).

6. **Periodic Refreshes:**
   - Add a 10-second polling interval in a `useEffect` cleanup loop to pull fresh data automatically without forcing manual page reloads.

## Out of Scope
- Modifying React Native client logging behaviors.
- Modifying PostgreSQL schemas or creating new database tables.
- Modifying other tabs or widgets in the Scraper stack.

## Verification Plan
### Automated Tests
- Run the verify script to ensure the compilation is green and tests pass:
  ```powershell
  npm run verify
  ```

### Manual Verification
- Run the Scraper/Command Center Docker stack.
- Access the dashboard at `http://localhost:5997/performance` (or performance tab).
- Navigate through targeted screens in the mobile Web Demo (e.g. `http://localhost:8081`).
- Verify that screen performance cards render dynamically in the Command Center dashboard.
- Verify that clicking a point on the chart shows a tooltip with exact millisecond values for TTID and TTFD.
