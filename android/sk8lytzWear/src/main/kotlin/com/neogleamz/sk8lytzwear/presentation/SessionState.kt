package com.neogleamz.sk8lytzwear.presentation

/**
 * SessionState — Finite State Machine for the watch dashboard.
 *
 * IDLE:    No active session. Show "Start" button.
 * ACTIVE:  Session running. Show live speed/HR/calories.
 *
 * Driven by DataClient state pushes from the phone app
 * OR by user tapping Start/Stop on the watch itself.
 */
enum class SessionState {
    /** No session — default state. Shows idle UI with Start button. */
    IDLE,

    /** Active skating session — shows live telemetry chips. */
    ACTIVE
}
