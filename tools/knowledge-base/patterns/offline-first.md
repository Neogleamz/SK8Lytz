# Offline-First Mandate

The SK8Lytz application enforces a strict "Hardware First, Cloud Second" policy. Core hardware control and BLE opcodes are NEVER gated behind an authentication wall or internet connection.

## Deliberate Exceptions

- **Crewz Mode**: The Crewz Hub and live group sessions are explicitly excluded from the Offline-First Mandate. Crewz Mode relies entirely on Cloud pub/sub (Supabase Realtime) for synchronization. It is an Online-Only feature and requires an active internet connection, Location permissions, and Data Sharing permissions to function.
