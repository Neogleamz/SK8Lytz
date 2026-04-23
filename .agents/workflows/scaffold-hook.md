---
description: Scaffold a new domain hook with boilerplate FSM state, typed params, error handling, and JSDoc
---

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

3. **Confirm** in chat: "Scaffolded `src/hooks/<hookName>.ts` with FSM state, VIP AppLogger telemetry, and typed result. Fill in the TODOs."

4. **Do NOT** create a branch, commit, or start implementing logic. This is a scaffolding-only workflow.
