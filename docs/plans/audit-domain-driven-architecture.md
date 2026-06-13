# [PLAN] audit/domain-driven-architecture (The Digital Soul Audit)

### Design Decisions & Rationale

To ensure long-term scalability and eliminate "Spaghetti State," we are planning a **Domain-Driven Design (DDD)** audit. This is not a "code-change" plan but a "Structural Blueprint" plan to evaluate how we can decouple the Hardware, Community, Identity, and Session logic into isolated, testable containers.

## Proposed Strategy

### Phase 1: Dependency Mapping

- Analyze the "Big Three" hooks (`useRegistration`, `useCrewHub`, `useBluetooth`) to identify where cross-domain bleed is occurring.
- Map out the "Hidden Contracts" between the UI and the Services.

### Phase 2: Interface Design

- Draft "Domain Interfaces" (e.g., `IHarewareController`, `ISessionTracker`) that allow the UI to interact with features without knowing the underlying implementation (BLE vs. Supabase).

### Phase 3: Extraction Roadmap

- Identify candidate logic for the first "Micro-App" extraction (e.g., extracting all Session Telemetry from the Dashboard into its own domain).

## Verification Plan

1. **Dependency Graph**: Generate a visual map of current project imports to identify circular dependencies.
2. **Mockability Test**: Verify if a core service can be "Mocked" today without breaking 5 other files; identify the gaps.
