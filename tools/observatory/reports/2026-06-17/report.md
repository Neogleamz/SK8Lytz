# Self-Healing Triage Report (2026-06-17)

## 🚨 Critical Regressions
### [ERR-WEB-001] Web Console Crash: getEnforcing undefined
- **Urgency:** 90
- **Occurrences:** 2 (Local & Build Quality Gate)
- **Description:** `TypeError: Cannot read properties of undefined (reading 'getEnforcing')`
- **Analysis:** This indicates a missing web mock for a TurboModule (likely introduced in the recent Expo 56 upgrade). Blocks web demo functionality.
- **Proposed Kanban Task:**
  - **Tags:** `[📝 NEEDS PLAN]` `[UI/WEB]` `[⚠️ H-RISK]` `[🍱 Meal]`
  - **Goal:** Fix `getEnforcing` TypeError on Web by stubbing the responsible NativeModule.

## 🟠 High Priority Errors
### [ERR-DB-003] Schema Drift: label_designs.product_name missing
- **Urgency:** 85
- **Occurrences:** 2
- **Description:** `column label_designs.product_name does not exist`
- **Proposed Kanban Task:**
  - **Tags:** `[📝 NEEDS PLAN]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍩 Snack]`
  - **Goal:** Apply missing database migration to add `product_name` to `label_designs` table.

### [ERR-DB-001] Telemetry Constraint Violation
- **Urgency:** 80
- **Occurrences:** 7
- **Description:** `new row for relation "crash_telemetry" violates check constraint "crash_telemetry_severity_check"`
- **Proposed Kanban Task:**
  - **Tags:** `[📝 NEEDS PLAN]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍩 Snack]`
  - **Goal:** Align app `crash_telemetry` payload with Supabase DB check constraint values.

### [ERR-DB-002] Telemetry Data Type Range Error
- **Urgency:** 75
- **Occurrences:** 1
- **Description:** `value "46733" is out of range for type smallint`
- **Proposed Kanban Task:**
  - **Tags:** `[📝 NEEDS PLAN]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍩 Snack]`
  - **Goal:** Alter telemetry table to use `integer` instead of `smallint` for larger payloads.
