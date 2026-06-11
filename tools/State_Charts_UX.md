# SK8Lytz UX State Charts

> **Audience:** UX Designers, QA, and Product Support.
> **Purpose:** To define exactly what the UI should look like at every phase of a complex background operation. This ensures designers account for every error state and loading spinner.

## State Chart 1: The Hardware Connection Lifecycle

Bluetooth is invisible, which makes it frustrating for users when it fails. This chart maps the UI states the user sees while the app tries to talk to the physical skates.

```mermaid
stateDiagram-v2
    [*] --> Disconnected

    state Disconnected {
        direction LR
        UI: Gray Icon
        Text: "Tap to Connect"
    }

    Disconnected --> Scanning : User Taps Connect

    state Scanning {
        direction LR
        UI: Pulsing Blue Icon
        Text: "Looking for skates..."
    }

    Scanning --> Connecting : Skates Found

    state Connecting {
        direction LR
        UI: Spinning Loading Wheel
        Text: "Pairing..."
    }

    Connecting --> Ready : Success!

    state Ready {
        direction LR
        UI: Solid Green Icon
        Text: "Connected (100% Battery)"
    }

    %% Error Paths
    Scanning --> NotFoundError : Timeout (10s)
    Connecting --> AuthError : Bad PIN
    Connecting --> GATTError : Hardware Lockout

    NotFoundError --> Disconnected : "Retry"
    AuthError --> Disconnected : "Retry"
    GATTError --> Disconnected : "Retry"

    %% Organic Disconnects
    Ready --> AutoRecovery : Walked out of range

    state AutoRecovery {
        direction LR
        UI: Yellow Warning Icon
        Text: "Reconnecting..."
    }

    AutoRecovery --> Ready : Walked back in range
    AutoRecovery --> Disconnected : Failed after 3 mins
```

### UX Mandates for Designers:
1. **Never Trap the User:** The `Connecting` and `Scanning` states must always have a manual "Cancel" button. Hardware can freeze; the app shouldn't.
2. **The "Auto-Recovery" State:** When a skater rides too far from their phone, the app shouldn't immediately throw a red error. It enters `AutoRecovery` (Yellow) and silently tries to fix it in the background so the user doesn't have to pull out their phone.

---

## State Chart 2: Data Matrix (Loading/Empty/Error/Success)

For every screen that fetches data from the Cloud (like the Community Patterns list or the Crew Leaderboard), the UI must explicitly design for the "4-State Matrix".

```mermaid
stateDiagram-v2
    [*] --> LoadingState : Screen Opens

    state LoadingState {
        UI: Shimmer Skeletons
        Rule: Show immediately, no blank white screens.
    }

    LoadingState --> SuccessState : Data Found
    LoadingState --> EmptyState : Zero Results
    LoadingState --> ErrorState : Network Failed

    state SuccessState {
        UI: The actual list/grid
    }

    state EmptyState {
        UI: Friendly illustration
        CallToAction: "Create your first pattern!"
    }

    state ErrorState {
        UI: Sad cloud icon
        CallToAction: "Tap to Retry"
        Rule: Do NOT show raw JSON error text.
    }
```
