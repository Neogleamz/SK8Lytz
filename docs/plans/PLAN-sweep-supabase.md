# Implementation Plan: BATCH:sweep-supabase

## Proposed Changes

### Domain: supabase

#### [MODIFY] supabase/functions/notify-crew-session/index.ts
- Line 108 (R-06): Missing standard e instanceof Error unwrapping in catch blocks.
- Line 27 (R-15): Direct supabase.auth.getUser() call instead of using AuthContext.

#### [MODIFY] src/types/supabase.ts
- Line 1 (R-23): File exceeds 30KB limit (150641 bytes) - flag for mandatory component extraction
