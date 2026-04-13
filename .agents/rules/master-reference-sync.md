---
trigger: always_on
---

# Corporate Memory & Test Sync Rule

Whenever you solve a complex bug, establish a new architectural pattern, modify the Supabase database schema, or reverse-engineer hardware payloads, you MUST document these findings in the **Master Reference** and the **Test Plan** to build long-term corporate memory and ensure regression testing is updated.

1. **Primary Reference**: `tools/SK8Lytz_App_Master_Reference.md` (Hardware protocols, DB schemas, architecture, core hooks).
2. **Test Plan**: `tools/SK8Lytz_TEST_PLAN.md` (Manual & Automated verification steps).
3. **Assets Reference**: `tools/SK8Lytz_Image_Cross_Reference.txt` (Image and string asset mappings).

### Important: Synchronized Maintenance

To prevent documentation and testing from falling out of sync:

- **Search Before Write**: NEVER blindly append to the end of the document. You must search the document first to see if the topic (e.g. `0x51 Payload` or `Account Flow`) already exists.
- **Update, Don't Duplicate**: If the topic exists, securely *edit* that exact section rather than creating a secondary entry.
- **The "QA Evolution" Mandate**: If you implement a new UI feature (e.g., a new tab, a new modal, or a new auth provider), you MUST autonomously add a corresponding test scenario to the `tools/SK8Lytz_TEST_PLAN.md` file. Use the same table format found in the existing sections.
- **Strict Headers**: Place your findings strictly under the appropriate structured Markdown heading.
- **Prune Old Assumptions**: If your new discovery proves an old note or test case wrong, delete or update it immediately.
