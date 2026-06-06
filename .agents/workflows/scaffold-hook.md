---
description: Scaffold a new domain hook with boilerplate FSM state, typed params, error handling, and JSDoc
persona_entry: "⚒️ Dev — Sage"
team_roster: .agents/team-roster.md
---

> **⚒️ Dev — Sage | Scaffold Hook Active**
> *Sage lays the foundation correctly the first time. FSM state, strict types, AppLogger telemetry — no shortcuts. The TODO is for you; the scaffold is Sage's.*

1. **Parse the hook name** from the user's prompt. Extract the name after "scaffold hook" (e.g., `scaffold hook useDeviceSync` → `useDeviceSync`).

2. **Generate the hook file** at `src/hooks/<hookName>.ts` using `write_to_file` with this template:

```typescript
/**
 * @hook <hookName>
 * @description TODO: Describe the domain responsibility of this hook.
 * @module Domain Hook
 */

import { useState, useCallback } from 'react';
import { AppLogger } from '../services/AppLogger';

/** Finite State Machine for this hook's lifecycle */
type HookState = 'IDLE' | 'LOADING' | 'SUCCESS' | 'ERROR';

interface HookResult {
  state: HookState;
  error: string | null;
  /** Execute the primary domain action */
  execute: () => Promise<void>;
}

export function <hookName>(): HookResult {
  const [state, setState] = useState<HookState>('IDLE');
  const [error, setError] = useState<string | null>(null);

  const execute = useCallback(async () => {
    try {
      setState('LOADING');
      setError(null);
      
      // TODO: Implement domain logic here
      
      setState('SUCCESS');
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Unknown error');
      setState('ERROR');
      // STRICT TELEMETRY REQUIREMENT: Never use console.warn/console.error here
      AppLogger.error('[<hookName>] execution failed', e);
    }
  }, []);

  return { state, error, execute };
}
```

4. **Update the Hook Registry**: Open `tools/SK8Lytz_App_Master_Reference.md` and find the §4 Hook & Service Registry table. Add a new row for the scaffolded hook:
   - Name: `<hookName>`
   - File: `src/hooks/<hookName>.ts`
   - Domain: (fill in based on hook purpose)
   - Status: `🟡 SCAFFOLDED — TODO: implement`
   This satisfies Kanban Constitution Rule 12 (Documentation Parity Gate).

5. **Confirm** in chat: "Scaffolded `src/hooks/<hookName>.ts` with FSM state, VIP AppLogger telemetry, and typed result. Hook Registry updated in Master Reference. Fill in the TODOs."

6. **Do NOT** create a branch, commit, or start implementing logic. This is a scaffolding-only workflow.
